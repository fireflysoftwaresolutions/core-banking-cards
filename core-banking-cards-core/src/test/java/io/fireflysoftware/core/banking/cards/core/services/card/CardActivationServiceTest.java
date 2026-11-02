/*
 * Copyright 2025 Firefly Software Foundation
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


package io.fireflysoftware.core.banking.cards.core.services.card;

import io.fireflysoftware.core.banking.cards.core.services.card.activation.impl.CardActivationServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.*;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

class CardActivationServiceTest {
    @Test
    void missingAuthenticatedPolicyOrIssuerCannotActivateARecord() {
        var service = new CardActivationServiceImpl(null, Optional.empty(), Optional.empty());
        var request = new CardActivationRequest();
        request.setActivationProof("syntheticPossessionProofForPersistenceTestsOnly0123456789");
        StepVerifier.create(service.activate(UUID.randomUUID(),UUID.randomUUID(),request))
                .expectErrorSatisfies(error -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE,
                        assertInstanceOf(ResponseStatusException.class,error).getStatusCode())).verify();
    }
    @Test
    void blankOrShortProofIsRejectedBeforeAnyPersistenceOrIssuerOperation() {
        CardActivationPolicy policy = card -> { throw new AssertionError("Invalid proof reached policy"); };
        CardIssuerPort issuer = (binding, action) -> { throw new AssertionError("Invalid proof reached issuer"); };
        var service = new CardActivationServiceImpl(null,Optional.of(policy),Optional.of(issuer));
        for (String proof : new String[]{"", " ", "123456", "a".repeat(129)}) {
            var request=new CardActivationRequest();
            request.setActivationProof(proof);
            StepVerifier.create(service.activate(UUID.randomUUID(),UUID.randomUUID(),request))
                    .expectErrorSatisfies(error -> assertEquals(HttpStatus.BAD_REQUEST,
                            assertInstanceOf(ResponseStatusException.class,error).getStatusCode())).verify();
        }
    }
    @Test
    void ephemeralProofDoesNotAppearInSerializationOrDiagnosticString() throws Exception {
        var request = new CardActivationRequest();
        String proof="syntheticPossessionProofForPersistenceTestsOnly0123456789";
        request.setActivationProof(proof);
        var serializationFailure = assertThrows(com.fasterxml.jackson.databind.exc.InvalidDefinitionException.class,
                () -> new ObjectMapper().writeValueAsString(request));
        assertFalse(serializationFailure.getMessage().contains(proof));
        assertFalse(request.toString().contains(proof));
    }
}
