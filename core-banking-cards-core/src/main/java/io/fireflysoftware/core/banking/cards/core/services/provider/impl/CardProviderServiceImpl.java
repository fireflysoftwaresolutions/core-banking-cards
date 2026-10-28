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


package io.fireflysoftware.core.banking.cards.core.services.provider.impl;

import io.fireflysoftware.core.banking.cards.core.services.provider.CardProviderService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.provider.CardProviderMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.provider.CardProviderDTO;
import io.fireflysoftware.core.banking.cards.models.entities.provider.CardProvider;
import io.fireflysoftware.core.banking.cards.models.repositories.provider.CardProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardProviderServiceImpl implements CardProviderService {

    @Autowired
    private CardProviderRepository repository;

    @Autowired
    private CardProviderMapper mapper;

    @Override
    public Mono<PaginationResponse<CardProviderDTO>> listProviders(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<CardProviderDTO> createProvider(UUID cardId, CardProviderDTO providerDTO) {
        CardProvider cardProvider = mapper.toEntity(providerDTO);
        cardProvider.setCardId(cardId);
        return repository.save(cardProvider)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardProviderDTO> getProvider(UUID cardId, UUID providerId) {
        return repository.findById(providerId)
                .filter(provider -> provider.getCardId().equals(cardId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardProviderDTO> updateProvider(UUID cardId, UUID providerId, CardProviderDTO providerDTO) {
        return repository.findById(providerId)
                .filter(provider -> provider.getCardId().equals(cardId))
                .flatMap(existing -> {
                    CardProvider toUpdate = mapper.toEntity(providerDTO);
                    toUpdate.setCardProviderId(providerId);
                    toUpdate.setCardId(cardId);
                    return repository.save(toUpdate);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteProvider(UUID cardId, UUID providerId) {
        return repository.findById(providerId)
                .filter(provider -> provider.getCardId().equals(cardId))
                .flatMap(repository::delete);
    }
}
