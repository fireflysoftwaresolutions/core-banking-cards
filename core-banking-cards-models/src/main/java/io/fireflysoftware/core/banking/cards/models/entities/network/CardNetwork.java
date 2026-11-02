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


package io.fireflysoftware.core.banking.cards.models.entities.network;

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
 * Card Network entity.
 * Represents a payment card network like Visa, Mastercard, American Express, etc.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_network")
public class CardNetwork extends BaseEntity {

    @Id
    @Column("card_network_id")
    private UUID cardNetworkId;

    @Column("network_name")
    private String networkName;

    @Column("network_code")
    private String networkCode;

    @Column("network_logo_url")
    private String networkLogoUrl;

    @Column("support_contact")
    private String supportContact;

    @Column("api_endpoint")
    private String apiEndpoint;

    @Column("api_key")
    private String apiKey;

    @Column("api_secret")
    private String apiSecret;

    @Column("is_active")
    private Boolean isActive;

    @Column("description")
    private String description;
}