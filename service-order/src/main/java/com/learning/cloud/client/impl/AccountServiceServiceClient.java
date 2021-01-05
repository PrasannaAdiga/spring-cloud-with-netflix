package com.learning.cloud.client.impl;

import com.learning.cloud.client.IAccountServiceClient;
import com.learning.cloud.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountServiceServiceClient implements IAccountServiceClient {
    private final Throwable cause;

    public AccountServiceServiceClient(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Account withdraw(Long id, int amount) {
        log.error("Error took place when withdraw was called with account ID {} for the amount {}. Error message: {}",
                id, amount, cause.getLocalizedMessage());
        return new Account();
    }
}
