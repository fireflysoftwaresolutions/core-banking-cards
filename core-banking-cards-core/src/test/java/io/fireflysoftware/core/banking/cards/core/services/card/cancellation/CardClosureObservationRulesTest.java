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

package io.fireflysoftware.core.banking.cards.core.services.card.cancellation;

import static io.fireflysoftware.core.banking.cards.interfaces.dtos.card.cancellation.CardClosureObservationContracts.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CardClosureObservationRulesTest {
    static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    static final UUID OTHER = UUID.fromString("22222222-2222-2222-2222-222222222222");
    static final Scope SCOPE = new Scope(ID, ID, ID, ID, 3L, 4L, "a".repeat(64));
    static final FenceSelection SELECTED = new FenceSelection(ID, ID, ID, ID);
    static final FenceRequest REQUEST = new FenceRequest(ID, ID, ID, 0L, 2000L);

    private Map<Capability, Observation> complete(Status status) {
        var result = new EnumMap<Capability, Observation>(Capability.class);
        for (Capability capability : Capability.values()) result.put(capability, new Observation(
                ID, SCOPE, capability, 5L, true, status, 0L, 0L));
        return result;
    }

    @Test
    void completeMatchingFencedEvidenceCanEstablishClearanceForThisOwnerOnly() {
        assertEquals(Status.CLEAR, CardClosureObservationRules.coverage(SCOPE, complete(Status.CLEAR)));
        assertEquals(Status.CLEAR, CardClosureObservationRules.coverage(SCOPE, complete(Status.NOT_APPLICABLE)));
    }

    @Test
    void everyRequiredCapabilityMustBeCoveredIncludingFinancialTokensAndLateItems() {
        for (Capability missing : Capability.values()) {
            var observations = complete(Status.CLEAR); observations.remove(missing);
            assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, observations));
        }
        assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, Map.of()));
        assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, null));
    }

    @Test
    void pendingOrUncertainEvidenceCannotBeOverriddenByOtherClearCapabilities() {
        var observations = complete(Status.CLEAR);
        observations.put(Capability.AUTHORIZATIONS, new Observation(ID, SCOPE, Capability.AUTHORIZATIONS, 5L, true, Status.OBLIGATIONS, 1L, 0L));
        assertEquals(Status.OBLIGATIONS, CardClosureObservationRules.coverage(SCOPE, observations));
        observations.put(Capability.LATE_ITEM_DISPOSITION, new Observation(ID, SCOPE, Capability.LATE_ITEM_DISPOSITION, 5L, true, Status.UNKNOWN, 0L, 1L));
        assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, observations));
    }

    @Test
    void aClearFlagCannotContradictPendingUnknownOrIncompleteEvidence() {
        for (Observation bad : new Observation[]{new Observation(ID, SCOPE, Capability.TOKEN_WALLET, 5L, false, Status.CLEAR, 0L, 0L),
                new Observation(ID, SCOPE, Capability.TOKEN_WALLET, 5L, true, Status.CLEAR, 1L, 0L),
                new Observation(ID, SCOPE, Capability.TOKEN_WALLET, 5L, true, Status.NOT_APPLICABLE, 0L, 1L),
                new Observation(ID, SCOPE, Capability.TOKEN_WALLET, -1L, true, Status.CLEAR, 0L, 0L),
                new Observation(null, SCOPE, Capability.TOKEN_WALLET, 5L, true, Status.CLEAR, 0L, 0L)}) {
            var observations = complete(Status.CLEAR);observations.put(Capability.TOKEN_WALLET, bad);
            assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, observations));
        }
    }

    @Test
    void closureTenantAccountEnrollmentFenceAndRelationshipSelectionMustMatch() {
        for (Scope changed : new Scope[]{new Scope(OTHER, ID, ID, ID, 3L, 4L, "a".repeat(64)),
                new Scope(ID, OTHER, ID, ID, 3L, 4L, "a".repeat(64)), new Scope(ID, ID, OTHER, ID, 3L, 4L, "a".repeat(64)),
                new Scope(ID, ID, ID, OTHER, 3L, 4L, "a".repeat(64)), new Scope(ID, ID, ID, ID, 4L, 4L, "a".repeat(64)),
                new Scope(ID, ID, ID, ID, 3L, 5L, "a".repeat(64)), new Scope(ID, ID, ID, ID, 3L, 4L, "b".repeat(64))}) {
            var observations = complete(Status.CLEAR);
            observations.put(Capability.CLEARING_SETTLEMENT, new Observation(ID, changed, Capability.CLEARING_SETTLEMENT, 5L, true, Status.CLEAR, 0L, 0L));
            assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, observations));
        }
    }

    @Test
    void malformedSelectionAndMislabeledCapabilityNeverEstablishClearance() {
        assertThrows(IllegalArgumentException.class, () -> CardClosureObservationRules.coverage(null, complete(Status.CLEAR)));
        assertThrows(IllegalArgumentException.class, () -> CardClosureObservationRules.coverage(new Scope(ID, ID, ID, ID, 3L, 0L, "a".repeat(64)), complete(Status.CLEAR)));
        var observations = complete(Status.CLEAR);
        observations.put(Capability.AUTHORIZATIONS, observations.get(Capability.REFUNDS_RETURNS));
        assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, observations));
    }

    @Test
    void fenceAdmissionIsVersionedAndItsDeadlineCannotBeRenewedByReplay() {
        assertDoesNotThrow(() -> CardClosureObservationRules.admit(REQUEST, 0, 1999));
        assertThrows(IllegalStateException.class, () -> CardClosureObservationRules.admit(REQUEST, 1, 1999));
        assertThrows(IllegalStateException.class, () -> CardClosureObservationRules.admit(REQUEST, 0, 2000));
        assertDoesNotThrow(() -> CardClosureObservationRules.requireReplay(SELECTED, REQUEST, SELECTED, REQUEST));
        assertThrows(IllegalStateException.class, () -> CardClosureObservationRules.requireReplay(SELECTED, REQUEST, SELECTED, new FenceRequest(ID, ID, ID, 0L, 3000L)));
        for (FenceSelection changed : new FenceSelection[]{new FenceSelection(OTHER, ID, ID, ID),
                new FenceSelection(ID, OTHER, ID, ID), new FenceSelection(ID, ID, OTHER, ID)}) {
            assertThrows(IllegalStateException.class, () -> CardClosureObservationRules.requireReplay(SELECTED, REQUEST, changed, REQUEST));
        }
        assertNotNull(CardClosureObservationRules.digest(SELECTED, REQUEST));
        assertNotEquals(CardClosureObservationRules.digest(SELECTED, REQUEST),
                CardClosureObservationRules.digest(new FenceSelection(OTHER, ID, ID, ID), REQUEST));
        assertThrows(IllegalArgumentException.class, () -> CardClosureObservationRules.digest(
                new FenceSelection(ID, ID, ID, OTHER), REQUEST));
    }

    @Test
    void providerConfirmationAloneSuppliesNoFinancialOrLateItemCoverage() {
        assertTrue(CardCancellationRules.restrictionRetained(io.fireflysoftware.core.banking.cards.interfaces.dtos.card.cancellation.CardCancellationContracts.State.PROVIDER_CONFIRMED));
        assertEquals(Status.UNKNOWN, CardClosureObservationRules.coverage(SCOPE, Map.of()));
    }
}
