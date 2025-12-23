package com.atziluth.sephirah.chesed.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for JSON operations.
 */
public class JSONHandler {
    private static final Logger logger = LoggerFactory.getLogger(JSONHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static <T> T parseJson(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            logger.error("Failed to parse JSON: {}", e.getMessage());
            return null;
        }
    }
    
    public static <T> T parseJsonTypeRef(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            logger.error("Failed to parse JSON: {}", e.getMessage());
            return null;
        }
    }
    
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("Failed to convert to JSON: {}", e.getMessage());
            return null;
        }
    }
    
    public static String toPrettyJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("Failed to convert to JSON: {}", e.getMessage());
            return null;
        }
    }
    
    public static <T> T loadFromFile(String filePath, Class<T> valueType) {
        try {
            return mapper.readValue(new File(filePath), valueType);
        } catch (IOException e) {
            logger.error("Failed to load JSON from {}: {}", filePath, e.getMessage());
            return null;
        }
    }
    
    public static boolean saveToFile(Object obj, String filePath) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), obj);
            return true;
        } catch (IOException e) {
            logger.error("Failed to save JSON to {}: {}", filePath, e.getMessage());
            return false;
        }
    }
}
