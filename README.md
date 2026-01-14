# Order Management Service

A production-ready microservice for handling order processing, inventory checks, and order lifecycle management built with Spring Boot.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Monitoring](#monitoring)
- [Deployment](#deployment)

## Overview

The Order Management Service is a business microservice that handles the complete order lifecycle from creation to delivery or cancellation. It integrates with external services for inventory management, payment processing, and customer notifications.

### Key Responsibilities

- **Order Creation and Validation**: Accept and validate new orders with comprehensive input validation
- **Order Status Management**: Track orders through their complete lifecycle
- **Inventory Reservation**: Coordinate with inventory service to reserve stock
- **Payment Integration**: Process payments through external payment gateway
- **Order History Tracking**: Maintain complete audit trail of order changes
- **Notification Triggers**: Send notifications for order events

## Features

- **RESTful API** with comprehensive endpoint coverage
- **Clean Architecture** with clear separation of concerns (Controller → Service → Repository)
- **SOLID Principles** applied throughout the codebase
- **Database Migrations** with Flyway for version control
- **OpenAPI/Swagger Documentation** for interactive API exploration
- **Spring Security** configuration with authentication placeholders
- **Comprehensive Validation** using Jakarta Bean Validation
- **Global Exception Handling** with consistent error responses
- **Structured Logging** with SLF4J and Logback
- **Health Checks and Metrics** via Spring Boot Actuator
- **Prometheus Integration** for metrics collection
- **Docker Support** with multi-stage builds
- **Unit and Integration Tests** with high coverage

## Technology Stack

- **Java 17**: Latest LTS version
- **Spring Boot 3.2.1**: Application framework
- **Spring Data JPA**: Data access layer
- **Hibernate**: ORM framework
- **MySQL 8.0**: Primary database
- **Flyway**: Database migration tool
- **Spring Security**: Security framework
- **Lombok**: Reduce boilerplate code
- **SpringDoc OpenAPI**: API documentation
- **Maven**: Build and dependency management
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework
- **Docker**: Containerization
- **Prometheus**: Metrics collection
- **Grafana**: Metrics visualization

## Architecture

### Layer Structure

```
┌─────────────────────────────────────────┐
│         Controller Layer                │
│  (REST API, Request/Response DTOs)      │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│  (Business Logic, Orchestration)        │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│  (Data Access, JPA Repositories)        │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Database Layer                 │
│        (MySQL Database)                 │
└─────────────────────────────────────────┘
```

### Domain Model

```
Order (1) ──────► (N) OrderItem
  │
  │ (1:1)
  ↓
PaymentInfo
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0
- Docker and Docker Compose (optional)

### Installation

1. **Clone the repository**

```bash
git clone <repository-url>
cd order-management-service
```

2. **Configure environment variables**

Copy the example environment file and update with your values:

```bash
cp .env.example .env
```

Edit `.env` file with your database and service credentials.

3. **Set up the database**

Create the MySQL database:

```sql
CREATE DATABASE order_management;
CREATE USER 'order_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON order_management.* TO 'order_user'@'localhost';
FLUSH PRIVILEGES;
```

4. **Build the application**

```bash
mvn clean install
```

## API Documentation

### Available Endpoints

#### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create a new order |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders` | Get all orders (paginated) |
| GET | `/api/orders/user/{userId}` | Get orders by user ID |
| PUT | `/api/orders/{id}/status` | Update order status |
| POST | `/api/orders/{id}/cancel` | Cancel an order |
| GET | `/api/orders/{id}/items` | Get order items |

#### Health & Monitoring

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Service health check |
| GET | `/api/status` | Service status information |
| GET | `/actuator/health` | Actuator health endpoint |
| GET | `/actuator/metrics` | Metrics endpoint |
| GET | `/actuator/prometheus` | Prometheus metrics |

### Interactive API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI specification available at:

```
http://localhost:8080/v3/api-docs
```

### Example API Requests

#### Create Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {
        "productId": 101,
        "productName": "Laptop",
        "productSku": "LAP-001",
        "quantity": 1,
        "unitPrice": 1299.99,
        "discountAmount": 100.00,
        "taxAmount": 120.00
      }
    ],
    "shippingAddress": "123 Main St, City, State 12345",
    "billingAddress": "123 Main St, City, State 12345",
    "customerEmail": "customer@example.com",
    "customerPhone": "+1234567890",
    "taxAmount": 120.00,
    "shippingAmount": 10.00,
    "discountAmount": 100.00,
    "paymentMethod": "CREDIT_CARD"
  }'
```

#### Get Order by ID

```bash
curl -X GET http://localhost:8080/api/orders/1
```

#### Cancel Order

```bash
curl -X POST http://localhost:8080/api/orders/1/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "reason": "Customer requested cancellation"
  }'
