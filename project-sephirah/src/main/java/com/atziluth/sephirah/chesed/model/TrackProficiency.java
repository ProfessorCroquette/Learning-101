package com.atziluth.sephirah.chesed.model;

/**
 * Track proficiency system for Umamusume.
 */
public class TrackProficiency {
    private TrackType trackType;
    private DistanceType distanceType;
    private Grade grade;
    
    public TrackProficiency() {}
    
    public TrackProficiency(TrackType trackType, DistanceType distanceType, Grade grade) {
        this.trackType = trackType;
        this.distanceType = distanceType;
        this.grade = grade;
    }
    
    // Getters
    public TrackType getTrackType() { return trackType; }
    public DistanceType getDistanceType() { return distanceType; }
    public Grade getGrade() { return grade; }
    
    public int getGradeScore() {
        return grade.getScore();
    }
    
    @Override
    public String toString() {
        return String.format("%s %s: %s", 
            trackType.getDisplayName(), 
            distanceType.getDisplayName(), 
            grade);
    }
    
    // Enums
    public enum TrackType {
        TURF("Turf", "芝"), DIRT("Dirt", "ダート");
        
        private final String english;
        private final String japanese;
        
        TrackType(String english, String japanese) {
            this.english = english;
            this.japanese = japanese;
        }
        
        public String getDisplayName() { return english; }
        public String getJapanese() { return japanese; }
    }
    
    public enum DistanceType {
        SPRINT("Sprint", "短距離", 1000, 1400),
        MILE("Mile", "マイル", 1401, 1800),
        MEDIUM("Medium", "中距離", 1801, 2400),
        LONG("Long", "長距離", 2401, 3200);
        
        private final String english;
        private final String japanese;
        private final int minDistance;
        private final int maxDistance;
        
        DistanceType(String english, String japanese, int minDistance, int maxDistance) {
            this.english = english;
            this.japanese = japanese;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
        }
        
        public String getDisplayName() { return english; }
        public String getJapanese() { return japanese; }
        public boolean isInRange(int distance) {
            return distance >= minDistance && distance <= maxDistance;
        }
    }
    
    public enum Grade {
        G(0), F(1), E(2), D(3), C(4), B(5), A(6), S(7);
        
        private final int score;
        
        Grade(int score) {
            this.score = score;
        }
        
        public int getScore() { return score; }
        
        public static Grade fromScore(int score) {
            score = Math.min(Math.max(score, 0), 7);
            return values()[score];
        }
    }
}