package com.hallsymphony.model;

import java.io.Serializable;

public class Issue implements Serializable {
    private String id;
    private String customerId;
    private String bookingId;
    private String description;
    private String status; // OPEN, IN_PROGRESS, DONE, CLOSED, CANCELLED
    private String assignedTo; // Staff ID (Scheduler)

    public Issue(String id, String customerId, String bookingId, String description, String status, String assignedTo) {
        this.id = id;
        this.customerId = customerId;
        this.bookingId = bookingId;
        this.description = description;
        this.status = status;
        this.assignedTo = assignedTo;
    }

    // Getters and Setters ...
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getBookingId() { return bookingId; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }

    public void setStatus(String status) { this.status = status; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    @Override
    public String toString() {
        return String.join("|", id, customerId, bookingId, description, status, assignedTo != null ? assignedTo : "NONE");
    }

    public static Issue fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6) {
            return new Issue(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5].equals("NONE") ? null : parts[5]);
        }
        return null;
    }
}
