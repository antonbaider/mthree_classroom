package com.mthree.IO;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.controller.StudentControllerImpl;
import com.mthree.model.Student;
import com.mthree.view.StudentViewImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class UserIOImplTest {
    UserIOImpl ioClass;
    StudentControllerImpl studentController;
    StudentViewImpl studentView;
    StudentDaoImpl studentDao;

    @BeforeEach
    public void setUp() {
        studentView = new StudentViewImpl();
        studentDao = new StudentDaoImpl();
        studentController = new StudentControllerImpl(studentDao, studentView);
        ioClass = new UserIOImpl(studentController, studentView);
    }

    @Test
    @DisplayName("Create student")
    public void testCreateStudent() {
        boolean result = studentController.addStudent("Student Name", 19);
        Assertions.assertTrue(result);

        Map<Integer, Student> studentMap = studentController.getAllStudents();

        Assertions.assertFalse(studentMap.isEmpty());

        Integer lastKey = studentMap.keySet().stream().max(Integer::compareTo).orElseThrow();

        Student student = studentMap.get(lastKey);

        Assertions.assertEquals("Student Name", student.getName());
        Assertions.assertEquals(19, student.getAge());
    }

    @Test
    @DisplayName("Read one by ID")
    public void testReadById() {
        studentController.addStudent("Student Name 2", 29);

        Map<Integer, Student> studentMap = studentController.getAllStudents();
        Integer lastKey = studentMap.keySet().stream().max(Integer::compareTo).orElseThrow();

        Student student = studentMap.get(lastKey);
        Assertions.assertEquals("Student Name 2", student.getName());
        Assertions.assertEquals(29, student.getAge());

        Student lastDBStudent = studentController.getStudentById(lastKey);

        Assertions.assertEquals("Student Name 2", lastDBStudent.getName());
        Assertions.assertEquals(29, lastDBStudent.getAge());
    }

    @Test
    @DisplayName("Read All")
    public void testReadAll() {
        studentController.addStudent("Student Name", 19);
        Map<Integer, Student> listOfStudents = studentController.getAllStudents();

        Assertions.assertNotNull(listOfStudents);
    }

    @Test
    @DisplayName("Update by ID")
    public void testUpdateStudent() {
        studentController.addStudent("NameForUpdate", 18);
        Map<Integer, Student> listOfStudents = studentController.getAllStudents();

        Integer lastKey = listOfStudents.keySet().stream().max(Integer::compareTo).orElseThrow();
        Student lastStudent = listOfStudents.get(lastKey);

        studentController.updateStudent(lastKey, "Updated Name", 30);

        Student dbLastStudent = studentController.getStudentById(lastKey);

        Assertions.assertNotEquals(lastStudent, dbLastStudent, "Student should be updated.");
        Assertions.assertEquals("Updated Name", dbLastStudent.getName(), "Name should be updated.");
        Assertions.assertEquals(30, dbLastStudent.getAge(), "Age should be updated.");
    }

    @Test
    @DisplayName("Delete by ID")
    public void testDeleteById() {
        studentController.addStudent("Student to Delete", 21);

        Map<Integer, Student> students = studentController.getAllStudents();
        Integer lastKey = students.keySet().stream().max(Integer::compare).orElseThrow();

        boolean deleted = studentController.deleteStudent(lastKey);

        Assertions.assertTrue(deleted, "Student should be deleted.");

        Student deletedStudent = studentController.getStudentById(lastKey);

        Assertions.assertNull(deletedStudent, "Deleted student should not be found.");
    }

}