package com.mthree;

import java.util.Random;
import java.util.Scanner;

/**
 * Write a program that asks the user for the name of their dog, and then generates a fake DNA background report on the pet dog.
 * It should assign a random percentage using five dog breeds. The total percentage should be 100%
 */

public class DogGenetics {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        String dogName = getUserInput("What is your dog's name? ");
        int[] dnaPercentages = getRandoms();
        String[] breeds = getBreeds();

        getResult(dogName, breeds, dnaPercentages);
    }

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

    public static String getUserInput(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).nextLine();
    }

    static String[] getBreeds() {
        return new String[]{"St. Bernard", "Chihuahua", "Dramatic RedNosed Asian Pug", "Common Cur", "King Doberman"};
    }

    static void getResult(String dogName, String[] breeds, int[] arr) {
        System.out.println("Well then, I have this highly reliable report on " + dogName + "'s prestigious background right here.");
        System.out.println(dogName + " is:");
        for (int i = 0; i < breeds.length; i++) {
            System.out.println(arr[i] + "% " + breeds[i]);
        }
        System.out.println("\nWow, that's QUITE the dog!");
    }
}