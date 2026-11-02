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


package io.fireflysoftware.core.banking.cards.core.services.gateway;

import io.fireflysoftware.core.banking.cards.core.services.gateway.impl.CardGatewayServiceImpl;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.gateway.CardGatewayMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.gateway.CardGatewayDTO;
import io.fireflysoftware.core.banking.cards.models.entities.gateway.CardGateway;
import io.fireflysoftware.core.banking.cards.models.repositories.gateway.CardGatewayRepository;
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
public class CardGatewayServiceTest {

    @Mock
    private CardGatewayRepository repository;

    @Mock
    private CardGatewayMapper mapper;

    @InjectMocks
    private CardGatewayServiceImpl service;

    private CardGatewayDTO gatewayDTO;
    private CardGateway gatewayEntity;
    private final UUID gatewayId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDateTime now = LocalDateTime.now();

        gatewayDTO = CardGatewayDTO.builder()
                .gatewayId(gatewayId)
                .gatewayReference("GTW-REF-001")
                .gatewayName("Test Gateway")
                .gatewayLegalName("Test Gateway Inc.")
                .gatewayCode("TEST-GTW")
                .gatewayDescription("Test gateway description")
                .gatewayType("PAYMENT")
                .gatewayStatus("ACTIVE")
                .isActive(true)
                .activationDate(now)
                .taxId("123456789")
                .registrationNumber("REG-12345")
                .licenseNumber("LIC-12345")
                .licenseExpiryDate(now.plusYears(2))
                .websiteUrl("https://testgateway.com")
                .logoUrl("https://testgateway.com/logo.png")
                .addressLine1("123 Main St")
                .city("Test City")
                .state("Test State")
                .postalCode("12345")
                .country("USA")
                .phone("+1-555-123-4567")
                .email("contact@testgateway.com")
                .contactPersonName("John Doe")
                .contactPersonTitle("Account Manager")
                .contactPersonPhone("+1-555-987-6543")
                .contactPersonEmail("john.doe@testgateway.com")
                .supportedCardNetworks("VISA,MASTERCARD,AMEX")
                .supportedPaymentMethods("CREDIT,DEBIT,PREPAID")
                .supportedCurrencies("USD,EUR,GBP")
                .defaultCurrency("USD")
                .supportedCountries("USA,CAN,MEX")
                .isInternational(true)
                .isDomestic(true)
                .processorId(UUID.randomUUID())
                .processorName("Test Processor")
                .acquirerId(UUID.randomUUID())
                .acquirerName("Test Acquirer")
                .apiBaseUrl("https://api.testgateway.com")
                .apiVersion("v1")
                .apiKey("api-key-123")
                .apiSecret("api-secret-456")
                .apiUsername("testuser")
                .apiPassword("testpass")
                .apiCertificate("cert-data")
                .webhookUrl("https://webhook.testgateway.com")
                .webhookSecret("webhook-secret")
                .redirectUrl("https://redirect.testgateway.com")
                .callbackUrl("https://callback.testgateway.com")
                .hostedPageUrl("https://hosted.testgateway.com")
                .merchantCount(100)
                .transactionVolumeDailyAvg(10000L)
                .transactionValueDailyAvg(1000000L)
                .processingTimeAvgMs(250)
                .uptimePercentage(99.9)
                .settlementFrequency("DAILY")
                .settlementDelayDays(1)
                .feeStructure("TIERED")
                .processingFeePercentage(2.5)
                .processingFeeFlat(0.30)
                .chargebackFee(25.0)
                .refundFee(5.0)
                .contractStartDate(now.minusYears(1))
                .contractEndDate(now.plusYears(2))
                .contractRenewalDate(now.plusYears(2))
                .contractStatus("ACTIVE")
                .isPciCompliant(true)
                .pciComplianceLevel("PCI DSS 3.2.1")
                .pciComplianceDate(now.minusMonths(6))
                .pciComplianceExpiry(now.plusMonths(6))
                .build();

