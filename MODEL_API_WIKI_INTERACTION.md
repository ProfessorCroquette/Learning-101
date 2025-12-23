# 🔗 Model-API-Wiki Interaction Architecture

## 📊 High-Level Data Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER INTERACTION                             │
│                   (ChesedSephirah Menu System)                      │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
    [Search]          [Get Stats]          [View Profile]
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
        ┌───────────────────▼───────────────────┐
        │                                       │
        │   CharacterService (Service Layer)   │
        │   - getCharacterById()                │
        │   - searchCharactersByName()          │
        │   - enrichCharacterWithWikiData()     │
        │                                       │
        └─────────────┬─────────────────────────┘
                      │
        ┌─────────────┴─────────────┐
        │                           │
        ▼                           ▼
   ┌────────────────┐      ┌──────────────────────┐
   │  API Client    │      │  Wiki Scraper        │
   │ (REST HTTP)    │      │  (HTML Parsing)      │
   │                │      │                      │
   │ UmapyoiApiClient  │      │ UmamusumeWikiScraper │
   └────────┬────────┘      └──────────┬───────────┘
            │                          │
            ▼                          ▼
   ┌────────────────┐      ┌──────────────────────┐
   │ Umapyoi API    │      │ umamusu.wiki         │
   │ (External)     │      │ (HTML Content)       │
   │                │      │                      │
   │ umapyoi.net    │      │ https://umamusu.wiki │
   └────────────────┘      └──────────────────────┘


        ┌─────────────────────────────────────┐
        │                                     │
        │   DATA MODELS (Converters)          │
        │                                     │
        │ UmapyoiCharacter ──────────────┐    │
        │ (API DTO - Raw JSON mapping)   │    │
        │                                 │    │
        │ ↓ (via enrichment)              │    │
        │                                 │    │
        │ Umamusume                        │    │
        │ (Domain Model - Typed, Enums)   │    │
        │                                 │    │
        │ ↓ (via factory)                 │    │
        │                                 │    │
        │ SpeedUma / StaminaUma / etc     │    │
        │ (Polymorphic - Type-specific)   │    │
        │                                 │    │
        └─────────────────────────────────┘
                      │
                      ▼
        ┌─────────────────────────────────────┐
        │     Display Layer                   │
        │   (WikiDataDisplay)                 │
        │   Shows formatted output            │
        └─────────────────────────────────────┘
```

---

## 🔄 Detailed Model Interactions

### **1. UmapyoiCharacter (API DTO Model)**

**Purpose:** Direct mapping from API JSON response
**Source:** Umapyoi.net REST API
**Jackson annotations:** @JsonProperty for field mapping

```java
public class UmapyoiCharacter {
    @JsonProperty("id")
    private int apiId;                    // Raw API ID
    
    @JsonProperty("name_en")
    private String nameEnglish;           // API name
    
    @JsonProperty("height")
    private int height;                   // Physical stat
    
    @JsonProperty("category_label")
    private String categoryLabel;         // Character type hint
    
    @JsonProperty("profile")
    private String profile;               // Character bio
    
    // ... 50+ more fields from API
}
```

**Used by:**
- `UmapyoiApiClient.getCharacterById()` → Returns `UmapyoiCharacter`
- `CharacterService.enrichCharacterWithWikiData()` → Input for enrichment
- `UmamusumeWikiScraper.enrichCharacterData()` → Input for conversion

**Conversion Path:**
```
JSON Response → Jackson Deserialization → UmapyoiCharacter
```

---

### **2. Umamusume (Domain Model)**

**Purpose:** Type-safe domain model with enums and composition
**Source:** Converted from `UmapyoiCharacter` + Wiki data
**Enums:** Rarity, CharacterType

```java
public class Umamusume {
    private int id;
    private String name;
    private String japaneseName;
    
    // ENUMS (Type Safety)
    private Rarity rarity;                // UR, SSR, SR, R, N
    private CharacterType type;           // RUNNER, POWER, STAMINA, GUTS, INTELLIGENCE
    
    // COMPOSITION (Stats object)
    private Stats stats;                  // Inner class with speed/stamina/power/guts/intel
    
    // COLLECTIONS (Track proficiencies)
    private List<TrackProficiency> proficiencies;
    
    // PUBLIC ENUM: Rarity
    public enum Rarity {
        N("Normal", 1), R("Rare", 2), SR("Super Rare", 3),
        SSR("Special Super Rare", 4), UR("Ultra Rare", 5);
        private String displayName;
        private int value;
    }
    
