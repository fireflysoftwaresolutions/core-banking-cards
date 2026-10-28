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


package io.fireflysoftware.core.banking.cards.models.entities.security;

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
 * Card Security entity.
 * Represents security features and settings for a payment card.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_security")
public class CardSecurity extends BaseEntity {
    @Id
    @Column("security_id")
    private UUID cardSecurityId;

    @Column("card_id")
    private UUID cardId;

    @Column("program_id")
    private UUID programId;

    @Column("security_feature_name")
    private String securityFeatureName;

    @Column("security_feature_code")
    private String securityFeatureCode;

    @Column("security_value")
    private String securityValue;

    @Column("is_enabled")
    private Boolean isEnabled;

    @Column("is_mandatory")
    private Boolean isMandatory;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column("last_used_date")
    private LocalDateTime lastUsedDate;

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

    @Column("supports_3d_secure")
    private Boolean supports3dSecure;

    @Column("supports_tokenization")
    private Boolean supportsTokenization;

    @Column("supports_biometric")
    private Boolean supportsBiometric;

    @Column("supports_pin")
    private Boolean supportsPin;

    @Column("supports_otp")
    private Boolean supportsOtp;

    @Column("fraud_detection_level")
    private String fraudDetectionLevel;

    @Column("description")
    private String description;
}
