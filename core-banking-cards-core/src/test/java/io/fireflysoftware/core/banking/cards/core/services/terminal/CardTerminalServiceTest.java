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


package io.fireflysoftware.core.banking.cards.core.services.terminal;

import io.fireflysoftware.core.banking.cards.core.services.terminal.impl.CardTerminalServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.terminal.CardTerminalMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.terminal.CardTerminalDTO;
import io.fireflysoftware.core.banking.cards.models.entities.terminal.CardTerminal;
import io.fireflysoftware.core.banking.cards.models.repositories.terminal.CardTerminalRepository;
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
public class CardTerminalServiceTest {

    @Mock
    private CardTerminalRepository repository;

    @Mock
    private CardTerminalMapper mapper;

    @InjectMocks
    private CardTerminalServiceImpl service;

    private CardTerminalDTO terminalDTO;
    private CardTerminal terminalEntity;
    private final UUID terminalId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();

        terminalDTO = CardTerminalDTO.builder()
                .terminalId(terminalId)
                .terminalReference("TERM-REF-001")
                .terminalSerialNumber("SN12345678")
                .terminalName("Test Terminal")
                .terminalDescription("Test terminal description")
                .terminalType("POS")
                .terminalModel("Model X")
                .terminalManufacturer("Manufacturer Y")
                .terminalStatus("ACTIVE")
                .isActive(true)
                .activationDate(now)
                .merchantId(UUID.randomUUID())
                .merchantName("Test Merchant")
                .merchantLocationId("LOC-001")
                .merchantLocationName("Main Store")
                .addressLine1("123 Main St")
                .city("Test City")
                .state("Test State")
                .postalCode("12345")
                .country("USA")
                .geolocation("40.7128,-74.0060")
                .acquirerId("ACQ-001")
                .acquirerName("Test Acquirer")
                .processorId("PROC-001")
                .processorName("Test Processor")
                .softwareVersion("1.0.0")
                .firmwareVersion("2.0.0")
                .hardwareVersion("3.0.0")
                .lastMaintenanceDate(now.minusMonths(1))
                .nextMaintenanceDate(now.plusMonths(5))
                .lastUpdateDate(now.minusWeeks(2))
                .installationDate(now.minusYears(1))
                .isPhysical(true)
                .isVirtual(false)
                .isMobile(false)
                .isAttended(true)
                .isUnattended(false)
                .isContactless(true)
                .isChip(true)
                .isMagstripe(true)
                .isPinSupported(true)
                .isSignatureSupported(true)
                .isBiometricSupported(false)
                .isNfcSupported(true)
                .isQrSupported(true)
                .isEmvSupported(true)
                .isP2peSupported(true)
                .isTokenizationSupported(true)
                .supportedCardNetworks("VISA,MASTERCARD,AMEX")
                .supportedPaymentMethods("CREDIT,DEBIT,PREPAID")
                .supportedCurrencies("USD,EUR,GBP")
                .defaultCurrency("USD")
                .communicationType("IP")
                .connectionType("ETHERNET")
                .ipAddress("192.168.1.100")
                .macAddress("00:11:22:33:44:55")
                .isPciCompliant(true)
                .pciComplianceDate(now.minusMonths(6))
                .pciComplianceExpiry(now.plusMonths(6))
                .encryptionMethod("AES-256")
                .keyManagementScheme("DUKPT")
                .lastKeyRotationDate(now.minusMonths(1))
                .nextKeyRotationDate(now.plusMonths(2))
                .isFaultDetected(false)
                .lastTransactionTimestamp(now.minusDays(1))
                .lastTransactionId("TXN-12345")
                .lastTransactionStatus("APPROVED")
                .notes("Test terminal notes")
                .build();

