package com.atziluth.sephirah.chesed.model;

/**
 * Interface for special abilities in Umamusume.
 * Demonstrates interface-based design and composition.
 */
public interface SpecialAbility {
    String getAbilityName();
    String getAbilityDescription();
    int getPowerLevel();
    void activate();
    void deactivate();
}
