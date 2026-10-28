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


package io.fireflysoftware.core.banking.cards.models.repositories.cardtype;

import io.fireflysoftware.core.banking.cards.models.entities.cardtype.CardType;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing CardType entities.
 */
@Repository
public interface CardTypeRepository extends BaseRepository<CardType, UUID> {
    /**
     * Find a CardType by its ID.
     *
     * @param cardTypeId the ID of the CardType to find
     * @return a Mono emitting the CardType if found, or empty if not found
     */
    Mono<CardType> findByCardTypeId(UUID cardTypeId);
    
    /**
     * Find a CardType by its code.
     *
     * @param typeCode the code of the CardType to find
     * @return a Mono emitting the CardType if found, or empty if not found
     */
    Mono<CardType> findByTypeCode(String typeCode);
    
    /**
     * Find a CardType by its name.
     *
     * @param typeName the name of the CardType to find
     * @return a Mono emitting the CardType if found, or empty if not found
     */
    Mono<CardType> findByTypeName(String typeName);
    
    /**
     * Find active CardTypes.
     *
     * @return a Flux emitting all active CardTypes
     */
    Flux<CardType> findByIsActiveTrue();
    
    /**
     * Find credit card types.
     *
     * @return a Flux emitting all credit card types
     */
    Flux<CardType> findByIsCreditTrue();
    
    /**
     * Find debit card types.
     *
     * @return a Flux emitting all debit card types
     */
    Flux<CardType> findByIsDebitTrue();
    
    /**
     * Find prepaid card types.
     *
     * @return a Flux emitting all prepaid card types
     */
    Flux<CardType> findByIsPrepaidTrue();
}