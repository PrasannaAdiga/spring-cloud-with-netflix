package com.learning.cloud.controller.v1.impl;

import com.learning.cloud.controller.v1.ICustomerController;
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
@RequiredArgsConstructor
public class CustomerController implements ICustomerController {
    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity<List<Account>> findAccountsByCustomerId(String customerId) {
        return ResponseEntity.ok().body(accountRepository.findByCustomerId(customerId));
    }

}
