package com.hallsymphony.service;

import com.hallsymphony.model.*;
import com.hallsymphony.util.DataStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthService {
    private static final String STAFF_FILE = "staff.txt";
    private static final String CUSTOMER_FILE = "customers.txt";

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(readUsersFromFile(STAFF_FILE));
        users.addAll(readUsersFromFile(CUSTOMER_FILE));
        return users;
    }

    private static List<User> readUsersFromFile(String fileName) {
        List<String> lines = DataStorage.readList(fileName);
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
                if (user != null) users.add(user);
            }
        }
        return users;
    }

    private static void saveUsers(List<User> allUsers) {
        List<User> staff = new ArrayList<>();
        List<User> customers = new ArrayList<>();
        for (User u : allUsers) {
            if (u instanceof Customer) customers.add(u);
            else staff.add(u);
        }
        DataStorage.saveList(STAFF_FILE, staff);
        DataStorage.saveList(CUSTOMER_FILE, customers);
    }

    public static Optional<User> login(String username, String password) {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    private static int getNextUserId(String prefix) {
        List<User> users = getAllUsers();
        int maxId = 0;
        for (User u : users) {
             if (u.getId().startsWith(prefix)) {
                try {
                    String numPart = u.getId().substring(prefix.length());
                    int num = Integer.parseInt(numPart);
                    if (num > maxId) maxId = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return maxId + 1;
    }

    public static synchronized boolean registerCustomer(String username, String password, String fullName, String contact) {
        if (getAllUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            return false;
        }
        String id = "CUST" + getNextUserId("CUST");
        Customer customer = new Customer(id, username, password, fullName, contact);
        DataStorage.appendToFile(CUSTOMER_FILE, customer);
        return true;
    }

    public static synchronized boolean addStaff(String username, String password, String fullName, String contact, String role) {
        if (getAllUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            return false;
        }
        String prefix = role.equals("Scheduler") ? "SCH" : (role.equals("Administrator") ? "ADM" : "MGR");
        String id = prefix + getNextUserId(prefix);
        User user = null;
        if (role.equals("Scheduler")) user = new Scheduler(id, username, password, fullName, contact);
        else if (role.equals("Administrator")) user = new Administrator(id, username, password, fullName, contact);
        else if (role.equals("Manager")) user = new Manager(id, username, password, fullName, contact);

        if (user != null) {
            DataStorage.appendToFile(STAFF_FILE, user);
            return true;
        }
        return false;
    }

    public static synchronized boolean updateProfile(String userId, String username, String password, String fullName, String contact) {
        List<User> users = getAllUsers();
        boolean found = false;
        for (User u : users) {
             if (u.getId().equals(userId)) {
                // Check if new username is unique (if it changed)
                if (!u.getUsername().equalsIgnoreCase(username)) {
                    if (users.stream().anyMatch(other -> other.getUsername().equalsIgnoreCase(username))) {
                        return false; // Username taken
                    }
                }
                u.setUsername(username);
                u.setPassword(password);
                u.setFullName(fullName);
                u.setContact(contact);
                found = true;
                break;
            }
        }
        if (found) {
            saveUsers(users);
        }
        return found;
    }

    public static synchronized boolean deleteUser(String userId) {
        List<User> users = getAllUsers();
        int initialSize = users.size();
        users.removeIf(u -> u.getId().equals(userId));
        if (users.size() < initialSize) {
            saveUsers(users);
            return true;
        }
        return false;
    }
}
