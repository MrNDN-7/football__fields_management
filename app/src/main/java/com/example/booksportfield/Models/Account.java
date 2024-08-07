package com.example.booksportfield.Models;

import java.io.Serializable;

public class Account implements Serializable {

    private String name;
    private String phoneNumber;
    private String email;
    private String gender;
    private String urlImage;
    private String dateOfBirth;
    private String role;
    private String username;
    private String password;

    public Account() {
    }

    public Account(String name, String phoneNumber, String email, String gender, String urlImage, String dateOfBirth, String role, String username, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.urlImage = urlImage;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.username = username;
        this.password = password;
    }
    public Account(String name, String email, String password, String role, String urlImage)
    {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role=role;
        this.urlImage=urlImage;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
