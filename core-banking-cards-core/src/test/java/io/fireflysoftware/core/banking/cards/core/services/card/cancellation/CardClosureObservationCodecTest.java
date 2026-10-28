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

class CardClosureObservationCodecTest {
    private static final String VALID = "{\"actionId\":\"11111111-1111-1111-1111-111111111111\",\"closureId\":\"11111111-1111-1111-1111-111111111111\",\"enrollmentId\":\"11111111-1111-1111-1111-111111111111\",\"expectedFenceVersion\":0,\"acceptNotAfterEpochMillis\":2000}";
    private byte[] bytes(String text) { return text.getBytes(StandardCharsets.UTF_8); }

    @Test
    void aFreshFenceRequestHasOnlyRetainedActionAndSelectionIdentifiers() {
        assertEquals(CardClosureObservationRulesTest.REQUEST, CardClosureObservationCodec.decode(bytes(VALID)));
    }

    @Test
    void callerCannotDeclareCoverageIdentityOrProviderClearance() {
        for (String field : new String[]{"tenantId", "accountId", "actorId", "complete", "clear", "requiredCapabilities", "providerReceipt"}) {
            assertThrows(IllegalArgumentException.class, () -> CardClosureObservationCodec.decode(bytes(VALID.replace("}", ",\"" + field + "\":true}"))));
        }
    }

    @Test
    void duplicateNullMissingUnknownAndTrailingFieldsAreRejected() {
        for (String value : new String[]{VALID.replace("}", ",\"expectedFenceVersion\":1}"),
                VALID.replace(",\"expectedFenceVersion\":0", ""), VALID.replace(":0,", ":null,"), VALID + "[]", "[]", "null"}) {
            assertThrows(IllegalArgumentException.class, () -> CardClosureObservationCodec.decode(bytes(value)));
        }
    }

    @Test
    void onlyCanonicalIdentifiersExactIntegersAndFinitePositiveDeadlinesAreAccepted() {
        for (String value : new String[]{VALID.replace(":0,", ":-1,"), VALID.replace(":0,", ":\"0\","),
                VALID.replace(":0,", ":0.0,"), VALID.replace(":2000}", ":0}"),
                VALID.replace(":2000}", ":9223372036854775808}"), VALID.replace("11111111-1111-1111-1111-111111111111", "1-1-1-1-1")}) {
            assertThrows(IllegalArgumentException.class, () -> CardClosureObservationCodec.decode(bytes(value)));
        }
    }

    @Test
    void malformedAndOversizedBodiesAreRejectedWithoutLenientReplacement() {
        for (byte[] raw : new byte[][]{null, new byte[0], bytes(" ".repeat(8193)), {(byte)0xc3, (byte)0x28}}) {
            assertThrows(IllegalArgumentException.class, () -> CardClosureObservationCodec.decode(raw));
        }
    }
}
