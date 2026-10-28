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

import java.util.Map;
import java.util.Objects;

public final class ManagedCardProviderRules {
    private ManagedCardProviderRules() {}

    public static void requireOperation(Operation operation, Selection retained, Request original) {
        String digest = ManagedCardEnrollmentRules.digest(retained, original);
        if (operation == null || !ManagedCardEnrollmentRules.uuid(operation.operationId())
                || !Objects.equals(original.actionId(), operation.actionId()) || !retained.equals(operation.selection())
                || !digest.equals(operation.intentDigest()) || !original.acceptNotAfterEpochMillis().equals(operation.acceptNotAfterEpochMillis())) {
            throw ManagedCardEnrollmentRules.invalid();
        }
    }

    public static State resolve(Operation operation, Selection retained, Request original, Map<Role, Observation> facts, long now) {
        requireOperation(operation, retained, original);
        if (now < 0) throw ManagedCardEnrollmentRules.invalid();
        if (facts == null || facts.size() != Role.values().length) return State.RECONCILIATION_REQUIRED;
        boolean pending = false;
        boolean noEffect = false;
        boolean issued = false;
        String issuerReference = null;
        String processorIssuerReference = null;
        for (Role role : Role.values()) {
            Observation fact = facts.get(role);
            String adapter = role == Role.ISSUER ? retained.providers().issuerAdapterId() : retained.providers().processorAdapterId();
            if (fact == null || fact.role() != role || !Objects.equals(operation.operationId(), fact.operationId())
                    || !Objects.equals(operation.actionId(), fact.actionId()) || !retained.equals(fact.selection())
                    || !operation.intentDigest().equals(fact.intentDigest()) || !adapter.equals(fact.adapterId())
                    || fact.outcome() == null || fact.outcome() == Outcome.UNKNOWN || fact.useState() == null
                    || fact.useState() == UseState.ACTIVE || !reference(fact.providerReceipt())
                    || fact.providerRevision() == null || fact.providerRevision() < 0 || fact.observedAtEpochMillis() == null
                    || fact.observedAtEpochMillis() <= 0 || fact.observedAtEpochMillis() > now) return State.RECONCILIATION_REQUIRED;
            switch (fact.outcome()) {
                case ISSUED_INACTIVE -> {
                    if (fact.useState() != UseState.INACTIVE_RESTRICTED || !reference(fact.instrumentReference())
                            || !reference(fact.providerInstrumentReference())) return State.RECONCILIATION_REQUIRED;
                    if (role == Role.ISSUER) {
                        if (!fact.instrumentReference().equals(fact.providerInstrumentReference())) return State.RECONCILIATION_REQUIRED;
                        issuerReference = fact.instrumentReference();
                    } else {
                        processorIssuerReference = fact.instrumentReference();
                    }
                    issued = true;
                }
                case PENDING -> pending = true;
                case REJECTED, ABSENT_CLOSED -> noEffect = true;
                case UNKNOWN -> { return State.RECONCILIATION_REQUIRED; }
            }
        }
        if (issuerReference != null && processorIssuerReference != null
                && !issuerReference.equals(processorIssuerReference)) return State.RECONCILIATION_REQUIRED;
        if (noEffect && (issued || pending)) return State.RECONCILIATION_REQUIRED;
        if (noEffect) return State.REJECTED;
        return pending ? State.PROVIDER_PENDING : State.ENROLLED_INACTIVE;
    }

    public static NextStep nextStep(State state) {
        if (state == null) throw ManagedCardEnrollmentRules.invalid();
        return switch (state) {
            case RESERVED -> NextStep.ISSUE;
            case PROVIDER_PENDING, RECONCILIATION_REQUIRED -> NextStep.OBSERVE;
            case ENROLLED_INACTIVE, REJECTED -> NextStep.NONE;
        };
    }

    /** The future store must commit this state change before issuing the retained operation. */
    public static State beginIssue(State state) {
        if (state != State.RESERVED) throw new IllegalStateException("MANAGED_CARD_ISSUE_ALREADY_ATTEMPTED");
        return State.PROVIDER_PENDING;
    }

    static void validateRecovery(RecoveryRequest request) {
        if (request == null || !ManagedCardEnrollmentRules.uuid(request.actionId()) || request.expectedProgressVersion() == null
                || request.expectedProgressVersion() < 0) throw ManagedCardEnrollmentRules.invalid();
    }

    public static void requireRecovery(RecoveryRequest request, Operation retained, Selection selected, Request original, long currentVersion) {
        validateRecovery(request);
        requireOperation(retained, selected, original);
        if (currentVersion < 0) throw ManagedCardEnrollmentRules.invalid();
        if (!request.actionId().equals(retained.actionId()) || request.expectedProgressVersion() != currentVersion) {
            throw new IllegalStateException("MANAGED_CARD_RECOVERY_CONFLICT");
        }
    }

    private static boolean reference(String value) {
        return value != null && value.matches("[A-Za-z0-9][A-Za-z0-9_.:/-]{0,127}");
    }
}
