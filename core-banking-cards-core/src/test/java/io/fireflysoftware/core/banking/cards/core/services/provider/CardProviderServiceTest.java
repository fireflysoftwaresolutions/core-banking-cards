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


package io.fireflysoftware.core.banking.cards.core.services.provider;

import io.fireflysoftware.core.banking.cards.core.services.provider.impl.CardProviderServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.provider.CardProviderMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.provider.CardProviderDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.provider.ProviderStatusEnum;
import io.fireflysoftware.core.banking.cards.models.entities.provider.CardProvider;
import io.fireflysoftware.core.banking.cards.models.repositories.provider.CardProviderRepository;
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
public class CardProviderServiceTest {

    @Mock
    private CardProviderRepository repository;

    @Mock
    private CardProviderMapper mapper;

    @InjectMocks
    private CardProviderServiceImpl service;

    private CardProviderDTO providerDTO;
    private CardProvider providerEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID providerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        providerDTO = CardProviderDTO.builder()
                .cardProviderId(providerId)
                .cardId(cardId)
                .providerName("Test Provider")
                .providerCode("TEST")
                .providerType("PROCESSOR")
                .apiBaseUrl("https://api.testprovider.com")
                .apiVersion("v1")
                .apiKey("api-key-123")
                .apiSecret("api-secret-456")
                .apiUsername("testuser")
                .apiPassword("testpass")
                .apiCertificate("cert-data")
                .webhookUrl("https://webhook.testprovider.com")
                .webhookSecret("webhook-secret")
                .supportsPhysicalCards(true)
                .supportsVirtualCards(true)
                .supportsTokenization(true)
                .supports3dSecure(true)
                .supportsApplePay(true)
                .supportsGooglePay(true)
                .supportsSamsungPay(true)
                .contactName("John Contact")
                .contactEmail("john@testprovider.com")
                .contactPhone("+1234567890")
                .supportUrl("https://support.testprovider.com")
                .supportEmail("support@testprovider.com")
                .supportPhone("+1987654321")
                .contractStartDate(now.minusMonths(6))
                .contractEndDate(now.plusYears(2))
                .status(ProviderStatusEnum.ACTIVE)
                .lastConnectionDate(now.minusDays(1))
                .lastConnectionStatus("SUCCESS")
                .description("Test provider description")
                .build();

