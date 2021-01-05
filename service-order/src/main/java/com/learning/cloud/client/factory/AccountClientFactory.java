package com.learning.cloud.client.factory;

import com.learning.cloud.client.impl.AccountServiceServiceClient;
import feign.hystrix.FallbackFactory;

public class AccountClientFactory implements FallbackFactory<AccountServiceServiceClient> {
    @Override
    public AccountServiceServiceClient create(Throwable cause) {
        return new AccountServiceServiceClient(cause);
    }
}
