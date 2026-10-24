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


package io.fireflysoftware.core.banking.cards.core.mappers.transaction;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.transaction.CardTransactionDTO;
import io.fireflysoftware.core.banking.cards.models.entities.transaction.CardTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardTransaction entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardTransactionMapper {

    /**
     * Convert a CardTransaction entity to a CardTransactionDTO.
     *
     * @param LCardLTransaction the CardTransaction entity to convert
     * @return the resulting CardTransactionDTO
     */
    CardTransactionDTO toDTO(CardTransaction LCardLTransaction);

    /**
     * Convert a CardTransactionDTO to a CardTransaction entity.
     *
     * @param LCardLTransactionLDLTLO the CardTransactionDTO to convert
     * @return the resulting CardTransaction entity
     */
    CardTransaction toEntity(CardTransactionDTO LCardLTransactionLDLTLO);
}
