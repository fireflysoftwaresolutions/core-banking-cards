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


package io.fireflysoftware.core.banking.cards.core.services.activity;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.activity.CardActivityDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardActivityService {

    /**
     * List all activities (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardActivityDTO>> listActivities(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new activity for a specific card.
     */
    Mono<CardActivityDTO> createActivity(UUID cardId, CardActivityDTO activityDTO);

    /**
     * Retrieve a specific activity by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardActivityDTO> getActivity(UUID cardId, UUID activityId);

    /**
     * Update an existing activity for a specific card.
     */
    Mono<CardActivityDTO> updateActivity(UUID cardId, UUID activityId, CardActivityDTO activityDTO);

    /**
     * Delete an activity by its unique ID, ensuring it belongs to the card.
     */
    Mono<Void> deleteActivity(UUID cardId, UUID activityId);
}
