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


package io.fireflysoftware.core.banking.cards.core.services.payment.impl;

import io.fireflysoftware.core.banking.cards.core.services.payment.CardPaymentService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.payment.CardPaymentMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.payment.CardPaymentDTO;
import io.fireflysoftware.core.banking.cards.models.entities.payment.CardPayment;
import io.fireflysoftware.core.banking.cards.models.repositories.payment.CardPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardPaymentServiceImpl implements CardPaymentService {

    @Autowired
    private CardPaymentRepository repository;

    @Autowired
    private CardPaymentMapper mapper;

    @Override
    public Mono<PaginationResponse<CardPaymentDTO>> listPayments(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<CardPaymentDTO> createPayment(UUID cardId, CardPaymentDTO paymentDTO) {
        paymentDTO.setCardId(cardId);
        CardPayment entity = mapper.toEntity(paymentDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardPaymentDTO> getPayment(UUID cardId, UUID paymentId) {
        return repository.findByPaymentId(paymentId)
                .flatMap(entity -> {
                    if (!entity.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardPayment not found for the specified card."));
                    }
                    return Mono.just(mapper.toDTO(entity));
                });
    }

    @Override
    public Mono<CardPaymentDTO> updatePayment(UUID cardId, UUID paymentId, CardPaymentDTO paymentDTO) {
        return repository.findByPaymentId(paymentId)
                .flatMap(existingPayment -> {
                    if (!existingPayment.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardPayment not found for the specified card."));
                    }
                    
                    CardPayment updatedPayment = mapper.toEntity(paymentDTO);
                    updatedPayment.setPaymentId(existingPayment.getPaymentId());
                    updatedPayment.setCardId(cardId);
                    
                    return repository.save(updatedPayment);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePayment(UUID cardId, UUID paymentId) {
        return repository.findByPaymentId(paymentId)
                .flatMap(payment -> {
                    if (!payment.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardPayment not found for the specified card."));
                    }
                    return repository.delete(payment);
                });
    }
}
