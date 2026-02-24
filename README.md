# NariSurokkha – Women Safety & Emergency Response Platform

NariSurokkha is a **real-time women safety platform** built using **microservices architecture** to provide instant emergency response, live location tracking, incident reporting, and real-time communication.

The system is designed for **high scalability**, **fault tolerance**, and **event-driven processing** using modern backend and frontend technologies.

---

## Project Vision

The goal of NariSurokkha is to build a **production-grade distributed system** that enables:

- One-tap SOS emergency alerts
- Live GPS tracking during emergencies
- Trusted contact notifications
- Authority response dashboards
- Incident reporting and analytics
- Real-time chat support

---

## Tech Stack

### Frontend
- Next.js (App Router)
- TypeScript
- Tailwind CSS
- TanStack Query
- WebSockets
- Map Integration (Mapbox / Google Maps)

### Backend
- Spring Boot (Microservices)
- Spring Security (JWT)
- Kafka (Event Streaming)
- Redis (Caching + Real-time state)
- PostgreSQL (Database per service)

### DevOps
- Docker
- Docker Compose
- Kubernetes (planned)
- GitHub Actions (CI/CD planned)
- Prometheus + Grafana (monitoring planned)

---

## Architecture Overview

The system follows an **event-driven microservices architecture**.

Frontend (Next.js)
|
API Gateway
|
| Auth | User | SOS | Location | Notification | Incident |
| Chat | Analytics | Admin |

|
Kafka Event Bus
|
Redis Cache + PostgreSQL (per service)


---

## Microservices Breakdown

### 1. API Gateway
Handles:
- Routing
- Authentication validation
- Rate limiting

---

### 2. Auth Service
Handles:
- User authentication
- JWT access + refresh tokens
- OTP verification
- Role-based access

Database:
- users
- roles
- refresh_tokens

Cache:
- Redis (token blacklist, OTP storage)

---

### 3. User Service
Handles:
- User profile
- Trusted contacts
- Preferences

Database:
- user_profiles
- trusted_contacts

---

### 4. SOS Service (Core Service)
Handles:
- SOS trigger/cancel/resolve
- Active SOS state management
- Kafka event publishing

Database:
- sos_cases

Redis:
- Active SOS cache

Kafka Events:
- SOS_TRIGGERED
- SOS_CANCELLED
- SOS_RESOLVED

---

### 5. Location Service
Handles:
- Real-time GPS updates
- Live location streaming

Tech:
- Redis GEO
- WebSockets
- Kafka

---

### 6. Notification Service
Handles:
- Push notifications
- SMS alerts
- Email alerts

Kafka Consumers:
- sos.events
- incident.events

Future integrations:
- Firebase Cloud Messaging
- Twilio

---

### 7. Incident Service
Handles:
- Harassment reporting
- Evidence uploads
- Anonymous reports

Database:
- incidents
- attachments

---

### 8. Chat Service
Handles:
- Real-time messaging during SOS
- Authority communication

Tech:
- WebSockets
- Redis Pub/Sub
- Kafka persistence

---

### 9. Analytics Service
Handles:
- SOS heatmaps
- Incident trends
- Response metrics

Tech:
- Kafka Streams
- Aggregated PostgreSQL tables

---

### 10. Admin Service
Handles:
- System monitoring
- User moderation
- Incident management

---

## Event-Driven Design

Kafka Topics:


sos.events
location.updates
incident.events
chat.messages
analytics.events


Example Event:

```json
{
  "eventType": "SOS_TRIGGERED",
  "userId": "uuid",
  "sosId": "uuid",
  "timestamp": "ISO"
}
```

## Database Strategy

Each microservice owns its **independent database** to ensure loose coupling and scalability.

### Rules

- **No cross-service joins**  
  Services must communicate through APIs or events (Kafka), never direct database joins.

- **UUID primary keys**  
  All tables use UUIDs to avoid ID collisions across distributed services.

- **Soft deletes where required**  
  Records should use flags like `is_deleted` or `deleted_at` instead of hard deletes when audit/history is needed.

- **Event-based communication only**  
  All cross-service data flow must happen via Kafka events or REST APIs.

---

## Redis Usage

Redis is used as a **high-speed in-memory store** for real-time and transient data.

### Use Cases

- **Active SOS state caching**  
  Stores currently active SOS sessions for fast lookup.

- **OTP storage**  
  Temporary storage with TTL for authentication verification.

- **Rate limiting**  
  Prevents abuse of sensitive endpoints (e.g., SOS trigger, login attempts).

- **WebSocket presence**  
  Tracks online users and active real-time sessions.

- **Geo-location storage**  
  Uses Redis GEO commands for fast location updates and proximity queries.

---

## Local Development Setup

### Prerequisites

- Java 17+
- Docker
- PostgreSQL
- Redis
- Kafka

---

### Run Infrastructure (Docker)

```bash
docker-compose up -d
```

## Run Backend Services

Example:

```bash
cd sos-service
./mvnw spring-boot:run
```

Repeat the same process for each microservice.

Run Frontend 
```bash
cd frontend
npm install
npm run dev
```
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sos_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

KAFKA_BOOTSTRAP_SERVERS=localhost:9092

REDIS_HOST=localhost
REDIS_PORT=6379
```

## Development Roadmap

### Phase 1 – Core MVP
- Auth Service
- User Service
- SOS Service
- Location Service
- Notification Service

### Phase 2 – Communication
- Chat Service
- Incident Reporting
- Admin Dashboard

### Phase 3 – Intelligence
- Analytics Engine
- Heatmaps
- AI Risk Prediction

---

## Security Design
- JWT Authentication
- Role-based access control (RBAC)
- Rate limiting
- Audit logging
- HTTPS enforcement (production)

---

## Failure Handling Strategy

| Failure                 | Solution                   |
|-------------------------|----------------------------|
| Kafka Down              | Outbox Pattern             |
| Redis Down              | DB fallback                |
| Duplicate Events        | Idempotent consumers       |
| Notification Failure    | Retry + DLQ                |

---

## Future Improvements
- Offline SOS via SMS fallback
- Voice-triggered SOS
- Smartwatch integration
- AI-based risk alerts
- Multi-language support (Bangla first)

---

## Why This Project Matters
This project demonstrates:

- Distributed systems architecture
- Event-driven microservices
- Real-time processing
- Scalable backend design
- Production-grade system patterns  