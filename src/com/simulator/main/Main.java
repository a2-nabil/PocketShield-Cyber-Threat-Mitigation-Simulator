package com.simulator.main;

import com.simulator.model.SystemCore;
import com.simulator.threats.*;
import com.simulator.security.SecurityFirewall;
import com.simulator.repository.LogRepository;
import com.simulator.repository.LocalFileLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN   = "\u001B[36m";
    private static final String BOLD   = "\u001B[1m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printBootSequence();

        SystemCore system = new SystemCore("Metropalitan University");
        LogRepository logger = new LocalFileLogger();
        SecurityFirewall firewall = new SecurityFirewall("AEGIS-V2", logger);

        boolean running = true;

        while (running) {
            printDashboard(system, firewall);
            System.out.print("    " + CYAN + "Enter command > " + RESET);

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    displaySystemHealth(system);
                    break;
                case "2":
                    simulateSingleThreat(firewall, new DDoSAttack(), system);
                    break;
                case "3":
                    simulateSingleThreat(firewall, new PhishingAttack(), system);
                    break;
                case "4":
                    simulateSingleThreat(firewall, new SQLInjection(), system);
                    break;
                case "5":
                    simulateMultiVectorAttack(firewall, system);
                    break;
                case "6":
                    repairSystem(system);
                    break;
                case "7":
                    viewAttackLogs(logger);
                    break;
                case "8":
                    running = false;
                    shutdown(system, logger);
                    break;
                default:
                    System.out.println("    " + RED + "[!] Invalid command. Please choose 1-8." + RESET);
            }

            if (running) {
                System.out.println();
                System.out.print("    Press ENTER to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void printBootSequence() {
        System.out.println();
        System.out.println(CYAN + BOLD);
        System.out.println("    ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("    ║                                                                    ║");
        System.out.println("    ║     ██████╗██╗   ██╗██████╗ ███████╗██████╗                        ║");
        System.out.println("    ║    ██╔════╝╚██╗ ██╔╝██╔══██╗██╔════╝██╔══██╗                       ║");
        System.out.println("    ║    ██║      ╚████╔╝ ██████╔╝█████╗  ██████╔╝                       ║");
        System.out.println("    ║    ██║       ╚██╔╝  ██╔══██╗██╔══╝  ██╔══██╗                       ║");
        System.out.println("    ║    ╚██████╗   ██║   ██████╔╝███████╗██║  ██║                       ║");
        System.out.println("    ║     ╚═════╝   ╚═╝   ╚═════╝ ╚══════╝╚═╝  ╚═╝                       ║");
        System.out.println("    ║                                                                    ║");
        System.out.println("    ║        THREAT  MITIGATION  SIMULATOR  v1.0                         ║");
        System.out.println("    ║                                                                    ║");
        System.out.println("    ╚════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);

        String[] bootMessages = {
            "Initializing kernel modules............",
            "Loading threat intelligence database...",
            "Configuring firewall rules.............",
            "Establishing secure perimeter..........",
            "System ONLINE. Firewall ACTIVE."
        };

        for (String msg : bootMessages) {
            System.out.print("    " + GREEN + "[+] " + msg + RESET);
            sleep(400);
            System.out.println(" " + GREEN + "OK" + RESET);
        }

        System.out.println();
        System.out.println("    " + GREEN + BOLD + "════════════════════════════════════════════" + RESET);
        System.out.println("    " + GREEN + BOLD + "  SYSTEM BOOTING COMPLETE. FIREWALL ONLINE." + RESET);
        System.out.println("    " + GREEN + BOLD + "════════════════════════════════════════════" + RESET);
        sleep(500);
    }

    private static void printDashboard(SystemCore system, SecurityFirewall firewall) {
        System.out.println();
        System.out.println("    " + BOLD + "┌──────────────────────────────────────────────────┐" + RESET);
        System.out.println("    " + BOLD + "│          COMMAND CENTER — POCKETSHIELD DASHBOARD          │" + RESET);
        System.out.println("    " + BOLD + "├──────────────────────────────────────────────────┤" + RESET);
        System.out.println("    " + BOLD + "│                                                  │" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [1]  View System Health                       " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [2]  Simulate DDoS Attack                     " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [3]  Simulate Phishing Campaign               " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [4]  Simulate SQL Injection                   " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [5]  Simulate Random Multi-Vector Attack      " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [6]  Repair / Patch System                    " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [7]  View Attack Logs                         " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "   [8]  Shutdown & Exit                          " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│                                                  │" + RESET);
        System.out.println("    " + BOLD + "├──────────────────────────────────────────────────┤" + RESET);

        String statusColor = getStatusColor(system.getSystemStatus());
        System.out.println("    " + BOLD + "│" + RESET + "  System : " + statusColor + system.getSystemStatus() + RESET
                + "  Health: " + system.getHealthBar()
                + "  " + BOLD + "│" + RESET);
        System.out.println("    " + BOLD + "│" + RESET + "  Threats Blocked: " + firewall.getThreatsBlocked()
                + "                              " + BOLD + "│" + RESET);

        System.out.println("    " + BOLD + "└──────────────────────────────────────────────────┘" + RESET);
    }

    private static void displaySystemHealth(SystemCore system) {
        System.out.println();
        System.out.println("    ┌──────────────────────────────────────────────────┐");
        System.out.println("    │            SYSTEM HEALTH REPORT                  │");
        System.out.println("    ├──────────────────────────────────────────────────┤");
        System.out.println("    │  Name         : " + system.getSystemName());
        System.out.println("    │  Status       : " + getStatusColor(system.getSystemStatus())
                + system.getSystemStatus() + RESET);
        System.out.println("    │  Health       : " + system.getHealthBar());
        System.out.println("    │  Credentials  : " + (system.isCredentialsCompromised()
                ? RED + "COMPROMISED" + RESET : GREEN + "SECURE" + RESET));
        System.out.println("    │  Database     : " + (system.isDatabaseCompromised()
                ? RED + "COMPROMISED" + RESET : GREEN + "SECURE" + RESET));
        System.out.println("    └──────────────────────────────────────────────────┘");
    }

    private static void simulateSingleThreat(SecurityFirewall firewall, CyberThreat threat, SystemCore system) {
        List<CyberThreat> threats = new ArrayList<>();
        threats.add(threat);
        firewall.scanAndDefend(threats, system);
    }

    private static void simulateMultiVectorAttack(SecurityFirewall firewall, SystemCore system) {
        System.out.println();
        System.out.println("    " + RED + BOLD + "╔══════════════════════════════════════════════════╗" + RESET);
        System.out.println("    " + RED + BOLD + "║  MULTI-VECTOR ATTACK INCOMING — 5 THREATS!       ║" + RESET);
        System.out.println("    " + RED + BOLD + "╚══════════════════════════════════════════════════╝" + RESET);

        Random random = new Random();
        List<CyberThreat> threats = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int type = random.nextInt(3);
            switch (type) {
                case 0: threats.add(new DDoSAttack());      break;
                case 1: threats.add(new PhishingAttack());   break;
                case 2: threats.add(new SQLInjection());     break;
            }
        }

        firewall.scanAndDefend(threats, system);
    }

    private static void repairSystem(SystemCore system) {
        System.out.println();
        System.out.println("    " + GREEN + "┌──────────────────────────────────────────────────┐" + RESET);
        System.out.println("    " + GREEN + "│         SYSTEM REPAIR / PATCH MODULE             │" + RESET);
        System.out.println("    " + GREEN + "└──────────────────────────────────────────────────┘" + RESET);
        System.out.println("    " + GREEN + "[+] Running diagnostics..." + RESET);
        sleep(500);
        System.out.println("    " + GREEN + "[+] Applying security patches..." + RESET);
        sleep(500);
        System.out.println("    " + GREEN + "[+] Restoring system integrity..." + RESET);
        sleep(500);

        system.patchSystem(100);

        System.out.println("    " + GREEN + BOLD + "[+] REPAIR COMPLETE." + RESET);
        System.out.println("    [i] Health: " + system.getHealthBar());
        System.out.println("    [i] Status: " + getStatusColor(system.getSystemStatus())
                + system.getSystemStatus() + RESET);
    }

    private static void viewAttackLogs(LogRepository logger) {
        System.out.println();
        System.out.println("    ┌──────────────────────────────────────────────────┐");
        System.out.println("    │            ATTACK HISTORY LOG                    │");
        System.out.println("    └──────────────────────────────────────────────────┘");

        List<String> logs = logger.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("    [i] No logs recorded yet.");
        } else {
            for (String log : logs) {
                System.out.println("    " + log);
            }
        }
    }

    private static void shutdown(SystemCore system, LogRepository logger) {
        System.out.println();
        System.out.println("    " + YELLOW + "[*] Saving session data..." + RESET);
        sleep(300);

        List<String> eventLog = system.getEventLog();
        for (String event : eventLog) {
            logger.saveLog("[SYSTEM] " + event);
        }

        System.out.println("    " + YELLOW + "[*] Shutting down firewall..." + RESET);
        sleep(300);
        System.out.println("    " + YELLOW + "[*] Closing secure connections..." + RESET);
        sleep(300);

        System.out.println();
        System.out.println(RED + BOLD);
        System.out.println("    ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("    ║                                                                    ║");
        System.out.println("    ║                    SYSTEM SHUTDOWN COMPLETE                        ║");
        System.out.println("    ║                                                                    ║");
        System.out.println("    ║                  \"Stay ready. Stay secure.\"                      ║");
        System.out.println("    ║         PocketShield - (Built by Nabil, Gilman & Ricky)            ║");
        System.out.println("    ║                                                                    ║");
        System.out.println("    ╚════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    private static String getStatusColor(String status) {
        switch (status) {
            case "ONLINE":   return GREEN;
            case "DEGRADED": return YELLOW;
            case "CRITICAL": return RED;
            case "OFFLINE":  return RED + BOLD;
            default:         return RESET;
        }
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
