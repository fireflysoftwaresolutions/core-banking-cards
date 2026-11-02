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


package io.fireflysoftware.core.banking.cards.core.services.physical;

import io.fireflysoftware.core.banking.cards.core.services.physical.impl.PhysicalCardServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.physical.PhysicalCardMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.physical.PhysicalCardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.physical.PhysicalCard;
import io.fireflysoftware.core.banking.cards.models.repositories.physical.PhysicalCardRepository;
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
public class PhysicalCardServiceTest {

    @Mock
    private PhysicalCardRepository repository;

    @Mock
    private PhysicalCardMapper mapper;

    @InjectMocks
    private PhysicalCardServiceImpl service;

    private PhysicalCardDTO physicalCardDTO;
    private PhysicalCard physicalCardEntity;
    private final UUID cardId = UUID.randomUUID();
    private final UUID physicalCardId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Initialize test data
        physicalCardDTO = PhysicalCardDTO.builder()
                .physicalCardId(physicalCardId)
                .cardId(cardId)
                .embossedName("John Doe")
                .plasticId("P12345")
                .designId(UUID.randomUUID())
                .isContactless(true)
                .isChip(true)
                .isMagstripe(true)
                .manufacturingStatus("COMPLETED")
                .manufacturingDate(now.minusDays(10))
                .shippingAddress("123 Main St")
                .shippingCity("New York")
                .shippingState("NY")
                .shippingCountry("USA")
                .shippingPostalCode("10001")
                .shippingMethod("Express")
                .shippingTrackingNumber("TRK123456")
                .shippingCarrier("FedEx")
                .shipmentDate(now)
                .estimatedDeliveryDate(now.plusDays(3))
                .actualDeliveryDate(now.plusDays(5))
                .activationMethod("ONLINE")
                .activationDate(now.plusDays(6))
                .isActivated(true)
                .replacementReason(null)
                .previousCardId(null)
                .notes("Test card")
                .build();

