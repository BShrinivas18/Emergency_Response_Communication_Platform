# Local development run — 2026-11-12

Started the Angular development server and all eight Spring Boot processes on loopback.

- Frontend: http://127.0.0.1:4200/ (HTTP 200).
- Gateway: http://127.0.0.1:8888 (anonymous GET /incidents returns 401, as intended).
- Eureka: http://127.0.0.1:8761/.
- Incident 8080, Responder 8081, Victim 8082, Location 8000, Log 8084: empty-list GET endpoints checked, without creating incidents or assignments.
- Separate User Service uses 8083 to avoid the original 8080 collision; it has no gateway route.
- Isolated PostgreSQL 18 development cluster: 127.0.0.1:55432, with seven separate development databases. The existing system PostgreSQL service/data was not changed.

Runtime logs, PID files, generated development credentials and local helper scripts are under `.local-run/`, excluded by `.gitignore`. Credentials are not printed or committed. Java heaps are capped at 256 MB per service for this local run.

The databases are fresh; no staff login accounts, responders, or incidents were seeded. Public landing is available. Staff dashboards require separately provisioned local accounts. No emergency reporting, assignment, notification, telephony or ERSS operation was invoked.

Backend readiness was checked through startup logs, discovery registration and read-only HTTP requests. Browser automation timed out; a visual browser session was not verified. Google Maps remains unconfigured and the weather provider is disabled, as described in the stabilization notes.

To stop only this local stack from the repository root:

```powershell
& ./.local-run/stop-local.ps1
```

Local helper scripts and credentials are machine-local runtime state, not a deployment system. No production deployment, remote push, or main-branch merge was performed.
