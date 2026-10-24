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

import io.fireflysoftware.core.banking.cards.core.services.balance.impl.CardBalanceServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.balance.CardBalanceMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.balance.CardBalanceDTO;
import io.fireflysoftware.core.banking.cards.models.entities.balance.CardBalance;
import io.fireflysoftware.core.banking.cards.models.repositories.balance.CardBalanceRepository;
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
public class CardBalanceServiceTest {

    @Mock
    private CardBalanceRepository repository;

    @Mock
    private CardBalanceMapper mapper;

    @InjectMocks
    private CardBalanceServiceImpl service;

    private CardBalanceDTO balanceDTO;
    private CardBalance balanceEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID balanceId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        balanceDTO = CardBalanceDTO.builder()
                .balanceId(balanceId)
                .cardId(cardId)
                .balanceType("AVAILABLE")
                .balanceAmount(new BigDecimal("1000.00"))
                .currencyCode("USD")
                .asOfDate(now)
                .build();

        balanceEntity = new CardBalance();
        balanceEntity.setBalanceId(balanceId);
        balanceEntity.setCardId(cardId);
        balanceEntity.setBalanceType("AVAILABLE");
        balanceEntity.setBalanceAmount(new BigDecimal("1000.00"));
        balanceEntity.setCurrencyCode("USD");
        balanceEntity.setAsOfDate(now);
    }

    @Test
    void listBalances_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardBalanceDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listBalances(cardId, paginationRequest))
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
    void createBalance_Success() {
        // Arrange
        when(mapper.toEntity(any(CardBalanceDTO.class))).thenReturn(balanceEntity);
        when(repository.save(any(CardBalance.class))).thenReturn(Mono.just(balanceEntity));
        when(mapper.toDTO(any(CardBalance.class))).thenReturn(balanceDTO);

        // Act & Assert
        StepVerifier.create(service.createBalance(cardId, balanceDTO))
                .expectNext(balanceDTO)
                .verifyComplete();

        verify(mapper).toEntity(balanceDTO);
        verify(repository).save(balanceEntity);
        verify(mapper).toDTO(balanceEntity);
    }

    @Test
    void getBalance_Success() {
        // Arrange
        when(repository.findByBalanceId(balanceId)).thenReturn(Mono.just(balanceEntity));
        when(mapper.toDTO(any(CardBalance.class))).thenReturn(balanceDTO);

        // Act & Assert
        StepVerifier.create(service.getBalance(cardId, balanceId))
                .expectNext(balanceDTO)
                .verifyComplete();

        verify(repository).findByBalanceId(balanceId);
        verify(mapper).toDTO(balanceEntity);
    }

    @Test
    void getBalance_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByBalanceId(balanceId)).thenReturn(Mono.just(balanceEntity));

        // Act & Assert
        StepVerifier.create(service.getBalance(wrongCardId, balanceId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByBalanceId(balanceId);
        verify(mapper, never()).toDTO(any(CardBalance.class));
    }

    @Test
    void updateBalance_Success() {
        // Arrange
        when(repository.findByBalanceId(balanceId)).thenReturn(Mono.just(balanceEntity));
        when(mapper.toEntity(any(CardBalanceDTO.class))).thenReturn(balanceEntity);
        when(repository.save(any(CardBalance.class))).thenReturn(Mono.just(balanceEntity));
        when(mapper.toDTO(any(CardBalance.class))).thenReturn(balanceDTO);

        // Act & Assert
        StepVerifier.create(service.updateBalance(cardId, balanceId, balanceDTO))
                .expectNext(balanceDTO)
                .verifyComplete();

        verify(repository).findByBalanceId(balanceId);
        verify(mapper).toEntity(balanceDTO);
        verify(repository).save(balanceEntity);
        verify(mapper).toDTO(balanceEntity);
    }

    @Test
    void updateBalance_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByBalanceId(balanceId)).thenReturn(Mono.just(balanceEntity));

        // Act & Assert
        StepVerifier.create(service.updateBalance(wrongCardId, balanceId, balanceDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByBalanceId(balanceId);
        verify(repository, never()).save(any(CardBalance.class));
        verify(mapper, never()).toDTO(any(CardBalance.class));
    }

    @Test
    void deleteBalance_Success() {
        // Arrange
        when(repository.findByBalanceId(balanceId)).thenReturn(Mono.just(balanceEntity));
        when(repository.delete(balanceEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteBalance(cardId, balanceId))
                .verifyComplete();

        verify(repository).findByBalanceId(balanceId);
        verify(repository).delete(balanceEntity);
    }

    @Test
    void deleteBalance_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByBalanceId(balanceId)).thenReturn(Mono.just(balanceEntity));

        // Act & Assert
        StepVerifier.create(service.deleteBalance(wrongCardId, balanceId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByBalanceId(balanceId);
        verify(repository, never()).delete(any(CardBalance.class));
    }
}
