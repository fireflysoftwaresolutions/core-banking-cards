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


package io.fireflysoftware.core.banking.cards.interfaces.dtos.limit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.BaseDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.limit.LimitTypeEnum;
import io.fireflysoftware.core.banking.cards.interfaces.enums.limit.ResetPeriodEnum;
import org.fireflyframework.utils.annotations.FilterableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardLimitDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID cardLimitId;

    @FilterableId
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @NotNull(message = "Limit type is required")
    private LimitTypeEnum limitType;

    @NotNull(message = "Limit amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Limit amount must be greater than 0")
    @DecimalMax(value = "99999999.99", message = "Limit amount cannot exceed 99,999,999.99")
    private BigDecimal limitAmount;

    @NotNull(message = "Current usage is required")
    @DecimalMin(value = "0.0", message = "Current usage cannot be negative")
    private BigDecimal currentUsage;

    @NotNull(message = "Reset period is required")
    private ResetPeriodEnum resetPeriod;
}