// 📄 src/main/java/com/atziluth/sephirah/chesed/api/UmapyoiApiClient.java
package com.atziluth.sephirah.chesed.api;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import com.atziluth.sephirah.chesed.model.Umamusume;
import com.atziluth.sephirah.chesed.model.UmapyoiCharacter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * [CLASS] Main facade for Umapyoi.net API interactions
 * PURPOSE: Single entry point for all API calls, handles HTTP communication
 * DEMONSTRATES: Facade pattern, API integration, error handling, async operations
 */
public class UmapyoiApiClient {
    private static final Logger logger = LoggerFactory.getLogger(UmapyoiApiClient.class);
    
    // 🔒 ENCAPSULATED DEPENDENCIES
    private final ApiConfig config;
    private final LocalCache cache;
    
    // 🏗️ CONSTRUCTOR (dependency injection)
    public UmapyoiApiClient(ApiConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("ApiConfig cannot be null");
        }
        this.config = config;
        this.cache = new LocalCache();
        logger.info("UmapyoiApiClient initialized with {}", config);
    }
    
    /**
     * PUBLIC API: Get all character IDs
     * Endpoint: GET /api/v1/character
     * ⏱ Complexity: O(1) cache hit, O(n) network call
     */
    public List<Integer> getAllCharacterIds() throws IOException {
        final String cacheKey = "character_ids";
        
        // 🔒 Check cache first
        if (cache.has(cacheKey)) {
            logger.debug("Cache hit for {}", cacheKey);
            return cache.get(cacheKey, new TypeReference<List<Integer>>() {});
        }
        
        // 🌐 Make API call
        HttpUrl url = HttpUrl.parse(config.getBaseUrl() + "/api/v1/character");
        Request request = new Request.Builder().url(url).build();
        
        logger.info("Fetching character IDs from {}", url);
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API request failed: " + response.code() + " - " + response.message());
            }
            
            // 📦 Parse response
            String responseBody = response.body().string();
            List<Integer> characterIds = config.getObjectMapper().readValue(
                responseBody, new TypeReference<List<Integer>>() {});
            
            // 💾 Cache the result
            cache.put(cacheKey, characterIds);
            logger.info("Fetched {} character IDs", characterIds.size());
            
            return characterIds;
        }
    }
    
    /**
     * PUBLIC API: Get character by ID
     * Endpoint: GET /api/v1/character/{id}
     * Returns: Raw JSON response as UmapyoiCharacter model
     */
    public UmapyoiCharacter getCharacterById(int characterId) throws IOException {
        final String cacheKey = "character_" + characterId;
        
        if (cache.has(cacheKey)) {
            logger.debug("Cache hit for character {}", characterId);
            return cache.get(cacheKey, UmapyoiCharacter.class);
        }
        
        HttpUrl url = HttpUrl.parse(config.getBaseUrl() + "/api/v1/character/" + characterId);
        Request request = new Request.Builder().url(url).build();
        
        logger.debug("Fetching character {}", characterId);
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new IOException("Character not found with ID: " + characterId);
                }
                throw new IOException("Failed to fetch character: " + response.code());
            }
            
            String responseBody = response.body().string();
            UmapyoiCharacter character = config.getObjectMapper().readValue(
                responseBody, UmapyoiCharacter.class);
            
            cache.put(cacheKey, character);
            logger.info("Fetched character: {}", character.getNameEn());
            
            return character;
        }
    }
    
    /**
     *  PUBLIC API: Get character and convert to your domain model
     * Demonstrates: API-to-domain model transformation
     */
    public Umamusume getCharacterAsUmamusume(int characterId) throws IOException {
        UmapyoiCharacter apiCharacter = getCharacterById(characterId);
        return convertToUmamusume(apiCharacter);
    }
    
    /**
     * PUBLIC API: Get multiple characters
     * Demonstrates: Batch processing
     */
    public List<Umamusume> getMultipleCharacters(List<Integer> characterIds) throws IOException {
        List<Umamusume> characters = new ArrayList<>();
        
        for (int id : characterIds) {
            try {
                Umamusume character = getCharacterAsUmamusume(id);
                characters.add(character);
                logger.debug("Processed character ID: {}", id);
                
                // ⏸️ Rate limiting: sleep to respect API limits
                Thread.sleep(100); // 100ms delay between requests
                
            } catch (IOException e) {
                logger.warn("Failed to fetch character {}: {}", id, e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Operation interrupted", e);
            }
        }
        
        return characters;
    }
    
    /**
     * PUBLIC API: Health check
     *  Endpoint: HEAD /api/v1/character (lightweight check)
     */
    public boolean isApiAvailable() {
        HttpUrl url = HttpUrl.parse(config.getBaseUrl() + "/api/v1/character");
        Request request = new Request.Builder().url(url).head().build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            boolean available = response.isSuccessful();
            logger.debug("API availability check: {}", available ? "✅ Available" : "❌ Unavailable");
            return available;
        } catch (IOException e) {
            logger.warn("API health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * PUBLIC API: Clear cache
     */
    public void clearCache() {
        cache.clear();
        logger.info("Cache cleared");
    }
    
    /**
     * 🔓 PUBLIC API: Get cache statistics
     */
    public String getCacheStats() {
        return String.format("Cache: %d items, %d%% hit rate (estimated)", 
            cache.size(), cache.getHitRate());
    }
    
    /**
     * PRIVATE: Convert API model to domain model
     */
    private Umamusume convertToUmamusume(UmapyoiCharacter apiCharacter) {
        // Create your Umamusume object from API data
        // This is where you map API fields to your domain model
        
        Umamusume character = new Umamusume();
        character.setId(apiCharacter.getGameId());
        character.setName(apiCharacter.getNameEn());
        character.setJapaneseName(apiCharacter.getNameJp());
        
        // You would add more mapping logic here
        // For example, calculate stats from height/weight
        
        return character;
    }
    
    /**
     * UTILITY: Build complete URL for endpoint
     */
    public String buildUrl(String endpoint) {
        return config.getBaseUrl() + endpoint;
    }
}