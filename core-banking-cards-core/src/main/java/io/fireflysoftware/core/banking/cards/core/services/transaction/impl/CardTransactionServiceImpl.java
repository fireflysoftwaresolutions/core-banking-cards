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


package io.fireflysoftware.core.banking.cards.core.services.transaction.impl;

import io.fireflysoftware.core.banking.cards.core.services.transaction.CardTransactionService;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.transaction.CardTransactionMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.transaction.CardTransactionDTO;
import io.fireflysoftware.core.banking.cards.models.entities.transaction.CardTransaction;
import io.fireflysoftware.core.banking.cards.models.repositories.transaction.CardTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardTransactionServiceImpl implements CardTransactionService {

    @Autowired
    private CardTransactionRepository repository;

    @Autowired
    private CardTransactionMapper mapper;

    @Override
    public Mono<PaginationResponse<CardTransactionDTO>> listTransactions(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<CardTransactionDTO> createTransaction(UUID cardId, CardTransactionDTO transactionDTO) {
        transactionDTO.setCardId(cardId);
        return repository.save(mapper.toEntity(transactionDTO))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardTransactionDTO> getTransaction(UUID cardId, UUID transactionId) {
        return repository.findByCardTransactionId(transactionId)
                .filter(transaction -> cardId.equals(transaction.getCardId()))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<CardTransactionDTO>> findFiltered(FilterRequest<CardTransactionDTO> request) {
        return FilterUtils
                .createFilter(
                        CardTransaction.class,
                        mapper::toDTO
                )
                .filter(request);
    }

    @Override
    public Mono<CardTransactionDTO> updateTransaction(UUID cardId, UUID transactionId, CardTransactionDTO transactionDTO) {
        return repository.findByCardTransactionId(transactionId)
                .filter(transaction -> cardId.equals(transaction.getCardId()))
                .flatMap(existingTransaction -> {
                    CardTransaction updatedEntity = mapper.toEntity(transactionDTO);
                    updatedEntity.setCardTransactionId(existingTransaction.getCardTransactionId());
                    updatedEntity.setCardId(existingTransaction.getCardId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteTransaction(UUID cardId, UUID transactionId) {
        return repository.findByCardTransactionId(transactionId)
                .filter(transaction -> cardId.equals(transaction.getCardId()))
                .flatMap(repository::delete);
    }
}
