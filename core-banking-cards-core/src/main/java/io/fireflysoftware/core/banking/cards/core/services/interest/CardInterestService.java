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


package io.fireflysoftware.core.banking.cards.core.services.interest;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.interest.CardInterestDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardInterestService {

    /**
     * List all interest records (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardInterestDTO>> listInterests(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new interest record for a specific card.
     */
    Mono<CardInterestDTO> createInterest(UUID cardId, CardInterestDTO interestDTO);

    /**
     * Retrieve a specific interest record by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardInterestDTO> getInterest(UUID cardId, UUID interestId);

    /**
     * Update an existing interest record for a specific card.
     */
    Mono<CardInterestDTO> updateInterest(UUID cardId, UUID interestId, CardInterestDTO interestDTO);

    /**
     * Delete an interest record by its unique ID, ensuring it belongs to the card.
     */
    Mono<Void> deleteInterest(UUID cardId, UUID interestId);
}
