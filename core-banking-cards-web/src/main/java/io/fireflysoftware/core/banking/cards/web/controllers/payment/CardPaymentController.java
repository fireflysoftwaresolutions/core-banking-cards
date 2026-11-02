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


package io.fireflysoftware.core.banking.cards.web.controllers.payment;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.payment.CardPaymentService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.payment.CardPaymentDTO;
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
@Tag(name = "Card Payments", description = "APIs for managing payment records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/payments")
public class CardPaymentController {

    @Autowired
    private CardPaymentService service;

    @Operation(
            summary = "List Card Payments",
            description = "Retrieve a paginated list of payment records associated with the specified card.\n\n" +
                    "Card payments represent funds transferred to the card account, such as bill payments, " +
                    "deposits, or transfers from other accounts. This endpoint returns all payment records " +
                    "for a specific card, allowing tracking of incoming funds.\n\n" +
                    "The response includes payment details such as amount, date, source, reference number, " +
                    "and status. Results can be filtered and sorted using the pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card payments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No payment records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardPaymentDTO>>> getAllPayments(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listPayments(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Payment",
            description = "Create a new payment record for the specified card.\n\n" +
                    "This endpoint allows recording a payment made to a card account. Payments typically " +
                    "represent incoming funds that increase the available balance on the card.\n\n" +
                    "Required information includes the payment amount, date, and source. Additional details " +
                    "like reference numbers, descriptions, and payment methods can also be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPaymentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardPaymentDTO>> createPayment(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card payment record", required = true,
                    schema = @Schema(implementation = CardPaymentDTO.class))
            @RequestBody CardPaymentDTO paymentDTO
    ) {
        return service.createPayment(cardId, paymentDTO)
                .map(createdPayment -> ResponseEntity.status(201).body(createdPayment))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Payment",
            description = "Retrieve a specific payment record by its unique identifier.\n\n" +
                    "This endpoint returns detailed information about a specific payment made to a card account. " +
                    "This information is useful for reconciliation, dispute resolution, and financial reporting.\n\n" +
                    "The payment ID is a unique identifier assigned to each payment record in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the payment record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPaymentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardPaymentDTO>> getPayment(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the payment record", required = true)
            @PathVariable UUID paymentId
    ) {
        return service.getPayment(cardId, paymentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Payment",
            description = "Update an existing payment record associated with the specified card.\n\n" +
                    "This endpoint allows modification of payment attributes such as description, reference numbers, " +
                    "and status. Core payment details like amount and date should generally only be modified to " +
                    "correct errors, as they may affect financial reporting and reconciliation.\n\n" +
                    "Updates to payment records should be carefully audited and may require special permissions " +
                    "depending on the system's security configuration."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPaymentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment record not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid payment data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardPaymentDTO>> updatePayment(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the payment record to update", required = true)
            @PathVariable UUID paymentId,

            @Parameter(description = "Updated data for the payment record", required = true,
                    schema = @Schema(implementation = CardPaymentDTO.class))
            @RequestBody CardPaymentDTO paymentDTO
    ) {
        return service.updatePayment(cardId, paymentId, paymentDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Payment",
            description = "Delete a payment record by its unique identifier.\n\n" +
                    "This endpoint permanently removes a payment record from the system. This operation should be used " +
                    "with extreme caution, as it may impact financial reporting, reconciliation, and audit trails.\n\n" +
                    "In most production environments, payments should be reversed or voided rather than deleted to " +
                    "maintain a complete financial history. Deletion may require special permissions and should be " +
                    "limited to correcting test data or specific regulatory requirements."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{paymentId}")
    public Mono<ResponseEntity<Void>> deletePayment(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the payment record to delete", required = true)
            @PathVariable UUID paymentId
    ) {
        return service.deletePayment(cardId, paymentId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
