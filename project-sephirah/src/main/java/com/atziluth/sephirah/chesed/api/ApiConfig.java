// 📄 src/main/java/com/atziluth/sephirah/chesed/api/ApiConfig.java
package com.atziluth.sephirah.chesed.api;

import okhttp3.OkHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;

/**
 * [CLASS] Configuration manager for API connections
 * PURPOSE: Centralizes all API settings, HTTP client configuration, and JSON mapping
 * DEMONSTRATES: Singleton pattern, configuration management, dependency setup
 */
public class ApiConfig {
    
   
    private static final String BASE_URL = "https://umapyoi.net";
    private static final int CONNECT_TIMEOUT = 30; // seconds
    private static final int READ_TIMEOUT = 30;    // seconds
    private static final int WRITE_TIMEOUT = 30;   // seconds
    
   
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey; // For future authenticated endpoints
    
   
    ApiConfig(String apiKey) {
        this.apiKey = apiKey;
        
        // Configure HTTP client with timeouts
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                // Add headers to every request
                var request = chain.request().newBuilder()
                    .header("User-Agent", "Project-Sephirah/1.0")
                    .header("Accept", "application/json")
                    .build();
                return chain.proceed(request);
            })
            .build();
        
        // Configure JSON mapper
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules(); // For Java 8 time support
    }
    
    // 🔓 FACTORY METHOD (public access point)
    public static ApiConfig createDefault() {
        return new ApiConfig(null); // No API key needed for public endpoints
    }
    
    public static ApiConfig createWithKey(String apiKey) {
        return new ApiConfig(apiKey);
    }
    
    // 🔓 GETTERS (controlled access to encapsulated resources)
    public OkHttpClient getHttpClient() {
        return httpClient;
    }
    
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    public String getBaseUrl() {
        return BASE_URL;
    }
    
    public boolean hasApiKey() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }
    
        // 🔓 VALIDATION METHOD
    public void validate() {
        if (BASE_URL.trim().isEmpty()) {
            throw new IllegalStateException("Base URL is not configured");
        }
    }
    
    @Override
    public String toString() {
        return String.format("ApiConfig{baseUrl='%s', hasKey=%s}", 
            BASE_URL, hasApiKey());
    }
}