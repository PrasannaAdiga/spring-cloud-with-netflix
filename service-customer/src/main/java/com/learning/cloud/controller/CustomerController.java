package com.learning.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.clinet.AccountServiceClient;
import com.learning.cloud.entity.Account;
import com.learning.cloud.entity.Customer;
import com.learning.cloud.repository.CustomerRepository;
import exception.ResourceFoundException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/customers")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final AccountServiceClient accountServiceClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable
                                             @Positive(message = "Customer ID should be positive value")
                                                     Long id) {
        Customer customer = checkCustomer(id);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(params = "id")
    public ResponseEntity<List<Customer>> findByIds(@RequestParam
                                                    @NotEmpty(message = "Should contain at-least single Id")
                                                            List<Long> ids) {
        return ResponseEntity.ok().body(customerRepository.findByIds(ids));
    }

    @PostMapping
    public ResponseEntity<Customer> add(@Valid @RequestBody Customer customer) {
        customerRepository.findByName(customer.getName()).ifPresent(c -> {
            throw new ResourceFoundException("Customer with name " + c.getName() + " already found!");
        });
        Customer newCustomer = customerRepository.add(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCustomer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Customer> update(@Valid @RequestBody Customer customer) {
        checkCustomer(customer.getId());
        return ResponseEntity.ok().body(customerRepository.update(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")
                                       @Positive(message = "Customer ID should be positive value")
                                               Long id) {
        checkCustomer(id);
        customerRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/withAccounts")
    public Customer findByIdWithAccounts(@PathVariable("id")
                                         @Positive(message = "Customer ID should be positive value")
                                                 Long customerId) throws JsonProcessingException {
        List<Account> accounts = accountServiceClient.findAccountsByCustomerId(customerId);
        log.info("Accounts found: {}", objectMapper.writeValueAsString(accounts));
        Customer customer = checkCustomer(customerId);
        customer.setAccounts(accounts);
        return customer;
    }

    private Customer checkCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found"));
    }
}