package com.learning.cloud.client;

import com.learning.cloud.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PostMapping("/product/ids")
    public List<Product> findByIds(List<Long> ids);
}
