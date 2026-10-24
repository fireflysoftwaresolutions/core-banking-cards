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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.reward;

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
 * Data Transfer Object for Card Reward.
 * Contains the essential information about a Card Reward that needs to be exposed through the API.
 * A Card Reward represents rewards earned and redeemed through card usage.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardRewardDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID rewardId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @FilterableId
    private UUID transactionId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @FilterableId
    private UUID programId;

    @NotBlank(message = "Reward reference is required")
    @Size(max = 100, message = "Reward reference cannot exceed 100 characters")
    private String rewardReference;

    @NotBlank(message = "Reward type is required")
    @Size(max = 50, message = "Reward type cannot exceed 50 characters")
    private String rewardType;

    @Size(max = 50, message = "Reward category cannot exceed 50 characters")
    private String rewardCategory;

    @Size(max = 500, message = "Reward description cannot exceed 500 characters")
    private String rewardDescription;

    @NotBlank(message = "Reward status is required")
    @Size(max = 50, message = "Reward status cannot exceed 50 characters")
    private String rewardStatus;

    @NotNull(message = "Earning flag is required")
    private Boolean isEarning;

    @NotNull(message = "Redemption flag is required")
    private Boolean isRedemption;

    @NotNull(message = "Adjustment flag is required")
    private Boolean isAdjustment;

    @NotNull(message = "Expiration flag is required")
    private Boolean isExpiration;

    @DecimalMin(value = "0.0", message = "Points earned cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Points earned cannot exceed 999,999,999.99")
    private BigDecimal pointsEarned;

    @DecimalMin(value = "0.0", message = "Points redeemed cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Points redeemed cannot exceed 999,999,999.99")
    private BigDecimal pointsRedeemed;

    @DecimalMin(value = "-999999999.99", message = "Points adjusted cannot be less than -999,999,999.99")
    @DecimalMax(value = "999999999.99", message = "Points adjusted cannot exceed 999,999,999.99")
    private BigDecimal pointsAdjusted;

    @DecimalMin(value = "0.0", message = "Points expired cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Points expired cannot exceed 999,999,999.99")
    private BigDecimal pointsExpired;

    @DecimalMin(value = "0.0", message = "Points balance cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Points balance cannot exceed 999,999,999.99")
    private BigDecimal pointsBalance;

    @DecimalMin(value = "0.0", message = "Cash value cannot be negative")
    @DecimalMax(value = "999999.99", message = "Cash value cannot exceed 999,999.99")
    private BigDecimal cashValue;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @DecimalMin(value = "0.0", message = "Earning rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Earning rate cannot exceed 100%")
    private BigDecimal earningRate;

    @DecimalMin(value = "1.0", message = "Earning multiplier must be at least 1.0")
    @DecimalMax(value = "100.0", message = "Earning multiplier cannot exceed 100.0")
    private BigDecimal earningMultiplier;

    @Size(max = 255, message = "Earning reason cannot exceed 255 characters")
    private String earningReason;

    @Size(max = 50, message = "Redemption type cannot exceed 50 characters")
    private String redemptionType;

    @Size(max = 500, message = "Redemption description cannot exceed 500 characters")
    private String redemptionDescription;

    @DecimalMin(value = "0.0", message = "Redemption value cannot be negative")
    @DecimalMax(value = "999999.99", message = "Redemption value cannot exceed 999,999.99")
    private BigDecimal redemptionValue;

    @Size(max = 255, message = "Adjustment reason cannot exceed 255 characters")
    private String adjustmentReason;

    @Size(max = 500, message = "Adjustment description cannot exceed 500 characters")
    private String adjustmentDescription;

    @Size(max = 50, message = "Merchant ID cannot exceed 50 characters")
    private String merchantId;

    @Size(max = 100, message = "Merchant name cannot exceed 100 characters")
    private String merchantName;

    @Size(max = 50, message = "Merchant category cannot exceed 50 characters")
    private String merchantCategory;

    @DecimalMin(value = "0.0", message = "Transaction amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Transaction amount cannot exceed 999,999,999.99")
    private BigDecimal transactionAmount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Transaction currency must be a 3-letter ISO code")
    private String transactionCurrency;

    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDateTime transactionDate;

    @PastOrPresent(message = "Posting date cannot be in the future")
    private LocalDateTime postingDate;

    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;

    @NotNull(message = "Promotional flag is required")
    private Boolean isPromotional;

    @FilterableId
    private UUID promotionId;

    @Size(max = 100, message = "Promotion name cannot exceed 100 characters")
    private String promotionName;

    @NotNull(message = "Transferable flag is required")
    private Boolean isTransferable;

    private UUID transferToPartyId;

    private UUID transferFromPartyId;

    @PastOrPresent(message = "Transfer date cannot be in the future")
    private LocalDateTime transferDate;
}