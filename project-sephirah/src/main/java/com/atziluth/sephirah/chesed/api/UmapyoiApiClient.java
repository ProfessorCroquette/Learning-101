package com.atziluth.sephirah.chesed.api;

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
 * Main facade for Umapyoi.net API interactions
 * Provides a single entry point for all API calls
 * Handles HTTP communication, caching, and error handling
 */
public class UmapyoiApiClient {
    private static final Logger logger = LoggerFactory.getLogger(UmapyoiApiClient.class);
    
    private final ApiConfig config;
    private final LocalCache cache;
    
    /**
     * Constructor with dependency injection
     * @param config The API configuration
     */
    public UmapyoiApiClient(ApiConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("ApiConfig cannot be null");
        }
        this.config = config;
        this.cache = new LocalCache();
        if (logger.isDebugEnabled()) {
            logger.debug("UmapyoiApiClient initialized with {}", config);
        }
    }
    
    /**
     * Get all character IDs from the API
     * @return List of character IDs
     * @throws IOException if API request fails
     */
    public List<Integer> getAllCharacterIds() throws IOException {
        final String cacheKey = "character_ids";
        
        if (cache.has(cacheKey)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cache hit for {}", cacheKey);
            }
            return cache.get(cacheKey, new TypeReference<List<Integer>>() {});
        }
        
        String url = buildUrl("/character/all/ids");
        Request request = new Request.Builder().url(url).get().build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("API request failed with status code: {}", response.code());
                return new ArrayList<>();
            }
            
            String body = response.body() != null ? response.body().string() : "[]";
            List<Integer> ids = config.getObjectMapper().readValue(body, new TypeReference<List<Integer>>() {});
            cache.put(cacheKey, ids);
            return ids;
        }
    }
    
    /**
     * Get a single character by ID
     * @param characterId The character ID to fetch
     * @return The character data or null if not found
     * @throws IOException if API request fails
     */
    public UmapyoiCharacter getCharacterById(int characterId) throws IOException {
        String cacheKey = "character_" + characterId;
        
        if (cache.has(cacheKey)) {
            return cache.get(cacheKey, UmapyoiCharacter.class);
        }
        
        String url = buildUrl("/character/" + characterId);
        Request request = new Request.Builder().url(url).get().build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.warn("Character not found: {}", characterId);
                return null;
            }
            
            String body = response.body() != null ? response.body().string() : "{}";
            UmapyoiCharacter character = config.getObjectMapper().readValue(body, UmapyoiCharacter.class);
            cache.put(cacheKey, character);
            return character;
        }
    }
    
    /**
     * Get a character by ID and convert to domain model
     * @param characterId The character ID to fetch
     * @return The character as Umamusume domain model or null if not found
     * @throws IOException if API request fails
     */
    public Umamusume getCharacterAsUmamusume(int characterId) throws IOException {
        UmapyoiCharacter dto = getCharacterById(characterId);
        return dto != null ? dto.toDomainModel() : null;
    }
    
    /**
     * Get multiple characters by IDs
     * @param characterIds List of character IDs to fetch
     * @return List of Umamusume domain models
     * @throws IOException if API request fails
     */
    public List<Umamusume> getMultipleCharacters(List<Integer> characterIds) throws IOException {
        List<Umamusume> result = new ArrayList<>();
        for (int id : characterIds) {
            Umamusume uma = getCharacterAsUmamusume(id);
            if (uma != null) {
                result.add(uma);
            }
        }
        return result;
    }
    
    /**
     * Check if the API is available
     * @return true if API is responding, false otherwise
     */
    public boolean isApiAvailable() {
        try {
            String url = buildUrl("/character/1001");
            Request request = new Request.Builder().url(url).get().build();
            
            try (Response response = config.getHttpClient().newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("API availability check failed: {}", e.getMessage());
            }
            return false;
        }
    }
    
    /**
     * Build complete URL for endpoint
     * @param endpoint The endpoint path
     * @return Complete URL
     */
    public String buildUrl(String endpoint) {
        return config.getBaseUrl() + endpoint;
    }
}
