package com.orderservice.service;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.PaymentInfo;
import com.orderservice.domain.enums.OrderStatus;
import com.orderservice.domain.enums.PaymentMethod;
import com.orderservice.domain.enums.PaymentStatus;
import com.orderservice.dto.request.CancelOrderRequest;
import com.orderservice.dto.request.CreateOrderRequest;
import com.orderservice.dto.request.OrderItemRequest;
import com.orderservice.dto.response.OrderResponse;
import com.orderservice.exception.InvalidOrderStateException;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest createOrderRequest;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .productId(1L)
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("50.00"))
                .build();

        createOrderRequest = CreateOrderRequest.builder()
                .userId(1L)
                .items(Arrays.asList(itemRequest))
                .shippingAddress("123 Test St")
                .billingAddress("123 Test St")
                .customerEmail("test@example.com")
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        order = Order.builder()
                .id(1L)
                .orderNumber("ORD-123456")
                .userId(1L)
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("100.00"))
                .build();

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .id(1L)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(new BigDecimal("100.00"))
                .build();
        order.setPaymentInfo(paymentInfo);

        orderResponse = OrderResponse.builder()
                .id(1L)
                .orderNumber("ORD-123456")
                .userId(1L)
                .status(OrderStatus.CONFIRMED)
                .build();
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(orderMapper.toEntity(any(CreateOrderRequest.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);
        doNothing().when(inventoryService).reserveInventory(any(Order.class));
        doNothing().when(paymentService).processPayment(any(Order.class));
        doNothing().when(notificationService).sendOrderConfirmation(any(Order.class));

        // Act
        OrderResponse result = orderService.createOrder(createOrderRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrderNumber()).isEqualTo("ORD-123456");
        verify(orderRepository).save(any(Order.class));
        verify(inventoryService).reserveInventory(any(Order.class));
        verify(notificationService).sendOrderConfirmation(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        // Arrange
        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        // Act
        OrderResponse result = orderService.getOrderById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrderNumber()).isEqualTo("ORD-123456");
        verify(orderRepository).findByIdWithItems(1L);
    }

    @Test
    void getOrderById_NotFound() {
        // Arrange
        when(orderRepository.findByIdWithItems(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById(999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found with ID: 999");
    }

    @Test
    void cancelOrder_Success() {
        // Arrange
        order.setStatus(OrderStatus.CONFIRMED);
        CancelOrderRequest cancelRequest = CancelOrderRequest.builder()
                .reason("Customer request")
                .build();

        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);
        doNothing().when(inventoryService).releaseInventory(any(Order.class));
        doNothing().when(notificationService).sendOrderCancellation(any(Order.class));

        // Act
        OrderResponse result = orderService.cancelOrder(1L, cancelRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(inventoryService).releaseInventory(order);
        verify(notificationService).sendOrderCancellation(order);
    }

    @Test
    void cancelOrder_InvalidState() {
        // Arrange
        order.setStatus(OrderStatus.DELIVERED);
        CancelOrderRequest cancelRequest = CancelOrderRequest.builder()
                .reason("Customer request")
                .build();

        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThatThrownBy(() -> orderService.cancelOrder(1L, cancelRequest))
                .isInstanceOf(InvalidOrderStateException.class)
                .hasMessageContaining("cannot be cancelled");
    }
}
