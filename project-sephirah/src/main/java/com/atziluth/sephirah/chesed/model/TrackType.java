package com.atziluth.sephirah.chesed.model;

/**
 * Enum representing different race track types in Umamusume.
 * Each track type focuses on different racing strategies and character abilities.
 */
public enum TrackType {
    DIRT("Dirt Track", "Tracks made of dirt/soil"),
    TURF("Turf Track", "Grass-based racing surface"),
    SHORT("Short Distance", "Distance under 1200m"),
    MILE("Mile", "Distance around 1600-2000m"),
    MEDIUM("Medium Distance", "Distance around 2200-2600m"),
    LONG("Long Distance", "Distance over 2800m");
    
    private final String displayName;
    private final String description;
    
    TrackType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
