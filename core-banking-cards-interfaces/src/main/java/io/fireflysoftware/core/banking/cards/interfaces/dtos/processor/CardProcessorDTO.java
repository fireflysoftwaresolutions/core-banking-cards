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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.processor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Processor.
 * Contains the essential information about a Card Processor that needs to be exposed through the API.
 * A Card Processor represents entities that process card transactions between acquirers, card networks, and issuers,
 * including processor details, capabilities, and relationships.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardProcessorDTO extends BaseDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID processorId;

    @NotBlank(message = "Processor reference is required")
    @Size(max = 100, message = "Processor reference cannot exceed 100 characters")
    private String processorReference;

    @NotBlank(message = "Processor name is required")
    @Size(min = 2, max = 100, message = "Processor name must be between 2 and 100 characters")
    private String processorName;

    @NotBlank(message = "Processor legal name is required")
    @Size(min = 2, max = 150, message = "Processor legal name must be between 2 and 150 characters")
    private String processorLegalName;

    @NotBlank(message = "Processor code is required")
    @Pattern(regexp = "^[A-Z0-9_]{2,20}$", message = "Processor code must be 2-20 characters, uppercase letters, numbers, and underscores only")
    private String processorCode;

    @Size(max = 500, message = "Processor description cannot exceed 500 characters")
    private String processorDescription;

    @NotBlank(message = "Processor type is required")
    @Size(max = 50, message = "Processor type cannot exceed 50 characters")
    private String processorType;

    @NotBlank(message = "Processor status is required")
    @Size(max = 50, message = "Processor status cannot exceed 50 characters")
    private String processorStatus;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @PastOrPresent(message = "Activation date cannot be in the future")
    private LocalDateTime activationDate;

    @PastOrPresent(message = "Deactivation date cannot be in the future")
    private LocalDateTime deactivationDate;

    @Size(max = 255, message = "Deactivation reason cannot exceed 255 characters")
    private String deactivationReason;

    @Size(max = 50, message = "Tax ID cannot exceed 50 characters")
    private String taxId;

    @Size(max = 50, message = "Registration number cannot exceed 50 characters")
    private String registrationNumber;

    @Size(max = 50, message = "License number cannot exceed 50 characters")
    private String licenseNumber;

    @Future(message = "License expiry date must be in the future")
    private LocalDateTime licenseExpiryDate;

    @Pattern(regexp = "^https?://.*", message = "Website URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Website URL cannot exceed 500 characters")
    private String websiteUrl;

    @Pattern(regexp = "^https?://.*", message = "Logo URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Logo URL cannot exceed 500 characters")
    private String logoUrl;
    
    @Size(max = 100, message = "Address line 1 cannot exceed 100 characters")
    private String addressLine1;

    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    private String addressLine2;

    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be a 2-letter ISO code")
    private String country;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be a valid international phone number")
    private String phone;

    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 100, message = "Contact person name cannot exceed 100 characters")
    private String contactPersonName;

    @Size(max = 100, message = "Contact person title cannot exceed 100 characters")
    private String contactPersonTitle;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Contact person phone must be a valid international phone number")
    private String contactPersonPhone;

    @Email(message = "Contact person email must be a valid email address")
    @Size(max = 100, message = "Contact person email cannot exceed 100 characters")
    private String contactPersonEmail;

    @Size(max = 500, message = "Supported card networks cannot exceed 500 characters")
    private String supportedCardNetworks;

    @Size(max = 500, message = "Supported payment methods cannot exceed 500 characters")
    private String supportedPaymentMethods;

    @Size(max = 500, message = "Supported currencies cannot exceed 500 characters")
    private String supportedCurrencies;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Default currency must be a 3-letter ISO code")
    private String defaultCurrency;

    @Size(max = 500, message = "Supported countries cannot exceed 500 characters")
    private String supportedCountries;

    @NotNull(message = "International flag is required")
    private Boolean isInternational;

    @NotNull(message = "Domestic flag is required")
    private Boolean isDomestic;

    @NotNull(message = "Issuer processor flag is required")
    private Boolean isIssuerProcessor;

    @NotNull(message = "Acquirer processor flag is required")
    private Boolean isAcquirerProcessor;

    @NotNull(message = "Switch processor flag is required")
    private Boolean isSwitchProcessor;

    @NotNull(message = "Gateway flag is required")
    private Boolean isGateway;
    
    private String apiBaseUrl;
    
    private String apiVersion;
    
    private String apiKey;
    
    private String apiSecret;
    
    private String apiUsername;
    
    private String apiPassword;
    
    private String apiCertificate;
    
    private String webhookUrl;
    
    private String webhookSecret;
    
    private Integer acquirerCount;
    
    private Integer issuerCount;
    
    private Integer merchantCount;
    
    private Integer terminalCount;
    
    private Long transactionVolumeDailyAvg;
    
    private Long transactionValueDailyAvg;
    
    private Integer processingTimeAvgMs;
    
    private Double uptimePercentage;
    
    private LocalDateTime lastDowntimeDate;
    
    private Integer lastDowntimeDurationMinutes;
    
    private String settlementFrequency;
    
    private Integer settlementDelayDays;
    
    private String feeStructure;
    
    private Double processingFeePercentage;
    
    private Double processingFeeFlat;
    
    private Double chargebackFee;
    
    private Double refundFee;
    
    private LocalDateTime contractStartDate;
    
    private LocalDateTime contractEndDate;
    
    private LocalDateTime contractRenewalDate;
    
    private String contractStatus;
    
    private Boolean isPciCompliant;
    
    private String pciComplianceLevel;
    
    private LocalDateTime pciComplianceDate;
    
    private LocalDateTime pciComplianceExpiry;
    
    private Boolean isEmvCompliant;
    
    private LocalDateTime emvComplianceDate;
    
    private Boolean isPaDssCompliant;
    
    private LocalDateTime paDssComplianceDate;
    
    private LocalDateTime paDssComplianceExpiry;
    
    private Boolean supportsTokenization;
    
    private Boolean supports3dSecure;
    
    private Boolean supportsEmv;
    
    private Boolean supportsContactless;
    
    private Boolean supportsApplePay;
    
    private Boolean supportsGooglePay;
    
    private Boolean supportsSamsungPay;
    
    private LocalDateTime lastAuditDate;
    
    private LocalDateTime nextAuditDate;
}