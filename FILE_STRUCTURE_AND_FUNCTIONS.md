# Project Sephirah - Complete File Structure & Functions

## 📂 Directory Hierarchy

```
project-sephirah/
├── src/main/java/com/atziluth/
│   ├── ProjectSephirah.java              [MAIN ENTRY POINT]
│   ├── core/
│   │   ├── Sephirah.java                 [INTERFACE]
│   │   ├── SubjectRegistry.java          [REGISTRY PATTERN]
│   │   └── PluginLoader.java             [PLUGIN SYSTEM]
│   └── sephirah/chesed/                  [MODULE: OOP & ALGORITHMS]
│       ├── ChesedSephirah.java           [MODULE IMPLEMENTATION]
│       ├── api/                          [API INTEGRATION LAYER]
│       │   ├── ApiConfig.java            [SINGLETON CONFIG]
│       │   ├── UmapyoiApiClient.java     [HTTP CLIENT]
│       │   ├── CharacterService.java     [SERVICE LAYER]
│       │   ├── LocalCache.java           [CACHING LAYER]
│       │   ├── ApiSimpleTest.java        [BASIC TESTS]
│       │   └── ApiTestSuite.java         [COMPREHENSIVE TESTS]
│       ├── model/                        [DATA MODELS]
│       │   ├── AbstractUma.java          [BASE CLASS - INHERITANCE]
│       │   ├── Umamusume.java            [MAIN MODEL - COMPOSITION]
│       │   ├── UmapyoiCharacter.java     [API DTO]
│       │   ├── SpeedUma.java             [CONCRETE IMPL 1]
│       │   ├── StaminaUma.java           [CONCRETE IMPL 2]
│       │   ├── UniqueSkillUma.java       [CONCRETE IMPL 3]
│       │   ├── TrackProficiency.java     [PROFICIENCY MODEL]
│       │   ├── TrackType.java            [ENUM]
│       │   ├── DistanceType.java         [ENUM]
│       │   ├── SpecialAbility.java       [ENUM]
│       │   ├── CharacterBasic.java       [BASIC INFO]
│       │   ├── CharacterBirthday.java    [BIRTHDAY INFO]
│       │   └── CharacterImages.java      [IMAGE DATA]
│       ├── sorting/                      [SORTING & SEARCHING]
│       │   ├── UmamusumeSorter.java      [MERGE SORT - O(n log n)]
│       │   ├── UmapyoiEnhancedSorter.java [ADVANCED SORTING]
│       │   └── Comparators.java          [STRATEGY PATTERN]
│       ├── factory/                      [DESIGN PATTERNS]
│       │   └── UmaFactory.java           [FACTORY PATTERN]
│       ├── util/                         [UTILITIES]
│       │   ├── DataGenerator.java        [MOCK DATA]
│       │   └── JSONHandler.java          [JSON PROCESSING]
│       ├── algorithms/                   [ALGORITHMS PLACEHOLDER]
│       ├── db/                           [DATABASE PLACEHOLDER]
│       ├── demo/                         [DEMONSTRATIONS]
│       │   ├── DemoOOPConcepts.java      [ENCAPSULATION, INHERITANCE, POLYMORPHISM]
│       │   ├── InheritanceDemo.java      [INHERITANCE EXAMPLES]
│       │   ├── PolymorphismDemo.java     [POLYMORPHISM EXAMPLES]
│       │   ├── ApiDataProcessingDemo.java [API INTEGRATION DEMO]
│       │   └── ComparatorsDemo.java      [SORTING DEMO]
│       └── searching/                    [SEARCH ALGORITHMS PLACEHOLDER]
└── pom.xml                               [MAVEN BUILD CONFIG]
```

---

## 📋 File Functions & Methods

### 🎯 ENTRY POINT & CORE

#### **ProjectSephirah.java** (Main Entry Point)
- **Purpose**: Application entry point and main menu system
- **Key Methods**:
  - `main(String[] args)` → Starts application
  - `run()` → Main loop with menu system
  - `initialize()` → Registers Sephirah modules
  - `displayMainMenu()` → Shows menu options
  - `runSephirah(String name)` → Executes specific module
  - `displayAbout()` → Shows project information

#### **Sephirah.java** (Interface)
- **Purpose**: Interface for modular framework
- **Methods**:
  - `getName()` → Returns module name
  - `getConcept()` → Returns concept description
  - `getDescription()` → Returns detailed description
  - `initialize()` → Module initialization
  - `demonstrate()` → Module demonstration

