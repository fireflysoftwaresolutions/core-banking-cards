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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.merchant;

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
 * Data Transfer Object for Card Merchant.
 * Contains the essential information about a Card Merchant that needs to be exposed through the API.
 * A Card Merchant represents merchants that accept card payments, including merchant details, categories,
 * and relationships with card networks and issuers.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardMerchantDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID merchantId;

    @NotBlank(message = "Merchant reference is required")
    @Size(max = 100, message = "Merchant reference cannot exceed 100 characters")
    private String merchantReference;

    @NotBlank(message = "Merchant name is required")
    @Size(min = 2, max = 100, message = "Merchant name must be between 2 and 100 characters")
    private String merchantName;

    @NotBlank(message = "Merchant legal name is required")
    @Size(min = 2, max = 150, message = "Merchant legal name must be between 2 and 150 characters")
    private String merchantLegalName;

    @Size(max = 100, message = "Merchant display name cannot exceed 100 characters")
    private String merchantDisplayName;

    @Size(max = 500, message = "Merchant description cannot exceed 500 characters")
    private String merchantDescription;

    @NotBlank(message = "Merchant category code is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Merchant category code must be a 4-digit number")
    private String merchantCategoryCode;

    @NotBlank(message = "Merchant category name is required")
    @Size(min = 2, max = 100, message = "Merchant category name must be between 2 and 100 characters")
    private String merchantCategoryName;

    @NotBlank(message = "Merchant type is required")
    @Size(max = 50, message = "Merchant type cannot exceed 50 characters")
    private String merchantType;

    @NotBlank(message = "Merchant status is required")
    @Size(max = 50, message = "Merchant status cannot exceed 50 characters")
    private String merchantStatus;

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

    @Pattern(regexp = "^https?://.*", message = "Website URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Website URL cannot exceed 500 characters")
    private String websiteUrl;

    @Pattern(regexp = "^https?://.*", message = "Logo URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Logo URL cannot exceed 500 characters")
    private String logoUrl;

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 100, message = "Address line 1 cannot exceed 100 characters")
    private String addressLine1;

    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be a 2-letter ISO code")
    private String country;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be a valid international phone number")
    private String phone;

    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(min = 2, max = 100, message = "Contact person name must be between 2 and 100 characters")
    private String contactPersonName;

    @Size(max = 100, message = "Contact person title cannot exceed 100 characters")
    private String contactPersonTitle;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Contact person phone must be a valid international phone number")
    private String contactPersonPhone;

    @Email(message = "Contact person email must be a valid email address")
    @Size(max = 100, message = "Contact person email cannot exceed 100 characters")
    private String contactPersonEmail;

    @NotNull(message = "Online flag is required")
    private Boolean isOnline;

    @NotNull(message = "Physical flag is required")
    private Boolean isPhysical;

    @NotNull(message = "Mobile flag is required")
    private Boolean isMobile;

    @NotNull(message = "International flag is required")
    private Boolean isInternational;

    @Size(max = 500, message = "Supported currencies cannot exceed 500 characters")
    private String supportedCurrencies;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Default currency must be a 3-letter ISO code")
    private String defaultCurrency;

    @Size(max = 500, message = "Supported card networks cannot exceed 500 characters")
    private String supportedCardNetworks;

    @Size(max = 500, message = "Supported payment methods cannot exceed 500 characters")
    private String supportedPaymentMethods;

    @NotNull(message = "High risk flag is required")
    private Boolean isHighRisk;

    @Size(max = 50, message = "Risk rating cannot exceed 50 characters")
    private String riskRating;

    @Min(value = 0, message = "Risk score cannot be negative")
    @Max(value = 1000, message = "Risk score cannot exceed 1000")
    private Integer riskScore;

    @PastOrPresent(message = "Risk assessment date cannot be in the future")
    private LocalDateTime riskAssessmentDate;

    @NotNull(message = "Fraud suspected flag is required")
    private Boolean isFraudSuspected;

    @Size(max = 255, message = "Fraud reason cannot exceed 255 characters")
    private String fraudReason;

    @PastOrPresent(message = "Fraud report date cannot be in the future")
    private LocalDateTime fraudReportDate;

    @NotNull(message = "Blacklisted flag is required")
    private Boolean isBlacklisted;

    @Size(max = 255, message = "Blacklist reason cannot exceed 255 characters")
    private String blacklistReason;

    @PastOrPresent(message = "Blacklist date cannot be in the future")
    private LocalDateTime blacklistDate;

    @NotNull(message = "Settlement enabled flag is required")
    private Boolean isSettlementEnabled;

    @Size(max = 50, message = "Settlement frequency cannot exceed 50 characters")
    private String settlementFrequency;

    @Min(value = 1, message = "Settlement day must be between 1 and 31")
    @Max(value = 31, message = "Settlement day must be between 1 and 31")
    private Integer settlementDay;

    @Size(max = 100, message = "Settlement bank name cannot exceed 100 characters")
    private String settlementBankName;

    @Size(max = 50, message = "Settlement account number cannot exceed 50 characters")
    private String settlementAccountNumber;

    @Size(max = 100, message = "Settlement account name cannot exceed 100 characters")
    private String settlementAccountName;

    @Size(max = 20, message = "Settlement bank code cannot exceed 20 characters")
    private String settlementBankCode;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Settlement currency must be a 3-letter ISO code")
    private String settlementCurrency;

    @Size(max = 50, message = "Acquirer ID cannot exceed 50 characters")
    private String acquirerId;

    @Size(max = 100, message = "Acquirer name cannot exceed 100 characters")
    private String acquirerName;

    @Size(max = 50, message = "Processor ID cannot exceed 50 characters")
    private String processorId;

    @Size(max = 100, message = "Processor name cannot exceed 100 characters")
    private String processorName;

    @Size(max = 500, message = "Terminal IDs cannot exceed 500 characters")
    private String terminalIds;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}