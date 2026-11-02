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


package io.fireflysoftware.core.banking.cards.web.controllers.promotion;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.promotion.CardPromotionService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.promotion.CardPromotionDTO;
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
@Tag(name = "Card Promotions", description = "APIs for managing promotion records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/promotions")
public class CardPromotionController {

    @Autowired
    private CardPromotionService service;

    @Operation(
            summary = "List Card Promotions",
            description = "Retrieve a paginated list of promotion records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card promotions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No promotion records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardPromotionDTO>>> getAllPromotions(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listPromotions(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Promotion",
            description = "Create a new promotion record for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promotion record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPromotionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid promotion data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardPromotionDTO>> createPromotion(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card promotion record", required = true,
                    schema = @Schema(implementation = CardPromotionDTO.class))
            @RequestBody CardPromotionDTO promotionDTO
    ) {
        return service.createPromotion(cardId, promotionDTO)
                .map(createdPromotion -> ResponseEntity.status(201).body(createdPromotion))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Promotion",
            description = "Retrieve a specific promotion record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the promotion record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPromotionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Promotion record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{promotionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardPromotionDTO>> getPromotion(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the promotion record", required = true)
            @PathVariable UUID promotionId
    ) {
        return service.getPromotion(cardId, promotionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Promotion",
            description = "Update an existing promotion record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promotion record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPromotionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Promotion record not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid promotion data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{promotionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardPromotionDTO>> updatePromotion(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the promotion record to update", required = true)
            @PathVariable UUID promotionId,

            @Parameter(description = "Updated data for the promotion record", required = true,
                    schema = @Schema(implementation = CardPromotionDTO.class))
            @RequestBody CardPromotionDTO promotionDTO
    ) {
        return service.updatePromotion(cardId, promotionId, promotionDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Promotion",
            description = "Delete a promotion record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promotion record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Promotion record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{promotionId}")
    public Mono<ResponseEntity<Void>> deletePromotion(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the promotion record to delete", required = true)
            @PathVariable UUID promotionId
    ) {
        return service.deletePromotion(cardId, promotionId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
