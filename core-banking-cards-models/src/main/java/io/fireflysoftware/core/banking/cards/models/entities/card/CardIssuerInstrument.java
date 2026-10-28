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

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** Read-only issuer binding; lifecycle changes belong to the native issuance boundary. */
@Table("card_issuer_instrument")
public record CardIssuerInstrument(
        @Id @Column("card_id") UUID cardId,
        @Column("tenant_id") UUID tenantId,
        @Column("party_id") UUID partyId,
        @Column("account_id") UUID accountId,
        @Column("adapter_id") String adapterId,
        @Column("instrument_reference") String instrumentReference,
        @Column("binding_version") long bindingVersion) {}
