package com.atziluth.sephirah.chesed.sorting;

import com.atziluth.sephirah.chesed.model.AbstractUma;
import java.util.*;

/**
 * Enhanced sorter for AbstractUma types.
 * Extends sorting capabilities with polymorphic types.
 */
public class UmapyoiEnhancedSorter {
    
    public static List<AbstractUma> sortBySpecialPower(List<AbstractUma> list) {
        List<AbstractUma> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparingInt(AbstractUma::calculateSpecialPower).reversed());
        return sorted;
    }
    
    public static List<AbstractUma> sortByType(List<AbstractUma> list) {
        List<AbstractUma> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparing(uma -> uma.getCharacterType().toString()));
        return sorted;
    }
    
    public static List<AbstractUma> sortByTotalStats(List<AbstractUma> list) {
        List<AbstractUma> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparingInt(AbstractUma::getTotalStats).reversed());
        return sorted;
    }
    
    public static List<AbstractUma> sortByName(List<AbstractUma> list) {
        List<AbstractUma> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparing(AbstractUma::getName));
        return sorted;
    }
    
    public static Map<String, List<AbstractUma>> groupByType(List<AbstractUma> list) {
        Map<String, List<AbstractUma>> grouped = new LinkedHashMap<>();
        
        for (AbstractUma uma : list) {
            String type = uma.getCharacterType().toString();
            grouped.computeIfAbsent(type, k -> new ArrayList<>()).add(uma);
        }
        
        return grouped;
    }
}
