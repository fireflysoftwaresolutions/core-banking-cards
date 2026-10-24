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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.acquirer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Acquirer.
 * Contains the essential information about a Card Acquirer that needs to be exposed through the API.
 * A Card Acquirer represents financial institutions that process card payments on behalf of merchants,
 * including acquirer details, relationships with card networks, and processing capabilities.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardAcquirerDTO extends BaseDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID acquirerId;
    
    private String acquirerReference;
    
    private String acquirerName;
    
    private String acquirerLegalName;
    
    private String acquirerCode;
    
    private String acquirerDescription;
    
    private String acquirerType;
    
    private String acquirerStatus;
    
    private Boolean isActive;
    
    private LocalDateTime activationDate;
    
    private LocalDateTime deactivationDate;
    
    private String deactivationReason;
    
    private String taxId;
    
    private String registrationNumber;
    
    private String licenseNumber;
    
    private LocalDateTime licenseExpiryDate;
    
    private String websiteUrl;
    
    private String logoUrl;
    
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String country;
    
    private String phone;
    
    private String email;
    
    private String contactPersonName;
    
    private String contactPersonTitle;
    
    private String contactPersonPhone;
    
    private String contactPersonEmail;
    
    private String supportedCardNetworks;
    
    private String supportedPaymentMethods;
    
    private String supportedCurrencies;
    
    private String defaultCurrency;
    
    private String supportedCountries;
    
    private Boolean isInternational;
    
    private Boolean isDomestic;
    
    private Boolean isOnline;
    
    private Boolean isPhysical;
    
    private Boolean isMobile;
    
    @FilterableId
    private UUID processorId;
    
    private String processorName;
    
    @FilterableId
    private UUID gatewayId;
    
    private String gatewayName;
    
    private String apiBaseUrl;
    
    private String apiVersion;
    
    private String apiKey;
    
    private String apiSecret;
    
    private String apiUsername;
    
    private String apiPassword;
    
    private String apiCertificate;
    
    private String webhookUrl;
    
    private String webhookSecret;
    
    private Integer merchantCount;
    
    private Integer terminalCount;
    
    private Long transactionVolumeDailyAvg;
    
    private Long transactionValueDailyAvg;
    
    private String settlementFrequency;
    
    private Integer settlementDelayDays;
    
    private String feeStructure;
    
    private Double interchangeFeePercentage;
    
    private Double processingFeePercentage;
    
    private Double processingFeeFlat;
    
    private Double chargebackFee;
    
    private Double refundFee;
    
    private LocalDateTime contractStartDate;
    
    private LocalDateTime contractEndDate;
    
    private LocalDateTime contractRenewalDate;
    
    private String contractStatus;
    
    private Boolean isPciCompliant;
    
    private LocalDateTime pciComplianceDate;
    
    private LocalDateTime pciComplianceExpiry;
    
    private Boolean isEmvCompliant;
    
    private LocalDateTime emvComplianceDate;
    
    private LocalDateTime lastAuditDate;
    
    private LocalDateTime nextAuditDate;
}