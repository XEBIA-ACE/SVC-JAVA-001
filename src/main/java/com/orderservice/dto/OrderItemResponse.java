package com.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for order item details")
public class OrderItemResponse {

    @Schema(description = "Order item ID", example = "1")
    private Long id;

    @Schema(description = "Product ID", example = "101")
    private Long productId;

    @Schema(description = "Product name", example = "Laptop")
    private String productName;

    @Schema(description = "Product SKU", example = "LAP-001")
    private String productSku;

    @Schema(description = "Quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Price per unit", example = "999.99")
    private BigDecimal price;

    @Schema(description = "Discount amount", example = "50.00")
    private BigDecimal discount;

    @Schema(description = "Tax amount", example = "20.00")
    private BigDecimal tax;

    @Schema(description = "Total amount", example = "1949.98")
    private BigDecimal total;
}
