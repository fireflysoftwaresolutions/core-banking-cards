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


package io.fireflysoftware.core.banking.cards.core.services.card;

import io.fireflysoftware.core.banking.cards.core.services.card.impl.CardServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.fireflysoftware.core.banking.cards.core.mappers.card.CardMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import io.fireflysoftware.core.banking.cards.interfaces.enums.card.CardStatusEnum;
import io.fireflysoftware.core.banking.cards.models.entities.card.Card;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CardActivationCrudTest {
    private CardServiceImpl service;
    private CardRepository repository;
    private final CardMapper mapper = Mappers.getMapper(CardMapper.class);

    @BeforeEach
    void setup() {
        repository = mock(CardRepository.class);
        service = new CardServiceImpl();
        ReflectionTestUtils.setField(service, "repository", repository);
        ReflectionTestUtils.setField(service, "mapper", mapper);
    }

    @Test
    void ordinaryCreateCannotInventAnActiveOrUnlockedInstrument() {
        for (String scenario : new String[]{"active", "enabled", "timestamp"}) {
            var request = new CardDTO();
            switch (scenario) {
                case "active" -> request.setCardStatus(CardStatusEnum.ACTIVE);
                case "enabled" -> request.setIsActive(true);
                case "timestamp" -> request.setActivationDate(LocalDateTime.of(2026, 1, 1, 0, 0));
            }
            reject(service.createCard(request));
        }
    }

    @Test
    void ordinaryCreatePersistsAnInactiveLockedDraft() {
        when(repository.save(any(Card.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        var request = new CardDTO();
        request.setIsLocked(false);
        var result = service.createCard(request).block();
        assertNotNull(result);
        assertEquals(CardStatusEnum.DRAFT, result.getCardStatus());
        assertFalse(result.getIsActive());
        assertTrue(result.getIsLocked());
        assertNull(result.getActivationDate());
    }

    @Test
    void draftAndBlockedRecordsCannotBecomeActiveThroughOrdinaryUpdate() {
        for (var status : new CardStatusEnum[]{CardStatusEnum.DRAFT, CardStatusEnum.BLOCKED}) {
            var card = card(status);
            when(repository.findByCardId(card.getCardId())).thenReturn(Mono.just(card));
            var request = mapper.toDTO(card);
            request.setCardStatus(CardStatusEnum.ACTIVE);
            reject(service.updateCard(card.getCardId(), request));
        }
    }

    @Test
    void ordinaryUpdateCannotChangeOwnershipOrActivationDateOrRemoveRestrictions() {
        for (String scenario : new String[]{"party", "account", "timestamp", "unlock", "enable"}) {
            var card = card(CardStatusEnum.DRAFT);
            when(repository.findByCardId(card.getCardId())).thenReturn(Mono.just(card));
            var request = mapper.toDTO(card);
            switch (scenario) {
                case "party" -> request.setPartyId(UUID.randomUUID());
                case "account" -> request.setAccountId(UUID.randomUUID());
                case "timestamp" -> request.setActivationDate(LocalDateTime.of(2026, 1, 1, 0, 0));
                case "unlock" -> request.setIsLocked(false);
                case "enable" -> request.setIsActive(true);
            }
            reject(service.updateCard(card.getCardId(), request));
        }
    }

    private static Card card(CardStatusEnum status) {
        var card = new Card();
        card.setCardId(UUID.randomUUID());
        card.setPartyId(UUID.randomUUID());
        card.setAccountId(UUID.randomUUID());
        card.setCardStatus(status);
        card.setIsActive(false);
        card.setIsLocked(true);
        return card;
    }

    private static void reject(Mono<CardDTO> result) {
        StepVerifier.create(result).expectErrorSatisfies(error -> assertEquals(HttpStatus.CONFLICT,
                assertInstanceOf(ResponseStatusException.class, error).getStatusCode())).verify();
    }
}
