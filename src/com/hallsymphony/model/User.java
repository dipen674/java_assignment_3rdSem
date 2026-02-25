package com.hallsymphony.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String contact;

    public User(String id, String username, String password, String role, String fullName, String contact) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.contact = contact;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return String.join("|", id, username, password, role, fullName, contact);
    }
}
