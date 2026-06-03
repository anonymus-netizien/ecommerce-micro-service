package com.stschool.ecommerce.productservice.exception;

public class ProductExistsException extends RuntimeException {
    public ProductExistsException(String message) {
        super(message);
    }
}
