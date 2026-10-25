# Supermarket API

REST API for a supermarket system, built with **Java 21** and **Spring Boot**. A study project focused on layered architecture, standardized responses, and database versioning.

The front-end for this project lives in [`super-market-web`](https://github.com/gabriellatcc/super-market-web) (Angular).

---

## Tech stack

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation, Security)
- PostgreSQL 16
- Flyway (database migrations)
- Docker Compose
- Lombok
- Maven (via Maven Wrapper)

---

## Project structure

```
supermarket-api/
├── compose.yaml                  # PostgreSQL database via Docker
├── pom.xml
├── .env.example                  # Environment variables template
└── src/main/
    ├── java/br/com/supermarket_api/
    │   ├── SupermarketApiApplication.java
    │   ├── common/               # ApiResponse, ApiException, PageResponse
    │   ├── controller/           # HTTP routes (UserController)
    │   ├── dto/user/             # Requests and responses (UserCreateRequest, UserResponse...)
    │   ├── model/                # JPA entities (User)
    │   ├── repository/           # Database access (UserRepository)
    │   ├── security/             # Spring Security configuration
    │   └── service/              # Business logic (UserService)
    └── resources/
        ├── application.properties
        └── db/migration/         # Flyway migrations (V1, V2...)
```

### Request flow

```
Request → DTO validation (@Valid) → Controller → Service → Repository → Database
                                                                          │
ApiResponse ← UserResponse (DTO) ← Controller ← Service ← Repository ←────┘
```

---

## Response format

Every response follows the same shape:

```json
{
  "success": true,
  "data": {},
  "message": "User created successfully!"
}
```

On error, `success` is `false`, `data` is `null`, and the HTTP status reflects the problem (404, 422, 500...).

The password is never returned: `UserResponse` exposes only `id`, `name`, `createdAt`, and `lastUpdatedAt`.

---

## Prerequisites

- [JDK 21](https://adoptium.net/) (the **JDK** with `javac` is required, not just the JRE)
- [Docker](https://www.docker.com/) with Docker Compose
- Maven does not need to be installed: the project uses `mvnw`

Check your versions:

```bash
java -version
javac -version
docker --version
```

---

## Getting started

### 1. Clone the repository

```bash
git clone https://github.com/gabriellatcc/supermarket-api.git
cd supermarket-api
```

### 2. Set up environment variables

Copy the example file:

```bash
cp .env.example .env
```

Fill in `.env` with values that match `compose.yaml`:

```env
APP_NAME=Supermarket
DB_PORT=5432
DB_DATABASE=supermarket
DB_USERNAME=root
DB_PASSWORD=root1234
```

> The `.env` file contains credentials and **must not be committed**. Only `.env.example` goes into the repository.

### 3. Run the application

With Docker open and running:

```bash
./mvnw spring-boot:run
```

Spring starts the PostgreSQL container automatically (Docker Compose Support), Flyway applies the migrations, and the API becomes available at:

```
http://localhost:8080/api
```

---

## Database and migrations

Tables are created by **Flyway** from the SQL files in `src/main/resources/db/migration/`:

| File | Description |
|---|---|
| `V1__create_tables.sql` | Creates the `users` table |
| `V2__add_new_column_users.sql` | Adds a new column to the `users` table |

Rules for new migrations:

- Name files as `V<number>__<description>.sql` (uppercase V and **two** underscores).
- Never edit a migration that has already been applied: create a new version instead.

To recreate the database from scratch (deletes all data):

```bash
docker compose down -v
./mvnw spring-boot:run
```

---

## Endpoints

Base URL: `http://localhost:8080/api`.

### Users (`/user`)

| Method | Route | Description | Authentication |
|---|---|---|---|
| `POST` | `/user` | Creates a new user | Public |
| `GET` | `/user` | Lists users with pagination (`search`, `per_page`) | Login required |
| `GET` | `/user/{id}` | Gets a user by UUID | Login required |
| `PUT` | `/user/{id}` | Updates a user | Login required |
| `DELETE` | `/user/{id}` | Deletes a user | Login required |

### Example: create a user

```http
POST /api/user
Content-Type: application/json

{
  "name": "mary jane",
  "password": "admin123"
}
```

Response (`201`):

```json
{
  "success": true,
  "data": {
    "id": "13ffaaff-a7d8-4178-8de3-9686b2d621ed",
    "name": "mary jane",
    "createdAt": "2026-10-24T16:34:05.622330465",
    "lastUpdatedAt": "2026-10-24T16:34:05.622330465"
  },
  "message": "User created successfully!"
}
```

---

## Security

- Spring Security with a `stateless` session and CSRF disabled (the API is consumed with tokens).
- User registration (`POST /user`) is public; all other routes require authentication.
- Passwords are stored as **BCrypt** hashes.
- The password is never returned in responses (response DTO plus `@JsonIgnore` on the entity).

---

## Roadmap

- [x] Project structure, Docker, and migrations
- [x] Response standard (`ApiResponse`) and error handling (`ApiException`)
- [x] User CRUD
- [ ] JWT authentication (`/auth/login` and `/auth/me`)
- [ ] Product CRUD
- [ ] Global exception handling (`@RestControllerAdvice`)
- [ ] CORS configuration for the Angular front-end

---

## Author

**Gabriella** — [@gabriellatcc](https://github.com/gabriellatcc)