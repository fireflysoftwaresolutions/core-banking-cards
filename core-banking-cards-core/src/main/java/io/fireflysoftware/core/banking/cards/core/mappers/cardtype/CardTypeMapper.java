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


package io.fireflysoftware.core.banking.cards.core.mappers.cardtype;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.cardtype.CardTypeDTO;
import io.fireflysoftware.core.banking.cards.models.entities.cardtype.CardType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardType entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardTypeMapper {

    /**
     * Convert a CardType entity to a CardTypeDTO.
     *
     * @param cardType the CardType entity to convert
     * @return the resulting CardTypeDTO
     */
    CardTypeDTO toDTO(CardType cardType);

    /**
     * Convert a CardTypeDTO to a CardType entity.
     *
     * @param cardTypeDTO the CardTypeDTO to convert
     * @return the resulting CardType entity
     */
    CardType toEntity(CardTypeDTO cardTypeDTO);
}
