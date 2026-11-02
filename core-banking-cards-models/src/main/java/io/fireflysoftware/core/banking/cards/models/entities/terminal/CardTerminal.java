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


package io.fireflysoftware.core.banking.cards.models.entities.terminal;

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
 * Card Terminal entity.
 * Represents physical or virtual terminals where card transactions are processed, including terminal details,
 * capabilities, and relationships with merchants and processors.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_terminal")
public class CardTerminal extends BaseEntity {

    @Id
    @Column("terminal_id")
    private UUID terminalId;

    @Column("terminal_reference")
    private String terminalReference;

    @Column("terminal_serial_number")
    private String terminalSerialNumber;

    @Column("terminal_name")
    private String terminalName;

    @Column("terminal_description")
    private String terminalDescription;

    @Column("terminal_type")
    private String terminalType;

    @Column("terminal_model")
    private String terminalModel;

    @Column("terminal_manufacturer")
    private String terminalManufacturer;

    @Column("terminal_status")
    private String terminalStatus;

    @Column("is_active")
    private Boolean isActive;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("deactivation_date")
    private LocalDateTime deactivationDate;

    @Column("deactivation_reason")
    private String deactivationReason;

    @Column("merchant_id")
    private UUID merchantId;

    @Column("merchant_name")
    private String merchantName;

    @Column("merchant_location_id")
    private String merchantLocationId;

    @Column("merchant_location_name")
    private String merchantLocationName;

    @Column("address_line1")
    private String addressLine1;

    @Column("address_line2")
    private String addressLine2;

    @Column("city")
    private String city;

    @Column("state")
    private String state;

    @Column("postal_code")
    private String postalCode;

    @Column("country")
    private String country;

    @Column("geolocation")
    private String geolocation;

    @Column("acquirer_id")
    private String acquirerId;

    @Column("acquirer_name")
    private String acquirerName;

    @Column("processor_id")
    private String processorId;

    @Column("processor_name")
    private String processorName;

    @Column("software_version")
    private String softwareVersion;

    @Column("firmware_version")
    private String firmwareVersion;

    @Column("hardware_version")
    private String hardwareVersion;

    @Column("last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;

    @Column("next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    @Column("last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column("installation_date")
    private LocalDateTime installationDate;

    @Column("is_physical")
    private Boolean isPhysical;

    @Column("is_virtual")
    private Boolean isVirtual;

    @Column("is_mobile")
    private Boolean isMobile;

    @Column("is_attended")
    private Boolean isAttended;

    @Column("is_unattended")
    private Boolean isUnattended;

    @Column("is_contactless")
    private Boolean isContactless;

    @Column("is_chip")
    private Boolean isChip;

    @Column("is_magstripe")
    private Boolean isMagstripe;

    @Column("is_pin_supported")
    private Boolean isPinSupported;

    @Column("is_signature_supported")
    private Boolean isSignatureSupported;

    @Column("is_biometric_supported")
    private Boolean isBiometricSupported;

    @Column("is_nfc_supported")
    private Boolean isNfcSupported;

    @Column("is_qr_supported")
    private Boolean isQrSupported;

    @Column("is_emv_supported")
    private Boolean isEmvSupported;

    @Column("is_p2pe_supported")
    private Boolean isP2peSupported;

    @Column("is_tokenization_supported")
    private Boolean isTokenizationSupported;

    @Column("supported_card_networks")
    private String supportedCardNetworks;

    @Column("supported_payment_methods")
    private String supportedPaymentMethods;

    @Column("supported_currencies")
    private String supportedCurrencies;

    @Column("default_currency")
    private String defaultCurrency;

    @Column("communication_type")
    private String communicationType;

    @Column("connection_type")
    private String connectionType;

    @Column("ip_address")
    private String ipAddress;

    @Column("mac_address")
    private String macAddress;

    @Column("is_pci_compliant")
    private Boolean isPciCompliant;

    @Column("pci_compliance_date")
    private LocalDateTime pciComplianceDate;

    @Column("pci_compliance_expiry")
    private LocalDateTime pciComplianceExpiry;

    @Column("encryption_method")
    private String encryptionMethod;

    @Column("key_management_scheme")
    private String keyManagementScheme;

    @Column("last_key_rotation_date")
    private LocalDateTime lastKeyRotationDate;

    @Column("next_key_rotation_date")
    private LocalDateTime nextKeyRotationDate;

    @Column("is_fault_detected")
    private Boolean isFaultDetected;

    @Column("fault_code")
    private String faultCode;

    @Column("fault_description")
    private String faultDescription;

    @Column("fault_detection_date")
    private LocalDateTime faultDetectionDate;

    @Column("last_transaction_timestamp")
    private LocalDateTime lastTransactionTimestamp;

    @Column("last_transaction_id")
    private String lastTransactionId;

    @Column("last_transaction_status")
    private String lastTransactionStatus;

    @Column("notes")
    private String notes;
}