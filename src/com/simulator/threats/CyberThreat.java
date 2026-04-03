package com.simulator.threats;

import com.simulator.model.SystemCore;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  OOP PILLAR: ABSTRACTION                                 │
 * │  This abstract class defines the contract that every     │
 * │  cyber threat must fulfill, without specifying how.      │
 * │  Child classes provide concrete implementations.         │
 * └──────────────────────────────────────────────────────────┘
 */
public abstract class CyberThreat {

    protected String threatName;
    protected String severityLevel;   // LOW, MEDIUM, HIGH, CRITICAL
    protected int baseDamage;
    protected boolean active;

    public CyberThreat(String threatName, String severityLevel, int baseDamage) {
        this.threatName = threatName;
        this.severityLevel = severityLevel;
        this.baseDamage = baseDamage;
        this.active = true;
    }

    /**
     * Each threat defines its own unique attack behavior.
     */
    public abstract void attack(SystemCore target);

    /**
     * Each threat defines how it gets neutralized.
     */
    public abstract String neutralize();

    /**
     * Shared behavior: all threats can describe themselves.
     */
    public String getThreatSummary() {
        return "[THREAT] " + threatName + " | Severity: " + severityLevel
                + " | Base Damage: " + baseDamage + " | Active: " + active;
    }

    public String getThreatName() {
        return threatName;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public boolean isActive() {
        return active;
    }
}
