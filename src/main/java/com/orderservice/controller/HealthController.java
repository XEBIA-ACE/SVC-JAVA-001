package com.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for health check and status endpoints.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "Health check and service status endpoints")
public class HealthController {

    @Operation(summary = "Health check", description = "Returns the health status of the service")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Order Management Service");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Service status", description = "Returns detailed service information")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Order Management Service");
        response.put("version", "1.0.0");
        response.put("status", "OPERATIONAL");
        response.put("timestamp", LocalDateTime.now());
        response.put("uptime", "Available");
        return ResponseEntity.ok(response);
    }
}
