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


package io.fireflysoftware.core.banking.cards.models.entities.issuer;

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
 * Issuer entity.
 * Represents a financial institution that issues payment cards to consumers.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("issuer")
public class Issuer extends BaseEntity {

    @Id
    @Column("issuer_id")
    private UUID issuerId;

    @Column("issuer_name")
    private String issuerName;

    @Column("issuer_code")
    private String issuerCode;

    @Column("country_code")
    private String countryCode;

    @Column("contact_email")
    private String contactEmail;

    @Column("contact_phone")
    private String contactPhone;

    @Column("is_active")
    private Boolean isActive;

    @Column("support_url")
    private String supportUrl;

    @Column("logo_url")
    private String logoUrl;

    @Column("description")
    private String description;
}