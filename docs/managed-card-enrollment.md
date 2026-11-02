# Opaque managed-card enrollment contracts

Managed enrollment has a separate identity and contract from legacy PAN-bearing card records. The current source provides pure immutable records, strict request decoders and eligibility, intent, provider-result and recovery rules. It introduces no persistence service, database schema, authenticated eligibility adapter, issuer/processor implementation, HTTP endpoint or generated SDK operation.

A supplied eligibility record is not proof of current authority. An inactive issuer instrument is not an activated card, financial admission, complete card-product readiness or account clearance.

## Intent and server selection

An enrollment request contains `actionId`, `expectedAccountVersion`, `reasonCode` and `acceptNotAfterEpochMillis`. The action is stable; the account version must match the server selection. The reason is a bounded uppercase code and the fixed deadline controls first admission. The closed decoder requires a bounded UTF-8 object, canonical UUID and exact integers. Duplicate, missing, unknown, null, coerced or trailing values are rejected.

The request cannot declare tenant, actor, party, account, contract, product, managed/legacy card identity, eligibility, provider outcome or sensitive card data. The future authenticated boundary resolves and authorizes the complete resource selection.

The pure server selection binds tenant, actor, party, account, contract, product, a typed managed-card identity, enrollment identity, account/product revisions and configured issuer/processor adapter IDs. A `ManagedCardId` does not perform a legacy lookup or prove a row exists. Actual storage must resolve the managed aggregate explicitly; matching UUIDs, account metadata or masked digits cannot establish a legacy-to-managed association.

Canonical intent and replay bind that full selection, configured providers, action, reason, version and original deadline. Changed intent conflicts. An immutable acceptance also binds its receipt ID and database-recorded admission time. The pure validator compares supplied retained facts; it does not query a database, authenticate them or commit admission. Historical replay does not renew eligibility or the original deadline.

## Eligibility is an actual owner responsibility

The pure comparison requires matching, current account and product observations for the selected tenant, party, account, contract and product, with receipt identities, exact source revisions and validity horizons. Missing, expired, mismatched or unknown evidence stays `UNKNOWN`; known ineligibility prevents admission. Missing configured issuer or processor capability is unavailable. No permissive default is supplied.

These initial fact categories are not the complete cross-owner authority implementation. Existing account/funds-enrollment/product metadata does not establish a current card-enrollment purpose, actual party mandate, governed offering or bank/institution permission. Do not infer eligibility from account `ACTIVE`, funds `MANAGED`, product status, a Bank active flag or a nonempty identifier.

The actual governed CARD offering support and contract/party/bank purpose receipts remain prerequisites. A product capability must be explicitly governed; unsupported or absent offering/owner evidence cannot become a synthetic eligible result. The future adapters must use genuine qualified owner contracts and current authenticated policy, including the real account→contract→product and contract→party relationships. A fake eligible port or a caller-declared actor/provider kind cannot fill the gap.

## Stable provider operation and inactive output

The retained operation binds its own identity, the enrollment action, full original selection, intent digest and original deadline. Recovery cannot replace those fields. Provider observations must match the configured role/adapter and exact retained operation/action/scope/digest, carry a receipt and revision, and have a coherent observation time.

Issuer and processor may use different local references. Their verified common issuer instrument identity must agree before the rules return `ENROLLED_INACTIVE`. Missing or mismatched references, an unrelated instrument, an unreceipted outcome or an active/unknown use state cannot be converted into inactive enrollment. Reference formatting and typed records do not themselves authenticate a provider or prove a reference contains no sensitive data; actual adapters must retain only verified opaque identifiers and allowlisted safe metadata, never raw credential-bearing responses.

Every public progress state requires the retained restriction:

| State | Contract meaning |
| --- | --- |
| `RESERVED` | The admitted intent and unusable managed identity require durable retention before any effect. |
| `PROVIDER_PENDING` | A retained attempt is pending; the next step is observation. |
| `RECONCILIATION_REQUIRED` | The outcome is uncertain or contradictory; observe the original operation while restricted. |
| `ENROLLED_INACTIVE` | Matching issuer and processor evidence describes the same inactive restricted instrument. |
| `REJECTED` | Definitive matching provider outcomes close this enrollment action without authorizing reissue. |

An adverse `ACTIVE` provider observation is an input to rejection/reconciliation, not a usable managed output. No activation, authorization, funds, fee or final-closure operation is introduced by these rules.

## Observation before retry

The pure transition permits an initial issue step only from `RESERVED`. The future store must atomically commit `PROVIDER_PENDING` and the stable attempt before external I/O. Once an attempt is pending or uncertain, the next step is `OBSERVE`; it cannot reset to a new issue key or extend the original deadline.

A recovery request contains only the original `actionId` and `expectedProgressVersion`. Its closed decoder does not accept a provider, operation replacement, outcome or new deadline. Recovery rechecks the full retained operation binding and version. The actual service still needs fresh current authority for each observation or effect.

A timeout, empty response or generic 404 is not definitive absence. `ABSENT_CLOSED` requires an actual provider-authored retained receipt for the exact original operation. Mixed issued/declined outcomes remain reconciliation. Notification failure, worker expiry or a lost reply cannot release the restriction or erase a pending relationship.

## Pending persistence and business acceptance

A later implementation must retain monotonic actions, attempts, provider revisions, inbox/outbox and receipts in real transactions, with a complete account-owned pending-enrollment fence. Pending work must remain visible to account closure even before an instrument binding is finalized. An expired worker lease is not proof of no effect. All legacy and managed relationships, financial obligations, tokens and late items require their own complete owner evidence before any card-owner clearance; enrollment alone never makes an account `CLOSED`.

The maintained tests currently exercise pure records and rules with supplied synthetic owner/provider facts. They do not establish authentic eligibility, current JWT/policy enforcement, persistence, durable sandbox recovery, provider termination, HTTP/SDK transport or financial/card-product acceptance. Preserve the existing [authentication-data](card-authentication-data.md), [activation](card-activation.md) and [cancellation](card-cancellation.md) boundaries.

Actual managed APIs must precede original owner producer and genuine SDK generation. Do not hand-edit a spec or generated client, fabricate a PAN, seed private owner bindings, relax legacy validation globally or replace missing owner purposes with an always-eligible implementation.
