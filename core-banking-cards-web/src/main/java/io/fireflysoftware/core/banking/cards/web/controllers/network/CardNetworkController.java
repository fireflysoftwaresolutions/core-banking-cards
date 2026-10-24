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


package io.fireflysoftware.core.banking.cards.web.controllers.network;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.network.CardNetworkService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.network.CardNetworkDTO;
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
@Tag(name = "Card Networks", description = "APIs for managing card network records (Visa, Mastercard, etc.)")
@RestController
@RequestMapping("/api/v1/networks")
public class CardNetworkController {

    @Autowired
    private CardNetworkService service;

    @Operation(
            summary = "List Card Networks",
            description = "Retrieve a paginated list of all card network records.\n\n" +
                    "Card networks are payment processing organizations (such as Visa, Mastercard, American Express) " +
                    "that facilitate transactions between merchants, acquirers, and card issuers. This endpoint returns " +
                    "information about all supported card networks in the system.\n\n" +
                    "The response includes network details such as name, code, region coverage, and supported features. " +
                    "Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card networks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No card network records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardNetworkDTO>>> getAllNetworks(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listNetworks(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Network",
            description = "Create a new card network record.\n\n" +
                    "This endpoint allows adding a new payment network to the system. Card networks are essential " +
                    "for transaction routing and processing. Each network has its own set of rules, fees, and " +
                    "processing capabilities.\n\n" +
                    "Required information includes the network name, code, and region coverage. Additional " +
                    "details like support contact information and API endpoints can also be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card network created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardNetworkDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card network data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardNetworkDTO>> createNetwork(
            @Parameter(description = "Data for the new card network", required = true,
                    schema = @Schema(implementation = CardNetworkDTO.class))
            @RequestBody CardNetworkDTO networkDTO
    ) {
        return service.createNetwork(networkDTO)
                .map(createdNetwork -> ResponseEntity.status(201).body(createdNetwork))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Network by ID",
            description = "Retrieve a specific card network record by its unique identifier.\n\n" +
                    "This endpoint returns detailed information about a specific card network, including its " +
                    "configuration, supported features, and integration details. This information is used when " +
                    "setting up card programs and processing transactions.\n\n" +
                    "The network ID is a unique identifier assigned to each card network in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card network",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardNetworkDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card network not found",
                    content = @Content)
    })
    @GetMapping(value = "/{networkId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardNetworkDTO>> getNetwork(
            @Parameter(description = "Unique identifier of the card network", required = true)
            @PathVariable UUID networkId
    ) {
        return service.getNetwork(networkId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Network",
            description = "Update an existing card network record by its unique identifier.\n\n" +
                    "This endpoint allows modification of card network attributes such as contact information, " +
                    "API endpoints, and supported features. The core network identity (name and code) should " +
                    "generally remain stable as they are used in many related records.\n\n" +
                    "Updates to network configuration may affect transaction processing, so changes should be " +
                    "carefully tested before being applied to production environments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card network updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardNetworkDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card network not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid card network data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{networkId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardNetworkDTO>> updateNetwork(
            @Parameter(description = "Unique identifier of the card network to update", required = true)
            @PathVariable UUID networkId,

            @Parameter(description = "Updated data for the card network", required = true,
                    schema = @Schema(implementation = CardNetworkDTO.class))
            @RequestBody CardNetworkDTO networkDTO
    ) {
        return service.updateNetwork(networkId, networkDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Network",
            description = "Delete a card network record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a card network from the system. This operation should be used " +
                    "with extreme caution, as it may impact existing cards, programs, and transactions associated " +
                    "with this network.\n\n" +
                    "Before deletion, ensure that no active cards or programs are using this network. In most cases, " +
                    "deactivating a network rather than deleting it is the safer approach."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card network deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card network not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{networkId}")
    public Mono<ResponseEntity<Void>> deleteNetwork(
            @Parameter(description = "Unique identifier of the card network to delete", required = true)
            @PathVariable UUID networkId
    ) {
        return service.deleteNetwork(networkId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
