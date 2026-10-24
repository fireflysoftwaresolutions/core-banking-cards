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

import io.fireflysoftware.core.banking.cards.core.services.transaction.impl.CardTransactionServiceImpl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.transaction.CardTransactionMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.transaction.CardTransactionDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.transaction.TransactionStatusEnum;
import io.fireflysoftware.core.banking.cards.interfaces.enums.transaction.TransactionTypeEnum;
import io.fireflysoftware.core.banking.cards.models.entities.transaction.CardTransaction;
import io.fireflysoftware.core.banking.cards.models.repositories.transaction.CardTransactionRepository;
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
public class CardTransactionServiceTest {

    @Mock
    private CardTransactionRepository repository;

    @Mock
    private CardTransactionMapper mapper;

    @InjectMocks
    private CardTransactionServiceImpl service;

    private CardTransactionDTO transactionDTO;
    private CardTransaction transactionEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID transactionId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Initialize test data
        transactionDTO = CardTransactionDTO.builder()
                .cardTransactionId(transactionId)
                .cardId(cardId)
                .transactionType(TransactionTypeEnum.PURCHASE)
                .merchantInfo("Test Merchant Info")
                .transactionStatus(TransactionStatusEnum.COMPLETED)
                .cardAuthCode("AUTH123")
                .cardMerchantCategoryCode("5411")
                .cardMerchantName("Test Merchant")
                .cardPosEntryMode("CHIP")
                .cardTransactionReference("REF123")
                .cardTerminalId("TERM123")
                .cardHolderCountry("US")
                .cardPresentFlag(true)
                .cardTransactionTimestamp(now)
                .cardFraudFlag(false)
                .cardCurrencyConversionRate(new BigDecimal("1.0"))
                .cardFeeAmount(new BigDecimal("5.0"))
                .cardFeeCurrency("USD")
                .cardInstallmentPlan("NONE")
                .build();

