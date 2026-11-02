# Persisted card activation boundary

Core owns the local activation action and independently verifies the issuer observation. Domain and experience callers cannot provide a trusted issuer decision or activate a record through ordinary card CRUD.

This is a core persistence boundary. It does not qualify a live issuer, an operational issuance journey, secure PIN establishment, or a durable issuer sandbox. The authenticated ownership policy has no permissive default. The default issuer adapter is a replaceable NoOp: it reports `NOT_CONFIGURED` and `NOT_EXECUTED`, never an activated instrument or issuer receipt. Without a configured policy and an issuer adapter explicitly reporting `CONFIGURED`, activation returns HTTP 503 before reserving proof or creating an action.

## Request and result

`POST /api/v1/cards/{cardId}/activation` accepts an `X-Idempotency-Key` UUID and a JSON body with `activationProof`. The proof must be a high-entropy, issuer-issued value encoded with 43–128 URL-safe characters. A short customer OTP requires a different keyed-verification and attempt-limiting contract; this endpoint does not implement one.

The request cannot choose tenant, actor, issuer instrument or issuer outcome. Proof is write-only and excluded from diagnostic strings. Do not put this request into a workflow, event, audit payload or URL. The service hashes the proof before storage and sends no raw proof to either issuer operation.

A receipt contains the stable action ID, card ID, action state and historical activation time. `CONFIRMED` means that the local proof, card state and receipt committed together. `UNCERTAIN` leaves the card unusable and permits the same action ID to recheck the issuer. `REJECTED` and `RECONCILIATION_REQUIRED` are terminal receipts; they do not activate the card. A receipt describes that action, not the card's current restrictions.

Reusing an action ID with another card, actor, tenant, proof digest or binding version conflicts. A different action ID cannot reuse a reserved or consumed proof. Exact confirmed retries retain the first activation time.

Activation uses the framework's native `@DisableIdempotency` opt-out for transport response reuse. Its OpenAPI operation carries `x-disable-idempotency: true`. The required UUID `X-Idempotency-Key` remains an explicit owner action identifier, including in the generated SDK. Every retry reaches core's current authorization and persisted action checks before a receipt can be returned. A previously returned transport response cannot substitute for those checks.

## Ownership and issuer contracts

`CardActivationPolicy` must resolve the current authenticated actor and tenant, check current permission for the exact card and return its actual party/account scope with a short-lived authorization expiry. Customer or tenant IDs from a request are not proof of identity. Core checks the authorization expiry using the database clock after acquiring locks and verifies the current card against its persisted tenant/party/account binding.

`CardIssuerPort` belongs to core. Its deployment adapter must implement `initiateActivation(binding, actionId, expiresAt)` and read-only `observeActivation(binding, actionId)`, and explicitly report `Configuration.CONFIGURED`. A deployment supplies its adapter as a `CardIssuerPort` bean; the conditional NoOp backs off. The adapter must use configured issuer credentials, preserve the exact stable Core action and binding, enforce the supplied initiation deadline, and never retry an ambiguously delivered command. Provider rejection, pending status and unknown delivery remain distinct; the adapter must never infer activation from request acceptance. Core compares the returned action, tenant, adapter, instrument and binding version before accepting an `ACTIVATED` result with a nonempty issuer receipt. Missing, mismatched, pending or failed observations cannot activate the card. Configure credentials through the environment or a secret integration, never card provider records or request bodies.

There is no public arbitrary proof-registration API. The `card_issuer_instrument` and `card_activation_proof` records are reserved for a future qualified, core-owned issuance integration. Historical cards receive no guessed issuer or tenant assignment. A SQL fixture can demonstrate transaction mechanics; it cannot establish that a real issuer issued an instrument or possession proof.

## Persistence and races

Migration V7 adds issuer bindings, proof verifiers and stable action receipts. It does not rewrite historical card rows or change V6 credential protection.

Reservation locks card, binding, proof and action in that order. A known exact expiry is never extended by month/year metadata; unknown, invalid or contradictory expiry data rejects. It checks database time after acquiring the locks, requires an inactive locked draft and reserves the proof for one action. The database transaction commits before any issuer call. Migration V9 adds a separate dispatch claim: Core rechecks current authority, locks the same card/binding/proof/action, checks proof expiry and restrictions, inserts one claim and marks the action `UNCERTAIN` before sending. Concurrent or repeated calls that cannot acquire the claim only observe the original action. No transaction lock spans an issuer call.

