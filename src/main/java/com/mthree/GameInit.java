package com.mthree;

import java.util.Random;
import java.util.Scanner;

public class GameInit {
    public static boolean isPlayAgain(int userWins, int computerWins, int ties, Scanner scanner, boolean playAgain) {
        if (userWins == computerWins) {
            System.out.println("_______________________\nIt' a tie! \n_______________________\n" + "\nScore:\nYou wins: " + userWins + "\nComputer wins: " + computerWins + "\nTies: " + ties);
        } else if (computerWins > userWins) {
            System.out.println("_______________________\nComputer wins! \n_______________________\n" + "\nScore:\nYou wins: " + userWins + "\nComputer wins: " + computerWins + "\nTies: " + ties);
        } else {
            System.out.println("_______________________\n--------You win!-------- \n_______________________\n" + "\nScore:\nYou wins: " + userWins + "\nComputer wins: " + computerWins + "\nTies: " + ties);
        }
        System.out.println("\nDo you want to play a new game? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();
        if (!response.equals("yes") && !response.equals("y")) {
            playAgain = false;
            System.out.println("Thank you for playing! Goodbye!");
        }
        return playAgain;
    }

    static String getComputerChoice(String[] choice, Random random) {
        String computerChoice;
        computerChoice = choice[random.nextInt(choice.length)];
        System.out.println("\nComputer selected " + computerChoice + " role!\n");
        return computerChoice;
    }

    static int getNumOfSelectedRounds(Scanner scanner, boolean isValid, int numOfSelectedRounds) {
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