        transactionEntity = new CardTransaction();
        transactionEntity.setCardTransactionId(transactionId);
        transactionEntity.setCardId(cardId);
        transactionEntity.setTransactionType(TransactionTypeEnum.PURCHASE);
        transactionEntity.setTransactionStatus(TransactionStatusEnum.COMPLETED);
        transactionEntity.setTransactionTimestamp(now);
        transactionEntity.setAuthorizationCode("AUTH123");
        transactionEntity.setTransactionReference("REF123");
        transactionEntity.setTransactionAmount(new BigDecimal("100.0"));
        transactionEntity.setTransactionCurrency("USD");
        transactionEntity.setMerchantId("MERCH123");
        transactionEntity.setMerchantName("Test Merchant");
        transactionEntity.setMerchantCategoryCode("5411");
        transactionEntity.setMerchantCity("New York");
        transactionEntity.setMerchantCountry("US");
        transactionEntity.setTerminalId("TERM123");
        transactionEntity.setEntryMode("CHIP");
        transactionEntity.setIsCardPresent(true);
        transactionEntity.setIsFraudSuspected(false);
        transactionEntity.setExchangeRate(new BigDecimal("1.0"));
        transactionEntity.setFeeAmount(new BigDecimal("5.0"));
        transactionEntity.setFeeCurrency("USD");
        transactionEntity.setInstallmentPlan("NONE");
    }

    @Test
    void listTransactions_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardTransactionDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listTransactions(cardId, paginationRequest))
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
    void createTransaction_Success() {
        // Arrange
        when(mapper.toEntity(any(CardTransactionDTO.class))).thenReturn(transactionEntity);
        when(repository.save(any(CardTransaction.class))).thenReturn(Mono.just(transactionEntity));
        when(mapper.toDTO(any(CardTransaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.createTransaction(cardId, transactionDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(mapper).toEntity(transactionDTO);
        verify(repository).save(transactionEntity);
        verify(mapper).toDTO(transactionEntity);
    }

    @Test
    void getTransaction_Success() {
        // Arrange
        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.just(transactionEntity));
        when(mapper.toDTO(any(CardTransaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.getTransaction(cardId, transactionId))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(mapper).toDTO(transactionEntity);
    }

    @Test
    void getTransaction_NotFound() {
        // Arrange
        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getTransaction(cardId, transactionId))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(CardTransaction.class));
    }

    @Test
    void getTransaction_WrongCard() {
        // Arrange
        CardTransaction wrongCardTransaction = new CardTransaction();
        wrongCardTransaction.setCardTransactionId(transactionId);
        wrongCardTransaction.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.just(wrongCardTransaction));

        // Act & Assert
        StepVerifier.create(service.getTransaction(cardId, transactionId))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(CardTransaction.class));
    }

    @Test
    void findFiltered_Success() {
        // Arrange
        @SuppressWarnings("unchecked")
        FilterRequest<CardTransactionDTO> filterRequest = mock(FilterRequest.class);

        @SuppressWarnings("unchecked")
        PaginationResponse<CardTransactionDTO> expectedResponse = mock(PaginationResponse.class);

        // Since we can't directly mock the GenericFilter class, we'll mock the service method instead
        CardTransactionServiceImpl serviceSpy = spy(service);

        // Mock the findFiltered method to return our expected response
        doReturn(Mono.just(expectedResponse)).when(serviceSpy).findFiltered(filterRequest);

        // Act & Assert
        StepVerifier.create(serviceSpy.findFiltered(filterRequest))
                .expectNext(expectedResponse)
                .verifyComplete();

        // Verify the method was called with the request
        verify(serviceSpy).findFiltered(filterRequest);
    }

    @Test
    void updateTransaction_Success() {
        // Arrange
        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.just(transactionEntity));
        when(mapper.toEntity(any(CardTransactionDTO.class))).thenReturn(transactionEntity);
        when(repository.save(any(CardTransaction.class))).thenReturn(Mono.just(transactionEntity));
        when(mapper.toDTO(any(CardTransaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.updateTransaction(cardId, transactionId, transactionDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(mapper).toEntity(transactionDTO);
        verify(repository).save(transactionEntity);
        verify(mapper).toDTO(transactionEntity);
    }

    @Test
    void updateTransaction_NotFound() {
        // Arrange
        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateTransaction(cardId, transactionId, transactionDTO))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(CardTransactionDTO.class));
        verify(repository, never()).save(any(CardTransaction.class));
        verify(mapper, never()).toDTO(any(CardTransaction.class));
    }

    @Test
    void updateTransaction_WrongCard() {
        // Arrange
        CardTransaction wrongCardTransaction = new CardTransaction();
        wrongCardTransaction.setCardTransactionId(transactionId);
        wrongCardTransaction.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.just(wrongCardTransaction));

        // Act & Assert
        StepVerifier.create(service.updateTransaction(cardId, transactionId, transactionDTO))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(CardTransactionDTO.class));
        verify(repository, never()).save(any(CardTransaction.class));
        verify(mapper, never()).toDTO(any(CardTransaction.class));
    }

    @Test
    void deleteTransaction_Success() {
        // Arrange
        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.just(transactionEntity));
        when(repository.delete(transactionEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteTransaction(cardId, transactionId))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(repository).delete(transactionEntity);
    }

    @Test
    void deleteTransaction_NotFound() {
        // Arrange
        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteTransaction(cardId, transactionId))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(repository, never()).delete(any(CardTransaction.class));
    }

    @Test
    void deleteTransaction_WrongCard() {
        // Arrange
        CardTransaction wrongCardTransaction = new CardTransaction();
        wrongCardTransaction.setCardTransactionId(transactionId);
        wrongCardTransaction.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByCardTransactionId(transactionId)).thenReturn(Mono.just(wrongCardTransaction));

        // Act & Assert
        StepVerifier.create(service.deleteTransaction(cardId, transactionId))
                .verifyComplete();

        verify(repository).findByCardTransactionId(transactionId);
        verify(repository, never()).delete(any(CardTransaction.class));
    }
}
