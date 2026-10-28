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


package io.fireflysoftware.core.banking.cards.models.repositories.terminal;

import io.fireflysoftware.core.banking.cards.models.entities.terminal.CardTerminal;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardTerminal entities.
 */
@Repository
public interface CardTerminalRepository extends BaseRepository<CardTerminal, UUID> {
    /**
     * Find a CardTerminal by its ID.
     *
     * @param terminalId the ID of the CardTerminal to find
     * @return a Mono emitting the CardTerminal if found, or empty if not found
     */
    Mono<CardTerminal> findByTerminalId(UUID terminalId);
    
    /**
     * Find a CardTerminal by its reference.
     *
     * @param terminalReference the reference of the CardTerminal to find
     * @return a Mono emitting the CardTerminal if found, or empty if not found
     */
    Mono<CardTerminal> findByTerminalReference(String terminalReference);
    
    /**
     * Find a CardTerminal by its serial number.
     *
     * @param terminalSerialNumber the serial number of the CardTerminal to find
     * @return a Mono emitting the CardTerminal if found, or empty if not found
     */
    Mono<CardTerminal> findByTerminalSerialNumber(String terminalSerialNumber);
    
    /**
     * Find CardTerminals by terminal type.
     *
     * @param terminalType the terminal type to search for
     * @return a Flux emitting the CardTerminals of the specified type
     */
    Flux<CardTerminal> findByTerminalType(String terminalType);
    
    /**
     * Find CardTerminals by terminal model.
     *
     * @param terminalModel the terminal model to search for
     * @return a Flux emitting the CardTerminals of the specified model
     */
    Flux<CardTerminal> findByTerminalModel(String terminalModel);
    
    /**
     * Find CardTerminals by terminal manufacturer.
     *
     * @param terminalManufacturer the terminal manufacturer to search for
     * @return a Flux emitting the CardTerminals from the specified manufacturer
     */
    Flux<CardTerminal> findByTerminalManufacturer(String terminalManufacturer);
    
    /**
     * Find CardTerminals by terminal status.
     *
     * @param terminalStatus the terminal status to search for
     * @return a Flux emitting the CardTerminals with the specified status
     */
    Flux<CardTerminal> findByTerminalStatus(String terminalStatus);
    
    /**
     * Find active CardTerminals.
     *
     * @return a Flux emitting all active CardTerminals
     */
    Flux<CardTerminal> findByIsActiveTrue();
    
    /**
     * Find CardTerminals by merchant ID.
     *
     * @param merchantId the merchant ID to search for
     * @return a Flux emitting the CardTerminals for the specified merchant
     */
    Flux<CardTerminal> findByMerchantId(UUID merchantId);
    
    /**
     * Find CardTerminals by merchant location ID.
     *
     * @param merchantLocationId the merchant location ID to search for
     * @return a Flux emitting the CardTerminals for the specified merchant location
     */
    Flux<CardTerminal> findByMerchantLocationId(String merchantLocationId);
    
    /**
     * Find CardTerminals by country.
     *
     * @param country the country to search for
     * @return a Flux emitting the CardTerminals from the specified country
     */
    Flux<CardTerminal> findByCountry(String country);
    
    /**
     * Find CardTerminals by acquirer ID.
     *
     * @param acquirerId the acquirer ID to search for
     * @return a Flux emitting the CardTerminals for the specified acquirer
     */
    Flux<CardTerminal> findByAcquirerId(String acquirerId);
    
    /**
     * Find CardTerminals by processor ID.
     *
     * @param processorId the processor ID to search for
     * @return a Flux emitting the CardTerminals for the specified processor
     */
    Flux<CardTerminal> findByProcessorId(String processorId);
    
    /**
     * Find physical CardTerminals.
     *
     * @return a Flux emitting all physical CardTerminals
     */
    Flux<CardTerminal> findByIsPhysicalTrue();
    
    /**
     * Find virtual CardTerminals.
     *
     * @return a Flux emitting all virtual CardTerminals
     */
    Flux<CardTerminal> findByIsVirtualTrue();
    
    /**
     * Find mobile CardTerminals.
     *
     * @return a Flux emitting all mobile CardTerminals
     */
    Flux<CardTerminal> findByIsMobileTrue();
    
    /**
     * Find attended CardTerminals.
     *
     * @return a Flux emitting all attended CardTerminals
     */
    Flux<CardTerminal> findByIsAttendedTrue();
    
    /**
     * Find unattended CardTerminals.
     *
     * @return a Flux emitting all unattended CardTerminals
     */
    Flux<CardTerminal> findByIsUnattendedTrue();
    
    /**
     * Find contactless CardTerminals.
     *
     * @return a Flux emitting all contactless CardTerminals
     */
    Flux<CardTerminal> findByIsContactlessTrue();
    
    /**
     * Find CardTerminals with fault detected.
     *
     * @return a Flux emitting all CardTerminals with fault detected
     */
    Flux<CardTerminal> findByIsFaultDetectedTrue();
    
    /**
     * Find CardTerminals by last transaction timestamp range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Flux emitting the CardTerminals with last transaction within the specified date range
     */
    Flux<CardTerminal> findByLastTransactionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}