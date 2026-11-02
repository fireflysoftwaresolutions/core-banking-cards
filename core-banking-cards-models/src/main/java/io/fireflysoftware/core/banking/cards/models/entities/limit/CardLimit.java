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


package io.fireflysoftware.core.banking.cards.models.entities.limit;

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
 * Card Limit entity.
 * Represents spending limits for a payment card.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_limit")
public class CardLimit extends BaseEntity {

    @Id
    @Column("limit_id")
    private UUID cardLimitId;

    @Column("card_id")
    private UUID cardId;

    @Column("program_id")
    private UUID programId;

    @Column("limit_name")
    private String limitName;

    @Column("limit_code")
    private String limitCode;

    @Column("limit_type")
    private String limitType;

    @Column("channel_type")
    private String channelType;

    @Column("transaction_type")
    private String transactionType;

    @Column("merchant_category_code")
    private String merchantCategoryCode;

    @Column("country_code")
    private String countryCode;

    @Column("currency_code")
    private String currencyCode;

    @Column("limit_value")
    private BigDecimal limitAmount;

    @Column("current_usage")
    private BigDecimal currentUsage;

    @Column("available_amount")
    private BigDecimal availableAmount;

    @Column("reset_period")
    private String resetPeriod;

    @Column("reset_day")
    private Integer resetDay;

    @Column("reset_hour")
    private Integer resetHour;

    @Column("last_reset_date")
    private LocalDateTime lastResetDate;

    @Column("next_reset_date")
    private LocalDateTime nextResetDate;

    @Column("effective_from")
    private LocalDateTime effectiveFrom;

    @Column("effective_to")
    private LocalDateTime effectiveTo;

    @Column("is_active")
    private Boolean isActive;

    @Column("is_system_default")
    private Boolean isSystemDefault;

    @Column("is_program_default")
    private Boolean isProgramDefault;

    @Column("is_party_configurable")
    private Boolean isPartyConfigurable;

    @Column("min_limit")
    private BigDecimal minLimit;

    @Column("max_limit")
    private BigDecimal maxLimit;

    @Column("description")
    private String description;
}
