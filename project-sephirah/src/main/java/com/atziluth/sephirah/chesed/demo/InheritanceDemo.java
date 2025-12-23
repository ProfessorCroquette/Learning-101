package com.atziluth.sephirah.chesed.demo;

import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.sorting.UmapyoiEnhancedSorter;
import com.atziluth.sephirah.chesed.util.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Demonstrates inheritance and class hierarchies.
 */
public class InheritanceDemo {
    private static final Logger logger = LoggerFactory.getLogger(InheritanceDemo.class);
    
    public static void demonstrate() {
        logger.info("\n" + "=".repeat(60));
        logger.info("INHERITANCE DEMONSTRATION");
        logger.info("=".repeat(60));
        
        // Create characters of different types
        List<AbstractUma> characters = new ArrayList<>();
        characters.add(new SpeedUma("Speed Character", "スピード", Umamusume.Rarity.UR));
        characters.add(new StaminaUma("Stamina Character", "スタミナ", Umamusume.Rarity.SSR));
        characters.add(new UniqueSkillUma("Unique Character", "ユニーク", Umamusume.Rarity.SR,
                                         "Special Skill", "A powerful skill", 90));
        
        logger.info("\nAll share AbstractUma base class:");
        for (AbstractUma uma : characters) {
            logger.info("  {} ({}) - Type: {} - Specialty: {}", 
                uma.getName(), uma.getJapaneseName(), 
                uma.getCharacterType(), uma.getSpecialty());
        }
        
        logger.info("\nInheritance chain visualization:");
        logger.info("  AbstractUma (Base Class)");
        logger.info("  ├── SpeedUma (Concrete)");
        logger.info("  ├── StaminaUma (Concrete)");
        logger.info("  └── UniqueSkillUma (Concrete + implements SpecialAbility)");
        
        // Group by type
        Map<String, List<AbstractUma>> grouped = UmapyoiEnhancedSorter.groupByType(characters);
        logger.info("\nGrouped by type:");
        for (String type : grouped.keySet()) {
            logger.info("  {}: {} characters", type, grouped.get(type).size());
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}
