# Spring Boot REST API — User & Company Management

A production-ready RESTful backend built with **Spring Boot 3**, developed during my internship where I applied and continuously strengthened my Spring Boot skills by building the project incrementally — one feature at a time — from a bare CRUD API all the way through to a fully secured, Dockerised service with a CI/CD deployment pipeline.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Development Journey](#development-journey)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Running Locally](#running-locally)
  - [Running with Docker](#running-with-docker)
- [API Reference](#api-reference)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Companies](#companies)
  - [Password Reset](#password-reset)
  - [Statistics](#statistics)
  - [Health Check](#health-check)
- [CI/CD Pipeline](#cicd-pipeline)
- [Security](#security)
- [Testing](#testing)

---

## About the Project

This project is a Spring Boot REST API for managing **Users** and **Companies**. It was built from scratch during my internship as a structured, hands-on exercise in backend development with the Spring ecosystem.

Key concepts practised and applied throughout the project:

- Layered architecture (Controller → Service → Repository)
- DTO pattern and standardised API response wrapping
- JPA/Hibernate for ORM with a MySQL database
- Soft-delete, pagination, and dynamic filtering
- JWT-based stateless authentication with Spring Security
- User status workflow (approval/rejection flow)
- Integration with an external email microservice for OTP-based password reset
- Dockerisation with multi-platform image builds
- Automated CI/CD with GitHub Actions — build, push, deploy, and health-check

---

## Development Journey

The project was built incrementally through 15 pull requests. Each phase introduced a new concept or production concern:

| Phase | What was built |
|---|---|
| 1 — Core User CRUD | Create user, paginated list, search by name, update |
| 2 — Lombok | Replaced boilerplate with `@Data`, `@Builder`, `@RequiredArgsConstructor` |
| 3 — Custom API Response | Introduced `ApiResponse<T>` wrapper with `success`, `message`, `data`, and `timeStamp` fields |
| 4 — Soft Delete | Added logical deletion and a `/users/deleted` retrieval endpoint |
| 5 — DTOs | Introduced request/response DTOs for data abstraction, security, and efficiency |
| 6 — Company Entity | Modelled a `Company` entity with a one-to-many relationship to `User` |
| 7 — JWT Security | Integrated Spring Security with a JWT filter, `TokenManager`, and `CustomUserDetails`; added `/users/me` and `/users/me/company` endpoints |
| 8 — User Status | Added `PENDING` / `APPROVED` / `REJECTED` enum and status-based filtering endpoints per company |
| 9 — Password Reset | OTP-based forgot-password flow delegated to an external email microservice |
| 10 — Statistics | Added a `/stats` endpoint reporting platform-wide aggregates |
| 11 — CI/CD | GitHub Actions pipeline: Gradle build → Docker image → Docker Hub push → SSH deploy → liveness health check |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.3 |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Build Tool | Gradle 8 |
| Utilities | Lombok, Jakarta Validation |
| Containerisation | Docker, Docker Compose |
| CI/CD | GitHub Actions |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/          # Security configuration
│   │   ├── controller/      # REST controllers (Auth, User, Company, Stats, PasswordReset, HealthCheck)
│   │   ├── dto/             # Request / response Data Transfer Objects
│   │   ├── model/           # JPA entities (User, Company) & enums (UserStatus)
│   │   ├── repository/      # Spring Data JPA repositories
│   │   ├── security/        # JWT filter, TokenManager, CustomUserDetails
│   │   └── service/         # Business logic (Auth, User, Company, Stats)
│   └── resources/
│       └── application-prod.properties
└── test/
    └── java/com/example/demo/
        ├── service/         # Unit tests — UserService
        └── ApplicationTests.java
```

---

## Features

- **JWT Authentication** — stateless login and registration with BCrypt password hashing
- **User Management** — full CRUD with soft-delete, pagination, and optional name filtering
- **Company Management** — CRUD operations and per-company user filtering by status
- **User Status Workflow** — `PENDING → APPROVED / REJECTED` tracking per user
- **Password Reset Flow** — forgot-password → OTP verification → reset password, via external email microservice
- **Statistics** — total users, total companies, average users per company, biggest company, companies without users
- **Health Check** — `/ping` endpoint used as a liveness probe by the CI/CD pipeline
- **CORS** — pre-configured and easily locked down for production
- **Dockerised** — multi-platform image (`linux/amd64`, `linux/arm64`) with a non-root `spring` user

---

## Getting Started

### Prerequisites

- Java 17+
- MySQL 8+
- Gradle 8+ (or use the included `./gradlew` wrapper)
- Docker & Docker Compose *(optional, for containerised setup)*

### Environment Variables

All sensitive configuration is injected via environment variables (see `application-prod.properties`):

| Variable | Description |
|---|---|
| `MYSQL_URL` | JDBC connection URL, e.g. `jdbc:mysql://localhost:3306/mydb` |
| `MYSQL_USERNAME` | MySQL username |
| `MYSQL_PASSWORD` | MySQL password |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `EMAIL_SERVICE_URL` | Base URL of the external email/OTP microservice |

### Running Locally

1. **Clone the repository**

   ```bash
   git clone https://github.com/tasneem1010/Springboot_project.git
   cd Springboot_project
   ```

2. **Export environment variables**

   ```bash
   export MYSQL_URL=jdbc:mysql://localhost:3306/mydb
   export MYSQL_USERNAME=root
   export MYSQL_PASSWORD=secret
   export JWT_SECRET=my_jwt_secret
   export EMAIL_SERVICE_URL=http://localhost:8081
   ```

3. **Build and run**

   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=prod'
   ```

   The application starts on **http://localhost:8080**.

### Running with Docker

A `docker-compose.yml` is provided for quick deployment. Create a `.env` file in the project root:

```env
MYSQL_SERVER_IP=<your-mysql-host>
MYSQL_DATABASE=mydb
MYSQL_ROOT_PASSWORD=secret
EMAIL_SERVICE_SERVER_IP=<your-email-service-host>
JWT_SECRET=my_jwt_secret
```

Then run:

```bash
docker compose up -d
```

To build the image yourself instead of pulling from Docker Hub:

```bash
./gradlew bootJar
docker build -t springboot-app .
```

---

## API Reference

All endpoints return a unified JSON envelope:

```json
{
  "success": true,
  "message": "...",
  "data": { ... },
  "timeStamp": "2024-01-01T00:00:00Z"
}
```

Protected endpoints require the header:

```
Authorization: Bearer <jwt_token>
```

---

### Authentication

| Method | Endpoint | Auth Required | Description |
|--------|----------|:---:|---|
| `POST` | `/auth/login` | ✗ | Login with email & password — returns JWT in the `message` field |
| `POST` | `/auth/register` | ✗ | Register a new user (pass `company_id` as a request header) |

**Login request body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Register request body:**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "password123"
}
```
> Pass the target company's ID in a `company_id` request header.

---

### Users

All user endpoints require authentication.

| Method | Endpoint | Description |
|--------|----------|---|
| `GET` | `/users` | List users (optional `?name=` filter; supports `?page=`, `?size=`, `?sort=`) |
| `POST` | `/users` | Create a new user |
| `PUT` | `/users?id={id}` | Full replacement update of a user |
| `PATCH` | `/users/{id}` | Partial update of a user |
| `DELETE` | `/users?id={id}` | Soft-delete a user |
| `GET` | `/users/deleted` | List soft-deleted users (paginated) |
| `GET` | `/users/me` | Get the currently authenticated user's info |
| `GET` | `/users/me/company` | Get the currently authenticated user's company name |

**User status values:** `PENDING` · `APPROVED` · `REJECTED`

---

### Companies

All company endpoints require authentication.

| Method | Endpoint | Description |
|--------|----------|---|
| `GET` | `/companies` | List companies (optional `?name=` filter, pagination) |
| `POST` | `/companies` | Create a new company |
| `PUT` | `/companies/{id}` | Update a company |
| `GET` | `/companies/{id}/users` | List users of a company (optional `?status=` filter, pagination) |
| `GET` | `/companies/{id}/statusCounts` | Count users grouped by status for a given company |

---

### Password Reset

These endpoints are public (no authentication required).

| Method | Endpoint | Description |
|--------|----------|---|
| `POST` | `/forgotPassword` | Send an OTP to the given email address |
| `POST` | `/forgotPassword/verifyOtp` | Verify the OTP received by email |
| `POST` | `/forgotPassword/resetPassword` | Reset the password using a verified OTP token |

**Forgot password request body:**
```json
{ "email": "user@example.com" }
```

> These endpoints proxy requests to the external email microservice configured via `EMAIL_SERVICE_URL`.

---

### Statistics

| Method | Endpoint | Auth Required | Description |
|--------|----------|:---:|---|
| `GET` | `/stats` | ✓ | Returns platform-wide statistics |

**Response `data` example:**
```json
{
  "totalUsers": 120,
  "totalCompany": 15,
  "averageUsersPerCompany": 8.0,
  "biggestCompany": "Acme Corp",
  "companiesWithoutUsers": ["Empty Ltd"]
}
```

---

### Health Check

| Method | Endpoint | Auth Required | Description |
|--------|----------|:---:|---|
| `GET` | `/ping` | ✗ | Returns `200 OK` — used as a liveness probe |

---

## CI/CD Pipeline

Pushes to `main` (or manual triggers) kick off a GitHub Actions workflow (`.github/workflows/main.yml`) that runs the following steps automatically:

1. **Build** — compiles the project and produces `build/libs/myapp.jar` via Gradle (`bootJar`)
2. **Docker Build & Push** — builds a multi-platform image (`linux/amd64` + `linux/arm64`) and pushes it to Docker Hub (`taneem101/project:latest`)
3. **Copy** — uploads `docker-compose.yml` to the target server via SCP
4. **Deploy** — SSHs into the server, pulls the new image, and restarts the stack with `docker compose up -d`
5. **Health Check** — hits `http://localhost:8080/ping` with retries; fails the pipeline if the app does not respond with HTTP 200

All credentials (MySQL, JWT secret, Docker Hub, SSH key) are stored as GitHub Actions secrets and never hard-coded.

---

## Security

- Passwords are hashed with **BCrypt** before storage — plain-text passwords are never persisted.
- All API endpoints except `/auth/**`, `/forgotPassword/**`, and `/ping` require a valid **JWT Bearer token**.
- The application is **stateless** — no HTTP sessions are maintained server-side.
- The Docker image runs as a non-root system user (`spring`) to limit the blast radius of container escape.
- CORS is currently open for all origins; tighten `allowedOriginPatterns` in `SecurityConfig` before going to production.

---

## Testing

Run the test suite with:

```bash
./gradlew test
```

Tests live in `src/test/java/com/example/demo/` and cover service-layer logic using JUnit 5 and Spring Boot Test.
