package com.atziluth.sephirah.chesed.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for character image URLs from the Umapyoi API.
 * Stores references to different character artwork and in-game images.
 */
public class CharacterImages {
    @JsonProperty("icon")
    private String icon;
    
    @JsonProperty("cover")
    private String cover;
    
    @JsonProperty("illustration")
    private String illustration;
    
    @JsonProperty("full_body")
    private String fullBody;
    
    public CharacterImages() {}
    
    public CharacterImages(String icon, String cover, String illustration, String fullBody) {
        this.icon = icon;
        this.cover = cover;
        this.illustration = illustration;
        this.fullBody = fullBody;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getCover() {
        return cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    
    public String getIllustration() {
        return illustration;
    }
    
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }
    
    public String getFullBody() {
        return fullBody;
    }
    
    public void setFullBody(String fullBody) {
        this.fullBody = fullBody;
    }
    
    @Override
    public String toString() {
        return "CharacterImages{" +
                "icon='" + icon + '\'' +
                ", cover='" + cover + '\'' +
                ", illustration='" + illustration + '\'' +
                ", fullBody='" + fullBody + '\'' +
                '}';
    }
}
