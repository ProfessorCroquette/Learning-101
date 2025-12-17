package com.atziluth;

import java.util.Scanner;

/**
 * Main entry point for Project Sephirah.
 */
public class ProjectSephirah {
    
    public static void main(String[] args) {
        System.out.println("""
            ╔═════════════════════════
                       PROJECT SEPHIRAH v1.0.0           
                 Computational Kabbalah Framework        
            
            """);
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("""
                \n SELECT A SEPHIRAH:
                
                1.  Chesed - Umamusume Database & Algorithms
                2.   Gevurah - RPG Market Simulation (Coming Soon)
                3.  Tiferet - Boolean Logic System (Coming Soon)
                4.  Binah - Library Architecture (Coming Soon)
                0.  Exit
                """);
            
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> runChesed();
                case "0" -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void runChesed() {
        System.out.println("\n" + "".repeat(50));
        System.out.println(" SEPHIRAH CHESED: UMAMUSUME DATABASE SYSTEM");
        System.out.println("".repeat(50));
        System.out.println("Implementing sorting algorithms...");
        System.out.println("Building track proficiency system...");
        System.out.println("Loading Umamusume data...");
        System.out.println("\n Chesed module coming soon!");
    }
}
