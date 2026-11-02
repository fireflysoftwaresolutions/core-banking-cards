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


package io.fireflysoftware.core.banking.cards.web.controllers.bin;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.bin.BINService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.bin.BINDTO;
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
@Tag(name = "BINs", description = "APIs for managing Bank Identification Number (BIN) records")
@RestController
@RequestMapping("/api/v1/bins")
public class BINController {

    @Autowired
    private BINService service;

    @Operation(
            summary = "List BINs",
            description = "Retrieve a paginated list of all Bank Identification Number (BIN) records.\n\n" +
                    "## Description\n" +
                    "BINs are the first 6 to 8 digits of a payment card number that identify the card issuer, " +
                    "card type, and other key attributes. They play a crucial role in routing transactions to the " +
                    "appropriate financial institutions during payment processing.\n\n" +
                    "## Response Details\n" +
                    "The response includes a paginated list of BIN records with the following information for each BIN:\n" +
                    "* BIN number and length\n" +
                    "* Associated issuer, card network, and card type\n" +
                    "* Geographic information (country code)\n" +
                    "* Currency information (currency code)\n" +
                    "* Activation status\n\n" +
                    "## Filtering and Sorting\n" +
                    "This endpoint supports filtering and sorting capabilities through the following parameters:\n" +
                    "* `page` - Page number (zero-based)\n" +
                    "* `size` - Number of items per page\n" +
                    "* `sort` - Sort field and direction (e.g., `binNumber,asc`)\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving all BINs for a specific issuer\n" +
                    "* Listing BINs for a particular card network\n" +
                    "* Finding BINs associated with a specific country or currency"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved BINs",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No BIN records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<BINDTO>>> getAllBINs(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listBINs(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create BIN",
            description = "Create a new Bank Identification Number (BIN) record.\n\n" +
                    "## Description\n" +
                    "This endpoint allows financial institutions to register new BINs in the system. " +
                    "The BIN is associated with a specific issuer, card network, and card type, and is used to identify " +
                    "the origin of payment cards.\n\n" +
                    "## Required Fields\n" +
                    "* `binNumber` - The BIN digits (typically 6-8 digits)\n" +
                    "* `binLength` - Length of the BIN (6, 8, etc.)\n" +
                    "* `issuerId` - Financial institution that owns this BIN\n" +
                    "* `cardNetworkId` - Card network associated with this BIN\n" +
                    "* `cardTypeId` - Type of card issued under this BIN\n\n" +
                    "## Optional Fields\n" +
                    "* `countryCode` - ISO country code for the BIN\n" +
                    "* `currencyCode` - ISO currency code for cards issued under this BIN\n" +
                    "* `isActive` - Whether the BIN is active\n" +
                    "* `description` - Additional information about the BIN\n\n" +
                    "## Regulatory Considerations\n" +
                    "BINs are typically assigned by card networks to financial institutions. Before creating a BIN, " +
                    "ensure that your organization has been officially assigned this BIN by the relevant card network. " +
                    "Using unauthorized BINs can lead to transaction routing issues and regulatory violations."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "BIN created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BINDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid BIN data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BINDTO>> createBIN(
            @Parameter(description = "Data for the new BIN", required = true,
                    schema = @Schema(implementation = BINDTO.class))
            @Valid @RequestBody BINDTO binDTO
    ) {
        return service.createBIN(binDTO)
                .map(createdBIN -> ResponseEntity.status(201).body(createdBIN))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get BIN by ID",
            description = "Retrieve a specific Bank Identification Number (BIN) record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint returns detailed information about a BIN, including its associated issuer, " +
                    "card network, and card type. This information is essential for card issuance and transaction processing.\n\n" +
                    "## Response Details\n" +
                    "The response includes comprehensive information about the requested BIN:\n" +
                    "* BIN number and length\n" +
                    "* Associated issuer details\n" +
                    "* Card network information\n" +
                    "* Card type specifications\n" +
                    "* Geographic and currency information\n" +
                    "* Activation status and description\n\n" +
                    "## Path Parameters\n" +
                    "* `binId` - The unique identifier of the BIN record in the system\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving BIN details before card issuance\n" +
                    "* Verifying BIN configuration during system setup\n" +
                    "* Checking BIN status during transaction processing"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the BIN",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BINDTO.class))),
            @ApiResponse(responseCode = "404", description = "BIN not found",
                    content = @Content)
    })
    @GetMapping(value = "/{binId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BINDTO>> getBIN(
            @Parameter(description = "Unique identifier of the BIN", required = true)
            @PathVariable UUID binId
    ) {
        return service.getBIN(binId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get BIN by Number",
            description = "Retrieve a specific Bank Identification Number (BIN) record by its BIN number.\n\n" +
                    "## Description\n" +
                    "This endpoint allows looking up BIN information using the actual BIN number (first 6-8 digits of a card), " +
                    "which is useful during transaction processing and card validation workflows.\n\n" +
                    "## Query Parameters\n" +
                    "* `number` - The BIN number to search for (e.g., '411111' for a Visa card)\n\n" +
                    "## Response Details\n" +
                    "The response includes the same comprehensive BIN information as the 'Get BIN by ID' endpoint, " +
                    "but the lookup is performed using the actual BIN number rather than the internal database identifier.\n\n" +
                    "## Common Use Cases\n" +
                    "* Identifying the issuer of a card during transaction processing\n" +
                    "* Determining card type and network during payment validation\n" +
                    "* Verifying card origin during fraud detection processes\n" +
                    "* Routing transactions based on BIN information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the BIN",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BINDTO.class))),
            @ApiResponse(responseCode = "404", description = "BIN not found",
                    content = @Content)
    })
    @GetMapping(params = "number", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BINDTO>> getBINByNumber(
            @Parameter(description = "BIN number", required = true)
            @RequestParam("number") String binNumber
    ) {
        return service.getBINByNumber(binNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update BIN",
            description = "Update an existing Bank Identification Number (BIN) record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint allows modification of BIN attributes such as associated card type, network, " +
                    "country code, currency code, and activation status.\n\n" +
                    "## Updatable Fields\n" +
                    "* `cardTypeId` - Type of card issued under this BIN\n" +
                    "* `cardNetworkId` - Card network associated with this BIN\n" +
                    "* `countryCode` - ISO country code for the BIN\n" +
                    "* `currencyCode` - ISO currency code for cards issued under this BIN\n" +
                    "* `isActive` - Whether the BIN is active\n" +
                    "* `description` - Additional information about the BIN\n\n" +
                    "## Important Note\n" +
                    "⚠️ The BIN number itself should typically not be changed as it is a standardized identifier " +
                    "assigned by card networks. Changing the BIN number could cause transaction routing issues and " +
                    "affect existing cards issued with this BIN.\n\n" +
                    "## Path Parameters\n" +
                    "* `binId` - The unique identifier of the BIN record to update\n\n" +
                    "## Common Use Cases\n" +
                    "* Updating BIN status (activating or deactivating)\n" +
                    "* Modifying currency or country information\n" +
                    "* Updating card type or network associations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "BIN updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BINDTO.class))),
            @ApiResponse(responseCode = "404", description = "BIN not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid BIN data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{binId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BINDTO>> updateBIN(
            @Parameter(description = "Unique identifier of the BIN to update", required = true)
            @PathVariable UUID binId,

            @Parameter(description = "Updated data for the BIN", required = true,
                    schema = @Schema(implementation = BINDTO.class))
            @RequestBody BINDTO binDTO
    ) {
        return service.updateBIN(binId, binDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete BIN",
            description = "Delete a Bank Identification Number (BIN) record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint permanently removes a BIN from the system. It completely erases the BIN record " +
                    "from the database.\n\n" +
                    "## Warning\n" +
                    "⚠️ **This operation should be used with extreme caution!**\n\n" +
                    "Deleting a BIN has several important implications:\n" +
                    "* Cards issued with this BIN may experience transaction routing issues\n" +
                    "* Card issuance processes using this BIN will fail\n" +
                    "* Historical transaction data may become inconsistent\n\n" +
                    "## Recommended Alternative\n" +
                    "In most production scenarios, BINs should be **deactivated** rather than deleted if they are no longer in use. " +
                    "This preserves the BIN record for historical purposes while preventing new cards from being issued with this BIN.\n\n" +
                    "## Appropriate Use Cases\n" +
                    "BIN deletion should be limited to:\n" +
                    "* Test BINs in non-production environments\n" +
                    "* BINs created in error that have never been used for card issuance\n" +
                    "* BINs that are no longer assigned to your organization by the card network\n\n" +
                    "## Path Parameters\n" +
                    "* `binId` - The unique identifier of the BIN record to delete\n\n" +
                    "## Required Permissions\n" +
                    "This operation typically requires elevated administrative privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "BIN deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "BIN not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{binId}")
    public Mono<ResponseEntity<Void>> deleteBIN(
            @Parameter(description = "Unique identifier of the BIN to delete", required = true)
            @PathVariable UUID binId
    ) {
        return service.deleteBIN(binId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
