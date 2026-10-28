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


package io.fireflysoftware.core.banking.cards.web.controllers.limit;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.limit.CardLimitService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.limit.CardLimitDTO;
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
@Tag(name = "Card Limits", description = "APIs for managing limit records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/limits")
public class CardLimitController {

    @Autowired
    private CardLimitService service;

    @Operation(
            summary = "List Card Limits",
            description = "Retrieve a paginated list of limit records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card limits",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No limit records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardLimitDTO>>> getAllLimits(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listLimits(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Limit",
            description = "Create a new limit record for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card limit created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardLimitDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid limit data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardLimitDTO>> createLimit(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card limit record", required = true,
                    schema = @Schema(implementation = CardLimitDTO.class))
            @RequestBody CardLimitDTO limitDTO
    ) {
        return service.createLimit(cardId, limitDTO)
                .map(createdLimit -> ResponseEntity.status(201).body(createdLimit))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Limit by ID",
            description = "Retrieve a specific card limit record by its unique identifier, ensuring it belongs to the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card limit",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardLimitDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card limit not found",
                    content = @Content)
    })
    @GetMapping(value = "/{limitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardLimitDTO>> getLimit(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the card limit record", required = true)
            @PathVariable UUID limitId
    ) {
        return service.getLimit(cardId, limitId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Limit",
            description = "Update an existing limit record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card limit updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardLimitDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card limit record not found",
                    content = @Content)
    })
    @PutMapping(value = "/{limitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardLimitDTO>> updateLimit(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the card limit record to update", required = true)
            @PathVariable UUID limitId,

            @Parameter(description = "Updated card limit data", required = true,
                    schema = @Schema(implementation = CardLimitDTO.class))
            @RequestBody CardLimitDTO limitDTO
    ) {
        return service.updateLimit(cardId, limitId, limitDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Limit",
            description = "Remove an existing limit record from a card by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card limit record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card limit record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{limitId}")
    public Mono<ResponseEntity<Void>> deleteLimit(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the card limit record to delete", required = true)
            @PathVariable UUID limitId
    ) {
        return service.deleteLimit(cardId, limitId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}