# Quick Start Guide - Order Management Service

## Prerequisites

- Java 17 (Note: The project uses Java 17. If you have Java 25, you may need to use SDKMAN or similar to install Java 17)
- Maven 3.8+
- MySQL 8.0 or use Docker Compose
- Docker and Docker Compose (optional, but recommended)

## Option 1: Quick Start with Docker Compose (Recommended)

This is the fastest way to get the service running with all dependencies.

```bash
# 1. Clone or navigate to the project directory
cd order-management-service

# 2. Copy environment file
cp .env.example .env

# 3. Start all services (MySQL + Application)
docker-compose up -d

# 4. Check logs
docker-compose logs -f order-service

# 5. Wait for application to start (about 30-60 seconds)
# Look for: "Started OrderManagementServiceApplication"

# 6. Access the application
# Swagger UI: http://localhost:8080/swagger-ui.html
# Health Check: http://localhost:8080/actuator/health
```

## Option 2: Local Development Setup

### Step 1: Setup MySQL Database

```bash
# Start MySQL (if using Docker)
docker run --name mysql-order-db \
  -e MYSQL_ROOT_PASSWORD=rootpass \
  -e MYSQL_DATABASE=order_management \
  -e MYSQL_USER=order_user \
  -e MYSQL_PASSWORD=order_pass \
  -p 3306:3306 \
  -d mysql:8.0

# Or create database manually if MySQL is already running
mysql -u root -p
CREATE DATABASE order_management;
CREATE USER 'order_user'@'localhost' IDENTIFIED BY 'order_pass';
GRANT ALL PRIVILEGES ON order_management.* TO 'order_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Step 2: Configure Environment

```bash
# Copy the example environment file
cp .env.example .env

# Edit .env file with your settings (or use defaults)
# The default values should work for local development
```

### Step 3: Build and Run

```bash
# Build the application
mvn clean package -DskipTests

# Run the application
java -jar target/order-management-service-1.0.0.jar

# Or use Maven Spring Boot plugin
mvn spring-boot:run
```

### Step 4: Verify Installation

```bash
# Check health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}

# Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Testing the API

### 1. Create an Order

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
        "quantity": 2,
        "price": 999.99,
        "discount": 50.00,
        "tax": 20.00
      }
    ],
    "shippingAddress": "123 Main St, City, State 12345",
    "billingAddress": "123 Main St, City, State 12345",
    "customerEmail": "customer@example.com",
    "customerPhone": "+1234567890",
    "taxAmount": 40.00,
    "shippingCost": 15.00,
    "discountAmount": 100.00
  }'
```

### 2. Get Order by ID

```bash
curl http://localhost:8080/api/orders/1
```

### 3. Get Orders by User

```bash
curl http://localhost:8080/api/orders/user/1
```

### 4. Update Order Status

```bash
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "CONFIRMED",
    "notes": "Payment verified and order confirmed"
  }'
```

### 5. Cancel Order

```bash
curl -X POST http://localhost:8080/api/orders/1/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "reason": "Customer requested cancellation"
  }'
```

### 6. Get Order Items

```bash
curl http://localhost:8080/api/orders/1/items
```

## Accessing API Documentation

Once the application is running, you can access:

### Swagger UI (Interactive API Documentation)
```
http://localhost:8080/swagger-ui.html
```

This provides an interactive interface to:
- View all available endpoints
- Test API calls directly from the browser
- See request/response schemas
- View example requests

### OpenAPI JSON Specification
```
http://localhost:8080/v3/api-docs
```

## Monitoring and Health Checks

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Application Info
```bash
curl http://localhost:8080/actuator/info
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Prometheus Metrics
```bash
curl http://localhost:8080/actuator/prometheus
```

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=OrderServiceTest

# Run tests with coverage
mvn clean verify

# View coverage report
open target/site/jacoco/index.html
```

## Troubleshooting

### Issue: Port 8080 already in use
```bash
# Find and kill process using port 8080
lsof -ti:8080 | xargs kill -9

# Or change port in .env file
APP_PORT=8081
```

### Issue: Cannot connect to MySQL
```bash
# Check if MySQL is running
docker ps | grep mysql

# Check MySQL logs
docker logs mysql-order-db

# Verify connection
mysql -h localhost -u order_user -p order_management
```

### Issue: Flyway migration errors
```bash
# Reset database (WARNING: This will delete all data)
mysql -u root -p
DROP DATABASE order_management;
CREATE DATABASE order_management;
EXIT;

# Restart application
```

### Issue: Java version mismatch
```bash
# Check Java version
java -version

# Should show Java 17. If not, install Java 17:
# Using SDKMAN (recommended)
curl -s "https://get.sdkman.io" | bash
sdk install java 17.0.10-tem
sdk use java 17.0.10-tem
```

## Stopping the Application

### If using Docker Compose:
```bash
docker-compose down

# To also remove volumes (database data):
docker-compose down -v
```

### If running locally:
```bash
# Press Ctrl+C in the terminal running the application

# Or find and kill the process
ps aux | grep order-management
kill -9 <PID>
```

## Development Workflow

1. **Make code changes**
2. **Run tests**: `mvn test`
3. **Build**: `mvn clean package`
4. **Run locally**: `mvn spring-boot:run`
5. **Test API**: Use Swagger UI or curl commands
6. **Check logs**: `tail -f logs/order-service.log`

## Next Steps

1. **Explore the API**: Use Swagger UI to test all endpoints
2. **Review the code**: Check out the clean architecture structure
3. **Run tests**: Verify everything works with `mvn test`
4. **Customize**: Modify configuration in `application.yml`
5. **Extend**: Add new features following the existing patterns

## Useful Commands

```bash
# Build without tests
mvn clean package -DskipTests

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Debug mode
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Build Docker image
docker build -t order-management-service:1.0.0 .

# Run Docker container
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  order-management-service:1.0.0

# View logs in real-time
tail -f logs/order-service.log

# Clean up everything
mvn clean
docker-compose down -v
rm -rf target/
```

## Support

For issues or questions:
- Check the main README.md for detailed documentation
- Review PROJECT_STRUCTURE.md for code organization
- Open an issue on GitHub
- Contact: support@orderservice.com

---

**Happy Coding! 🚀**
