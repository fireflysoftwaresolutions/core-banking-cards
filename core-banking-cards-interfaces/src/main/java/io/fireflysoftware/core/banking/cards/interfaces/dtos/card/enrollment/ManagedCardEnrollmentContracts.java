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

package io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment;

import java.util.UUID;

public final class ManagedCardEnrollmentContracts {
    private ManagedCardEnrollmentContracts() {}

    /** A server-resolved managed aggregate identity; no legacy lookup or conversion is implied. */
    public record ManagedCardId(UUID value) {
        public ManagedCardId {
            if (value == null || value.equals(new UUID(0, 0))) throw new IllegalArgumentException("MANAGED_CARD_ID_REQUIRED");
        }
    }
    public enum Owner { ACCOUNT, PRODUCT }
    public enum Eligibility { UNKNOWN, INELIGIBLE, ELIGIBLE }
    public enum State { RESERVED, PROVIDER_PENDING, RECONCILIATION_REQUIRED, ENROLLED_INACTIVE, REJECTED }
    public enum Restriction { RETAINED }
    public record Request(UUID actionId, Long expectedAccountVersion, String reasonCode, Long acceptNotAfterEpochMillis) {}
    public record ConfiguredProviders(String issuerAdapterId, String processorAdapterId) {}
    /** Current authenticated owner selection and configured capabilities, never caller-declared scope. */
    public record Selection(UUID tenantId, UUID actorId, UUID partyId, UUID accountId, UUID contractId,
                            UUID productId, ManagedCardId managedCardId, UUID enrollmentId, Long accountVersion,
                            Long productVersion, ConfiguredProviders providers) {}
    /** Internal owner evidence; authenticity and actual eligibility must be established by the owner adapter. */
    public record EligibilityReceipt(UUID receiptId, Owner owner, UUID tenantId, UUID partyId, UUID accountId,
                                     UUID contractId, UUID productId, Long sourceVersion, Long validUntilEpochMillis,
                                     Eligibility status) {}
    public record Acceptance(UUID actionId, Selection selection, String intentDigest, UUID receiptId,
                             long databaseRecordedAtEpochMillis) {}
    public record Progress(UUID actionId, ManagedCardId managedCardId, long version, State state, Restriction restriction) {
        public Progress {
            if (actionId == null || actionId.equals(new UUID(0, 0)) || managedCardId == null || version < 0
                    || state == null || restriction != Restriction.RETAINED) throw new IllegalArgumentException("RESTRICTED_MANAGED_CARD_PROGRESS_REQUIRED");
        }
    }
}
