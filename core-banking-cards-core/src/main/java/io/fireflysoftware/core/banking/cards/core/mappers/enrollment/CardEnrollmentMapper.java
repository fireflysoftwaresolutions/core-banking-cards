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


package io.fireflysoftware.core.banking.cards.core.mappers.enrollment;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.enrollment.CardEnrollmentDTO;
import io.fireflysoftware.core.banking.cards.models.entities.enrollment.CardEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardEnrollment entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardEnrollmentMapper {

    /**
     * Convert a CardEnrollment entity to a CardEnrollmentDTO.
     *
     * @param LCardLEnrollment the CardEnrollment entity to convert
     * @return the resulting CardEnrollmentDTO
     */
    CardEnrollmentDTO toDTO(CardEnrollment LCardLEnrollment);

    /**
     * Convert a CardEnrollmentDTO to a CardEnrollment entity.
     *
     * @param LCardLEnrollmentLDLTLO the CardEnrollmentDTO to convert
     * @return the resulting CardEnrollment entity
     */
    CardEnrollment toEntity(CardEnrollmentDTO LCardLEnrollmentLDLTLO);
}
