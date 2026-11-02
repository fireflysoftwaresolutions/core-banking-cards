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


package io.fireflysoftware.core.banking.cards.core.services.limit;

import io.fireflysoftware.core.banking.cards.core.services.limit.impl.CardLimitServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.limit.CardLimitMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.limit.CardLimitDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.limit.LimitTypeEnum;
import io.fireflysoftware.core.banking.cards.interfaces.enums.limit.ResetPeriodEnum;
import io.fireflysoftware.core.banking.cards.models.entities.limit.CardLimit;
import io.fireflysoftware.core.banking.cards.models.repositories.limit.CardLimitRepository;
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
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardLimitServiceTest {

    @Mock
    private CardLimitRepository repository;

    @Mock
    private CardLimitMapper mapper;

    @InjectMocks
    private CardLimitServiceImpl service;

    private CardLimitDTO limitDTO;
    private CardLimit limitEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID limitId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        limitDTO = CardLimitDTO.builder()
                .cardLimitId(limitId)
                .cardId(cardId)
                .limitType(LimitTypeEnum.DAILY_SPENDING)
                .limitAmount(new BigDecimal("1000.00"))
                .currentUsage(new BigDecimal("500.00"))
                .resetPeriod(ResetPeriodEnum.DAILY)
                .build();

        limitEntity = new CardLimit();
        limitEntity.setCardLimitId(limitId);
        limitEntity.setCardId(cardId);
        limitEntity.setLimitType(LimitTypeEnum.DAILY_SPENDING.name());
        limitEntity.setLimitAmount(new BigDecimal("1000.00"));
        limitEntity.setCurrentUsage(new BigDecimal("500.00"));
        limitEntity.setResetPeriod(ResetPeriodEnum.DAILY.name());
    }

    @Test
    void listLimits_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardLimitDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listLimits(cardId, paginationRequest))
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
    void createLimit_Success() {
        // Arrange
        when(mapper.toEntity(any(CardLimitDTO.class))).thenReturn(limitEntity);
        when(repository.save(any(CardLimit.class))).thenReturn(Mono.just(limitEntity));
        when(mapper.toDTO(any(CardLimit.class))).thenReturn(limitDTO);

        // Act & Assert
        StepVerifier.create(service.createLimit(cardId, limitDTO))
                .expectNext(limitDTO)
                .verifyComplete();

        verify(mapper).toEntity(limitDTO);
        verify(repository).save(limitEntity);
        verify(mapper).toDTO(limitEntity);
    }

    @Test
    void getLimit_Success() {
        // Arrange
        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.just(limitEntity));
        when(mapper.toDTO(any(CardLimit.class))).thenReturn(limitDTO);

        // Act & Assert
        StepVerifier.create(service.getLimit(cardId, limitId))
                .expectNext(limitDTO)
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(mapper).toDTO(limitEntity);
    }

    @Test
    void getLimit_NotFound() {
        // Arrange
        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getLimit(cardId, limitId))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(mapper, never()).toDTO(any(CardLimit.class));
    }

    @Test
    void getLimit_WrongCard() {
        // Arrange
        CardLimit wrongCardLimit = new CardLimit();
        wrongCardLimit.setCardLimitId(limitId);
        wrongCardLimit.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.just(wrongCardLimit));

        // Act & Assert
        StepVerifier.create(service.getLimit(cardId, limitId))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(mapper, never()).toDTO(any(CardLimit.class));
    }

    @Test
    void updateLimit_Success() {
        // Arrange
        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.just(limitEntity));
        when(repository.save(any(CardLimit.class))).thenReturn(Mono.just(limitEntity));
        when(mapper.toDTO(any(CardLimit.class))).thenReturn(limitDTO);

        // Act & Assert
        StepVerifier.create(service.updateLimit(cardId, limitId, limitDTO))
                .expectNext(limitDTO)
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(repository).save(limitEntity);
        verify(mapper).toDTO(limitEntity);
    }

    @Test
    void updateLimit_NotFound() {
        // Arrange
        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateLimit(cardId, limitId, limitDTO))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(repository, never()).save(any(CardLimit.class));
        verify(mapper, never()).toDTO(any(CardLimit.class));
    }

    @Test
    void updateLimit_WrongCard() {
        // Arrange
        CardLimit wrongCardLimit = new CardLimit();
        wrongCardLimit.setCardLimitId(limitId);
        wrongCardLimit.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.just(wrongCardLimit));

        // Act & Assert
        StepVerifier.create(service.updateLimit(cardId, limitId, limitDTO))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(repository, never()).save(any(CardLimit.class));
        verify(mapper, never()).toDTO(any(CardLimit.class));
    }

    @Test
    void deleteLimit_Success() {
        // Arrange
        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.just(limitEntity));
        when(repository.delete(limitEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteLimit(cardId, limitId))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(repository).delete(limitEntity);
    }

    @Test
    void deleteLimit_NotFound() {
        // Arrange
        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteLimit(cardId, limitId))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(repository, never()).delete(any(CardLimit.class));
    }

    @Test
    void deleteLimit_WrongCard() {
        // Arrange
        CardLimit wrongCardLimit = new CardLimit();
        wrongCardLimit.setCardLimitId(limitId);
        wrongCardLimit.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByCardLimitId(limitId)).thenReturn(Mono.just(wrongCardLimit));

        // Act & Assert
        StepVerifier.create(service.deleteLimit(cardId, limitId))
                .verifyComplete();

        verify(repository).findByCardLimitId(limitId);
        verify(repository, never()).delete(any(CardLimit.class));
    }
}
