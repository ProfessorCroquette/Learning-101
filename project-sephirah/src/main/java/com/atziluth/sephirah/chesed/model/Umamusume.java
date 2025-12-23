package com.atziluth.sephirah.chesed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

/**
 * Complete Umamusume character model with full API and GameTora integration.
 * Consolidates all data from:
 * - Umapyoi.net API (profile, physical attributes, images, etc.)
 * - GameTora wiki (stats, aptitudes, skills, rarity)
 * Demonstrates OOP: Encapsulation, Inheritance, Composition.
 */
public class Umamusume {
    // ===== IDENTIFICATION =====
    private int id;                           // Game character ID (from API)
    private String name;                      // English name
    private String japaneseName;              // Japanese name
    
    // ===== RARITY & TYPE =====
    private Rarity rarity;                    // From GameTora
    private CharacterType type;               // Inferred from stats
    
    // ===== GAME STATS (from GameTora) =====
    private Stats stats;
    
    // ===== TRACK APTITUDES (from GameTora) =====
    private List<TrackProficiency> proficiencies;
    private Aptitudes aptitudes;              // Surface, Distance, Strategy grades
    
    // ===== API PROFILE DATA (from Umapyoi API) =====
    private String profile;                   // Character description
    private String slogan;                    // Character slogan
    private String strengths;                 // Character strengths
    private String weaknesses;                // Character weaknesses
    
    // ===== PHYSICAL ATTRIBUTES (from Umapyoi API) =====
    private int height;                       // in cm
    private String weight;
    private int bustSize;
    private int waistSize;
    private int hipSize;
    private String shoeSize;
    
    // ===== BIRTHDAY & LOCATION (from API) =====
    private int birthMonth;
    private int birthDay;
    private String residence;                 // Where they live
    
    // ===== CHARACTER LORE (from API) =====
    private String earsFact;                  // About their ears
    private String tailFact;                  // About their tail
    private String familyFact;                // Family background
    
    // ===== WIKI & SKILLS (from GameTora) =====
    private List<String> skills;              // Unique, Innate, Awakening, Event skills
    private String biography;                 // Character biography from wiki
    private List<String> relationships;       // Related characters
    
    // ===== VISUAL ELEMENTS (from API) =====
    private String colorMain;                 // Primary theme color
    private String colorSub;                  // Secondary theme color
    private String thumbnailImageUrl;
    private String detailImageUrl;
    private String snsIconUrl;
    
    // ===== GAME METADATA (from API) =====
    private String categoryLabel;             // Game category
    private String grade;                     // Grade/rank
    
    // Constructor
    public Umamusume() {
        this.proficiencies = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.relationships = new ArrayList<>();
        this.aptitudes = new Aptitudes();
    }
    
    public Umamusume(String name, Rarity rarity, CharacterType type) {
        this();
        this.name = name;
        this.rarity = rarity;
        this.type = type;
        this.stats = new Stats();
    }
    
    
    // ===== IDENTIFICATION GETTERS/SETTERS =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getJapaneseName() { return japaneseName; }
    public void setJapaneseName(String japaneseName) { this.japaneseName = japaneseName; }
    
    // ===== RARITY & TYPE GETTERS/SETTERS =====
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    
    public CharacterType getType() { return type; }
    public void setType(CharacterType type) { this.type = type; }
    
    // ===== GAME STATS GETTERS/SETTERS =====
    public Stats getStats() { 
        if (stats == null) {
            stats = new Stats();
        }
        return stats; 
    }
    public void setStats(Stats stats) { this.stats = stats; }
    
    public int getTotalStats() {
        return stats != null ? stats.getTotal() : 0;
    }
    
    // ===== APTITUDES GETTERS/SETTERS =====
    public Aptitudes getAptitudes() { return aptitudes; }
    public void setAptitudes(Aptitudes aptitudes) { this.aptitudes = aptitudes; }
    
    // ===== PROFICIENCIES GETTERS/SETTERS =====
    public List<TrackProficiency> getProficiencies() { return Collections.unmodifiableList(proficiencies); }
    public void addProficiency(TrackProficiency tp) { proficiencies.add(tp); }
    
    public int getProficiencyScore(TrackProficiency.TrackType trackType, TrackProficiency.DistanceType distanceType) {
        return proficiencies.stream()
            .filter(p -> p.getTrackType() == trackType && p.getDistanceType() == distanceType)
            .findFirst()
            .map(TrackProficiency::getGradeScore)
            .orElse(0);
    }
    
