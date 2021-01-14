package com.learning.cloud.controller.v1.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.client.IAccountServiceClient;
import com.learning.cloud.client.ICustomerServiceClient;
import com.learning.cloud.client.IProductServiceClient;
import com.learning.cloud.controller.v1.IOrderController;
import com.learning.cloud.model.*;
import com.learning.cloud.exception.custom.ResourceNotFoundException;
import com.learning.cloud.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController implements IOrderController {
    private final IAccountServiceClient iAccountServiceClient;
    private final ICustomerServiceClient iCustomerServiceClient;
    private final IProductServiceClient iProductServiceClient;
    private final OrderRepository orderRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<Void> prepare(Order order) throws JsonProcessingException {
        int price = 0;
        List<Product> products = iProductServiceClient.findByIds(order.getProductId());
        if(products.isEmpty()) throw new ResourceNotFoundException("Requested product with ID " + order.getProductId() + " is not available!");
        log.info("Products found: {}", objectMapper.writeValueAsString(products));
        Customer customer = iCustomerServiceClient.findByIdWithAccounts(order.getCustomerId());
        log.info("Customer found: {}", objectMapper.writeValueAsString(customer));
        for (Product product : products)
            price += product.getPrice();
        final int priceDiscounted = priceDiscount(price, customer);
        log.info("Discounted price: {}", objectMapper.writeValueAsString(Collections.singletonMap("price", priceDiscounted)));
        Optional<Account> account = customer.getAccounts().stream().filter(a -> a.getBalance() > priceDiscounted).findFirst();
        if (account.isPresent()) {
            order.setAccountId(account.get().getId());
            order.setOrderStatus(OrderStatus.ACCEPTED);
            order.setPrice(priceDiscounted);
            log.info("Account found: {}", objectMapper.writeValueAsString(account.get()));
        } else {
            order.setOrderStatus(OrderStatus.REJECTED);
            log.info("Account not found: {}", objectMapper.writeValueAsString(customer.getAccounts()));
            throw new ResourceNotFoundException("Customer with ID " + order.getCustomerId()+ " does not have enough balance in any of their account to order these products!");
        }
        Order newOrder = orderRepository.add(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newOrder.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Order> accept(Long id) throws JsonProcessingException {
        final Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order with ID " + id + " not found!"));
        log.info("Order found: {}", objectMapper.writeValueAsString(order));
        iAccountServiceClient.withdraw(order.getAccountId(), order.getPrice());
        HashMap<String, Object> logger = new HashMap<>();
        logger.put("accountId", order.getAccountId());
        logger.put("price", order.getPrice());
        log.info("Account modified: {}", objectMapper.writeValueAsString(logger));
        order.setOrderStatus(OrderStatus.DONE);
        log.info("Order status changed: {}", objectMapper.writeValueAsString(Collections.singletonMap("status", order.getOrderStatus())));
        return ResponseEntity.ok().body(orderRepository.update(order));
    }

    private int priceDiscount(int price, Customer customer) {
        double discount = 0;
        switch (customer.getCustomerType()) {
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
