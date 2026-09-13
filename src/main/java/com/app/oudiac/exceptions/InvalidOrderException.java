package com.app.oudiac.exceptions;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String msg) {
        super(msg);
    }
}
