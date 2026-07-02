# Admin Team Pokémon API

REST API developed with **Spring Boot 3** to manage Pokémon teams for authenticated trainers.

The project was built as a learning project focused on modern backend development practices, including authentication, REST APIs, Docker, OpenAPI, design patterns, and automated testing.

---

## Features

- Trainer registration and authentication using JWT
- Capture Pokémon
- Move Pokémon between Team and Storage
- Rename captured Pokémon
- Delete captured Pokémon
- Retrieve Team and Storage information
- Automatic Pokémon information retrieval from PokeAPI
- OpenAPI / Swagger documentation
- Docker & Docker Compose support

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- Docker
- Docker Compose
- OpenAPI / Swagger
- JUnit 5
- Mockito
- Maven

---

## Architecture

The project follows a layered architecture.

```
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

External integrations are isolated using the Adapter Pattern.

```
PokemonClient
        ▲
        │
PokeApiAdapter
        │
     PokeAPI
```

---

## Running the project

### Clone the repository

```bash
git clone https://github.com/Roberhp/admin-team-pokemon.git
```

### Start the application

```bash
docker compose up --build
```

Swagger will be available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Project Status

Current features:

- Authentication
- Pokémon management
- Dockerized environment
- Swagger documentation
- Unit Tests

Planned improvements:

- GitHub Actions
- Flyway
- Redis Cache
- Integration Tests
- AWS Deployment
- Kubernetes

---

## Author

Roberto Huerta