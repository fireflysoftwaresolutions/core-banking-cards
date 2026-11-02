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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.statement;

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
 * Data Transfer Object for Card Statement.
 * Contains the essential information about a Card Statement that needs to be exposed through the API.
 * A Card Statement represents a periodic statement of card activity, including transactions, fees, and balances.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardStatementDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID statementId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotBlank(message = "Statement reference is required")
    @Size(min = 5, max = 50, message = "Statement reference must be between 5 and 50 characters")
    private String statementReference;

    @NotNull(message = "Statement date is required")
    @PastOrPresent(message = "Statement date cannot be in the future")
    private LocalDateTime statementDate;

    @NotNull(message = "Statement period start is required")
    @PastOrPresent(message = "Statement period start cannot be in the future")
    private LocalDateTime statementPeriodStart;

    @NotNull(message = "Statement period end is required")
    @PastOrPresent(message = "Statement period end cannot be in the future")
    private LocalDateTime statementPeriodEnd;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    @NotNull(message = "Closing balance is required")
    @DecimalMin(value = "-999999999.99", message = "Closing balance cannot be less than -999,999,999.99")
    @DecimalMax(value = "999999999.99", message = "Closing balance cannot exceed 999,999,999.99")
    private BigDecimal closingBalance;

    @NotNull(message = "Opening balance is required")
    @DecimalMin(value = "-999999999.99", message = "Opening balance cannot be less than -999,999,999.99")
    @DecimalMax(value = "999999999.99", message = "Opening balance cannot exceed 999,999,999.99")
    private BigDecimal openingBalance;

    @NotNull(message = "Minimum payment due is required")
    @DecimalMin(value = "0.0", message = "Minimum payment due cannot be negative")
    private BigDecimal minimumPaymentDue;

    @NotNull(message = "Total payment due is required")
    @DecimalMin(value = "0.0", message = "Total payment due cannot be negative")
    private BigDecimal totalPaymentDue;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @DecimalMin(value = "0.0", message = "Total purchases cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Total purchases cannot exceed 999,999,999.99")
    private BigDecimal totalPurchases;

    @DecimalMin(value = "0.0", message = "Total cash advances cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Total cash advances cannot exceed 999,999,999.99")
    private BigDecimal totalCashAdvances;

    @DecimalMin(value = "0.0", message = "Total fees cannot be negative")
    @DecimalMax(value = "999999.99", message = "Total fees cannot exceed 999,999.99")
    private BigDecimal totalFees;

    @DecimalMin(value = "0.0", message = "Total interest cannot be negative")
    @DecimalMax(value = "999999.99", message = "Total interest cannot exceed 999,999.99")
    private BigDecimal totalInterest;

    @DecimalMin(value = "0.0", message = "Total credits cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Total credits cannot exceed 999,999,999.99")
    private BigDecimal totalCredits;

    @DecimalMin(value = "0.0", message = "Total payments cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Total payments cannot exceed 999,999,999.99")
    private BigDecimal totalPayments;

    @DecimalMin(value = "-999999.99", message = "Total adjustments cannot be less than -999,999.99")
    @DecimalMax(value = "999999.99", message = "Total adjustments cannot exceed 999,999.99")
    private BigDecimal totalAdjustments;

    @DecimalMin(value = "0.0", message = "Total rewards earned cannot be negative")
    @DecimalMax(value = "999999.99", message = "Total rewards earned cannot exceed 999,999.99")
    private BigDecimal totalRewardsEarned;

    @DecimalMin(value = "0.0", message = "Total rewards redeemed cannot be negative")
    @DecimalMax(value = "999999.99", message = "Total rewards redeemed cannot exceed 999,999.99")
    private BigDecimal totalRewardsRedeemed;

    @DecimalMin(value = "0.0", message = "Available credit cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Available credit cannot exceed 999,999,999.99")
    private BigDecimal availableCredit;

    @DecimalMin(value = "0.0", message = "Credit limit cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Credit limit cannot exceed 999,999,999.99")
    private BigDecimal creditLimit;

    @DecimalMin(value = "0.0", message = "Cash advance limit cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Cash advance limit cannot exceed 999,999,999.99")
    private BigDecimal cashAdvanceLimit;

    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    @DecimalMin(value = "0.0", message = "Annual percentage rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Annual percentage rate cannot exceed 100%")
    private BigDecimal annualPercentageRate;

    @Size(max = 50, message = "Payment status cannot exceed 50 characters")
    private String paymentStatus;

    @NotNull(message = "Generated flag is required")
    private Boolean isGenerated;

    @PastOrPresent(message = "Generation timestamp cannot be in the future")
    private LocalDateTime generationTimestamp;

    @NotNull(message = "Delivered flag is required")
    private Boolean isDelivered;

    @Size(max = 50, message = "Delivery method cannot exceed 50 characters")
    private String deliveryMethod;

    @PastOrPresent(message = "Delivery timestamp cannot be in the future")
    private LocalDateTime deliveryTimestamp;

    @Size(max = 500, message = "Delivery address cannot exceed 500 characters")
    private String deliveryAddress;

    @NotNull(message = "Viewed flag is required")
    private Boolean isViewed;

    @PastOrPresent(message = "View timestamp cannot be in the future")
    private LocalDateTime viewTimestamp;

    @Pattern(regexp = "^https?://.*", message = "Document URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Document URL cannot exceed 500 characters")
    private String documentUrl;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}