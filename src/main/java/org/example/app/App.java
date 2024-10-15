package org.example.app;

import org.example.DAO.StudentDaoImpl;
import org.example.controller.StudentController;
import org.example.model.Student;
import org.example.view.StudentView;

public class App {

    public static void main(String[] args) {
        StudentDaoImpl studentDao = new StudentDaoImpl();

        int id = 80;

        //create
        System.out.println("\nCreate Student");
        studentDao.addStudent("Anton", 23);

        //read
        System.out.println("\nGet Student");

        Student s1 = studentDao.getStudent(id);
        StudentView view = new StudentView();
        StudentController controller = new StudentController(s1, view);
        controller.updateView();

        //update
        System.out.println("\nUpdate Student");

        studentDao.updateStudent(id, 45, "New Name");
        s1 = studentDao.getStudent(id);
        controller = new StudentController(s1, view);
        controller.updateView();

        //delete
        System.out.println("\nDelete Student");

        studentDao.deleteStudent(id);
        controller.updateView();


    }
}
