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


package io.fireflysoftware.core.banking.cards.core.mappers.payment;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.payment.CardPaymentDTO;
import io.fireflysoftware.core.banking.cards.models.entities.payment.CardPayment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardPayment entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardPaymentMapper {

    /**
     * Convert a CardPayment entity to a CardPaymentDTO.
     *
     * @param LCardLPayment the CardPayment entity to convert
     * @return the resulting CardPaymentDTO
     */
    CardPaymentDTO toDTO(CardPayment LCardLPayment);

    /**
     * Convert a CardPaymentDTO to a CardPayment entity.
     *
     * @param LCardLPaymentLDLTLO the CardPaymentDTO to convert
     * @return the resulting CardPayment entity
     */
    CardPayment toEntity(CardPaymentDTO LCardLPaymentLDLTLO);
}
