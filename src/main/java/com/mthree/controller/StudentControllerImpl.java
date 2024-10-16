package com.mthree.controller;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.model.Student;
import com.mthree.view.StudentView;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mthree.configs.LoggerConfig.getLogger;

public class StudentControllerImpl implements StudentController {
    private final Logger logger = getLogger();
    private final StudentDaoImpl studentDAO;
    private final StudentView view;

    public StudentControllerImpl(StudentDaoImpl studentDao, StudentView view) {
        this.studentDAO = studentDao;
        this.view = view;
    }

    @Override
    public void addStudent(String studentName, int studentAge) {
        studentDAO.addStudent(studentName, studentAge);
        logger.log(Level.INFO, "Student with name {0} added successfully", studentName);
    }

    @Override
    public void showAllStudents() {
        Map<Integer, Student> students = studentDAO.getAllStudents();
        view.showAllStudents(students);
    }

    @Override
    public void showStudentById(int id) {
        Student student = studentDAO.getStudent(id);
        if (student != null) {
            view.showStudent(id, student.getName(), student.getAge());
        } else {
            logger.log(Level.SEVERE, "Student with id {0} not found", id);
        }
    }

    @Override
    public void updateStudent(int id, String studentName, int studentAge) {
        boolean result = studentDAO.updateStudent(id, studentAge, studentName);
        if (result) {
            logger.log(Level.INFO, "Student with id {0} updated successfully", id);
        } else {
            logger.log(Level.SEVERE, "Student with id {0} not updated", id);
        }
    }

    @Override
    public void deleteStudent(int id) {
        boolean result = studentDAO.deleteStudent(id);
        if (result) {
            logger.log(Level.INFO, "Student with id {0} deleted successfully", id);
        } else {
            logger.log(Level.SEVERE, "Student with id {0} not deleted", id);
        }
    }
}
