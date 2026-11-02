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


package io.fireflysoftware.core.banking.cards.core.mappers.gateway;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.gateway.CardGatewayDTO;
import io.fireflysoftware.core.banking.cards.models.entities.gateway.CardGateway;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardGateway entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardGatewayMapper {

    /**
     * Convert a CardGateway entity to a CardGatewayDTO.
     *
     * @param cardGateway the CardGateway entity to convert
     * @return the resulting CardGatewayDTO
     */
    CardGatewayDTO toDTO(CardGateway cardGateway);

    /**
     * Convert a CardGatewayDTO to a CardGateway entity.
     *
     * @param cardGatewayDTO the CardGatewayDTO to convert
     * @return the resulting CardGateway entity
     */
    CardGateway toEntity(CardGatewayDTO cardGatewayDTO);
}
