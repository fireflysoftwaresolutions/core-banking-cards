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

import java.util.List;
import java.util.Map;

public final class CardClosureObservationRules {
    private CardClosureObservationRules() {}

    public static Status coverage(Scope expected, Map<Capability, Observation> observations) {
        scope(expected);
        if (observations == null || observations.size() != Capability.values().length) return Status.UNKNOWN;
        boolean obligations = false;
        for (Capability capability : Capability.values()) {
            Observation evidence = observations.get(capability);
            if (evidence == null || !CardCancellationRules.uuid(evidence.receiptId())
                    || !expected.equals(evidence.scope()) || evidence.capability() != capability
                    || evidence.sourceVersion() == null || evidence.sourceVersion() < 0
                    || !evidence.complete() || evidence.status() == null || evidence.status() == Status.UNKNOWN
                    || evidence.pendingCount() == null || evidence.pendingCount() < 0
                    || evidence.unknownCount() == null || evidence.unknownCount() != 0) return Status.UNKNOWN;
            if ((evidence.status() == Status.CLEAR || evidence.status() == Status.NOT_APPLICABLE)
                    && evidence.pendingCount() != 0) return Status.UNKNOWN;
            if (evidence.status() == Status.OBLIGATIONS) obligations = true;
        }
        return obligations ? Status.OBLIGATIONS : Status.CLEAR;
    }

    static void validate(FenceRequest request) {
        if (request == null || !CardCancellationRules.uuid(request.actionId())
                || !CardCancellationRules.uuid(request.closureId()) || !CardCancellationRules.uuid(request.enrollmentId())
                || request.expectedFenceVersion() == null || request.expectedFenceVersion() < 0
                || request.acceptNotAfterEpochMillis() == null || request.acceptNotAfterEpochMillis() <= 0) throw invalid();
    }

    public static void admit(FenceRequest request, long version, long now) {
        validate(request);
        if (version < 0 || now < 0) throw invalid();
        if (request.expectedFenceVersion() != version || request.acceptNotAfterEpochMillis() <= now) {
            throw new IllegalStateException("CARD_CLOSURE_FENCE_ADMISSION_CONFLICT");
        }
    }

    public static String digest(FenceSelection selected, FenceRequest request) {
        selection(selected);
        validate(request);
        if (!selected.enrollmentId().equals(request.enrollmentId())) throw invalid();
        return CardCancellationRules.canonicalDigest("CARD_CLOSURE_FENCE_INTENT_1", List.of(
                selected.tenantId().toString(), selected.actorId().toString(), selected.accountId().toString(),
                selected.enrollmentId().toString(), request.actionId().toString(), request.closureId().toString(),
                request.enrollmentId().toString(), request.expectedFenceVersion().toString(),
                request.acceptNotAfterEpochMillis().toString()));
    }

    public static void requireReplay(FenceSelection stored, FenceRequest original, FenceSelection current, FenceRequest retry) {
        digest(stored, original);
        selection(current);
        validate(retry);
        if (!stored.equals(current) || !original.equals(retry)) {
            throw new IllegalStateException("CARD_CLOSURE_FENCE_INTENT_CONFLICT");
        }
    }

    private static void selection(FenceSelection selected) {
        if (selected == null || !CardCancellationRules.uuid(selected.tenantId())
                || !CardCancellationRules.uuid(selected.actorId()) || !CardCancellationRules.uuid(selected.accountId())
                || !CardCancellationRules.uuid(selected.enrollmentId())) throw invalid();
    }

    private static void scope(Scope scope) {
        if (scope == null || !CardCancellationRules.uuid(scope.tenantId())
                || !CardCancellationRules.uuid(scope.accountId()) || !CardCancellationRules.uuid(scope.enrollmentId())
                || !CardCancellationRules.uuid(scope.closureId()) || scope.relationshipVersion() == null
                || scope.relationshipVersion() < 0 || scope.fenceVersion() == null || scope.fenceVersion() < 1
                || scope.selectionDigest() == null || !scope.selectionDigest().matches("[0-9a-f]{64}")) throw invalid();
    }

    private static IllegalArgumentException invalid() {
        return new IllegalArgumentException("EXACT_CARD_CLOSURE_SELECTION_REQUIRED");
    }
}
