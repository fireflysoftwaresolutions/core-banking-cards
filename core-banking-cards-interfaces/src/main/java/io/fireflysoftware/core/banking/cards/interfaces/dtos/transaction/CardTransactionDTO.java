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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.transaction.TransactionStatusEnum;
import io.fireflysoftware.core.banking.cards.interfaces.enums.transaction.TransactionTypeEnum;
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

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardTransactionDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID cardTransactionId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @NotNull(message = "Transaction type is required")
    private TransactionTypeEnum transactionType;

    @Size(max = 500, message = "Merchant info cannot exceed 500 characters")
    private String merchantInfo;

    @NotNull(message = "Transaction status is required")
    private TransactionStatusEnum transactionStatus;

    @Size(max = 20, message = "Card auth code cannot exceed 20 characters")
    private String cardAuthCode;

    @Pattern(regexp = "^[0-9]{4}$", message = "Merchant category code must be a 4-digit number")
    private String cardMerchantCategoryCode;

    @Size(max = 100, message = "Card merchant name cannot exceed 100 characters")
    private String cardMerchantName;

    @Size(max = 50, message = "Card POS entry mode cannot exceed 50 characters")
    private String cardPosEntryMode;

    @NotBlank(message = "Card transaction reference is required")
    @Size(max = 100, message = "Card transaction reference cannot exceed 100 characters")
    private String cardTransactionReference;

    @FilterableId
    @Size(max = 50, message = "Card terminal ID cannot exceed 50 characters")
    private String cardTerminalId;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Card holder country must be a 2-letter ISO code")
    private String cardHolderCountry;

    @NotNull(message = "Card present flag is required")
    private Boolean cardPresentFlag;

    @NotNull(message = "Card transaction timestamp is required")
    @PastOrPresent(message = "Card transaction timestamp cannot be in the future")
    private LocalDateTime cardTransactionTimestamp;

    @NotNull(message = "Card fraud flag is required")
    private Boolean cardFraudFlag;

    @DecimalMin(value = "0.0", message = "Currency conversion rate cannot be negative")
    @DecimalMax(value = "999.999999", message = "Currency conversion rate cannot exceed 999.999999")
    private BigDecimal cardCurrencyConversionRate;

    @DecimalMin(value = "0.0", message = "Card fee amount cannot be negative")
    @DecimalMax(value = "99999.99", message = "Card fee amount cannot exceed 99,999.99")
    private BigDecimal cardFeeAmount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Card fee currency must be a 3-letter ISO code")
    private String cardFeeCurrency;

    @Size(max = 100, message = "Card installment plan cannot exceed 100 characters")
    private String cardInstallmentPlan;
}
