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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.interest;

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
 * Data Transfer Object for Card Interest.
 * Contains the essential information about Card Interest that needs to be exposed through the API.
 * A Card Interest represents interest charged on card balances, including rates, calculation methods, and accrual information.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardInterestDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID interestId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @FilterableId
    @NotNull(message = "Party ID is required")
    private UUID partyId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @FilterableId
    private UUID statementId;

    @FilterableId
    private UUID programId;

    @NotBlank(message = "Interest reference is required")
    @Size(max = 100, message = "Interest reference cannot exceed 100 characters")
    private String interestReference;

    @NotBlank(message = "Interest type is required")
    @Size(max = 50, message = "Interest type cannot exceed 50 characters")
    private String interestType;

    @NotBlank(message = "Interest name is required")
    @Size(min = 2, max = 100, message = "Interest name must be between 2 and 100 characters")
    private String interestName;

    @Size(max = 500, message = "Interest description cannot exceed 500 characters")
    private String interestDescription;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    @NotNull(message = "Annual percentage rate is required")
    @DecimalMin(value = "0.0", message = "Annual percentage rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Annual percentage rate cannot exceed 100%")
    private BigDecimal annualPercentageRate;

    @NotNull(message = "Variable rate flag is required")
    private Boolean isVariableRate;

    @DecimalMin(value = "0.0", message = "Base rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Base rate cannot exceed 100%")
    private BigDecimal baseRate;

    @DecimalMin(value = "-100.0", message = "Rate margin cannot be less than -100%")
    @DecimalMax(value = "100.0", message = "Rate margin cannot exceed 100%")
    private BigDecimal rateMargin;

    @DecimalMin(value = "0.0", message = "Rate cap cannot be negative")
    @DecimalMax(value = "100.0", message = "Rate cap cannot exceed 100%")
    private BigDecimal rateCap;

    @DecimalMin(value = "0.0", message = "Rate floor cannot be negative")
    @DecimalMax(value = "100.0", message = "Rate floor cannot exceed 100%")
    private BigDecimal rateFloor;

    @Size(max = 50, message = "Rate change frequency cannot exceed 50 characters")
    private String rateChangeFrequency;

    @Future(message = "Next rate change date must be in the future")
    private LocalDateTime nextRateChangeDate;

    @DecimalMin(value = "0.0", message = "Previous rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Previous rate cannot exceed 100%")
    private BigDecimal previousRate;

    @PastOrPresent(message = "Rate change timestamp cannot be in the future")
    private LocalDateTime rateChangeTimestamp;

    @NotBlank(message = "Calculation method is required")
    @Size(max = 50, message = "Calculation method cannot exceed 50 characters")
    private String calculationMethod;

    @Size(max = 50, message = "Compounding frequency cannot exceed 50 characters")
    private String compoundingFrequency;

    @Size(max = 50, message = "Accrual frequency cannot exceed 50 characters")
    private String accrualFrequency;

    @NotBlank(message = "Balance type is required")
    @Size(max = 50, message = "Balance type cannot exceed 50 characters")
    private String balanceType;

    @NotNull(message = "Balance amount is required")
    @DecimalMin(value = "0.0", message = "Balance amount cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Balance amount cannot exceed 999,999,999.99")
    private BigDecimal balanceAmount;

    @NotNull(message = "Interest amount is required")
    @DecimalMin(value = "0.0", message = "Interest amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Interest amount cannot exceed 999,999.99")
    private BigDecimal interestAmount;

    @DecimalMin(value = "0.0", message = "Accrued interest cannot be negative")
    @DecimalMax(value = "999999.99", message = "Accrued interest cannot exceed 999,999.99")
    private BigDecimal accruedInterest;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a 3-letter ISO code")
    private String currencyCode;

    @NotNull(message = "Accrual start date is required")
    @PastOrPresent(message = "Accrual start date cannot be in the future")
    private LocalDateTime accrualStartDate;

    @NotNull(message = "Accrual end date is required")
    @PastOrPresent(message = "Accrual end date cannot be in the future")
    private LocalDateTime accrualEndDate;

    @NotNull(message = "Days in period is required")
    @Min(value = 1, message = "Days in period must be at least 1")
    @Max(value = 366, message = "Days in period cannot exceed 366")
    private Integer daysInPeriod;

    @NotNull(message = "Days in year is required")
    @Min(value = 365, message = "Days in year must be at least 365")
    @Max(value = 366, message = "Days in year cannot exceed 366")
    private Integer daysInYear;

    @NotNull(message = "Promotional rate flag is required")
    private Boolean isPromotionalRate;

    @FilterableId
    private UUID promotionId;

    @PastOrPresent(message = "Promotion start date cannot be in the future")
    private LocalDateTime promotionStartDate;

    @Future(message = "Promotion end date must be in the future")
    private LocalDateTime promotionEndDate;

    @DecimalMin(value = "0.0", message = "Post promotion rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Post promotion rate cannot exceed 100%")
    private BigDecimal postPromotionRate;

    @NotNull(message = "Grace period flag is required")
    private Boolean isGracePeriod;

    @Min(value = 0, message = "Grace period days cannot be negative")
    @Max(value = 365, message = "Grace period days cannot exceed 365")
    private Integer gracePeriodDays;

    @Future(message = "Grace period end date must be in the future")
    private LocalDateTime gracePeriodEndDate;

    @NotNull(message = "Charged flag is required")
    private Boolean isCharged;

    @PastOrPresent(message = "Charge timestamp cannot be in the future")
    private LocalDateTime chargeTimestamp;

    @PastOrPresent(message = "Posting timestamp cannot be in the future")
    private LocalDateTime postingTimestamp;

    @PastOrPresent(message = "Value date cannot be in the future")
    private LocalDateTime valueDate;

    @NotNull(message = "Waived flag is required")
    private Boolean isWaived;

    @Size(max = 255, message = "Waiver reason cannot exceed 255 characters")
    private String waiverReason;

    @Size(max = 100, message = "Waiver reference cannot exceed 100 characters")
    private String waiverReference;

    @PastOrPresent(message = "Waiver timestamp cannot be in the future")
    private LocalDateTime waiverTimestamp;

    @Size(max = 100, message = "Waiver authorized by cannot exceed 100 characters")
    private String waiverAuthorizedBy;

    @NotNull(message = "Billed flag is required")
    private Boolean isBilled;

    @PastOrPresent(message = "Billing timestamp cannot be in the future")
    private LocalDateTime billingTimestamp;

    @NotNull(message = "Paid flag is required")
    private Boolean isPaid;

    @PastOrPresent(message = "Payment timestamp cannot be in the future")
    private LocalDateTime paymentTimestamp;

    @Size(max = 100, message = "Payment reference cannot exceed 100 characters")
    private String paymentReference;
}