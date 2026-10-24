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


package io.fireflysoftware.core.banking.cards.core.services.provider;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.provider.CardProviderDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardProviderService {

    /**
     * Retrieve a paginated list of card providers for a specified card.
     */
    Mono<PaginationResponse<CardProviderDTO>> listProviders(UUID cardId, PaginationRequest paginationRequest);

    /**
     * Create a new card provider record for a specified card.
     */
    Mono<CardProviderDTO> createProvider(UUID cardId, CardProviderDTO providerDTO);

    /**
     * Retrieve a specific card provider by its unique ID,
     * ensuring it belongs to the specified card.
     */
    Mono<CardProviderDTO> getProvider(UUID cardId, UUID providerId);

    /**
     * Update an existing card provider by ID, ensuring it belongs to the specified card.
     */
    Mono<CardProviderDTO> updateProvider(UUID cardId, UUID providerId, CardProviderDTO providerDTO);

    /**
     * Delete a card provider record by its unique ID,
     * ensuring it belongs to the specified card.
     */
    Mono<Void> deleteProvider(UUID cardId, UUID providerId);
}
