package com.atziluth.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Registry for managing Sephirah modules and subjects.
 */
public class SubjectRegistry {
    private static final Logger logger = LoggerFactory.getLogger(SubjectRegistry.class);
    private final Map<String, Sephirah> registry = new LinkedHashMap<>();
    
    public SubjectRegistry() {
    }
    
    public void register(String id, Sephirah sephirah) {
        registry.put(id, sephirah);
        logger.info("Registered subject: {} - {}", id, sephirah.getName());
    }
    
    public Sephirah getSubject(String id) {
        return registry.getOrDefault(id, null);
    }
    
    public String getDescription(String id) {
        Sephirah subject = registry.get(id);
        return subject != null ? subject.getDescription() : "Unknown subject";
    }
    
    public Map<String, Sephirah> getAllSubjects() {
        return new LinkedHashMap<>(registry);
    }
    
    public void listAll() {
        logger.info("=== REGISTERED SUBJECTS ===");
        registry.forEach((id, subject) -> 
            logger.info("  {} -> {}", id, subject.getName())
        );
    }
}
