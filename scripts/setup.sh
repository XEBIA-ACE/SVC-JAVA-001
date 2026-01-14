#!/bin/bash

# Order Management Service Setup Script
# This script sets up the development environment

set -e

echo "========================================="
echo "Order Management Service Setup"
echo "========================================="
echo ""

# Check Java installation
echo "Checking Java installation..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        echo "✓ Java $JAVA_VERSION is installed"
    else
        echo "✗ Java 17 or higher is required"
        exit 1
    fi
else
    echo "✗ Java is not installed"
    exit 1
fi

# Check Maven installation
echo "Checking Maven installation..."
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1 | awk '{print $3}')
    echo "✓ Maven $MVN_VERSION is installed"
else
    echo "✗ Maven is not installed"
    exit 1
fi

# Check MySQL installation
echo "Checking MySQL installation..."
if command -v mysql &> /dev/null; then
    echo "✓ MySQL is installed"
else
    echo "⚠ MySQL is not installed (optional if using Docker)"
fi

# Create .env file if it doesn't exist
echo "Setting up environment variables..."
if [ ! -f .env ]; then
    cp .env.example .env
    echo "✓ Created .env file from .env.example"
    echo "⚠ Please update .env with your configuration"
else
    echo "✓ .env file already exists"
fi

# Create logs directory
echo "Creating logs directory..."
mkdir -p logs
echo "✓ Logs directory created"

# Install dependencies
echo "Installing Maven dependencies..."
mvn clean install -DskipTests
echo "✓ Dependencies installed"

# Run database migrations (optional)
read -p "Do you want to run database migrations? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Running database migrations..."
    mvn flyway:migrate
    echo "✓ Database migrations completed"
fi

echo ""
echo "========================================="
echo "Setup completed successfully!"
echo "========================================="
echo ""
echo "Next steps:"
echo "1. Update .env file with your configuration"
echo "2. Start the application with: mvn spring-boot:run"
echo "3. Access Swagger UI at: http://localhost:8080/swagger-ui.html"
echo ""
