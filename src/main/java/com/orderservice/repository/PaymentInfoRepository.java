package com.orderservice.repository;

import com.orderservice.domain.entity.PaymentInfo;
import com.orderservice.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PaymentInfo entity operations.
 */
@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

    /**
     * Find payment information by order ID.
     */
    Optional<PaymentInfo> findByOrderId(Long orderId);

    /**
     * Find payment by transaction ID.
     */
    Optional<PaymentInfo> findByTransactionId(String transactionId);

    /**
     * Find all payments with a specific status.
     */
    List<PaymentInfo> findByPaymentStatus(PaymentStatus paymentStatus);

    /**
     * Find pending payments older than specified date.
     */
    @Query("SELECT p FROM PaymentInfo p WHERE p.paymentStatus = 'PENDING' AND p.createdAt < :cutoffDate")
    List<PaymentInfo> findStalePendingPayments(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Check if a transaction ID already exists.
     */
    boolean existsByTransactionId(String transactionId);
}
