package main.java.com.mthree;

/**
 * Write a program that asks the user for the name of their dog, and then generates a fake DNA background report on the pet dog.
 * It should assign a random percentage using five dog breeds. The total percentage should be 100%
 */

public class DogGenetics {

    public static void main(String[] args) {
        ResultsOutput.getResult(UserInput.getUserInput(), ResultsOutput.getBreeds(), RandomizerDNA.getRandoms());
    }

}