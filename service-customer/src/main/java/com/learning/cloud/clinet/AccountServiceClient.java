package com.learning.cloud.clinet;

import com.learning.cloud.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping("/account/customer/{customerId}")
    List<Account> findByCustomerId(@PathVariable("customerId") Long id);
}
