package com.mthree;

//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.context.annotation.ComponentScan;

import static com.mthree.app.Initialize.start;

/**
 * Main entry point for the Student Management Application.
 * MVC Console-based Student Management Application with CRUD operations.
 *
 * <p>This application allows users to create, read, update, and delete student
 * records through a console interface. It interacts with a relational database
 * to persist data and provides functionality such as:
 * <ul>
 *   <li>Database Management System (DBMS) integration</li>
 *   <li>Logging of operations and events</li>
 *   <li>Localization support for messages</li>
 *   <li>Input/Output (IO) validation for user inputs</li>
 *   <li>Error Handling</li>
 * </ul>
 *
 * <p>Usage Example:</p>
 * <pre>{@code
 * public static void main(String[] args) {
 *     start();
 * }
 * }</pre>
 *
 * @author AntonBaider
 * @version 1.0
 * @since 2024-10-16
 */
public class App {
    public static void main(String[] args) {
        start();
    }
}