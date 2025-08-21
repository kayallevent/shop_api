package com.ecommerce.shopapi.service;

import com.ecommerce.shopapi.dto.product.*;
import com.ecommerce.shopapi.exception.NotFoundException;
import com.ecommerce.shopapi.model.Product;
import com.ecommerce.shopapi.model.ProductCategory;
import com.ecommerce.shopapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public ProductResponse create(ProductCreateRequest req) {
        if (repo.existsByNameIgnoreCase(req.name())) {
            throw new IllegalArgumentException("Bu isimde ürün zaten var.");
        }
        Product p = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stock(req.stock())
                .category(req.category())
                .build();
        return toResponse(repo.save(p));
    }

    public List<ProductResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ürün bulunamadı: " + id));
        return toResponse(p);
    }

    public List<ProductResponse> getByCategory(ProductCategory category) {
        return repo.findByCategory(category).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse update(Long id, ProductUpdateRequest req) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ürün bulunamadı: " + id));
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setStock(req.stock());
        p.setCategory(req.category());
        return toResponse(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("Ürün bulunamadı: " + id);
        repo.deleteById(id);
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getCategory()
        );
    }
}
