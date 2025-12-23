package com.atziluth.sephirah.chesed.demo;

import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.factory.UmaFactory;
import com.atziluth.sephirah.chesed.util.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Demonstrates core OOP concepts:
 * - Encapsulation
 * - Inheritance
 * - Polymorphism
 * - Abstraction
 */
public class DemoOOPConcepts {
    private static final Logger logger = LoggerFactory.getLogger(DemoOOPConcepts.class);
    
    public static void demonstrate() {
        logger.info("\n" + "=".repeat(60));
        logger.info("OOP CONCEPTS DEMONSTRATION");
        logger.info("=".repeat(60));
        
        demonstrateEncapsulation();
        demonstrateInheritance();
        demonstratePolymorphism();
        demonstrateAbstraction();
    }
    
    private static void demonstrateEncapsulation() {
        logger.info("\n1. ENCAPSULATION");
        logger.info("-".repeat(60));
        
        SpeedUma uma = new SpeedUma("Special Week", "スペシャルウィーク", Umamusume.Rarity.UR);
        uma.setId(1001);
        
        logger.info("Character created with encapsulated fields:");
        logger.info("  Name: {}", uma.getName());
        logger.info("  Japanese: {}", uma.getJapaneseName());
        logger.info("  Speed: {}", uma.getStats().getSpeed());
        logger.info("  Total Stats: {}", uma.getTotalStats());
    }
    
    private static void demonstrateInheritance() {
        logger.info("\n2. INHERITANCE");
        logger.info("-".repeat(60));
        
        List<AbstractUma> characters = new java.util.ArrayList<>();
        characters.add(new SpeedUma("Silence Suzuka", "サイレンススズカ", Umamusume.Rarity.UR));
        characters.add(new StaminaUma("Tokai Teio", "トウカイテイオー", Umamusume.Rarity.SSR));
        characters.add(new SpeedUma("Oguri Cap", "オグリキャップ", Umamusume.Rarity.SSR));
        
        logger.info("Characters inheriting from AbstractUma:");
        for (AbstractUma uma : characters) {
            logger.info("  {} - Type: {} - Power: {}", 
                uma.getName(), uma.getCharacterType(), uma.calculateSpecialPower());
        }
    }
    
    private static void demonstratePolymorphism() {
        logger.info("\n3. POLYMORPHISM");
        logger.info("-".repeat(60));
        
        AbstractUma speed = new SpeedUma("Sakura Bakushin O", "サクラバクシンオー", Umamusume.Rarity.SR);
        AbstractUma stamina = new StaminaUma("Mejiro Mcqueen", "メジロマックイーン", Umamusume.Rarity.SR);
        
        AbstractUma[] umas = {speed, stamina};
        
        logger.info("Different types respond differently to same method:");
        for (AbstractUma uma : umas) {
            logger.info("  {}: {}", uma.getName(), uma.getSpecialty());
        }
    }
    
    private static void demonstrateAbstraction() {
        logger.info("\n4. ABSTRACTION");
        logger.info("-".repeat(60));
        
        UniqueSkillUma uniqueUma = new UniqueSkillUma(
            "Gold Ship", "ゴールドシップ", Umamusume.Rarity.SR,
            "Golden Route", "Finds the optimal racing path", 85
        );
        
        logger.info("Abstract ability interface implementation:");
        logger.info("  Ability: {}", uniqueUma.getAbilityName());
        logger.info("  Description: {}", uniqueUma.getAbilityDescription());
        logger.info("  Power (inactive): {}", uniqueUma.getPowerLevel());
        
        uniqueUma.activate();
        logger.info("  Power (active): {}", uniqueUma.calculateSpecialPower());
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}
