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


package io.fireflysoftware.core.banking.cards.web.controllers.merchant;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.merchant.CardMerchantService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.merchant.CardMerchantDTO;
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
@Tag(name = "Card Merchants", description = "APIs for managing card merchant records")
@RestController
@RequestMapping("/api/v1/merchants")
public class CardMerchantController {

    @Autowired
    private CardMerchantService service;

    @Operation(
            summary = "List Card Merchants",
            description = "Retrieve a paginated list of all card merchant records.\n\n" +
                    "## Description\n" +
                    "Card merchants represent businesses that accept card payments. They are essential entities " +
                    "in the payment ecosystem, connecting cardholders with goods and services providers. " +
                    "This endpoint provides a comprehensive view of all merchants in the system.\n\n" +
                    "## Response Details\n" +
                    "The response includes a paginated list of merchant records with the following information for each merchant:\n" +
                    "* Merchant identification (ID, reference, name)\n" +
                    "* Business details (legal name, category, type)\n" +
                    "* Contact information (address, phone, email)\n" +
                    "* Payment capabilities (supported networks, payment methods)\n" +
                    "* Risk assessment (risk rating, score, fraud indicators)\n" +
                    "* Settlement information (bank details, frequency)\n" +
                    "* Status and activation details\n\n" +
                    "## Filtering and Sorting\n" +
                    "This endpoint supports filtering and sorting capabilities through the following parameters:\n" +
                    "* `page` - Page number (zero-based)\n" +
                    "* `size` - Number of items per page\n" +
                    "* `sort` - Sort field and direction (e.g., `merchantName,asc`)\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving all active merchants\n" +
                    "* Listing merchants by category or risk rating\n" +
                    "* Finding merchants that support specific payment methods\n" +
                    "* Generating merchant reports for compliance or business analysis"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card merchants",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No card merchant records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardMerchantDTO>>> getAllMerchants(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listMerchants(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Merchant",
            description = "Create a new card merchant record.\n\n" +
                    "## Description\n" +
                    "This endpoint allows adding new merchants to the system. Merchants are essential for " +
                    "transaction processing and reporting. The merchant record includes details about the " +
                    "business, contact information, supported payment methods, and risk assessment data.\n\n" +
                    "## Required Fields\n" +
                    "* `merchantReference` - Unique reference code for the merchant\n" +
                    "* `merchantName` - Display name of the merchant\n" +
                    "* `merchantLegalName` - Legal business name\n" +
                    "* `merchantCategoryCode` - Standard MCC (Merchant Category Code)\n" +
                    "* `merchantType` - Type of business (RETAIL, ONLINE, etc.)\n" +
                    "* `merchantStatus` - Current status (ACTIVE, PENDING, etc.)\n" +
                    "* `country` - Country of operation\n\n" +
                    "## Optional Fields\n" +
                    "* Contact information (address, phone, email)\n" +
                    "* Business details (tax ID, registration number, website)\n" +
                    "* Payment capabilities (supported networks, payment methods)\n" +
                    "* Risk assessment (risk rating, score)\n" +
                    "* Settlement information (bank details, frequency)\n\n" +
                    "## Business Rules\n" +
                    "When creating a merchant, consider the following business rules:\n" +
                    "* Merchant references must be unique across the system\n" +
                    "* Merchant category codes should follow the ISO standard\n" +
                    "* Risk assessment should be performed before activating high-volume merchants\n" +
                    "* Settlement information is required for merchants that will process real transactions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card merchant created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardMerchantDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card merchant data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardMerchantDTO>> createMerchant(
            @Parameter(description = "Data for the new card merchant", required = true,
                    schema = @Schema(implementation = CardMerchantDTO.class))
            @RequestBody CardMerchantDTO merchantDTO
    ) {
        return service.createMerchant(merchantDTO)
                .map(createdMerchant -> ResponseEntity.status(201).body(createdMerchant))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Merchant by ID",
            description = "Retrieve a specific card merchant record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint returns comprehensive information about a merchant, including its " +
                    "business details, category codes, risk assessment, and settlement information. " +
                    "This data is useful for transaction analysis and merchant management.\n\n" +
                    "## Response Details\n" +
                    "The response includes detailed information about the requested merchant:\n" +
                    "* Merchant identification (ID, reference, name)\n" +
                    "* Business details (legal name, category, type)\n" +
                    "* Contact information (address, phone, email)\n" +
                    "* Payment capabilities (supported networks, payment methods)\n" +
                    "* Risk assessment (risk rating, score, fraud indicators)\n" +
                    "* Settlement information (bank details, frequency)\n" +
                    "* Status and activation details\n\n" +
                    "## Path Parameters\n" +
                    "* `merchantId` - The unique identifier of the merchant in the system\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving merchant details for transaction verification\n" +
                    "* Checking merchant status during payment processing\n" +
                    "* Verifying merchant risk rating for fraud prevention\n" +
                    "* Accessing settlement information for financial reconciliation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card merchant",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardMerchantDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card merchant not found",
                    content = @Content)
    })
    @GetMapping(value = "/{merchantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardMerchantDTO>> getMerchant(
            @Parameter(description = "Unique identifier of the card merchant", required = true)
            @PathVariable UUID merchantId
    ) {
        return service.getMerchant(merchantId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Merchant",
            description = "Update an existing card merchant record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint allows modification of merchant attributes such as contact information, " +
                    "business details, risk ratings, and settlement information. Keeping merchant information " +
                    "up-to-date is essential for accurate transaction processing and reporting.\n\n" +
                    "## Updatable Fields\n" +
                    "* `merchantName` - Display name of the merchant\n" +
                    "* `merchantDisplayName` - Name shown on statements\n" +
                    "* `merchantDescription` - Description of the business\n" +
                    "* `merchantStatus` - Current status (ACTIVE, SUSPENDED, etc.)\n" +
                    "* `isActive` - Whether the merchant is active\n" +
                    "* Contact information (address, phone, email)\n" +
                    "* Business details (website, logo URL)\n" +
                    "* Payment capabilities (supported networks, payment methods)\n" +
                    "* Risk assessment (risk rating, score)\n" +
                    "* Settlement information (bank details, frequency)\n\n" +
                    "## Important Note\n" +
                    "⚠️ Some core merchant attributes should generally remain stable:\n" +
                    "* Merchant reference\n" +
                    "* Merchant legal name\n" +
                    "* Merchant category code\n" +
                    "* Tax ID and registration number\n\n" +
                    "Changes to these core attributes may require additional verification or approval processes.\n\n" +
                    "## Path Parameters\n" +
                    "* `merchantId` - The unique identifier of the merchant to update\n\n" +
                    "## Common Use Cases\n" +
                    "* Updating merchant contact information\n" +
                    "* Changing merchant status (activating, suspending)\n" +
                    "* Updating risk assessment after periodic review\n" +
                    "* Modifying settlement details after bank account changes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card merchant updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardMerchantDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card merchant not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid card merchant data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{merchantId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardMerchantDTO>> updateMerchant(
            @Parameter(description = "Unique identifier of the card merchant to update", required = true)
            @PathVariable UUID merchantId,

            @Parameter(description = "Updated data for the card merchant", required = true,
                    schema = @Schema(implementation = CardMerchantDTO.class))
            @RequestBody CardMerchantDTO merchantDTO
    ) {
        return service.updateMerchant(merchantId, merchantDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Merchant",
            description = "Delete a card merchant record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint permanently removes a merchant from the system. It completely erases the merchant record " +
                    "from the database.\n\n" +
                    "## Warning\n" +
                    "⚠️ **This operation should be used with extreme caution!**\n\n" +
                    "Deleting a merchant has several important implications:\n" +
                    "* All transaction history associated with this merchant will be orphaned\n" +
                    "* Reporting and analytics will be affected\n" +
                    "* Chargeback and dispute processing may be compromised\n" +
                    "* Regulatory compliance and audit trails may be impacted\n\n" +
                    "## Recommended Alternative\n" +
                    "In most production scenarios, merchants should be **deactivated** rather than deleted if they are no longer active. " +
                    "This preserves the merchant record for historical purposes while preventing new transactions from being " +
                    "processed for this merchant.\n\n" +
                    "To deactivate a merchant, use the Update endpoint to set:\n" +
                    "* `merchantStatus` to `INACTIVE`\n" +
                    "* `isActive` to `false`\n\n" +
                    "## Appropriate Use Cases\n" +
                    "Merchant deletion should be limited to:\n" +
                    "* Test merchants in non-production environments\n" +
                    "* Merchants created in error that have never processed transactions\n" +
                    "* Specific regulatory requirements that mandate complete removal\n\n" +
                    "## Path Parameters\n" +
                    "* `merchantId` - The unique identifier of the merchant to delete\n\n" +
                    "## Required Permissions\n" +
                    "This operation typically requires elevated administrative privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card merchant deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card merchant not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{merchantId}")
    public Mono<ResponseEntity<Void>> deleteMerchant(
            @Parameter(description = "Unique identifier of the card merchant to delete", required = true)
            @PathVariable UUID merchantId
    ) {
        return service.deleteMerchant(merchantId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
