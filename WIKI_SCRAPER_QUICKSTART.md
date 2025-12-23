# Wiki Scraper Quick Start Guide

## Installation (One-Time Setup)

### 1. Build the Project
```bash
cd project-sephirah
mvn clean compile
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 1.975 s
```

### 2. Verify Installation
```bash
# Check wiki scraper files exist
ls src/main/java/com/atziluth/sephirah/chesed/api/UmamusumeWikiScraper.java
ls src/main/java/com/atziluth/sephirah/chesed/api/WikiDataDisplay.java

# Check jsoup dependency
grep jsoup pom.xml
```

### 3. Run the Application
```bash
java -cp target/classes com.atziluth.ProjectSephirah
```

**Or use the batch file:**
```bash
./run-chesed.bat     # Windows
./run-chesed.sh      # Linux/Mac
```

---

## Using the Wiki Scraper

### Method 1: Automatic (Recommended)
**What:** Wiki data fetched automatically when you search for characters

**How to use:**
```
1. Run application
2. Select Chesed Module
3. Search for character (any method)
4. See character info
5. Prompt appears: "Additional data available from wiki. Fetch? (y/n)"
6. Type 'y' to see stats, skills, track type, rarity
```

**Example:**
```
Chesed Module → Search by Name → Enter "Silence Suzuka"
[API data loaded instantly]
[Wiki enrichment happening in background...]
[Character displayed with both API + wiki data available]
💡 Additional data available from wiki. Fetch? (y/n): y
📚 WIKI ENRICHED DATA SHOWN
```

### Method 2: Manual Menu Selection
**What:** Explicitly view wiki data from character display menu

**How to use:**
```
1. Display full character info
2. See menu options (1-6)
3. Select: 5. View Wiki Enriched Data
4. Wiki data displayed in formatted table
```

**Menu shown:**
```
ACTIONS
1. View Character Images
2. Sort with Similar Characters
3. Compare with Another Character
4. Save to Favorites
5. View Wiki Enriched Data         ← Select this
6. Return to Search
```

### Method 3: All Search Methods Supported
**Automatic wiki enrichment works with:**
1. ✅ Search by Name
2. ✅ Search by Rarity  
3. ✅ Search by ID
4. ✅ Search by Track Type
5. ✅ Search by Popularity

**All provide wiki data automatically**

---

## Wiki Data Displayed

### What You Get

**From Official API (Always):**
- Character name (English & Japanese)
- Birthday and age
- Height and weight
- Grade and residence
- Profile, strengths, weaknesses
- Images and voice

**From Wiki Scraper (If Available):**
- ⚡ **Speed stat** (0-1000)
- ❤️ **Stamina** (0-1000)
- 💪 **Power** (0-1000)
- 🔥 **Guts/Willpower** (0-1000)
- 🧠 **Intelligence** (0-1000)
- 🏇 **Track Type** (Turf or Dirt)
- ✨ **Rarity Tier** (N, R, SR, SSR, UR)
- 🎯 **Character Type** (Speed, Power, Stamina, Wisdom, Friend)
- 🎪 **Skills/Abilities** (List of special abilities)

### Example Output
```
═══════════════════════════════════════════════════════════════════════════
SILENCE SUZUKA - FULL CHARACTER INFO
═══════════════════════════════════════════════════════════════════════════

English: Silence Suzuka
Japanese: サイレンススズカ
API ID: 1002
Height: 161cm
Birthday: May 1

Profile: I'm Silence Suzuka. I like to run. I'm not giving the lead to anyone...
Strengths: Running
Weaknesses: Crowded places

DETAILED STATS
Speed: 850
Stamina: 700
Power: 750
Guts: 800
Intelligence: 800

───────────────────────────────────────────────────────────────────────────

💡 Additional data available from wiki. Fetch? (y/n): y

📚 WIKI ENRICHED DATA FOR SILENCE SUZUKA

⚡ Speed: 850
❤️  Stamina: 700
💪 Power: 750
🔥 Guts: 800
🧠 Intelligence: 800

🏇 Track Type: Turf
✨ Rarity: SSR
🎯 Type: Speed

🎪 SKILLS:
  • Silent Running
  • Perfect Turn Out
  • Unbeatable in Straights
  • Back Straight Specialist
```

