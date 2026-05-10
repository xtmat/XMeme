# XMeme

A Spring Boot REST API backend for a meme-sharing platform. XMeme lets users post, view, and manage memes, backed by MongoDB for persistence and Redis for caching. The project ships with a Docker setup that bundles MongoDB, Redis, and the Spring Boot app into a single deployable container, plus a Python-based assessment suite for automated API testing.

> Built as part of the [Crio.Do](https://crio.do) backend engineering curriculum. Licensed under Apache 2.0.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| Framework | Spring Boot 2.7.1 |
| Primary Database | MongoDB |
| Cache / Session | Redis |
| Message Broker | RabbitMQ (Spring AMQP) |
| Secondary DB | MySQL 8.0.29 (connector available) |
| Build Tool | Gradle (wrapper included) |
| Testing (Java) | JUnit 5 + Spring Boot Test |
| Testing (Python) | pytest 6.2.2 + requests |
| Code Quality | Checkstyle, SpotBugs 4.7.1, JaCoCo 0.8.8 |
| Utilities | Lombok 1.18.24 |
| Containerisation | Docker (Dockerfile included) |

---

## Project Structure

```
XMeme/
├── src/
│   └── main/java/          # Spring Boot application source
├── assessment/             # Python pytest-based API test suite
├── sample-data/
│   └── load-sample-data.sh # Script to seed MongoDB with sample memes
├── config/checkstyle/      # Checkstyle configuration
├── __CRIO__/               # Crio platform tooling (git hooks, rules)
├── gradle/wrapper/         # Gradle wrapper binaries
├── Dockerfile              # Multi-service Docker image (MongoDB + Redis + app)
├── start.sh                # Container entrypoint: starts mongod, loads data, runs jar
├── build.gradle            # Build config & dependency declarations
├── settings.gradle         # Gradle project settings
├── requirements.txt        # Python dependencies for the assessment suite
└── gradlew / gradlew.bat   # Gradle wrapper executables
```

---

## Prerequisites

**Local development:**
- Java 11 or higher
- MongoDB running locally
- Redis running locally
- Gradle (or use the included `./gradlew` wrapper)
- Python 3 + pip (for running the assessment test suite)

**Docker (recommended for a quick start):**
- Docker Engine

---

## Getting Started

### Option A — Docker (all-in-one)

The Docker image bundles MongoDB 6.0, Redis, and the application. Build and run with a single pair of commands:

```bash
# 1. Build the image (downloads dependencies, compiles, packages the jar)
docker build -t xmeme .

# 2. Run the container — server starts on port 8080
docker run -p 8080:8080 xmeme
```

What happens inside the container on startup (`start.sh`):
1. Starts `mongod` as a background daemon
2. Loads sample meme data via `sample-data/load-sample-data.sh`
3. Launches the Spring Boot executable jar

---

### Option B — Local Development

#### 1. Clone the repository

```bash
git clone https://github.com/xtmat/XMeme.git
cd XMeme
```

#### 2. Start required services

Ensure MongoDB and Redis are running locally before starting the app:

```bash
# MongoDB
sudo systemctl start mongod

# Redis
sudo systemctl start redis
```

#### 3. Build the project

```bash
# Compile, run tests, and produce a build report
./gradlew build test

# Or just create the executable jar
./gradlew bootjar
# Jar location: /tmp/external_build/libs/spring-starter-0.0.1-SNAPSHOT.jar
```

#### 4. Run the application

```bash
./gradlew bootRun
```

The server starts on **port 8080** by default.

---

## Running Tests

### Java unit tests

```bash
./gradlew test
```

### Python assessment suite

Install Python dependencies first:

```bash
pip install -r requirements.txt
```

Then run the test suite from the `assessment/` directory:

```bash
cd assessment
pytest
```

The suite uses `pytest`, `pytest-order`, and `requests` to test the live API endpoints against a running server on port 8080.

---

## API Overview

> Base URL: `http://localhost:8080`

XMeme exposes a REST API for meme management. Typical endpoints include:

| Method | Endpoint | Description |
|---|---|---|
| POST | `/memes` | Upload a new meme (name, caption, URL) |
| GET | `/memes` | Retrieve the latest memes (most recent first) |
| GET | `/memes/{id}` | Get a single meme by ID |
| PATCH | `/memes/{id}` | Update caption or URL of an existing meme |

*(Refer to the source controllers or assessment tests for the exact request/response schema.)*

---

## Code Quality

Static analysis runs automatically as part of the build:

- **Checkstyle** — style rules from `config/checkstyle/`
- **SpotBugs** — HTML reports generated per module (failures ignored by default)
- **JaCoCo 0.8.8** — XML + HTML coverage reports under `/tmp/external_build/`

---

## Development Notes

- **Lombok** is used extensively — install the Lombok plugin in your IDE (IntelliJ: Settings → Plugins → Lombok).
- Build output is redirected to `/tmp/external_build` (configured in `build.gradle`).
- The `__CRIO__` directory contains platform tooling and git hook scripts; do not modify it.
- RabbitMQ (Spring AMQP) and MySQL connector are declared as dependencies but may be optional depending on which features are active — check `application.properties` for active profiles.

---

## License

This project is licensed under the **Apache License 2.0**. Redistribution requires retaining the `README.md` and `LICENSE` files.