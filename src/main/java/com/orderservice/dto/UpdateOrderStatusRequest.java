package com.orderservice.dto;

import com.orderservice.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating order status")
public class UpdateOrderStatusRequest {

    @NotNull(message = "Order status is required")
    @Schema(description = "New order status", example = "CONFIRMED")
    private OrderStatus status;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Schema(description = "Status update notes", example = "Order confirmed and payment received")
    private String notes;
}
