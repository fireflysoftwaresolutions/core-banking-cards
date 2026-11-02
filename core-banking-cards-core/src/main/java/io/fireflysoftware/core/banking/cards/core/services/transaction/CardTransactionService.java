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


package io.fireflysoftware.core.banking.cards.core.services.transaction;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.transaction.CardTransactionDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardTransactionService {

    /**
     * Retrieve a paginated list of transactions for a specific card.
     */
    Mono<PaginationResponse<CardTransactionDTO>> listTransactions(UUID cardId, PaginationRequest paginationRequest);

    /**
     * Create a new transaction for a specific card.
     */
    Mono<CardTransactionDTO> createTransaction(UUID cardId, CardTransactionDTO transactionDTO);

    /**
     * Retrieve a specific transaction by its unique ID, ensuring it belongs to the given card.
     */
    Mono<CardTransactionDTO> getTransaction(UUID cardId, UUID transactionId);

    /**
     * Filters card transactions based on the given filter request and returns a paginated response.
     */
    Mono<PaginationResponse<CardTransactionDTO>> findFiltered(FilterRequest<CardTransactionDTO> request);

    /**
     * Update an existing transaction record for a specific card.
     */
    Mono<CardTransactionDTO> updateTransaction(UUID cardId, UUID transactionId, CardTransactionDTO transactionDTO);

    /**
     * Delete a transaction by its unique ID, ensuring it belongs to the specified card.
     */
    Mono<Void> deleteTransaction(UUID cardId, UUID transactionId);
}