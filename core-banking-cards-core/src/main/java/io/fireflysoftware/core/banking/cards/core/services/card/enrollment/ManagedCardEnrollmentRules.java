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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ManagedCardEnrollmentRules {
    private ManagedCardEnrollmentRules() {}

    public static void validate(Request request) {
        if (request == null || !uuid(request.actionId()) || request.expectedAccountVersion() == null
                || request.expectedAccountVersion() < 0 || request.reasonCode() == null
                || !request.reasonCode().matches("[A-Z][A-Z0-9_]{0,63}")
                || request.acceptNotAfterEpochMillis() == null || request.acceptNotAfterEpochMillis() <= 0) throw invalid();
    }

    /** Pure owner-evidence comparison; the future adapters must establish authenticity and actual eligibility. */
    public static Eligibility eligibility(Selection selected, Map<Owner, EligibilityReceipt> facts, long now) {
        selection(selected);
        if (now < 0) throw invalid();
        if (facts == null || facts.size() != Owner.values().length) return Eligibility.UNKNOWN;
        boolean ineligible = false;
        for (Owner owner : Owner.values()) {
            EligibilityReceipt receipt = facts.get(owner);
            long version = owner == Owner.ACCOUNT ? selected.accountVersion() : selected.productVersion();
            if (receipt == null || !uuid(receipt.receiptId()) || receipt.owner() != owner
                    || !Objects.equals(selected.tenantId(), receipt.tenantId())
                    || !Objects.equals(selected.partyId(), receipt.partyId())
                    || !Objects.equals(selected.accountId(), receipt.accountId())
                    || !Objects.equals(selected.contractId(), receipt.contractId())
                    || !Objects.equals(selected.productId(), receipt.productId())
                    || receipt.sourceVersion() == null || receipt.sourceVersion() != version
                    || receipt.validUntilEpochMillis() == null || receipt.validUntilEpochMillis() <= now
                    || receipt.status() == null || receipt.status() == Eligibility.UNKNOWN) return Eligibility.UNKNOWN;
            if (receipt.status() == Eligibility.INELIGIBLE) ineligible = true;
        }
        return ineligible ? Eligibility.INELIGIBLE : Eligibility.ELIGIBLE;
    }

    public static void admit(Request request, Selection selected, Map<Owner, EligibilityReceipt> facts, long now) {
        validate(request);
        selection(selected);
        configured(selected.providers());
        if (now < 0) throw invalid();
        if (!request.expectedAccountVersion().equals(selected.accountVersion()) || request.acceptNotAfterEpochMillis() <= now) {
            throw new IllegalStateException("MANAGED_CARD_ENROLLMENT_ADMISSION_CONFLICT");
        }
        if (eligibility(selected, facts, now) != Eligibility.ELIGIBLE) {
            throw new IllegalStateException("MANAGED_CARD_ELIGIBILITY_UNAVAILABLE_OR_DENIED");
        }
    }

    public static String digest(Selection selected, Request request) {
        validate(request);
        selection(selected);
        configured(selected.providers());
        if (!request.expectedAccountVersion().equals(selected.accountVersion())) throw invalid();
        var value = new StringBuilder("MANAGED_CARD_ENROLLMENT_INTENT_1\n");
        for (String field : List.of(selected.tenantId().toString(), selected.actorId().toString(),
                selected.partyId().toString(), selected.accountId().toString(), selected.contractId().toString(),
                selected.productId().toString(), selected.managedCardId().value().toString(), selected.enrollmentId().toString(),
                selected.accountVersion().toString(), selected.productVersion().toString(), selected.providers().issuerAdapterId(),
                selected.providers().processorAdapterId(), request.actionId().toString(), request.expectedAccountVersion().toString(),
                request.reasonCode(), request.acceptNotAfterEpochMillis().toString())) {
            value.append(field.getBytes(StandardCharsets.UTF_8).length).append(':').append(field);
        }
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException(error);
        }
    }

    public static void requireReplay(Selection retained, Request original, Selection current, Request retry) {
        digest(retained, original);
        selection(current);
        configured(current.providers());
        validate(retry);
        if (!retained.equals(current) || !original.equals(retry)) throw new IllegalStateException("MANAGED_CARD_ENROLLMENT_INTENT_CONFLICT");
    }

    /** Compares supplied retained facts; it does not commit or authenticate an admission. */
    public static void requireAcceptance(Acceptance acceptance, Selection retained, Request original, UUID receiptId, long databaseTime) {
        String digest = digest(retained, original);
        if (acceptance == null || !uuid(acceptance.actionId()) || !uuid(acceptance.receiptId()) || !uuid(receiptId)
                || acceptance.intentDigest() == null || !acceptance.intentDigest().matches("[0-9a-f]{64}")
                || acceptance.databaseRecordedAtEpochMillis() <= 0 || databaseTime <= 0) throw invalid();
        selection(acceptance.selection());
        if (databaseTime >= original.acceptNotAfterEpochMillis() || !acceptance.actionId().equals(original.actionId())
                || !retained.equals(acceptance.selection()) || !digest.equals(acceptance.intentDigest())
                || !receiptId.equals(acceptance.receiptId()) || databaseTime != acceptance.databaseRecordedAtEpochMillis()) {
            throw new IllegalStateException("MANAGED_CARD_ENROLLMENT_ACCEPTANCE_CONFLICT");
        }
    }

    static void selection(Selection selected) {
        if (selected == null || !uuid(selected.tenantId()) || !uuid(selected.actorId()) || !uuid(selected.partyId())
                || !uuid(selected.accountId()) || !uuid(selected.contractId()) || !uuid(selected.productId())
                || selected.managedCardId() == null || !uuid(selected.enrollmentId())
                || selected.accountVersion() == null || selected.accountVersion() < 0
                || selected.productVersion() == null || selected.productVersion() < 0) throw invalid();
    }

    static void configured(ConfiguredProviders providers) {
        if (providers == null || !adapter(providers.issuerAdapterId()) || !adapter(providers.processorAdapterId())) {
            throw new IllegalStateException("MANAGED_CARD_PROVIDER_UNAVAILABLE");
        }
    }

    static boolean adapter(String id) { return id != null && id.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}"); }
    static boolean uuid(UUID value) { return value != null && !value.equals(new UUID(0, 0)); }
    static IllegalArgumentException invalid() { return new IllegalArgumentException("EXACT_MANAGED_CARD_ENROLLMENT_REQUIRED"); }
}
