package com.atziluth.sephirah.chesed.api;

import com.atziluth.sephirah.chesed.model.UmapyoiCharacter;
import com.atziluth.sephirah.chesed.model.Umamusume;
import com.atziluth.sephirah.chesed.model.TrackProficiency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// JSON parsing imports
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * Scrapes GameTora database for missing character data (stats, skills, track type, etc.)
 * Uses gametora.com/umamusume as primary source for complete character information
 * Returns fully populated Umamusume domain model objects
 */
public class UmamusumeWikiScraper {
    private static final Logger logger = LoggerFactory.getLogger(UmamusumeWikiScraper.class);
    
    private static final String WIKI_BASE_URL = "https://gametora.com/umamusume";
    private static final int TIMEOUT_MS = 15000;  // GameTora timeout
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    
    /**
     * Scrape GameTora using Character ID (Preferred Method)
     * Automatically handles base IDs (e.g., 1052) by appending '01' to reach the specific card page.
     * @param characterId The game ID of the character
     * @return Umamusume domain model with wiki-enriched data
     */
    public static Umamusume scrapeCharacterStats(int characterId) {
        // Fix for GameTora: Base IDs (4 digits) often point to a profile page without stats.
        // We append "01" to target the default playable card (6 digits).
        String urlId = String.valueOf(characterId);
        if (urlId.length() == 4) {
            urlId += "01";
            logger.debug("Adjusted base ID {} to {} for GameTora stats lookup", characterId, urlId);
        }

        String wikiUrl = WIKI_BASE_URL + "/characters/" + urlId;
        return scrapeFromUrl(wikiUrl, "ID: " + characterId);
    }

    /**
     * Scrape GameTora using Character Name (Fallback Method)
     * @param characterName The English name of the character
     * @return Umamusume domain model with wiki-enriched data
     */
    public static Umamusume scrapeCharacterStats(String characterName) {
        String wikiUrl = buildWikiUrl(characterName);
        return scrapeFromUrl(wikiUrl, "Name: " + characterName);
    }

    /**
     * Internal method to perform the actual scraping logic given a URL
     */
    private static Umamusume scrapeFromUrl(String wikiUrl, String identifier) {
        // Create base model
        Umamusume character = new Umamusume();
        // Identifier is just for logging/debugging context here
        
        // Container for extracted data
        Map<String, Object> stats = new HashMap<>();
        
        try {
            logger.info("Scraping GameTora for {}: {}", identifier, wikiUrl);
            
            // Fetch and parse HTML
            Document doc = Jsoup.connect(wikiUrl)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .followRedirects(true)
                .get();
            
            // Extract all available data
            extractStats(doc, stats);          // Profile data + game stats + rarity + aptitudes
            extractSkills(doc, stats);         // Songs and skills
            extractBiography(doc, stats);      // Character description
            extractRelationships(doc, stats);  // Related characters
            
            // Build domain model from extracted data
            // Note: If scraping by ID, name might be null initially, it will be filled by enricher later
            character = buildUmamusumeModel(stats);
            
            if (stats.size() >= 3) {
                logger.info("Successfully scraped {} fields from GameTora for {}", 
                    stats.size(), identifier);
            } else {
                logger.warn("Limited data extracted from GameTora for {}", identifier);
            }
            
        } catch (IOException e) {
            logger.warn("Failed to scrape GameTora for {}: {}", identifier, e.getMessage());
        } catch (Exception e) {
            logger.warn("Unexpected error scraping GameTora for {}: {}", identifier, e.getMessage());
        }
        
        return character;
    }
    
