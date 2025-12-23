package com.atziluth.sephirah.chesed.model;

/**
 * Enum representing different race distance categories in Umamusume.
 * Characters have different proficiency levels for different distances.
 */
public enum DistanceType {
    SHORT(1200, "Short", "Under 1200m"),
    MILE(1600, "Mile", "1200-2000m"),
    MEDIUM(2400, "Medium", "2000-2600m"),
    LONG(3200, "Long", "Over 2600m");
    
    private final int distance;
    private final String shortName;
    private final String description;
    
    DistanceType(int distance, String shortName, String description) {
        this.distance = distance;
        this.shortName = shortName;
        this.description = description;
    }
    
    public int getDistance() {
        return distance;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getDescription() {
        return description;
    }
}
