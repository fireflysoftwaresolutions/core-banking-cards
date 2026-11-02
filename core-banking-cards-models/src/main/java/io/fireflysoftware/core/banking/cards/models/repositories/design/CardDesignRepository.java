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


package io.fireflysoftware.core.banking.cards.models.repositories.design;

import io.fireflysoftware.core.banking.cards.models.entities.design.CardDesign;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing CardDesign entities.
 */
@Repository
public interface CardDesignRepository extends BaseRepository<CardDesign, UUID> {
    /**
     * Find a CardDesign by its ID.
     *
     * @param designId the ID of the CardDesign to find
     * @return a Mono emitting the CardDesign if found, or empty if not found
     */
    Mono<CardDesign> findByDesignId(UUID designId);
    
    /**
     * Find a CardDesign by its code.
     *
     * @param designCode the code of the CardDesign to find
     * @return a Mono emitting the CardDesign if found, or empty if not found
     */
    Mono<CardDesign> findByDesignCode(String designCode);
    
    /**
     * Find CardDesigns by card type ID.
     *
     * @param cardTypeId the card type ID to search for
     * @return a Flux emitting the CardDesigns for the specified card type
     */
    Flux<CardDesign> findByCardTypeId(UUID cardTypeId);
    
    /**
     * Find CardDesigns by issuer ID.
     *
     * @param issuerId the issuer ID to search for
     * @return a Flux emitting the CardDesigns for the specified issuer
     */
    Flux<CardDesign> findByIssuerId(UUID issuerId);
    
    /**
     * Find CardDesigns by card network ID.
     *
     * @param cardNetworkId the card network ID to search for
     * @return a Flux emitting the CardDesigns for the specified card network
     */
    Flux<CardDesign> findByCardNetworkId(UUID cardNetworkId);
    
    /**
     * Find active CardDesigns.
     *
     * @return a Flux emitting all active CardDesigns
     */
    Flux<CardDesign> findByIsActiveTrue();
    
    /**
     * Find default CardDesigns.
     *
     * @return a Flux emitting all default CardDesigns
     */
    Flux<CardDesign> findByIsDefaultTrue();
    
    /**
     * Find customizable CardDesigns.
     *
     * @return a Flux emitting all customizable CardDesigns
     */
    Flux<CardDesign> findByIsCustomizableTrue();
}