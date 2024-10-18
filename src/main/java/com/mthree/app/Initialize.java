package com.mthree.app;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.IO.UserIOImpl;
import com.mthree.controller.StudentControllerImpl;
import com.mthree.view.StudentViewImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

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
            Thread.sleep(100);
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
                Thread.sleep(100);
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
//        pom -> appcontext -> annotations
//        1 line solution -> start Spring-context->core->bean->oap etc, start beanFactory - context - IOC container, start ComponentScan, start DI, start lifecycle of Beans, start create Bean, start interact Beans, start business logic, start handle smth, start return results, destroy()
        ApplicationContext context = new AnnotationConfigApplicationContext("com.mthree");

//        @BEFORE we are passing parameters -> dependencies:
//
//        StudentControllerImpl controller = new StudentControllerImpl(studentDAO, view);
//
//        @AFTER: we are passing nothing
        StudentControllerImpl controller = context.getBean(StudentControllerImpl.class);
//        StudentDaoImpl studentDAO = new StudentDaoImpl();
        StudentDaoImpl studentDAO = context.getBean(StudentDaoImpl.class);
//        StudentViewImpl view = new StudentViewImpl();
        StudentViewImpl studentView = context.getBean(StudentViewImpl.class);
//        UserIOImpl userIO = new UserIOImpl(controller, view);
        UserIOImpl userIO = context.getBean(UserIOImpl.class);


        displayLogo();
        displayLoadingMessagesWithPercentage();
        userIO.applicationStart();
    }
}