package com.atziluth.sephirah.chesed;

import com.atziluth.core.Sephirah;
import com.atziluth.sephirah.chesed.model.Umamusume;
import com.atziluth.sephirah.chesed.sorting.UmamusumeSorter;
import com.atziluth.sephirah.chesed.sorting.Comparators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Chesed Sephirah - OOP & Algorithms Module
 * Demonstrates: Object-Oriented Programming, Sorting & Search Algorithms
 */
public class ChesedSephirah implements Sephirah {
    private static final Logger logger = LoggerFactory.getLogger(ChesedSephirah.class);
    
    private List<Umamusume> characters;
    private Scanner scanner;
    
    public ChesedSephirah() {
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public String getName() {
        return "Chesed";
    }
    
    @Override
    public String getConcept() {
        return "Object-Oriented Programming & Algorithms";
    }
    
    @Override
    public String getDescription() {
        return "Demonstrates OOP principles and algorithm complexity through Umamusume database management";
    }
    
    @Override
    public void initialize() {
        logger.info("Initializing Chesed module...");
        characters = new ArrayList<>();
        logger.info("Chesed module initialized");
    }
    
    @Override
    public void demonstrate() {
        logger.info("\n" + "=".repeat(60));
        logger.info("CHESED: OOP & ALGORITHMS DEMONSTRATION");
        logger.info("=".repeat(60));
        
        // Generate mock characters
        characters = generateMockCharacters();
        
        // Demonstrate Sorting Algorithms
        demonstrateSorting();
        
        // Demonstrate Search Algorithms
        demonstrateSearching();
        
        logger.info("=" .repeat(60));
    }
    
    // Interactive menu for Chesed module
    public void showChesedMenu() {
        boolean running = true;
        while (running) {
            displayChesedMenu();
            String choice = "";
            try {
                if (scanner.hasNextLine()) {
                    choice = scanner.nextLine().trim();
                } else {
                    choice = "0";
                }
            } catch (Exception e) {
                System.out.println("Input error: " + e.getMessage());
                choice = "0";
            }
            
            switch (choice) {
                case "1":
                    demonstrateOOP();
                    break;
                case "2":
                    demonstrateSorting();
                    break;
                case "3":
                    demonstrateSearching();
                    break;
                case "4":
                    demonstrateComparators();
                    break;
                case "5":
                    demonstrate(); // Full demonstration
                    break;
                case "0":
                    running = false;
                    System.out.println("\n👈 Returning to main menu...\n");
                    break;
                default:
                    if (!choice.isEmpty()) {
                        System.out.println("❌ Invalid choice. Please enter 0-5");
                    }
            }
        }
    }
    
    private void displayChesedMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("⚙️  CHESED MODULE MENU");
        System.out.println("═".repeat(60));
        System.out.println();
        System.out.println("  1️⃣  OOP Concepts Demo");
        System.out.println("  2️⃣  Sorting Algorithms Demo");
        System.out.println("  3️⃣  Search Algorithms Demo");
        System.out.println("  4️⃣  Comparators & Strategies");
        System.out.println("  5️⃣  Full Demonstration");
        System.out.println();
        System.out.println("  0️⃣  Back to Main Menu");
        System.out.println("─".repeat(60));
        System.out.print("📌 Enter choice (0-5): ");
        System.out.flush();
    }
    
    private void demonstrateOOP() {
        logger.info("\n" + "▼".repeat(30));
        logger.info("OOP CONCEPTS DEMONSTRATION");
        logger.info("▼".repeat(30));
        
        characters = generateMockCharacters();
        
        logger.info("\n1️⃣  ENCAPSULATION");
        logger.info("   Private fields with getters/setters");
        characters.stream().limit(2).forEach(c -> 
            logger.info("   Character: {} | Rarity: {} | Type: {}", 
                c.getName(), c.getRarity(), c.getType())
        );
        
        logger.info("\n2️⃣  INHERITANCE");
        logger.info("   AbstractUma base class with concrete implementations");
        logger.info("   SpeedUma, StaminaUma, PowerUma, etc.");
        
        logger.info("\n3️⃣  POLYMORPHISM");
        logger.info("   Different character types, same interface");
        characters.stream().map(Umamusume::getType).distinct()
            .forEach(t -> logger.info("   - Type: {}", t));
        
        logger.info("\n4️⃣  ABSTRACTION");
        logger.info("   Abstract methods in base class");
        logger.info("   Each subclass implements its own behavior");
        logger.info("▲".repeat(30) + "\n");
    }
    
