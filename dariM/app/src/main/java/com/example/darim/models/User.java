package com.example.darim.models;

public class User {
    private int id;
    private String username;
    private String type;
    private String fullName;
    private String email;

    public User(int id, String username, String type, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getType() { return type; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}