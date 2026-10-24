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


package io.fireflysoftware.core.banking.cards.web.controllers.acquirer;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.acquirer.CardAcquirerService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.acquirer.CardAcquirerDTO;
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
@Tag(name = "Card Acquirers", description = "APIs for managing card acquirer records")
@RestController
@RequestMapping("/api/v1/acquirers")
public class CardAcquirerController {

    @Autowired
    private CardAcquirerService service;

    @Operation(
            summary = "List Card Acquirers",
            description = "Retrieve a paginated list of all card acquirer records.\n\n" +
                    "Card acquirers are financial institutions that process card payments on behalf of merchants. " +
                    "This endpoint returns information about all acquirers registered in the system.\n\n" +
                    "The response includes acquirer details such as name, code, region coverage, supported card networks, " +
                    "and integration information. Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card acquirers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No card acquirer records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardAcquirerDTO>>> getAllAcquirers(
            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listAcquirers(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Acquirer",
            description = "Create a new card acquirer record.\n\n" +
                    "This endpoint allows adding a new card acquirer to the system. Acquirers are financial institutions " +
                    "that maintain merchant accounts and process card transactions on behalf of merchants.\n\n" +
                    "Required information includes the acquirer name, code, and region coverage. Additional details like " +
                    "supported card networks, settlement information, and integration parameters can also be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card acquirer created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardAcquirerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card acquirer data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardAcquirerDTO>> createAcquirer(
            @Parameter(description = "Data for the new card acquirer", required = true,
                    schema = @Schema(implementation = CardAcquirerDTO.class))
            @RequestBody CardAcquirerDTO acquirerDTO
    ) {
        return service.createAcquirer(acquirerDTO)
                .map(createdAcquirer -> ResponseEntity.status(201).body(createdAcquirer))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Acquirer by ID",
            description = "Retrieve a specific card acquirer record by its unique identifier.\n\n" +
                    "This endpoint returns detailed information about a specific card acquirer, including its " +
                    "configuration, supported card networks, integration details, and settlement information. " +
                    "This information is used when configuring merchant accounts and processing transactions.\n\n" +
                    "The acquirer ID is a unique identifier assigned to each card acquirer in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card acquirer",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardAcquirerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card acquirer not found",
                    content = @Content)
    })
    @GetMapping(value = "/{acquirerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardAcquirerDTO>> getAcquirer(
            @Parameter(description = "Unique identifier of the card acquirer", required = true)
            @PathVariable UUID acquirerId
    ) {
        return service.getAcquirer(acquirerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Acquirer",
            description = "Update an existing card acquirer record by its unique identifier.\n\n" +
                    "This endpoint allows modification of acquirer attributes such as contact information, " +
                    "supported card networks, settlement details, and integration parameters. The core acquirer " +
                    "identity (name and code) should generally remain stable.\n\n" +
                    "Updates to acquirer configuration may affect transaction processing and settlement, so changes " +
                    "should be carefully tested before being applied to production environments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card acquirer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardAcquirerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card acquirer not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid card acquirer data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{acquirerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardAcquirerDTO>> updateAcquirer(
            @Parameter(description = "Unique identifier of the card acquirer to update", required = true)
            @PathVariable UUID acquirerId,

            @Parameter(description = "Updated data for the card acquirer", required = true,
                    schema = @Schema(implementation = CardAcquirerDTO.class))
            @RequestBody CardAcquirerDTO acquirerDTO
    ) {
        return service.updateAcquirer(acquirerId, acquirerDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Acquirer",
            description = "Delete a card acquirer record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a card acquirer from the system. This operation should be used " +
                    "with extreme caution, as it may impact transaction processing and settlement for merchants " +
                    "associated with this acquirer.\n\n" +
                    "Before deletion, ensure that no active merchants are using this acquirer. In most cases, " +
                    "deactivating an acquirer rather than deleting it is the safer approach to maintain historical records."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card acquirer deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card acquirer not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{acquirerId}")
    public Mono<ResponseEntity<Void>> deleteAcquirer(
            @Parameter(description = "Unique identifier of the card acquirer to delete", required = true)
            @PathVariable UUID acquirerId
    ) {
        return service.deleteAcquirer(acquirerId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
