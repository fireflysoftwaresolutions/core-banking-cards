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


package io.fireflysoftware.core.banking.cards.models.repositories.alert;

import io.fireflysoftware.core.banking.cards.models.entities.alert.CardAlert;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardAlert entities.
 */
@Repository
public interface CardAlertRepository extends BaseRepository<CardAlert, UUID> {
    /**
     * Find a CardAlert by its ID.
     *
     * @param alertId the ID of the CardAlert to find
     * @return a Mono emitting the CardAlert if found, or empty if not found
     */
    Mono<CardAlert> findByAlertId(UUID alertId);

    /**
     * Find a CardAlert by its reference.
     *
     * @param alertReference the reference of the CardAlert to find
     * @return a Mono emitting the CardAlert if found, or empty if not found
     */
    Mono<CardAlert> findByAlertReference(String alertReference);

    /**
     * Find CardAlerts by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the CardAlerts for the specified card
     */
    Flux<CardAlert> findByCardId(UUID cardId);

    /**
     * Find CardAlerts by transaction ID.
     *
     * @param transactionId the transaction ID to search for
     * @return a Flux emitting the CardAlerts for the specified transaction
     */
    Flux<CardAlert> findByTransactionId(UUID transactionId);

    /**
     * Find CardAlerts by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the CardAlerts for the specified party
     */
    Flux<CardAlert> findByPartyId(UUID partyId);

    /**
     * Find CardAlerts by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the CardAlerts for the specified account
     */
    Flux<CardAlert> findByAccountId(UUID accountId);

    /**
     * Find CardAlerts by alert type.
     *
     * @param alertType the alert type to search for
     * @return a Flux emitting the CardAlerts of the specified type
     */
    Flux<CardAlert> findByAlertType(String alertType);

    /**
     * Find CardAlerts by alert category.
     *
     * @param alertCategory the alert category to search for
     * @return a Flux emitting the CardAlerts of the specified category
     */
    Flux<CardAlert> findByAlertCategory(String alertCategory);

    /**
     * Find CardAlerts by alert severity.
     *
     * @param alertSeverity the alert severity to search for
     * @return a Flux emitting the CardAlerts with the specified severity
     */
    Flux<CardAlert> findByAlertSeverity(String alertSeverity);

    /**
     * Find CardAlerts by alert status.
     *
     * @param alertStatus the alert status to search for
     * @return a Flux emitting the CardAlerts with the specified status
     */
    Flux<CardAlert> findByAlertStatus(String alertStatus);

    /**
     * Find CardAlerts by trigger event.
     *
     * @param triggerEvent the trigger event to search for
     * @return a Flux emitting the CardAlerts with the specified trigger event
     */
    Flux<CardAlert> findByTriggerEvent(String triggerEvent);

    /**
     * Find CardAlerts by trigger timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardAlerts within the specified trigger date range
     */
    Flux<CardAlert> findByTriggerTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find party configurable CardAlerts.
     *
     * @return a Flux emitting all party configurable CardAlerts
     */
    Flux<CardAlert> findByIsPartyConfigurableTrue();

    /**
     * Find system generated CardAlerts.
     *
     * @return a Flux emitting all system generated CardAlerts
     */
    Flux<CardAlert> findByIsSystemGeneratedTrue();

    /**
     * Find read CardAlerts.
     *
     * @return a Flux emitting all read CardAlerts
     */
    Flux<CardAlert> findByIsReadTrue();

    /**
     * Find unread CardAlerts.
     *
     * @return a Flux emitting all unread CardAlerts
     */
    Flux<CardAlert> findByIsReadFalse();

    /**
     * Find CardAlerts by delivery channel.
     *
     * @param deliveryChannel the delivery channel to search for
     * @return a Flux emitting the CardAlerts with the specified delivery channel
     */
    Flux<CardAlert> findByDeliveryChannel(String deliveryChannel);

    /**
     * Find CardAlerts by delivery status.
     *
     * @param deliveryStatus the delivery status to search for
     * @return a Flux emitting the CardAlerts with the specified delivery status
     */
    Flux<CardAlert> findByDeliveryStatus(String deliveryStatus);

    /**
     * Find CardAlerts requiring action.
     *
     * @return a Flux emitting all CardAlerts requiring action
     */
    Flux<CardAlert> findByActionRequiredTrue();

    /**
     * Find CardAlerts by action type.
     *
     * @param actionType the action type to search for
     * @return a Flux emitting the CardAlerts with the specified action type
     */
    Flux<CardAlert> findByActionType(String actionType);
}