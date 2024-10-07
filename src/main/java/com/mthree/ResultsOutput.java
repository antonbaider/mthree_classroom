package main.java.com.mthree;

public class ResultsOutput {
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
