package com.hallsymphony.model;

public class Manager extends User {
    public Manager(String id, String username, String password, String fullName, String contact) {
        super(id, username, password, "Manager", fullName, contact);
    }

    public static Manager fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6 && parts[3].equals("Manager")) {
            return new Manager(parts[0], parts[1], parts[2], parts[4], parts[5]);
        }
        return null;
    }
}
