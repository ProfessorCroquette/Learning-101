# API Integration Guide

## Overview
The Chesed module integrates with the Umapyoi API to fetch and manage Umamusume character data.

---

## Architecture

### Three-Layer Architecture

```
┌─────────────────────────────────┐
│   API Clients Layer             │
│  UmapyoiApiClient               │
│  CharacterService               │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Caching & Configuration       │
│  LocalCache<T>                  │
│  ApiConfig (Singleton)          │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   HTTP Client (OkHttp3)         │
│   JSON Serialization (Jackson)  │
└─────────────────────────────────┘
```

---

## Configuration

### ApiConfig Class
Singleton pattern providing centralized configuration.

```java
public class ApiConfig {
    private static final String BASE_URL = "https://api.umapyoi.net";
    private static final OkHttpClient HTTP_CLIENT = buildHttpClient();
    private static final ObjectMapper MAPPER = buildObjectMapper();
    
    public static OkHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }
    
    public static ObjectMapper getObjectMapper() {
        return MAPPER;
    }
}
```

### Configuration Parameters
- **Base URL**: https://api.umapyoi.net
- **Timeout**: 10 seconds
- **Max Retries**: 3
- **Rate Limit**: 100ms between requests

---

## API Endpoints

### 1. Get Character by ID
**Endpoint**: `GET /characters/{id}`
**Response**: Full UmapyoiCharacter object

```java
Umamusume character = characterService.getCharacterById(1);
// Returns cached result if available (TTL: 1 hour)
```

### 2. Search Characters
**Endpoint**: `GET /characters/search?name={query}`
**Response**: List of matching characters

```java
List<Umamusume> results = characterService.searchCharactersByName("speed");
```

### 3. Get All Characters
**Endpoint**: `GET /characters`
**Response**: List of all characters (paginated)

```java
CompletableFuture<List<Umamusume>> future = characterService.getAllCharactersAsync();
```

---

## UmapyoiCharacter DTO

Maps API responses to domain model:

```java
public class UmapyoiCharacter {
    @JsonProperty("id") private String id;
    @JsonProperty("name") private String name;
    @JsonProperty("rarity") private String rarity;
    @JsonProperty("stats") private StatsDto stats;
    @JsonProperty("skills") private List<String> skills;
    
    // Converts to Umamusume domain model
    public Umamusume toDomainModel() { ... }
}
```

### Adapter Pattern
Converts API response format to internal domain model:

```java
UmapyoiCharacter apiChar = // from API
Umamusume domainChar = apiChar.toDomainModel();
```

---

## CharacterService

**Responsibilities**:
- API integration
- Caching management
- Rate limiting
- Search functionality

### Key Methods

```java
// Get single character with cache
public Umamusume getCharacterById(int id)

// Search with fuzzy matching
public List<Umamusume> searchCharactersByName(String name)

// Async batch operations
public CompletableFuture<Umamusume> getCharacterAsync(int id)
public CompletableFuture<List<Umamusume>> getAllCharactersAsync()
```

### Caching Strategy

```java
// Check cache first
Umamusume cached = localCache.get(cacheKey, Umamusume.class);
if (cached != null) {
    logger.debug("Cache hit for character {}", id);
    return cached;
}

// Fetch from API if not cached
Umamusume character = apiClient.getCharacter(id);
localCache.put(cacheKey, character, Umamusume.class);
return character;
```

---

## Error Handling

### API Errors
```java
try {
    Umamusume character = characterService.getCharacterById(1);
} catch (IOException e) {
    logger.error("API request failed: {}", e.getMessage());
    // Fallback logic
}
```

### Retry Logic
Built-in retry mechanism for transient failures:

```java
// Automatically retries up to 3 times
OkHttpClient client = new OkHttpClient.Builder()
    .retryOnConnectionFailure(true)
    .connectTimeout(10, TimeUnit.SECONDS)
    .build();
```

---

## Performance Optimization

### 1. Caching
- **TTL**: 1 hour (configurable)
- **Max Entries**: 10,000
- **Hit Rate Tracking**: Monitor cache effectiveness

```java
if (cache.getHitRate() < 0.5) {
    logger.warn("Cache hit rate low: {}%", cache.getHitRate() * 100);
}
```

### 2. Rate Limiting
- **Delay**: 100ms between requests
- **Async Processing**: Use CompletableFuture for non-blocking calls

```java
// Non-blocking async call
characterService.getCharacterAsync(id)
    .thenAccept(character -> processCharacter(character))
    .exceptionally(e -> {
        logger.error("Async fetch failed", e);
        return null;
    });
```

### 3. Batch Operations
- Group multiple requests
- Use streaming for large result sets

```java
List<Umamusume> characters = characterService.searchCharactersByName("speed")
    .stream()
    .limit(100)  // Limit result size
    .collect(Collectors.toList());
```

---

## Integration Examples

### Example 1: Simple Lookup
```java
CharacterService service = new CharacterService();
Umamusume character = service.getCharacterById(1);
System.out.println(character.getName());
```

### Example 2: Search and Sort
```java
List<Umamusume> speedUmas = service.searchCharactersByName("speed")
    .stream()
    .sorted(Comparator.comparingInt(Umamusume::getTotalStats).reversed())
    .collect(Collectors.toList());
```

### Example 3: Async Batch Processing
```java
CompletableFuture<List<Umamusume>> future = service.getAllCharactersAsync();
future.thenAccept(characters -> {
    logger.info("Loaded {} characters", characters.size());
    processCharacters(characters);
});
```

### Example 4: Error Handling
```java
try {
    Umamusume character = service.getCharacterById(invalidId);
} catch (IOException e) {
    logger.error("Failed to fetch character: {}", e.getMessage());
    // Use fallback or default
}
```

---

## Testing

### Mock Data
Use DataGenerator for testing without real API calls:

```java
List<Umamusume> mockCharacters = DataGenerator.generateMockUmamususeList(5);
```

### Unit Testing
```java
@Test
public void testCharacterSearch() {
    CharacterService service = new CharacterService();
    List<Umamusume> results = service.searchCharactersByName("Suzuka");
    assertFalse(results.isEmpty());
}
```

---

## Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| Slow Response | Cache miss | Check cache TTL, increase max entries |
| Rate Limit Errors | Too many requests | Increase RATE_LIMIT_DELAY_MS |
| Connection Timeout | Network latency | Increase timeout in ApiConfig |
| Memory Issues | Large cache | Reduce max_entries or TTL |

---

## Best Practices

1. **Always use CharacterService** instead of calling API directly
2. **Check cache before API calls** to reduce latency
3. **Use async methods** for batch operations
4. **Handle exceptions gracefully** with fallbacks
5. **Monitor cache statistics** to optimize performance
6. **Log API interactions** for debugging

