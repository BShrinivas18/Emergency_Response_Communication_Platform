# Local startup and runtime repair (2026-11-14)

Use the frontend at http://127.0.0.1:4200/. The API gateway is at http://127.0.0.1:8888/; its root returns a small JSON description rather than an application screen. Internal service ports expose their API paths, not a website.

## Starting after a reboot

Prerequisites: Java 21 (`JAVA_HOME`), Maven, Node/npm, installed frontend dependencies (`npm ci`), and PostgreSQL 18 binaries (or set `PG_BIN`). Maven dependencies must already be cached for the offline local startup script; on a fresh machine run Maven package online first.

From the repository root, run:

```powershell
$env:JAVA_HOME = 'C:\Users\91911\Documents\jdk-21'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
./scripts/start-local.ps1
```

Stop the existing stack before starting another copy:

```powershell
./scripts/stop-local.ps1
```

Scripts use the separate PostgreSQL cluster on 127.0.0.1:55432 and seven development databases. They do not modify the system database service. Logs and generated credentials stay in ignored `.local-run/`. Java service heaps are limited; run browser tests/builds before starting the full stack on memory-constrained machines.

The `local` Spring profile can provision an initial administrator only into an empty account database, and only when both `ERCP_LOCAL_ADMIN_USERNAME` and `ERCP_LOCAL_ADMIN_PASSWORD` are supplied. The script uses username `local-admin`; open `.local-run/secrets.json` locally and use its `localAdminPassword` field to log in. Do not commit or share that file. There is no default password, password logging, public registration exception, or credential reset. Existing account databases are left untouched. Responder records and accounts still need deliberate provisioning.

## Repairs

- Simplified Angular standalone application providers: one HTTP provider chain and one animation provider; interceptor is no longer passed as an NgModule.
- Removed the invalid Maps script and guarded the map component. Missing Google Maps configuration shows a fallback; browser GPS and reporting remain independent. Weather unavailable is no longer presented as a verified absence of alerts.
- Lazy-loaded routes and sent unknown paths to the public page rather than the staff dashboard.
- Login navigation uses the authenticated token's role instead of the role selected in the form.
- Added public GET / API information without making protected reads or staff provisioning public.
- Added opt-in local initial admin provisioning and reproducible start/stop scripts.

## Verification

Gateway regression run: 20 tests passed (JwtBehaviorTest, SecurityBoundaryTest, LocalAdminBootstrapTest), zero failures/errors/skips. Angular development compilation completed. Browser regression, production build, and live login/API results are recorded below when complete.

No real emergency service, ERSS, notification, or telephony operation is involved in verification. No production deployment or main merge.

## 2026-11-14 repair verification

- Angular regression suite: **18 passed**, including rendering the actual landing template with application providers and no Google Maps, and navigation using the authenticated role.
- Gateway regression suite: **20 passed**, including API root/security boundary and initial local admin provisioning tests.
- Angular production build: **passed**, initial bundle **729.24 kB**, below the unchanged 1 MB error budget. The 500 kB warning budget, existing optional-chain template warning, and Bootstrap selector warnings remain.
- Gateway executable package: **passed**.
- Local helper scripts: PowerShell parser checks passed. Launching from the checked-in scripts successfully started the isolated database and requested all eight services plus Angular. Final HTTP readiness is checked separately.
- Existing unrelated legacy Angular specs and full database context suites are not claimed fixed by this bounded runtime repair.

Reproduce the targeted frontend checks using the commands in TEST_EVIDENCE.md. Reproduce gateway checks with:

```powershell
mvn -B -o -f Backend-ERCP/Api-Gateway-Server/pom.xml '-Dtest=JwtBehaviorTest,SecurityBoundaryTest,LocalAdminBootstrapTest' test
./scripts/check-local.ps1
```

The final command requires the local stack to finish starting and uses generated credentials without printing them. It only reads protected API data; it never creates an incident or triggers assignment.

## Live verification completed

The production frontend was opened in the in-app browser: landing page and navigation to the login screen rendered correctly, and captured browser error logs were empty. This is a public-page rendering check; staff dashboard UI and SOS submission were not exercised in that browser session.

The checked-in read-only smoke script passed against the running local stack: frontend and gateway roots 200; anonymous incident read 401; generated local administrator login 200; authenticated gateway reads of incidents, responders, logs, and locations all 200. Evidence: `evidence/runtime-smoke.txt`. No incident was created.

The local run serves the built production frontend with `scripts/serve-frontend.cjs` on loopback. This avoids retaining Angular's compiler in memory alongside eight Java services. The startup script builds the frontend first. Initial backend startup on this memory-constrained machine took several minutes; a launched process is not yet a ready service. Run `scripts/check-local.ps1` after startup to confirm readiness.
