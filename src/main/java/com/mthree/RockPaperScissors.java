package com.mthree;

import java.util.Random;
import java.util.Scanner;

/**
 * Task List
 * Create a flowchart that describes what the program will do.
 * Show your flowchart to your instructor.
 * After receiving approval for your flowchart, begin writing code.
 * Write the code in stages, checking that each stage works before continuing to the next stage.
 * Once all parts of the code work as expected and the program meets all requirements listed below, submit the code using instructions provided by your instructor.
 * Rules
 * The rules of the game are as follows:
 * The standard game includes two players by default.
 * Each player chooses Rock, Paper, or Scissors.
 * If both players choose the same thing, the round is a tie.
 * Otherwise:
 * Paper wraps Rock to win
 * Scissors cut Paper to win
 * Rock breaks Scissors to win
 */

public class RockPaperScissors {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;
        while (playAgain) {
            Configuration initial = (Configuration) Config.getConfiguration();
            Random random = new Random();

        /*
        The program first asks the user how many rounds he/she wants to play.
        Maximum number of rounds = 10, minimum number of rounds = 1.
        If the user asks for something outside this range, the program prints an error message and quits.
        If the number of rounds is in range, the program plays that number of rounds.
        Each round is played according to the requirements below.
         */

            int numOfSelectedRounds = InitialiseFirstRound.getNumOfSelectedRounds(scanner, initial.isValid(), initial.numOfSelectedRounds());

        /*
        For each round of Rock, Paper, Scissors, the program does the following:
        The computer asks the user for his/her choice (Rock, Paper, or Scissors).
        Hint: 1 = Rock, 2 = Paper, 3 = Scissors
        After the computer asks for the user’s input, the computer randomly chooses Rock, Paper, or Scissors and displays the result of the round (tie, user win, or computer win).
        Hint: Use the Random class.
         */

            ResultsOutput.Result result = ResultsOutput.getResult(initial.currentRound(), numOfSelectedRounds, scanner, initial.playerChoice(), initial.choice(), random, initial.ties(), initial.computerWins(), initial.userWins());

            playAgain = ResultsOutput.isPlayAgain(result, scanner, playAgain);
        }
        scanner.close();
    }

    record Configuration(int numOfSelectedRounds, int currentRound, int ties, int userWins, int computerWins,
                         boolean isValid, String[] choice, String playerChoice) {
    }
}
