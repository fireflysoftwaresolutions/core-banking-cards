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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Card Application.
 * Contains the essential information about a Card Application that needs to be exposed through the API.
 * A Card Application represents an application for a new card, including application details, status, and approval information.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardApplicationDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID applicationId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotBlank(message = "Application reference is required")
    @Size(min = 5, max = 50, message = "Application reference must be between 5 and 50 characters")
    private String applicationReference;

    @NotBlank(message = "Application type is required")
    @Pattern(regexp = "^(NEW|REPLACEMENT|UPGRADE|ADDITIONAL)$", message = "Application type must be NEW, REPLACEMENT, UPGRADE, or ADDITIONAL")
    private String applicationType;

    @FilterableId
    @NotNull(message = "Card type ID is required")
    private UUID cardTypeId;

    @FilterableId
    @NotNull(message = "Program ID is required")
    private UUID programId;

    @FilterableId
    private UUID designId;

    @NotBlank(message = "Application status is required")
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|CANCELLED|PROCESSING)$", message = "Application status must be PENDING, APPROVED, REJECTED, CANCELLED, or PROCESSING")
    private String applicationStatus;

    @NotBlank(message = "Application stage is required")
    @Pattern(regexp = "^(SUBMITTED|VERIFICATION|APPROVAL|ISSUANCE|COMPLETED)$", message = "Application stage must be SUBMITTED, VERIFICATION, APPROVAL, ISSUANCE, or COMPLETED")
    private String applicationStage;

    @NotBlank(message = "Application channel is required")
    @Pattern(regexp = "^(ONLINE|MOBILE|BRANCH|PHONE|MAIL)$", message = "Application channel must be ONLINE, MOBILE, BRANCH, PHONE, or MAIL")
    private String applicationChannel;

    @NotNull(message = "Application timestamp is required")
    @PastOrPresent(message = "Application timestamp cannot be in the future")
    private LocalDateTime applicationTimestamp;

    @NotBlank(message = "Applicant first name is required")
    @Size(min = 1, max = 50, message = "Applicant first name must be between 1 and 50 characters")
    private String applicantFirstName;

    @Size(max = 50, message = "Applicant middle name cannot exceed 50 characters")
    private String applicantMiddleName;

    @NotBlank(message = "Applicant last name is required")
    @Size(min = 1, max = 50, message = "Applicant last name must be between 1 and 50 characters")
    private String applicantLastName;

    @NotBlank(message = "Applicant email is required")
    @Email(message = "Applicant email must be a valid email address")
    @Size(max = 100, message = "Applicant email cannot exceed 100 characters")
    private String applicantEmail;

    @NotBlank(message = "Applicant phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Applicant phone must be a valid international phone number")
    private String applicantPhone;

    @Size(max = 100, message = "Applicant address line 1 cannot exceed 100 characters")
    private String applicantAddressLine1;

    @Size(max = 100, message = "Applicant address line 2 cannot exceed 100 characters")
    private String applicantAddressLine2;

    @Size(max = 50, message = "Applicant city cannot exceed 50 characters")
    private String applicantCity;

    @Size(max = 50, message = "Applicant state cannot exceed 50 characters")
    private String applicantState;

    @Size(max = 20, message = "Applicant postal code cannot exceed 20 characters")
    private String applicantPostalCode;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Applicant country must be a 2-letter ISO code")
    private String applicantCountry;

    @Size(max = 50, message = "Applicant ID type cannot exceed 50 characters")
    private String applicantIdType;

    @Size(max = 50, message = "Applicant ID number cannot exceed 50 characters")
    private String applicantIdNumber;

    @Future(message = "Applicant ID expiry must be in the future")
    private LocalDateTime applicantIdExpiry;

    @Past(message = "Applicant date of birth must be in the past")
    private LocalDateTime applicantDateOfBirth;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Applicant nationality must be a 2-letter ISO code")
    private String applicantNationality;

    @Size(max = 50, message = "Employment status cannot exceed 50 characters")
    private String employmentStatus;

    @Size(max = 100, message = "Employer name cannot exceed 100 characters")
    private String employerName;

    @Size(max = 100, message = "Employment position cannot exceed 100 characters")
    private String employmentPosition;

    @DecimalMin(value = "0.0", message = "Annual income cannot be negative")
    @DecimalMax(value = "99999999.99", message = "Annual income cannot exceed 99,999,999.99")
    private BigDecimal annualIncome;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Income currency must be a 3-letter ISO code")
    private String incomeCurrency;

    @DecimalMin(value = "0.0", message = "Requested credit limit cannot be negative")
    @DecimalMax(value = "9999999.99", message = "Requested credit limit cannot exceed 9,999,999.99")
    private BigDecimal requestedCreditLimit;

    @DecimalMin(value = "0.0", message = "Approved credit limit cannot be negative")
    @DecimalMax(value = "9999999.99", message = "Approved credit limit cannot exceed 9,999,999.99")
    private BigDecimal approvedCreditLimit;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @NotNull(message = "Pre-approved flag is required")
    private Boolean isPreApproved;

    @NotNull(message = "Instant issuance flag is required")
    private Boolean isInstantIssuance;

    @NotNull(message = "Digital only flag is required")
    private Boolean isDigitalOnly;

    @NotNull(message = "Requires physical card flag is required")
    private Boolean requiresPhysicalCard;

    @NotNull(message = "Shipping address same as residential flag is required")
    private Boolean shippingAddressSameAsResidential;

    @Size(max = 100, message = "Shipping address line 1 cannot exceed 100 characters")
    private String shippingAddressLine1;

    @Size(max = 100, message = "Shipping address line 2 cannot exceed 100 characters")
    private String shippingAddressLine2;

    @Size(max = 50, message = "Shipping city cannot exceed 50 characters")
    private String shippingCity;

    @Size(max = 50, message = "Shipping state cannot exceed 50 characters")
    private String shippingState;

    @Size(max = 20, message = "Shipping postal code cannot exceed 20 characters")
    private String shippingPostalCode;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Shipping country must be a 2-letter ISO code")
    private String shippingCountry;

    @Size(max = 50, message = "Shipping method cannot exceed 50 characters")
    private String shippingMethod;

    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    private Integer creditScore;

    @Size(max = 50, message = "Credit score provider cannot exceed 50 characters")
    private String creditScoreProvider;

    @PastOrPresent(message = "Credit check timestamp cannot be in the future")
    private LocalDateTime creditCheckTimestamp;

    @Size(max = 100, message = "Credit check reference cannot exceed 100 characters")
    private String creditCheckReference;

    @Size(max = 50, message = "KYC status cannot exceed 50 characters")
    private String kycStatus;

    @Size(max = 100, message = "KYC reference cannot exceed 100 characters")
    private String kycReference;

    @PastOrPresent(message = "KYC timestamp cannot be in the future")
    private LocalDateTime kycTimestamp;

    @Size(max = 50, message = "AML status cannot exceed 50 characters")
    private String amlStatus;

    @Size(max = 100, message = "AML reference cannot exceed 100 characters")
    private String amlReference;

    @PastOrPresent(message = "AML timestamp cannot be in the future")
    private LocalDateTime amlTimestamp;

    @PastOrPresent(message = "Approval timestamp cannot be in the future")
    private LocalDateTime approvalTimestamp;

    @Size(max = 100, message = "Approved by cannot exceed 100 characters")
    private String approvedBy;

    @Size(max = 255, message = "Rejection reason cannot exceed 255 characters")
    private String rejectionReason;

    @PastOrPresent(message = "Rejection timestamp cannot be in the future")
    private LocalDateTime rejectionTimestamp;

    @Size(max = 50, message = "Rejection code cannot exceed 50 characters")
    private String rejectionCode;

    @FilterableId
    private UUID cardId;

    @PastOrPresent(message = "Card issuance timestamp cannot be in the future")
    private LocalDateTime cardIssuanceTimestamp;

    @NotNull(message = "Terms accepted flag is required")
    private Boolean termsAccepted;

    @PastOrPresent(message = "Terms accepted timestamp cannot be in the future")
    private LocalDateTime termsAcceptedTimestamp;

    @Size(max = 20, message = "Terms version cannot exceed 20 characters")
    private String termsVersion;

    @Size(max = 1000, message = "Supporting documents cannot exceed 1000 characters")
    private String supportingDocuments;
}