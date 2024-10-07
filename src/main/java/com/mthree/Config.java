package com.mthree;

public class Config {
    static Record getConfiguration() {
        int numOfSelectedRounds = 0;
        int currentRound = 0;
        int ties = 0;
        int userWins = 0;
        int computerWins = 0;
        boolean isValid = false;
        String[] choice = {"Rock", "Paper", "Scissors"};
        String playerChoice = "";
        String computerChoice;
        return new RockPaperScissors.Configuration(numOfSelectedRounds, currentRound, ties, userWins, computerWins, isValid, choice, playerChoice);
    }
}
