package com.learning.cloud.controller.v1.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.clinet.IAccountServiceClient;
import com.learning.cloud.controller.v1.ICustomerController;
import com.learning.cloud.model.Account;
import com.learning.cloud.model.Customer;
import com.learning.cloud.repository.CustomerRepository;
import com.learning.cloud.exception.custom.ResourceFoundException;
import com.learning.cloud.exception.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerController implements ICustomerController {
    private final CustomerRepository customerRepository;
    private final IAccountServiceClient iAccountServiceClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<Customer> findById(Long id) {
        Customer customer = checkCustomer(id);
        return ResponseEntity.ok().body(customer);
    }

    @Override
    public ResponseEntity<List<Customer>> findByIds(List<Long> ids) {
        return ResponseEntity.ok().body(customerRepository.findByIds(ids));
    }

    @Override
    public ResponseEntity<Customer> add(Customer customer) {
        customerRepository.findByName(customer.getName()).ifPresent(c -> {
            throw new ResourceFoundException("Customer with name " + c.getName() + " already found!");
        });
        Customer newCustomer = customerRepository.add(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCustomer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Customer> update(Customer customer) {
        checkCustomer(customer.getId());
        return ResponseEntity.ok().body(customerRepository.update(customer));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        checkCustomer(id);
        customerRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Customer> findByIdWithAccounts(Long customerId) throws JsonProcessingException {
        List<Account> accounts = iAccountServiceClient.findAccountsByCustomerId(customerId);
        log.info("Accounts found: {}", objectMapper.writeValueAsString(accounts));
        Customer customer = checkCustomer(customerId);
        customer.setAccounts(accounts);
        return ResponseEntity.ok().body(customer);
    }

    private Customer checkCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found"));
    }
}