package com.simulator.repository;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation that writes attack logs to a local .txt file.
 * When you move to the cloud, create a CloudDatabaseLogger that implements
 * LogRepository and swap it in — zero changes to core logic needed.
 */
public class LocalFileLogger implements LogRepository {

    private static final String LOG_FILE = "attack_history.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalFileLogger() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println("═══════════════════════════════════════════════════════════");
            writer.println("  SESSION STARTED: " + LocalDateTime.now().format(FORMATTER));
            writer.println("═══════════════════════════════════════════════════════════");
        } catch (IOException e) {
            System.err.println("    [ERROR] Could not initialize log file: " + e.getMessage());
        }
    }

    @Override
    public void saveLog(String logMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println("[" + LocalDateTime.now().format(FORMATTER) + "] " + logMessage);
        } catch (IOException e) {
            System.err.println("    [ERROR] Failed to write log: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllLogs() {
        List<String> logs = new ArrayList<>();
        File file = new File(LOG_FILE);
        if (!file.exists()) return logs;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.err.println("    [ERROR] Failed to read logs: " + e.getMessage());
        }
        return logs;
    }

    @Override
    public void clearLogs() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, false))) {
            writer.println("  LOG CLEARED: " + LocalDateTime.now().format(FORMATTER));
        } catch (IOException e) {
            System.err.println("    [ERROR] Failed to clear logs: " + e.getMessage());
        }
    }
}
