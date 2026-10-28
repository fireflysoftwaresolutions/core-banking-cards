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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Program.
 * Contains the essential information about a Card Program that needs to be exposed through the API.
 * A Card Program represents a set of rules, configurations, and features for a group of cards.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardProgramDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID programId;

    @NotBlank(message = "Program name is required")
    @Size(min = 2, max = 100, message = "Program name must be between 2 and 100 characters")
    private String programName;

    @NotBlank(message = "Program code is required")
    @Pattern(regexp = "^[A-Z0-9_]{2,20}$", message = "Program code must be 2-20 characters, uppercase letters, numbers, and underscores only")
    private String programCode;

    @FilterableId
    @NotNull(message = "Issuer ID is required")
    private UUID issuerId;

    @FilterableId
    @NotNull(message = "BIN ID is required")
    private UUID binId;

    @FilterableId
    @NotNull(message = "Card type ID is required")
    private UUID cardTypeId;

    @FilterableId
    @NotNull(message = "Card network ID is required")
    private UUID cardNetworkId;

    @FilterableId
    private UUID defaultDesignId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDateTime startDate;

    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @Min(value = 1, message = "Maximum cards per party must be at least 1")
    @Max(value = 100, message = "Maximum cards per party cannot exceed 100")
    private Integer maxCardsPerParty;

    @DecimalMin(value = "0.0", inclusive = false, message = "Default daily limit must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Default daily limit cannot exceed 999,999.99")
    private Double defaultDailyLimit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Default monthly limit must be greater than 0")
    @DecimalMax(value = "9999999.99", message = "Default monthly limit cannot exceed 9,999,999.99")
    private Double defaultMonthlyLimit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Default credit limit must be greater than 0")
    @DecimalMax(value = "99999999.99", message = "Default credit limit cannot exceed 99,999,999.99")
    private Double defaultCreditLimit;

    @Min(value = 1, message = "Default card validity years must be at least 1")
    @Max(value = 10, message = "Default card validity years cannot exceed 10")
    private Integer defaultCardValidityYears;

    @NotNull(message = "Physical cards support flag is required")
    private Boolean supportsPhysicalCards;

    @NotNull(message = "Virtual cards support flag is required")
    private Boolean supportsVirtualCards;

    @NotNull(message = "Contactless support flag is required")
    private Boolean supportsContactless;

    @NotNull(message = "International support flag is required")
    private Boolean supportsInternational;

    @NotNull(message = "ATM withdrawal support flag is required")
    private Boolean supportsAtmWithdrawal;

    @NotNull(message = "Online transactions support flag is required")
    private Boolean supportsOnlineTransactions;

    @NotNull(message = "Recurring payments support flag is required")
    private Boolean supportsRecurringPayments;

    @NotNull(message = "Apple Pay support flag is required")
    private Boolean supportsApplePay;

    @NotNull(message = "Google Pay support flag is required")
    private Boolean supportsGooglePay;

    @NotNull(message = "Samsung Pay support flag is required")
    private Boolean supportsSamsungPay;

    @NotNull(message = "PIN requirement flag is required")
    private Boolean requiresPin;

    @NotNull(message = "Activation requirement flag is required")
    private Boolean requiresActivation;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be a 2-letter ISO code")
    private String countryCode;

    private String termsAndConditionsUrl;

    private String description;
}