#### **SubjectRegistry.java** (Registry Pattern)
- **Purpose**: Central registry for modules
- **Methods**:
  - `register(String key, Sephirah subject)`
  - `getSubject(String key)`
  - `getAll()`

#### **PluginLoader.java** (Plugin System)
- **Purpose**: Dynamic plugin loading
- **Methods**:
  - `loadPlugins(String path)`
  - `getPlugins()`

---

### 🎓 MODULE: CHESED (OOP & ALGORITHMS)

#### **ChesedSephirah.java** (Module Implementation)
- **Purpose**: Main Chesed module - coordinates OOP demonstrations
- **Key Methods**:
  - `getName()` → Returns "Chesed"
  - `getConcept()` → Returns "Object-Oriented Programming & Algorithms"
  - `initialize()` → Sets up character list
  - `demonstrate()` → Runs all demonstrations
  - `generateMockCharacters()` → Creates sample data
  - `demonstrateOOP()` → Shows OOP concepts
  - `demonstrateAlgorithms()` → Shows sorting/searching
  - `demonstrateDesignPatterns()` → Shows factory pattern

---

### 🌐 API INTEGRATION LAYER

#### **ApiConfig.java** (Singleton Configuration)
- **Purpose**: Centralized HTTP client and JSON serialization configuration
- **Pattern**: Singleton
- **Key Methods**:
  - `getInstance()` → Get singleton instance
  - `getOkHttpClient()` → Returns configured OkHttp3 client
  - `getObjectMapper()` → Returns configured Jackson ObjectMapper
  - `initializeObjectMapper()` → Sets up JSON parsing

**Configuration**:
- 30-second connection timeout
- 30-second read timeout
- 30-second write timeout
- Persistent connection pooling
- Custom HTTP headers (User-Agent, Accept)

#### **UmapyoiApiClient.java** (HTTP Client)
- **Purpose**: Low-level HTTP communication with Umapoei API
- **Key Methods**:
  - `getCharacter(int id)` → Fetch character by ID
  - `searchCharacter(String name)` → Search characters
  - `getCharacterAsync(int id)` → Async character fetch
  - `makeRequest(String url)` → Generic HTTP request
  - `retryRequest(String url, int maxRetries)` → Retry logic (3 attempts, 1s delay)
  - `parseResponse(String json)` → Parse JSON response

#### **CharacterService.java** (Service Layer)
- **Purpose**: High-level character operations with caching
- **Pattern**: Facade
- **Key Methods**:
  - `getCharacterById(int id)` → Get character with caching
  - `searchCharactersByName(String name)` → Search and cache
  - `getCharacterByIdAsync(int id)` → Async operation
  - `getPopularCharacters()` → Get trending characters
  - `getCharactersByRarity(String rarity)` → Filter by rarity
  - `getAllCharacters()` → Get all characters
  - `refreshCache()` → Clear cache

#### **LocalCache.java** (Caching Layer)
- **Purpose**: Thread-safe TTL-based cache
- **Pattern**: Decorator
- **Key Methods**:
  - `get(String key)` → Retrieve from cache
  - `put(String key, V value)` → Store with TTL (5 minutes default)
  - `put(String key, V value, long ttlMillis)` → Store with custom TTL
  - `clear()` → Clear all cache
  - `remove(String key)` → Remove specific entry
  - `getHitRate()` → Cache statistics
  - `expireOldEntries()` → Remove expired entries

#### **ApiSimpleTest.java** (Basic API Testing)
- **Purpose**: Simple API verification tests
- **Key Methods**:
  - `testApiConfiguration()` → Test API setup
  - `testDataGeneration()` → Test mock data
  - `runTests()` → Execute basic tests

#### **ApiTestSuite.java** (Comprehensive Testing)
- **Purpose**: Full API test suite
- **Test Methods**:
  - `testSingleCharacterFetch()` → Fetch single character
  - `testCharacterSearch()` → Search functionality
  - `testCaching()` → Cache behavior
  - `testAsyncOperations()` → Async operations
  - `testDataSerialization()` → JSON parsing
  - `testErrorHandling()` → Error scenarios
  - `runFullTestSuite()` → Execute all tests

---

### 📦 DATA MODELS

#### **AbstractUma.java** (Base Class - Inheritance)
- **Purpose**: Abstract parent for all Umamusume
- **Fields**:
  - `id: int`
  - `name: String`
  - `japaneseName: String`
  - `rarity: Umamusume.Rarity`
  - `stats: Umamusume.Stats`
