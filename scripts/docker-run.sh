#!/bin/bash

# Docker Run Script for Order Management Service

set -e

echo "========================================="
echo "Starting Order Management Service"
echo "========================================="
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "✗ Docker is not installed"
    exit 1
fi

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "✗ docker-compose is not installed"
    exit 1
fi

# Start services
echo "Starting services with Docker Compose..."
docker-compose up -d

echo ""
echo "Waiting for services to be healthy..."
sleep 10

# Check service health
echo "Checking service health..."
if curl -s http://localhost:8080/api/health > /dev/null; then
    echo "✓ Order Management Service is running"
else
    echo "⚠ Service might still be starting up..."
fi

echo ""
echo "========================================="
echo "Services started successfully!"
echo "========================================="
echo ""
echo "Available services:"
echo "- Order Management API: http://localhost:8080"
echo "- Swagger UI: http://localhost:8080/swagger-ui.html"
echo "- MySQL: localhost:3306"
echo "- Prometheus: http://localhost:9090"
echo "- Grafana: http://localhost:3000 (admin/admin)"
echo ""
echo "To view logs: docker-compose logs -f order-service"
echo "To stop services: docker-compose down"
echo ""
