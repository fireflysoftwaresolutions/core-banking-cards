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


package io.fireflysoftware.core.banking.cards.web.controllers.security;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.security.CardSecurityService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.security.CardSecurityDTO;
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
@Tag(name = "Card Security", description = "APIs for managing security settings associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/security")
public class CardSecurityController {

    @Autowired
    private CardSecurityService service;

    @Operation(
            summary = "List Card Security Settings",
            description = "Retrieve a paginated list of security settings for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card security settings",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No security settings found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardSecurityDTO>>> getAllSecuritySettings(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listSecuritySettings(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Security Setting",
            description = "Create a new security setting for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Security setting created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardSecurityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid security setting data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardSecurityDTO>> createSecuritySetting(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new security setting record", required = true,
                    schema = @Schema(implementation = CardSecurityDTO.class))
            @RequestBody CardSecurityDTO securityDTO
    ) {
        return service.createSecuritySetting(cardId, securityDTO)
                .map(createdSetting -> ResponseEntity.status(201).body(createdSetting))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Security Setting by ID",
            description = "Retrieve a specific security setting by its unique identifier, ensuring it belongs to the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the security setting",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardSecurityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Security setting not found",
                    content = @Content)
    })
    @GetMapping(value = "/{securityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardSecurityDTO>> getSecuritySetting(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the security setting", required = true)
            @PathVariable UUID securityId
    ) {
        return service.getSecuritySetting(cardId, securityId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Security Setting",
            description = "Update an existing security setting associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Security setting updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardSecurityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Security setting not found",
                    content = @Content)
    })
    @PutMapping(value = "/{securityId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardSecurityDTO>> updateSecuritySetting(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the security setting to update", required = true)
            @PathVariable UUID securityId,

            @Parameter(description = "Updated security setting data", required = true,
                    schema = @Schema(implementation = CardSecurityDTO.class))
            @RequestBody CardSecurityDTO securityDTO
    ) {
        return service.updateSecuritySetting(cardId, securityId, securityDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Security Setting",
            description = "Remove an existing security setting by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Security setting record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Security setting not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{securityId}")
    public Mono<ResponseEntity<Void>> deleteSecuritySetting(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the security setting record to delete", required = true)
            @PathVariable UUID securityId
    ) {
        return service.deleteSecuritySetting(cardId, securityId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}