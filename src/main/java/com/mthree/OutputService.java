package com.mthree;

public class OutputService {
    static void getResults(int maxRate, int[] heartRateZone) {
        System.out.println("Your maximum heart rate should be " + maxRate + " beats per minute");
        System.out.println("Your target HR Zone is " + heartRateZone[0] + " - " + heartRateZone[1] + " beats per minute");
    }
}