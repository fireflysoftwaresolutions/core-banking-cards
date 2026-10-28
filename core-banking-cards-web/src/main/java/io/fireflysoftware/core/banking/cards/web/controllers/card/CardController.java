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


package io.fireflysoftware.core.banking.cards.web.controllers.card;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.idempotency.annotation.DisableIdempotency;
import io.fireflysoftware.core.banking.cards.core.services.card.CardService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardDTO;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationRequest;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.card.CardActivationReceipt;
import io.fireflysoftware.core.banking.cards.core.services.card.activation.CardActivationService;
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
@Tag(name = "Cards", description = "APIs for managing card records within the banking system")
@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    @Autowired
    private CardService service;

    @Autowired
    private CardActivationService activationService;

    @Operation(summary = "Verify a card activation action",
            description = "Consumes issued possession proof only after core independently verifies the bound issuer operation. "
                    + "Requires authenticated current ownership and a configured issuer adapter. Pending or uncertain receipts do not activate a card.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activation confirmed",
                    content = @Content(schema = @Schema(implementation = CardActivationReceipt.class))),
            @ApiResponse(responseCode = "202", description = "Activation observation is pending or uncertain",
                    content = @Content(schema = @Schema(implementation = CardActivationReceipt.class))),
            @ApiResponse(responseCode = "400", description = "Invalid proof request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Activation is not authorized", content = @Content),
            @ApiResponse(responseCode = "409", description = "Action conflict or issuer reconciliation required", content = @Content),
            @ApiResponse(responseCode = "422", description = "Issuer rejected the action",
                    content = @Content(schema = @Schema(implementation = CardActivationReceipt.class))),
            @ApiResponse(responseCode = "503", description = "Required activation integration is unavailable", content = @Content)
    })
    @DisableIdempotency
    @PostMapping(value = "/{cardId}/activation", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardActivationReceipt>> activateCard(
            @PathVariable UUID cardId,
            @RequestHeader("X-Idempotency-Key") UUID actionId,
            @Valid @RequestBody CardActivationRequest request) {
        return activationService.activate(cardId, actionId, request)
                .map(receipt -> ResponseEntity.status(switch (receipt.state()) {
                    case CONFIRMED -> 200;
                    case REJECTED -> 422;
                    case RECONCILIATION_REQUIRED -> 409;
                    case RESERVED, UNCERTAIN -> 202;
                }).body(receipt));
    }

    @Operation(
            summary = "Filter Cards",
            description = "Retrieve a paginated list of all bank cards based on filter criteria."
    )
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardDTO>>> filterCards(
            @Valid @RequestBody FilterRequest<CardDTO> filterRequest
    ) {
        return service.filterCards(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card",
            description = "Create a new card record in the banking system.\n\n" +
                    "## Description\n" +
                    "This endpoint allows the creation of new payment cards. Cards are the central entity in the system " +
                    "and are associated with specific card programs, issuers, networks, and account holders. " +
                    "New records are inactive locked drafts. ACTIVE status and activation evidence require a verified lifecycle action. " +
                    "The created card can be either physical, virtual, or both, depending on the configuration.\n\n" +
                    "## Required Fields\n" +
                    "* `cardTypeId` - Type of card (credit, debit, prepaid)\n" +
                    "* `cardNetworkId` - Card network (Visa, Mastercard, etc.)\n" +
                    "* `issuerId` - Financial institution issuing the card\n" +
                    "* `binId` - Bank Identification Number\n" +
                    "* `partyId` - Account holder identifier\n" +
                    "* `cardHolderName` - Name to appear on the card\n\n" +
                    "## Optional Fields\n" +
                    "* `isPhysical` - Whether to issue a physical card\n" +
                    "* `isVirtual` - Whether to issue a virtual card\n" +
                    "* `designId` - Card design template\n" +
                    "* `dailyLimit` - Maximum daily transaction amount\n" +
                    "* `monthlyLimit` - Maximum monthly transaction amount\n" +
                    "* `creditLimit` - Credit limit (for credit cards)\n\n" +
                    "## Security Considerations\n" +
                    "Card record creation does not provision processor credentials. PIN and CVV inputs are rejected " +
                    "and are omitted from ordinary responses. Existing records containing these credentials require " +
                    "controlled remediation before ordinary updates. This legacy record API still handles full card " +
                    "numbers; processor tokenization and protected card-data display require a separate integration."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardDTO>> createCard(
            @Parameter(description = "Data for the new card", required = true,
                    schema = @Schema(implementation = CardDTO.class))
            @Valid @RequestBody CardDTO cardDTO
    ) {
        return service.createCard(cardDTO)
                .map(createdCard -> ResponseEntity.status(201).body(createdCard))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card by ID",
            description = "Retrieve an existing card record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint returns comprehensive information about a card, including its status, " +
                    "associated accounts, limits, and key attributes. It provides all the details needed for " +
                    "card management and transaction processing.\n\n" +
                    "## Response Details\n" +
                    "The response includes:\n" +
                    "* Basic card information (masked number, expiration date, status)\n" +
                    "* Associated entities (issuer, network, program)\n" +
                    "* Account details (account ID, party ID)\n" +
                    "* Card limits and restrictions\n" +
                    "* Card features and capabilities\n\n" +
                    "## Security Considerations\n" +
                    "PIN and CVV are omitted from ordinary responses, including historical records. " +
                    "The legacy response still includes the full card number when stored; the masked-number field " +
                    "does not itself suppress that property. Processor tokenization and protected display are " +
                    "separate integration requirements.\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving card details for display in party portals\n" +
                    "* Checking card status before processing transactions\n" +
                    "* Verifying card limits and restrictions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content)
    })
    @GetMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardDTO>> getCard(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId
    ) {
        return service.getCard(cardId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card",
            description = "Update an existing card record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint allows modification of card attributes such as status, limits, and expiration details. " +
                    "It enables financial institutions to manage card lifecycle events and adjust card parameters " +
                    "based on party needs or risk assessments.\n\n" +
                    "## Updatable Fields\n" +
                    "* `cardStatus` - Restriction or terminal status; activation and unblocking require verified lifecycle actions\n" +
                    "* `cardHolderName` - Name displayed on the card\n" +
                    "* `dailyLimit` - Maximum daily transaction amount\n" +
                    "* `monthlyLimit` - Maximum monthly transaction amount\n" +
                    "* `creditLimit` - Credit limit (for credit cards)\n" +
                    "* `expirationDate` - Card expiration date (for extensions)\n" +
                    "* `isActive` - Whether the card is active\n" +
                    "* `isLocked` - Whether the card is temporarily locked\n\n" +
                    "## Immutable Fields\n" +
                    "Note that certain sensitive fields and core attributes cannot be modified after card creation:\n" +
                    "* Card number\n" +
                    "* BIN\n" +
                    "* Card type\n" +
                    "* Card network\n" +
                    "* Issuer\n\n" +
                    "For changes to these immutable attributes, a new card must be issued.\n\n" +
                    "## Common Use Cases\n" +
                    "* Applying a restriction to an existing card\n" +
                    "* Blocking a card due to suspected fraud\n" +
                    "* Adjusting card limits based on party request\n" +
                    "* Updating cardholder information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content)
    })
    @PutMapping(value = "/{cardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardDTO>> updateCard(
            @Parameter(description = "Unique identifier of the card to update", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Updated card data", required = true,
                    schema = @Schema(implementation = CardDTO.class))
            @RequestBody CardDTO cardDTO
    ) {
        return service.updateCard(cardId, cardDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card",
            description = "Remove an existing card record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint permanently removes a card from the system. It completely erases the card record " +
                    "and all its associated data from the database.\n\n" +
                    "## Warning\n" +
                    "⚠️ **This operation should be used with extreme caution!**\n\n" +
                    "Deleting a card has several important implications:\n" +
                    "* All transaction history associated with the card will be orphaned\n" +
                    "* Audit trails will be incomplete\n" +
                    "* Regulatory compliance may be compromised\n" +
                    "* Customer service inquiries about the card cannot be addressed\n\n" +
                    "## Recommended Alternative\n" +
                    "In most production scenarios, cards should be **deactivated** or **blocked** rather than deleted. " +
                    "This preserves the card record and its history while preventing any new transactions.\n\n" +
                    "## Appropriate Use Cases\n" +
                    "Card deletion should be limited to:\n" +
                    "* Test cards in non-production environments\n" +
                    "* Cards created in error that have never been activated\n" +
                    "* Specific regulatory requirements that mandate complete removal\n\n" +
                    "## Required Permissions\n" +
                    "This operation typically requires elevated administrative privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{cardId}")
    public Mono<ResponseEntity<Void>> deleteCard(
            @Parameter(description = "Unique identifier of the card to delete", required = true)
            @PathVariable UUID cardId
    ) {
        return service.deleteCard(cardId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
