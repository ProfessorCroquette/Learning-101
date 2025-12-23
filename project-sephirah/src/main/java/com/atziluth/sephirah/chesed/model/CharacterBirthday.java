package com.atziluth.sephirah.chesed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Model for character birthday information from the Umapyoi API.
 * Represents when each character has their birthday in the game.
 */
public class CharacterBirthday {
    @JsonProperty("character_id")
    private int characterId;
    
    @JsonProperty("month")
    private int month;
    
    @JsonProperty("day")
    private int day;
    
    @JsonProperty("birthday_date")
    private String birthdayDate;
    
    public CharacterBirthday() {}
    
    public CharacterBirthday(int characterId, int month, int day, String birthdayDate) {
        this.characterId = characterId;
        this.month = month;
        this.day = day;
        this.birthdayDate = birthdayDate;
    }
    
    public int getCharacterId() {
        return characterId;
    }
    
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getDay() {
        return day;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public String getBirthdayDate() {
        return birthdayDate;
    }
    
    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }
    
    /**
     * Get formatted birthday string (e.g., "March 15")
     */
    public String getFormattedBirthday() {
        String[] monthNames = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        
        if (month > 0 && month <= 12) {
            return monthNames[month - 1] + " " + day;
        }
        return "Unknown";
    }
    
    @Override
    public String toString() {
        return "CharacterBirthday{" +
                "characterId=" + characterId +
                ", month=" + month +
                ", day=" + day +
                ", birthdayDate='" + birthdayDate + '\'' +
                '}';
    }
}
