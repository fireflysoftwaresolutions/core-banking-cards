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


package io.fireflysoftware.core.banking.cards.core.services.promotion;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.promotion.CardPromotionDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardPromotionService {

    /**
     * List all promotions (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardPromotionDTO>> listPromotions(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new promotion for a specific card.
     */
    Mono<CardPromotionDTO> createPromotion(UUID cardId, CardPromotionDTO promotionDTO);

    /**
     * Retrieve a specific promotion by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardPromotionDTO> getPromotion(UUID cardId, UUID promotionId);

    /**
     * Update an existing promotion for a specific card.
     */
    Mono<CardPromotionDTO> updatePromotion(UUID cardId, UUID promotionId, CardPromotionDTO promotionDTO);

    /**
     * Delete a promotion by its unique ID, ensuring it belongs to the card.
     */
    Mono<Void> deletePromotion(UUID cardId, UUID promotionId);
}
