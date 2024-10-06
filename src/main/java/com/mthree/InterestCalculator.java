package com.mthree;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * Make the program general; that is, it should prompt for the following inputs and use those inputs in the calculations.
 *
 * The annual interest rate
 * The initial amount of principal
 * The number of years the money is to stay in the fund
 * The output should include the following for each year:
 *
 * The year number
 * The principal at the beginning of the year
 * The total amount of interest earned for the year
 * The principal at the end of the year
 */

public class InterestCalculator {

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

    // Method to calculate earned interest using BigDecimal
    static BigDecimal getEarnedInterest(BigDecimal initialInvestSum, BigDecimal rate) {
        return initialInvestSum.multiply(rate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    // Method to print yearly interest results using BigDecimal
    static void printResult(BigDecimal initialInvestSum, BigDecimal rate, int years) {
        System.out.println("\nCalculating...\n");
        BigDecimal totalAmount = initialInvestSum;

        for (int i = 1; i <= years; i++) {
            BigDecimal earnedInterest = getEarnedInterest(totalAmount, rate);
            BigDecimal endOfYearAmount = totalAmount.add(earnedInterest);

            System.out.printf("Year %d:%n", i);
            System.out.printf("Began with $%.2f%n", totalAmount);
            System.out.printf("Earned $%.2f%n", earnedInterest);
            System.out.printf("Ended with $%.2f%n%n", endOfYearAmount);

            totalAmount = endOfYearAmount;
        }
    }

    // Method to print monthly interest results using BigDecimal
    static void printMonthlyInterest(BigDecimal initialInvestSum, BigDecimal rate, int years) {
        BigDecimal totalAmount = initialInvestSum;

        System.out.printf("%-10s %-10s %-20s %-20s %-20s%n", "Year", "Month", "Began With ($)", "Earned ($)", "Ended With ($)");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (int j = 1; j <= years; j++) {
            for (int i = 1; i <= 12; i++) {
                BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
                BigDecimal earnedInterest = getEarnedInterest(totalAmount, monthlyRate);
                BigDecimal endOfMonthAmount = totalAmount.add(earnedInterest);

                System.out.printf("%-10d %-10d $%-19.2f $%-19.2f $%-19.2f%n", j, i, totalAmount, earnedInterest, endOfMonthAmount);

                totalAmount = endOfMonthAmount;
            }
        }

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("Summary: Began with $%.2f, Earned a total of $%.2f, Ended with $%.2f%n", initialInvestSum, totalAmount.subtract(initialInvestSum), totalAmount);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user inputs as BigDecimal
        BigDecimal initialInvestSum = getInputAsBigDecimal(scanner, "How much do you want to invest?");
        int years = getInputAsInt(scanner, "How many years are investing?");
        BigDecimal rate = getInputAsBigDecimal(scanner, "What is the annual interest rate % growth?");

        // Print the results
        printMonthlyInterest(initialInvestSum, rate, years);

        scanner.close();
    }

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
}