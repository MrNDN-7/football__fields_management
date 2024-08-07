package com.example.booksportfield.Models;

public class UpdateRole {
    private String addressField, PhoneNumber;
    private String userId;

    public UpdateRole() {
    }

    public UpdateRole(String addressField, String phoneNumber, String userId) {
        this.addressField = addressField;
        PhoneNumber = phoneNumber;
        this.userId = userId;
    }

    public String getAddressField() {
        return addressField;
    }

    public void setAddressField(String addressField) {
        this.addressField = addressField;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
