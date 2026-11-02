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


package io.fireflysoftware.core.banking.cards.models.entities.interest;

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
 * Card Interest entity.
 * Represents interest charged on card balances, including rates, calculation methods, and accrual information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_interest")
public class CardInterest extends BaseEntity {

    @Id
    @Column("interest_id")
    private UUID interestId;

    @Column("card_id")
    private UUID cardId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("statement_id")
    private UUID statementId;

    @Column("program_id")
    private UUID programId;

    @Column("interest_reference")
    private String interestReference;

    @Column("interest_type")
    private String interestType;

    @Column("interest_name")
    private String interestName;

    @Column("interest_description")
    private String interestDescription;

    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("annual_percentage_rate")
    private BigDecimal annualPercentageRate;

    @Column("is_variable_rate")
    private Boolean isVariableRate;

    @Column("base_rate")
    private BigDecimal baseRate;

    @Column("rate_margin")
    private BigDecimal rateMargin;

    @Column("rate_cap")
    private BigDecimal rateCap;

    @Column("rate_floor")
    private BigDecimal rateFloor;

    @Column("rate_change_frequency")
    private String rateChangeFrequency;

    @Column("next_rate_change_date")
    private LocalDateTime nextRateChangeDate;

    @Column("previous_rate")
    private BigDecimal previousRate;

    @Column("rate_change_timestamp")
    private LocalDateTime rateChangeTimestamp;

    @Column("calculation_method")
    private String calculationMethod;

    @Column("compounding_frequency")
    private String compoundingFrequency;

    @Column("accrual_frequency")
    private String accrualFrequency;

    @Column("balance_type")
    private String balanceType;

    @Column("balance_amount")
    private BigDecimal balanceAmount;

    @Column("interest_amount")
    private BigDecimal interestAmount;

    @Column("accrued_interest")
    private BigDecimal accruedInterest;

    @Column("currency_code")
    private String currencyCode;

    @Column("accrual_start_date")
    private LocalDateTime accrualStartDate;

    @Column("accrual_end_date")
    private LocalDateTime accrualEndDate;

    @Column("days_in_period")
    private Integer daysInPeriod;

    @Column("days_in_year")
    private Integer daysInYear;

    @Column("is_promotional_rate")
    private Boolean isPromotionalRate;

    @Column("promotion_id")
    private UUID promotionId;

    @Column("promotion_start_date")
    private LocalDateTime promotionStartDate;

    @Column("promotion_end_date")
    private LocalDateTime promotionEndDate;

    @Column("post_promotion_rate")
    private BigDecimal postPromotionRate;

    @Column("is_grace_period")
    private Boolean isGracePeriod;

    @Column("grace_period_days")
    private Integer gracePeriodDays;

    @Column("grace_period_end_date")
    private LocalDateTime gracePeriodEndDate;

    @Column("is_charged")
    private Boolean isCharged;

    @Column("charge_timestamp")
    private LocalDateTime chargeTimestamp;

    @Column("posting_timestamp")
    private LocalDateTime postingTimestamp;

    @Column("value_date")
    private LocalDateTime valueDate;

    @Column("is_waived")
    private Boolean isWaived;

    @Column("waiver_reason")
    private String waiverReason;

    @Column("waiver_reference")
    private String waiverReference;

    @Column("waiver_timestamp")
    private LocalDateTime waiverTimestamp;

    @Column("waiver_authorized_by")
    private String waiverAuthorizedBy;

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