package com.learning.cloud.clinet.factory;

import com.learning.cloud.clinet.IAccountServiceClient;
import com.learning.cloud.clinet.impl.AccountServiceClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceClientFactory implements FallbackFactory<IAccountServiceClient> {
    @Override
    public IAccountServiceClient create(Throwable cause) {
        return new AccountServiceClient(cause);
    }
}