    // PUBLIC ENUM: CharacterType
    public enum CharacterType {
        RUNNER("Speed"), STAMINA("Stamina"), POWER("Power"),
        GUTS("Guts"), INTELLIGENCE("Intelligence");
        private String description;
    }
    
    // INNER CLASS: Stats (Composition)
    public static class Stats {
        private int speed;
        private int stamina;
        private int power;
        private int guts;
        private int intelligence;
        
        public int getTotal() {
            return speed + stamina + power + guts + intelligence;
        }
    }
}
```

**Used by:**
- `UmamusumeWikiScraper.scrapeCharacterStats()` → Returns `Umamusume`
- `UmamusumeWikiScraper.buildUmamusumeModel()` → Conversion logic
- `CharacterService.getCharacterAsUmamusume()` → High-level getter
- `WikiDataDisplay.displayWikiEnrichedData()` → Display input

**Conversion Path:**
```
UmapyoiCharacter + Wiki Data → buildUmamusumeModel() → Umamusume
```

---

### **3. Supporting Models**

#### **TrackProficiency**
```java
public class TrackProficiency {
    private TrackType trackType;          // TURF or DIRT
    private DistanceType distanceType;    // SPRINT, MILE, MEDIUM, LONG
    private Grade grade;                  // A, B, C, D, E, F, G (proficiency level)
    
    public enum TrackType { TURF, DIRT }
    public enum DistanceType { SPRINT, MILE, MEDIUM, LONG }
    public enum Grade { A, B, C, D, E, F, G }
}
```

**Used by:**
- `Umamusume.addProficiency()` → Stores track data
- `UmamusumeWikiScraper.buildUmamusumeModel()` → Creates from wiki data
- `WikiDataDisplay.displayWikiEnrichedData()` → Displays proficiencies

#### **CharacterImages**
- Stores character artwork URLs
- Used by: `CharacterService.getCharacterImages()`

#### **CharacterBirthday**
- Stores birthday information
- Used by: `CharacterService.getCurrentBirthdays()`

---

## 🔌 API Layer Interactions

### **UmapyoiApiClient (Direct HTTP Client)**

```java
public class UmapyoiApiClient {
    private final ApiConfig config;
    private final LocalCache cache;
    
    // PRIMARY METHOD
    public UmapyoiCharacter getCharacterById(int characterId) throws IOException {
        // 1. Check cache first
        if (cache.has(cacheKey)) {
            return cache.get(cacheKey, UmapyoiCharacter.class);  // ← Returns DTO
        }
        
        // 2. Make HTTP GET request
        String url = buildUrl("/character/all/ids");
        Request request = new Request.Builder().url(url).get().build();
        Response response = config.getHttpClient().newCall(request).execute();
        
        // 3. Parse JSON to UmapyoiCharacter
        String json = response.body().string();
        UmapyoiCharacter character = config.getObjectMapper()
            .readValue(json, UmapyoiCharacter.class);  // ← Jackson deserialization
        
        // 4. Cache and return
        cache.put(cacheKey, character, TimeUnit.HOURS.toMillis(24));
        return character;
    }
    
    // RESPONSE EXAMPLE:
    // {
    //   "id": 4737,
    //   "game_id": 1002,
    //   "name_en": "Silence Suzuka",
    //   "name_jp": "サイレンススズカ",
    //   "height": 168,
    //   "category_label": "Speed",
    //   ...
    // }
}
```

**Returns:** `UmapyoiCharacter` (API DTO Model)

---

### **CharacterService (Service Layer)**

```java
public class CharacterService {
    private final ApiConfig config;
    private final LocalCache cache;
    
