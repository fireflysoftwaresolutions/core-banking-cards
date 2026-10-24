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


package io.fireflysoftware.core.banking.cards.core.services.enrollment.impl;

import io.fireflysoftware.core.banking.cards.core.services.enrollment.CardEnrollmentService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.enrollment.CardEnrollmentMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.enrollment.CardEnrollmentDTO;
import io.fireflysoftware.core.banking.cards.models.entities.enrollment.CardEnrollment;
import io.fireflysoftware.core.banking.cards.models.repositories.enrollment.CardEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardEnrollmentServiceImpl implements CardEnrollmentService {

    @Autowired
    private CardEnrollmentRepository repository;

    @Autowired
    private CardEnrollmentMapper mapper;

    @Override
    public Mono<PaginationResponse<CardEnrollmentDTO>> listEnrollments(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<CardEnrollmentDTO> createEnrollment(UUID cardId, CardEnrollmentDTO enrollmentDTO) {
        enrollmentDTO.setCardId(cardId);
        CardEnrollment entity = mapper.toEntity(enrollmentDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardEnrollmentDTO> getEnrollment(UUID cardId, UUID enrollmentId) {
        return repository.findByEnrollmentId(enrollmentId)
                .flatMap(entity -> {
                    if (!entity.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardEnrollment not found for the specified card."));
                    }
                    return Mono.just(mapper.toDTO(entity));
                });
    }

    @Override
    public Mono<CardEnrollmentDTO> updateEnrollment(UUID cardId, UUID enrollmentId, CardEnrollmentDTO enrollmentDTO) {
        return repository.findByEnrollmentId(enrollmentId)
                .flatMap(existingEnrollment -> {
                    if (!existingEnrollment.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardEnrollment not found for the specified card."));
                    }
                    
                    CardEnrollment updatedEnrollment = mapper.toEntity(enrollmentDTO);
                    updatedEnrollment.setEnrollmentId(existingEnrollment.getEnrollmentId());
                    updatedEnrollment.setCardId(cardId);
                    
                    return repository.save(updatedEnrollment);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteEnrollment(UUID cardId, UUID enrollmentId) {
        return repository.findByEnrollmentId(enrollmentId)
                .flatMap(enrollment -> {
                    if (!enrollment.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardEnrollment not found for the specified card."));
                    }
                    return repository.delete(enrollment);
                });
    }
}
