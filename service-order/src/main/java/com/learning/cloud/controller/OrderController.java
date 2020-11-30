package com.learning.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.client.AccountClient;
import com.learning.cloud.client.CustomerClient;
import com.learning.cloud.client.ProductClient;
import com.learning.cloud.entity.*;
import com.learning.cloud.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private AccountClient accountClient;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderRepository orderRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Order prepare(@RequestBody Order order) throws JsonProcessingException {
        int price = 0;
        List<Product> products = productClient.findByIds(order.getProductId());
        log.info("Products found: {}", objectMapper.writeValueAsString(products));
        Customer customer = customerClient.findByIdWithAccounts(order.getCustomerId());
        log.info("Customer found: {}", objectMapper.writeValueAsString(customer));
        for (Product product : products)
            price += product.getPrice();
        final int priceDiscounted = priceDiscount(price, customer);
        log.info("Discounted price: {}", objectMapper.writeValueAsString(Collections.singletonMap("price", priceDiscounted)));
        Optional<Account> account = customer.getAccounts().stream().filter(a -> a.getBalance() > priceDiscounted).findFirst();
        if(account.isPresent()) {
            order.setAccountId(account.get().getId());
            order.setOrderStatus(OrderStatus.ACCEPTED);
            order.setPrice(priceDiscounted);
            log.info("Account found: {}", objectMapper.writeValueAsString(account.get()));
        } else {
            order.setOrderStatus(OrderStatus.REJECTED);
            log.info("Account not found: {}", objectMapper.writeValueAsString(customer.getAccounts()));
        }
        return orderRepository.add(order);
    }

    @PutMapping("/{id}")
    public Order accept(@PathVariable Long id) throws JsonProcessingException {
        final Order order = orderRepository.findById(id);
        log.info("Order found: {}", objectMapper.writeValueAsString(order));
        accountClient.withdraw(order.getAccountId(), order.getPrice());
        HashMap<String, Object> logger = new HashMap<>();
        logger.put("accountId", order.getAccountId());
        logger.put("price", order.getPrice());
        log.info("Account modified: {}", objectMapper.writeValueAsString(logger));
        order.setOrderStatus(OrderStatus.DONE);
        log.info("Order status changed: {}", objectMapper.writeValueAsString(Collections.singletonMap("status", order.getOrderStatus())));
        orderRepository.update(order);
        return order;
    }

    private int priceDiscount(int price, Customer customer) {
        double discount = 0;
        switch(customer.getCustomerType()) {
            case REGULER:
                discount += 0.05;
                break;
            case VIP:
                discount += 0.1;
                break;
            default:
                break;
        }
        int totalOrderCount = orderRepository.countByCustomerId(customer.getId());
        discount += totalOrderCount * 0.01;
        return (int) (price - (price * discount));
    }
}
