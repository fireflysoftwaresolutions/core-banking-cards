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


package io.fireflysoftware.core.banking.cards.models.entities.card;

import io.fireflysoftware.core.banking.cards.interfaces.enums.card.CardStatusEnum;
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
 * Card entity.
 * Represents a payment card with its characteristics and relationships.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card")
public class Card extends BaseEntity {

    @Id
    @Column("card_id")
    private UUID cardId;

    @Column("card_number")
    private String cardNumber;

    @Column("masked_card_number")
    private String maskedCardNumber;

    @Column("card_sequence_number")
    private String cardSequenceNumber;

    @Column("bin_id")
    private UUID binId;

    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("card_network_id")
    private UUID cardNetworkId;

    @Column("issuer_id")
    private UUID issuerId;

    @Column("contract_id")
    private UUID contractId;

    @Column("account_id")
    private UUID accountId;

    @Column("party_id")
    private UUID partyId;

    @Column("card_status")
    private CardStatusEnum cardStatus;

    @Column("card_holder_name")
    private String cardHolderName;

    @Column("card_holder_id")
    private String cardHolderId;

    @Column("expiration_month")
    private Integer expirationMonth;

    @Column("expiration_year")
    private Integer expirationYear;

    @Column("cvv")
    private String cvv;

    @Column("pin")
    private String pin;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("issuance_date")
    private LocalDateTime issuanceDate;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("last_used_date")
    private LocalDateTime lastUsedDate;

    @Column("is_physical")
    private Boolean isPhysical;

    @Column("is_virtual")
    private Boolean isVirtual;

    @Column("is_primary")
    private Boolean isPrimary;

    @Column("is_active")
    private Boolean isActive;

    @Column("is_locked")
    private Boolean isLocked;

    @Column("lock_reason")
    private String lockReason;

    @Column("daily_limit")
    private Double dailyLimit;

    @Column("monthly_limit")
    private Double monthlyLimit;

    @Column("credit_limit")
    private Double creditLimit;

    @Column("available_balance")
    private Double availableBalance;

    @Column("currency_code")
    private String currencyCode;

    @Column("design_id")
    private UUID designId;

    @Column("notes")
    private String notes;
}
