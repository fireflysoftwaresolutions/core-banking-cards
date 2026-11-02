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


package io.fireflysoftware.core.banking.cards.models.entities.acquirer;

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
 * Card Acquirer entity.
 * Represents financial institutions that process card payments on behalf of merchants,
 * including acquirer details, relationships with card networks, and processing capabilities.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_acquirer")
public class CardAcquirer extends BaseEntity {

    @Id
    @Column("acquirer_id")
    private UUID acquirerId;

    @Column("acquirer_reference")
    private String acquirerReference;

    @Column("acquirer_name")
    private String acquirerName;

    @Column("acquirer_legal_name")
    private String acquirerLegalName;

    @Column("acquirer_code")
    private String acquirerCode;

    @Column("acquirer_description")
    private String acquirerDescription;

    @Column("acquirer_type")
    private String acquirerType;

    @Column("acquirer_status")
    private String acquirerStatus;

    @Column("is_active")
    private Boolean isActive;

    @Column("activation_date")
    private LocalDateTime activationDate;

    @Column("deactivation_date")
    private LocalDateTime deactivationDate;

    @Column("deactivation_reason")
    private String deactivationReason;

    @Column("tax_id")
    private String taxId;

    @Column("registration_number")
    private String registrationNumber;

    @Column("license_number")
    private String licenseNumber;

    @Column("license_expiry_date")
    private LocalDateTime licenseExpiryDate;

    @Column("website_url")
    private String websiteUrl;

    @Column("logo_url")
    private String logoUrl;

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

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("contact_person_name")
    private String contactPersonName;

    @Column("contact_person_title")
    private String contactPersonTitle;

    @Column("contact_person_phone")
    private String contactPersonPhone;

    @Column("contact_person_email")
    private String contactPersonEmail;

    @Column("supported_card_networks")
    private String supportedCardNetworks;

    @Column("supported_payment_methods")
    private String supportedPaymentMethods;

    @Column("supported_currencies")
    private String supportedCurrencies;

    @Column("default_currency")
    private String defaultCurrency;

    @Column("supported_countries")
    private String supportedCountries;

    @Column("is_international")
    private Boolean isInternational;

    @Column("is_domestic")
    private Boolean isDomestic;

    @Column("is_online")
    private Boolean isOnline;

    @Column("is_physical")
    private Boolean isPhysical;

    @Column("is_mobile")
    private Boolean isMobile;

    @Column("processor_id")
    private UUID processorId;

    @Column("processor_name")
    private String processorName;

    @Column("gateway_id")
    private UUID gatewayId;

    @Column("gateway_name")
    private String gatewayName;

    @Column("api_base_url")
    private String apiBaseUrl;

    @Column("api_version")
    private String apiVersion;

    @Column("api_key")
    private String apiKey;

    @Column("api_secret")
    private String apiSecret;

    @Column("api_username")
    private String apiUsername;

    @Column("api_password")
    private String apiPassword;

    @Column("api_certificate")
    private String apiCertificate;

    @Column("webhook_url")
    private String webhookUrl;

    @Column("webhook_secret")
    private String webhookSecret;

    @Column("merchant_count")
    private Integer merchantCount;

    @Column("terminal_count")
    private Integer terminalCount;

    @Column("transaction_volume_daily_avg")
    private Long transactionVolumeDailyAvg;

    @Column("transaction_value_daily_avg")
    private Long transactionValueDailyAvg;

    @Column("settlement_frequency")
    private String settlementFrequency;

    @Column("settlement_delay_days")
    private Integer settlementDelayDays;

    @Column("fee_structure")
    private String feeStructure;

    @Column("interchange_fee_percentage")
    private Double interchangeFeePercentage;

    @Column("processing_fee_percentage")
    private Double processingFeePercentage;

    @Column("processing_fee_flat")
    private Double processingFeeFlat;

    @Column("chargeback_fee")
    private Double chargebackFee;

    @Column("refund_fee")
    private Double refundFee;

    @Column("contract_start_date")
    private LocalDateTime contractStartDate;

    @Column("contract_end_date")
    private LocalDateTime contractEndDate;

    @Column("contract_renewal_date")
    private LocalDateTime contractRenewalDate;

    @Column("contract_status")
    private String contractStatus;

    @Column("is_pci_compliant")
    private Boolean isPciCompliant;

    @Column("pci_compliance_date")
    private LocalDateTime pciComplianceDate;

    @Column("pci_compliance_expiry")
    private LocalDateTime pciComplianceExpiry;

    @Column("is_emv_compliant")
    private Boolean isEmvCompliant;

    @Column("emv_compliance_date")
    private LocalDateTime emvComplianceDate;

    @Column("last_audit_date")
    private LocalDateTime lastAuditDate;

    @Column("next_audit_date")
    private LocalDateTime nextAuditDate;

    @Column("notes")
    private String notes;
}