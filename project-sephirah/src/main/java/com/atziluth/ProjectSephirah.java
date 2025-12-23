package com.atziluth;

import com.atziluth.core.ConsoleUI;
import com.atziluth.core.Sephirah;
import com.atziluth.core.SubjectRegistry;
import com.atziluth.sephirah.chesed.ChesedSephirah;

/**
 * Clean, modern main entry point for Project Sephirah
 */
public class ProjectSephirah {
    private static final SubjectRegistry registry = new SubjectRegistry();
    
    public static void main(String[] args) {
        ConsoleUI.clearScreen();
        displayWelcome();
        initializeModules();
        
        boolean running = true;
        while (running) {
            ConsoleUI.clearScreen();
            running = displayMainMenu();
        }
        
        displayGoodbye();
    }
    
    private static void displayWelcome() {
        ConsoleUI.displayHeader("PROJECT SEPHIRAH");
        System.out.println("""
            A modular framework for learning Computer Science concepts
            through practical implementation and real-world examples.
            
            Version: 1.0.0 | Chesed Module: OOP & Algorithms
            """);
        ConsoleUI.pressEnterToContinue();
    }
    
    private static void initializeModules() {
        ConsoleUI.displayLoading("Initializing modules");
        
        // Register Chesed (OOP & Algorithms)
        registry.register("chesed", new ChesedSephirah());
        ConsoleUI.finishLoading();
        ConsoleUI.displaySuccess("Chesed module loaded (OOP & Algorithms)");
        
        // Placeholder for future modules
        ConsoleUI.displayInfo("Gevurah module (Algorithm Complexity) - Coming Soon");
        ConsoleUI.displayInfo("Tiferet module (Formal Logic) - Coming Soon");
        
        ConsoleUI.pressEnterToContinue();
    }
    
    private static boolean displayMainMenu() {
        ConsoleUI.displayHeader("MAIN MENU");
        System.out.println("""
            Select a module to explore:
            
            Modules represent different computer science subjects.
            Each module contains practical implementations,
            demonstrations, and interactive tools.
            """);
        
        String[] options = {
            "Chesed - OOP & Algorithms",
            "About Project Sephirah",
            "Exit"
        };
        
        ConsoleUI.displayMenu(options);
        
        int choice = ConsoleUI.promptInt("Select option (1-3)");
        
        switch (choice) {
            case 1:
                runSephirah("chesed");
                return true;
            case 2:
                displayAbout();
                return true;
            case 3:
                return false;
            default:
                ConsoleUI.displayError("Invalid selection. Please try again.");
                ConsoleUI.pressEnterToContinue();
                return true;
        }
    }
    
    private static void runSephirah(String name) {
        Sephirah module = registry.getSubject(name);
        if (module != null) {
            module.initialize();
            module.demonstrate();
        }
    }
    
    private static void displayAbout() {
        ConsoleUI.clearScreen();
        ConsoleUI.displayHeader("ABOUT PROJECT SEPHIRAH");
        
        System.out.println("""
            🎯 PURPOSE
            Project Sephirah is a learning framework that transforms
            theoretical computer science concepts into practical,
            runnable Java code.
            
            📚 LEARNING APPROACH
            • Each module (Sephirah) focuses on one subject
            • Theory is connected directly to implementation
            • Real-world examples (Umamusume API) make concepts tangible
            • Progressive difficulty with clear learning paths
            
            🏗️ ARCHITECTURE
            • Modular design for easy expansion
            • Clean separation of concerns
            • Professional software engineering patterns
            • Comprehensive error handling and logging
            
            🔮 ROADMAP
            • Chesed  ✓ OOP & Algorithms (Current)
            • Gevurah → Algorithm Complexity Analysis
            • Tiferet → Formal Logic & Proof Systems
            • Netzach → Discrete Mathematics
            • Hod    → System Architecture
            
            👨‍💻 CREATED BY
            Atziluth - A computer science student exploring
            the practical side of theoretical concepts.
            """);
        
        ConsoleUI.displaySeparator();
        ConsoleUI.prompt("Press Enter to return to main menu");
    }
    
    private static void displayGoodbye() {
        ConsoleUI.clearScreen();
        ConsoleUI.displayHeader("THANK YOU");
        System.out.println("""
            Thank you for exploring Project Sephirah!
            
            Keep learning, keep coding, keep growing.
            
            "The beautiful thing about learning is that
            nobody can take it away from you."
            ― B.B. King
            """);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}