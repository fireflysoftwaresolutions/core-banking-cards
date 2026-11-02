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


package io.fireflysoftware.core.banking.cards.core.mappers.security;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.security.CardSecurityDTO;
import io.fireflysoftware.core.banking.cards.models.entities.security.CardSecurity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardSecurity entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardSecurityMapper {

    /**
     * Convert a CardSecurity entity to a CardSecurityDTO.
     *
     * @param LCardLSecurity the CardSecurity entity to convert
     * @return the resulting CardSecurityDTO
     */
    CardSecurityDTO toDTO(CardSecurity LCardLSecurity);

    /**
     * Convert a CardSecurityDTO to a CardSecurity entity.
     *
     * @param LCardLSecurityLDLTLO the CardSecurityDTO to convert
     * @return the resulting CardSecurity entity
     */
    CardSecurity toEntity(CardSecurityDTO LCardLSecurityLDLTLO);
}
