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


package io.fireflysoftware.core.banking.cards.models.entities.statement;

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
 * Card Statement entity.
 * Represents a periodic statement of card activity, including transactions, fees, and balances.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_statement")
public class CardStatement extends BaseEntity {

    @Id
    @Column("statement_id")
    private UUID statementId;

    @Column("card_id")
    private UUID cardId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("statement_reference")
    private String statementReference;

    @Column("statement_date")
    private LocalDateTime statementDate;

    @Column("statement_period_start")
    private LocalDateTime statementPeriodStart;

    @Column("statement_period_end")
    private LocalDateTime statementPeriodEnd;

    @Column("due_date")
    private LocalDateTime dueDate;

    @Column("closing_balance")
    private BigDecimal closingBalance;

    @Column("opening_balance")
    private BigDecimal openingBalance;

    @Column("minimum_payment_due")
    private BigDecimal minimumPaymentDue;

    @Column("total_payment_due")
    private BigDecimal totalPaymentDue;

    @Column("currency_code")
    private String currencyCode;

    @Column("total_purchases")
    private BigDecimal totalPurchases;

    @Column("total_cash_advances")
    private BigDecimal totalCashAdvances;

    @Column("total_fees")
    private BigDecimal totalFees;

    @Column("total_interest")
    private BigDecimal totalInterest;

    @Column("total_credits")
    private BigDecimal totalCredits;

    @Column("total_payments")
    private BigDecimal totalPayments;

    @Column("total_adjustments")
    private BigDecimal totalAdjustments;

    @Column("total_rewards_earned")
    private BigDecimal totalRewardsEarned;

    @Column("total_rewards_redeemed")
    private BigDecimal totalRewardsRedeemed;

    @Column("available_credit")
    private BigDecimal availableCredit;

    @Column("credit_limit")
    private BigDecimal creditLimit;

    @Column("cash_advance_limit")
    private BigDecimal cashAdvanceLimit;

    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("annual_percentage_rate")
    private BigDecimal annualPercentageRate;

    @Column("payment_status")
    private String paymentStatus;

    @Column("is_generated")
    private Boolean isGenerated;

    @Column("generation_timestamp")
    private LocalDateTime generationTimestamp;

    @Column("is_delivered")
    private Boolean isDelivered;

    @Column("delivery_method")
    private String deliveryMethod;

    @Column("delivery_timestamp")
    private LocalDateTime deliveryTimestamp;

    @Column("delivery_address")
    private String deliveryAddress;

    @Column("is_viewed")
    private Boolean isViewed;

    @Column("view_timestamp")
    private LocalDateTime viewTimestamp;

    @Column("document_url")
    private String documentUrl;

    @Column("notes")
    private String notes;
}