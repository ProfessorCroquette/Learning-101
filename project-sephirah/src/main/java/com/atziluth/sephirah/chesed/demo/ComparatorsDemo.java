package com.atziluth.sephirah.chesed.demo;

import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.sorting.Comparators;
import com.atziluth.sephirah.chesed.util.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates the Comparators utility class with various sorting strategies
 */
public class ComparatorsDemo {
    private static final Logger logger = LoggerFactory.getLogger(ComparatorsDemo.class);
    
    public static void main(String[] args) {
        demonstrate();
    }
    
    public static void demonstrate() {
        logger.info("\n" + "=".repeat(70));
        logger.info("COMPARATORS UTILITY DEMONSTRATION");
        logger.info("=".repeat(70));
        
        // Generate test data
        List<Umamusume> characters = DataGenerator.generateMockUmamususeList(5);
        
        demonstrateBasicComparators(characters);
        demonstrateStatComparators(characters);
        demonstrateChainedComparators(characters);
        demonstrateAbstractUmaComparators();
    }
    
    private static void demonstrateBasicComparators(List<Umamusume> characters) {
        logger.info("\n1. BASIC COMPARATORS");
        logger.info("-".repeat(70));
        
        // Sort by name
        logger.info("Sort by Name (A-Z):");
        characters.stream()
                .sorted(Comparators.byName())
                .forEach(c -> logger.info("  {}", c.getName()));
        
        // Sort by rarity
        logger.info("\nSort by Rarity (UR → N):");
        characters.stream()
                .sorted(Comparators.byRarity())
                .forEach(c -> logger.info("  {} ({})", c.getName(), c.getRarity()));
        
        // Sort by type
        logger.info("\nSort by Type (Character Category):");
        characters.stream()
                .sorted(Comparators.byType())
                .forEach(c -> logger.info("  {} - {}", c.getName(), c.getType()));
    }
    
    private static void demonstrateStatComparators(List<Umamusume> characters) {
        logger.info("\n2. STAT-BASED COMPARATORS");
        logger.info("-".repeat(70));
        
        // Sort by total stats
        logger.info("Sort by Total Stats (Highest First):");
        characters.stream()
                .sorted(Comparators.byTotalStats())
                .forEach(c -> logger.info("  {} - Total: {}", c.getName(), c.getTotalStats()));
        
        // Sort by speed
        logger.info("\nSort by Speed Stat (Highest First):");
        characters.stream()
                .sorted(Comparators.bySpeed())
                .forEach(c -> logger.info("  {} - Speed: {}", c.getName(), c.getStats().getSpeed()));
        
        // Sort by stamina
        logger.info("\nSort by Stamina Stat (Highest First):");
        characters.stream()
                .sorted(Comparators.byStamina())
                .forEach(c -> logger.info("  {} - Stamina: {}", c.getName(), c.getStats().getStamina()));
    }
    
    private static void demonstrateChainedComparators(List<Umamusume> characters) {
        logger.info("\n3. CHAINED COMPARATORS (Multiple Sort Keys)");
        logger.info("-".repeat(70));
        
        // Sort by rarity, then by stats
        logger.info("Sort by Rarity (UR first), then by Total Stats:");
        characters.stream()
                .sorted(Comparators.byRarityThenStats())
                .forEach(c -> logger.info("  {} - Rarity: {}, Stats: {}", 
                        c.getName(), c.getRarity(), c.getTotalStats()));
        
        // Sort by type, then by name
        logger.info("\nSort by Type, then by Name:");
        characters.stream()
                .sorted(Comparators.byTypeThenName())
                .forEach(c -> logger.info("  {} ({}) - {}", 
                        c.getName(), c.getType(), c.getJapaneseName()));
        
        // Sort by rarity, type, then stats
        logger.info("\nSort by Rarity, Type, then Stats (Complex Chain):");
        characters.stream()
                .sorted(Comparators.byRarityTypeThenStats())
                .forEach(c -> logger.info("  {} ({}) - {} - {}", 
                        c.getRarity(), c.getType(), c.getName(), c.getTotalStats()));
    }
    
    private static void demonstrateAbstractUmaComparators() {
        logger.info("\n4. ABSTRACT UMA COMPARATORS");
        logger.info("-".repeat(70));
        
        // Create AbstractUma instances
        List<AbstractUma> umas = List.of(
                new SpeedUma("Speed Test", "スピード", Umamusume.Rarity.SSR),
                new StaminaUma("Stamina Test", "スタミナ", Umamusume.Rarity.UR),
                new SpeedUma("Another Speed", "スピード2", Umamusume.Rarity.SR)
        );
        
        // Sort by name
        logger.info("AbstractUma sorted by Name:");
        umas.stream()
                .sorted(Comparators.umaByName())
                .forEach(u -> logger.info("  {}", u.getName()));
        
        // Sort by total stats
        logger.info("\nAbstractUma sorted by Total Stats:");
        umas.stream()
                .sorted(Comparators.umaByTotalStats())
                .forEach(u -> logger.info("  {} - Stats: {}", u.getName(), u.getTotalStats()));
        
        // Sort by type
        logger.info("\nAbstractUma sorted by Type:");
        umas.stream()
                .sorted(Comparators.umaByType())
                .forEach(u -> logger.info("  {} - Type: {}", u.getName(), u.getCharacterType()));
    }
}
