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

import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ManagedCardEnrollmentCodecTest {
    static final String VALID = "{\"actionId\":\"11111111-1111-1111-1111-111111111111\",\"expectedAccountVersion\":2,\"reasonCode\":\"CUSTOMER_REQUEST\",\"acceptNotAfterEpochMillis\":2000}";
    private byte[] bytes(String text) { return text.getBytes(StandardCharsets.UTF_8); }
    @Test void decodesOnlyTheStableEnrollmentIntent() {
        assertEquals(ManagedCardEnrollmentRulesTest.REQUEST, ManagedCardEnrollmentCodec.decode(bytes(VALID)));
    }
    @Test void identityEligibilityProviderAndSensitiveDataAreNeverCallerInputs() {
        for (String field : new String[]{"tenantId", "actorId", "accountId", "contractId", "productId", "managedCardId",
                "legacyCardId", "eligible", "providerReference", "active", "cardNumber", "pan", "pin", "cvv"}) {
            assertThrows(IllegalArgumentException.class, () -> ManagedCardEnrollmentCodec.decode(bytes(VALID.replace("}", ",\"" + field + "\":true}"))));
        }
    }
    @Test void rejectsDuplicateMissingNullAndTrailingObjects() {
        for (String text : new String[]{VALID.replace("}", ",\"reasonCode\":\"OTHER\"}"), VALID.replace(",\"expectedAccountVersion\":2", ""),
                VALID.replace(":2,", ":null,"), VALID + "{}", "[]", "null"}) {
            assertThrows(IllegalArgumentException.class, () -> ManagedCardEnrollmentCodec.decode(bytes(text)));
        }
    }
    @Test void versionsAndDeadlinesAreExactIntegersAndUuidIsCanonical() {
        for (String text : new String[]{VALID.replace(":2,", ":\"2\","), VALID.replace(":2,", ":2.0,"), VALID.replace(":2,", ":2e0,"),
                VALID.replace(":2000}", ":9223372036854775808}"), VALID.replace("11111111-1111-1111-1111-111111111111", "1-1-1-1-1")}) {
            assertThrows(IllegalArgumentException.class, () -> ManagedCardEnrollmentCodec.decode(bytes(text)));
        }
    }
    @Test void malformedUtf8AndOversizedBodiesFailBeforeBinding() {
        for (byte[] bytes : new byte[][]{null, new byte[0], bytes(" ".repeat(8193)), {(byte)0xc3, (byte)0x28}}) {
            assertThrows(IllegalArgumentException.class, () -> ManagedCardEnrollmentCodec.decode(bytes));
        }
    }
}
