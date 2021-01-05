package com.learning.cloud.client;

import com.learning.cloud.client.factory.ProductServiceClientFactory;
import com.learning.cloud.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", fallbackFactory = ProductServiceClientFactory.class)
public interface IProductServiceClient {

    @PostMapping("/v1/products/id")
    List<Product> findByIds(List<Long> ids);
}
