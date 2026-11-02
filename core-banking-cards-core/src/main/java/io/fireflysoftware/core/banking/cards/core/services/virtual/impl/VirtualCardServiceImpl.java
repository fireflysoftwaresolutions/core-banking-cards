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


package io.fireflysoftware.core.banking.cards.core.services.virtual.impl;

import io.fireflysoftware.core.banking.cards.core.services.virtual.VirtualCardService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.virtual.VirtualCardMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.virtual.VirtualCardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.virtual.VirtualCard;
import io.fireflysoftware.core.banking.cards.models.repositories.virtual.VirtualCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class VirtualCardServiceImpl implements VirtualCardService {

    @Autowired
    private VirtualCardRepository repository;

    @Autowired
    private VirtualCardMapper mapper;

    @Override
    public Mono<PaginationResponse<VirtualCardDTO>> listVirtualCards(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<VirtualCardDTO> createVirtualCard(UUID cardId, VirtualCardDTO virtualCardDTO) {
        virtualCardDTO.setCardId(cardId);
        VirtualCard virtualCard = mapper.toEntity(virtualCardDTO);
        return repository.save(virtualCard)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<VirtualCardDTO> getVirtualCard(UUID cardId, UUID virtualCardId) {
        return repository.findByVirtualCardId(virtualCardId)
                .filter(virtualCard -> virtualCard.getCardId().equals(cardId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<VirtualCardDTO> updateVirtualCard(UUID cardId, UUID virtualCardId, VirtualCardDTO virtualCardDTO) {
        return repository.findByVirtualCardId(virtualCardId)
                .filter(existingCard -> existingCard.getCardId().equals(cardId))
                .flatMap(existingCard -> {
                    virtualCardDTO.setVirtualCardId(existingCard.getVirtualCardId());
                    virtualCardDTO.setCardId(cardId);
                    VirtualCard updatedEntity = mapper.toEntity(virtualCardDTO);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteVirtualCard(UUID cardId, UUID virtualCardId) {
        return repository.findByVirtualCardId(virtualCardId)
                .filter(virtualCard -> virtualCard.getCardId().equals(cardId))
                .flatMap(repository::delete);
    }
}
