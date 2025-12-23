package com.atziluth.sephirah.chesed.model;

/**
 * Speed-type Umamusume character.
 * Specialized for speed racing with enhanced speed stats.
 */
public class SpeedUma extends AbstractUma {
    private int speedBonus = 20;
    
    public SpeedUma(String name, String japaneseName, Umamusume.Rarity rarity) {
        super(name, japaneseName, rarity);
        initializeStats();
    }
    
    private void initializeStats() {
        stats.setSpeed(90 + speedBonus);
        stats.setStamina(70);
        stats.setPower(65);
        stats.setGuts(75);
        stats.setIntelligence(80);
    }
    
    @Override
    public Umamusume.CharacterType getCharacterType() {
        return Umamusume.CharacterType.RUNNER;
    }
    
    @Override
    public int calculateSpecialPower() {
        return stats.getSpeed() + (rarity.getValue() * 10);
    }
    
    @Override
    public String getSpecialty() {
        return "Sprint races on turf tracks";
    }
    
    public void setSpeedBonus(int bonus) {
        this.speedBonus = bonus;
        initializeStats();
    }
}