        gatewayEntity = new CardGateway();
        gatewayEntity.setGatewayId(gatewayId);
        gatewayEntity.setGatewayReference("GTW-REF-001");
        gatewayEntity.setGatewayName("Test Gateway");
        gatewayEntity.setGatewayLegalName("Test Gateway Inc.");
        gatewayEntity.setGatewayCode("TEST-GTW");
        gatewayEntity.setGatewayDescription("Test gateway description");
        gatewayEntity.setGatewayType("PAYMENT");
        gatewayEntity.setGatewayStatus("ACTIVE");
        gatewayEntity.setIsActive(true);
        gatewayEntity.setActivationDate(now);
        gatewayEntity.setTaxId("123456789");
        gatewayEntity.setRegistrationNumber("REG-12345");
        gatewayEntity.setLicenseNumber("LIC-12345");
        gatewayEntity.setLicenseExpiryDate(now.plusYears(2));
        gatewayEntity.setWebsiteUrl("https://testgateway.com");
        gatewayEntity.setLogoUrl("https://testgateway.com/logo.png");
        gatewayEntity.setAddressLine1("123 Main St");
        gatewayEntity.setCity("Test City");
        gatewayEntity.setState("Test State");
        gatewayEntity.setPostalCode("12345");
        gatewayEntity.setCountry("USA");
        gatewayEntity.setPhone("+1-555-123-4567");
        gatewayEntity.setEmail("contact@testgateway.com");
        gatewayEntity.setContactPersonName("John Doe");
        gatewayEntity.setContactPersonTitle("Account Manager");
        gatewayEntity.setContactPersonPhone("+1-555-987-6543");
        gatewayEntity.setContactPersonEmail("john.doe@testgateway.com");
        gatewayEntity.setSupportedCardNetworks("VISA,MASTERCARD,AMEX");
        gatewayEntity.setSupportedPaymentMethods("CREDIT,DEBIT,PREPAID");
        gatewayEntity.setSupportedCurrencies("USD,EUR,GBP");
        gatewayEntity.setDefaultCurrency("USD");
        gatewayEntity.setSupportedCountries("USA,CAN,MEX");
        gatewayEntity.setIsInternational(true);
        gatewayEntity.setIsDomestic(true);
        gatewayEntity.setProcessorId(UUID.randomUUID());
        gatewayEntity.setProcessorName("Test Processor");
        gatewayEntity.setAcquirerId(UUID.randomUUID());
        gatewayEntity.setAcquirerName("Test Acquirer");
        gatewayEntity.setApiBaseUrl("https://api.testgateway.com");
        gatewayEntity.setApiVersion("v1");
        gatewayEntity.setApiKey("api-key-123");
        gatewayEntity.setApiSecret("api-secret-456");
        gatewayEntity.setApiUsername("testuser");
        gatewayEntity.setApiPassword("testpass");
        gatewayEntity.setApiCertificate("cert-data");
        gatewayEntity.setWebhookUrl("https://webhook.testgateway.com");
        gatewayEntity.setWebhookSecret("webhook-secret");
        gatewayEntity.setRedirectUrl("https://redirect.testgateway.com");
        gatewayEntity.setCallbackUrl("https://callback.testgateway.com");
        gatewayEntity.setHostedPageUrl("https://hosted.testgateway.com");
        gatewayEntity.setMerchantCount(100);
        gatewayEntity.setTransactionVolumeDailyAvg(10000L);
        gatewayEntity.setTransactionValueDailyAvg(1000000L);
        gatewayEntity.setProcessingTimeAvgMs(250);
        gatewayEntity.setUptimePercentage(99.9);
        gatewayEntity.setSettlementFrequency("DAILY");
        gatewayEntity.setSettlementDelayDays(1);
        gatewayEntity.setFeeStructure("TIERED");
        gatewayEntity.setProcessingFeePercentage(2.5);
        gatewayEntity.setProcessingFeeFlat(0.30);
        gatewayEntity.setChargebackFee(25.0);
        gatewayEntity.setRefundFee(5.0);
        gatewayEntity.setContractStartDate(now.minusYears(1));
        gatewayEntity.setContractEndDate(now.plusYears(2));
        gatewayEntity.setContractRenewalDate(now.plusYears(2));
        gatewayEntity.setContractStatus("ACTIVE");
        gatewayEntity.setIsPciCompliant(true);
        gatewayEntity.setPciComplianceLevel("PCI DSS 3.2.1");
        gatewayEntity.setPciComplianceDate(now.minusMonths(6));
        gatewayEntity.setPciComplianceExpiry(now.plusMonths(6));
    }

    @Test
    void listGateways_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        @SuppressWarnings("unchecked")
        PaginationResponse<CardGatewayDTO> expectedResponse = mock(PaginationResponse.class);

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listGateways(paginationRequest))
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
    void createGateway_Success() {
        // Arrange
        when(mapper.toEntity(any(CardGatewayDTO.class))).thenReturn(gatewayEntity);
        when(repository.save(any(CardGateway.class))).thenReturn(Mono.just(gatewayEntity));
        when(mapper.toDTO(any(CardGateway.class))).thenReturn(gatewayDTO);

        // Act & Assert
        StepVerifier.create(service.createGateway(gatewayDTO))
                .expectNext(gatewayDTO)
                .verifyComplete();

        verify(mapper).toEntity(gatewayDTO);
        verify(repository).save(gatewayEntity);
        verify(mapper).toDTO(gatewayEntity);
    }

    @Test
    void getGateway_Success() {
        // Arrange
        when(repository.findByGatewayId(gatewayId)).thenReturn(Mono.just(gatewayEntity));
        when(mapper.toDTO(any(CardGateway.class))).thenReturn(gatewayDTO);

        // Act & Assert
        StepVerifier.create(service.getGateway(gatewayId))
                .expectNext(gatewayDTO)
                .verifyComplete();

        verify(repository).findByGatewayId(gatewayId);
        verify(mapper).toDTO(gatewayEntity);
    }

    @Test
    void getGateway_NotFound() {
        // Arrange
        when(repository.findByGatewayId(gatewayId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getGateway(gatewayId))
                .verifyComplete();

        verify(repository).findByGatewayId(gatewayId);
        verify(mapper, never()).toDTO(any(CardGateway.class));
    }

    @Test
    void updateGateway_Success() {
        // Arrange
        when(repository.findByGatewayId(gatewayId)).thenReturn(Mono.just(gatewayEntity));
        when(mapper.toEntity(any(CardGatewayDTO.class))).thenReturn(gatewayEntity);
        when(repository.save(any(CardGateway.class))).thenReturn(Mono.just(gatewayEntity));
        when(mapper.toDTO(any(CardGateway.class))).thenReturn(gatewayDTO);

        // Act & Assert
        StepVerifier.create(service.updateGateway(gatewayId, gatewayDTO))
                .expectNext(gatewayDTO)
                .verifyComplete();

        verify(repository).findByGatewayId(gatewayId);
        verify(mapper).toEntity(gatewayDTO);
        verify(repository).save(gatewayEntity);
        verify(mapper).toDTO(gatewayEntity);
    }

    @Test
    void updateGateway_NotFound() {
        // Arrange
        when(repository.findByGatewayId(gatewayId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateGateway(gatewayId, gatewayDTO))
                .verifyComplete();

        verify(repository).findByGatewayId(gatewayId);
        verify(mapper, never()).toEntity(any(CardGatewayDTO.class));
        verify(repository, never()).save(any(CardGateway.class));
        verify(mapper, never()).toDTO(any(CardGateway.class));
    }

    @Test
    void deleteGateway_Success() {
        // Arrange
        when(repository.findByGatewayId(gatewayId)).thenReturn(Mono.just(gatewayEntity));
        when(repository.delete(gatewayEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteGateway(gatewayId))
                .verifyComplete();

        verify(repository).findByGatewayId(gatewayId);
        verify(repository).delete(gatewayEntity);
    }

    @Test
    void deleteGateway_NotFound() {
        // Arrange
        when(repository.findByGatewayId(gatewayId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteGateway(gatewayId))
                .verifyComplete();

        verify(repository).findByGatewayId(gatewayId);
        verify(repository, never()).delete(any(CardGateway.class));
    }
}
