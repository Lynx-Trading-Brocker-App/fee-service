# Fee Service

## Overview

The Fee Service is responsible for calculating and recording platform fees for executed orders within the broker platform.

It operates as an **internal microservice**, meaning it is not exposed to frontend clients and is only accessible by other services (e.g., Order Service).

---

## Features

* Platform fee calculation based on configurable rate
* Total cost calculation (price × quantity + fees)
* Internal API secured via API key
* Global error handling
* PostgreSQL integration (Dockerized)

---

## Tech Stack

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA / Hibernate
* PostgreSQL
* Docker
* JUnit 5 + Mockito
* Testcontainers

---

## Setup

### 1. Start Database (Docker)

```bash
docker compose up -d
```

Database will be available at:

```
localhost:5433
```

---

### 2. Run Application

```bash
./gradlew bootRun
```

---

## Configuration

Example `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/fee_db
    username: user
    password: password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

fee:
  rate: 0.01

internal:
  api-key: secret123
```

---

## Security

The service is protected using a simple **internal API key** mechanism.

All endpoints require the following header:

```
X-INTERNAL-KEY: <your-api-key>
```

If the key is invalid, the service returns:

```
403 FORBIDDEN
```

---

## API Endpoints

### 1. Calculate Fee

**POST /internal/fees/calculate**

Calculates platform fee and total cost **without persisting data**.

#### Request

```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "platformUserId": "user-123",
  "price": 100,
  "quantity": 2,
  "exchangeFee": 1
}
```

#### Response

```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "platformFee": 2.00,
  "totalCost": 203.00
}
```

---

### 2. Record Fee

**POST /internal/fees/record**

Stores a fee entry in the ledger.

#### Request

```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "platformUserId": "user-123",
  "price": 100,
  "quantity": 2,
  "exchangeFee": 1
}
```

#### Response

```
200 OK
```

---

## Fee Calculation Logic

* **Base amount** = `price × quantity`
* **Platform fee** = `base × fee.rate`
* **Total cost** = `base + exchangeFee + platformFee`

---

## Database

The service uses PostgreSQL.

### Table: `fee_ledger`

| Column           | Type      | Description              |
| ---------------- | --------- | ------------------------ |
| id               | UUID      | Primary key              |
| order_id         | UUID      | Order identifier         |
| platform_user_id | TEXT      | User identifier          |
| amount           | DECIMAL   | price × quantity         |
| exchange_fee     | DECIMAL   | External exchange fee    |
| platform_fee     | DECIMAL   | Platform fee             |
| created_at       | TIMESTAMP | Auto-generated timestamp |

---

## Error Handling

All errors follow a consistent structure:

```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Description",
    "details": {}
  }
}
```

### Common Errors

| Status | Code             | Description             |
| ------ | ---------------- | ----------------------- |
| 400    | VALIDATION_ERROR | Invalid input           |
| 403    | FORBIDDEN        | Invalid API key         |
| 404    | FEE_NOT_FOUND    | Fee not found           |
| 500    | INTERNAL_ERROR   | Unexpected server error |

---

## Testing

Run all tests:

```bash
./gradlew test
```

### Unit Tests

* Service layer
* Uses Mockito

### Integration Tests

* Repository layer
* Uses Testcontainers with PostgreSQL


