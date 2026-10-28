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


package io.fireflysoftware.core.banking.cards.web.controllers.terminal;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.terminal.CardTerminalService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.terminal.CardTerminalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Tag(name = "Card Terminals", description = "APIs for managing card terminal records")
@RestController
@RequestMapping("/api/v1/terminals")
public class CardTerminalController {

    @Autowired
    private CardTerminalService service;

    @Operation(
            summary = "List Card Terminals",
            description = "Retrieve a paginated list of all card terminal records.\n\n" +
                    "Card terminals are physical devices used by merchants to process card payments. This endpoint returns " +
                    "information about all terminals registered in the system, including their type, model, location, " +
                    "and capabilities.\n\n" +
                    "The response includes terminal details such as serial number, manufacturer, supported features, " +
                    "and associated merchant information. Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card terminals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No card terminal records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardTerminalDTO>>> getAllTerminals(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listTerminals(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Terminal",
            description = "Create a new card terminal record.\n\n" +
                    "This endpoint allows registering a new payment terminal in the system. Terminals are the physical " +
                    "or virtual devices that merchants use to accept card payments from parties.\n\n" +
                    "Required information includes the terminal reference, serial number, type, model, and associated " +
                    "merchant. Additional details like location, capabilities (contactless, chip, magstripe), and " +
                    "processor information can also be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card terminal created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardTerminalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card terminal data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardTerminalDTO>> createTerminal(
            @Parameter(description = "Data for the new card terminal", required = true,
                    schema = @Schema(implementation = CardTerminalDTO.class))
            @RequestBody CardTerminalDTO terminalDTO
    ) {
        return service.createTerminal(terminalDTO)
                .map(createdTerminal -> ResponseEntity.status(201).body(createdTerminal))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Terminal by ID",
            description = "Retrieve a specific card terminal record by its unique identifier.\n\n" +
                    "This endpoint returns detailed information about a specific payment terminal, including its " +
                    "configuration, capabilities, location, and status. This information is useful for terminal " +
                    "management, troubleshooting, and transaction analysis.\n\n" +
                    "The terminal ID is a unique identifier assigned to each terminal in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card terminal",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardTerminalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card terminal not found",
                    content = @Content)
    })
    @GetMapping(value = "/{terminalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardTerminalDTO>> getTerminal(
            @Parameter(description = "Unique identifier of the card terminal", required = true)
            @PathVariable UUID terminalId
    ) {
        return service.getTerminal(terminalId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Terminal",
            description = "Update an existing card terminal record by its unique identifier.\n\n" +
                    "This endpoint allows modification of terminal attributes such as location, status, capabilities, " +
                    "and associated merchant or processor. The core terminal identity (reference and serial number) " +
                    "should generally remain stable.\n\n" +
                    "Updates to terminal configuration may affect transaction processing, so changes should be " +
                    "carefully validated. Terminal software and firmware versions can also be updated through this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card terminal updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardTerminalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card terminal not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid card terminal data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{terminalId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardTerminalDTO>> updateTerminal(
            @Parameter(description = "Unique identifier of the card terminal to update", required = true)
            @PathVariable UUID terminalId,

            @Parameter(description = "Updated data for the card terminal", required = true,
                    schema = @Schema(implementation = CardTerminalDTO.class))
            @RequestBody CardTerminalDTO terminalDTO
    ) {
        return service.updateTerminal(terminalId, terminalDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Terminal",
            description = "Delete a card terminal record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a terminal from the system. This operation should be used " +
                    "with caution, as it may impact transaction history and reporting.\n\n" +
                    "Before deletion, ensure that the terminal is no longer in use and has been properly decommissioned. " +
                    "In many cases, deactivating a terminal rather than deleting it is the preferred approach to " +
                    "maintain historical records."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card terminal deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card terminal not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{terminalId}")
    public Mono<ResponseEntity<Void>> deleteTerminal(
            @Parameter(description = "Unique identifier of the card terminal to delete", required = true)
            @PathVariable UUID terminalId
    ) {
        return service.deleteTerminal(terminalId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
