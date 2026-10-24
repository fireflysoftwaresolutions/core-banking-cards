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


package io.fireflysoftware.core.banking.cards.models.repositories.merchant;

import io.fireflysoftware.core.banking.cards.models.entities.merchant.CardMerchant;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing CardMerchant entities.
 */
@Repository
public interface CardMerchantRepository extends BaseRepository<CardMerchant, UUID> {
    /**
     * Find a CardMerchant by its ID.
     *
     * @param merchantId the ID of the CardMerchant to find
     * @return a Mono emitting the CardMerchant if found, or empty if not found
     */
    Mono<CardMerchant> findByMerchantId(UUID merchantId);
    
    /**
     * Find a CardMerchant by its reference.
     *
     * @param merchantReference the reference of the CardMerchant to find
     * @return a Mono emitting the CardMerchant if found, or empty if not found
     */
    Mono<CardMerchant> findByMerchantReference(String merchantReference);
    
    /**
     * Find CardMerchants by merchant name.
     *
     * @param merchantName the merchant name to search for
     * @return a Flux emitting the CardMerchants with the specified name
     */
    Flux<CardMerchant> findByMerchantName(String merchantName);
    
    /**
     * Find CardMerchants by merchant legal name.
     *
     * @param merchantLegalName the merchant legal name to search for
     * @return a Flux emitting the CardMerchants with the specified legal name
     */
    Flux<CardMerchant> findByMerchantLegalName(String merchantLegalName);
    
    /**
     * Find CardMerchants by merchant category code.
     *
     * @param merchantCategoryCode the merchant category code to search for
     * @return a Flux emitting the CardMerchants with the specified category code
     */
    Flux<CardMerchant> findByMerchantCategoryCode(String merchantCategoryCode);
    
    /**
     * Find CardMerchants by merchant type.
     *
     * @param merchantType the merchant type to search for
     * @return a Flux emitting the CardMerchants of the specified type
     */
    Flux<CardMerchant> findByMerchantType(String merchantType);
    
    /**
     * Find CardMerchants by merchant status.
     *
     * @param merchantStatus the merchant status to search for
     * @return a Flux emitting the CardMerchants with the specified status
     */
    Flux<CardMerchant> findByMerchantStatus(String merchantStatus);
    
    /**
     * Find active CardMerchants.
     *
     * @return a Flux emitting all active CardMerchants
     */
    Flux<CardMerchant> findByIsActiveTrue();
    
    /**
     * Find CardMerchants by country.
     *
     * @param country the country to search for
     * @return a Flux emitting the CardMerchants from the specified country
     */
    Flux<CardMerchant> findByCountry(String country);
    
    /**
     * Find online CardMerchants.
     *
     * @return a Flux emitting all online CardMerchants
     */
    Flux<CardMerchant> findByIsOnlineTrue();
    
    /**
     * Find physical CardMerchants.
     *
     * @return a Flux emitting all physical CardMerchants
     */
    Flux<CardMerchant> findByIsPhysicalTrue();
    
    /**
     * Find mobile CardMerchants.
     *
     * @return a Flux emitting all mobile CardMerchants
     */
    Flux<CardMerchant> findByIsMobileTrue();
    
    /**
     * Find international CardMerchants.
     *
     * @return a Flux emitting all international CardMerchants
     */
    Flux<CardMerchant> findByIsInternationalTrue();
    
    /**
     * Find CardMerchants by supported currencies.
     *
     * @param currencyCode the currency code to search for
     * @return a Flux emitting the CardMerchants supporting the specified currency
     */
    Flux<CardMerchant> findBySupportedCurrenciesContaining(String currencyCode);
    
    /**
     * Find CardMerchants by supported card networks.
     *
     * @param cardNetwork the card network to search for
     * @return a Flux emitting the CardMerchants supporting the specified card network
     */
    Flux<CardMerchant> findBySupportedCardNetworksContaining(String cardNetwork);
    
    /**
     * Find high risk CardMerchants.
     *
     * @return a Flux emitting all high risk CardMerchants
     */
    Flux<CardMerchant> findByIsHighRiskTrue();
    
    /**
     * Find CardMerchants by risk rating.
     *
     * @param riskRating the risk rating to search for
     * @return a Flux emitting the CardMerchants with the specified risk rating
     */
    Flux<CardMerchant> findByRiskRating(String riskRating);
    
    /**
     * Find CardMerchants with suspected fraud.
     *
     * @return a Flux emitting all CardMerchants with suspected fraud
     */
    Flux<CardMerchant> findByIsFraudSuspectedTrue();
    
    /**
     * Find blacklisted CardMerchants.
     *
     * @return a Flux emitting all blacklisted CardMerchants
     */
    Flux<CardMerchant> findByIsBlacklistedTrue();
    
    /**
     * Find CardMerchants by acquirer ID.
     *
     * @param acquirerId the acquirer ID to search for
     * @return a Flux emitting the CardMerchants for the specified acquirer
     */
    Flux<CardMerchant> findByAcquirerId(String acquirerId);
    
    /**
     * Find CardMerchants by processor ID.
     *
     * @param processorId the processor ID to search for
     * @return a Flux emitting the CardMerchants for the specified processor
     */
    Flux<CardMerchant> findByProcessorId(String processorId);
}