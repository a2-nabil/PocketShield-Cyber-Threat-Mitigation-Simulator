package com.simulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  OOP PILLAR: ENCAPSULATION                               │
 * │  All fields are private. External code must go through   │
 * │  controlled public methods to read or modify state.      │
 * └──────────────────────────────────────────────────────────┘
 */
public class SystemCore {

    private String systemName;
    private int healthScore;
    private int maxHealth;
    private String systemStatus;
    private boolean credentialsCompromised;
    private boolean databaseCompromised;
    private List<String> eventLog;

    public SystemCore(String systemName) {
        this.systemName = systemName;
        this.maxHealth = 100;
        this.healthScore = maxHealth;
        this.systemStatus = "ONLINE";
        this.credentialsCompromised = false;
        this.databaseCompromised = false;
        this.eventLog = new ArrayList<>();
        log("System \"" + systemName + "\" initialized. Status: " + systemStatus);
    }

    public void applyDamage(int damage) {
        if (damage < 0) return;
        this.healthScore = Math.max(0, this.healthScore - damage);
        if (this.healthScore <= 0) {
            this.systemStatus = "OFFLINE";
            log("CRITICAL: System health reached 0. Status changed to OFFLINE.");
        } else if (this.healthScore <= 30) {
            this.systemStatus = "CRITICAL";
            log("WARNING: System health critical (" + this.healthScore + "/" + this.maxHealth + ").");
        } else if (this.healthScore <= 60) {
            this.systemStatus = "DEGRADED";
        }
    }

    public void patchSystem(int repairValue) {
        if (repairValue < 0) return;
        int before = this.healthScore;
        this.healthScore = Math.min(this.maxHealth, this.healthScore + repairValue);
        this.credentialsCompromised = false;
        this.databaseCompromised = false;

        if (this.healthScore > 60) {
            this.systemStatus = "ONLINE";
        } else if (this.healthScore > 30) {
            this.systemStatus = "DEGRADED";
        } else if (this.healthScore > 0) {
            this.systemStatus = "CRITICAL";
        }
        log("Patch applied. Health restored from " + before + " to " + this.healthScore + ".");
    }

    public void compromiseCredentials() {
        this.credentialsCompromised = true;
        log("User credentials have been COMPROMISED.");
    }

    public void compromiseDatabase() {
        this.databaseCompromised = true;
        log("Database integrity has been COMPROMISED.");
    }

    public void restoreCredentials() {
        this.credentialsCompromised = false;
        log("User credentials have been SECURED.");
    }

    public void restoreDatabase() {
        this.databaseCompromised = false;
        log("Database integrity has been RESTORED.");
    }

    public void log(String message) {
        this.eventLog.add(message);
    }

    // --- Controlled Getters (no setters exposed for critical fields) ---

    public String getSystemName() {
        return systemName;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public boolean isCredentialsCompromised() {
        return credentialsCompromised;
    }

    public boolean isDatabaseCompromised() {
        return databaseCompromised;
    }

    public List<String> getEventLog() {
        return new ArrayList<>(eventLog);
    }

    public void clearLog() {
        this.eventLog.clear();
    }

    public String getHealthBar() {
        int filled = (int) ((healthScore / (double) maxHealth) * 20);
        int empty = 20 - filled;
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < filled; i++) bar.append("█");
        for (int i = 0; i < empty; i++) bar.append("░");
        bar.append("] ").append(healthScore).append("/").append(maxHealth);
        return bar.toString();
    }
}
