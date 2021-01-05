package com.learning.cloud.client.factory;

import com.learning.cloud.client.impl.CustomerServiceClient;
import feign.hystrix.FallbackFactory;

public class CustomerClientFactory implements FallbackFactory<CustomerServiceClient> {
    @Override
    public CustomerServiceClient create(Throwable cause) {
        return new CustomerServiceClient(cause);
    }
}
