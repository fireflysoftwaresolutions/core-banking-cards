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


package io.fireflysoftware.core.banking.cards.web.controllers.program;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.program.CardProgramService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.program.CardProgramDTO;
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
@Tag(name = "Card Programs", description = "APIs for managing card program records")
@RestController
@RequestMapping("/api/v1/programs")
public class CardProgramController {

    @Autowired
    private CardProgramService service;

    @Operation(
            summary = "List Card Programs",
            description = "Retrieve a paginated list of all card program records.\n\n" +
                    "## Description\n" +
                    "Card programs define the rules, configurations, and features for a group of cards. " +
                    "They specify parameters such as card limits, supported features, and eligibility criteria. " +
                    "Programs are the templates from which individual cards are created.\n\n" +
                    "## Response Details\n" +
                    "The response includes a paginated list of card program records with the following information for each program:\n" +
                    "* Program name, code, and description\n" +
                    "* Associated issuer, BIN, card type, and network\n" +
                    "* Default card design\n" +
                    "* Program validity period (start and end dates)\n" +
                    "* Default limits (daily, monthly, credit)\n" +
                    "* Supported features (contactless, international, ATM, etc.)\n" +
                    "* Activation status\n\n" +
                    "## Filtering and Sorting\n" +
                    "This endpoint supports filtering and sorting capabilities through the following parameters:\n" +
                    "* `page` - Page number (zero-based)\n" +
                    "* `size` - Number of items per page\n" +
                    "* `sort` - Sort field and direction (e.g., `programName,asc`)\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving all active card programs\n" +
                    "* Listing programs for a specific issuer or card type\n" +
                    "* Finding programs with specific features (e.g., international support)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card programs",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No card program records found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardProgramDTO>>> getAllPrograms(
            @RequestBody PaginationRequest paginationRequest
    ) {
        return service.listPrograms(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Program",
            description = "Create a new card program record.\n\n" +
                    "## Description\n" +
                    "Card programs are the foundation for issuing cards to parties. Each program defines " +
                    "the characteristics, limits, and features of cards issued under it. Programs are typically " +
                    "associated with specific issuers, BINs, card types, and networks.\n\n" +
                    "## Required Fields\n" +
                    "* `programName` - Name of the card program\n" +
                    "* `programCode` - Unique code for the program\n" +
                    "* `issuerId` - Financial institution issuing cards under this program\n" +
                    "* `binId` - Bank Identification Number for cards in this program\n" +
                    "* `cardTypeId` - Type of cards issued under this program\n" +
                    "* `cardNetworkId` - Card network for this program\n" +
                    "* `startDate` - When the program becomes active\n\n" +
                    "## Optional Fields\n" +
                    "* `endDate` - When the program expires\n" +
                    "* `defaultDesignId` - Default card design template\n" +
                    "* `maxCardsPerParty` - Maximum cards allowed per party\n" +
                    "* `defaultDailyLimit` - Default daily transaction limit\n" +
                    "* `defaultMonthlyLimit` - Default monthly transaction limit\n" +
                    "* `defaultCreditLimit` - Default credit limit (for credit cards)\n" +
                    "* `defaultCardValidityYears` - Default card validity period in years\n" +
                    "* Feature flags (supports* fields) - Capabilities of cards in this program\n\n" +
                    "## Business Rules\n" +
                    "When creating a card program, consider the following business rules:\n" +
                    "* Programs must have a valid start date (today or in the future)\n" +
                    "* If an end date is specified, it must be after the start date\n" +
                    "* The BIN must match the card type and network specified\n" +
                    "* Default limits should align with regulatory requirements"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card program created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardProgramDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card program data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardProgramDTO>> createProgram(
            @Parameter(description = "Data for the new card program", required = true,
                    schema = @Schema(implementation = CardProgramDTO.class))
            @RequestBody CardProgramDTO programDTO
    ) {
        return service.createProgram(programDTO)
                .map(createdProgram -> ResponseEntity.status(201).body(createdProgram))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Program by ID",
            description = "Retrieve a specific card program record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint returns detailed information about a card program, including its configuration, " +
                    "limits, supported features, and associations with issuers, BINs, and card networks.\n\n" +
                    "## Response Details\n" +
                    "The response includes comprehensive information about the requested card program:\n" +
                    "* Program identification (ID, name, code)\n" +
                    "* Associated entities (issuer, BIN, card type, network)\n" +
                    "* Validity period (start and end dates)\n" +
                    "* Default limits and parameters\n" +
                    "* Supported features and capabilities\n" +
                    "* Activation status\n\n" +
                    "## Path Parameters\n" +
                    "* `programId` - The unique identifier of the card program in the system\n\n" +
                    "## Common Use Cases\n" +
                    "* Retrieving program details before card issuance\n" +
                    "* Checking program configuration during system setup\n" +
                    "* Verifying program features and limits"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card program",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardProgramDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card program not found",
                    content = @Content)
    })
    @GetMapping(value = "/{programId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardProgramDTO>> getProgram(
            @Parameter(description = "Unique identifier of the card program", required = true)
            @PathVariable UUID programId
    ) {
        return service.getProgram(programId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Program",
            description = "Update an existing card program record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint allows modification of card program attributes such as limits, supported features, " +
                    "and activation status. It enables financial institutions to adjust program parameters based on " +
                    "business needs, market conditions, or regulatory requirements.\n\n" +
                    "## Updatable Fields\n" +
                    "* `programName` - Name of the card program\n" +
                    "* `defaultDesignId` - Default card design template\n" +
                    "* `endDate` - When the program expires\n" +
                    "* `isActive` - Whether the program is active\n" +
                    "* `maxCardsPerParty` - Maximum cards allowed per party\n" +
                    "* `defaultDailyLimit` - Default daily transaction limit\n" +
                    "* `defaultMonthlyLimit` - Default monthly transaction limit\n" +
                    "* `defaultCreditLimit` - Default credit limit (for credit cards)\n" +
                    "* `defaultCardValidityYears` - Default card validity period in years\n" +
                    "* Feature flags (supports* fields) - Capabilities of cards in this program\n\n" +
                    "## Important Note\n" +
                    "⚠️ Changes to a card program may affect new cards issued under the program, " +
                    "but typically do not automatically apply to existing cards unless explicitly configured. " +
                    "Consider implementing a separate process to update existing cards if needed.\n\n" +
                    "## Path Parameters\n" +
                    "* `programId` - The unique identifier of the card program to update\n\n" +
                    "## Common Use Cases\n" +
                    "* Adjusting program limits based on risk assessment\n" +
                    "* Adding or removing program features\n" +
                    "* Extending or shortening program validity period\n" +
                    "* Activating or deactivating a program"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card program updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardProgramDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card program not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid card program data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{programId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardProgramDTO>> updateProgram(
            @Parameter(description = "Unique identifier of the card program to update", required = true)
            @PathVariable UUID programId,

            @Parameter(description = "Updated data for the card program", required = true,
                    schema = @Schema(implementation = CardProgramDTO.class))
            @RequestBody CardProgramDTO programDTO
    ) {
        return service.updateProgram(programId, programDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Program",
            description = "Delete a card program record by its unique identifier.\n\n" +
                    "## Description\n" +
                    "This endpoint permanently removes a card program from the system. It completely erases the program record " +
                    "from the database.\n\n" +
                    "## Warning\n" +
                    "⚠️ **This operation should be used with extreme caution!**\n\n" +
                    "Deleting a card program has several important implications:\n" +
                    "* New cards cannot be issued under this program\n" +
                    "* Existing cards issued under this program may lose their program reference\n" +
                    "* Program-specific rules and configurations will no longer be available\n" +
                    "* Historical data and reporting may be affected\n\n" +
                    "## Recommended Alternative\n" +
                    "In most production scenarios, card programs should be **deactivated** rather than deleted if they are no longer in use. " +
                    "This preserves the program record for historical purposes while preventing new cards from being issued under this program.\n\n" +
                    "## Appropriate Use Cases\n" +
                    "Card program deletion should be limited to:\n" +
                    "* Test programs in non-production environments\n" +
                    "* Programs created in error that have never been used for card issuance\n" +
                    "* Programs that need to be completely removed due to specific regulatory requirements\n\n" +
                    "## Path Parameters\n" +
                    "* `programId` - The unique identifier of the card program to delete\n\n" +
                    "## Required Permissions\n" +
                    "This operation typically requires elevated administrative privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card program deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Card program not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{programId}")
    public Mono<ResponseEntity<Void>> deleteProgram(
            @Parameter(description = "Unique identifier of the card program to delete", required = true)
            @PathVariable UUID programId
    ) {
        return service.deleteProgram(programId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
