package com.atziluth.sephirah.chesed.factory;

import com.atziluth.sephirah.chesed.model.*;

/**
 * Factory pattern for creating different types of Umamusume.
 * Demonstrates the Factory design pattern.
 */
public class UmaFactory {
    
    public enum UmaType {
        SPEED, STAMINA, POWER, GUTS, INTELLIGENCE, UNIQUE
    }
    
    public static AbstractUma createUma(UmaType type, String name, String japaneseName, Umamusume.Rarity rarity) {
        switch (type) {
            case SPEED:
                return new SpeedUma(name, japaneseName, rarity);
            case STAMINA:
                return new StaminaUma(name, japaneseName, rarity);
            case POWER:
                return createPowerUma(name, japaneseName, rarity);
            case GUTS:
                return createGutsUma(name, japaneseName, rarity);
            case INTELLIGENCE:
                return createIntelligenceUma(name, japaneseName, rarity);
            default:
                return new SpeedUma(name, japaneseName, rarity);
        }
    }
    
    public static AbstractUma createFromApiModel(UmapyoiCharacter apiCharacter) {
        String strengthsLower = apiCharacter.getStrengths() != null ? 
            apiCharacter.getStrengths().toLowerCase() : "";
        
        UmaType type = UmaType.STAMINA;
        if (strengthsLower.contains("speed")) type = UmaType.SPEED;
        else if (strengthsLower.contains("power")) type = UmaType.POWER;
        else if (strengthsLower.contains("guts")) type = UmaType.GUTS;
        else if (strengthsLower.contains("intelligence")) type = UmaType.INTELLIGENCE;
        
        AbstractUma uma = createUma(type, apiCharacter.getNameEnglish(), 
                                   apiCharacter.getNameJapanese(),
                                   determineRarity(apiCharacter.getGameId()));
        uma.setId(apiCharacter.getGameId());
        return uma;
    }
    
    private static AbstractUma createPowerUma(String name, String japaneseName, Umamusume.Rarity rarity) {
        AbstractUma uma = new StaminaUma(name, japaneseName, rarity);
        uma.getStats().setPower(90);
        return uma;
    }
    
    private static AbstractUma createGutsUma(String name, String japaneseName, Umamusume.Rarity rarity) {
        AbstractUma uma = new StaminaUma(name, japaneseName, rarity);
        uma.getStats().setGuts(95);
        return uma;
    }
    
    private static AbstractUma createIntelligenceUma(String name, String japaneseName, Umamusume.Rarity rarity) {
        AbstractUma uma = new StaminaUma(name, japaneseName, rarity);
        uma.getStats().setIntelligence(95);
        return uma;
    }
    
    private static Umamusume.Rarity determineRarity(int gameId) {
        if (gameId < 1010) return Umamusume.Rarity.UR;
        else if (gameId < 1100) return Umamusume.Rarity.SSR;
        else if (gameId < 2000) return Umamusume.Rarity.SR;
        else if (gameId < 3000) return Umamusume.Rarity.R;
        else return Umamusume.Rarity.N;
    }
}
