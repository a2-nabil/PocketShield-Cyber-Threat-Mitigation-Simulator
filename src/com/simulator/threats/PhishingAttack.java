package com.simulator.threats;

import com.simulator.model.SystemCore;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  OOP PILLAR: INHERITANCE                                 │
 * │  PhishingAttack extends CyberThreat and targets user     │
 * │  credentials specifically — different from DDoS.         │
 * └──────────────────────────────────────────────────────────┘
 */
public class PhishingAttack extends CyberThreat {

    private String targetVector;

    public PhishingAttack() {
        super("Phishing Campaign", "MEDIUM", 10);
        this.targetVector = "User Credentials (Email Spoofing)";
    }

    @Override
    public void attack(SystemCore target) {
        if (!active) return;

        System.out.println();
        System.out.println("    ╔══════════════════════════════════════════════════╗");
        System.out.println("    ║  [!] ALERT: Phishing Campaign detected!         ║");
        System.out.println("    ╚══════════════════════════════════════════════════╝");
        System.out.println("    [!] Type        : Social Engineering — Phishing");
        System.out.println("    [!] Severity    : " + severityLevel);
        System.out.println("    [!] Vector      : " + targetVector);
        System.out.println("    [!] Impact      : Credential theft + " + baseDamage + " system damage.");

        target.applyDamage(baseDamage);
        target.compromiseCredentials();
        target.log("Phishing Campaign hit. " + baseDamage + " damage dealt. Credentials compromised.");

        System.out.println("    [!] System Health: " + target.getHealthBar());
        System.out.println("    [!] Credentials : *** COMPROMISED ***");
    }

    @Override
    public String neutralize() {
        this.active = false;
        String msg = "    [+] FIREWALL: Blacklisting spoofed domains... Phishing Neutralized.";
        System.out.println(msg);
        return "Phishing Campaign neutralized. Spoofed domains blacklisted.";
    }
}
