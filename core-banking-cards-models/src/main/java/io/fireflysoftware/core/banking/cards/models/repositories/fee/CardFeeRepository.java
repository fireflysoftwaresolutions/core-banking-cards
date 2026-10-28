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


package io.fireflysoftware.core.banking.cards.models.repositories.fee;

import io.fireflysoftware.core.banking.cards.models.entities.fee.CardFee;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardFee entities.
 */
@Repository
public interface CardFeeRepository extends BaseRepository<CardFee, UUID> {
    /**
     * Find a CardFee by its ID.
     *
     * @param feeId the ID of the CardFee to find
     * @return a Mono emitting the CardFee if found, or empty if not found
     */
    Mono<CardFee> findByFeeId(UUID feeId);

    /**
     * Find a CardFee by its reference.
     *
     * @param feeReference the reference of the CardFee to find
     * @return a Mono emitting the CardFee if found, or empty if not found
     */
    Mono<CardFee> findByFeeReference(String feeReference);

    /**
     * Find CardFees by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardFees for the specified card
     */
    Flux<CardFee> findByCardId(UUID cardId);

    /**
     * Find CardFees by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardFees for the specified party
     */
    Flux<CardFee> findByPartyId(UUID partyId);

    /**
     * Find CardFees by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardFees for the specified account
     */
    Flux<CardFee> findByAccountId(UUID accountId);

    /**
     * Find CardFees by transaction ID.
     *
     * @param transactionId the transaction ID to search for
     * @return a Flux emitting the CardFees for the specified transaction
     */
    Flux<CardFee> findByTransactionId(UUID transactionId);

    /**
     * Find CardFees by statement ID.
     *
     * @param statementId the statement ID to search for
     * @return a Flux emitting the CardFees for the specified statement
     */
    Flux<CardFee> findByStatementId(UUID statementId);

    /**
     * Find CardFees by program ID.
     *
     * @param programId the program ID to search for
     * @return a Flux emitting the CardFees for the specified program
     */
    Flux<CardFee> findByProgramId(UUID programId);

    /**
     * Find CardFees by fee type.
     *
     * @param feeType the fee type to search for
     * @return a Flux emitting the CardFees of the specified type
     */
    Flux<CardFee> findByFeeType(String feeType);

    /**
     * Find CardFees by fee status.
     *
     * @param feeStatus the fee status to search for
     * @return a Flux emitting the CardFees with the specified status
     */
    Flux<CardFee> findByFeeStatus(String feeStatus);

    /**
     * Find recurring CardFees.
     *
     * @return a Flux emitting all recurring CardFees
     */
    Flux<CardFee> findByIsRecurringTrue();

    /**
     * Find waived CardFees.
     *
     * @return a Flux emitting all waived CardFees
     */
    Flux<CardFee> findByIsWaivedTrue();

    /**
     * Find refunded CardFees.
     *
     * @return a Flux emitting all refunded CardFees
     */
    Flux<CardFee> findByIsRefundedTrue();

    /**
     * Find billed CardFees.
     *
     * @return a Flux emitting all billed CardFees
     */
    Flux<CardFee> findByIsBilledTrue();

    /**
     * Find paid CardFees.
     *
     * @return a Flux emitting all paid CardFees
     */
    Flux<CardFee> findByIsPaidTrue();

    /**
     * Find CardFees by charge timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardFees within the specified date range
     */
    Flux<CardFee> findByChargeTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardFees with next recurrence date before a specific date.
     *
     * @param date the date to compare against
     * @return a Flux emitting the CardFees with next recurrence date before the specified date
     */
    Flux<CardFee> findByIsRecurringTrueAndNextRecurrenceDateBefore(LocalDateTime date);
}