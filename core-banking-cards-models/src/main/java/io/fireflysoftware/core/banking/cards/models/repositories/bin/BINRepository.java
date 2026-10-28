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


package io.fireflysoftware.core.banking.cards.models.repositories.bin;

import io.fireflysoftware.core.banking.cards.models.entities.bin.BIN;
import io.fireflysoftware.core.banking.cards.models.repositories.BaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for managing BIN (Bank Identification Number) entities.
 */
@Repository
public interface BINRepository extends BaseRepository<BIN, UUID> {
    /**
     * Find a BIN by its ID.
     *
     * @param binId the ID of the BIN to find
     * @return a Mono emitting the BIN if found, or empty if not found
     */
    Mono<BIN> findByBinId(UUID binId);
    
    /**
     * Find a BIN by its number.
     *
     * @param binNumber the number of the BIN to find
     * @return a Mono emitting the BIN if found, or empty if not found
     */
    Mono<BIN> findByBinNumber(String binNumber);
}