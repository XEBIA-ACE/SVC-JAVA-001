package com.orderservice.enums;

/**
 * Enum representing payment status
 */
public enum PaymentStatus {
    PENDING("Payment is pending"),
    PROCESSING("Payment is being processed"),
    COMPLETED("Payment completed successfully"),
    FAILED("Payment failed"),
    REFUNDED("Payment has been refunded"),
    CANCELLED("Payment cancelled");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
