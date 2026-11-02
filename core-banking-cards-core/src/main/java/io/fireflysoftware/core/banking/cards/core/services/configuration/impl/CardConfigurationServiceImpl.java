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


package io.fireflysoftware.core.banking.cards.core.services.configuration.impl;

import io.fireflysoftware.core.banking.cards.core.services.configuration.CardConfigurationService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.configuration.CardConfigurationMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.configuration.CardConfigurationDTO;
import io.fireflysoftware.core.banking.cards.models.entities.configuration.CardConfiguration;
import io.fireflysoftware.core.banking.cards.models.repositories.configuration.CardConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardConfigurationServiceImpl implements CardConfigurationService {

    @Autowired
    private CardConfigurationRepository repository;

    @Autowired
    private CardConfigurationMapper mapper;

    @Override
    public Mono<PaginationResponse<CardConfigurationDTO>> listConfigurations(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<CardConfigurationDTO> createConfiguration(UUID cardId, CardConfigurationDTO configDTO) {
        configDTO.setCardId(cardId);
        CardConfiguration entity = mapper.toEntity(configDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardConfigurationDTO> getConfiguration(UUID cardId, UUID configId) {
        return repository.findByCardConfigurationId(configId)
                .filter(config -> config.getCardId().equals(cardId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardConfigurationDTO> updateConfiguration(UUID cardId, UUID configId, CardConfigurationDTO configDTO) {
        return repository.findByCardConfigurationId(configId)
                .filter(config -> config.getCardId().equals(cardId))
                .flatMap(existingConfig -> {
                    existingConfig.setConfigType(configDTO.getConfigType());
                    existingConfig.setConfigValue(configDTO.getConfigValue());
                    return repository.save(existingConfig);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteConfiguration(UUID cardId, UUID configId) {
        return repository.findByCardConfigurationId(configId)
                .filter(config -> config.getCardId().equals(cardId))
                .flatMap(repository::delete);
    }
}