# Quick Start Guide

Get the Order Management Service up and running in 5 minutes.

## Prerequisites

Choose one of the following options:

### Option 1: Docker (Recommended)
- Docker Desktop installed
- Docker Compose installed

### Option 2: Local Development
- Java 17 or higher
- Maven 3.8+
- MySQL 8.0

## Quick Start with Docker (Fastest)

### 1. Clone and Navigate
```bash
cd order-management-service
```

### 2. Configure Environment
```bash
# Copy example environment file
cp .env.example .env

# The default values will work with Docker Compose
# No need to edit unless you want custom configuration
```

### 3. Start Everything
```bash
# Start all services (MySQL, API, Prometheus, Grafana)
docker-compose up -d

# Wait about 30 seconds for services to initialize
```

### 4. Verify It's Running
```bash
# Check health
curl http://localhost:8080/api/health

# Expected response:
# {"status":"UP","service":"Order Management Service","timestamp":"2026-01-14T..."}
```

### 5. Access the Application

**Swagger UI (Interactive API Documentation)**
```
http://localhost:8080/swagger-ui.html
```

**Sample API Requests**

Create an Order:
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {
        "productId": 101,
        "productName": "Laptop",
        "quantity": 1,
        "unitPrice": 999.99
      }
    ],
    "shippingAddress": "123 Main St, City, State 12345",
    "billingAddress": "123 Main St, City, State 12345",
    "customerEmail": "customer@example.com",
    "paymentMethod": "CREDIT_CARD"
  }'
```

Get All Orders:
```bash
curl http://localhost:8080/api/orders
```

### 6. Access Monitoring Tools

**Prometheus** (Metrics Collection)
```
http://localhost:9090
```

**Grafana** (Dashboards)
```
http://localhost:3000
Username: admin
Password: admin
```

### 7. View Logs
```bash
# Follow logs
docker-compose logs -f order-service

# View specific lines
docker-compose logs --tail=50 order-service
```

### 8. Stop Services
```bash
# Stop all services
docker-compose down

# Stop and remove all data
docker-compose down -v
```

## Quick Start with Local Development

### 1. Setup Database
```bash
# Login to MySQL
mysql -u root -p

# Create database and user
CREATE DATABASE order_management;
CREATE USER 'order_user'@'localhost' IDENTIFIED BY 'order_password';
GRANT ALL PRIVILEGES ON order_management.* TO 'order_user'@'localhost';
FLUSH PRIVILEGES;
exit;
```

### 2. Configure Environment
```bash
# Copy and edit environment file
cp .env.example .env

# Update these values in .env:
DB_HOST=localhost
DB_PORT=3306
DB_NAME=order_management
DB_USERNAME=order_user
DB_PASSWORD=order_password
```

### 3. Build and Run
```bash
# Install dependencies and run migrations
mvn clean install

# Start the application
mvn spring-boot:run
```

### 4. Verify It's Running
```bash
curl http://localhost:8080/api/health
```

### 5. Access Swagger UI
```
http://localhost:8080/swagger-ui.html
```

## Testing the API

### Using Swagger UI (Easiest)

1. Go to http://localhost:8080/swagger-ui.html
2. Click on "POST /api/orders"
3. Click "Try it out"
4. Edit the request body
5. Click "Execute"

### Using cURL

**Create an Order**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {
        "productId": 101,
        "productName": "Gaming Laptop",
        "productSku": "LAP-GAME-001",
        "quantity": 1,
        "unitPrice": 1299.99,
        "discountAmount": 100.00,
        "taxAmount": 120.00
      }
    ],
    "shippingAddress": "456 Oak St, Apt 2B, Springfield, IL 62701",
    "billingAddress": "456 Oak St, Apt 2B, Springfield, IL 62701",
    "customerEmail": "john.doe@example.com",
    "customerPhone": "+1234567890",
    "taxAmount": 120.00,
    "shippingAmount": 15.00,
    "discountAmount": 100.00,
    "paymentMethod": "CREDIT_CARD",
    "notes": "Please deliver between 9 AM - 5 PM"
  }'
```

