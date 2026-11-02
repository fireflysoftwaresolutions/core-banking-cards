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

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardService {

    /**
     * Retrieves a paginated list of cards based on the provided filter criteria.
     *
     * @param filterRequest the filter and pagination criteria for retrieving cards
     * @return a reactive stream emitting a PaginationResponse containing a list of CardDTO objects
     */
    Mono<PaginationResponse<CardDTO>> filterCards(FilterRequest<CardDTO> filterRequest);

    /**
     * Create a new card record.
     */
    Mono<CardDTO> createCard(CardDTO cardDTO);

    /**
     * Retrieve a specific card by ID.
     */
    Mono<CardDTO> getCard(UUID cardId);

    /**
     * Update an existing card record by ID.
     */
    Mono<CardDTO> updateCard(UUID cardId, CardDTO cardDTO);

    /**
     * Delete a card by its unique ID.
     */
    Mono<Void> deleteCard(UUID cardId);
}