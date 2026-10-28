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
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ManagedCardEnrollmentRulesTest {
    static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    static final UUID OTHER = UUID.fromString("22222222-2222-2222-2222-222222222222");
    static final ManagedCardId CARD = new ManagedCardId(UUID.fromString("33333333-3333-3333-3333-333333333333"));
    static final Selection SCOPE = new Selection(ID, ID, ID, ID, ID, ID, CARD, ID, 2L, 3L,
            new ConfiguredProviders("issuer-test", "processor-test"));
    static final Request REQUEST = new Request(ID, 2L, "CUSTOMER_REQUEST", 2000L);

    static Map<Owner, EligibilityReceipt> eligible() {
        var result = new EnumMap<Owner, EligibilityReceipt>(Owner.class);
        for (Owner owner : Owner.values()) result.put(owner, receipt(owner, Eligibility.ELIGIBLE));
        return result;
    }

    static EligibilityReceipt receipt(Owner owner, Eligibility status) {
        return new EligibilityReceipt(ID, owner, ID, ID, ID, ID, ID, owner == Owner.ACCOUNT ? 2L : 3L, 1900L, status);
    }

    @Test void admissionRequiresBothCurrentOwnersExactVersionAndFixedDeadline() {
        assertEquals(Eligibility.ELIGIBLE, ManagedCardEnrollmentRules.eligibility(SCOPE, eligible(), 1500));
        assertDoesNotThrow(() -> ManagedCardEnrollmentRules.admit(REQUEST, SCOPE, eligible(), 1500));
        assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.admit(REQUEST, SCOPE, eligible(), 2000));
        assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.admit(
                new Request(ID, 1L, "CUSTOMER_REQUEST", 2000L), SCOPE, eligible(), 1500));
    }

    @Test void missingUnknownExpiredOrMismatchedOwnerEvidenceRemainsUnknown() {
        assertEquals(Eligibility.UNKNOWN, ManagedCardEnrollmentRules.eligibility(SCOPE, Map.of(), 1500));
        assertEquals(Eligibility.UNKNOWN, ManagedCardEnrollmentRules.eligibility(SCOPE, null, 1500));
        for (EligibilityReceipt bad : new EligibilityReceipt[]{receipt(Owner.ACCOUNT, Eligibility.UNKNOWN),
                new EligibilityReceipt(ID, Owner.ACCOUNT, OTHER, ID, ID, ID, ID, 2L, 1900L, Eligibility.ELIGIBLE),
                new EligibilityReceipt(ID, Owner.ACCOUNT, ID, OTHER, ID, ID, ID, 2L, 1900L, Eligibility.ELIGIBLE),
                new EligibilityReceipt(ID, Owner.ACCOUNT, ID, ID, OTHER, ID, ID, 2L, 1900L, Eligibility.ELIGIBLE),
                new EligibilityReceipt(ID, Owner.ACCOUNT, ID, ID, ID, OTHER, ID, 2L, 1900L, Eligibility.ELIGIBLE),
                new EligibilityReceipt(ID, Owner.ACCOUNT, ID, ID, ID, ID, OTHER, 2L, 1900L, Eligibility.ELIGIBLE),
                receipt(Owner.PRODUCT, Eligibility.ELIGIBLE),
                new EligibilityReceipt(ID, Owner.ACCOUNT, ID, ID, ID, ID, ID, 1L, 1900L, Eligibility.ELIGIBLE),
                new EligibilityReceipt(ID, Owner.ACCOUNT, ID, ID, ID, ID, ID, 2L, 1500L, Eligibility.ELIGIBLE),
                new EligibilityReceipt(null, Owner.ACCOUNT, ID, ID, ID, ID, ID, 2L, 1900L, Eligibility.ELIGIBLE)}) {
            var facts = eligible(); facts.put(Owner.ACCOUNT, bad);
            assertEquals(Eligibility.UNKNOWN, ManagedCardEnrollmentRules.eligibility(SCOPE, facts, 1500));
            assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.admit(REQUEST, SCOPE, facts, 1500));
        }
    }

    @Test void knownIneligibilityNeverBecomesAdmissionAndUnknownStillTakesPrecedence() {
        var facts = eligible(); facts.put(Owner.ACCOUNT, receipt(Owner.ACCOUNT, Eligibility.INELIGIBLE));
        assertEquals(Eligibility.INELIGIBLE, ManagedCardEnrollmentRules.eligibility(SCOPE, facts, 1500));
        assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.admit(REQUEST, SCOPE, facts, 1500));
        facts.remove(Owner.PRODUCT);
        assertEquals(Eligibility.UNKNOWN, ManagedCardEnrollmentRules.eligibility(SCOPE, facts, 1500));
    }

    @Test void replayAndDigestBindManagedScopeProvidersReasonAndOriginalDeadline() {
        assertDoesNotThrow(() -> ManagedCardEnrollmentRules.requireReplay(SCOPE, REQUEST, SCOPE, REQUEST));
        String digest = ManagedCardEnrollmentRules.digest(SCOPE, REQUEST);
        assertEquals("b8ac78c5022728beacb65cbfc8570821d18b1bf0ca76ba078701e53dae6be9ab", digest);
        for (Request changed : new Request[]{new Request(OTHER, 2L, "CUSTOMER_REQUEST", 2000L),
                new Request(ID, 2L, "OTHER", 2000L), new Request(ID, 2L, "CUSTOMER_REQUEST", 3000L)}) {
            assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.requireReplay(SCOPE, REQUEST, SCOPE, changed));
            assertNotEquals(digest, ManagedCardEnrollmentRules.digest(SCOPE, changed));
        }
        for (Selection changed : new Selection[]{new Selection(ID, OTHER, ID, ID, ID, ID, CARD, ID, 2L, 3L, SCOPE.providers()),
                new Selection(ID, ID, ID, OTHER, ID, ID, CARD, ID, 2L, 3L, SCOPE.providers()),
                new Selection(ID, ID, ID, ID, OTHER, ID, CARD, ID, 2L, 3L, SCOPE.providers()),
                new Selection(ID, ID, ID, ID, ID, OTHER, CARD, ID, 2L, 3L, SCOPE.providers()),
                new Selection(ID, ID, ID, ID, ID, ID, new ManagedCardId(OTHER), ID, 2L, 3L, SCOPE.providers()),
                new Selection(ID, ID, ID, ID, ID, ID, CARD, OTHER, 2L, 3L, SCOPE.providers()),
                new Selection(ID, ID, ID, ID, ID, ID, CARD, ID, 2L, 4L, SCOPE.providers()),
                new Selection(ID, ID, ID, ID, ID, ID, CARD, ID, 2L, 3L, new ConfiguredProviders("other-issuer", "processor-test"))}) {
            assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.requireReplay(SCOPE, REQUEST, changed, REQUEST));
            assertNotEquals(digest, ManagedCardEnrollmentRules.digest(changed, REQUEST));
            assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.requireAcceptance(
                    new Acceptance(ID, changed, digest, OTHER, 1500), SCOPE, REQUEST, OTHER, 1500));
        }
    }

    @Test void noConfiguredIssuerOrProcessorCanBeInventedFromEligibility() {
        for (ConfiguredProviders config : new ConfiguredProviders[]{null,
                new ConfiguredProviders(null, "processor-test"), new ConfiguredProviders("issuer-test", "")}) {
            var missing = new Selection(ID, ID, ID, ID, ID, ID, CARD, ID, 2L, 3L, config);
            assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.admit(REQUEST, missing, eligible(), 1500));
        }
    }

    @Test void immutableAcceptanceRequiresFullRetainedIdentityDigestReceiptAndDatabaseTime() {
        String digest = ManagedCardEnrollmentRules.digest(SCOPE, REQUEST);
        var receipt = new Acceptance(ID, SCOPE, digest, OTHER, 1500);
        assertDoesNotThrow(() -> ManagedCardEnrollmentRules.requireAcceptance(receipt, SCOPE, REQUEST, OTHER, 1500));
        for (Acceptance changed : new Acceptance[]{new Acceptance(OTHER, SCOPE, digest, OTHER, 1500),
                new Acceptance(ID, SCOPE, "a".repeat(64), OTHER, 1500), new Acceptance(ID, SCOPE, digest, ID, 1500),
                new Acceptance(ID, SCOPE, digest, OTHER, 1501)}) {
            assertThrows(IllegalStateException.class, () -> ManagedCardEnrollmentRules.requireAcceptance(changed, SCOPE, REQUEST, OTHER, 1500));
        }
    }

    @Test void managedIdentityAndTypedRequestCannotUseMissingScopeOrCoercedDefaults() {
        assertThrows(IllegalArgumentException.class, () -> new ManagedCardId(null));
        assertThrows(IllegalArgumentException.class, () -> new ManagedCardId(new UUID(0, 0)));
        for (Request bad : new Request[]{null, new Request(null, 2L, "A", 1L), new Request(ID, null, "A", 1L),
                new Request(ID, -1L, "A", 1L), new Request(ID, 2L, "a", 1L), new Request(ID, 2L, "A\n", 1L),
                new Request(ID, 2L, "A", 0L)}) assertThrows(IllegalArgumentException.class, () -> ManagedCardEnrollmentRules.validate(bad));
    }

    @Test void everyEnrollmentProgressStateIsRestrictedAndHasNoUsableInstrumentOutput() {
        for (State state : State.values()) {
            var progress = new Progress(ID, CARD, 0, state, Restriction.RETAINED);
            assertEquals(Restriction.RETAINED, progress.restriction());
            assertThrows(IllegalArgumentException.class, () -> new Progress(ID, CARD, 0, state, null));
        }
        assertThrows(IllegalArgumentException.class, () -> new Progress(ID, CARD, -1, State.RESERVED, Restriction.RETAINED));
    }
}
