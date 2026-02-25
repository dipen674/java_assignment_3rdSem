package com.hallsymphony.model;

public class Scheduler extends User {
    public Scheduler(String id, String username, String password, String fullName, String contact) {
        super(id, username, password, "Scheduler", fullName, contact);
    }

    public static Scheduler fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6 && parts[3].equals("Scheduler")) {
            return new Scheduler(parts[0], parts[1], parts[2], parts[4], parts[5]);
        }
        return null;
    }
}
