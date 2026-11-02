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


package io.fireflysoftware.core.banking.cards.models.entities.promotion;

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
 * Card Promotion entity.
 * Represents special offers and promotional programs for cardholders, including cashback offers,
 * bonus points, and introductory rates.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_promotion")
public class CardPromotion extends BaseEntity {

    @Id
    @Column("promotion_id")
    private UUID promotionId;

    @Column("card_id")
    private UUID cardId;

    @Column("program_id")
    private UUID programId;

    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("issuer_id")
    private UUID issuerId;

    @Column("promotion_code")
    private String promotionCode;

    @Column("promotion_name")
    private String promotionName;

    @Column("promotion_description")
    private String promotionDescription;

    @Column("promotion_type")
    private String promotionType;

    @Column("promotion_category")
    private String promotionCategory;

    @Column("promotion_status")
    private String promotionStatus;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate;

    @Column("is_active")
    private Boolean isActive;

    @Column("is_limited_time")
    private Boolean isLimitedTime;

    @Column("is_targeted")
    private Boolean isTargeted;

    @Column("target_criteria")
    private String targetCriteria;

    @Column("is_opt_in_required")
    private Boolean isOptInRequired;

    @Column("opt_in_channel")
    private String optInChannel;

    @Column("opt_in_url")
    private String optInUrl;

    @Column("is_auto_enrolled")
    private Boolean isAutoEnrolled;

    @Column("max_enrollments")
    private Integer maxEnrollments;

    @Column("current_enrollments")
    private Integer currentEnrollments;

    @Column("benefit_type")
    private String benefitType;

    @Column("benefit_value")
    private BigDecimal benefitValue;

    @Column("benefit_percentage")
    private BigDecimal benefitPercentage;

    @Column("benefit_description")
    private String benefitDescription;

    @Column("minimum_spend")
    private BigDecimal minimumSpend;

    @Column("maximum_benefit")
    private BigDecimal maximumBenefit;

    @Column("currency_code")
    private String currencyCode;

    @Column("merchant_category_codes")
    private String merchantCategoryCodes;

    @Column("merchant_ids")
    private String merchantIds;

    @Column("country_codes")
    private String countryCodes;

    @Column("transaction_types")
    private String transactionTypes;

    @Column("channel_types")
    private String channelTypes;

    @Column("is_first_transaction_only")
    private Boolean isFirstTransactionOnly;

    @Column("is_new_cards_only")
    private Boolean isNewCardsOnly;

    @Column("card_age_min_days")
    private Integer cardAgeMinDays;

    @Column("card_age_max_days")
    private Integer cardAgeMaxDays;

    @Column("is_balance_transfer")
    private Boolean isBalanceTransfer;

    @Column("balance_transfer_fee")
    private BigDecimal balanceTransferFee;

    @Column("balance_transfer_fee_percentage")
    private BigDecimal balanceTransferFeePercentage;

    @Column("balance_transfer_min_amount")
    private BigDecimal balanceTransferMinAmount;

    @Column("balance_transfer_max_amount")
    private BigDecimal balanceTransferMaxAmount;

    @Column("is_introductory_rate")
    private Boolean isIntroductoryRate;

    @Column("introductory_rate")
    private BigDecimal introductoryRate;

    @Column("post_introductory_rate")
    private BigDecimal postIntroductoryRate;

    @Column("introductory_period_months")
    private Integer introductoryPeriodMonths;

    @Column("terms_and_conditions")
    private String termsAndConditions;

    @Column("terms_url")
    private String termsUrl;

    @Column("marketing_image_url")
    private String marketingImageUrl;

    @Column("marketing_description")
    private String marketingDescription;

    @Column("is_combinable")
    private Boolean isCombinable;

    @Column("combinable_promotions")
    private String combinablePromotions;

    @Column("exclusion_criteria")
    private String exclusionCriteria;

    @Column("notes")
    private String notes;
}