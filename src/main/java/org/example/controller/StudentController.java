package org.example.controller;

import org.example.model.Student;
import org.example.view.StudentView;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.configs.LoggerConfig.getLogger;

public class StudentController {
    private final Logger logger = getLogger();
    private Student model;
    private StudentView view;

    public StudentController(Student model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public Student getModel() {
        return model;
    }

    public void setModel(Student model) {
        this.model = model;
    }

    public StudentView getView() {
        return view;
    }

    public void setView(StudentView view) {
        this.view = view;
    }

    public void updateView() {
        try {
            view.displayStudentDetails(model.getName(), model.getAge());
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "No student found");
        }
    }
}
