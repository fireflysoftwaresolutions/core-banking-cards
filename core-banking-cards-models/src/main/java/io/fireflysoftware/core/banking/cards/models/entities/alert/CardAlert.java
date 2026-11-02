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


package io.fireflysoftware.core.banking.cards.models.entities.alert;

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
 * Card Alert entity.
 * Represents an alert or notification related to a card or transaction.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_alert")
public class CardAlert extends BaseEntity {

    @Id
    @Column("alert_id")
    private UUID alertId;

    @Column("card_id")
    private UUID cardId;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("alert_reference")
    private String alertReference;

    @Column("alert_type")
    private String alertType;

    @Column("alert_category")
    private String alertCategory;

    @Column("alert_severity")
    private String alertSeverity;

    @Column("alert_status")
    private String alertStatus;

    @Column("alert_title")
    private String alertTitle;

    @Column("alert_message")
    private String alertMessage;

    @Column("alert_details")
    private String alertDetails;

    @Column("trigger_event")
    private String triggerEvent;

    @Column("trigger_timestamp")
    private LocalDateTime triggerTimestamp;

    @Column("trigger_amount")
    private BigDecimal triggerAmount;

    @Column("trigger_currency")
    private String triggerCurrency;

    @Column("trigger_location")
    private String triggerLocation;

    @Column("trigger_merchant")
    private String triggerMerchant;

    @Column("trigger_merchant_category")
    private String triggerMerchantCategory;

    @Column("is_party_configurable")
    private Boolean isPartyConfigurable;

    @Column("is_system_generated")
    private Boolean isSystemGenerated;

    @Column("is_read")
    private Boolean isRead;

    @Column("read_timestamp")
    private LocalDateTime readTimestamp;

    @Column("delivery_channel")
    private String deliveryChannel;

    @Column("delivery_status")
    private String deliveryStatus;

    @Column("delivery_timestamp")
    private LocalDateTime deliveryTimestamp;

    @Column("delivery_recipient")
    private String deliveryRecipient;

    @Column("delivery_details")
    private String deliveryDetails;

    @Column("expiration_timestamp")
    private LocalDateTime expirationTimestamp;

    @Column("action_required")
    private Boolean actionRequired;

    @Column("action_type")
    private String actionType;

    @Column("action_taken")
    private String actionTaken;

    @Column("action_timestamp")
    private LocalDateTime actionTimestamp;

    @Column("action_by")
    private String actionBy;

    @Column("notes")
    private String notes;
}