    // STEP 1: Fetch from API
    public UmapyoiCharacter getCharacterById(int characterId) throws IOException {
        String cacheKey = "character_" + characterId;
        
        // Check cache
        if (cache.has(cacheKey)) {
            return cache.get(cacheKey, UmapyoiCharacter.class);
        }
        
        // Fetch from API
        String url = config.getBaseUrl() + "/api/v1/character/" + characterId;
        Request request = new Request.Builder().url(url).build();
        
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            String json = response.body().string();
            UmapyoiCharacter character = config.getObjectMapper()
                .readValue(json, UmapyoiCharacter.class);  // ← UmapyoiCharacter
            
            // STEP 2: ENRICH WITH WIKI DATA
            enrichCharacterWithWikiData(character);  // ← Adds wiki data to DTO
            
            // Cache and return
            cache.put(cacheKey, character, TimeUnit.HOURS.toMillis(24));
            return character;
        }
    }
    
    // STEP 3: Convert to Domain Model
    public Umamusume getCharacterAsUmamusume(int characterId) throws IOException {
        UmapyoiCharacter apiCharacter = getCharacterById(characterId);
        // Convert DTO → Domain Model
        return UmamusumeWikiScraper.enrichCharacterData(apiCharacter);  // ← Umamusume
    }
    
    // STEP 2 HELPER: Enrich API data with wiki data
    private void enrichCharacterWithWikiData(UmapyoiCharacter character) {
        try {
            // Scrape wiki for missing data
            Umamusume wikiData = UmamusumeWikiScraper.scrapeCharacterStats(
                character.getNameEnglish()
            );
            
            // Merge wiki data into API DTO
            if (wikiData.getStats() != null) {
                character.setWikiStats(wikiData.getStats());  // Add to DTO
            }
            if (wikiData.getRarity() != null) {
                character.setWikiRarity(wikiData.getRarity());
            }
            // ... more fields
            
            logger.info("Enriched character with wiki data");
        } catch (Exception e) {
            logger.warn("Wiki enrichment failed, continuing with API data only");
        }
    }
}
```

**Flow:**
```
1. getCharacterById() → Fetch UmapyoiCharacter from API
2. enrichCharacterWithWikiData() → Add wiki data to DTO
3. getCharacterAsUmamusume() → Convert to Umamusume domain model
```

---

## 🌐 Wiki Scraper Interactions

### **UmamusumeWikiScraper (HTML → Model Conversion)**

```java
public class UmamusumeWikiScraper {
    
    // ENTRY POINT 1: From character name (direct scraping)
    public static Umamusume scrapeCharacterStats(String characterName) {
        Umamusume character = new Umamusume();
        character.setName(characterName);
        
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Fetch HTML from wiki
            String wikiUrl = buildWikiUrl(characterName);
            Document doc = Jsoup.connect(wikiUrl)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .get();  // ← HTML document
            
            // Extract data from HTML
            extractStats(doc, stats);              // Profile + game stats
            extractSkills(doc, stats);             // Songs/abilities
            extractBiography(doc, stats);          // Character bio
            extractRelationships(doc, stats);      // Related characters
            extractTrackType(doc, stats);          // Turf/Dirt
            extractRarity(doc, stats);             // Rarity enum
            extractType(doc, stats);               // Type enum
            
            // Build domain model from extracted data
            character = buildUmamusumeModel(characterName, stats);
            
            return character;  // ← Returns Umamusume
        } catch (IOException e) {
            logger.warn("Wiki scraping failed: {}", e.getMessage());
        }
        
        return character;
    }
    
    // ENTRY POINT 2: From API model (conversion + enrichment)
    public static Umamusume enrichCharacterData(UmapyoiCharacter apiCharacter) {
        // Start with API data
        String characterName = apiCharacter.getNameEnglish();
        
        // Scrape wiki for additional/missing data
        Umamusume enriched = scrapeCharacterStats(characterName);
        
        // Merge in API data that wiki doesn't have
        if (apiCharacter.getHeight() > 0) {
            enriched.setHeight(apiCharacter.getHeight());
        }
        if (apiCharacter.getProfile() != null) {
            enriched.setProfile(apiCharacter.getProfile());
        }
        
        return enriched;  // ← Returns enriched Umamusume
    }
    
    // BUILDER: Convert raw extraction → Domain model
    private static Umamusume buildUmamusumeModel(
        String characterName, 
        Map<String, Object> stats
    ) {
        Umamusume character = new Umamusume();
        character.setName(characterName);
        
        // Build Stats object from extracted values
        Umamusume.Stats modelStats = new Umamusume.Stats();
        modelStats.setSpeed((Integer) stats.get("speed"));
        modelStats.setStamina((Integer) stats.get("stamina"));
        // ... more stats
        character.setStats(modelStats);
        
        // Set Rarity enum
        String rarityStr = (String) stats.get("rarity");
        character.setRarity(Umamusume.Rarity.valueOf(rarityStr));
        
        // Set CharacterType enum
        String typeStr = (String) stats.get("type");
        character.setType(mapTypeToCharacterType(typeStr));
        
        // Build TrackProficiency
        if (stats.containsKey("trackType")) {
            TrackProficiency prof = new TrackProficiency(
                TrackProficiency.TrackType.valueOf((String) stats.get("trackType")),
                TrackProficiency.DistanceType.MILE,
                TrackProficiency.Grade.A
            );
            character.addProficiency(prof);
        }
        
        return character;  // ← Fully constructed Umamusume
    }
}
```

**HTML Structure Parsed:**
```html
<!-- Infobox table for profile data -->
<table class="infobox">
  <tr><th>Birthday</th><td>April 30</td></tr>
  <tr><th>Height</th><td>165cm</td></tr>
  <tr><th>Voice Actor</th><td>Hina Suzuki</td></tr>
