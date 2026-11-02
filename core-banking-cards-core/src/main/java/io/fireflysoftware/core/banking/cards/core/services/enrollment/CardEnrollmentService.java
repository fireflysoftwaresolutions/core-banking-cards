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


package io.fireflysoftware.core.banking.cards.core.services.enrollment;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.enrollment.CardEnrollmentDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;
public interface CardEnrollmentService {

    /**
     * List all enrollments (paginated) for a specific card.
     */
    Mono<PaginationResponse<CardEnrollmentDTO>> listEnrollments(
            UUID cardId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new enrollment for a specific card.
     */
    Mono<CardEnrollmentDTO> createEnrollment(UUID cardId, CardEnrollmentDTO enrollmentDTO);

    /**
     * Retrieve a specific enrollment by its unique ID, ensuring it belongs to the card.
     */
    Mono<CardEnrollmentDTO> getEnrollment(UUID cardId, UUID enrollmentId);

    /**
     * Update an existing enrollment for a specific card.
     */
    Mono<CardEnrollmentDTO> updateEnrollment(UUID cardId, UUID enrollmentId, CardEnrollmentDTO enrollmentDTO);

    /**
     * Delete an enrollment by its unique ID, ensuring it belongs to the card.
     */
    Mono<Void> deleteEnrollment(UUID cardId, UUID enrollmentId);
}
