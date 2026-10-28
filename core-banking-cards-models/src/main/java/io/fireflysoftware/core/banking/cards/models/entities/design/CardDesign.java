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


package io.fireflysoftware.core.banking.cards.models.entities.design;

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
 * Card Design entity.
 * Represents a physical design template for payment cards.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_design")
public class CardDesign extends BaseEntity {

    @Id
    @Column("design_id")
    private UUID designId;

    @Column("design_name")
    private String designName;

    @Column("design_code")
    private String designCode;

    @Column("front_image_url")
    private String frontImageUrl;

    @Column("back_image_url")
    private String backImageUrl;

    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("issuer_id")
    private UUID issuerId;

    @Column("card_network_id")
    private UUID cardNetworkId;

    @Column("is_customizable")
    private Boolean isCustomizable;

    @Column("is_default")
    private Boolean isDefault;

    @Column("is_active")
    private Boolean isActive;

    @Column("description")
    private String description;
}