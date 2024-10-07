package main.java.com.mthree;

import java.util.Scanner;

public class UserInput {
    public static String getUserInput() {
        System.out.println("What is your dog's name?");
        return new Scanner(System.in).nextLine();
    }
}
