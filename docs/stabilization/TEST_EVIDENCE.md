# Executed verification — 2026-11-12

## Passing regression suites

46 targeted behavioral tests passed, with no failures or skipped tests:

- Gateway: **15** (5 JWT/actor/serialization tests and 10 MVC security/login tests).
- Incident: **5** (minimal anonymous receipt, downstream failure after save, failed save, coordinate validation, Accident type).
- Responder: **8** (history linkage, off-duty semantics, audit despite remote log failure, missing-assignment rejection, available-only fallback, safe audited creation, NEW-to-IN_PROGRESS projection, no deliberate reopening of terminal incidents).
- Log: **2** (append ignores caller ID/time; update/delete rejection).
- Frontend: **16**, executed in headless Edge 157 using Karma's ChromeHeadless launcher (cold observable, error propagation, invalid receipts, timeout, Accident mapping, delayed SOS confirmation, duplicate suppression, GPS denial/geocoder independence, modal failure/different-location rejection, token scope, real history/failure state, confirmed profile updates, admin-edit compatibility).

Backend commands, from repository root:

```powershell
mvn -B -o -f Backend-ERCP/Api-Gateway-Server/pom.xml '-Dtest=JwtBehaviorTest,SecurityBoundaryTest' test
mvn -B -o -f Backend-ERCP/Incident-Service/pom.xml '-Dtest=IncidentIntakeTest' test
mvn -B -o -f Backend-ERCP/Responder-Service/pom.xml '-Dtest=ResponderHistoryTest' test
mvn -B -o -f Backend-ERCP/Log-Service/pom.xml '-Dtest=AppendOnlyLogTest' test
```

Frontend command, from `Frontend-ERCP/emergency-response-platform`:

```powershell
$env:CHROME_BIN='C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe'
npm test -- --watch=false --browsers=ChromeHeadless --include=src/app/stabilization.spec.ts --ts-config=tsconfig.stabilization.json
```

Surefire reports reside under each service's `target/surefire-reports`; condensed passing summaries are in `evidence/`. The separate Angular tsconfig includes the new regression suite because legacy specs fail compilation. It does not replace or hide the result of the full suite.

Initial regression iterations found a real public-SOS stale-token filter bypass issue and test-fixture stubbing/type errors; these were corrected. One gateway login run collided with compilation of the same target directory and failed with `NoClassDefFoundError: LoginResponse`; the serial rerun passed all 15. Sandbox setup stalls were interrupted and rerun with authorized local execution. These interrupted/failed attempts are not counted as passes.

Karma reported successful completion of all tests, followed by Windows browser shutdown warnings (`ChromeHeadless was not killed ... continuing`). No real dispatch, ERSS, telephony, database or notification endpoint is exercised by these tests.

## Build and full-suite status

- All eight backend projects compiled main and test sources successfully: Gateway, Incident, Responder and Log through their regression commands; Location, Victim, User and Eureka through `mvn -B -o -f Backend-ERCP/<service>/pom.xml test-compile`. This is compilation evidence, not a full runtime integration pass.
- `npm run build -- --configuration development`: **passed**. Existing optional-chain template warning remains.
- `npm run build`: **failed** production bundle budget. Initial bundle approximately **1.28 MB**, configured maximum **1 MB** (reported 1.05 MB in decimal units), approximately 233 kB over. Initial sandbox attempt also failed fetching Google Fonts; authorized rerun reached the budget check. The budget was not raised to manufacture a pass.
- `npm test -- --watch=false --browsers=ChromeHeadless`: **failed compilation**, before running tests. Existing generated specs import nonexistent names (`DashboardComponent`, `ResponderTypeCardComponent`, `ActiveIncidentsComponent`, `PastIncidentsComponent`, `ProfileCardComponent`, `SosAlertModalComponent`, `UserModel`); model specs instantiate interfaces/use type-only IncidentStatus as an enum and obsolete `location` properties; interceptor spec omits its required constructor dependency (already required before this patch).
- Baseline full Maven tests for all eight services: **blocked**, missing offline Surefire dependencies. After installing dependencies, focused tests above passed. Full database-dependent `contextLoads` suites were not rerun against an actual PostgreSQL instance. No end-to-end or PostgreSQL integration pass is claimed.
- `git diff --check`: **passed** on the review diff.
- A literal-credential scan of tracked source/config found no remaining identified real JWT/database/weather/Maps credential literals. The remaining `YOUR_GOOGLE_MAPS_API_KEY` match is a commented example. This is not a complete secret-history or third-party vulnerability audit.

## Explicit evidence limits

Mocks establish save-before-dispatch ordering and error behavior, not physical database durability or rollback. The local audit transaction uses Spring/JPA but requires PostgreSQL integration coverage before operational use. No concurrent reservation, database migration, real network failover, load test, measured latency or production readiness claim is made. Historical exposed secrets still require external rotation.
