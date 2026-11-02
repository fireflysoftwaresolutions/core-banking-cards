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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.promotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Promotion.
 * Contains the essential information about a Card Promotion that needs to be exposed through the API.
 * A Card Promotion represents special offers and promotional programs for cardholders, including
 * cashback offers, bonus points, and introductory rates.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardPromotionDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID promotionId;

    @FilterableId
    private UUID cardId;

    @FilterableId
    private UUID programId;

    @FilterableId
    private UUID cardTypeId;

    @FilterableId
    private UUID issuerId;

    @NotBlank(message = "Promotion code is required")
    @Size(min = 3, max = 20, message = "Promotion code must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Promotion code must contain only uppercase letters, numbers, and underscores")
    private String promotionCode;

    @NotBlank(message = "Promotion name is required")
    @Size(min = 2, max = 100, message = "Promotion name must be between 2 and 100 characters")
    private String promotionName;

    @Size(max = 500, message = "Promotion description cannot exceed 500 characters")
    private String promotionDescription;

    @NotBlank(message = "Promotion type is required")
    @Size(max = 50, message = "Promotion type cannot exceed 50 characters")
    private String promotionType;

    @Size(max = 50, message = "Promotion category cannot exceed 50 characters")
    private String promotionCategory;

    @NotBlank(message = "Promotion status is required")
    @Size(max = 50, message = "Promotion status cannot exceed 50 characters")
    private String promotionStatus;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @NotNull(message = "Limited time flag is required")
    private Boolean isLimitedTime;

    @NotNull(message = "Targeted flag is required")
    private Boolean isTargeted;

    @Size(max = 500, message = "Target criteria cannot exceed 500 characters")
    private String targetCriteria;

    @NotNull(message = "Opt-in required flag is required")
    private Boolean isOptInRequired;

    @Size(max = 50, message = "Opt-in channel cannot exceed 50 characters")
    private String optInChannel;

    @Pattern(regexp = "^https?://.*", message = "Opt-in URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Opt-in URL cannot exceed 500 characters")
    private String optInUrl;

    @NotNull(message = "Auto enrolled flag is required")
    private Boolean isAutoEnrolled;

    @Min(value = 1, message = "Maximum enrollments must be at least 1")
    @Max(value = 1000000, message = "Maximum enrollments cannot exceed 1,000,000")
    private Integer maxEnrollments;

    @Min(value = 0, message = "Current enrollments cannot be negative")
    private Integer currentEnrollments;

    @NotBlank(message = "Benefit type is required")
    @Size(max = 50, message = "Benefit type cannot exceed 50 characters")
    private String benefitType;

    @DecimalMin(value = "0.0", message = "Benefit value cannot be negative")
    @DecimalMax(value = "999999.99", message = "Benefit value cannot exceed 999,999.99")
    private BigDecimal benefitValue;

    @DecimalMin(value = "0.0", message = "Benefit percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Benefit percentage cannot exceed 100%")
    private BigDecimal benefitPercentage;

    @Size(max = 500, message = "Benefit description cannot exceed 500 characters")
    private String benefitDescription;

    @DecimalMin(value = "0.0", message = "Minimum spend cannot be negative")
    @DecimalMax(value = "999999.99", message = "Minimum spend cannot exceed 999,999.99")
    private BigDecimal minimumSpend;

    @DecimalMin(value = "0.0", message = "Maximum benefit cannot be negative")
    @DecimalMax(value = "999999.99", message = "Maximum benefit cannot exceed 999,999.99")
    private BigDecimal maximumBenefit;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @Size(max = 500, message = "Merchant category codes cannot exceed 500 characters")
    private String merchantCategoryCodes;

    @Size(max = 500, message = "Merchant IDs cannot exceed 500 characters")
    private String merchantIds;

    @Size(max = 500, message = "Country codes cannot exceed 500 characters")
    private String countryCodes;

    @Size(max = 500, message = "Transaction types cannot exceed 500 characters")
    private String transactionTypes;

    @Size(max = 500, message = "Channel types cannot exceed 500 characters")
    private String channelTypes;

    @NotNull(message = "First transaction only flag is required")
    private Boolean isFirstTransactionOnly;

    @NotNull(message = "New cards only flag is required")
    private Boolean isNewCardsOnly;

    @Min(value = 0, message = "Card age minimum days cannot be negative")
    @Max(value = 3650, message = "Card age minimum days cannot exceed 3650 (10 years)")
    private Integer cardAgeMinDays;

    @Min(value = 0, message = "Card age maximum days cannot be negative")
    @Max(value = 3650, message = "Card age maximum days cannot exceed 3650 (10 years)")
    private Integer cardAgeMaxDays;

    @NotNull(message = "Balance transfer flag is required")
    private Boolean isBalanceTransfer;

    @DecimalMin(value = "0.0", message = "Balance transfer fee cannot be negative")
    @DecimalMax(value = "9999.99", message = "Balance transfer fee cannot exceed 9,999.99")
    private BigDecimal balanceTransferFee;

    @DecimalMin(value = "0.0", message = "Balance transfer fee percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Balance transfer fee percentage cannot exceed 100%")
    private BigDecimal balanceTransferFeePercentage;

    @DecimalMin(value = "0.0", message = "Balance transfer minimum amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Balance transfer minimum amount cannot exceed 999,999.99")
    private BigDecimal balanceTransferMinAmount;

    @DecimalMin(value = "0.0", message = "Balance transfer maximum amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Balance transfer maximum amount cannot exceed 999,999.99")
    private BigDecimal balanceTransferMaxAmount;

    @NotNull(message = "Introductory rate flag is required")
    private Boolean isIntroductoryRate;

    @DecimalMin(value = "0.0", message = "Introductory rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Introductory rate cannot exceed 100%")
    private BigDecimal introductoryRate;

    @DecimalMin(value = "0.0", message = "Post introductory rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Post introductory rate cannot exceed 100%")
    private BigDecimal postIntroductoryRate;

    @Min(value = 1, message = "Introductory period must be at least 1 month")
    @Max(value = 60, message = "Introductory period cannot exceed 60 months")
    private Integer introductoryPeriodMonths;

    @Size(max = 2000, message = "Terms and conditions cannot exceed 2000 characters")
    private String termsAndConditions;

    @Pattern(regexp = "^https?://.*", message = "Terms URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Terms URL cannot exceed 500 characters")
    private String termsUrl;

    @Pattern(regexp = "^https?://.*", message = "Marketing image URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Marketing image URL cannot exceed 500 characters")
    private String marketingImageUrl;

    @Size(max = 1000, message = "Marketing description cannot exceed 1000 characters")
    private String marketingDescription;

    @NotNull(message = "Combinable flag is required")
    private Boolean isCombinable;

    @Size(max = 500, message = "Combinable promotions cannot exceed 500 characters")
    private String combinablePromotions;

    @Size(max = 1000, message = "Exclusion criteria cannot exceed 1000 characters")
    private String exclusionCriteria;
}