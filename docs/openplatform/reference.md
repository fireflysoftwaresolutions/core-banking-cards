# Banking Cards: source reference

[Repository guide](../../README.md) · [Machine-readable inventory](source-inventory.json)

This reference is derived from checked-in source. It separates declared HTTP mappings, typed contracts, property placeholders, and validation locations. Java reflection, inherited framework routes, generated targets, and runtime infrastructure are outside this static inventory.

## HTTP operations

| Mapping | Method | Controller | Kind |
| --- | --- | --- | --- |
| `GET /api/v1/acquirers` | `public Mono<ResponseEntity<PaginationResponse<CardAcquirerDTO>>> getAllAcquirers( @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardAcquirerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/acquirer/CardAcquirerController.java) | Local declaration |
| `GET /api/v1/acquirers/{acquirerId}` | `public Mono<ResponseEntity<CardAcquirerDTO>> getAcquirer( @Parameter(description = "Unique identifier of the card acquirer", required = true) @PathVariable UUID acquirerId )` | [CardAcquirerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/acquirer/CardAcquirerController.java) | Local declaration |
| `POST /api/v1/acquirers` | `public Mono<ResponseEntity<CardAcquirerDTO>> createAcquirer( @Parameter(description = "Data for the new card acquirer", required = true, schema = @Schema(implementation = CardAcquirerDTO.class)) @RequestBody CardAcquirerDTO acquirerDTO )` | [CardAcquirerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/acquirer/CardAcquirerController.java) | Local declaration |
| `PUT /api/v1/acquirers/{acquirerId}` | `public Mono<ResponseEntity<CardAcquirerDTO>> updateAcquirer( @Parameter(description = "Unique identifier of the card acquirer to update", required = true) @PathVariable UUID acquirerId, @Parameter(description = "Updated data for the card acquirer", required = true, schema = @Schema(implementation = CardAcquirerDTO.class)) @RequestBody CardAcquirerDTO acquirerDTO )` | [CardAcquirerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/acquirer/CardAcquirerController.java) | Local declaration |
| `DELETE /api/v1/acquirers/{acquirerId}` | `public Mono<ResponseEntity<Void>> deleteAcquirer( @Parameter(description = "Unique identifier of the card acquirer to delete", required = true) @PathVariable UUID acquirerId )` | [CardAcquirerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/acquirer/CardAcquirerController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/activities` | `public Mono<ResponseEntity<PaginationResponse<CardActivityDTO>>> getAllActivities( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardActivityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/activity/CardActivityController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/activities/{activityId}` | `public Mono<ResponseEntity<CardActivityDTO>> getActivity( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the activity record", required = true) @PathVariable UUID activityId )` | [CardActivityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/activity/CardActivityController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/activities` | `public Mono<ResponseEntity<CardActivityDTO>> createActivity( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card activity record", required = true, schema = @Schema(implementation = CardActivityDTO.class)) @RequestBody CardActivityDTO activityDTO )` | [CardActivityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/activity/CardActivityController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/activities/{activityId}` | `public Mono<ResponseEntity<CardActivityDTO>> updateActivity( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the activity record to update", required = true) @PathVariable UUID activityId, @Parameter(description = "Updated data for the activity record", required = true, schema = @Schema(implementation = CardActivityDTO.class)) @RequestBody CardActivityDTO activityDTO )` | [CardActivityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/activity/CardActivityController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/activities/{activityId}` | `public Mono<ResponseEntity<Void>> deleteActivity( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the activity record to delete", required = true) @PathVariable UUID activityId )` | [CardActivityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/activity/CardActivityController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/balances` | `public Mono<ResponseEntity<PaginationResponse<CardBalanceDTO>>> getAllBalances( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardBalanceController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/balance/CardBalanceController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/balances/{balanceId}` | `public Mono<ResponseEntity<CardBalanceDTO>> getBalance( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the balance record", required = true) @PathVariable UUID balanceId )` | [CardBalanceController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/balance/CardBalanceController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/balances` | `public Mono<ResponseEntity<CardBalanceDTO>> createBalance( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card balance record", required = true, schema = @Schema(implementation = CardBalanceDTO.class)) @RequestBody CardBalanceDTO balanceDTO )` | [CardBalanceController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/balance/CardBalanceController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/balances/{balanceId}` | `public Mono<ResponseEntity<CardBalanceDTO>> updateBalance( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the balance record to update", required = true) @PathVariable UUID balanceId, @Parameter(description = "Updated data for the balance record", required = true, schema = @Schema(implementation = CardBalanceDTO.class)) @RequestBody CardBalanceDTO balanceDTO )` | [CardBalanceController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/balance/CardBalanceController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/balances/{balanceId}` | `public Mono<ResponseEntity<Void>> deleteBalance( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the balance record to delete", required = true) @PathVariable UUID balanceId )` | [CardBalanceController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/balance/CardBalanceController.java) | Local declaration |
| `GET /api/v1/bins` | `public Mono<ResponseEntity<PaginationResponse<BINDTO>>> getAllBINs( @RequestBody PaginationRequest paginationRequest )` | [BINController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/bin/BINController.java) | Local declaration |
| `GET /api/v1/bins/{binId}` | `public Mono<ResponseEntity<BINDTO>> getBIN( @Parameter(description = "Unique identifier of the BIN", required = true) @PathVariable UUID binId )` | [BINController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/bin/BINController.java) | Local declaration |
| `GET /api/v1/bins` | `public Mono<ResponseEntity<BINDTO>> getBINByNumber( @Parameter(description = "BIN number", required = true) @RequestParam("number") String binNumber )` | [BINController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/bin/BINController.java) | Local declaration |
| `POST /api/v1/bins` | `public Mono<ResponseEntity<BINDTO>> createBIN( @Parameter(description = "Data for the new BIN", required = true, schema = @Schema(implementation = BINDTO.class)) @Valid @RequestBody BINDTO binDTO )` | [BINController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/bin/BINController.java) | Local declaration |
| `PUT /api/v1/bins/{binId}` | `public Mono<ResponseEntity<BINDTO>> updateBIN( @Parameter(description = "Unique identifier of the BIN to update", required = true) @PathVariable UUID binId, @Parameter(description = "Updated data for the BIN", required = true, schema = @Schema(implementation = BINDTO.class)) @RequestBody BINDTO binDTO )` | [BINController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/bin/BINController.java) | Local declaration |
| `DELETE /api/v1/bins/{binId}` | `public Mono<ResponseEntity<Void>> deleteBIN( @Parameter(description = "Unique identifier of the BIN to delete", required = true) @PathVariable UUID binId )` | [BINController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/bin/BINController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}` | `public Mono<ResponseEntity<CardDTO>> getCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId )` | [CardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/card/CardController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/activation` | `public Mono<ResponseEntity<CardActivationReceipt>> activateCard( @PathVariable UUID cardId, @RequestHeader("X-Idempotency-Key") UUID actionId, @Valid @RequestBody CardActivationRequest request)` | [CardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/card/CardController.java) | Local declaration |
| `POST /api/v1/cards/filter` | `public Mono<ResponseEntity<PaginationResponse<CardDTO>>> filterCards( @Valid @RequestBody FilterRequest<CardDTO> filterRequest )` | [CardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/card/CardController.java) | Local declaration |
| `POST /api/v1/cards` | `public Mono<ResponseEntity<CardDTO>> createCard( @Parameter(description = "Data for the new card", required = true, schema = @Schema(implementation = CardDTO.class)) @Valid @RequestBody CardDTO cardDTO )` | [CardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/card/CardController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}` | `public Mono<ResponseEntity<CardDTO>> updateCard( @Parameter(description = "Unique identifier of the card to update", required = true) @PathVariable UUID cardId, @Parameter(description = "Updated card data", required = true, schema = @Schema(implementation = CardDTO.class)) @RequestBody CardDTO cardDTO )` | [CardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/card/CardController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}` | `public Mono<ResponseEntity<Void>> deleteCard( @Parameter(description = "Unique identifier of the card to delete", required = true) @PathVariable UUID cardId )` | [CardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/card/CardController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/configurations` | `public Mono<ResponseEntity<PaginationResponse<CardConfigurationDTO>>> getAllConfigurations( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardConfigurationController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/configuration/CardConfigurationController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/configurations/{configId}` | `public Mono<ResponseEntity<CardConfigurationDTO>> getConfiguration( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card configuration record", required = true) @PathVariable UUID configId )` | [CardConfigurationController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/configuration/CardConfigurationController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/configurations` | `public Mono<ResponseEntity<CardConfigurationDTO>> createConfiguration( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card configuration record", required = true, schema = @Schema(implementation = CardConfigurationDTO.class)) @RequestBody CardConfigurationDTO configDTO )` | [CardConfigurationController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/configuration/CardConfigurationController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/configurations/{configId}` | `public Mono<ResponseEntity<CardConfigurationDTO>> updateConfiguration( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card configuration record to update", required = true) @PathVariable UUID configId, @Parameter(description = "Updated configuration data", required = true, schema = @Schema(implementation = CardConfigurationDTO.class)) @RequestBody CardConfigurationDTO configDTO )` | [CardConfigurationController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/configuration/CardConfigurationController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/configurations/{configId}` | `public Mono<ResponseEntity<Void>> deleteConfiguration( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card configuration record to delete", required = true) @PathVariable UUID configId )` | [CardConfigurationController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/configuration/CardConfigurationController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/disputes` | `public Mono<ResponseEntity<PaginationResponse<CardDisputeDTO>>> getAllDisputes( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardDisputeController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/dispute/CardDisputeController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/disputes/{disputeId}` | `public Mono<ResponseEntity<CardDisputeDTO>> getDispute( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the dispute record", required = true) @PathVariable UUID disputeId )` | [CardDisputeController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/dispute/CardDisputeController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/disputes` | `public Mono<ResponseEntity<CardDisputeDTO>> createDispute( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card dispute record", required = true, schema = @Schema(implementation = CardDisputeDTO.class)) @RequestBody CardDisputeDTO disputeDTO )` | [CardDisputeController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/dispute/CardDisputeController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/disputes/{disputeId}` | `public Mono<ResponseEntity<CardDisputeDTO>> updateDispute( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the dispute record to update", required = true) @PathVariable UUID disputeId, @Parameter(description = "Updated data for the dispute record", required = true, schema = @Schema(implementation = CardDisputeDTO.class)) @RequestBody CardDisputeDTO disputeDTO )` | [CardDisputeController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/dispute/CardDisputeController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/disputes/{disputeId}` | `public Mono<ResponseEntity<Void>> deleteDispute( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the dispute record to delete", required = true) @PathVariable UUID disputeId )` | [CardDisputeController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/dispute/CardDisputeController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/enrollments` | `public Mono<ResponseEntity<PaginationResponse<CardEnrollmentDTO>>> getAllEnrollments( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardEnrollmentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/enrollment/CardEnrollmentController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/enrollments/{enrollmentId}` | `public Mono<ResponseEntity<CardEnrollmentDTO>> getEnrollment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the enrollment record", required = true) @PathVariable UUID enrollmentId )` | [CardEnrollmentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/enrollment/CardEnrollmentController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/enrollments` | `public Mono<ResponseEntity<CardEnrollmentDTO>> createEnrollment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card enrollment record", required = true, schema = @Schema(implementation = CardEnrollmentDTO.class)) @RequestBody CardEnrollmentDTO enrollmentDTO )` | [CardEnrollmentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/enrollment/CardEnrollmentController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/enrollments/{enrollmentId}` | `public Mono<ResponseEntity<CardEnrollmentDTO>> updateEnrollment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the enrollment record to update", required = true) @PathVariable UUID enrollmentId, @Parameter(description = "Updated data for the enrollment record", required = true, schema = @Schema(implementation = CardEnrollmentDTO.class)) @RequestBody CardEnrollmentDTO enrollmentDTO )` | [CardEnrollmentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/enrollment/CardEnrollmentController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/enrollments/{enrollmentId}` | `public Mono<ResponseEntity<Void>> deleteEnrollment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the enrollment record to delete", required = true) @PathVariable UUID enrollmentId )` | [CardEnrollmentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/enrollment/CardEnrollmentController.java) | Local declaration |
| `GET /api/v1/gateways` | `public Mono<ResponseEntity<PaginationResponse<CardGatewayDTO>>> getAllGateways( @RequestBody PaginationRequest paginationRequest )` | [CardGatewayController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/gateway/CardGatewayController.java) | Local declaration |
| `GET /api/v1/gateways/{gatewayId}` | `public Mono<ResponseEntity<CardGatewayDTO>> getGateway( @Parameter(description = "Unique identifier of the card gateway", required = true) @PathVariable UUID gatewayId )` | [CardGatewayController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/gateway/CardGatewayController.java) | Local declaration |
| `POST /api/v1/gateways` | `public Mono<ResponseEntity<CardGatewayDTO>> createGateway( @Parameter(description = "Data for the new card gateway", required = true, schema = @Schema(implementation = CardGatewayDTO.class)) @RequestBody CardGatewayDTO gatewayDTO )` | [CardGatewayController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/gateway/CardGatewayController.java) | Local declaration |
| `PUT /api/v1/gateways/{gatewayId}` | `public Mono<ResponseEntity<CardGatewayDTO>> updateGateway( @Parameter(description = "Unique identifier of the card gateway to update", required = true) @PathVariable UUID gatewayId, @Parameter(description = "Updated data for the card gateway", required = true, schema = @Schema(implementation = CardGatewayDTO.class)) @RequestBody CardGatewayDTO gatewayDTO )` | [CardGatewayController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/gateway/CardGatewayController.java) | Local declaration |
| `DELETE /api/v1/gateways/{gatewayId}` | `public Mono<ResponseEntity<Void>> deleteGateway( @Parameter(description = "Unique identifier of the card gateway to delete", required = true) @PathVariable UUID gatewayId )` | [CardGatewayController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/gateway/CardGatewayController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/interests` | `public Mono<ResponseEntity<PaginationResponse<CardInterestDTO>>> getAllInterests( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardInterestController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/interest/CardInterestController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/interests/{interestId}` | `public Mono<ResponseEntity<CardInterestDTO>> getInterest( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the interest record", required = true) @PathVariable UUID interestId )` | [CardInterestController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/interest/CardInterestController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/interests` | `public Mono<ResponseEntity<CardInterestDTO>> createInterest( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card interest record", required = true, schema = @Schema(implementation = CardInterestDTO.class)) @RequestBody CardInterestDTO interestDTO )` | [CardInterestController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/interest/CardInterestController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/interests/{interestId}` | `public Mono<ResponseEntity<CardInterestDTO>> updateInterest( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the interest record to update", required = true) @PathVariable UUID interestId, @Parameter(description = "Updated data for the interest record", required = true, schema = @Schema(implementation = CardInterestDTO.class)) @RequestBody CardInterestDTO interestDTO )` | [CardInterestController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/interest/CardInterestController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/interests/{interestId}` | `public Mono<ResponseEntity<Void>> deleteInterest( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the interest record to delete", required = true) @PathVariable UUID interestId )` | [CardInterestController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/interest/CardInterestController.java) | Local declaration |
| `GET /api/v1/issuers` | `public Mono<ResponseEntity<PaginationResponse<IssuerDTO>>> getAllIssuers( @RequestBody PaginationRequest paginationRequest )` | [IssuerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/issuer/IssuerController.java) | Local declaration |
| `GET /api/v1/issuers/{issuerId}` | `public Mono<ResponseEntity<IssuerDTO>> getIssuer( @Parameter(description = "Unique identifier of the issuer", required = true) @PathVariable UUID issuerId )` | [IssuerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/issuer/IssuerController.java) | Local declaration |
| `POST /api/v1/issuers` | `public Mono<ResponseEntity<IssuerDTO>> createIssuer( @Parameter(description = "Data for the new issuer", required = true, schema = @Schema(implementation = IssuerDTO.class)) @RequestBody IssuerDTO issuerDTO )` | [IssuerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/issuer/IssuerController.java) | Local declaration |
| `PUT /api/v1/issuers/{issuerId}` | `public Mono<ResponseEntity<IssuerDTO>> updateIssuer( @Parameter(description = "Unique identifier of the issuer to update", required = true) @PathVariable UUID issuerId, @Parameter(description = "Updated data for the issuer", required = true, schema = @Schema(implementation = IssuerDTO.class)) @RequestBody IssuerDTO issuerDTO )` | [IssuerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/issuer/IssuerController.java) | Local declaration |
| `DELETE /api/v1/issuers/{issuerId}` | `public Mono<ResponseEntity<Void>> deleteIssuer( @Parameter(description = "Unique identifier of the issuer to delete", required = true) @PathVariable UUID issuerId )` | [IssuerController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/issuer/IssuerController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/limits` | `public Mono<ResponseEntity<PaginationResponse<CardLimitDTO>>> getAllLimits( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardLimitController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/limit/CardLimitController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/limits/{limitId}` | `public Mono<ResponseEntity<CardLimitDTO>> getLimit( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card limit record", required = true) @PathVariable UUID limitId )` | [CardLimitController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/limit/CardLimitController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/limits` | `public Mono<ResponseEntity<CardLimitDTO>> createLimit( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card limit record", required = true, schema = @Schema(implementation = CardLimitDTO.class)) @RequestBody CardLimitDTO limitDTO )` | [CardLimitController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/limit/CardLimitController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/limits/{limitId}` | `public Mono<ResponseEntity<CardLimitDTO>> updateLimit( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card limit record to update", required = true) @PathVariable UUID limitId, @Parameter(description = "Updated card limit data", required = true, schema = @Schema(implementation = CardLimitDTO.class)) @RequestBody CardLimitDTO limitDTO )` | [CardLimitController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/limit/CardLimitController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/limits/{limitId}` | `public Mono<ResponseEntity<Void>> deleteLimit( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card limit record to delete", required = true) @PathVariable UUID limitId )` | [CardLimitController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/limit/CardLimitController.java) | Local declaration |
| `GET /api/v1/merchants` | `public Mono<ResponseEntity<PaginationResponse<CardMerchantDTO>>> getAllMerchants( @RequestBody PaginationRequest paginationRequest )` | [CardMerchantController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/merchant/CardMerchantController.java) | Local declaration |
| `GET /api/v1/merchants/{merchantId}` | `public Mono<ResponseEntity<CardMerchantDTO>> getMerchant( @Parameter(description = "Unique identifier of the card merchant", required = true) @PathVariable UUID merchantId )` | [CardMerchantController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/merchant/CardMerchantController.java) | Local declaration |
| `POST /api/v1/merchants` | `public Mono<ResponseEntity<CardMerchantDTO>> createMerchant( @Parameter(description = "Data for the new card merchant", required = true, schema = @Schema(implementation = CardMerchantDTO.class)) @RequestBody CardMerchantDTO merchantDTO )` | [CardMerchantController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/merchant/CardMerchantController.java) | Local declaration |
| `PUT /api/v1/merchants/{merchantId}` | `public Mono<ResponseEntity<CardMerchantDTO>> updateMerchant( @Parameter(description = "Unique identifier of the card merchant to update", required = true) @PathVariable UUID merchantId, @Parameter(description = "Updated data for the card merchant", required = true, schema = @Schema(implementation = CardMerchantDTO.class)) @RequestBody CardMerchantDTO merchantDTO )` | [CardMerchantController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/merchant/CardMerchantController.java) | Local declaration |
| `DELETE /api/v1/merchants/{merchantId}` | `public Mono<ResponseEntity<Void>> deleteMerchant( @Parameter(description = "Unique identifier of the card merchant to delete", required = true) @PathVariable UUID merchantId )` | [CardMerchantController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/merchant/CardMerchantController.java) | Local declaration |
| `GET /api/v1/networks` | `public Mono<ResponseEntity<PaginationResponse<CardNetworkDTO>>> getAllNetworks( @RequestBody PaginationRequest paginationRequest )` | [CardNetworkController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/network/CardNetworkController.java) | Local declaration |
| `GET /api/v1/networks/{networkId}` | `public Mono<ResponseEntity<CardNetworkDTO>> getNetwork( @Parameter(description = "Unique identifier of the card network", required = true) @PathVariable UUID networkId )` | [CardNetworkController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/network/CardNetworkController.java) | Local declaration |
| `POST /api/v1/networks` | `public Mono<ResponseEntity<CardNetworkDTO>> createNetwork( @Parameter(description = "Data for the new card network", required = true, schema = @Schema(implementation = CardNetworkDTO.class)) @RequestBody CardNetworkDTO networkDTO )` | [CardNetworkController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/network/CardNetworkController.java) | Local declaration |
| `PUT /api/v1/networks/{networkId}` | `public Mono<ResponseEntity<CardNetworkDTO>> updateNetwork( @Parameter(description = "Unique identifier of the card network to update", required = true) @PathVariable UUID networkId, @Parameter(description = "Updated data for the card network", required = true, schema = @Schema(implementation = CardNetworkDTO.class)) @RequestBody CardNetworkDTO networkDTO )` | [CardNetworkController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/network/CardNetworkController.java) | Local declaration |
| `DELETE /api/v1/networks/{networkId}` | `public Mono<ResponseEntity<Void>> deleteNetwork( @Parameter(description = "Unique identifier of the card network to delete", required = true) @PathVariable UUID networkId )` | [CardNetworkController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/network/CardNetworkController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/payments` | `public Mono<ResponseEntity<PaginationResponse<CardPaymentDTO>>> getAllPayments( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @RequestBody PaginationRequest paginationRequest )` | [CardPaymentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/payment/CardPaymentController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/payments/{paymentId}` | `public Mono<ResponseEntity<CardPaymentDTO>> getPayment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the payment record", required = true) @PathVariable UUID paymentId )` | [CardPaymentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/payment/CardPaymentController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/payments` | `public Mono<ResponseEntity<CardPaymentDTO>> createPayment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card payment record", required = true, schema = @Schema(implementation = CardPaymentDTO.class)) @RequestBody CardPaymentDTO paymentDTO )` | [CardPaymentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/payment/CardPaymentController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/payments/{paymentId}` | `public Mono<ResponseEntity<CardPaymentDTO>> updatePayment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the payment record to update", required = true) @PathVariable UUID paymentId, @Parameter(description = "Updated data for the payment record", required = true, schema = @Schema(implementation = CardPaymentDTO.class)) @RequestBody CardPaymentDTO paymentDTO )` | [CardPaymentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/payment/CardPaymentController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/payments/{paymentId}` | `public Mono<ResponseEntity<Void>> deletePayment( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the payment record to delete", required = true) @PathVariable UUID paymentId )` | [CardPaymentController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/payment/CardPaymentController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/physical-cards` | `public Mono<ResponseEntity<PaginationResponse<PhysicalCardDTO>>> getAllPhysicalCards( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [PhysicalCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/physical/PhysicalCardController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/physical-cards/{physicalCardId}` | `public Mono<ResponseEntity<PhysicalCardDTO>> getPhysicalCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the physical card record", required = true) @PathVariable UUID physicalCardId )` | [PhysicalCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/physical/PhysicalCardController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/physical-cards` | `public Mono<ResponseEntity<PhysicalCardDTO>> createPhysicalCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new physical card record", required = true, schema = @Schema(implementation = PhysicalCardDTO.class)) @RequestBody PhysicalCardDTO physicalCardDTO )` | [PhysicalCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/physical/PhysicalCardController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/physical-cards/{physicalCardId}` | `public Mono<ResponseEntity<PhysicalCardDTO>> updatePhysicalCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the physical card record to update", required = true) @PathVariable UUID physicalCardId, @Parameter(description = "Updated data for the physical card record", required = true, schema = @Schema(implementation = PhysicalCardDTO.class)) @RequestBody PhysicalCardDTO physicalCardDTO )` | [PhysicalCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/physical/PhysicalCardController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/physical-cards/{physicalCardId}` | `public Mono<ResponseEntity<Void>> deletePhysicalCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the physical card record to delete", required = true) @PathVariable UUID physicalCardId )` | [PhysicalCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/physical/PhysicalCardController.java) | Local declaration |
| `GET /api/v1/programs` | `public Mono<ResponseEntity<PaginationResponse<CardProgramDTO>>> getAllPrograms( @RequestBody PaginationRequest paginationRequest )` | [CardProgramController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/program/CardProgramController.java) | Local declaration |
| `GET /api/v1/programs/{programId}` | `public Mono<ResponseEntity<CardProgramDTO>> getProgram( @Parameter(description = "Unique identifier of the card program", required = true) @PathVariable UUID programId )` | [CardProgramController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/program/CardProgramController.java) | Local declaration |
| `POST /api/v1/programs` | `public Mono<ResponseEntity<CardProgramDTO>> createProgram( @Parameter(description = "Data for the new card program", required = true, schema = @Schema(implementation = CardProgramDTO.class)) @RequestBody CardProgramDTO programDTO )` | [CardProgramController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/program/CardProgramController.java) | Local declaration |
| `PUT /api/v1/programs/{programId}` | `public Mono<ResponseEntity<CardProgramDTO>> updateProgram( @Parameter(description = "Unique identifier of the card program to update", required = true) @PathVariable UUID programId, @Parameter(description = "Updated data for the card program", required = true, schema = @Schema(implementation = CardProgramDTO.class)) @RequestBody CardProgramDTO programDTO )` | [CardProgramController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/program/CardProgramController.java) | Local declaration |
| `DELETE /api/v1/programs/{programId}` | `public Mono<ResponseEntity<Void>> deleteProgram( @Parameter(description = "Unique identifier of the card program to delete", required = true) @PathVariable UUID programId )` | [CardProgramController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/program/CardProgramController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/promotions` | `public Mono<ResponseEntity<PaginationResponse<CardPromotionDTO>>> getAllPromotions( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardPromotionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/promotion/CardPromotionController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/promotions/{promotionId}` | `public Mono<ResponseEntity<CardPromotionDTO>> getPromotion( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the promotion record", required = true) @PathVariable UUID promotionId )` | [CardPromotionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/promotion/CardPromotionController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/promotions` | `public Mono<ResponseEntity<CardPromotionDTO>> createPromotion( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card promotion record", required = true, schema = @Schema(implementation = CardPromotionDTO.class)) @RequestBody CardPromotionDTO promotionDTO )` | [CardPromotionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/promotion/CardPromotionController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/promotions/{promotionId}` | `public Mono<ResponseEntity<CardPromotionDTO>> updatePromotion( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the promotion record to update", required = true) @PathVariable UUID promotionId, @Parameter(description = "Updated data for the promotion record", required = true, schema = @Schema(implementation = CardPromotionDTO.class)) @RequestBody CardPromotionDTO promotionDTO )` | [CardPromotionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/promotion/CardPromotionController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/promotions/{promotionId}` | `public Mono<ResponseEntity<Void>> deletePromotion( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the promotion record to delete", required = true) @PathVariable UUID promotionId )` | [CardPromotionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/promotion/CardPromotionController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/providers` | `public Mono<ResponseEntity<PaginationResponse<CardProviderDTO>>> getAllProviders( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardProviderController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/provider/CardProviderController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/providers/{providerId}` | `public Mono<ResponseEntity<CardProviderDTO>> getProvider( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card provider record", required = true) @PathVariable UUID providerId )` | [CardProviderController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/provider/CardProviderController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/providers` | `public Mono<ResponseEntity<CardProviderDTO>> createProvider( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card provider record", required = true, schema = @Schema(implementation = CardProviderDTO.class)) @RequestBody CardProviderDTO providerDTO )` | [CardProviderController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/provider/CardProviderController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/providers/{providerId}` | `public Mono<ResponseEntity<CardProviderDTO>> updateProvider( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card provider record to update", required = true) @PathVariable UUID providerId, @Parameter(description = "Updated card provider data", required = true, schema = @Schema(implementation = CardProviderDTO.class)) @RequestBody CardProviderDTO providerDTO )` | [CardProviderController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/provider/CardProviderController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/providers/{providerId}` | `public Mono<ResponseEntity<Void>> deleteProvider( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card provider record to delete", required = true) @PathVariable UUID providerId )` | [CardProviderController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/provider/CardProviderController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/security` | `public Mono<ResponseEntity<PaginationResponse<CardSecurityDTO>>> getAllSecuritySettings( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [CardSecurityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/security/CardSecurityController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/security/{securityId}` | `public Mono<ResponseEntity<CardSecurityDTO>> getSecuritySetting( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the security setting", required = true) @PathVariable UUID securityId )` | [CardSecurityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/security/CardSecurityController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/security` | `public Mono<ResponseEntity<CardSecurityDTO>> createSecuritySetting( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new security setting record", required = true, schema = @Schema(implementation = CardSecurityDTO.class)) @RequestBody CardSecurityDTO securityDTO )` | [CardSecurityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/security/CardSecurityController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/security/{securityId}` | `public Mono<ResponseEntity<CardSecurityDTO>> updateSecuritySetting( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the security setting to update", required = true) @PathVariable UUID securityId, @Parameter(description = "Updated security setting data", required = true, schema = @Schema(implementation = CardSecurityDTO.class)) @RequestBody CardSecurityDTO securityDTO )` | [CardSecurityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/security/CardSecurityController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/security/{securityId}` | `public Mono<ResponseEntity<Void>> deleteSecuritySetting( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the security setting record to delete", required = true) @PathVariable UUID securityId )` | [CardSecurityController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/security/CardSecurityController.java) | Local declaration |
| `GET /api/v1/terminals` | `public Mono<ResponseEntity<PaginationResponse<CardTerminalDTO>>> getAllTerminals( @RequestBody PaginationRequest paginationRequest )` | [CardTerminalController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/terminal/CardTerminalController.java) | Local declaration |
| `GET /api/v1/terminals/{terminalId}` | `public Mono<ResponseEntity<CardTerminalDTO>> getTerminal( @Parameter(description = "Unique identifier of the card terminal", required = true) @PathVariable UUID terminalId )` | [CardTerminalController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/terminal/CardTerminalController.java) | Local declaration |
| `POST /api/v1/terminals` | `public Mono<ResponseEntity<CardTerminalDTO>> createTerminal( @Parameter(description = "Data for the new card terminal", required = true, schema = @Schema(implementation = CardTerminalDTO.class)) @RequestBody CardTerminalDTO terminalDTO )` | [CardTerminalController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/terminal/CardTerminalController.java) | Local declaration |
| `PUT /api/v1/terminals/{terminalId}` | `public Mono<ResponseEntity<CardTerminalDTO>> updateTerminal( @Parameter(description = "Unique identifier of the card terminal to update", required = true) @PathVariable UUID terminalId, @Parameter(description = "Updated data for the card terminal", required = true, schema = @Schema(implementation = CardTerminalDTO.class)) @RequestBody CardTerminalDTO terminalDTO )` | [CardTerminalController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/terminal/CardTerminalController.java) | Local declaration |
| `DELETE /api/v1/terminals/{terminalId}` | `public Mono<ResponseEntity<Void>> deleteTerminal( @Parameter(description = "Unique identifier of the card terminal to delete", required = true) @PathVariable UUID terminalId )` | [CardTerminalController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/terminal/CardTerminalController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/transactions` | `public Mono<ResponseEntity<PaginationResponse<CardTransactionDTO>>> getAllTransactions( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @RequestBody PaginationRequest paginationRequest )` | [CardTransactionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/transaction/CardTransactionController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/transactions` | `public Mono<ResponseEntity<PaginationResponse<CardTransactionDTO>>> filterProducts( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @RequestBody FilterRequest<CardTransactionDTO> filterRequest )` | [CardTransactionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/transaction/CardTransactionController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/transactions/{transactionId}` | `public Mono<ResponseEntity<CardTransactionDTO>> getTransaction( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the transaction record", required = true) @PathVariable UUID transactionId )` | [CardTransactionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/transaction/CardTransactionController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/transactions` | `public Mono<ResponseEntity<CardTransactionDTO>> createTransaction( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new card transaction record", required = true, schema = @Schema(implementation = CardTransactionDTO.class)) @RequestBody CardTransactionDTO transactionDTO )` | [CardTransactionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/transaction/CardTransactionController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/transactions/{transactionId}` | `public Mono<ResponseEntity<CardTransactionDTO>> updateTransaction( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the card transaction to update", required = true) @PathVariable UUID transactionId, @Parameter(description = "Updated transaction data", required = true, schema = @Schema(implementation = CardTransactionDTO.class)) @RequestBody CardTransactionDTO transactionDTO )` | [CardTransactionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/transaction/CardTransactionController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/transactions/{transactionId}` | `public Mono<ResponseEntity<Void>> deleteTransaction( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the transaction record to delete", required = true) @PathVariable UUID transactionId )` | [CardTransactionController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/transaction/CardTransactionController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/virtual-cards` | `public Mono<ResponseEntity<PaginationResponse<VirtualCardDTO>>> getAllVirtualCards( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @ParameterObject @ModelAttribute PaginationRequest paginationRequest )` | [VirtualCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/virtual/VirtualCardController.java) | Local declaration |
| `GET /api/v1/cards/{cardId}/virtual-cards/{virtualCardId}` | `public Mono<ResponseEntity<VirtualCardDTO>> getVirtualCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the virtual card record", required = true) @PathVariable UUID virtualCardId )` | [VirtualCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/virtual/VirtualCardController.java) | Local declaration |
| `POST /api/v1/cards/{cardId}/virtual-cards` | `public Mono<ResponseEntity<VirtualCardDTO>> createVirtualCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Data for the new virtual card record", required = true, schema = @Schema(implementation = VirtualCardDTO.class)) @RequestBody VirtualCardDTO virtualCardDTO )` | [VirtualCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/virtual/VirtualCardController.java) | Local declaration |
| `PUT /api/v1/cards/{cardId}/virtual-cards/{virtualCardId}` | `public Mono<ResponseEntity<VirtualCardDTO>> updateVirtualCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the virtual card record to update", required = true) @PathVariable UUID virtualCardId, @Parameter(description = "Updated virtual card data", required = true, schema = @Schema(implementation = VirtualCardDTO.class)) @RequestBody VirtualCardDTO virtualCardDTO )` | [VirtualCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/virtual/VirtualCardController.java) | Local declaration |
| `DELETE /api/v1/cards/{cardId}/virtual-cards/{virtualCardId}` | `public Mono<ResponseEntity<Void>> deleteVirtualCard( @Parameter(description = "Unique identifier of the card", required = true) @PathVariable UUID cardId, @Parameter(description = "Unique identifier of the virtual card record to delete", required = true) @PathVariable UUID virtualCardId )` | [VirtualCardController](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/controllers/virtual/VirtualCardController.java) | Local declaration |

## Data contracts

### BaseDTO

[BaseDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/BaseDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `dateCreated` | `LocalDateTime` | See declaration / service rules |
| `dateUpdated` | `LocalDateTime` | See declaration / service rules |

### CardAcquirerDTO

[CardAcquirerDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/acquirer/CardAcquirerDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `acquirerId` | `UUID` | See declaration / service rules |
| `acquirerReference` | `String` | See declaration / service rules |
| `acquirerName` | `String` | See declaration / service rules |
| `acquirerLegalName` | `String` | See declaration / service rules |
| `acquirerCode` | `String` | See declaration / service rules |
| `acquirerDescription` | `String` | See declaration / service rules |
| `acquirerType` | `String` | See declaration / service rules |
| `acquirerStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `taxId` | `String` | See declaration / service rules |
| `registrationNumber` | `String` | See declaration / service rules |
| `licenseNumber` | `String` | See declaration / service rules |
| `licenseExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `websiteUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | See declaration / service rules |
| `contactPersonName` | `String` | See declaration / service rules |
| `contactPersonTitle` | `String` | See declaration / service rules |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCountries` | `String` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `isDomestic` | `Boolean` | See declaration / service rules |
| `isOnline` | `Boolean` | See declaration / service rules |
| `isPhysical` | `Boolean` | See declaration / service rules |
| `isMobile` | `Boolean` | See declaration / service rules |
| `processorId` | `UUID` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `gatewayId` | `UUID` | See declaration / service rules |
| `gatewayName` | `String` | See declaration / service rules |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `merchantCount` | `Integer` | See declaration / service rules |
| `terminalCount` | `Integer` | See declaration / service rules |
| `transactionVolumeDailyAvg` | `Long` | See declaration / service rules |
| `transactionValueDailyAvg` | `Long` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDelayDays` | `Integer` | See declaration / service rules |
| `feeStructure` | `String` | See declaration / service rules |
| `interchangeFeePercentage` | `Double` | See declaration / service rules |
| `processingFeePercentage` | `Double` | See declaration / service rules |
| `processingFeeFlat` | `Double` | See declaration / service rules |
| `chargebackFee` | `Double` | See declaration / service rules |
| `refundFee` | `Double` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `contractRenewalDate` | `LocalDateTime` | See declaration / service rules |
| `contractStatus` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `isEmvCompliant` | `Boolean` | See declaration / service rules |
| `emvComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `lastAuditDate` | `LocalDateTime` | See declaration / service rules |
| `nextAuditDate` | `LocalDateTime` | See declaration / service rules |

### CardActivityDTO

[CardActivityDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/activity/CardActivityDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `activityId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `activityReference` | `String` | `@NotBlank(message = "Activity reference is required")`; `@Size(max = 100, message = "Activity reference cannot exceed 100 characters")` |
| `activityType` | `String` | `@NotBlank(message = "Activity type is required")`; `@Size(max = 50, message = "Activity type cannot exceed 50 characters")` |
| `activityCategory` | `String` | `@Size(max = 50, message = "Activity category cannot exceed 50 characters")` |
| `activityDescription` | `String` | `@Size(max = 500, message = "Activity description cannot exceed 500 characters")` |
| `activityTimestamp` | `LocalDateTime` | `@NotNull(message = "Activity timestamp is required")` |
| `activityChannel` | `String` | `@Size(max = 50, message = "Activity channel cannot exceed 50 characters")` |
| `activitySource` | `String` | `@Size(max = 50, message = "Activity source cannot exceed 50 characters")` |
| `activityStatus` | `String` | `@Size(max = 50, message = "Activity status cannot exceed 50 characters")` |
| `activityResult` | `String` | `@Size(max = 50, message = "Activity result cannot exceed 50 characters")` |
| `activityDetails` | `String` | `@Size(max = 1000, message = "Activity details cannot exceed 1000 characters")` |
| `previousValue` | `String` | `@Size(max = 500, message = "Previous value cannot exceed 500 characters")` |
| `newValue` | `String` | `@Size(max = 500, message = "New value cannot exceed 500 characters")` |
| `changeReason` | `String` | `@Size(max = 255, message = "Change reason cannot exceed 255 characters")` |
| `changeAuthorizedBy` | `String` | `@Size(max = 100, message = "Change authorized by cannot exceed 100 characters")` |
| `ipAddress` | `String` | See declaration / service rules |
| `deviceId` | `String` | `@Size(max = 100, message = "Device ID cannot exceed 100 characters")` |
| `deviceType` | `String` | `@Size(max = 50, message = "Device type cannot exceed 50 characters")` |
| `deviceOs` | `String` | `@Size(max = 50, message = "Device OS cannot exceed 50 characters")` |
| `browserType` | `String` | `@Size(max = 50, message = "Browser type cannot exceed 50 characters")` |
| `userAgent` | `String` | `@Size(max = 500, message = "User agent cannot exceed 500 characters")` |
| `geolocation` | `String` | `@Size(max = 100, message = "Geolocation cannot exceed 100 characters")` |
| `isPartyInitiated` | `Boolean` | `@NotNull(message = "Party initiated flag is required")` |
| `isSystemInitiated` | `Boolean` | `@NotNull(message = "System initiated flag is required")` |
| `isAgentInitiated` | `Boolean` | `@NotNull(message = "Agent initiated flag is required")` |
| `agentId` | `String` | `@Size(max = 50, message = "Agent ID cannot exceed 50 characters")` |
| `agentName` | `String` | `@Size(max = 100, message = "Agent name cannot exceed 100 characters")` |
| `isSuccessful` | `Boolean` | `@NotNull(message = "Successful flag is required")` |
| `failureReason` | `String` | `@Size(max = 255, message = "Failure reason cannot exceed 255 characters")` |
| `failureCode` | `String` | `@Size(max = 50, message = "Failure code cannot exceed 50 characters")` |
| `isNotificationSent` | `Boolean` | `@NotNull(message = "Notification sent flag is required")` |
| `notificationChannel` | `String` | `@Size(max = 50, message = "Notification channel cannot exceed 50 characters")` |
| `notificationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notificationRecipient` | `String` | `@Size(max = 100, message = "Notification recipient cannot exceed 100 characters")` |
| `relatedEntityType` | `String` | `@Size(max = 50, message = "Related entity type cannot exceed 50 characters")` |
| `relatedEntityId` | `UUID` | See declaration / service rules |
| `notes` | `String` | `@Size(max = 1000, message = "Notes cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardAlertDTO

[CardAlertDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/alert/CardAlertDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `alertId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `alertReference` | `String` | `@NotBlank(message = "Alert reference is required")`; `@Size(min = 5, max = 50, message = "Alert reference must be between 5 and 50 characters")` |
| `alertType` | `String` | `@NotBlank(message = "Alert type is required")`; `@Pattern(regexp = "^(TRANSACTION\|SECURITY\|LIMIT\|PAYMENT\|FRAUD\|SYSTEM)$", message = "Alert type must be TRANSACTION, SECURITY, LIMIT, PAYMENT, FRAUD, or SYSTEM")` |
| `alertCategory` | `String` | `@NotBlank(message = "Alert category is required")`; `@Pattern(regexp = "^(HIGH_VALUE\|UNUSUAL_ACTIVITY\|DECLINED\|LIMIT_EXCEEDED\|FRAUD_SUSPECTED)$", message = "Alert category must be HIGH_VALUE, UNUSUAL_ACTIVITY, DECLINED, LIMIT_EXCEEDED, or FRAUD_SUSPECTED")` |
| `alertSeverity` | `String` | `@NotBlank(message = "Alert severity is required")`; `@Pattern(regexp = "^(LOW\|MEDIUM\|HIGH\|CRITICAL)$", message = "Alert severity must be LOW, MEDIUM, HIGH, or CRITICAL")` |
| `alertStatus` | `String` | `@NotBlank(message = "Alert status is required")`; `@Pattern(regexp = "^(ACTIVE\|ACKNOWLEDGED\|RESOLVED\|DISMISSED)$", message = "Alert status must be ACTIVE, ACKNOWLEDGED, RESOLVED, or DISMISSED")` |
| `alertTitle` | `String` | `@NotBlank(message = "Alert title is required")`; `@Size(min = 5, max = 100, message = "Alert title must be between 5 and 100 characters")` |
| `alertMessage` | `String` | `@NotBlank(message = "Alert message is required")`; `@Size(min = 10, max = 500, message = "Alert message must be between 10 and 500 characters")` |
| `alertDetails` | `String` | `@Size(max = 1000, message = "Alert details cannot exceed 1000 characters")` |
| `triggerEvent` | `String` | `@NotBlank(message = "Trigger event is required")`; `@Size(min = 5, max = 100, message = "Trigger event must be between 5 and 100 characters")` |
| `triggerTimestamp` | `LocalDateTime` | `@NotNull(message = "Trigger timestamp is required")` |
| `triggerAmount` | `BigDecimal` | See declaration / service rules |
| `triggerCurrency` | `String` | See declaration / service rules |
| `triggerLocation` | `String` | See declaration / service rules |
| `triggerMerchant` | `String` | See declaration / service rules |
| `triggerMerchantCategory` | `String` | See declaration / service rules |
| `isPartyConfigurable` | `Boolean` | See declaration / service rules |
| `isSystemGenerated` | `Boolean` | See declaration / service rules |
| `isRead` | `Boolean` | See declaration / service rules |
| `readTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deliveryChannel` | `String` | See declaration / service rules |
| `deliveryStatus` | `String` | See declaration / service rules |
| `deliveryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deliveryRecipient` | `String` | See declaration / service rules |
| `deliveryDetails` | `String` | See declaration / service rules |
| `expirationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `actionRequired` | `Boolean` | See declaration / service rules |
| `actionType` | `String` | See declaration / service rules |
| `actionTaken` | `String` | See declaration / service rules |
| `actionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `actionBy` | `String` | See declaration / service rules |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardApplicationDTO

[CardApplicationDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/application/CardApplicationDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `applicationId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `applicationReference` | `String` | `@NotBlank(message = "Application reference is required")`; `@Size(min = 5, max = 50, message = "Application reference must be between 5 and 50 characters")` |
| `applicationType` | `String` | `@NotBlank(message = "Application type is required")`; `@Pattern(regexp = "^(NEW\|REPLACEMENT\|UPGRADE\|ADDITIONAL)$", message = "Application type must be NEW, REPLACEMENT, UPGRADE, or ADDITIONAL")` |
| `cardTypeId` | `UUID` | `@NotNull(message = "Card type ID is required")` |
| `programId` | `UUID` | `@NotNull(message = "Program ID is required")` |
| `designId` | `UUID` | See declaration / service rules |
| `applicationStatus` | `String` | `@NotBlank(message = "Application status is required")`; `@Pattern(regexp = "^(PENDING\|APPROVED\|REJECTED\|CANCELLED\|PROCESSING)$", message = "Application status must be PENDING, APPROVED, REJECTED, CANCELLED, or PROCESSING")` |
| `applicationStage` | `String` | `@NotBlank(message = "Application stage is required")`; `@Pattern(regexp = "^(SUBMITTED\|VERIFICATION\|APPROVAL\|ISSUANCE\|COMPLETED)$", message = "Application stage must be SUBMITTED, VERIFICATION, APPROVAL, ISSUANCE, or COMPLETED")` |
| `applicationChannel` | `String` | `@NotBlank(message = "Application channel is required")`; `@Pattern(regexp = "^(ONLINE\|MOBILE\|BRANCH\|PHONE\|MAIL)$", message = "Application channel must be ONLINE, MOBILE, BRANCH, PHONE, or MAIL")` |
| `applicationTimestamp` | `LocalDateTime` | `@NotNull(message = "Application timestamp is required")` |
| `applicantFirstName` | `String` | `@NotBlank(message = "Applicant first name is required")`; `@Size(min = 1, max = 50, message = "Applicant first name must be between 1 and 50 characters")` |
| `applicantMiddleName` | `String` | `@Size(max = 50, message = "Applicant middle name cannot exceed 50 characters")` |
| `applicantLastName` | `String` | `@NotBlank(message = "Applicant last name is required")`; `@Size(min = 1, max = 50, message = "Applicant last name must be between 1 and 50 characters")` |
| `applicantEmail` | `String` | `@NotBlank(message = "Applicant email is required")`; `@Email(message = "Applicant email must be a valid email address")`; `@Size(max = 100, message = "Applicant email cannot exceed 100 characters")` |
| `applicantPhone` | `String` | See declaration / service rules |
| `applicantAddressLine1` | `String` | `@Size(max = 100, message = "Applicant address line 1 cannot exceed 100 characters")` |
| `applicantAddressLine2` | `String` | `@Size(max = 100, message = "Applicant address line 2 cannot exceed 100 characters")` |
| `applicantCity` | `String` | `@Size(max = 50, message = "Applicant city cannot exceed 50 characters")` |
| `applicantState` | `String` | `@Size(max = 50, message = "Applicant state cannot exceed 50 characters")` |
| `applicantPostalCode` | `String` | `@Size(max = 20, message = "Applicant postal code cannot exceed 20 characters")` |
| `applicantCountry` | `String` | See declaration / service rules |
| `applicantIdType` | `String` | `@Size(max = 50, message = "Applicant ID type cannot exceed 50 characters")` |
| `applicantIdNumber` | `String` | `@Size(max = 50, message = "Applicant ID number cannot exceed 50 characters")` |
| `applicantIdExpiry` | `LocalDateTime` | See declaration / service rules |
| `applicantDateOfBirth` | `LocalDateTime` | See declaration / service rules |
| `applicantNationality` | `String` | See declaration / service rules |
| `employmentStatus` | `String` | `@Size(max = 50, message = "Employment status cannot exceed 50 characters")` |
| `employerName` | `String` | `@Size(max = 100, message = "Employer name cannot exceed 100 characters")` |
| `employmentPosition` | `String` | `@Size(max = 100, message = "Employment position cannot exceed 100 characters")` |
| `annualIncome` | `BigDecimal` | See declaration / service rules |
| `incomeCurrency` | `String` | See declaration / service rules |
| `requestedCreditLimit` | `BigDecimal` | See declaration / service rules |
| `approvedCreditLimit` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `isPreApproved` | `Boolean` | `@NotNull(message = "Pre-approved flag is required")` |
| `isInstantIssuance` | `Boolean` | `@NotNull(message = "Instant issuance flag is required")` |
| `isDigitalOnly` | `Boolean` | `@NotNull(message = "Digital only flag is required")` |
| `requiresPhysicalCard` | `Boolean` | `@NotNull(message = "Requires physical card flag is required")` |
| `shippingAddressSameAsResidential` | `Boolean` | `@NotNull(message = "Shipping address same as residential flag is required")` |
| `shippingAddressLine1` | `String` | `@Size(max = 100, message = "Shipping address line 1 cannot exceed 100 characters")` |
| `shippingAddressLine2` | `String` | `@Size(max = 100, message = "Shipping address line 2 cannot exceed 100 characters")` |
| `shippingCity` | `String` | `@Size(max = 50, message = "Shipping city cannot exceed 50 characters")` |
| `shippingState` | `String` | `@Size(max = 50, message = "Shipping state cannot exceed 50 characters")` |
| `shippingPostalCode` | `String` | `@Size(max = 20, message = "Shipping postal code cannot exceed 20 characters")` |
| `shippingCountry` | `String` | See declaration / service rules |
| `shippingMethod` | `String` | `@Size(max = 50, message = "Shipping method cannot exceed 50 characters")` |
| `creditScore` | `Integer` | `@Min(value = 300, message = "Credit score must be at least 300")`; `@Max(value = 850, message = "Credit score cannot exceed 850")` |
| `creditScoreProvider` | `String` | `@Size(max = 50, message = "Credit score provider cannot exceed 50 characters")` |
| `creditCheckTimestamp` | `LocalDateTime` | See declaration / service rules |
| `creditCheckReference` | `String` | `@Size(max = 100, message = "Credit check reference cannot exceed 100 characters")` |
| `kycStatus` | `String` | `@Size(max = 50, message = "KYC status cannot exceed 50 characters")` |
| `kycReference` | `String` | `@Size(max = 100, message = "KYC reference cannot exceed 100 characters")` |
| `kycTimestamp` | `LocalDateTime` | See declaration / service rules |
| `amlStatus` | `String` | `@Size(max = 50, message = "AML status cannot exceed 50 characters")` |
| `amlReference` | `String` | `@Size(max = 100, message = "AML reference cannot exceed 100 characters")` |
| `amlTimestamp` | `LocalDateTime` | See declaration / service rules |
| `approvalTimestamp` | `LocalDateTime` | See declaration / service rules |
| `approvedBy` | `String` | `@Size(max = 100, message = "Approved by cannot exceed 100 characters")` |
| `rejectionReason` | `String` | `@Size(max = 255, message = "Rejection reason cannot exceed 255 characters")` |
| `rejectionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `rejectionCode` | `String` | `@Size(max = 50, message = "Rejection code cannot exceed 50 characters")` |
| `cardId` | `UUID` | See declaration / service rules |
| `cardIssuanceTimestamp` | `LocalDateTime` | See declaration / service rules |
| `termsAccepted` | `Boolean` | `@NotNull(message = "Terms accepted flag is required")` |
| `termsAcceptedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `termsVersion` | `String` | `@Size(max = 20, message = "Terms version cannot exceed 20 characters")` |
| `supportingDocuments` | `String` | `@Size(max = 1000, message = "Supporting documents cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@Email`, `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardBalanceDTO

[CardBalanceDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/balance/CardBalanceDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `balanceId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `statementId` | `UUID` | See declaration / service rules |
| `balanceType` | `String` | `@NotBlank(message = "Balance type is required")`; `@Pattern(regexp = "^(CURRENT\|AVAILABLE\|PENDING\|CREDIT\|DEBIT)$", message = "Balance type must be CURRENT, AVAILABLE, PENDING, CREDIT, or DEBIT")` |
| `balanceCategory` | `String` | `@NotBlank(message = "Balance category is required")`; `@Pattern(regexp = "^(PURCHASE\|CASH_ADVANCE\|BALANCE_TRANSFER\|FEE\|INTEREST)$", message = "Balance category must be PURCHASE, CASH_ADVANCE, BALANCE_TRANSFER, FEE, or INTEREST")` |
| `balanceDescription` | `String` | `@Size(max = 500, message = "Balance description cannot exceed 500 characters")` |
| `balanceAmount` | `BigDecimal` | `@NotNull(message = "Balance amount is required")` |
| `availableAmount` | `BigDecimal` | See declaration / service rules |
| `reservedAmount` | `BigDecimal` | See declaration / service rules |
| `pendingAmount` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `asOfDate` | `LocalDateTime` | `@NotNull(message = "As of date is required")` |
| `lastTransactionId` | `UUID` | See declaration / service rules |
| `lastTransactionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `lastPaymentId` | `UUID` | See declaration / service rules |
| `lastPaymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `lastStatementId` | `UUID` | See declaration / service rules |
| `lastStatementDate` | `LocalDateTime` | See declaration / service rules |
| `isPromotionalRate` | `Boolean` | `@NotNull(message = "Promotional rate flag is required")` |
| `promotionId` | `UUID` | See declaration / service rules |
| `promotionEndDate` | `LocalDateTime` | See declaration / service rules |
| `interestRate` | `BigDecimal` | See declaration / service rules |
| `annualPercentageRate` | `BigDecimal` | See declaration / service rules |
| `accruedInterest` | `BigDecimal` | See declaration / service rules |
| `lastInterestAccrualDate` | `LocalDateTime` | See declaration / service rules |
| `nextInterestAccrualDate` | `LocalDateTime` | See declaration / service rules |
| `isInGracePeriod` | `Boolean` | `@NotNull(message = "Grace period flag is required")` |
| `gracePeriodEndDate` | `LocalDateTime` | See declaration / service rules |
| `minimumPaymentDue` | `BigDecimal` | See declaration / service rules |
| `minimumPaymentDueDate` | `LocalDateTime` | See declaration / service rules |
| `daysPastDue` | `Integer` | `@Min(value = 0, message = "Days past due cannot be negative")`; `@Max(value = 9999, message = "Days past due cannot exceed 9999")` |
| `isDelinquent` | `Boolean` | `@NotNull(message = "Delinquent flag is required")` |
| `delinquencyStartDate` | `LocalDateTime` | See declaration / service rules |
| `delinquencyDays` | `Integer` | `@Min(value = 0, message = "Delinquency days cannot be negative")`; `@Max(value = 9999, message = "Delinquency days cannot exceed 9999")` |
| `delinquencyStage` | `String` | `@Size(max = 50, message = "Delinquency stage cannot exceed 50 characters")` |
| `isChargedOff` | `Boolean` | `@NotNull(message = "Charged off flag is required")` |
| `chargeOffDate` | `LocalDateTime` | See declaration / service rules |
| `chargeOffAmount` | `BigDecimal` | See declaration / service rules |
| `isWrittenOff` | `Boolean` | `@NotNull(message = "Written off flag is required")` |
| `writeOffDate` | `LocalDateTime` | See declaration / service rules |
| `writeOffAmount` | `BigDecimal` | See declaration / service rules |
| `isInCollection` | `Boolean` | `@NotNull(message = "In collection flag is required")` |
| `collectionStartDate` | `LocalDateTime` | See declaration / service rules |
| `collectionAgencyId` | `String` | `@Size(max = 50, message = "Collection agency ID cannot exceed 50 characters")` |
| `collectionReference` | `String` | `@Size(max = 100, message = "Collection reference cannot exceed 100 characters")` |
| `collectionStatus` | `String` | `@Size(max = 50, message = "Collection status cannot exceed 50 characters")` |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### BINDTO

[BINDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/bin/BINDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `binId` | `UUID` | See declaration / service rules |
| `binNumber` | `String` | `@NotBlank(message = "BIN number is required")` |
| `binLength` | `Integer` | `@NotNull(message = "BIN length is required")`; `@Min(value = 6, message = "BIN length must be at least 6")`; `@Max(value = 8, message = "BIN length cannot exceed 8")` |
| `issuerId` | `UUID` | `@NotNull(message = "Issuer ID is required")` |
| `cardNetworkId` | `UUID` | `@NotNull(message = "Card network ID is required")` |
| `cardTypeId` | `UUID` | `@NotNull(message = "Card type ID is required")` |
| `countryCode` | `String` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardActivationReceipt

[CardActivationReceipt](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/card/CardActivationReceipt.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `actionId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `state` | `State` | See declaration / service rules |
| `activationTime` | `OffsetDateTime` | See declaration / service rules |

### CardActivationRequest

[CardActivationRequest](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/card/CardActivationRequest.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `activationProof` | `String` | See declaration / service rules |

Validation annotations present in this declaration: `@NotBlank`, `@Pattern`. Inspect the linked source for the field-level constraints.

### CardDTO

[CardDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/card/CardDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardId` | `UUID` | See declaration / service rules |
| `cardNumber` | `String` | `@NotBlank(message = "Card number is required")` |
| `maskedCardNumber` | `String` | See declaration / service rules |
| `cardSequenceNumber` | `String` | See declaration / service rules |
| `binId` | `UUID` | `@NotNull(message = "BIN ID is required")` |
| `cardTypeId` | `UUID` | `@NotNull(message = "Card type ID is required")` |
| `cardNetworkId` | `UUID` | `@NotNull(message = "Card network ID is required")` |
| `issuerId` | `UUID` | `@NotNull(message = "Issuer ID is required")` |
| `contractId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `cardStatus` | `CardStatusEnum` | `@NotNull(message = "Card status is required")` |
| `cardHolderName` | `String` | `@NotBlank(message = "Card holder name is required")`; `@Size(min = 2, max = 100, message = "Card holder name must be between 2 and 100 characters")` |
| `cardHolderId` | `String` | `@NotBlank(message = "Card holder ID is required")`; `@Size(min = 5, max = 50, message = "Card holder ID must be between 5 and 50 characters")` |
| `expirationMonth` | `Integer` | `@NotNull(message = "Expiration month is required")`; `@Min(value = 1, message = "Expiration month must be between 1 and 12")`; `@Max(value = 12, message = "Expiration month must be between 1 and 12")` |
| `expirationYear` | `Integer` | `@NotNull(message = "Expiration year is required")`; `@Min(value = 2024, message = "Expiration year must be current year or later")`; `@Max(value = 2050, message = "Expiration year cannot exceed 2050")` |
| `cvv` | `String` | See declaration / service rules |
| `pin` | `String` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `issuanceDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `lastUsedDate` | `LocalDateTime` | See declaration / service rules |
| `isPhysical` | `Boolean` | `@NotNull(message = "Physical card flag is required")` |
| `isVirtual` | `Boolean` | `@NotNull(message = "Virtual card flag is required")` |
| `isPrimary` | `Boolean` | `@NotNull(message = "Primary card flag is required")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `isLocked` | `Boolean` | `@NotNull(message = "Locked flag is required")` |
| `lockReason` | `String` | `@Size(max = 255, message = "Lock reason cannot exceed 255 characters")` |
| `dailyLimit` | `Double` | See declaration / service rules |
| `monthlyLimit` | `Double` | See declaration / service rules |
| `creditLimit` | `Double` | See declaration / service rules |
| `availableBalance` | `Double` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `designId` | `UUID` | See declaration / service rules |
| `notes` | `String` | `@Size(max = 1000, message = "Notes cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardTypeDTO

[CardTypeDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/cardtype/CardTypeDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `typeName` | `String` | `@NotBlank(message = "Type name is required")`; `@Size(min = 2, max = 100, message = "Type name must be between 2 and 100 characters")` |
| `typeCode` | `String` | See declaration / service rules |
| `isCredit` | `Boolean` | `@NotNull(message = "Credit card flag is required")` |
| `isDebit` | `Boolean` | `@NotNull(message = "Debit card flag is required")` |
| `isPrepaid` | `Boolean` | `@NotNull(message = "Prepaid card flag is required")` |
| `isVirtual` | `Boolean` | `@NotNull(message = "Virtual card flag is required")` |
| `isCommercial` | `Boolean` | `@NotNull(message = "Commercial card flag is required")` |
| `isGift` | `Boolean` | `@NotNull(message = "Gift card flag is required")` |
| `defaultCreditLimit` | `Double` | See declaration / service rules |
| `defaultDailyLimit` | `Double` | See declaration / service rules |
| `defaultMonthlyLimit` | `Double` | See declaration / service rules |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardConfigurationDTO

[CardConfigurationDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/configuration/CardConfigurationDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardConfigurationId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `configKey` | `String` | See declaration / service rules |
| `configValue` | `String` | See declaration / service rules |
| `configType` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `isSystemDefault` | `Boolean` | See declaration / service rules |
| `isProgramDefault` | `Boolean` | See declaration / service rules |
| `isPartyConfigurable` | `Boolean` | See declaration / service rules |
| `allowedValues` | `String` | See declaration / service rules |
| `minValue` | `String` | See declaration / service rules |
| `maxValue` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardDesignDTO

[CardDesignDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/design/CardDesignDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `designId` | `UUID` | See declaration / service rules |
| `designName` | `String` | `@NotBlank(message = "Design name is required")`; `@Size(min = 2, max = 100, message = "Design name must be between 2 and 100 characters")` |
| `designCode` | `String` | See declaration / service rules |
| `frontImageUrl` | `String` | `@Pattern(regexp = "^https?://.*\\.(jpg\|jpeg\|png\|gif\|svg)$", message = "Front image URL must be a valid HTTP/HTTPS URL pointing to an image file")` |
| `backImageUrl` | `String` | `@Pattern(regexp = "^https?://.*\\.(jpg\|jpeg\|png\|gif\|svg)$", message = "Back image URL must be a valid HTTP/HTTPS URL pointing to an image file")` |
| `cardTypeId` | `UUID` | `@NotNull(message = "Card type ID is required")` |
| `issuerId` | `UUID` | `@NotNull(message = "Issuer ID is required")` |
| `cardNetworkId` | `UUID` | `@NotNull(message = "Card network ID is required")` |
| `isCustomizable` | `Boolean` | `@NotNull(message = "Customizable flag is required")` |
| `isDefault` | `Boolean` | `@NotNull(message = "Default flag is required")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardDisputeDTO

[CardDisputeDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/dispute/CardDisputeDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `disputeId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `disputeReference` | `String` | See declaration / service rules |
| `providerReference` | `String` | See declaration / service rules |
| `networkReference` | `String` | See declaration / service rules |
| `disputeReasonCode` | `String` | See declaration / service rules |
| `disputeReasonDescription` | `String` | See declaration / service rules |
| `disputeStatus` | `String` | See declaration / service rules |
| `disputeStage` | `String` | See declaration / service rules |
| `disputeAmount` | `BigDecimal` | See declaration / service rules |
| `disputeCurrency` | `String` | See declaration / service rules |
| `filingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `responseDueDate` | `LocalDateTime` | See declaration / service rules |
| `resolutionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `resolutionCode` | `String` | See declaration / service rules |
| `resolutionDescription` | `String` | See declaration / service rules |
| `isCardholderCredited` | `Boolean` | See declaration / service rules |
| `creditTimestamp` | `LocalDateTime` | See declaration / service rules |
| `creditAmount` | `BigDecimal` | See declaration / service rules |
| `creditCurrency` | `String` | See declaration / service rules |
| `isMerchantDebited` | `Boolean` | See declaration / service rules |
| `debitTimestamp` | `LocalDateTime` | See declaration / service rules |
| `debitAmount` | `BigDecimal` | See declaration / service rules |
| `debitCurrency` | `String` | See declaration / service rules |
| `cardholderStatement` | `String` | See declaration / service rules |
| `merchantResponse` | `String` | See declaration / service rules |
| `evidenceDocuments` | `String` | See declaration / service rules |
| `assignedAgentId` | `UUID` | See declaration / service rules |
| `assignedAgentName` | `String` | See declaration / service rules |
| `lastUpdatedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardEnrollmentDTO

[CardEnrollmentDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/enrollment/CardEnrollmentDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `enrollmentId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `enrollmentType` | `String` | See declaration / service rules |
| `enrollmentReference` | `String` | See declaration / service rules |
| `enrollmentStatus` | `String` | See declaration / service rules |
| `enrollmentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `enrollmentChannel` | `String` | See declaration / service rules |
| `enrollmentSource` | `String` | See declaration / service rules |
| `enrollmentAgent` | `String` | See declaration / service rules |
| `promotionId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `featureId` | `UUID` | See declaration / service rules |
| `featureCode` | `String` | See declaration / service rules |
| `featureName` | `String` | See declaration / service rules |
| `isAutoEnrolled` | `Boolean` | See declaration / service rules |
| `isOptIn` | `Boolean` | See declaration / service rules |
| `isOptOut` | `Boolean` | See declaration / service rules |
| `optInTimestamp` | `LocalDateTime` | See declaration / service rules |
| `optOutTimestamp` | `LocalDateTime` | See declaration / service rules |
| `optOutReason` | `String` | See declaration / service rules |
| `startDate` | `LocalDateTime` | See declaration / service rules |
| `endDate` | `LocalDateTime` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deactivationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `isRecurring` | `Boolean` | See declaration / service rules |
| `recurrenceFrequency` | `String` | See declaration / service rules |
| `nextRecurrenceDate` | `LocalDateTime` | See declaration / service rules |
| `lastRecurrenceDate` | `LocalDateTime` | See declaration / service rules |
| `termsAccepted` | `Boolean` | See declaration / service rules |
| `termsAcceptedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `termsVersion` | `String` | See declaration / service rules |
| `confirmationCode` | `String` | See declaration / service rules |
| `confirmationSent` | `Boolean` | See declaration / service rules |
| `confirmationChannel` | `String` | See declaration / service rules |
| `confirmationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `confirmationRecipient` | `String` | See declaration / service rules |

### CardFeeDTO

[CardFeeDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/fee/CardFeeDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `feeId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `transactionId` | `UUID` | See declaration / service rules |
| `statementId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `feeReference` | `String` | `@NotBlank(message = "Fee reference is required")`; `@Size(max = 100, message = "Fee reference cannot exceed 100 characters")` |
| `feeType` | `String` | `@NotBlank(message = "Fee type is required")`; `@Size(max = 50, message = "Fee type cannot exceed 50 characters")` |
| `feeName` | `String` | `@NotBlank(message = "Fee name is required")`; `@Size(min = 2, max = 100, message = "Fee name must be between 2 and 100 characters")` |
| `feeDescription` | `String` | `@Size(max = 500, message = "Fee description cannot exceed 500 characters")` |
| `feeAmount` | `BigDecimal` | `@NotNull(message = "Fee amount is required")` |
| `feePercentage` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `feeStatus` | `String` | `@NotBlank(message = "Fee status is required")`; `@Size(max = 50, message = "Fee status cannot exceed 50 characters")` |
| `isRecurring` | `Boolean` | `@NotNull(message = "Recurring flag is required")` |
| `recurrenceFrequency` | `String` | `@Size(max = 50, message = "Recurrence frequency cannot exceed 50 characters")` |
| `nextRecurrenceDate` | `LocalDateTime` | See declaration / service rules |
| `isProrated` | `Boolean` | `@NotNull(message = "Prorated flag is required")` |
| `prorationRule` | `String` | `@Size(max = 100, message = "Proration rule cannot exceed 100 characters")` |
| `isWaived` | `Boolean` | `@NotNull(message = "Waived flag is required")` |
| `waiverReason` | `String` | `@Size(max = 255, message = "Waiver reason cannot exceed 255 characters")` |
| `waiverReference` | `String` | `@Size(max = 100, message = "Waiver reference cannot exceed 100 characters")` |
| `waiverTimestamp` | `LocalDateTime` | See declaration / service rules |
| `waiverExpiry` | `LocalDateTime` | See declaration / service rules |
| `waiverAuthorizedBy` | `String` | `@Size(max = 100, message = "Waiver authorized by cannot exceed 100 characters")` |
| `isRefunded` | `Boolean` | `@NotNull(message = "Refunded flag is required")` |
| `refundReason` | `String` | `@Size(max = 255, message = "Refund reason cannot exceed 255 characters")` |
| `refundReference` | `String` | `@Size(max = 100, message = "Refund reference cannot exceed 100 characters")` |
| `refundTimestamp` | `LocalDateTime` | See declaration / service rules |
| `refundAmount` | `BigDecimal` | See declaration / service rules |
| `refundAuthorizedBy` | `String` | `@Size(max = 100, message = "Refund authorized by cannot exceed 100 characters")` |
| `chargeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `postingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `valueDate` | `LocalDateTime` | See declaration / service rules |
| `taxAmount` | `BigDecimal` | See declaration / service rules |
| `taxRate` | `BigDecimal` | See declaration / service rules |
| `taxType` | `String` | `@Size(max = 50, message = "Tax type cannot exceed 50 characters")` |
| `totalAmount` | `BigDecimal` | `@NotNull(message = "Total amount is required")` |
| `isBilled` | `Boolean` | `@NotNull(message = "Billed flag is required")` |
| `billingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isPaid` | `Boolean` | `@NotNull(message = "Paid flag is required")` |
| `paymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `paymentReference` | `String` | `@Size(max = 100, message = "Payment reference cannot exceed 100 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### FraudCaseDTO

[FraudCaseDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/fraud/FraudCaseDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `fraudCaseId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `caseReference` | `String` | See declaration / service rules |
| `providerReference` | `String` | See declaration / service rules |
| `networkReference` | `String` | See declaration / service rules |
| `fraudType` | `String` | See declaration / service rules |
| `fraudReasonCode` | `String` | See declaration / service rules |
| `fraudReasonDescription` | `String` | See declaration / service rules |
| `fraudStatus` | `String` | See declaration / service rules |
| `fraudScore` | `Integer` | See declaration / service rules |
| `riskLevel` | `String` | See declaration / service rules |
| `detectionSource` | `String` | See declaration / service rules |
| `detectionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `reportedByParty` | `Boolean` | See declaration / service rules |
| `reportedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `affectedAmount` | `BigDecimal` | See declaration / service rules |
| `affectedCurrency` | `String` | See declaration / service rules |
| `isCardBlocked` | `Boolean` | See declaration / service rules |
| `blockTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isPartyNotified` | `Boolean` | See declaration / service rules |
| `notificationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notificationMethod` | `String` | See declaration / service rules |
| `isCardholderCredited` | `Boolean` | See declaration / service rules |
| `creditTimestamp` | `LocalDateTime` | See declaration / service rules |
| `creditAmount` | `BigDecimal` | See declaration / service rules |
| `creditCurrency` | `String` | See declaration / service rules |
| `resolutionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `resolutionCode` | `String` | See declaration / service rules |
| `resolutionDescription` | `String` | See declaration / service rules |
| `isPoliceReportFiled` | `Boolean` | See declaration / service rules |
| `policeReportNumber` | `String` | See declaration / service rules |
| `policeReportDate` | `LocalDateTime` | See declaration / service rules |
| `evidenceDocuments` | `String` | See declaration / service rules |
| `assignedAgentId` | `UUID` | See declaration / service rules |
| `assignedAgentName` | `String` | See declaration / service rules |
| `lastUpdatedTimestamp` | `LocalDateTime` | See declaration / service rules |

### CardGatewayDTO

[CardGatewayDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/gateway/CardGatewayDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `gatewayId` | `UUID` | See declaration / service rules |
| `gatewayReference` | `String` | See declaration / service rules |
| `gatewayName` | `String` | See declaration / service rules |
| `gatewayLegalName` | `String` | See declaration / service rules |
| `gatewayCode` | `String` | See declaration / service rules |
| `gatewayDescription` | `String` | See declaration / service rules |
| `gatewayType` | `String` | See declaration / service rules |
| `gatewayStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `taxId` | `String` | See declaration / service rules |
| `registrationNumber` | `String` | See declaration / service rules |
| `licenseNumber` | `String` | See declaration / service rules |
| `licenseExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `websiteUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | See declaration / service rules |
| `contactPersonName` | `String` | See declaration / service rules |
| `contactPersonTitle` | `String` | See declaration / service rules |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCountries` | `String` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `isDomestic` | `Boolean` | See declaration / service rules |
| `processorId` | `UUID` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `acquirerId` | `UUID` | See declaration / service rules |
| `acquirerName` | `String` | See declaration / service rules |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `redirectUrl` | `String` | See declaration / service rules |
| `callbackUrl` | `String` | See declaration / service rules |
| `hostedPageUrl` | `String` | See declaration / service rules |
| `merchantCount` | `Integer` | See declaration / service rules |
| `transactionVolumeDailyAvg` | `Long` | See declaration / service rules |
| `transactionValueDailyAvg` | `Long` | See declaration / service rules |
| `processingTimeAvgMs` | `Integer` | See declaration / service rules |
| `uptimePercentage` | `Double` | See declaration / service rules |
| `lastDowntimeDate` | `LocalDateTime` | See declaration / service rules |
| `lastDowntimeDurationMinutes` | `Integer` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDelayDays` | `Integer` | See declaration / service rules |
| `feeStructure` | `String` | See declaration / service rules |
| `processingFeePercentage` | `Double` | See declaration / service rules |
| `processingFeeFlat` | `Double` | See declaration / service rules |
| `chargebackFee` | `Double` | See declaration / service rules |
| `refundFee` | `Double` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `contractRenewalDate` | `LocalDateTime` | See declaration / service rules |
| `contractStatus` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceLevel` | `String` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `supportsTokenization` | `Boolean` | See declaration / service rules |
| `supports3dSecure` | `Boolean` | See declaration / service rules |
| `supportsRecurringPayments` | `Boolean` | See declaration / service rules |
| `supportsSplitPayments` | `Boolean` | See declaration / service rules |
| `supportsRefunds` | `Boolean` | See declaration / service rules |
| `supportsPartialRefunds` | `Boolean` | See declaration / service rules |
| `supportsVoid` | `Boolean` | See declaration / service rules |
| `supportsCapture` | `Boolean` | See declaration / service rules |
| `supportsPreAuth` | `Boolean` | See declaration / service rules |
| `supportsApplePay` | `Boolean` | See declaration / service rules |
| `supportsGooglePay` | `Boolean` | See declaration / service rules |
| `supportsSamsungPay` | `Boolean` | See declaration / service rules |
| `supportsPaypal` | `Boolean` | See declaration / service rules |
| `integrationType` | `String` | See declaration / service rules |
| `integrationComplexity` | `String` | See declaration / service rules |
| `integrationDocumentationUrl` | `String` | See declaration / service rules |
| `sdkAvailable` | `Boolean` | See declaration / service rules |
| `sdkPlatforms` | `String` | See declaration / service rules |
| `sdkVersion` | `String` | See declaration / service rules |
| `sdkDocumentationUrl` | `String` | See declaration / service rules |
| `lastAuditDate` | `LocalDateTime` | See declaration / service rules |
| `nextAuditDate` | `LocalDateTime` | See declaration / service rules |

### CardInterestDTO

[CardInterestDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/interest/CardInterestDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `interestId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `statementId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `interestReference` | `String` | `@NotBlank(message = "Interest reference is required")`; `@Size(max = 100, message = "Interest reference cannot exceed 100 characters")` |
| `interestType` | `String` | `@NotBlank(message = "Interest type is required")`; `@Size(max = 50, message = "Interest type cannot exceed 50 characters")` |
| `interestName` | `String` | `@NotBlank(message = "Interest name is required")`; `@Size(min = 2, max = 100, message = "Interest name must be between 2 and 100 characters")` |
| `interestDescription` | `String` | `@Size(max = 500, message = "Interest description cannot exceed 500 characters")` |
| `interestRate` | `BigDecimal` | `@NotNull(message = "Interest rate is required")` |
| `annualPercentageRate` | `BigDecimal` | `@NotNull(message = "Annual percentage rate is required")` |
| `isVariableRate` | `Boolean` | `@NotNull(message = "Variable rate flag is required")` |
| `baseRate` | `BigDecimal` | See declaration / service rules |
| `rateMargin` | `BigDecimal` | See declaration / service rules |
| `rateCap` | `BigDecimal` | See declaration / service rules |
| `rateFloor` | `BigDecimal` | See declaration / service rules |
| `rateChangeFrequency` | `String` | `@Size(max = 50, message = "Rate change frequency cannot exceed 50 characters")` |
| `nextRateChangeDate` | `LocalDateTime` | See declaration / service rules |
| `previousRate` | `BigDecimal` | See declaration / service rules |
| `rateChangeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `calculationMethod` | `String` | `@NotBlank(message = "Calculation method is required")`; `@Size(max = 50, message = "Calculation method cannot exceed 50 characters")` |
| `compoundingFrequency` | `String` | `@Size(max = 50, message = "Compounding frequency cannot exceed 50 characters")` |
| `accrualFrequency` | `String` | `@Size(max = 50, message = "Accrual frequency cannot exceed 50 characters")` |
| `balanceType` | `String` | `@NotBlank(message = "Balance type is required")`; `@Size(max = 50, message = "Balance type cannot exceed 50 characters")` |
| `balanceAmount` | `BigDecimal` | `@NotNull(message = "Balance amount is required")` |
| `interestAmount` | `BigDecimal` | `@NotNull(message = "Interest amount is required")` |
| `accruedInterest` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `accrualStartDate` | `LocalDateTime` | `@NotNull(message = "Accrual start date is required")` |
| `accrualEndDate` | `LocalDateTime` | `@NotNull(message = "Accrual end date is required")` |
| `daysInPeriod` | `Integer` | `@NotNull(message = "Days in period is required")`; `@Min(value = 1, message = "Days in period must be at least 1")`; `@Max(value = 366, message = "Days in period cannot exceed 366")` |
| `daysInYear` | `Integer` | `@NotNull(message = "Days in year is required")`; `@Min(value = 365, message = "Days in year must be at least 365")`; `@Max(value = 366, message = "Days in year cannot exceed 366")` |
| `isPromotionalRate` | `Boolean` | `@NotNull(message = "Promotional rate flag is required")` |
| `promotionId` | `UUID` | See declaration / service rules |
| `promotionStartDate` | `LocalDateTime` | See declaration / service rules |
| `promotionEndDate` | `LocalDateTime` | See declaration / service rules |
| `postPromotionRate` | `BigDecimal` | See declaration / service rules |
| `isGracePeriod` | `Boolean` | `@NotNull(message = "Grace period flag is required")` |
| `gracePeriodDays` | `Integer` | `@Min(value = 0, message = "Grace period days cannot be negative")`; `@Max(value = 365, message = "Grace period days cannot exceed 365")` |
| `gracePeriodEndDate` | `LocalDateTime` | See declaration / service rules |
| `isCharged` | `Boolean` | `@NotNull(message = "Charged flag is required")` |
| `chargeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `postingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `valueDate` | `LocalDateTime` | See declaration / service rules |
| `isWaived` | `Boolean` | `@NotNull(message = "Waived flag is required")` |
| `waiverReason` | `String` | `@Size(max = 255, message = "Waiver reason cannot exceed 255 characters")` |
| `waiverReference` | `String` | `@Size(max = 100, message = "Waiver reference cannot exceed 100 characters")` |
| `waiverTimestamp` | `LocalDateTime` | See declaration / service rules |
| `waiverAuthorizedBy` | `String` | `@Size(max = 100, message = "Waiver authorized by cannot exceed 100 characters")` |
| `isBilled` | `Boolean` | `@NotNull(message = "Billed flag is required")` |
| `billingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isPaid` | `Boolean` | `@NotNull(message = "Paid flag is required")` |
| `paymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `paymentReference` | `String` | `@Size(max = 100, message = "Payment reference cannot exceed 100 characters")` |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### IssuerDTO

[IssuerDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/issuer/IssuerDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `issuerId` | `UUID` | See declaration / service rules |
| `issuerName` | `String` | `@NotBlank(message = "Issuer name is required")`; `@Size(min = 2, max = 100, message = "Issuer name must be between 2 and 100 characters")` |
| `issuerCode` | `String` | See declaration / service rules |
| `countryCode` | `String` | See declaration / service rules |
| `contactEmail` | `String` | `@Email(message = "Contact email must be a valid email address")`; `@Size(max = 100, message = "Contact email cannot exceed 100 characters")` |
| `contactPhone` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `supportUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Support URL must be a valid HTTP/HTTPS URL")` |
| `logoUrl` | `String` | `@Pattern(regexp = "^https?://.*\\.(jpg\|jpeg\|png\|gif\|svg)$", message = "Logo URL must be a valid HTTP/HTTPS URL pointing to an image file")` |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` |

Validation annotations present in this declaration: `@Email`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardLimitDTO

[CardLimitDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/limit/CardLimitDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardLimitId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `limitType` | `LimitTypeEnum` | `@NotNull(message = "Limit type is required")` |
| `limitAmount` | `BigDecimal` | `@NotNull(message = "Limit amount is required")` |
| `currentUsage` | `BigDecimal` | `@NotNull(message = "Current usage is required")` |
| `resetPeriod` | `ResetPeriodEnum` | `@NotNull(message = "Reset period is required")` |

Validation annotations present in this declaration: `@NotNull`. Inspect the linked source for the field-level constraints.

### CardMerchantDTO

[CardMerchantDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/merchant/CardMerchantDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `merchantId` | `UUID` | See declaration / service rules |
| `merchantReference` | `String` | `@NotBlank(message = "Merchant reference is required")`; `@Size(max = 100, message = "Merchant reference cannot exceed 100 characters")` |
| `merchantName` | `String` | `@NotBlank(message = "Merchant name is required")`; `@Size(min = 2, max = 100, message = "Merchant name must be between 2 and 100 characters")` |
| `merchantLegalName` | `String` | `@NotBlank(message = "Merchant legal name is required")`; `@Size(min = 2, max = 150, message = "Merchant legal name must be between 2 and 150 characters")` |
| `merchantDisplayName` | `String` | `@Size(max = 100, message = "Merchant display name cannot exceed 100 characters")` |
| `merchantDescription` | `String` | `@Size(max = 500, message = "Merchant description cannot exceed 500 characters")` |
| `merchantCategoryCode` | `String` | See declaration / service rules |
| `merchantCategoryName` | `String` | `@NotBlank(message = "Merchant category name is required")`; `@Size(min = 2, max = 100, message = "Merchant category name must be between 2 and 100 characters")` |
| `merchantType` | `String` | `@NotBlank(message = "Merchant type is required")`; `@Size(max = 50, message = "Merchant type cannot exceed 50 characters")` |
| `merchantStatus` | `String` | `@NotBlank(message = "Merchant status is required")`; `@Size(max = 50, message = "Merchant status cannot exceed 50 characters")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | `@Size(max = 255, message = "Deactivation reason cannot exceed 255 characters")` |
| `taxId` | `String` | `@Size(max = 50, message = "Tax ID cannot exceed 50 characters")` |
| `registrationNumber` | `String` | `@Size(max = 50, message = "Registration number cannot exceed 50 characters")` |
| `websiteUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Website URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Website URL cannot exceed 500 characters")` |
| `logoUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Logo URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Logo URL cannot exceed 500 characters")` |
| `addressLine1` | `String` | `@NotBlank(message = "Address line 1 is required")`; `@Size(max = 100, message = "Address line 1 cannot exceed 100 characters")` |
| `addressLine2` | `String` | `@Size(max = 100, message = "Address line 2 cannot exceed 100 characters")` |
| `city` | `String` | `@NotBlank(message = "City is required")`; `@Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")` |
| `state` | `String` | `@Size(max = 50, message = "State cannot exceed 50 characters")` |
| `postalCode` | `String` | `@NotBlank(message = "Postal code is required")`; `@Size(max = 20, message = "Postal code cannot exceed 20 characters")` |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | `@Email(message = "Email must be a valid email address")`; `@Size(max = 100, message = "Email cannot exceed 100 characters")` |
| `contactPersonName` | `String` | `@Size(min = 2, max = 100, message = "Contact person name must be between 2 and 100 characters")` |
| `contactPersonTitle` | `String` | `@Size(max = 100, message = "Contact person title cannot exceed 100 characters")` |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | `@Email(message = "Contact person email must be a valid email address")`; `@Size(max = 100, message = "Contact person email cannot exceed 100 characters")` |
| `isOnline` | `Boolean` | `@NotNull(message = "Online flag is required")` |
| `isPhysical` | `Boolean` | `@NotNull(message = "Physical flag is required")` |
| `isMobile` | `Boolean` | `@NotNull(message = "Mobile flag is required")` |
| `isInternational` | `Boolean` | `@NotNull(message = "International flag is required")` |
| `supportedCurrencies` | `String` | `@Size(max = 500, message = "Supported currencies cannot exceed 500 characters")` |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | `@Size(max = 500, message = "Supported card networks cannot exceed 500 characters")` |
| `supportedPaymentMethods` | `String` | `@Size(max = 500, message = "Supported payment methods cannot exceed 500 characters")` |
| `isHighRisk` | `Boolean` | `@NotNull(message = "High risk flag is required")` |
| `riskRating` | `String` | `@Size(max = 50, message = "Risk rating cannot exceed 50 characters")` |
| `riskScore` | `Integer` | `@Min(value = 0, message = "Risk score cannot be negative")`; `@Max(value = 1000, message = "Risk score cannot exceed 1000")` |
| `riskAssessmentDate` | `LocalDateTime` | See declaration / service rules |
| `isFraudSuspected` | `Boolean` | `@NotNull(message = "Fraud suspected flag is required")` |
| `fraudReason` | `String` | `@Size(max = 255, message = "Fraud reason cannot exceed 255 characters")` |
| `fraudReportDate` | `LocalDateTime` | See declaration / service rules |
| `isBlacklisted` | `Boolean` | `@NotNull(message = "Blacklisted flag is required")` |
| `blacklistReason` | `String` | `@Size(max = 255, message = "Blacklist reason cannot exceed 255 characters")` |
| `blacklistDate` | `LocalDateTime` | See declaration / service rules |
| `isSettlementEnabled` | `Boolean` | `@NotNull(message = "Settlement enabled flag is required")` |
| `settlementFrequency` | `String` | `@Size(max = 50, message = "Settlement frequency cannot exceed 50 characters")` |
| `settlementDay` | `Integer` | `@Min(value = 1, message = "Settlement day must be between 1 and 31")`; `@Max(value = 31, message = "Settlement day must be between 1 and 31")` |
| `settlementBankName` | `String` | `@Size(max = 100, message = "Settlement bank name cannot exceed 100 characters")` |
| `settlementAccountNumber` | `String` | `@Size(max = 50, message = "Settlement account number cannot exceed 50 characters")` |
| `settlementAccountName` | `String` | `@Size(max = 100, message = "Settlement account name cannot exceed 100 characters")` |
| `settlementBankCode` | `String` | `@Size(max = 20, message = "Settlement bank code cannot exceed 20 characters")` |
| `settlementCurrency` | `String` | See declaration / service rules |
| `acquirerId` | `String` | `@Size(max = 50, message = "Acquirer ID cannot exceed 50 characters")` |
| `acquirerName` | `String` | `@Size(max = 100, message = "Acquirer name cannot exceed 100 characters")` |
| `processorId` | `String` | `@Size(max = 50, message = "Processor ID cannot exceed 50 characters")` |
| `processorName` | `String` | `@Size(max = 100, message = "Processor name cannot exceed 100 characters")` |
| `terminalIds` | `String` | `@Size(max = 500, message = "Terminal IDs cannot exceed 500 characters")` |
| `notes` | `String` | `@Size(max = 1000, message = "Notes cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@Email`, `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardNetworkDTO

[CardNetworkDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/network/CardNetworkDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardNetworkId` | `UUID` | See declaration / service rules |
| `networkName` | `String` | `@NotBlank(message = "Network name is required")`; `@Size(min = 2, max = 100, message = "Network name must be between 2 and 100 characters")` |
| `networkCode` | `String` | See declaration / service rules |
| `networkLogoUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Network logo URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Network logo URL cannot exceed 500 characters")` |
| `supportContact` | `String` | `@Email(message = "Support contact must be a valid email address")`; `@Size(max = 100, message = "Support contact cannot exceed 100 characters")` |
| `apiEndpoint` | `String` | `@Pattern(regexp = "^https?://.*", message = "API endpoint must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "API endpoint cannot exceed 500 characters")` |
| `apiKey` | `String` | `@Size(max = 255, message = "API key cannot exceed 255 characters")` |
| `apiSecret` | `String` | `@Size(max = 255, message = "API secret cannot exceed 255 characters")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` |

Validation annotations present in this declaration: `@Email`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardPaymentDTO

[CardPaymentDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/payment/CardPaymentDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `paymentId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `statementId` | `UUID` | See declaration / service rules |
| `paymentReference` | `String` | `@NotBlank(message = "Payment reference is required")`; `@Size(min = 5, max = 50, message = "Payment reference must be between 5 and 50 characters")` |
| `externalReference` | `String` | `@Size(max = 50, message = "External reference cannot exceed 50 characters")` |
| `paymentAmount` | `BigDecimal` | `@NotNull(message = "Payment amount is required")` |
| `currencyCode` | `String` | See declaration / service rules |
| `paymentMethod` | `String` | `@NotBlank(message = "Payment method is required")`; `@Pattern(regexp = "^(BANK_TRANSFER\|CREDIT_CARD\|DEBIT_CARD\|CASH\|CHECK\|ONLINE)$", message = "Payment method must be BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, CASH, CHECK, or ONLINE")` |
| `paymentChannel` | `String` | `@NotBlank(message = "Payment channel is required")`; `@Pattern(regexp = "^(ONLINE\|MOBILE\|ATM\|BRANCH\|PHONE\|MAIL)$", message = "Payment channel must be ONLINE, MOBILE, ATM, BRANCH, PHONE, or MAIL")` |
| `paymentStatus` | `String` | `@NotBlank(message = "Payment status is required")`; `@Pattern(regexp = "^(PENDING\|PROCESSING\|COMPLETED\|FAILED\|CANCELLED\|REFUNDED)$", message = "Payment status must be PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, or REFUNDED")` |
| `paymentTimestamp` | `LocalDateTime` | `@NotNull(message = "Payment timestamp is required")` |
| `postingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `valueDate` | `LocalDateTime` | See declaration / service rules |
| `isAutoPayment` | `Boolean` | `@NotNull(message = "Auto payment flag is required")` |
| `isMinimumPayment` | `Boolean` | `@NotNull(message = "Minimum payment flag is required")` |
| `isFullPayment` | `Boolean` | `@NotNull(message = "Full payment flag is required")` |
| `isScheduledPayment` | `Boolean` | `@NotNull(message = "Scheduled payment flag is required")` |
| `scheduledDate` | `LocalDateTime` | See declaration / service rules |
| `recurrencePattern` | `String` | `@Size(max = 100, message = "Recurrence pattern cannot exceed 100 characters")` |
| `sourceAccountId` | `String` | `@Size(max = 50, message = "Source account ID cannot exceed 50 characters")` |
| `sourceAccountType` | `String` | `@Size(max = 50, message = "Source account type cannot exceed 50 characters")` |
| `sourceBankCode` | `String` | `@Size(max = 20, message = "Source bank code cannot exceed 20 characters")` |
| `sourceBankName` | `String` | `@Size(max = 100, message = "Source bank name cannot exceed 100 characters")` |
| `sourceAccountHolder` | `String` | `@Size(max = 100, message = "Source account holder cannot exceed 100 characters")` |
| `paymentProcessor` | `String` | `@Size(max = 100, message = "Payment processor cannot exceed 100 characters")` |
| `processorFee` | `BigDecimal` | See declaration / service rules |
| `processorReference` | `String` | `@Size(max = 100, message = "Processor reference cannot exceed 100 characters")` |
| `confirmationCode` | `String` | `@Size(max = 50, message = "Confirmation code cannot exceed 50 characters")` |
| `failureReason` | `String` | `@Size(max = 255, message = "Failure reason cannot exceed 255 characters")` |
| `failureCode` | `String` | `@Size(max = 50, message = "Failure code cannot exceed 50 characters")` |
| `retryCount` | `Integer` | `@Min(value = 0, message = "Retry count cannot be negative")`; `@Max(value = 10, message = "Retry count cannot exceed 10")` |
| `lastRetryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `nextRetryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `receiptUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Receipt URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Receipt URL cannot exceed 500 characters")` |
| `notes` | `String` | `@Size(max = 1000, message = "Notes cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### PhysicalCardDTO

[PhysicalCardDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/physical/PhysicalCardDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `physicalCardId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `embossedName` | `String` | `@NotBlank(message = "Embossed name is required")`; `@Size(min = 2, max = 26, message = "Embossed name must be between 2 and 26 characters")` |
| `plasticId` | `String` | See declaration / service rules |
| `designId` | `UUID` | `@NotNull(message = "Design ID is required")` |
| `isContactless` | `Boolean` | `@NotNull(message = "Contactless flag is required")` |
| `isChip` | `Boolean` | `@NotNull(message = "Chip flag is required")` |
| `isMagstripe` | `Boolean` | `@NotNull(message = "Magstripe flag is required")` |
| `manufacturingStatus` | `String` | `@NotBlank(message = "Manufacturing status is required")`; `@Pattern(regexp = "^(PENDING\|IN_PROGRESS\|COMPLETED\|FAILED)$", message = "Manufacturing status must be PENDING, IN_PROGRESS, COMPLETED, or FAILED")` |
| `manufacturingDate` | `LocalDateTime` | See declaration / service rules |
| `shippingAddress` | `String` | `@NotBlank(message = "Shipping address is required")`; `@Size(min = 10, max = 200, message = "Shipping address must be between 10 and 200 characters")` |
| `shippingCity` | `String` | `@NotBlank(message = "Shipping city is required")`; `@Size(min = 2, max = 50, message = "Shipping city must be between 2 and 50 characters")` |
| `shippingState` | `String` | See declaration / service rules |
| `shippingCountry` | `String` | See declaration / service rules |
| `shippingPostalCode` | `String` | See declaration / service rules |
| `shippingMethod` | `String` | See declaration / service rules |
| `shippingTrackingNumber` | `String` | See declaration / service rules |
| `shippingCarrier` | `String` | See declaration / service rules |
| `shipmentDate` | `LocalDateTime` | See declaration / service rules |
| `estimatedDeliveryDate` | `LocalDateTime` | See declaration / service rules |
| `actualDeliveryDate` | `LocalDateTime` | See declaration / service rules |
| `activationMethod` | `String` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `isActivated` | `Boolean` | See declaration / service rules |
| `replacementReason` | `String` | See declaration / service rules |
| `previousCardId` | `UUID` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardProcessorDTO

[CardProcessorDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/processor/CardProcessorDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `processorId` | `UUID` | See declaration / service rules |
| `processorReference` | `String` | `@NotBlank(message = "Processor reference is required")`; `@Size(max = 100, message = "Processor reference cannot exceed 100 characters")` |
| `processorName` | `String` | `@NotBlank(message = "Processor name is required")`; `@Size(min = 2, max = 100, message = "Processor name must be between 2 and 100 characters")` |
| `processorLegalName` | `String` | `@NotBlank(message = "Processor legal name is required")`; `@Size(min = 2, max = 150, message = "Processor legal name must be between 2 and 150 characters")` |
| `processorCode` | `String` | See declaration / service rules |
| `processorDescription` | `String` | `@Size(max = 500, message = "Processor description cannot exceed 500 characters")` |
| `processorType` | `String` | `@NotBlank(message = "Processor type is required")`; `@Size(max = 50, message = "Processor type cannot exceed 50 characters")` |
| `processorStatus` | `String` | `@NotBlank(message = "Processor status is required")`; `@Size(max = 50, message = "Processor status cannot exceed 50 characters")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | `@Size(max = 255, message = "Deactivation reason cannot exceed 255 characters")` |
| `taxId` | `String` | `@Size(max = 50, message = "Tax ID cannot exceed 50 characters")` |
| `registrationNumber` | `String` | `@Size(max = 50, message = "Registration number cannot exceed 50 characters")` |
| `licenseNumber` | `String` | `@Size(max = 50, message = "License number cannot exceed 50 characters")` |
| `licenseExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `websiteUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Website URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Website URL cannot exceed 500 characters")` |
| `logoUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Logo URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Logo URL cannot exceed 500 characters")` |
| `addressLine1` | `String` | `@Size(max = 100, message = "Address line 1 cannot exceed 100 characters")` |
| `addressLine2` | `String` | `@Size(max = 100, message = "Address line 2 cannot exceed 100 characters")` |
| `city` | `String` | `@Size(max = 50, message = "City cannot exceed 50 characters")` |
| `state` | `String` | `@Size(max = 50, message = "State cannot exceed 50 characters")` |
| `postalCode` | `String` | `@Size(max = 20, message = "Postal code cannot exceed 20 characters")` |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | `@Email(message = "Email must be a valid email address")`; `@Size(max = 100, message = "Email cannot exceed 100 characters")` |
| `contactPersonName` | `String` | `@Size(max = 100, message = "Contact person name cannot exceed 100 characters")` |
| `contactPersonTitle` | `String` | `@Size(max = 100, message = "Contact person title cannot exceed 100 characters")` |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | `@Email(message = "Contact person email must be a valid email address")`; `@Size(max = 100, message = "Contact person email cannot exceed 100 characters")` |
| `supportedCardNetworks` | `String` | `@Size(max = 500, message = "Supported card networks cannot exceed 500 characters")` |
| `supportedPaymentMethods` | `String` | `@Size(max = 500, message = "Supported payment methods cannot exceed 500 characters")` |
| `supportedCurrencies` | `String` | `@Size(max = 500, message = "Supported currencies cannot exceed 500 characters")` |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCountries` | `String` | `@Size(max = 500, message = "Supported countries cannot exceed 500 characters")` |
| `isInternational` | `Boolean` | `@NotNull(message = "International flag is required")` |
| `isDomestic` | `Boolean` | `@NotNull(message = "Domestic flag is required")` |
| `isIssuerProcessor` | `Boolean` | `@NotNull(message = "Issuer processor flag is required")` |
| `isAcquirerProcessor` | `Boolean` | `@NotNull(message = "Acquirer processor flag is required")` |
| `isSwitchProcessor` | `Boolean` | `@NotNull(message = "Switch processor flag is required")` |
| `isGateway` | `Boolean` | `@NotNull(message = "Gateway flag is required")` |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `acquirerCount` | `Integer` | See declaration / service rules |
| `issuerCount` | `Integer` | See declaration / service rules |
| `merchantCount` | `Integer` | See declaration / service rules |
| `terminalCount` | `Integer` | See declaration / service rules |
| `transactionVolumeDailyAvg` | `Long` | See declaration / service rules |
| `transactionValueDailyAvg` | `Long` | See declaration / service rules |
| `processingTimeAvgMs` | `Integer` | See declaration / service rules |
| `uptimePercentage` | `Double` | See declaration / service rules |
| `lastDowntimeDate` | `LocalDateTime` | See declaration / service rules |
| `lastDowntimeDurationMinutes` | `Integer` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDelayDays` | `Integer` | See declaration / service rules |
| `feeStructure` | `String` | See declaration / service rules |
| `processingFeePercentage` | `Double` | See declaration / service rules |
| `processingFeeFlat` | `Double` | See declaration / service rules |
| `chargebackFee` | `Double` | See declaration / service rules |
| `refundFee` | `Double` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `contractRenewalDate` | `LocalDateTime` | See declaration / service rules |
| `contractStatus` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceLevel` | `String` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `isEmvCompliant` | `Boolean` | See declaration / service rules |
| `emvComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `isPaDssCompliant` | `Boolean` | See declaration / service rules |
| `paDssComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `paDssComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `supportsTokenization` | `Boolean` | See declaration / service rules |
| `supports3dSecure` | `Boolean` | See declaration / service rules |
| `supportsEmv` | `Boolean` | See declaration / service rules |
| `supportsContactless` | `Boolean` | See declaration / service rules |
| `supportsApplePay` | `Boolean` | See declaration / service rules |
| `supportsGooglePay` | `Boolean` | See declaration / service rules |
| `supportsSamsungPay` | `Boolean` | See declaration / service rules |
| `lastAuditDate` | `LocalDateTime` | See declaration / service rules |
| `nextAuditDate` | `LocalDateTime` | See declaration / service rules |

Validation annotations present in this declaration: `@Email`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardProgramDTO

[CardProgramDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/program/CardProgramDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `programId` | `UUID` | See declaration / service rules |
| `programName` | `String` | `@NotBlank(message = "Program name is required")`; `@Size(min = 2, max = 100, message = "Program name must be between 2 and 100 characters")` |
| `programCode` | `String` | See declaration / service rules |
| `issuerId` | `UUID` | `@NotNull(message = "Issuer ID is required")` |
| `binId` | `UUID` | `@NotNull(message = "BIN ID is required")` |
| `cardTypeId` | `UUID` | `@NotNull(message = "Card type ID is required")` |
| `cardNetworkId` | `UUID` | `@NotNull(message = "Card network ID is required")` |
| `defaultDesignId` | `UUID` | See declaration / service rules |
| `startDate` | `LocalDateTime` | `@NotNull(message = "Start date is required")` |
| `endDate` | `LocalDateTime` | See declaration / service rules |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `maxCardsPerParty` | `Integer` | `@Min(value = 1, message = "Maximum cards per party must be at least 1")`; `@Max(value = 100, message = "Maximum cards per party cannot exceed 100")` |
| `defaultDailyLimit` | `Double` | See declaration / service rules |
| `defaultMonthlyLimit` | `Double` | See declaration / service rules |
| `defaultCreditLimit` | `Double` | See declaration / service rules |
| `defaultCardValidityYears` | `Integer` | `@Min(value = 1, message = "Default card validity years must be at least 1")`; `@Max(value = 10, message = "Default card validity years cannot exceed 10")` |
| `supportsPhysicalCards` | `Boolean` | `@NotNull(message = "Physical cards support flag is required")` |
| `supportsVirtualCards` | `Boolean` | `@NotNull(message = "Virtual cards support flag is required")` |
| `supportsContactless` | `Boolean` | `@NotNull(message = "Contactless support flag is required")` |
| `supportsInternational` | `Boolean` | `@NotNull(message = "International support flag is required")` |
| `supportsAtmWithdrawal` | `Boolean` | `@NotNull(message = "ATM withdrawal support flag is required")` |
| `supportsOnlineTransactions` | `Boolean` | `@NotNull(message = "Online transactions support flag is required")` |
| `supportsRecurringPayments` | `Boolean` | `@NotNull(message = "Recurring payments support flag is required")` |
| `supportsApplePay` | `Boolean` | `@NotNull(message = "Apple Pay support flag is required")` |
| `supportsGooglePay` | `Boolean` | `@NotNull(message = "Google Pay support flag is required")` |
| `supportsSamsungPay` | `Boolean` | `@NotNull(message = "Samsung Pay support flag is required")` |
| `requiresPin` | `Boolean` | `@NotNull(message = "PIN requirement flag is required")` |
| `requiresActivation` | `Boolean` | `@NotNull(message = "Activation requirement flag is required")` |
| `currencyCode` | `String` | See declaration / service rules |
| `countryCode` | `String` | See declaration / service rules |
| `termsAndConditionsUrl` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardPromotionDTO

[CardPromotionDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/promotion/CardPromotionDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `promotionId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `issuerId` | `UUID` | See declaration / service rules |
| `promotionCode` | `String` | `@NotBlank(message = "Promotion code is required")`; `@Size(min = 3, max = 20, message = "Promotion code must be between 3 and 20 characters")`; `@Pattern(regexp = "^[A-Z0-9_]+$", message = "Promotion code must contain only uppercase letters, numbers, and underscores")` |
| `promotionName` | `String` | `@NotBlank(message = "Promotion name is required")`; `@Size(min = 2, max = 100, message = "Promotion name must be between 2 and 100 characters")` |
| `promotionDescription` | `String` | `@Size(max = 500, message = "Promotion description cannot exceed 500 characters")` |
| `promotionType` | `String` | `@NotBlank(message = "Promotion type is required")`; `@Size(max = 50, message = "Promotion type cannot exceed 50 characters")` |
| `promotionCategory` | `String` | `@Size(max = 50, message = "Promotion category cannot exceed 50 characters")` |
| `promotionStatus` | `String` | `@NotBlank(message = "Promotion status is required")`; `@Size(max = 50, message = "Promotion status cannot exceed 50 characters")` |
| `startDate` | `LocalDateTime` | `@NotNull(message = "Start date is required")` |
| `endDate` | `LocalDateTime` | `@NotNull(message = "End date is required")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `isLimitedTime` | `Boolean` | `@NotNull(message = "Limited time flag is required")` |
| `isTargeted` | `Boolean` | `@NotNull(message = "Targeted flag is required")` |
| `targetCriteria` | `String` | `@Size(max = 500, message = "Target criteria cannot exceed 500 characters")` |
| `isOptInRequired` | `Boolean` | `@NotNull(message = "Opt-in required flag is required")` |
| `optInChannel` | `String` | `@Size(max = 50, message = "Opt-in channel cannot exceed 50 characters")` |
| `optInUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Opt-in URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Opt-in URL cannot exceed 500 characters")` |
| `isAutoEnrolled` | `Boolean` | `@NotNull(message = "Auto enrolled flag is required")` |
| `maxEnrollments` | `Integer` | `@Min(value = 1, message = "Maximum enrollments must be at least 1")`; `@Max(value = 1000000, message = "Maximum enrollments cannot exceed 1,000,000")` |
| `currentEnrollments` | `Integer` | `@Min(value = 0, message = "Current enrollments cannot be negative")` |
| `benefitType` | `String` | `@NotBlank(message = "Benefit type is required")`; `@Size(max = 50, message = "Benefit type cannot exceed 50 characters")` |
| `benefitValue` | `BigDecimal` | See declaration / service rules |
| `benefitPercentage` | `BigDecimal` | See declaration / service rules |
| `benefitDescription` | `String` | `@Size(max = 500, message = "Benefit description cannot exceed 500 characters")` |
| `minimumSpend` | `BigDecimal` | See declaration / service rules |
| `maximumBenefit` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `merchantCategoryCodes` | `String` | `@Size(max = 500, message = "Merchant category codes cannot exceed 500 characters")` |
| `merchantIds` | `String` | `@Size(max = 500, message = "Merchant IDs cannot exceed 500 characters")` |
| `countryCodes` | `String` | `@Size(max = 500, message = "Country codes cannot exceed 500 characters")` |
| `transactionTypes` | `String` | `@Size(max = 500, message = "Transaction types cannot exceed 500 characters")` |
| `channelTypes` | `String` | `@Size(max = 500, message = "Channel types cannot exceed 500 characters")` |
| `isFirstTransactionOnly` | `Boolean` | `@NotNull(message = "First transaction only flag is required")` |
| `isNewCardsOnly` | `Boolean` | `@NotNull(message = "New cards only flag is required")` |
| `cardAgeMinDays` | `Integer` | `@Min(value = 0, message = "Card age minimum days cannot be negative")`; `@Max(value = 3650, message = "Card age minimum days cannot exceed 3650 (10 years)")` |
| `cardAgeMaxDays` | `Integer` | `@Min(value = 0, message = "Card age maximum days cannot be negative")`; `@Max(value = 3650, message = "Card age maximum days cannot exceed 3650 (10 years)")` |
| `isBalanceTransfer` | `Boolean` | `@NotNull(message = "Balance transfer flag is required")` |
| `balanceTransferFee` | `BigDecimal` | See declaration / service rules |
| `balanceTransferFeePercentage` | `BigDecimal` | See declaration / service rules |
| `balanceTransferMinAmount` | `BigDecimal` | See declaration / service rules |
| `balanceTransferMaxAmount` | `BigDecimal` | See declaration / service rules |
| `isIntroductoryRate` | `Boolean` | `@NotNull(message = "Introductory rate flag is required")` |
| `introductoryRate` | `BigDecimal` | See declaration / service rules |
| `postIntroductoryRate` | `BigDecimal` | See declaration / service rules |
| `introductoryPeriodMonths` | `Integer` | `@Min(value = 1, message = "Introductory period must be at least 1 month")`; `@Max(value = 60, message = "Introductory period cannot exceed 60 months")` |
| `termsAndConditions` | `String` | `@Size(max = 2000, message = "Terms and conditions cannot exceed 2000 characters")` |
| `termsUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Terms URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Terms URL cannot exceed 500 characters")` |
| `marketingImageUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Marketing image URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Marketing image URL cannot exceed 500 characters")` |
| `marketingDescription` | `String` | `@Size(max = 1000, message = "Marketing description cannot exceed 1000 characters")` |
| `isCombinable` | `Boolean` | `@NotNull(message = "Combinable flag is required")` |
| `combinablePromotions` | `String` | `@Size(max = 500, message = "Combinable promotions cannot exceed 500 characters")` |
| `exclusionCriteria` | `String` | `@Size(max = 1000, message = "Exclusion criteria cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@Max`, `@Min`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardProviderDTO

[CardProviderDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/provider/CardProviderDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardProviderId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `providerName` | `String` | `@NotBlank(message = "Provider name is required")`; `@Size(min = 2, max = 100, message = "Provider name must be between 2 and 100 characters")` |
| `providerCode` | `String` | See declaration / service rules |
| `providerType` | `String` | `@NotBlank(message = "Provider type is required")`; `@Size(max = 50, message = "Provider type cannot exceed 50 characters")` |
| `apiBaseUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "API base URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "API base URL cannot exceed 500 characters")` |
| `apiVersion` | `String` | `@Size(max = 20, message = "API version cannot exceed 20 characters")` |
| `apiKey` | `String` | `@Size(max = 255, message = "API key cannot exceed 255 characters")` |
| `apiSecret` | `String` | `@Size(max = 255, message = "API secret cannot exceed 255 characters")` |
| `apiUsername` | `String` | `@Size(max = 100, message = "API username cannot exceed 100 characters")` |
| `apiPassword` | `String` | `@Size(max = 255, message = "API password cannot exceed 255 characters")` |
| `apiCertificate` | `String` | `@Size(max = 1000, message = "API certificate cannot exceed 1000 characters")` |
| `webhookUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Webhook URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Webhook URL cannot exceed 500 characters")` |
| `webhookSecret` | `String` | `@Size(max = 255, message = "Webhook secret cannot exceed 255 characters")` |
| `supportsPhysicalCards` | `Boolean` | `@NotNull(message = "Physical cards support flag is required")` |
| `supportsVirtualCards` | `Boolean` | `@NotNull(message = "Virtual cards support flag is required")` |
| `supportsTokenization` | `Boolean` | `@NotNull(message = "Tokenization support flag is required")` |
| `supports3dSecure` | `Boolean` | `@NotNull(message = "3D Secure support flag is required")` |
| `supportsApplePay` | `Boolean` | `@NotNull(message = "Apple Pay support flag is required")` |
| `supportsGooglePay` | `Boolean` | `@NotNull(message = "Google Pay support flag is required")` |
| `supportsSamsungPay` | `Boolean` | `@NotNull(message = "Samsung Pay support flag is required")` |
| `contactName` | `String` | `@Size(max = 100, message = "Contact name cannot exceed 100 characters")` |
| `contactEmail` | `String` | `@Email(message = "Contact email must be a valid email address")`; `@Size(max = 100, message = "Contact email cannot exceed 100 characters")` |
| `contactPhone` | `String` | See declaration / service rules |
| `supportUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Support URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Support URL cannot exceed 500 characters")` |
| `supportEmail` | `String` | `@Email(message = "Support email must be a valid email address")`; `@Size(max = 100, message = "Support email cannot exceed 100 characters")` |
| `supportPhone` | `String` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `status` | `ProviderStatusEnum` | `@NotNull(message = "Provider status is required")` |
| `lastConnectionDate` | `LocalDateTime` | See declaration / service rules |
| `lastConnectionStatus` | `String` | `@Size(max = 50, message = "Last connection status cannot exceed 50 characters")` |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` |

Validation annotations present in this declaration: `@Email`, `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardRewardDTO

[CardRewardDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/reward/CardRewardDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `rewardId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `programId` | `UUID` | See declaration / service rules |
| `rewardReference` | `String` | `@NotBlank(message = "Reward reference is required")`; `@Size(max = 100, message = "Reward reference cannot exceed 100 characters")` |
| `rewardType` | `String` | `@NotBlank(message = "Reward type is required")`; `@Size(max = 50, message = "Reward type cannot exceed 50 characters")` |
| `rewardCategory` | `String` | `@Size(max = 50, message = "Reward category cannot exceed 50 characters")` |
| `rewardDescription` | `String` | `@Size(max = 500, message = "Reward description cannot exceed 500 characters")` |
| `rewardStatus` | `String` | `@NotBlank(message = "Reward status is required")`; `@Size(max = 50, message = "Reward status cannot exceed 50 characters")` |
| `isEarning` | `Boolean` | `@NotNull(message = "Earning flag is required")` |
| `isRedemption` | `Boolean` | `@NotNull(message = "Redemption flag is required")` |
| `isAdjustment` | `Boolean` | `@NotNull(message = "Adjustment flag is required")` |
| `isExpiration` | `Boolean` | `@NotNull(message = "Expiration flag is required")` |
| `pointsEarned` | `BigDecimal` | See declaration / service rules |
| `pointsRedeemed` | `BigDecimal` | See declaration / service rules |
| `pointsAdjusted` | `BigDecimal` | See declaration / service rules |
| `pointsExpired` | `BigDecimal` | See declaration / service rules |
| `pointsBalance` | `BigDecimal` | See declaration / service rules |
| `cashValue` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `earningRate` | `BigDecimal` | See declaration / service rules |
| `earningMultiplier` | `BigDecimal` | See declaration / service rules |
| `earningReason` | `String` | `@Size(max = 255, message = "Earning reason cannot exceed 255 characters")` |
| `redemptionType` | `String` | `@Size(max = 50, message = "Redemption type cannot exceed 50 characters")` |
| `redemptionDescription` | `String` | `@Size(max = 500, message = "Redemption description cannot exceed 500 characters")` |
| `redemptionValue` | `BigDecimal` | See declaration / service rules |
| `adjustmentReason` | `String` | `@Size(max = 255, message = "Adjustment reason cannot exceed 255 characters")` |
| `adjustmentDescription` | `String` | `@Size(max = 500, message = "Adjustment description cannot exceed 500 characters")` |
| `merchantId` | `String` | `@Size(max = 50, message = "Merchant ID cannot exceed 50 characters")` |
| `merchantName` | `String` | `@Size(max = 100, message = "Merchant name cannot exceed 100 characters")` |
| `merchantCategory` | `String` | `@Size(max = 50, message = "Merchant category cannot exceed 50 characters")` |
| `transactionAmount` | `BigDecimal` | See declaration / service rules |
| `transactionCurrency` | `String` | See declaration / service rules |
| `transactionDate` | `LocalDateTime` | See declaration / service rules |
| `postingDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `isPromotional` | `Boolean` | `@NotNull(message = "Promotional flag is required")` |
| `promotionId` | `UUID` | See declaration / service rules |
| `promotionName` | `String` | `@Size(max = 100, message = "Promotion name cannot exceed 100 characters")` |
| `isTransferable` | `Boolean` | `@NotNull(message = "Transferable flag is required")` |
| `transferToPartyId` | `UUID` | See declaration / service rules |
| `transferFromPartyId` | `UUID` | See declaration / service rules |
| `transferDate` | `LocalDateTime` | See declaration / service rules |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardSecurityDTO

[CardSecurityDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/security/CardSecurityDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardSecurityId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `securityFeature` | `SecurityFeatureEnum` | See declaration / service rules |
| `securityStatus` | `boolean` | See declaration / service rules |

### CardStatementDTO

[CardStatementDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/statement/CardStatementDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `statementId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `partyId` | `UUID` | `@NotNull(message = "Party ID is required")` |
| `accountId` | `UUID` | `@NotNull(message = "Account ID is required")` |
| `statementReference` | `String` | `@NotBlank(message = "Statement reference is required")`; `@Size(min = 5, max = 50, message = "Statement reference must be between 5 and 50 characters")` |
| `statementDate` | `LocalDateTime` | `@NotNull(message = "Statement date is required")` |
| `statementPeriodStart` | `LocalDateTime` | `@NotNull(message = "Statement period start is required")` |
| `statementPeriodEnd` | `LocalDateTime` | `@NotNull(message = "Statement period end is required")` |
| `dueDate` | `LocalDateTime` | See declaration / service rules |
| `closingBalance` | `BigDecimal` | `@NotNull(message = "Closing balance is required")` |
| `openingBalance` | `BigDecimal` | `@NotNull(message = "Opening balance is required")` |
| `minimumPaymentDue` | `BigDecimal` | `@NotNull(message = "Minimum payment due is required")` |
| `totalPaymentDue` | `BigDecimal` | `@NotNull(message = "Total payment due is required")` |
| `currencyCode` | `String` | See declaration / service rules |
| `totalPurchases` | `BigDecimal` | See declaration / service rules |
| `totalCashAdvances` | `BigDecimal` | See declaration / service rules |
| `totalFees` | `BigDecimal` | See declaration / service rules |
| `totalInterest` | `BigDecimal` | See declaration / service rules |
| `totalCredits` | `BigDecimal` | See declaration / service rules |
| `totalPayments` | `BigDecimal` | See declaration / service rules |
| `totalAdjustments` | `BigDecimal` | See declaration / service rules |
| `totalRewardsEarned` | `BigDecimal` | See declaration / service rules |
| `totalRewardsRedeemed` | `BigDecimal` | See declaration / service rules |
| `availableCredit` | `BigDecimal` | See declaration / service rules |
| `creditLimit` | `BigDecimal` | See declaration / service rules |
| `cashAdvanceLimit` | `BigDecimal` | See declaration / service rules |
| `interestRate` | `BigDecimal` | See declaration / service rules |
| `annualPercentageRate` | `BigDecimal` | See declaration / service rules |
| `paymentStatus` | `String` | `@Size(max = 50, message = "Payment status cannot exceed 50 characters")` |
| `isGenerated` | `Boolean` | `@NotNull(message = "Generated flag is required")` |
| `generationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isDelivered` | `Boolean` | `@NotNull(message = "Delivered flag is required")` |
| `deliveryMethod` | `String` | `@Size(max = 50, message = "Delivery method cannot exceed 50 characters")` |
| `deliveryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deliveryAddress` | `String` | `@Size(max = 500, message = "Delivery address cannot exceed 500 characters")` |
| `isViewed` | `Boolean` | `@NotNull(message = "Viewed flag is required")` |
| `viewTimestamp` | `LocalDateTime` | See declaration / service rules |
| `documentUrl` | `String` | `@Pattern(regexp = "^https?://.*", message = "Document URL must be a valid HTTP/HTTPS URL")`; `@Size(max = 500, message = "Document URL cannot exceed 500 characters")` |
| `notes` | `String` | `@Size(max = 1000, message = "Notes cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardTerminalDTO

[CardTerminalDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/terminal/CardTerminalDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `terminalId` | `UUID` | See declaration / service rules |
| `terminalReference` | `String` | `@NotBlank(message = "Terminal reference is required")`; `@Size(max = 100, message = "Terminal reference cannot exceed 100 characters")` |
| `terminalSerialNumber` | `String` | `@NotBlank(message = "Terminal serial number is required")`; `@Size(max = 50, message = "Terminal serial number cannot exceed 50 characters")` |
| `terminalName` | `String` | `@NotBlank(message = "Terminal name is required")`; `@Size(min = 2, max = 100, message = "Terminal name must be between 2 and 100 characters")` |
| `terminalDescription` | `String` | `@Size(max = 500, message = "Terminal description cannot exceed 500 characters")` |
| `terminalType` | `String` | `@NotBlank(message = "Terminal type is required")`; `@Size(max = 50, message = "Terminal type cannot exceed 50 characters")` |
| `terminalModel` | `String` | `@Size(max = 100, message = "Terminal model cannot exceed 100 characters")` |
| `terminalManufacturer` | `String` | `@Size(max = 100, message = "Terminal manufacturer cannot exceed 100 characters")` |
| `terminalStatus` | `String` | `@NotBlank(message = "Terminal status is required")`; `@Size(max = 50, message = "Terminal status cannot exceed 50 characters")` |
| `isActive` | `Boolean` | `@NotNull(message = "Active flag is required")` |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | `@Size(max = 255, message = "Deactivation reason cannot exceed 255 characters")` |
| `merchantId` | `UUID` | See declaration / service rules |
| `merchantName` | `String` | `@Size(max = 100, message = "Merchant name cannot exceed 100 characters")` |
| `merchantLocationId` | `String` | `@Size(max = 50, message = "Merchant location ID cannot exceed 50 characters")` |
| `merchantLocationName` | `String` | `@Size(max = 100, message = "Merchant location name cannot exceed 100 characters")` |
| `addressLine1` | `String` | `@Size(max = 100, message = "Address line 1 cannot exceed 100 characters")` |
| `addressLine2` | `String` | `@Size(max = 100, message = "Address line 2 cannot exceed 100 characters")` |
| `city` | `String` | `@Size(max = 50, message = "City cannot exceed 50 characters")` |
| `state` | `String` | `@Size(max = 50, message = "State cannot exceed 50 characters")` |
| `postalCode` | `String` | `@Size(max = 20, message = "Postal code cannot exceed 20 characters")` |
| `country` | `String` | See declaration / service rules |
| `geolocation` | `String` | `@Size(max = 100, message = "Geolocation cannot exceed 100 characters")` |
| `acquirerId` | `String` | `@Size(max = 50, message = "Acquirer ID cannot exceed 50 characters")` |
| `acquirerName` | `String` | `@Size(max = 100, message = "Acquirer name cannot exceed 100 characters")` |
| `processorId` | `String` | `@Size(max = 50, message = "Processor ID cannot exceed 50 characters")` |
| `processorName` | `String` | `@Size(max = 100, message = "Processor name cannot exceed 100 characters")` |
| `softwareVersion` | `String` | `@Size(max = 50, message = "Software version cannot exceed 50 characters")` |
| `firmwareVersion` | `String` | `@Size(max = 50, message = "Firmware version cannot exceed 50 characters")` |
| `hardwareVersion` | `String` | `@Size(max = 50, message = "Hardware version cannot exceed 50 characters")` |
| `lastMaintenanceDate` | `LocalDateTime` | See declaration / service rules |
| `nextMaintenanceDate` | `LocalDateTime` | See declaration / service rules |
| `lastUpdateDate` | `LocalDateTime` | See declaration / service rules |
| `installationDate` | `LocalDateTime` | See declaration / service rules |
| `isPhysical` | `Boolean` | `@NotNull(message = "Physical flag is required")` |
| `isVirtual` | `Boolean` | `@NotNull(message = "Virtual flag is required")` |
| `isMobile` | `Boolean` | `@NotNull(message = "Mobile flag is required")` |
| `isAttended` | `Boolean` | `@NotNull(message = "Attended flag is required")` |
| `isUnattended` | `Boolean` | `@NotNull(message = "Unattended flag is required")` |
| `isContactless` | `Boolean` | `@NotNull(message = "Contactless flag is required")` |
| `isChip` | `Boolean` | `@NotNull(message = "Chip flag is required")` |
| `isMagstripe` | `Boolean` | `@NotNull(message = "Magstripe flag is required")` |
| `isPinSupported` | `Boolean` | `@NotNull(message = "PIN supported flag is required")` |
| `isSignatureSupported` | `Boolean` | `@NotNull(message = "Signature supported flag is required")` |
| `isBiometricSupported` | `Boolean` | `@NotNull(message = "Biometric supported flag is required")` |
| `isNfcSupported` | `Boolean` | `@NotNull(message = "NFC supported flag is required")` |
| `isQrSupported` | `Boolean` | `@NotNull(message = "QR supported flag is required")` |
| `isEmvSupported` | `Boolean` | `@NotNull(message = "EMV supported flag is required")` |
| `isP2peSupported` | `Boolean` | `@NotNull(message = "P2PE supported flag is required")` |
| `isTokenizationSupported` | `Boolean` | `@NotNull(message = "Tokenization supported flag is required")` |
| `supportedCardNetworks` | `String` | `@Size(max = 500, message = "Supported card networks cannot exceed 500 characters")` |
| `supportedPaymentMethods` | `String` | `@Size(max = 500, message = "Supported payment methods cannot exceed 500 characters")` |
| `supportedCurrencies` | `String` | `@Size(max = 500, message = "Supported currencies cannot exceed 500 characters")` |
| `defaultCurrency` | `String` | See declaration / service rules |
| `communicationType` | `String` | `@Size(max = 50, message = "Communication type cannot exceed 50 characters")` |
| `connectionType` | `String` | `@Size(max = 50, message = "Connection type cannot exceed 50 characters")` |
| `ipAddress` | `String` | See declaration / service rules |
| `macAddress` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | `@NotNull(message = "PCI compliant flag is required")` |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `encryptionMethod` | `String` | `@Size(max = 100, message = "Encryption method cannot exceed 100 characters")` |
| `keyManagementScheme` | `String` | `@Size(max = 100, message = "Key management scheme cannot exceed 100 characters")` |
| `lastKeyRotationDate` | `LocalDateTime` | See declaration / service rules |
| `nextKeyRotationDate` | `LocalDateTime` | See declaration / service rules |
| `isFaultDetected` | `Boolean` | `@NotNull(message = "Fault detected flag is required")` |
| `faultCode` | `String` | `@Size(max = 50, message = "Fault code cannot exceed 50 characters")` |
| `faultDescription` | `String` | `@Size(max = 255, message = "Fault description cannot exceed 255 characters")` |
| `faultDetectionDate` | `LocalDateTime` | See declaration / service rules |
| `lastTransactionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `lastTransactionId` | `String` | `@Size(max = 100, message = "Last transaction ID cannot exceed 100 characters")` |
| `lastTransactionStatus` | `String` | `@Size(max = 50, message = "Last transaction status cannot exceed 50 characters")` |
| `notes` | `String` | `@Size(max = 1000, message = "Notes cannot exceed 1000 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### CardTransactionDTO

[CardTransactionDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/transaction/CardTransactionDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardTransactionId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | `@NotNull(message = "Card ID is required")` |
| `transactionType` | `TransactionTypeEnum` | `@NotNull(message = "Transaction type is required")` |
| `merchantInfo` | `String` | `@Size(max = 500, message = "Merchant info cannot exceed 500 characters")` |
| `transactionStatus` | `TransactionStatusEnum` | `@NotNull(message = "Transaction status is required")` |
| `cardAuthCode` | `String` | `@Size(max = 20, message = "Card auth code cannot exceed 20 characters")` |
| `cardMerchantCategoryCode` | `String` | See declaration / service rules |
| `cardMerchantName` | `String` | `@Size(max = 100, message = "Card merchant name cannot exceed 100 characters")` |
| `cardPosEntryMode` | `String` | `@Size(max = 50, message = "Card POS entry mode cannot exceed 50 characters")` |
| `cardTransactionReference` | `String` | `@NotBlank(message = "Card transaction reference is required")`; `@Size(max = 100, message = "Card transaction reference cannot exceed 100 characters")` |
| `cardTerminalId` | `String` | `@Size(max = 50, message = "Card terminal ID cannot exceed 50 characters")` |
| `cardHolderCountry` | `String` | See declaration / service rules |
| `cardPresentFlag` | `Boolean` | `@NotNull(message = "Card present flag is required")` |
| `cardTransactionTimestamp` | `LocalDateTime` | `@NotNull(message = "Card transaction timestamp is required")` |
| `cardFraudFlag` | `Boolean` | `@NotNull(message = "Card fraud flag is required")` |
| `cardCurrencyConversionRate` | `BigDecimal` | See declaration / service rules |
| `cardFeeAmount` | `BigDecimal` | See declaration / service rules |
| `cardFeeCurrency` | `String` | See declaration / service rules |
| `cardInstallmentPlan` | `String` | `@Size(max = 100, message = "Card installment plan cannot exceed 100 characters")` |

Validation annotations present in this declaration: `@NotBlank`, `@NotNull`, `@Pattern`, `@Size`. Inspect the linked source for the field-level constraints.

### VirtualCardDTO

[VirtualCardDTO](../../core-banking-cards-interfaces/src/main/java/io/fireflysoftware/core/banking/cards/interfaces/dtos/virtual/VirtualCardDTO.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `virtualCardId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `deviceId` | `String` | See declaration / service rules |
| `virtualCardNumber` | `String` | See declaration / service rules |
| `virtualCardStatus` | `VirtualCardStatusEnum` | See declaration / service rules |
| `creationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `updateTimestamp` | `LocalDateTime` | See declaration / service rules |

### BaseEntity

[BaseEntity](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/BaseEntity.java) · DTO / record / model

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `dateCreated` | `LocalDateTime` | See declaration / service rules |
| `dateUpdated` | `LocalDateTime` | See declaration / service rules |

### CardAcquirer

[CardAcquirer](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/acquirer/CardAcquirer.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `acquirerId` | `UUID` | See declaration / service rules |
| `acquirerReference` | `String` | See declaration / service rules |
| `acquirerName` | `String` | See declaration / service rules |
| `acquirerLegalName` | `String` | See declaration / service rules |
| `acquirerCode` | `String` | See declaration / service rules |
| `acquirerDescription` | `String` | See declaration / service rules |
| `acquirerType` | `String` | See declaration / service rules |
| `acquirerStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `taxId` | `String` | See declaration / service rules |
| `registrationNumber` | `String` | See declaration / service rules |
| `licenseNumber` | `String` | See declaration / service rules |
| `licenseExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `websiteUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | See declaration / service rules |
| `contactPersonName` | `String` | See declaration / service rules |
| `contactPersonTitle` | `String` | See declaration / service rules |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCountries` | `String` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `isDomestic` | `Boolean` | See declaration / service rules |
| `isOnline` | `Boolean` | See declaration / service rules |
| `isPhysical` | `Boolean` | See declaration / service rules |
| `isMobile` | `Boolean` | See declaration / service rules |
| `processorId` | `UUID` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `gatewayId` | `UUID` | See declaration / service rules |
| `gatewayName` | `String` | See declaration / service rules |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `merchantCount` | `Integer` | See declaration / service rules |
| `terminalCount` | `Integer` | See declaration / service rules |
| `transactionVolumeDailyAvg` | `Long` | See declaration / service rules |
| `transactionValueDailyAvg` | `Long` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDelayDays` | `Integer` | See declaration / service rules |
| `feeStructure` | `String` | See declaration / service rules |
| `interchangeFeePercentage` | `Double` | See declaration / service rules |
| `processingFeePercentage` | `Double` | See declaration / service rules |
| `processingFeeFlat` | `Double` | See declaration / service rules |
| `chargebackFee` | `Double` | See declaration / service rules |
| `refundFee` | `Double` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `contractRenewalDate` | `LocalDateTime` | See declaration / service rules |
| `contractStatus` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `isEmvCompliant` | `Boolean` | See declaration / service rules |
| `emvComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `lastAuditDate` | `LocalDateTime` | See declaration / service rules |
| `nextAuditDate` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardActivity

[CardActivity](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/activity/CardActivity.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `activityId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `activityReference` | `String` | See declaration / service rules |
| `activityType` | `String` | See declaration / service rules |
| `activityCategory` | `String` | See declaration / service rules |
| `activityDescription` | `String` | See declaration / service rules |
| `activityTimestamp` | `LocalDateTime` | See declaration / service rules |
| `activityChannel` | `String` | See declaration / service rules |
| `activitySource` | `String` | See declaration / service rules |
| `activityStatus` | `String` | See declaration / service rules |
| `activityResult` | `String` | See declaration / service rules |
| `activityDetails` | `String` | See declaration / service rules |
| `previousValue` | `String` | See declaration / service rules |
| `newValue` | `String` | See declaration / service rules |
| `changeReason` | `String` | See declaration / service rules |
| `changeAuthorizedBy` | `String` | See declaration / service rules |
| `ipAddress` | `String` | See declaration / service rules |
| `deviceId` | `String` | See declaration / service rules |
| `deviceType` | `String` | See declaration / service rules |
| `deviceOs` | `String` | See declaration / service rules |
| `browserType` | `String` | See declaration / service rules |
| `userAgent` | `String` | See declaration / service rules |
| `geolocation` | `String` | See declaration / service rules |
| `isPartyInitiated` | `Boolean` | See declaration / service rules |
| `isSystemInitiated` | `Boolean` | See declaration / service rules |
| `isAgentInitiated` | `Boolean` | See declaration / service rules |
| `agentId` | `String` | See declaration / service rules |
| `agentName` | `String` | See declaration / service rules |
| `isSuccessful` | `Boolean` | See declaration / service rules |
| `failureReason` | `String` | See declaration / service rules |
| `failureCode` | `String` | See declaration / service rules |
| `isNotificationSent` | `Boolean` | See declaration / service rules |
| `notificationChannel` | `String` | See declaration / service rules |
| `notificationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notificationRecipient` | `String` | See declaration / service rules |
| `relatedEntityType` | `String` | See declaration / service rules |
| `relatedEntityId` | `UUID` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardAlert

[CardAlert](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/alert/CardAlert.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `alertId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `alertReference` | `String` | See declaration / service rules |
| `alertType` | `String` | See declaration / service rules |
| `alertCategory` | `String` | See declaration / service rules |
| `alertSeverity` | `String` | See declaration / service rules |
| `alertStatus` | `String` | See declaration / service rules |
| `alertTitle` | `String` | See declaration / service rules |
| `alertMessage` | `String` | See declaration / service rules |
| `alertDetails` | `String` | See declaration / service rules |
| `triggerEvent` | `String` | See declaration / service rules |
| `triggerTimestamp` | `LocalDateTime` | See declaration / service rules |
| `triggerAmount` | `BigDecimal` | See declaration / service rules |
| `triggerCurrency` | `String` | See declaration / service rules |
| `triggerLocation` | `String` | See declaration / service rules |
| `triggerMerchant` | `String` | See declaration / service rules |
| `triggerMerchantCategory` | `String` | See declaration / service rules |
| `isPartyConfigurable` | `Boolean` | See declaration / service rules |
| `isSystemGenerated` | `Boolean` | See declaration / service rules |
| `isRead` | `Boolean` | See declaration / service rules |
| `readTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deliveryChannel` | `String` | See declaration / service rules |
| `deliveryStatus` | `String` | See declaration / service rules |
| `deliveryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deliveryRecipient` | `String` | See declaration / service rules |
| `deliveryDetails` | `String` | See declaration / service rules |
| `expirationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `actionRequired` | `Boolean` | See declaration / service rules |
| `actionType` | `String` | See declaration / service rules |
| `actionTaken` | `String` | See declaration / service rules |
| `actionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `actionBy` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardApplication

[CardApplication](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/application/CardApplication.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `applicationId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `applicationReference` | `String` | See declaration / service rules |
| `applicationType` | `String` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `designId` | `UUID` | See declaration / service rules |
| `applicationStatus` | `String` | See declaration / service rules |
| `applicationStage` | `String` | See declaration / service rules |
| `applicationChannel` | `String` | See declaration / service rules |
| `applicationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `applicantFirstName` | `String` | See declaration / service rules |
| `applicantMiddleName` | `String` | See declaration / service rules |
| `applicantLastName` | `String` | See declaration / service rules |
| `applicantEmail` | `String` | See declaration / service rules |
| `applicantPhone` | `String` | See declaration / service rules |
| `applicantAddressLine1` | `String` | See declaration / service rules |
| `applicantAddressLine2` | `String` | See declaration / service rules |
| `applicantCity` | `String` | See declaration / service rules |
| `applicantState` | `String` | See declaration / service rules |
| `applicantPostalCode` | `String` | See declaration / service rules |
| `applicantCountry` | `String` | See declaration / service rules |
| `applicantIdType` | `String` | See declaration / service rules |
| `applicantIdNumber` | `String` | See declaration / service rules |
| `applicantIdExpiry` | `LocalDateTime` | See declaration / service rules |
| `applicantDateOfBirth` | `LocalDateTime` | See declaration / service rules |
| `applicantNationality` | `String` | See declaration / service rules |
| `employmentStatus` | `String` | See declaration / service rules |
| `employerName` | `String` | See declaration / service rules |
| `employmentPosition` | `String` | See declaration / service rules |
| `annualIncome` | `BigDecimal` | See declaration / service rules |
| `incomeCurrency` | `String` | See declaration / service rules |
| `requestedCreditLimit` | `BigDecimal` | See declaration / service rules |
| `approvedCreditLimit` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `isPreApproved` | `Boolean` | See declaration / service rules |
| `isInstantIssuance` | `Boolean` | See declaration / service rules |
| `isDigitalOnly` | `Boolean` | See declaration / service rules |
| `requiresPhysicalCard` | `Boolean` | See declaration / service rules |
| `shippingAddressSameAsResidential` | `Boolean` | See declaration / service rules |
| `shippingAddressLine1` | `String` | See declaration / service rules |
| `shippingAddressLine2` | `String` | See declaration / service rules |
| `shippingCity` | `String` | See declaration / service rules |
| `shippingState` | `String` | See declaration / service rules |
| `shippingPostalCode` | `String` | See declaration / service rules |
| `shippingCountry` | `String` | See declaration / service rules |
| `shippingMethod` | `String` | See declaration / service rules |
| `creditScore` | `Integer` | See declaration / service rules |
| `creditScoreProvider` | `String` | See declaration / service rules |
| `creditCheckTimestamp` | `LocalDateTime` | See declaration / service rules |
| `creditCheckReference` | `String` | See declaration / service rules |
| `kycStatus` | `String` | See declaration / service rules |
| `kycReference` | `String` | See declaration / service rules |
| `kycTimestamp` | `LocalDateTime` | See declaration / service rules |
| `amlStatus` | `String` | See declaration / service rules |
| `amlReference` | `String` | See declaration / service rules |
| `amlTimestamp` | `LocalDateTime` | See declaration / service rules |
| `approvalTimestamp` | `LocalDateTime` | See declaration / service rules |
| `approvedBy` | `String` | See declaration / service rules |
| `rejectionReason` | `String` | See declaration / service rules |
| `rejectionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `rejectionCode` | `String` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `cardIssuanceTimestamp` | `LocalDateTime` | See declaration / service rules |
| `termsAccepted` | `Boolean` | See declaration / service rules |
| `termsAcceptedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `termsVersion` | `String` | See declaration / service rules |
| `supportingDocuments` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardBalance

[CardBalance](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/balance/CardBalance.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `balanceId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `statementId` | `UUID` | See declaration / service rules |
| `balanceType` | `String` | See declaration / service rules |
| `balanceCategory` | `String` | See declaration / service rules |
| `balanceDescription` | `String` | See declaration / service rules |
| `balanceAmount` | `BigDecimal` | See declaration / service rules |
| `availableAmount` | `BigDecimal` | See declaration / service rules |
| `reservedAmount` | `BigDecimal` | See declaration / service rules |
| `pendingAmount` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `asOfDate` | `LocalDateTime` | See declaration / service rules |
| `lastTransactionId` | `UUID` | See declaration / service rules |
| `lastTransactionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `lastPaymentId` | `UUID` | See declaration / service rules |
| `lastPaymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `lastStatementId` | `UUID` | See declaration / service rules |
| `lastStatementDate` | `LocalDateTime` | See declaration / service rules |
| `isPromotionalRate` | `Boolean` | See declaration / service rules |
| `promotionId` | `UUID` | See declaration / service rules |
| `promotionEndDate` | `LocalDateTime` | See declaration / service rules |
| `interestRate` | `BigDecimal` | See declaration / service rules |
| `annualPercentageRate` | `BigDecimal` | See declaration / service rules |
| `accruedInterest` | `BigDecimal` | See declaration / service rules |
| `lastInterestAccrualDate` | `LocalDateTime` | See declaration / service rules |
| `nextInterestAccrualDate` | `LocalDateTime` | See declaration / service rules |
| `isInGracePeriod` | `Boolean` | See declaration / service rules |
| `gracePeriodEndDate` | `LocalDateTime` | See declaration / service rules |
| `minimumPaymentDue` | `BigDecimal` | See declaration / service rules |
| `minimumPaymentDueDate` | `LocalDateTime` | See declaration / service rules |
| `daysPastDue` | `Integer` | See declaration / service rules |
| `isDelinquent` | `Boolean` | See declaration / service rules |
| `delinquencyStartDate` | `LocalDateTime` | See declaration / service rules |
| `delinquencyDays` | `Integer` | See declaration / service rules |
| `delinquencyStage` | `String` | See declaration / service rules |
| `isChargedOff` | `Boolean` | See declaration / service rules |
| `chargeOffDate` | `LocalDateTime` | See declaration / service rules |
| `chargeOffAmount` | `BigDecimal` | See declaration / service rules |
| `isWrittenOff` | `Boolean` | See declaration / service rules |
| `writeOffDate` | `LocalDateTime` | See declaration / service rules |
| `writeOffAmount` | `BigDecimal` | See declaration / service rules |
| `isInCollection` | `Boolean` | See declaration / service rules |
| `collectionStartDate` | `LocalDateTime` | See declaration / service rules |
| `collectionAgencyId` | `String` | See declaration / service rules |
| `collectionReference` | `String` | See declaration / service rules |
| `collectionStatus` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### BIN

[BIN](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/bin/BIN.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `binId` | `UUID` | See declaration / service rules |
| `binNumber` | `String` | See declaration / service rules |
| `binLength` | `Integer` | See declaration / service rules |
| `issuerId` | `UUID` | See declaration / service rules |
| `cardNetworkId` | `UUID` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `countryCode` | `String` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### Card

[Card](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/card/Card.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardId` | `UUID` | See declaration / service rules |
| `cardNumber` | `String` | See declaration / service rules |
| `maskedCardNumber` | `String` | See declaration / service rules |
| `cardSequenceNumber` | `String` | See declaration / service rules |
| `binId` | `UUID` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `cardNetworkId` | `UUID` | See declaration / service rules |
| `issuerId` | `UUID` | See declaration / service rules |
| `contractId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `cardStatus` | `CardStatusEnum` | See declaration / service rules |
| `cardHolderName` | `String` | See declaration / service rules |
| `cardHolderId` | `String` | See declaration / service rules |
| `expirationMonth` | `Integer` | See declaration / service rules |
| `expirationYear` | `Integer` | See declaration / service rules |
| `cvv` | `String` | See declaration / service rules |
| `pin` | `String` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `issuanceDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `lastUsedDate` | `LocalDateTime` | See declaration / service rules |
| `isPhysical` | `Boolean` | See declaration / service rules |
| `isVirtual` | `Boolean` | See declaration / service rules |
| `isPrimary` | `Boolean` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `isLocked` | `Boolean` | See declaration / service rules |
| `lockReason` | `String` | See declaration / service rules |
| `dailyLimit` | `Double` | See declaration / service rules |
| `monthlyLimit` | `Double` | See declaration / service rules |
| `creditLimit` | `Double` | See declaration / service rules |
| `availableBalance` | `Double` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `designId` | `UUID` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardActivationAction

[CardActivationAction](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/card/CardActivationAction.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `actionId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `activationTime` | `OffsetDateTime` | See declaration / service rules |

### CardIssuerInstrument

[CardIssuerInstrument](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/card/CardIssuerInstrument.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardId` | `UUID` | See declaration / service rules |
| `tenantId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `adapterId` | `String` | See declaration / service rules |
| `instrumentReference` | `String` | See declaration / service rules |
| `bindingVersion` | `long` | See declaration / service rules |

### CardType

[CardType](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/cardtype/CardType.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `typeName` | `String` | See declaration / service rules |
| `typeCode` | `String` | See declaration / service rules |
| `isCredit` | `Boolean` | See declaration / service rules |
| `isDebit` | `Boolean` | See declaration / service rules |
| `isPrepaid` | `Boolean` | See declaration / service rules |
| `isVirtual` | `Boolean` | See declaration / service rules |
| `isCommercial` | `Boolean` | See declaration / service rules |
| `isGift` | `Boolean` | See declaration / service rules |
| `defaultCreditLimit` | `Double` | See declaration / service rules |
| `defaultDailyLimit` | `Double` | See declaration / service rules |
| `defaultMonthlyLimit` | `Double` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardConfiguration

[CardConfiguration](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/configuration/CardConfiguration.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardConfigurationId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `configKey` | `String` | See declaration / service rules |
| `configValue` | `String` | See declaration / service rules |
| `configType` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `isSystemDefault` | `Boolean` | See declaration / service rules |
| `isProgramDefault` | `Boolean` | See declaration / service rules |
| `isPartyConfigurable` | `Boolean` | See declaration / service rules |
| `allowedValues` | `String` | See declaration / service rules |
| `minValue` | `String` | See declaration / service rules |
| `maxValue` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardDesign

[CardDesign](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/design/CardDesign.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `designId` | `UUID` | See declaration / service rules |
| `designName` | `String` | See declaration / service rules |
| `designCode` | `String` | See declaration / service rules |
| `frontImageUrl` | `String` | See declaration / service rules |
| `backImageUrl` | `String` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `issuerId` | `UUID` | See declaration / service rules |
| `cardNetworkId` | `UUID` | See declaration / service rules |
| `isCustomizable` | `Boolean` | See declaration / service rules |
| `isDefault` | `Boolean` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardDispute

[CardDispute](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/dispute/CardDispute.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `disputeId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `disputeReference` | `String` | See declaration / service rules |
| `providerReference` | `String` | See declaration / service rules |
| `networkReference` | `String` | See declaration / service rules |
| `disputeReasonCode` | `String` | See declaration / service rules |
| `disputeReasonDescription` | `String` | See declaration / service rules |
| `disputeStatus` | `String` | See declaration / service rules |
| `disputeStage` | `String` | See declaration / service rules |
| `disputeAmount` | `BigDecimal` | See declaration / service rules |
| `disputeCurrency` | `String` | See declaration / service rules |
| `filingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `responseDueDate` | `LocalDateTime` | See declaration / service rules |
| `resolutionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `resolutionCode` | `String` | See declaration / service rules |
| `resolutionDescription` | `String` | See declaration / service rules |
| `isCardholderCredited` | `Boolean` | See declaration / service rules |
| `creditTimestamp` | `LocalDateTime` | See declaration / service rules |
| `creditAmount` | `BigDecimal` | See declaration / service rules |
| `creditCurrency` | `String` | See declaration / service rules |
| `isMerchantDebited` | `Boolean` | See declaration / service rules |
| `debitTimestamp` | `LocalDateTime` | See declaration / service rules |
| `debitAmount` | `BigDecimal` | See declaration / service rules |
| `debitCurrency` | `String` | See declaration / service rules |
| `cardholderStatement` | `String` | See declaration / service rules |
| `merchantResponse` | `String` | See declaration / service rules |
| `evidenceDocuments` | `String` | See declaration / service rules |
| `assignedAgentId` | `UUID` | See declaration / service rules |
| `assignedAgentName` | `String` | See declaration / service rules |
| `lastUpdatedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardEnrollment

[CardEnrollment](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/enrollment/CardEnrollment.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `enrollmentId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `enrollmentType` | `String` | See declaration / service rules |
| `enrollmentReference` | `String` | See declaration / service rules |
| `enrollmentStatus` | `String` | See declaration / service rules |
| `enrollmentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `enrollmentChannel` | `String` | See declaration / service rules |
| `enrollmentSource` | `String` | See declaration / service rules |
| `enrollmentAgent` | `String` | See declaration / service rules |
| `promotionId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `featureId` | `UUID` | See declaration / service rules |
| `featureCode` | `String` | See declaration / service rules |
| `featureName` | `String` | See declaration / service rules |
| `isAutoEnrolled` | `Boolean` | See declaration / service rules |
| `isOptIn` | `Boolean` | See declaration / service rules |
| `isOptOut` | `Boolean` | See declaration / service rules |
| `optInTimestamp` | `LocalDateTime` | See declaration / service rules |
| `optOutTimestamp` | `LocalDateTime` | See declaration / service rules |
| `optOutReason` | `String` | See declaration / service rules |
| `startDate` | `LocalDateTime` | See declaration / service rules |
| `endDate` | `LocalDateTime` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deactivationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `isRecurring` | `Boolean` | See declaration / service rules |
| `recurrenceFrequency` | `String` | See declaration / service rules |
| `nextRecurrenceDate` | `LocalDateTime` | See declaration / service rules |
| `lastRecurrenceDate` | `LocalDateTime` | See declaration / service rules |
| `termsAccepted` | `Boolean` | See declaration / service rules |
| `termsAcceptedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `termsVersion` | `String` | See declaration / service rules |
| `confirmationCode` | `String` | See declaration / service rules |
| `confirmationSent` | `Boolean` | See declaration / service rules |
| `confirmationChannel` | `String` | See declaration / service rules |
| `confirmationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `confirmationRecipient` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardFee

[CardFee](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/fee/CardFee.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `feeId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `statementId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `feeReference` | `String` | See declaration / service rules |
| `feeType` | `String` | See declaration / service rules |
| `feeName` | `String` | See declaration / service rules |
| `feeDescription` | `String` | See declaration / service rules |
| `feeAmount` | `BigDecimal` | See declaration / service rules |
| `feePercentage` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `feeStatus` | `String` | See declaration / service rules |
| `isRecurring` | `Boolean` | See declaration / service rules |
| `recurrenceFrequency` | `String` | See declaration / service rules |
| `nextRecurrenceDate` | `LocalDateTime` | See declaration / service rules |
| `isProrated` | `Boolean` | See declaration / service rules |
| `prorationRule` | `String` | See declaration / service rules |
| `isWaived` | `Boolean` | See declaration / service rules |
| `waiverReason` | `String` | See declaration / service rules |
| `waiverReference` | `String` | See declaration / service rules |
| `waiverTimestamp` | `LocalDateTime` | See declaration / service rules |
| `waiverExpiry` | `LocalDateTime` | See declaration / service rules |
| `waiverAuthorizedBy` | `String` | See declaration / service rules |
| `isRefunded` | `Boolean` | See declaration / service rules |
| `refundReason` | `String` | See declaration / service rules |
| `refundReference` | `String` | See declaration / service rules |
| `refundTimestamp` | `LocalDateTime` | See declaration / service rules |
| `refundAmount` | `BigDecimal` | See declaration / service rules |
| `refundAuthorizedBy` | `String` | See declaration / service rules |
| `chargeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `postingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `valueDate` | `LocalDateTime` | See declaration / service rules |
| `taxAmount` | `BigDecimal` | See declaration / service rules |
| `taxRate` | `BigDecimal` | See declaration / service rules |
| `taxType` | `String` | See declaration / service rules |
| `totalAmount` | `BigDecimal` | See declaration / service rules |
| `isBilled` | `Boolean` | See declaration / service rules |
| `billingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isPaid` | `Boolean` | See declaration / service rules |
| `paymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `paymentReference` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### FraudCase

[FraudCase](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/fraud/FraudCase.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `fraudCaseId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `caseReference` | `String` | See declaration / service rules |
| `providerReference` | `String` | See declaration / service rules |
| `networkReference` | `String` | See declaration / service rules |
| `fraudType` | `String` | See declaration / service rules |
| `fraudReasonCode` | `String` | See declaration / service rules |
| `fraudReasonDescription` | `String` | See declaration / service rules |
| `fraudStatus` | `String` | See declaration / service rules |
| `fraudScore` | `Integer` | See declaration / service rules |
| `riskLevel` | `String` | See declaration / service rules |
| `detectionSource` | `String` | See declaration / service rules |
| `detectionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `reportedByParty` | `Boolean` | See declaration / service rules |
| `reportedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `affectedAmount` | `BigDecimal` | See declaration / service rules |
| `affectedCurrency` | `String` | See declaration / service rules |
| `isCardBlocked` | `Boolean` | See declaration / service rules |
| `blockTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isPartyNotified` | `Boolean` | See declaration / service rules |
| `notificationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notificationMethod` | `String` | See declaration / service rules |
| `isCardholderCredited` | `Boolean` | See declaration / service rules |
| `creditTimestamp` | `LocalDateTime` | See declaration / service rules |
| `creditAmount` | `BigDecimal` | See declaration / service rules |
| `creditCurrency` | `String` | See declaration / service rules |
| `resolutionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `resolutionCode` | `String` | See declaration / service rules |
| `resolutionDescription` | `String` | See declaration / service rules |
| `isPoliceReportFiled` | `Boolean` | See declaration / service rules |
| `policeReportNumber` | `String` | See declaration / service rules |
| `policeReportDate` | `LocalDateTime` | See declaration / service rules |
| `evidenceDocuments` | `String` | See declaration / service rules |
| `assignedAgentId` | `UUID` | See declaration / service rules |
| `assignedAgentName` | `String` | See declaration / service rules |
| `lastUpdatedTimestamp` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardGateway

[CardGateway](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/gateway/CardGateway.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `gatewayId` | `UUID` | See declaration / service rules |
| `gatewayReference` | `String` | See declaration / service rules |
| `gatewayName` | `String` | See declaration / service rules |
| `gatewayLegalName` | `String` | See declaration / service rules |
| `gatewayCode` | `String` | See declaration / service rules |
| `gatewayDescription` | `String` | See declaration / service rules |
| `gatewayType` | `String` | See declaration / service rules |
| `gatewayStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `taxId` | `String` | See declaration / service rules |
| `registrationNumber` | `String` | See declaration / service rules |
| `licenseNumber` | `String` | See declaration / service rules |
| `licenseExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `websiteUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | See declaration / service rules |
| `contactPersonName` | `String` | See declaration / service rules |
| `contactPersonTitle` | `String` | See declaration / service rules |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCountries` | `String` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `isDomestic` | `Boolean` | See declaration / service rules |
| `processorId` | `UUID` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `acquirerId` | `UUID` | See declaration / service rules |
| `acquirerName` | `String` | See declaration / service rules |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `redirectUrl` | `String` | See declaration / service rules |
| `callbackUrl` | `String` | See declaration / service rules |
| `hostedPageUrl` | `String` | See declaration / service rules |
| `merchantCount` | `Integer` | See declaration / service rules |
| `transactionVolumeDailyAvg` | `Long` | See declaration / service rules |
| `transactionValueDailyAvg` | `Long` | See declaration / service rules |
| `processingTimeAvgMs` | `Integer` | See declaration / service rules |
| `uptimePercentage` | `Double` | See declaration / service rules |
| `lastDowntimeDate` | `LocalDateTime` | See declaration / service rules |
| `lastDowntimeDurationMinutes` | `Integer` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDelayDays` | `Integer` | See declaration / service rules |
| `feeStructure` | `String` | See declaration / service rules |
| `processingFeePercentage` | `Double` | See declaration / service rules |
| `processingFeeFlat` | `Double` | See declaration / service rules |
| `chargebackFee` | `Double` | See declaration / service rules |
| `refundFee` | `Double` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `contractRenewalDate` | `LocalDateTime` | See declaration / service rules |
| `contractStatus` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceLevel` | `String` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `supportsTokenization` | `Boolean` | See declaration / service rules |
| `supports3dSecure` | `Boolean` | See declaration / service rules |
| `supportsRecurringPayments` | `Boolean` | See declaration / service rules |
| `supportsSplitPayments` | `Boolean` | See declaration / service rules |
| `supportsRefunds` | `Boolean` | See declaration / service rules |
| `supportsPartialRefunds` | `Boolean` | See declaration / service rules |
| `supportsVoid` | `Boolean` | See declaration / service rules |
| `supportsCapture` | `Boolean` | See declaration / service rules |
| `supportsPreAuth` | `Boolean` | See declaration / service rules |
| `supportsApplePay` | `Boolean` | See declaration / service rules |
| `supportsGooglePay` | `Boolean` | See declaration / service rules |
| `supportsSamsungPay` | `Boolean` | See declaration / service rules |
| `supportsPaypal` | `Boolean` | See declaration / service rules |
| `integrationType` | `String` | See declaration / service rules |
| `integrationComplexity` | `String` | See declaration / service rules |
| `integrationDocumentationUrl` | `String` | See declaration / service rules |
| `sdkAvailable` | `Boolean` | See declaration / service rules |
| `sdkPlatforms` | `String` | See declaration / service rules |
| `sdkVersion` | `String` | See declaration / service rules |
| `sdkDocumentationUrl` | `String` | See declaration / service rules |
| `lastAuditDate` | `LocalDateTime` | See declaration / service rules |
| `nextAuditDate` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardInterest

[CardInterest](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/interest/CardInterest.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `interestId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `statementId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `interestReference` | `String` | See declaration / service rules |
| `interestType` | `String` | See declaration / service rules |
| `interestName` | `String` | See declaration / service rules |
| `interestDescription` | `String` | See declaration / service rules |
| `interestRate` | `BigDecimal` | See declaration / service rules |
| `annualPercentageRate` | `BigDecimal` | See declaration / service rules |
| `isVariableRate` | `Boolean` | See declaration / service rules |
| `baseRate` | `BigDecimal` | See declaration / service rules |
| `rateMargin` | `BigDecimal` | See declaration / service rules |
| `rateCap` | `BigDecimal` | See declaration / service rules |
| `rateFloor` | `BigDecimal` | See declaration / service rules |
| `rateChangeFrequency` | `String` | See declaration / service rules |
| `nextRateChangeDate` | `LocalDateTime` | See declaration / service rules |
| `previousRate` | `BigDecimal` | See declaration / service rules |
| `rateChangeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `calculationMethod` | `String` | See declaration / service rules |
| `compoundingFrequency` | `String` | See declaration / service rules |
| `accrualFrequency` | `String` | See declaration / service rules |
| `balanceType` | `String` | See declaration / service rules |
| `balanceAmount` | `BigDecimal` | See declaration / service rules |
| `interestAmount` | `BigDecimal` | See declaration / service rules |
| `accruedInterest` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `accrualStartDate` | `LocalDateTime` | See declaration / service rules |
| `accrualEndDate` | `LocalDateTime` | See declaration / service rules |
| `daysInPeriod` | `Integer` | See declaration / service rules |
| `daysInYear` | `Integer` | See declaration / service rules |
| `isPromotionalRate` | `Boolean` | See declaration / service rules |
| `promotionId` | `UUID` | See declaration / service rules |
| `promotionStartDate` | `LocalDateTime` | See declaration / service rules |
| `promotionEndDate` | `LocalDateTime` | See declaration / service rules |
| `postPromotionRate` | `BigDecimal` | See declaration / service rules |
| `isGracePeriod` | `Boolean` | See declaration / service rules |
| `gracePeriodDays` | `Integer` | See declaration / service rules |
| `gracePeriodEndDate` | `LocalDateTime` | See declaration / service rules |
| `isCharged` | `Boolean` | See declaration / service rules |
| `chargeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `postingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `valueDate` | `LocalDateTime` | See declaration / service rules |
| `isWaived` | `Boolean` | See declaration / service rules |
| `waiverReason` | `String` | See declaration / service rules |
| `waiverReference` | `String` | See declaration / service rules |
| `waiverTimestamp` | `LocalDateTime` | See declaration / service rules |
| `waiverAuthorizedBy` | `String` | See declaration / service rules |
| `isBilled` | `Boolean` | See declaration / service rules |
| `billingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isPaid` | `Boolean` | See declaration / service rules |
| `paymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `paymentReference` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### Issuer

[Issuer](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/issuer/Issuer.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `issuerId` | `UUID` | See declaration / service rules |
| `issuerName` | `String` | See declaration / service rules |
| `issuerCode` | `String` | See declaration / service rules |
| `countryCode` | `String` | See declaration / service rules |
| `contactEmail` | `String` | See declaration / service rules |
| `contactPhone` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `supportUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardLimit

[CardLimit](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/limit/CardLimit.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardLimitId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `limitName` | `String` | See declaration / service rules |
| `limitCode` | `String` | See declaration / service rules |
| `limitType` | `String` | See declaration / service rules |
| `channelType` | `String` | See declaration / service rules |
| `transactionType` | `String` | See declaration / service rules |
| `merchantCategoryCode` | `String` | See declaration / service rules |
| `countryCode` | `String` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `limitAmount` | `BigDecimal` | See declaration / service rules |
| `currentUsage` | `BigDecimal` | See declaration / service rules |
| `availableAmount` | `BigDecimal` | See declaration / service rules |
| `resetPeriod` | `String` | See declaration / service rules |
| `resetDay` | `Integer` | See declaration / service rules |
| `resetHour` | `Integer` | See declaration / service rules |
| `lastResetDate` | `LocalDateTime` | See declaration / service rules |
| `nextResetDate` | `LocalDateTime` | See declaration / service rules |
| `effectiveFrom` | `LocalDateTime` | See declaration / service rules |
| `effectiveTo` | `LocalDateTime` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `isSystemDefault` | `Boolean` | See declaration / service rules |
| `isProgramDefault` | `Boolean` | See declaration / service rules |
| `isPartyConfigurable` | `Boolean` | See declaration / service rules |
| `minLimit` | `BigDecimal` | See declaration / service rules |
| `maxLimit` | `BigDecimal` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardMerchant

[CardMerchant](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/merchant/CardMerchant.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `merchantId` | `UUID` | See declaration / service rules |
| `merchantReference` | `String` | See declaration / service rules |
| `merchantName` | `String` | See declaration / service rules |
| `merchantLegalName` | `String` | See declaration / service rules |
| `merchantDisplayName` | `String` | See declaration / service rules |
| `merchantDescription` | `String` | See declaration / service rules |
| `merchantCategoryCode` | `String` | See declaration / service rules |
| `merchantCategoryName` | `String` | See declaration / service rules |
| `merchantType` | `String` | See declaration / service rules |
| `merchantStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `taxId` | `String` | See declaration / service rules |
| `registrationNumber` | `String` | See declaration / service rules |
| `websiteUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | See declaration / service rules |
| `contactPersonName` | `String` | See declaration / service rules |
| `contactPersonTitle` | `String` | See declaration / service rules |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | See declaration / service rules |
| `isOnline` | `Boolean` | See declaration / service rules |
| `isPhysical` | `Boolean` | See declaration / service rules |
| `isMobile` | `Boolean` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `isHighRisk` | `Boolean` | See declaration / service rules |
| `riskRating` | `String` | See declaration / service rules |
| `riskScore` | `Integer` | See declaration / service rules |
| `riskAssessmentDate` | `LocalDateTime` | See declaration / service rules |
| `isFraudSuspected` | `Boolean` | See declaration / service rules |
| `fraudReason` | `String` | See declaration / service rules |
| `fraudReportDate` | `LocalDateTime` | See declaration / service rules |
| `isBlacklisted` | `Boolean` | See declaration / service rules |
| `blacklistReason` | `String` | See declaration / service rules |
| `blacklistDate` | `LocalDateTime` | See declaration / service rules |
| `isSettlementEnabled` | `Boolean` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDay` | `Integer` | See declaration / service rules |
| `settlementBankName` | `String` | See declaration / service rules |
| `settlementAccountNumber` | `String` | See declaration / service rules |
| `settlementAccountName` | `String` | See declaration / service rules |
| `settlementBankCode` | `String` | See declaration / service rules |
| `settlementCurrency` | `String` | See declaration / service rules |
| `acquirerId` | `String` | See declaration / service rules |
| `acquirerName` | `String` | See declaration / service rules |
| `processorId` | `String` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `terminalIds` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardNetwork

[CardNetwork](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/network/CardNetwork.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardNetworkId` | `UUID` | See declaration / service rules |
| `networkName` | `String` | See declaration / service rules |
| `networkCode` | `String` | See declaration / service rules |
| `networkLogoUrl` | `String` | See declaration / service rules |
| `supportContact` | `String` | See declaration / service rules |
| `apiEndpoint` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardPayment

[CardPayment](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/payment/CardPayment.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `paymentId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `statementId` | `UUID` | See declaration / service rules |
| `paymentReference` | `String` | See declaration / service rules |
| `externalReference` | `String` | See declaration / service rules |
| `paymentAmount` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `paymentMethod` | `String` | See declaration / service rules |
| `paymentChannel` | `String` | See declaration / service rules |
| `paymentStatus` | `String` | See declaration / service rules |
| `paymentTimestamp` | `LocalDateTime` | See declaration / service rules |
| `postingTimestamp` | `LocalDateTime` | See declaration / service rules |
| `valueDate` | `LocalDateTime` | See declaration / service rules |
| `isAutoPayment` | `Boolean` | See declaration / service rules |
| `isMinimumPayment` | `Boolean` | See declaration / service rules |
| `isFullPayment` | `Boolean` | See declaration / service rules |
| `isScheduledPayment` | `Boolean` | See declaration / service rules |
| `scheduledDate` | `LocalDateTime` | See declaration / service rules |
| `recurrencePattern` | `String` | See declaration / service rules |
| `sourceAccountId` | `String` | See declaration / service rules |
| `sourceAccountType` | `String` | See declaration / service rules |
| `sourceBankCode` | `String` | See declaration / service rules |
| `sourceBankName` | `String` | See declaration / service rules |
| `sourceAccountHolder` | `String` | See declaration / service rules |
| `paymentProcessor` | `String` | See declaration / service rules |
| `processorFee` | `BigDecimal` | See declaration / service rules |
| `processorReference` | `String` | See declaration / service rules |
| `confirmationCode` | `String` | See declaration / service rules |
| `failureReason` | `String` | See declaration / service rules |
| `failureCode` | `String` | See declaration / service rules |
| `retryCount` | `Integer` | See declaration / service rules |
| `lastRetryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `nextRetryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `receiptUrl` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### PhysicalCard

[PhysicalCard](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/physical/PhysicalCard.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `physicalCardId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `embossedName` | `String` | See declaration / service rules |
| `plasticId` | `String` | See declaration / service rules |
| `designId` | `UUID` | See declaration / service rules |
| `isContactless` | `Boolean` | See declaration / service rules |
| `isChip` | `Boolean` | See declaration / service rules |
| `isMagstripe` | `Boolean` | See declaration / service rules |
| `manufacturingStatus` | `String` | See declaration / service rules |
| `manufacturingDate` | `LocalDateTime` | See declaration / service rules |
| `shippingAddress` | `String` | See declaration / service rules |
| `shippingCity` | `String` | See declaration / service rules |
| `shippingState` | `String` | See declaration / service rules |
| `shippingCountry` | `String` | See declaration / service rules |
| `shippingPostalCode` | `String` | See declaration / service rules |
| `shippingMethod` | `String` | See declaration / service rules |
| `shippingTrackingNumber` | `String` | See declaration / service rules |
| `shippingCarrier` | `String` | See declaration / service rules |
| `shipmentDate` | `LocalDateTime` | See declaration / service rules |
| `estimatedDeliveryDate` | `LocalDateTime` | See declaration / service rules |
| `actualDeliveryDate` | `LocalDateTime` | See declaration / service rules |
| `activationMethod` | `String` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `isActivated` | `Boolean` | See declaration / service rules |
| `replacementReason` | `String` | See declaration / service rules |
| `previousCardId` | `UUID` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardProcessor

[CardProcessor](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/processor/CardProcessor.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `processorId` | `UUID` | See declaration / service rules |
| `processorReference` | `String` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `processorLegalName` | `String` | See declaration / service rules |
| `processorCode` | `String` | See declaration / service rules |
| `processorDescription` | `String` | See declaration / service rules |
| `processorType` | `String` | See declaration / service rules |
| `processorStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `taxId` | `String` | See declaration / service rules |
| `registrationNumber` | `String` | See declaration / service rules |
| `licenseNumber` | `String` | See declaration / service rules |
| `licenseExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `websiteUrl` | `String` | See declaration / service rules |
| `logoUrl` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `phone` | `String` | See declaration / service rules |
| `email` | `String` | See declaration / service rules |
| `contactPersonName` | `String` | See declaration / service rules |
| `contactPersonTitle` | `String` | See declaration / service rules |
| `contactPersonPhone` | `String` | See declaration / service rules |
| `contactPersonEmail` | `String` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `supportedCountries` | `String` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `isDomestic` | `Boolean` | See declaration / service rules |
| `isIssuerProcessor` | `Boolean` | See declaration / service rules |
| `isAcquirerProcessor` | `Boolean` | See declaration / service rules |
| `isSwitchProcessor` | `Boolean` | See declaration / service rules |
| `isGateway` | `Boolean` | See declaration / service rules |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `acquirerCount` | `Integer` | See declaration / service rules |
| `issuerCount` | `Integer` | See declaration / service rules |
| `merchantCount` | `Integer` | See declaration / service rules |
| `terminalCount` | `Integer` | See declaration / service rules |
| `transactionVolumeDailyAvg` | `Long` | See declaration / service rules |
| `transactionValueDailyAvg` | `Long` | See declaration / service rules |
| `processingTimeAvgMs` | `Integer` | See declaration / service rules |
| `uptimePercentage` | `Double` | See declaration / service rules |
| `lastDowntimeDate` | `LocalDateTime` | See declaration / service rules |
| `lastDowntimeDurationMinutes` | `Integer` | See declaration / service rules |
| `settlementFrequency` | `String` | See declaration / service rules |
| `settlementDelayDays` | `Integer` | See declaration / service rules |
| `feeStructure` | `String` | See declaration / service rules |
| `processingFeePercentage` | `Double` | See declaration / service rules |
| `processingFeeFlat` | `Double` | See declaration / service rules |
| `chargebackFee` | `Double` | See declaration / service rules |
| `refundFee` | `Double` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `contractRenewalDate` | `LocalDateTime` | See declaration / service rules |
| `contractStatus` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceLevel` | `String` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `isEmvCompliant` | `Boolean` | See declaration / service rules |
| `emvComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `isPaDssCompliant` | `Boolean` | See declaration / service rules |
| `paDssComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `paDssComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `supportsTokenization` | `Boolean` | See declaration / service rules |
| `supports3dSecure` | `Boolean` | See declaration / service rules |
| `supportsEmv` | `Boolean` | See declaration / service rules |
| `supportsContactless` | `Boolean` | See declaration / service rules |
| `supportsApplePay` | `Boolean` | See declaration / service rules |
| `supportsGooglePay` | `Boolean` | See declaration / service rules |
| `supportsSamsungPay` | `Boolean` | See declaration / service rules |
| `lastAuditDate` | `LocalDateTime` | See declaration / service rules |
| `nextAuditDate` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardProgram

[CardProgram](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/program/CardProgram.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `programId` | `UUID` | See declaration / service rules |
| `programName` | `String` | See declaration / service rules |
| `programCode` | `String` | See declaration / service rules |
| `issuerId` | `UUID` | See declaration / service rules |
| `binId` | `UUID` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `cardNetworkId` | `UUID` | See declaration / service rules |
| `defaultDesignId` | `UUID` | See declaration / service rules |
| `startDate` | `LocalDateTime` | See declaration / service rules |
| `endDate` | `LocalDateTime` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `maxCardsPerParty` | `Integer` | See declaration / service rules |
| `defaultDailyLimit` | `Double` | See declaration / service rules |
| `defaultMonthlyLimit` | `Double` | See declaration / service rules |
| `defaultCreditLimit` | `Double` | See declaration / service rules |
| `defaultCardValidityYears` | `Integer` | See declaration / service rules |
| `supportsPhysicalCards` | `Boolean` | See declaration / service rules |
| `supportsVirtualCards` | `Boolean` | See declaration / service rules |
| `supportsContactless` | `Boolean` | See declaration / service rules |
| `supportsInternational` | `Boolean` | See declaration / service rules |
| `supportsAtmWithdrawal` | `Boolean` | See declaration / service rules |
| `supportsOnlineTransactions` | `Boolean` | See declaration / service rules |
| `supportsRecurringPayments` | `Boolean` | See declaration / service rules |
| `supportsApplePay` | `Boolean` | See declaration / service rules |
| `supportsGooglePay` | `Boolean` | See declaration / service rules |
| `supportsSamsungPay` | `Boolean` | See declaration / service rules |
| `requiresPin` | `Boolean` | See declaration / service rules |
| `requiresActivation` | `Boolean` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `countryCode` | `String` | See declaration / service rules |
| `termsAndConditionsUrl` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardPromotion

[CardPromotion](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/promotion/CardPromotion.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `promotionId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `cardTypeId` | `UUID` | See declaration / service rules |
| `issuerId` | `UUID` | See declaration / service rules |
| `promotionCode` | `String` | See declaration / service rules |
| `promotionName` | `String` | See declaration / service rules |
| `promotionDescription` | `String` | See declaration / service rules |
| `promotionType` | `String` | See declaration / service rules |
| `promotionCategory` | `String` | See declaration / service rules |
| `promotionStatus` | `String` | See declaration / service rules |
| `startDate` | `LocalDateTime` | See declaration / service rules |
| `endDate` | `LocalDateTime` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `isLimitedTime` | `Boolean` | See declaration / service rules |
| `isTargeted` | `Boolean` | See declaration / service rules |
| `targetCriteria` | `String` | See declaration / service rules |
| `isOptInRequired` | `Boolean` | See declaration / service rules |
| `optInChannel` | `String` | See declaration / service rules |
| `optInUrl` | `String` | See declaration / service rules |
| `isAutoEnrolled` | `Boolean` | See declaration / service rules |
| `maxEnrollments` | `Integer` | See declaration / service rules |
| `currentEnrollments` | `Integer` | See declaration / service rules |
| `benefitType` | `String` | See declaration / service rules |
| `benefitValue` | `BigDecimal` | See declaration / service rules |
| `benefitPercentage` | `BigDecimal` | See declaration / service rules |
| `benefitDescription` | `String` | See declaration / service rules |
| `minimumSpend` | `BigDecimal` | See declaration / service rules |
| `maximumBenefit` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `merchantCategoryCodes` | `String` | See declaration / service rules |
| `merchantIds` | `String` | See declaration / service rules |
| `countryCodes` | `String` | See declaration / service rules |
| `transactionTypes` | `String` | See declaration / service rules |
| `channelTypes` | `String` | See declaration / service rules |
| `isFirstTransactionOnly` | `Boolean` | See declaration / service rules |
| `isNewCardsOnly` | `Boolean` | See declaration / service rules |
| `cardAgeMinDays` | `Integer` | See declaration / service rules |
| `cardAgeMaxDays` | `Integer` | See declaration / service rules |
| `isBalanceTransfer` | `Boolean` | See declaration / service rules |
| `balanceTransferFee` | `BigDecimal` | See declaration / service rules |
| `balanceTransferFeePercentage` | `BigDecimal` | See declaration / service rules |
| `balanceTransferMinAmount` | `BigDecimal` | See declaration / service rules |
| `balanceTransferMaxAmount` | `BigDecimal` | See declaration / service rules |
| `isIntroductoryRate` | `Boolean` | See declaration / service rules |
| `introductoryRate` | `BigDecimal` | See declaration / service rules |
| `postIntroductoryRate` | `BigDecimal` | See declaration / service rules |
| `introductoryPeriodMonths` | `Integer` | See declaration / service rules |
| `termsAndConditions` | `String` | See declaration / service rules |
| `termsUrl` | `String` | See declaration / service rules |
| `marketingImageUrl` | `String` | See declaration / service rules |
| `marketingDescription` | `String` | See declaration / service rules |
| `isCombinable` | `Boolean` | See declaration / service rules |
| `combinablePromotions` | `String` | See declaration / service rules |
| `exclusionCriteria` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardProvider

[CardProvider](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/provider/CardProvider.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardProviderId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `providerName` | `String` | See declaration / service rules |
| `providerCode` | `String` | See declaration / service rules |
| `providerType` | `String` | See declaration / service rules |
| `apiBaseUrl` | `String` | See declaration / service rules |
| `apiVersion` | `String` | See declaration / service rules |
| `apiKey` | `String` | See declaration / service rules |
| `apiSecret` | `String` | See declaration / service rules |
| `apiUsername` | `String` | See declaration / service rules |
| `apiPassword` | `String` | See declaration / service rules |
| `apiCertificate` | `String` | See declaration / service rules |
| `webhookUrl` | `String` | See declaration / service rules |
| `webhookSecret` | `String` | See declaration / service rules |
| `supportsPhysicalCards` | `Boolean` | See declaration / service rules |
| `supportsVirtualCards` | `Boolean` | See declaration / service rules |
| `supportsTokenization` | `Boolean` | See declaration / service rules |
| `supports3dSecure` | `Boolean` | See declaration / service rules |
| `supportsApplePay` | `Boolean` | See declaration / service rules |
| `supportsGooglePay` | `Boolean` | See declaration / service rules |
| `supportsSamsungPay` | `Boolean` | See declaration / service rules |
| `contactName` | `String` | See declaration / service rules |
| `contactEmail` | `String` | See declaration / service rules |
| `contactPhone` | `String` | See declaration / service rules |
| `supportUrl` | `String` | See declaration / service rules |
| `supportEmail` | `String` | See declaration / service rules |
| `supportPhone` | `String` | See declaration / service rules |
| `contractStartDate` | `LocalDateTime` | See declaration / service rules |
| `contractEndDate` | `LocalDateTime` | See declaration / service rules |
| `status` | `ProviderStatusEnum` | See declaration / service rules |
| `lastConnectionDate` | `LocalDateTime` | See declaration / service rules |
| `lastConnectionStatus` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardReward

[CardReward](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/reward/CardReward.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `rewardId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `transactionId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `rewardReference` | `String` | See declaration / service rules |
| `rewardType` | `String` | See declaration / service rules |
| `rewardCategory` | `String` | See declaration / service rules |
| `rewardDescription` | `String` | See declaration / service rules |
| `rewardStatus` | `String` | See declaration / service rules |
| `isEarning` | `Boolean` | See declaration / service rules |
| `isRedemption` | `Boolean` | See declaration / service rules |
| `isAdjustment` | `Boolean` | See declaration / service rules |
| `isExpiration` | `Boolean` | See declaration / service rules |
| `pointsEarned` | `BigDecimal` | See declaration / service rules |
| `pointsRedeemed` | `BigDecimal` | See declaration / service rules |
| `pointsAdjusted` | `BigDecimal` | See declaration / service rules |
| `pointsExpired` | `BigDecimal` | See declaration / service rules |
| `pointsBalance` | `BigDecimal` | See declaration / service rules |
| `cashValue` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `earningRate` | `BigDecimal` | See declaration / service rules |
| `earningMultiplier` | `BigDecimal` | See declaration / service rules |
| `earningReason` | `String` | See declaration / service rules |
| `redemptionType` | `String` | See declaration / service rules |
| `redemptionDescription` | `String` | See declaration / service rules |
| `redemptionValue` | `BigDecimal` | See declaration / service rules |
| `adjustmentReason` | `String` | See declaration / service rules |
| `adjustmentDescription` | `String` | See declaration / service rules |
| `merchantId` | `String` | See declaration / service rules |
| `merchantName` | `String` | See declaration / service rules |
| `merchantCategory` | `String` | See declaration / service rules |
| `transactionAmount` | `BigDecimal` | See declaration / service rules |
| `transactionCurrency` | `String` | See declaration / service rules |
| `transactionDate` | `LocalDateTime` | See declaration / service rules |
| `postingDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `isPromotional` | `Boolean` | See declaration / service rules |
| `promotionId` | `UUID` | See declaration / service rules |
| `promotionName` | `String` | See declaration / service rules |
| `isTransferable` | `Boolean` | See declaration / service rules |
| `transferToPartyId` | `UUID` | See declaration / service rules |
| `transferFromPartyId` | `UUID` | See declaration / service rules |
| `transferDate` | `LocalDateTime` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardSecurity

[CardSecurity](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/security/CardSecurity.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardSecurityId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `programId` | `UUID` | See declaration / service rules |
| `securityFeatureName` | `String` | See declaration / service rules |
| `securityFeatureCode` | `String` | See declaration / service rules |
| `securityValue` | `String` | See declaration / service rules |
| `isEnabled` | `Boolean` | See declaration / service rules |
| `isMandatory` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `expirationDate` | `LocalDateTime` | See declaration / service rules |
| `lastUpdatedDate` | `LocalDateTime` | See declaration / service rules |
| `lastUsedDate` | `LocalDateTime` | See declaration / service rules |
| `isSystemDefault` | `Boolean` | See declaration / service rules |
| `isProgramDefault` | `Boolean` | See declaration / service rules |
| `isPartyConfigurable` | `Boolean` | See declaration / service rules |
| `allowedValues` | `String` | See declaration / service rules |
| `minValue` | `String` | See declaration / service rules |
| `maxValue` | `String` | See declaration / service rules |
| `supports3dSecure` | `Boolean` | See declaration / service rules |
| `supportsTokenization` | `Boolean` | See declaration / service rules |
| `supportsBiometric` | `Boolean` | See declaration / service rules |
| `supportsPin` | `Boolean` | See declaration / service rules |
| `supportsOtp` | `Boolean` | See declaration / service rules |
| `fraudDetectionLevel` | `String` | See declaration / service rules |
| `description` | `String` | See declaration / service rules |

### CardStatement

[CardStatement](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/statement/CardStatement.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `statementId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `statementReference` | `String` | See declaration / service rules |
| `statementDate` | `LocalDateTime` | See declaration / service rules |
| `statementPeriodStart` | `LocalDateTime` | See declaration / service rules |
| `statementPeriodEnd` | `LocalDateTime` | See declaration / service rules |
| `dueDate` | `LocalDateTime` | See declaration / service rules |
| `closingBalance` | `BigDecimal` | See declaration / service rules |
| `openingBalance` | `BigDecimal` | See declaration / service rules |
| `minimumPaymentDue` | `BigDecimal` | See declaration / service rules |
| `totalPaymentDue` | `BigDecimal` | See declaration / service rules |
| `currencyCode` | `String` | See declaration / service rules |
| `totalPurchases` | `BigDecimal` | See declaration / service rules |
| `totalCashAdvances` | `BigDecimal` | See declaration / service rules |
| `totalFees` | `BigDecimal` | See declaration / service rules |
| `totalInterest` | `BigDecimal` | See declaration / service rules |
| `totalCredits` | `BigDecimal` | See declaration / service rules |
| `totalPayments` | `BigDecimal` | See declaration / service rules |
| `totalAdjustments` | `BigDecimal` | See declaration / service rules |
| `totalRewardsEarned` | `BigDecimal` | See declaration / service rules |
| `totalRewardsRedeemed` | `BigDecimal` | See declaration / service rules |
| `availableCredit` | `BigDecimal` | See declaration / service rules |
| `creditLimit` | `BigDecimal` | See declaration / service rules |
| `cashAdvanceLimit` | `BigDecimal` | See declaration / service rules |
| `interestRate` | `BigDecimal` | See declaration / service rules |
| `annualPercentageRate` | `BigDecimal` | See declaration / service rules |
| `paymentStatus` | `String` | See declaration / service rules |
| `isGenerated` | `Boolean` | See declaration / service rules |
| `generationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isDelivered` | `Boolean` | See declaration / service rules |
| `deliveryMethod` | `String` | See declaration / service rules |
| `deliveryTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deliveryAddress` | `String` | See declaration / service rules |
| `isViewed` | `Boolean` | See declaration / service rules |
| `viewTimestamp` | `LocalDateTime` | See declaration / service rules |
| `documentUrl` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardTerminal

[CardTerminal](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/terminal/CardTerminal.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `terminalId` | `UUID` | See declaration / service rules |
| `terminalReference` | `String` | See declaration / service rules |
| `terminalSerialNumber` | `String` | See declaration / service rules |
| `terminalName` | `String` | See declaration / service rules |
| `terminalDescription` | `String` | See declaration / service rules |
| `terminalType` | `String` | See declaration / service rules |
| `terminalModel` | `String` | See declaration / service rules |
| `terminalManufacturer` | `String` | See declaration / service rules |
| `terminalStatus` | `String` | See declaration / service rules |
| `isActive` | `Boolean` | See declaration / service rules |
| `activationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `merchantId` | `UUID` | See declaration / service rules |
| `merchantName` | `String` | See declaration / service rules |
| `merchantLocationId` | `String` | See declaration / service rules |
| `merchantLocationName` | `String` | See declaration / service rules |
| `addressLine1` | `String` | See declaration / service rules |
| `addressLine2` | `String` | See declaration / service rules |
| `city` | `String` | See declaration / service rules |
| `state` | `String` | See declaration / service rules |
| `postalCode` | `String` | See declaration / service rules |
| `country` | `String` | See declaration / service rules |
| `geolocation` | `String` | See declaration / service rules |
| `acquirerId` | `String` | See declaration / service rules |
| `acquirerName` | `String` | See declaration / service rules |
| `processorId` | `String` | See declaration / service rules |
| `processorName` | `String` | See declaration / service rules |
| `softwareVersion` | `String` | See declaration / service rules |
| `firmwareVersion` | `String` | See declaration / service rules |
| `hardwareVersion` | `String` | See declaration / service rules |
| `lastMaintenanceDate` | `LocalDateTime` | See declaration / service rules |
| `nextMaintenanceDate` | `LocalDateTime` | See declaration / service rules |
| `lastUpdateDate` | `LocalDateTime` | See declaration / service rules |
| `installationDate` | `LocalDateTime` | See declaration / service rules |
| `isPhysical` | `Boolean` | See declaration / service rules |
| `isVirtual` | `Boolean` | See declaration / service rules |
| `isMobile` | `Boolean` | See declaration / service rules |
| `isAttended` | `Boolean` | See declaration / service rules |
| `isUnattended` | `Boolean` | See declaration / service rules |
| `isContactless` | `Boolean` | See declaration / service rules |
| `isChip` | `Boolean` | See declaration / service rules |
| `isMagstripe` | `Boolean` | See declaration / service rules |
| `isPinSupported` | `Boolean` | See declaration / service rules |
| `isSignatureSupported` | `Boolean` | See declaration / service rules |
| `isBiometricSupported` | `Boolean` | See declaration / service rules |
| `isNfcSupported` | `Boolean` | See declaration / service rules |
| `isQrSupported` | `Boolean` | See declaration / service rules |
| `isEmvSupported` | `Boolean` | See declaration / service rules |
| `isP2peSupported` | `Boolean` | See declaration / service rules |
| `isTokenizationSupported` | `Boolean` | See declaration / service rules |
| `supportedCardNetworks` | `String` | See declaration / service rules |
| `supportedPaymentMethods` | `String` | See declaration / service rules |
| `supportedCurrencies` | `String` | See declaration / service rules |
| `defaultCurrency` | `String` | See declaration / service rules |
| `communicationType` | `String` | See declaration / service rules |
| `connectionType` | `String` | See declaration / service rules |
| `ipAddress` | `String` | See declaration / service rules |
| `macAddress` | `String` | See declaration / service rules |
| `isPciCompliant` | `Boolean` | See declaration / service rules |
| `pciComplianceDate` | `LocalDateTime` | See declaration / service rules |
| `pciComplianceExpiry` | `LocalDateTime` | See declaration / service rules |
| `encryptionMethod` | `String` | See declaration / service rules |
| `keyManagementScheme` | `String` | See declaration / service rules |
| `lastKeyRotationDate` | `LocalDateTime` | See declaration / service rules |
| `nextKeyRotationDate` | `LocalDateTime` | See declaration / service rules |
| `isFaultDetected` | `Boolean` | See declaration / service rules |
| `faultCode` | `String` | See declaration / service rules |
| `faultDescription` | `String` | See declaration / service rules |
| `faultDetectionDate` | `LocalDateTime` | See declaration / service rules |
| `lastTransactionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `lastTransactionId` | `String` | See declaration / service rules |
| `lastTransactionStatus` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### CardTransaction

[CardTransaction](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/transaction/CardTransaction.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `cardTransactionId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `accountId` | `UUID` | See declaration / service rules |
| `partyId` | `UUID` | See declaration / service rules |
| `transactionReference` | `String` | See declaration / service rules |
| `providerReference` | `String` | See declaration / service rules |
| `networkReference` | `String` | See declaration / service rules |
| `transactionType` | `TransactionTypeEnum` | See declaration / service rules |
| `transactionStatus` | `TransactionStatusEnum` | See declaration / service rules |
| `transactionTimestamp` | `LocalDateTime` | See declaration / service rules |
| `settlementTimestamp` | `LocalDateTime` | See declaration / service rules |
| `authorizationCode` | `String` | See declaration / service rules |
| `responseCode` | `String` | See declaration / service rules |
| `responseMessage` | `String` | See declaration / service rules |
| `transactionAmount` | `BigDecimal` | See declaration / service rules |
| `transactionCurrency` | `String` | See declaration / service rules |
| `billingAmount` | `BigDecimal` | See declaration / service rules |
| `billingCurrency` | `String` | See declaration / service rules |
| `exchangeRate` | `BigDecimal` | See declaration / service rules |
| `feeAmount` | `BigDecimal` | See declaration / service rules |
| `feeCurrency` | `String` | See declaration / service rules |
| `taxAmount` | `BigDecimal` | See declaration / service rules |
| `taxCurrency` | `String` | See declaration / service rules |
| `merchantId` | `String` | See declaration / service rules |
| `merchantName` | `String` | See declaration / service rules |
| `merchantCategoryCode` | `String` | See declaration / service rules |
| `merchantCategoryName` | `String` | See declaration / service rules |
| `merchantCity` | `String` | See declaration / service rules |
| `merchantState` | `String` | See declaration / service rules |
| `merchantCountry` | `String` | See declaration / service rules |
| `merchantPostalCode` | `String` | See declaration / service rules |
| `terminalId` | `String` | See declaration / service rules |
| `terminalType` | `String` | See declaration / service rules |
| `entryMode` | `String` | See declaration / service rules |
| `channelType` | `String` | See declaration / service rules |
| `isCardPresent` | `Boolean` | See declaration / service rules |
| `isPinVerified` | `Boolean` | See declaration / service rules |
| `isSignatureVerified` | `Boolean` | See declaration / service rules |
| `is3dSecure` | `Boolean` | See declaration / service rules |
| `isTokenized` | `Boolean` | See declaration / service rules |
| `isRecurring` | `Boolean` | See declaration / service rules |
| `isInstallment` | `Boolean` | See declaration / service rules |
| `installmentPlan` | `String` | See declaration / service rules |
| `installmentNumber` | `Integer` | See declaration / service rules |
| `totalInstallments` | `Integer` | See declaration / service rules |
| `isInternational` | `Boolean` | See declaration / service rules |
| `isDisputed` | `Boolean` | See declaration / service rules |
| `disputeReason` | `String` | See declaration / service rules |
| `disputeStatus` | `String` | See declaration / service rules |
| `disputeTimestamp` | `LocalDateTime` | See declaration / service rules |
| `isFraudSuspected` | `Boolean` | See declaration / service rules |
| `fraudReason` | `String` | See declaration / service rules |
| `fraudScore` | `Integer` | See declaration / service rules |
| `deviceId` | `String` | See declaration / service rules |
| `ipAddress` | `String` | See declaration / service rules |
| `userAgent` | `String` | See declaration / service rules |
| `geolocation` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

### VirtualCard

[VirtualCard](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/entities/virtual/VirtualCard.java) · Persisted entity

| Field | Java type | Validation annotations |
| --- | --- | --- |
| `virtualCardId` | `UUID` | See declaration / service rules |
| `cardId` | `UUID` | See declaration / service rules |
| `deviceId` | `String` | See declaration / service rules |
| `deviceType` | `String` | See declaration / service rules |
| `deviceModel` | `String` | See declaration / service rules |
| `deviceOs` | `String` | See declaration / service rules |
| `deviceOsVersion` | `String` | See declaration / service rules |
| `walletProvider` | `String` | See declaration / service rules |
| `walletAccountId` | `String` | See declaration / service rules |
| `tokenizationProvider` | `String` | See declaration / service rules |
| `tokenId` | `String` | See declaration / service rules |
| `tokenExpiryDate` | `LocalDateTime` | See declaration / service rules |
| `tokenStatus` | `String` | See declaration / service rules |
| `virtualCardNumber` | `String` | See declaration / service rules |
| `virtualCardStatus` | `VirtualCardStatusEnum` | See declaration / service rules |
| `isDefaultForDevice` | `Boolean` | See declaration / service rules |
| `isDefaultForWallet` | `Boolean` | See declaration / service rules |
| `isProvisioned` | `Boolean` | See declaration / service rules |
| `provisioningDate` | `LocalDateTime` | See declaration / service rules |
| `lastUsedDate` | `LocalDateTime` | See declaration / service rules |
| `creationTimestamp` | `LocalDateTime` | See declaration / service rules |
| `updateTimestamp` | `LocalDateTime` | See declaration / service rules |
| `deactivationDate` | `LocalDateTime` | See declaration / service rules |
| `deactivationReason` | `String` | See declaration / service rules |
| `notes` | `String` | See declaration / service rules |

## Ports and service contracts

### CardGatewayMapper

[CardGatewayMapper](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/mappers/gateway/CardGatewayMapper.java)

```java
CardGatewayDTO toDTO(CardGateway cardGateway);
CardGateway toEntity(CardGatewayDTO cardGatewayDTO);
```

### CardProviderMapper

[CardProviderMapper](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/mappers/provider/CardProviderMapper.java)

```java
CardProviderDTO toDTO (CardProvider cardProvider);
CardProvider toEntity (CardProviderDTO cardProviderDTO);
```

### CardAcquirerService

[CardAcquirerService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/acquirer/CardAcquirerService.java)

```java
Mono<PaginationResponse<CardAcquirerDTO>> listAcquirers(PaginationRequest paginationRequest);
Mono<CardAcquirerDTO> createAcquirer(CardAcquirerDTO acquirerDTO);
Mono<CardAcquirerDTO> getAcquirer(UUID acquirerId);
Mono<CardAcquirerDTO> updateAcquirer(UUID acquirerId, CardAcquirerDTO acquirerDTO);
Mono<Void> deleteAcquirer(UUID acquirerId);
```

### CardActivityService

[CardActivityService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/activity/CardActivityService.java)

```java
Mono<PaginationResponse<CardActivityDTO>> listActivities( UUID cardId, PaginationRequest paginationRequest );
Mono<CardActivityDTO> createActivity(UUID cardId, CardActivityDTO activityDTO);
Mono<CardActivityDTO> getActivity(UUID cardId, UUID activityId);
Mono<CardActivityDTO> updateActivity(UUID cardId, UUID activityId, CardActivityDTO activityDTO);
Mono<Void> deleteActivity(UUID cardId, UUID activityId);
```

### CardBalanceService

[CardBalanceService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/balance/CardBalanceService.java)

```java
Mono<PaginationResponse<CardBalanceDTO>> listBalances( UUID cardId, PaginationRequest paginationRequest );
Mono<CardBalanceDTO> createBalance(UUID cardId, CardBalanceDTO balanceDTO);
Mono<CardBalanceDTO> getBalance(UUID cardId, UUID balanceId);
Mono<CardBalanceDTO> updateBalance(UUID cardId, UUID balanceId, CardBalanceDTO balanceDTO);
Mono<Void> deleteBalance(UUID cardId, UUID balanceId);
```

### BINService

[BINService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/bin/BINService.java)

```java
Mono<PaginationResponse<BINDTO>> listBINs(PaginationRequest paginationRequest);
Mono<BINDTO> createBIN(BINDTO binDTO);
Mono<BINDTO> getBIN(UUID binId);
Mono<BINDTO> getBINByNumber(String binNumber);
Mono<BINDTO> updateBIN(UUID binId, BINDTO binDTO);
Mono<Void> deleteBIN(UUID binId);
```

### CardService

[CardService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/CardService.java)

```java
Mono<PaginationResponse<CardDTO>> filterCards(FilterRequest<CardDTO> filterRequest);
Mono<CardDTO> createCard(CardDTO cardDTO);
Mono<CardDTO> getCard(UUID cardId);
Mono<CardDTO> updateCard(UUID cardId, CardDTO cardDTO);
Mono<Void> deleteCard(UUID cardId);
```

### CardActivationPolicy

[CardActivationPolicy](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardActivationPolicy.java)

```java
Mono<AuthorizedScope> authorize(UUID cardId);
record AuthorizedScope(UUID tenantId, UUID actorId, UUID partyId, UUID accountId, OffsetDateTime expiresAt);
```

### CardActivationService

[CardActivationService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardActivationService.java)

```java
Mono<CardActivationReceipt> activate(UUID cardId, UUID actionId, CardActivationRequest request);
```

### CardIssuerPort

[CardIssuerPort](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardIssuerPort.java)

```java
Mono<Observation> observeActivation(Binding binding, UUID actionId);
record Binding(UUID cardId, UUID tenantId, UUID partyId, UUID accountId, String adapterId, String instrumentReference, long version);
record Observation(UUID actionId, UUID tenantId, String adapterId, String instrumentReference, long bindingVersion, Outcome outcome, String receiptReference);
```

### CardConfigurationService

[CardConfigurationService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/configuration/CardConfigurationService.java)

```java
Mono<PaginationResponse<CardConfigurationDTO>> listConfigurations(UUID cardId, PaginationRequest paginationRequest);
Mono<CardConfigurationDTO> createConfiguration(UUID cardId, CardConfigurationDTO configDTO);
Mono<CardConfigurationDTO> getConfiguration(UUID cardId, UUID configId);
Mono<CardConfigurationDTO> updateConfiguration(UUID cardId, UUID configId, CardConfigurationDTO configDTO);
Mono<Void> deleteConfiguration(UUID cardId, UUID configId);
```

### CardDisputeService

[CardDisputeService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/dispute/CardDisputeService.java)

```java
Mono<PaginationResponse<CardDisputeDTO>> listDisputes( UUID cardId, PaginationRequest paginationRequest );
Mono<CardDisputeDTO> createDispute(UUID cardId, CardDisputeDTO disputeDTO);
Mono<CardDisputeDTO> getDispute(UUID cardId, UUID disputeId);
Mono<CardDisputeDTO> updateDispute(UUID cardId, UUID disputeId, CardDisputeDTO disputeDTO);
Mono<Void> deleteDispute(UUID cardId, UUID disputeId);
```

### CardEnrollmentService

[CardEnrollmentService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/enrollment/CardEnrollmentService.java)

```java
Mono<PaginationResponse<CardEnrollmentDTO>> listEnrollments( UUID cardId, PaginationRequest paginationRequest );
Mono<CardEnrollmentDTO> createEnrollment(UUID cardId, CardEnrollmentDTO enrollmentDTO);
Mono<CardEnrollmentDTO> getEnrollment(UUID cardId, UUID enrollmentId);
Mono<CardEnrollmentDTO> updateEnrollment(UUID cardId, UUID enrollmentId, CardEnrollmentDTO enrollmentDTO);
Mono<Void> deleteEnrollment(UUID cardId, UUID enrollmentId);
```

### CardGatewayService

[CardGatewayService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/gateway/CardGatewayService.java)

```java
Mono<PaginationResponse<CardGatewayDTO>> listGateways(PaginationRequest paginationRequest);
Mono<CardGatewayDTO> createGateway(CardGatewayDTO gatewayDTO);
Mono<CardGatewayDTO> getGateway(UUID gatewayId);
Mono<CardGatewayDTO> updateGateway(UUID gatewayId, CardGatewayDTO gatewayDTO);
Mono<Void> deleteGateway(UUID gatewayId);
```

### CardInterestService

[CardInterestService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/interest/CardInterestService.java)

```java
Mono<PaginationResponse<CardInterestDTO>> listInterests( UUID cardId, PaginationRequest paginationRequest );
Mono<CardInterestDTO> createInterest(UUID cardId, CardInterestDTO interestDTO);
Mono<CardInterestDTO> getInterest(UUID cardId, UUID interestId);
Mono<CardInterestDTO> updateInterest(UUID cardId, UUID interestId, CardInterestDTO interestDTO);
Mono<Void> deleteInterest(UUID cardId, UUID interestId);
```

### IssuerService

[IssuerService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/issuer/IssuerService.java)

```java
Mono<PaginationResponse<IssuerDTO>> listIssuers(PaginationRequest paginationRequest);
Mono<IssuerDTO> createIssuer(IssuerDTO issuerDTO);
Mono<IssuerDTO> getIssuer(UUID issuerId);
Mono<IssuerDTO> updateIssuer(UUID issuerId, IssuerDTO issuerDTO);
Mono<Void> deleteIssuer(UUID issuerId);
```

### CardLimitService

[CardLimitService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/limit/CardLimitService.java)

```java
Mono<PaginationResponse<CardLimitDTO>> listLimits(UUID cardId, PaginationRequest paginationRequest);
Mono<CardLimitDTO> createLimit(UUID cardId, CardLimitDTO limitDTO);
Mono<CardLimitDTO> getLimit(UUID cardId, UUID limitId);
Mono<CardLimitDTO> updateLimit(UUID cardId, UUID limitId, CardLimitDTO limitDTO);
Mono<Void> deleteLimit(UUID cardId, UUID limitId);
```

### CardMerchantService

[CardMerchantService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/merchant/CardMerchantService.java)

```java
Mono<PaginationResponse<CardMerchantDTO>> listMerchants(PaginationRequest paginationRequest);
Mono<CardMerchantDTO> createMerchant(CardMerchantDTO merchantDTO);
Mono<CardMerchantDTO> getMerchant(UUID merchantId);
Mono<CardMerchantDTO> updateMerchant(UUID merchantId, CardMerchantDTO merchantDTO);
Mono<Void> deleteMerchant(UUID merchantId);
```

### CardNetworkService

[CardNetworkService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/network/CardNetworkService.java)

```java
Mono<PaginationResponse<CardNetworkDTO>> listNetworks(PaginationRequest paginationRequest);
Mono<CardNetworkDTO> createNetwork(CardNetworkDTO networkDTO);
Mono<CardNetworkDTO> getNetwork(UUID networkId);
Mono<CardNetworkDTO> updateNetwork(UUID networkId, CardNetworkDTO networkDTO);
Mono<Void> deleteNetwork(UUID networkId);
```

### CardPaymentService

[CardPaymentService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/payment/CardPaymentService.java)

```java
Mono<PaginationResponse<CardPaymentDTO>> listPayments( UUID cardId, PaginationRequest paginationRequest );
Mono<CardPaymentDTO> createPayment(UUID cardId, CardPaymentDTO paymentDTO);
Mono<CardPaymentDTO> getPayment(UUID cardId, UUID paymentId);
Mono<CardPaymentDTO> updatePayment(UUID cardId, UUID paymentId, CardPaymentDTO paymentDTO);
Mono<Void> deletePayment(UUID cardId, UUID paymentId);
```

### PhysicalCardService

[PhysicalCardService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/physical/PhysicalCardService.java)

```java
Mono<PaginationResponse<PhysicalCardDTO>> listPhysicalCards( UUID cardId, PaginationRequest paginationRequest );
Mono<PhysicalCardDTO> createPhysicalCard(UUID cardId, PhysicalCardDTO physicalCardDTO);
Mono<PhysicalCardDTO> getPhysicalCard(UUID cardId, UUID physicalCardId);
Mono<PhysicalCardDTO> updatePhysicalCard(UUID cardId, UUID physicalCardId, PhysicalCardDTO physicalCardDTO);
Mono<Void> deletePhysicalCard(UUID cardId, UUID physicalCardId);
```

### CardProgramService

[CardProgramService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/program/CardProgramService.java)

```java
Mono<PaginationResponse<CardProgramDTO>> listPrograms(PaginationRequest paginationRequest);
Mono<CardProgramDTO> createProgram(CardProgramDTO programDTO);
Mono<CardProgramDTO> getProgram(UUID programId);
Mono<CardProgramDTO> updateProgram(UUID programId, CardProgramDTO programDTO);
Mono<Void> deleteProgram(UUID programId);
```

### CardPromotionService

[CardPromotionService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/promotion/CardPromotionService.java)

```java
Mono<PaginationResponse<CardPromotionDTO>> listPromotions( UUID cardId, PaginationRequest paginationRequest );
Mono<CardPromotionDTO> createPromotion(UUID cardId, CardPromotionDTO promotionDTO);
Mono<CardPromotionDTO> getPromotion(UUID cardId, UUID promotionId);
Mono<CardPromotionDTO> updatePromotion(UUID cardId, UUID promotionId, CardPromotionDTO promotionDTO);
Mono<Void> deletePromotion(UUID cardId, UUID promotionId);
```

### CardProviderService

[CardProviderService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/provider/CardProviderService.java)

```java
Mono<PaginationResponse<CardProviderDTO>> listProviders(UUID cardId, PaginationRequest paginationRequest);
Mono<CardProviderDTO> createProvider(UUID cardId, CardProviderDTO providerDTO);
Mono<CardProviderDTO> getProvider(UUID cardId, UUID providerId);
Mono<CardProviderDTO> updateProvider(UUID cardId, UUID providerId, CardProviderDTO providerDTO);
Mono<Void> deleteProvider(UUID cardId, UUID providerId);
```

### CardSecurityService

[CardSecurityService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/security/CardSecurityService.java)

```java
Mono<PaginationResponse<CardSecurityDTO>> listSecuritySettings( UUID cardId, PaginationRequest paginationRequest );
Mono<CardSecurityDTO> createSecuritySetting(UUID cardId, CardSecurityDTO securityDTO);
Mono<CardSecurityDTO> getSecuritySetting(UUID cardId, UUID securityId);
Mono<CardSecurityDTO> updateSecuritySetting(UUID cardId, UUID securityId, CardSecurityDTO securityDTO);
Mono<Void> deleteSecuritySetting(UUID cardId, UUID securityId);
```

### CardTerminalService

[CardTerminalService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/terminal/CardTerminalService.java)

```java
Mono<PaginationResponse<CardTerminalDTO>> listTerminals(PaginationRequest paginationRequest);
Mono<CardTerminalDTO> createTerminal(CardTerminalDTO terminalDTO);
Mono<CardTerminalDTO> getTerminal(UUID terminalId);
Mono<CardTerminalDTO> updateTerminal(UUID terminalId, CardTerminalDTO terminalDTO);
Mono<Void> deleteTerminal(UUID terminalId);
```

### CardTransactionService

[CardTransactionService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/transaction/CardTransactionService.java)

```java
Mono<PaginationResponse<CardTransactionDTO>> listTransactions(UUID cardId, PaginationRequest paginationRequest);
Mono<CardTransactionDTO> createTransaction(UUID cardId, CardTransactionDTO transactionDTO);
Mono<CardTransactionDTO> getTransaction(UUID cardId, UUID transactionId);
Mono<PaginationResponse<CardTransactionDTO>> findFiltered(FilterRequest<CardTransactionDTO> request);
Mono<CardTransactionDTO> updateTransaction(UUID cardId, UUID transactionId, CardTransactionDTO transactionDTO);
Mono<Void> deleteTransaction(UUID cardId, UUID transactionId);
```

### VirtualCardService

[VirtualCardService](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/virtual/VirtualCardService.java)

```java
Mono<PaginationResponse<VirtualCardDTO>> listVirtualCards(UUID cardId, PaginationRequest paginationRequest);
Mono<VirtualCardDTO> createVirtualCard(UUID cardId, VirtualCardDTO virtualCardDTO);
Mono<VirtualCardDTO> getVirtualCard(UUID cardId, UUID virtualCardId);
Mono<VirtualCardDTO> updateVirtualCard(UUID cardId, UUID virtualCardId, VirtualCardDTO virtualCardDTO);
Mono<Void> deleteVirtualCard(UUID cardId, UUID virtualCardId);
```

### CardGatewayRepository

[CardGatewayRepository](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/repositories/gateway/CardGatewayRepository.java)

```java
Mono<CardGateway> findByGatewayId(UUID gatewayId);
Mono<CardGateway> findByGatewayReference(String gatewayReference);
Flux<CardGateway> findByGatewayName(String gatewayName);
Flux<CardGateway> findByGatewayLegalName(String gatewayLegalName);
Flux<CardGateway> findByGatewayCode(String gatewayCode);
Flux<CardGateway> findByGatewayType(String gatewayType);
Flux<CardGateway> findByGatewayStatus(String gatewayStatus);
Flux<CardGateway> findByIsActiveTrue();
Flux<CardGateway> findByCountry(String country);
Flux<CardGateway> findBySupportedCardNetworksContaining(String cardNetwork);
Flux<CardGateway> findBySupportedPaymentMethodsContaining(String paymentMethod);
Flux<CardGateway> findBySupportedCurrenciesContaining(String currencyCode);
Flux<CardGateway> findBySupportedCountriesContaining(String countryCode);
Flux<CardGateway> findByIsInternationalTrue();
Flux<CardGateway> findByIsDomesticTrue();
Flux<CardGateway> findByProcessorId(UUID processorId);
Flux<CardGateway> findByAcquirerId(UUID acquirerId);
Flux<CardGateway> findByIsPciCompliantTrue();
Flux<CardGateway> findBySupportsTokenizationTrue();
Flux<CardGateway> findBySupports3dSecureTrue();
Flux<CardGateway> findBySupportsRecurringPaymentsTrue();
Flux<CardGateway> findByIntegrationType(String integrationType);
Flux<CardGateway> findBySdkAvailableTrue();
```

### CardProviderRepository

[CardProviderRepository](../../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/repositories/provider/CardProviderRepository.java)

```java
Flux<CardProvider> findByCardId(UUID cardId, Pageable pageable);
Mono<Long> countByCardId(UUID cardId);
```

## Configuration

Values below come from source placeholders. Plain YAML values and configuration-property classes must also be reviewed at the linked configuration sources. Secret fallback values are deliberately redacted.

| Key | Fallback | Source |
| --- | --- | --- |
| `spring.application.name` | No fallback | [CardsApplication](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/CardsApplication.java) |
| `spring.application.version` | No fallback | [CardsApplication](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/CardsApplication.java) |
| `spring.application.description` | No fallback | [CardsApplication](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/CardsApplication.java) |
| `spring.application.team.name` | No fallback | [CardsApplication](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/CardsApplication.java) |
| `spring.application.team.email` | No fallback | [CardsApplication](../../core-banking-cards-web/src/main/java/io/fireflysoftware/core/banking/cards/web/CardsApplication.java) |
| `DB_HOST` | No fallback | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `DB_PORT` | No fallback | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `DB_NAME` | No fallback | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `DB_SSL_MODE` | No fallback | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `DB_USERNAME` | No fallback | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `DB_PASSWORD` | No fallback | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `SERVER_ADDRESS` | `localhost` | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |
| `SERVER_PORT` | `8080` | [application](../../core-banking-cards-web/src/main/resources/application.yaml) |

## Security and failure evidence

| Declaration or failure site | Source |
| --- | --- |
| `new RuntimeException("CardActivity not found for the specified card."));` | [CardActivityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/activity/impl/CardActivityServiceImpl.java) |
| `new RuntimeException("CardActivity not found for the specified card."));` | [CardActivityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/activity/impl/CardActivityServiceImpl.java) |
| `new RuntimeException("CardActivity not found for the specified card."));` | [CardActivityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/activity/impl/CardActivityServiceImpl.java) |
| `new RuntimeException("CardBalance not found for the specified card."));` | [CardBalanceServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/balance/impl/CardBalanceServiceImpl.java) |
| `new RuntimeException("CardBalance not found for the specified card."));` | [CardBalanceServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/balance/impl/CardBalanceServiceImpl.java) |
| `new RuntimeException("CardBalance not found for the specified card."));` | [CardBalanceServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/balance/impl/CardBalanceServiceImpl.java) |
| `new ResponseStatusException(HttpStatus.CONFLICT, "Activation is not permitted")));` | [CardActivationStore](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardActivationStore.java) |
| `new ResponseStatusException(HttpStatus.CONFLICT, "Activation is not permitted");` | [CardActivationStore](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardActivationStore.java) |
| `new ResponseStatusException(HttpStatus.CONFLICT, "Activation is not permitted");` | [CardActivationStore](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardActivationStore.java) |
| `new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Activation persistence is unavailable");` | [CardActivationStore](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/CardActivationStore.java) |
| `new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,` | [CardActivationServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/impl/CardActivationServiceImpl.java) |
| `new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid activation request"));` | [CardActivationServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/impl/CardActivationServiceImpl.java) |
| `new ResponseStatusException(` | [CardActivationServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/impl/CardActivationServiceImpl.java) |
| `HttpStatus.FORBIDDEN` | [CardActivationServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/impl/CardActivationServiceImpl.java) |
| `new IllegalStateException("SHA-256 unavailable");` | [CardActivationServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/activation/impl/CardActivationServiceImpl.java) |
| `new ResponseStatusException(HttpStatus.CONFLICT,` | [CardServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/impl/CardServiceImpl.java) |
| `new IllegalStateException(` | [CardServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/impl/CardServiceImpl.java) |
| `new ResponseStatusException(HttpStatus.CONFLICT,` | [CardServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/impl/CardServiceImpl.java) |
| `new ResponseStatusException(HttpStatus.BAD_REQUEST,` | [CardServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/impl/CardServiceImpl.java) |
| `new IllegalStateException(` | [CardServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/impl/CardServiceImpl.java) |
| `new RuntimeException("CardDispute not found for the specified card."));` | [CardDisputeServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/dispute/impl/CardDisputeServiceImpl.java) |
| `new RuntimeException("CardDispute not found for the specified card."));` | [CardDisputeServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/dispute/impl/CardDisputeServiceImpl.java) |
| `new RuntimeException("CardDispute not found for the specified card."));` | [CardDisputeServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/dispute/impl/CardDisputeServiceImpl.java) |
| `new RuntimeException("CardEnrollment not found for the specified card."));` | [CardEnrollmentServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/enrollment/impl/CardEnrollmentServiceImpl.java) |
| `new RuntimeException("CardEnrollment not found for the specified card."));` | [CardEnrollmentServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/enrollment/impl/CardEnrollmentServiceImpl.java) |
| `new RuntimeException("CardEnrollment not found for the specified card."));` | [CardEnrollmentServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/enrollment/impl/CardEnrollmentServiceImpl.java) |
| `new RuntimeException("CardInterest not found for the specified card."));` | [CardInterestServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/interest/impl/CardInterestServiceImpl.java) |
| `new RuntimeException("CardInterest not found for the specified card."));` | [CardInterestServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/interest/impl/CardInterestServiceImpl.java) |
| `new RuntimeException("CardInterest not found for the specified card."));` | [CardInterestServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/interest/impl/CardInterestServiceImpl.java) |
| `new RuntimeException("CardPayment not found for the specified card."));` | [CardPaymentServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/payment/impl/CardPaymentServiceImpl.java) |
| `new RuntimeException("CardPayment not found for the specified card."));` | [CardPaymentServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/payment/impl/CardPaymentServiceImpl.java) |
| `new RuntimeException("CardPayment not found for the specified card."));` | [CardPaymentServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/payment/impl/CardPaymentServiceImpl.java) |
| `new RuntimeException("CardPromotion not found for the specified card."));` | [CardPromotionServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/promotion/impl/CardPromotionServiceImpl.java) |
| `new RuntimeException("CardPromotion not found for the specified card."));` | [CardPromotionServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/promotion/impl/CardPromotionServiceImpl.java) |
| `new RuntimeException("CardPromotion not found for the specified card."));` | [CardPromotionServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/promotion/impl/CardPromotionServiceImpl.java) |
| `new RuntimeException("Error saving CardSecurity: " + throwable.getMessage())));` | [CardSecurityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/security/impl/CardSecurityServiceImpl.java) |
| `new RuntimeException("CardSecurity not found for the specified card."));` | [CardSecurityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/security/impl/CardSecurityServiceImpl.java) |
| `new RuntimeException("CardSecurity not found for the specified card."));` | [CardSecurityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/security/impl/CardSecurityServiceImpl.java) |
| `new RuntimeException("CardSecurity not found for the specified card."));` | [CardSecurityServiceImpl](../../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/security/impl/CardSecurityServiceImpl.java) |

## Build and dependencies

### core-banking-cards-core

[core-banking-cards-core/pom.xml](../../core-banking-cards-core/pom.xml)

Coordinates: `io.fireflysoftware:core-banking-cards-core:26.10.6`. Parent: `io.fireflysoftware:core-banking-cards:26.10.6`.

- `io.fireflysoftware:core-banking-cards-interfaces:`
- `io.fireflysoftware:core-banking-cards-models:`
- `io.projectreactor:reactor-test:`
- `org.fireflyframework:fireflyframework-starter-core:`
- `org.flywaydb:flyway-database-postgresql:`
- `org.mapstruct:mapstruct:`
- `org.projectlombok:lombok:`
- `org.springframework.boot:spring-boot-starter-test:`
- `org.springframework.boot:spring-boot-starter-webflux:`
- `org.testcontainers:testcontainers-junit-jupiter:`
- `org.testcontainers:testcontainers-postgresql:`

### core-banking-cards-interfaces

[core-banking-cards-interfaces/pom.xml](../../core-banking-cards-interfaces/pom.xml)

Coordinates: `io.fireflysoftware:core-banking-cards-interfaces:26.10.6`. Parent: `io.fireflysoftware:core-banking-cards:26.10.6`.

- `com.fasterxml.jackson.core:jackson-databind:`
- `com.fasterxml.jackson.datatype:jackson-datatype-jsr310:`
- `io.swagger.core.v3:swagger-annotations:`
- `org.fireflyframework:fireflyframework-utils:`
- `org.projectlombok:lombok:`
- `org.springframework.boot:spring-boot-starter-validation:`

### core-banking-cards-models

[core-banking-cards-models/pom.xml](../../core-banking-cards-models/pom.xml)

Coordinates: `io.fireflysoftware:core-banking-cards-models:26.10.6`. Parent: `io.fireflysoftware:core-banking-cards:26.10.6`.

- `io.fireflysoftware:core-banking-cards-interfaces:`
- `org.fireflyframework:fireflyframework-r2dbc:`
- `org.flywaydb:flyway-core:`
- `org.postgresql:postgresql:`
- `org.postgresql:r2dbc-postgresql:`
- `org.projectlombok:lombok:`
- `org.springframework.boot:spring-boot-starter-data-r2dbc:`

### core-banking-cards-sdk

[core-banking-cards-sdk/pom.xml](../../core-banking-cards-sdk/pom.xml)

Coordinates: `io.fireflysoftware:core-banking-cards-sdk:26.10.6`. Parent: `io.fireflysoftware:core-banking-cards:26.10.6`.

- `com.fasterxml.jackson.core:jackson-databind:`
- `com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:`
- `com.fasterxml.jackson.datatype:jackson-datatype-jsr310:`
- `com.squareup.okhttp3:logging-interceptor:`
- `com.squareup.okhttp3:okhttp:`
- `io.fireflysoftware:core-banking-cards-web:`
- `io.swagger.core.v3:swagger-annotations:`
- `jakarta.annotation:jakarta.annotation-api:`
- `javax.annotation:javax.annotation-api:1.3.2`
- `org.flywaydb:flyway-database-postgresql:`
- `org.openapitools:jackson-databind-nullable:0.2.8`
- `org.projectlombok:lombok:`
- `org.springframework.boot:spring-boot-starter-test:`
- `org.springframework:spring-webflux:`
- `org.testcontainers:testcontainers-junit-jupiter:`
- `org.testcontainers:testcontainers-postgresql:`

### core-banking-cards-web

[core-banking-cards-web/pom.xml](../../core-banking-cards-web/pom.xml)

Coordinates: `io.fireflysoftware:core-banking-cards-web:26.10.6`. Parent: `io.fireflysoftware:core-banking-cards:26.10.6`.

- `io.fireflysoftware:core-banking-cards-core:`
- `io.fireflysoftware:core-banking-cards-interfaces:`
- `io.micrometer:micrometer-registry-prometheus:`
- `io.projectreactor:reactor-test:`
- `org.fireflyframework:fireflyframework-web:`
- `org.projectlombok:lombok:`
- `org.springdoc:springdoc-openapi-starter-webflux-ui:`
- `org.springframework.boot:spring-boot-starter-actuator:`
- `org.springframework.boot:spring-boot-starter-test:`
- `org.springframework.boot:spring-boot-starter-validation:`
- `org.springframework.boot:spring-boot-starter-webflux:`

### core-banking-cards

[pom.xml](../../pom.xml)

Coordinates: `io.fireflysoftware:core-banking-cards:26.10.6`. Parent: `io.fireflysoftware:firefly-parent:26.10.6`.

- `io.fireflysoftware:core-banking-cards-core:${project.version}`
- `io.fireflysoftware:core-banking-cards-interfaces:${project.version}`
- `io.fireflysoftware:core-banking-cards-models:${project.version}`
- `io.fireflysoftware:core-banking-cards-sdk:${project.version}`
- `io.fireflysoftware:core-banking-cards-web:${project.version}`

## Test sources

- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/architecture/CardRepositoryArchitectureTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/architecture/CardRepositoryArchitectureTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/README.md](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/README.md)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/acquirer/CardAcquirerServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/acquirer/CardAcquirerServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/balance/CardBalanceServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/balance/CardBalanceServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/bin/BINServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/bin/BINServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardActivationCrudTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardActivationCrudTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardActivationDatabaseTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardActivationDatabaseTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardActivationServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardActivationServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardSensitiveDataBoundaryTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardSensitiveDataBoundaryTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardSensitiveDataDatabaseTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardSensitiveDataDatabaseTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/CardServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/configuration/CardConfigurationServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/configuration/CardConfigurationServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/dispute/CardDisputeServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/dispute/CardDisputeServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/enrollment/CardEnrollmentServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/enrollment/CardEnrollmentServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/gateway/CardGatewayServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/gateway/CardGatewayServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/interest/CardInterestServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/interest/CardInterestServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/issuer/IssuerServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/issuer/IssuerServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/limit/CardLimitServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/limit/CardLimitServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/merchant/CardMerchantServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/merchant/CardMerchantServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/network/CardNetworkServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/network/CardNetworkServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/payment/CardPaymentServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/payment/CardPaymentServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/physical/PhysicalCardServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/physical/PhysicalCardServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/program/CardProgramServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/program/CardProgramServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/promotion/CardPromotionServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/promotion/CardPromotionServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/provider/CardProviderServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/provider/CardProviderServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/security/CardSecurityServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/security/CardSecurityServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/terminal/CardTerminalServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/terminal/CardTerminalServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/transaction/CardTransactionServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/transaction/CardTransactionServiceTest.java)
- [core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/virtual/VirtualCardServiceTest.java](../../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/virtual/VirtualCardServiceTest.java)
- [core-banking-cards-sdk/src/test/java/io/fireflysoftware/qualification/cards/CardActivationRuntimeIT.java](../../core-banking-cards-sdk/src/test/java/io/fireflysoftware/qualification/cards/CardActivationRuntimeIT.java)
- [core-banking-cards-sdk/src/test/java/io/fireflysoftware/qualification/cards/CardAuthenticationDataRuntimeIT.java](../../core-banking-cards-sdk/src/test/java/io/fireflysoftware/qualification/cards/CardAuthenticationDataRuntimeIT.java)
- [core-banking-cards-web/src/test/java/io/fireflysoftware/core/banking/cards/web/CardServiceWiringTest.java](../../core-banking-cards-web/src/test/java/io/fireflysoftware/core/banking/cards/web/CardServiceWiringTest.java)
- [core-banking-cards-web/src/test/java/io/fireflysoftware/core/banking/cards/web/openapi/OpenApiGenApplication.java](../../core-banking-cards-web/src/test/java/io/fireflysoftware/core/banking/cards/web/openapi/OpenApiGenApplication.java)

## CI definitions

- [.github/workflows/maven-central.yml](../../.github/workflows/maven-central.yml)
- [.github/workflows/release.yml](../../.github/workflows/release.yml)
