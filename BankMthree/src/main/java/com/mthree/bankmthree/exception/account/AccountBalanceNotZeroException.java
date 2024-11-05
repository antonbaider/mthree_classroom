package com.mthree.bankmthree.exception.account;

public class AccountBalanceNotZeroException extends AccountException {
    public AccountBalanceNotZeroException(String message) {
        super(message);
    }
}
