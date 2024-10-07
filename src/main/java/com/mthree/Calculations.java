package com.mthree;

import java.util.ArrayList;
import java.util.List;

public class Calculations {
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
        return factorsList.stream().mapToInt(i -> i).toArray();
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
}
