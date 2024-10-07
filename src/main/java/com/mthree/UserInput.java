package com.mthree;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserInput {
    // Utility method to get an integer input from the user
    public static int getInputAsInt(Scanner scanner, String prompt) {
        System.out.print(prompt + " ");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid integer.");
            System.out.print(prompt + " ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Method to get input as BigDecimal from the user
    public static BigDecimal getInputAsBigDecimal(Scanner scanner, String prompt) {
        System.out.print(prompt + " ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt + " ");
            scanner.next();
        }
        return scanner.nextBigDecimal();
    }
}
