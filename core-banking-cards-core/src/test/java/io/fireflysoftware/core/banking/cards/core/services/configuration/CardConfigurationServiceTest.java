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


package io.fireflysoftware.core.banking.cards.core.services.configuration;

import io.fireflysoftware.core.banking.cards.core.services.configuration.impl.CardConfigurationServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.configuration.CardConfigurationMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.configuration.CardConfigurationDTO;
import io.fireflysoftware.core.banking.cards.models.entities.configuration.CardConfiguration;
import io.fireflysoftware.core.banking.cards.models.repositories.configuration.CardConfigurationRepository;
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
public class CardConfigurationServiceTest {

    @Mock
    private CardConfigurationRepository repository;

    @Mock
    private CardConfigurationMapper mapper;

    @InjectMocks
    private CardConfigurationServiceImpl service;

    private CardConfigurationDTO configDTO;
    private CardConfiguration configEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID configId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        configDTO = CardConfigurationDTO.builder()
                .cardConfigurationId(configId)
                .cardId(cardId)
                .programId(UUID.randomUUID())
                .configKey("CONTACTLESS")
                .configValue("true")
                .configType("BOOLEAN")
                .isActive(true)
                .activationDate(now.minusDays(30))
                .expirationDate(now.plusYears(1))
                .isSystemDefault(false)
                .isProgramDefault(true)
                .isPartyConfigurable(true)
                .allowedValues("true,false")
                .minValue(null)
                .maxValue(null)
                .description("Enable or disable contactless payments")
                .build();

        configEntity = new CardConfiguration();
        configEntity.setCardConfigurationId(configId);
        configEntity.setCardId(cardId);
        configEntity.setProgramId(UUID.randomUUID());
        configEntity.setConfigKey("CONTACTLESS");
        configEntity.setConfigValue("true");
        configEntity.setConfigType("BOOLEAN");
        configEntity.setIsActive(true);
        configEntity.setActivationDate(now.minusDays(30));
        configEntity.setExpirationDate(now.plusYears(1));
        configEntity.setIsSystemDefault(false);
        configEntity.setIsProgramDefault(true);
        configEntity.setIsPartyConfigurable(true);
        configEntity.setAllowedValues("true,false");
        configEntity.setMinValue(null);
        configEntity.setMaxValue(null);
        configEntity.setDescription("Enable or disable contactless payments");
    }

    @Test
    void listConfigurations_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardConfigurationDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listConfigurations(cardId, paginationRequest))
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
    void createConfiguration_Success() {
        // Arrange
        when(mapper.toEntity(any(CardConfigurationDTO.class))).thenReturn(configEntity);
        when(repository.save(any(CardConfiguration.class))).thenReturn(Mono.just(configEntity));
        when(mapper.toDTO(any(CardConfiguration.class))).thenReturn(configDTO);

        // Act & Assert
        StepVerifier.create(service.createConfiguration(cardId, configDTO))
                .expectNext(configDTO)
                .verifyComplete();

        verify(mapper).toEntity(configDTO);
        verify(repository).save(configEntity);
        verify(mapper).toDTO(configEntity);
    }

    @Test
    void getConfiguration_Success() {
        // Arrange
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.just(configEntity));
        when(mapper.toDTO(any(CardConfiguration.class))).thenReturn(configDTO);

        // Act & Assert
        StepVerifier.create(service.getConfiguration(cardId, configId))
                .expectNext(configDTO)
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(mapper).toDTO(configEntity);
    }

    @Test
    void getConfiguration_NotFound() {
        // Arrange
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getConfiguration(cardId, configId))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(mapper, never()).toDTO(any(CardConfiguration.class));
    }

    @Test
    void getConfiguration_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.just(configEntity));

        // Act & Assert
        StepVerifier.create(service.getConfiguration(wrongCardId, configId))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(mapper, never()).toDTO(any(CardConfiguration.class));
    }

    @Test
    void updateConfiguration_Success() {
        // Arrange
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.just(configEntity));
        when(repository.save(any(CardConfiguration.class))).thenReturn(Mono.just(configEntity));
        when(mapper.toDTO(any(CardConfiguration.class))).thenReturn(configDTO);

        // Act & Assert
        StepVerifier.create(service.updateConfiguration(cardId, configId, configDTO))
                .expectNext(configDTO)
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(repository).save(configEntity);
        verify(mapper).toDTO(configEntity);
    }

    @Test
    void updateConfiguration_NotFound() {
        // Arrange
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateConfiguration(cardId, configId, configDTO))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(repository, never()).save(any(CardConfiguration.class));
        verify(mapper, never()).toDTO(any(CardConfiguration.class));
    }

    @Test
    void updateConfiguration_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.just(configEntity));

        // Act & Assert
        StepVerifier.create(service.updateConfiguration(wrongCardId, configId, configDTO))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(repository, never()).save(any(CardConfiguration.class));
        verify(mapper, never()).toDTO(any(CardConfiguration.class));
    }

    @Test
    void deleteConfiguration_Success() {
        // Arrange
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.just(configEntity));
        when(repository.delete(configEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteConfiguration(cardId, configId))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(repository).delete(configEntity);
    }

    @Test
    void deleteConfiguration_NotFound() {
        // Arrange
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteConfiguration(cardId, configId))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(repository, never()).delete(any(CardConfiguration.class));
    }

    @Test
    void deleteConfiguration_WrongCardId() {
        // Arrange
        UUID wrongCardId = UUID.randomUUID();
        when(repository.findByCardConfigurationId(configId)).thenReturn(Mono.just(configEntity));

        // Act & Assert
        StepVerifier.create(service.deleteConfiguration(wrongCardId, configId))
                .verifyComplete();

        verify(repository).findByCardConfigurationId(configId);
        verify(repository, never()).delete(any(CardConfiguration.class));
    }
}
