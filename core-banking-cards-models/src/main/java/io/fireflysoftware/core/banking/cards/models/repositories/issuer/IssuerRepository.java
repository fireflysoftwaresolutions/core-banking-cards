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


package io.fireflysoftware.core.banking.cards.models.repositories.issuer;

import io.fireflysoftware.core.banking.cards.models.entities.issuer.Issuer;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing Issuer entities.
 */
@Repository
public interface IssuerRepository extends BaseRepository<Issuer, UUID> {
    /**
     * Find an Issuer by its ID.
     *
     * @param issuerId the ID of the Issuer to find
     * @return a Mono emitting the Issuer if found, or empty if not found
     */
    Mono<Issuer> findByIssuerId(UUID issuerId);
    
    /**
     * Find an Issuer by its code.
     *
     * @param issuerCode the code of the Issuer to find
     * @return a Mono emitting the Issuer if found, or empty if not found
     */
    Mono<Issuer> findByIssuerCode(String issuerCode);
    
    /**
     * Find Issuers by country code.
     *
     * @param countryCode the country code to search for
     * @return a Flux emitting the Issuers for the specified country
     */
    Flux<Issuer> findByCountryCode(String countryCode);
    
    /**
     * Find active Issuers.
     *
     * @return a Flux emitting all active Issuers
     */
    Flux<Issuer> findByIsActiveTrue();
}