package com.learning.cloud.controller.v1.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.controller.v1.IProductController;
import com.learning.cloud.entity.Product;
import com.learning.cloud.exception.ResourceFoundException;
import com.learning.cloud.exception.ResourceNotFoundException;
import com.learning.cloud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController implements IProductController {
    private final ProductRepository productRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<Product> findById(Long id) {
        Product product = checkProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @Override
    public ResponseEntity<List<Product>> findByIds(List<Long> ids) throws JsonProcessingException {
        List<Product> products = productRepository.findByIds(ids);
        log.info("Products found: {}", objectMapper.writeValueAsString(Collections.singletonMap("Count", products.size())));
        return ResponseEntity.ok().body(products);
    }

    @Override
    public ResponseEntity<Void> create(Product product) {
        productRepository.findByName(product.getName()).ifPresent( p -> {
            throw new ResourceFoundException("Product with name " + p.getName() + " already exists!");
        });
        Product newProduct = productRepository.add(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Product> update(Product product) {
        checkProduct(product.getId());
        return ResponseEntity.ok().body(productRepository.update(product));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        checkProduct(id);
        productRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Product checkProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " Not Found!"));
    }

}
