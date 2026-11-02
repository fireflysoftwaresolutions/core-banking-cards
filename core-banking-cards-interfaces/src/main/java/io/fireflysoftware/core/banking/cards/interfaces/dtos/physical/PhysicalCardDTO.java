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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.physical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Physical Card.
 * Contains the essential information about a Physical Card that needs to be exposed through the API.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PhysicalCardDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID physicalCardId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @NotBlank(message = "Embossed name is required")
    @Size(min = 2, max = 26, message = "Embossed name must be between 2 and 26 characters")
    private String embossedName;

    @NotBlank(message = "Plastic ID is required")
    @Pattern(regexp = "^[A-Z0-9]{8,20}$", message = "Plastic ID must be 8-20 alphanumeric characters")
    private String plasticId;

    @FilterableId
    @NotNull(message = "Design ID is required")
    private UUID designId;

    @NotNull(message = "Contactless flag is required")
    private Boolean isContactless;

    @NotNull(message = "Chip flag is required")
    private Boolean isChip;

    @NotNull(message = "Magstripe flag is required")
    private Boolean isMagstripe;

    @NotBlank(message = "Manufacturing status is required")
    @Pattern(regexp = "^(PENDING|IN_PROGRESS|COMPLETED|FAILED)$", message = "Manufacturing status must be PENDING, IN_PROGRESS, COMPLETED, or FAILED")
    private String manufacturingStatus;

    @PastOrPresent(message = "Manufacturing date cannot be in the future")
    private LocalDateTime manufacturingDate;

    @NotBlank(message = "Shipping address is required")
    @Size(min = 10, max = 200, message = "Shipping address must be between 10 and 200 characters")
    private String shippingAddress;

    @NotBlank(message = "Shipping city is required")
    @Size(min = 2, max = 50, message = "Shipping city must be between 2 and 50 characters")
    private String shippingCity;

    private String shippingState;

    private String shippingCountry;

    private String shippingPostalCode;

    private String shippingMethod;

    private String shippingTrackingNumber;

    private String shippingCarrier;

    private LocalDateTime shipmentDate;

    private LocalDateTime estimatedDeliveryDate;

    private LocalDateTime actualDeliveryDate;

    private String activationMethod;

    private LocalDateTime activationDate;

    private Boolean isActivated;

    private String replacementReason;

    @FilterableId
    private UUID previousCardId;

    private String notes;
}
