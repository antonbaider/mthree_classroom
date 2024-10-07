package com.mthree;

import java.util.Scanner;

/**
 * This program must be in a new console application named Factorizer.
 * This program must ask the user for the number the program will factor.
 * The program must print out the original number.
 * The program must print out each factor of the number (not including the number itself).
 * The program must print out the total number of factors for the number.
 * The program must print out whether or not the number is perfect.
 * The program must print out whether or not the number is prime.
 */

public class Factorize {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserIo.inputOutput(scanner);
        scanner.close();
    }

}