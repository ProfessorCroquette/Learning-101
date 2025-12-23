package com.atziluth.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Registry for managing Sephirah modules and subjects.
 */
public class SubjectRegistry {
    private static final Logger logger = LoggerFactory.getLogger(SubjectRegistry.class);
    private final Map<String, String> registry = new LinkedHashMap<>();
    
    public SubjectRegistry() {
        initializeRegistry();
    }
    
    private void initializeRegistry() {
        registry.put("chesed", "OOP & Algorithms - Umamusume Database");
        registry.put("gevurah", "Algorithm Complexity - RPG Market Simulation");
        registry.put("tiferet", "Discrete Mathematics - Boolean Logic");
        registry.put("binah", "System Architecture - Library Management");
    }
    
    public void register(String id, String description) {
        registry.put(id, description);
        logger.info("Registered subject: {} - {}", id, description);
    }
    
    public String getDescription(String id) {
        return registry.getOrDefault(id, "Unknown subject");
    }
    
    public Map<String, String> getAllSubjects() {
        return new LinkedHashMap<>(registry);
    }
    
    public void listAll() {
        logger.info("=== REGISTERED SUBJECTS ===");
        registry.forEach((id, desc) -> 
            logger.info("  {} -> {}", id, desc)
        );
    }
}
