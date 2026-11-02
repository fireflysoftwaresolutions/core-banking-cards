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


package io.fireflysoftware.core.banking.cards.web.controllers.issuer;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.issuer.IssuerService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.issuer.IssuerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Tag(name = "Issuers", description = "APIs for managing card issuer records")
@RestController
@RequestMapping("/api/v1/issuers")
public class IssuerController {

    @Autowired
    private IssuerService service;

    @Operation(
            summary = "List Issuers",
            description = "Retrieve a paginated list of all issuer records.\n\n" +
                    "Card issuers are financial institutions that issue payment cards to consumers and businesses. " +
                    "This endpoint returns information about all issuers registered in the system.\n\n" +
                    "The response includes issuer details such as name, code, contact information, supported card types, " +
                    "and regulatory information. Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved issuers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No issuer records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<IssuerDTO>>> getAllIssuers(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listIssuers(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Issuer",
            description = "Create a new issuer record.\n\n" +
                    "This endpoint allows adding a new card issuer to the system. Issuers are financial institutions " +
                    "that issue payment cards to consumers and businesses, and are responsible for authorizing transactions " +
                    "and settling payments.\n\n" +
                    "Required information includes the issuer name, code, and contact details. Additional information like " +
                    "supported card types, BIN ranges, and regulatory information can also be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Issuer created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IssuerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid issuer data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<IssuerDTO>> createIssuer(
            @Parameter(description = "Data for the new issuer", required = true,
                    schema = @Schema(implementation = IssuerDTO.class))
            @RequestBody IssuerDTO issuerDTO
    ) {
        return service.createIssuer(issuerDTO)
                .map(createdIssuer -> ResponseEntity.status(201).body(createdIssuer))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Issuer by ID",
            description = "Retrieve a specific issuer record by its unique identifier.\n\n" +
                    "This endpoint returns detailed information about a specific card issuer, including its " +
                    "configuration, supported card types, contact information, and regulatory details. This information " +
                    "is used when setting up card programs and processing transactions.\n\n" +
                    "The issuer ID is a unique identifier assigned to each card issuer in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the issuer",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IssuerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Issuer not found",
                    content = @Content)
    })
    @GetMapping(value = "/{issuerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<IssuerDTO>> getIssuer(
            @Parameter(description = "Unique identifier of the issuer", required = true)
            @PathVariable UUID issuerId
    ) {
        return service.getIssuer(issuerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Issuer",
            description = "Update an existing issuer record by its unique identifier.\n\n" +
                    "This endpoint allows modification of issuer attributes such as contact information, " +
                    "supported card types, and regulatory details. The core issuer identity (name and code) " +
                    "should generally remain stable.\n\n" +
                    "Updates to issuer configuration may affect card issuance and transaction processing, so changes " +
                    "should be carefully validated before being applied to production environments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Issuer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IssuerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Issuer not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid issuer data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{issuerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<IssuerDTO>> updateIssuer(
            @Parameter(description = "Unique identifier of the issuer to update", required = true)
            @PathVariable UUID issuerId,

            @Parameter(description = "Updated data for the issuer", required = true,
                    schema = @Schema(implementation = IssuerDTO.class))
            @RequestBody IssuerDTO issuerDTO
    ) {
        return service.updateIssuer(issuerId, issuerDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Issuer",
            description = "Delete an issuer record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a card issuer from the system. This operation should be used " +
                    "with extreme caution, as it may impact existing cards, programs, and transactions associated " +
                    "with this issuer.\n\n" +
                    "Before deletion, ensure that no active cards or programs are using this issuer. In most cases, " +
                    "deactivating an issuer rather than deleting it is the safer approach to maintain historical records."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Issuer deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Issuer not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{issuerId}")
    public Mono<ResponseEntity<Void>> deleteIssuer(
            @Parameter(description = "Unique identifier of the issuer to delete", required = true)
            @PathVariable UUID issuerId
    ) {
        return service.deleteIssuer(issuerId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
