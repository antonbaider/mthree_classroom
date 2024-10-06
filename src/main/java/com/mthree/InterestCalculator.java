package com.mthree;

import java.util.Scanner;

public class InterestCalculator {

    public static int getInputAsInt(Scanner scanner, String prompt) {
        System.out.println(prompt + " ");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid integer.");
            System.out.println(prompt + " ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    static double getEarnedInterest(double initialInvestSum, double rate) {
        return initialInvestSum * (rate / 100);
    }

    static void printResult(double initialInvestSum, double rate, int years) {
        System.out.println("\nCalculating...\n");
        double totalAmount = initialInvestSum;
        for (int i = 1; i <= years; i++) {
            double earnedInterest = getEarnedInterest(totalAmount, rate);
            double endOfYearAmount = totalAmount + earnedInterest;

            System.out.printf("Year %d:%n", i);
            System.out.printf("Began with $%.2f%n", totalAmount);
            System.out.printf("Earned $%.2f%n", earnedInterest);
            System.out.printf("Ended with $%.2f%n%n", endOfYearAmount);

            totalAmount = endOfYearAmount;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int initialInvestSum = getInputAsInt(scanner, "How much do you want to invest?");
        int years = getInputAsInt(scanner, "How many years are investing?");
        int rate = getInputAsInt(scanner, "What is the annual interest rate % growth?");

        printResult(initialInvestSum, rate, years);
        scanner.close();
    }
}