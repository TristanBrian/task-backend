# Task Backend

A Spring Boot REST API for managing tasks. Create, list, update, and delete tasks with optional status filtering and due dates. Interactive API docs are available via Swagger UI.

## Tech Stack

- **Java 21**
- **Spring Boot 4.1** (Web, JPA, Validation, Actuator)
- **PostgreSQL**
- **Springdoc OpenAPI 3** (Swagger UI)
- **Docker & Docker Compose**

## Quick Start with Docker (recommended)

Run the full stack (PostgreSQL + API) with one command:

```bash
docker compose up --build
```

When the app is healthy, these URLs are live:

| URL | Purpose |
|-----|---------|
| http://localhost:8080/ | Live status page (home) |
| http://localhost:8080/api | API entry point with links |
| http://localhost:8080/api/tasks | Task CRUD endpoints |
| http://localhost:8080/swagger-ui.html | Swagger UI (interactive docs) |
| http://localhost:8080/v3/api-docs | OpenAPI JSON |
| http://localhost:8080/health | Browser-friendly health status |
| http://localhost:8080/actuator/health | Health check (JSON) |

Docker seeds **3 sample tasks** on first startup so you can test immediately.

### Test live endpoints

```bash
# Health
curl http://localhost:8080/actuator/health

# List seeded tasks
curl http://localhost:8080/api/tasks

# Create a task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"My task","status":"TODO"}'
```

### Stop Docker

```bash
docker compose down
```

Remove the database volume as well:

```bash
docker compose down -v
```

## Local Development (without Docker)

### Prerequisites

- Java 21+
- Maven 3.9+ (or use `./mvnw`)
- PostgreSQL 14+

### 1. Create the database

```sql
CREATE DATABASE taskdb;
```

### 2. Configure the database (optional)

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/taskdb` | JDBC URL |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | `root` | Database password |
| `SERVER_PORT` | `8080` | HTTP port |
| `SEED_SAMPLE_DATA` | `false` | Seed demo tasks on startup |

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Optional sample data locally:

```bash
SEED_SAMPLE_DATA=true ./mvnw spring-boot:run
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/` | Live status page (browser) |
| `GET` | `/api` | Service info and doc links |
| `GET` | `/api/tasks` | List all tasks |
| `GET` | `/api/tasks?status=TODO` | Filter by status (`TODO`, `IN_PROGRESS`, `DONE`) |
| `GET` | `/api/tasks/{id}` | Get a single task |
| `POST` | `/api/tasks` | Create a task |
| `PUT` | `/api/tasks/{id}` | Update a task (partial fields) |
| `DELETE` | `/api/tasks/{id}` | Delete a task |
| `GET` | `/actuator/health` | Application health |

### Example: create a task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Write API docs",
    "description": "Document all endpoints in README",
    "status": "IN_PROGRESS",
    "dueDate": "2026-07-15"
  }'
```

### Example response

```json
{
  "id": 1,
  "title": "Write API docs",
  "description": "Document all endpoints in README",
  "status": "IN_PROGRESS",
  "dueDate": "2026-07-15",
  "createdAt": "2026-06-30T12:00:00Z",
  "updatedAt": "2026-06-30T12:00:00Z"
}
```

## Swagger UI

Open **http://localhost:8080/swagger-ui.html** to explore and execute all endpoints from the browser.

The OpenAPI contract is also exposed at **http://localhost:8080/v3/api-docs**.

## Project Structure

```
src/main/java/Task_Mng/task_backend/
├── config/          # OpenAPI config and sample data seeder
├── controller/      # REST controllers
├── dto/             # Request/response records
├── exception/       # Error handling
├── model/           # JPA entities and enums
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic
```

## Running Tests

Tests use an in-memory H2 database and verify live endpoints (health, Swagger docs, full task CRUD):

```bash
./mvnw test
```

## Build JAR

```bash
./mvnw clean package
java -jar target/task-backend-0.0.1-SNAPSHOT.jar
```

## Docker Files

| File | Purpose |
|------|---------|
| `Dockerfile` | Multi-stage build (Maven → JRE image) |
| `docker-compose.yml` | PostgreSQL + API with health checks |
| `.dockerignore` | Keeps build context small |

## License

Apache 2.0
