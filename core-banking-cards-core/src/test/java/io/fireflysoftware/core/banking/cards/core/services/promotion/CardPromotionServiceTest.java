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


package io.fireflysoftware.core.banking.cards.core.services.promotion;

import io.fireflysoftware.core.banking.cards.core.services.promotion.impl.CardPromotionServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.promotion.CardPromotionMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.promotion.CardPromotionDTO;
import io.fireflysoftware.core.banking.cards.models.entities.promotion.CardPromotion;
import io.fireflysoftware.core.banking.cards.models.repositories.promotion.CardPromotionRepository;
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
public class CardPromotionServiceTest {

    @Mock
    private CardPromotionRepository repository;

    @Mock
    private CardPromotionMapper mapper;

    @InjectMocks
    private CardPromotionServiceImpl service;

    private CardPromotionDTO promotionDTO;
    private CardPromotion promotionEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID promotionId = UUID.randomUUID();
    private final UUID programId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        promotionDTO = CardPromotionDTO.builder()
                .promotionId(promotionId)
                .cardId(cardId)
                .programId(programId)
                .promotionName("Cashback Offer")
                .promotionCode("CASH10")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(3))
                .build();

        promotionEntity = new CardPromotion();
        promotionEntity.setPromotionId(promotionId);
        promotionEntity.setCardId(cardId);
        promotionEntity.setProgramId(programId);
        promotionEntity.setPromotionName("Cashback Offer");
        promotionEntity.setPromotionCode("CASH10");
        promotionEntity.setStartDate(LocalDateTime.now());
        promotionEntity.setEndDate(LocalDateTime.now().plusMonths(3));
    }

    @Test
    void listPromotions_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardPromotionDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listPromotions(cardId, paginationRequest))
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
    void createPromotion_Success() {
        // Arrange
        when(mapper.toEntity(any(CardPromotionDTO.class))).thenReturn(promotionEntity);
        when(repository.save(any(CardPromotion.class))).thenReturn(Mono.just(promotionEntity));
        when(mapper.toDTO(any(CardPromotion.class))).thenReturn(promotionDTO);

        // Act & Assert
        StepVerifier.create(service.createPromotion(cardId, promotionDTO))
                .expectNext(promotionDTO)
                .verifyComplete();

        verify(mapper).toEntity(promotionDTO);
        verify(repository).save(promotionEntity);
        verify(mapper).toDTO(promotionEntity);
    }

    @Test
    void getPromotion_Success() {
        // Arrange
        when(repository.findByPromotionId(promotionId)).thenReturn(Mono.just(promotionEntity));
        when(mapper.toDTO(any(CardPromotion.class))).thenReturn(promotionDTO);

        // Act & Assert
        StepVerifier.create(service.getPromotion(cardId, promotionId))
                .expectNext(promotionDTO)
                .verifyComplete();

        verify(repository).findByPromotionId(promotionId);
        verify(mapper).toDTO(promotionEntity);
    }

    @Test
    void getPromotion_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByPromotionId(promotionId)).thenReturn(Mono.just(promotionEntity));

        // Act & Assert
        StepVerifier.create(service.getPromotion(wrongCardId, promotionId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByPromotionId(promotionId);
        verify(mapper, never()).toDTO(any(CardPromotion.class));
    }

    @Test
    void updatePromotion_Success() {
        // Arrange
        when(repository.findByPromotionId(promotionId)).thenReturn(Mono.just(promotionEntity));
        when(mapper.toEntity(any(CardPromotionDTO.class))).thenReturn(promotionEntity);
        when(repository.save(any(CardPromotion.class))).thenReturn(Mono.just(promotionEntity));
        when(mapper.toDTO(any(CardPromotion.class))).thenReturn(promotionDTO);

        // Act & Assert
        StepVerifier.create(service.updatePromotion(cardId, promotionId, promotionDTO))
                .expectNext(promotionDTO)
                .verifyComplete();

        verify(repository).findByPromotionId(promotionId);
        verify(mapper).toEntity(promotionDTO);
        verify(repository).save(promotionEntity);
        verify(mapper).toDTO(promotionEntity);
    }

    @Test
    void updatePromotion_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByPromotionId(promotionId)).thenReturn(Mono.just(promotionEntity));

        // Act & Assert
        StepVerifier.create(service.updatePromotion(wrongCardId, promotionId, promotionDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByPromotionId(promotionId);
        verify(repository, never()).save(any(CardPromotion.class));
        verify(mapper, never()).toDTO(any(CardPromotion.class));
    }

    @Test
    void deletePromotion_Success() {
        // Arrange
        when(repository.findByPromotionId(promotionId)).thenReturn(Mono.just(promotionEntity));
        when(repository.delete(promotionEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deletePromotion(cardId, promotionId))
                .verifyComplete();

        verify(repository).findByPromotionId(promotionId);
        verify(repository).delete(promotionEntity);
    }

    @Test
    void deletePromotion_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByPromotionId(promotionId)).thenReturn(Mono.just(promotionEntity));

        // Act & Assert
        StepVerifier.create(service.deletePromotion(wrongCardId, promotionId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByPromotionId(promotionId);
        verify(repository, never()).delete(any(CardPromotion.class));
    }
}
