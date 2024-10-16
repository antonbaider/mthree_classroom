//package com.mthree.IO;
//
//import com.mthree.DAO.StudentDaoImpl;
//import com.mthree.model.Student;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class UserIOImplTest {
//    UserIOImpl ioClass;
//    StudentDaoImpl studentDao;
//
//    @BeforeEach
//    public void setUp() {
//        ioClass = new UserIOImpl();
//        studentDao = new StudentDaoImpl();
//    }
//
//    @Test
//    @DisplayName("Create student")
//    public void testCreateStudent() {
//        boolean result = studentDao.addStudent("Student Name", 19);
//
//        Assertions.assertTrue(result);
//    }
//
//    @Test
//    @DisplayName("Read one by ID")
//    public void testReadById() {
//        studentDao.addStudent("Last Student", 19);
//        Map<Integer, Student> listOfStudents = studentDao.getAllStudents();
//        List<Integer> keys = new ArrayList<>(listOfStudents.keySet());
//        int lastKey = keys.get(keys.size() - 1);
//        Student lastStudent = listOfStudents.get(lastKey);
//        Student dbStudent = studentDao.getStudent(lastKey);
//        System.out.println(lastStudent + " equals " + dbStudent + "?");
//
//        Assertions.assertEquals(lastStudent, studentDao.getStudent(lastKey));
//    }
//
//    @Test
//    @DisplayName("Read All")
//    public void testReadAll() {
//        studentDao.addStudent("Student Name", 19);
//        Map<Integer, Student> listOfStudents = studentDao.getAllStudents();
//
//        Assertions.assertNotNull(listOfStudents);
//    }
//
//    @Test
//    @DisplayName("Update by ID")
//    public void testUpdateStudent() {
//        studentDao.addStudent("NameForUpdate", 18);
//        Map<Integer, Student> listOfStudents = studentDao.getAllStudents();
//
//        Integer lastKey = null;
//        Student lastStudent = null;
//        for (Map.Entry<Integer, Student> entry : listOfStudents.entrySet()) {
//            lastKey = entry.getKey();
//            lastStudent = entry.getValue();
//        }
//
//        assert lastStudent != null;
//        System.out.println("Student " + lastStudent.getName() + " " + lastStudent.getAge() + " y.o. is our last student");
//
//        Student dbLastStudent = studentDao.getStudent(lastKey);
//
//        Assertions.assertEquals(dbLastStudent, lastStudent);
//
//        studentDao.updateStudent(lastKey, 58, "Just Updated Name");
//
//        Assertions.assertNotEquals(dbLastStudent, studentDao.getStudent(lastKey));
//    }
//
//    @Test
//    @DisplayName("Delete by ID")
//    public void testDeleteById() {
//        studentDao.addStudent("Student for Delete", 38);
//        Map<Integer, Student> listOfStudents = studentDao.getAllStudents();
//
//        Integer lastKey = null;
//        Student lastStudent = null;
//        for (Map.Entry<Integer, Student> entry : listOfStudents.entrySet()) {
//            lastKey = entry.getKey();
//            lastStudent = entry.getValue();
//        }
//        boolean deleted = studentDao.deleteStudent(lastKey);
//
//        Assertions.assertTrue(deleted);
//
//        studentDao.getStudent(lastKey);
//
//        Assertions.assertNull(studentDao.getStudent(lastKey));
//
//    }
//}