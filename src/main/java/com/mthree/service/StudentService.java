package com.mthree.service;

import com.mthree.model.*;
import com.mthree.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setAge(updatedStudent.getAge());
                    existingStudent.setFirstName(updatedStudent.getFirstName());
                    existingStudent.setLastName(updatedStudent.getLastName());
                    existingStudent.setEmail(updatedStudent.getEmail());
                    existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
                    existingStudent.setAddress(updatedStudent.getAddress());
                    existingStudent.setCity(updatedStudent.getCity());
                    existingStudent.setState(updatedStudent.getState());
                    existingStudent.setCountry(updatedStudent.getCountry());
                    existingStudent.setZipCode(updatedStudent.getZipCode());
                    existingStudent.setGender(updatedStudent.getGender());
                    existingStudent.setPassword(updatedStudent.getPassword());
                    existingStudent.setConfirmPassword(updatedStudent.getConfirmPassword());
                    return studentRepository.save(existingStudent);
                });
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

}
