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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.dispute;

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
 * Data Transfer Object for Card Dispute.
 * Contains the essential information about a Card Dispute that needs to be exposed through the API.
 * A Card Dispute represents a dispute filed by a cardholder against a transaction.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardDisputeDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID disputeId;

    @FilterableId
    private UUID cardId;

    @FilterableId
    private UUID transactionId;

    @FilterableId
    private UUID partyId;

    @FilterableId
    private UUID accountId;

    private String disputeReference;

    private String providerReference;

    private String networkReference;

    private String disputeReasonCode;

    private String disputeReasonDescription;

    private String disputeStatus;

    private String disputeStage;

    private BigDecimal disputeAmount;

    private String disputeCurrency;

    private LocalDateTime filingTimestamp;

    private LocalDateTime responseDueDate;

    private LocalDateTime resolutionTimestamp;

    private String resolutionCode;

    private String resolutionDescription;

    private Boolean isCardholderCredited;

    private LocalDateTime creditTimestamp;

    private BigDecimal creditAmount;

    private String creditCurrency;

    private Boolean isMerchantDebited;

    private LocalDateTime debitTimestamp;

    private BigDecimal debitAmount;

    private String debitCurrency;

    private String cardholderStatement;

    private String merchantResponse;

    private String evidenceDocuments;

    private UUID assignedAgentId;

    private String assignedAgentName;

    private LocalDateTime lastUpdatedTimestamp;

    private String notes;
}