        terminalEntity = new CardTerminal();
        terminalEntity.setTerminalId(terminalId);
        terminalEntity.setTerminalReference("TERM-REF-001");
        terminalEntity.setTerminalSerialNumber("SN12345678");
        terminalEntity.setTerminalName("Test Terminal");
        terminalEntity.setTerminalDescription("Test terminal description");
        terminalEntity.setTerminalType("POS");
        terminalEntity.setTerminalModel("Model X");
        terminalEntity.setTerminalManufacturer("Manufacturer Y");
        terminalEntity.setTerminalStatus("ACTIVE");
        terminalEntity.setIsActive(true);
        terminalEntity.setActivationDate(now);
        terminalEntity.setMerchantId(UUID.randomUUID());
        terminalEntity.setMerchantName("Test Merchant");
        terminalEntity.setMerchantLocationId("LOC-001");
        terminalEntity.setMerchantLocationName("Main Store");
        terminalEntity.setAddressLine1("123 Main St");
        terminalEntity.setCity("Test City");
        terminalEntity.setState("Test State");
        terminalEntity.setPostalCode("12345");
        terminalEntity.setCountry("USA");
        terminalEntity.setGeolocation("40.7128,-74.0060");
        terminalEntity.setAcquirerId("ACQ-001");
        terminalEntity.setAcquirerName("Test Acquirer");
        terminalEntity.setProcessorId("PROC-001");
        terminalEntity.setProcessorName("Test Processor");
        terminalEntity.setSoftwareVersion("1.0.0");
        terminalEntity.setFirmwareVersion("2.0.0");
        terminalEntity.setHardwareVersion("3.0.0");
        terminalEntity.setLastMaintenanceDate(now.minusMonths(1));
        terminalEntity.setNextMaintenanceDate(now.plusMonths(5));
        terminalEntity.setLastUpdateDate(now.minusWeeks(2));
        terminalEntity.setInstallationDate(now.minusYears(1));
        terminalEntity.setIsPhysical(true);
        terminalEntity.setIsVirtual(false);
        terminalEntity.setIsMobile(false);
        terminalEntity.setIsAttended(true);
        terminalEntity.setIsUnattended(false);
        terminalEntity.setIsContactless(true);
        terminalEntity.setIsChip(true);
        terminalEntity.setIsMagstripe(true);
        terminalEntity.setIsPinSupported(true);
        terminalEntity.setIsSignatureSupported(true);
        terminalEntity.setIsBiometricSupported(false);
        terminalEntity.setIsNfcSupported(true);
        terminalEntity.setIsQrSupported(true);
        terminalEntity.setIsEmvSupported(true);
        terminalEntity.setIsP2peSupported(true);
        terminalEntity.setIsTokenizationSupported(true);
        terminalEntity.setSupportedCardNetworks("VISA,MASTERCARD,AMEX");
        terminalEntity.setSupportedPaymentMethods("CREDIT,DEBIT,PREPAID");
        terminalEntity.setSupportedCurrencies("USD,EUR,GBP");
        terminalEntity.setDefaultCurrency("USD");
        terminalEntity.setCommunicationType("IP");
        terminalEntity.setConnectionType("ETHERNET");
        terminalEntity.setIpAddress("192.168.1.100");
        terminalEntity.setMacAddress("00:11:22:33:44:55");
        terminalEntity.setIsPciCompliant(true);
        terminalEntity.setPciComplianceDate(now.minusMonths(6));
        terminalEntity.setPciComplianceExpiry(now.plusMonths(6));
        terminalEntity.setEncryptionMethod("AES-256");
        terminalEntity.setKeyManagementScheme("DUKPT");
        terminalEntity.setLastKeyRotationDate(now.minusMonths(1));
        terminalEntity.setNextKeyRotationDate(now.plusMonths(2));
        terminalEntity.setIsFaultDetected(false);
        terminalEntity.setLastTransactionTimestamp(now.minusDays(1));
        terminalEntity.setLastTransactionId("TXN-12345");
        terminalEntity.setLastTransactionStatus("APPROVED");
        terminalEntity.setNotes("Test terminal notes");
    }

    @Test
    void listTerminals_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardTerminalDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listTerminals(paginationRequest))
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
    void createTerminal_Success() {
        // Arrange
        when(mapper.toEntity(any(CardTerminalDTO.class))).thenReturn(terminalEntity);
        when(repository.save(any(CardTerminal.class))).thenReturn(Mono.just(terminalEntity));
        when(mapper.toDTO(any(CardTerminal.class))).thenReturn(terminalDTO);

        // Act & Assert
        StepVerifier.create(service.createTerminal(terminalDTO))
                .expectNext(terminalDTO)
                .verifyComplete();

        verify(mapper).toEntity(terminalDTO);
        verify(repository).save(terminalEntity);
        verify(mapper).toDTO(terminalEntity);
    }

    @Test
    void getTerminal_Success() {
        // Arrange
        when(repository.findByTerminalId(terminalId)).thenReturn(Mono.just(terminalEntity));
        when(mapper.toDTO(any(CardTerminal.class))).thenReturn(terminalDTO);

        // Act & Assert
        StepVerifier.create(service.getTerminal(terminalId))
                .expectNext(terminalDTO)
                .verifyComplete();

        verify(repository).findByTerminalId(terminalId);
        verify(mapper).toDTO(terminalEntity);
    }

    @Test
    void getTerminal_NotFound() {
        // Arrange
        when(repository.findByTerminalId(terminalId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getTerminal(terminalId))
                .verifyComplete();

        verify(repository).findByTerminalId(terminalId);
        verify(mapper, never()).toDTO(any(CardTerminal.class));
    }

    @Test
    void updateTerminal_Success() {
        // Arrange
        when(repository.findByTerminalId(terminalId)).thenReturn(Mono.just(terminalEntity));
        when(mapper.toEntity(any(CardTerminalDTO.class))).thenReturn(terminalEntity);
        when(repository.save(any(CardTerminal.class))).thenReturn(Mono.just(terminalEntity));
        when(mapper.toDTO(any(CardTerminal.class))).thenReturn(terminalDTO);

        // Act & Assert
        StepVerifier.create(service.updateTerminal(terminalId, terminalDTO))
                .expectNext(terminalDTO)
                .verifyComplete();

        verify(repository).findByTerminalId(terminalId);
        verify(mapper).toEntity(terminalDTO);
        verify(repository).save(terminalEntity);
        verify(mapper).toDTO(terminalEntity);
    }

    @Test
    void updateTerminal_NotFound() {
        // Arrange
        when(repository.findByTerminalId(terminalId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateTerminal(terminalId, terminalDTO))
                .verifyComplete();

        verify(repository).findByTerminalId(terminalId);
        verify(mapper, never()).toEntity(any(CardTerminalDTO.class));
        verify(repository, never()).save(any(CardTerminal.class));
        verify(mapper, never()).toDTO(any(CardTerminal.class));
    }

    @Test
    void deleteTerminal_Success() {
        // Arrange
        when(repository.findByTerminalId(terminalId)).thenReturn(Mono.just(terminalEntity));
        when(repository.delete(terminalEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteTerminal(terminalId))
                .verifyComplete();

        verify(repository).findByTerminalId(terminalId);
        verify(repository).delete(terminalEntity);
    }

    @Test
    void deleteTerminal_NotFound() {
        // Arrange
        when(repository.findByTerminalId(terminalId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteTerminal(terminalId))
                .verifyComplete();

        verify(repository).findByTerminalId(terminalId);
        verify(repository, never()).delete(any(CardTerminal.class));
    }
}
