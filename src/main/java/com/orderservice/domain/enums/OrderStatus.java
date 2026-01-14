package com.orderservice.domain.enums;

/**
 * Enum representing the various states an order can be in throughout its lifecycle.
 */
public enum OrderStatus {
    PENDING("Order has been created and is awaiting processing"),
    CONFIRMED("Order has been confirmed and inventory reserved"),
    PAYMENT_PENDING("Waiting for payment confirmation"),
    PAYMENT_COMPLETED("Payment has been successfully processed"),
    PAYMENT_FAILED("Payment processing failed"),
    PROCESSING("Order is being prepared for shipment"),
    SHIPPED("Order has been shipped to the customer"),
    DELIVERED("Order has been delivered to the customer"),
    CANCELLED("Order has been cancelled"),
    REFUNDED("Order has been refunded");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
