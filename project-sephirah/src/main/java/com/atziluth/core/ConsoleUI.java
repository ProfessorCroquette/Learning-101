package com.atziluth.core;

import java.util.Scanner;
import java.util.List;

/**
 * Clean console UI utilities for consistent display
 */
public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String LINE = "═".repeat(60);
    private static final String THIN_LINE = "─".repeat(60);
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void displayHeader(String title) {
        System.out.println("\n" + LINE);
        System.out.println("    " + title.toUpperCase());
        System.out.println(LINE);
    }
    
    public static void displaySubHeader(String subtitle) {
        System.out.println("\n" + THIN_LINE);
        System.out.println("  " + subtitle);
        System.out.println(THIN_LINE);
    }
    
    public static void displayMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, options[i]);
        }
        System.out.println(THIN_LINE);
    }
    
    public static void displayCharacterCard(String title, String content) {
        System.out.println("\n┌──────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-56s │%n", title);
        System.out.println("├──────────────────────────────────────────────────────────┤");
        String[] lines = content.split("\n");
        for (String line : lines) {
            System.out.printf("│ %-56s │%n", line);
        }
        System.out.println("└──────────────────────────────────────────────────────────┘");
    }
    
    public static void displaySuccess(String message) {
        System.out.println("✅ " + message);
    }
    
    public static void displayError(String message) {
        System.out.println("❌ " + message);
    }
    
    public static void displayInfo(String message) {
        System.out.println("ℹ️  " + message);
    }
    
    public static void displayLoading(String message) {
        System.out.print("⏳ " + message + " ");
    }
    
    public static void finishLoading() {
        System.out.println("✅");
    }
    
    public static String prompt(String message) {
        System.out.print("> " + message + ": ");
        return scanner.nextLine().trim();
    }
    
    public static int promptInt(String message) {
        while (true) {
            try {
                System.out.print("> " + message + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                displayError("Please enter a valid number");
            }
        }
    }
    
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    public static void displaySeparator() {
        System.out.println("\n" + THIN_LINE);
    }
}