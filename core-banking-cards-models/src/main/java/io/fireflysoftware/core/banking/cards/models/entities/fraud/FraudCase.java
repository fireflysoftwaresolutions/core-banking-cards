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


package io.fireflysoftware.core.banking.cards.models.entities.fraud;

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
 * Fraud Case entity.
 * Represents a fraud case related to a card or transaction.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("fraud_case")
public class FraudCase extends BaseEntity {

    @Id
    @Column("fraud_case_id")
    private UUID fraudCaseId;

    @Column("card_id")
    private UUID cardId;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("case_reference")
    private String caseReference;

    @Column("provider_reference")
    private String providerReference;

    @Column("network_reference")
    private String networkReference;

    @Column("fraud_type")
    private String fraudType;

    @Column("fraud_reason_code")
    private String fraudReasonCode;

    @Column("fraud_reason_description")
    private String fraudReasonDescription;

    @Column("fraud_status")
    private String fraudStatus;

    @Column("fraud_score")
    private Integer fraudScore;

    @Column("risk_level")
    private String riskLevel;

    @Column("detection_source")
    private String detectionSource;

    @Column("detection_timestamp")
    private LocalDateTime detectionTimestamp;

    @Column("reported_by_party")
    private Boolean reportedByParty;

    @Column("reported_timestamp")
    private LocalDateTime reportedTimestamp;

    @Column("affected_amount")
    private BigDecimal affectedAmount;

    @Column("affected_currency")
    private String affectedCurrency;

    @Column("is_card_blocked")
    private Boolean isCardBlocked;

    @Column("block_timestamp")
    private LocalDateTime blockTimestamp;

    @Column("is_party_notified")
    private Boolean isPartyNotified;

    @Column("notification_timestamp")
    private LocalDateTime notificationTimestamp;

    @Column("notification_method")
    private String notificationMethod;

    @Column("is_cardholder_credited")
    private Boolean isCardholderCredited;

    @Column("credit_timestamp")
    private LocalDateTime creditTimestamp;

    @Column("credit_amount")
    private BigDecimal creditAmount;

    @Column("credit_currency")
    private String creditCurrency;

    @Column("resolution_timestamp")
    private LocalDateTime resolutionTimestamp;

    @Column("resolution_code")
    private String resolutionCode;

    @Column("resolution_description")
    private String resolutionDescription;

    @Column("is_police_report_filed")
    private Boolean isPoliceReportFiled;

    @Column("police_report_number")
    private String policeReportNumber;

    @Column("police_report_date")
    private LocalDateTime policeReportDate;

    @Column("evidence_documents")
    private String evidenceDocuments;

    @Column("assigned_agent_id")
    private UUID assignedAgentId;

    @Column("assigned_agent_name")
    private String assignedAgentName;

    @Column("last_updated_timestamp")
    private LocalDateTime lastUpdatedTimestamp;

    @Column("notes")
    private String notes;
}