package com.learning.cloud.repository;

import com.learning.cloud.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private List<Order> orders = new ArrayList<>();

    public Order add(Order order) {
        order.setId((long) orders.size() + 1);
        orders.add(order);
        return order;
    }

    public Order update(Order order) {
        orders.set(order.getId().intValue() - 1, order);
        return order;
    }

    public void delete(Order order) {
        orders.remove(order.getId().intValue() - 1);
    }

    public List<Order> find(List<Long> ids) {
        return orders.stream().filter(o -> ids.contains(o.getId())).collect(Collectors.toList());
    }

    public Optional<Order> findById(Long id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    public int countByCustomerId(Long id) {
        return (int) orders.stream().filter(o -> o.getCustomerId().equals(id)).count();
    }
}
