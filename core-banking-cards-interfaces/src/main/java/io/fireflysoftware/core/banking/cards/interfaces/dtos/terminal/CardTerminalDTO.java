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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.terminal;

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
 * Data Transfer Object for Card Terminal.
 * Contains the essential information about a Card Terminal that needs to be exposed through the API.
 * A Card Terminal represents physical or virtual terminals where card transactions are processed,
 * including terminal details, capabilities, and relationships with merchants and processors.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardTerminalDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID terminalId;

    @NotBlank(message = "Terminal reference is required")
    @Size(max = 100, message = "Terminal reference cannot exceed 100 characters")
    private String terminalReference;

    @NotBlank(message = "Terminal serial number is required")
    @Size(max = 50, message = "Terminal serial number cannot exceed 50 characters")
    private String terminalSerialNumber;

    @NotBlank(message = "Terminal name is required")
    @Size(min = 2, max = 100, message = "Terminal name must be between 2 and 100 characters")
    private String terminalName;

    @Size(max = 500, message = "Terminal description cannot exceed 500 characters")
    private String terminalDescription;

    @NotBlank(message = "Terminal type is required")
    @Size(max = 50, message = "Terminal type cannot exceed 50 characters")
    private String terminalType;

    @Size(max = 100, message = "Terminal model cannot exceed 100 characters")
    private String terminalModel;

    @Size(max = 100, message = "Terminal manufacturer cannot exceed 100 characters")
    private String terminalManufacturer;

    @NotBlank(message = "Terminal status is required")
    @Size(max = 50, message = "Terminal status cannot exceed 50 characters")
    private String terminalStatus;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @PastOrPresent(message = "Activation date cannot be in the future")
    private LocalDateTime activationDate;

    @PastOrPresent(message = "Deactivation date cannot be in the future")
    private LocalDateTime deactivationDate;

    @Size(max = 255, message = "Deactivation reason cannot exceed 255 characters")
    private String deactivationReason;

    @FilterableId
    private UUID merchantId;

    @Size(max = 100, message = "Merchant name cannot exceed 100 characters")
    private String merchantName;

    @Size(max = 50, message = "Merchant location ID cannot exceed 50 characters")
    private String merchantLocationId;

    @Size(max = 100, message = "Merchant location name cannot exceed 100 characters")
    private String merchantLocationName;

    @Size(max = 100, message = "Address line 1 cannot exceed 100 characters")
    private String addressLine1;

    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    private String addressLine2;

    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be a 2-letter ISO code")
    private String country;

    @Size(max = 100, message = "Geolocation cannot exceed 100 characters")
    private String geolocation;

    @Size(max = 50, message = "Acquirer ID cannot exceed 50 characters")
    private String acquirerId;

    @Size(max = 100, message = "Acquirer name cannot exceed 100 characters")
    private String acquirerName;

    @Size(max = 50, message = "Processor ID cannot exceed 50 characters")
    private String processorId;

    @Size(max = 100, message = "Processor name cannot exceed 100 characters")
    private String processorName;

    @Size(max = 50, message = "Software version cannot exceed 50 characters")
    private String softwareVersion;

    @Size(max = 50, message = "Firmware version cannot exceed 50 characters")
    private String firmwareVersion;

    @Size(max = 50, message = "Hardware version cannot exceed 50 characters")
    private String hardwareVersion;

    @PastOrPresent(message = "Last maintenance date cannot be in the future")
    private LocalDateTime lastMaintenanceDate;

    @Future(message = "Next maintenance date must be in the future")
    private LocalDateTime nextMaintenanceDate;

    @PastOrPresent(message = "Last update date cannot be in the future")
    private LocalDateTime lastUpdateDate;

    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDateTime installationDate;

    @NotNull(message = "Physical flag is required")
    private Boolean isPhysical;

    @NotNull(message = "Virtual flag is required")
    private Boolean isVirtual;

    @NotNull(message = "Mobile flag is required")
    private Boolean isMobile;

    @NotNull(message = "Attended flag is required")
    private Boolean isAttended;

    @NotNull(message = "Unattended flag is required")
    private Boolean isUnattended;

    @NotNull(message = "Contactless flag is required")
    private Boolean isContactless;

    @NotNull(message = "Chip flag is required")
    private Boolean isChip;

    @NotNull(message = "Magstripe flag is required")
    private Boolean isMagstripe;

    @NotNull(message = "PIN supported flag is required")
    private Boolean isPinSupported;

    @NotNull(message = "Signature supported flag is required")
    private Boolean isSignatureSupported;

    @NotNull(message = "Biometric supported flag is required")
    private Boolean isBiometricSupported;

    @NotNull(message = "NFC supported flag is required")
    private Boolean isNfcSupported;

    @NotNull(message = "QR supported flag is required")
    private Boolean isQrSupported;

    @NotNull(message = "EMV supported flag is required")
    private Boolean isEmvSupported;

    @NotNull(message = "P2PE supported flag is required")
    private Boolean isP2peSupported;

    @NotNull(message = "Tokenization supported flag is required")
    private Boolean isTokenizationSupported;

    @Size(max = 500, message = "Supported card networks cannot exceed 500 characters")
    private String supportedCardNetworks;

    @Size(max = 500, message = "Supported payment methods cannot exceed 500 characters")
    private String supportedPaymentMethods;

    @Size(max = 500, message = "Supported currencies cannot exceed 500 characters")
    private String supportedCurrencies;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Default currency must be a 3-letter ISO code")
    private String defaultCurrency;

    @Size(max = 50, message = "Communication type cannot exceed 50 characters")
    private String communicationType;

    @Size(max = 50, message = "Connection type cannot exceed 50 characters")
    private String connectionType;

    @Pattern(regexp = "^(?:(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|::1|::)$", message = "IP address must be a valid IPv4 or IPv6 address")
    private String ipAddress;

    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "MAC address must be a valid format")
    private String macAddress;

    @NotNull(message = "PCI compliant flag is required")
    private Boolean isPciCompliant;

    @PastOrPresent(message = "PCI compliance date cannot be in the future")
    private LocalDateTime pciComplianceDate;

    @Future(message = "PCI compliance expiry must be in the future")
    private LocalDateTime pciComplianceExpiry;

    @Size(max = 100, message = "Encryption method cannot exceed 100 characters")
    private String encryptionMethod;

    @Size(max = 100, message = "Key management scheme cannot exceed 100 characters")
    private String keyManagementScheme;

    @PastOrPresent(message = "Last key rotation date cannot be in the future")
    private LocalDateTime lastKeyRotationDate;

    @Future(message = "Next key rotation date must be in the future")
    private LocalDateTime nextKeyRotationDate;

    @NotNull(message = "Fault detected flag is required")
    private Boolean isFaultDetected;

    @Size(max = 50, message = "Fault code cannot exceed 50 characters")
    private String faultCode;

    @Size(max = 255, message = "Fault description cannot exceed 255 characters")
    private String faultDescription;

    @PastOrPresent(message = "Fault detection date cannot be in the future")
    private LocalDateTime faultDetectionDate;

    @PastOrPresent(message = "Last transaction timestamp cannot be in the future")
    private LocalDateTime lastTransactionTimestamp;

    @Size(max = 100, message = "Last transaction ID cannot exceed 100 characters")
    private String lastTransactionId;

    @Size(max = 50, message = "Last transaction status cannot exceed 50 characters")
    private String lastTransactionStatus;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}