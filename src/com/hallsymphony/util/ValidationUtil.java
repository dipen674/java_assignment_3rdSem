package com.hallsymphony.util;

import java.util.regex.Pattern;
import java.time.LocalDateTime;

public class ValidationUtil {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^\\d{10,12}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidContact(String contact) {
        return contact != null && CONTACT_PATTERN.matcher(contact).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidCapacity(String cap) {
        try {
            int c = Integer.parseInt(cap);
            return c > 0 && c <= 10000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidRate(String rate) {
        try {
            double r = Double.parseDouble(rate);
            return r > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDateTime(String dateTime) {
        try {
            LocalDateTime.parse(dateTime);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Formats a LocalDateTime to ISO format without nanoseconds.
     * Example: 2026-03-17T14:00:00
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.withNano(0).toString();
    }
}
