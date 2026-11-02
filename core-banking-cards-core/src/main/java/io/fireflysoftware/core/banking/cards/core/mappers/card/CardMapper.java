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


package io.fireflysoftware.core.banking.cards.core.mappers.card;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.card.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the Card entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    /**
     * Convert a Card entity to a CardDTO.
     *
     * @param card the Card entity to convert
     * @return the resulting CardDTO
     */
    @Mapping(target = "pin", ignore = true)
    @Mapping(target = "cvv", ignore = true)
    CardDTO toDTO(Card card);

    /**
     * Convert a CardDTO to a Card entity.
     *
     * @param cardDTO the CardDTO to convert
     * @return the resulting Card entity
     */
    @Mapping(target = "pin", ignore = true)
    @Mapping(target = "cvv", ignore = true)
    Card toEntity(CardDTO cardDTO);
}
