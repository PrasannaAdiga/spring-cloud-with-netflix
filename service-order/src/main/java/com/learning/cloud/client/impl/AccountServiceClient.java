package com.learning.cloud.client.impl;

import com.learning.cloud.client.IAccountServiceClient;
import com.learning.cloud.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountServiceClient implements IAccountServiceClient {
    private final Throwable cause;

    public AccountServiceClient(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Account withdraw(Long id, int amount) {
        log.error("Error took place when withdraw was called with account ID {} for the amount {}. Error message: {}",
                id, amount, cause.getLocalizedMessage());
        return new Account();
    }
}
