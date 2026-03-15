package com.hallsymphony.service;

import com.hallsymphony.model.Booking;
import com.hallsymphony.model.Schedule;
import com.hallsymphony.util.DataStorage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookingService {
    private static final String BOOKING_FILE = "bookings.txt";

    public static List<Booking> getAllBookings() {
        return DataStorage.readList(BOOKING_FILE).stream()
                .map(Booking::fromString)
                .filter(Objects::nonNull)  // Bug fix: null safety
                .collect(Collectors.toList());
    }

    public static List<Booking> getBookingsByCustomer(String customerId) {
        return getAllBookings().stream()
                .filter(b -> b.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    // Bug fix: Use max-ID to avoid collisions after deletions
    private static int getNextId() {
        List<Booking> bookings = getAllBookings();
        int maxId = 0;
        for (Booking b : bookings) {
            try {
                int num = Integer.parseInt(b.getId().replace("BOOK", ""));
                if (num > maxId) maxId = num;
            } catch (NumberFormatException ignored) {}
        }
        return maxId + 1;
    }

    public static synchronized boolean createBooking(String customerId, String hallId, LocalDateTime start, LocalDateTime end, double price, String remarks) {
        // Validation: start must be before end
        if (!start.isBefore(end)) {
            return false;
        }

        // Validation: Business hours 8 AM - 6 PM
        if (start.getHour() < 8 || end.getHour() > 18 || (end.getHour() == 18 && end.getMinute() > 0)) {
            return false;
        }

        // Validation: Overlap check with existing bookings
        List<Booking> bookings = getAllBookings();
        boolean overlap = bookings.stream()
                .filter(b -> b.getHallId().equals(hallId) && !b.getStatus().equals("CANCELLED"))
                .anyMatch(b -> start.isBefore(b.getEndTime()) && end.isAfter(b.getStartTime()));

        if (overlap) return false;

        // Validation: Overlap check with maintenance
        List<Schedule> schedules = HallService.getAllSchedules();
        boolean maintenanceOverlap = schedules.stream()
                .filter(s -> s.getHallId().equals(hallId) && s.getType().equals("MAINTENANCE"))
                .anyMatch(s -> start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime()));

        if (maintenanceOverlap) return false;

        // Validation: Check if within availability set by scheduler
        boolean available = schedules.stream()
                .filter(s -> s.getHallId().equals(hallId) && s.getType().equals("AVAILABILITY"))
                .anyMatch(s -> !start.isBefore(s.getStartTime()) && !end.isAfter(s.getEndTime()));

        if (!available) return false;

        // Bug fix: Use PAID status (payment is taken at booking time)
        String id = "BOOK" + getNextId();
        Booking booking = new Booking(id, customerId, hallId, start, end, price, "PAID", remarks);
        DataStorage.appendToFile(BOOKING_FILE, booking);
        return true;
    }

    public static boolean cancelBooking(String bookingId) {
        List<Booking> bookings = getAllBookings();
        for (Booking b : bookings) {
            if (b.getId().equals(bookingId)) {
                // Production-grade: Must be at least 72 hours (3 full days) before event starts
                if (java.time.Duration.between(LocalDateTime.now(), b.getStartTime()).toHours() < 72) {
                    return false;
                }
                b.setStatus("CANCELLED");
                DataStorage.saveList(BOOKING_FILE, bookings);
                return true;
            }
        }
        return false;
    }

    public static boolean updateBookingStatus(String bookingId, String status) {
        List<Booking> bookings = getAllBookings();
        for (Booking b : bookings) {
            if (b.getId().equals(bookingId)) {
                b.setStatus(status);
                DataStorage.saveList(BOOKING_FILE, bookings);
                return true;
            }
        }
        return false;
    }
}
