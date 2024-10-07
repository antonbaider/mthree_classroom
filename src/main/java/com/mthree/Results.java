package com.mthree;

import java.util.Random;
import java.util.Scanner;

public class Results {
    public static RockPaperScissors.Result getResult(int currentRound, int numOfSelectedRounds, Scanner scanner, String playerChoice, String[] choice, Random random, int ties, int computerWins, int userWins) {
        String computerChoice;
        boolean isValid;
        while (currentRound < numOfSelectedRounds) {
            do {
                System.out.println("\nRound " + (currentRound + 1) + ": ");
                System.out.println("Please choose you role:\n1 = Rock\n2 = Paper\n3 = Scissors");
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        isValid = true;
                        playerChoice = choice[0];
                        System.out.println("You selected " + playerChoice + " role!");
                        break;
                    case "2":
                        isValid = true;
                        playerChoice = choice[1];
                        System.out.println("You selected " + playerChoice + " role!");
                        break;
                    case "3":
                        isValid = true;
                        playerChoice = choice[2];
                        System.out.println("You selected " + playerChoice + " role!");
                        break;
                    default:
                        isValid = false;
                        System.out.println("You did not enter a valid choice!");
                        break;
                }
                currentRound++;
            } while (!isValid);

    /* After the computer asks for the user’s input, the computer randomly chooses
    Rock, Paper, or Scissors and displays the result of the round (tie, user win, or computer win).
    */

            computerChoice = GameInit.getComputerChoice(choice, random);

    /*
    If both players choose the same thing, the round is a tie.
    Otherwise:
    Paper wraps Rock to win
    Scissors cut Paper to win
    Rock breaks Scissors to win
    */

            if (playerChoice.equals(computerChoice)) {
                ties++;
                System.out.println("The round is a tie, both players choose the same thing.");
            } else if (computerChoice.equals("Paper") && playerChoice.equals("Rock")) {
                computerWins++;
                System.out.println("Paper wraps Rock\nComputer win this round");
            } else if (computerChoice.equals("Scissors") && playerChoice.equals("Paper")) {
                computerWins++;
                System.out.println("Scissors cut Paper\nComputer win this round");
            } else if (computerChoice.equals("Rock") && playerChoice.equals("Scissors")) {
                computerWins++;
                System.out.println("Rock breaks Scissors\nComputer win this round");
            } else {
                userWins++;
                System.out.println("You win this round! " + playerChoice + " and " + computerChoice + " wins!");
            }
        }
        return new RockPaperScissors.Result(ties, userWins, computerWins);
    }
}
