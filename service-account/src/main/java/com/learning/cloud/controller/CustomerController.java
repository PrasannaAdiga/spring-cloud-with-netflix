package com.learning.cloud.controller;

import com.learning.cloud.entity.Account;
import com.learning.cloud.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final AccountRepository accountRepository;

    @GetMapping("/{customerId}/accounts")
    public ResponseEntity<List<Account>> findAccountsByCustomerId(@PathVariable("customerId")
                                                          @Size(min = 1, max = 3, message = "Customer Id must be 1 to 3 digits only")
                                                          @Positive(message = "Customer Id should be positive value")
                                                                  String customerId) {
        return ResponseEntity.ok().body(accountRepository.findByCustomerId(customerId));
    }
}
