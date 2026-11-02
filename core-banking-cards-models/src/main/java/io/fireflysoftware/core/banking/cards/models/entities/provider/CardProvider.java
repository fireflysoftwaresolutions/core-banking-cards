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


package io.fireflysoftware.core.banking.cards.models.entities.provider;

import io.fireflysoftware.core.banking.cards.interfaces.enums.provider.ProviderStatusEnum;
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
 * Card Provider entity.
 * Represents a provider that processes card transactions and manages card services.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_provider")
public class CardProvider extends BaseEntity {
    @Id
    @Column("card_provider_id")
    private UUID cardProviderId;

    @Column("card_id")
    private UUID cardId;

    @Column("provider_name")
    private String providerName;

    @Column("provider_code")
    private String providerCode;

    @Column("provider_type")
    private String providerType;

    @Column("api_base_url")
    private String apiBaseUrl;

    @Column("api_version")
    private String apiVersion;

    @Column("api_key")
    private String apiKey;

    @Column("api_secret")
    private String apiSecret;

    @Column("api_username")
    private String apiUsername;

    @Column("api_password")
    private String apiPassword;

    @Column("api_certificate")
    private String apiCertificate;

    @Column("webhook_url")
    private String webhookUrl;

    @Column("webhook_secret")
    private String webhookSecret;

    @Column("supports_physical_cards")
    private Boolean supportsPhysicalCards;

    @Column("supports_virtual_cards")
    private Boolean supportsVirtualCards;

    @Column("supports_tokenization")
    private Boolean supportsTokenization;

    @Column("supports_3d_secure")
    private Boolean supports3dSecure;

    @Column("supports_apple_pay")
    private Boolean supportsApplePay;

    @Column("supports_google_pay")
    private Boolean supportsGooglePay;

    @Column("supports_samsung_pay")
    private Boolean supportsSamsungPay;

    @Column("contact_name")
    private String contactName;

    @Column("contact_email")
    private String contactEmail;

    @Column("contact_phone")
    private String contactPhone;

    @Column("support_url")
    private String supportUrl;

    @Column("support_email")
    private String supportEmail;

    @Column("support_phone")
    private String supportPhone;

    @Column("contract_start_date")
    private LocalDateTime contractStartDate;

    @Column("contract_end_date")
    private LocalDateTime contractEndDate;

    @Column("status")
    private ProviderStatusEnum status;

    @Column("last_connection_date")
    private LocalDateTime lastConnectionDate;

    @Column("last_connection_status")
    private String lastConnectionStatus;

    @Column("description")
    private String description;
}
