package com.learning.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private int price;
    private OrderStatus orderStatus;
    private Long customerId;
    private Long accountId;
    private List<Long> productId;
}
