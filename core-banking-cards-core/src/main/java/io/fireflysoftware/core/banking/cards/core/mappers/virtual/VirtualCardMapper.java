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


package io.fireflysoftware.core.banking.cards.core.mappers.virtual;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.virtual.VirtualCardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.virtual.VirtualCard;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the VirtualCard entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VirtualCardMapper {

    /**
     * Convert a VirtualCard entity to a VirtualCardDTO.
     *
     * @param LVirtualLCard the VirtualCard entity to convert
     * @return the resulting VirtualCardDTO
     */
    VirtualCardDTO toDTO(VirtualCard LVirtualLCard);

    /**
     * Convert a VirtualCardDTO to a VirtualCard entity.
     *
     * @param LVirtualLCardLDLTLO the VirtualCardDTO to convert
     * @return the resulting VirtualCard entity
     */
    VirtualCard toEntity(VirtualCardDTO LVirtualLCardLDLTLO);
}
