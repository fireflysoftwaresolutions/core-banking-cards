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


package io.fireflysoftware.core.banking.cards.core.mappers.limit;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.limit.CardLimitDTO;
import io.fireflysoftware.core.banking.cards.models.entities.limit.CardLimit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardLimit entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardLimitMapper {

    /**
     * Convert a CardLimit entity to a CardLimitDTO.
     *
     * @param LCardLLimit the CardLimit entity to convert
     * @return the resulting CardLimitDTO
     */
    CardLimitDTO toDTO(CardLimit LCardLLimit);

    /**
     * Convert a CardLimitDTO to a CardLimit entity.
     *
     * @param LCardLLimitLDLTLO the CardLimitDTO to convert
     * @return the resulting CardLimit entity
     */
    CardLimit toEntity(CardLimitDTO LCardLLimitLDLTLO);
}
