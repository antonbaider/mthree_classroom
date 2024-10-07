package com.mthree;

import java.util.Scanner;

/**
 * Create a simple calculator that asks the user for their age, calculates the healthy heart rate range for that age, and displays the result.
 * Their maximum heart rate should be 220 - their age.
 * The target heart rate zone is the 50 - 85% of the maximum.
 */

public class HealthyHearts {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Calculate.getResults(scanner);
        }
    }
}