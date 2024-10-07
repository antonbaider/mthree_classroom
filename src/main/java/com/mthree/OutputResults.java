package com.mthree;

public class OutputResults {
    //printing in game result
    public static void printRollStatus(int rollCount, int rolledValue, int money) {
        System.out.println("------------------------------------------------");
        System.out.printf("Roll %3d: You rolled %d. You now have $%,d.%n", rollCount, rolledValue, money);
        System.out.println("------------------------------------------------");
    }

    //summary result
    public static void printGameSummary(int rollCount, int rollCountAtMax, int maxValueOfMoney) {
        System.out.println("\n================== GAME OVER ==================");
        System.out.printf("You are broke after %d rolls.%n", rollCount);
        System.out.printf("You should have quit after %d rolls when you had $%,d.%n", rollCountAtMax, maxValueOfMoney);
        System.out.println("===============================================");
    }

}
