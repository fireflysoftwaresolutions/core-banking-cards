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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.fee;

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
 * Data Transfer Object for Card Fee.
 * Contains the essential information about a Card Fee that needs to be exposed through the API.
 * A Card Fee represents fees associated with a card, such as annual fees, late payment fees, and transaction fees.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardFeeDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID feeId;

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
    private UUID transactionId;

    @FilterableId
    private UUID statementId;

    @FilterableId
    private UUID programId;

    @NotBlank(message = "Fee reference is required")
    @Size(max = 100, message = "Fee reference cannot exceed 100 characters")
    private String feeReference;

    @NotBlank(message = "Fee type is required")
    @Size(max = 50, message = "Fee type cannot exceed 50 characters")
    private String feeType;

    @NotBlank(message = "Fee name is required")
    @Size(min = 2, max = 100, message = "Fee name must be between 2 and 100 characters")
    private String feeName;

    @Size(max = 500, message = "Fee description cannot exceed 500 characters")
    private String feeDescription;

    @NotNull(message = "Fee amount is required")
    @DecimalMin(value = "0.0", message = "Fee amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Fee amount cannot exceed 999,999.99")
    private BigDecimal feeAmount;

    @DecimalMin(value = "0.0", message = "Fee percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Fee percentage cannot exceed 100%")
    private BigDecimal feePercentage;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @NotBlank(message = "Fee status is required")
    @Size(max = 50, message = "Fee status cannot exceed 50 characters")
    private String feeStatus;

    @NotNull(message = "Recurring flag is required")
    private Boolean isRecurring;

    @Size(max = 50, message = "Recurrence frequency cannot exceed 50 characters")
    private String recurrenceFrequency;

    @Future(message = "Next recurrence date must be in the future")
    private LocalDateTime nextRecurrenceDate;

    @NotNull(message = "Prorated flag is required")
    private Boolean isProrated;

    @Size(max = 100, message = "Proration rule cannot exceed 100 characters")
    private String prorationRule;

    @NotNull(message = "Waived flag is required")
    private Boolean isWaived;

    @Size(max = 255, message = "Waiver reason cannot exceed 255 characters")
    private String waiverReason;

    @Size(max = 100, message = "Waiver reference cannot exceed 100 characters")
    private String waiverReference;

    @PastOrPresent(message = "Waiver timestamp cannot be in the future")
    private LocalDateTime waiverTimestamp;

    @Future(message = "Waiver expiry must be in the future")
    private LocalDateTime waiverExpiry;

    @Size(max = 100, message = "Waiver authorized by cannot exceed 100 characters")
    private String waiverAuthorizedBy;

    @NotNull(message = "Refunded flag is required")
    private Boolean isRefunded;

    @Size(max = 255, message = "Refund reason cannot exceed 255 characters")
    private String refundReason;

    @Size(max = 100, message = "Refund reference cannot exceed 100 characters")
    private String refundReference;

    @PastOrPresent(message = "Refund timestamp cannot be in the future")
    private LocalDateTime refundTimestamp;

    @DecimalMin(value = "0.0", message = "Refund amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Refund amount cannot exceed 999,999.99")
    private BigDecimal refundAmount;

    @Size(max = 100, message = "Refund authorized by cannot exceed 100 characters")
    private String refundAuthorizedBy;

    @PastOrPresent(message = "Charge timestamp cannot be in the future")
    private LocalDateTime chargeTimestamp;

    @PastOrPresent(message = "Posting timestamp cannot be in the future")
    private LocalDateTime postingTimestamp;

    @PastOrPresent(message = "Value date cannot be in the future")
    private LocalDateTime valueDate;

    @DecimalMin(value = "0.0", message = "Tax amount cannot be negative")
    @DecimalMax(value = "99999.99", message = "Tax amount cannot exceed 99,999.99")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Tax rate cannot exceed 100%")
    private BigDecimal taxRate;

    @Size(max = 50, message = "Tax type cannot exceed 50 characters")
    private String taxType;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Total amount cannot exceed 999,999.99")
    private BigDecimal totalAmount;

    @NotNull(message = "Billed flag is required")
    private Boolean isBilled;

    @PastOrPresent(message = "Billing timestamp cannot be in the future")
    private LocalDateTime billingTimestamp;

    @NotNull(message = "Paid flag is required")
    private Boolean isPaid;

    @PastOrPresent(message = "Payment timestamp cannot be in the future")
    private LocalDateTime paymentTimestamp;

    @Size(max = 100, message = "Payment reference cannot exceed 100 characters")
    private String paymentReference;
}