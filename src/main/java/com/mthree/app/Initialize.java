package com.mthree.app;

import com.mthree.DAO.StudentDaoImpl;
import com.mthree.IO.UserIOImpl;
import com.mthree.controller.StudentControllerImpl;
import com.mthree.view.StudentViewImpl;

public class Initialize {
    public static void starting() {
        StudentDaoImpl studentDAO = new StudentDaoImpl();
        StudentViewImpl view = new StudentViewImpl();
        StudentControllerImpl controller = new StudentControllerImpl(studentDAO, view);
        UserIOImpl userIO = new UserIOImpl(controller, view);

        userIO.applicationStart();
    }
}
