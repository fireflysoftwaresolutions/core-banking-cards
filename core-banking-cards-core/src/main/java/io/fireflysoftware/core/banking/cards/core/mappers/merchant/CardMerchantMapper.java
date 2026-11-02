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


package io.fireflysoftware.core.banking.cards.core.mappers.merchant;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.merchant.CardMerchantDTO;
import io.fireflysoftware.core.banking.cards.models.entities.merchant.CardMerchant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the CardMerchant entity and its DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMerchantMapper {

    /**
     * Convert a CardMerchant entity to a CardMerchantDTO.
     *
     * @param cardMerchant the CardMerchant entity to convert
     * @return the resulting CardMerchantDTO
     */
    CardMerchantDTO toDTO(CardMerchant cardMerchant);

    /**
     * Convert a CardMerchantDTO to a CardMerchant entity.
     *
     * @param cardMerchantDTO the CardMerchantDTO to convert
     * @return the resulting CardMerchant entity
     */
    CardMerchant toEntity(CardMerchantDTO cardMerchantDTO);
}
