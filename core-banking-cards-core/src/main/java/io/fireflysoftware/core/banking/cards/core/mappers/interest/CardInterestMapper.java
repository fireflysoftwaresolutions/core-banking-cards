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


package io.fireflysoftware.core.banking.cards.core.mappers.interest;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.interest.CardInterestDTO;
import io.fireflysoftware.core.banking.cards.models.entities.interest.CardInterest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardInterest entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardInterestMapper {

    /**
     * Convert a CardInterest entity to a CardInterestDTO.
     *
     * @param LCardLInterest the CardInterest entity to convert
     * @return the resulting CardInterestDTO
     */
    CardInterestDTO toDTO(CardInterest LCardLInterest);

    /**
     * Convert a CardInterestDTO to a CardInterest entity.
     *
     * @param LCardLInterestLDLTLO the CardInterestDTO to convert
     * @return the resulting CardInterest entity
     */
    CardInterest toEntity(CardInterestDTO LCardLInterestLDLTLO);
}
