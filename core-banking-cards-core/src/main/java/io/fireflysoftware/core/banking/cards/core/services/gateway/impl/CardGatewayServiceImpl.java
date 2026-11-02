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


package io.fireflysoftware.core.banking.cards.core.services.gateway.impl;

import io.fireflysoftware.core.banking.cards.core.services.gateway.CardGatewayService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.gateway.CardGatewayMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.gateway.CardGatewayDTO;
import io.fireflysoftware.core.banking.cards.models.entities.gateway.CardGateway;
import io.fireflysoftware.core.banking.cards.models.repositories.gateway.CardGatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardGatewayServiceImpl implements CardGatewayService {

    @Autowired
    private CardGatewayRepository repository;

    @Autowired
    private CardGatewayMapper mapper;

    @Override
    public Mono<PaginationResponse<CardGatewayDTO>> listGateways(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                repository::findAllBy,
                repository::count
        );
    }

    @Override
    public Mono<CardGatewayDTO> createGateway(CardGatewayDTO gatewayDTO) {
        CardGateway entity = mapper.toEntity(gatewayDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardGatewayDTO> getGateway(UUID gatewayId) {
        return repository.findByGatewayId(gatewayId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardGatewayDTO> updateGateway(UUID gatewayId, CardGatewayDTO gatewayDTO) {
        return repository.findByGatewayId(gatewayId)
                .flatMap(existingGateway -> {
                    CardGateway updatedGateway = mapper.toEntity(gatewayDTO);
                    updatedGateway.setGatewayId(existingGateway.getGatewayId());
                    return repository.save(updatedGateway);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteGateway(UUID gatewayId) {
        return repository.findByGatewayId(gatewayId)
                .flatMap(repository::delete);
    }
}
