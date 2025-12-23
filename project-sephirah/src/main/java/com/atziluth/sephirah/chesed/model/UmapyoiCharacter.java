package com.atziluth.sephirah.chesed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for character data from the Umapyoi.net API.
 * Maps directly to the JSON response from the API endpoint.
 * Demonstrates encapsulation with private fields and public getters/setters.
 */
public class UmapyoiCharacter {
    
    // ===== API RESPONSE FIELDS (Direct JSON mapping) =====
    
    // Basic Information
    @JsonProperty("id")
    private int apiId; // Internal API ID (e.g., 4737)
    
    @JsonProperty("game_id")
    private int gameId; // Game character ID (e.g., 1001 for Special Week)
    
    @JsonProperty("row_number")
    private int rowNumber;
    
    // Names
    @JsonProperty("name_en")
    private String nameEnglish;
    
    @JsonProperty("name_jp")
    private String nameJapanese;
    
    @JsonProperty("name_en_internal")
    private String nameInternal;
    
    @JsonProperty("preferred_url")
    private String preferredUrl;
    
    // Profile & Description
    @JsonProperty("profile")
    private String profile;
    
    @JsonProperty("slogan")
    private String slogan;
    
    @JsonProperty("grade")
    private String grade;
    
    // Physical Attributes
    @JsonProperty("height")
    private int height; // in centimeters
    
    @JsonProperty("weight")
    private String weight;
    
    // Body Measurements
    @JsonProperty("size_b")
    private int bustSize;
    
    @JsonProperty("size_w")
    private int waistSize;
    
    @JsonProperty("size_h")
    private int hipSize;
    
    @JsonProperty("shoe_size")
    private String shoeSize;
    
    // Category Information
    @JsonProperty("category_label")
    private String categoryLabel;
    
    @JsonProperty("category_label_en")
    private String categoryLabelEnglish;
    
    @JsonProperty("category_value")
    private String categoryValue;
    
    // Birthday
    @JsonProperty("birth_month")
    private int birthMonth;
    
    @JsonProperty("birth_day")
    private int birthDay;
    
    // Character Traits
    @JsonProperty("strengths")
    private String strengths;
    
    @JsonProperty("weaknesses")
    private String weaknesses;
    
    @JsonProperty("ears_fact")
    private String earsFact;
    
    @JsonProperty("tail_fact")
    private String tailFact;
    
    @JsonProperty("family_fact")
    private String familyFact;
    
    // Location
    @JsonProperty("residence")
    private String residence;
    
    // Theme Colors
    @JsonProperty("color_main")
    private String colorMain;
    
    @JsonProperty("color_sub")
    private String colorSub;
    
    // Images
    @JsonProperty("thumb_img")
    private String thumbnailImageUrl;
    
    @JsonProperty("detail_img_pc")
    private String detailImagePcUrl;
    
    @JsonProperty("detail_img_sp")
    private String detailImageSpUrl;
    
    @JsonProperty("sns_icon")
    private String snsIconUrl;
    
    @JsonProperty("sns_header")
    private String snsHeaderUrl;
    
    // Audio
    @JsonProperty("voice")
    private String voiceClipUrl;
    
    // Links
    @JsonProperty("link")
    private String officialLink;
    
    // Timestamps
    @JsonProperty("date_gmt")
    private String dateCreated;
    
    @JsonProperty("modified_gmt")
    private String dateModified;
    
    // ===== CONSTRUCTORS =====
    
    /** Default constructor required by Jackson for deserialization. */
    public UmapyoiCharacter() {}
    
    /**
     * Convenience constructor for testing.
     * @param gameId The game character ID
     * @param nameEnglish English name
     * @param nameJapanese Japanese name
     */
    public UmapyoiCharacter(int gameId, String nameEnglish, String nameJapanese) {
        this.gameId = gameId;
        this.nameEnglish = nameEnglish;
        this.nameJapanese = nameJapanese;
    }
    
    // ===== GETTERS =====
    
