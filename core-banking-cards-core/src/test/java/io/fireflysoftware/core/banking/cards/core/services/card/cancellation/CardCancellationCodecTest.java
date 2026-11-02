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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class CardCancellationCodecTest {
    private static final String VALID = "{\"actionId\":\"11111111-1111-1111-1111-111111111111\",\"expectedBindingVersion\":7,\"reasonCode\":\"CUSTOMER_REQUEST\",\"acceptNotAfterEpochMillis\":2000}";
    private byte[] bytes(String text) { return text.getBytes(StandardCharsets.UTF_8); }

    @Test
    void exactRequestDecodesAndCanonicalDigestIgnoresFieldOrder() {
        var request = CardCancellationCodec.decode(bytes(VALID));
        assertEquals(CardCancellationRulesTest.REQUEST, request);
        var reordered = CardCancellationCodec.decode(bytes("{\"reasonCode\":\"CUSTOMER_REQUEST\",\"acceptNotAfterEpochMillis\":2000,\"expectedBindingVersion\":7,\"actionId\":\"11111111-1111-1111-1111-111111111111\"}"));
        assertEquals(CardCancellationRules.digest(CardCancellationRulesTest.SELECTED, request),
                CardCancellationRules.digest(CardCancellationRulesTest.SELECTED, reordered));
    }

    @Test
    void callerIdentityProviderOutcomeAndClearFlagsAreNotCommandInputs() {
        for (String field : new String[]{"tenantId", "actorId", "cancelledBy", "providerConfirmed", "clear", "cardStatus", "force"}) {
            assertThrows(IllegalArgumentException.class, () -> CardCancellationCodec.decode(bytes(VALID.replace("}", ",\"" + field + "\":true}"))));
        }
    }

    @Test
    void duplicateMissingNullAndTrailingDocumentsFailClosed() {
        for (String text : new String[]{VALID.replace("}", ",\"reasonCode\":\"OTHER\"}"),
                VALID.replace(",\"expectedBindingVersion\":7", ""), VALID.replace("\"CUSTOMER_REQUEST\"", "null"),
                VALID + " {}", "[]", "null", "{\"wrapper\":" + VALID + "}"}) {
            assertThrows(IllegalArgumentException.class, () -> CardCancellationCodec.decode(bytes(text)));
        }
    }

    @Test
    void coercedFractionalOverflowAndNoncanonicalIdentifiersAreRejected() {
        for (String text : new String[]{VALID.replace(":7,", ":\"7\","), VALID.replace(":7,", ":7.0,"),
                VALID.replace(":7,", ":7e0,"), VALID.replace(":2000}", ":9223372036854775808}"),
                VALID.replace("11111111-1111-1111-1111-111111111111", "1-1-1-1-1"),
                VALID.replace("CUSTOMER_REQUEST", "customer_request")}) {
            assertThrows(IllegalArgumentException.class, () -> CardCancellationCodec.decode(bytes(text)));
        }
    }

    @Test
    void payloadBoundsAndMalformedUtf8AreEnforcedBeforeBinding() {
        for (byte[] raw : new byte[][]{null, new byte[0], bytes(" ".repeat(8193)), {(byte)0xc3, (byte)0x28}}) {
            assertThrows(IllegalArgumentException.class, () -> CardCancellationCodec.decode(raw));
        }
    }
}
