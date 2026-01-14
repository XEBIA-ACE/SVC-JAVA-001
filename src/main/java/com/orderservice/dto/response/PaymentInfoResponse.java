package com.orderservice.dto.response;

import com.orderservice.domain.enums.PaymentMethod;
import com.orderservice.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for payment information response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoResponse {

    private Long id;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private String transactionId;
    private String paymentGateway;
    private String cardLastFour;
    private String cardBrand;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
    private BigDecimal refundAmount;
    private String refundReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
