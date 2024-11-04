package com.mthree.bankmthree.exception;

public class AccountBalanceNotZeroException extends RuntimeException {
    public AccountBalanceNotZeroException(String message) {
        super(message);
    }
}
