package com.learning.cloud.repository;

import com.learning.cloud.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public Product add(Product product) {
        product.setId((long) products.size() + 1);
        products.add(product);
        return product;
    }

    public Product update(Product product) {
        products.set(product.getId().intValue() - 1, product);
        return product;
    }

    public void delete(Long id) {
        products.remove(id.intValue() - 1);
    }

    public Optional<Product> findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Product> findByName(String name) {
        return products.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    public List<Product> findByIds(List<Long> ids) {
        return products.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toList());
    }
}
