package com.atziluth.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * Plugin loader for dynamic module loading.
 * Demonstrates reflection and dynamic class loading.
 */
public class PluginLoader {
    private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);
    
    public static Sephirah loadPlugin(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (Sephirah.class.isAssignableFrom(clazz)) {
                logger.info("Successfully loaded plugin: {}", className);
                return (Sephirah) clazz.getDeclaredConstructor().newInstance();
            } else {
                logger.warn("Class {} does not implement Sephirah", className);
                return null;
            }
        } catch (Exception e) {
            logger.error("Failed to load plugin {}: {}", className, e.getMessage());
            return null;
        }
    }
    
    public static void loadFromDirectory(String directory) {
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            logger.warn("Directory not found: {}", directory);
            return;
        }
        
        logger.info("Scanning directory for plugins: {}", directory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));
        
        if (files != null) {
            logger.info("Found {} JAR files", files.length);
            for (File file : files) {
                logger.info("  - {}", file.getName());
            }
        }
    }
}
