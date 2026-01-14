package com.orderservice.repository;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for OrderRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .orderNumber("ORD-TEST-001")
                .userId(1L)
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("100.00"))
                .shippingAddress("123 Test St")
                .billingAddress("123 Test St")
                .customerEmail("test@example.com")
                .build();
    }

    @Test
    void saveAndFindById_Success() {
        // Act
        Order saved = orderRepository.save(testOrder);
        Optional<Order> found = orderRepository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getOrderNumber()).isEqualTo("ORD-TEST-001");
        assertThat(found.get().getUserId()).isEqualTo(1L);
    }

    @Test
    void findByOrderNumber_Success() {
        // Arrange
        orderRepository.save(testOrder);

        // Act
        Optional<Order> found = orderRepository.findByOrderNumber("ORD-TEST-001");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(1L);
    }

    @Test
    void findByUserId_Success() {
        // Arrange
        orderRepository.save(testOrder);

        Order anotherOrder = Order.builder()
                .orderNumber("ORD-TEST-002")
                .userId(1L)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(new BigDecimal("200.00"))
                .shippingAddress("456 Test Ave")
                .billingAddress("456 Test Ave")
                .customerEmail("test2@example.com")
                .build();
        orderRepository.save(anotherOrder);

        // Act
        var orders = orderRepository.findByUserIdAndStatus(1L, OrderStatus.PENDING);

        // Assert
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void existsByOrderNumber_Success() {
        // Arrange
        orderRepository.save(testOrder);

        // Act
        boolean exists = orderRepository.existsByOrderNumber("ORD-TEST-001");
        boolean notExists = orderRepository.existsByOrderNumber("ORD-INVALID");

        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
