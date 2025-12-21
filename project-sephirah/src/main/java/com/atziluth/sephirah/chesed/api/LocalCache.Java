// 📄 src/main/java/com/atziluth/sephirah/chesed/api/LocalCache.java
package com.atziluth.sephirah.chesed.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 🟢 [CLASS] In-memory cache for API responses with TTL
 * 🎯 PURPOSE: Reduce API calls, improve performance, handle rate limits
 * 📚 DEMONSTRATES: Caching strategy, concurrency, TTL patterns
 * 🔒 ENCAPSULATION: Private inner CacheEntry class
 */
public class LocalCache {
    private static final Logger logger = LoggerFactory.getLogger(LocalCache.class);
    
    // 🔒 PRIVATE INNER CLASS (encapsulation example)
    private static class CacheEntry {
        final Object data;
        final long expiryTime;
        final String type;
        
        CacheEntry(Object data, long ttlMillis, String type) {
            this.data = data;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
            this.type = type;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
        
        long getTimeUntilExpiry() {
            return Math.max(0, expiryTime - System.currentTimeMillis());
        }
    }
    
    // 🔒 PRIVATE FIELDS
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private long defaultTTL = TimeUnit.MINUTES.toMillis(30);
    
    // 📊 Cache statistics (encapsulated)
    private int hitCount = 0;
    private int missCount = 0;
    private int evictionCount = 0;
    
    // 🏗️ CONSTRUCTORS
    public LocalCache() {
        this.objectMapper = new ObjectMapper();
    }
    
    public LocalCache(long defaultTTLMillis) {
        this();
        this.defaultTTL = defaultTTLMillis;
    }
    
    /**
     * 🔓 PUT: Store data in cache with default TTL
     */
    public <T> void put(String key, T data) {
        put(key, data, defaultTTL);
    }
    
    /**
     * 🔓 PUT: Store data with custom TTL
     */
    public <T> void put(String key, T data, long ttlMillis) {
        if (key == null || data == null) {
            throw new IllegalArgumentException("Key and data cannot be null");
        }
        
        String type = data.getClass().getName();
        CacheEntry entry = new CacheEntry(data, ttlMillis, type);
        cache.put(key, entry);
        
        logger.debug("Cached: {} (TTL: {}ms, Type: {})", 
            key, ttlMillis, getSimpleTypeName(type));
    }
    
    /**
     * 🔓 GET: Retrieve cached data by key
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        CacheEntry entry = cache.get(key);
        
        if (entry == null) {
            missCount++;
            return null;
        }
        
        if (entry.isExpired()) {
            cache.remove(key);
            evictionCount++;
            missCount++;
            logger.debug("Cache expired: {}", key);
            return null;
        }
        
        hitCount++;
        try {
            // Direct cast if types match
            if (clazz.isInstance(entry.data)) {
                return (T) entry.data;
            }
            
            // Serialize/deserialize for type conversion
            String json = objectMapper.writeValueAsString(entry.data);
            return objectMapper.readValue(json, clazz);
            
        } catch (Exception e) {
            logger.error("Cache deserialization failed for {}: {}", key, e.getMessage());
            remove(key);
            return null;
        }
    }
    
    /**
     * 🔓 GET: Retrieve with TypeReference (for Lists, Maps)
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        CacheEntry entry = cache.get(key);
        
        if (entry == null || entry.isExpired()) {
            missCount++;
            if (entry != null && entry.isExpired()) {
                cache.remove(key);
                evictionCount++;
            }
            return null;
        }
        
        hitCount++;
        try {
            String json = objectMapper.writeValueAsString(entry.data);
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            logger.error("Cache deserialization failed", e);
            remove(key);
            return null;
        }
    }
    
    /**
     * 🔓 CHECK: If key exists and is not expired
     */
    public boolean has(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        
        if (entry.isExpired()) {
            cache.remove(key);
            evictionCount++;
            return false;
        }
        
        return true;
    }
    
    /**
     * 🔓 REMOVE: Remove specific key
     */
    public void remove(String key) {
        cache.remove(key);
        logger.debug("Removed from cache: {}", key);
    }
    
    /**
     * 🔓 CLEAR: Clear entire cache
     */
    public void clear() {
        int sizeBefore = cache.size();
        cache.clear();
        logger.info("Cache cleared (removed {} items)", sizeBefore);
    }
    
    /**
     * 🔓 CLEANUP: Remove all expired entries
     */
    public void cleanupExpired() {
        int before = cache.size();
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        int after = cache.size();
        
        if (before - after > 0) {
            evictionCount += (before - after);
            logger.debug("Cleaned up {} expired cache entries", before - after);
        }
    }
    
    /**
     * 🔓 STATISTICS: Get cache hit rate
     */
    public double getHitRate() {
        int total = hitCount + missCount;
        return total > 0 ? (double) hitCount / total * 100 : 0;
    }
    
    /**
     * 🔓 STATISTICS: Get cache size
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * 🔓 STATISTICS: Get all statistics
     */
    public Map<String, Object> getStats() {
        cleanupExpired();
        
        return Map.of(
            "size", size(),
            "hits", hitCount,
            "misses", missCount,
            "evictions", evictionCount,
            "hitRate", String.format("%.1f%%", getHitRate()),
            "defaultTTL", defaultTTL + "ms"
        );
    }
    
    /**
     * 🔓 INFO: Get cache info for debugging
     */
    public void printCacheInfo() {
        Map<String, Object> stats = getStats();
        logger.info("=== CACHE STATISTICS ===");
        stats.forEach((key, value) -> logger.info("  {}: {}", key, value));
        
        if (!cache.isEmpty()) {
            logger.info("=== CACHE ENTRIES ===");
            cache.forEach((key, entry) -> {
                long ttl = entry.getTimeUntilExpiry();
                logger.info("  {} -> {} (expires in {}ms)", 
                    key, getSimpleTypeName(entry.type), ttl);
            });
        }
    }
    
    /**
     * 🔒 PRIVATE: Get simple class name
     */
    private String getSimpleTypeName(String fullName) {
        int lastDot = fullName.lastIndexOf('.');
        return lastDot != -1 ? fullName.substring(lastDot + 1) : fullName;
    }
}