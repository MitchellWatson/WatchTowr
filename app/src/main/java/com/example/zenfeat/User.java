package com.example.zenfeat;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String age;
    private String occupation;
    private String email;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String userId, String firstName, String lastName, String age, String occupation, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.occupation = occupation;
        this.email = email;
    }

    // Getters for all properties

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getEmail() {
        return email;
    }
}