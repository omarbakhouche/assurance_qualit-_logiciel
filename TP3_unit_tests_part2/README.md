# TP3: Persistence and Integration Testing with Testcontainers

**Author:** Bakhouche Omar  
**Specialty:** RSD (Networks and Distributed Systems)

---

## Project Overview
This project demonstrates the transition from mocked unit tests to **real-world integration testing** using **Testcontainers**. By spinning up a live **MySQL 8.0** instance within a Docker container, we validate JPA mappings, database constraints, and repository logic in an environment that mirrors production.

## Key Components

| Module | Entities | Description |
| :--- | :--- | :--- |
| **Exo 1** | `User` | Basic JPA persistence and CRUD operations. |
| **Exo 2** | `Order` | Full 3-layer integration (Controller -> Service -> Repository). |
| **Exo 3** | `Product` | Advanced persistence scenarios and data integrity validation. |
| **Task Manager** | `Task` | Complete task management system with status-based filtering. |

## Technologies Used
*   **Java 17+**
*   **Spring Boot 3** (Data JPA, Web)
*   **MySQL 8.0** (Production-grade database engine)
*   **Testcontainers** (Docker-based containerized testing)
*   **JUnit 5 & AssertJ**

## Getting Started

### 1. Prerequisites
*   **Docker Desktop** must be running to manage the container lifecycle.
*   **Maven** installed for dependency management and execution.

### 2. Local Development
To run the MySQL database locally for manual testing:
```bash
docker compose up -d