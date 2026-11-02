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


package io.fireflysoftware.core.banking.cards.models.entities.transaction;

import io.fireflysoftware.core.banking.cards.interfaces.enums.transaction.TransactionStatusEnum;
import io.fireflysoftware.core.banking.cards.interfaces.enums.transaction.TransactionTypeEnum;
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
 * Card Transaction entity.
 * Represents a financial transaction performed with a payment card.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_transaction")
public class CardTransaction extends BaseEntity {

    @Id
    @Column("card_transaction_id")
    private UUID cardTransactionId;

    @Column("card_id")
    private UUID cardId;

    @Column("account_id")
    private UUID accountId;

    @Column("party_id")
    private UUID partyId;

    @Column("transaction_reference")
    private String transactionReference;

    @Column("provider_reference")
    private String providerReference;

    @Column("network_reference")
    private String networkReference;

    @Column("transaction_type")
    private TransactionTypeEnum transactionType;

    @Column("transaction_status")
    private TransactionStatusEnum transactionStatus;

    @Column("transaction_timestamp")
    private LocalDateTime transactionTimestamp;

    @Column("settlement_timestamp")
    private LocalDateTime settlementTimestamp;

    @Column("authorization_code")
    private String authorizationCode;

    @Column("response_code")
    private String responseCode;

    @Column("response_message")
    private String responseMessage;

    @Column("transaction_amount")
    private BigDecimal transactionAmount;

    @Column("transaction_currency")
    private String transactionCurrency;

    @Column("billing_amount")
    private BigDecimal billingAmount;

    @Column("billing_currency")
    private String billingCurrency;

    @Column("exchange_rate")
    private BigDecimal exchangeRate;

    @Column("fee_amount")
    private BigDecimal feeAmount;

    @Column("fee_currency")
    private String feeCurrency;

    @Column("tax_amount")
    private BigDecimal taxAmount;

    @Column("tax_currency")
    private String taxCurrency;

    @Column("merchant_id")
    private String merchantId;

    @Column("merchant_name")
    private String merchantName;

    @Column("merchant_category_code")
    private String merchantCategoryCode;

    @Column("merchant_category_name")
    private String merchantCategoryName;

    @Column("merchant_city")
    private String merchantCity;

    @Column("merchant_state")
    private String merchantState;

    @Column("merchant_country")
    private String merchantCountry;

    @Column("merchant_postal_code")
    private String merchantPostalCode;

    @Column("terminal_id")
    private String terminalId;

    @Column("terminal_type")
    private String terminalType;

    @Column("entry_mode")
    private String entryMode;

    @Column("channel_type")
    private String channelType;

    @Column("is_card_present")
    private Boolean isCardPresent;

    @Column("is_pin_verified")
    private Boolean isPinVerified;

    @Column("is_signature_verified")
    private Boolean isSignatureVerified;

    @Column("is_3d_secure")
    private Boolean is3dSecure;

    @Column("is_tokenized")
    private Boolean isTokenized;

    @Column("is_recurring")
    private Boolean isRecurring;

    @Column("is_installment")
    private Boolean isInstallment;

    @Column("installment_plan")
    private String installmentPlan;

    @Column("installment_number")
    private Integer installmentNumber;

    @Column("total_installments")
    private Integer totalInstallments;

    @Column("is_international")
    private Boolean isInternational;

    @Column("is_disputed")
    private Boolean isDisputed;

    @Column("dispute_reason")
    private String disputeReason;

    @Column("dispute_status")
    private String disputeStatus;

    @Column("dispute_timestamp")
    private LocalDateTime disputeTimestamp;

    @Column("is_fraud_suspected")
    private Boolean isFraudSuspected;

    @Column("fraud_reason")
    private String fraudReason;

    @Column("fraud_score")
    private Integer fraudScore;

    @Column("device_id")
    private String deviceId;

    @Column("ip_address")
    private String ipAddress;

    @Column("user_agent")
    private String userAgent;

    @Column("geolocation")
    private String geolocation;

    @Column("notes")
    private String notes;
}
