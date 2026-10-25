# Card cancellation and closure contracts

The cancellation contracts define local intent, immutable acceptance, restricted progress and card-owner obligation coverage. They are pure Java records, strict decoders and rules. A cancellation persistence service, authenticated command boundary, issuer/processor adapter, HTTP operation and generated SDK operation remain pending. There is no new cancellation endpoint to call at this stage.

A card record marked `CANCELLED` does not prove that an issuer stopped the instrument, that a processor has no pending work, or that an account can close. Instrument termination and financial clearance have separate evidence requirements.

## Request and server scope

The cancellation request contains `actionId`, `expectedBindingVersion`, `reasonCode` and `acceptNotAfterEpochMillis`. The action is a stable UUID; the binding version must match the retained instrument. The reason is a bounded uppercase code. The fixed deadline governs first admission and is not extended by an exact retry.

The strict decoder accepts one bounded UTF-8 JSON object with exactly those fields. Missing, duplicate, unknown, null or coerced values are rejected. Versions and deadlines require exact integers; identifiers require canonical UUIDs. The request cannot declare an actor, tenant, provider decision, card status, force flag or clearance.

The server resolves tenant, actor, party, account, card and binding version. The future command boundary must obtain that selection from current authenticated ownership and policy, then check it at the actual admission/finalization boundary. The pure selection record and comparison rules do not authenticate a caller or establish ownership.

A separate fence request contains `actionId`, `closureId`, `enrollmentId`, `expectedFenceVersion` and `acceptNotAfterEpochMillis`. Its server selection binds tenant, actor, account and enrollment. Callers cannot supply the required capability set, provider receipts or a complete/clear flag.

## Immutable acceptance and replay

Acceptance links the action, full retained server selection, canonical intent digest, receipt ID and database-recorded admission time. The pure acceptance validator compares every link against supplied retained facts and checks that the original admission met its fixed deadline. It does not query a database, authenticate those facts or commit a transaction.

Exact replay compares the original request and retained server selection. A changed action, actor, account, instrument binding, reason or deadline conflicts. Historical replay preserves the original acceptance; it does not re-admit an expired request or create fresh authority. A future command service must reauthorize the retained scope before returning a receipt.

Acceptance is separate from the versioned progress view. Provider progress cannot rewrite the original receipt or turn it into a statement about current liabilities.

## Retained spend restriction

Every public progress state describes admitted work and requires `SpendRestriction.RETAINED`, including the initial `REQUESTED` state. The typed progress constructor rejects a missing restriction, invalid identity/version or missing state. The initial-progress rule first validates the retained acceptance.

| State | Meaning in the contract |
| --- | --- |
| `REQUESTED` | The accepted intent and spend restriction must already be retained together. |
| `LOCAL_RESTRICTION_RETAINED` | The local restriction remains in place before provider progress. |
| `PROVIDER_PENDING` | Provider completion is pending; spending remains restricted. |
| `PROVIDER_CONFIRMED` | The cancellation action records provider confirmation; liabilities still require separate evidence. |
| `REJECTED` | The provider result is rejected; the local restriction remains. |
| `RECONCILIATION_REQUIRED` | An uncertain outcome requires recovery while the restriction remains. |

Allowed transitions do not include restriction release or reactivation. Pending, rejected or uncertain provider work, a retry, and notification failure cannot automatically restore spending. The future persistence implementation must commit intent, restriction, receipt and history atomically and retain them across restart; the pure invariant is not proof that this transaction exists.

## Complete card-owner coverage

The coverage rule requires an observation for each capability:

- Authorizations and admitted holds.
- Clearing, presentment and processor settlement.
- Refunds and returns.
- Disputes and chargebacks.
- Credit liabilities, fees and tax.
- Tokens and wallets.
- Late-item disposition.

Each internal observation must match the exact retained tenant, account, enrollment, closure, relationship revision, fence revision and complete-selection digest. It carries a receipt identity, source revision, completeness marker and pending/unknown counts. Those records are internal producer evidence, not trusted request fields. Future producers must establish their authenticity, completeness and coherent fenced selection.

Missing capabilities, partial or mismatched selections, invalid receipts, unknown outcomes and contradictory clear/pending evidence yield `UNKNOWN`. A complete selection with outstanding obligations yields `OBLIGATIONS`. Only complete, matching evidence with no unresolved items can yield card-owner `CLEAR`. An explicit `NOT_APPLICABLE` observation still needs the same complete scoped evidence; an empty list or missing adapter is insufficient.

Issuer confirmation or card status alone supplies none of the missing financial, token or late-item evidence. Existing admitted work, refunds and disputes must remain observable after instrument termination. A final account decision also needs every other required owner and an authorized late-item disposition. Card-owner `CLEAR` is not account `CLOSED`.

## Development boundary

The maintained cancellation and closure-observation tests exercise pure decoding, canonical intent/replay, acceptance linkage, restricted progress and coverage rules. They establish no database persistence, durable fence, current JWT/policy enforcement, provider termination, HTTP/SDK integration, financial settlement or final account closure.

The next enrollment and issuer/processor work must use service-created opaque instruments and a durable sandbox with explicit pending and uncertain outcomes. Preserve the existing [authentication-data boundary](card-authentication-data.md) and [activation boundary](card-activation.md). Do not fabricate a PAN, relax legacy validation globally, seed private owner bindings, or edit generated clients to fill those gaps. New SDK operations must come from the original owner producer and generator after the actual HTTP source exists.
