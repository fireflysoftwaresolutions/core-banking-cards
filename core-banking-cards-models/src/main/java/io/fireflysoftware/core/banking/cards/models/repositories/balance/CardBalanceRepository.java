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


package io.fireflysoftware.core.banking.cards.models.repositories.balance;

import io.fireflysoftware.core.banking.cards.models.entities.balance.CardBalance;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardBalance entities.
 */
@Repository
public interface CardBalanceRepository extends BaseRepository<CardBalance, UUID> {
    /**
     * Find a CardBalance by its ID.
     *
     * @param balanceId the ID of the CardBalance to find
     * @return a Mono emitting the CardBalance if found, or empty if not found
     */
    Mono<CardBalance> findByBalanceId(UUID balanceId);

    /**
     * Find CardBalances by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardBalances for the specified card
     */
    Flux<CardBalance> findByCardId(UUID cardId);

    /**
     * Find CardBalances by card ID with pagination.
     *
     * @param cardId the card ID to search for
     * @param pageable the pagination information
     * @return a Flux emitting the CardBalances for the specified card with pagination
     */
    Flux<CardBalance> findByCardId(UUID cardId, org.springframework.data.domain.Pageable pageable);

    /**
     * Count CardBalances by card ID.
     *
     * @param cardId the card ID to count for
     * @return a Mono emitting the count of CardBalances for the specified card
     */
    @org.springframework.data.r2dbc.repository.Query("SELECT COUNT(*) FROM card_balance WHERE card_id = :cardId")
    Mono<Long> countByCardId(UUID cardId);

    /**
     * Find CardBalances by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardBalances for the specified party
     */
    Flux<CardBalance> findByPartyId(UUID partyId);

    /**
     * Find CardBalances by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardBalances for the specified account
     */
    Flux<CardBalance> findByAccountId(UUID accountId);

    /**
     * Find CardBalances by statement ID.
     *
     * @param statementId the statement ID to search for
     * @return a Flux emitting the CardBalances for the specified statement
     */
    Flux<CardBalance> findByStatementId(UUID statementId);

    /**
     * Find CardBalances by balance type.
     *
     * @param balanceType the balance type to search for
     * @return a Flux emitting the CardBalances of the specified type
     */
    Flux<CardBalance> findByBalanceType(String balanceType);

    /**
     * Find CardBalances by balance category.
     *
     * @param balanceCategory the balance category to search for
     * @return a Flux emitting the CardBalances of the specified category
     */
    Flux<CardBalance> findByBalanceCategory(String balanceCategory);

    /**
     * Find CardBalances by currency code.
     *
     * @param currencyCode the currency code to search for
     * @return a Flux emitting the CardBalances with the specified currency
     */
    Flux<CardBalance> findByCurrencyCode(String currencyCode);

    /**
     * Find CardBalances with promotional rate.
     *
     * @return a Flux emitting all CardBalances with promotional rate
     */
    Flux<CardBalance> findByIsPromotionalRateTrue();

    /**
     * Find CardBalances in grace period.
     *
     * @return a Flux emitting all CardBalances in grace period
     */
    Flux<CardBalance> findByIsInGracePeriodTrue();

    /**
     * Find delinquent CardBalances.
     *
     * @return a Flux emitting all delinquent CardBalances
     */
    Flux<CardBalance> findByIsDelinquentTrue();

    /**
     * Find charged off CardBalances.
     *
     * @return a Flux emitting all charged off CardBalances
     */
    Flux<CardBalance> findByIsChargedOffTrue();

    /**
     * Find written off CardBalances.
     *
     * @return a Flux emitting all written off CardBalances
     */
    Flux<CardBalance> findByIsWrittenOffTrue();

    /**
     * Find CardBalances in collection.
     *
     * @return a Flux emitting all CardBalances in collection
     */
    Flux<CardBalance> findByIsInCollectionTrue();

    /**
     * Find CardBalances by promotion ID.
     *
     * @param promotionId the promotion ID to search for
     * @return a Flux emitting the CardBalances for the specified promotion
     */
    Flux<CardBalance> findByPromotionId(UUID promotionId);

    /**
     * Find CardBalances by as of date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardBalances within the specified date range
     */
    Flux<CardBalance> findByAsOfDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardBalances by last transaction ID.
     *
     * @param lastTransactionId the last transaction ID to search for
     * @return a Flux emitting the CardBalances with the specified last transaction
     */
    Flux<CardBalance> findByLastTransactionId(UUID lastTransactionId);
}