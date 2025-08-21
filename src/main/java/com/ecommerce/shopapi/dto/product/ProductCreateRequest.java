package com.ecommerce.shopapi.dto.product;

import com.ecommerce.shopapi.model.ProductCategory;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductCreateRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        @NotNull @Min(0) Integer stock,
        @NotNull ProductCategory category
) {}
