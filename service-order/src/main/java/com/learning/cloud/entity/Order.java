package com.learning.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;

    @NotNull(message = "Order price is mandatory")
    @Positive(message = "Order price must be positive")
    private int price;

    private OrderStatus orderStatus;

    @NotNull(message = "Customer ID is mandatory")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotNull(message = "Account ID is mandatory")
    @Positive(message = "Account ID must be positive")
    private Long accountId;

    @NotNull(message = "Product ID is mandatory")
    @NotEmpty(message = "Should have at-least one product ID")
    private List<Long> productId;
}
