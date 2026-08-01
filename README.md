# FormCraft

FormCraft is a multi-service form platform with an API gateway, authentication service, and React front end.

## Architecture

| Service | Path | Stack | Port |
|---------|------|-------|------|
| **Gateway server** | `gateway-server/` | Spring Cloud Gateway (WebFlux) | `8080` |
| **Auth service** | `auth/` | Spring Boot, Security, JPA, JWT, Google OAuth | `8081` |
| **Front end** | `front-end/` | React, Vite, TypeScript, MUI | `3000` |

```
Browser (3000)
    → Gateway (8080)
        → Auth service (8081)  [/api/auth/**, /api/users/**]
```

## Prerequisites

- **Java 21** and Maven (or use the included `mvnw` wrappers)
- **Node.js** 20+ and npm
- **PostgreSQL** running locally with a database named `formcraft-auth`
- **Docker** and Docker Compose (for the local observability stack)

## Quick start

### 0. Observability stack (optional but recommended)

```bash
cd observability
docker compose up -d
```

Apps still run locally via `mvnw`. The stack scrapes metrics from them, Alloy ships log files to Loki, and apps push traces to Tempo.

| Service | Host port | URL |
|---------|-----------|-----|
| Grafana | `3300` | [http://localhost:3300](http://localhost:3300) (admin/admin, dark theme) |
| Prometheus | `9091` | [http://localhost:9091](http://localhost:9091) |
| Loki | `3401` | [http://localhost:3401](http://localhost:3401) |
| Tempo (HTTP) | `3201` | [http://localhost:3201](http://localhost:3201) |
| Tempo OTLP HTTP | `14318` | used by apps for traces |
| Tempo OTLP gRPC | `14317` | available if needed |
| Alloy | `12346` | log shipper UI |

Grafana is provisioned with **Prometheus**, **Loki**, and **Tempo** datasources (Connections → Data sources, or the Explore datasource picker). Start the Spring apps before expecting scrapes, logs, or traces.

Telemetry wiring:

- **Metrics:** Micrometer → `/actuator/prometheus` → Prometheus scrape
- **Logs:** SLF4J / Lombok `@Slf4j` → structured console/file (`[service, span_id=..., trace_id=...]`) → Alloy → Loki
- **Traces:** OpenTelemetry (Micrometer Tracing) → OTLP → Tempo

Log lines look like:

```text
2026-07-31T15:00:00.000+03:00  INFO [auth, span_id=a1b2c3d4e5f60718, trace_id=803b448a0489f84084905d3093480352] c.f.auth.auth.AuthController : Register request: ...
```

In Grafana Explore:

- Logs → choose **Loki**, query `{app="auth"}` or `{app="gateway-server"}`
- Traces → choose **Tempo**, search by service name or TraceQL
- Click a `trace_id` in Loki to jump to Tempo

Log files are written under the process working directory as `logs/<service>.log`. When you start apps from the IntelliJ project root, that is `FormCraft/logs/`. Alloy watches that folder (and `auth/logs`, `gateway-server/logs`) and ships lines to Loki.
### 1. Auth service

```bash
cd auth
./mvnw spring-boot:run
```

Runs on [http://localhost:8081](http://localhost:8081). Copy `auth/src/main/resources/application-local.yaml.example` to `application-local.yaml` and fill in DB password, JWT secret, and Google OAuth credentials (that file is gitignored).

### 2. Gateway server

```bash
cd gateway-server
./mvnw spring-boot:run
```

Runs on [http://localhost:8080](http://localhost:8080) and routes auth/user traffic to the auth service.

### 3. Front end

```bash
cd front-end
cp .env.example .env.development
npm install
npm run dev
```

Runs on [http://localhost:3000](http://localhost:3000). Point `VITE_API_URL` at the gateway (`http://localhost:8080`).

## Services overview

### Gateway server

Spring Cloud Gateway entry point for the API. Handles CORS and forwards `/api/auth/**` and `/api/users/**` to the auth service.

### Auth service

Handles registration, login/logout, JWT access/refresh tokens, user identity, and Google OAuth. Exposes OpenAPI docs when the service is running.

### Front end

React SPA for public marketing pages, login/register (including Google continue), and an authenticated dashboard shell.

## Repository layout

```
FormCraft/
├── gateway-server/   # API gateway
├── auth/             # Authentication & user service
├── front-end/        # React client
├── observability/    # Prometheus, Loki, Tempo, Grafana (Docker Compose)
├── .gitignore
└── README.md
```
