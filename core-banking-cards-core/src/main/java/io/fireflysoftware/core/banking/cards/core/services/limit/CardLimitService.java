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


package io.fireflysoftware.core.banking.cards.core.services.limit;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.limit.CardLimitDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardLimitService {

    /**
     * Retrieve a paginated list of limits for a specified card.
     */
    Mono<PaginationResponse<CardLimitDTO>> listLimits(UUID cardId, PaginationRequest paginationRequest);

    /**
     * Create a new limit record for a specified card.
     */
    Mono<CardLimitDTO> createLimit(UUID cardId, CardLimitDTO limitDTO);

    /**
     * Retrieve a specific limit by its unique ID, ensuring it belongs to the specified card.
     */
    Mono<CardLimitDTO> getLimit(UUID cardId, UUID limitId);

    /**
     * Update an existing limit record for a specific card.
     */
    Mono<CardLimitDTO> updateLimit(UUID cardId, UUID limitId, CardLimitDTO limitDTO);

    /**
     * Delete a limit by its unique ID, ensuring it belongs to the specified card.
     */
    Mono<Void> deleteLimit(UUID cardId, UUID limitId);
}