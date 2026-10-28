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


package io.fireflysoftware.core.banking.cards.core.mappers.acquirer;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.acquirer.CardAcquirerDTO;
import io.fireflysoftware.core.banking.cards.models.entities.acquirer.CardAcquirer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardAcquirer entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardAcquirerMapper {

    /**
     * Convert a CardAcquirer entity to a CardAcquirerDTO.
     *
     * @param cardAcquirer the CardAcquirer entity to convert
     * @return the resulting CardAcquirerDTO
     */
    CardAcquirerDTO toDTO(CardAcquirer cardAcquirer);

    /**
     * Convert a CardAcquirerDTO to a CardAcquirer entity.
     *
     * @param cardAcquirerDTO the CardAcquirerDTO to convert
     * @return the resulting CardAcquirer entity
     */
    CardAcquirer toEntity(CardAcquirerDTO cardAcquirerDTO);
}
