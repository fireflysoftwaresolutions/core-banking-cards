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


package io.fireflysoftware.core.banking.cards.web.controllers.virtual;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.virtual.VirtualCardService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.virtual.VirtualCardDTO;
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
@Tag(name = "Virtual Cards", description = "APIs for managing virtual card records linked to a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/virtual-cards")
public class VirtualCardController {

    @Autowired
    private VirtualCardService service;

    @Operation(
            summary = "List Virtual Cards",
            description = "Retrieve a paginated list of all virtual card records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the virtual card records",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No virtual cards found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<VirtualCardDTO>>> getAllVirtualCards(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listVirtualCards(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Virtual Card",
            description = "Create a new virtual card record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Virtual card created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VirtualCardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid virtual card data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<VirtualCardDTO>> createVirtualCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new virtual card record", required = true,
                    schema = @Schema(implementation = VirtualCardDTO.class))
            @RequestBody VirtualCardDTO virtualCardDTO
    ) {
        return service.createVirtualCard(cardId, virtualCardDTO)
                .map(createdCard -> ResponseEntity.status(201).body(createdCard))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Virtual Card by ID",
            description = "Retrieve a specific virtual card record by its unique identifier, ensuring it belongs to the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the virtual card record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VirtualCardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Virtual card record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{virtualCardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<VirtualCardDTO>> getVirtualCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the virtual card record", required = true)
            @PathVariable UUID virtualCardId
    ) {
        return service.getVirtualCard(cardId, virtualCardId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Virtual Card",
            description = "Update an existing virtual card record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Virtual card updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VirtualCardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Virtual card record not found",
                    content = @Content)
    })
    @PutMapping(value = "/{virtualCardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<VirtualCardDTO>> updateVirtualCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the virtual card record to update", required = true)
            @PathVariable UUID virtualCardId,

            @Parameter(description = "Updated virtual card data", required = true,
                    schema = @Schema(implementation = VirtualCardDTO.class))
            @RequestBody VirtualCardDTO virtualCardDTO
    ) {
        return service.updateVirtualCard(cardId, virtualCardId, virtualCardDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Virtual Card",
            description = "Remove an existing virtual card record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Virtual card record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Virtual card record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{virtualCardId}")
    public Mono<ResponseEntity<Void>> deleteVirtualCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the virtual card record to delete", required = true)
            @PathVariable UUID virtualCardId
    ) {
        return service.deleteVirtualCard(cardId, virtualCardId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}