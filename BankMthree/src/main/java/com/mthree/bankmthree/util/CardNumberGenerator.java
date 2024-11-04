package com.mthree.bankmthree.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CardNumberGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CARD_NUMBER_LENGTH = 16;

    public String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            int digit = RANDOM.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }
    public String generateUniqueCardNumber() {
        int maxAttempts = 5;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            String cardNumber = generateCardNumber();
            return cardNumber;
        }
        throw new RuntimeException("Unable to generate a unique card number after " + maxAttempts + " attempts");
    }
}
