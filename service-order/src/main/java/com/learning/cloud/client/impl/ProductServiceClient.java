package com.learning.cloud.client.impl;

import com.learning.cloud.client.IProductServiceClient;
import com.learning.cloud.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ProductServiceClient implements IProductServiceClient {
    private final Throwable cause;

    public ProductServiceClient(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public List<Product> findByIds(List<Long> ids) {
        log.error("Error took place when findByIds was called with list of product IDs {}. Error message: {}",
                ids.toString(), cause.getLocalizedMessage());
        return Collections.emptyList();
    }
}
