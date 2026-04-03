package com.simulator.threats;

import com.simulator.model.SystemCore;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  OOP PILLAR: INHERITANCE                                 │
 * │  SQLInjection extends CyberThreat and targets database   │
 * │  integrity — yet another unique attack behavior.         │
 * └──────────────────────────────────────────────────────────┘
 */
public class SQLInjection extends CyberThreat {

    private String payload;

    public SQLInjection() {
        super("SQL Injection", "CRITICAL", 25);
        this.payload = "' OR '1'='1'; DROP TABLE users; --";
    }

    @Override
    public void attack(SystemCore target) {
        if (!active) return;

        System.out.println();
        System.out.println("    ╔══════════════════════════════════════════════════╗");
        System.out.println("    ║  [!] CRITICAL: SQL Injection detected!          ║");
        System.out.println("    ╚══════════════════════════════════════════════════╝");
        System.out.println("    [!] Type        : SQL Injection");
        System.out.println("    [!] Severity    : " + severityLevel);
        System.out.println("    [!] Payload     : " + payload);
        System.out.println("    [!] Impact      : Database breach + " + baseDamage + " system damage.");

        target.applyDamage(baseDamage);
        target.compromiseDatabase();
        target.log("SQL Injection hit. " + baseDamage + " damage dealt. Database compromised. Payload: " + payload);

        System.out.println("    [!] System Health: " + target.getHealthBar());
        System.out.println("    [!] Database    : *** COMPROMISED ***");
    }

    @Override
    public String neutralize() {
        this.active = false;
        String msg = "    [+] FIREWALL: Sanitizing inputs & patching queries... SQL Injection Neutralized.";
        System.out.println(msg);
        return "SQL Injection neutralized. Input validation enforced.";
    }
}
