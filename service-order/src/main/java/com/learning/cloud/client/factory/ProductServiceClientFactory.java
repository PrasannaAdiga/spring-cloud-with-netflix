package com.learning.cloud.client.factory;

import com.learning.cloud.client.impl.ProductServiceClient;
import feign.hystrix.FallbackFactory;

public class ProductServiceClientFactory implements FallbackFactory<ProductServiceClient> {
    @Override
    public ProductServiceClient create(Throwable cause) {
        return new ProductServiceClient(cause);
    }
}
