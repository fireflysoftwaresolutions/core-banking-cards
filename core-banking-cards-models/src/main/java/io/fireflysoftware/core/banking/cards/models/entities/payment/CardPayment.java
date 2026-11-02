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


package io.fireflysoftware.core.banking.cards.models.entities.payment;

import io.fireflysoftware.core.banking.cards.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Card Payment entity.
 * Represents a payment made towards a card balance.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_payment")
public class CardPayment extends BaseEntity {

    @Id
    @Column("payment_id")
    private UUID paymentId;

    @Column("card_id")
    private UUID cardId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("statement_id")
    private UUID statementId;

    @Column("payment_reference")
    private String paymentReference;

    @Column("external_reference")
    private String externalReference;

    @Column("payment_amount")
    private BigDecimal paymentAmount;

    @Column("currency_code")
    private String currencyCode;

    @Column("payment_method")
    private String paymentMethod;

    @Column("payment_channel")
    private String paymentChannel;

    @Column("payment_status")
    private String paymentStatus;

    @Column("payment_timestamp")
    private LocalDateTime paymentTimestamp;

    @Column("posting_timestamp")
    private LocalDateTime postingTimestamp;

    @Column("value_date")
    private LocalDateTime valueDate;

    @Column("is_auto_payment")
    private Boolean isAutoPayment;

    @Column("is_minimum_payment")
    private Boolean isMinimumPayment;

    @Column("is_full_payment")
    private Boolean isFullPayment;

    @Column("is_scheduled_payment")
    private Boolean isScheduledPayment;

    @Column("scheduled_date")
    private LocalDateTime scheduledDate;

    @Column("recurrence_pattern")
    private String recurrencePattern;

    @Column("source_account_id")
    private String sourceAccountId;

    @Column("source_account_type")
    private String sourceAccountType;

    @Column("source_bank_code")
    private String sourceBankCode;

    @Column("source_bank_name")
    private String sourceBankName;

    @Column("source_account_holder")
    private String sourceAccountHolder;

    @Column("payment_processor")
    private String paymentProcessor;

    @Column("processor_fee")
    private BigDecimal processorFee;

    @Column("processor_reference")
    private String processorReference;

    @Column("confirmation_code")
    private String confirmationCode;

    @Column("failure_reason")
    private String failureReason;

    @Column("failure_code")
    private String failureCode;

    @Column("retry_count")
    private Integer retryCount;

    @Column("last_retry_timestamp")
    private LocalDateTime lastRetryTimestamp;

    @Column("next_retry_timestamp")
    private LocalDateTime nextRetryTimestamp;

    @Column("receipt_url")
    private String receiptUrl;

    @Column("notes")
    private String notes;
}