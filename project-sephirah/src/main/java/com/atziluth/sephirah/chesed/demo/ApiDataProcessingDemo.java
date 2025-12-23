package com.atziluth.sephirah.chesed.demo;

import com.atziluth.sephirah.chesed.api.CharacterService;
import com.atziluth.sephirah.chesed.api.ApiConfig;
import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.factory.UmaFactory;
import com.atziluth.sephirah.chesed.sorting.UmamusumeSorter;
import com.atziluth.sephirah.chesed.util.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Demonstrates API integration with algorithms.
 */
public class ApiDataProcessingDemo {
    private static final Logger logger = LoggerFactory.getLogger(ApiDataProcessingDemo.class);
    
    public static void demonstrate() {
        logger.info("\n" + "=".repeat(60));
        logger.info("API DATA PROCESSING DEMONSTRATION");
        logger.info("=".repeat(60));
        
        // Generate mock API characters
        logger.info("\n1. Simulating API Response Processing");
        logger.info("-".repeat(60));
        
        List<Umamusume> apiCharacters = DataGenerator.generateMockUmamususeList(5);
        logger.info("Received {} characters from simulated API", apiCharacters.size());
        
        // Sort by total stats
        logger.info("\n2. Sorting Results (Algorithm Demo)");
        logger.info("-".repeat(60));
        
        UmamusumeSorter sorter = new UmamusumeSorter();
        List<Umamusume> sorted = sorter.mergeSort(apiCharacters, 
            Comparator.comparingInt(Umamusume::getTotalStats).reversed());
        
        logger.info("Top 3 by total stats:");
        sorted.stream().limit(3).forEach(u -> 
            logger.info("  {} - Total: {}", u.getName(), u.getTotalStats())
        );
        
        // Filter and process
        logger.info("\n3. Filtering & Processing");
        logger.info("-".repeat(60));
        
        long rarityCount = apiCharacters.stream()
            .filter(u -> u.getRarity() == Umamusume.Rarity.UR)
            .count();
        
        logger.info("UR rarity characters: {}", rarityCount);
        
        // Factory pattern integration
        logger.info("\n4. Factory Pattern Integration");
        logger.info("-".repeat(60));
        
        List<AbstractUma> enhanced = new ArrayList<>();
        for (Umamusume uma : apiCharacters) {
            // Simulate creating enhanced models from API data
            AbstractUma enhancedUma = UmaFactory.createUma(
                UmaFactory.UmaType.SPEED,
                uma.getName(),
                uma.getJapaneseName(),
                uma.getRarity()
            );
            enhanced.add(enhancedUma);
        }
        
        logger.info("Created {} enhanced models from API data", enhanced.size());
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}
