package com.hallsymphony.util;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    // Dynamically resolve the absolute path to the project root's data/db directory
    // regardless of where the java command is executed from.
    private static final String BASE_PATH = getBasePath();

    private static String getBasePath() {
        String userDir = System.getProperty("user.dir");
        // If they ran it from inside src or bin, walk up one level
        if (userDir.endsWith("src") || userDir.endsWith("bin")) {
            userDir = new File(userDir).getParent();
        }
        
        File dbDir = new File(userDir, "data/db");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        
        return dbDir.getAbsolutePath() + File.separator;
    }

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
