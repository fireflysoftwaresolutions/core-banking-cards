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


package io.fireflysoftware.core.banking.cards.core.services.card.activation;

import java.util.UUID;
import java.time.Instant;
import reactor.core.publisher.Mono;

/** Core-owned integration. Implementations verify the issuer using configured credentials. */
public interface CardIssuerPort {
    default Configuration configuration() { return Configuration.NOT_CONFIGURED; }

    /** One external command, keyed by the durable Core action. Implementations must not retry uncertain delivery. */
    default Mono<Observation> initiateActivation(Binding binding, UUID actionId, Instant expiresAt) {
        return Mono.error(new CardIssuerUnavailableException());
    }

    /** Read-only provider lookup for the same binding and action; this must never initiate activation. */
    Mono<Observation> observeActivation(Binding binding, UUID actionId);

    enum Configuration { CONFIGURED, NOT_CONFIGURED }

    record Binding(UUID cardId, UUID tenantId, UUID partyId, UUID accountId, String adapterId,
                   String instrumentReference, long version) {}
    enum Outcome { ACTIVATED, REJECTED, PENDING, UNCERTAIN }
    record Observation(UUID actionId, UUID tenantId, String adapterId, String instrumentReference,
                       long bindingVersion, Outcome outcome, String receiptReference) {}
}
