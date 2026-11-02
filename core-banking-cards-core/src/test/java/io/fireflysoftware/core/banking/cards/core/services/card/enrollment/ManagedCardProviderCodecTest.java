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

import static io.fireflysoftware.core.banking.cards.interfaces.dtos.card.enrollment.ManagedCardProviderContracts.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ManagedCardProviderCodecTest {
    private static final String VALID = "{\"actionId\":\"11111111-1111-1111-1111-111111111111\",\"expectedProgressVersion\":1}";
    private byte[] bytes(String text) { return text.getBytes(StandardCharsets.UTF_8); }
    @Test void recoverySelectsOnlyTheRetainedActionAndProgressVersion() {
        assertEquals(new RecoveryRequest(ManagedCardEnrollmentRulesTest.ID, 1L), ManagedCardProviderCodec.decode(bytes(VALID)));
    }
    @Test void callerCannotChooseOperationProviderOutcomeDeadlineOrSensitiveData() {
        for (String field : new String[]{"operationId", "provider", "providerReceipt", "outcome", "active", "acceptNotAfterEpochMillis", "pan", "pin", "cvv"}) {
            assertThrows(IllegalArgumentException.class, () -> ManagedCardProviderCodec.decode(bytes(VALID.replace("}", ",\"" + field + "\":true}"))));
        }
    }
    @Test void duplicateMissingNullAndTrailingRequestsAreRejected() {
        for (String text : new String[]{VALID.replace("}", ",\"expectedProgressVersion\":2}"), VALID.replace(",\"expectedProgressVersion\":1", ""),
                VALID.replace(":1}", ":null}"), VALID + "{}", "null", "[]"}) assertThrows(IllegalArgumentException.class, () -> ManagedCardProviderCodec.decode(bytes(text)));
    }
    @Test void exactIntegerAndCanonicalUuidAreRequired() {
        for (String text : new String[]{VALID.replace(":1}", ":-1}"), VALID.replace(":1}", ":\"1\"}"), VALID.replace(":1}", ":1.0}"),
                VALID.replace(":1}", ":9223372036854775808}"), VALID.replace("11111111-1111-1111-1111-111111111111", "1-1-1-1-1")}) assertThrows(IllegalArgumentException.class, () -> ManagedCardProviderCodec.decode(bytes(text)));
    }
    @Test void malformedAndOversizedBodiesDoNotReachRecovery() {
        for (byte[] raw : new byte[][]{null, new byte[0], bytes(" ".repeat(8193)), {(byte)0xc3, (byte)0x28}}) assertThrows(IllegalArgumentException.class, () -> ManagedCardProviderCodec.decode(raw));
    }
}
