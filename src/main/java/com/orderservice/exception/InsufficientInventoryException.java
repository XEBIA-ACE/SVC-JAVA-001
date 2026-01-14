package com.orderservice.exception;

/**
 * Exception thrown when there is insufficient inventory for an order.
 */
public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(String message) {
        super(message);
    }

    public InsufficientInventoryException(Long productId, int requested, int available) {
        super(String.format("Insufficient inventory for product %d. Requested: %d, Available: %d",
                productId, requested, available));
    }
}
