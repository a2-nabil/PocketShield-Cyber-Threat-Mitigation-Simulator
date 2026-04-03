# PocketShield

**PocketShield** is a portable **Cyber Threat Mitigation Simulator** written in Java. It runs entirely on your machine with an interactive terminal UI, in-memory game state, and file-based attack logs—designed for classroom demos, portfolio use, and discussions about **object-oriented design** and **ethical responsibility** when building secure systems.

---

## Table of contents

- [Overview](#overview)
- [Features](#features)
- [Architecture & separation of concerns](#architecture--separation-of-concerns)
- [The four pillars of OOP (in this project)](#the-four-pillars-of-oop-in-this-project)
- [Project structure](#project-structure)
- [Requirements](#requirements)
- [Getting started](#getting-started)
- [How to use (CLI menu)](#how-to-use-cli-menu)
- [Generated files](#generated-files)
- [Troubleshooting](#troubleshooting)
- [Roadmap: from CLI to cloud](#roadmap-from-cli-to-cloud)
- [License](#license)

---

## Overview

PocketShield models a protected **system** (`SystemCore`) that can be targeted by different **cyber threats** (`DDoSAttack`, `PhishingAttack`, `SQLInjection`). A **firewall** (`SecurityFirewall`) processes threats polymorphically and writes a persistent audit trail through a **repository abstraction** (`LogRepository` → `LocalFileLogger`).

The **console layer** (`Main`) only handles input/output. **Business logic** lives in `model`, `threats`, `security`, and `repository`, so the same core code can later be wrapped by a REST API (for example Spring Boot) without rewriting the domain model.

---

## Features

- **Interactive command dashboard** with boot sequence, health bar, and colored terminal output (ANSI).
- **Single-threat simulations**: DDoS, phishing, SQL injection—each with distinct behavior.
- **Multi-vector simulation**: five random threats in one run (stress test for polymorphism).
- **System repair**: restore health and clear compromise flags via `patchSystem`.
- **Attack history**: append-only logging to `attack_history.txt` via the `LogRepository` interface.
- **No database required**: state is in memory; logs are local files—ideal for laptops and presentations.

---

## Architecture & separation of concerns

| Layer | Package | Responsibility |
|--------|---------|----------------|
| **Application / UI** | `com.simulator.main` | Boot sequence, `Scanner` menu, printing results only. |
| **Domain model** | `com.simulator.model` | Encapsulated system state (`SystemCore`). |
| **Threats** | `com.simulator.threats` | Abstract threat contract + concrete attack types. |
| **Defense** | `com.simulator.security` | Firewall orchestration (`scanAndDefend`). |
| **Persistence (pluggable)** | `com.simulator.repository` | `LogRepository` interface + `LocalFileLogger` implementation. |

---

## The four pillars of OOP (in this project)

| Pillar | Implementation | What to say in a presentation |
|--------|----------------|--------------------------------|
| **Encapsulation** | `SystemCore` keeps fields `private`; mutators like `applyDamage`, `patchSystem`, and flags for credentials/database enforce rules internally. | External code cannot set health directly; behavior is controlled through methods. |
| **Abstraction** | `CyberThreat` is abstract; it declares `attack(SystemCore)` and `neutralize()` without fixing how each threat works. | The “blueprint” hides complexity behind a clear contract. |
| **Inheritance** | `DDoSAttack`, `PhishingAttack`, and `SQLInjection` extend `CyberThreat` and override behavior. | Shared structure, specialized attacks. |
| **Polymorphism** | `SecurityFirewall.scanAndDefend(List<CyberThreat>, SystemCore)` calls `attack` / `neutralize` on each element; the JVM dispatches to the correct subclass at runtime. | One loop, many behaviors—classic polymorphism. |

---

## Project structure

```
.
├── README.md
├── attack_history.txt          # Created at runtime (see Generated files)
├── out/                        # Compiled .class files (after build)
└── src/
    └── com/
        └── simulator/
            ├── main/
            │   └── Main.java
            ├── model/
            │   └── SystemCore.java
            ├── threats/
            │   ├── CyberThreat.java
            │   ├── DDoSAttack.java
            │   ├── PhishingAttack.java
            │   └── SQLInjection.java
            ├── security/
            │   └── SecurityFirewall.java
            └── repository/
                ├── LogRepository.java
                └── LocalFileLogger.java
```

---

## Requirements

- **Java Development Kit (JDK)** **17 or newer** (the project was verified with **JDK 21**).
- A terminal that supports **ANSI colors** (Windows Terminal, modern VS Code / Cursor integrated terminal, PowerShell 7, or most Linux/macOS terminals). If colors appear as escape codes, enable UTF-8 / use Windows Terminal.

Verify Java:

```bash
java -version
javac -version
```

---

## Getting started

### 1. Open a terminal in the project root

The project root is the folder that contains `src` and this `README.md`. **Run the app from this directory** so `attack_history.txt` is created next to your project (expected by `LocalFileLogger`).

### 2. Compile

**Git Bash / macOS / Linux:**

```bash
mkdir -p out
javac -d out \
  src/com/simulator/repository/*.java \
  src/com/simulator/model/*.java \
  src/com/simulator/threats/*.java \
  src/com/simulator/security/*.java \
  src/com/simulator/main/*.java
```

**Windows Command Prompt (cmd), from project root:**

```cmd
mkdir out 2>nul
javac -d out src\com\simulator\repository\*.java src\com\simulator\model\*.java src\com\simulator\threats\*.java src\com\simulator\security\*.java src\com\simulator\main\*.java
```

**PowerShell:**

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -d out `
  src/com/simulator/repository/*.java `
  src/com/simulator/model/*.java `
  src/com/simulator/threats/*.java `
  src/com/simulator/security/*.java `
  src/com/simulator/main/*.java
```

### 3. Run

```bash
java -cp out com.simulator.main.Main
```

You should see the boot banner, then the **Command Center** menu. Enter a number (`1`–`8`) and press **Enter**. After most actions, press **Enter** again to return to the menu.

### 4. Optional: one-liner (compile + run)

**Git Bash / Linux / macOS:**

```bash
mkdir -p out && javac -d out src/com/simulator/repository/*.java src/com/simulator/model/*.java src/com/simulator/threats/*.java src/com/simulator/security/*.java src/com/simulator/main/*.java && java -cp out com.simulator.main.Main
```

---

## How to use (CLI menu)

| Key | Action |
|-----|--------|
| **1** | View system health (status, health bar, credential/database flags). |
| **2** | Simulate a **DDoS** attack (high direct damage). |
| **3** | Simulate a **phishing** campaign (damage + credential compromise). |
| **4** | Simulate **SQL injection** (critical damage + database compromise). |
| **5** | **Random multi-vector** attack: five threats chosen at random from the three types. |
| **6** | **Repair / patch** the system (restore health and clear compromise flags). |
| **7** | Print **attack logs** (from `attack_history.txt` via the repository). |
| **8** | **Exit** gracefully (persists session-related log lines, shutdown message). |

---

## Generated files

| File | Description |
|------|-------------|
| `attack_history.txt` | Timestamped lines for neutralized attacks (and session markers). Created/updated in the **current working directory** when you run the program. |
| `out/` | Output of `javac`; contains compiled `.class` files. Safe to delete; recompile anytime. |

---

## Troubleshooting

| Issue | What to try |
|-------|-------------|
| `javac` / `java` not found | Install a JDK and add its `bin` folder to your system **PATH**, or run using the full path to `javac` and `java`. |
| `attack_history.txt` missing or empty | Ensure you run `java` from the **project root**. Check option **7** after simulating at least one attack. |
| Garbled colors or weird characters | Use **Windows Terminal** or enable UTF-8 in your console; avoid legacy conhost limits if possible. |
| Compile errors about packages | Always compile from the repo root and use `-d out` with `java -cp out com.simulator.main.Main` as shown above. |

---

## Roadmap: from CLI to cloud

Because the **Scanner** and `println` logic are isolated in `Main`, you can:

1. Keep `SystemCore`, `CyberThreat` hierarchy, and `SecurityFirewall` unchanged.
2. Add **Spring Boot** with a controller that calls `scanAndDefend` (or a thin service layer).
3. Return JSON logs instead of printing to the console.
4. Implement `CloudDatabaseLogger` (or JPA repository) that implements `LogRepository` and swap it in via dependency injection.

No rewrite of the core simulation is required—only new “delivery” layers.

---

## License

This project is provided as educational sample code. Add a license file (for example MIT or Apache-2.0) if you publish the repository publicly.

---

## Acknowledgments

Built as a teaching-oriented demonstration of **encapsulation, abstraction, inheritance, and polymorphism**, with a layout that mirrors professional **layered** architectures suitable for future API integration.
