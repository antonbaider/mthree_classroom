package com.mthree.bankmthree.exception.account;

public class ReceiverAccountNotFoundException extends RuntimeException {
    public ReceiverAccountNotFoundException(String message) {
        super(message);
    }
}