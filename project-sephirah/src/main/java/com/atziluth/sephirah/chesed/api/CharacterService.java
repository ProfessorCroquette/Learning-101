package com.atziluth.sephirah.chesed.api;

import com.atziluth.sephirah.chesed.model.UmapyoiCharacter;
import com.atziluth.sephirah.chesed.model.CharacterImages;
import com.atziluth.sephirah.chesed.model.CharacterBirthday;
import com.atziluth.sephirah.chesed.model.Umamusume;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Service class for handling all character-related API operations.
 * Demonstrates service pattern, async programming, and API integration.
 */
public class CharacterService {
    private static final Logger logger = LoggerFactory.getLogger(CharacterService.class);
    
    // 🔒 ENCAPSULATED DEPENDENCIES
    private final ApiConfig config;
    private final LocalCache cache;
    private final ExecutorService executor;
    
    // Rate limiting configuration
    private static final int MAX_REQUESTS_PER_SECOND = 10;
    private static final int REQUEST_DELAY_MS = 1000 / MAX_REQUESTS_PER_SECOND;
    
    // 🏗️ CONSTRUCTOR
    public CharacterService(ApiConfig config) {
        this.config = Objects.requireNonNull(config, "ApiConfig cannot be null");
        this.cache = new LocalCache();
        this.executor = Executors.newFixedThreadPool(5);
        logger.info("CharacterService initialized");
    }
    
