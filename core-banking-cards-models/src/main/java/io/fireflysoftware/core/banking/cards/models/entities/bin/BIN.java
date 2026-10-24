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


package io.fireflysoftware.core.banking.cards.models.entities.bin;

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
 * Bank Identification Number (BIN) entity.
 * The BIN is the first 6 or 8 digits of a payment card number (PAN) that identifies the card issuer.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("bin")
public class BIN extends BaseEntity {

    @Id
    @Column("bin_id")
    private UUID binId;

    @Column("bin_number")
    private String binNumber;

    @Column("bin_length")
    private Integer binLength;

    @Column("issuer_id")
    private UUID issuerId;

    @Column("card_network_id")
    private UUID cardNetworkId;

    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("country_code")
    private String countryCode;

    @Column("currency_code")
    private String currencyCode;

    @Column("is_active")
    private Boolean isActive;

    @Column("description")
    private String description;
}