        providerEntity = new CardProvider();
        providerEntity.setCardProviderId(providerId);
        providerEntity.setCardId(cardId);
        providerEntity.setProviderName("Test Provider");
        providerEntity.setProviderCode("TEST");
        providerEntity.setProviderType("PROCESSOR");
        providerEntity.setApiBaseUrl("https://api.testprovider.com");
        providerEntity.setApiVersion("v1");
        providerEntity.setApiKey("api-key-123");
        providerEntity.setApiSecret("api-secret-456");
        providerEntity.setApiUsername("testuser");
        providerEntity.setApiPassword("testpass");
        providerEntity.setApiCertificate("cert-data");
        providerEntity.setWebhookUrl("https://webhook.testprovider.com");
        providerEntity.setWebhookSecret("webhook-secret");
        providerEntity.setSupportsPhysicalCards(true);
        providerEntity.setSupportsVirtualCards(true);
        providerEntity.setSupportsTokenization(true);
        providerEntity.setSupports3dSecure(true);
        providerEntity.setSupportsApplePay(true);
        providerEntity.setSupportsGooglePay(true);
        providerEntity.setSupportsSamsungPay(true);
        providerEntity.setContactName("John Contact");
        providerEntity.setContactEmail("john@testprovider.com");
        providerEntity.setContactPhone("+1234567890");
        providerEntity.setSupportUrl("https://support.testprovider.com");
        providerEntity.setSupportEmail("support@testprovider.com");
        providerEntity.setSupportPhone("+1987654321");
        providerEntity.setContractStartDate(now.minusMonths(6));
        providerEntity.setContractEndDate(now.plusYears(2));
        providerEntity.setStatus(ProviderStatusEnum.ACTIVE);
        providerEntity.setLastConnectionDate(now.minusDays(1));
        providerEntity.setLastConnectionStatus("SUCCESS");
        providerEntity.setDescription("Test provider description");
    }

    @Test
    void listProviders_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardProviderDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listProviders(cardId, paginationRequest))
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
    void createProvider_Success() {
        // Arrange
        when(mapper.toEntity(any(CardProviderDTO.class))).thenReturn(providerEntity);
        when(repository.save(any(CardProvider.class))).thenReturn(Mono.just(providerEntity));
        when(mapper.toDTO(any(CardProvider.class))).thenReturn(providerDTO);

        // Act & Assert
        StepVerifier.create(service.createProvider(cardId, providerDTO))
                .expectNext(providerDTO)
                .verifyComplete();

        verify(mapper).toEntity(providerDTO);
        verify(repository).save(providerEntity);
        verify(mapper).toDTO(providerEntity);
    }

    @Test
    void getProvider_Success() {
        // Arrange
        when(repository.findById(providerId)).thenReturn(Mono.just(providerEntity));
        when(mapper.toDTO(any(CardProvider.class))).thenReturn(providerDTO);

        // Act & Assert
        StepVerifier.create(service.getProvider(cardId, providerId))
                .expectNext(providerDTO)
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(mapper).toDTO(providerEntity);
    }

    @Test
    void getProvider_NotFound() {
        // Arrange
        when(repository.findById(providerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getProvider(cardId, providerId))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(mapper, never()).toDTO(any(CardProvider.class));
    }

    @Test
    void getProvider_WrongCard() {
        // Arrange
        CardProvider wrongCardProvider = new CardProvider();
        wrongCardProvider.setCardProviderId(providerId);
        wrongCardProvider.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findById(providerId)).thenReturn(Mono.just(wrongCardProvider));

        // Act & Assert
        StepVerifier.create(service.getProvider(cardId, providerId))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(mapper, never()).toDTO(any(CardProvider.class));
    }

    @Test
    void updateProvider_Success() {
        // Arrange
        when(repository.findById(providerId)).thenReturn(Mono.just(providerEntity));
        when(mapper.toEntity(any(CardProviderDTO.class))).thenReturn(providerEntity);
        when(repository.save(any(CardProvider.class))).thenReturn(Mono.just(providerEntity));
        when(mapper.toDTO(any(CardProvider.class))).thenReturn(providerDTO);

        // Act & Assert
        StepVerifier.create(service.updateProvider(cardId, providerId, providerDTO))
                .expectNext(providerDTO)
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(mapper).toEntity(providerDTO);
        verify(repository).save(providerEntity);
        verify(mapper).toDTO(providerEntity);
    }

    @Test
    void updateProvider_NotFound() {
        // Arrange
        when(repository.findById(providerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateProvider(cardId, providerId, providerDTO))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(mapper, never()).toEntity(any(CardProviderDTO.class));
        verify(repository, never()).save(any(CardProvider.class));
        verify(mapper, never()).toDTO(any(CardProvider.class));
    }

    @Test
    void updateProvider_WrongCard() {
        // Arrange
        CardProvider wrongCardProvider = new CardProvider();
        wrongCardProvider.setCardProviderId(providerId);
        wrongCardProvider.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findById(providerId)).thenReturn(Mono.just(wrongCardProvider));

        // Act & Assert
        StepVerifier.create(service.updateProvider(cardId, providerId, providerDTO))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(mapper, never()).toEntity(any(CardProviderDTO.class));
        verify(repository, never()).save(any(CardProvider.class));
        verify(mapper, never()).toDTO(any(CardProvider.class));
    }

    @Test
    void deleteProvider_Success() {
        // Arrange
        when(repository.findById(providerId)).thenReturn(Mono.just(providerEntity));
        when(repository.delete(providerEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteProvider(cardId, providerId))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(repository).delete(providerEntity);
    }

    @Test
    void deleteProvider_NotFound() {
        // Arrange
        when(repository.findById(providerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteProvider(cardId, providerId))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(repository, never()).delete(any(CardProvider.class));
    }

    @Test
    void deleteProvider_WrongCard() {
        // Arrange
        CardProvider wrongCardProvider = new CardProvider();
        wrongCardProvider.setCardProviderId(providerId);
        wrongCardProvider.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findById(providerId)).thenReturn(Mono.just(wrongCardProvider));

        // Act & Assert
        StepVerifier.create(service.deleteProvider(cardId, providerId))
                .verifyComplete();

        verify(repository).findById(providerId);
        verify(repository, never()).delete(any(CardProvider.class));
    }
}