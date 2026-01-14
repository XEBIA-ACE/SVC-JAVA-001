package com.orderservice.mapper;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.domain.entity.PaymentInfo;
import com.orderservice.dto.request.CreateOrderRequest;
import com.orderservice.dto.request.OrderItemRequest;
import com.orderservice.dto.response.OrderItemResponse;
import com.orderservice.dto.response.OrderResponse;
import com.orderservice.dto.response.PaymentInfoResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper component for converting between entities and DTOs.
 */
@Component
public class OrderMapper {

    /**
     * Converts CreateOrderRequest to Order entity.
     */
    public Order toEntity(CreateOrderRequest request) {
        Order order = Order.builder()
                .userId(request.getUserId())
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .notes(request.getNotes())
                .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
                .shippingAmount(request.getShippingAmount() != null ? request.getShippingAmount() : BigDecimal.ZERO)
                .discountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO)
                .build();

        // Map order items
        List<OrderItem> items = request.getItems().stream()
                .map(this::toOrderItemEntity)
                .collect(Collectors.toList());

        items.forEach(order::addItem);

        return order;
    }

    /**
     * Converts OrderItemRequest to OrderItem entity.
     */
    public OrderItem toOrderItemEntity(OrderItemRequest request) {
        BigDecimal discountAmount = request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal taxAmount = request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal subtotal = request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        BigDecimal totalPrice = subtotal.add(taxAmount).subtract(discountAmount);

        return OrderItem.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .productSku(request.getProductSku())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .totalPrice(totalPrice)
                .build();
    }

    /**
     * Converts Order entity to OrderResponse DTO.
     */
    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .taxAmount(order.getTaxAmount())
                .shippingAmount(order.getShippingAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.calculateFinalAmount())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .notes(order.getNotes())
                .items(order.getItems().stream()
                        .map(this::toOrderItemResponse)
                        .collect(Collectors.toList()))
                .paymentInfo(order.getPaymentInfo() != null ? toPaymentInfoResponse(order.getPaymentInfo()) : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .cancelledAt(order.getCancelledAt())
                .cancellationReason(order.getCancellationReason())
                .build();
    }

    /**
     * Converts OrderItem entity to OrderItemResponse DTO.
     */
    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productSku(item.getProductSku())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discountAmount(item.getDiscountAmount())
                .taxAmount(item.getTaxAmount())
                .totalPrice(item.getTotalPrice())
                .createdAt(item.getCreatedAt())
                .build();
    }

    /**
     * Converts PaymentInfo entity to PaymentInfoResponse DTO.
     */
    public PaymentInfoResponse toPaymentInfoResponse(PaymentInfo paymentInfo) {
        return PaymentInfoResponse.builder()
                .id(paymentInfo.getId())
                .paymentMethod(paymentInfo.getPaymentMethod())
                .paymentStatus(paymentInfo.getPaymentStatus())
                .amount(paymentInfo.getAmount())
                .transactionId(paymentInfo.getTransactionId())
                .paymentGateway(paymentInfo.getPaymentGateway())
                .cardLastFour(paymentInfo.getCardLastFour())
                .cardBrand(paymentInfo.getCardBrand())
                .paidAt(paymentInfo.getPaidAt())
                .refundedAt(paymentInfo.getRefundedAt())
                .refundAmount(paymentInfo.getRefundAmount())
                .refundReason(paymentInfo.getRefundReason())
                .createdAt(paymentInfo.getCreatedAt())
                .updatedAt(paymentInfo.getUpdatedAt())
                .build();
    }

    /**
     * Converts list of Order entities to list of OrderResponse DTOs.
     */
    public List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
