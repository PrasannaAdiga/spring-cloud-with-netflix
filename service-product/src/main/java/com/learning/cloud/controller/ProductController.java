package com.learning.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.entity.Product;
import com.learning.cloud.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepository.add(product);
    }

    @PostMapping("/ids")
    public List<Product> find(@RequestBody List<Long> ids) throws JsonProcessingException {
        List<Product> products = productRepository.find(ids);
        log.info("Products found: {}", objectMapper.writeValueAsString(Collections.singletonMap("Count", products.size())));
        return products;
    }

    @PutMapping
    public Product update(@RequestBody Product product) {
        return productRepository.update(product);
    }

    @DeleteMapping
    public void delete(Long id) {
        productRepository.delete(id);
    }

    @GetMapping("{/id}")
    public Product findById(@PathVariable("id") Long id) {
        return productRepository.findById(id);
    }

}
