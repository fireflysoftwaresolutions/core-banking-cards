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


package io.fireflysoftware.core.banking.cards.core.services.interest;

import io.fireflysoftware.core.banking.cards.core.services.interest.impl.CardInterestServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.interest.CardInterestMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.interest.CardInterestDTO;
import io.fireflysoftware.core.banking.cards.models.entities.interest.CardInterest;
import io.fireflysoftware.core.banking.cards.models.repositories.interest.CardInterestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardInterestServiceTest {

    @Mock
    private CardInterestRepository repository;

    @Mock
    private CardInterestMapper mapper;

    @InjectMocks
    private CardInterestServiceImpl service;

    private CardInterestDTO interestDTO;
    private CardInterest interestEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID interestId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        interestDTO = CardInterestDTO.builder()
                .interestId(interestId)
                .cardId(cardId)
                .interestType("PURCHASE")
                .interestRate(new BigDecimal("18.99"))
                .calculationMethod("DAILY")
                .accrualStartDate(now)
                .build();

        interestEntity = new CardInterest();
        interestEntity.setInterestId(interestId);
        interestEntity.setCardId(cardId);
        interestEntity.setInterestType("PURCHASE");
        interestEntity.setInterestRate(new BigDecimal("18.99"));
        interestEntity.setCalculationMethod("DAILY");
        interestEntity.setAccrualStartDate(now);
    }

    @Test
    void listInterests_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardInterestDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listInterests(cardId, paginationRequest))
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
    void createInterest_Success() {
        // Arrange
        when(mapper.toEntity(any(CardInterestDTO.class))).thenReturn(interestEntity);
        when(repository.save(any(CardInterest.class))).thenReturn(Mono.just(interestEntity));
        when(mapper.toDTO(any(CardInterest.class))).thenReturn(interestDTO);

        // Act & Assert
        StepVerifier.create(service.createInterest(cardId, interestDTO))
                .expectNext(interestDTO)
                .verifyComplete();

        verify(mapper).toEntity(interestDTO);
        verify(repository).save(interestEntity);
        verify(mapper).toDTO(interestEntity);
    }

    @Test
    void getInterest_Success() {
        // Arrange
        when(repository.findByInterestId(interestId)).thenReturn(Mono.just(interestEntity));
        when(mapper.toDTO(any(CardInterest.class))).thenReturn(interestDTO);

        // Act & Assert
        StepVerifier.create(service.getInterest(cardId, interestId))
                .expectNext(interestDTO)
                .verifyComplete();

        verify(repository).findByInterestId(interestId);
        verify(mapper).toDTO(interestEntity);
    }

    @Test
    void getInterest_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByInterestId(interestId)).thenReturn(Mono.just(interestEntity));

        // Act & Assert
        StepVerifier.create(service.getInterest(wrongCardId, interestId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByInterestId(interestId);
        verify(mapper, never()).toDTO(any(CardInterest.class));
    }

    @Test
    void updateInterest_Success() {
        // Arrange
        when(repository.findByInterestId(interestId)).thenReturn(Mono.just(interestEntity));
        when(mapper.toEntity(any(CardInterestDTO.class))).thenReturn(interestEntity);
        when(repository.save(any(CardInterest.class))).thenReturn(Mono.just(interestEntity));
        when(mapper.toDTO(any(CardInterest.class))).thenReturn(interestDTO);

        // Act & Assert
        StepVerifier.create(service.updateInterest(cardId, interestId, interestDTO))
                .expectNext(interestDTO)
                .verifyComplete();

        verify(repository).findByInterestId(interestId);
        verify(mapper).toEntity(interestDTO);
        verify(repository).save(interestEntity);
        verify(mapper).toDTO(interestEntity);
    }

    @Test
    void updateInterest_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByInterestId(interestId)).thenReturn(Mono.just(interestEntity));

        // Act & Assert
        StepVerifier.create(service.updateInterest(wrongCardId, interestId, interestDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByInterestId(interestId);
        verify(repository, never()).save(any(CardInterest.class));
        verify(mapper, never()).toDTO(any(CardInterest.class));
    }

    @Test
    void deleteInterest_Success() {
        // Arrange
        when(repository.findByInterestId(interestId)).thenReturn(Mono.just(interestEntity));
        when(repository.delete(interestEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteInterest(cardId, interestId))
                .verifyComplete();

        verify(repository).findByInterestId(interestId);
        verify(repository).delete(interestEntity);
    }

    @Test
    void deleteInterest_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByInterestId(interestId)).thenReturn(Mono.just(interestEntity));

        // Act & Assert
        StepVerifier.create(service.deleteInterest(wrongCardId, interestId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByInterestId(interestId);
        verify(repository, never()).delete(any(CardInterest.class));
    }
}
