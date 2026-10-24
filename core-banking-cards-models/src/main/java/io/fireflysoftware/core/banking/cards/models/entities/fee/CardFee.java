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


package io.fireflysoftware.core.banking.cards.models.entities.fee;

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
 * Card Fee entity.
 * Represents fees associated with a card, such as annual fees, late payment fees, and transaction fees.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_fee")
public class CardFee extends BaseEntity {

    @Id
    @Column("fee_id")
    private UUID feeId;

    @Column("card_id")
    private UUID cardId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("statement_id")
    private UUID statementId;

    @Column("program_id")
    private UUID programId;

    @Column("fee_reference")
    private String feeReference;

    @Column("fee_type")
    private String feeType;

    @Column("fee_name")
    private String feeName;

    @Column("fee_description")
    private String feeDescription;

    @Column("fee_amount")
    private BigDecimal feeAmount;

    @Column("fee_percentage")
    private BigDecimal feePercentage;

    @Column("currency_code")
    private String currencyCode;

    @Column("fee_status")
    private String feeStatus;

    @Column("is_recurring")
    private Boolean isRecurring;

    @Column("recurrence_frequency")
    private String recurrenceFrequency;

    @Column("next_recurrence_date")
    private LocalDateTime nextRecurrenceDate;

    @Column("is_prorated")
    private Boolean isProrated;

    @Column("proration_rule")
    private String prorationRule;

    @Column("is_waived")
    private Boolean isWaived;

    @Column("waiver_reason")
    private String waiverReason;

    @Column("waiver_reference")
    private String waiverReference;

    @Column("waiver_timestamp")
    private LocalDateTime waiverTimestamp;

    @Column("waiver_expiry")
    private LocalDateTime waiverExpiry;

    @Column("waiver_authorized_by")
    private String waiverAuthorizedBy;

    @Column("is_refunded")
    private Boolean isRefunded;

    @Column("refund_reason")
    private String refundReason;

    @Column("refund_reference")
    private String refundReference;

    @Column("refund_timestamp")
    private LocalDateTime refundTimestamp;

    @Column("refund_amount")
    private BigDecimal refundAmount;

    @Column("refund_authorized_by")
    private String refundAuthorizedBy;

    @Column("charge_timestamp")
    private LocalDateTime chargeTimestamp;

    @Column("posting_timestamp")
    private LocalDateTime postingTimestamp;

    @Column("value_date")
    private LocalDateTime valueDate;

    @Column("tax_amount")
    private BigDecimal taxAmount;

    @Column("tax_rate")
    private BigDecimal taxRate;

    @Column("tax_type")
    private String taxType;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("is_billed")
    private Boolean isBilled;

    @Column("billing_timestamp")
    private LocalDateTime billingTimestamp;

    @Column("is_paid")
    private Boolean isPaid;

    @Column("payment_timestamp")
    private LocalDateTime paymentTimestamp;

    @Column("payment_reference")
    private String paymentReference;

    @Column("notes")
    private String notes;
}