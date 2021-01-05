package com.learning.cloud.client;

import com.learning.cloud.client.impl.CustomerServiceClient;
import com.learning.cloud.entity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", fallback = CustomerServiceClient.class)
public interface ICustomerServiceClient {

    @GetMapping("/v1/customers/{customerId}/withAccounts")
    Customer findByIdWithAccounts(@PathVariable("customerId") Long id);
}
