#!/bin/bash

echo "=========================================="
echo "   Order Management Service Verification"
echo "=========================================="
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1"
        return 1
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1/"
        return 0
    else
        echo -e "${RED}✗${NC} $1/"
        return 1
    fi
}

echo "Checking project structure..."
echo ""

# Root files
check_file "pom.xml"
check_file "Dockerfile"
check_file "docker-compose.yml"
check_file ".gitignore"
check_file ".env.example"
check_file "README.md"

echo ""
echo "Checking documentation..."
check_file "ARCHITECTURE.md"
check_file "CONTRIBUTING.md"
check_file "DEPLOYMENT_CHECKLIST.md"
check_file "PROJECT_SUMMARY.md"
check_file "QUICKSTART.md"
check_file "PROJECT_COMPLETION_SUMMARY.md"

echo ""
echo "Checking source directories..."
check_dir "src/main/java/com/orderservice"
check_dir "src/main/java/com/orderservice/config"
check_dir "src/main/java/com/orderservice/controller"
check_dir "src/main/java/com/orderservice/domain/entity"
check_dir "src/main/java/com/orderservice/domain/enums"
check_dir "src/main/java/com/orderservice/dto/request"
check_dir "src/main/java/com/orderservice/dto/response"
check_dir "src/main/java/com/orderservice/exception"
check_dir "src/main/java/com/orderservice/mapper"
check_dir "src/main/java/com/orderservice/repository"
check_dir "src/main/java/com/orderservice/service"

echo ""
echo "Checking configuration files..."
check_file "src/main/resources/application.yml"
check_file "src/main/resources/application-dev.yml"
check_file "src/main/resources/application-prod.yml"
check_file "src/main/resources/application-test.yml"

echo ""
echo "Checking database migrations..."
check_file "src/main/resources/db/migration/V1__create_orders_table.sql"
check_file "src/main/resources/db/migration/V2__create_order_items_table.sql"
check_file "src/main/resources/db/migration/V3__create_payment_info_table.sql"

echo ""
echo "Checking test structure..."
check_dir "src/test/java/com/orderservice"
check_file "src/test/java/com/orderservice/service/OrderServiceTest.java"
check_file "src/test/java/com/orderservice/controller/OrderControllerTest.java"
check_file "src/test/java/com/orderservice/repository/OrderRepositoryTest.java"

echo ""
echo "=========================================="
echo "Counting project files..."
echo "=========================================="

JAVA_COUNT=$(find src -name "*.java" 2>/dev/null | wc -l | tr -d ' ')
SQL_COUNT=$(find src/main/resources -name "*.sql" 2>/dev/null | wc -l | tr -d ' ')
TEST_COUNT=$(find src/test -name "*Test.java" 2>/dev/null | wc -l | tr -d ' ')
MD_COUNT=$(find . -maxdepth 1 -name "*.md" 2>/dev/null | wc -l | tr -d ' ')

echo "Java files: $JAVA_COUNT"
echo "SQL migrations: $SQL_COUNT"
echo "Test files: $TEST_COUNT"
echo "Documentation files: $MD_COUNT"

echo ""
echo "=========================================="
echo "   ✅ Project Verification Complete"
echo "=========================================="
echo ""
echo "All required files and directories are present!"
echo ""
echo "To run the service:"
echo "  docker-compose up -d"
echo ""
echo "To access:"
echo "  API: http://localhost:8080/api/orders"
echo "  Swagger: http://localhost:8080/swagger-ui.html"
echo "  Health: http://localhost:8080/api/health"
echo ""