    private void demonstrateSorting() {
        if (characters.isEmpty()) {
            characters = generateMockCharacters();
        }
        
        logger.info("\n" + "▼".repeat(30));
        logger.info("📊 SORTING ALGORITHMS");
        logger.info("▼".repeat(30));
        
        // Create copies for different sorts
        List<Umamusume> byStats = new ArrayList<>(characters);
        List<Umamusume> byName = new ArrayList<>(characters);
        List<Umamusume> byRarity = new ArrayList<>(characters);
        
        // Merge sort by stats
        byStats.sort(Comparator.comparingInt(Umamusume::getTotalStats).reversed());
        logger.info("\n✔️  Sorted by Total Stats (descending):");
        byStats.forEach(c -> 
            logger.info("   {} [{}] - Stats: {}", c.getName(), c.getRarity(), c.getTotalStats())
        );
        
        // Sort by name
        byName.sort(Comparator.comparing(Umamusume::getName));
        logger.info("\n✔️  Sorted by Name (A-Z):");
        byName.forEach(c -> 
            logger.info("   {} [{}]", c.getName(), c.getType())
        );
        
        // Sort by rarity
        byRarity.sort(Comparator.comparing(Umamusume::getRarity));
        logger.info("\n✔️  Sorted by Rarity:");
        byRarity.forEach(c -> 
            logger.info("   {} [{}] - Rarity: {}", c.getName(), c.getType(), c.getRarity())
        );
        
        logger.info("▲".repeat(30) + "\n");
    }
    
    private void demonstrateSearching() {
        if (characters.isEmpty()) {
            characters = generateMockCharacters();
        }
        
        logger.info("\n" + "▼".repeat(30));
        logger.info("🔎 SEARCH ALGORITHMS");
        logger.info("▼".repeat(30));
        
        // Build search index
        Map<String, Umamusume> searchIndex = new HashMap<>();
        for (Umamusume uma : characters) {
            searchIndex.put(uma.getName().toLowerCase(), uma);
        }
        
        logger.info("\n✔️  Search Index built with {} characters", characters.size());
        logger.info("   Available characters: {}", searchIndex.keySet());
        
        // Perform multiple searches
        String[] searchTerms = {"Special Week", "Tokai Teio", "Unknown"};
        for (String term : searchTerms) {
            Umamusume found = searchIndex.get(term.toLowerCase());
            if (found != null) {
                logger.info("\n   🎯 Search: '{}' → FOUND");
                logger.info("      Name: {}", found.getName());
                logger.info("      Rarity: {}", found.getRarity());
                logger.info("      Type: {}", found.getType());
                logger.info("      Stats: {}", found.getTotalStats());
            } else {
                logger.info("\n   ❌ Search: '{}' → NOT FOUND", term);
            }
        }
        
        logger.info("\n" + "▲".repeat(30) + "\n");
    }
    
    private void demonstrateComparators() {
        if (characters.isEmpty()) {
            characters = generateMockCharacters();
        }
        
        logger.info("\n" + "▼".repeat(30));
        logger.info("⚡ COMPARATORS & STRATEGIES");
        logger.info("▼".repeat(30));
        
        logger.info("\nDemonstrating Strategy Pattern with Comparators:");
        
        // Different sorting strategies
        logger.info("\n1️⃣  Sort by Name:");
        characters.stream()
            .sorted(Comparators.byName())
            .forEach(c -> logger.info("   {}", c.getName()));
        
        logger.info("\n2️⃣  Sort by Rarity:");
        characters.stream()
            .sorted(Comparators.byRarity())
            .forEach(c -> logger.info("   {} [{}]", c.getName(), c.getRarity()));
        
        logger.info("\n3️⃣  Sort by Total Stats (Descending):");
        characters.stream()
            .sorted(Comparators.byTotalStats())
            .forEach(c -> logger.info("   {} - {}", c.getName(), c.getTotalStats()));
        
        logger.info("\n4️⃣  Chained Sort (Rarity → Stats):");
        characters.stream()
            .sorted(Comparators.byRarityThenStats())
            .forEach(c -> logger.info("   {} [{}] - Stats: {}", c.getName(), c.getRarity(), c.getTotalStats()));
        
        logger.info("\n▲".repeat(30) + "\n");
    }
    
    private List<Umamusume> generateMockCharacters() {
        List<Umamusume> mockChars = new ArrayList<>();
        
        String[] names = {"Special Week", "Silence Suzuka", "Tokai Teio", "Oguri Cap", "Gold Ship"};
        Umamusume.Rarity[] rarities = {
            Umamusume.Rarity.UR, Umamusume.Rarity.SSR, Umamusume.Rarity.SR,
            Umamusume.Rarity.SR, Umamusume.Rarity.SSR
        };
        Umamusume.CharacterType[] types = {
            Umamusume.CharacterType.RUNNER,
            Umamusume.CharacterType.STAMINA,
            Umamusume.CharacterType.POWER,
            Umamusume.CharacterType.GUTS,
            Umamusume.CharacterType.INTELLIGENCE
        };
        
        for (int i = 0; i < names.length; i++) {
            Umamusume uma = new Umamusume(names[i], rarities[i], types[i]);
            uma.setId(1001 + i);
            
            Umamusume.Stats stats = new Umamusume.Stats(
                70 + i * 5, 75 + i * 3, 70 + i * 2, 80 - i * 2, 85 + i * 1
            );
            uma.setStats(stats);
            mockChars.add(uma);
        }
        
        logger.info("Generated {} mock characters", mockChars.size());
        return mockChars;
    }
    
    @Override
    public void shutdown() {
        logger.info("Shutting down Chesed module...");
        logger.info("Chesed module shut down");
    }
}
