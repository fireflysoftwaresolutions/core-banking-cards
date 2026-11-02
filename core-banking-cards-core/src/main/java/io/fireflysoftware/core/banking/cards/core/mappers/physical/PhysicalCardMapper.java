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


package io.fireflysoftware.core.banking.cards.core.mappers.physical;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.physical.PhysicalCardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.physical.PhysicalCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhysicalCardMapper {
    PhysicalCardDTO toDTO (PhysicalCard physicalCard);
    PhysicalCard toEntity (PhysicalCardDTO physicalCardDTO);
}
