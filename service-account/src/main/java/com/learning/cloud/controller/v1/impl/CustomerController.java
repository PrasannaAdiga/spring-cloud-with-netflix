package com.learning.cloud.controller.v1.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.controller.v1.ICustomerController;
import com.learning.cloud.model.AccountDTO;
import com.learning.cloud.service.impl.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CustomerController implements ICustomerController {
    private final CustomerService customerService;

    @Override
    public ResponseEntity<List<AccountDTO>> findAccountsByCustomerId(UUID customerId) throws JsonProcessingException {
        return ResponseEntity.ok().body(customerService.findAllAccountByCustomerId(customerId));
    }

}
