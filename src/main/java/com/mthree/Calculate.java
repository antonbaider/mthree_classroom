package com.mthree;

import java.util.Scanner;

public class Calculate {
    static void getResults(Scanner scanner) {
        int age = UserService.getIntInput(scanner);
        int maxRate = UserService.getMaxRate(age);
        int[] heartRateZone = UserService.getHeartRateZone(maxRate);
        OutputService.getResults(maxRate, heartRateZone);
    }
}