    public int getApiId() { return apiId; }
    public int getGameId() { return gameId; }
    public int getRowNumber() { return rowNumber; }
    public String getNameEnglish() { return nameEnglish; }
    public String getNameJapanese() { return nameJapanese; }
    public String getNameInternal() { return nameInternal; }
    public String getPreferredUrl() { return preferredUrl; }
    public String getProfile() { return profile; }
    public String getSlogan() { return slogan; }
    public String getGrade() { return grade; }
    public int getHeight() { return height; }
    public String getWeight() { return weight; }
    public int getBustSize() { return bustSize; }
    public int getWaistSize() { return waistSize; }
    public int getHipSize() { return hipSize; }
    public String getShoeSize() { return shoeSize; }
    public String getCategoryLabel() { return categoryLabel; }
    public String getCategoryLabelEnglish() { return categoryLabelEnglish; }
    public String getCategoryValue() { return categoryValue; }
    public int getBirthMonth() { return birthMonth; }
    public int getBirthDay() { return birthDay; }
    public String getStrengths() { return strengths; }
    public String getWeaknesses() { return weaknesses; }
    public String getEarsFact() { return earsFact; }
    public String getTailFact() { return tailFact; }
    public String getFamilyFact() { return familyFact; }
    public String getResidence() { return residence; }
    public String getColorMain() { return colorMain; }
    public String getColorSub() { return colorSub; }
    public String getThumbnailImageUrl() { return thumbnailImageUrl; }
    public String getDetailImagePcUrl() { return detailImagePcUrl; }
    public String getDetailImageSpUrl() { return detailImageSpUrl; }
    public String getSnsIconUrl() { return snsIconUrl; }
    public String getSnsHeaderUrl() { return snsHeaderUrl; }
    public String getVoiceClipUrl() { return voiceClipUrl; }
    public String getOfficialLink() { return officialLink; }
    public String getDateCreated() { return dateCreated; }
    public String getDateModified() { return dateModified; }
    
    // ===== SETTERS =====
    
