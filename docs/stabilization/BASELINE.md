# Stabilization baseline — 2026-11-12

Repository: BShrinivas18/Emergency_Response_Communication_Platform.
Clean checkout of main: `7408d0ef9d6d78713b4c3db4b45ffecd153a25c5`.
Local branch: `stabilization/p0-security-sos`. No push, deployment, merge, secret rotation or ERSS contact.
The four supplied handoff documents were read as proposed guidance. No repository AGENTS.md existed; the proposed file was not installed.

## Architecture and assessment

Angular standalone application -> Spring Cloud Gateway MVC (8888) -> Eureka-discovered Spring Boot services -> PostgreSQL.
Eight independent Maven projects remain intact. There is no aggregate Maven reactor.

- Gateway: `/login`, `/register`, `/user/{username}` and routing to incidents, locations, victims, responders and logs. BCrypt and JWT authentication; committed signing key, permissive registration and inconsistent authority mapping verified in baseline.
- Eureka (8761): service discovery. No service identity/authentication boundary in the original code.
- Incident (default 8080): `/incidents/create`, CRUD, victim/status lookup and status mutation. Intake calls Location, Victim, Log and Responder synchronously. A record can commit before a later dependency throws. Public creation accepts DTOs with client-controlled ID/status fields, but service constructs a new entity and sets NEW/server time.
- Responder (8081): responder CRUD, duty state, assignment, additional responder requests. Candidate selection is not atomic. Police fallback can choose an unavailable responder. OFF_DUTY incorrectly implies RESOLVED. Existing past-history UI is fabricated. Log helper omits incident ID.
- Victim (8082): victim CRUD and incident lookup, with synchronous back-reference to Incident. Failure is swallowed as an empty incident list.
- Log (8084): incident timeline CRUD, previously mutable/deletable; caller-supplied IDs could overwrite records.
- Location (8000): coordinate/address lookup and CRUD; exact floating-point equality; DTOs incorrectly mapped as JPA entities as well as models.
- User (default 8080): separate user creation service, no gateway route. Duplicates identity responsibility and conflicts with Incident's default port. Leave disabled for this local stabilization workflow.

Frontend review covered routes/guards, authentication/interceptor, API services, public intake and location dialogs, confirmation/tracking components, admin CRUD/dashboards, responder profile/current/history views, models, templates and generated tests. Key findings: broken cold observable, premature SOS confirmation, inconsistent token storage, wrong signup/username endpoint, missing Accident mapping, geocoder construction dependency, false alternate-location coordinates, static tracking/history/statistics and incomplete profile status persistence. Backend review covered every service's controllers, models/DTOs, repositories, service logic, Feign contracts, configuration and POM/test inventory.

Cross-service DTOs disagree on enums and sometimes annotate remote entities for local persistence. Existing persisted enum ordinals cannot safely be converted to strings without migration. The new incident type is appended to preserve old Incident enum ordinal positions; broader schema/enum alignment remains required.

## Toolchain and original checks

- Windows/PowerShell 7.6.9, OpenJDK 21, Maven 3.9.11, Node 24.18.0, npm 11.16.0.
- POMs use Spring Boot 3.3.5 / Cloud 2023.0.3, Java 17. Gateway also declared source/target 23; inherited compiler release 17 was observed. Removed redundant conflicting declarations, without upgrading frameworks.
- Angular package declarations are 19.1 prereleases. Node 24 compatibility is not established by this task.
- `mvn -B -o -f Backend-ERCP/<service>/pom.xml test` was attempted for all eight projects. All were blocked by missing Surefire 3.2.5 artifacts in the local cache. No baseline tests passed. Initial compilation reached release 17. Some baseline jobs overlapped initial edits, so these are environment/blocker evidence, not a clean before/after benchmark.
- Initial `npm run build` and `npm test -- --watch=false --browsers=ChromeHeadless` failed because Angular builder packages were absent.
- `npm ci --ignore-scripts --cache .npm-cache` initially hit sandbox EPERM, then succeeded with authorized execution. npm reported 71 vulnerabilities: 7 low, 23 moderate, 35 high, 6 critical. No force-upgrade was performed.
- Maven dependency prefetch partly succeeded but `dependency:go-offline` failed resolving repo.spring.io. Targeted test commands subsequently use the standard test lifecycle.

## Reproduction and boundaries

Set environment variables from `.env.example` using disposable local credentials, never historical committed credentials. Run Maven from each service directory (`mvn test` / `mvn spring-boot:run`). Existing context-load tests require PostgreSQL and service environment; the targeted behavioral tests mock dependencies.

Do not start all services against a shared production database. Existing defaults use shared PostgreSQL tables and `ddl-auto=update`; review schema isolation/migrations first. The patch binds every service and gateway to 127.0.0.1 and advertises loopback in Eureka. This is a single-host development boundary only, not service-to-service authentication. Do not override bindings or expose ports for multi-host deployment without a separate authenticated network design.

Frontend: `npm ci --ignore-scripts`, `npm run build`, `npm run build -- --configuration development`, `npm test -- --watch=false --browsers=ChromeHeadless`. API origin is centralized in `src/app/core/api.config.ts`. Browser geolocation requires browser permission and a secure context. Tests must never call real emergency or dispatch infrastructure.

Final executed test evidence and remaining blockers are in CHANGES.md.
