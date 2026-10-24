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


package io.fireflysoftware.core.banking.cards.core.services.bin;

import io.fireflysoftware.core.banking.cards.core.services.bin.impl.BINServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.bin.BINMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.bin.BINDTO;
import io.fireflysoftware.core.banking.cards.models.entities.bin.BIN;
import io.fireflysoftware.core.banking.cards.models.repositories.bin.BINRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BINServiceTest {

    @Mock
    private BINRepository repository;

    @Mock
    private BINMapper mapper;

    @InjectMocks
    private BINServiceImpl service;

    private BINDTO binDTO;
    private BIN binEntity;
    private final UUID binId = UUID.randomUUID();
    private final String binNumber = "123456";

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();

        binDTO = BINDTO.builder()
                .binId(binId)
                .binNumber(binNumber)
                .binLength(6)
                .issuerId(UUID.randomUUID())
                .cardNetworkId(UUID.randomUUID())
                .cardTypeId(UUID.randomUUID())
                .countryCode("USA")
                .currencyCode("USD")
                .isActive(true)
                .description("Test BIN")
                .build();

        binEntity = new BIN();
        binEntity.setBinId(binId);
        binEntity.setBinNumber(binNumber);
        binEntity.setBinLength(6);
        binEntity.setIssuerId(UUID.randomUUID());
        binEntity.setCardNetworkId(UUID.randomUUID());
        binEntity.setCardTypeId(UUID.randomUUID());
        binEntity.setCountryCode("USA");
        binEntity.setCurrencyCode("USD");
        binEntity.setIsActive(true);
        binEntity.setDescription("Test BIN");
    }

    @Test
    void listBINs_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<BINDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listBINs(paginationRequest))
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
    void createBIN_Success() {
        // Arrange
        when(mapper.toEntity(any(BINDTO.class))).thenReturn(binEntity);
        when(repository.save(any(BIN.class))).thenReturn(Mono.just(binEntity));
        when(mapper.toDTO(any(BIN.class))).thenReturn(binDTO);

        // Act & Assert
        StepVerifier.create(service.createBIN(binDTO))
                .expectNext(binDTO)
                .verifyComplete();

        verify(mapper).toEntity(binDTO);
        verify(repository).save(binEntity);
        verify(mapper).toDTO(binEntity);
    }

    @Test
    void getBIN_Success() {
        // Arrange
        when(repository.findByBinId(binId)).thenReturn(Mono.just(binEntity));
        when(mapper.toDTO(any(BIN.class))).thenReturn(binDTO);

        // Act & Assert
        StepVerifier.create(service.getBIN(binId))
                .expectNext(binDTO)
                .verifyComplete();

        verify(repository).findByBinId(binId);
        verify(mapper).toDTO(binEntity);
    }

    @Test
    void getBIN_NotFound() {
        // Arrange
        when(repository.findByBinId(binId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getBIN(binId))
                .verifyComplete();

        verify(repository).findByBinId(binId);
        verify(mapper, never()).toDTO(any(BIN.class));
    }

    @Test
    void getBINByNumber_Success() {
        // Arrange
        when(repository.findByBinNumber(binNumber)).thenReturn(Mono.just(binEntity));
        when(mapper.toDTO(any(BIN.class))).thenReturn(binDTO);

        // Act & Assert
        StepVerifier.create(service.getBINByNumber(binNumber))
                .expectNext(binDTO)
                .verifyComplete();

        verify(repository).findByBinNumber(binNumber);
        verify(mapper).toDTO(binEntity);
    }

    @Test
    void getBINByNumber_NotFound() {
        // Arrange
        when(repository.findByBinNumber(binNumber)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getBINByNumber(binNumber))
                .verifyComplete();

        verify(repository).findByBinNumber(binNumber);
        verify(mapper, never()).toDTO(any(BIN.class));
    }

    @Test
    void updateBIN_Success() {
        // Arrange
        when(repository.findByBinId(binId)).thenReturn(Mono.just(binEntity));
        when(mapper.toEntity(any(BINDTO.class))).thenReturn(binEntity);
        when(repository.save(any(BIN.class))).thenReturn(Mono.just(binEntity));
        when(mapper.toDTO(any(BIN.class))).thenReturn(binDTO);

        // Act & Assert
        StepVerifier.create(service.updateBIN(binId, binDTO))
                .expectNext(binDTO)
                .verifyComplete();

        verify(repository).findByBinId(binId);
        verify(mapper).toEntity(binDTO);
        verify(repository).save(binEntity);
        verify(mapper).toDTO(binEntity);
    }

    @Test
    void updateBIN_NotFound() {
        // Arrange
        when(repository.findByBinId(binId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateBIN(binId, binDTO))
                .verifyComplete();

        verify(repository).findByBinId(binId);
        verify(mapper, never()).toEntity(any(BINDTO.class));
        verify(repository, never()).save(any(BIN.class));
        verify(mapper, never()).toDTO(any(BIN.class));
    }

    @Test
    void deleteBIN_Success() {
        // Arrange
        when(repository.findByBinId(binId)).thenReturn(Mono.just(binEntity));
        when(repository.delete(binEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteBIN(binId))
                .verifyComplete();

        verify(repository).findByBinId(binId);
        verify(repository).delete(binEntity);
    }

    @Test
    void deleteBIN_NotFound() {
        // Arrange
        when(repository.findByBinId(binId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteBIN(binId))
                .verifyComplete();

        verify(repository).findByBinId(binId);
        verify(repository, never()).delete(any(BIN.class));
    }
}
