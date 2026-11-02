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


package io.fireflysoftware.core.banking.cards.models.entities.configuration;

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
 * Card Configuration entity.
 * Represents configuration settings for a payment card.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_configuration")
public class CardConfiguration extends BaseEntity {
    @Id
    @Column("card_configuration_id")
    private UUID cardConfigurationId;

    @Column("card_id")
    private UUID cardId;

    @Column("program_id")
    private UUID programId;

    @Column("config_key")
    private String configKey;

    @Column("config_value")
    private String configValue;

    @Column("config_type")
    private String configType;

    @Column("is_active")
    private Boolean isActive;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("is_system_default")
    private Boolean isSystemDefault;

    @Column("is_program_default")
    private Boolean isProgramDefault;

    @Column("is_party_configurable")
    private Boolean isPartyConfigurable;

    @Column("allowed_values")
    private String allowedValues;

    @Column("min_value")
    private String minValue;

    @Column("max_value")
    private String maxValue;

    @Column("description")
    private String description;
}
