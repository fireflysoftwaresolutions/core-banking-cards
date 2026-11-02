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


package io.fireflysoftware.core.banking.cards.models.entities.cardtype;

import io.fireflysoftware.core.banking.cards.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Card Type entity.
 * Represents a type of payment card (debit, credit, prepaid, etc.) with its specific characteristics.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_type")
public class CardType extends BaseEntity {

    @Id
    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("type_name")
    private String typeName;

    @Column("type_code")
    private String typeCode;

    @Column("is_credit")
    private Boolean isCredit;

    @Column("is_debit")
    private Boolean isDebit;

    @Column("is_prepaid")
    private Boolean isPrepaid;

    @Column("is_virtual")
    private Boolean isVirtual;

    @Column("is_commercial")
    private Boolean isCommercial;

    @Column("is_gift")
    private Boolean isGift;

    @Column("default_credit_limit")
    private Double defaultCreditLimit;

    @Column("default_daily_limit")
    private Double defaultDailyLimit;

    @Column("default_monthly_limit")
    private Double defaultMonthlyLimit;

    @Column("is_active")
    private Boolean isActive;

    @Column("description")
    private String description;
}