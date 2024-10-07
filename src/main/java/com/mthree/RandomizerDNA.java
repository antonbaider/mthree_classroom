package main.java.com.mthree;

import java.util.Random;

public class RandomizerDNA {
    private static final Random RANDOM = new Random();

    static int[] getRandoms() {
        int sum = 0;
        int[] percentages = new int[5];

        for (int i = 0; i < percentages.length - 1; i++) {
            percentages[i] = RANDOM.nextInt(101 - sum);
            sum += percentages[i];
        }
        percentages[percentages.length - 1] = 100 - sum;

        for (int i = percentages.length - 1; i > 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            int temp = percentages[index];
            percentages[index] = percentages[i];
            percentages[i] = temp;
        }

        return percentages;
    }

}
