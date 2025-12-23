package com.atziluth.sephirah.chesed.api;

import com.atziluth.sephirah.chesed.model.UmapyoiCharacter;
import com.atziluth.sephirah.chesed.model.Umamusume;
import com.atziluth.core.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to display wiki-scraped character data in console
 * Shows skills, stats, track type, and other data from Umamusume model
 */
public class WikiDataDisplay {
    private static final Logger logger = LoggerFactory.getLogger(WikiDataDisplay.class);
    
    /**
     * Display Umamusume model data in formatted table
     */
    public static void displayWikiEnrichedData(Umamusume character) {
        if (character == null) {
            ConsoleUI.displayError("Character data not available");
            return;
        }
        
        try {
            String characterName = character.getName();
            if (characterName == null || characterName.isEmpty()) {
                ConsoleUI.displayError("Character name not available");
                return;
            }
            
            ConsoleUI.displayHeader("📚 WIKI ENRICHED DATA FOR " + characterName.toUpperCase());
            
            // Display stats if available
            Umamusume.Stats stats = character.getStats();
            if (stats != null) {
                if (stats.getSpeed() > 0) {
                    System.out.println("⚡ Speed: " + stats.getSpeed());
                }
                if (stats.getStamina() > 0) {
                    System.out.println("❤️  Stamina: " + stats.getStamina());
                }
                if (stats.getPower() > 0) {
                    System.out.println("💪 Power: " + stats.getPower());
                }
                if (stats.getGuts() > 0) {
                    System.out.println("🔥 Guts: " + stats.getGuts());
                }
                if (stats.getIntelligence() > 0) {
                    System.out.println("🧠 Intelligence: " + stats.getIntelligence());
                }
                
                int total = stats.getTotal();
                if (total > 0) {
                    System.out.println("📊 Total: " + total);
                }
            }
            
            // Display track proficiencies
            if (character.getProficiencies() != null && !character.getProficiencies().isEmpty()) {
                System.out.println("\n🏇 TRACK PROFICIENCIES:");
                for (com.atziluth.sephirah.chesed.model.TrackProficiency prof : character.getProficiencies()) {
                    System.out.println("  • " + prof.toString());
                }
            }
            
            // Display rarity
            if (character.getRarity() != null) {
                System.out.println("\n✨ Rarity: " + character.getRarity().getDisplayName() + " (" + character.getRarity() + ")");
            }
            
            // Display type
            if (character.getType() != null) {
                System.out.println("🎯 Type: " + character.getType().getDescription());
            }
            
            System.out.println();
            
        } catch (Exception e) {
            logger.warn("Failed to display wiki enriched data: {}", e.getMessage());
            ConsoleUI.displayError("Could not display character data");
        }
    }
    
    /**
     * Display wiki-scraped stats from character name (legacy method)
     */
    public static void displayWikiEnrichedData(UmapyoiCharacter apiCharacter) {
        if (apiCharacter == null) {
            ConsoleUI.displayError("Character data not available");
            return;
        }
        
        try {
            // Use new scraper that returns Umamusume model
            Umamusume character = UmamusumeWikiScraper.enrichCharacterData(apiCharacter);
            displayWikiEnrichedData(character);
        } catch (Exception e) {
            logger.warn("Failed to display wiki enriched data: {}", e.getMessage());
            ConsoleUI.displayError("Could not retrieve additional wiki data");
        }
    }
    
    /**
     * Check if character has incomplete data and offer wiki lookup
     */
    public static boolean offerWikiLookup(UmapyoiCharacter character) {
        if (character == null) {
            return false;
        }
        
        try {
            // Check if character has basic data
            boolean hasBasicData = character.getNameEnglish() != null && 
                                  !character.getNameEnglish().isEmpty();
            
            if (hasBasicData) {
                System.out.println("\n💡 Additional data available from wiki. Fetch? (y/n): ");
                String response = new java.util.Scanner(System.in).nextLine().trim().toLowerCase();
                
                if (response.equals("y") || response.equals("yes")) {
                    displayWikiEnrichedData(character);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.debug("Wiki lookup offer failed: {}", e.getMessage());
        }
        
        return false;
    }
}
