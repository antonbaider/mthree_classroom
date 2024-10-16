package com.mthree.view;

import com.mthree.model.Student;

import java.util.Map;

public interface StudentView {
    void showStudent(int id, String name, int age);

    void showAllStudents(Map<Integer, Student> students);

    void displaySuccessMessage(String message);

    void displayErrorMessage(String message);

    void displayMessage(String message);

    void displayPrompt(String message);
}
