package com.mthree;

import java.util.Random;
import java.util.Scanner;

public class Config {
    public static void gameInit(boolean playAgain, Scanner scanner) {
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

            numOfSelectedRounds = GameInit.getNumOfSelectedRounds(scanner, isValid, numOfSelectedRounds);

        /*
        For each round of Rock, Paper, Scissors, the program does the following:
        The computer asks the user for his/her choice (Rock, Paper, or Scissors).
        Hint: 1 = Rock, 2 = Paper, 3 = Scissors
        After the computer asks for the user’s input, the computer randomly chooses Rock, Paper, or Scissors and displays the result of the round (tie, user win, or computer win).
        Hint: Use the Random class.
         */

            RockPaperScissors.Result result = Results.getResult(currentRound, numOfSelectedRounds, scanner, playerChoice, choice, random, ties, computerWins, userWins);

            playAgain = GameInit.isPlayAgain(result.userWins(), result.computerWins(), result.ties(), scanner, playAgain);
        }
    }

    static boolean isPlayAgain() {
        return true;
    }
}
