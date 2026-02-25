package com.hallsymphony.model;

import java.io.Serializable;

public class Hall implements Serializable {
    private String id;
    private String name;
    private String type; // Auditorium, Banquet Hall, Meeting Room
    private int capacity;
    private double ratePerHour;
    private String description;

    public Hall(String id, String name, String type, int capacity, double ratePerHour, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.ratePerHour = ratePerHour;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public double getRatePerHour() { return ratePerHour; }
    public void setRatePerHour(double ratePerHour) { this.ratePerHour = ratePerHour; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.join("|", id, name, type, String.valueOf(capacity), String.valueOf(ratePerHour), description);
    }

    public static Hall fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6) {
            return new Hall(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4]), parts[5]);
        }
        return null;
    }
}
