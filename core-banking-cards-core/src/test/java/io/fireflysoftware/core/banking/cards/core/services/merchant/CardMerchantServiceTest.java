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


package io.fireflysoftware.core.banking.cards.core.services.merchant;

import io.fireflysoftware.core.banking.cards.core.services.merchant.impl.CardMerchantServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.merchant.CardMerchantMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.merchant.CardMerchantDTO;
import io.fireflysoftware.core.banking.cards.models.entities.merchant.CardMerchant;
import io.fireflysoftware.core.banking.cards.models.repositories.merchant.CardMerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardMerchantServiceTest {

    @Mock
    private CardMerchantRepository repository;

    @Mock
    private CardMerchantMapper mapper;

    @InjectMocks
    private CardMerchantServiceImpl service;

    private CardMerchantDTO merchantDTO;
    private CardMerchant merchantEntity;
    private final UUID merchantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        merchantDTO = CardMerchantDTO.builder()
                .merchantId(merchantId)
                .merchantName("Test Merchant")
                .merchantType("Retail")
                .merchantCategoryCode("5411")
                .merchantDescription("Test Merchant Description")
                .build();

        merchantEntity = new CardMerchant();
        merchantEntity.setMerchantId(merchantId);
        merchantEntity.setMerchantName("Test Merchant");
        merchantEntity.setMerchantType("Retail");
        merchantEntity.setMerchantCategoryCode("5411");
        merchantEntity.setMerchantDescription("Test Merchant Description");
    }

    @Test
    void listMerchants_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardMerchantDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listMerchants(paginationRequest))
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
    void createMerchant_Success() {
        // Arrange
        when(mapper.toEntity(any(CardMerchantDTO.class))).thenReturn(merchantEntity);
        when(repository.save(any(CardMerchant.class))).thenReturn(Mono.just(merchantEntity));
        when(mapper.toDTO(any(CardMerchant.class))).thenReturn(merchantDTO);

        // Act & Assert
        StepVerifier.create(service.createMerchant(merchantDTO))
                .expectNext(merchantDTO)
                .verifyComplete();

        verify(mapper).toEntity(merchantDTO);
        verify(repository).save(merchantEntity);
        verify(mapper).toDTO(merchantEntity);
    }

    @Test
    void getMerchant_Success() {
        // Arrange
        when(repository.findByMerchantId(merchantId)).thenReturn(Mono.just(merchantEntity));
        when(mapper.toDTO(any(CardMerchant.class))).thenReturn(merchantDTO);

        // Act & Assert
        StepVerifier.create(service.getMerchant(merchantId))
                .expectNext(merchantDTO)
                .verifyComplete();

        verify(repository).findByMerchantId(merchantId);
        verify(mapper).toDTO(merchantEntity);
    }

    @Test
    void getMerchant_NotFound() {
        // Arrange
        when(repository.findByMerchantId(merchantId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getMerchant(merchantId))
                .verifyComplete();

        verify(repository).findByMerchantId(merchantId);
        verify(mapper, never()).toDTO(any(CardMerchant.class));
    }

    @Test
    void updateMerchant_Success() {
        // Arrange
        when(repository.findByMerchantId(merchantId)).thenReturn(Mono.just(merchantEntity));
        when(mapper.toEntity(any(CardMerchantDTO.class))).thenReturn(merchantEntity);
        when(repository.save(any(CardMerchant.class))).thenReturn(Mono.just(merchantEntity));
        when(mapper.toDTO(any(CardMerchant.class))).thenReturn(merchantDTO);

        // Act & Assert
        StepVerifier.create(service.updateMerchant(merchantId, merchantDTO))
                .expectNext(merchantDTO)
                .verifyComplete();

        verify(repository).findByMerchantId(merchantId);
        verify(mapper).toEntity(merchantDTO);
        verify(repository).save(merchantEntity);
        verify(mapper).toDTO(merchantEntity);
    }

    @Test
    void updateMerchant_NotFound() {
        // Arrange
        when(repository.findByMerchantId(merchantId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateMerchant(merchantId, merchantDTO))
                .verifyComplete();

        verify(repository).findByMerchantId(merchantId);
        verify(repository, never()).save(any(CardMerchant.class));
        verify(mapper, never()).toDTO(any(CardMerchant.class));
    }

    @Test
    void deleteMerchant_Success() {
        // Arrange
        when(repository.findByMerchantId(merchantId)).thenReturn(Mono.just(merchantEntity));
        when(repository.delete(merchantEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteMerchant(merchantId))
                .verifyComplete();

        verify(repository).findByMerchantId(merchantId);
        verify(repository).delete(merchantEntity);
    }

    @Test
    void deleteMerchant_NotFound() {
        // Arrange
        when(repository.findByMerchantId(merchantId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteMerchant(merchantId))
                .verifyComplete();

        verify(repository).findByMerchantId(merchantId);
        verify(repository, never()).delete(any(CardMerchant.class));
    }
}
