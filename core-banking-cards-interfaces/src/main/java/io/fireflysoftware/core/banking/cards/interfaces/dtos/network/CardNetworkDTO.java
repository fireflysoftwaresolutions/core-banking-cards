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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.util.UUID;

/**
 * Data Transfer Object for Card Network.
 * Contains the essential information about a Card Network that needs to be exposed through the API.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardNetworkDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID cardNetworkId;

    @NotBlank(message = "Network name is required")
    @Size(min = 2, max = 100, message = "Network name must be between 2 and 100 characters")
    private String networkName;

    @NotBlank(message = "Network code is required")
    @Pattern(regexp = "^[A-Z0-9_]{2,20}$", message = "Network code must be 2-20 characters, uppercase letters, numbers, and underscores only")
    private String networkCode;

    @Pattern(regexp = "^https?://.*", message = "Network logo URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Network logo URL cannot exceed 500 characters")
    private String networkLogoUrl;

    @Email(message = "Support contact must be a valid email address")
    @Size(max = 100, message = "Support contact cannot exceed 100 characters")
    private String supportContact;

    @Pattern(regexp = "^https?://.*", message = "API endpoint must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "API endpoint cannot exceed 500 characters")
    private String apiEndpoint;

    @Size(max = 255, message = "API key cannot exceed 255 characters")
    private String apiKey;

    @Size(max = 255, message = "API secret cannot exceed 255 characters")
    private String apiSecret;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}