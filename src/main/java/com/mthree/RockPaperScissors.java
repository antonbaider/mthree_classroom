package com.mthree;
    /*
    Task List
    Create a flowchart that describes what the program will do.
    Show your flowchart to your instructor.
    After receiving approval for your flowchart, begin writing code.
    Write the code in stages, checking that each stage works before continuing to the next stage.
    Once all parts of the code work as expected and the program meets all requirements listed below, submit the code using instructions provided by your instructor.
    Rules
    The rules of the game are as follows:

    The standard game includes two players by default.
    Each player chooses Rock, Paper, or Scissors.
    If both players choose the same thing, the round is a tie.
    Otherwise:
    Paper wraps Rock to win
    Scissors cut Paper to win
    Rock breaks Scissors to win
 */

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;
        while (playAgain) {
            int numOfSelectedRounds = 0;
            int currentRound = 0;
            int ties = 0;
            int userWins = 0;
            int computerWins = 0;
            boolean isValid = false;
            String[] choice = {"Rock", "Paper", "Scissors"};
            String playerChoice = "";
            String computerChoice;
            Random random = new Random();

        /*
        The program first asks the user how many rounds he/she wants to play.
Maximum number of rounds = 10, minimum number of rounds = 1.
If the user asks for something outside this range, the program prints an error message and quits.
If the number of rounds is in range, the program plays that number of rounds.
Each round is played according to the requirements below.
         */

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

        /*
        For each round of Rock, Paper, Scissors, the program does the following:
The computer asks the user for his/her choice (Rock, Paper, or Scissors).
Hint: 1 = Rock, 2 = Paper, 3 = Scissors
After the computer asks for the user’s input, the computer randomly chooses Rock, Paper, or Scissors and displays the result of the round (tie, user win, or computer win).
Hint: Use the Random class.
         */
            while (currentRound < numOfSelectedRounds) {
                isValid = false;
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

                computerChoice = choice[random.nextInt(choice.length)];
                System.out.println("\nComputer selected " + computerChoice + " role!\n");

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
        }
        scanner.close();
    }
}
