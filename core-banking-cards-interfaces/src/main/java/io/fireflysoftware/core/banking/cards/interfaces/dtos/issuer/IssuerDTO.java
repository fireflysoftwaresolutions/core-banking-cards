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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.issuer;

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
 * Data Transfer Object for Issuer.
 * Contains the essential information about an Issuer that needs to be exposed through the API.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class IssuerDTO extends BaseDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID issuerId;

    @NotBlank(message = "Issuer name is required")
    @Size(min = 2, max = 100, message = "Issuer name must be between 2 and 100 characters")
    private String issuerName;

    @NotBlank(message = "Issuer code is required")
    @Pattern(regexp = "^[A-Z0-9_]{2,20}$", message = "Issuer code must be 2-20 characters, uppercase letters, numbers, and underscores only")
    private String issuerCode;

    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be a 2-letter ISO code")
    private String countryCode;

    @Email(message = "Contact email must be a valid email address")
    @Size(max = 100, message = "Contact email cannot exceed 100 characters")
    private String contactEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Contact phone must be a valid international phone number")
    private String contactPhone;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @Pattern(regexp = "^https?://.*", message = "Support URL must be a valid HTTP/HTTPS URL")
    private String supportUrl;

    @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif|svg)$", message = "Logo URL must be a valid HTTP/HTTPS URL pointing to an image file")
    private String logoUrl;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}