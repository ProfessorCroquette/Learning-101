package com.atziluth.sephirah.chesed.model;

/**
 * Abstract base class for Umamusume characters.
 * Demonstrates inheritance and polymorphism in OOP.
 */
public abstract class AbstractUma {
    protected int id;
    protected String name;
    protected String japaneseName;
    protected Umamusume.Rarity rarity;
    protected Umamusume.Stats stats;
    
    public AbstractUma(String name, String japaneseName, Umamusume.Rarity rarity) {
        this.name = name;
        this.japaneseName = japaneseName;
        this.rarity = rarity;
        this.stats = new Umamusume.Stats();
    }
    
    // Abstract methods that subclasses must implement
    public abstract Umamusume.CharacterType getCharacterType();
    public abstract int calculateSpecialPower();
    public abstract String getSpecialty();
    
    // Concrete methods
    public String getFullName() {
        return name + " (" + japaneseName + ")";
    }
    
    public int getTotalStats() {
        return stats.getTotal();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public String getJapaneseName() { return japaneseName; }
    public Umamusume.Rarity getRarity() { return rarity; }
    public Umamusume.Stats getStats() { return stats; }
    public void setStats(Umamusume.Stats stats) { this.stats = stats; }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - Type: %s | Power: %d", 
            getFullName(), rarity, getCharacterType(), calculateSpecialPower());
    }
}
