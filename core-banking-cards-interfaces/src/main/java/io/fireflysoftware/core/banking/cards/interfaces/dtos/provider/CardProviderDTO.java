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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.provider.ProviderStatusEnum;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardProviderDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID cardProviderId;

    @FilterableId
    private UUID cardId;

    @NotBlank(message = "Provider name is required")
    @Size(min = 2, max = 100, message = "Provider name must be between 2 and 100 characters")
    private String providerName;

    @NotBlank(message = "Provider code is required")
    @Pattern(regexp = "^[A-Z0-9_]{2,20}$", message = "Provider code must be 2-20 characters, uppercase letters, numbers, and underscores only")
    private String providerCode;

    @NotBlank(message = "Provider type is required")
    @Size(max = 50, message = "Provider type cannot exceed 50 characters")
    private String providerType;

    @Pattern(regexp = "^https?://.*", message = "API base URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "API base URL cannot exceed 500 characters")
    private String apiBaseUrl;

    @Size(max = 20, message = "API version cannot exceed 20 characters")
    private String apiVersion;

    @Size(max = 255, message = "API key cannot exceed 255 characters")
    private String apiKey;

    @Size(max = 255, message = "API secret cannot exceed 255 characters")
    private String apiSecret;

    @Size(max = 100, message = "API username cannot exceed 100 characters")
    private String apiUsername;

    @Size(max = 255, message = "API password cannot exceed 255 characters")
    private String apiPassword;

    @Size(max = 1000, message = "API certificate cannot exceed 1000 characters")
    private String apiCertificate;

    @Pattern(regexp = "^https?://.*", message = "Webhook URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Webhook URL cannot exceed 500 characters")
    private String webhookUrl;

    @Size(max = 255, message = "Webhook secret cannot exceed 255 characters")
    private String webhookSecret;

    @NotNull(message = "Physical cards support flag is required")
    private Boolean supportsPhysicalCards;

    @NotNull(message = "Virtual cards support flag is required")
    private Boolean supportsVirtualCards;

    @NotNull(message = "Tokenization support flag is required")
    private Boolean supportsTokenization;

    @NotNull(message = "3D Secure support flag is required")
    private Boolean supports3dSecure;

    @NotNull(message = "Apple Pay support flag is required")
    private Boolean supportsApplePay;

    @NotNull(message = "Google Pay support flag is required")
    private Boolean supportsGooglePay;

    @NotNull(message = "Samsung Pay support flag is required")
    private Boolean supportsSamsungPay;

    @Size(max = 100, message = "Contact name cannot exceed 100 characters")
    private String contactName;

    @Email(message = "Contact email must be a valid email address")
    @Size(max = 100, message = "Contact email cannot exceed 100 characters")
    private String contactEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Contact phone must be a valid international phone number")
    private String contactPhone;
    @Pattern(regexp = "^https?://.*", message = "Support URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Support URL cannot exceed 500 characters")
    private String supportUrl;

    @Email(message = "Support email must be a valid email address")
    @Size(max = 100, message = "Support email cannot exceed 100 characters")
    private String supportEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Support phone must be a valid international phone number")
    private String supportPhone;

    @PastOrPresent(message = "Contract start date cannot be in the future")
    private LocalDateTime contractStartDate;

    @Future(message = "Contract end date must be in the future")
    private LocalDateTime contractEndDate;

    @NotNull(message = "Provider status is required")
    private ProviderStatusEnum status;

    @PastOrPresent(message = "Last connection date cannot be in the future")
    private LocalDateTime lastConnectionDate;

    @Size(max = 50, message = "Last connection status cannot exceed 50 characters")
    private String lastConnectionStatus;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}