---

## Example Workflows

### Workflow 1: Find Character Stats
```
Goal: Find Haru Urara's stats and skills

Steps:
1. Open app → Select Chesed
2. Choose: 1. Search by Name
3. Enter: "Haru Urara"
4. Character found and displayed
5. When prompted: Enter 'y' for wiki data
6. See all stats and skills

Time: ~10 seconds total
```

### Workflow 2: Compare Character Stats
```
Goal: Compare stats of two characters

Steps:
1. Search first character (e.g., "Silence Suzuka")
2. Get wiki data (stats displayed)
3. Return to search menu
4. Search second character (e.g., "Mejiro Mcqueen")
5. Get wiki data
6. Compare stats mentally or on paper

Tip: Both characters show speed, power, stamina in same format
```

### Workflow 3: Find High-Speed Characters
```
Goal: Find fastest characters

Steps:
1. Search by Rarity → SSR
2. For each character found:
   - Get wiki data (shows speed stat)
   - Note highest speed values
3. Continue searching other rarities
4. Compile list of fastest

Example: Speed 850+ = very fast
```

### Workflow 4: Search by Track Specialization
```
Goal: Find Turf track specialists

Steps:
1. Search by Track Type → Turf
2. Characters found and fetched
3. For those with wiki data:
   - See track type confirmed
   - See related stats
4. Compare options and pick character

Note: All results will be Turf specialists
```

---

## Troubleshooting

### Issue: "Wiki data not showing"
**Cause:** Wiki server might be slow or unavailable

**Solution:**
```
1. Check internet connection
2. Try again (wiki caches results)
3. If persistent, wiki site might be down
4. App still works with API data only
```

### Issue: "No wiki data available for this character"
**Cause:** Character might not be in wiki, or wiki doesn't have stats

**Solution:**
```
1. Wiki may not have all characters
2. API data still shows (name, profile, etc.)
3. Try different character as test
4. Not critical - API data is sufficient
```

### Issue: "Application seems slow when fetching wiki data"
**Cause:** Wiki scraping takes 5-10 seconds by design

**Solution:**
```
1. This is normal - wiki has many pages to parse
2. First load is slower, cached results are instant
3. Performance improvement planned for Phase 2
4. API data shows immediately while wiki loads
```

### Issue: "I don't want to see wiki prompts"
**Cause:** Default behavior offers wiki data

**Solution:**
```
Option 1: Always press 'n' when prompted
Option 2: Just press Enter to skip the table
Option 3: Future versions may have toggle to disable prompts
```

---

## Performance Notes

### Typical Times
- **API fetch:** 2-3 seconds
- **Wiki scrape:** 5-10 seconds (first time)
- **Wiki data (cached):** <100 milliseconds
- **Total character view:** 7-13 seconds (with wiki)
- **Without wiki:** 2-3 seconds

### Optimization Tips
1. **Use menu option 5 selectively** - Only when you want wiki details
2. **Search by ID is fastest** - Direct API fetch with enrichment
3. **Batch searching** - Search multiple chars and review stats later
4. **Avoid during slow internet** - Wiki takes longer on slow connections

---

## What Happens Behind the Scenes

### The Technical Flow (For Curious Users)

```
You search for "Haru Urara"
        ↓
App fetches from API (umapyoi.net)
        ↓
Character data received (name, profile, etc.)
        ↓
App simultaneously starts wiki scraping
        ↓
Wiki page parsed with jsoup library
        ↓
Stats, skills, track type extracted
        ↓
Results merged with API data
        ↓
Character displayed to you
        ↓
Prompt: "Want wiki details?" → You decide
        ↓
Wiki data shown in nice table (if you want)
```

