package com.mthree.bankmthree.exception;

public class AccountsNotFoundException extends RuntimeException {
    public AccountsNotFoundException(String message) {
        super(message);
    }
}
