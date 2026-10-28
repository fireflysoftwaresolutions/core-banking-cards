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


package io.fireflysoftware.core.banking.cards.core.services.dispute;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.dispute.CardDisputeDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardDisputeService {

    /**
     * List all disputes (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardDisputeDTO>> listDisputes(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new dispute for a specific card.
     */
    Mono<CardDisputeDTO> createDispute(UUID cardId, CardDisputeDTO disputeDTO);

    /**
     * Retrieve a specific dispute by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardDisputeDTO> getDispute(UUID cardId, UUID disputeId);

    /**
     * Update an existing dispute for a specific card.
     */
    Mono<CardDisputeDTO> updateDispute(UUID cardId, UUID disputeId, CardDisputeDTO disputeDTO);

    /**
     * Delete a dispute by its unique ID, ensuring it belongs to the card.
     */
    Mono<Void> deleteDispute(UUID cardId, UUID disputeId);
}
