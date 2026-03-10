package com.hallsymphony.service;

import com.hallsymphony.model.Booking;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    public static double getSalesForPeriod(LocalDateTime start, LocalDateTime end) {
        return BookingService.getAllBookings().stream()
                .filter(b -> "PAID".equalsIgnoreCase(b.getStatus()))
                .filter(b -> !b.getStartTime().isBefore(start) && !b.getStartTime().isAfter(end))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    public static Map<String, Double> getWeeklySales() {
        LocalDateTime now = LocalDateTime.now();
        return Map.of("Current Week", getSalesForPeriod(now.minusWeeks(1), now));
    }

    public static Map<String, Double> getMonthlySales() {
        LocalDateTime now = LocalDateTime.now();
        return Map.of("Current Month", getSalesForPeriod(now.minusMonths(1), now));
    }

    public static Map<String, Double> getYearlySales() {
        LocalDateTime now = LocalDateTime.now();
        return Map.of("Current Year", getSalesForPeriod(now.minusYears(1), now));
    }

    public static double getTotalRevenue() {
        return BookingService.getAllBookings().stream()
                .filter(b -> "PAID".equalsIgnoreCase(b.getStatus()))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    public static int getTotalBookingCount() {
        return BookingService.getAllBookings().size();
    }
}
