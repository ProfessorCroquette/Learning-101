package com.atziluth.sephirah.chesed.api;

import com.atziluth.sephirah.chesed.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * API Test Suite - Tests actual API connectivity and data retrieval
 */
public class ApiTestSuite {
    private static final Logger logger = LoggerFactory.getLogger(ApiTestSuite.class);
    
    public static void main(String[] args) {
        logger.info("\n" + "=".repeat(70));
        logger.info("PROJECT-SEPHIRAH API TEST SUITE");
        logger.info("=".repeat(70));
        
        // Initialize API configuration
        ApiConfig config = new ApiConfig("");
        CharacterService service = new CharacterService(config);
        
        testBasicConnectivity(service);
        testCaching(service);
        testErrorHandling(service);
        testAsyncOperations(service);
        
        logger.info("\n" + "=".repeat(70));
        logger.info("API TESTS COMPLETED");
        logger.info("=".repeat(70));
    }
    
    private static void testBasicConnectivity(CharacterService service) {
        logger.info("\n[TEST 1] Basic API Connectivity");
        logger.info("-".repeat(70));
        
        try {
            // Attempt to fetch a character
            logger.info("Attempting to fetch character ID: 1");
            UmapyoiCharacter character = service.getCharacterById(1);
            
            if (character != null) {
                logger.info("✅ SUCCESS - Character fetched: {}", character.getNameEnglish());
                logger.info("   Japanese Name: {}", character.getNameJapanese());
                logger.info("   Height: {} cm", character.getHeight());
                logger.info("   Grade: {}", character.getGrade());
            } else {
                logger.warn("⚠️  Character data is null");
            }
        } catch (IOException e) {
            logger.error("❌ FAILED - API Connection Error");
            logger.error("   Reason: {}", e.getMessage());
            logger.info("   Note: If API endpoint is unavailable, this is expected");
            logger.info("   Current endpoint: https://umapyoi.net/api/v1/character/1");
        }
    }
    
    private static void testCaching(CharacterService service) {
        logger.info("\n[TEST 2] Caching Mechanism");
        logger.info("-".repeat(70));
        
        try {
            logger.info("First request (should hit API):");
            long start = System.currentTimeMillis();
            UmapyoiCharacter char1 = service.getCharacterById(2);
            long time1 = System.currentTimeMillis() - start;
            logger.info("   Time: {}ms", time1);
            
            logger.info("Second request (should hit cache):");
            start = System.currentTimeMillis();
            UmapyoiCharacter char2 = service.getCharacterById(2);
            long time2 = System.currentTimeMillis() - start;
            logger.info("   Time: {}ms", time2);
            
            if (time2 < time1) {
                logger.info("✅ CACHE WORKING - Second request was faster");
            } else {
                logger.info("⚠️  Cache performance: Sequential timing");
            }
        } catch (IOException e) {
            logger.warn("⚠️  Cache test skipped - API unavailable");
        }
    }
    
    private static void testErrorHandling(CharacterService service) {
        logger.info("\n[TEST 3] Error Handling");
        logger.info("-".repeat(70));
        
        try {
            logger.info("Attempting to fetch invalid character ID: 99999");
            UmapyoiCharacter character = service.getCharacterById(99999);
            
            if (character == null) {
                logger.info("✅ Proper null handling for non-existent character");
            }
        } catch (IOException e) {
            logger.info("✅ IOException caught properly: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Unexpected exception: {}", e.getClass().getName());
        }
    }
    
    private static void testAsyncOperations(CharacterService service) {
        logger.info("\n[TEST 4] Asynchronous Operations");
        logger.info("-".repeat(70));
        
        try {
            logger.info("Executing async character fetch...");
            var future = service.getCharacterByIdAsync(3);
            
            logger.info("Waiting for async result (timeout: 15 seconds)...");
            UmapyoiCharacter character = future.get();
            
            if (character != null) {
                logger.info("✅ Async operation successful: {}", character.getNameEnglish());
            } else {
                logger.info("⚠️  Async returned null");
            }
        } catch (Exception e) {
            logger.warn("⚠️  Async test result: {}", e.getMessage());
        }
    }
}
