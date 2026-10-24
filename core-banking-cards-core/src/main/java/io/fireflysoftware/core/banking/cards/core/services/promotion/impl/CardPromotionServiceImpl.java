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


package io.fireflysoftware.core.banking.cards.core.services.promotion.impl;

import io.fireflysoftware.core.banking.cards.core.services.promotion.CardPromotionService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.promotion.CardPromotionMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.promotion.CardPromotionDTO;
import io.fireflysoftware.core.banking.cards.models.entities.promotion.CardPromotion;
import io.fireflysoftware.core.banking.cards.models.repositories.promotion.CardPromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardPromotionServiceImpl implements CardPromotionService {

    @Autowired
    private CardPromotionRepository repository;

    @Autowired
    private CardPromotionMapper mapper;

    @Override
    public Mono<PaginationResponse<CardPromotionDTO>> listPromotions(UUID cardId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardId(cardId, pageable),
                () -> repository.countByCardId(cardId)
        );
    }

    @Override
    public Mono<CardPromotionDTO> createPromotion(UUID cardId, CardPromotionDTO promotionDTO) {
        promotionDTO.setCardId(cardId);
        CardPromotion entity = mapper.toEntity(promotionDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardPromotionDTO> getPromotion(UUID cardId, UUID promotionId) {
        return repository.findByPromotionId(promotionId)
                .flatMap(entity -> {
                    if (!entity.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardPromotion not found for the specified card."));
                    }
                    return Mono.just(mapper.toDTO(entity));
                });
    }

    @Override
    public Mono<CardPromotionDTO> updatePromotion(UUID cardId, UUID promotionId, CardPromotionDTO promotionDTO) {
        return repository.findByPromotionId(promotionId)
                .flatMap(existingPromotion -> {
                    if (!existingPromotion.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardPromotion not found for the specified card."));
                    }
                    
                    CardPromotion updatedPromotion = mapper.toEntity(promotionDTO);
                    updatedPromotion.setPromotionId(existingPromotion.getPromotionId());
                    updatedPromotion.setCardId(cardId);
                    
                    return repository.save(updatedPromotion);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePromotion(UUID cardId, UUID promotionId) {
        return repository.findByPromotionId(promotionId)
                .flatMap(promotion -> {
                    if (!promotion.getCardId().equals(cardId)) {
                        return Mono.error(new RuntimeException("CardPromotion not found for the specified card."));
                    }
                    return repository.delete(promotion);
                });
    }
}
