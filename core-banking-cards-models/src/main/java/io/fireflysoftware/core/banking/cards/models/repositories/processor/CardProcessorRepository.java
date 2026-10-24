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


package io.fireflysoftware.core.banking.cards.models.repositories.processor;

import io.fireflysoftware.core.banking.cards.models.entities.processor.CardProcessor;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing CardProcessor entities.
 */
@Repository
public interface CardProcessorRepository extends BaseRepository<CardProcessor, UUID> {
    /**
     * Find a CardProcessor by its ID.
     *
     * @param processorId the ID of the CardProcessor to find
     * @return a Mono emitting the CardProcessor if found, or empty if not found
     */
    Mono<CardProcessor> findByProcessorId(UUID processorId);
    
    /**
     * Find a CardProcessor by its reference.
     *
     * @param processorReference the reference of the CardProcessor to find
     * @return a Mono emitting the CardProcessor if found, or empty if not found
     */
    Mono<CardProcessor> findByProcessorReference(String processorReference);
    
    /**
     * Find CardProcessors by processor name.
     *
     * @param processorName the processor name to search for
     * @return a Flux emitting the CardProcessors with the specified name
     */
    Flux<CardProcessor> findByProcessorName(String processorName);
    
    /**
     * Find CardProcessors by processor legal name.
     *
     * @param processorLegalName the processor legal name to search for
     * @return a Flux emitting the CardProcessors with the specified legal name
     */
    Flux<CardProcessor> findByProcessorLegalName(String processorLegalName);
    
    /**
     * Find CardProcessors by processor code.
     *
     * @param processorCode the processor code to search for
     * @return a Flux emitting the CardProcessors with the specified code
     */
    Flux<CardProcessor> findByProcessorCode(String processorCode);
    
    /**
     * Find CardProcessors by processor type.
     *
     * @param processorType the processor type to search for
     * @return a Flux emitting the CardProcessors of the specified type
     */
    Flux<CardProcessor> findByProcessorType(String processorType);
    
    /**
     * Find CardProcessors by processor status.
     *
     * @param processorStatus the processor status to search for
     * @return a Flux emitting the CardProcessors with the specified status
     */
    Flux<CardProcessor> findByProcessorStatus(String processorStatus);
    
    /**
     * Find active CardProcessors.
     *
     * @return a Flux emitting all active CardProcessors
     */
    Flux<CardProcessor> findByIsActiveTrue();
    
    /**
     * Find CardProcessors by country.
     *
     * @param country the country to search for
     * @return a Flux emitting the CardProcessors from the specified country
     */
    Flux<CardProcessor> findByCountry(String country);
    
    /**
     * Find CardProcessors by supported card networks.
     *
     * @param cardNetwork the card network to search for
     * @return a Flux emitting the CardProcessors supporting the specified card network
     */
    Flux<CardProcessor> findBySupportedCardNetworksContaining(String cardNetwork);
    
    /**
     * Find CardProcessors by supported currencies.
     *
     * @param currencyCode the currency code to search for
     * @return a Flux emitting the CardProcessors supporting the specified currency
     */
    Flux<CardProcessor> findBySupportedCurrenciesContaining(String currencyCode);
    
    /**
     * Find CardProcessors by supported countries.
     *
     * @param countryCode the country code to search for
     * @return a Flux emitting the CardProcessors supporting the specified country
     */
    Flux<CardProcessor> findBySupportedCountriesContaining(String countryCode);
    
    /**
     * Find international CardProcessors.
     *
     * @return a Flux emitting all international CardProcessors
     */
    Flux<CardProcessor> findByIsInternationalTrue();
    
    /**
     * Find domestic CardProcessors.
     *
     * @return a Flux emitting all domestic CardProcessors
     */
    Flux<CardProcessor> findByIsDomesticTrue();
    
    /**
     * Find issuer processors.
     *
     * @return a Flux emitting all issuer processors
     */
    Flux<CardProcessor> findByIsIssuerProcessorTrue();
    
    /**
     * Find acquirer processors.
     *
     * @return a Flux emitting all acquirer processors
     */
    Flux<CardProcessor> findByIsAcquirerProcessorTrue();
    
    /**
     * Find switch processors.
     *
     * @return a Flux emitting all switch processors
     */
    Flux<CardProcessor> findByIsSwitchProcessorTrue();
    
    /**
     * Find gateway processors.
     *
     * @return a Flux emitting all gateway processors
     */
    Flux<CardProcessor> findByIsGatewayTrue();
    
    /**
     * Find PCI compliant CardProcessors.
     *
     * @return a Flux emitting all PCI compliant CardProcessors
     */
    Flux<CardProcessor> findByIsPciCompliantTrue();
    
    /**
     * Find EMV compliant CardProcessors.
     *
     * @return a Flux emitting all EMV compliant CardProcessors
     */
    Flux<CardProcessor> findByIsEmvCompliantTrue();
    
    /**
     * Find CardProcessors by processing time average.
     *
     * @param processingTimeAvgMs the processing time average to search for
     * @return a Flux emitting the CardProcessors with the specified processing time average
     */
    Flux<CardProcessor> findByProcessingTimeAvgMsLessThan(Integer processingTimeAvgMs);
    
    /**
     * Find CardProcessors by uptime percentage.
     *
     * @param uptimePercentage the uptime percentage to search for
     * @return a Flux emitting the CardProcessors with the specified uptime percentage
     */
    Flux<CardProcessor> findByUptimePercentageGreaterThan(Double uptimePercentage);
}