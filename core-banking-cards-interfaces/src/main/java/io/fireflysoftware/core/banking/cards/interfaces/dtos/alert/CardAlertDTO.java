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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Alert.
 * Contains the essential information about a Card Alert that needs to be exposed through the API.
 * A Card Alert represents an alert or notification related to a card or transaction.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardAlertDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID alertId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @FilterableId
    private UUID transactionId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotBlank(message = "Alert reference is required")
    @Size(min = 5, max = 50, message = "Alert reference must be between 5 and 50 characters")
    private String alertReference;

    @NotBlank(message = "Alert type is required")
    @Pattern(regexp = "^(TRANSACTION|SECURITY|LIMIT|PAYMENT|FRAUD|SYSTEM)$", message = "Alert type must be TRANSACTION, SECURITY, LIMIT, PAYMENT, FRAUD, or SYSTEM")
    private String alertType;

    @NotBlank(message = "Alert category is required")
    @Pattern(regexp = "^(HIGH_VALUE|UNUSUAL_ACTIVITY|DECLINED|LIMIT_EXCEEDED|FRAUD_SUSPECTED)$", message = "Alert category must be HIGH_VALUE, UNUSUAL_ACTIVITY, DECLINED, LIMIT_EXCEEDED, or FRAUD_SUSPECTED")
    private String alertCategory;

    @NotBlank(message = "Alert severity is required")
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "Alert severity must be LOW, MEDIUM, HIGH, or CRITICAL")
    private String alertSeverity;

    @NotBlank(message = "Alert status is required")
    @Pattern(regexp = "^(ACTIVE|ACKNOWLEDGED|RESOLVED|DISMISSED)$", message = "Alert status must be ACTIVE, ACKNOWLEDGED, RESOLVED, or DISMISSED")
    private String alertStatus;

    @NotBlank(message = "Alert title is required")
    @Size(min = 5, max = 100, message = "Alert title must be between 5 and 100 characters")
    private String alertTitle;

    @NotBlank(message = "Alert message is required")
    @Size(min = 10, max = 500, message = "Alert message must be between 10 and 500 characters")
    private String alertMessage;

    @Size(max = 1000, message = "Alert details cannot exceed 1000 characters")
    private String alertDetails;

    @NotBlank(message = "Trigger event is required")
    @Size(min = 5, max = 100, message = "Trigger event must be between 5 and 100 characters")
    private String triggerEvent;

    @NotNull(message = "Trigger timestamp is required")
    @PastOrPresent(message = "Trigger timestamp cannot be in the future")
    private LocalDateTime triggerTimestamp;

    private BigDecimal triggerAmount;

    private String triggerCurrency;

    private String triggerLocation;

    private String triggerMerchant;

    private String triggerMerchantCategory;

    private Boolean isPartyConfigurable;

    private Boolean isSystemGenerated;

    private Boolean isRead;

    private LocalDateTime readTimestamp;

    private String deliveryChannel;

    private String deliveryStatus;

    private LocalDateTime deliveryTimestamp;

    private String deliveryRecipient;

    private String deliveryDetails;

    private LocalDateTime expirationTimestamp;

    private Boolean actionRequired;

    private String actionType;

    private String actionTaken;

    private LocalDateTime actionTimestamp;

    private String actionBy;
}