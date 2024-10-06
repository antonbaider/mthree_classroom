package com.mthree;

import java.util.Random;
import java.util.Scanner;

/**
 * A program that plays Lucky Sevens. The rules of the game are:
 * Each round, the program rolls a virtual pair of dice for the user.
 * If the sum of the two dice is equal to 7, the player wins $4; otherwise, the player loses $1.
 */

public class LuckySevens {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int money = getInfo(scanner);
        int maxValueOfMoney = money;
        int rollCountAtMax = 0;
        int rollCount = 0;

        int[] gameResults = runGame(money, maxValueOfMoney, rollCount, rollCountAtMax);
        rollCount = gameResults[0];
        rollCountAtMax = gameResults[1];
        maxValueOfMoney = gameResults[2];

        printGameSummary(rollCount, rollCountAtMax, maxValueOfMoney);
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

            printRollStatus(rollCount, rolledValue, money);
        }
        return new int[]{rollCount, rollCountAtMax, maxValueOfMoney};
    }

    //printing in game result
    static void printRollStatus(int rollCount, int rolledValue, int money) {
        System.out.println("------------------------------------------------");
        System.out.printf("Roll %3d: You rolled %d. You now have $%,d.%n", rollCount, rolledValue, money);
        System.out.println("------------------------------------------------");
    }

    //summary result
    static void printGameSummary(int rollCount, int rollCountAtMax, int maxValueOfMoney) {
        System.out.println("\n================== GAME OVER ==================");
        System.out.printf("You are broke after %d rolls.%n", rollCount);
        System.out.printf("You should have quit after %d rolls when you had $%,d.%n", rollCountAtMax, maxValueOfMoney);
        System.out.println("===============================================");
    }
}