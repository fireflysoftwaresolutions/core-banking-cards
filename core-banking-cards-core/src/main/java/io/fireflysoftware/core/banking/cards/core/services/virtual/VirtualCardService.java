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


package io.fireflysoftware.core.banking.cards.core.services.virtual;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.virtual.VirtualCardDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface VirtualCardService {

    /**
     * Retrieve a paginated list of virtual cards for a specific card.
     */
    Mono<PaginationResponse<VirtualCardDTO>> listVirtualCards(UUID cardId, PaginationRequest paginationRequest);

    /**
     * Create a new virtual card record for a specific card.
     */
    Mono<VirtualCardDTO> createVirtualCard(UUID cardId, VirtualCardDTO virtualCardDTO);

    /**
     * Retrieve a specific virtual card by its ID, ensuring it belongs to the specified card.
     */
    Mono<VirtualCardDTO> getVirtualCard(UUID cardId, UUID virtualCardId);

    /**
     * Update an existing virtual card associated with the specified card.
     */
    Mono<VirtualCardDTO> updateVirtualCard(UUID cardId, UUID virtualCardId, VirtualCardDTO virtualCardDTO);

    /**
     * Delete a virtual card by its unique ID, ensuring it belongs to the specified card.
     */
    Mono<Void> deleteVirtualCard(UUID cardId, UUID virtualCardId);
}