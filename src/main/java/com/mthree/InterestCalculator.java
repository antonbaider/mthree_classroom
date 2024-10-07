package com.mthree;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Make the program general; that is, it should prompt for the following inputs and use those inputs in the calculations.
 * <p>
 * The annual interest rate
 * The initial amount of principal
 * The number of years the money is to stay in the fund
 * The output should include the following for each year:
 * <p>
 * The year number
 * The principal at the beginning of the year
 * The total amount of interest earned for the year
 * The principal at the end of the year
 */

public class InterestCalculator {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Result result = Results.getResult(scanner);
            Results.printMonthlyInterest(result.initialInvestSum(), result.rate(), result.years());
        }
    }

    record Result(BigDecimal initialInvestSum, int years, BigDecimal rate) {
    }

}