    /**
     * 🔓 Get detailed character information by ID
     * Endpoint: GET /api/v1/character/{id}
     */
    public UmapyoiCharacter getCharacterById(int characterId) throws IOException {
        String cacheKey = "character_" + characterId;
        
        // Check cache first
        if (cache.has(cacheKey)) {
            logger.debug("Cache hit for character ID: {}", characterId);
            return cache.get(cacheKey, UmapyoiCharacter.class);
        }
        
        // Build URL
        String url = config.getBaseUrl() + "/api/v1/character/" + characterId;
        Request request = new Request.Builder()
            .url(url)
            .build();
        
        logger.info("Fetching character ID: {}", characterId);
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch character " + characterId + 
                                    ": " + response.code() + " " + response.message());
            }
            
            // Parse JSON response
            String json = response.body().string();
            UmapyoiCharacter character = config.getObjectMapper()
                .readValue(json, UmapyoiCharacter.class);
            
            // Cache the result
            cache.put(cacheKey, character, TimeUnit.HOURS.toMillis(24));
            logger.debug("Successfully fetched and cached character: {}", character.getNameEnglish());
            
            return character;
        }
    }
    
    /**
     * 🔓 Get character and convert to Umamusume domain model
     */
    public Umamusume getCharacterAsUmamusume(int characterId) throws IOException {
        UmapyoiCharacter apiCharacter = getCharacterById(characterId);
        return apiCharacter.toDomainModel();
    }
    
    /**
     * 🔓 Get multiple characters by IDs
     */
    public List<UmapyoiCharacter> getCharacters(List<Integer> characterIds) throws IOException {
        List<UmapyoiCharacter> characters = new ArrayList<>();
        
        for (Integer id : characterIds) {
            try {
                UmapyoiCharacter character = getCharacterById(id);
                characters.add(character);
                logger.debug("Fetched character ID: {}", id);
                
                // Rate limiting
                Thread.sleep(REQUEST_DELAY_MS);
                
            } catch (IOException e) {
                logger.warn("Failed to fetch character {}: {}", id, e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Fetch interrupted", e);
            }
        }
        
        logger.info("Fetched {} out of {} requested characters", 
                   characters.size(), characterIds.size());
        return characters;
    }
    
    /**
     * 🔓 Search characters by name (fuzzy search)
     * Searches IDs 1001-1400 for matching names
     */
    public List<UmapyoiCharacter> searchCharactersByName(String name) throws IOException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }
        
        String searchTerm = name.trim().toLowerCase();
        logger.info("Searching for characters with name containing: '{}'", searchTerm);
        
        List<UmapyoiCharacter> results = new ArrayList<>();
        
        // Search through a range of character IDs (1001-1400)
        for (int characterId = 1001; characterId <= 1400; characterId++) {
            try {
                UmapyoiCharacter character = getCharacterById(characterId);
                
                // Check if character matches search term
                if (character.matchesSearch(searchTerm)) {
                    results.add(character);
                    logger.debug("Found match: {} (ID: {})", 
                               character.getNameEnglish(), characterId);
                }
                
                // Rate limiting
                Thread.sleep(REQUEST_DELAY_MS);
                
            } catch (IOException e) {
                // Character might not exist, skip
                logger.trace("Character ID {} not found or error: {}", characterId, e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Search interrupted", e);
            }
            
            // Progress logging
            if (characterId % 50 == 0) {
                logger.info("Search progress: {}/400 characters checked", characterId - 1000);
            }
        }
        
        logger.info("Search completed. Found {} matches for '{}'", 
                   results.size(), searchTerm);
        return results;
    }
    
    /**
     * 🔓 Async version of getCharacterById
     */
    public CompletableFuture<UmapyoiCharacter> getCharacterByIdAsync(int characterId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getCharacterById(characterId);
            } catch (IOException e) {
                logger.error("Async fetch failed for character {}: {}", characterId, e.getMessage());
                throw new CompletionException(e);
            }
        }, executor);
    }
    
    /**
     * 🔓 Batch fetch multiple characters asynchronously
     */
    public CompletableFuture<List<UmapyoiCharacter>> getCharactersAsync(List<Integer> characterIds) {
        List<CompletableFuture<UmapyoiCharacter>> futures = characterIds.stream()
            .map(this::getCharacterByIdAsync)
            .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }
    
    /**
     * 🔓 Get character images
     * Endpoint: GET /api/v1/character/images/{id}
     */
    public CharacterImages getCharacterImages(int characterId) throws IOException {
        String cacheKey = "character_images_" + characterId;
        
        if (cache.has(cacheKey)) {
            return cache.get(cacheKey, CharacterImages.class);
        }
        
        String url = config.getBaseUrl() + "/api/v1/character/images/" + characterId;
        Request request = new Request.Builder()
            .url(url)
            .build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch images for character " + characterId);
            }
            
            String json = response.body().string();
            CharacterImages images = config.getObjectMapper()
                .readValue(json, CharacterImages.class);
            
            cache.put(cacheKey, images, TimeUnit.HOURS.toMillis(168)); // 7 days cache
            return images;
        }
    }
    
    /**
     * 🔓 Get current birthdays
     * Endpoint: GET /api/v1/character/currentbirthdays
     */
    public List<CharacterBirthday> getCurrentBirthdays() throws IOException {
        String cacheKey = "current_birthdays";
        
        if (cache.has(cacheKey)) {
            return cache.get(cacheKey, new TypeReference<List<CharacterBirthday>>() {});
        }
        
        String url = config.getBaseUrl() + "/api/v1/character/currentbirthdays";
        Request request = new Request.Builder()
            .url(url)
            .build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch current birthdays");
            }
            
            String json = response.body().string();
            List<CharacterBirthday> birthdays = config.getObjectMapper()
                .readValue(json, new TypeReference<List<CharacterBirthday>>() {});
            
            // Cache for 1 hour (birthdays change daily)
            cache.put(cacheKey, birthdays, TimeUnit.HOURS.toMillis(1));
            logger.info("Fetched {} characters with birthdays today", birthdays.size());
            
            return birthdays;
        }
    }
    
    /**
     * 🔓 Get character list
     * Endpoint: GET /api/v1/character/list
     */
    public List<Map<String, Object>> getCharacterList() throws IOException {
        String cacheKey = "character_list";
        
        if (cache.has(cacheKey)) {
            return cache.get(cacheKey, new TypeReference<List<Map<String, Object>>>() {});
        }
        
        String url = config.getBaseUrl() + "/api/v1/character/list";
        Request request = new Request.Builder()
            .url(url)
            .build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch character list");
            }
            
            String json = response.body().string();
            List<Map<String, Object>> characterList = config.getObjectMapper()
                .readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            
            cache.put(cacheKey, characterList);
            logger.info("Fetched character list with {} entries", characterList.size());
            
            return characterList;
        }
    }
    
    /**
     * 🔓 Get popular characters (predefined IDs)
     */
    public List<UmapyoiCharacter> getPopularCharacters() throws IOException {
        List<Integer> popularIds = Arrays.asList(
            1001, // Special Week
            1002, // Silence Suzuka
            1003, // Tokai Teio
            1004, // Oguri Cap
            1005, // Gold Ship
            1006, // Vodka
            1007  // Daiwa Scarlet
        );
        
        return getCharacters(popularIds);
    }
    
    /**
     * 🔓 Get characters by type (using estimated type from strengths)
     */
    public List<UmapyoiCharacter> getCharactersByType(String type) throws IOException {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        
        String typeLower = type.trim().toLowerCase();
        logger.info("Searching for characters of type: {}", type);
        
        List<UmapyoiCharacter> results = new ArrayList<>();
        List<UmapyoiCharacter> popularChars = getPopularCharacters();
        
        for (UmapyoiCharacter character : popularChars) {
            // Simple type estimation based on strengths
            String strengths = character.getStrengths();
            if (strengths != null) {
                String strengthsLower = strengths.toLowerCase();
                
                boolean matches = false;
                switch (typeLower) {
                    case "speed":
                        matches = strengthsLower.contains("speed") || 
                                 strengthsLower.contains("fast") ||
                                 strengthsLower.contains("sprint");
                        break;
                    case "stamina":
                        matches = strengthsLower.contains("stamina") || 
                                 strengthsLower.contains("endurance");
                        break;
                    case "power":
                        matches = strengthsLower.contains("power") || 
                                 strengthsLower.contains("strength");
                        break;
                    case "intelligence":
                        matches = strengthsLower.contains("intelligent") || 
                                 strengthsLower.contains("smart");
                        break;
                    case "guts":
                        matches = strengthsLower.contains("guts") || 
                                 strengthsLower.contains("brave");
                        break;
                    default:
                        matches = strengthsLower.contains(typeLower);
                }
                
                if (matches) {
                    results.add(character);
                }
            }
        }
        
        logger.info("Found {} characters of type {}", results.size(), type);
        return results;
    }
    
    /**
     * 🔓 Get characters by height range
     */
    public List<UmapyoiCharacter> getCharactersByHeight(int minHeight, int maxHeight) throws IOException {
        logger.info("Searching for characters with height between {} and {} cm", 
                   minHeight, maxHeight);
        
        List<UmapyoiCharacter> results = new ArrayList<>();
        List<UmapyoiCharacter> popularChars = getPopularCharacters();
        
        for (UmapyoiCharacter character : popularChars) {
            if (character.getHeight() >= minHeight && character.getHeight() <= maxHeight) {
                results.add(character);
            }
        }
        
        logger.info("Found {} characters in height range", results.size());
        return results;
    }
    
    /**
     * 🔓 Get service statistics
     */
    public Map<String, Object> getServiceStats() {
        return Map.of(
            "cacheSize", cache.size(),
            "cacheHitRate", String.format("%.1f%%", cache.getHitRate()),
            "executorActive", !executor.isShutdown(),
            "serviceName", "CharacterService",
            "rateLimit", MAX_REQUESTS_PER_SECOND + " requests/second"
        );
    }
    
    /**
     * 🔓 Get cache information
     */
    public void printCacheInfo() {
        logger.info("=== CharacterService Cache Info ===");
        logger.info("Total cached items: {}", cache.size());
        logger.info("Cache hit rate: {}%", String.format("%.1f", cache.getHitRate()));
    }
    
    /**
     * 🔓 Clear service cache
     */
    public void clearCache() {
        cache.clear();
        logger.info("CharacterService cache cleared");
    }
    
    /**
     * 🔓 Clean shutdown
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("CharacterService shutdown complete");
    }
    
    /**
     * 🔓 Test API connectivity
     */
    public boolean testConnection() {
        try {
            String url = config.getBaseUrl() + "/api/v1/character/1001";
            Request request = new Request.Builder()
                .url(url)
                .head()
                .build();
            
            try (Response response = config.getHttpClient().newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.warn("Connection test failed: {}", e.getMessage());
            return false;
        }
    }
}