        physicalCardEntity = new PhysicalCard();
        physicalCardEntity.setPhysicalCardId(physicalCardId);
        physicalCardEntity.setCardId(cardId);
        physicalCardEntity.setEmbossedName("John Doe");
        physicalCardEntity.setPlasticId("P12345");
        physicalCardEntity.setDesignId(UUID.randomUUID());
        physicalCardEntity.setIsContactless(true);
        physicalCardEntity.setIsChip(true);
        physicalCardEntity.setIsMagstripe(true);
        physicalCardEntity.setManufacturingStatus("COMPLETED");
        physicalCardEntity.setManufacturingDate(now.minusDays(10));
        physicalCardEntity.setShippingAddress("123 Main St");
        physicalCardEntity.setShippingCity("New York");
        physicalCardEntity.setShippingState("NY");
        physicalCardEntity.setShippingCountry("USA");
        physicalCardEntity.setShippingPostalCode("10001");
        physicalCardEntity.setShippingMethod("Express");
        physicalCardEntity.setShippingTrackingNumber("TRK123456");
        physicalCardEntity.setShippingCarrier("FedEx");
        physicalCardEntity.setShipmentDate(now);
        physicalCardEntity.setEstimatedDeliveryDate(now.plusDays(3));
        physicalCardEntity.setActualDeliveryDate(now.plusDays(5));
        physicalCardEntity.setActivationMethod("ONLINE");
        physicalCardEntity.setActivationDate(now.plusDays(6));
        physicalCardEntity.setIsActivated(true);
        physicalCardEntity.setReplacementReason(null);
        physicalCardEntity.setPreviousCardId(null);
        physicalCardEntity.setNotes("Test card");
    }

    @Test
    void listPhysicalCards_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<PhysicalCardDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listPhysicalCards(cardId, paginationRequest))
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
    void createPhysicalCard_Success() {
        // Arrange
        when(mapper.toEntity(any(PhysicalCardDTO.class))).thenReturn(physicalCardEntity);
        when(repository.save(any(PhysicalCard.class))).thenReturn(Mono.just(physicalCardEntity));
        when(mapper.toDTO(any(PhysicalCard.class))).thenReturn(physicalCardDTO);

        // Act & Assert
        StepVerifier.create(service.createPhysicalCard(cardId, physicalCardDTO))
                .expectNext(physicalCardDTO)
                .verifyComplete();

        verify(mapper).toEntity(physicalCardDTO);
        verify(repository).save(physicalCardEntity);
        verify(mapper).toDTO(physicalCardEntity);
    }

    @Test
    void getPhysicalCard_Success() {
        // Arrange
        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.just(physicalCardEntity));
        when(mapper.toDTO(any(PhysicalCard.class))).thenReturn(physicalCardDTO);

        // Act & Assert
        StepVerifier.create(service.getPhysicalCard(cardId, physicalCardId))
                .expectNext(physicalCardDTO)
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(mapper).toDTO(physicalCardEntity);
    }

    @Test
    void getPhysicalCard_NotFound() {
        // Arrange
        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getPhysicalCard(cardId, physicalCardId))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(mapper, never()).toDTO(any(PhysicalCard.class));
    }

    @Test
    void getPhysicalCard_WrongCard() {
        // Arrange
        PhysicalCard wrongCardPhysicalCard = new PhysicalCard();
        wrongCardPhysicalCard.setPhysicalCardId(physicalCardId);
        wrongCardPhysicalCard.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.just(wrongCardPhysicalCard));

        // Act & Assert
        StepVerifier.create(service.getPhysicalCard(cardId, physicalCardId))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(mapper, never()).toDTO(any(PhysicalCard.class));
    }

    @Test
    void updatePhysicalCard_Success() {
        // Arrange
        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.just(physicalCardEntity));
        when(mapper.toEntity(any(PhysicalCardDTO.class))).thenReturn(physicalCardEntity);
        when(repository.save(any(PhysicalCard.class))).thenReturn(Mono.just(physicalCardEntity));
        when(mapper.toDTO(any(PhysicalCard.class))).thenReturn(physicalCardDTO);

        // Act & Assert
        StepVerifier.create(service.updatePhysicalCard(cardId, physicalCardId, physicalCardDTO))
                .expectNext(physicalCardDTO)
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(mapper).toEntity(physicalCardDTO);
        verify(repository).save(physicalCardEntity);
        verify(mapper).toDTO(physicalCardEntity);
    }

    @Test
    void updatePhysicalCard_NotFound() {
        // Arrange
        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updatePhysicalCard(cardId, physicalCardId, physicalCardDTO))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(mapper, never()).toEntity(any(PhysicalCardDTO.class));
        verify(repository, never()).save(any(PhysicalCard.class));
        verify(mapper, never()).toDTO(any(PhysicalCard.class));
    }

    @Test
    void updatePhysicalCard_WrongCard() {
        // Arrange
        PhysicalCard wrongCardPhysicalCard = new PhysicalCard();
        wrongCardPhysicalCard.setPhysicalCardId(physicalCardId);
        wrongCardPhysicalCard.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.just(wrongCardPhysicalCard));

        // Act & Assert
        StepVerifier.create(service.updatePhysicalCard(cardId, physicalCardId, physicalCardDTO))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(mapper, never()).toEntity(any(PhysicalCardDTO.class));
        verify(repository, never()).save(any(PhysicalCard.class));
        verify(mapper, never()).toDTO(any(PhysicalCard.class));
    }

    @Test
    void deletePhysicalCard_Success() {
        // Arrange
        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.just(physicalCardEntity));
        when(repository.delete(physicalCardEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deletePhysicalCard(cardId, physicalCardId))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(repository).delete(physicalCardEntity);
    }

    @Test
    void deletePhysicalCard_NotFound() {
        // Arrange
        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deletePhysicalCard(cardId, physicalCardId))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(repository, never()).delete(any(PhysicalCard.class));
    }

    @Test
    void deletePhysicalCard_WrongCard() {
        // Arrange
        PhysicalCard wrongCardPhysicalCard = new PhysicalCard();
        wrongCardPhysicalCard.setPhysicalCardId(physicalCardId);
        wrongCardPhysicalCard.setCardId(UUID.randomUUID()); // Different card ID

        when(repository.findByPhysicalCardId(physicalCardId)).thenReturn(Mono.just(wrongCardPhysicalCard));

        // Act & Assert
        StepVerifier.create(service.deletePhysicalCard(cardId, physicalCardId))
                .verifyComplete();

        verify(repository).findByPhysicalCardId(physicalCardId);
        verify(repository, never()).delete(any(PhysicalCard.class));
    }
}