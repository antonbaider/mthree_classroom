package com.mthree.app;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.IO.UserIOImpl;
import com.mthree.controller.StudentControllerImpl;
import com.mthree.view.StudentViewImpl;

/**
 * This class starts our app by dependency injection with
 * "controller", "dao", "view", and "userIo" layers
 */
public class Initialize {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private static void displayLogo() {
        System.out.println("MTHREE");
        System.out.println("Loading Student Data Application:");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void displayLoadingMessagesWithPercentage() {
        String[] loadingMessages = {"Loading Student Data Application, please wait...", "Preparing the classroom... 🎓", "Fetching records from the database... 🗂️", "Sharpening the pencils and opening the books... ✏️📚", "Unlocking the classroom doors... 🔓", "Hang tight! We're setting things up... ⏳"};
        int[] percentages = getNumberLoading(loadingMessages.length);

        for (int i = 0; i < loadingMessages.length; i++) {
            System.out.println(ANSI_GREEN + percentages[i] + "% - " + loadingMessages[i] + ANSI_RESET);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static int[] getNumberLoading(int num) {
        int[] number = new int[num];
        int sum = 100 / num;
        int remain = 100 % num;

        int load = 0;
        for (int i = 0; i < num; i++) {
            load += sum;
            number[i] = load;

            if (i < remain) {
                load++;
                number[i] = load;
            }
        }
        return number;
    }

    public static void start() {
        StudentDaoImpl studentDAO = new StudentDaoImpl();
        StudentViewImpl view = new StudentViewImpl();
        StudentControllerImpl controller = new StudentControllerImpl(studentDAO, view);
        UserIOImpl userIO = new UserIOImpl(controller, view);
        displayLogo();
        displayLoadingMessagesWithPercentage();
        userIO.applicationStart();
    }

}