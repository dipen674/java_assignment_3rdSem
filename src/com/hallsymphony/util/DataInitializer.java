package com.hallsymphony.util;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.service.HallService;
import java.time.LocalDateTime;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataInitializer {
    public static void initialize() {
        File dbDir = new File("data/db");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        // ─── MIGRATION & INITIALIZATION ───
        File legacyFile = new File("data/db/users.txt");
        File staffFile = new File("data/db/staff.txt");
        File customerFile = new File("data/db/customers.txt");

        // 1. Check for legacy data to migrate
        if (legacyFile.exists() && (!staffFile.exists() || staffFile.length() == 0)) {
            System.out.println("Migrating legacy users.txt to new storage format...");
            List<String> lines = DataStorage.readList("users.txt");
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    if (parts[3].equals("Customer")) {
                        DataStorage.appendToFile("customers.txt", line);
                    } else {
                        DataStorage.appendToFile("staff.txt", line);
                    }
                }
            }
            // Optional: Backup or delete legacy file? Let's rename it.
            legacyFile.renameTo(new File("data/db/users.txt.bak"));
        }

        // 2. If still empty, seed default users
        if ((!staffFile.exists() || staffFile.length() == 0) && (!customerFile.exists() || customerFile.length() == 0)) {
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
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = LocalDateTime.of(now.getYear(), 1, 1, 8, 0);
            LocalDateTime end = LocalDateTime.of(now.getYear() + 1, 12, 31, 18, 0);
            HallService.addSchedule(new com.hallsymphony.model.Schedule("SCH1", "HALL1", start, end, "AVAILABILITY", "Standard Availability"));
            HallService.addSchedule(new com.hallsymphony.model.Schedule("SCH2", "HALL2", start, end, "AVAILABILITY", "Standard Availability"));
            HallService.addSchedule(new com.hallsymphony.model.Schedule("SCH3", "HALL3", start, end, "AVAILABILITY", "Standard Availability"));
        }

        File issueFile = new File("data/db/issues.txt");
        if (!issueFile.exists()) {
            try {
                issueFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File bookingFile = new File("data/db/bookings.txt");
        if (!bookingFile.exists() || bookingFile.length() < 100) { // If empty or legacy data
            System.out.println("Initializing fresh sample bookings for 2026...");
            LocalDateTime now = LocalDateTime.now();
            
            // Booking 1: This Week (PAID)
            com.hallsymphony.model.Booking b1 = new com.hallsymphony.model.Booking("BOOK101", "CUST1", "HALL1", 
                now.minusDays(2), now.minusDays(2).plusHours(4), 1500.0, "PAID", "Corporate Seminar");
            
            // Booking 2: This Month (PAID)
            com.hallsymphony.model.Booking b2 = new com.hallsymphony.model.Booking("BOOK102", "CUST2", "HALL2", 
                now.minusDays(15), now.minusDays(15).plusHours(6), 2800.0, "PAID", "Wedding Banquet");

            // Store them
            com.hallsymphony.util.DataStorage.appendToFile("bookings.txt", b1);
            com.hallsymphony.util.DataStorage.appendToFile("bookings.txt", b2);
        }
    }
}
