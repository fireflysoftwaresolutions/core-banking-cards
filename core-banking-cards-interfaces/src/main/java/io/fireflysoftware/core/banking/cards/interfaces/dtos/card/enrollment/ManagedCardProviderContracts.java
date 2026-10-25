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

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardEnrollmentContracts.Selection;
import java.util.UUID;

public final class ManagedCardProviderContracts {
    private ManagedCardProviderContracts() {}
    public enum Role { ISSUER, PROCESSOR }
    public enum Outcome { PENDING, UNKNOWN, ISSUED_INACTIVE, REJECTED, ABSENT_CLOSED }
    public enum UseState { INACTIVE_RESTRICTED, ACTIVE, UNKNOWN }
    public enum NextStep { ISSUE, OBSERVE, NONE }
    /** Retained owner operation; a recovery request cannot replace its identity or deadline. */
    public record Operation(UUID operationId, UUID actionId, Selection selection, String intentDigest,
                            Long acceptNotAfterEpochMillis) {}
    /** Verified adapter output; instrumentReference is the common issuer identity, distinct from each provider local reference. */
    public record Observation(UUID operationId, UUID actionId, Selection selection, String intentDigest,
                              Role role, String adapterId, Outcome outcome, UseState useState,
                              String instrumentReference, String providerInstrumentReference, String providerReceipt, Long providerRevision,
                              Long observedAtEpochMillis) {}
    public record RecoveryRequest(UUID actionId, Long expectedProgressVersion) {}
}
