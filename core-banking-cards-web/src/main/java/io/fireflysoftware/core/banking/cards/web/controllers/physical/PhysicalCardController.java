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


package io.fireflysoftware.core.banking.cards.web.controllers.physical;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.physical.PhysicalCardService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.physical.PhysicalCardDTO;
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
@Tag(name = "Physical Cards", description = "APIs for managing physical card records linked to a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/physical-cards")
public class PhysicalCardController {

    @Autowired
    private PhysicalCardService service;

    @Operation(
            summary = "List Physical Cards",
            description = "Retrieve a paginated list of all physical card records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the physical cards",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No physical cards found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<PhysicalCardDTO>>> getAllPhysicalCards(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listPhysicalCards(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Physical Card",
            description = "Create a new physical card record for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Physical card created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhysicalCardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid physical card data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PhysicalCardDTO>> createPhysicalCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new physical card record", required = true,
                    schema = @Schema(implementation = PhysicalCardDTO.class))
            @RequestBody PhysicalCardDTO physicalCardDTO
    ) {
        return service.createPhysicalCard(cardId, physicalCardDTO)
                .map(createdCard -> ResponseEntity.status(201).body(createdCard))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Physical Card by ID",
            description = "Retrieve a specific physical card record by its unique identifier, ensuring it belongs to the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the physical card",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhysicalCardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Physical card record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{physicalCardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PhysicalCardDTO>> getPhysicalCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the physical card record", required = true)
            @PathVariable UUID physicalCardId
    ) {
        return service.getPhysicalCard(cardId, physicalCardId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Physical Card",
            description = "Update an existing physical card record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Physical card updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhysicalCardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Physical card record not found",
                    content = @Content)
    })
    @PutMapping(value = "/{physicalCardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PhysicalCardDTO>> updatePhysicalCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the physical card record to update", required = true)
            @PathVariable UUID physicalCardId,

            @Parameter(description = "Updated data for the physical card record", required = true,
                    schema = @Schema(implementation = PhysicalCardDTO.class))
            @RequestBody PhysicalCardDTO physicalCardDTO
    ) {
        return service.updatePhysicalCard(cardId, physicalCardId, physicalCardDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Physical Card",
            description = "Remove an existing physical card record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Physical card record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Physical card record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{physicalCardId}")
    public Mono<ResponseEntity<Void>> deletePhysicalCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the physical card record to delete", required = true)
            @PathVariable UUID physicalCardId
    ) {
        return service.deletePhysicalCard(cardId, physicalCardId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}