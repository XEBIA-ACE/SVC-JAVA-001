package com.orderservice.enums;

/**
 * Enum representing the lifecycle states of an order
 */
public enum OrderStatus {
    PENDING("Order has been created and awaiting processing"),
    CONFIRMED("Order has been confirmed and payment verified"),
    PROCESSING("Order is being prepared"),
    SHIPPED("Order has been shipped"),
    DELIVERED("Order has been delivered to customer"),
    CANCELLED("Order has been cancelled"),
    REFUNDED("Order has been refunded"),
    FAILED("Order processing failed");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
