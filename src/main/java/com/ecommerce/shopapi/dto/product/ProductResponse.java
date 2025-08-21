package com.ecommerce.shopapi.dto.product;

import com.ecommerce.shopapi.model.ProductCategory;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductCategory category
) {}
