package com.atziluth.sephirah.chesed.model;

/**
 * Umamusume with unique skills/abilities.
 * Demonstrates composition and interface implementation.
 */
public class UniqueSkillUma extends AbstractUma implements SpecialAbility {
    private String skillName;
    private String skillDescription;
    private int skillPowerLevel;
    private boolean skillActive = false;
    
    public UniqueSkillUma(String name, String japaneseName, Umamusume.Rarity rarity,
                         String skillName, String skillDescription, int powerLevel) {
        super(name, japaneseName, rarity);
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.skillPowerLevel = powerLevel;
    }
    
    @Override
    public Umamusume.CharacterType getCharacterType() {
        return Umamusume.CharacterType.GUTS;
    }
    
    @Override
    public int calculateSpecialPower() {
        return skillActive ? skillPowerLevel * 2 : skillPowerLevel;
    }
    
    @Override
    public String getSpecialty() {
        return "Unique skill: " + skillName;
    }
    
    @Override
    public String getAbilityName() {
        return skillName;
    }
    
    @Override
    public String getAbilityDescription() {
        return skillDescription;
    }
    
    @Override
    public int getPowerLevel() {
        return skillPowerLevel;
    }
    
    @Override
    public void activate() {
        skillActive = true;
    }
    
    @Override
    public void deactivate() {
        skillActive = false;
    }
}
