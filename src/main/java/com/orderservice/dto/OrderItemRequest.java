package com.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating an order item")
public class OrderItemRequest {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    @Schema(description = "Product ID", example = "1")
    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Schema(description = "Product name", example = "Laptop")
    private String productName;

    @Size(max = 100, message = "Product SKU must not exceed 100 characters")
    @Schema(description = "Product SKU", example = "LAP-001")
    private String productSku;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000, message = "Quantity must not exceed 1000")
    @Schema(description = "Quantity", example = "2")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Price per unit", example = "999.99")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Discount must be non-negative")
    @Schema(description = "Discount amount", example = "50.00")
    private BigDecimal discount;

    @DecimalMin(value = "0.0", message = "Tax must be non-negative")
    @Schema(description = "Tax amount", example = "20.00")
    private BigDecimal tax;
}
