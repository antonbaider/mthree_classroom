package com.mthree.controller;

import com.mthree.model.Student;

import java.util.Map;

public interface StudentController {
    void showAllStudents();

    void showStudentById(int id);

    boolean addStudent(String studentName, int studentAge);

    void updateStudent(int id, String studentName, int studentAge);

    boolean deleteStudent(int id);

    Map<Integer, Student> getAllStudents();

    Student getStudentById(int id);
}
