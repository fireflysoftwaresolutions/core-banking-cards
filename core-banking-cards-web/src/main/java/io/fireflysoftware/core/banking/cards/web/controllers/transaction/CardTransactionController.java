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


package io.fireflysoftware.core.banking.cards.web.controllers.transaction;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.transaction.CardTransactionService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.transaction.CardTransactionDTO;
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
@Tag(name = "Card Transactions", description = "APIs for managing transaction records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/transactions")
public class CardTransactionController {

    @Autowired
    private CardTransactionService service;

    @Operation(
            summary = "List Card Transactions",
            description = "Retrieve a paginated list of all transaction records associated with the specified card.\n\n" +
                    "Card transactions represent purchases, withdrawals, refunds, and other financial activities " +
                    "performed with the card. This endpoint returns a comprehensive transaction history for a " +
                    "specific card.\n\n" +
                    "The response includes transaction details such as amount, date, merchant information, " +
                    "authorization code, and status. Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card transactions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No transaction records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardTransactionDTO>>> getAllTransactions(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listTransactions(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Filter Card Transactions",
            description = "Apply custom filters to retrieve a list of transactions for the specified card.\n\n" +
                    "This endpoint provides advanced filtering capabilities for card transactions, allowing clients " +
                    "to search for specific transactions based on criteria such as date range, amount range, " +
                    "merchant name, transaction type, and status.\n\n" +
                    "The filter request can include multiple conditions that are combined using logical AND/OR operations. " +
                    "Results can be sorted and paginated for efficient data retrieval."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered card transactions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No filtered results found for the specified card",
                    content = @Content)
    })
    @GetMapping(params = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardTransactionDTO>>> filterProducts(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,
            @RequestBody FilterRequest<CardTransactionDTO> filterRequest
    ) {
        filterRequest.getFilters().setCardId(cardId);
        return service.findFiltered(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Transaction",
            description = "Create a new transaction record and associate it with the specified card.\n\n" +
                    "This endpoint allows recording a new transaction for a card. Transactions typically represent " +
                    "purchases, withdrawals, refunds, or other financial activities performed with the card.\n\n" +
                    "Required information includes the transaction amount, date, merchant details, and transaction type. " +
                    "Additional details like authorization codes, reference numbers, and location data can also be provided.\n\n" +
                    "Note: This endpoint records transactions that have already been authorized and executed. It does not " +
                    "perform real-time transaction authorization, which is handled by a separate microservice."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card transaction created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardTransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardTransactionDTO>> createTransaction(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card transaction record", required = true,
                    schema = @Schema(implementation = CardTransactionDTO.class))
            @RequestBody CardTransactionDTO transactionDTO
    ) {
        return service.createTransaction(cardId, transactionDTO)
                .map(createdTransaction -> ResponseEntity.status(201).body(createdTransaction))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Transaction by ID",
            description = "Retrieve a specific transaction record by its unique identifier, ensuring it belongs to the specified card.\n\n" +
                    "This endpoint returns detailed information about a specific card transaction. This information is useful " +
                    "for transaction verification, dispute resolution, and financial reporting.\n\n" +
                    "The transaction ID is a unique identifier assigned to each transaction record in the system. The endpoint " +
                    "verifies that the requested transaction belongs to the specified card before returning the data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card transaction",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardTransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card transaction not found",
                    content = @Content)
    })
    @GetMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardTransactionDTO>> getTransaction(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the transaction record", required = true)
            @PathVariable UUID transactionId
    ) {
        return service.getTransaction(cardId, transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Transaction",
            description = "Update an existing transaction record associated with the specified card.\n\n" +
                    "This endpoint allows modification of transaction attributes such as description, category, " +
                    "and status. Core transaction details like amount, date, and merchant information should generally " +
                    "only be modified to correct errors, as they may affect financial reporting and reconciliation.\n\n" +
                    "Updates to transaction records should be carefully audited and may require special permissions " +
                    "depending on the system's security configuration."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card transaction updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardTransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card transaction record not found",
                    content = @Content)
    })
    @PutMapping(value = "/{transactionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardTransactionDTO>> updateTransaction(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the card transaction to update", required = true)
            @PathVariable UUID transactionId,

            @Parameter(description = "Updated transaction data", required = true,
                    schema = @Schema(implementation = CardTransactionDTO.class))
            @RequestBody CardTransactionDTO transactionDTO
    ) {
        return service.updateTransaction(cardId, transactionId, transactionDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Transaction",
            description = "Remove an existing transaction record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a transaction record from the system. This operation should be used " +
                    "with extreme caution, as it may impact financial reporting, reconciliation, and audit trails.\n\n" +
                    "In most production environments, transactions should be reversed or voided rather than deleted to " +
                    "maintain a complete financial history. Deletion may require special permissions and should be " +
                    "limited to correcting test data or specific regulatory requirements."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card transaction record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card transaction record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{transactionId}")
    public Mono<ResponseEntity<Void>> deleteTransaction(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the transaction record to delete", required = true)
            @PathVariable UUID transactionId
    ) {
        return service.deleteTransaction(cardId, transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}