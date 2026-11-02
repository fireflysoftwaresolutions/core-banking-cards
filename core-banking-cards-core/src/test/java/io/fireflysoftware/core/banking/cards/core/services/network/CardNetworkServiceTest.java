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


package io.fireflysoftware.core.banking.cards.core.services.network;

import io.fireflysoftware.core.banking.cards.core.services.network.impl.CardNetworkServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.network.CardNetworkMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.network.CardNetworkDTO;
import io.fireflysoftware.core.banking.cards.models.entities.network.CardNetwork;
import io.fireflysoftware.core.banking.cards.models.repositories.network.CardNetworkRepository;
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
public class CardNetworkServiceTest {

    @Mock
    private CardNetworkRepository repository;

    @Mock
    private CardNetworkMapper mapper;

    @InjectMocks
    private CardNetworkServiceImpl service;

    private CardNetworkDTO networkDTO;
    private CardNetwork networkEntity;
    private final UUID networkId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        networkDTO = CardNetworkDTO.builder()
                .cardNetworkId(networkId)
                .networkName("Test Network")
                .networkCode("TEST")
                .networkLogoUrl("https://testnetwork.com/logo.png")
                .supportContact("support@testnetwork.com")
                .apiEndpoint("https://api.testnetwork.com")
                .apiKey("api-key-123")
                .apiSecret("api-secret-456")
                .isActive(true)
                .description("Test network description")
                .build();

        networkEntity = new CardNetwork();
        networkEntity.setCardNetworkId(networkId);
        networkEntity.setNetworkName("Test Network");
        networkEntity.setNetworkCode("TEST");
        networkEntity.setNetworkLogoUrl("https://testnetwork.com/logo.png");
        networkEntity.setSupportContact("support@testnetwork.com");
        networkEntity.setApiEndpoint("https://api.testnetwork.com");
        networkEntity.setApiKey("api-key-123");
        networkEntity.setApiSecret("api-secret-456");
        networkEntity.setIsActive(true);
        networkEntity.setDescription("Test network description");
    }

    @Test
    void listNetworks_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardNetworkDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listNetworks(paginationRequest))
                    .expectNext(expectedResponse)
                    .verifyComplete();

            // Verify that PaginationUtils.paginateQuery was called with the correct parameters
            paginationUtilsMocked.verify(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            ));
        }
    }

    @Test
    void createNetwork_Success() {
        // Arrange
        when(mapper.toEntity(any(CardNetworkDTO.class))).thenReturn(networkEntity);
        when(repository.save(any(CardNetwork.class))).thenReturn(Mono.just(networkEntity));
        when(mapper.toDTO(any(CardNetwork.class))).thenReturn(networkDTO);

        // Act & Assert
        StepVerifier.create(service.createNetwork(networkDTO))
                .expectNext(networkDTO)
                .verifyComplete();

        verify(mapper).toEntity(networkDTO);
        verify(repository).save(networkEntity);
        verify(mapper).toDTO(networkEntity);
    }

    @Test
    void getNetwork_Success() {
        // Arrange
        when(repository.findByCardNetworkId(networkId)).thenReturn(Mono.just(networkEntity));
        when(mapper.toDTO(any(CardNetwork.class))).thenReturn(networkDTO);

        // Act & Assert
        StepVerifier.create(service.getNetwork(networkId))
                .expectNext(networkDTO)
                .verifyComplete();

        verify(repository).findByCardNetworkId(networkId);
        verify(mapper).toDTO(networkEntity);
    }

    @Test
    void getNetwork_NotFound() {
        // Arrange
        when(repository.findByCardNetworkId(networkId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getNetwork(networkId))
                .verifyComplete();

        verify(repository).findByCardNetworkId(networkId);
        verify(mapper, never()).toDTO(any(CardNetwork.class));
    }

    @Test
    void updateNetwork_Success() {
        // Arrange
        when(repository.findByCardNetworkId(networkId)).thenReturn(Mono.just(networkEntity));
        when(mapper.toEntity(any(CardNetworkDTO.class))).thenReturn(networkEntity);
        when(repository.save(any(CardNetwork.class))).thenReturn(Mono.just(networkEntity));
        when(mapper.toDTO(any(CardNetwork.class))).thenReturn(networkDTO);

        // Act & Assert
        StepVerifier.create(service.updateNetwork(networkId, networkDTO))
                .expectNext(networkDTO)
                .verifyComplete();

        verify(repository).findByCardNetworkId(networkId);
        verify(mapper).toEntity(networkDTO);
        verify(repository).save(networkEntity);
        verify(mapper).toDTO(networkEntity);
    }

    @Test
    void updateNetwork_NotFound() {
        // Arrange
        when(repository.findByCardNetworkId(networkId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateNetwork(networkId, networkDTO))
                .verifyComplete();

        verify(repository).findByCardNetworkId(networkId);
        verify(mapper, never()).toEntity(any(CardNetworkDTO.class));
        verify(repository, never()).save(any(CardNetwork.class));
        verify(mapper, never()).toDTO(any(CardNetwork.class));
    }

    @Test
    void deleteNetwork_Success() {
        // Arrange
        when(repository.findByCardNetworkId(networkId)).thenReturn(Mono.just(networkEntity));
        when(repository.delete(networkEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteNetwork(networkId))
                .verifyComplete();

        verify(repository).findByCardNetworkId(networkId);
        verify(repository).delete(networkEntity);
    }

    @Test
    void deleteNetwork_NotFound() {
        // Arrange
        when(repository.findByCardNetworkId(networkId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteNetwork(networkId))
                .verifyComplete();

        verify(repository).findByCardNetworkId(networkId);
        verify(repository, never()).delete(any(CardNetwork.class));
    }
}
