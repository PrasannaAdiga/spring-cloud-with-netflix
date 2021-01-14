package com.learning.cloud.clinet.impl;

import com.learning.cloud.clinet.IAccountServiceClient;
import com.learning.cloud.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class AccountServiceClient implements IAccountServiceClient {
    private final Throwable cause;

    public AccountServiceClient(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public List<Account> findAccountsByCustomerId(Long id) {
        log.error("Error took place when findAccountsByCustomerId was called with customer ID {}. Error message: {}",
                id, cause.getLocalizedMessage());
        return Collections.emptyList();
    }
}
