package com.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.domain.enums.OrderStatus;
import com.orderservice.domain.enums.PaymentMethod;
import com.orderservice.dto.request.CreateOrderRequest;
import com.orderservice.dto.request.OrderItemRequest;
import com.orderservice.dto.response.OrderResponse;
import com.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for OrderController.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser
    void createOrder_Success() throws Exception {
        // Arrange
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .productId(1L)
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("50.00"))
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .userId(1L)
                .items(Arrays.asList(itemRequest))
                .shippingAddress("123 Test St")
                .billingAddress("123 Test St")
                .customerEmail("test@example.com")
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderNumber("ORD-123456")
                .userId(1L)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(new BigDecimal("100.00"))
                .build();

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-123456"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser
    void createOrder_InvalidRequest() throws Exception {
        // Arrange - request with missing required fields
        CreateOrderRequest request = CreateOrderRequest.builder()
                .userId(1L)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getOrderById_Success() throws Exception {
        // Arrange
        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderNumber("ORD-123456")
                .userId(1L)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(new BigDecimal("100.00"))
                .build();

        when(orderService.getOrderById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-123456"));
    }
}
