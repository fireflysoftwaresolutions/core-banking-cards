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


package io.fireflysoftware.core.banking.cards.models.repositories.interest;

import io.fireflysoftware.core.banking.cards.models.entities.interest.CardInterest;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardInterest entities.
 */
@Repository
public interface CardInterestRepository extends BaseRepository<CardInterest, UUID> {
    /**
     * Find a CardInterest by its ID.
     *
     * @param interestId the ID of the CardInterest to find
     * @return a Mono emitting the CardInterest if found, or empty if not found
     */
    Mono<CardInterest> findByInterestId(UUID interestId);

    /**
     * Find a CardInterest by its reference.
     *
     * @param interestReference the reference of the CardInterest to find
     * @return a Mono emitting the CardInterest if found, or empty if not found
     */
    Mono<CardInterest> findByInterestReference(String interestReference);

    /**
     * Find CardInterests by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardInterests for the specified card
     */
    Flux<CardInterest> findByCardId(UUID cardId);

    /**
     * Find CardInterests by card ID with pagination.
     *
     * @param cardId the card ID to search for
     * @param pageable the pagination information
     * @return a Flux emitting the CardInterests for the specified card with pagination
     */
    Flux<CardInterest> findByCardId(UUID cardId, org.springframework.data.domain.Pageable pageable);

    /**
     * Count CardInterests by card ID.
     *
     * @param cardId the card ID to count for
     * @return a Mono emitting the count of CardInterests for the specified card
     */
    @org.springframework.data.r2dbc.repository.Query("SELECT COUNT(*) FROM card_interest WHERE card_id = :cardId")
    Mono<Long> countByCardId(UUID cardId);

    /**
     * Find CardInterests by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardInterests for the specified party
     */
    Flux<CardInterest> findByPartyId(UUID partyId);

    /**
     * Find CardInterests by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardInterests for the specified account
     */
    Flux<CardInterest> findByAccountId(UUID accountId);

    /**
     * Find CardInterests by statement ID.
     *
     * @param statementId the statement ID to search for
     * @return a Flux emitting the CardInterests for the specified statement
     */
    Flux<CardInterest> findByStatementId(UUID statementId);

    /**
     * Find CardInterests by program ID.
     *
     * @param programId the program ID to search for
     * @return a Flux emitting the CardInterests for the specified program
     */
    Flux<CardInterest> findByProgramId(UUID programId);

    /**
     * Find CardInterests by interest type.
     *
     * @param interestType the interest type to search for
     * @return a Flux emitting the CardInterests of the specified type
     */
    Flux<CardInterest> findByInterestType(String interestType);

    /**
     * Find variable rate CardInterests.
     *
     * @return a Flux emitting all variable rate CardInterests
     */
    Flux<CardInterest> findByIsVariableRateTrue();

    /**
     * Find promotional rate CardInterests.
     *
     * @return a Flux emitting all promotional rate CardInterests
     */
    Flux<CardInterest> findByIsPromotionalRateTrue();

    /**
     * Find CardInterests in grace period.
     *
     * @return a Flux emitting all CardInterests in grace period
     */
    Flux<CardInterest> findByIsGracePeriodTrue();

    /**
     * Find charged CardInterests.
     *
     * @return a Flux emitting all charged CardInterests
     */
    Flux<CardInterest> findByIsChargedTrue();

    /**
     * Find waived CardInterests.
     *
     * @return a Flux emitting all waived CardInterests
     */
    Flux<CardInterest> findByIsWaivedTrue();

    /**
     * Find billed CardInterests.
     *
     * @return a Flux emitting all billed CardInterests
     */
    Flux<CardInterest> findByIsBilledTrue();

    /**
     * Find paid CardInterests.
     *
     * @return a Flux emitting all paid CardInterests
     */
    Flux<CardInterest> findByIsPaidTrue();

    /**
     * Find CardInterests by charge timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardInterests within the specified date range
     */
    Flux<CardInterest> findByChargeTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardInterests by accrual period.
     *
     * @param startDate the start date of the accrual period
     * @param endDate the end date of the accrual period
     * @return a Flux emitting the CardInterests within the specified accrual period
     */
    Flux<CardInterest> findByAccrualStartDateBeforeAndAccrualEndDateAfter(
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardInterests by promotion ID.
     *
     * @param promotionId the promotion ID to search for
     * @return a Flux emitting the CardInterests for the specified promotion
     */
    Flux<CardInterest> findByPromotionId(UUID promotionId);
}