</table>

<!-- Biography section -->
<h2>Biography</h2>
<p>Character description paragraph...</p>

<!-- Stats table -->
<table>
  <tr><td>Speed</td><td>850</td></tr>
  <tr><td>Stamina</td><td>800</td></tr>
</table>

<!-- Track specialty -->
<span class="track-turf">Turf</span>
<span class="track-dirt">Dirt</span>

<!-- Rarity indicators -->
<span class="rarity-ur">UR</span>
```

**Extraction Methods:**
```
extractStats()         → Parses infobox table → Map<"speed", 850>
extractSkills()        → Parses skill tables → Map<"skills", List>
extractBiography()     → Finds <h2>Biography</h2> → Map<"biography", text>
extractRelationships() → Finds <h2>Relationships</h2> → Map<"relationships", List>
extractTrackType()     → Searches for Turf/Dirt indicators → Map<"trackType", "TURF">
extractRarity()        → Finds UR/SSR/SR/R/N → Map<"rarity", "UR">
extractType()          → Identifies type from page → Map<"type", "RUNNER">
```

---

## 📱 Display Layer Interactions

### **WikiDataDisplay (Model → Console Output)**

```java
public class WikiDataDisplay {
    
    // Takes Umamusume domain model and displays it
    public static void displayWikiEnrichedData(Umamusume character) {
        if (character == null) {
            ConsoleUI.displayError("Character data not available");
            return;
        }
        
        // Access model fields via getters (type-safe)
        Umamusume.Stats stats = character.getStats();
        if (stats != null) {
            System.out.println("⚡ Speed: " + stats.getSpeed());
            System.out.println("❤️  Stamina: " + stats.getStamina());
            System.out.println("💪 Power: " + stats.getPower());
            System.out.println("🔥 Guts: " + stats.getGuts());
            System.out.println("🧠 Intelligence: " + stats.getIntelligence());
            System.out.println("📊 Total: " + stats.getTotal());
        }
        
        // Display enum values (prettier)
        if (character.getRarity() != null) {
            System.out.println("✨ Rarity: " + 
                character.getRarity().getDisplayName());  // ← Enum method
        }
        
        if (character.getType() != null) {
            System.out.println("🎯 Type: " + 
                character.getType().getDescription());    // ← Enum method
        }
        
        // Display proficiencies (collections)
        for (TrackProficiency prof : character.getProficiencies()) {
            System.out.println("🏇 " + prof.toString());
        }
    }
    
    // Overload: Takes API DTO, converts to domain model
    public static void displayWikiEnrichedData(UmapyoiCharacter apiCharacter) {
        // Convert API DTO → Domain model
        Umamusume character = UmamusumeWikiScraper.enrichCharacterData(apiCharacter);
        
        // Display using domain model
        displayWikiEnrichedData(character);
    }
}
```

**Output Example:**
```
📚 WIKI ENRICHED DATA FOR ADMIRE GROOVE

⚡ Speed: 850
❤️  Stamina: 800
💪 Power: 750
🔥 Guts: 850
🧠 Intelligence: 700
📊 Total: 3950

🏇 TRACK PROFICIENCIES:
  • Turf Mile: A

✨ Rarity: Ultra Rare (UR)
🎯 Type: Speed
```

---

## 🔄 Complete Interaction Example

### **Scenario: User searches for "Admire Groove"**

```
Step 1: User Input
├─ ChesedSephirah.searchCharacterMenu()
├─ User enters: "Admire Groove"
└─ Calls: CharacterService.searchCharactersByName("Admire Groove")

