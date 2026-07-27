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

## Quick start

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
├── .gitignore
└── README.md
```
