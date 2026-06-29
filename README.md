# Employee DevOps POC

A proof-of-concept demonstrating end-to-end DevOps practices around a simple **Employee Management** web application — from build and test automation to containerization, deployment, and UI smoke testing.

## Overview

This project showcases a complete software delivery pipeline for a Spring Boot employee management app. It includes a multi-module Maven workspace, Jenkins CI/CD pipeline, Docker containerization, Kubernetes deployment manifests, and Selenium-based smoke tests with Allure reporting.

## Project Structure

| Module | Description |
|--------|-------------|
| **employee-app** | Spring Boot 3 web application with Thymeleaf UI, Spring Security login, and JSON file storage |
| **automation-tests** | Selenium + TestNG smoke tests with Allure reporting |
| **Jenkinsfile** | Full CI/CD pipeline: build, unit tests, Docker image, optional K8s deploy, smoke tests, and reports |
| **Dockerfile** | Multi-stage container build for the application |
| **docker-compose.yml** | Local deployment on port 8080 |
| **k8s/** | Kubernetes Deployment and Service manifests |

## Application Features

- Secure form-based login (Spring Security)
- List, add, edit, delete, and search employees
- Persistent data via JSON files (`users.json`, `employees.json`)
- Thymeleaf-based web UI
- Unit tests with JaCoCo code coverage

## DevOps Pipeline Stages

The Jenkins pipeline (`Jenkinsfile`) runs the following stages:

1. **Checkout** — Pull source code from SCM
2. **CI Build** — Run Maven unit tests for `employee-app`
3. **Package Application** — Build the Spring Boot JAR
4. **Docker Build** — Create a container image
5. **Push Image** — Push to a Docker registry (optional)
6. **Deploy** — Roll out to Kubernetes (optional)
7. **Post Deployment Validation** — Wait for K8s rollout (when deploying)
8. **Start App For Smoke Tests** — Run via Docker Compose when not deploying to K8s
9. **Trigger Selenium Tests** — Execute smoke test suite against the running app
10. **Generate Report** — Publish Allure test report
11. **Notify** — Send email notification on completion (optional)

### Jenkins Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `APP_URL` | `http://localhost:8080` | Base URL for smoke tests |
| `DOCKER_REGISTRY` | _(empty)_ | Optional registry prefix for image push |
| `RUN_DEPLOY` | `false` | Deploy to Kubernetes after image build |
| `SEND_NOTIFICATION` | `false` | Send email notification on completion |

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.3, Spring Security, Thymeleaf
- **Testing:** JUnit, JaCoCo, Selenium 4, TestNG, Allure
- **DevOps:** Jenkins, Docker, Docker Compose, Kubernetes
- **Build:** Maven (multi-module)

## Prerequisites

- Java 21
- Maven 3.9+
- Docker and Docker Compose (for containerized runs)
- kubectl (optional, for Kubernetes deployment)
- Chrome browser (for local Selenium test runs)

## Getting Started

### Run the application locally

```bash
mvn -pl employee-app spring-boot:run
```

The app starts at [http://localhost:8080](http://localhost:8080).

### Run with Docker Compose

```bash
docker compose up -d --build
```

Stop the containers:

```bash
docker compose down
```

### Default login credentials

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN |

## Running Tests

### Unit tests (employee-app)

```bash
mvn clean test -pl employee-app -am
```

JaCoCo coverage report: `employee-app/target/site/jacoco/index.html`

### Smoke tests (automation-tests)

Start the application first, then run:

```bash
mvn test -pl automation-tests \
  -DskipTests=false \
  -DsuiteXmlFile=SmokeSuite.xml \
  -Dbase.url=http://localhost:8080 \
  -Dheadless=true
```

Smoke test suite includes:

- Login
- Home page
- Employee search

Allure report: `automation-tests/target/site/allure-maven-plugin/index.html`

## Kubernetes Deployment

Apply the manifests:

```bash
kubectl apply -f k8s/deployment.yaml
```

The deployment exposes the app on port 80 within the cluster (target port 8080).

## License

This project is a proof-of-concept for demonstration and learning purposes.
