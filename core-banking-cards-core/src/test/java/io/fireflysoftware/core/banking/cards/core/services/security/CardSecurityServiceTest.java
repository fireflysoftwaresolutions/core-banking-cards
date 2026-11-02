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


package io.fireflysoftware.core.banking.cards.core.services.security;

import io.fireflysoftware.core.banking.cards.core.services.security.impl.CardSecurityServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.security.CardSecurityMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.security.CardSecurityDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.security.SecurityFeatureEnum;
import io.fireflysoftware.core.banking.cards.models.entities.security.CardSecurity;
import io.fireflysoftware.core.banking.cards.models.repositories.security.CardSecurityRepository;
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
public class CardSecurityServiceTest {

    @Mock
    private CardSecurityRepository repository;

    @Mock
    private CardSecurityMapper mapper;

    @InjectMocks
    private CardSecurityServiceImpl service;

    private CardSecurityDTO securityDTO;
    private CardSecurity securityEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID securityId = UUID.randomUUID();
    private final UUID wrongCardId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();
        securityDTO = CardSecurityDTO.builder()
                .cardSecurityId(securityId)
                .cardId(cardId)
                .securityFeature(SecurityFeatureEnum.PIN_ENABLED)
                .securityStatus(true)
                .build();

        securityEntity = new CardSecurity();
        securityEntity.setCardSecurityId(securityId);
        securityEntity.setCardId(cardId);
        securityEntity.setSecurityFeatureName("PIN_ENABLED");
        securityEntity.setSecurityFeatureCode("PIN");
        securityEntity.setIsEnabled(true);
        securityEntity.setIsMandatory(true);
        securityEntity.setActivationDate(now);
        securityEntity.setExpirationDate(now.plusYears(1));
        securityEntity.setLastUpdatedDate(now);
        securityEntity.setIsSystemDefault(true);
        securityEntity.setIsProgramDefault(true);
        securityEntity.setIsPartyConfigurable(true);
        securityEntity.setSupportsPin(true);
    }

    @Test
    void listSecuritySettings_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardSecurityDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listSecuritySettings(cardId, paginationRequest))
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
    void createSecuritySetting_Success() {
        // Arrange
        when(mapper.toEntity(any(CardSecurityDTO.class))).thenReturn(securityEntity);
        when(repository.save(any(CardSecurity.class))).thenReturn(Mono.just(securityEntity));
        when(mapper.toDTO(any(CardSecurity.class))).thenReturn(securityDTO);

        // Act & Assert
        StepVerifier.create(service.createSecuritySetting(cardId, securityDTO))
                .expectNext(securityDTO)
                .verifyComplete();

        verify(mapper).toEntity(securityDTO);
        verify(repository).save(securityEntity);
        verify(mapper).toDTO(securityEntity);
    }

    @Test
    void createSecuritySetting_Error() {
        // Arrange
        when(mapper.toEntity(any(CardSecurityDTO.class))).thenReturn(securityEntity);
        when(repository.save(any(CardSecurity.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(service.createSecuritySetting(cardId, securityDTO))
                .expectErrorMatches(throwable ->
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().contains("Error saving CardSecurity"))
                .verify();

        verify(mapper).toEntity(securityDTO);
        verify(repository).save(securityEntity);
        verify(mapper, never()).toDTO(any(CardSecurity.class));
    }

    @Test
    void getSecuritySetting_Success() {
        // Arrange
        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.just(securityEntity));
        when(mapper.toDTO(any(CardSecurity.class))).thenReturn(securityDTO);

        // Act & Assert
        StepVerifier.create(service.getSecuritySetting(cardId, securityId))
                .expectNext(securityDTO)
                .verifyComplete();

        verify(repository).findByCardSecurityId(securityId);
        verify(mapper).toDTO(securityEntity);
    }

    @Test
    void getSecuritySetting_NotFound() {
        // Arrange
        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getSecuritySetting(cardId, securityId))
                .verifyComplete();

        verify(repository).findByCardSecurityId(securityId);
        verify(mapper, never()).toDTO(any(CardSecurity.class));
    }

    @Test
    void getSecuritySetting_WrongCard() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        CardSecurity wrongCardSecurity = new CardSecurity();
        wrongCardSecurity.setCardSecurityId(securityId);
        wrongCardSecurity.setCardId(wrongCardId);
        wrongCardSecurity.setSecurityFeatureName("PIN_ENABLED");
        wrongCardSecurity.setSecurityFeatureCode("PIN");
        wrongCardSecurity.setIsEnabled(true);
        wrongCardSecurity.setIsMandatory(true);
        wrongCardSecurity.setActivationDate(now);
        wrongCardSecurity.setExpirationDate(now.plusYears(1));
        wrongCardSecurity.setLastUpdatedDate(now);
        wrongCardSecurity.setIsSystemDefault(true);
        wrongCardSecurity.setIsProgramDefault(true);
        wrongCardSecurity.setIsPartyConfigurable(true);
        wrongCardSecurity.setSupportsPin(true);

        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.just(wrongCardSecurity));

        // Act & Assert
        StepVerifier.create(service.getSecuritySetting(cardId, securityId))
                .expectErrorMatches(throwable ->
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("CardSecurity not found for the specified card."))
                .verify();

        verify(repository).findByCardSecurityId(securityId);
        verify(mapper, never()).toDTO(any(CardSecurity.class));
    }

    @Test
    void updateSecuritySetting_Success() {
        // Arrange
        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.just(securityEntity));
        when(mapper.toEntity(any(CardSecurityDTO.class))).thenReturn(securityEntity);
        when(repository.save(any(CardSecurity.class))).thenReturn(Mono.just(securityEntity));
        when(mapper.toDTO(any(CardSecurity.class))).thenReturn(securityDTO);

        // Act & Assert
        StepVerifier.create(service.updateSecuritySetting(cardId, securityId, securityDTO))
                .expectNext(securityDTO)
                .verifyComplete();

        verify(repository).findByCardSecurityId(securityId);
        verify(mapper).toEntity(securityDTO);
        verify(repository).save(securityEntity);
        verify(mapper).toDTO(securityEntity);
    }

    @Test
    void updateSecuritySetting_NotFound() {
        // Arrange
        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateSecuritySetting(cardId, securityId, securityDTO))
                .verifyComplete();

        verify(repository).findByCardSecurityId(securityId);
        verify(mapper, never()).toEntity(any(CardSecurityDTO.class));
        verify(repository, never()).save(any(CardSecurity.class));
        verify(mapper, never()).toDTO(any(CardSecurity.class));
    }

    @Test
    void updateSecuritySetting_WrongCard() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        CardSecurity wrongCardSecurity = new CardSecurity();
        wrongCardSecurity.setCardSecurityId(securityId);
        wrongCardSecurity.setCardId(wrongCardId);
        wrongCardSecurity.setSecurityFeatureName("PIN_ENABLED");
        wrongCardSecurity.setSecurityFeatureCode("PIN");
        wrongCardSecurity.setIsEnabled(true);
        wrongCardSecurity.setIsMandatory(true);
        wrongCardSecurity.setActivationDate(now);
        wrongCardSecurity.setExpirationDate(now.plusYears(1));
        wrongCardSecurity.setLastUpdatedDate(now);
        wrongCardSecurity.setIsSystemDefault(true);
        wrongCardSecurity.setIsProgramDefault(true);
        wrongCardSecurity.setIsPartyConfigurable(true);
        wrongCardSecurity.setSupportsPin(true);

        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.just(wrongCardSecurity));

        // Act & Assert
        StepVerifier.create(service.updateSecuritySetting(cardId, securityId, securityDTO))
                .expectErrorMatches(throwable ->
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("CardSecurity not found for the specified card."))
                .verify();

        verify(repository).findByCardSecurityId(securityId);
        verify(mapper, never()).toEntity(any(CardSecurityDTO.class));
        verify(repository, never()).save(any(CardSecurity.class));
        verify(mapper, never()).toDTO(any(CardSecurity.class));
    }

    @Test
    void deleteSecuritySetting_Success() {
        // Arrange
        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.just(securityEntity));
        when(repository.deleteById(securityId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteSecuritySetting(cardId, securityId))
                .verifyComplete();

        verify(repository).findByCardSecurityId(securityId);
        verify(repository).deleteById(securityId);
    }

    @Test
    void deleteSecuritySetting_NotFound() {
        // Arrange
        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteSecuritySetting(cardId, securityId))
                .verifyComplete();

        verify(repository).findByCardSecurityId(securityId);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void deleteSecuritySetting_WrongCard() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        CardSecurity wrongCardSecurity = new CardSecurity();
        wrongCardSecurity.setCardSecurityId(securityId);
        wrongCardSecurity.setCardId(wrongCardId);
        wrongCardSecurity.setSecurityFeatureName("PIN_ENABLED");
        wrongCardSecurity.setSecurityFeatureCode("PIN");
        wrongCardSecurity.setIsEnabled(true);
        wrongCardSecurity.setIsMandatory(true);
        wrongCardSecurity.setActivationDate(now);
        wrongCardSecurity.setExpirationDate(now.plusYears(1));
        wrongCardSecurity.setLastUpdatedDate(now);
        wrongCardSecurity.setIsSystemDefault(true);
        wrongCardSecurity.setIsProgramDefault(true);
        wrongCardSecurity.setIsPartyConfigurable(true);
        wrongCardSecurity.setSupportsPin(true);

        when(repository.findByCardSecurityId(securityId)).thenReturn(Mono.just(wrongCardSecurity));

        // Act & Assert
        StepVerifier.create(service.deleteSecuritySetting(cardId, securityId))
                .expectErrorMatches(throwable ->
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("CardSecurity not found for the specified card."))
                .verify();

        verify(repository).findByCardSecurityId(securityId);
        verify(repository, never()).deleteById(any(UUID.class));
    }
}