**Get Order by ID**
```bash
curl http://localhost:8080/api/orders/1
```

**Get Orders for User**
```bash
curl http://localhost:8080/api/orders/user/1
```

**Update Order Status**
```bash
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "PROCESSING",
    "notes": "Order is being prepared for shipment"
  }'
```

**Cancel Order**
```bash
curl -X POST http://localhost:8080/api/orders/1/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "reason": "Customer requested cancellation due to address change"
  }'
```

**Get Order Items**
```bash
curl http://localhost:8080/api/orders/1/items
```

### Using Postman

1. Import the Swagger/OpenAPI spec:
   - Open Postman
   - Click Import
   - Enter: `http://localhost:8080/v3/api-docs`
   - Click Import

2. All endpoints will be available in Postman with examples

## Common Commands

### Docker Compose

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f

# Restart a service
docker-compose restart order-service

# Rebuild and restart
docker-compose up -d --build

# Check service status
docker-compose ps
```

### Maven

```bash
# Run application
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package

# Skip tests during build
mvn clean package -DskipTests

# Run specific test
mvn test -Dtest=OrderServiceTest

# Clean and rebuild
mvn clean install
```

### Application Management

```bash
# Check health
curl http://localhost:8080/api/health

# Check actuator health
curl http://localhost:8080/actuator/health

# View metrics
curl http://localhost:8080/actuator/metrics

# View specific metric
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus
```

## Default Ports

| Service | Port | URL |
|---------|------|-----|
| API | 8080 | http://localhost:8080 |
| Swagger UI | 8080 | http://localhost:8080/swagger-ui.html |
| MySQL | 3306 | localhost:3306 |
| Prometheus | 9090 | http://localhost:9090 |
| Grafana | 3000 | http://localhost:3000 |

## Environment Profiles

Switch between profiles:

```bash
# Development (verbose logging)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production (optimized)
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Testing (H2 database)
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

## Troubleshooting

### Application Won't Start

**Issue**: Port 8080 already in use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

**Issue**: Database connection failed
```bash
# Check MySQL is running
docker-compose ps mysql

# Check database credentials in .env file
cat .env
```

### Database Issues

**Issue**: Tables not created
```bash
# Run migrations manually
mvn flyway:migrate

# Check migration status
mvn flyway:info
```

**Issue**: Connection pool exhausted
```bash
# Check application logs
docker-compose logs order-service | grep "connection"

# Restart the service
docker-compose restart order-service
```

### Docker Issues

**Issue**: Container keeps restarting
```bash
# Check logs for errors
docker-compose logs order-service

# Check container status
docker-compose ps
```

**Issue**: Out of disk space
```bash
# Clean up Docker
docker system prune -a --volumes
```

## Next Steps

1. **Read the Documentation**
   - [README.md](README.md) - Complete documentation
   - [ARCHITECTURE.md](ARCHITECTURE.md) - Architecture details
   - [API Documentation](http://localhost:8080/swagger-ui.html) - Interactive API docs

2. **Explore the Code**
   - Check out the clean architecture
   - Review the service layer implementations
   - Examine the test cases

3. **Customize for Your Needs**
   - Add new endpoints
   - Implement JWT authentication
   - Add caching layer
   - Configure external services

4. **Deploy to Production**
   - Review [DEPLOYMENT_CHECKLIST.md](DEPLOYMENT_CHECKLIST.md)
   - Configure production environment
   - Set up monitoring and alerts

## Support

- **GitHub Issues**: Report bugs or request features
- **Documentation**: Check README.md for detailed information
- **Architecture**: See ARCHITECTURE.md for design details

## Quick Reference Card

```bash
# Start everything
docker-compose up -d

# View logs
docker-compose logs -f order-service

# Test API
curl http://localhost:8080/api/health

# Swagger UI
open http://localhost:8080/swagger-ui.html

# Stop everything
docker-compose down
```

---

**You're all set! Start building amazing order management features!** 🚀
