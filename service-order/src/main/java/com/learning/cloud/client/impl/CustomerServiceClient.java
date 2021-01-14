package com.learning.cloud.client.impl;

import com.learning.cloud.client.ICustomerServiceClient;
import com.learning.cloud.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerServiceClient implements ICustomerServiceClient {
    private final Throwable cause;

    public CustomerServiceClient(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Customer findByIdWithAccounts(Long id) {
        log.error("Error took place when findByIdWithAccounts was called with customer ID {}. Error message: {}",
                id, cause.getLocalizedMessage());
        return new Customer();
    }
}
