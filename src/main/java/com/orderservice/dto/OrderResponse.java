package com.orderservice.dto;

import com.orderservice.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing order details")
public class OrderResponse {

    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Order number", example = "ORD-2024-0001")
    private String orderNumber;

    @Schema(description = "User ID", example = "123")
    private Long userId;

    @Schema(description = "Order status", example = "CONFIRMED")
    private OrderStatus status;

    @Schema(description = "Total amount", example = "149.99")
    private BigDecimal totalAmount;

    @Schema(description = "Subtotal", example = "129.99")
    private BigDecimal subtotal;

    @Schema(description = "Tax amount", example = "10.00")
    private BigDecimal taxAmount;

    @Schema(description = "Shipping cost", example = "10.00")
    private BigDecimal shippingCost;

    @Schema(description = "Discount amount", example = "0.00")
    private BigDecimal discountAmount;

    @Schema(description = "Shipping address")
    private String shippingAddress;

    @Schema(description = "Billing address")
    private String billingAddress;

    @Schema(description = "Customer email")
    private String customerEmail;

    @Schema(description = "Customer phone")
    private String customerPhone;

    @Schema(description = "Order notes")
    private String notes;

    @Schema(description = "Estimated delivery date")
    private LocalDateTime estimatedDeliveryDate;

    @Schema(description = "Shipped date")
    private LocalDateTime shippedAt;

    @Schema(description = "Delivered date")
    private LocalDateTime deliveredAt;

    @Schema(description = "Cancelled date")
    private LocalDateTime cancelledAt;

    @Schema(description = "Cancellation reason")
    private String cancellationReason;

    @Schema(description = "List of order items")
    private List<OrderItemResponse> items;

    @Schema(description = "Payment information")
    private PaymentInfoResponse paymentInfo;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
