package com.orderservice.repository;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity operations.
 * Provides CRUD operations and custom queries for order management.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find an order by its unique order number.
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find all orders for a specific user with pagination.
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Find all orders for a specific user with a given status.
     */
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Find orders by status with pagination.
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find orders created within a date range.
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find orders with items eagerly loaded to avoid N+1 query problem.
     */
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    /**
     * Find orders with payment info eagerly loaded.
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.paymentInfo WHERE o.id = :orderId")
    Optional<Order> findByIdWithPaymentInfo(@Param("orderId") Long orderId);

    /**
     * Find all orders for a user with items eagerly loaded.
     */
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.userId = :userId")
    List<Order> findByUserIdWithItems(@Param("userId") Long userId);

    /**
     * Count orders by status for a specific user.
     */
    long countByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Check if an order number already exists.
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Find pending orders older than specified date.
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createdAt < :cutoffDate")
    List<Order> findStalePendingOrders(@Param("cutoffDate") LocalDateTime cutoffDate);
}
