package com.mthree;

import java.util.Scanner;

public class InitialiseFirstRound {
    public static int getNumOfSelectedRounds(Scanner scanner, boolean isValid, int numOfSelectedRounds) {
        do {
            try {
                System.out.println("Please select numbers of rounds (from 1 to 10): ");
                String input = scanner.nextLine();
                if (input == null || input.isEmpty()) {
                    System.out.println("You did not enter anything!");
                } else if (Integer.valueOf(input) < 1 || Integer.valueOf(input) > 10) {
                    System.out.println("You did not enter a valid number!");
                } else if (input.contains(".") || input.contains(",")) {
                    System.out.println("The input is NOT a valid Integer.");
                } else {
                    isValid = true;
                    numOfSelectedRounds = Integer.valueOf(input);
                    System.out.println("You selected " + numOfSelectedRounds + " rounds!");
                }
            } catch (NumberFormatException ex) {
                System.out.println("That was not a whole number!");
            }
        } while (!isValid);
        return numOfSelectedRounds;
    }
}
