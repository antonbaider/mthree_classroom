package com.mthree;

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
        try (Scanner scanner = new Scanner(System.in)) {
            boolean playAgain = Config.isPlayAgain();
            Config.gameInit(playAgain, scanner);
        }
    }

    record Result(int ties, int userWins, int computerWins) {
    }

}