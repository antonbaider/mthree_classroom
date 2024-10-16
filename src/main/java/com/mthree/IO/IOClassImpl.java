package com.mthree.IO;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.model.Student;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class IOClassImpl implements IOClass {
    static Scanner scanner = new Scanner(System.in);
    static StudentDaoImpl studentDao = new StudentDaoImpl();

    static void createStudent() {
        System.out.println("Create student");
        System.out.println("============================");
        String name = getValidName("Enter name: ");
        int age = getValidAge("Enter age: ");
        scanner.nextLine();
        studentDao.addStudent(name, age);
    }

    static void readById() {
        System.out.println("Find student");
        System.out.println("============================");
        int studentId = getValidInt("Enter student ID to find: ");
        scanner.nextLine();
        Student student = studentDao.getStudent(studentId);
        if (student != null) {
            printStudents(student, studentId);
        } else {
            System.out.println("\nStudent not found.\n");
        }
    }

    static void readAll() {
        System.out.println("Show all students");
        System.out.println("============================");
        Map<Integer, Student> students = studentDao.getAllStudents();
        printStudents(students);
    }

    static void updateStudent() {
        System.out.println("Update student");
        System.out.println("============================");
        System.out.println("Enter student ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        String newName = getValidName("Enter new name: ");
        scanner.nextLine();
        int newAge = getValidAge("Enter new age: ");
        scanner.nextLine();
        studentDao.updateStudent(updateId, newAge, newName);
    }

    static void deleteById() {
        System.out.println("Delete student");
        System.out.println("============================");
        int deleteId = getValidInt("Enter student ID to delete: ");
        scanner.nextLine();
        studentDao.deleteStudent(deleteId);
    }

    static void printMainMenu() {
        String menuFormat = "| %-5s | %-25s |%n";

        System.out.format("+-------+---------------------------+%n");
        System.out.format("| No.   | Option                    |%n");
        System.out.format("+-------+---------------------------+%n");
        System.out.format(menuFormat, "1", "Create student");
        System.out.format(menuFormat, "2", "Show all students");
        System.out.format(menuFormat, "3", "Find student");
        System.out.format(menuFormat, "4", "Update student");
        System.out.format(menuFormat, "5", "Delete student");
        System.out.format(menuFormat, "6", "Exit");
        System.out.format("+-------+---------------------------+%n");
        System.out.println("\nPlease choose your option: ");
    }


    public static void printStudents(Map<Integer, Student> students) {
        String headerFormat = "| %-5s | %-20s | %-5s |%n";
        System.out.format("+-------+----------------------+-------+%n");
        System.out.format("| Id.   | Name                 | Age   |%n");
        System.out.format("+-------+----------------------+-------+%n");

        String rowFormat = "| %-5d | %-20s | %-5d |%n";
        for (Map.Entry<Integer, Student> entry : students.entrySet()) {
            Integer rowNumber = entry.getKey();
            Student student = entry.getValue();
            System.out.format(rowFormat, student.getId(), student.getName(), student.getAge());
        }

        System.out.format("+-------+----------------------+-------+%n");
    }

    public static void printStudents(Student student, int studentId) {
        String headerFormat = "| %-5s | %-20s | %-5s |%n";
        System.out.format("+-------+----------------------+-------+%n");
        System.out.format("| Id.   | Name                 | Age   |%n");
        System.out.format("+-------+----------------------+-------+%n");

        String rowFormat = "| %-5d | %-20s | %-5d |%n";

        System.out.format(rowFormat, studentId, student.getName(), student.getAge());


        System.out.format("+-------+----------------------+-------+%n");
    }

    public static int getValidAge(String message) {
        while (true) {
            try {
                System.out.println(message);
                int input = scanner.nextInt();
                if (input >= 18 && input <= 59) {
                    return input;
                } else {
                    System.out.println("Wrong input. Try again. Range of age is: from 18 till 59.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
    }

    public static String getValidName(String message) {
        System.out.println(message);
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            System.out.println("Invalid input. Try again.");
        }
        return input;
    }

    public static int getValidInt(String message) {
        while (true) {
            try {
                System.out.println(message);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
    }

}
