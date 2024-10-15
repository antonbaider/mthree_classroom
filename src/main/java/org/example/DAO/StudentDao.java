package org.example.DAO;

import org.example.model.Student;

public interface StudentDao {

    boolean addStudent(String name, int age);

    Student getStudent(int id);

    boolean updateStudent(int id, int age, String name);

    boolean deleteStudent(int id);
}
