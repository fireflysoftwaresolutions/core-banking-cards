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


package io.fireflysoftware.core.banking.cards.models.repositories.statement;

import io.fireflysoftware.core.banking.cards.models.entities.statement.CardStatement;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardStatement entities.
 */
@Repository
public interface CardStatementRepository extends BaseRepository<CardStatement, UUID> {
    /**
     * Find a CardStatement by its ID.
     *
     * @param statementId the ID of the CardStatement to find
     * @return a Mono emitting the CardStatement if found, or empty if not found
     */
    Mono<CardStatement> findByStatementId(UUID statementId);

    /**
     * Find a CardStatement by its reference.
     *
     * @param statementReference the reference of the CardStatement to find
     * @return a Mono emitting the CardStatement if found, or empty if not found
     */
    Mono<CardStatement> findByStatementReference(String statementReference);

    /**
     * Find CardStatements by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardStatements for the specified card
     */
    Flux<CardStatement> findByCardId(UUID cardId);

    /**
     * Find CardStatements by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardStatements for the specified party
     */
    Flux<CardStatement> findByPartyId(UUID partyId);

    /**
     * Find CardStatements by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardStatements for the specified account
     */
    Flux<CardStatement> findByAccountId(UUID accountId);

    /**
     * Find CardStatements by statement date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardStatements within the specified date range
     */
    Flux<CardStatement> findByStatementDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardStatements by due date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardStatements within the specified due date range
     */
    Flux<CardStatement> findByDueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardStatements by payment status.
     *
     * @param paymentStatus the payment status to search for
     * @return a Flux emitting the CardStatements with the specified payment status
     */
    Flux<CardStatement> findByPaymentStatus(String paymentStatus);

    /**
     * Find generated CardStatements.
     *
     * @return a Flux emitting all generated CardStatements
     */
    Flux<CardStatement> findByIsGeneratedTrue();

    /**
     * Find delivered CardStatements.
     *
     * @return a Flux emitting all delivered CardStatements
     */
    Flux<CardStatement> findByIsDeliveredTrue();

    /**
     * Find viewed CardStatements.
     *
     * @return a Flux emitting all viewed CardStatements
     */
    Flux<CardStatement> findByIsViewedTrue();

    /**
     * Find CardStatements by delivery method.
     *
     * @param deliveryMethod the delivery method to search for
     * @return a Flux emitting the CardStatements with the specified delivery method
     */
    Flux<CardStatement> findByDeliveryMethod(String deliveryMethod);
}