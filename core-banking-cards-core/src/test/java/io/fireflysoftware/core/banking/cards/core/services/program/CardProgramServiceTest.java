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


package io.fireflysoftware.core.banking.cards.core.services.program;

import io.fireflysoftware.core.banking.cards.core.services.program.impl.CardProgramServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.program.CardProgramMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.program.CardProgramDTO;
import io.fireflysoftware.core.banking.cards.models.entities.program.CardProgram;
import io.fireflysoftware.core.banking.cards.models.repositories.program.CardProgramRepository;
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
public class CardProgramServiceTest {

    @Mock
    private CardProgramRepository repository;

    @Mock
    private CardProgramMapper mapper;

    @InjectMocks
    private CardProgramServiceImpl service;

    private CardProgramDTO programDTO;
    private CardProgram programEntity;
    private final UUID programId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();

        programDTO = CardProgramDTO.builder()
                .programId(programId)
                .programName("Test Program")
                .programCode("TEST-PROG")
                .issuerId(UUID.randomUUID())
                .binId(UUID.randomUUID())
                .cardTypeId(UUID.randomUUID())
                .cardNetworkId(UUID.randomUUID())
                .defaultDesignId(UUID.randomUUID())
                .startDate(now)
                .endDate(now.plusYears(5))
                .isActive(true)
                .maxCardsPerParty(5)
                .defaultDailyLimit(1000.0)
                .defaultMonthlyLimit(10000.0)
                .defaultCreditLimit(5000.0)
                .defaultCardValidityYears(4)
                .supportsPhysicalCards(true)
                .supportsVirtualCards(true)
                .supportsContactless(true)
                .supportsInternational(true)
                .supportsAtmWithdrawal(true)
                .supportsOnlineTransactions(true)
                .supportsRecurringPayments(true)
                .supportsApplePay(true)
                .supportsGooglePay(true)
                .supportsSamsungPay(true)
                .requiresPin(true)
                .requiresActivation(true)
                .currencyCode("USD")
                .countryCode("US")
                .termsAndConditionsUrl("https://example.com/terms")
                .description("Test program description")
                .build();

        programEntity = new CardProgram();
        programEntity.setProgramId(programId);
        programEntity.setProgramName("Test Program");
        programEntity.setProgramCode("TEST-PROG");
        programEntity.setIssuerId(UUID.randomUUID());
        programEntity.setBinId(UUID.randomUUID());
        programEntity.setCardTypeId(UUID.randomUUID());
        programEntity.setCardNetworkId(UUID.randomUUID());
        programEntity.setDefaultDesignId(UUID.randomUUID());
        programEntity.setStartDate(now);
        programEntity.setEndDate(now.plusYears(5));
        programEntity.setIsActive(true);
        programEntity.setMaxCardsPerParty(5);
        programEntity.setDefaultDailyLimit(1000.0);
        programEntity.setDefaultMonthlyLimit(10000.0);
        programEntity.setDefaultCreditLimit(5000.0);
        programEntity.setDefaultCardValidityYears(4);
        programEntity.setSupportsPhysicalCards(true);
        programEntity.setSupportsVirtualCards(true);
        programEntity.setSupportsContactless(true);
        programEntity.setSupportsInternational(true);
        programEntity.setSupportsAtmWithdrawal(true);
        programEntity.setSupportsOnlineTransactions(true);
        programEntity.setSupportsRecurringPayments(true);
        programEntity.setSupportsApplePay(true);
        programEntity.setSupportsGooglePay(true);
        programEntity.setSupportsSamsungPay(true);
        programEntity.setRequiresPin(true);
        programEntity.setRequiresActivation(true);
        programEntity.setCurrencyCode("USD");
        programEntity.setCountryCode("US");
        programEntity.setTermsAndConditionsUrl("https://example.com/terms");
        programEntity.setDescription("Test program description");
    }

    @Test
    void listPrograms_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardProgramDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listPrograms(paginationRequest))
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
    void createProgram_Success() {
        // Arrange
        when(mapper.toEntity(any(CardProgramDTO.class))).thenReturn(programEntity);
        when(repository.save(any(CardProgram.class))).thenReturn(Mono.just(programEntity));
        when(mapper.toDTO(any(CardProgram.class))).thenReturn(programDTO);

        // Act & Assert
        StepVerifier.create(service.createProgram(programDTO))
                .expectNext(programDTO)
                .verifyComplete();

        verify(mapper).toEntity(programDTO);
        verify(repository).save(programEntity);
        verify(mapper).toDTO(programEntity);
    }

    @Test
    void getProgram_Success() {
        // Arrange
        when(repository.findByProgramId(programId)).thenReturn(Mono.just(programEntity));
        when(mapper.toDTO(any(CardProgram.class))).thenReturn(programDTO);

        // Act & Assert
        StepVerifier.create(service.getProgram(programId))
                .expectNext(programDTO)
                .verifyComplete();

        verify(repository).findByProgramId(programId);
        verify(mapper).toDTO(programEntity);
    }

    @Test
    void getProgram_NotFound() {
        // Arrange
        when(repository.findByProgramId(programId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getProgram(programId))
                .verifyComplete();

        verify(repository).findByProgramId(programId);
        verify(mapper, never()).toDTO(any(CardProgram.class));
    }

    @Test
    void updateProgram_Success() {
        // Arrange
        when(repository.findByProgramId(programId)).thenReturn(Mono.just(programEntity));
        when(mapper.toEntity(any(CardProgramDTO.class))).thenReturn(programEntity);
        when(repository.save(any(CardProgram.class))).thenReturn(Mono.just(programEntity));
        when(mapper.toDTO(any(CardProgram.class))).thenReturn(programDTO);

        // Act & Assert
        StepVerifier.create(service.updateProgram(programId, programDTO))
                .expectNext(programDTO)
                .verifyComplete();

        verify(repository).findByProgramId(programId);
        verify(mapper).toEntity(programDTO);
        verify(repository).save(programEntity);
        verify(mapper).toDTO(programEntity);
    }

    @Test
    void updateProgram_NotFound() {
        // Arrange
        when(repository.findByProgramId(programId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateProgram(programId, programDTO))
                .verifyComplete();

        verify(repository).findByProgramId(programId);
        verify(mapper, never()).toEntity(any(CardProgramDTO.class));
        verify(repository, never()).save(any(CardProgram.class));
        verify(mapper, never()).toDTO(any(CardProgram.class));
    }

    @Test
    void deleteProgram_Success() {
        // Arrange
        when(repository.findByProgramId(programId)).thenReturn(Mono.just(programEntity));
        when(repository.delete(programEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteProgram(programId))
                .verifyComplete();

        verify(repository).findByProgramId(programId);
        verify(repository).delete(programEntity);
    }

    @Test
    void deleteProgram_NotFound() {
        // Arrange
        when(repository.findByProgramId(programId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteProgram(programId))
                .verifyComplete();

        verify(repository).findByProgramId(programId);
        verify(repository, never()).delete(any(CardProgram.class));
    }
}
