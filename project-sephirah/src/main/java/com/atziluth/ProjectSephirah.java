package com.atziluth;

import com.atziluth.core.Sephirah;
import com.atziluth.sephirah.chesed.ChesedSephirah;
import java.util.*;

/**
 * Main entry point for Project Sephirah.
 */
public class ProjectSephirah {
    
    private final Map<String, Sephirah> sephirot = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private ChesedSephirah chesedModule;
    
    public static void main(String[] args) {
        System.out.println("""
            ╔══════════════════════════════════════════════════════╗
            ║           PROJECT SEPHIRAH v1.0.0                    ║
            ║     Computational Kabbalah Learning Framework        ║
            ║        Chesed Module: Umamusume Database             ║
            ╚══════════════════════════════════════════════════════╝
            """);
        
        ProjectSephirah framework = new ProjectSephirah();
        framework.run();
    }
    
    private void run() {
        initialize();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = "";
            try {
                if (scanner.hasNextLine()) {
                    choice = scanner.nextLine().trim();
                } else {
                    choice = "0"; // Exit if no input available
                }
            } catch (Exception e) {
                System.out.println("Input error: " + e.getMessage());
                choice = "0";
            }

            switch (choice) {
                case "1":
                    runSephirah("chesed");
                    break;
                case "2":
                    System.out.println("🔮 Gevurah module coming soon!");
                    break;
                case "3":
                    System.out.println("🔮 Tiferet module coming soon!");
                    break;
                case "4":
                    System.out.println("🔮 Binah module coming soon!");
                    break;
                case "0":
                    running = false;
                    System.out.println("\n👋 Goodbye!");
                    break;
                case "?":
                    showHelp();
                    break;
                case "!":
                    showSystemInfo();
                    break;
                default:
                    if (!choice.isEmpty()) {
                        System.out.println("❌ Invalid choice. Please enter 0-4, ?, or !");
                    }
            }
        }
        shutdown();
    }
    
    private void initialize() {
        System.out.println("🚀 Initializing Sephirah modules...\n");
        
        // Register Chesed module
        chesedModule = new ChesedSephirah();
        chesedModule.initialize();
        sephirot.put("chesed", chesedModule);
        
        System.out.println("✅ Chesed module ready: Umamusume Database System");
        System.out.println("📊 Total modules: " + sephirot.size());
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📚 PROJECT SEPHIRAH - MAIN MENU");
        System.out.println("═".repeat(60));
        System.out.println();
        System.out.println("  1️⃣  CHESED   - Umamusume Database & Algorithms");
        System.out.println("  2️⃣  GEVURAH  - RPG Market Simulation (Coming Soon)");
        System.out.println("  3️⃣  TIFERET  - Boolean Logic System (Coming Soon)");
        System.out.println("  4️⃣  BINAH    - Library Architecture (Coming Soon)");
        System.out.println();
        System.out.println("  0️⃣  EXIT");
        System.out.println();
        System.out.println("  ❓ HELP   🔧 SYSTEM INFO");
        System.out.println("─".repeat(60));
        System.out.print("📌 Enter choice (1/2/3/4/0): ");
        System.out.flush();
    }
    
    private void runSephirah(String name) {
        if ("chesed".equalsIgnoreCase(name)) {
            System.out.println("\n" + "✨".repeat(30));
            System.out.println("   ENTERING CHESED MODULE");
            System.out.println("✨".repeat(30));
            chesedModule.showChesedMenu();
        } else {
            Sephirah sephirah = sephirot.get(name);
            if (sephirah != null) {
                System.out.println("\n" + "✨".repeat(30));
                System.out.println("   LAUNCHING: " + sephirah.getName().toUpperCase());
                System.out.println("✨".repeat(30));
                sephirah.demonstrate();
            }
        }
    }
    
    private void showHelp() {
        System.out.println("""
            📖 PROJECT SEPHIRAH HELP

            This framework demonstrates CS concepts through 4 modules:

            1. CHESED - Object-Oriented Programming & Algorithms
               • Umamusume character database
               • Sorting algorithms (Bubble, Merge, Quick, etc.)
               • Search algorithms (Linear, Binary, etc.)
               • Track proficiency analysis

            2. GEVURAH - Algorithm Complexity
               • RPG market simulation with 20k agents
               • Time/Space complexity analysis

            3. TIFERET - Discrete Mathematics
               • Boolean algebra for game requirements
               • Logic gates and circuit design

            4. BINAH - System Architecture
               • Library management system
               • State machines and graph theory

            Press Enter to continue..."""
        );
        scanner.nextLine();
    }

    private void showSystemInfo() {
        System.out.printf(
            "\n⚙️  SYSTEM INFORMATION%n%n" +
            "Java Version: %s%n" +
            "OS: %s%n" +
            "Available Processors: %d%n" +
            "Free Memory: %d MB%n%n" +
            "Project Location: I:\\REPO\\Learning-101\\project-sephirah%n" +
            "GitHub: https://github.com/ProfessorCroquette/Learning-101%n%n" +
            "Press Enter to continue...",
            System.getProperty("java.version"),
            System.getProperty("os.name"),
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().freeMemory() / (1024 * 1024)
        );
        scanner.nextLine();
    }

    
    private void shutdown() {
        System.out.println("\n🛑 Shutting down...");
        for (Sephirah sephirah : sephirot.values()) {
            sephirah.shutdown();
        }
        scanner.close();
    }
}
