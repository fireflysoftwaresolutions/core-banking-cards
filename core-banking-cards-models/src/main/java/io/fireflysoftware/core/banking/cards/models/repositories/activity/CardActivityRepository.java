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


package io.fireflysoftware.core.banking.cards.models.repositories.activity;

import io.fireflysoftware.core.banking.cards.models.entities.activity.CardActivity;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardActivity entities.
 */
@Repository
public interface CardActivityRepository extends BaseRepository<CardActivity, UUID> {
    /**
     * Find a CardActivity by its ID.
     *
     * @param activityId the ID of the CardActivity to find
     * @return a Mono emitting the CardActivity if found, or empty if not found
     */
    Mono<CardActivity> findByActivityId(UUID activityId);

    /**
     * Find a CardActivity by its reference.
     *
     * @param activityReference the reference of the CardActivity to find
     * @return a Mono emitting the CardActivity if found, or empty if not found
     */
    Mono<CardActivity> findByActivityReference(String activityReference);

    /**
     * Find CardActivities by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardActivities for the specified card
     */
    Flux<CardActivity> findByCardId(UUID cardId);

    /**
     * Find CardActivities by card ID with pagination.
     *
     * @param cardId the card ID to search for
     * @param pageable the pagination information
     * @return a Flux emitting the CardActivities for the specified card with pagination
     */
    Flux<CardActivity> findByCardId(UUID cardId, org.springframework.data.domain.Pageable pageable);

    /**
     * Count CardActivities by card ID.
     *
     * @param cardId the card ID to count for
     * @return a Mono emitting the count of CardActivities for the specified card
     */
    @org.springframework.data.r2dbc.repository.Query("SELECT COUNT(*) FROM card_activity WHERE card_id = :cardId")
    Mono<Long> countByCardId(UUID cardId);

    /**
     * Find CardActivities by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardActivities for the specified party
     */
    Flux<CardActivity> findByPartyId(UUID partyId);

    /**
     * Find CardActivities by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardActivities for the specified account
     */
    Flux<CardActivity> findByAccountId(UUID accountId);

    /**
     * Find CardActivities by activity type.
     *
     * @param activityType the activity type to search for
     * @return a Flux emitting the CardActivities of the specified type
     */
    Flux<CardActivity> findByActivityType(String activityType);

    /**
     * Find CardActivities by activity category.
     *
     * @param activityCategory the activity category to search for
     * @return a Flux emitting the CardActivities of the specified category
     */
    Flux<CardActivity> findByActivityCategory(String activityCategory);

    /**
     * Find CardActivities by activity status.
     *
     * @param activityStatus the activity status to search for
     * @return a Flux emitting the CardActivities with the specified status
     */
    Flux<CardActivity> findByActivityStatus(String activityStatus);

    /**
     * Find CardActivities by activity result.
     *
     * @param activityResult the activity result to search for
     * @return a Flux emitting the CardActivities with the specified result
     */
    Flux<CardActivity> findByActivityResult(String activityResult);

    /**
     * Find CardActivities by activity timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardActivities within the specified date range
     */
    Flux<CardActivity> findByActivityTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find CardActivities by activity channel.
     *
     * @param activityChannel the activity channel to search for
     * @return a Flux emitting the CardActivities with the specified channel
     */
    Flux<CardActivity> findByActivityChannel(String activityChannel);

    /**
     * Find CardActivities by activity source.
     *
     * @param activitySource the activity source to search for
     * @return a Flux emitting the CardActivities with the specified source
     */
    Flux<CardActivity> findByActivitySource(String activitySource);

    /**
     * Find CardActivities initiated by parties.
     *
     * @return a Flux emitting all CardActivities initiated by parties
     */
    Flux<CardActivity> findByIsPartyInitiatedTrue();

    /**
     * Find CardActivities initiated by the system.
     *
     * @return a Flux emitting all CardActivities initiated by the system
     */
    Flux<CardActivity> findByIsSystemInitiatedTrue();

    /**
     * Find CardActivities initiated by agents.
     *
     * @return a Flux emitting all CardActivities initiated by agents
     */
    Flux<CardActivity> findByIsAgentInitiatedTrue();

    /**
     * Find CardActivities by agent ID.
     *
     * @param agentId the agent ID to search for
     * @return a Flux emitting the CardActivities for the specified agent
     */
    Flux<CardActivity> findByAgentId(String agentId);

    /**
     * Find successful CardActivities.
     *
     * @return a Flux emitting all successful CardActivities
     */
    Flux<CardActivity> findByIsSuccessfulTrue();

    /**
     * Find unsuccessful CardActivities.
     *
     * @return a Flux emitting all unsuccessful CardActivities
     */
    Flux<CardActivity> findByIsSuccessfulFalse();

    /**
     * Find CardActivities by failure code.
     *
     * @param failureCode the failure code to search for
     * @return a Flux emitting the CardActivities with the specified failure code
     */
    Flux<CardActivity> findByFailureCode(String failureCode);

    /**
     * Find CardActivities where notifications were sent.
     *
     * @return a Flux emitting all CardActivities where notifications were sent
     */
    Flux<CardActivity> findByIsNotificationSentTrue();

    /**
     * Find CardActivities by related entity type and ID.
     *
     * @param relatedEntityType the related entity type to search for
     * @param relatedEntityId the related entity ID to search for
     * @return a Flux emitting the CardActivities related to the specified entity
     */
    Flux<CardActivity> findByRelatedEntityTypeAndRelatedEntityId(String relatedEntityType, UUID relatedEntityId);
}