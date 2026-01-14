package com.orderservice.dto.request;

import com.orderservice.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for creating a new order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address must not exceed 500 characters")
    private String shippingAddress;

    @NotBlank(message = "Billing address is required")
    @Size(max = 500, message = "Billing address must not exceed 500 characters")
    private String billingAddress;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String customerEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String customerPhone;

    @DecimalMin(value = "0.0", inclusive = true, message = "Tax amount must be non-negative")
    @Digits(integer = 17, fraction = 2, message = "Invalid tax amount format")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Shipping amount must be non-negative")
    @Digits(integer = 17, fraction = 2, message = "Invalid shipping amount format")
    private BigDecimal shippingAmount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be non-negative")
    @Digits(integer = 17, fraction = 2, message = "Invalid discount amount format")
    private BigDecimal discountAmount;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
