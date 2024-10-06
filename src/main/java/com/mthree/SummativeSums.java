package com.mthree;

import java.util.Arrays;

public class SummativeSums {
    public static void main(String[] args) {
        int[] a1 = {1, 90, -33, -55, 67, -16, 28, -55, 15};
        int[] a2 = {999, -60, -77, 14, 160, 301};
        int[] a3 = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130,
                140, 150, 160, 170, 180, 190, 200, -99};

        System.out.println("#1 Array Sum: " + getSum(a1));
        System.out.println("#2 Array Sum: " + getSum(a2));
        System.out.println("#3 Array Sum: " + getSum(a3));
    }

    static int getSum(int[] array) {
        int sum = Arrays.stream(array).sum();
        return sum;
    }
}