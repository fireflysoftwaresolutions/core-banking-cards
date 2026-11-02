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


package io.fireflysoftware.core.banking.cards.models.repositories.fraud;

import io.fireflysoftware.core.banking.cards.models.entities.fraud.FraudCase;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing FraudCase entities.
 */
@Repository
public interface FraudCaseRepository extends BaseRepository<FraudCase, UUID> {
    /**
     * Find a FraudCase by its ID.
     *
     * @param fraudCaseId the ID of the FraudCase to find
     * @return a Mono emitting the FraudCase if found, or empty if not found
     */
    Mono<FraudCase> findByFraudCaseId(UUID fraudCaseId);

    /**
     * Find a FraudCase by its reference.
     *
     * @param caseReference the reference of the FraudCase to find
     * @return a Mono emitting the FraudCase if found, or empty if not found
     */
    Mono<FraudCase> findByCaseReference(String caseReference);

    /**
     * Find FraudCases by card ID.
     *
     * @param cardId the card ID to search for
     * @return a Flux emitting the FraudCases for the specified card
     */
    Flux<FraudCase> findByCardId(UUID cardId);

    /**
     * Find FraudCases by transaction ID.
     *
     * @param transactionId the transaction ID to search for
     * @return a Flux emitting the FraudCases for the specified transaction
     */
    Flux<FraudCase> findByTransactionId(UUID transactionId);

    /**
     * Find FraudCases by party ID.
     *
     * @param partyId the party ID to search for
     * @return a Flux emitting the FraudCases for the specified party
     */
    Flux<FraudCase> findByPartyId(UUID partyId);

    /**
     * Find FraudCases by account ID.
     *
     * @param accountId the account ID to search for
     * @return a Flux emitting the FraudCases for the specified account
     */
    Flux<FraudCase> findByAccountId(UUID accountId);

    /**
     * Find FraudCases by provider reference.
     *
     * @param providerReference the provider reference to search for
     * @return a Flux emitting the FraudCases with the specified provider reference
     */
    Flux<FraudCase> findByProviderReference(String providerReference);

    /**
     * Find FraudCases by network reference.
     *
     * @param networkReference the network reference to search for
     * @return a Flux emitting the FraudCases with the specified network reference
     */
    Flux<FraudCase> findByNetworkReference(String networkReference);

    /**
     * Find FraudCases by fraud type.
     *
     * @param fraudType the fraud type to search for
     * @return a Flux emitting the FraudCases of the specified type
     */
    Flux<FraudCase> findByFraudType(String fraudType);

    /**
     * Find FraudCases by fraud reason code.
     *
     * @param fraudReasonCode the fraud reason code to search for
     * @return a Flux emitting the FraudCases with the specified reason code
     */
    Flux<FraudCase> findByFraudReasonCode(String fraudReasonCode);

    /**
     * Find FraudCases by fraud status.
     *
     * @param fraudStatus the fraud status to search for
     * @return a Flux emitting the FraudCases with the specified status
     */
    Flux<FraudCase> findByFraudStatus(String fraudStatus);

    /**
     * Find FraudCases by risk level.
     *
     * @param riskLevel the risk level to search for
     * @return a Flux emitting the FraudCases with the specified risk level
     */
    Flux<FraudCase> findByRiskLevel(String riskLevel);

    /**
     * Find FraudCases by detection source.
     *
     * @param detectionSource the detection source to search for
     * @return a Flux emitting the FraudCases with the specified detection source
     */
    Flux<FraudCase> findByDetectionSource(String detectionSource);

    /**
     * Find FraudCases by detection timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the FraudCases within the specified detection date range
     */
    Flux<FraudCase> findByDetectionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find FraudCases reported by party.
     *
     * @return a Flux emitting all FraudCases reported by parties
     */
    Flux<FraudCase> findByReportedByPartyTrue();

    /**
     * Find FraudCases where the card has been blocked.
     *
     * @return a Flux emitting all FraudCases where the card has been blocked
     */
    Flux<FraudCase> findByIsCardBlockedTrue();

    /**
     * Find FraudCases where the party has been notified.
     *
     * @return a Flux emitting all FraudCases where the party has been notified
     */
    Flux<FraudCase> findByIsPartyNotifiedTrue();

    /**
     * Find FraudCases where the cardholder has been credited.
     *
     * @return a Flux emitting all FraudCases where the cardholder has been credited
     */
    Flux<FraudCase> findByIsCardholderCreditedTrue();

    /**
     * Find FraudCases by assigned agent ID.
     *
     * @param assignedAgentId the assigned agent ID to search for
     * @return a Flux emitting the FraudCases assigned to the specified agent
     */
    Flux<FraudCase> findByAssignedAgentId(UUID assignedAgentId);
}