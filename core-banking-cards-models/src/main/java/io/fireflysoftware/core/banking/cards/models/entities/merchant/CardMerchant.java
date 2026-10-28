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


package io.fireflysoftware.core.banking.cards.models.entities.merchant;

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
 * Card Merchant entity.
 * Represents merchants that accept card payments, including merchant details, categories,
 * and relationships with card networks and issuers.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_merchant")
public class CardMerchant extends BaseEntity {

    @Id
    @Column("merchant_id")
    private UUID merchantId;

    @Column("merchant_reference")
    private String merchantReference;

    @Column("merchant_name")
    private String merchantName;

    @Column("merchant_legal_name")
    private String merchantLegalName;

    @Column("merchant_display_name")
    private String merchantDisplayName;

    @Column("merchant_description")
    private String merchantDescription;

    @Column("merchant_category_code")
    private String merchantCategoryCode;

    @Column("merchant_category_name")
    private String merchantCategoryName;

    @Column("merchant_type")
    private String merchantType;

    @Column("merchant_status")
    private String merchantStatus;

    @Column("is_active")
    private Boolean isActive;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("deactivation_date")
    private LocalDateTime deactivationDate;

    @Column("deactivation_reason")
    private String deactivationReason;

    @Column("tax_id")
    private String taxId;

    @Column("registration_number")
    private String registrationNumber;

    @Column("website_url")
    private String websiteUrl;

    @Column("logo_url")
    private String logoUrl;

    @Column("address_line1")
    private String addressLine1;

    @Column("address_line2")
    private String addressLine2;

    @Column("city")
    private String city;

    @Column("state")
    private String state;

    @Column("postal_code")
    private String postalCode;

    @Column("country")
    private String country;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("contact_person_name")
    private String contactPersonName;

    @Column("contact_person_title")
    private String contactPersonTitle;

    @Column("contact_person_phone")
    private String contactPersonPhone;

    @Column("contact_person_email")
    private String contactPersonEmail;

    @Column("is_online")
    private Boolean isOnline;

    @Column("is_physical")
    private Boolean isPhysical;

    @Column("is_mobile")
    private Boolean isMobile;

    @Column("is_international")
    private Boolean isInternational;

    @Column("supported_currencies")
    private String supportedCurrencies;

    @Column("default_currency")
    private String defaultCurrency;

    @Column("supported_card_networks")
    private String supportedCardNetworks;

    @Column("supported_payment_methods")
    private String supportedPaymentMethods;

    @Column("is_high_risk")
    private Boolean isHighRisk;

    @Column("risk_rating")
    private String riskRating;

    @Column("risk_score")
    private Integer riskScore;

    @Column("risk_assessment_date")
    private LocalDateTime riskAssessmentDate;

    @Column("is_fraud_suspected")
    private Boolean isFraudSuspected;

    @Column("fraud_reason")
    private String fraudReason;

    @Column("fraud_report_date")
    private LocalDateTime fraudReportDate;

    @Column("is_blacklisted")
    private Boolean isBlacklisted;

    @Column("blacklist_reason")
    private String blacklistReason;

    @Column("blacklist_date")
    private LocalDateTime blacklistDate;

    @Column("is_settlement_enabled")
    private Boolean isSettlementEnabled;

    @Column("settlement_frequency")
    private String settlementFrequency;

    @Column("settlement_day")
    private Integer settlementDay;

    @Column("settlement_bank_name")
    private String settlementBankName;

    @Column("settlement_account_number")
    private String settlementAccountNumber;

    @Column("settlement_account_name")
    private String settlementAccountName;

    @Column("settlement_bank_code")
    private String settlementBankCode;

    @Column("settlement_currency")
    private String settlementCurrency;

    @Column("acquirer_id")
    private String acquirerId;

    @Column("acquirer_name")
    private String acquirerName;

    @Column("processor_id")
    private String processorId;

    @Column("processor_name")
    private String processorName;

    @Column("terminal_ids")
    private String terminalIds;

    @Column("notes")
    private String notes;
}