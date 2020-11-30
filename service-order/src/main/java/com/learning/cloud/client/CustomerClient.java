package com.learning.cloud.client;

import com.learning.cloud.entity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/customer/{accountId}/withAccounts")
    public Customer findByIdWithAccounts(@PathVariable("accountId") Long id);
}