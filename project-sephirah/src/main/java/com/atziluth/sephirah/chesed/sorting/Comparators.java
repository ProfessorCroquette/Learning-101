package com.atziluth.sephirah.chesed.sorting;

import com.atziluth.sephirah.chesed.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * [CLASS] Centralized comparator implementations for sorting operations
 * PURPOSE: Provides reusable Comparator<T> instances for different sorting scenarios
 * DEMONSTRATES: Strategy pattern, functional programming, composition
 * USAGE: Sort characters by various criteria (stats, rarity, name, etc.)
 */
public class Comparators {
    private static final Logger logger = LoggerFactory.getLogger(Comparators.class);
    
    // ============================================================================
    // UMAMUSUME COMPARATORS
    // ============================================================================
    
    /**
     * Sort by total stats (descending - higher stats first)
     */
    public static Comparator<Umamusume> byTotalStats() {
        return Comparator.comparingInt(Umamusume::getTotalStats).reversed();
    }
    
    /**
     * Sort by total stats (ascending - lower stats first)
     */
    public static Comparator<Umamusume> byTotalStatsAscending() {
        return Comparator.comparingInt(Umamusume::getTotalStats);
    }
    
    /**
     * Sort by speed stat only (descending)
     */
    public static Comparator<Umamusume> bySpeed() {
        return Comparator.comparingInt((Umamusume u) -> u.getStats().getSpeed()).reversed();
    }
    
    /**
     * Sort by stamina stat only (descending)
     */
    public static Comparator<Umamusume> byStamina() {
        return Comparator.comparingInt((Umamusume u) -> u.getStats().getStamina()).reversed();
    }
    
    /**
     * Sort by power stat only (descending)
     */
    public static Comparator<Umamusume> byPower() {
        return Comparator.comparingInt((Umamusume u) -> u.getStats().getPower()).reversed();
    }
    
    /**
     * Sort by guts stat only (descending)
     */
    public static Comparator<Umamusume> byGuts() {
        return Comparator.comparingInt((Umamusume u) -> u.getStats().getGuts()).reversed();
    }
    
    /**
     * Sort by intelligence stat only (descending)
     */
    public static Comparator<Umamusume> byIntelligence() {
        return Comparator.comparingInt((Umamusume u) -> u.getStats().getIntelligence()).reversed();
    }
    
    /**
     * Sort by character name (alphabetically, A-Z)
     */
    public static Comparator<Umamusume> byName() {
        return Comparator.comparing(Umamusume::getName);
    }
    
    /**
     * Sort by Japanese name (alphabetically)
     */
    public static Comparator<Umamusume> byJapaneseName() {
        return Comparator.comparing(Umamusume::getJapaneseName);
    }
    
    /**
     * Sort by rarity (descending - UR first, then SSR, SR, R, N)
     */
    public static Comparator<Umamusume> byRarity() {
        return (u1, u2) -> u2.getRarity().compareTo(u1.getRarity());
    }
    
    /**
     * Sort by character type (SPEED, STAMINA, POWER, GUTS, INTELLIGENCE)
     */
    public static Comparator<Umamusume> byType() {
        return Comparator.comparing(Umamusume::getType);
    }
    
    /**
     * Chain multiple comparators - sort by rarity first, then by total stats
     */
    public static Comparator<Umamusume> byRarityThenStats() {
        return byRarity().thenComparing(byTotalStats());
    }
    
    /**
     * Chain multiple comparators - sort by type, then by name
     */
    public static Comparator<Umamusume> byTypeThenName() {
        return byType().thenComparing(byName());
    }
    
    /**
     * Chain multiple comparators - sort by rarity, then type, then stats
     */
    public static Comparator<Umamusume> byRarityTypeThenStats() {
        return byRarity()
                .thenComparing(byType())
                .thenComparing(byTotalStats());
    }
    
    // ============================================================================
    // ABSTRACT UMA COMPARATORS
    // ============================================================================
    
    /**
     * Sort AbstractUma by name
     */
    public static Comparator<AbstractUma> umaByName() {
        return Comparator.comparing(AbstractUma::getName);
    }
    
    /**
     * Sort AbstractUma by total stats (descending)
     */
    public static Comparator<AbstractUma> umaByTotalStats() {
        return Comparator.comparingInt(AbstractUma::getTotalStats).reversed();
    }
    
    /**
     * Sort AbstractUma by special power (descending)
     */
    public static Comparator<AbstractUma> umaBySpecialPower() {
        return Comparator.comparingInt(AbstractUma::calculateSpecialPower).reversed();
    }
    
    /**
     * Sort AbstractUma by character type
     */
    public static Comparator<AbstractUma> umaByType() {
        return Comparator.comparing(AbstractUma::getCharacterType);
    }
    
    // ============================================================================
    // UMAPYOI CHARACTER COMPARATORS
    // ============================================================================
    
    /**
     * Sort UmapyoiCharacter by English name
     */
    public static Comparator<UmapyoiCharacter> characterByName() {
        return Comparator.comparing(UmapyoiCharacter::getNameEnglish);
    }
    
    /**
     * Sort UmapyoiCharacter by height (descending - taller first)
     */
    public static Comparator<UmapyoiCharacter> characterByHeight() {
        return Comparator.comparingInt(UmapyoiCharacter::getHeight).reversed();
    }
    
    /**
     * Sort UmapyoiCharacter by bust size (descending)
     */
    public static Comparator<UmapyoiCharacter> characterByBust() {
        return Comparator.comparingInt(UmapyoiCharacter::getBustSize).reversed();
    }
    
    /**
     * Sort UmapyoiCharacter by row number (game order)
     */
    public static Comparator<UmapyoiCharacter> characterByGameOrder() {
        return Comparator.comparingInt(UmapyoiCharacter::getRowNumber);
    }
    
    // ============================================================================
    // UTILITY METHODS
    // ============================================================================
    
    /**
     * Create a reverse comparator from any comparator
     */
    public static <T> Comparator<T> reverse(Comparator<T> comparator) {
        return comparator.reversed();
    }
    
    /**
     * Chain multiple comparators together
     */
    @SafeVarargs
    public static <T> Comparator<T> chain(Comparator<T>... comparators) {
        if (comparators.length == 0) {
            throw new IllegalArgumentException("At least one comparator required");
        }
        
        Comparator<T> result = comparators[0];
        for (int i = 1; i < comparators.length; i++) {
            result = result.thenComparing(comparators[i]);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Created chained comparator with {} comparators", comparators.length);
        }
        
        return result;
    }
    
    /**
     * Null-safe comparator wrapper
     */
    public static <T> Comparator<T> nullsFirst(Comparator<T> comparator) {
        return Comparator.nullsFirst(comparator);
    }
    
    /**
     * Null-safe comparator wrapper (nulls last)
     */
    public static <T> Comparator<T> nullsLast(Comparator<T> comparator) {
        return Comparator.nullsLast(comparator);
    }
}
