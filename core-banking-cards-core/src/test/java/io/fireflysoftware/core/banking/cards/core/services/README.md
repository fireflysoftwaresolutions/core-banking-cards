# Card record services: test guide

[Repository guide](../../../../../../../../../../../README.md) · [Module build](../../../../../../../../../../pom.xml)

## Purpose and boundary

This guide maps the authored tests beneath this directory to their source files and gives reproducible commands for reviewing their actual assertions. The test inventory below is a source inventory, not a measured coverage percentage or a record of a successful run.

CardProviderServiceTest exercises provider-record CRUD. It does not contact an issuer, processor, network, or card sandbox. CardTransaction records and mocked card-security tests do not establish authorization, clearing, settlement, PIN handling, or PCI acceptance.

## Prerequisites and fixture ownership

Run commands from the repository root with JDK 25 selected in both `java --version` and `mvn --version`. Resolve the matching parent/framework and reactor dependencies through your authorized Maven configuration. The listed fixture source controls the synthetic IDs, DTOs, mocked dependencies, and expected outcomes; it is not a seed script for a running bank. Read setup and teardown before extending a fixture or executing another module's integration tests.

## Run and inspect

Run one existing class together with its reactor dependencies:

```sh
mvn -B -pl core-banking-cards-core -am test -DskipOpenApiGen \
  -Dtest=CardAcquirerServiceTest -Dsurefire.failIfNoSpecifiedTests=false
```

To run that module's configured test lifecycle after inspecting all of its fixtures:

```sh
mvn -B -pl core-banking-cards-core -am test -DskipOpenApiGen
```

`skipOpenApiGen` prevents the parent's fixed-port API-generation startup; it does not skip tests or isolate external resources. `failIfNoSpecifiedTests=false` permits dependency modules without the selected class. Confirm that the selected class appears in `core-banking-cards-core/target/surefire-reports/` and inspect tests, failures, errors, and skips in the XML. A missing report or skipped case is not a passing qualification. These commands were source-checked for this guide; no new product-test pass is claimed here.

## Source inventory

Each entry lists the existing test method names. Names identify intent; read the linked body to see whether a collaborator, static utility, or the method itself is stubbed.

### [CardAcquirerServiceTest](acquirer/v1/CardAcquirerServiceTest.java)

- `listAcquirers_Success`
- `createAcquirer_Success`
- `getAcquirer_Success`
- `getAcquirer_NotFound`
- `updateAcquirer_Success`
- `updateAcquirer_NotFound`
- `deleteAcquirer_Success`
- `deleteAcquirer_NotFound`

### [CardBalanceServiceTest](balance/v1/CardBalanceServiceTest.java)

- `listBalances_Success`
- `createBalance_Success`
- `getBalance_Success`
- `getBalance_WrongCardId`
- `updateBalance_Success`
- `updateBalance_WrongCardId`
- `deleteBalance_Success`
- `deleteBalance_WrongCardId`

### [BINServiceTest](bin/v1/BINServiceTest.java)

- `listBINs_Success`
- `createBIN_Success`
- `getBIN_Success`
- `getBIN_NotFound`
- `getBINByNumber_Success`
- `getBINByNumber_NotFound`
- `updateBIN_Success`
- `updateBIN_NotFound`
- `deleteBIN_Success`
- `deleteBIN_NotFound`

### [CardServiceTest](card/v1/CardServiceTest.java)

- `createCard_Success`
- `getCard_Success`
- `getCard_NotFound`
- `updateCard_Success`
- `updateCard_NotFound`
- `deleteCard_Success`
- `deleteCard_NotFound`

### [CardConfigurationServiceTest](configuration/v1/CardConfigurationServiceTest.java)

- `listConfigurations_Success`
- `createConfiguration_Success`
- `getConfiguration_Success`
- `getConfiguration_NotFound`
- `getConfiguration_WrongCardId`
- `updateConfiguration_Success`
- `updateConfiguration_NotFound`
- `updateConfiguration_WrongCardId`
- `deleteConfiguration_Success`
- `deleteConfiguration_NotFound`
- `deleteConfiguration_WrongCardId`

### [CardDisputeServiceTest](dispute/v1/CardDisputeServiceTest.java)

- `listDisputes_Success`
- `createDispute_Success`
- `getDispute_Success`
- `getDispute_WrongCardId`
- `updateDispute_Success`
- `updateDispute_WrongCardId`
- `deleteDispute_Success`
- `deleteDispute_WrongCardId`

### [CardEnrollmentServiceTest](enrollment/v1/CardEnrollmentServiceTest.java)

