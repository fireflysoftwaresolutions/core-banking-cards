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


package io.fireflysoftware.core.banking.cards.models.entities.program;

import io.fireflysoftware.core.banking.cards.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Card Program entity.
 * Represents a card program which is a set of rules, configurations, and features for a group of cards.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_program")
public class CardProgram extends BaseEntity {

    @Id
    @Column("program_id")
    private UUID programId;

    @Column("program_name")
    private String programName;

    @Column("program_code")
    private String programCode;

    @Column("issuer_id")
    private UUID issuerId;

    @Column("bin_id")
    private UUID binId;

    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("card_network_id")
    private UUID cardNetworkId;

    @Column("default_design_id")
    private UUID defaultDesignId;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate;

    @Column("is_active")
    private Boolean isActive;

    @Column("max_cards_per_party")
    private Integer maxCardsPerParty;

    @Column("default_daily_limit")
    private Double defaultDailyLimit;

    @Column("default_monthly_limit")
    private Double defaultMonthlyLimit;

    @Column("default_credit_limit")
    private Double defaultCreditLimit;

    @Column("default_card_validity_years")
    private Integer defaultCardValidityYears;

    @Column("supports_physical_cards")
    private Boolean supportsPhysicalCards;

    @Column("supports_virtual_cards")
    private Boolean supportsVirtualCards;

    @Column("supports_contactless")
    private Boolean supportsContactless;

    @Column("supports_international")
    private Boolean supportsInternational;

    @Column("supports_atm_withdrawal")
    private Boolean supportsAtmWithdrawal;

    @Column("supports_online_transactions")
    private Boolean supportsOnlineTransactions;

    @Column("supports_recurring_payments")
    private Boolean supportsRecurringPayments;

    @Column("supports_apple_pay")
    private Boolean supportsApplePay;

    @Column("supports_google_pay")
    private Boolean supportsGooglePay;

    @Column("supports_samsung_pay")
    private Boolean supportsSamsungPay;

    @Column("requires_pin")
    private Boolean requiresPin;

    @Column("requires_activation")
    private Boolean requiresActivation;

    @Column("currency_code")
    private String currencyCode;

    @Column("country_code")
    private String countryCode;

    @Column("terms_and_conditions_url")
    private String termsAndConditionsUrl;

    @Column("description")
    private String description;
}