- **Abstract Methods**:
  - `getCharacterType()` → Returns character type
  - `calculateSpecialPower()` → Calculates power value
  - `getSpecialty()` → Returns specialty string
- **Concrete Methods**:
  - `getFullName()` → Name + Japanese name
  - `getTotalStats()` → Sum of all stats

#### **Umamusume.java** (Main Model - Composition)
- **Purpose**: Core character model with nested classes
- **Inner Class: Stats**
  - Fields: `speed, stamina, power, guts, intelligence: int`
  - Methods: `getTotal()`, getters/setters
- **Inner Class: Rarity (Enum)**
  - Values: `UR, SSR, SR, R, N`
- **Inner Class: CharacterType (Enum)**
  - Values: `SPEED, STAMINA, POWER, GUTS, INTELLIGENCE`
- **Fields**:
  - `id, name, japaneseName: String`
  - `rarity: Rarity`
  - `stats: Stats`
  - `type: CharacterType`
  - `proficiencies: List<TrackProficiency>`
- **Methods**:
  - `getters/setters` for all fields
  - `addProficiency(TrackProficiency)`
  - `toString()`, `equals()`, `hashCode()`

#### **UmapyoiCharacter.java** (API DTO)
- **Purpose**: Data Transfer Object for API responses
- **Fields**:
  - `id, name, japaneseName: String`
  - `height, bust, waist, hip: int`
  - `birthday: CharacterBirthday`
  - `images: CharacterImages`
  - `gameOrder: int`
- **Methods**:
  - Getters/setters for all fields
  - `toString()`

#### **SpeedUma.java** (Concrete Implementation)
- **Purpose**: Demonstrates inheritance - Speed specialist
- **Overrides**:
  - `getCharacterType()` → Returns `SPEED`
  - `calculateSpecialPower()` → Formula based on speed stat
  - `getSpecialty()` → Returns "High-speed racing"

#### **StaminaUma.java** (Concrete Implementation)
- **Purpose**: Demonstrates inheritance - Stamina specialist
- **Overrides**:
  - `getCharacterType()` → Returns `STAMINA`
  - `calculateSpecialPower()` → Formula based on stamina stat
  - `getSpecialty()` → Returns "Long-distance endurance"

#### **UniqueSkillUma.java** (Concrete Implementation)
- **Purpose**: Demonstrates inheritance - Unique skill specialist
- **Overrides**:
  - `getCharacterType()` → Returns custom type
  - `calculateSpecialPower()` → Unique formula
  - `getSpecialty()` → Returns special skill name

#### **TrackProficiency.java** (Proficiency Model)
- **Purpose**: Track/distance proficiency data
- **Fields**:
  - `trackType: TrackType`
  - `proficiency: int (0-100)`
  - `distance: DistanceType`

#### **TrackType.java** (Enum)
- **Values**: `TURF, DIRT, SHORT, MILE, MEDIUM, LONG`

#### **DistanceType.java** (Enum)
- **Values**: `SHORT_800M, MEDIUM_1200M, LONG_2000M`

#### **SpecialAbility.java** (Enum)
- **Values**: `ACCELERATION, ENDURANCE, POWER_BURST, INTUITION`

#### **CharacterBasic.java** (Basic Info)
- **Fields**: ID, name, Japanese name, basic stats

#### **CharacterBirthday.java** (Birthday Info)
- **Fields**: `month: int`, `day: int`
- **Methods**: `getMonthName()`, `getDayOfYear()`

#### **CharacterImages.java** (Image Data)
- **Fields**: `profileUrl, cardUrl, illustratorName: String`
- **Methods**: Getters/setters

---

### 🔧 SORTING & SEARCHING

#### **UmamusumeSorter.java** (Merge Sort - O(n log n))
- **Purpose**: Generic merge sort implementation
- **Key Method**:
  - `mergeSort(List<T> list, Comparator<T> comparator)` → Main sort method
  - `merge(List<T> left, List<T> right, Comparator<T> cmp)` → Merge operation
  - `getComplexity()` → Returns "O(n log n)"

**Algorithm**: Divide-and-conquer merge sort
- Stable: Yes (maintains order of equal elements)
- In-place: No (requires O(n) extra space)

