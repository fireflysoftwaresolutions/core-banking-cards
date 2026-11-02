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


package io.fireflysoftware.core.banking.cards.core.services.network.impl;

import io.fireflysoftware.core.banking.cards.core.services.network.CardNetworkService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.network.CardNetworkMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.network.CardNetworkDTO;
import io.fireflysoftware.core.banking.cards.models.entities.network.CardNetwork;
import io.fireflysoftware.core.banking.cards.models.repositories.network.CardNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardNetworkServiceImpl implements CardNetworkService {

    @Autowired
    private CardNetworkRepository repository;

    @Autowired
    private CardNetworkMapper mapper;

    @Override
    public Mono<PaginationResponse<CardNetworkDTO>> listNetworks(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                repository::findAllBy,
                repository::count
        );
    }

    @Override
    public Mono<CardNetworkDTO> createNetwork(CardNetworkDTO networkDTO) {
        CardNetwork entity = mapper.toEntity(networkDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardNetworkDTO> getNetwork(UUID networkId) {
        return repository.findByCardNetworkId(networkId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardNetworkDTO> updateNetwork(UUID networkId, CardNetworkDTO networkDTO) {
        return repository.findByCardNetworkId(networkId)
                .flatMap(existingNetwork -> {
                    CardNetwork updatedNetwork = mapper.toEntity(networkDTO);
                    updatedNetwork.setCardNetworkId(existingNetwork.getCardNetworkId());
                    return repository.save(updatedNetwork);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteNetwork(UUID networkId) {
        return repository.findByCardNetworkId(networkId)
                .flatMap(repository::delete);
    }
}