    /**
     * Build Umamusume domain model from extracted GameTora data
     */
    private static Umamusume buildUmamusumeModel(Map<String, Object> stats) {
        Umamusume character = new Umamusume();
        // Name is handled by the caller (enrichCharacterData) merging this object
        
        // Build and populate stats
        Umamusume.Stats modelStats = new Umamusume.Stats();
        if (stats.containsKey("speed")) {
            modelStats.setSpeed((Integer) stats.get("speed"));
        }
        if (stats.containsKey("stamina")) {
            modelStats.setStamina((Integer) stats.get("stamina"));
        }
        if (stats.containsKey("power")) {
            modelStats.setPower((Integer) stats.get("power"));
        }
        if (stats.containsKey("guts")) {
            modelStats.setGuts((Integer) stats.get("guts"));
        }
        if (stats.containsKey("intelligence")) {
            modelStats.setIntelligence((Integer) stats.get("intelligence"));
        }
        character.setStats(modelStats);
        
        // Set rarity enum
        if (stats.containsKey("rarity")) {
            try {
                String rarityStr = (String) stats.get("rarity");
                character.setRarity(Umamusume.Rarity.valueOf(rarityStr));
            } catch (IllegalArgumentException e) {
                character.setRarity(Umamusume.Rarity.N);
                logger.debug("Invalid rarity value from wiki: {}", stats.get("rarity"));
            }
        } else {
            character.setRarity(Umamusume.Rarity.N);
        }
        
        // Set character type enum
        if (stats.containsKey("type")) {
            try {
                String typeStr = (String) stats.get("type");
                // Map wiki type names to enum
                Umamusume.CharacterType charType = mapTypeToCharacterType(typeStr);
                character.setType(charType);
            } catch (Exception e) {
                logger.debug("Failed to set character type: {}", e.getMessage());
            }
        }
        
        // Set track proficiency if available
        if (stats.containsKey("trackType")) {
            String trackType = (String) stats.get("trackType");
            try {
                TrackProficiency.TrackType profType = "Dirt".equalsIgnoreCase(trackType) 
                    ? TrackProficiency.TrackType.DIRT 
                    : TrackProficiency.TrackType.TURF;
                TrackProficiency proficiency = new TrackProficiency(
                    profType,
                    TrackProficiency.DistanceType.MILE,  // Default
                    TrackProficiency.Grade.A
                );
                character.addProficiency(proficiency);
            } catch (Exception e) {
                logger.debug("Failed to set track proficiency: {}", e.getMessage());
            }
        }
        
        return character;
    }
    
    /**
     * Map wiki type names to CharacterType enum
     */
    private static Umamusume.CharacterType mapTypeToCharacterType(String typeStr) {
        if (typeStr == null) {
            return null;
        }
        
        String normalized = typeStr.trim().toLowerCase();
        if (normalized.contains("speed")) {
            return Umamusume.CharacterType.RUNNER;
        } else if (normalized.contains("power")) {
            return Umamusume.CharacterType.POWER;
        } else if (normalized.contains("stamina")) {
            return Umamusume.CharacterType.STAMINA;
        } else if (normalized.contains("wisdom") || normalized.contains("intelligence")) {
            return Umamusume.CharacterType.INTELLIGENCE;
        } else if (normalized.contains("friend") || normalized.contains("guts")) {
            return Umamusume.CharacterType.GUTS;
        }
        return null;
    }
    
    private static String buildWikiUrl(String characterName) {
        // Convert "Tosen Jordan" to "tosen-jordan" for GameTora
        String encodedName = characterName.trim()
            .toLowerCase()
            .replaceAll("\\s+", "-")
            .replaceAll("[^a-z0-9-]", "");
        return WIKI_BASE_URL + "/characters/" + encodedName;
    }
    
