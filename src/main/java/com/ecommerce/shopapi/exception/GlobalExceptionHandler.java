package com.ecommerce.shopapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Domain not found (bizim fırlattığımız)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        var status = HttpStatus.NOT_FOUND;
        var body = ApiError.of(status.value(), status.getReasonPhrase(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }

    // 400 - Validation hataları
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var status = HttpStatus.BAD_REQUEST;
        var body = ApiError.ofWithValidation(
                status.value(),
                status.getReasonPhrase(),
                "Validation failed",
                req.getRequestURI(),
                errors
        );
        return ResponseEntity.status(status).body(body);
    }

    // 400 - İş kuralı (IllegalArgument vs.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArg(IllegalArgumentException ex, HttpServletRequest req) {
        var status = HttpStatus.BAD_REQUEST;
        var body = ApiError.of(status.value(), status.getReasonPhrase(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }

    // 500 - Beklenmeyen durumlar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = ApiError.of(status.value(), status.getReasonPhrase(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
