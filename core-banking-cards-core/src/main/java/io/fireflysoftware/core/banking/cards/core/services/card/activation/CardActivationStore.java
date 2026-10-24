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


package io.fireflysoftware.core.banking.cards.core.services.card.activation;

import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt;
import io.fireflysoftware.core.banking.cards.models.entities.card.CardActivationAction;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardActivationActionRepository;
import io.fireflysoftware.core.banking.cards.models.repositories.card.CardIssuerInstrumentRepository;
import io.r2dbc.postgresql.api.PostgresqlException;
import java.util.Set;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** Native operations remain atomic; no transaction or lock spans an issuer call. */
@Repository
public class CardActivationStore {
    private static final Set<String> ACTIVATION_REJECTIONS = Set.of(
            "Activation is not permitted", "Activation action conflicts with an existing request",
            "Invalid issuer observation");
    private final CardActivationActionRepository actions;
    private final CardIssuerInstrumentRepository instruments;

    public CardActivationStore(CardActivationActionRepository actions, CardIssuerInstrumentRepository instruments) {
        this.actions = actions;
        this.instruments = instruments;
    }

    public Mono<CardActivationReceipt> reserve(UUID action, UUID card, CardActivationPolicy.AuthorizedScope scope,
                                               String digest) {
        return actions.reserve(action, card, scope.tenantId(), scope.actorId(), scope.partyId(),
                        scope.accountId(), digest, scope.expiresAt())
                .map(CardActivationStore::receipt).onErrorMap(DataAccessException.class,
                        CardActivationStore::persistenceFailure);
    }

    public Mono<CardIssuerPort.Binding> binding(UUID card) {
        return instruments.findByCardId(card).map(instrument -> new CardIssuerPort.Binding(
                        instrument.cardId(), instrument.tenantId(), instrument.partyId(), instrument.accountId(),
                        instrument.adapterId(), instrument.instrumentReference(), instrument.bindingVersion()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Activation is not permitted")));
    }

    public Mono<CardActivationReceipt> finish(UUID action, CardActivationPolicy.AuthorizedScope scope,
                                              String outcome, String issuerReceipt) {
        return actions.finish(action, scope.tenantId(), scope.actorId(), scope.partyId(), scope.accountId(),
                        scope.expiresAt(), outcome, issuerReceipt)
                .map(CardActivationStore::receipt).onErrorMap(DataAccessException.class,
                CardActivationStore::persistenceFailure);
    }

    private static ResponseStatusException persistenceFailure(DataAccessException error) {
        Throwable cause = error;
        while (cause != null) {
            if (cause instanceof io.r2dbc.spi.R2dbcException databaseError
                    && "P0001".equals(databaseError.getSqlState())
                    && cause instanceof PostgresqlException postgresError
                    && ACTIVATION_REJECTIONS.contains(postgresError.getErrorDetails().getMessage())) {
                return new ResponseStatusException(HttpStatus.CONFLICT, "Activation is not permitted");
            }
            if (cause instanceof io.r2dbc.spi.R2dbcException databaseError
                    && "23505".equals(databaseError.getSqlState())
                    && cause instanceof PostgresqlException postgresError
                    && postgresError.getErrorDetails().getTableName().filter("card_activation_action"::equals).isPresent()
                    && postgresError.getErrorDetails().getConstraintName().filter("card_activation_action_pkey"::equals).isPresent()) {
                return new ResponseStatusException(HttpStatus.CONFLICT, "Activation is not permitted");
            }
            cause = cause.getCause();
        }
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Activation persistence is unavailable");
    }

    private static CardActivationReceipt receipt(CardActivationAction action) {
        return new CardActivationReceipt(action.actionId(), action.cardId(),
                CardActivationReceipt.State.valueOf(action.state()), action.activationTime());
    }
}
