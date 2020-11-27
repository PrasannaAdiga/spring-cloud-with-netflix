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

    public List<Customer> find(List<Long> ids) {
        return customers.stream().filter(c -> ids.contains(c.getId())).collect(Collectors.toList());
    }

    public Customer findById(Long id) {
        Optional<Customer> sCustomer = customers.stream().filter(c -> c.getId().equals(id)).findFirst();
        if(sCustomer.isPresent()) {
            return sCustomer.get();
        } else {
            return null;
        }
    }

}
