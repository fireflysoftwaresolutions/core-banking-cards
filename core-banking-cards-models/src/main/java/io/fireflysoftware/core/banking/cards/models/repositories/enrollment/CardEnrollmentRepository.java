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


package io.fireflysoftware.core.banking.cards.models.repositories.enrollment;

import io.fireflysoftware.core.banking.cards.models.entities.enrollment.CardEnrollment;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardEnrollment entities.
 */
@Repository
public interface CardEnrollmentRepository extends BaseRepository<CardEnrollment, UUID> {
    /**
     * Find a CardEnrollment by its ID.
     *
     * @param enrollmentId the ID of the CardEnrollment to find
     * @return a Mono emitting the CardEnrollment if found, or empty if not found
     */
    Mono<CardEnrollment> findByEnrollmentId(UUID enrollmentId);

    /**
     * Find a CardEnrollment by its reference.
     *
     * @param enrollmentReference the reference of the CardEnrollment to find
     * @return a Mono emitting the CardEnrollment if found, or empty if not found
     */
    Mono<CardEnrollment> findByEnrollmentReference(String enrollmentReference);

    /**
     * Find CardEnrollments by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardEnrollments for the specified card
     */
    Flux<CardEnrollment> findByCardId(UUID cardId);

    /**
     * Find CardEnrollments by card ID with pagination.
     *
     * @param cardId the card ID to search for
     * @param pageable the pagination information
     * @return a Flux emitting the CardEnrollments for the specified card with pagination
     */
    Flux<CardEnrollment> findByCardId(UUID cardId, org.springframework.data.domain.Pageable pageable);

    /**
     * Count CardEnrollments by card ID.
     *
     * @param cardId the card ID to count for
     * @return a Mono emitting the count of CardEnrollments for the specified card
     */
    @org.springframework.data.r2dbc.repository.Query("SELECT COUNT(*) FROM card_enrollment WHERE card_id = :cardId")
    Mono<Long> countByCardId(UUID cardId);

    /**
     * Find CardEnrollments by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardEnrollments for the specified party
     */
    Flux<CardEnrollment> findByPartyId(UUID partyId);

    /**
     * Find CardEnrollments by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardEnrollments for the specified account
     */
    Flux<CardEnrollment> findByAccountId(UUID accountId);

    /**
     * Find CardEnrollments by enrollment type.
     *
     * @param enrollmentType the enrollment type to search for
     * @return a Flux emitting the CardEnrollments of the specified type
     */
    Flux<CardEnrollment> findByEnrollmentType(String enrollmentType);

    /**
     * Find CardEnrollments by enrollment status.
     *
     * @param enrollmentStatus the enrollment status to search for
     * @return a Flux emitting the CardEnrollments with the specified status
     */
    Flux<CardEnrollment> findByEnrollmentStatus(String enrollmentStatus);

    /**
     * Find CardEnrollments by enrollment timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardEnrollments within the specified date range
     */
    Flux<CardEnrollment> findByEnrollmentTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardEnrollments by enrollment channel.
     *
     * @param enrollmentChannel the enrollment channel to search for
     * @return a Flux emitting the CardEnrollments from the specified channel
     */
    Flux<CardEnrollment> findByEnrollmentChannel(String enrollmentChannel);

    /**
     * Find CardEnrollments by enrollment source.
     *
     * @param enrollmentSource the enrollment source to search for
     * @return a Flux emitting the CardEnrollments from the specified source
     */
    Flux<CardEnrollment> findByEnrollmentSource(String enrollmentSource);

    /**
     * Find CardEnrollments by promotion ID.
     *
     * @param promotionId the promotion ID to search for
     * @return a Flux emitting the CardEnrollments for the specified promotion
     */
    Flux<CardEnrollment> findByPromotionId(UUID promotionId);

    /**
     * Find CardEnrollments by program ID.
     *
     * @param programId the program ID to search for
     * @return a Flux emitting the CardEnrollments for the specified program
     */
    Flux<CardEnrollment> findByProgramId(UUID programId);

    /**
     * Find CardEnrollments by feature ID.
     *
     * @param featureId the feature ID to search for
     * @return a Flux emitting the CardEnrollments for the specified feature
     */
    Flux<CardEnrollment> findByFeatureId(UUID featureId);

    /**
     * Find CardEnrollments by feature code.
     *
     * @param featureCode the feature code to search for
     * @return a Flux emitting the CardEnrollments for the specified feature code
     */
    Flux<CardEnrollment> findByFeatureCode(String featureCode);

    /**
     * Find auto-enrolled CardEnrollments.
     *
     * @return a Flux emitting all auto-enrolled CardEnrollments
     */
    Flux<CardEnrollment> findByIsAutoEnrolledTrue();

    /**
     * Find opt-in CardEnrollments.
     *
     * @return a Flux emitting all opt-in CardEnrollments
     */
    Flux<CardEnrollment> findByIsOptInTrue();

    /**
     * Find opt-out CardEnrollments.
     *
     * @return a Flux emitting all opt-out CardEnrollments
     */
    Flux<CardEnrollment> findByIsOptOutTrue();

    /**
     * Find active CardEnrollments.
     *
     * @return a Flux emitting all active CardEnrollments
     */
    Flux<CardEnrollment> findByIsActiveTrue();

    /**
     * Find recurring CardEnrollments.
     *
     * @return a Flux emitting all recurring CardEnrollments
     */
    Flux<CardEnrollment> findByIsRecurringTrue();

    /**
     * Find CardEnrollments with accepted terms.
     *
     * @return a Flux emitting all CardEnrollments with accepted terms
     */
    Flux<CardEnrollment> findByTermsAcceptedTrue();

    /**
     * Find CardEnrollments with next recurrence date before a specific date.
     *
     * @param date the date to compare against
     * @return a Flux emitting the CardEnrollments with next recurrence date before the specified date
     */
    Flux<CardEnrollment> findByIsRecurringTrueAndNextRecurrenceDateBefore(LocalDateTime date);
}