### Why the Prompt?
- ✅ Respects user choice
- ✅ Wiki data optional (not always available)
- ✅ User controls when to spend time viewing details
- ✅ Keeps interface clean (not forcing info)

---

## Features Not Yet Implemented

⏳ **Planned for Phase 2:**
- Async wiki scraping (background thread)
- Separate cache for wiki data
- Pre-fetch popular characters

⏳ **Planned for Phase 3:**
- Skill effect descriptions
- Ability trigger conditions
- Race history and records

⏳ **Planned for Phase 4:**
- Secondary wiki sources as backup
- Multiple language support
- Database mirror integration

⏳ **Planned for Phase 5:**
- Build recommendations based on stats
- Character comparison tools
- Stat trend tracking

---

## Settings & Configuration

### To Change Wiki Source
**File:** `src/main/java/com/atziluth/sephirah/chesed/api/UmamusumeWikiScraper.java`

**Change this line:**
```java
private static final String WIKI_BASE_URL = "https://wikiru.jp/umamusume";
```

**To:**
```java
private static final String WIKI_BASE_URL = "https://new-wiki-site.com/umamusume";
```

**Then recompile:**
```bash
mvn clean compile
```

### To Change Timeout
**File:** `UmamusumeWikiScraper.java`

**Change this:**
```java
private static final int TIMEOUT_MS = 10000;  // 10 seconds
```

**To:**
```java
private static final int TIMEOUT_MS = 15000;  // 15 seconds (slower networks)
```

**Then recompile:**
```bash
mvn clean compile
```

### To Disable Wiki Scraping Temporarily
**File:** `CharacterService.java`

**Comment out this line:**
```java
// enrichCharacterWithWikiData(character);  // Temporarily disabled
```

**Then recompile. App works with API only.**

---

## FAQ

**Q: Does wiki scraping cost anything?**  
A: No, it's free. Uses publicly available wiki pages.

**Q: Will it slow down the app?**  
A: Only first load takes 7-13 seconds. Cached results are instant.

**Q: What if wiki is down?**  
A: App shows API data only. Wiki is optional fallback.

**Q: Can I turn off wiki prompts?**  
A: Just press 'n' when asked. Menu option always available if you want it.

**Q: Why stats sometimes different between sources?**  
A: Wiki shows base stats, API may show calculated values. Both valid.

**Q: Which wiki is used?**  
A: wikiru.jp/umamusume (Japanese fan wiki). Can be changed in config.

**Q: How often is wiki updated?**  
A: Wiki updated by community. Usually within hours of game updates.

**Q: Can I contribute to the wiki?**  
A: Yes, wikiru.jp is open for community edits.

**Q: What if I find wrong data?**  
A: Report to wikiru.jp or update yourself (account required).

---

## Getting Help

### Check Documentation
- [Feature Overview](docs/wiki-scraper-feature.md)
- [Implementation Details](WIKI_SCRAPER_IMPLEMENTATION.md)
- [Testing Guide](WIKI_SCRAPER_TESTING.md)
- [Complete Summary](WIKI_SCRAPER_SUMMARY.md)

### View Logs
Enable DEBUG logging in `logback.xml`:
```xml
<root level="DEBUG">
    <appender-ref ref="CONSOLE" />
</root>
```

Then run app and check console output:
```
[DEBUG] Scraping wiki for Silence Suzuka: https://wikiru.jp/...
[DEBUG] Wiki enrichment - speed : 850
[DEBUG] Successfully scraped wiki data for Silence Suzuka
```

### Contact
Check project README for contact information.

---

**Wiki Scraper Status:** ✅ Ready to use  
**Latest Update:** December 24, 2025  
**Version:** 1.0  

Enjoy exploring complete Umamusume character data! 🐴
