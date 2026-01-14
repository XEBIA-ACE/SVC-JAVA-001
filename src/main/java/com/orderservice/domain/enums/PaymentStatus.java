package com.orderservice.domain.enums;

/**
 * Enum representing the status of a payment transaction.
 */
public enum PaymentStatus {
    PENDING,
    AUTHORIZED,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED
}