- `listEnrollments_Success`
- `createEnrollment_Success`
- `getEnrollment_Success`
- `getEnrollment_WrongCardId`
- `updateEnrollment_Success`
- `updateEnrollment_WrongCardId`
- `deleteEnrollment_Success`
- `deleteEnrollment_WrongCardId`

### [CardGatewayServiceTest](gateway/v1/CardGatewayServiceTest.java)

- `listGateways_Success`
- `createGateway_Success`
- `getGateway_Success`
- `getGateway_NotFound`
- `updateGateway_Success`
- `updateGateway_NotFound`
- `deleteGateway_Success`
- `deleteGateway_NotFound`

### [CardInterestServiceTest](interest/v1/CardInterestServiceTest.java)

- `listInterests_Success`
- `createInterest_Success`
- `getInterest_Success`
- `getInterest_WrongCardId`
- `updateInterest_Success`
- `updateInterest_WrongCardId`
- `deleteInterest_Success`
- `deleteInterest_WrongCardId`

### [IssuerServiceTest](issuer/v1/IssuerServiceTest.java)

- `listIssuers_Success`
- `createIssuer_Success`
- `getIssuer_Success`
- `getIssuer_NotFound`
- `updateIssuer_Success`
- `updateIssuer_NotFound`
- `deleteIssuer_Success`
- `deleteIssuer_NotFound`

### [CardLimitServiceTest](limit/v1/CardLimitServiceTest.java)

- `listLimits_Success`
- `createLimit_Success`
- `getLimit_Success`
- `getLimit_NotFound`
- `getLimit_WrongCard`
- `updateLimit_Success`
- `updateLimit_NotFound`
- `updateLimit_WrongCard`
- `deleteLimit_Success`
- `deleteLimit_NotFound`
- `deleteLimit_WrongCard`

### [CardMerchantServiceTest](merchant/v1/CardMerchantServiceTest.java)

- `listMerchants_Success`
- `createMerchant_Success`
- `getMerchant_Success`
- `getMerchant_NotFound`
- `updateMerchant_Success`
- `updateMerchant_NotFound`
- `deleteMerchant_Success`
- `deleteMerchant_NotFound`

### [CardNetworkServiceTest](network/v1/CardNetworkServiceTest.java)

- `listNetworks_Success`
- `createNetwork_Success`
- `getNetwork_Success`
- `getNetwork_NotFound`
- `updateNetwork_Success`
- `updateNetwork_NotFound`
- `deleteNetwork_Success`
- `deleteNetwork_NotFound`

### [CardPaymentServiceTest](payment/v1/CardPaymentServiceTest.java)

- `listPayments_Success`
- `createPayment_Success`
- `getPayment_Success`
- `getPayment_WrongCardId`
- `updatePayment_Success`
- `updatePayment_WrongCardId`
- `deletePayment_Success`
- `deletePayment_WrongCardId`

### [PhysicalCardServiceTest](physical/v1/PhysicalCardServiceTest.java)

- `listPhysicalCards_Success`
- `createPhysicalCard_Success`
- `getPhysicalCard_Success`
- `getPhysicalCard_NotFound`
- `getPhysicalCard_WrongCard`
- `updatePhysicalCard_Success`
- `updatePhysicalCard_NotFound`
- `updatePhysicalCard_WrongCard`
- `deletePhysicalCard_Success`
- `deletePhysicalCard_NotFound`
- `deletePhysicalCard_WrongCard`

### [CardProgramServiceTest](program/v1/CardProgramServiceTest.java)

- `listPrograms_Success`
- `createProgram_Success`
- `getProgram_Success`
- `getProgram_NotFound`
- `updateProgram_Success`
- `updateProgram_NotFound`
- `deleteProgram_Success`
- `deleteProgram_NotFound`

### [CardPromotionServiceTest](promotion/v1/CardPromotionServiceTest.java)

- `listPromotions_Success`
- `createPromotion_Success`
- `getPromotion_Success`
- `getPromotion_WrongCardId`
- `updatePromotion_Success`
- `updatePromotion_WrongCardId`
- `deletePromotion_Success`
- `deletePromotion_WrongCardId`

### [CardProviderServiceTest](provider/v1/CardProviderServiceTest.java)

- `listProviders_Success`
- `createProvider_Success`
- `getProvider_Success`
- `getProvider_NotFound`
- `getProvider_WrongCard`
- `updateProvider_Success`
- `updateProvider_NotFound`
- `updateProvider_WrongCard`
- `deleteProvider_Success`
- `deleteProvider_NotFound`
- `deleteProvider_WrongCard`

