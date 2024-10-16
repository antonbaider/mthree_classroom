package com.mthree.DAO;

import com.mthree.configs.DBConfig;
import com.mthree.exceptions.Exceptions.DaoException;
import com.mthree.model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mthree.configs.LoggerConfig.getLogger;

public class StudentDaoImpl extends DBConfig implements StudentDao {
    private final Logger logger = getLogger();


    @Override
    public boolean addStudent(String name, int age) {
        String sql = "INSERT INTO student (name, age) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = startConnection(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.executeUpdate();
            logger.info("Student added successfully: " + name + " " + age + " years old");
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding student: " + name, e);
            return false;
        }
    }

    @Override
    public Student getStudent(int id) {
        String sql = "SELECT name, age FROM student WHERE id = ?";
        Student student = null;
        try (PreparedStatement preparedStatement = startConnection(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                student = new Student(age, name);
                logger.info("Student found: " + name + " " + age + " years old with id: " + id);
            } else {
                logger.warning("No student found with id: " + id);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving student with id: " + id, e);
        }
        return student;
    }

    @Override
    public boolean updateStudent(int id, int age, String name) {
        String sql = "UPDATE student SET name = ?, age = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = startConnection(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, id);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                logger.info("Student updated: " + name + ", " + age + " years old with id: " + id);
                return true;
            } else {
                logger.warning("No student found to update with id: " + id);
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student with id: " + id, e);
            return false;
        }
    }

    @Override
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM student WHERE id = ?";

        try (PreparedStatement preparedStatement = startConnection(sql)) {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                logger.info("Student deleted with id: " + id);
                return true;
            } else {
                logger.warning("No student found to delete with id: " + id);
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting student with id: " + id, e);
            throw new DaoException("Failed to delete student", e);
        }
    }

    public Map<Integer, Student> getAllStudents() {
        String sql = "SELECT * FROM student";
        Map<Integer, Student> students = new LinkedHashMap<>();
        try (PreparedStatement preparedStatement = startConnection(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int age = resultSet.getInt("age");
                String name = resultSet.getString("name");
                Student student;
                student = new Student(id, age, name);
                students.put(id, student);
            }
            if (students.isEmpty()) {
                logger.warning("No students found");
            } else {
                logger.info("Students found: " + students);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving students: " + e);
        }
        return students;
    }
}