#### **UmapyoiEnhancedSorter.java** (Advanced Sorting)
- **Purpose**: API-aware sorting with grouping
- **Key Methods**:
  - `groupByRarity(List<Umamusume>)` → Sort by rarity
  - `groupByType(List<AbstractUma>)` → Sort by character type
  - `groupByHeight(List<UmapyoiCharacter>)` → Sort by height
  - `getSortedByMultipleCriteria(...)` → Complex multi-level sort
  - `getTopNCharacters(List, int, Comparator)` → Get top N

#### **Comparators.java** (Strategy Pattern)
- **Purpose**: Reusable Comparator implementations
- **Umamusume Comparators**:
  - `byTotalStats()` → Descending by total stats
  - `bySpeed()`, `byStamina()`, `byPower()`, `byGuts()`, `byIntelligence()` → Individual stats
  - `byName()` → Alphabetical by English name
  - `byJapaneseName()` → Alphabetical by Japanese name
  - `byRarity()` → By rarity level (UR → N)
  - `byType()` → By character type
  - `byRarityThenStats()` → Rarity then stats
  - `byTypeThenName()` → Type then name
  - `byRarityTypeThenStats()` → Complex multi-level
- **AbstractUma Comparators**:
  - `umaByName()` → Sort any Uma by name
  - `umaByTotalStats()` → Sort any Uma by stats
  - `umaBySpecialPower()` → Sort by power value
  - `umaByType()` → Sort by character type
- **UmapyoiCharacter Comparators**:
  - `characterByName()` → English name
  - `characterByHeight()` → Height descending
  - `characterByBust()` → Bust descending
  - `characterByGameOrder()` → Game appearance order
- **Utility Methods**:
  - `reverse(Comparator<T>)` → Reverse any comparator
  - `chain(Comparator<T>...)` → Chain multiple comparators
  - `nullsFirst(Comparator<T>)` → Null-safe sort
  - `nullsLast(Comparator<T>)` → Null-safe sort

---

### 🏭 DESIGN PATTERNS

#### **UmaFactory.java** (Factory Pattern)
- **Purpose**: Create different character types
- **Key Method**:
  - `createUma(String type)` → Factory method
  - Returns: `AbstractUma` (SpeedUma, StaminaUma, etc.)
- **Supported Types**: SPEED, STAMINA, POWER, GUTS, INTELLIGENCE, UNIQUE_SKILL

---

### 🛠️ UTILITIES

#### **DataGenerator.java** (Mock Data Generation)
- **Purpose**: Generate sample character data
- **Key Methods**:
  - `generateRandomUmamusume()` → Single random character
  - `generateUmamusumeList(int count)` → Multiple characters
  - `generateRandomStats()` → Random stats
  - `generateRandomRarity()` → Random rarity
  - `generateRandomType()` → Random type
  - `generateRandomProficiencies()` → Proficiencies

#### **JSONHandler.java** (JSON Processing)
- **Purpose**: JSON serialization/deserialization
- **Key Methods**:
  - `toJson(Object obj)` → Object → JSON
  - `fromJson(String json, Class<T> type)` → JSON → Object
  - `toJsonFile(Object obj, String filepath)` → Write to file
  - `fromJsonFile(String filepath, Class<T> type)` → Read from file
  - `prettyPrint(String json)` → Format JSON

---

### 🎓 DEMONSTRATIONS

#### **DemoOOPConcepts.java** (OOP Concepts)
- **Purpose**: Educational demonstration of OOP principles
- **Demonstrated Concepts**:
  - **Encapsulation**: Private fields, public getters/setters
  - **Inheritance**: AbstractUma → SpeedUma, StaminaUma
  - **Polymorphism**: Override abstract methods
  - **Abstraction**: Abstract methods and classes
- **Methods**:
  - `demonstrateEncapsulation()` → Shows data hiding
  - `demonstrateInheritance()` → Shows class hierarchies
  - `demonstratePolymorphism()` → Shows runtime dispatch
  - `demonstrateAbstraction()` → Shows abstract types

#### **InheritanceDemo.java** (Inheritance Examples)
- **Purpose**: Show inheritance relationships
- **Methods**:
  - `demonstrateClassHierarchy()` → Display class tree
  - `demonstrateMethodOverriding()` → Show override behavior
  - `demonstrateParameterizedTypes()` → Generic types
  - `demonstrateSuper()` → Call parent methods

