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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

public final class CardCancellationRules {
    private CardCancellationRules() {}

    public static void validate(Request request) {
        if (request == null || !uuid(request.actionId()) || request.expectedBindingVersion() == null
                || request.expectedBindingVersion() < 1 || request.reasonCode() == null
                || !request.reasonCode().matches("[A-Z][A-Z0-9_]{0,63}")
                || request.acceptNotAfterEpochMillis() == null || request.acceptNotAfterEpochMillis() <= 0) {
            throw invalid();
        }
    }

    public static void admit(Request request, long bindingVersion, long now) {
        validate(request);
        if (bindingVersion < 1 || now < 0) throw invalid();
        if (request.expectedBindingVersion() != bindingVersion || request.acceptNotAfterEpochMillis() <= now) {
            throw new IllegalStateException("CARD_CANCELLATION_ADMISSION_CONFLICT");
        }
    }

    public static String digest(Selection selection, Request request) {
        selection(selection);
        validate(request);
        if (!selection.bindingVersion().equals(request.expectedBindingVersion())) throw invalid();
        return canonicalDigest("CARD_CANCELLATION_INTENT_1", List.of(
                selection.tenantId().toString(), selection.actorId().toString(), selection.partyId().toString(),
                selection.accountId().toString(), selection.cardId().toString(), selection.bindingVersion().toString(),
                request.actionId().toString(), request.expectedBindingVersion().toString(),
                request.reasonCode(), request.acceptNotAfterEpochMillis().toString()));
    }

    static String canonicalDigest(String domain, List<String> fields) {
        var value = new StringBuilder(domain).append('\n');
        for (String field : fields) {
            value.append(field.getBytes(StandardCharsets.UTF_8).length).append(':').append(field);
        }
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException(error);
        }
    }

    /** Pure retained-intent comparison; current authorization belongs to the command boundary. */
    public static void requireReplay(Selection stored, Request original, Selection current, Request retry) {
        digest(stored, original);
        selection(current);
        validate(retry);
        if (!stored.equals(current) || !original.equals(retry)) {
            throw new IllegalStateException("CARD_CANCELLATION_INTENT_CONFLICT");
        }
    }

    /** Validates retained facts; it does not authenticate, commit or observe a database. */
    public static void requireAcceptance(Acceptance acceptance, Selection retained, Request original,
                                         UUID retainedReceiptId, long databaseRecordedAt) {
        String intentDigest = digest(retained, original);
        if (acceptance == null || !uuid(retainedReceiptId) || databaseRecordedAt <= 0
                || !uuid(acceptance.actionId()) || !uuid(acceptance.receiptId())
                || acceptance.intentDigest() == null || !acceptance.intentDigest().matches("[0-9a-f]{64}")
                || acceptance.databaseRecordedAtEpochMillis() <= 0) throw invalid();
        selection(acceptance.selection());
        admit(original, retained.bindingVersion(), databaseRecordedAt);
        if (!original.actionId().equals(acceptance.actionId()) || !retained.equals(acceptance.selection())
                || !intentDigest.equals(acceptance.intentDigest()) || !retainedReceiptId.equals(acceptance.receiptId())
                || databaseRecordedAt != acceptance.databaseRecordedAtEpochMillis()) {
            throw new IllegalStateException("CARD_CANCELLATION_ACCEPTANCE_CONFLICT");
        }
    }

    public static Progress initialProgress(Acceptance acceptance, Selection retained, Request original,
                                           UUID retainedReceiptId, long databaseRecordedAt) {
        requireAcceptance(acceptance, retained, original, retainedReceiptId, databaseRecordedAt);
        return new Progress(acceptance.actionId(), acceptance.selection().cardId(), 0,
                State.REQUESTED, SpendRestriction.RETAINED);
    }

    public static boolean restrictionRetained(State state) {
        if (state == null) throw invalid();
        return true;
    }

    public static void transition(State from, State to) {
        if (from == null || to == null) throw invalid();
        if (from == to) return;
        boolean allowed = switch (from) {
            case REQUESTED -> to == State.LOCAL_RESTRICTION_RETAINED;
            case LOCAL_RESTRICTION_RETAINED -> to == State.PROVIDER_PENDING;
            case PROVIDER_PENDING -> to == State.PROVIDER_CONFIRMED || to == State.REJECTED
                    || to == State.RECONCILIATION_REQUIRED;
            case RECONCILIATION_REQUIRED -> to == State.PROVIDER_PENDING;
            case PROVIDER_CONFIRMED, REJECTED -> false;
        };
        if (!allowed) throw new IllegalStateException("CARD_CANCELLATION_TRANSITION_CONFLICT");
    }

    private static void selection(Selection selection) {
        if (selection == null || !uuid(selection.tenantId()) || !uuid(selection.actorId())
                || !uuid(selection.partyId()) || !uuid(selection.accountId()) || !uuid(selection.cardId())
                || selection.bindingVersion() == null || selection.bindingVersion() < 1) throw invalid();
    }

    static boolean uuid(UUID value) {
        return value != null && !value.equals(new UUID(0, 0));
    }

    private static IllegalArgumentException invalid() {
        return new IllegalArgumentException("EXACT_CARD_CANCELLATION_INTENT_REQUIRED");
    }
}
