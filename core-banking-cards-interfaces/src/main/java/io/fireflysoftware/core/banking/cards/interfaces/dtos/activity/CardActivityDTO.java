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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Activity.
 * Contains the essential information about a Card Activity that needs to be exposed through the API.
 * A Card Activity represents a log of all activities related to a card, including status changes,
 * configuration changes, and other non-financial activities.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardActivityDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID activityId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotBlank(message = "Activity reference is required")
    @Size(max = 100, message = "Activity reference cannot exceed 100 characters")
    private String activityReference;

    @NotBlank(message = "Activity type is required")
    @Size(max = 50, message = "Activity type cannot exceed 50 characters")
    private String activityType;

    @Size(max = 50, message = "Activity category cannot exceed 50 characters")
    private String activityCategory;

    @Size(max = 500, message = "Activity description cannot exceed 500 characters")
    private String activityDescription;

    @NotNull(message = "Activity timestamp is required")
    @PastOrPresent(message = "Activity timestamp cannot be in the future")
    private LocalDateTime activityTimestamp;

    @Size(max = 50, message = "Activity channel cannot exceed 50 characters")
    private String activityChannel;

    @Size(max = 50, message = "Activity source cannot exceed 50 characters")
    private String activitySource;

    @Size(max = 50, message = "Activity status cannot exceed 50 characters")
    private String activityStatus;

    @Size(max = 50, message = "Activity result cannot exceed 50 characters")
    private String activityResult;

    @Size(max = 1000, message = "Activity details cannot exceed 1000 characters")
    private String activityDetails;

    @Size(max = 500, message = "Previous value cannot exceed 500 characters")
    private String previousValue;

    @Size(max = 500, message = "New value cannot exceed 500 characters")
    private String newValue;

    @Size(max = 255, message = "Change reason cannot exceed 255 characters")
    private String changeReason;

    @Size(max = 100, message = "Change authorized by cannot exceed 100 characters")
    private String changeAuthorizedBy;

    @Pattern(regexp = "^(?:(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|::1|::)$", message = "IP address must be a valid IPv4 or IPv6 address")
    private String ipAddress;

    @Size(max = 100, message = "Device ID cannot exceed 100 characters")
    private String deviceId;

    @Size(max = 50, message = "Device type cannot exceed 50 characters")
    private String deviceType;

    @Size(max = 50, message = "Device OS cannot exceed 50 characters")
    private String deviceOs;

    @Size(max = 50, message = "Browser type cannot exceed 50 characters")
    private String browserType;

    @Size(max = 500, message = "User agent cannot exceed 500 characters")
    private String userAgent;

    @Size(max = 100, message = "Geolocation cannot exceed 100 characters")
    private String geolocation;

    @NotNull(message = "Party initiated flag is required")
    private Boolean isPartyInitiated;

    @NotNull(message = "System initiated flag is required")
    private Boolean isSystemInitiated;

    @NotNull(message = "Agent initiated flag is required")
    private Boolean isAgentInitiated;

    @Size(max = 50, message = "Agent ID cannot exceed 50 characters")
    private String agentId;

    @Size(max = 100, message = "Agent name cannot exceed 100 characters")
    private String agentName;

    @NotNull(message = "Successful flag is required")
    private Boolean isSuccessful;

    @Size(max = 255, message = "Failure reason cannot exceed 255 characters")
    private String failureReason;

    @Size(max = 50, message = "Failure code cannot exceed 50 characters")
    private String failureCode;

    @NotNull(message = "Notification sent flag is required")
    private Boolean isNotificationSent;

    @Size(max = 50, message = "Notification channel cannot exceed 50 characters")
    private String notificationChannel;

    @PastOrPresent(message = "Notification timestamp cannot be in the future")
    private LocalDateTime notificationTimestamp;

    @Size(max = 100, message = "Notification recipient cannot exceed 100 characters")
    private String notificationRecipient;

    @Size(max = 50, message = "Related entity type cannot exceed 50 characters")
    private String relatedEntityType;

    private UUID relatedEntityId;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}