package com.learning.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.clinet.AccountServiceClient;
import com.learning.cloud.model.Account;
import com.learning.cloud.model.Customer;
import com.learning.cloud.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountServiceClient accountServiceClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        return customerRepository.add(customer);
    }

    @PutMapping
    public Customer update(@RequestBody Customer customer) {
        return customerRepository.update(customer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        customerRepository.delete(id);
    }

    @GetMapping("/{id}/withAccounts")
    public Customer findByIdWithAccounts(@PathVariable("id") Long customerId) throws JsonProcessingException {
        List<Account> accounts = accountServiceClient.findByCustomerId(customerId);
        log.info("Accounts found: {}", objectMapper.writeValueAsString(accounts));
        Customer customer = customerRepository.findById(customerId);
        customer.setAccounts(accounts);
        return customer;
    }


    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return customerRepository.findById(id);
    }

    @GetMapping("/ids")
    public List<Customer> find(@RequestBody List<Long> ids) {
        return customerRepository.find(ids);
    }

}
