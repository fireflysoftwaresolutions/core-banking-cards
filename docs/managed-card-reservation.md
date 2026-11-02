# Managed card reservation service

Managed reservation records an opaque, inactive enrollment intent before any provider effect. Its service source implements reservation, exact replay and scoped recovery using Spring Data. It does not issue or activate a card, reuse a legacy PAN-bearing record, release an account restriction or prove financial admission.

The feature is disabled by default. The source includes conditional service construction, but supplies no production authority adapter, account-fence adapter, native context, database roles or configured sessions. There is no new HTTP route or generated SDK operation. Enabling the property alone cannot make this journey available.

## Reservation, replay and recovery

`ManagedCardEnrollmentService.reserve(accountId, request)` resolves the current principal through the shared `SecurityContextPort` and requires a genuine `ManagedCardEnrollmentAuthority` implementation. The caller cannot supply an actor, eligibility result or provider outcome. Current owner checks must cover the selected tenant, actor, party, account, contract, product, source revisions, configured issuer/processor and the complete account-owned pending-enrollment fence.

Before reading or writing, the native context must install the bounded scope and lock the account/action consistently. A new reservation stores one linked graph in the command transaction:

| Mapped relation | Retained fact |
| --- | --- |
| `managed_card` | Opaque managed identity with the retained restriction |
| `managed_card_enrollment_action` | Original selection, request, intent, receipt and account-fence binding |
| `managed_card_provider_operation` | Stable provider-operation identity and original deadline |
| `managed_card_enrollment_event` | Reservation event linked to the same action |
| `managed_card_enrollment_outbox` | Pending coordination record; this service does not dispatch it |

An exact `RESERVE` replay returns the original receipt and operation without another graph. Both the original intent and the retained `accountFenceReceiptId` must match current reservation authorization. A different fence receipt conflicts and rolls back; it is not silently substituted. An expired original mutation deadline is never extended.

`recover(accountId, recoveryRequest)` uses a separate reader session and fresh current read authorization. It reads the original action/progress version and returns the retained graph without requesting replacement reservation-fence authority or permitting another provider effect. Recovery may observe an earlier reservation after its mutation deadline when the new read authorization remains valid.

## Persistence and transaction boundaries

The service interface has an implementation under `services/card/enrollment/impl`. `ManagedCardReservationRepositories` creates a genuine Spring Data repository from the supplied `R2dbcEntityTemplate`; its narrow fragment uses ordinary mapped inserts, scoped counts and complete graph readback. It does not execute handwritten business SQL or expose generic reservation CRUD.

Five successful entity emissions alone do not establish a reservation. The source requires five scoped stored-row counts equal to one and an exact same-transaction graph readback. These are stored-row witnesses, not proof of native INSERT affected-row counts. The future native gate must separately qualify first-reservation uniqueness, existing/partial graph handling, locking, constraints and deferred completeness.

A duplicate/constraint failure propagates toward rollback. The service never catches that error and then attempts replay inside the aborted PostgreSQL transaction. No recovery path automatically reposts an uncertain action.

The template, client, repositories, transaction manager, native context and owned-lease factory share the same qualified connection graph. Caller-provided converters, callbacks and client customization are retained. Callback changes to the expected immutable inserted graph cause refusal. Command and reader sessions must be distinct. Both use physical read-write, repeatable-read transactions for future private technical-context installation; actual reader business privileges must remain SELECT-only and require separate native role qualification.

The original authority horizon and pre-observation monotonic anchor bound acquisition, begin, work, commit and delivery. A native stamp must carry the actual installed database scope/time and may only narrow that bound; the window reserves one millisecond for database-floor uncertainty. The service rechecks current policy before commit and after transaction/cleanup before returning the protected receipt. Cleanup has its own finite technical budget and creates no new business authority.

Known commit and uncertain outcome remain distinct. A late policy withdrawal or expiry withholds the protected receipt while retaining safe transaction facts and the action reference. It does not claim rollback, no effect or permission to retry a provider operation.

## Required integration before activation

| Dependency | Required implementation |
| --- | --- |
| Current principal and policy | Trusted shared principal plus current purpose-specific authorization; no actor-header or machine fallback |
| Owner authority and fence | Genuine account/contract/party/product/bank checks and the complete pending-enrollment fence with exact retained receipt continuity |
| Native context and schema | A separately reviewed migration after V1–V7, real database clock, scoped context/locks, roles, ACL/RLS and full graph integrity |
| Qualified sessions | Explicit `managedCardCommandSession` and `managedCardReaderSession`, each preserving the same connection/template/manager identity |
| Shared transaction capability | Coordinated adoption of the genuine Jafly bounded transaction API; direct source aggregation is not released-dependency or original Maven qualification |
| Provider execution | Durable attempts, dispatch, observation, reconciliation and issuer/processor adapters; no implementation is supplied by this reservation block |
| API and SDK | A real owner route and original producer/OpenAPI generation workflow; generated code and specifications must not be hand-edited |

Do not enable `firefly.cards.managed-enrollment.enabled` until these dependencies and their native/security gates are qualified. Missing required beans fail closed; no synthetic success adapter is installed.

## Verification scope

The earlier 57-case cancellation/enrollment contract scope is historical pure-rule evidence. The extended source suite contains 104 cases, including those original cases, and exercises actual Spring Data mapping, transaction/lease behavior, replay, recovery, current-policy checks and the fence-continuity correction with in-process driver and authority observations. Independent source review and production-only compilation cover this source checkpoint.

These observations establish neither installed schema/roles nor real owner authority, current signed HTTP/PDP enforcement, provider persistence, original Maven/released-coordinate adoption, genuine SDK transport, issued/active cards or a complete financial journey. V1–V7 and existing SDK/specification files remain unchanged.

Source: [service implementation](../core-banking-cards-core/src/main/java/io/fireflysoftware/core/banking/cards/core/services/card/enrollment/impl/ManagedCardEnrollmentServiceImpl.java), [mapped repository fragment](../core-banking-cards-models/src/main/java/io/fireflysoftware/core/banking/cards/models/repositories/card/enrollment/impl/ManagedCardReservationOperationsImpl.java), [fence/recovery controls](../core-banking-cards-core/src/test/java/io/fireflysoftware/core/banking/cards/core/services/card/enrollment/store/ManagedCardFenceReplayReviewTest.java). The [earlier pure contracts](managed-card-enrollment.md) remain the historical foundation; this guide describes their later mapped reservation extension.
