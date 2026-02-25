package com.hallsymphony.model;

public class Customer extends User {
    public Customer(String id, String username, String password, String fullName, String contact) {
        super(id, username, password, "Customer", fullName, contact);
    }

    public static Customer fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6 && parts[3].equals("Customer")) {
            return new Customer(parts[0], parts[1], parts[2], parts[4], parts[5]);
        }
        return null;
    }
}