```

## Database Schema

### Orders Table

```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL,
    tax_amount DECIMAL(19,2) DEFAULT 0.00,
    shipping_amount DECIMAL(19,2) DEFAULT 0.00,
    discount_amount DECIMAL(19,2) DEFAULT 0.00,
    shipping_address VARCHAR(500) NOT NULL,
    billing_address VARCHAR(500) NOT NULL,
    customer_email VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP NULL,
    cancellation_reason VARCHAR(500)
);
```

### Order Items Table

```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_sku VARCHAR(100),
    quantity INT NOT NULL,
    unit_price DECIMAL(19,2) NOT NULL,
    discount_amount DECIMAL(19,2) DEFAULT 0.00,
    tax_amount DECIMAL(19,2) DEFAULT 0.00,
    total_price DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
```

### Payment Info Table

```sql
CREATE TABLE payment_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    payment_method VARCHAR(30) NOT NULL,
    payment_status VARCHAR(30) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    payment_gateway VARCHAR(50),
    card_last_four VARCHAR(4),
    card_brand VARCHAR(20),
    paid_at TIMESTAMP NULL,
    refunded_at TIMESTAMP NULL,
    refund_amount DECIMAL(19,2),
    refund_reason VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
```

## Configuration

### Environment Variables

Key environment variables (see `.env.example` for complete list):

```env
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=order_management
DB_USERNAME=order_user
DB_PASSWORD=your_password

# Application
APP_PORT=8080
APP_ENV=dev

# External Services
PAYMENT_GATEWAY_URL=http://localhost:8081/api/payments
INVENTORY_SERVICE_URL=http://localhost:8082/api/inventory
NOTIFICATION_SERVICE_URL=http://localhost:8083/api/notifications
```

### Application Profiles

- **dev**: Development environment with verbose logging
- **prod**: Production environment with optimized settings
- **test**: Test environment with H2 in-memory database

Activate a profile:

```bash
java -jar target/order-management-service-1.0.0.jar --spring.profiles.active=prod
```

## Running the Application

### Local Development

```bash
# Using Maven
mvn spring-boot:run

# Using Java
mvn clean package
java -jar target/order-management-service-1.0.0.jar
```

### Using Docker Compose

```bash
# Start all services (app + MySQL + monitoring)
docker-compose up -d

# View logs
docker-compose logs -f order-service

# Stop all services
docker-compose down
```

### Building Docker Image

```bash
# Build the image
docker build -t order-management-service:1.0.0 .

# Run the container
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=3306 \
  order-management-service:1.0.0
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Classes

```bash
# Unit tests
mvn test -Dtest=OrderServiceTest

# Integration tests
mvn test -Dtest=OrderControllerTest
```

### Test Coverage

Generate test coverage report:

```bash
mvn clean verify
# Report available at: target/site/jacoco/index.html
```

### Test Structure

```
src/test/java/
├── com/orderservice/
│   ├── controller/     # Controller integration tests
│   ├── service/        # Service unit tests
│   └── repository/     # Repository integration tests
```

## Monitoring

### Actuator Endpoints

Access health and metrics:

```bash
# Health check
curl http://localhost:8080/actuator/health

# Metrics
curl http://localhost:8080/actuator/metrics

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus
```

### Prometheus

Access Prometheus UI at: `http://localhost:9090`

### Grafana

Access Grafana dashboard at: `http://localhost:3000`
- Default credentials: admin/admin

## Deployment

### Production Deployment Checklist

1. **Update application-prod.yml** with production settings
2. **Configure secure database credentials**
3. **Set up SSL/TLS certificates**
4. **Enable JWT authentication** in SecurityConfig
5. **Configure external service endpoints**
6. **Set up monitoring and alerting**
7. **Configure log aggregation**
8. **Set up database backups**
9. **Configure auto-scaling policies**
10. **Set up CI/CD pipeline**

### Kubernetes Deployment (Example)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order-management-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: order-service
  template:
    metadata:
      labels:
        app: order-service
    spec:
      containers:
      - name: order-service
        image: order-management-service:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: host
```

## Best Practices Implemented

- **Clean Architecture**: Clear separation of layers
- **SOLID Principles**: Single responsibility, dependency inversion
- **Design Patterns**: Builder, Repository, Factory
- **Error Handling**: Global exception handling with consistent responses
- **Validation**: Input validation at API boundary
- **Logging**: Structured logging with appropriate levels
- **Security**: Input sanitization, prepared statements (SQL injection prevention)
- **Testing**: Unit and integration tests with high coverage
- **Documentation**: Comprehensive inline and API documentation
- **Observability**: Health checks, metrics, and monitoring

## Future Enhancements

- [ ] Implement JWT authentication
- [ ] Add event-driven architecture with Kafka/RabbitMQ
- [ ] Implement CQRS pattern for read/write separation
- [ ] Add Redis caching for frequently accessed data
- [ ] Implement rate limiting
- [ ] Add API versioning
- [ ] Implement soft delete for orders
- [ ] Add order search and filtering capabilities
- [ ] Implement webhook notifications
- [ ] Add multi-currency support

## License

This project is licensed under the MIT License.

## Support

For issues, questions, or contributions, please contact:
- Email: support@orderservice.com
- GitHub: [Create an issue](https://github.com/your-org/order-management-service/issues)

---

**Built with ❤️ using Spring Boot**
