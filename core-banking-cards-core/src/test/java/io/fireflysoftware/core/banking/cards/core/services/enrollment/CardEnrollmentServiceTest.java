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


package io.fireflysoftware.core.banking.cards.core.services.enrollment;

import io.fireflysoftware.core.banking.cards.core.services.enrollment.impl.CardEnrollmentServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.enrollment.CardEnrollmentMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.enrollment.CardEnrollmentDTO;
import io.fireflysoftware.core.banking.cards.models.entities.enrollment.CardEnrollment;
import io.fireflysoftware.core.banking.cards.models.repositories.enrollment.CardEnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardEnrollmentServiceTest {

    @Mock
    private CardEnrollmentRepository repository;

    @Mock
    private CardEnrollmentMapper mapper;

    @InjectMocks
    private CardEnrollmentServiceImpl service;

    private CardEnrollmentDTO enrollmentDTO;
    private CardEnrollment enrollmentEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID enrollmentId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        enrollmentDTO = CardEnrollmentDTO.builder()
                .enrollmentId(enrollmentId)
                .cardId(cardId)
                .featureName("Rewards Program")
                .enrollmentTimestamp(now)
                .enrollmentStatus("ACTIVE")
                .build();

        enrollmentEntity = new CardEnrollment();
        enrollmentEntity.setEnrollmentId(enrollmentId);
        enrollmentEntity.setCardId(cardId);
        enrollmentEntity.setFeatureName("Rewards Program");
        enrollmentEntity.setEnrollmentTimestamp(now);
        enrollmentEntity.setEnrollmentStatus("ACTIVE");
    }

    @Test
    void listEnrollments_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardEnrollmentDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listEnrollments(cardId, paginationRequest))
                    .expectNext(expectedResponse)
                    .verifyComplete();

            paginationUtilsMocked.verify(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            ));
        }
    }

    @Test
    void createEnrollment_Success() {
        // Arrange
        when(mapper.toEntity(any(CardEnrollmentDTO.class))).thenReturn(enrollmentEntity);
        when(repository.save(any(CardEnrollment.class))).thenReturn(Mono.just(enrollmentEntity));
        when(mapper.toDTO(any(CardEnrollment.class))).thenReturn(enrollmentDTO);

        // Act & Assert
        StepVerifier.create(service.createEnrollment(cardId, enrollmentDTO))
                .expectNext(enrollmentDTO)
                .verifyComplete();

        verify(mapper).toEntity(enrollmentDTO);
        verify(repository).save(enrollmentEntity);
        verify(mapper).toDTO(enrollmentEntity);
    }

    @Test
    void getEnrollment_Success() {
        // Arrange
        when(repository.findByEnrollmentId(enrollmentId)).thenReturn(Mono.just(enrollmentEntity));
        when(mapper.toDTO(any(CardEnrollment.class))).thenReturn(enrollmentDTO);

        // Act & Assert
        StepVerifier.create(service.getEnrollment(cardId, enrollmentId))
                .expectNext(enrollmentDTO)
                .verifyComplete();

        verify(repository).findByEnrollmentId(enrollmentId);
        verify(mapper).toDTO(enrollmentEntity);
    }

    @Test
    void getEnrollment_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByEnrollmentId(enrollmentId)).thenReturn(Mono.just(enrollmentEntity));

        // Act & Assert
        StepVerifier.create(service.getEnrollment(wrongCardId, enrollmentId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByEnrollmentId(enrollmentId);
        verify(mapper, never()).toDTO(any(CardEnrollment.class));
    }

    @Test
    void updateEnrollment_Success() {
        // Arrange
        when(repository.findByEnrollmentId(enrollmentId)).thenReturn(Mono.just(enrollmentEntity));
        when(mapper.toEntity(any(CardEnrollmentDTO.class))).thenReturn(enrollmentEntity);
        when(repository.save(any(CardEnrollment.class))).thenReturn(Mono.just(enrollmentEntity));
        when(mapper.toDTO(any(CardEnrollment.class))).thenReturn(enrollmentDTO);

        // Act & Assert
        StepVerifier.create(service.updateEnrollment(cardId, enrollmentId, enrollmentDTO))
                .expectNext(enrollmentDTO)
                .verifyComplete();

        verify(repository).findByEnrollmentId(enrollmentId);
        verify(mapper).toEntity(enrollmentDTO);
        verify(repository).save(enrollmentEntity);
        verify(mapper).toDTO(enrollmentEntity);
    }

    @Test
    void updateEnrollment_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByEnrollmentId(enrollmentId)).thenReturn(Mono.just(enrollmentEntity));

        // Act & Assert
        StepVerifier.create(service.updateEnrollment(wrongCardId, enrollmentId, enrollmentDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByEnrollmentId(enrollmentId);
        verify(repository, never()).save(any(CardEnrollment.class));
        verify(mapper, never()).toDTO(any(CardEnrollment.class));
    }

    @Test
    void deleteEnrollment_Success() {
        // Arrange
        when(repository.findByEnrollmentId(enrollmentId)).thenReturn(Mono.just(enrollmentEntity));
        when(repository.delete(enrollmentEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteEnrollment(cardId, enrollmentId))
                .verifyComplete();

        verify(repository).findByEnrollmentId(enrollmentId);
        verify(repository).delete(enrollmentEntity);
    }

    @Test
    void deleteEnrollment_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByEnrollmentId(enrollmentId)).thenReturn(Mono.just(enrollmentEntity));

        // Act & Assert
        StepVerifier.create(service.deleteEnrollment(wrongCardId, enrollmentId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByEnrollmentId(enrollmentId);
        verify(repository, never()).delete(any(CardEnrollment.class));
    }
}