Finalization reacquires those locks and checks a newly authorized scope. It atomically consumes the proof, sets ACTIVE, `isActive=true` and `isLocked=false` and the database activation timestamp, and commits the confirmed action receipt. A restriction, expiry or changed binding encountered after issuer observation records `RECONCILIATION_REQUIRED` and leaves the card unusable. A configured issuer adapter performs the external command. The NoOp performs none. No durable sandbox or live issuer adapter is included or qualified by this boundary.

Proof expiry is checked after locking both at initial reservation and at the first dispatch claim. An already dispatched action can be observed and finalized later using its existing identity. A different action cannot use an expired, reserved or consumed proof.

A native database guard also prevents stale updates from lifting a later restriction or erasing committed activation history and issuer ownership.

Ordinary card creation rejects ACTIVE status and activation evidence, and always persists a new draft inactive and locked, including when the caller supplied `isLocked=false`. Ordinary update cannot activate a draft, unblock a card, enable an inactive flag, remove an existing lock, change ownership or rewrite an activation timestamp. An ACTIVE record may retain its status during a permitted metadata update. Existing blocked-to-active callers must migrate to a separately authorized restriction-removal action; ordinary PUT is no longer that operation.


## Read-only recovery and uncertain dispatch

`GET /api/v1/cards/{cardId}/activation/{actionId}` returns the persisted receipt only after current authenticated ownership and the original actor, tenant, card and binding are verified. It does not call the issuer, reserve or consume proof, claim dispatch, or update state. A missing or out-of-scope receipt returns 404. `UNCERTAIN` remains uncertain on this read.

A crash between the committed dispatch claim and the actual network request is conservatively uncertain. Core does not resend automatically, including after restart. An explicit repeat of the same POST with its original proof and current authority can observe that original action and finalize a matching issuer result; it cannot claim a second initiation. If the provider cannot resolve whether the original action executed, reconciliation is required operationally. NoOp availability is not provider rejection and never resolves an uncertain receipt.

## Qualification boundary

The maintained database tests use disposable PostgreSQL and synthetic, SQL-inserted bindings/proofs. They cover reservation, wrong/expired/scope proof rejection, replay, concurrency, rollback, post-lock expiry, restriction races and independent core issuer-observation checks. Their positive SQL fixtures are persistence mechanics only. They do not qualify a positive issuance or activation journey.

Before enabling production, configure and qualify the native authenticated tenant/card ownership policy and a core-owned issuer adapter, the genuine issuance entry point, durable issuer sandbox restart/recovery and generated-SDK HTTP calls. Experience must call the generated Domain SDK, and Domain must call the generated Core SDK, preserving the original customer proof and action identity across both hops. Experience must never call Core directly. Existing synthetic activation flows remain unqualified until migrated.

## Native receiver and policy deployment

The executable includes the published native resource server. Configure its trusted issuer, JWKS endpoint, audience, tenant projection and required scopes; set `firefly.security.resource-server.strict-policy=true`. Core's activation ownership policy is separately enabled with `firefly.cards.activation.native.enabled=true` and its explicit issuer, platform tenant, resource tenant, audience, permitted client IDs and native claim mappings. These routing limits do not grant ownership: the current signed principal and persisted issuer binding are checked for every operation.

A deployment can supply its own `PolicyDecisionPort` and `PolicyEvaluationPort`. To use the published OPA adapters, explicitly enable `firefly.cards.authorization.opa.enabled` and set `endpoint`, `decision-path` and `evaluation-path` under that prefix. The endpoint must be an HTTPS origin; the explicit `allow-loopback-http` option is limited to loopback qualification. User information, query parameters, fragments and document traversal are rejected. Requests have a two-second deadline and bounded response buffering. Policy errors are indeterminate and cannot authorize activation. The evaluation document must return the actual engine policy revision and decision ID; missing provenance cannot create a Core ownership scope.

The maintained three-service native test runs the original Experience, Domain and Core executables with generated SDKs, signed native HUMAN tokens, real HTTP token exchange, scoped controlled policy responses and isolated PostgreSQL caches. It verifies that current authorization reaches Core while the default issuer still returns `NOT_CONFIGURED: NOT_EXECUTED`, and that card, binding, proof, action and dispatch rows remain unchanged. It also checks withdrawn policy and changed native identity/scope. This is authenticated transport and NoOp-boundary evidence, not issuer execution, genuine issuance, a durable issuer sandbox or successful activation.
