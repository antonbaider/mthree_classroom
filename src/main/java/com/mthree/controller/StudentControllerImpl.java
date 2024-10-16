package com.mthree.controller;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.model.Student;
import com.mthree.view.StudentView;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mthree.configs.LoggerConfig.getLogger;

/**
 * Injecting dao with view to controller
 */
public class StudentControllerImpl implements StudentController {
    private final Logger logger = getLogger();
    private final StudentDaoImpl studentDAO;
    private final StudentView view;

    public StudentControllerImpl(StudentDaoImpl studentDao, StudentView view) {
        this.studentDAO = studentDao;
        this.view = view;
    }

    @Override
    public boolean addStudent(String studentName, int studentAge) {
        studentDAO.addStudent(studentName, studentAge);
        logger.log(Level.INFO, "Student with name {0} added successfully", studentName);
        return true;
    }

    @Override
    public void showAllStudents() {
        view.showAllStudents(getAllStudents());
    }

    @Override
    public Map<Integer, Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.getStudent(id);
    }

    @Override
    public void showStudentById(int id) {
        Student student = getStudentById(id);
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
    public boolean deleteStudent(int id) {
        boolean result = studentDAO.deleteStudent(id);
        if (result) {
            logger.log(Level.INFO, "Student with id {0} deleted successfully", id);
            return true;
        } else {
            logger.log(Level.SEVERE, "Student with id {0} not deleted", id);
            return false;
        }
    }
}
