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

        File schedFile = new File("data/db/schedules.txt");
        if (!schedFile.exists() || schedFile.length() == 0) {
            System.out.println("Initializing default schedules...");
            java.time.LocalDateTime start = java.time.LocalDateTime.of(2024, 7, 1, 8, 0);
            java.time.LocalDateTime end = java.time.LocalDateTime.of(2024, 7, 31, 18, 0);
            HallService.addSchedule(new com.hallsymphony.model.Schedule("SCH1", "HALL1", start, end, "AVAILABILITY", "July Slots"));
            HallService.addSchedule(new com.hallsymphony.model.Schedule("SCH2", "HALL2", start, end, "AVAILABILITY", "July Slots"));
            HallService.addSchedule(new com.hallsymphony.model.Schedule("SCH3", "HALL3", start, end, "AVAILABILITY", "July Slots"));
        }

        File issueFile = new File("data/db/issues.txt");
        if (!issueFile.exists() || issueFile.length() == 0) {
            System.out.println("Initializing default issues...");
            com.hallsymphony.service.IssueService.raiseIssue("CUST1", "BOOK1", "A/C not working in Grand Auditorium");
        }
    }
}
