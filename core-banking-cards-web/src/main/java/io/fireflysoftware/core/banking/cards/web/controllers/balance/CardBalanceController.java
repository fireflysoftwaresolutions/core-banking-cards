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


package io.fireflysoftware.core.banking.cards.web.controllers.balance;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.balance.CardBalanceService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.balance.CardBalanceDTO;
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

@Tag(name = "Card Balances", description = "APIs for managing balance records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/balances")
public class CardBalanceController {

    @Autowired
    private CardBalanceService service;

    @Operation(
            summary = "List Card Balances",
            description = "Retrieve a paginated list of balance records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card balances",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No balance records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardBalanceDTO>>> getAllBalances(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listBalances(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Balance",
            description = "Create a new balance record for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Balance record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardBalanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid balance data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardBalanceDTO>> createBalance(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card balance record", required = true,
                    schema = @Schema(implementation = CardBalanceDTO.class))
            @RequestBody CardBalanceDTO balanceDTO
    ) {
        return service.createBalance(cardId, balanceDTO)
                .map(createdBalance -> ResponseEntity.status(201).body(createdBalance))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Balance",
            description = "Retrieve a specific balance record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the balance record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardBalanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Balance record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{balanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardBalanceDTO>> getBalance(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the balance record", required = true)
            @PathVariable UUID balanceId
    ) {
        return service.getBalance(cardId, balanceId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Balance",
            description = "Update an existing balance record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardBalanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Balance record not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid balance data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{balanceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardBalanceDTO>> updateBalance(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the balance record to update", required = true)
            @PathVariable UUID balanceId,

            @Parameter(description = "Updated data for the balance record", required = true,
                    schema = @Schema(implementation = CardBalanceDTO.class))
            @RequestBody CardBalanceDTO balanceDTO
    ) {
        return service.updateBalance(cardId, balanceId, balanceDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Balance",
            description = "Delete a balance record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Balance record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Balance record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{balanceId}")
    public Mono<ResponseEntity<Void>> deleteBalance(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the balance record to delete", required = true)
            @PathVariable UUID balanceId
    ) {
        return service.deleteBalance(cardId, balanceId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
