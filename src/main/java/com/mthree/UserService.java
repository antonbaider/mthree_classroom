package com.mthree;

import java.util.Scanner;

public class UserService {
    protected static int getIntInput(Scanner scanner) {
        int age = -1;
        while (age < 0) {
            System.out.println("Please enter your age: ");
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

    protected static int getMaxRate(int age) {
        return 220 - age;
    }

    protected static int[] getHeartRateZone(int maxRate) {
        int[] heartRateZone = new int[2];
        heartRateZone[0] = (int) (maxRate * 0.5);
        heartRateZone[1] = (int) (maxRate * 0.85);
        return heartRateZone;
    }
}
