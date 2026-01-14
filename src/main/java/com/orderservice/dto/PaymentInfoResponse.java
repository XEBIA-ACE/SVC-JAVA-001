package com.orderservice.dto;

import com.orderservice.enums.PaymentMethod;
import com.orderservice.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for payment information")
public class PaymentInfoResponse {

    @Schema(description = "Payment ID", example = "1")
    private Long id;

    @Schema(description = "Transaction ID", example = "TXN-123456789")
    private String transactionId;

    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "Payment status", example = "COMPLETED")
    private PaymentStatus paymentStatus;

    @Schema(description = "Payment amount", example = "149.99")
    private BigDecimal amount;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Payment gateway", example = "Stripe")
    private String paymentGateway;

    @Schema(description = "Payment date")
    private LocalDateTime paymentDate;

    @Schema(description = "Refund amount", example = "0.00")
    private BigDecimal refundAmount;

    @Schema(description = "Refund date")
    private LocalDateTime refundDate;

    @Schema(description = "Payment details")
    private String paymentDetails;

    @Schema(description = "Failure reason")
    private String failureReason;
}
