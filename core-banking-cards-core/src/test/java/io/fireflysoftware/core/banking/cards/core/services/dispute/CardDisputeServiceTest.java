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


package io.fireflysoftware.core.banking.cards.core.services.dispute;

import io.fireflysoftware.core.banking.cards.core.services.dispute.impl.CardDisputeServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.dispute.CardDisputeMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.dispute.CardDisputeDTO;
import io.fireflysoftware.core.banking.cards.models.entities.dispute.CardDispute;
import io.fireflysoftware.core.banking.cards.models.repositories.dispute.CardDisputeRepository;
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
public class CardDisputeServiceTest {

    @Mock
    private CardDisputeRepository repository;

    @Mock
    private CardDisputeMapper mapper;

    @InjectMocks
    private CardDisputeServiceImpl service;

    private CardDisputeDTO disputeDTO;
    private CardDispute disputeEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID disputeId = UUID.randomUUID();
    private final UUID transactionId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        disputeDTO = CardDisputeDTO.builder()
                .disputeId(disputeId)
                .cardId(cardId)
                .transactionId(transactionId)
                .disputeReasonCode("UNAUTHORIZED_TRANSACTION")
                .disputeAmount(new BigDecimal("125.50"))
                .filingTimestamp(now)
                .disputeStatus("PENDING")
                .build();

        disputeEntity = new CardDispute();
        disputeEntity.setDisputeId(disputeId);
        disputeEntity.setCardId(cardId);
        disputeEntity.setTransactionId(transactionId);
        disputeEntity.setDisputeReasonCode("UNAUTHORIZED_TRANSACTION");
        disputeEntity.setDisputeAmount(new BigDecimal("125.50"));
        disputeEntity.setFilingTimestamp(now);
        disputeEntity.setDisputeStatus("PENDING");
    }

    @Test
    void listDisputes_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardDisputeDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listDisputes(cardId, paginationRequest))
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
    void createDispute_Success() {
        // Arrange
        when(mapper.toEntity(any(CardDisputeDTO.class))).thenReturn(disputeEntity);
        when(repository.save(any(CardDispute.class))).thenReturn(Mono.just(disputeEntity));
        when(mapper.toDTO(any(CardDispute.class))).thenReturn(disputeDTO);

        // Act & Assert
        StepVerifier.create(service.createDispute(cardId, disputeDTO))
                .expectNext(disputeDTO)
                .verifyComplete();

        verify(mapper).toEntity(disputeDTO);
        verify(repository).save(disputeEntity);
        verify(mapper).toDTO(disputeEntity);
    }

    @Test
    void getDispute_Success() {
        // Arrange
        when(repository.findByDisputeId(disputeId)).thenReturn(Mono.just(disputeEntity));
        when(mapper.toDTO(any(CardDispute.class))).thenReturn(disputeDTO);

        // Act & Assert
        StepVerifier.create(service.getDispute(cardId, disputeId))
                .expectNext(disputeDTO)
                .verifyComplete();

        verify(repository).findByDisputeId(disputeId);
        verify(mapper).toDTO(disputeEntity);
    }

    @Test
    void getDispute_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByDisputeId(disputeId)).thenReturn(Mono.just(disputeEntity));

        // Act & Assert
        StepVerifier.create(service.getDispute(wrongCardId, disputeId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByDisputeId(disputeId);
        verify(mapper, never()).toDTO(any(CardDispute.class));
    }

    @Test
    void updateDispute_Success() {
        // Arrange
        when(repository.findByDisputeId(disputeId)).thenReturn(Mono.just(disputeEntity));
        when(mapper.toEntity(any(CardDisputeDTO.class))).thenReturn(disputeEntity);
        when(repository.save(any(CardDispute.class))).thenReturn(Mono.just(disputeEntity));
        when(mapper.toDTO(any(CardDispute.class))).thenReturn(disputeDTO);

        // Act & Assert
        StepVerifier.create(service.updateDispute(cardId, disputeId, disputeDTO))
                .expectNext(disputeDTO)
                .verifyComplete();

        verify(repository).findByDisputeId(disputeId);
        verify(mapper).toEntity(disputeDTO);
        verify(repository).save(disputeEntity);
        verify(mapper).toDTO(disputeEntity);
    }

    @Test
    void updateDispute_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByDisputeId(disputeId)).thenReturn(Mono.just(disputeEntity));

        // Act & Assert
        StepVerifier.create(service.updateDispute(wrongCardId, disputeId, disputeDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByDisputeId(disputeId);
        verify(repository, never()).save(any(CardDispute.class));
        verify(mapper, never()).toDTO(any(CardDispute.class));
    }

    @Test
    void deleteDispute_Success() {
        // Arrange
        when(repository.findByDisputeId(disputeId)).thenReturn(Mono.just(disputeEntity));
        when(repository.delete(disputeEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDispute(cardId, disputeId))
                .verifyComplete();

        verify(repository).findByDisputeId(disputeId);
        verify(repository).delete(disputeEntity);
    }

    @Test
    void deleteDispute_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByDisputeId(disputeId)).thenReturn(Mono.just(disputeEntity));

        // Act & Assert
        StepVerifier.create(service.deleteDispute(wrongCardId, disputeId))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).findByDisputeId(disputeId);
        verify(repository, never()).delete(any(CardDispute.class));
    }
}
