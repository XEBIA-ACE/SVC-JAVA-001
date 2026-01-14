package com.orderservice.service;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.PaymentInfo;
import com.orderservice.domain.enums.OrderStatus;
import com.orderservice.domain.enums.PaymentStatus;
import com.orderservice.exception.PaymentProcessingException;
import com.orderservice.repository.OrderRepository;
import com.orderservice.repository.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for managing payment operations.
 * Integrates with external Payment Gateway for transaction processing.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentInfoRepository paymentInfoRepository;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${external.payment-gateway.url}")
    private String paymentGatewayUrl;

    @Value("${external.payment-gateway.api-key}")
    private String paymentGatewayApiKey;

    /**
     * Processes payment for an order.
     * This is a placeholder for integration with external payment gateway.
     */
    @Transactional
    public void processPayment(Order order) {
        log.info("Processing payment for order: {}", order.getOrderNumber());

        PaymentInfo paymentInfo = order.getPaymentInfo();
        if (paymentInfo == null) {
            throw new PaymentProcessingException("Payment information not found for order: " + order.getOrderNumber());
        }

        try {
            // TODO: Call external payment gateway to process payment
            // Example:
            // HttpHeaders headers = new HttpHeaders();
            // headers.set("X-API-Key", paymentGatewayApiKey);
            // PaymentRequest paymentRequest = createPaymentRequest(order);
            // HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);
            // ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
            //     paymentGatewayUrl + "/charge", request, PaymentResponse.class);

            // Simulate payment processing
            String transactionId = "TXN-" + UUID.randomUUID().toString();

            paymentInfo.setTransactionId(transactionId);
            paymentInfo.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentInfo.setPaymentGateway("MockGateway");
            paymentInfo.setPaidAt(LocalDateTime.now());
            paymentInfo.setPaymentGatewayResponse("Payment successful");

            // Simulate card details (only last 4 digits should be stored)
            paymentInfo.setCardLastFour("1234");
            paymentInfo.setCardBrand("VISA");

            paymentInfoRepository.save(paymentInfo);

            // Update order status
            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
            orderRepository.save(order);

            log.info("Payment processed successfully for order: {} with transaction ID: {}",
                    order.getOrderNumber(), transactionId);

        } catch (Exception e) {
            log.error("Payment processing failed for order: {}", order.getOrderNumber(), e);

            paymentInfo.setPaymentStatus(PaymentStatus.FAILED);
            paymentInfo.setPaymentGatewayResponse("Payment failed: " + e.getMessage());
            paymentInfoRepository.save(paymentInfo);

            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);

            throw new PaymentProcessingException("Payment processing failed", e);
        }
    }

    /**
     * Processes refund for a cancelled order.
     */
    @Transactional
    public void processRefund(Order order) {
        log.info("Processing refund for order: {}", order.getOrderNumber());

        PaymentInfo paymentInfo = order.getPaymentInfo();
        if (paymentInfo == null || !paymentInfo.isRefundable()) {
            throw new PaymentProcessingException("Order is not eligible for refund: " + order.getOrderNumber());
        }

        try {
            // TODO: Call external payment gateway to process refund

            paymentInfo.setPaymentStatus(PaymentStatus.REFUNDED);
            paymentInfo.setRefundAmount(paymentInfo.getAmount());
            paymentInfo.setRefundedAt(LocalDateTime.now());
            paymentInfo.setRefundReason(order.getCancellationReason());

            paymentInfoRepository.save(paymentInfo);

            order.setStatus(OrderStatus.REFUNDED);
            orderRepository.save(order);

            log.info("Refund processed successfully for order: {}", order.getOrderNumber());

        } catch (Exception e) {
            log.error("Refund processing failed for order: {}", order.getOrderNumber(), e);
            throw new PaymentProcessingException("Refund processing failed", e);
        }
    }
}
