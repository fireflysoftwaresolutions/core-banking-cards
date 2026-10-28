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


package io.fireflysoftware.core.banking.cards.core.services.issuer;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.issuer.IssuerDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface IssuerService {

    /**
     * List all issuers (paginated).
     */
    Mono<PaginationResponse<IssuerDTO>> listIssuers(PaginationRequest paginationRequest);

    /**
     * Create a new issuer.
     */
    Mono<IssuerDTO> createIssuer(IssuerDTO issuerDTO);

    /**
     * Retrieve a specific issuer by ID.
     */
    Mono<IssuerDTO> getIssuer(UUID issuerId);

    /**
     * Update an existing issuer by ID.
     */
    Mono<IssuerDTO> updateIssuer(UUID issuerId, IssuerDTO issuerDTO);

    /**
     * Delete an issuer by its unique ID.
     */
    Mono<Void> deleteIssuer(UUID issuerId);
}
