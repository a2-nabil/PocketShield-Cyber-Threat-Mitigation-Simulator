package com.simulator.security;

import com.simulator.model.SystemCore;
import com.simulator.threats.CyberThreat;
import com.simulator.repository.LogRepository;

import java.util.List;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  OOP PILLAR: POLYMORPHISM                                │
 * │  scanAndDefend() accepts a List<CyberThreat>.            │
 * │  It doesn't know or care about the concrete type —       │
 * │  Java resolves the correct attack() and neutralize()     │
 * │  implementations at RUNTIME (dynamic dispatch).          │
 * └──────────────────────────────────────────────────────────┘
 */
public class SecurityFirewall {

    private String firewallName;
    private int threatsBlocked;
    private LogRepository logger;

    public SecurityFirewall(String firewallName, LogRepository logger) {
        this.firewallName = firewallName;
        this.threatsBlocked = 0;
        this.logger = logger;
    }

    /**
     * The polymorphic engine: iterates over threats of ANY type and
     * calls their overridden attack() and neutralize() methods.
     */
    public void scanAndDefend(List<CyberThreat> activeThreats, SystemCore system) {
        System.out.println();
        System.out.println("    ┌──────────────────────────────────────────────────┐");
        System.out.println("    │  FIREWALL [" + firewallName + "] — SCAN INITIATED          │");
        System.out.println("    │  Threats Detected: " + padRight(String.valueOf(activeThreats.size()), 29) + "│");
        System.out.println("    └──────────────────────────────────────────────────┘");

        for (CyberThreat threat : activeThreats) {
            System.out.println();
            System.out.println("    ── Processing: " + threat.getThreatName() + " ──");

            threat.attack(system);

            try { Thread.sleep(600); } catch (InterruptedException ignored) {}

            String result = threat.neutralize();
            threatsBlocked++;

            if (system.isCredentialsCompromised()) {
                system.restoreCredentials();
            }
            if (system.isDatabaseCompromised()) {
                system.restoreDatabase();
            }

            logger.saveLog("[ATTACK] " + threat.getThreatName()
                    + " | Severity: " + threat.getSeverityLevel()
                    + " | Damage: " + threat.getBaseDamage()
                    + " | Result: " + result);

            System.out.println("    [i] Status: " + system.getSystemStatus()
                    + " | Health: " + system.getHealthBar());
        }

        System.out.println();
        System.out.println("    ╔══════════════════════════════════════════════════╗");
        System.out.println("    ║  SCAN COMPLETE — All threats neutralized.       ║");
        System.out.println("    ║  Total Threats Blocked (session): " + padRight(String.valueOf(threatsBlocked), 14) + "║");
        System.out.println("    ╚══════════════════════════════════════════════════╝");
    }

    public int getThreatsBlocked() {
        return threatsBlocked;
    }

    private String padRight(String text, int length) {
        if (text.length() >= length) return text;
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < length) sb.append(' ');
        return sb.toString();
    }
}