Step 2: API Fetch (CharacterService)
├─ searchCharactersByName("admire groove")
├─ Loops through character IDs (1001-1400)
├─ Calls: UmapyoiApiClient.getCharacterById(ID)
├─ ↓ Returns: UmapyoiCharacter (from JSON)
│  ├─ apiId: 4737
│  ├─ nameEnglish: "Admire Groove"
│  ├─ height: 165
│  ├─ categoryLabel: "Speed"
│  └─ ... 50 more fields
│
├─ Enrichment: enrichCharacterWithWikiData(UmapyoiCharacter)
│  ├─ Calls: UmamusumeWikiScraper.scrapeCharacterStats("Admire Groove")
│  └─ Adds wiki-extracted data to DTO
│
└─ Returns: UmapyoiCharacter (enriched with wiki data)

Step 3: Display (WikiDataDisplay)
├─ User selects character from results
├─ Calls: displayWikiEnrichedData(UmapyoiCharacter)
│
├─ Conversion inside display:
│  ├─ UmamusumeWikiScraper.enrichCharacterData(UmapyoiCharacter)
│  │  ├─ Scrapes wiki again for fresh data
│  │  └─ Returns: Umamusume (domain model)
│  └─ Displays: Umamusume.getStats(), Umamusume.getRarity(), etc.
│
└─ Console Output:
   ├─ ⚡ Speed: 850
   ├─ ✨ Rarity: Ultra Rare (UR)
   ├─ 🎯 Type: Speed
   └─ ... formatted data

Step 4: Sorting/Analysis (Optional)
├─ User can sort results using UmamusumeSorter
├─ Uses: Comparators.bySpeed(), byRarity(), etc.
├─ Comparators work with: Umamusume domain model
└─ Returns: Sorted List<Umamusume>
```

---

## 🎯 Key Interactions Summary

| Interaction | Source | Target | Purpose |
|---|---|---|---|
| **API → DTO** | REST API (JSON) | `UmapyoiCharacter` | Parse API response |
| **DTO → Wiki** | `UmapyoiCharacter` | `UmamusumeWikiScraper` | Input for enrichment |
| **Wiki → Domain** | HTML + DTO | `Umamusume` | Build typed model |
| **Domain → Display** | `Umamusume` | `WikiDataDisplay` | Format for output |
| **Domain → Sorting** | `Umamusume` | `UmamusumeSorter` | Sort operations |
| **Domain → Factory** | `Umamusume` | `UmaFactory` | Create polymorphic types |
| **Enum Access** | `Umamusume` | Comparators | Type-safe filtering |

---

## 🏗️ Architecture Layers

### **Layer 1: External Data (outside our code)**
- REST API: `umapyoi.net/api/v1/...`
- Wiki: `umamusu.wiki/Character_Name`

### **Layer 2: HTTP & Data Fetching**
- `UmapyoiApiClient` - Makes HTTP requests, returns `UmapyoiCharacter`
- `UmamusumeWikiScraper` - Parses HTML, returns `Umamusume`
- `LocalCache` - Caches both DTOs and domain models

### **Layer 3: Service Layer**
- `CharacterService` - Orchestrates API + Wiki
- `ApiConfig` - Configuration and HTTP client setup

### **Layer 4: Model/Data Layer**
- `UmapyoiCharacter` (DTO) - Direct API mapping
- `Umamusume` (Domain Model) - Type-safe with enums
- Supporting models: `TrackProficiency`, `Stats`, enums

### **Layer 5: Business Logic & Sorting**
- `UmamusumeSorter` - Sorts domain models
- `Comparators` - Comparison strategies
- `UmaFactory` - Creates polymorphic types

### **Layer 6: Display & UI**
- `WikiDataDisplay` - Formats for console output
- `ChesedSephirah` - Menu controller

---

## 🔐 Data Flow Summary

**Direction:** External → HTTP → DTO → Enrichment → Domain Model → Display

**Key Points:**
1. **API data** enters as `UmapyoiCharacter` (raw DTO)
2. **Wiki data** enriches the DTO with missing fields
3. **Domain model** (`Umamusume`) provides type safety with enums
4. **Display layer** reads from domain model, not DTO
5. **Sorting & analysis** works with domain model for type safety

This architecture ensures:
- ✅ Clean separation of concerns
- ✅ Type safety through enums
- ✅ Fallback mechanism (API + Wiki)
- ✅ Reusable conversion methods
- ✅ Testable layers
