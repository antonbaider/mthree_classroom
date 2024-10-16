package com.mthree.DAO;

import com.mthree.model.Student;

import java.util.Map;

public interface StudentDao {

    boolean addStudent(String name, int age);

    Student getStudent(int id);

    boolean updateStudent(int id, int age, String name);

    boolean deleteStudent(int id);

    Map<Integer, Student> getAllStudents();
}
