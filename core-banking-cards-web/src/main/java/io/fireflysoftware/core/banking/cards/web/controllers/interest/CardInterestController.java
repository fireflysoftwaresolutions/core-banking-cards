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


package io.fireflysoftware.core.banking.cards.web.controllers.interest;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.interest.CardInterestService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.interest.CardInterestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Tag(name = "Card Interests", description = "APIs for managing interest records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/interests")
public class CardInterestController {

    @Autowired
    private CardInterestService service;

    @Operation(
            summary = "List Card Interests",
            description = "Retrieve a paginated list of interest records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card interests",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No interest records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardInterestDTO>>> getAllInterests(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listInterests(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Interest",
            description = "Create a new interest record for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Interest record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid interest data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardInterestDTO>> createInterest(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card interest record", required = true,
                    schema = @Schema(implementation = CardInterestDTO.class))
            @RequestBody CardInterestDTO interestDTO
    ) {
        return service.createInterest(cardId, interestDTO)
                .map(createdInterest -> ResponseEntity.status(201).body(createdInterest))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Interest",
            description = "Retrieve a specific interest record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the interest record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Interest record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{interestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardInterestDTO>> getInterest(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the interest record", required = true)
            @PathVariable UUID interestId
    ) {
        return service.getInterest(cardId, interestId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Interest",
            description = "Update an existing interest record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interest record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Interest record not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid interest data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{interestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardInterestDTO>> updateInterest(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the interest record to update", required = true)
            @PathVariable UUID interestId,

            @Parameter(description = "Updated data for the interest record", required = true,
                    schema = @Schema(implementation = CardInterestDTO.class))
            @RequestBody CardInterestDTO interestDTO
    ) {
        return service.updateInterest(cardId, interestId, interestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Interest",
            description = "Delete an interest record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Interest record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Interest record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{interestId}")
    public Mono<ResponseEntity<Void>> deleteInterest(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the interest record to delete", required = true)
            @PathVariable UUID interestId
    ) {
        return service.deleteInterest(cardId, interestId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