    // ===== API PROFILE DATA GETTERS/SETTERS =====
    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }
    
    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }
    
    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }
    
    public String getWeaknesses() { return weaknesses; }
    public void setWeaknesses(String weaknesses) { this.weaknesses = weaknesses; }
    
    // ===== PHYSICAL ATTRIBUTES GETTERS/SETTERS =====
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    
    public int getBustSize() { return bustSize; }
    public void setBustSize(int bustSize) { this.bustSize = bustSize; }
    
    public int getWaistSize() { return waistSize; }
    public void setWaistSize(int waistSize) { this.waistSize = waistSize; }
    
    public int getHipSize() { return hipSize; }
    public void setHipSize(int hipSize) { this.hipSize = hipSize; }
    
    public String getShoeSize() { return shoeSize; }
    public void setShoeSize(String shoeSize) { this.shoeSize = shoeSize; }
    
    // ===== BIRTHDAY & LOCATION GETTERS/SETTERS =====
    public int getBirthMonth() { return birthMonth; }
    public void setBirthMonth(int birthMonth) { this.birthMonth = birthMonth; }
    
    public int getBirthDay() { return birthDay; }
    public void setBirthDay(int birthDay) { this.birthDay = birthDay; }
    
    public String getResidence() { return residence; }
    public void setResidence(String residence) { this.residence = residence; }
    
    // ===== CHARACTER LORE GETTERS/SETTERS =====
    public String getEarsFact() { return earsFact; }
    public void setEarsFact(String earsFact) { this.earsFact = earsFact; }
    
    public String getTailFact() { return tailFact; }
    public void setTailFact(String tailFact) { this.tailFact = tailFact; }
    
    public String getFamilyFact() { return familyFact; }
    public void setFamilyFact(String familyFact) { this.familyFact = familyFact; }
    
    // ===== WIKI & SKILLS GETTERS/SETTERS =====
    public List<String> getSkills() { return Collections.unmodifiableList(skills); }
    public void addSkill(String skill) { 
        if (skill != null && !skills.contains(skill)) {
            skills.add(skill); 
        }
    }
    public void setSkills(List<String> skills) { 
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>(); 
    }
    
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    
    public List<String> getRelationships() { return Collections.unmodifiableList(relationships); }
    public void addRelationship(String relationship) { 
        if (relationship != null && !relationships.contains(relationship)) {
            relationships.add(relationship); 
        }
    }
    public void setRelationships(List<String> relationships) { 
        this.relationships = relationships != null ? new ArrayList<>(relationships) : new ArrayList<>(); 
    }
    
    // ===== VISUAL ELEMENTS GETTERS/SETTERS =====
    public String getColorMain() { return colorMain; }
    public void setColorMain(String colorMain) { this.colorMain = colorMain; }
    
    public String getColorSub() { return colorSub; }
    public void setColorSub(String colorSub) { this.colorSub = colorSub; }
    
    public String getThumbnailImageUrl() { return thumbnailImageUrl; }
    public void setThumbnailImageUrl(String thumbnailImageUrl) { this.thumbnailImageUrl = thumbnailImageUrl; }
    
    public String getDetailImageUrl() { return detailImageUrl; }
    public void setDetailImageUrl(String detailImageUrl) { this.detailImageUrl = detailImageUrl; }
    
    public String getSnsIconUrl() { return snsIconUrl; }
    public void setSnsIconUrl(String snsIconUrl) { this.snsIconUrl = snsIconUrl; }
    
    // ===== GAME METADATA GETTERS/SETTERS =====
    public String getCategoryLabel() { return categoryLabel; }
    public void setCategoryLabel(String categoryLabel) { this.categoryLabel = categoryLabel; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - %s | Total: %d", 
            name, rarity, type, getTotalStats());
    }
    
    // ===== APTITUDES INNER CLASS =====
    public static class Aptitudes {
        // Surface aptitudes (Turf/Dirt)
        private String turfGrade;              // A-G
        private String dirtGrade;              // A-G
        
        // Distance aptitudes (4 types)
        private String shortDistance;          // Sprint (1000-1400m)
        private String mileDistance;           // Mile (1401-1800m)
        private String mediumDistance;         // Medium (1801-2400m)
        private String longDistance;           // Long (2401-3200m)
        
        // Strategy aptitudes (4 types)
        private String frontStrategy;          // Front running
        private String paceStrategy;           // Pace making
        private String lateStrategy;           // Late running
        private String endStrategy;            // End sprint
        
        public Aptitudes() {}
        
        // Getters and Setters
        public String getTurfGrade() { return turfGrade; }
        public void setTurfGrade(String grade) { this.turfGrade = grade; }
        
        public String getDirtGrade() { return dirtGrade; }
        public void setDirtGrade(String grade) { this.dirtGrade = grade; }
        
        public String getShortDistance() { return shortDistance; }
        public void setShortDistance(String grade) { this.shortDistance = grade; }
        
        public String getMileDistance() { return mileDistance; }
        public void setMileDistance(String grade) { this.mileDistance = grade; }
        
        public String getMediumDistance() { return mediumDistance; }
        public void setMediumDistance(String grade) { this.mediumDistance = grade; }
        
        public String getLongDistance() { return longDistance; }
        public void setLongDistance(String grade) { this.longDistance = grade; }
        
        public String getFrontStrategy() { return frontStrategy; }
        public void setFrontStrategy(String grade) { this.frontStrategy = grade; }
        
        public String getPaceStrategy() { return paceStrategy; }
        public void setPaceStrategy(String grade) { this.paceStrategy = grade; }
        
        public String getLateStrategy() { return lateStrategy; }
        public void setLateStrategy(String grade) { this.lateStrategy = grade; }
        
        public String getEndStrategy() { return endStrategy; }
        public void setEndStrategy(String grade) { this.endStrategy = grade; }
        
        @Override
        public String toString() {
            return String.format("Surface: Turf[%s] Dirt[%s] | Distance: Short[%s] Mile[%s] Medium[%s] Long[%s] | Strategy: Front[%s] Pace[%s] Late[%s] End[%s]",
                turfGrade, dirtGrade, shortDistance, mileDistance, mediumDistance, longDistance, 
                frontStrategy, paceStrategy, lateStrategy, endStrategy);
        }
    }
    
    // ===== ENUMS =====
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