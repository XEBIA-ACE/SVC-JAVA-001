package com.orderservice.service;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.domain.entity.PaymentInfo;
import com.orderservice.domain.enums.OrderStatus;
import com.orderservice.domain.enums.PaymentStatus;
import com.orderservice.dto.request.CancelOrderRequest;
import com.orderservice.dto.request.CreateOrderRequest;
import com.orderservice.dto.request.UpdateOrderStatusRequest;
import com.orderservice.dto.response.OrderItemResponse;
import com.orderservice.dto.response.OrderResponse;
import com.orderservice.exception.InvalidOrderStateException;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing order operations.
 * Handles order creation, status updates, cancellation, and retrieval.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    /**
     * Creates a new order with inventory reservation and payment processing.
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for user: {}", request.getUserId());

        // Map request to entity
        Order order = orderMapper.toEntity(request);

        // Generate unique order number
        order.setOrderNumber(generateOrderNumber());

        // Calculate total amount
        BigDecimal totalAmount = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        // Reserve inventory for all items
        try {
            inventoryService.reserveInventory(order);
            log.info("Inventory reserved for order: {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to reserve inventory for order: {}", order.getOrderNumber(), e);
            throw e;
        }

        // Create payment info
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .paymentMethod(request.getPaymentMethod())
                .amount(order.calculateFinalAmount())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        order.setPaymentInfo(paymentInfo);

        // Update order status
        order.setStatus(OrderStatus.CONFIRMED);

        // Save order
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully: {}", savedOrder.getOrderNumber());

        // Initiate payment processing asynchronously
        try {
            paymentService.processPayment(savedOrder);
        } catch (Exception e) {
            log.error("Payment processing failed for order: {}", savedOrder.getOrderNumber(), e);
            // Payment failure is handled asynchronously
        }

        // Send order confirmation notification
        notificationService.sendOrderConfirmation(savedOrder);

        return orderMapper.toResponse(savedOrder);
    }

    /**
     * Retrieves an order by ID.
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        log.debug("Fetching order with ID: {}", orderId);
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toResponse(order);
    }

    /**
     * Retrieves an order by order number.
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        log.debug("Fetching order with number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("orderNumber", orderNumber));
        return orderMapper.toResponse(order);
    }

    /**
     * Retrieves all orders for a specific user.
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUserId(Long userId, Pageable pageable) {
        log.debug("Fetching orders for user: {}", userId);
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(orderMapper::toResponse);
    }

    /**
     * Retrieves all orders with pagination.
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.debug("Fetching all orders");
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::toResponse);
    }

    /**
     * Updates the status of an order.
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        log.info("Updating status for order ID: {} to {}", orderId, request.getStatus());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Validate status transition
        validateStatusTransition(order.getStatus(), request.getStatus());

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(request.getStatus());

        if (request.getNotes() != null) {
            order.setNotes(order.getNotes() != null
                ? order.getNotes() + "\n" + request.getNotes()
                : request.getNotes());
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated from {} to {} for order: {}",
                oldStatus, request.getStatus(), order.getOrderNumber());

        // Send status update notification
        notificationService.sendOrderStatusUpdate(updatedOrder, oldStatus);

        return orderMapper.toResponse(updatedOrder);
    }

    /**
     * Cancels an order.
     */
    @Transactional
    public OrderResponse cancelOrder(Long orderId, CancelOrderRequest request) {
        log.info("Cancelling order ID: {}", orderId);

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.isCancellable()) {
            throw new InvalidOrderStateException(
                    String.format("Order %s cannot be cancelled in status: %s",
                            order.getOrderNumber(), order.getStatus()));
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(request.getReason());
        order.setCancelledAt(LocalDateTime.now());

        // Release inventory
        inventoryService.releaseInventory(order);

        // Process refund if payment was completed
        if (order.getPaymentInfo() != null && order.getPaymentInfo().isPaymentCompleted()) {
            paymentService.processRefund(order);
        }

        Order cancelledOrder = orderRepository.save(order);
        log.info("Order cancelled successfully: {}", order.getOrderNumber());

        // Send cancellation notification
        notificationService.sendOrderCancellation(cancelledOrder);

        return orderMapper.toResponse(cancelledOrder);
    }

    /**
     * Retrieves all items for a specific order.
     */
    @Transactional(readOnly = true)
    public List<OrderItemResponse> getOrderItems(Long orderId) {
        log.debug("Fetching items for order ID: {}", orderId);
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return order.getItems().stream()
                .map(orderMapper::toOrderItemResponse)
                .toList();
    }

    /**
     * Generates a unique order number.
     */
    private String generateOrderNumber() {
        String prefix = "ORD";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return String.format("%s-%s-%s", prefix, timestamp, uuid);
    }

    /**
     * Validates order status transitions.
     */
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Define valid transitions
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException(
                    String.format("Cannot transition from %s to %s", currentStatus, newStatus));
        }

        if (currentStatus == OrderStatus.REFUNDED && newStatus != OrderStatus.REFUNDED) {
            throw new InvalidOrderStateException(
                    String.format("Cannot transition from %s to %s", currentStatus, newStatus));
        }

        log.debug("Status transition validated: {} -> {}", currentStatus, newStatus);
    }
}
