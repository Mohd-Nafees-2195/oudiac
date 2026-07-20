package com.app.oudiac.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String invalidOrder) {
        super(invalidOrder);
    }
}
