package com.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new order")
public class CreateOrderRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @Schema(description = "User ID", example = "123")
    private Long userId;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    @Schema(description = "List of order items")
    private List<OrderItemRequest> items;

    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address must not exceed 500 characters")
    @Schema(description = "Shipping address", example = "123 Main St, City, State 12345")
    private String shippingAddress;

    @NotBlank(message = "Billing address is required")
    @Size(max = 500, message = "Billing address must not exceed 500 characters")
    @Schema(description = "Billing address", example = "123 Main St, City, State 12345")
    private String billingAddress;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Customer email", example = "customer@example.com")
    private String customerEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Schema(description = "Customer phone", example = "+1234567890")
    private String customerPhone;

    @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
    @Schema(description = "Tax amount", example = "15.00")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Shipping cost must be non-negative")
    @Schema(description = "Shipping cost", example = "10.00")
    private BigDecimal shippingCost;

    @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
    @Schema(description = "Discount amount", example = "5.00")
    private BigDecimal discountAmount;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Schema(description = "Order notes", example = "Please deliver between 9 AM - 5 PM")
    private String notes;
}
