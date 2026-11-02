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


package io.fireflysoftware.core.banking.cards.models.entities.application;

import io.fireflysoftware.core.banking.cards.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Card Application entity.
 * Represents an application for a new card, including application details, status, and approval information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("card_application")
public class CardApplication extends BaseEntity {

    @Id
    @Column("application_id")
    private UUID applicationId;

    @Column("party_id")
    private UUID partyId;

    @Column("account_id")
    private UUID accountId;

    @Column("application_reference")
    private String applicationReference;

    @Column("application_type")
    private String applicationType;

    @Column("card_type_id")
    private UUID cardTypeId;

    @Column("program_id")
    private UUID programId;

    @Column("design_id")
    private UUID designId;

    @Column("application_status")
    private String applicationStatus;

    @Column("application_stage")
    private String applicationStage;

    @Column("application_channel")
    private String applicationChannel;

    @Column("application_timestamp")
    private LocalDateTime applicationTimestamp;

    @Column("applicant_first_name")
    private String applicantFirstName;

    @Column("applicant_middle_name")
    private String applicantMiddleName;

    @Column("applicant_last_name")
    private String applicantLastName;

    @Column("applicant_email")
    private String applicantEmail;

    @Column("applicant_phone")
    private String applicantPhone;

    @Column("applicant_address_line1")
    private String applicantAddressLine1;

    @Column("applicant_address_line2")
    private String applicantAddressLine2;

    @Column("applicant_city")
    private String applicantCity;

    @Column("applicant_state")
    private String applicantState;

    @Column("applicant_postal_code")
    private String applicantPostalCode;

    @Column("applicant_country")
    private String applicantCountry;

    @Column("applicant_id_type")
    private String applicantIdType;

    @Column("applicant_id_number")
    private String applicantIdNumber;

    @Column("applicant_id_expiry")
    private LocalDateTime applicantIdExpiry;

    @Column("applicant_date_of_birth")
    private LocalDateTime applicantDateOfBirth;

    @Column("applicant_nationality")
    private String applicantNationality;

    @Column("employment_status")
    private String employmentStatus;

    @Column("employer_name")
    private String employerName;

    @Column("employment_position")
    private String employmentPosition;

    @Column("annual_income")
    private BigDecimal annualIncome;

    @Column("income_currency")
    private String incomeCurrency;

    @Column("requested_credit_limit")
    private BigDecimal requestedCreditLimit;

    @Column("approved_credit_limit")
    private BigDecimal approvedCreditLimit;

    @Column("currency_code")
    private String currencyCode;

    @Column("is_pre_approved")
    private Boolean isPreApproved;

    @Column("is_instant_issuance")
    private Boolean isInstantIssuance;

    @Column("is_digital_only")
    private Boolean isDigitalOnly;

    @Column("requires_physical_card")
    private Boolean requiresPhysicalCard;

    @Column("shipping_address_same_as_residential")
    private Boolean shippingAddressSameAsResidential;

    @Column("shipping_address_line1")
    private String shippingAddressLine1;

    @Column("shipping_address_line2")
    private String shippingAddressLine2;

    @Column("shipping_city")
    private String shippingCity;

    @Column("shipping_state")
    private String shippingState;

    @Column("shipping_postal_code")
    private String shippingPostalCode;

    @Column("shipping_country")
    private String shippingCountry;

    @Column("shipping_method")
    private String shippingMethod;

    @Column("credit_score")
    private Integer creditScore;

    @Column("credit_score_provider")
    private String creditScoreProvider;

    @Column("credit_check_timestamp")
    private LocalDateTime creditCheckTimestamp;

    @Column("credit_check_reference")
    private String creditCheckReference;

    @Column("kyc_status")
    private String kycStatus;

    @Column("kyc_reference")
    private String kycReference;

    @Column("kyc_timestamp")
    private LocalDateTime kycTimestamp;

    @Column("aml_status")
    private String amlStatus;

    @Column("aml_reference")
    private String amlReference;

    @Column("aml_timestamp")
    private LocalDateTime amlTimestamp;

    @Column("approval_timestamp")
    private LocalDateTime approvalTimestamp;

    @Column("approved_by")
    private String approvedBy;

    @Column("rejection_reason")
    private String rejectionReason;

    @Column("rejection_timestamp")
    private LocalDateTime rejectionTimestamp;

    @Column("rejection_code")
    private String rejectionCode;

    @Column("card_id")
    private UUID cardId;

    @Column("card_issuance_timestamp")
    private LocalDateTime cardIssuanceTimestamp;

    @Column("terms_accepted")
    private Boolean termsAccepted;

    @Column("terms_accepted_timestamp")
    private LocalDateTime termsAcceptedTimestamp;

    @Column("terms_version")
    private String termsVersion;

    @Column("supporting_documents")
    private String supportingDocuments;

    @Column("notes")
    private String notes;
}