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


package io.fireflysoftware.core.banking.cards.web.controllers.gateway;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.gateway.CardGatewayService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.gateway.CardGatewayDTO;
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
@Tag(name = "Card Gateways", description = "APIs for managing card gateway records")
@RestController
@RequestMapping("/api/v1/gateways")
public class CardGatewayController {

    @Autowired
    private CardGatewayService service;

    @Operation(
            summary = "List Card Gateways",
            description = "Retrieve a paginated list of all card gateway records.\n\n" +
                    "Card gateways are services that authorize and process card transactions between merchants and " +
                    "payment processors. This endpoint returns information about all payment gateways configured " +
                    "in the system.\n\n" +
                    "The response includes gateway details such as name, provider, supported features, integration " +
                    "endpoints, and status. Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card gateways",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No card gateway records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardGatewayDTO>>> getAllGateways(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listGateways(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Gateway",
            description = "Create a new card gateway record.\n\n" +
                    "This endpoint allows adding a new payment gateway to the system. Gateways are essential for " +
                    "routing and processing card transactions between merchants, acquirers, and card networks.\n\n" +
                    "Required information includes the gateway name, provider, integration type, and endpoint URLs. " +
                    "Additional details like authentication credentials, supported features, and configuration " +
                    "parameters can also be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card gateway created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardGatewayDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card gateway data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardGatewayDTO>> createGateway(
            @Parameter(description = "Data for the new card gateway", required = true,
                    schema = @Schema(implementation = CardGatewayDTO.class))
            @RequestBody CardGatewayDTO gatewayDTO
    ) {
        return service.createGateway(gatewayDTO)
                .map(createdGateway -> ResponseEntity.status(201).body(createdGateway))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Gateway by ID",
            description = "Retrieve a specific card gateway record by its unique identifier.\n\n" +
                    "This endpoint returns detailed information about a specific payment gateway, including its " +
                    "configuration, integration details, supported features, and status. This information is used " +
                    "when processing transactions and configuring merchant integrations.\n\n" +
                    "The gateway ID is a unique identifier assigned to each payment gateway in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card gateway",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardGatewayDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card gateway not found",
                    content = @Content)
    })
    @GetMapping(value = "/{gatewayId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardGatewayDTO>> getGateway(
            @Parameter(description = "Unique identifier of the card gateway", required = true)
            @PathVariable UUID gatewayId
    ) {
        return service.getGateway(gatewayId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Gateway",
            description = "Update an existing card gateway record by its unique identifier.\n\n" +
                    "This endpoint allows modification of gateway attributes such as endpoint URLs, authentication " +
                    "credentials, supported features, and configuration parameters. The core gateway identity " +
                    "(name and provider) should generally remain stable.\n\n" +
                    "Updates to gateway configuration may affect transaction processing, so changes should be " +
                    "carefully tested in a staging environment before being applied to production."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card gateway updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardGatewayDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card gateway not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid card gateway data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{gatewayId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardGatewayDTO>> updateGateway(
            @Parameter(description = "Unique identifier of the card gateway to update", required = true)
            @PathVariable UUID gatewayId,

            @Parameter(description = "Updated data for the card gateway", required = true,
                    schema = @Schema(implementation = CardGatewayDTO.class))
            @RequestBody CardGatewayDTO gatewayDTO
    ) {
        return service.updateGateway(gatewayId, gatewayDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Gateway",
            description = "Delete a card gateway record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a payment gateway from the system. This operation should be used " +
                    "with extreme caution, as it may impact transaction processing for merchants using this gateway.\n\n" +
                    "Before deletion, ensure that no active merchants or terminals are configured to use this gateway. " +
                    "In most cases, deactivating a gateway rather than deleting it is the safer approach."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card gateway deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card gateway not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{gatewayId}")
    public Mono<ResponseEntity<Void>> deleteGateway(
            @Parameter(description = "Unique identifier of the card gateway to delete", required = true)
            @PathVariable UUID gatewayId
    ) {
        return service.deleteGateway(gatewayId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
