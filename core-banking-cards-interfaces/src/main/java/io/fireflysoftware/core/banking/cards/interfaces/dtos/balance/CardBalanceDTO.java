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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.balance;

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
 * Data Transfer Object for Card Balance.
 * Contains the essential information about a Card Balance that needs to be exposed through the API.
 * A Card Balance represents the current balance of a card, including different types of balances
 * like purchase balance, cash advance balance, and balance transfers.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardBalanceDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID balanceId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @FilterableId
    private UUID statementId;

    @NotBlank(message = "Balance type is required")
    @Pattern(regexp = "^(CURRENT|AVAILABLE|PENDING|CREDIT|DEBIT)$", message = "Balance type must be CURRENT, AVAILABLE, PENDING, CREDIT, or DEBIT")
    private String balanceType;

    @NotBlank(message = "Balance category is required")
    @Pattern(regexp = "^(PURCHASE|CASH_ADVANCE|BALANCE_TRANSFER|FEE|INTEREST)$", message = "Balance category must be PURCHASE, CASH_ADVANCE, BALANCE_TRANSFER, FEE, or INTEREST")
    private String balanceCategory;

    @Size(max = 500, message = "Balance description cannot exceed 500 characters")
    private String balanceDescription;

    @NotNull(message = "Balance amount is required")
    @DecimalMin(value = "-999999999.99", message = "Balance amount cannot be less than -999,999,999.99")
    @DecimalMax(value = "999999999.99", message = "Balance amount cannot exceed 999,999,999.99")
    private BigDecimal balanceAmount;

    @DecimalMin(value = "0.0", message = "Available amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Available amount cannot exceed 999,999,999.99")
    private BigDecimal availableAmount;

    @DecimalMin(value = "0.0", message = "Reserved amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Reserved amount cannot exceed 999,999,999.99")
    private BigDecimal reservedAmount;

    @DecimalMin(value = "0.0", message = "Pending amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Pending amount cannot exceed 999,999,999.99")
    private BigDecimal pendingAmount;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @NotNull(message = "As of date is required")
    @PastOrPresent(message = "As of date cannot be in the future")
    private LocalDateTime asOfDate;

    @FilterableId
    private UUID lastTransactionId;

    @PastOrPresent(message = "Last transaction timestamp cannot be in the future")
    private LocalDateTime lastTransactionTimestamp;

    @FilterableId
    private UUID lastPaymentId;

    @PastOrPresent(message = "Last payment timestamp cannot be in the future")
    private LocalDateTime lastPaymentTimestamp;

    @FilterableId
    private UUID lastStatementId;

    @PastOrPresent(message = "Last statement date cannot be in the future")
    private LocalDateTime lastStatementDate;

    @NotNull(message = "Promotional rate flag is required")
    private Boolean isPromotionalRate;

    @FilterableId
    private UUID promotionId;

    @Future(message = "Promotion end date must be in the future")
    private LocalDateTime promotionEndDate;

    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    @DecimalMin(value = "0.0", message = "Annual percentage rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Annual percentage rate cannot exceed 100%")
    private BigDecimal annualPercentageRate;

    @DecimalMin(value = "0.0", message = "Accrued interest cannot be negative")
    @DecimalMax(value = "999999.99", message = "Accrued interest cannot exceed 999,999.99")
    private BigDecimal accruedInterest;

    @PastOrPresent(message = "Last interest accrual date cannot be in the future")
    private LocalDateTime lastInterestAccrualDate;

    @Future(message = "Next interest accrual date must be in the future")
    private LocalDateTime nextInterestAccrualDate;

    @NotNull(message = "Grace period flag is required")
    private Boolean isInGracePeriod;

    @Future(message = "Grace period end date must be in the future")
    private LocalDateTime gracePeriodEndDate;

    @DecimalMin(value = "0.0", message = "Minimum payment due cannot be negative")
    @DecimalMax(value = "999999.99", message = "Minimum payment due cannot exceed 999,999.99")
    private BigDecimal minimumPaymentDue;

    @Future(message = "Minimum payment due date must be in the future")
    private LocalDateTime minimumPaymentDueDate;

    @Min(value = 0, message = "Days past due cannot be negative")
    @Max(value = 9999, message = "Days past due cannot exceed 9999")
    private Integer daysPastDue;

    @NotNull(message = "Delinquent flag is required")
    private Boolean isDelinquent;

    @PastOrPresent(message = "Delinquency start date cannot be in the future")
    private LocalDateTime delinquencyStartDate;

    @Min(value = 0, message = "Delinquency days cannot be negative")
    @Max(value = 9999, message = "Delinquency days cannot exceed 9999")
    private Integer delinquencyDays;

    @Size(max = 50, message = "Delinquency stage cannot exceed 50 characters")
    private String delinquencyStage;

    @NotNull(message = "Charged off flag is required")
    private Boolean isChargedOff;

    @PastOrPresent(message = "Charge off date cannot be in the future")
    private LocalDateTime chargeOffDate;

    @DecimalMin(value = "0.0", message = "Charge off amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Charge off amount cannot exceed 999,999,999.99")
    private BigDecimal chargeOffAmount;

    @NotNull(message = "Written off flag is required")
    private Boolean isWrittenOff;

    @PastOrPresent(message = "Write off date cannot be in the future")
    private LocalDateTime writeOffDate;

    @DecimalMin(value = "0.0", message = "Write off amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Write off amount cannot exceed 999,999,999.99")
    private BigDecimal writeOffAmount;

    @NotNull(message = "In collection flag is required")
    private Boolean isInCollection;

    @PastOrPresent(message = "Collection start date cannot be in the future")
    private LocalDateTime collectionStartDate;

    @Size(max = 50, message = "Collection agency ID cannot exceed 50 characters")
    private String collectionAgencyId;

    @Size(max = 100, message = "Collection reference cannot exceed 100 characters")
    private String collectionReference;

    @Size(max = 50, message = "Collection status cannot exceed 50 characters")
    private String collectionStatus;
}