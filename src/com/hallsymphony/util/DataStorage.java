package com.hallsymphony.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final String BASE_PATH = "data/db/";

    public static void saveList(String fileName, List<?> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BASE_PATH + fileName))) {
            for (Object obj : list) {
                writer.println(obj.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readList(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(BASE_PATH + fileName);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void appendToFile(String fileName, Object obj) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BASE_PATH + fileName, true))) {
            writer.println(obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
