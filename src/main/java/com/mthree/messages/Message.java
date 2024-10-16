package com.mthree.messages;

/**
 * Localisation part
 */
public interface Message {
    // General messages
    String WELCOME_MESSAGE = "WELCOME TO STUDENT-DATA APPLICATION";
    String EXIT_MESSAGE = "Exiting the application...";
    String INVALID_OPTION = "Invalid option. Please try again.";
    String CONTINUE_PROMPT = "Do you want to continue? (y/n): ";
    // Create a student
    String CREATE_STUDENT = "Create a new student";
    String ENTER_STUDENT_NAME = "Enter student name: ";
    String ENTER_STUDENT_AGE = "Enter student age: ";
    String STUDENT_CREATED_SUCCESS = "Student created successfully.";
    // Find a student
    String FIND_STUDENT_BY_ID = "Find a student by ID";
    String ENTER_STUDENT_ID = "Enter student ID: ";
    String UPDATE_STUDENT_OPTIONS = "1. Update this student";
    String DELETE_STUDENT_OPTIONS = "2. Delete this student";
    String BACK_TO_MAIN_OPTIONS = "3. Back to main";
    // Show all students
    String SHOW_ALL_STUDENTS = "Show all students";
    // Update student
    String UPDATE_STUDENT_INFO = "Update student information";
    String ENTER_NEW_NAME = "Enter new name: ";
    String ENTER_NEW_AGE = "Enter new age: ";
    String STUDENT_UPDATED_SUCCESS = "Student updated successfully.";
    // Delete student
    String DELETE_STUDENT = "Delete a student";
    String STUDENT_DELETED_SUCCESS = "Student deleted successfully.";
    // Error messages
    String INVALID_INPUT = "Invalid input. Please try again.";
    String WRONG_AGE_INPUT = "Wrong input. Try again. Range of age is: from 18 till 59.";
    // Menu options
    String MENU_OPTION = "Please choose an option:";
    String MENU_OPTION_CREATE = "1. Create student";
    String MENU_OPTION_SHOW = "2. Show all students";
    String MENU_OPTION_FIND = "3. Find student";
    String MENU_OPTION_UPDATE = "4. Update student";
    String MENU_OPTION_DELETE = "5. Delete student";
    String MENU_OPTION_EXIT = "6. Exit";
    // Line separator
    String LINE_SEPARATOR = "===================================";
}
