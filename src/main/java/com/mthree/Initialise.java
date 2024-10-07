package com.mthree;

import java.util.Random;
import java.util.Scanner;

public class Initialise {
    private static final Random RANDOM = new Random();

    public static void startGame() {
        Scanner scanner = new Scanner(System.in);

        int money = getInfo(scanner);
        int maxValueOfMoney = money;
        int rollCountAtMax = 0;
        int rollCount = 0;

        int[] gameResults = runGame(money, maxValueOfMoney, rollCount, rollCountAtMax);
        rollCount = gameResults[0];
        rollCountAtMax = gameResults[1];
        maxValueOfMoney = gameResults[2];

        OutputResults.printGameSummary(rollCount, rollCountAtMax, maxValueOfMoney);
        scanner.close();
    }

    // getting info from player
    static int getInfo(Scanner scanner) {
        System.out.println("How many dollars do you have?");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! How many dollars do you have?");
            scanner.next();
        }
        return scanner.nextInt();
    }

    //magic of dices
    static int rollDice() {
        int dice1 = RANDOM.nextInt(6) + 1;
        int dice2 = RANDOM.nextInt(6) + 1;
        return dice1 + dice2;
    }

    //is that 7 from 2 dices?
    static int checkLuckySevens(int rolledValue, int dollars) {
        return rolledValue == 7 ? dollars + 4 : dollars - 1;
    }

    //starting a game
    static int[] runGame(int money, int maxValueOfMoney, int rollCount, int rollCountAtMax) {
        while (money > 0) {
            int rolledValue = rollDice();
            money = (checkLuckySevens(rolledValue, money));
            rollCount++;

            if (money > maxValueOfMoney) {
                maxValueOfMoney = money;
                rollCountAtMax = rollCount;
            }

            OutputResults.printRollStatus(rollCount, rolledValue, money);
        }
        return new int[]{rollCount, rollCountAtMax, maxValueOfMoney};
    }
}
