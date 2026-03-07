package com.hallsymphony.service;

import com.hallsymphony.model.*;
import com.hallsymphony.util.DataStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthService {
    private static final String USER_FILE = "users.txt";

    public static List<User> getAllUsers() {
        List<String> lines = DataStorage.readList(USER_FILE);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                String role = parts[3];
                User user = null;
                switch (role) {
                    case "Customer": user = Customer.fromString(line); break;
                    case "Scheduler": user = Scheduler.fromString(line); break;
                    case "Administrator": user = Administrator.fromString(line); break;
                    case "Manager": user = Manager.fromString(line); break;
                }
                if (user != null) users.add(user); // Bug fix: null safety
            }
        }
        return users;
    }

    public static Optional<User> login(String username, String password) {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    // Bug fix: Use max-ID to avoid collisions after deletions
    private static int getNextUserId(String prefix) {
        List<User> users = getAllUsers();
        int maxId = 0;
        for (User u : users) {
            try {
                String numPart = u.getId().replaceAll("[^0-9]", "");
                int num = Integer.parseInt(numPart);
                if (num > maxId) maxId = num;
            } catch (NumberFormatException ignored) {}
        }
        return maxId + 1;
    }

    public static boolean registerCustomer(String username, String password, String fullName, String contact) {
        List<User> users = getAllUsers();
        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            return false; // User already exists
        }
        String id = "CUST" + getNextUserId("CUST");
        Customer customer = new Customer(id, username, password, fullName, contact);
        DataStorage.appendToFile(USER_FILE, customer);
        return true;
    }

    public static boolean addStaff(String username, String password, String fullName, String contact, String role) {
        List<User> users = getAllUsers();
        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            return false;
        }
        String prefix = role.substring(0, 3).toUpperCase();
        String id = prefix + getNextUserId(prefix);
        User user = null;
        if (role.equals("Scheduler")) user = new Scheduler(id, username, password, fullName, contact);
        else if (role.equals("Administrator")) user = new Administrator(id, username, password, fullName, contact);
        else if (role.equals("Manager")) user = new Manager(id, username, password, fullName, contact);

        if (user != null) {
            DataStorage.appendToFile(USER_FILE, user);
            return true;
        }
        return false;
    }

    public static boolean updateProfile(String userId, String fullName, String contact) {
        List<User> users = getAllUsers();
        boolean found = false;
        for (User u : users) {
            if (u.getId().equals(userId)) {
                u.setFullName(fullName);
                u.setContact(contact);
                found = true;
                break;
            }
        }
        if (found) {
            DataStorage.saveList(USER_FILE, users);
        }
        return found;
    }

    public static boolean deleteUser(String userId) {
        List<User> users = getAllUsers();
        int initialSize = users.size();
        users.removeIf(u -> u.getId().equals(userId));
        if (users.size() < initialSize) {
            DataStorage.saveList(USER_FILE, users);
            return true;
        }
        return false;
    }
}
