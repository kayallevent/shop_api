package com.ecommerce.shopapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String timestamp,   // ISO-8601, UTC
        int status,         // 404, 400, 500...
        String error,       // Not Found, Bad Request...
        String message,     // Ürün bulunamadı: 1
        String path,        // /api/products/1
        Map<String, String> validationErrors // sadece validation için dolu
) {
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(OffsetDateTime.now().toString(), status, error, message, path, null);
    }

    public static ApiError ofWithValidation(int status, String error, String message,
                                            String path, Map<String, String> validationErrors) {
        return new ApiError(OffsetDateTime.now().toString(), status, error, message, path, validationErrors);
    }
}
