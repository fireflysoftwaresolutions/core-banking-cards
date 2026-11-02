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


package io.fireflysoftware.core.banking.cards.core.services.physical;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.physical.PhysicalCardDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface PhysicalCardService {

    /**
     * Retrieve a paginated list of physical cards for a specified card.
     */
    Mono<PaginationResponse<PhysicalCardDTO>> listPhysicalCards(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new physical card record for a specified card.
     */
    Mono<PhysicalCardDTO> createPhysicalCard(UUID cardId, PhysicalCardDTO physicalCardDTO);

    /**
     * Retrieve a specific physical card by its ID, ensuring it belongs to the specified card.
     */
    Mono<PhysicalCardDTO> getPhysicalCard(UUID cardId, UUID physicalCardId);

    /**
     * Update an existing physical card for the specified card.
     */
    Mono<PhysicalCardDTO> updatePhysicalCard(UUID cardId, UUID physicalCardId, PhysicalCardDTO physicalCardDTO);

    /**
     * Delete a physical card by its unique ID, ensuring it belongs to the specified card.
     */
    Mono<Void> deletePhysicalCard(UUID cardId, UUID physicalCardId);
}
