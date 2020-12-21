package com.learning.cloud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.entity.Product;
import com.learning.cloud.exception.ResourceFoundException;
import com.learning.cloud.exception.ResourceNotFoundException;
import com.learning.cloud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id")
                                            @Size(min = 1, max = 10, message = "Product ID must be 1 to 10 digits only")
                                            @Positive(message = "Product ID should be positive value")
                                                    Long id) {
        Product product = checkProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping(params = "id")
    public ResponseEntity<List<Product>> findByIds(@RequestParam
                                                   @NotEmpty(message = "Should contain at-least single Id")
                                                           List<Long> ids) throws JsonProcessingException {
        List<Product> products = productRepository.findByIds(ids);
        log.info("Products found: {}", objectMapper.writeValueAsString(Collections.singletonMap("Count", products.size())));
        return ResponseEntity.ok().body(products);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Product product) {
        productRepository.findByName(product.getName()).ifPresent( p -> {
            throw new ResourceFoundException("Product with name " + p.getName() + " already exists!");
        });
        Product newProduct = productRepository.add(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
        checkProduct(product.getId());
        return ResponseEntity.ok().body(productRepository.update(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")
                                       @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                       @Positive(message = "Account number should be positive value")
                                               Long id) {
        checkProduct(id);
        productRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Product checkProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " Not Found!"));
    }

}
