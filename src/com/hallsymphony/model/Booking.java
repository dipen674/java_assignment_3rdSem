package com.hallsymphony.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking implements Serializable {
    private String id;
    private String customerId;
    private String hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private String status; // PENDING, PAID, CANCELLED
    private String remarks;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public Booking(String id, String customerId, String hallId, LocalDateTime startTime, LocalDateTime endTime, double totalPrice, String status, String remarks) {
        this.id = id;
        this.customerId = customerId;
        this.hallId = hallId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and Setters ...
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getHallId() { return hallId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getRemarks() { return remarks; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.join("|", id, customerId, hallId, 
            startTime.withNano(0).format(formatter), 
            endTime.withNano(0).format(formatter), 
            String.valueOf(totalPrice), status, remarks);
    }

    public static Booking fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 8) {
            return new Booking(parts[0], parts[1], parts[2], LocalDateTime.parse(parts[3], formatter), LocalDateTime.parse(parts[4], formatter), Double.parseDouble(parts[5]), parts[6], parts[7]);
        }
        return null;
    }
}
