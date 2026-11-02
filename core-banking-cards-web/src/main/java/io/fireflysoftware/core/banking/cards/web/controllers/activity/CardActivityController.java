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


package io.fireflysoftware.core.banking.cards.web.controllers.activity;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.banking.cards.core.services.activity.CardActivityService;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.activity.CardActivityDTO;
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
@Tag(name = "Card Activities", description = "APIs for managing activity records associated with a specific card")
@RestController
@RequestMapping("/api/v1/cards/{cardId}/activities")
public class CardActivityController {

    @Autowired
    private CardActivityService service;

    @Operation(
            summary = "List Card Activities",
            description = "Retrieve a paginated list of activity records associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved card activities",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No activity records found for the specified card",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<CardActivityDTO>>> getAllActivities(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listActivities(cardId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Card Activity",
            description = "Create a new activity record for the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Activity record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardActivityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid activity data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardActivityDTO>> createActivity(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Data for the new card activity record", required = true,
                    schema = @Schema(implementation = CardActivityDTO.class))
            @RequestBody CardActivityDTO activityDTO
    ) {
        return service.createActivity(cardId, activityDTO)
                .map(createdActivity -> ResponseEntity.status(201).body(createdActivity))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Card Activity",
            description = "Retrieve a specific activity record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the activity record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardActivityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Activity record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{activityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardActivityDTO>> getActivity(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the activity record", required = true)
            @PathVariable UUID activityId
    ) {
        return service.getActivity(cardId, activityId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Card Activity",
            description = "Update an existing activity record associated with the specified card."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardActivityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Activity record not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid activity data provided",
                    content = @Content)
    })
    @PutMapping(value = "/{activityId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CardActivityDTO>> updateActivity(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the activity record to update", required = true)
            @PathVariable UUID activityId,

            @Parameter(description = "Updated data for the activity record", required = true,
                    schema = @Schema(implementation = CardActivityDTO.class))
            @RequestBody CardActivityDTO activityDTO
    ) {
        return service.updateActivity(cardId, activityId, activityDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Card Activity",
            description = "Delete an activity record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Activity record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{activityId}")
    public Mono<ResponseEntity<Void>> deleteActivity(
            @Parameter(description = "Unique identifier of the card", required = true)
            @PathVariable UUID cardId,

            @Parameter(description = "Unique identifier of the activity record to delete", required = true)
            @PathVariable UUID activityId
    ) {
        return service.deleteActivity(cardId, activityId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
