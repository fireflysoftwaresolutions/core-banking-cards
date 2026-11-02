/*
 * Copyright 2026 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fireflysoftware.core.banking.cards.core.services.card.enrollment;

import static io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.*;
import static io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.*;
import static io.fireflysoftware.core.banking.cards.core.services.card.enrollment.ManagedCardEnrollmentRulesTest.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ManagedCardProviderRulesTest {
    private Operation operation() { return new Operation(OTHER, ID, SCOPE, ManagedCardEnrollmentRules.digest(SCOPE, REQUEST), 2000L); }
    private Observation observation(Role role, Outcome outcome, UseState use) {
        return new Observation(OTHER, ID, SCOPE, ManagedCardEnrollmentRules.digest(SCOPE, REQUEST), role,
                role == Role.ISSUER ? "issuer-test" : "processor-test", outcome, use,
                "issuer-ref", role == Role.ISSUER ? "issuer-ref" : "processor-ref", "provider-receipt", 1L, 1600L);
    }
    private Map<Role, Observation> issued() {
        var map = new EnumMap<Role, Observation>(Role.class);
        for (Role role : Role.values()) map.put(role, observation(role, Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED));
        return map;
    }
    private State resolve(Map<Role, Observation> facts) { return ManagedCardProviderRules.resolve(operation(), SCOPE, REQUEST, facts, 1700); }

    @Test void bothExactProviderReceiptsCanOnlyEnrollAnInactiveRestrictedInstrument() {
        assertEquals(State.ENROLLED_INACTIVE, resolve(issued()));
    }
    @Test void onePendingProviderCannotBecomeEnrolledOrClear() {
        var facts = issued(); facts.put(Role.PROCESSOR, observation(Role.PROCESSOR, Outcome.PENDING, UseState.UNKNOWN));
        assertEquals(State.PROVIDER_PENDING, resolve(facts));
    }
    @Test void missingUnknownOrUnreceiptedOutcomesNeedObservationAndReconciliation() {
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(Map.of()));
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(null));
        var facts = issued(); facts.put(Role.ISSUER, observation(Role.ISSUER, Outcome.UNKNOWN, UseState.UNKNOWN));
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
        facts.put(Role.ISSUER, new Observation(OTHER, ID, SCOPE, operation().intentDigest(), Role.ISSUER,
                "issuer-test", Outcome.ABSENT_CLOSED, UseState.UNKNOWN, null, null, null, 1L, 1600L));
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
    }
    @Test void activeProviderOutputIsNeverConvertedToInactiveEnrollment() {
        for (UseState use : new UseState[]{UseState.ACTIVE, UseState.UNKNOWN}) {
            var facts = issued(); facts.put(Role.ISSUER, observation(Role.ISSUER, Outcome.ISSUED_INACTIVE, use));
            assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
        }
    }
    @Test void changedProviderIdentityScopeActionOrOperationIsNotConfirmation() {
        for (Observation bad : new Observation[]{new Observation(ID, ID, SCOPE, operation().intentDigest(), Role.ISSUER, "issuer-test", Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED, "issuer-ref", "issuer-ref", "receipt", 1L, 1600L),
                new Observation(OTHER, OTHER, SCOPE, operation().intentDigest(), Role.ISSUER, "issuer-test", Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED, "issuer-ref", "issuer-ref", "receipt", 1L, 1600L),
                new Observation(OTHER, ID, SCOPE, operation().intentDigest(), Role.ISSUER, "wrong-adapter", Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED, "issuer-ref", "issuer-ref", "receipt", 1L, 1600L),
                new Observation(OTHER, ID, SCOPE, "a".repeat(64), Role.ISSUER, "issuer-test", Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED, "issuer-ref", "issuer-ref", "receipt", 1L, 1600L)}) {
            var facts = issued(); facts.put(Role.ISSUER, bad); assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
        }
    }
    @Test void aRetainedIssueAttemptIsObservedBeforeAnyFurtherEffect() {
        assertEquals(NextStep.ISSUE, ManagedCardProviderRules.nextStep(State.RESERVED));
        assertEquals(State.PROVIDER_PENDING, ManagedCardProviderRules.beginIssue(State.RESERVED));
        for (State state : new State[]{State.PROVIDER_PENDING, State.RECONCILIATION_REQUIRED}) {
            assertEquals(NextStep.OBSERVE, ManagedCardProviderRules.nextStep(state));
            assertThrows(IllegalStateException.class, () -> ManagedCardProviderRules.beginIssue(state));
        }
        assertEquals(NextStep.NONE, ManagedCardProviderRules.nextStep(State.ENROLLED_INACTIVE));
    }
    @Test void definitiveAbsenceNeedsBothExactReceiptsAndDoesNotAuthorizeReissue() {
        var facts = issued();
        for (Role role : Role.values()) facts.put(role, observation(role, Outcome.ABSENT_CLOSED, UseState.UNKNOWN));
        assertEquals(State.REJECTED, resolve(facts));
        assertEquals(NextStep.NONE, ManagedCardProviderRules.nextStep(State.REJECTED));
        facts.put(Role.ISSUER, observation(Role.ISSUER, Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED));
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
    }
    @Test void recoveryBindsOriginalActionAndVersionWithoutRenewingOperationDeadline() {
        assertDoesNotThrow(() -> ManagedCardProviderRules.requireRecovery(new RecoveryRequest(ID, 1L), operation(), SCOPE, REQUEST, 1));
        assertThrows(IllegalStateException.class, () -> ManagedCardProviderRules.requireRecovery(new RecoveryRequest(OTHER, 1L), operation(), SCOPE, REQUEST, 1));
        assertThrows(IllegalStateException.class, () -> ManagedCardProviderRules.requireRecovery(new RecoveryRequest(ID, 0L), operation(), SCOPE, REQUEST, 1));
        assertThrows(IllegalArgumentException.class, () -> ManagedCardProviderRules.requireOperation(new Operation(OTHER, ID, SCOPE, operation().intentDigest(), 3000L), SCOPE, REQUEST));
        assertThrows(IllegalArgumentException.class, () -> ManagedCardProviderRules.requireRecovery(new RecoveryRequest(ID, 1L),
                new Operation(OTHER, ID, SCOPE, operation().intentDigest(), 3000L), SCOPE, REQUEST, 1));
    }

    @Test void unrelatedIssuerAndProcessorInstrumentsCannotShareAnEnrollmentReceipt() {
        var facts = issued();
        facts.put(Role.PROCESSOR, new Observation(OTHER, ID, SCOPE, operation().intentDigest(), Role.PROCESSOR,
                "processor-test", Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED,
                "other-issuer", "processor-ref", "receipt", 1L, 1600L));
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
        facts = issued();
        facts.put(Role.ISSUER, new Observation(OTHER, ID, SCOPE, operation().intentDigest(), Role.ISSUER,
                "issuer-test", Outcome.ISSUED_INACTIVE, UseState.INACTIVE_RESTRICTED,
                "issuer-ref", "other-issuer", "receipt", 1L, 1600L));
        assertEquals(State.RECONCILIATION_REQUIRED, resolve(facts));
    }
}
