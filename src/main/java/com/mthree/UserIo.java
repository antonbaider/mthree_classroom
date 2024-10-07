package com.mthree;

import java.util.Arrays;
import java.util.Scanner;

public class UserIo {
    public static void inputOutput(Scanner scanner) {
        int number;
        System.out.println("What humber would you like to factor?");
        number = scanner.nextInt();
        System.out.println("The factors of " + number + " are: ");
        System.out.println(Arrays.toString(Calculations.getFactors(number)).replaceAll(",", ""));
        System.out.println(number + " has " + Calculations.getNumberOfFactors(Calculations.getFactors(number)) + " factors");
        System.out.println(number + " is a " + Calculations.isPerfect(number) + " number");
        System.out.println(number + " is a " + Calculations.isPrime(number) + " number");
    }
}
