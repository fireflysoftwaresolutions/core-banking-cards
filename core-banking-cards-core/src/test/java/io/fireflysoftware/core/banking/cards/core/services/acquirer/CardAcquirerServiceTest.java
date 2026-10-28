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


package io.fireflysoftware.core.banking.cards.core.services.acquirer;

import io.fireflysoftware.core.banking.cards.core.services.acquirer.impl.CardAcquirerServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.acquirer.CardAcquirerMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.acquirer.CardAcquirerDTO;
import io.fireflysoftware.core.banking.cards.models.entities.acquirer.CardAcquirer;
import io.fireflysoftware.core.banking.cards.models.repositories.acquirer.CardAcquirerRepository;
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
public class CardAcquirerServiceTest {

    @Mock
    private CardAcquirerRepository repository;

    @Mock
    private CardAcquirerMapper mapper;

    @InjectMocks
    private CardAcquirerServiceImpl service;

    private CardAcquirerDTO acquirerDTO;
    private CardAcquirer acquirerEntity;
    private final UUID acquirerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();

        acquirerDTO = CardAcquirerDTO.builder()
                .acquirerId(acquirerId)
                .acquirerReference("ACQ-REF-001")
                .acquirerName("Test Acquirer")
                .acquirerLegalName("Test Acquirer Inc.")
                .acquirerCode("TEST-ACQ")
                .acquirerDescription("Test acquirer description")
                .acquirerType("BANK")
                .acquirerStatus("ACTIVE")
                .isActive(true)
                .activationDate(now)
                .taxId("123456789")
                .registrationNumber("REG-12345")
                .licenseNumber("LIC-12345")
                .licenseExpiryDate(now.plusYears(2))
                .websiteUrl("https://testacquirer.com")
                .logoUrl("https://testacquirer.com/logo.png")
                .addressLine1("123 Main St")
                .city("Test City")
                .state("Test State")
                .postalCode("12345")
                .country("USA")
                .phone("+1-555-123-4567")
                .email("contact@testacquirer.com")
                .contactPersonName("John Doe")
                .contactPersonTitle("Account Manager")
                .contactPersonPhone("+1-555-987-6543")
                .contactPersonEmail("john.doe@testacquirer.com")
                .supportedCardNetworks("VISA,MASTERCARD,AMEX")
                .supportedPaymentMethods("CREDIT,DEBIT,PREPAID")
                .supportedCurrencies("USD,EUR,GBP")
                .defaultCurrency("USD")
                .supportedCountries("USA,CAN,MEX")
                .isInternational(true)
                .isDomestic(true)
                .isOnline(true)
                .isPhysical(true)
                .isMobile(true)
                .processorId(UUID.randomUUID())
                .processorName("Test Processor")
                .gatewayId(UUID.randomUUID())
                .gatewayName("Test Gateway")
                .apiBaseUrl("https://api.testacquirer.com")
                .apiVersion("v1")
                .apiKey("api-key-123")
                .apiSecret("api-secret-456")
                .apiUsername("testuser")
                .apiPassword("testpass")
                .apiCertificate("cert-data")
                .webhookUrl("https://webhook.testacquirer.com")
                .webhookSecret("webhook-secret")
                .merchantCount(100)
                .terminalCount(500)
                .transactionVolumeDailyAvg(10000L)
                .transactionValueDailyAvg(1000000L)
                .settlementFrequency("DAILY")
                .settlementDelayDays(1)
                .feeStructure("TIERED")
                .interchangeFeePercentage(1.5)
                .processingFeePercentage(0.5)
                .processingFeeFlat(0.25)
                .chargebackFee(25.0)
                .refundFee(5.0)
                .contractStartDate(now.minusYears(1))
                .contractEndDate(now.plusYears(2))
                .contractRenewalDate(now.plusYears(2))
                .contractStatus("ACTIVE")
                .isPciCompliant(true)
                .pciComplianceDate(now.minusMonths(6))
                .pciComplianceExpiry(now.plusMonths(6))
                .isEmvCompliant(true)
                .emvComplianceDate(now.minusMonths(6))
                .lastAuditDate(now.minusMonths(3))
                .build();

