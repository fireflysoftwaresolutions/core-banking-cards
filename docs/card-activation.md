# Persisted card activation boundary

Core owns the local activation action and independently verifies the issuer observation. Domain and experience callers cannot provide a trusted issuer decision or activate a record through ordinary card CRUD.

This is a core persistence boundary. It does not qualify a live issuer, an operational issuance journey, secure PIN establishment, or a durable issuer sandbox. The required authenticated ownership policy and issuer adapter have no permissive defaults. Without both configured beans, activation returns HTTP 503 before creating an action.

## Request and result

`POST /api/v1/cards/{cardId}/activation` accepts an `X-Idempotency-Key` UUID and a JSON body with `activationProof`. The proof must be a high-entropy, issuer-issued value encoded with 43–128 URL-safe characters. A short customer OTP requires a different keyed-verification and attempt-limiting contract; this endpoint does not implement one.

The request cannot choose tenant, actor, issuer instrument or issuer outcome. Proof is write-only and excluded from diagnostic strings. Do not put this request into a workflow, event, audit payload or URL. The service hashes the proof before storage and sends no raw proof to the issuer-observation port.

A receipt contains the stable action ID, card ID, action state and historical activation time. `CONFIRMED` means that the local proof, card state and receipt committed together. `UNCERTAIN` leaves the card unusable and permits the same action ID to recheck the issuer. `REJECTED` and `RECONCILIATION_REQUIRED` are terminal receipts; they do not activate the card. A receipt describes that action, not the card's current restrictions.

Reusing an action ID with another card, actor, tenant, proof digest or binding version conflicts. A different action ID cannot reuse a reserved or consumed proof. Exact confirmed retries retain the first activation time.

Activation uses the framework's native `@DisableIdempotency` opt-out for transport response reuse. Its OpenAPI operation carries `x-disable-idempotency: true`. The required UUID `X-Idempotency-Key` remains an explicit owner action identifier, including in the generated SDK. Every retry reaches core's current authorization and persisted action checks before a receipt can be returned. A previously returned transport response cannot substitute for those checks.

## Ownership and issuer contracts

`CardActivationPolicy` must resolve the current authenticated actor and tenant, check current permission for the exact card and return its actual party/account scope with a short-lived authorization expiry. Customer or tenant IDs from a request are not proof of identity. Core checks the authorization expiry using the database clock after acquiring locks and verifies the current card against its persisted tenant/party/account binding.

`CardIssuerPort` belongs to core. Its adapter must use configured issuer credentials to independently observe the exact stable action and instrument. Core compares the returned action, tenant, adapter, instrument and binding version before accepting an `ACTIVATED` result with a nonempty issuer receipt. Missing, mismatched, pending or failed observations cannot activate the card. Configure credentials through the environment or a secret integration, never card provider records or request bodies.

There is no public arbitrary proof-registration API. The `card_issuer_instrument` and `card_activation_proof` records are reserved for a future qualified, core-owned issuance integration. Historical cards receive no guessed issuer or tenant assignment. A SQL fixture can demonstrate transaction mechanics; it cannot establish that a real issuer issued an instrument or possession proof.

## Persistence and races

Migration V7 adds issuer bindings, proof verifiers and stable action receipts. It does not rewrite historical card rows or change V6 credential protection.

Reservation locks card, binding, proof and action in that order. A known exact expiry is never extended by month/year metadata; unknown, invalid or contradictory expiry data rejects. It checks database time after acquiring the locks, requires an inactive locked draft and reserves the proof for one action. The database transaction commits before the issuer is observed. No transaction lock spans an issuer call.

Finalization reacquires those locks and checks a newly authorized scope. It atomically consumes the proof, sets ACTIVE, `isActive=true` and `isLocked=false` and the database activation timestamp, and commits the confirmed action receipt. A restriction, expiry or changed binding encountered after issuer observation records `RECONCILIATION_REQUIRED` and leaves the card unusable. A qualified issuer adapter and recovery worker must reconcile any external instrument; this phase does not perform that external effect.

Proof expiry authorizes the first reservation at its post-lock instant. That reserved action can finish later using its existing identity. A different action cannot use an expired, reserved or consumed proof.

A native database guard also prevents stale updates from lifting a later restriction or erasing committed activation history and issuer ownership.

Ordinary card creation rejects ACTIVE status and activation evidence, and always persists a new draft inactive and locked, including when the caller supplied `isLocked=false`. Ordinary update cannot activate a draft, unblock a card, enable an inactive flag, remove an existing lock, change ownership or rewrite an activation timestamp. An ACTIVE record may retain its status during a permitted metadata update. Existing blocked-to-active callers must migrate to a separately authorized restriction-removal action; ordinary PUT is no longer that operation.

## Qualification boundary

The maintained database tests use disposable PostgreSQL and synthetic, SQL-inserted bindings/proofs. They cover reservation, wrong/expired/scope proof rejection, replay, concurrency, rollback, post-lock expiry, restriction races and independent core issuer-observation checks. Their positive SQL fixtures are persistence mechanics only. They do not qualify a positive issuance or activation journey.

Before enabling production, implement and qualify authenticated tenant/card ownership, a core-owned issuer adapter, the genuine issuance entry point, durable issuer sandbox restart/recovery and generated-SDK HTTP calls. Domain and experience must forward the genuine generated core commands and customer proof; their existing synthetic activation flows remain unqualified until migrated.
