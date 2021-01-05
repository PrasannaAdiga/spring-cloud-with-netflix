package com.learning.cloud.client;

import com.learning.cloud.client.impl.ProductServiceClient;
import com.learning.cloud.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", fallback = ProductServiceClient.class)
public interface IProductServiceClient {

    @PostMapping("/v1/products/id")
    List<Product> findByIds(List<Long> ids);
}