### [CardSecurityServiceTest](security/v1/CardSecurityServiceTest.java)

- `listSecuritySettings_Success`
- `createSecuritySetting_Success`
- `createSecuritySetting_Error`
- `getSecuritySetting_Success`
- `getSecuritySetting_NotFound`
- `getSecuritySetting_WrongCard`
- `updateSecuritySetting_Success`
- `updateSecuritySetting_NotFound`
- `updateSecuritySetting_WrongCard`
- `deleteSecuritySetting_Success`
- `deleteSecuritySetting_NotFound`
- `deleteSecuritySetting_WrongCard`

### [CardTerminalServiceTest](terminal/v1/CardTerminalServiceTest.java)

- `listTerminals_Success`
- `createTerminal_Success`
- `getTerminal_Success`
- `getTerminal_NotFound`
- `updateTerminal_Success`
- `updateTerminal_NotFound`
- `deleteTerminal_Success`
- `deleteTerminal_NotFound`

### [CardTransactionServiceTest](transaction/v1/CardTransactionServiceTest.java)

- `listTransactions_Success`
- `createTransaction_Success`
- `getTransaction_Success`
- `getTransaction_NotFound`
- `getTransaction_WrongCard`
- `findFiltered_Success`
- `updateTransaction_Success`
- `updateTransaction_NotFound`
- `updateTransaction_WrongCard`
- `deleteTransaction_Success`
- `deleteTransaction_NotFound`
- `deleteTransaction_WrongCard`

### [VirtualCardServiceTest](virtual/v1/VirtualCardServiceTest.java)

- `listVirtualCards_Success`
- `createVirtualCard_Success`
- `getVirtualCard_Success`
- `getVirtualCard_NotFound`
- `getVirtualCard_WrongCard`
- `updateVirtualCard_Success`
- `updateVirtualCard_NotFound`
- `updateVirtualCard_WrongCard`
- `deleteVirtualCard_Success`
- `deleteVirtualCard_NotFound`
- `deleteVirtualCard_WrongCard`

## Actual source example

This excerpt is `listAcquirers_Success` from [CardAcquirerServiceTest](acquirer/v1/CardAcquirerServiceTest.java). Use the surrounding class's imports and fixture setup; this excerpt is not a standalone Java program.

```java
@Test
void listAcquirers_Success() {
    // Arrange
    PaginationRequest paginationRequest = new PaginationRequest();

    @SuppressWarnings("unchecked")
    PaginationResponse<CardAcquirerDTO> expectedResponse = mock(PaginationResponse.class);

    try (MockedStatic<PaginationUtils> paginationUtilsMocked = mockStatic(PaginationUtils.class)) {
        paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                eq(paginationRequest),
                any(Function.class),
                any(Function.class),
                any(Supplier.class)
        )).thenReturn(Mono.just(expectedResponse));

        // Act & Assert
        StepVerifier.create(service.listAcquirers(paginationRequest))
                .expectNext(expectedResponse)
                .verifyComplete();

        // Verify that PaginationUtils.paginateQuery was called with the correct parameters
        paginationUtilsMocked.verify(() -> PaginationUtils.paginateQuery(
                eq(paginationRequest),
                any(Function.class),
                any(Function.class),
                any(Supplier.class)
        ));
    }
}
```

## Interpret and recover

- A Reactor assertion must subscribe and verify termination; constructing a `Mono` alone does not execute it. Match emitted values, empty completion, and errors to the production contract rather than converting every absence to success.
- A repository or mapper mock verifies a local call/result, not committed database state. A stubbed service method or static filter utility does not exercise its implementation. Use real HTTP/SDK/database cases for those boundaries.
- For a failing mock assertion, reread the production branch and fixture data before changing expected values. Do not make a test pass by manufacturing the behavior inside a save callback.
- For dependency resolution or Java errors, verify the actual Maven runtime and matching reactor artifacts. For no matching tests, check the class name and module rather than disabling all tests.
- Coverage requires a configured measurement run and inspected report. Neither a class list nor a successful Maven exit proves every operation, error branch, authorization rule, provider interaction, or customer journey is covered.

## Maintain this guide

Keep the inventory and runnable class selector aligned with the linked sources. Add regression cases for changed behavior, then record the command, fixture boundary, source revision, and reports in the relevant implementation review. Preserve separate evidence for unit behavior, transport contracts, database recovery, provider sandbox behavior, and live-provider acceptance.
