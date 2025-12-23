package com.atziluth.sephirah.chesed;

import com.atziluth.core.Sephirah;
import com.atziluth.sephirah.chesed.model.Umamusume;
import com.atziluth.sephirah.chesed.sorting.UmamusumeSorter;
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
    
    private void demonstrateSorting() {
        if (characters.isEmpty()) {
            logger.warn("No characters available for sorting demonstration");
            return;
        }
        
        logger.info("\n📊 SORTING ALGORITHMS DEMONSTRATION");
        logger.info("-".repeat(60));
        
        logger.info("Sorting {} characters by total stats...", characters.size());
        logger.info("Using Java Collections.sort() for reference...");
        
        List<Umamusume> copy = new ArrayList<>(characters);
        copy.sort(Comparator.comparingInt(Umamusume::getTotalStats).reversed());
        
        logger.info("Top 3 characters by total stats:");
        copy.stream().limit(3).forEach(c -> 
            logger.info("  {} - Total: {}", c.getName(), c.getTotalStats())
        );
    }
    
    private void demonstrateSearching() {
        if (characters.isEmpty()) {
            logger.warn("No characters available for search demonstration");
            return;
        }
        
        logger.info("\n🔎 SEARCH ALGORITHMS DEMONSTRATION");
        logger.info("-".repeat(60));
        
        // Build simple search index
        Map<String, Umamusume> searchIndex = new HashMap<>();
        for (Umamusume uma : characters) {
            searchIndex.put(uma.getName().toLowerCase(), uma);
        }
        
        logger.info("Built search index with {} characters", searchIndex.size());
        
        // Perform search
        String searchTerm = characters.get(0).getName();
        logger.info("Searching for: '{}'", searchTerm);
        
        Umamusume found = searchIndex.get(searchTerm.toLowerCase());
        if (found != null) {
            logger.info("✅ Found: {} ({})", found.getName(), found.getRarity());
        } else {
            logger.warn("⚠️  Character not found");
        }
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