        acquirerEntity = new CardAcquirer();
        acquirerEntity.setAcquirerId(acquirerId);
        acquirerEntity.setAcquirerReference("ACQ-REF-001");
        acquirerEntity.setAcquirerName("Test Acquirer");
        acquirerEntity.setAcquirerLegalName("Test Acquirer Inc.");
        acquirerEntity.setAcquirerCode("TEST-ACQ");
        acquirerEntity.setAcquirerDescription("Test acquirer description");
        acquirerEntity.setAcquirerType("BANK");
        acquirerEntity.setAcquirerStatus("ACTIVE");
        acquirerEntity.setIsActive(true);
        acquirerEntity.setActivationDate(now);
        acquirerEntity.setTaxId("123456789");
        acquirerEntity.setRegistrationNumber("REG-12345");
        acquirerEntity.setLicenseNumber("LIC-12345");
        acquirerEntity.setLicenseExpiryDate(now.plusYears(2));
        acquirerEntity.setWebsiteUrl("https://testacquirer.com");
        acquirerEntity.setLogoUrl("https://testacquirer.com/logo.png");
        acquirerEntity.setAddressLine1("123 Main St");
        acquirerEntity.setCity("Test City");
        acquirerEntity.setState("Test State");
        acquirerEntity.setPostalCode("12345");
        acquirerEntity.setCountry("USA");
        acquirerEntity.setPhone("+1-555-123-4567");
        acquirerEntity.setEmail("contact@testacquirer.com");
        acquirerEntity.setContactPersonName("John Doe");
        acquirerEntity.setContactPersonTitle("Account Manager");
        acquirerEntity.setContactPersonPhone("+1-555-987-6543");
        acquirerEntity.setContactPersonEmail("john.doe@testacquirer.com");
        acquirerEntity.setSupportedCardNetworks("VISA,MASTERCARD,AMEX");
        acquirerEntity.setSupportedPaymentMethods("CREDIT,DEBIT,PREPAID");
        acquirerEntity.setSupportedCurrencies("USD,EUR,GBP");
        acquirerEntity.setDefaultCurrency("USD");
        acquirerEntity.setSupportedCountries("USA,CAN,MEX");
        acquirerEntity.setIsInternational(true);
        acquirerEntity.setIsDomestic(true);
        acquirerEntity.setIsOnline(true);
        acquirerEntity.setIsPhysical(true);
        acquirerEntity.setIsMobile(true);
        acquirerEntity.setProcessorId(UUID.randomUUID());
        acquirerEntity.setProcessorName("Test Processor");
        acquirerEntity.setGatewayId(UUID.randomUUID());
        acquirerEntity.setGatewayName("Test Gateway");
        acquirerEntity.setApiBaseUrl("https://api.testacquirer.com");
        acquirerEntity.setApiVersion("v1");
        acquirerEntity.setApiKey("api-key-123");
        acquirerEntity.setApiSecret("api-secret-456");
        acquirerEntity.setApiUsername("testuser");
        acquirerEntity.setApiPassword("testpass");
        acquirerEntity.setApiCertificate("cert-data");
        acquirerEntity.setWebhookUrl("https://webhook.testacquirer.com");
        acquirerEntity.setWebhookSecret("webhook-secret");
        acquirerEntity.setMerchantCount(100);
        acquirerEntity.setTerminalCount(500);
        acquirerEntity.setTransactionVolumeDailyAvg(10000L);
        acquirerEntity.setTransactionValueDailyAvg(1000000L);
        acquirerEntity.setSettlementFrequency("DAILY");
        acquirerEntity.setSettlementDelayDays(1);
        acquirerEntity.setFeeStructure("TIERED");
        acquirerEntity.setInterchangeFeePercentage(1.5);
        acquirerEntity.setProcessingFeePercentage(0.5);
        acquirerEntity.setProcessingFeeFlat(0.25);
        acquirerEntity.setChargebackFee(25.0);
        acquirerEntity.setRefundFee(5.0);
        acquirerEntity.setContractStartDate(now.minusYears(1));
        acquirerEntity.setContractEndDate(now.plusYears(2));
        acquirerEntity.setContractRenewalDate(now.plusYears(2));
        acquirerEntity.setContractStatus("ACTIVE");
        acquirerEntity.setIsPciCompliant(true);
        acquirerEntity.setPciComplianceDate(now.minusMonths(6));
        acquirerEntity.setPciComplianceExpiry(now.plusMonths(6));
        acquirerEntity.setIsEmvCompliant(true);
        acquirerEntity.setEmvComplianceDate(now.minusMonths(6));
        acquirerEntity.setLastAuditDate(now.minusMonths(3));
    }

    @Test
    void listAcquirers_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardAcquirerDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listAcquirers(paginationRequest))
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
    void createAcquirer_Success() {
        // Arrange
        when(mapper.toEntity(any(CardAcquirerDTO.class))).thenReturn(acquirerEntity);
        when(repository.save(any(CardAcquirer.class))).thenReturn(Mono.just(acquirerEntity));
        when(mapper.toDTO(any(CardAcquirer.class))).thenReturn(acquirerDTO);

        // Act & Assert
        StepVerifier.create(service.createAcquirer(acquirerDTO))
                .expectNext(acquirerDTO)
                .verifyComplete();

        verify(mapper).toEntity(acquirerDTO);
        verify(repository).save(acquirerEntity);
        verify(mapper).toDTO(acquirerEntity);
    }

    @Test
    void getAcquirer_Success() {
        // Arrange
        when(repository.findByAcquirerId(acquirerId)).thenReturn(Mono.just(acquirerEntity));
        when(mapper.toDTO(any(CardAcquirer.class))).thenReturn(acquirerDTO);

        // Act & Assert
        StepVerifier.create(service.getAcquirer(acquirerId))
                .expectNext(acquirerDTO)
                .verifyComplete();

        verify(repository).findByAcquirerId(acquirerId);
        verify(mapper).toDTO(acquirerEntity);
    }

    @Test
    void getAcquirer_NotFound() {
        // Arrange
        when(repository.findByAcquirerId(acquirerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getAcquirer(acquirerId))
                .verifyComplete();

        verify(repository).findByAcquirerId(acquirerId);
        verify(mapper, never()).toDTO(any(CardAcquirer.class));
    }

    @Test
    void updateAcquirer_Success() {
        // Arrange
        when(repository.findByAcquirerId(acquirerId)).thenReturn(Mono.just(acquirerEntity));
        when(mapper.toEntity(any(CardAcquirerDTO.class))).thenReturn(acquirerEntity);
        when(repository.save(any(CardAcquirer.class))).thenReturn(Mono.just(acquirerEntity));
        when(mapper.toDTO(any(CardAcquirer.class))).thenReturn(acquirerDTO);

        // Act & Assert
        StepVerifier.create(service.updateAcquirer(acquirerId, acquirerDTO))
                .expectNext(acquirerDTO)
                .verifyComplete();

        verify(repository).findByAcquirerId(acquirerId);
        verify(mapper).toEntity(acquirerDTO);
        verify(repository).save(acquirerEntity);
        verify(mapper).toDTO(acquirerEntity);
    }

    @Test
    void updateAcquirer_NotFound() {
        // Arrange
        when(repository.findByAcquirerId(acquirerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateAcquirer(acquirerId, acquirerDTO))
                .verifyComplete();

        verify(repository).findByAcquirerId(acquirerId);
        verify(mapper, never()).toEntity(any(CardAcquirerDTO.class));
        verify(repository, never()).save(any(CardAcquirer.class));
        verify(mapper, never()).toDTO(any(CardAcquirer.class));
    }

    @Test
    void deleteAcquirer_Success() {
        // Arrange
        when(repository.findByAcquirerId(acquirerId)).thenReturn(Mono.just(acquirerEntity));
        when(repository.delete(acquirerEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteAcquirer(acquirerId))
                .verifyComplete();

        verify(repository).findByAcquirerId(acquirerId);
        verify(repository).delete(acquirerEntity);
    }

    @Test
    void deleteAcquirer_NotFound() {
        // Arrange
        when(repository.findByAcquirerId(acquirerId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteAcquirer(acquirerId))
                .verifyComplete();

        verify(repository).findByAcquirerId(acquirerId);
        verify(repository, never()).delete(any(CardAcquirer.class));
    }
}
