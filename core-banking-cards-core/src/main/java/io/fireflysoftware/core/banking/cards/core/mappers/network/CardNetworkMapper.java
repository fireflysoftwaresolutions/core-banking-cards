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


package io.fireflysoftware.core.banking.cards.core.mappers.network;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.network.CardNetworkDTO;
import io.fireflysoftware.core.banking.cards.models.entities.network.CardNetwork;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardNetwork entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardNetworkMapper {

    /**
     * Convert a CardNetwork entity to a CardNetworkDTO.
     *
     * @param LCardLNetwork the CardNetwork entity to convert
     * @return the resulting CardNetworkDTO
     */
    CardNetworkDTO toDTO(CardNetwork LCardLNetwork);

    /**
     * Convert a CardNetworkDTO to a CardNetwork entity.
     *
     * @param LCardLNetworkLDLTLO the CardNetworkDTO to convert
     * @return the resulting CardNetwork entity
     */
    CardNetwork toEntity(CardNetworkDTO LCardLNetworkLDLTLO);
}
