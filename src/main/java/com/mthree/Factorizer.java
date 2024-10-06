package com.mthree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Factorizer {

    static int getFactorial(int n) {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }

    static int[] getFactors(int n) {
        List<Integer> factorsList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
               factorsList.add(i);
            }
        }
        int[] factors = factorsList.stream().mapToInt(i -> i).toArray();
        return factors;
    }

    static int getNumberOfFactors(int[] factors) {
        return factors.length;
    }

    static String isPerfect(int n) {
       int[] arr = getFactors(n);
        int sum = 0;
            for (int i = 0; i < arr.length - 1; i++) {
                sum += i;
            }
            if (sum > n) {
                return "not a perfect";
            }
        return "perfect";
    }

    static String isPrime(int n) {
        int num = getNumberOfFactors(getFactors(n));
        if (num == 2) return "prime";
        else return "not a prime";
    }

    public static void main(String[] args) {
        int number;
        Scanner scanner = new Scanner(System.in);
        System.out.println("What humber would you like to factor?");
        number = scanner.nextInt();
        System.out.println("The factors of " + number + " are: ");
        System.out.println(Arrays.toString(getFactors(number)).replaceAll(",",""));
        System.out.println(number + " has " + getNumberOfFactors(getFactors(number)) + " factors");
        System.out.println(number + " is a " + isPerfect(number) + " number");
        System.out.println(number + " is a " + isPrime(number) + " number");
        scanner.close();
    }
}