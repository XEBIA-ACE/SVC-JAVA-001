package com.orderservice.service;

import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.exception.InsufficientInventoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for managing inventory operations.
 * Integrates with external Inventory Service for stock management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final RestTemplate restTemplate;

    @Value("${external.inventory-service.url}")
    private String inventoryServiceUrl;

    @Value("${external.inventory-service.api-key}")
    private String inventoryServiceApiKey;

    /**
     * Reserves inventory for all items in an order.
     * This is a placeholder for integration with external inventory service.
     */
    public void reserveInventory(Order order) {
        log.info("Reserving inventory for order: {}", order.getOrderNumber());

        for (OrderItem item : order.getItems()) {
            try {
                // TODO: Call external inventory service to reserve stock
                // Example:
                // HttpHeaders headers = new HttpHeaders();
                // headers.set("X-API-Key", inventoryServiceApiKey);
                // HttpEntity<ReservationRequest> request = new HttpEntity<>(reservationRequest, headers);
                // ResponseEntity<ReservationResponse> response = restTemplate.postForEntity(
                //     inventoryServiceUrl + "/reserve", request, ReservationResponse.class);

                log.debug("Reserved {} units of product {} for order {}",
                        item.getQuantity(), item.getProductId(), order.getOrderNumber());

                // Simulate inventory check
                if (item.getQuantity() > 1000) {
                    throw new InsufficientInventoryException(
                            item.getProductId(), item.getQuantity(), 1000);
                }
            } catch (Exception e) {
                log.error("Failed to reserve inventory for product: {}", item.getProductId(), e);
                throw new InsufficientInventoryException(
                        "Failed to reserve inventory for product: " + item.getProductName());
            }
        }

        log.info("Inventory reservation completed for order: {}", order.getOrderNumber());
    }

    /**
     * Releases reserved inventory for an order.
     * Called when an order is cancelled.
     */
    public void releaseInventory(Order order) {
        log.info("Releasing inventory for order: {}", order.getOrderNumber());

        for (OrderItem item : order.getItems()) {
            try {
                // TODO: Call external inventory service to release stock
                log.debug("Released {} units of product {} for order {}",
                        item.getQuantity(), item.getProductId(), order.getOrderNumber());
            } catch (Exception e) {
                log.error("Failed to release inventory for product: {}", item.getProductId(), e);
                // Continue releasing other items even if one fails
            }
        }

        log.info("Inventory release completed for order: {}", order.getOrderNumber());
    }
}
