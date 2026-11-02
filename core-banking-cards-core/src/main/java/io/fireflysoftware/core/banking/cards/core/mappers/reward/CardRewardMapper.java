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


package io.fireflysoftware.core.banking.cards.core.mappers.reward;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.reward.CardRewardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.reward.CardReward;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardReward entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardRewardMapper {

    /**
     * Convert a CardReward entity to a CardRewardDTO.
     *
     * @param LCardLReward the CardReward entity to convert
     * @return the resulting CardRewardDTO
     */
    CardRewardDTO toDTO(CardReward LCardLReward);

    /**
     * Convert a CardRewardDTO to a CardReward entity.
     *
     * @param LCardLRewardLDLTLO the CardRewardDTO to convert
     * @return the resulting CardReward entity
     */
    CardReward toEntity(CardRewardDTO LCardLRewardLDLTLO);
}
