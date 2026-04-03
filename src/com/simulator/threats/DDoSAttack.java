package com.simulator.threats;

import com.simulator.model.SystemCore;

/**
 * ┌──────────────────────────────────────────────────────────┐
 * │  OOP PILLAR: INHERITANCE                                 │
 * │  DDoSAttack extends CyberThreat and provides its own     │
 * │  concrete implementation of attack() and neutralize().   │
 * └──────────────────────────────────────────────────────────┘
 */
public class DDoSAttack extends CyberThreat {

    private int requestsPerSecond;

    public DDoSAttack() {
        super("DDoS Attack", "HIGH", 20);
        this.requestsPerSecond = 50000;
    }

    @Override
    public void attack(SystemCore target) {
        if (!active) return;

        System.out.println();
        System.out.println("    ╔══════════════════════════════════════════════════╗");
        System.out.println("    ║  [!] WARNING: Incoming DDoS Attack detected!    ║");
        System.out.println("    ╚══════════════════════════════════════════════════╝");
        System.out.println("    [!] Type        : Distributed Denial of Service");
        System.out.println("    [!] Severity    : " + severityLevel);
        System.out.println("    [!] Flood Rate  : " + requestsPerSecond + " requests/sec");
        System.out.println("    [!] Impact      : System Core taking " + baseDamage + " damage.");

        target.applyDamage(baseDamage);
        target.log("DDoS Attack hit. " + baseDamage + " damage dealt. (" + requestsPerSecond + " req/s flood)");

        System.out.println("    [!] System Health: " + target.getHealthBar());
    }

    @Override
    public String neutralize() {
        this.active = false;
        String msg = "    [+] FIREWALL: Rerouting traffic through CDN... DDoS Neutralized.";
        System.out.println(msg);
        return "DDoS Attack neutralized via traffic rerouting.";
    }
}
