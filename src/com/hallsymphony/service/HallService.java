package com.hallsymphony.service;

import com.hallsymphony.model.Hall;
import com.hallsymphony.model.Schedule;
import com.hallsymphony.util.DataStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HallService {
    private static final String HALL_FILE = "halls.txt";
    private static final String SCHEDULE_FILE = "schedules.txt";

    public static List<Hall> getAllHalls() {
        List<String> lines = DataStorage.readList(HALL_FILE);
        return lines.stream()
                .map(Hall::fromString)
                .collect(Collectors.toList());
    }

    public static boolean addHall(String name, String type, int capacity, double rate, String description) {
        List<Hall> halls = getAllHalls();
        String id = "HALL" + (halls.size() + 1);
        Hall hall = new Hall(id, name, type, capacity, rate, description);
        DataStorage.appendToFile(HALL_FILE, hall);
        return true;
    }

    public static boolean updateHall(Hall updatedHall) {
        List<Hall> halls = getAllHalls();
        boolean found = false;
        for (int i = 0; i < halls.size(); i++) {
            if (halls.get(i).getId().equals(updatedHall.getId())) {
                halls.set(i, updatedHall);
                found = true;
                break;
            }
        }
        if (found) {
            DataStorage.saveList(HALL_FILE, halls);
        }
        return found;
    }

    public static boolean deleteHall(String hallId) {
        List<Hall> halls = getAllHalls();
        int initialSize = halls.size();
        halls.removeIf(h -> h.getId().equals(hallId));
        if (halls.size() < initialSize) {
            DataStorage.saveList(HALL_FILE, halls);
            return true;
        }
        return false;
    }

    // Schedule Management
    public static List<Schedule> getAllSchedules() {
        return DataStorage.readList(SCHEDULE_FILE).stream()
                .map(Schedule::fromString)
                .collect(Collectors.toList());
    }

    public static List<Schedule> getSchedulesByHall(String hallId) {
        return getAllSchedules().stream()
                .filter(s -> s.getHallId().equals(hallId))
                .collect(Collectors.toList());
    }

    public static boolean addSchedule(Schedule schedule) {
        List<Schedule> schedules = getAllSchedules();
        // Check for maintenance overlapping
        if (schedule.getType().equals("MAINTENANCE")) {
            boolean conflict = schedules.stream()
                .filter(s -> s.getHallId().equals(schedule.getHallId()) && s.getType().equals("MAINTENANCE"))
                .anyMatch(s -> schedule.getStartTime().isBefore(s.getEndTime()) && schedule.getEndTime().isAfter(s.getStartTime()));
            if (conflict) return false;
        }
        
        DataStorage.appendToFile(SCHEDULE_FILE, schedule);
        return true;
    }

    public static boolean deleteSchedule(String scheduleId) {
        List<Schedule> schedules = getAllSchedules();
        int initialSize = schedules.size();
        schedules.removeIf(s -> s.getId().equals(scheduleId));
        if (schedules.size() < initialSize) {
            DataStorage.saveList(SCHEDULE_FILE, schedules);
            return true;
        }
        return false;
    }
}
