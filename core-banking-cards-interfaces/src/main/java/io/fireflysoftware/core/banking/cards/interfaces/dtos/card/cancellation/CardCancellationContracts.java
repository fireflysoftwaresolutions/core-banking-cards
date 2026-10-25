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

package io.fireflysoftware.core.banking.cards.interfaces.dtos.card.cancellation;

import java.util.UUID;

public final class CardCancellationContracts {
    private CardCancellationContracts() {}

    /** Every public state represents admitted work with its spend restriction retained. */
    public enum State {
        REQUESTED, LOCAL_RESTRICTION_RETAINED, PROVIDER_PENDING,
        PROVIDER_CONFIRMED, REJECTED, RECONCILIATION_REQUIRED
    }

    public enum SpendRestriction { RETAINED }

    public record Request(UUID actionId, Long expectedBindingVersion, String reasonCode,
                          Long acceptNotAfterEpochMillis) {}

    /** Server-resolved scope; a request never supplies identity or ownership. */
    public record Selection(UUID tenantId, UUID actorId, UUID partyId, UUID accountId,
                            UUID cardId, Long bindingVersion) {}

    /** Immutable admission linkage; database facts are supplied by the future persistence boundary. */
    public record Acceptance(UUID actionId, Selection selection, String intentDigest, UUID receiptId,
                             long databaseRecordedAtEpochMillis) {}

    public record Progress(UUID actionId, UUID cardId, long version, State state, SpendRestriction spendRestriction) {
        public Progress {
            if (actionId == null || actionId.equals(new UUID(0, 0)) || cardId == null
                    || cardId.equals(new UUID(0, 0)) || version < 0 || state == null
                    || spendRestriction != SpendRestriction.RETAINED) {
                throw new IllegalArgumentException("RETAINED_CARD_CANCELLATION_RESTRICTION_REQUIRED");
            }
        }
    }
}
