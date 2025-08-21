package com.ecommerce.shopapi.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
