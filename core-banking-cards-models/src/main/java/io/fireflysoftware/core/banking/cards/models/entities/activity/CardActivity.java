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


package io.fireflysoftware.core.banking.cards.models.entities.activity;

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
 * Card Activity entity.
 * Represents a log of all activities related to a card, including status changes, configuration changes,
 * and other non-financial activities.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_activity")
public class CardActivity extends BaseEntity {

    @Id
    @Column("activity_id")
    private UUID activityId;

    @Column("card_id")
    private UUID cardId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("activity_reference")
    private String activityReference;

    @Column("activity_type")
    private String activityType;

    @Column("activity_category")
    private String activityCategory;

    @Column("activity_description")
    private String activityDescription;

    @Column("activity_timestamp")
    private LocalDateTime activityTimestamp;

    @Column("activity_channel")
    private String activityChannel;

    @Column("activity_source")
    private String activitySource;

    @Column("activity_status")
    private String activityStatus;

    @Column("activity_result")
    private String activityResult;

    @Column("activity_details")
    private String activityDetails;

    @Column("previous_value")
    private String previousValue;

    @Column("new_value")
    private String newValue;

    @Column("change_reason")
    private String changeReason;

    @Column("change_authorized_by")
    private String changeAuthorizedBy;

    @Column("ip_address")
    private String ipAddress;

    @Column("device_id")
    private String deviceId;

    @Column("device_type")
    private String deviceType;

    @Column("device_os")
    private String deviceOs;

    @Column("browser_type")
    private String browserType;

    @Column("user_agent")
    private String userAgent;

    @Column("geolocation")
    private String geolocation;

    @Column("is_party_initiated")
    private Boolean isPartyInitiated;

    @Column("is_system_initiated")
    private Boolean isSystemInitiated;

    @Column("is_agent_initiated")
    private Boolean isAgentInitiated;

    @Column("agent_id")
    private String agentId;

    @Column("agent_name")
    private String agentName;

    @Column("is_successful")
    private Boolean isSuccessful;

    @Column("failure_reason")
    private String failureReason;

    @Column("failure_code")
    private String failureCode;

    @Column("is_notification_sent")
    private Boolean isNotificationSent;

    @Column("notification_channel")
    private String notificationChannel;

    @Column("notification_timestamp")
    private LocalDateTime notificationTimestamp;

    @Column("notification_recipient")
    private String notificationRecipient;

    @Column("related_entity_type")
    private String relatedEntityType;

    @Column("related_entity_id")
    private UUID relatedEntityId;

    @Column("notes")
    private String notes;
}