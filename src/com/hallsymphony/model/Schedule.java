package com.hallsymphony.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule implements Serializable {
    private String id;
    private String hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type; // AVAILABILITY, MAINTENANCE
    private String remarks;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public Schedule(String id, String hallId, LocalDateTime startTime, LocalDateTime endTime, String type, String remarks) {
        this.id = id;
        this.hallId = hallId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.remarks = remarks;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHallId() { return hallId; }
    public void setHallId(String hallId) { this.hallId = hallId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public String toString() {
        return String.join("|", id, hallId, 
            startTime.withNano(0).format(formatter), 
            endTime.withNano(0).format(formatter), 
            type, remarks != null ? remarks : "");
    }

    public static Schedule fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 5) {
            String remarks = parts.length > 5 ? parts[5] : "";
            return new Schedule(parts[0], parts[1], LocalDateTime.parse(parts[2], formatter), LocalDateTime.parse(parts[3], formatter), parts[4], remarks);
        }
        return null;
    }
}
