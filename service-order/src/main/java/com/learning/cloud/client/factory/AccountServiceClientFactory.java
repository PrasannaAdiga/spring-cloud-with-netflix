package com.learning.cloud.client.factory;

import com.learning.cloud.client.impl.AccountServiceClient;
import feign.hystrix.FallbackFactory;

public class AccountServiceClientFactory implements FallbackFactory<AccountServiceClient> {
    @Override
    public AccountServiceClient create(Throwable cause) {
        return new AccountServiceClient(cause);
    }
}
