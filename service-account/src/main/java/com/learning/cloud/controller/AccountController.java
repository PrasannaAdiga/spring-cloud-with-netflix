package com.learning.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.entity.Account;
import com.learning.cloud.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Account add(@RequestBody Account account) {
        return accountRepository.add(account);
    }

    @PutMapping
    public Account update(@RequestBody Account account) {
        return accountRepository.update(account);
    }

    @PutMapping("/{id}/withdraw/{amount}")
    public Account withdraw(@PathVariable("id") Long accountId, @PathVariable("amount") int amount) throws JsonProcessingException {
        Account account = accountRepository.findById(accountId);
        log.info("Account found: {}", objectMapper.writeValueAsString(account));
        account.setBalance(account.getBalance() - amount);
        log.info("Account balance: {}", objectMapper.writeValueAsString(Collections.singletonMap("balance", account.getBalance())));
        return accountRepository.update(account);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        accountRepository.delete(id);
    }

    @GetMapping("/ids")
    public List<Account> find(@RequestBody List<Long> ids) {
        return accountRepository.find(ids);
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable("id") Long id) {
        return accountRepository.findById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Account> findByCustomerId(@PathVariable("customerId") Long customerId) {
        return accountRepository.findByCustomer(customerId);
    }

}
