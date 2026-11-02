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


package io.fireflysoftware.core.banking.cards.core.mappers.dispute;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.dispute.CardDisputeDTO;
import io.fireflysoftware.core.banking.cards.models.entities.dispute.CardDispute;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardDispute entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardDisputeMapper {

    /**
     * Convert a CardDispute entity to a CardDisputeDTO.
     *
     * @param LCardLDispute the CardDispute entity to convert
     * @return the resulting CardDisputeDTO
     */
    CardDisputeDTO toDTO(CardDispute LCardLDispute);

    /**
     * Convert a CardDisputeDTO to a CardDispute entity.
     *
     * @param LCardLDisputeLDLTLO the CardDisputeDTO to convert
     * @return the resulting CardDispute entity
     */
    CardDispute toEntity(CardDisputeDTO LCardLDisputeLDLTLO);
}
