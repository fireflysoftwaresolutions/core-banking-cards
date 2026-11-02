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

import io.fireflysoftware.core.banking.cards.core.mappers.card.CardMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import io.fireflysoftware.core.banking.cards.models.entities.card.Card;

import jakarta.validation.Validation;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.RangeFilter;
import org.fireflyframework.core.queries.PaginationRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class CardSensitiveDataBoundaryTest {

    private final CardMapper mapper = Mappers.getMapper(CardMapper.class);

    @Test
    void historicalCredentialsNeverEnterTheOrdinaryResponseDto() {
        Card stored = new Card();
        stored.setCardId(UUID.randomUUID());
        stored.setPin("1234");
        stored.setCvv("987");
        stored.setMaskedCardNumber("************3456");

        CardDTO response = mapper.toDTO(stored);

        assertNull(response.getPin());
        assertNull(response.getCvv());
        assertEquals("************3456", response.getMaskedCardNumber());
        assertEquals(stored.getCardId(), response.getCardId());
        assertEquals("1234", stored.getPin());
        assertEquals("987", stored.getCvv());
    }

    @Test
    void dtoMappingCannotIntroduceCredentialsIntoPersistence() {
        CardDTO request = credentials();

        Card stored = mapper.toEntity(request);

        assertNull(stored.getPin());
        assertNull(stored.getCvv());
        assertEquals(request.getAccountId(), stored.getAccountId());
    }

    @Test
    void responseSerializationOmitsCredentialsEvenForAnExplicitlyPopulatedDto() {
        var json = JsonMapper.builder().build().valueToTree(credentials());

        assertFalse(json.has("pin"));
        assertFalse(json.has("cvv"));
        assertTrue(json.has("accountId"));
    }

    @Test
    void ordinaryInputRejectsBothCredentialsIncludingBlankValues() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            for (String value : new String[] {"1234", ""}) {
                CardDTO request = credentials();
                request.setPin(value);
                request.setCvv(value.isEmpty() ? "" : "987");
                Set<String> fields =
                        validator.validate(request).stream()
                                .map(v -> v.getPropertyPath().toString())
                                .collect(Collectors.toSet());
                assertTrue(fields.contains("pin"));
                assertTrue(fields.contains("cvv"));
            }
        }
    }

    @Test
    void dtoDiagnosticsDoNotExposeCredentials() {
        String diagnostic = credentials().toString();

        assertFalse(diagnostic.contains("1234"));
        assertFalse(diagnostic.contains("987"));
    }

    @Test
    void directCreateRejectsCredentialsBeforeAnyPersistenceCollaborator() {
        var service = new CardServiceImpl();
        for (CardDTO request : credentialVariants()) {
            StepVerifier.create(Mono.defer(() -> service.createCard(request)))
                    .expectErrorSatisfies(CardSensitiveDataBoundaryTest::assertBadInput)
                    .verify();
        }
    }

    @Test
    void directUpdateRejectsCredentialsBeforeAnyPersistenceCollaborator() {
        var service = new CardServiceImpl();
        for (CardDTO request : credentialVariants()) {
            StepVerifier.create(Mono.defer(() -> service.updateCard(UUID.randomUUID(), request)))
                    .expectErrorSatisfies(CardSensitiveDataBoundaryTest::assertBadInput)
                    .verify();
        }
    }

    @Test
    void credentialPredicatesAreRejectedBeforeAnyFilterQuery() {
        var service = new CardServiceImpl();
        for (CardDTO filters : credentialVariants()) {
            var request = new FilterRequest<CardDTO>();
            request.setFilters(filters);
            StepVerifier.create(Mono.defer(() -> service.filterCards(request)))
                    .expectErrorSatisfies(CardSensitiveDataBoundaryTest::assertBadInput)
                    .verify();
        }
        for (String field : new String[] {"pin", "cvv"}) {
            for (boolean absent : new boolean[] {true, false}) {
                var filters = new CardDTO();
                if (absent) FilterRequest.setNullFilter(filters, field);
                else FilterRequest.setNotNullFilter(filters, field);
                var request = new FilterRequest<CardDTO>();
                request.setFilters(filters);
                StepVerifier.create(Mono.defer(() -> service.filterCards(request)))
                        .expectErrorSatisfies(CardSensitiveDataBoundaryTest::assertBadInput)
                        .verify();
            }
        }
    }

    @Test
    void credentialRangeAndSortCannotBecomeObservationOracles() {
        var service = new CardServiceImpl();
        for (String field : new String[] {"pin", "cvv"}) {
            var request = new FilterRequest<CardDTO>();
            request.setRangeFilters(
                    new RangeFilter(Map.of(field, new RangeFilter.Range<>("0", "9"))));
            StepVerifier.create(Mono.defer(() -> service.filterCards(request)))
                    .expectErrorSatisfies(CardSensitiveDataBoundaryTest::assertBadInput)
                    .verify();
            request.setRangeFilters(null);
            var pagination = new PaginationRequest();
            pagination.setSortBy(field);
            request.setPagination(pagination);
            StepVerifier.create(Mono.defer(() -> service.filterCards(request)))
                    .expectErrorSatisfies(CardSensitiveDataBoundaryTest::assertBadInput)
                    .verify();
        }
    }

    private static void assertBadInput(Throwable error) {
        var response = assertInstanceOf(ResponseStatusException.class, error);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getReason().contains("1234"));
        assertFalse(response.getReason().contains("987"));
    }

    private static CardDTO[] credentialVariants() {
        CardDTO pinOnly = new CardDTO();
        pinOnly.setPin("");
        CardDTO cvvOnly = new CardDTO();
        cvvOnly.setCvv("");
        return new CardDTO[] {credentials(), pinOnly, cvvOnly};
    }

    private static CardDTO credentials() {
        CardDTO dto = new CardDTO();
        dto.setAccountId(UUID.fromString("f4c60a44-bdf8-45e0-aa00-cf498dc404ab"));
        dto.setPin("1234");
        dto.setCvv("987");
        return dto;
    }
}
