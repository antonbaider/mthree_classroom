package com.mthree;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Results {
    public static InterestCalculator.Result getResult(Scanner scanner) {
        // Get user inputs as BigDecimal
        BigDecimal initialInvestSum = UserInput.getInputAsBigDecimal(scanner, "How much do you want to invest?");
        int years = UserInput.getInputAsInt(scanner, "How many years are investing?");
        BigDecimal rate = UserInput.getInputAsBigDecimal(scanner, "What is the annual interest rate % growth?");
        InterestCalculator.Result result = new InterestCalculator.Result(initialInvestSum, years, rate);
        return result;
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

    // Method to calculate earned interest using BigDecimal
    static BigDecimal getEarnedInterest(BigDecimal initialInvestSum, BigDecimal rate) {
        return initialInvestSum.multiply(rate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }
}
