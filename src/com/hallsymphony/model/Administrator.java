package com.hallsymphony.model;

public class Administrator extends User {
    public Administrator(String id, String username, String password, String fullName, String contact) {
        super(id, username, password, "Administrator", fullName, contact);
    }

    public static Administrator fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6 && parts[3].equals("Administrator")) {
            return new Administrator(parts[0], parts[1], parts[2], parts[4], parts[5]);
        }
        return null;
    }
}
