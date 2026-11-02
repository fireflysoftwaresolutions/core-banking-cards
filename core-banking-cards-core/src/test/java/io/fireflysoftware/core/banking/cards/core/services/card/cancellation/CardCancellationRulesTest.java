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

import static io.fireflysoftware.core.banking.cards.interfaces.dtos.card.cancellation.CardCancellationContracts.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class CardCancellationRulesTest {
    static final UUID ACTION = UUID.fromString("11111111-1111-1111-1111-111111111111");
    static final UUID CARD = UUID.fromString("22222222-2222-2222-2222-222222222222");
    static final UUID TENANT = UUID.fromString("33333333-3333-3333-3333-333333333333");
    static final UUID ACTOR = UUID.fromString("44444444-4444-4444-4444-444444444444");
    static final UUID PARTY = UUID.fromString("55555555-5555-5555-5555-555555555555");
    static final UUID ACCOUNT = UUID.fromString("66666666-6666-6666-6666-666666666666");
    static final Selection SELECTED = new Selection(TENANT, ACTOR, PARTY, ACCOUNT, CARD, 7L);
    static final Request REQUEST = new Request(ACTION, 7L, "CUSTOMER_REQUEST", 2000L);

    @Test
    void normalAdmissionUsesExactCurrentBindingAndFixedDeadline() {
        assertDoesNotThrow(() -> CardCancellationRules.admit(REQUEST, 7, 1999));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.admit(REQUEST, 8, 1999));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.admit(REQUEST, 7, 2000));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.admit(REQUEST, 7, 2001));
    }

    @Test
    void invalidTypedRequestsCannotBypassTheStrictCodec() {
        for (Request request : new Request[]{null, new Request(null, 7L, "A", 1L),
                new Request(new UUID(0, 0), 7L, "A", 1L), new Request(ACTION, null, "A", 1L),
                new Request(ACTION, 0L, "A", 1L), new Request(ACTION, 7L, null, 1L),
                new Request(ACTION, 7L, "a", 1L), new Request(ACTION, 7L, "A\n", 1L),
                new Request(ACTION, 7L, "A".repeat(65), 1L), new Request(ACTION, 7L, "A", null),
                new Request(ACTION, 7L, "A", 0L)}) {
            assertThrows(IllegalArgumentException.class, () -> CardCancellationRules.validate(request));
        }
    }

    @Test
    void exactHistoricalReplayDoesNotReadmitOrExtendTheDeadline() {
        assertDoesNotThrow(() -> CardCancellationRules.requireReplay(SELECTED, REQUEST, SELECTED,
                new Request(ACTION, 7L, "CUSTOMER_REQUEST", 2000L)));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.requireReplay(
                SELECTED, REQUEST, SELECTED, new Request(ACTION, 7L, "CUSTOMER_REQUEST", 3000L)));
    }

    @Test
    void actionScopeReasonAndBindingChangesConflictWithRetainedIntent() {
        for (Request changed : new Request[]{new Request(CARD, 7L, "CUSTOMER_REQUEST", 2000L),
                new Request(ACTION, 8L, "CUSTOMER_REQUEST", 2000L), new Request(ACTION, 7L, "OTHER", 2000L)}) {
            assertThrows(IllegalStateException.class, () -> CardCancellationRules.requireReplay(SELECTED, REQUEST, SELECTED, changed));
        }
        for (Selection changed : new Selection[]{new Selection(CARD, ACTOR, PARTY, ACCOUNT, CARD, 7L),
                new Selection(TENANT, CARD, PARTY, ACCOUNT, CARD, 7L), new Selection(TENANT, ACTOR, CARD, ACCOUNT, CARD, 7L),
                new Selection(TENANT, ACTOR, PARTY, CARD, CARD, 7L), new Selection(TENANT, ACTOR, PARTY, ACCOUNT, ACTION, 7L),
                new Selection(TENANT, ACTOR, PARTY, ACCOUNT, CARD, 8L)}) {
            assertThrows(IllegalStateException.class, () -> CardCancellationRules.requireReplay(SELECTED, REQUEST, changed, REQUEST));
        }
    }

    @Test
    void canonicalDigestBindsEveryRetainedFieldWithoutDependingOnJsonOrder() {
        String digest = CardCancellationRules.digest(SELECTED, REQUEST);
        assertEquals("34de2a78929cb9dd964b7c76582f8b75f774a7cc88cd64e37b0790412d0066c6", digest);
        assertTrue(digest.matches("[0-9a-f]{64}"));
        assertEquals(digest, CardCancellationRules.digest(SELECTED,
                new Request(ACTION, 7L, "CUSTOMER_REQUEST", 2000L)));
        assertNotEquals(digest, CardCancellationRules.digest(SELECTED,
                new Request(ACTION, 7L, "OTHER", 2000L)));
        assertNotEquals(digest, CardCancellationRules.digest(SELECTED,
                new Request(ACTION, 7L, "CUSTOMER_REQUEST", 3000L)));
    }

    @Test
    void malformedServerSelectionAndConflictingBindingCannotBeDigested() {
        assertThrows(IllegalArgumentException.class, () -> CardCancellationRules.digest(null, REQUEST));
        assertThrows(IllegalArgumentException.class, () -> CardCancellationRules.digest(
                new Selection(null, ACTOR, PARTY, ACCOUNT, CARD, 7L), REQUEST));
        assertThrows(IllegalArgumentException.class, () -> CardCancellationRules.digest(
                new Selection(TENANT, ACTOR, PARTY, ACCOUNT, CARD, 8L), REQUEST));
    }

    @Test
    void uncertainAndRejectedProviderProgressAlwaysRetainsTheLocalRestriction() {
        for (State state : State.values()) {
            assertTrue(CardCancellationRules.restrictionRetained(state));
        }
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.PROVIDER_PENDING, State.RECONCILIATION_REQUIRED));
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.RECONCILIATION_REQUIRED, State.PROVIDER_PENDING));
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.PROVIDER_PENDING, State.REJECTED));
    }

    @Test
    void providerConfirmationCannotRegressOrSkipTheRetainedRestriction() {
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.REQUESTED, State.LOCAL_RESTRICTION_RETAINED));
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.LOCAL_RESTRICTION_RETAINED, State.PROVIDER_PENDING));
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.PROVIDER_PENDING, State.PROVIDER_CONFIRMED));
        assertDoesNotThrow(() -> CardCancellationRules.transition(State.PROVIDER_CONFIRMED, State.PROVIDER_CONFIRMED));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.transition(State.REQUESTED, State.PROVIDER_CONFIRMED));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.transition(State.PROVIDER_CONFIRMED, State.PROVIDER_PENDING));
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.transition(State.REJECTED, State.REQUESTED));
    }

    @Test
    void retainedAcceptanceBindsTheExactIntentAndObservedDatabaseAdmission() {
        UUID receiptId = UUID.fromString("77777777-7777-7777-7777-777777777777");
        var acceptance = new Acceptance(ACTION, SELECTED,
                "34de2a78929cb9dd964b7c76582f8b75f774a7cc88cd64e37b0790412d0066c6", receiptId, 1500);
        assertDoesNotThrow(() -> CardCancellationRules.requireAcceptance(acceptance, SELECTED, REQUEST, receiptId, 1500));
        var initial = CardCancellationRules.initialProgress(acceptance, SELECTED, REQUEST, receiptId, 1500);
        assertEquals(ACTION, initial.actionId());
        assertEquals(CARD, initial.cardId());
        assertEquals(0, initial.version());
        assertEquals(State.REQUESTED, initial.state());
        assertEquals(SpendRestriction.RETAINED, initial.spendRestriction());
        var later = new Progress(ACTION, CARD, 1, State.PROVIDER_PENDING, SpendRestriction.RETAINED);
        assertEquals(State.PROVIDER_PENDING, later.state());
        assertEquals(1500, acceptance.databaseRecordedAtEpochMillis());
        assertEquals(receiptId, acceptance.receiptId());
    }

    @Test
    void normalAcceptanceFieldMismatchesCannotBeAttachedToRetainedScope() {
        UUID receiptId = UUID.fromString("77777777-7777-7777-7777-777777777777");
        String digest = "34de2a78929cb9dd964b7c76582f8b75f774a7cc88cd64e37b0790412d0066c6";
        for (Selection changed : new Selection[]{new Selection(CARD, ACTOR, PARTY, ACCOUNT, CARD, 7L),
                new Selection(TENANT, CARD, PARTY, ACCOUNT, CARD, 7L), new Selection(TENANT, ACTOR, CARD, ACCOUNT, CARD, 7L),
                new Selection(TENANT, ACTOR, PARTY, CARD, CARD, 7L), new Selection(TENANT, ACTOR, PARTY, ACCOUNT, ACTION, 7L),
                new Selection(TENANT, ACTOR, PARTY, ACCOUNT, CARD, 8L)}) {
            var changedReceipt = new Acceptance(ACTION, changed, digest, receiptId, 1500);
            assertThrows(IllegalStateException.class, () -> CardCancellationRules.requireAcceptance(
                    changedReceipt, SELECTED, REQUEST, receiptId, 1500));
        }
        for (Acceptance changed : new Acceptance[]{new Acceptance(CARD, SELECTED, digest, receiptId, 1500),
                new Acceptance(ACTION, SELECTED, "b".repeat(64), receiptId, 1500),
                new Acceptance(ACTION, SELECTED, digest, CARD, 1500), new Acceptance(ACTION, SELECTED, digest, receiptId, 1501)}) {
            assertThrows(IllegalStateException.class, () -> CardCancellationRules.requireAcceptance(
                    changed, SELECTED, REQUEST, receiptId, 1500));
        }
    }

    @Test
    void missingOrInvalidAcceptanceEvidenceCannotProduceInitialProgress() {
        UUID receiptId = UUID.fromString("77777777-7777-7777-7777-777777777777");
        String digest = "34de2a78929cb9dd964b7c76582f8b75f774a7cc88cd64e37b0790412d0066c6";
        for (Acceptance invalid : new Acceptance[]{null, new Acceptance(null, SELECTED, digest, receiptId, 1500),
                new Acceptance(ACTION, null, digest, receiptId, 1500), new Acceptance(ACTION, SELECTED, null, receiptId, 1500),
                new Acceptance(ACTION, SELECTED, "not-a-digest", receiptId, 1500),
                new Acceptance(ACTION, SELECTED, digest, null, 1500), new Acceptance(ACTION, SELECTED, digest, new UUID(0, 0), 1500),
                new Acceptance(ACTION, SELECTED, digest, receiptId, 0)}) {
            assertThrows(IllegalArgumentException.class, () -> CardCancellationRules.initialProgress(
                    invalid, SELECTED, REQUEST, receiptId, 1500));
        }
        var acceptance = new Acceptance(ACTION, SELECTED, digest, receiptId, 2000);
        assertThrows(IllegalStateException.class, () -> CardCancellationRules.requireAcceptance(
                acceptance, SELECTED, REQUEST, receiptId, 2000));
    }

    @Test
    void everyPublicProgressStateIncludingRequestedMustRetainSpendRestriction() {
        for (State state : State.values()) {
            var progress = new Progress(ACTION, CARD, 0, state, SpendRestriction.RETAINED);
            assertEquals(SpendRestriction.RETAINED, progress.spendRestriction());
            assertTrue(CardCancellationRules.restrictionRetained(progress.state()));
            assertThrows(IllegalArgumentException.class, () -> new Progress(ACTION, CARD, 0, state, null));
        }
        assertThrows(IllegalArgumentException.class, () -> CardCancellationRules.restrictionRetained(null));
        assertThrows(IllegalArgumentException.class, () -> new Progress(ACTION, CARD, 0, null, SpendRestriction.RETAINED));
        assertThrows(IllegalArgumentException.class, () -> new Progress(ACTION, CARD, -1, State.REQUESTED, SpendRestriction.RETAINED));
    }
}
