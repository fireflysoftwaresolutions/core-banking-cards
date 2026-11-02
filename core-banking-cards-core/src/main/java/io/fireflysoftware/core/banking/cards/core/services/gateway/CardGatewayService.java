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


package io.fireflysoftware.core.banking.cards.core.services.gateway;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.gateway.CardGatewayDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardGatewayService {

    /**
     * List all card gateways (paginated).
     */
    Mono<PaginationResponse<CardGatewayDTO>> listGateways(PaginationRequest paginationRequest);

    /**
     * Create a new card gateway.
     */
    Mono<CardGatewayDTO> createGateway(CardGatewayDTO gatewayDTO);

    /**
     * Retrieve a specific card gateway by ID.
     */
    Mono<CardGatewayDTO> getGateway(UUID gatewayId);

    /**
     * Update an existing card gateway by ID.
     */
    Mono<CardGatewayDTO> updateGateway(UUID gatewayId, CardGatewayDTO gatewayDTO);

    /**
     * Delete a card gateway by its unique ID.
     */
    Mono<Void> deleteGateway(UUID gatewayId);
}
