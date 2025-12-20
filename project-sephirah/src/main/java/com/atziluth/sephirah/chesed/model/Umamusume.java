package com.atziluth.sephirah.chesed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

/**
 * Complete Umamusume character model.
 * Demonstrates OOP: Encapsulation, Inheritance, Composition.
 */
public class Umamusume {
    private int id;
    private String name;
    private String japaneseName;
    private Rarity rarity;
    private CharacterType type;
    
    // Stats using composition
    private Stats stats;
    
    // Track proficiencies
    private List<TrackProficiency> proficiencies;
    
    // Constructor
    public Umamusume() {
        this.proficiencies = new ArrayList<>();
    }
    
    public Umamusume(String name, Rarity rarity, CharacterType type) {
        this();
        this.name = name;
        this.rarity = rarity;
        this.type = type;
        this.stats = new Stats();
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    
    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }
    
    public List<TrackProficiency> getProficiencies() { return proficiencies; }
    public void addProficiency(TrackProficiency tp) { proficiencies.add(tp); }
    
    // Calculate total stats
    public int getTotalStats() {
        return stats != null ? stats.getTotal() : 0;
    }
    
    // Get proficiency score for specific track
    public int getProficiencyScore(TrackType trackType, DistanceType distanceType) {
        return proficiencies.stream()
            .filter(p -> p.getTrackType() == trackType && p.getDistanceType() == distanceType)
            .findFirst()
            .map(TrackProficiency::getGradeScore)
            .orElse(0);
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - %s | Total: %d", 
            name, rarity, type, getTotalStats());
    }
    
    // Enums
    public enum Rarity {
        N("Normal", 1), R("Rare", 2), SR("Super Rare", 3), 
        SSR("Special Super Rare", 4), UR("Ultra Rare", 5);
        
        private final String displayName;
        private final int value;
        
        Rarity(String displayName, int value) {
            this.displayName = displayName;
            this.value = value;
        }
        
        public int getValue() { return value; }
        public String getDisplayName() { return displayName; }
    }
    
    public enum CharacterType {
        RUNNER("Speed"), STAMINA("Stamina"), POWER("Power"), 
        GUTS("Guts"), INTELLIGENCE("Intelligence");
        
        private final String description;
        
        CharacterType(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    // Stats inner class
    public static class Stats {
        private int speed;
        private int stamina;
        private int power;
        private int guts;
        private int intelligence;
        
        public Stats() {}
        
        public Stats(int speed, int stamina, int power, int guts, int intelligence) {
            this.speed = speed;
            this.stamina = stamina;
            this.power = power;
            this.guts = guts;
            this.intelligence = intelligence;
        }
        
        // Getters and setters
        public int getSpeed() { return speed; }
        public void setSpeed(int speed) { this.speed = speed; }
        
        public int getStamina() { return stamina; }
        public void setStamina(int stamina) { this.stamina = stamina; }
        
        public int getPower() { return power; }
        public void setPower(int power) { this.power = power; }
        
        public int getGuts() { return guts; }
        public void setGuts(int guts) { this.guts = guts; }
        
        public int getIntelligence() { return intelligence; }
        public void setIntelligence(int intelligence) { 
            this.intelligence = intelligence; 
        }
        
        public int getTotal() {
            return speed + stamina + power + guts + intelligence;
        }
        
        @Override
        public String toString() {
            return String.format("Spd:%d Sta:%d Pow:%d Gut:%d Int:%d (Total:%d)",
                speed, stamina, power, guts, intelligence, getTotal());
        }
    }
}