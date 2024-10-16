package com.mthree.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String gender;
    private String password;
    private String confirmPassword;

    public Student(Long id, int age, String firstName, String lastName, String email, String phoneNumber, String address, String city, String state, String country, String zipCode, String gender, String password, String confirmPassword) {
        this.id = id;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.gender = gender;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public Student() {
    }

}
