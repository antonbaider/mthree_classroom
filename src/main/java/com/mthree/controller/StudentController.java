package com.mthree.controller;

public interface StudentController {
    void showAllStudents();

    void showStudentById(int id);

    void addStudent(String studentName, int studentAge);

    void updateStudent(int id, String studentName, int studentAge);

    void deleteStudent(int id);
}