    /**
     * Extract all character data from __NEXT_DATA__ JSON (FAST PATH)
     * This is 10-100x faster than HTML parsing and gets complete data
     */
    private static void extractFromJson(Document doc, Map<String, Object> stats) {
        try {
            // Find the script tag containing JSON data
            Element scriptTag = doc.selectFirst("script#__NEXT_DATA__");
            if (scriptTag == null) {
                logger.debug("No __NEXT_DATA__ script found, falling back to HTML parsing");
                return;
            }
            
            // Parse JSON
            String jsonText = scriptTag.html();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonText);
            
            // Navigate to character data: props.pageProps.itemData
            JsonNode itemData = root.path("props").path("pageProps").path("itemData");
            
            if (itemData.isMissingNode()) {
                logger.debug("itemData not found in JSON structure");
                return;
            }
            
            // Extract rarity (1-5 stars)
            if (itemData.has("rarity")) {
                int rarity = itemData.get("rarity").asInt();
                stats.put("rarity", mapStarCountToRarity(rarity));
                logger.debug("Extracted rarity from JSON: {} stars -> {}", rarity, stats.get("rarity"));
            }
            
            // Extract base stats: [speed, stamina, power, guts, intelligence]
            if (itemData.has("base_stats")) {
                JsonNode baseStats = itemData.get("base_stats");
                if (baseStats.isArray() && baseStats.size() >= 5) {
                    stats.put("speed", baseStats.get(0).asInt());
                    stats.put("stamina", baseStats.get(1).asInt());
                    stats.put("power", baseStats.get(2).asInt());
                    stats.put("guts", baseStats.get(3).asInt());
                    stats.put("intelligence", baseStats.get(4).asInt());
                    
                    logger.debug("Extracted stats from JSON: SPD={}, STA={}, POW={}, GUT={}, INT={}", 
                        baseStats.get(0).asInt(), baseStats.get(1).asInt(), 
                        baseStats.get(2).asInt(), baseStats.get(3).asInt(), 
                        baseStats.get(4).asInt());
                }
            }
            
            // Extract aptitudes: ["turf", "dirt", "short", "mile", "medium", "long", "front", "pace", "late", "end"]
            if (itemData.has("aptitude")) {
                JsonNode aptitude = itemData.get("aptitude");
                if (aptitude.isArray() && aptitude.size() >= 10) {
                    // Surface aptitudes (index 0-1)
                    String turfGrade = aptitude.get(0).asText();
                    String dirtGrade = aptitude.get(1).asText();
                    stats.put("surfaceTurf", turfGrade);
                    stats.put("surfaceDirt", dirtGrade);
                    
                    // Determine primary track type (higher grade wins)
                    if (compareGrades(dirtGrade, turfGrade) > 0) {
                        stats.put("trackType", "Dirt");
                    } else {
                        stats.put("trackType", "Turf");
                    }
                    
                    // Distance aptitudes (index 2-5)
                    stats.put("distanceShort", aptitude.get(2).asText());
                    stats.put("distanceMile", aptitude.get(3).asText());
                    stats.put("distanceMedium", aptitude.get(4).asText());
                    stats.put("distanceLong", aptitude.get(5).asText());
                    
                    // Strategy aptitudes (index 6-9)
                    stats.put("strategyFront", aptitude.get(6).asText());
                    stats.put("strategyPace", aptitude.get(7).asText());
                    stats.put("strategyLate", aptitude.get(8).asText());
                    stats.put("strategyEnd", aptitude.get(9).asText());
                    
                    logger.debug("Extracted aptitudes from JSON: Turf={}, Dirt={}, Short={}, Mile={}", 
                        turfGrade, dirtGrade, aptitude.get(2).asText(), aptitude.get(3).asText());
                }
            }
            
            logger.info("Successfully extracted {} fields from JSON (fast path)", stats.size());
            
        } catch (Exception e) {
            logger.debug("Failed to extract JSON data: {}", e.getMessage());
        }
    }
    
    /**
     * Compare aptitude grades (S > A > B > C > D > E > F > G)
     */
    private static int compareGrades(String grade1, String grade2) {
        Map<String, Integer> gradeValues = new HashMap<>();
        gradeValues.put("S", 8);
        gradeValues.put("A", 7);
        gradeValues.put("B", 6);
        gradeValues.put("C", 5);
        gradeValues.put("D", 4);
        gradeValues.put("E", 3);
        gradeValues.put("F", 2);
        gradeValues.put("G", 1);
        
        int value1 = gradeValues.getOrDefault(grade1, 0);
        int value2 = gradeValues.getOrDefault(grade2, 0);
        
        return Integer.compare(value1, value2);
    }
    
    /**
     * Extract stat values from GameTora infobox
     * FIRST tries JSON extraction (fast), then falls back to HTML parsing
     */
    private static void extractStats(Document doc, Map<String, Object> stats) {
        // ============================================
        // STEP 1: TRY JSON EXTRACTION FIRST (FAST)
        // ============================================
        extractFromJson(doc, stats);
        
        // If JSON extraction was successful (got stats + aptitudes), we're done
        if (stats.size() > 10) {
            logger.info("Used JSON fast path - extracted {} fields", stats.size());
            return;
        }
        
        // ============================================
        // STEP 2: FALLBACK TO HTML PARSING
        // ============================================
        logger.debug("JSON extraction incomplete ({} fields), using HTML fallback", stats.size());
        
        try {
            // GameTora stores stats in divs with specific class names
            // Stats section: <div class="characters_infobox_stats__MHrw9">
            Elements statDivs = doc.select("div.characters_infobox_stats__MHrw9");
            
            for (Element statDiv : statDivs) {
                // Each stat has icon + value in a row
                Elements rows = statDiv.select("div.characters_infobox_row__RNXnI");
                
                for (Element row : rows) {
                    String rowText = row.text();
                    
                    // Check for rarity (shown as stars: ⭐⭐⭐)
                    if (rowText.contains("⭐")) {
                        int starCount = rowText.split("⭐", -1).length - 1;
                        String rarity = mapStarCountToRarity(starCount);
                        stats.put("rarity", rarity);
                        logger.debug("Extracted rarity: {} stars -> {}", starCount, rarity);
                        continue;
                    }
                    
                    // Parse stat icon and value
                    Elements iconSpans = row.select("span.utils_stat_icon__J4nu0");
                    if (!iconSpans.isEmpty()) {
                        Element iconSpan = iconSpans.get(0);
                        String imgAlt = iconSpan.select("img").attr("alt");
                        
                        // Extract numeric value after icon
                        Elements divs = row.select("div");
                        for (int i = 1; i < divs.size(); i++) {
                            String text = divs.get(i).text().trim();
                            if (text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                
                                // Map icon alt text to stat name
                                if (imgAlt.contains("Speed") || imgAlt.contains("speed")) {
                                    stats.put("speed", value);
                                    logger.debug("Extracted Speed: {}", value);
                                } else if (imgAlt.contains("Stamina") || imgAlt.contains("stamina")) {
                                    stats.put("stamina", value);
                                    logger.debug("Extracted Stamina: {}", value);
                                } else if (imgAlt.contains("Power") || imgAlt.contains("power")) {
                                    stats.put("power", value);
                                    logger.debug("Extracted Power: {}", value);
                                } else if (imgAlt.contains("Guts") || imgAlt.contains("guts")) {
                                    stats.put("guts", value);
                                    logger.debug("Extracted Guts: {}", value);
                                } else if (imgAlt.contains("Intelligence") || imgAlt.contains("Wit") || 
                                             imgAlt.contains("intelligence") || imgAlt.contains("wit")) {
                                    stats.put("intelligence", value);
                                    logger.debug("Extracted Intelligence: {}", value);
                                }
                                break;
                            }
                        }
                    }
                }
            }
            
            // Extract aptitude data for track types and distances
            extractAptitudes(doc, stats);
            
            logger.debug("Successfully extracted stats and aptitudes from GameTora");
        } catch (Exception e) {
            logger.debug("Failed to extract stats from GameTora: {}", e.getMessage());
        }
    }
    
    /**
     * Extract aptitude grades from GameTora infobox
     * Aptitudes show Surface (Turf/Dirt), Distance (Short/Mile/Medium/Long), Strategy (Front/Pace/Late/End)
     */
    private static void extractAptitudes(Document doc, Map<String, Object> stats) {
        try {
            Elements statDivs = doc.select("div.characters_infobox_stats__MHrw9");
            
            for (Element statDiv : statDivs) {
                String divText = statDiv.text();
                
                // Look for Surface aptitudes (Turf/Dirt)
                if (divText.contains("Surface") || divText.contains("surface")) {
                    Elements rows = statDiv.select("div.characters_infobox_row__RNXnI");
                    for (Element row : rows) {
                        Elements splits = row.select("div.characters_infobox_row_split__AgKVj");
                        for (Element split : splits) {
                            Elements divs = split.select("div");
                            if (divs.size() >= 2) {
                                String trackName = divs.get(0).text().trim();
                                String grade = divs.get(1).text().trim();
                                
                                if (trackName.equalsIgnoreCase("Turf")) {
                                    stats.put("surfaceTurf", grade);
                                    stats.put("trackType", "Turf");
                                    logger.debug("Extracted Turf aptitude: {}", grade);
                                } else if (trackName.equalsIgnoreCase("Dirt")) {
                                    stats.put("surfaceDirt", grade);
                                    stats.put("trackType", "Dirt");
                                    logger.debug("Extracted Dirt aptitude: {}", grade);
                                }
                            }
                        }
                    }
                }
                
                // Look for Distance aptitudes
                if (divText.contains("Distance") || divText.contains("distance")) {
                    Elements rows = statDiv.select("div.characters_infobox_row__RNXnI");
                    for (Element row : rows) {
                        Elements splits = row.select("div.characters_infobox_row_split__AgKVj");
                        for (Element split : splits) {
                            Elements divs = split.select("div");
                            if (divs.size() >= 2) {
                                String distName = divs.get(0).text().trim();
                                String grade = divs.get(1).text().trim();
                                
                                if (distName.contains("Short")) {
                                    stats.put("distanceShort", grade);
                                } else if (distName.contains("Mile")) {
                                    stats.put("distanceMile", grade);
                                } else if (distName.contains("Medium")) {
                                    stats.put("distanceMedium", grade);
                                } else if (distName.contains("Long")) {
                                    stats.put("distanceLong", grade);
                                }
                                logger.debug("Extracted Distance {} aptitude: {}", distName, grade);
                            }
                        }
                    }
                }
                
                // Look for Strategy aptitudes
                if (divText.contains("Strategy") || divText.contains("strategy")) {
                    Elements rows = statDiv.select("div.characters_infobox_row__RNXnI");
                    for (Element row : rows) {
                        Elements splits = row.select("div.characters_infobox_row_split__AgKVj");
                        for (Element split : splits) {
                            Elements divs = split.select("div");
                            if (divs.size() >= 2) {
                                String stratName = divs.get(0).text().trim();
                                String grade = divs.get(1).text().trim();
                                
                                if (stratName.contains("Front")) {
                                    stats.put("strategyFront", grade);
                                } else if (stratName.contains("Pace")) {
                                    stats.put("strategyPace", grade);
                                } else if (stratName.contains("Late")) {
                                    stats.put("strategyLate", grade);
                                } else if (stratName.contains("End")) {
                                    stats.put("strategyEnd", grade);
                                }
                                logger.debug("Extracted Strategy {} aptitude: {}", stratName, grade);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to extract aptitudes: {}", e.getMessage());
        }
    }
    
    /**
     * Map star count to Rarity enum value
     */
    private static String mapStarCountToRarity(int starCount) {
        switch (starCount) {
            case 1: return "N";
            case 2: return "R";
            case 3: return "SR";
            case 4: return "SSR";
            case 5: return "UR";
            default: return "N";
        }
    }
    
    /**
     * Extract skills from GameTora
     * GameTora has well-organized skill sections with headers like "Unique skills", "Innate skills", "Awakening skills", etc.
     */
    private static void extractSkills(Document doc, Map<String, Object> stats) {
        try {
            List<String> skills = new ArrayList<>();
            
            // GameTora uses divs with characters_infobox_caption__UHck_ for skill section headers
            Elements skillHeaders = doc.select("div.characters_infobox_caption__UHck_");
            
            for (Element header : skillHeaders) {
                String headerText = header.text();
                
                // Look for skill-related sections: "Unique skills", "Innate skills", "Awakening skills", "Event skills"
                if (headerText.contains("Skill") || headerText.contains("skill") ||
                    headerText.contains("能力") || headerText.contains("イベント")) {
                    
                    // Get the parent container and find following rows for skill content
                    Element parent = header.parent();
                    if (parent != null) {
                        Elements rows = parent.select("div.characters_infobox_row__RNXnI");
                        
                        // Extract skill items from this section
                        for (Element row : rows) {
                            String skillText = row.text().trim();
                            
                            // Filter valid skill names (not headers, reasonable length, not empty)
                            if (!skillText.isEmpty() && 
                                !skillText.contains("Skill") && 
                                !skillText.contains("skill") &&
                                skillText.length() > 2 && 
                                skillText.length() < 150 &&
                                !skills.contains(skillText)) {
                                skills.add(skillText);
                                logger.debug("Extracted skill: {}", skillText);
                            }
                        }
                    }
                }
            }
            
            if (!skills.isEmpty()) {
                stats.put("skills", skills);
                logger.debug("Extracted {} skills from GameTora", skills.size());
            }
        } catch (Exception e) {
            logger.debug("Failed to extract skills from GameTora: {}", e.getMessage());
        }
    }
    
    /**
     * Extract biography/description from GameTora page
     * GameTora includes character description/profile text in dedicated sections
     */
    private static void extractBiography(Document doc, Map<String, Object> stats) {
        try {
            // Look for character description - GameTora places it near the character name or in profile section
            Elements containers = doc.select("div, article, section");
            
            StringBuilder biography = new StringBuilder();
            
            for (Element container : containers) {
                String containerText = container.text();
                
                // Look for profile/description sections
                if (containerText.length() > 100 && containerText.length() < 1000) {
                    // Check if this container has meaningful character description
                    // (usually after the name section but before game mechanics)
                    if (!containerText.contains("Speed") && 
                        !containerText.contains("Stamina") && 
                        !containerText.contains("Skill") &&
                        !containerText.contains("Strategy")) {
                        
                        // Extract first paragraph-like text
                        Elements paragraphs = container.select("p");
                        if (!paragraphs.isEmpty()) {
                            String text = paragraphs.get(0).text().trim();
                            if (!text.isEmpty() && text.length() > 50) {
                                biography.append(text);
                                break;
                            }
                        }
                    }
                }
            }
            
            if (biography.length() > 0) {
                stats.put("biography", biography.toString());
                logger.debug("Extracted biography for character");
            }
        } catch (Exception e) {
            logger.debug("Failed to extract biography from GameTora: {}", e.getMessage());
        }
    }
    
    /**
     * Extract relationships/appearances from GameTora page
     * GameTora may have dedicated relationship or story sections
     */
    private static void extractRelationships(Document doc, Map<String, Object> stats) {
        try {
            List<String> relationships = new ArrayList<>();
            
            // Look for relationship/story sections using GameTora structure
            // These may appear as caption sections like skills
            Elements captions = doc.select("div.characters_infobox_caption__UHck_");
            
            for (Element caption : captions) {
                String captionText = caption.text();
                
                if (captionText.contains("Relationship") || captionText.contains("Story") || 
                    captionText.contains("relationship") || captionText.contains("story") ||
                    captionText.contains("出身") || captionText.contains("関係")) {
                    
                    // Extract text from following rows
                    Element parent = caption.parent();
                    if (parent != null) {
                        Elements rows = parent.select("div.characters_infobox_row__RNXnI");
                        for (Element row : rows) {
                            String relText = row.text().trim();
                            if (!relText.isEmpty() && 
                                relText.length() > 5 && 
                                relText.length() < 200 &&
                                !relationships.contains(relText)) {
                                relationships.add(relText);
                                logger.debug("Extracted relationship: {}", relText);
                            }
                        }
                    }
                }
            }
            
            if (!relationships.isEmpty()) {
                stats.put("relationships", relationships);
                logger.debug("Extracted {} relationships from GameTora", relationships.size());
            }
        } catch (Exception e) {
            logger.debug("Failed to extract relationships from GameTora: {}", e.getMessage());
        }
    }
    
    /**
     * Enrich UmapyoiCharacter with wiki-scraped Umamusume data
     * Complete data flow: API → Wiki Scraping → Domain Model with All Data
     * @param apiCharacter API response character
     * @return Fully enriched Umamusume domain model with API + Wiki data
     */
    public static Umamusume enrichCharacterData(UmapyoiCharacter apiCharacter) {
        if (apiCharacter == null) {
            return new Umamusume();
        }
        
        try {
            Umamusume wikiEnrichedModel;
            
            // Step 1: Try to scrape using ID first (Better for GameTora specific pages)
            // GameTora uses 6-digit IDs for cards (105201) vs 4-digit base IDs (1052)
            if (apiCharacter.getGameId() > 0) {
                wikiEnrichedModel = scrapeCharacterStats(apiCharacter.getGameId());
            } else {
                // Fallback to name-based scraping if ID is missing
                String characterName = apiCharacter.getNameEnglish();
                if (characterName == null || characterName.isEmpty()) {
                    return new Umamusume();
                }
                wikiEnrichedModel = scrapeCharacterStats(characterName);
            }
            
            // Step 2: Enrich with API data (profile, physical attributes, lore, visuals)
            Umamusume fullyEnrichedModel = apiCharacter.enrichWithApiData(wikiEnrichedModel);
            
            logger.info("Fully enriched {} with GameTora wiki and Umapyoi API data", apiCharacter.getNameEnglish());
            return fullyEnrichedModel;
            
        } catch (Exception e) {
            logger.warn("Failed to fully enrich character data: {}", e.getMessage());
            
            // Fallback: create model from API data only
            Umamusume fallback = new Umamusume();
            fallback.setName(apiCharacter.getNameEnglish());
            fallback.setJapaneseName(apiCharacter.getNameJapanese());
            fallback.setId(apiCharacter.getGameId());
            
            // At least populate API-available fields
            apiCharacter.enrichWithApiData(fallback);
            
            return fallback;
        }
    }
}