package com.atziluth.sephirah.chesed.demo;

import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.util.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Demonstrates polymorphism in action.
 */
public class PolymorphismDemo {
    private static final Logger logger = LoggerFactory.getLogger(PolymorphismDemo.class);
    
    public static void demonstrate() {
        logger.info("\n" + "=".repeat(60));
        logger.info("POLYMORPHISM DEMONSTRATION");
        logger.info("=".repeat(60));
        
        // Generate mock characters
        List<AbstractUma> characters = DataGenerator.generateMockCharacters(5);
        
        logger.info("\nPolymorphic behavior - same method, different implementations:");
        for (AbstractUma uma : characters) {
            // calculateSpecialPower() is implemented differently in each subclass
            int power = uma.calculateSpecialPower();
            logger.info("  {}: calculateSpecialPower() = {} (Type: {})", 
                uma.getName(), power, uma.getClass().getSimpleName());
        }
        
        logger.info("\nRuntime type determination:");
        for (AbstractUma uma : characters) {
            String typeInfo = uma instanceof SpeedUma ? "SPEED SPECIALIST" :
                             uma instanceof StaminaUma ? "STAMINA SPECIALIST" :
                             uma instanceof UniqueSkillUma ? "UNIQUE SKILL USER" :
                             "UNKNOWN";
            logger.info("  {} -> {}", uma.getName(), typeInfo);
        }
        
        logger.info("\nPolymorphic interface (SpecialAbility):");
        if (!characters.isEmpty() && characters.get(0) instanceof UniqueSkillUma) {
            UniqueSkillUma skilled = (UniqueSkillUma) characters.get(0);
            
            logger.info("  Ability: {} - {}", 
                skilled.getAbilityName(), skilled.getAbilityDescription());
            logger.info("  Power Level: {}", skilled.getPowerLevel());
        }
    }
    
    public static void main(String[] args) {
        demonstrate();
    }
}
