package com.atziluth.sephirah.chesed.model;

/**
 * Stamina-type Umamusume character.
 * Specialized for long-distance racing with enhanced stamina stats.
 */
public class StaminaUma extends AbstractUma {
    private int staminaBonus = 20;
    
    public StaminaUma(String name, String japaneseName, Umamusume.Rarity rarity) {
        super(name, japaneseName, rarity);
        initializeStats();
    }
    
    private void initializeStats() {
        stats.setSpeed(70);
        stats.setStamina(90 + staminaBonus);
        stats.setPower(75);
        stats.setGuts(85);
        stats.setIntelligence(75);
    }
    
    @Override
    public Umamusume.CharacterType getCharacterType() {
        return Umamusume.CharacterType.STAMINA;
    }
    
    @Override
    public int calculateSpecialPower() {
        return stats.getStamina() + (rarity.getValue() * 15);
    }
    
    @Override
    public String getSpecialty() {
        return "Long-distance races";
    }
    
    public void setStaminaBonus(int bonus) {
        this.staminaBonus = bonus;
        initializeStats();
    }
}
