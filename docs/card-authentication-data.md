# Card record authentication data

Card records describe an instrument and its associations. They do not establish a processor credential, securely capture a PIN, verify an activation challenge, or prove a financial authorization.

## Ordinary record inputs and responses

Do not send `pin` or `cvv` to card creation or update. Every non-null value, including an empty string, is rejected. HTTP Bean Validation and the application service both enforce this rule. Error messages identify the unsupported operation without repeating submitted values.

Normal record mapping omits both fields in either direction. JSON responses omit them even if an internal caller constructs a populated DTO, and DTO diagnostic output excludes them. The existing Java properties remain available for source compatibility and explicit input rejection; their presence in a generated model does not make them supported request fields. Regenerate SDKs from the owning OpenAPI contract rather than changing generated Java.

Credential value predicates, null/not-null markers, ranges and sorting by either credential field are rejected before filtering. Redacting the returned DTO alone would still permit observations through matching records, totals or result order.

The legacy API still handles full card numbers. A populated `maskedCardNumber` does not suppress `cardNumber`. Full-PAN tokenization, safe display and the processor boundary require separate implementation and qualification. The credential correction does not establish those capabilities.

## Storage and historical records

The additive database migration installs triggers rejecting new non-null PIN/CVV values and updates introducing a different non-null credential. Applying the migration does not rewrite or delete historical records. Runtime roles must remain distinct from schema owners and must not receive permissions to disable triggers or change the schema.

Historical rows remain readable through the redacted DTO. Ordinary service updates return HTTP 409 while either credential column remains non-null, avoiding a silent loss of existing data during full-entity mapping. A controlled remediation must inventory affected records, establish the applicable retention and processor replacement procedure, obtain the required authorization, and verify the result separately. The migration itself performs no such cleanup.

## Processor integration

Use a separate authenticated processor operation for secure PIN establishment/change, verification, activation and any permitted sensitive-data display. Bind the operation to the actual cardholder, instrument, account, tenant and stable business operation. A processor sandbox must retain requests and outcomes across process restarts and expose explicit rejected, pending and uncertain outcomes. Replace it through a defined adapter contract and documented configuration; never switch a synthetic approval into a production success assumption.

The existing legacy activation/issuance path still requires that integration. A string validation rule, random activation code or card status change does not provide processor proof.

## Development checks

Run the repository's Maven `verify` lifecycle using its configured JDK and matching dependencies. The maintained boundary tests exercise real mapping, JSON serialization, validation and service rejection. Database tests start a disposable PostgreSQL instance and verify migration preservation, nonowner insert/update rejection, safe edits and historical read/update behavior. Docker must be available; a skipped database suite is not verification of these storage controls.

Generate and qualify the owner SDK with the repository's original OpenAPI producer and generator. Rebuild the card domain and direct experience/terminal consumers against the exact qualified client before adoption. The storage and DTO checks do not establish tenant authorization, full card lifecycle operation, scheme acceptance or regulatory compliance.