    public void setApiId(int apiId) { this.apiId = apiId; }
    public void setGameId(int gameId) { this.gameId = gameId; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }
    public void setNameJapanese(String nameJapanese) { this.nameJapanese = nameJapanese; }
    public void setNameInternal(String nameInternal) { this.nameInternal = nameInternal; }
    public void setPreferredUrl(String preferredUrl) { this.preferredUrl = preferredUrl; }
    public void setProfile(String profile) { this.profile = profile; }
    public void setSlogan(String slogan) { this.slogan = slogan; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setHeight(int height) { this.height = height; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setBustSize(int bustSize) { this.bustSize = bustSize; }
    public void setWaistSize(int waistSize) { this.waistSize = waistSize; }
    public void setHipSize(int hipSize) { this.hipSize = hipSize; }
    public void setShoeSize(String shoeSize) { this.shoeSize = shoeSize; }
    public void setCategoryLabel(String categoryLabel) { this.categoryLabel = categoryLabel; }
    public void setCategoryLabelEnglish(String categoryLabelEnglish) { this.categoryLabelEnglish = categoryLabelEnglish; }
    public void setCategoryValue(String categoryValue) { this.categoryValue = categoryValue; }
    public void setBirthMonth(int birthMonth) { this.birthMonth = birthMonth; }
    public void setBirthDay(int birthDay) { this.birthDay = birthDay; }
    public void setStrengths(String strengths) { this.strengths = strengths; }
    public void setWeaknesses(String weaknesses) { this.weaknesses = weaknesses; }
    public void setEarsFact(String earsFact) { this.earsFact = earsFact; }
    public void setTailFact(String tailFact) { this.tailFact = tailFact; }
    public void setFamilyFact(String familyFact) { this.familyFact = familyFact; }
    public void setResidence(String residence) { this.residence = residence; }
    public void setColorMain(String colorMain) { this.colorMain = colorMain; }
    public void setColorSub(String colorSub) { this.colorSub = colorSub; }
    public void setThumbnailImageUrl(String thumbnailImageUrl) { this.thumbnailImageUrl = thumbnailImageUrl; }
    public void setDetailImagePcUrl(String detailImagePcUrl) { this.detailImagePcUrl = detailImagePcUrl; }
    public void setDetailImageSpUrl(String detailImageSpUrl) { this.detailImageSpUrl = detailImageSpUrl; }
    public void setSnsIconUrl(String snsIconUrl) { this.snsIconUrl = snsIconUrl; }
    public void setSnsHeaderUrl(String snsHeaderUrl) { this.snsHeaderUrl = snsHeaderUrl; }
    public void setVoiceClipUrl(String voiceClipUrl) { this.voiceClipUrl = voiceClipUrl; }
    public void setOfficialLink(String officialLink) { this.officialLink = officialLink; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
    public void setDateModified(String dateModified) { this.dateModified = dateModified; }
    
    // ===== BUSINESS LOGIC / HELPER METHODS =====
    
    /**
     * Formats the birthday as "MM/DD".
     * @return Formatted birthday string, or "Unknown" if data is missing.
     */
    public String getFormattedBirthday() {
        if (birthMonth > 0 && birthDay > 0) {
            return String.format("%02d/%02d", birthMonth, birthDay);
        }
        return "Unknown";
    }
    
    /**
     * Checks if the character's name or profile contains the given search term.
     * This is useful for implementing a simple search function.
     * @param searchTerm The term to search for (case-insensitive).
     * @return true if the character matches the search term.
     */
    public boolean matchesSearch(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return false;
        }
        String term = searchTerm.toLowerCase().trim();
        return (nameEnglish != null && nameEnglish.toLowerCase().contains(term)) ||
               (nameJapanese != null && nameJapanese.contains(term)) ||
               (profile != null && profile.toLowerCase().contains(term));
    }
    
    /**
     * Converts this API model object into your domain model object (Umamusume).
     * This method acts as an adapter between the API layer and your application's domain logic.
     * @return A new Umamusume instance populated with data from this object.
     */
    public Umamusume toDomainModel() {
        Umamusume uma = new Umamusume();
        uma.setId(this.gameId);
        uma.setName(this.nameEnglish);
        uma.setJapaneseName(this.nameJapanese);
        
        Umamusume.Stats stats = createStats();
        uma.setStats(stats);
        
        uma.setRarity(determineRarity());
        uma.setType(determineCharacterType());
        
        return uma;
    }
    
    private Umamusume.Stats createStats() {
        Umamusume.Stats stats = new Umamusume.Stats();
        int speed = Math.min(70 + (this.height - 150) / 2, 120);
        stats.setSpeed(speed);
        
        int stamina = 70;
        if (this.weight != null) {
            if (this.weight.toLowerCase().contains("stable")) stamina += 20;
            else if (this.weight.toLowerCase().contains("decrease")) stamina -= 10;
        }
        stats.setStamina(Math.max(50, Math.min(stamina, 120)));
        
        stats.setPower(75);
        stats.setGuts(80);
        stats.setIntelligence(85);
        return stats;
    }
    
    private Umamusume.Rarity determineRarity() {
        if (this.gameId < 1010) return Umamusume.Rarity.UR;
        else if (this.gameId < 1100) return Umamusume.Rarity.SSR;
        else if (this.gameId < 2000) return Umamusume.Rarity.SR;
        else if (this.gameId < 3000) return Umamusume.Rarity.R;
        else return Umamusume.Rarity.N;
    }
    
    private Umamusume.CharacterType determineCharacterType() {
        if (this.strengths == null) return Umamusume.CharacterType.GUTS;
        
        String s = this.strengths.toLowerCase();
        if (s.contains("speed") || s.contains("fast")) return Umamusume.CharacterType.RUNNER;
        if (s.contains("stamina") || s.contains("endurance")) return Umamusume.CharacterType.STAMINA;
        if (s.contains("power") || s.contains("strength")) return Umamusume.CharacterType.POWER;
        if (s.contains("intelligence") || s.contains("smart")) return Umamusume.CharacterType.INTELLIGENCE;
        return Umamusume.CharacterType.GUTS;
    }
    
    /**
     * Provides a summary string of the character's key attributes.
     * @return A formatted summary string.
     */
    public String toSummaryString() {
        return String.format("%s (%s) | Height: %dcm | Birthday: %s | Grade: %s", 
            nameEnglish, nameJapanese, height, getFormattedBirthday(), grade);
    }
    
    // ===== OVERRIDDEN METHODS =====
    
    @Override
    public String toString() {
        return String.format("UmapyoiCharacter{gameId=%d, name='%s', height=%d}", 
            gameId, nameEnglish, height);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UmapyoiCharacter that = (UmapyoiCharacter) o;
        // Characters are considered equal if they have the same gameId
        return gameId == that.gameId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }
}