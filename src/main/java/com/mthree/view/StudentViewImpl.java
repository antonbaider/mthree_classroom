package com.mthree.view;

import com.mthree.model.Student;

import java.util.Map;

public class StudentViewImpl implements StudentView {
    @Override
    public void showStudent(int id, String name, int age) {
        String headerFormat = "| %-5s | %-20s | %-5s |%n";
        System.out.format("+-------+----------------------+-------+%n");
        System.out.format("| Id.   | Name                 | Age   |%n");
        System.out.format("+-------+----------------------+-------+%n");
        String rowFormat = "| %-5d | %-20s | %-5d |%n";
        System.out.format(rowFormat, id, name, age);
        System.out.format("+-------+----------------------+-------+%n");
    }

    @Override
    public void showAllStudents(Map<Integer, Student> students) {
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

    @Override
    public void displaySuccessMessage(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    @Override
    public void displayErrorMessage(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayPrompt(String message) {
        System.out.print(message);
    }
}
