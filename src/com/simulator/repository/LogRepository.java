package com.simulator.repository;

import java.util.List;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  FUTURE-PROOFING via INTERFACE                           │
 * │  Any storage backend (file, MySQL, MongoDB, cloud) can   │
 * │  implement this interface and be swapped in without      │
 * │  changing any core business logic.                       │
 * └──────────────────────────────────────────────────────────┘
 */
public interface LogRepository {

    void saveLog(String logMessage);

    List<String> getAllLogs();

    void clearLogs();
}
