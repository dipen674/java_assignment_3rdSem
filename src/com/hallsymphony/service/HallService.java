package com.hallsymphony.service;

import com.hallsymphony.model.Hall;
import com.hallsymphony.util.DataStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HallService {
    private static final String HALL_FILE = "halls.txt";

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
}
