package com.atziluth.sephirah.chesed;

import com.atziluth.sephirah.chesed.api.*;
import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.sorting.Comparators;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central manager for character operations
 */
public class UmapyoiCharacterManager {
    private final UmapyoiApiClient apiClient;
    private final CharacterService characterService;
    private final Map<Integer, UmapyoiCharacter> characterCache;
    
    public UmapyoiCharacterManager(UmapyoiApiClient apiClient, CharacterService characterService) {
        this.apiClient = apiClient;
        this.characterService = characterService;
        this.characterCache = new HashMap<>();
    }
    
    public List<UmapyoiCharacter> advancedSearch(String name, String rarity, 
                                                Integer minHeight, Integer maxHeight) 
            throws IOException {
        
        // Get popular characters as base
        List<UmapyoiCharacter> characters = characterService.getPopularCharacters();
        
        return characters.stream()
            .filter(char -> {
                // Filter by name
                if (name != null && !name.isEmpty()) {
                    if (!char.getNameEnglish().toLowerCase().contains(name.toLowerCase()) &&
                        !char.getNameJapanese().contains(name)) {
                        return false;
                    }
                }
                
                // Filter by rarity
                if (rarity != null && !rarity.isEmpty()) {
                    Umamusume uma = char.toDomainModel();
                    if (!uma.getRarity().name().equals(rarity.toUpperCase())) {
                        return false;
                    }
                }
                
                // Filter by height
                if (minHeight != null && char.getHeight() < minHeight) {
                    return false;
                }
                if (maxHeight != null && char.getHeight() > maxHeight) {
                    return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());
    }
    
    public List<UmapyoiCharacter> sortCharacters(List<UmapyoiCharacter> characters, int sortType) {
        List<UmapyoiCharacter> sorted = new ArrayList<>(characters);
        
        switch (sortType) {
            case 1: // By Name
                sorted.sort(Comparators.characterByName());
                break;
            case 2: // By Height (tallest first)
                sorted.sort(Comparators.characterByHeight().reversed());
                break;
            case 3: // By Estimated Stats
                sorted.sort((c1, c2) -> {
                    int stats1 = c1.toDomainModel().getTotalStats();
                    int stats2 = c2.toDomainModel().getTotalStats();
                    return Integer.compare(stats2, stats1); // Descending
                });
                break;
        }
        
        return sorted;
    }
    
    public void cacheCharacter(UmapyoiCharacter character) {
        characterCache.put(character.getGameId(), character);
    }
    
    public UmapyoiCharacter getCachedCharacter(int id) {
        return characterCache.get(id);
    }
    
    public void clearCache() {
        characterCache.clear();
    }
    
    public int getCacheSize() {
        return characterCache.size();
    }
}