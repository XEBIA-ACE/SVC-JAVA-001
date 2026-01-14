package com.orderservice.repository;

import com.orderservice.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OrderItem entity operations.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find all items for a specific order.
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Find all orders containing a specific product.
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * Calculate total quantity sold for a specific product.
     */
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.productId = :productId")
    Long getTotalQuantitySoldByProductId(@Param("productId") Long productId);

    /**
     * Delete all items for a specific order.
     */
    void deleteByOrderId(Long orderId);
}
