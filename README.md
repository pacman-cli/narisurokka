# Nari Surokkha (Women's Safety) 🛡️
A comprehensive microservices-based backend and real-time frontend platform dedicated to women's safety. Built with Spring Boot, Next.js, Kafka, and WebSockets.
## 🚀 Key Features
- **Real-Time Location Tracking:** Uses WebSockets to stream location updates.
- **Emergency SOS System:** Trigger instant alerts to designated emergency contacts.
- **Event-Driven Architecture:** Apache Kafka ensures decoupled, resilient incident and location processing.
- **Incident Reporting:** Safely log and report safety incidents.
## 🏗️ Architecture
This project implements a scalable microservices architecture:
- `api-gateway`: Handles edge traffic, routing, and aggregate rate limiting.
- `auth-service`: JWT token generation, security, and registration.
- `user-service`: Profiles, emergency contacts.
- `location-service`: Active coordinate tracking via WebSockets + Kafka.
- `notification-service`: Dispatches multi-channel alerts (SMS, Email).
- `sos-service`: Initiates and coordinates the emergency broadcast loop.
### C4 High-Level Pipeline
`[Next.js Client] -> [API Gateway (8080)] -> [Target Microservice]`
`[SOS Service] -> (Kafka Topic: sos-triggered) -> [Notification / Location Services]`
## 🛠️ Tech Stack
- **Backend:** Java 17+, Spring Boot 3+, Spring Cloud Gateway.
- **Frontend:** Next.js, React, TailwindCSS, TypeScript.
- **Infra:** PostgreSQL, Redis, Apache Kafka, Zookeeper, Docker.
## ⚙️ Getting Started
### Prerequisites
- Docker & Docker Compose
- Java 17 (if running outside Docker)
- Node.js 18+ (for frontend)
### Bootstrapping Infrastructure
Start all databases, cache, and message brokers via Docker Compose:
```bash
docker-compose up -d
```
### Services Included in `docker-compose.yml`
- **PostgreSQL (5432):** Contains logical DBs for each service (`narisurokkha_auth`, etc.)
- **Redis (6379):** Distributed caching.
- **Kafka & Zookeeper (9092, 2181):** Event streaming.
- **pgAdmin (5050):** `admin@narisurokkha.com` / `admin`
- **Kafka UI (8086):** Observe Kafka topics.
## 📖 Best Practices Enforced
- **OOP & SOLID Principles:** Clear encapsulation of domain rules, separating Controllers, Services, and Data structures.
- **DRY (Don't Repeat Yourself):** Shared DTOs and utilities via common configurations.
- **Resilience:** Designed to withstand microservice outages via async Kafka channels.
