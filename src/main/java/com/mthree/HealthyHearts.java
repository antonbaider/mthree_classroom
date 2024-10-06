package com.mthree;

import java.util.Scanner;

/**
 * Create a simple calculator that asks the user for their age, calculates the healthy heart rate range for that age, and displays the result.
 * Their maximum heart rate should be 220 - their age.
 * The target heart rate zone is the 50 - 85% of the maximum.
 */

public class HealthyHearts {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int age = getIntInput(scanner);
        int maxRate = getMaxRate(age);
        int[] heartRateZone = getHeartRateZone(maxRate);
        getResults(maxRate, heartRateZone);

        scanner.close();
    }

    static int getIntInput(Scanner scanner) {
        int age = -1;
        while (age < 0) {
            System.out.print("Please enter your age: ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                if (age < 0) {
                    System.out.println("Age cannot be negative. Please enter a valid age.");
                }
            } else {
                System.out.println("Invalid input! Please enter a valid integer for your age.");
                scanner.next();
            }
        }
        return age;
    }

    static int getMaxRate(int age) {
        return 220 - age;
    }

    static int[] getHeartRateZone(int maxRate) {
        int[] heartRateZone = new int[2];
        heartRateZone[0] = (int) (maxRate * 0.5);
        heartRateZone[1] = (int) (maxRate * 0.85);
        return heartRateZone;
    }

    static void getResults(int maxRate, int[] heartRateZone) {
        System.out.println("Your maximum heart rate should be " + maxRate + " beats per minute");
        System.out.println("Your target HR Zone is " + heartRateZone[0] + " - " + heartRateZone[1] + " beats per minute");

    }
}