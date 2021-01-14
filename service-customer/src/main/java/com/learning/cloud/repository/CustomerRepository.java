package com.learning.cloud.repository;

import com.learning.cloud.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerRepository {
    private List<Customer> customers = new ArrayList<>();

    public Customer add(Customer customer) {
        customer.setId((long) customers.size() + 1);
        customers.add(customer);
        return customer;
    }

    public Customer update(Customer customer) {
        return customers.set(customer.getId().intValue() - 1, customer);
    }

    public void delete(Long id) {
        customers.remove(id - 1);
    }

    public Optional<Customer> findById(Long id) {
        return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Customer> findByName(String name) {
        return customers.stream().filter(c -> c.getName().equals(name)).findFirst();
    }

    public List<Customer> findByIds(List<Long> ids) {
        return customers.stream().filter(c -> ids.contains(c.getId())).collect(Collectors.toList());
    }

}
