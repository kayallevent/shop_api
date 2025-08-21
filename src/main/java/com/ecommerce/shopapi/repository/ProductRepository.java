package com.ecommerce.shopapi.repository;

import com.ecommerce.shopapi.model.Product;
import com.ecommerce.shopapi.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    boolean existsByNameIgnoreCase(String name);
}
