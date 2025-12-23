package com.atziluth.sephirah.chesed.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic character list response model from API.
 * Lightweight model for list endpoints.
 */
public class CharacterBasic {
    @JsonProperty("id")
    private int gameId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("name_jp")
    private String nameJapanese;
    
    @JsonProperty("icon")
    private String iconUrl;
    
    public CharacterBasic() {}
    
    public CharacterBasic(int gameId, String name, String nameJapanese) {
        this.gameId = gameId;
        this.name = name;
        this.nameJapanese = nameJapanese;
    }
    
    public int getGameId() { return gameId; }
    public String getName() { return name; }
    public String getNameJapanese() { return nameJapanese; }
    public String getIconUrl() { return iconUrl; }
    
    public void setGameId(int gameId) { this.gameId = gameId; }
    public void setName(String name) { this.name = name; }
    public void setNameJapanese(String nameJapanese) { this.nameJapanese = nameJapanese; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", name, nameJapanese);
    }
}
