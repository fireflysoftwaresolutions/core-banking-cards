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


package io.fireflysoftware.core.banking.cards.models.repositories.program;

import io.fireflysoftware.core.banking.cards.models.entities.program.CardProgram;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing CardProgram entities.
 */
@Repository
public interface CardProgramRepository extends BaseRepository<CardProgram, UUID> {
    /**
     * Find a CardProgram by its ID.
     *
     * @param programId the ID of the CardProgram to find
     * @return a Mono emitting the CardProgram if found, or empty if not found
     */
    Mono<CardProgram> findByProgramId(UUID programId);
    
    /**
     * Find a CardProgram by its code.
     *
     * @param programCode the code of the CardProgram to find
     * @return a Mono emitting the CardProgram if found, or empty if not found
     */
    Mono<CardProgram> findByProgramCode(String programCode);
    
    /**
     * Find CardPrograms by issuer ID.
     *
     * @param issuerId the issuer ID to search for
     * @return a Flux emitting the CardPrograms for the specified issuer
     */
    Flux<CardProgram> findByIssuerId(UUID issuerId);
    
    /**
     * Find CardPrograms by BIN ID.
     *
     * @param binId the BIN ID to search for
     * @return a Flux emitting the CardPrograms for the specified BIN
     */
    Flux<CardProgram> findByBinId(UUID binId);
    
    /**
     * Find CardPrograms by card type ID.
     *
     * @param cardTypeId the card type ID to search for
     * @return a Flux emitting the CardPrograms for the specified card type
     */
    Flux<CardProgram> findByCardTypeId(UUID cardTypeId);
    
    /**
     * Find CardPrograms by card network ID.
     *
     * @param cardNetworkId the card network ID to search for
     * @return a Flux emitting the CardPrograms for the specified card network
     */
    Flux<CardProgram> findByCardNetworkId(UUID cardNetworkId);
    
    /**
     * Find active CardPrograms.
     *
     * @return a Flux emitting all active CardPrograms
     */
    Flux<CardProgram> findByIsActiveTrue();
    
    /**
     * Find CardPrograms by currency code.
     *
     * @param currencyCode the currency code to search for
     * @return a Flux emitting the CardPrograms for the specified currency
     */
    Flux<CardProgram> findByCurrencyCode(String currencyCode);
    
    /**
     * Find CardPrograms by country code.
     *
     * @param countryCode the country code to search for
     * @return a Flux emitting the CardPrograms for the specified country
     */
    Flux<CardProgram> findByCountryCode(String countryCode);
    
    /**
     * Find active CardPrograms that are currently valid (between start and end dates).
     *
     * @param currentDate the current date to check against
     * @return a Flux emitting all active and valid CardPrograms
     */
    Flux<CardProgram> findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(
            LocalDateTime currentDate, LocalDateTime currentDateEnd);
}