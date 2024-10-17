package com.mthree.IO;

import com.mthree.controller.StudentController;
import com.mthree.messages.Message;
import com.mthree.model.Student;
import com.mthree.view.StudentView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * All IO logic implementation
 */
public class UserIOImpl implements UserIO {
    private final StudentController studentController;
    private final StudentView view;
    private final Scanner scanner = new Scanner(System.in);

    public UserIOImpl(StudentController studentController, StudentView view) {
        this.studentController = studentController;
        this.view = view;
    }

    @Override
    public void applicationStart() {
        boolean exit = false;
        view.displayMessage(Message.WELCOME_MESSAGE);
        view.displayMessage(Message.LINE_SEPARATOR);

        try (Scanner scanner = new Scanner(System.in)) {
            do {
                printMainMenu();
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        createStudent();
                        break;
                    case "2":
                        showAllStudents();
                        break;
                    case "3":
                        findStudentById();
                        break;
                    case "4":
                        updateStudent();
                        break;
                    case "5":
                        deleteStudent();
                        break;
                    case "6":
                        exit = true;
                        view.displayMessage(Message.EXIT_MESSAGE);
                        break;
                    default:
                        view.displayErrorMessage(Message.INVALID_OPTION);
                        break;
                }

                if (!exit) {
                    view.displayPrompt(Message.CONTINUE_PROMPT);
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("n")) {
                        exit = true;
                    }
                }
            } while (!exit);
        }
    }

    public int getValidAge(String message) {
        while (true) {
            try {
                view.displayPrompt(message);
                String input = scanner.nextLine().trim();
                int age = Integer.parseInt(input);
                if (age >= 18 && age <= 59) {
                    return age;
                } else {
                    view.displayErrorMessage(Message.WRONG_AGE_INPUT);
                }
            } catch (InputMismatchException e) {
                view.displayErrorMessage(Message.INVALID_INPUT);
            }
        }
    }

    public String getValidName(String message) {
        view.displayPrompt(message);
        while (true) {
            String input = scanner.nextLine().trim();
            String namePattern = "^[a-zA-Z]+[a-zA-Z\\s'-]*[a-zA-Z]+$";

            if (!input.matches(namePattern) || input.length() < 3) {
                view.displayErrorMessage(Message.INVALID_INPUT);
            } else {
                return input;
            }
            view.displayPrompt(message);
        }
    }

    public int getValidInt(String message) {
        while (true) {
            view.displayPrompt(message);
            try {
                int id = scanner.nextInt();
                scanner.nextLine();
                return id;
            } catch (InputMismatchException e) {
                view.displayErrorMessage(Message.INVALID_INPUT);
                scanner.nextLine();
            }
        }
    }

    @Override
    public void createStudent() {
        view.displayMessage(Message.CREATE_STUDENT);
        view.displayMessage(Message.LINE_SEPARATOR);

        String name = getValidName(Message.ENTER_STUDENT_NAME);
        int age = getValidAge(Message.ENTER_STUDENT_AGE);

        studentController.addStudent(name, age);
        view.displaySuccessMessage(Message.STUDENT_CREATED_SUCCESS);
    }

    @Override
    public void findStudentById() {
        view.displayMessage(Message.FIND_STUDENT_BY_ID);
        view.displayMessage(Message.LINE_SEPARATOR);

        int studentId = getValidInt(Message.ENTER_STUDENT_ID);
        Student student = studentController.getStudentById(studentId);

        if (student != null) {
            studentController.showStudentById(studentId);

            boolean backToMain = false;
            while (!backToMain) {
                view.displayMessage(Message.UPDATE_STUDENT_OPTIONS);
                view.displayMessage(Message.DELETE_STUDENT_OPTIONS);
                view.displayMessage(Message.BACK_TO_MAIN_OPTIONS);
                view.displayPrompt(Message.MENU_OPTION);

                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        updateStudent(studentId);
                        backToMain = true;
                        break;
                    case 2:
                        deleteStudent(studentId);
                        backToMain = true;
                        break;
                    case 3:
                        backToMain = true;
                        break;
                    default:
                        view.displayErrorMessage(Message.INVALID_OPTION);
                        break;
                }
            }
        } else {
            view.displayErrorMessage(Message.STUDENT_NOT_FOUND_NOT_DELETED + studentId);
        }
    }

    @Override
    public void showAllStudents() {
        view.displayMessage(Message.SHOW_ALL_STUDENTS);
        view.displayMessage(Message.LINE_SEPARATOR);
        studentController.showAllStudents();
    }

    @Override
    public void updateStudent() {
        view.displayMessage(Message.UPDATE_STUDENT_INFO);
        view.displayMessage(Message.LINE_SEPARATOR);

        int studentId = getValidInt(Message.ENTER_STUDENT_ID);

        Student student = studentController.getStudentById(studentId);

        if (student != null) {
            String newName = getValidName(Message.ENTER_NEW_NAME);
            int newAge = getValidAge(Message.ENTER_NEW_AGE);

            studentController.updateStudent(studentId, newName, newAge);
            view.displaySuccessMessage(Message.STUDENT_UPDATED_SUCCESS);
        } else {
            view.displayErrorMessage(Message.STUDENT_NOT_FOUND_NOT_DELETED + studentId);
        }
    }

    public void updateStudent(int id) {
        view.displayMessage(Message.UPDATE_STUDENT_INFO);
        view.displayMessage(Message.LINE_SEPARATOR);

        String newName = getValidName(Message.ENTER_NEW_NAME);
        int newAge = getValidAge(Message.ENTER_NEW_AGE);

        studentController.updateStudent(id, newName, newAge);
        view.displaySuccessMessage(Message.STUDENT_UPDATED_SUCCESS);
    }

    @Override
    public void deleteStudent() {
        view.displayMessage(Message.DELETE_STUDENT);
        view.displayMessage(Message.LINE_SEPARATOR);

        int studentId = getValidInt(Message.ENTER_STUDENT_ID);

        boolean isDeleted = studentController.deleteStudent(studentId);

        if (isDeleted) {
            view.displaySuccessMessage(Message.STUDENT_DELETED_SUCCESS);
        } else {
            view.displayErrorMessage(Message.STUDENT_NOT_FOUND_NOT_DELETED + studentId);
        }
    }

    public void deleteStudent(int id) {
        view.displayMessage(Message.DELETE_STUDENT);
        view.displayMessage(Message.LINE_SEPARATOR);

        studentController.deleteStudent(id);
        view.displaySuccessMessage(Message.STUDENT_DELETED_SUCCESS);
    }

    @Override
    public void printMainMenu() {
        String menuFormat = "| %-5s | %-25s |%n";

        System.out.format("+-------+---------------------------+%n");
        System.out.format("| No.   | Option                    |%n");
        System.out.format("+-------+---------------------------+%n");
        System.out.format(menuFormat, "1", Message.MENU_OPTION_CREATE);
        System.out.format(menuFormat, "2", Message.MENU_OPTION_SHOW);
        System.out.format(menuFormat, "3", Message.MENU_OPTION_FIND);
        System.out.format(menuFormat, "4", Message.MENU_OPTION_UPDATE);
        System.out.format(menuFormat, "5", Message.MENU_OPTION_DELETE);
        System.out.format(menuFormat, "6", Message.MENU_OPTION_EXIT);
        System.out.format("+-------+---------------------------+%n");
        System.out.println(Message.MENU_OPTION);
    }
}