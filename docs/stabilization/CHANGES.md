# Phase 1 review notes

Branch: `stabilization/p0-security-sos`, based on main `7408d0ef9d6d78713b4c3db4b45ffecd153a25c5`.
No redesign, deployment, push, merge, emergency-service calls or credential rotation were performed.

## Changes

### Authentication and local security boundary

- Gateway `JwtService`, `JwtFilter`, `UserPrincipal`, `MyUserDetailsService`: mandatory validated environment signing key; current database role authorities; malformed/expired token rejection; public POST login/SOS bypass stale staff tokens; no token diagnostics. Removed contradictory compiler source/target 23 properties while retaining Java 17 release.
- `SecurityConfig`, `ResponderAccess`: public POST login/intake only; admin staff provisioning and administrative mutations; staff read access; responder-specific history/duty changes and additional responder requests checked against current server identity/assignment. Incident status lookup is no longer public because integer references are guessable. Unknown endpoints default to admin access.
- Gateway `UserController`, `UserResponse`: registration ignores caller ID and returns only ID/username/role. Lookup is self/admin only and returns no password hash. Failed responder lookup does not return a newly issued token.
- Explicit configurable CORS origins, no wildcard/credentialed origins. Browser interceptor uses AuthService's session JWT and attaches it only to private requests to the configured API origin. It does not redirect public SOS failures to login.
- Removed stored database password defaults and signing key; disabled credentialed client weather integration; replaced Google Maps browser-key reference with a configuration placeholder. SQL/entity/token/PII debug output removed. `.env.example` contains names only. All services bind/advertise loopback for a single-host development boundary.

### SOS and incident intake

- `EmergencyService.reportIncident` returns the real cold HTTP observable with a 15-second timeout, validates positive server reference, propagates failures and performs no internal subscription. SOS uses the existing create endpoint. API origin centralized; signup and responder-by-name paths corrected.
- Landing and incident modal wait for acknowledged server data before opening confirmation; duplicate in-flight taps are suppressed. Failure means receipt not confirmed. Dialogs distinguish recorded incident from responder assignment and expose `tel:112` without claiming ERSS connectivity. Removed the blocking landing warning and confirmation link into fabricated tracking.
- Browser GPS no longer waits for Google geocoding. Missing maps script cannot prevent constructing LocationService. Missing GPS displays an explicit no-report state. Alternate incident location is explicitly rejected rather than silently substituting reporter coordinates; a full manual/map workflow remains follow-up.
- Victim name/contact/address are optional for minimal intake. Backend validates coordinates/type, handles null victim details, sets NEW/server time, saves before downstream log/assignment, returns 201 and a minimal receipt without downstream read enrichment. Downstream failures do not erase a committed receipt. Pre-save failures produce a generic unconfirmed error.
- Accident maps to appended `TRAFFIC_ACCIDENT` in Incident Service. Existing enum ordinal positions retained; no unsafe automatic database enum migration.

### Responder history and audit

- New `ResponderEvent`/repository provides chronological `GET /responders/{id}/history`. Creation, profile edits, assignment and duty mutations persist immutable-by-API local events in the same Spring transaction as the responder change. Events include responder, incident, actor, old/new state and timestamp. No update/delete event endpoint exists. Creation ignores caller IDs/assignments and starts AVAILABLE; profile edits preserve operational status.
- Gateway replaces spoofed `X-ERCP-Actor` headers with authenticated identity; automatic Feign assignment records SYSTEM. Downstream trust relies on loopback isolation. Do not expose services directly or treat the header as cryptographic service authentication.
- OFF_DUTY/OFFLINE/AVAILABLE/NOT_AVAILABLE clear current assignment but retain its ID in history. They never mark an incident resolved. Operational states require an assigned incident; callers cannot self-assign through the duty endpoint. General responder edit rejects status changes that bypass audit.
- ON_ROUTE/ON_SCENE preserve the existing NEW-to-IN_PROGRESS projection. Already terminal incidents are not deliberately reopened. The cross-service read/update is not atomic and still needs transition concurrency controls and reconciliation after network failure.
- Police fallback selects from AVAILABLE police only. No candidate produces no fabricated assignment message. Selection remains non-atomic; concurrency/reservation is explicitly deferred.
- Fixed missing incident ID in responder log helper. Remote log failure does not discard local duty history. Remote delivery and the local transaction are not distributed-atomic.
- Log Service rejects updates/deletes, ignores client IDs on append and sets server timestamp. Gateway blocks external log writes. Database administrators can still alter records; this is not tamper-proof storage.
- Replaced fabricated responder history UI with server data and explicit empty/failure states. Profile changes use the audited PATCH endpoint and display the confirmed status only.

## Compatibility and setup

Public staff signup is intentionally closed. Use an existing authorized admin for `/register`. First-admin bootstrap and uniqueness/provisioning reconciliation require a reviewed, offline administrative procedure; no backdoor or seed admin was added. Staff creation still spans separate gateway/responder records and is not atomic.

Set `JWT_SECRET` (fresh random base64, at least 32 bytes), `RDS_PASSWORD` and explicit `CORS_ORIGINS` outside Git. Spring does not load `.env.example` automatically. Rotate the historical JWT signing key, database passwords and exposed API credentials externally and revoke old sessions. Deleting current literals does not remove Git history. No history rewrite was performed.

Review the additive `responder_events` table before applying to any existing database. Existing `ddl-auto=update` is retained; this patch has not applied a production migration. There is no fabricated backfill: history begins when audited writes are enabled. Existing historical assignments cannot be reconstructed reliably from current responder rows.

The optional weather widget currently yields no alerts because its credentialed provider is disabled. Configure a server-side provider in a future reviewed change. A restricted browser Maps key is optional for map/address UI; minimal SOS uses browser coordinates independently.

## Verification

Executed commands, results and test counts are recorded in TEST_EVIDENCE.md. Unit/MVC/browser tests use mocks; they do not prove PostgreSQL transaction durability, interservice availability, race safety, production readiness or latency. No real emergency dispatch was used.

## Remaining issues and next review boundaries

1. **Before any deployment:** external secret rotation, authenticated service networking, PostgreSQL schema isolation/migration review, dependency vulnerability triage, public-intake abuse/rate limits and operational ownership for stored-but-unassigned incidents. Current loopback defaults are development-only.
2. **Reliable intake:** idempotency/unique request ID, outbox and replayable dispatch/audit delivery, bounded Feign timeouts/retries/circuit breaking, explicit pending/failed delivery state, DB integration/fault-injection tests. Location and named-victim services still block before save; post-save calls still add latency. A lost response can lead to duplicate retries.
3. **Safe dispatch/lifecycle:** atomic reservation/locking and concurrent tests; strict transition graph; incident completion independent of duty state; safe reassignment after responder release; current-assignment checks for all incident mutations and reads; assignment actor propagation across service calls.
4. **Audit hardening:** PostgreSQL integration tests for event/write rollback, complete administrative lifecycle auditing, retention/access policy and tamper-evident storage. Existing logs lack reliable historical actor IDs; broad staff read access still needs a least-privilege review.
5. **Frontend/operations:** full manual incident location selection, real private tracking (existing tracking route still contains demo data), remove remaining demo metrics, repair generated legacy tests, supported toolchain/dependency updates and bundle optimization. Do not interpret absent weather alerts as an all-clear.

Review this phase before starting any of those follow-up implementations. Preserve all microservice boundaries. No ERSS adapter or new infrastructure is included.
