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


package io.fireflysoftware.core.banking.cards.core.services.balance;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.balance.CardBalanceDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardBalanceService {

    /**
     * List all balances (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardBalanceDTO>> listBalances(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new balance for a specific card.
     */
    Mono<CardBalanceDTO> createBalance(UUID cardId, CardBalanceDTO balanceDTO);

    /**
     * Retrieve a specific balance by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardBalanceDTO> getBalance(UUID cardId, UUID balanceId);

    /**
     * Update an existing balance for a specific card.
     */
    Mono<CardBalanceDTO> updateBalance(UUID cardId, UUID balanceId, CardBalanceDTO balanceDTO);

    /**
     * Delete a balance by its unique ID, ensuring it belongs to the card.
     */
    Mono<Void> deleteBalance(UUID cardId, UUID balanceId);
}
