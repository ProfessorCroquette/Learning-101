# Umapyoi API Endpoints Reference

## Base URL
```
https://api.umapyoi.net
```

## Authentication
Currently uses API key header authentication (optional for public endpoints).

```
Authorization: Bearer {API_KEY}
```

## Endpoints

### Characters

#### Get Character by ID
```http
GET /characters/{id}
```

**Response (200 OK)**:
```json
{
  "id": "1",
  "name": "Special Week",
  "japaneseName": "スペシャルウィーク",
  "characterType": "SPEED",
  "rarity": "UR",
  "stats": {
    "speed": 1200,
    "stamina": 800,
    "power": 600,
    "guts": 700,
    "intelligence": 900
  },
  "skills": ["Intuitive Sense", "Speed Star"],
  "images": {
    "portrait": "https://...",
    "card": "https://..."
  }
}
```

#### Search Characters
```http
GET /characters/search?name={query}&rarity={rarity}&type={type}
```

**Query Parameters**:
- `name` (string): Character name (fuzzy match)
- `rarity` (string, optional): UR, SSR, SR, R
- `type` (string, optional): SPEED, STAMINA, POWER, GUTS, INTELLIGENCE

**Response (200 OK)**:
```json
{
  "total": 5,
  "results": [
    { /* character object */ },
    ...
  ]
}
```

#### Get All Characters
```http
GET /characters?limit=50&offset=0
```

**Query Parameters**:
- `limit` (integer, default: 50): Results per page
- `offset` (integer, default: 0): Starting position

**Response (200 OK)**:
```json
{
  "total": 150,
  "limit": 50,
  "offset": 0,
  "results": [
    { /* character objects */ }
  ]
}
```

### Tracks (Courses)

#### Get Track by ID
```http
GET /tracks/{id}
```

**Response**:
```json
{
  "id": "1",
  "name": "Kentucky Derby",
  "distance": 2000,
  "surface": "DIRT",
  "region": "Churchill Downs",
  "difficulty": "HARD"
}
```

#### Get All Tracks
```http
GET /tracks
```

### Skills

#### Get Skill by ID
```http
GET /skills/{id}
```

**Response**:
```json
{
  "id": "1",
  "name": "Intuitive Sense",
  "category": "UNIQUE",
  "description": "Enhance early game performance based on running style",
  "powerLevel": 85
}
```

#### Get Skills by Category
```http
GET /skills/category/{category}
```

**Categories**: UNIQUE, SPEED, STAMINA, POWER, GUTS, INTELLIGENCE, STRATEGY

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid character ID",
  "message": "Character ID must be a positive integer"
}
```

### 404 Not Found
```json
{
  "error": "Character not found",
  "id": 99999
}
```

### 429 Too Many Requests
```json
{
  "error": "Rate limit exceeded",
  "retryAfter": 60
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "message": "Please try again later"
}
```

## Rate Limiting

- **Limit**: 100 requests per minute
- **Header**: `X-RateLimit-Remaining`
- **Reset**: `X-RateLimit-Reset`

### Rate Limit Headers
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 45
X-RateLimit-Reset: 1672531200
```

## Pagination

For list endpoints, use pagination:

```http
GET /characters?limit=10&offset=20
```

**Response**:
```json
{
  "total": 150,
  "limit": 10,
  "offset": 20,
  "results": [ ... ]
}
```

## Data Types

### Character Rarity
- `UR` - Ultra Rare
- `SSR` - Super Super Rare
- `SR` - Super Rare
- `R` - Rare
- `N` - Normal

### Character Type
- `SPEED` - Speed specialist
- `STAMINA` - Stamina specialist
- `POWER` - Power specialist
- `GUTS` - Guts specialist
- `INTELLIGENCE` - Intelligence specialist

### Track Surface
- `TURF` - Grass track
- `DIRT` - Dirt track
- `SAND` - Sand track

### Track Difficulty
- `EASY` - Beginner friendly
- `NORMAL` - Standard difficulty
- `HARD` - Challenging
- `VERY_HARD` - Extreme challenge

## Code Examples

### Java (Using CharacterService)
```java
CharacterService service = new CharacterService();

// Get single character
Umamusume character = service.getCharacterById(1);
System.out.println(character.getName());

// Search characters
List<Umamusume> results = service.searchCharactersByName("speed");

// Async batch operations
service.getAllCharactersAsync()
    .thenAccept(characters -> {
        System.out.println("Loaded " + characters.size() + " characters");
    });
```

### cURL
```bash
# Get character
curl -X GET "https://api.umapyoi.net/characters/1"

# Search
curl -X GET "https://api.umapyoi.net/characters/search?name=special"

# Get all with pagination
curl -X GET "https://api.umapyoi.net/characters?limit=10&offset=0"
```

## Caching Strategy

The CharacterService implements caching to reduce API calls:

- **TTL**: 1 hour
- **Max Entries**: 10,000
- **Cache Key**: Character ID or query string

### Cache Hit Example
```
First call: GET /characters/1
└─ API call made, result cached

Second call (within 1 hour): GET /characters/1
└─ Cache hit, instant response
```

## Versioning

Current API version: **v1**

Future versions may be accessed with:
```
https://api.umapyoi.net/v2/characters
```

## Changelog

### v1.0.0 (2024)
- Initial API release
- Character endpoints
- Search functionality
- Track and skill endpoints
- Rate limiting
- Error handling

## Support

For API issues or questions:
- Documentation: [API Integration Guide](../chesed/api-integration.md)
- Examples: See demo classes in `src/main/java/com/atziluth/sephirah/chesed/demo/`

