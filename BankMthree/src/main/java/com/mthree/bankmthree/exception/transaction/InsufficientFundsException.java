package com.mthree.bankmthree.exception.transaction;

public class InsufficientFundsException extends TransactionException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
