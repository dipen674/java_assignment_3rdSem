package com.hallsymphony.util;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.service.HallService;
import java.io.File;

public class DataInitializer {
    public static void initialize() {
        File dbDir = new File("data/db");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        File userFile = new File("data/db/users.txt");
        if (!userFile.exists() || userFile.length() == 0) {
            System.out.println("Initializing default users...");
            AuthService.addStaff("admin", "admin123", "System Admin", "0123456789", "Administrator");
            AuthService.addStaff("manager", "manager123", "Chief Manager", "0123456788", "Manager");
            AuthService.addStaff("staff", "staff123", "Lead Scheduler", "0123456787", "Scheduler");
        }

        File hallFile = new File("data/db/halls.txt");
        if (!hallFile.exists() || hallFile.length() == 0) {
            System.out.println("Initializing default halls...");
            HallService.addHall("Grand Auditorium", "Auditorium", 1000, 300.0, "Large hall for conferences");
            HallService.addHall("Royal Banquet", "Banquet Hall", 300, 100.0, "Perfect for weddings");
            HallService.addHall("Meeting Room A", "Meeting Room", 30, 50.0, "Small room for discussions");
        }
    }
}