#### **PolymorphismDemo.java** (Polymorphism Examples)
- **Purpose**: Show runtime polymorphic behavior
- **Methods**:
  - `demonstrateMethodDispatch()` → Runtime dispatch
  - `demonstrateInterfacePolymorphism()` → Interface types
  - `demonstrateCollectionPolymorphism()` → Mixed collections
  - `demonstrateCallback()` → Functional callbacks

#### **ApiDataProcessingDemo.java** (API Integration Demo)
- **Purpose**: Show API integration with algorithms
- **Methods**:
  - `fetchCharacters()` → Get data from API
  - `processCharacters()` → Apply algorithms
  - `analyzeCharacters()` → Generate statistics
  - `displayResults()` → Show formatted output

#### **ComparatorsDemo.java** (Sorting Demo)
- **Purpose**: Demonstrate all Comparator implementations
- **Methods**:
  - `demonstrate()` → Main entry point
  - `demonstrateBasicComparators()` → Simple sorts
  - `demonstrateStatComparators()` → Stat-based sorts
  - `demonstrateChainedComparators()` → Multi-level sorts
  - `demonstrateAbstractUmaComparators()` → Polymorphic sorts

---

## 📊 Design Patterns Summary

| Pattern | File | Purpose |
|---------|------|---------|
| **Singleton** | ApiConfig.java | Single HTTP client instance |
| **Factory** | UmaFactory.java | Create different Uma types |
| **Facade** | CharacterService.java | Simplify API interactions |
| **Strategy** | Comparators.java | Pluggable sorting strategies |
| **Decorator** | LocalCache.java | Add caching to service |
| **Adapter** | UmapyoiCharacter.java | Convert API response to model |
| **Template Method** | AbstractUma.java | Define algorithm skeleton |
| **Registry** | SubjectRegistry.java | Central module registry |

---

## 🔄 Data Flow

```
ProjectSephirah (Main)
    ↓
ChesedSephirah (Module)
    ↓
┌───────────────────────────────┐
│     Three-Layer Architecture   │
├───────────────────────────────┤
│ Layer 1: API (Fetch)          │
│   ├─ ApiConfig (Singleton)    │
│   ├─ UmapyoiApiClient         │
│   └─ LocalCache               │
├───────────────────────────────┤
│ Layer 2: Service (Process)    │
│   └─ CharacterService         │
├───────────────────────────────┤
│ Layer 3: Models (Store)       │
│   ├─ AbstractUma              │
│   ├─ Umamusume                │
│   └─ UmapyoiCharacter         │
└───────────────────────────────┘
    ↓
┌───────────────────────────────┐
│   Algorithms & Utilities       │
├───────────────────────────────┤
│ Sorting/Searching:            │
│   ├─ UmamusumeSorter (Merge)  │
│   ├─ UmapyoiEnhancedSorter    │
│   └─ Comparators (Strategy)   │
├───────────────────────────────┤
│ Factories & Utilities:        │
│   ├─ UmaFactory               │
│   ├─ DataGenerator            │
│   └─ JSONHandler              │
└───────────────────────────────┘
    ↓
Demonstrations (Educational)
```

---

## 📈 Statistics

| Category | Count |
|----------|-------|
| **Total Java Files** | 35 |
| **Model Classes** | 13 |
| **API Layer Files** | 6 |
| **Sorting/Algorithm Files** | 3 |
| **Demo Files** | 5 |
| **Utility Files** | 2 |
| **Core Framework Files** | 3 |
| **Factory/Pattern Files** | 1 |
| **Total Lines of Code** | ~4,500+ |
| **Methods Implemented** | 150+ |
| **Design Patterns** | 8 |

---

## ✅ Compilation Status

✅ **BUILD SUCCESS** (34 source files)

- All compilation errors resolved
- All dependencies configured (OkHttp3, Jackson, SLF4J, JUnit)
- Ready for production deployment

---

## 🚀 Key Features

- ✅ **OOP Demonstrations**: Encapsulation, Inheritance, Polymorphism, Abstraction
- ✅ **Sorting Algorithms**: Merge Sort (O(n log n)), Comparator Strategy Pattern
- ✅ **API Integration**: HTTP client with retry logic, caching, async operations
- ✅ **Design Patterns**: Singleton, Factory, Facade, Strategy, Decorator, Adapter
- ✅ **Thread-Safe Caching**: TTL-based cache with concurrent access
- ✅ **Comprehensive Testing**: Unit tests for API, data generation, algorithms
- ✅ **Mock Data Generation**: Random character generation for testing
- ✅ **Educational Demonstrations**: Interactive examples of all concepts

