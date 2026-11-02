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


package io.fireflysoftware.core.banking.cards.models.entities.dispute;

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
 * Card Dispute entity.
 * Represents a dispute filed by a cardholder against a transaction.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_dispute")
public class CardDispute extends BaseEntity {

    @Id
    @Column("dispute_id")
    private UUID disputeId;

    @Column("card_id")
    private UUID cardId;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("dispute_reference")
    private String disputeReference;

    @Column("provider_reference")
    private String providerReference;

    @Column("network_reference")
    private String networkReference;

    @Column("dispute_reason_code")
    private String disputeReasonCode;

    @Column("dispute_reason_description")
    private String disputeReasonDescription;

    @Column("dispute_status")
    private String disputeStatus;

    @Column("dispute_stage")
    private String disputeStage;

    @Column("dispute_amount")
    private BigDecimal disputeAmount;

    @Column("dispute_currency")
    private String disputeCurrency;

    @Column("filing_timestamp")
    private LocalDateTime filingTimestamp;

    @Column("response_due_date")
    private LocalDateTime responseDueDate;

    @Column("resolution_timestamp")
    private LocalDateTime resolutionTimestamp;

    @Column("resolution_code")
    private String resolutionCode;

    @Column("resolution_description")
    private String resolutionDescription;

    @Column("is_cardholder_credited")
    private Boolean isCardholderCredited;

    @Column("credit_timestamp")
    private LocalDateTime creditTimestamp;

    @Column("credit_amount")
    private BigDecimal creditAmount;

    @Column("credit_currency")
    private String creditCurrency;

    @Column("is_merchant_debited")
    private Boolean isMerchantDebited;

    @Column("debit_timestamp")
    private LocalDateTime debitTimestamp;

    @Column("debit_amount")
    private BigDecimal debitAmount;

    @Column("debit_currency")
    private String debitCurrency;

    @Column("cardholder_statement")
    private String cardholderStatement;

    @Column("merchant_response")
    private String merchantResponse;

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