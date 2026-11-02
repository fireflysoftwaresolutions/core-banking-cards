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


package io.fireflysoftware.core.banking.cards.models.entities.physical;

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
 * Physical Card entity.
 * Represents the physical aspects of a payment card.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("physical_card")
public class PhysicalCard extends BaseEntity {

    @Id
    @Column("physical_card_id")
    private UUID physicalCardId;

    @Column("card_id")
    private UUID cardId;

    @Column("embossed_name")
    private String embossedName;

    @Column("plastic_id")
    private String plasticId;

    @Column("design_id")
    private UUID designId;

    @Column("is_contactless")
    private Boolean isContactless;

    @Column("is_chip")
    private Boolean isChip;

    @Column("is_magstripe")
    private Boolean isMagstripe;

    @Column("manufacturing_status")
    private String manufacturingStatus;

    @Column("manufacturing_date")
    private LocalDateTime manufacturingDate;

    @Column("shipping_address")
    private String shippingAddress;

    @Column("shipping_city")
    private String shippingCity;

    @Column("shipping_state")
    private String shippingState;

    @Column("shipping_country")
    private String shippingCountry;

    @Column("shipping_postal_code")
    private String shippingPostalCode;

    @Column("shipping_method")
    private String shippingMethod;

    @Column("shipping_tracking_number")
    private String shippingTrackingNumber;

    @Column("shipping_carrier")
    private String shippingCarrier;

    @Column("shipment_date")
    private LocalDateTime shipmentDate;

    @Column("estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @Column("actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column("activation_method")
    private String activationMethod;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("is_activated")
    private Boolean isActivated;

    @Column("replacement_reason")
    private String replacementReason;

    @Column("previous_card_id")
    private UUID previousCardId;

    @Column("notes")
    private String notes;
}
