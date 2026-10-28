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


package io.fireflysoftware.core.banking.cards.models.repositories.network;

import io.fireflysoftware.core.banking.cards.models.entities.network.CardNetwork;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing CardNetwork entities.
 */
@Repository
public interface CardNetworkRepository extends BaseRepository<CardNetwork, UUID> {
    /**
     * Find a CardNetwork by its ID.
     *
     * @param cardNetworkId the ID of the CardNetwork to find
     * @return a Mono emitting the CardNetwork if found, or empty if not found
     */
    Mono<CardNetwork> findByCardNetworkId(UUID cardNetworkId);
    
    /**
     * Find a CardNetwork by its code.
     *
     * @param networkCode the code of the CardNetwork to find
     * @return a Mono emitting the CardNetwork if found, or empty if not found
     */
    Mono<CardNetwork> findByNetworkCode(String networkCode);
    
    /**
     * Find a CardNetwork by its name.
     *
     * @param networkName the name of the CardNetwork to find
     * @return a Mono emitting the CardNetwork if found, or empty if not found
     */
    Mono<CardNetwork> findByNetworkName(String networkName);
    
    /**
     * Find active CardNetworks.
     *
     * @return a Flux emitting all active CardNetworks
     */
    Flux<CardNetwork> findByIsActiveTrue();
}