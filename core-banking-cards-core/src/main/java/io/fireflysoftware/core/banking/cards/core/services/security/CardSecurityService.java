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


package io.fireflysoftware.core.banking.cards.core.services.security;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.security.CardSecurityDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardSecurityService {

    /**
     * List all security settings (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardSecurityDTO>> listSecuritySettings(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new security setting for a specific card.
     */
    Mono<CardSecurityDTO> createSecuritySetting(UUID cardId, CardSecurityDTO securityDTO);

    /**
     * Retrieve a specific security setting by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardSecurityDTO> getSecuritySetting(UUID cardId, UUID securityId);

    /**
     * Update an existing security setting for a specific card.
     */
    Mono<CardSecurityDTO> updateSecuritySetting(UUID cardId, UUID securityId, CardSecurityDTO securityDTO);

    /**
     * Delete a security setting by its unique ID, ensuring it belongs to the specified card.
     */
    Mono<Void> deleteSecuritySetting(UUID cardId, UUID securityId);
}
