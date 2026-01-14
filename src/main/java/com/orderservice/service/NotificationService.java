package com.orderservice.service;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for sending notifications to customers and administrators.
 * Integrates with external Notification Service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final RestTemplate restTemplate;

    @Value("${external.notification-service.url}")
    private String notificationServiceUrl;

    @Value("${external.notification-service.api-key}")
    private String notificationServiceApiKey;

    /**
     * Sends order confirmation notification to customer.
     */
    public void sendOrderConfirmation(Order order) {
        log.info("Sending order confirmation for order: {}", order.getOrderNumber());

        try {
            // TODO: Call external notification service to send email/SMS
            // Example notification payload:
            // {
            //   "recipient": order.getCustomerEmail(),
            //   "template": "order_confirmation",
            //   "data": {
            //     "orderNumber": order.getOrderNumber(),
            //     "totalAmount": order.calculateFinalAmount(),
            //     "items": order.getItems()
            //   }
            // }

            log.debug("Order confirmation sent to: {}", order.getCustomerEmail());
        } catch (Exception e) {
            log.error("Failed to send order confirmation for order: {}", order.getOrderNumber(), e);
            // Don't throw exception - notification failure shouldn't block order creation
        }
    }

    /**
     * Sends order status update notification.
     */
    public void sendOrderStatusUpdate(Order order, OrderStatus oldStatus) {
        log.info("Sending status update notification for order: {} from {} to {}",
                order.getOrderNumber(), oldStatus, order.getStatus());

        try {
            // TODO: Call external notification service
            log.debug("Status update notification sent to: {}", order.getCustomerEmail());
        } catch (Exception e) {
            log.error("Failed to send status update notification for order: {}", order.getOrderNumber(), e);
        }
    }

    /**
     * Sends order cancellation notification.
     */
    public void sendOrderCancellation(Order order) {
        log.info("Sending cancellation notification for order: {}", order.getOrderNumber());

        try {
            // TODO: Call external notification service
            log.debug("Cancellation notification sent to: {}", order.getCustomerEmail());
        } catch (Exception e) {
            log.error("Failed to send cancellation notification for order: {}", order.getOrderNumber(), e);
        }
    }

    /**
     * Sends payment failure notification.
     */
    public void sendPaymentFailure(Order order) {
        log.info("Sending payment failure notification for order: {}", order.getOrderNumber());

        try {
            // TODO: Call external notification service
            log.debug("Payment failure notification sent to: {}", order.getCustomerEmail());
        } catch (Exception e) {
            log.error("Failed to send payment failure notification for order: {}", order.getOrderNumber(), e);
        }
    }
}
