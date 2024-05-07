package edu.gatech.cs6310;

import java.util.UUID;

public abstract class User {
    private String userID;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public User(String userID, String firstName, String lastName, String phoneNumber) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;

    }

    public String getUserID() { return userID; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}


