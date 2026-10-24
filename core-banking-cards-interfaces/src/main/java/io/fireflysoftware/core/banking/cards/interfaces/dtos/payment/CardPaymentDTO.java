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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.payment;

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
 * Data Transfer Object for Card Payment.
 * Contains the essential information about a Card Payment that needs to be exposed through the API.
 * A Card Payment represents a payment made towards a card balance.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardPaymentDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID paymentId;

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

    @NotBlank(message = "Payment reference is required")
    @Size(min = 5, max = 50, message = "Payment reference must be between 5 and 50 characters")
    private String paymentReference;

    @Size(max = 50, message = "External reference cannot exceed 50 characters")
    private String externalReference;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
    @DecimalMax(value = "999999999.99", message = "Payment amount cannot exceed 999,999,999.99")
    private BigDecimal paymentAmount;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(BANK_TRANSFER|CREDIT_CARD|DEBIT_CARD|CASH|CHECK|ONLINE)$", message = "Payment method must be BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, CASH, CHECK, or ONLINE")
    private String paymentMethod;

    @NotBlank(message = "Payment channel is required")
    @Pattern(regexp = "^(ONLINE|MOBILE|ATM|BRANCH|PHONE|MAIL)$", message = "Payment channel must be ONLINE, MOBILE, ATM, BRANCH, PHONE, or MAIL")
    private String paymentChannel;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "^(PENDING|PROCESSING|COMPLETED|FAILED|CANCELLED|REFUNDED)$", message = "Payment status must be PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, or REFUNDED")
    private String paymentStatus;

    @NotNull(message = "Payment timestamp is required")
    @PastOrPresent(message = "Payment timestamp cannot be in the future")
    private LocalDateTime paymentTimestamp;

    @PastOrPresent(message = "Posting timestamp cannot be in the future")
    private LocalDateTime postingTimestamp;

    @PastOrPresent(message = "Value date cannot be in the future")
    private LocalDateTime valueDate;

    @NotNull(message = "Auto payment flag is required")
    private Boolean isAutoPayment;

    @NotNull(message = "Minimum payment flag is required")
    private Boolean isMinimumPayment;

    @NotNull(message = "Full payment flag is required")
    private Boolean isFullPayment;

    @NotNull(message = "Scheduled payment flag is required")
    private Boolean isScheduledPayment;

    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledDate;

    @Size(max = 100, message = "Recurrence pattern cannot exceed 100 characters")
    private String recurrencePattern;

    @Size(max = 50, message = "Source account ID cannot exceed 50 characters")
    private String sourceAccountId;

    @Size(max = 50, message = "Source account type cannot exceed 50 characters")
    private String sourceAccountType;

    @Size(max = 20, message = "Source bank code cannot exceed 20 characters")
    private String sourceBankCode;

    @Size(max = 100, message = "Source bank name cannot exceed 100 characters")
    private String sourceBankName;

    @Size(max = 100, message = "Source account holder cannot exceed 100 characters")
    private String sourceAccountHolder;

    @Size(max = 100, message = "Payment processor cannot exceed 100 characters")
    private String paymentProcessor;

    @DecimalMin(value = "0.0", message = "Processor fee cannot be negative")
    @DecimalMax(value = "9999.99", message = "Processor fee cannot exceed 9,999.99")
    private BigDecimal processorFee;

    @Size(max = 100, message = "Processor reference cannot exceed 100 characters")
    private String processorReference;

    @Size(max = 50, message = "Confirmation code cannot exceed 50 characters")
    private String confirmationCode;

    @Size(max = 255, message = "Failure reason cannot exceed 255 characters")
    private String failureReason;

    @Size(max = 50, message = "Failure code cannot exceed 50 characters")
    private String failureCode;

    @Min(value = 0, message = "Retry count cannot be negative")
    @Max(value = 10, message = "Retry count cannot exceed 10")
    private Integer retryCount;

    @PastOrPresent(message = "Last retry timestamp cannot be in the future")
    private LocalDateTime lastRetryTimestamp;

    @Future(message = "Next retry timestamp must be in the future")
    private LocalDateTime nextRetryTimestamp;

    @Pattern(regexp = "^https?://.*", message = "Receipt URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Receipt URL cannot exceed 500 characters")
    private String receiptUrl;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}