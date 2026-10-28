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


package io.fireflysoftware.core.banking.cards.models.repositories.acquirer;

import io.fireflysoftware.core.banking.cards.models.entities.acquirer.CardAcquirer;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing CardAcquirer entities.
 */
@Repository
public interface CardAcquirerRepository extends BaseRepository<CardAcquirer, UUID> {
    /**
     * Find a CardAcquirer by its ID.
     *
     * @param acquirerId the ID of the CardAcquirer to find
     * @return a Mono emitting the CardAcquirer if found, or empty if not found
     */
    Mono<CardAcquirer> findByAcquirerId(UUID acquirerId);
    
    /**
     * Find a CardAcquirer by its reference.
     *
     * @param acquirerReference the reference of the CardAcquirer to find
     * @return a Mono emitting the CardAcquirer if found, or empty if not found
     */
    Mono<CardAcquirer> findByAcquirerReference(String acquirerReference);
    
    /**
     * Find CardAcquirers by acquirer name.
     *
     * @param acquirerName the acquirer name to search for
     * @return a Flux emitting the CardAcquirers with the specified name
     */
    Flux<CardAcquirer> findByAcquirerName(String acquirerName);
    
    /**
     * Find CardAcquirers by acquirer legal name.
     *
     * @param acquirerLegalName the acquirer legal name to search for
     * @return a Flux emitting the CardAcquirers with the specified legal name
     */
    Flux<CardAcquirer> findByAcquirerLegalName(String acquirerLegalName);
    
    /**
     * Find CardAcquirers by acquirer code.
     *
     * @param acquirerCode the acquirer code to search for
     * @return a Flux emitting the CardAcquirers with the specified code
     */
    Flux<CardAcquirer> findByAcquirerCode(String acquirerCode);
    
    /**
     * Find CardAcquirers by acquirer type.
     *
     * @param acquirerType the acquirer type to search for
     * @return a Flux emitting the CardAcquirers of the specified type
     */
    Flux<CardAcquirer> findByAcquirerType(String acquirerType);
    
    /**
     * Find CardAcquirers by acquirer status.
     *
     * @param acquirerStatus the acquirer status to search for
     * @return a Flux emitting the CardAcquirers with the specified status
     */
    Flux<CardAcquirer> findByAcquirerStatus(String acquirerStatus);
    
    /**
     * Find active CardAcquirers.
     *
     * @return a Flux emitting all active CardAcquirers
     */
    Flux<CardAcquirer> findByIsActiveTrue();
    
    /**
     * Find CardAcquirers by country.
     *
     * @param country the country to search for
     * @return a Flux emitting the CardAcquirers from the specified country
     */
    Flux<CardAcquirer> findByCountry(String country);
    
    /**
     * Find CardAcquirers by supported card networks.
     *
     * @param cardNetwork the card network to search for
     * @return a Flux emitting the CardAcquirers supporting the specified card network
     */
    Flux<CardAcquirer> findBySupportedCardNetworksContaining(String cardNetwork);
    
    /**
     * Find CardAcquirers by supported currencies.
     *
     * @param currencyCode the currency code to search for
     * @return a Flux emitting the CardAcquirers supporting the specified currency
     */
    Flux<CardAcquirer> findBySupportedCurrenciesContaining(String currencyCode);
    
    /**
     * Find CardAcquirers by supported countries.
     *
     * @param countryCode the country code to search for
     * @return a Flux emitting the CardAcquirers supporting the specified country
     */
    Flux<CardAcquirer> findBySupportedCountriesContaining(String countryCode);
    
    /**
     * Find international CardAcquirers.
     *
     * @return a Flux emitting all international CardAcquirers
     */
    Flux<CardAcquirer> findByIsInternationalTrue();
    
    /**
     * Find domestic CardAcquirers.
     *
     * @return a Flux emitting all domestic CardAcquirers
     */
    Flux<CardAcquirer> findByIsDomesticTrue();
    
    /**
     * Find online CardAcquirers.
     *
     * @return a Flux emitting all online CardAcquirers
     */
    Flux<CardAcquirer> findByIsOnlineTrue();
    
    /**
     * Find physical CardAcquirers.
     *
     * @return a Flux emitting all physical CardAcquirers
     */
    Flux<CardAcquirer> findByIsPhysicalTrue();
    
    /**
     * Find mobile CardAcquirers.
     *
     * @return a Flux emitting all mobile CardAcquirers
     */
    Flux<CardAcquirer> findByIsMobileTrue();
    
    /**
     * Find CardAcquirers by processor ID.
     *
     * @param processorId the processor ID to search for
     * @return a Flux emitting the CardAcquirers for the specified processor
     */
    Flux<CardAcquirer> findByProcessorId(UUID processorId);
    
    /**
     * Find CardAcquirers by gateway ID.
     *
     * @param gatewayId the gateway ID to search for
     * @return a Flux emitting the CardAcquirers for the specified gateway
     */
    Flux<CardAcquirer> findByGatewayId(UUID gatewayId);
    
    /**
     * Find PCI compliant CardAcquirers.
     *
     * @return a Flux emitting all PCI compliant CardAcquirers
     */
    Flux<CardAcquirer> findByIsPciCompliantTrue();
}