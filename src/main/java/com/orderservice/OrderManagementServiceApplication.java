package com.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Order Management Service.
 *
 * This microservice handles:
 * - Order creation and validation
 * - Order status management
 * - Inventory reservation
 * - Payment integration
 * - Order history tracking
 * - Notification triggers
 */
@SpringBootApplication
public class OrderManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementServiceApplication.class, args);
    }
}
