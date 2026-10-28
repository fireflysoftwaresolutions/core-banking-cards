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


package io.fireflysoftware.core.banking.cards.core.services.card.impl;

import io.fireflysoftware.core.banking.cards.core.services.card.CardService;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.mappers.card.CardMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.card.CardStatusEnum;
import io.fireflysoftware.core.banking.cards.models.entities.card.Card;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository repository;

    @Autowired
    private CardMapper mapper;

    @Override
    public Mono<PaginationResponse<CardDTO>> filterCards(FilterRequest<CardDTO> filterRequest) {
        if (hasSensitiveAuthenticationFilter(filterRequest)) {
            return Mono.error(rejectedSensitiveAuthenticationData());
        }
        return FilterUtils
                .createFilter(
                        Card.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<CardDTO> createCard(CardDTO cardDTO) {
        if (hasSensitiveAuthenticationData(cardDTO)) {
            return Mono.error(rejectedSensitiveAuthenticationData());
        }
        if ((cardDTO.getCardStatus() != null && cardDTO.getCardStatus() != CardStatusEnum.DRAFT)
                || cardDTO.getActivationDate() != null || Boolean.TRUE.equals(cardDTO.getIsActive())) {
            return Mono.error(rejectedLifecycleChange());
        }
        Card card = mapper.toEntity(cardDTO);
        card.setCardStatus(CardStatusEnum.DRAFT);
        card.setIsActive(false);
        card.setIsLocked(true);
        return repository.save(card)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardDTO> getCard(UUID cardId) {
        return repository.findByCardId(cardId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<CardDTO> updateCard(UUID cardId, CardDTO cardDTO) {
        if (hasSensitiveAuthenticationData(cardDTO)) {
            return Mono.error(rejectedSensitiveAuthenticationData());
        }
        return repository.findByCardId(cardId)
                .flatMap(existingCard -> {
                    if (existingCard.getPin() != null || existingCard.getCvv() != null) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                "Historical card credentials require controlled remediation before an update"));
                    }
                    CardStatusEnum current = existingCard.getCardStatus() != null
                            ? existingCard.getCardStatus()
                            : CardStatusEnum.DRAFT;
                    if (!java.util.Objects.equals(cardDTO.getPartyId(), existingCard.getPartyId())
                            || !java.util.Objects.equals(cardDTO.getAccountId(), existingCard.getAccountId())
                            || (cardDTO.getCardStatus() == CardStatusEnum.ACTIVE && current != CardStatusEnum.ACTIVE)
                            || !java.util.Objects.equals(cardDTO.getActivationDate(), existingCard.getActivationDate())
                            || (Boolean.TRUE.equals(cardDTO.getIsActive())
                                    && !Boolean.TRUE.equals(existingCard.getIsActive()))
                            || (Boolean.FALSE.equals(cardDTO.getIsLocked())
                                    && Boolean.TRUE.equals(existingCard.getIsLocked()))) {
                        return Mono.error(rejectedLifecycleChange());
                    }
                    Card updatedCard = mapper.toEntity(cardDTO);
                    updatedCard.setCardId(existingCard.getCardId());
                    if (updatedCard.getCardStatus() == null) {
                        updatedCard.setCardStatus(current);
                    }
                    if (!CardLifecycle.canTransition(current, updatedCard.getCardStatus())) {
                        return Mono.error(new IllegalStateException(
                                "Card " + cardId + " cannot move from " + current + " to " + updatedCard.getCardStatus()));
                    }
                    if (updatedCard.getIsActive() == null) {
                        updatedCard.setIsActive(existingCard.getIsActive());
                    }
                    if (updatedCard.getIsLocked() == null) {
                        updatedCard.setIsLocked(existingCard.getIsLocked());
                    }
                    if (updatedCard.getCardStatus() != CardStatusEnum.ACTIVE) {
                        updatedCard.setIsActive(false);
                        updatedCard.setIsLocked(true);
                    }
                    return repository.save(updatedCard);
                })
                .map(mapper::toDTO);
    }

    private static ResponseStatusException rejectedLifecycleChange() {
        return new ResponseStatusException(HttpStatus.CONFLICT,
                "Card activation and restriction removal require a verified lifecycle action");
    }

    private static boolean hasSensitiveAuthenticationData(CardDTO card) {
        return card.getPin() != null || card.getCvv() != null;
    }

    private static boolean hasSensitiveAuthenticationFilter(FilterRequest<CardDTO> request) {
        var filters = request.getFilters();
        if (filters != null && (hasSensitiveAuthenticationData(filters)
                || FilterRequest.getSpecialFilter(filters, "pin") != null
                || FilterRequest.getSpecialFilter(filters, "cvv") != null)) {
            return true;
        }
        var range = request.getRangeFilters();
        if (range != null && range.getRanges() != null
                && range.getRanges().keySet().stream().anyMatch(CardServiceImpl::isCredentialField)) {
            return true;
        }
        return request.getPagination() != null && isCredentialField(request.getPagination().getSortBy());
    }

    private static boolean isCredentialField(String field) {
        return field != null && ("pin".equalsIgnoreCase(field.trim()) || "cvv".equalsIgnoreCase(field.trim()));
    }

    private static ResponseStatusException rejectedSensitiveAuthenticationData() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "PIN and CVV are not accepted by card record operations");
    }

    @Override
    public Mono<Void> deleteCard(UUID cardId) {
        return repository.findByCardId(cardId)
                .flatMap(existingCard -> {
                    CardStatusEnum status = existingCard.getCardStatus() != null
                            ? existingCard.getCardStatus()
                            : CardStatusEnum.DRAFT;
                    if (status != CardStatusEnum.DRAFT) {
                        return Mono.error(new IllegalStateException(
                                "Card " + cardId + " is " + status
                                        + " and cannot be deleted; cancel it so the card history is kept"));
                    }
                    return repository.delete(existingCard);
                });
    }
}
