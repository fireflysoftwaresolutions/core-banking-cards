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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.fraud;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Fraud Case.
 * Contains the essential information about a Fraud Case that needs to be exposed through the API.
 * A Fraud Case represents a fraud case related to a card or transaction.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FraudCaseDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID fraudCaseId;

    @FilterableId
    private UUID cardId;

    @FilterableId
    private UUID transactionId;

    @FilterableId
    private UUID partyId;

    @FilterableId
    private UUID accountId;

    private String caseReference;

    private String providerReference;

    private String networkReference;

    private String fraudType;

    private String fraudReasonCode;

    private String fraudReasonDescription;

    private String fraudStatus;

    private Integer fraudScore;

    private String riskLevel;

    private String detectionSource;

    private LocalDateTime detectionTimestamp;

    private Boolean reportedByParty;

    private LocalDateTime reportedTimestamp;

    private BigDecimal affectedAmount;

    private String affectedCurrency;

    private Boolean isCardBlocked;

    private LocalDateTime blockTimestamp;

    private Boolean isPartyNotified;

    private LocalDateTime notificationTimestamp;

    private String notificationMethod;

    private Boolean isCardholderCredited;

    private LocalDateTime creditTimestamp;

    private BigDecimal creditAmount;

    private String creditCurrency;

    private LocalDateTime resolutionTimestamp;

    private String resolutionCode;

    private String resolutionDescription;

    private Boolean isPoliceReportFiled;

    private String policeReportNumber;

    private LocalDateTime policeReportDate;

    private String evidenceDocuments;

    private UUID assignedAgentId;

    private String assignedAgentName;

    private LocalDateTime lastUpdatedTimestamp;
}