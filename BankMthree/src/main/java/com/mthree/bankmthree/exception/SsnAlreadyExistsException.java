package com.mthree.bankmthree.exception;

public class SsnAlreadyExistsException extends RuntimeException {
    public SsnAlreadyExistsException(String message) {
        super(message);
    }
}
