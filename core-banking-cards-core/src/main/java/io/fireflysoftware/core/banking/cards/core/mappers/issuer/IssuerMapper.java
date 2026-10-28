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


package io.fireflysoftware.core.banking.cards.core.mappers.issuer;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.issuer.IssuerDTO;
import io.fireflysoftware.core.banking.cards.models.entities.issuer.Issuer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the Issuer entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssuerMapper {

    /**
     * Convert a Issuer entity to a IssuerDTO.
     *
     * @param LIssuer the Issuer entity to convert
     * @return the resulting IssuerDTO
     */
    IssuerDTO toDTO(Issuer LIssuer);

    /**
     * Convert a IssuerDTO to a Issuer entity.
     *
     * @param LIssuerLDLTLO the IssuerDTO to convert
     * @return the resulting Issuer entity
     */
    Issuer toEntity(IssuerDTO LIssuerLDLTLO);
}
