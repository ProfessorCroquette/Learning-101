# Algorithms in Umamusume Project

## Overview
This document explains sorting and searching algorithms implemented in the Chesed module.

---

## 1. Sorting Algorithms

### Merge Sort (UmamusumeSorter)
**Time Complexity**: O(n log n)
**Space Complexity**: O(n)
**Stability**: Stable

#### Implementation
```java
public class UmamusumeSorter {
    public <T> List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return list;
        
        int mid = list.size() / 2;
        List<T> left = mergeSort(list.subList(0, mid), comparator);
        List<T> right = mergeSort(list.subList(mid, list.size()), comparator);
        
        return merge(left, right, comparator);
    }
}
```

#### When to Use
- Need guaranteed O(n log n) performance
- Stability is important
- Working with linked lists

#### Example Usage
```java
List<Umamusume> characters = getCharacters();
List<Umamusume> sorted = sorter.mergeSort(characters, 
    Comparator.comparingInt(Umamusume::getTotalStats).reversed());
```

---

## 2. Searching Algorithms

### Linear Search (UmamusumeSearcher)
**Time Complexity**: O(n)
**Space Complexity**: O(1)
**Best For**: Small lists, unsorted data

```java
public Umamusume findByName(String targetName) {
    for (Umamusume uma : characters) {
        if (uma.getName().equalsIgnoreCase(targetName)) {
            return uma;
        }
    }
    return null;
}
```

### Fuzzy Search (CharacterService)
**Pattern**: Partial string matching with scoring
**Used For**: Finding characters with typos or partial names

```java
public List<Umamusume> searchCharactersByName(String query) {
    return characters.stream()
        .filter(u -> u.getName().toLowerCase().contains(query.toLowerCase()))
        .collect(Collectors.toList());
}
```

---

## 3. Caching Strategy

### Local Cache with TTL
**Pattern**: In-Memory Cache with Time-To-Live
**Time Complexity**: O(1) for get/put operations
**Space Complexity**: O(n) where n = max_entries

#### Implementation
```java
public class LocalCache<T> {
    private final ConcurrentHashMap<String, CacheEntry<T>> cache;
    private final long ttlSeconds;
    
    public T get(String key, Class<T> type) {
        CacheEntry<T> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        return null;
    }
}
```

#### Benefits
- **Fast Access**: O(1) lookup time
- **Memory Efficient**: TTL prevents unbounded growth
- **Thread-Safe**: ConcurrentHashMap handles concurrency

---

## 4. Rate Limiting

### Delay-Based Rate Limiting
**Pattern**: Fixed delay between requests
**Purpose**: Prevent API quota exhaustion

```java
public CompletableFuture<Umamusume> getCharacterAsync(int id) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            Thread.sleep(RATE_LIMIT_DELAY_MS);  // 100ms between requests
            return getCharacter(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    });
}
```

---

## 5. Comparison Operations

### Comparator for Sorting
```java
// Sort by total stats (descending)
Comparator<Umamusume> byStats = 
    Comparator.comparingInt(Umamusume::getTotalStats).reversed();

// Sort by rarity, then by name
Comparator<Umamusume> byRarityThenName = 
    Comparator.comparing(Umamusume::getRarity)
              .thenComparing(Umamusume::getName);
```

---

## 6. Grouping and Aggregation

### Group by Type
```java
Map<String, List<AbstractUma>> grouped = 
    UmapyoiEnhancedSorter.groupByType(characters);
```

### Stream Operations
```java
// Count by rarity
Map<Rarity, Long> rarityCount = characters.stream()
    .collect(Collectors.groupingBy(Umamusume::getRarity, 
                                   Collectors.counting()));

// Get top 3 by stats
List<Umamusume> topThree = characters.stream()
    .sorted(Comparator.comparingInt(Umamusume::getTotalStats).reversed())
    .limit(3)
    .collect(Collectors.toList());
```

---

## Algorithm Complexity Reference

| Algorithm | Best | Average | Worst | Space |
|-----------|------|---------|-------|-------|
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Linear Search | O(1) | O(n) | O(n) | O(1) |
| Fuzzy Search | O(n*m) | O(n*m) | O(n*m) | O(1) |
| Hash Lookup | O(1) | O(1) | O(n) | O(n) |

---

## Practical Examples

### Example 1: Find Top 5 Speed Characters
```java
characters.stream()
    .filter(u -> u.getCharacterType() == "SPEED")
    .sorted(Comparator.comparingInt(Umamusume::getTotalStats).reversed())
    .limit(5)
    .forEach(u -> System.out.println(u.getName()));
```

### Example 2: Search and Sort
```java
List<Umamusume> results = searchCharactersByName("speed")
    .stream()
    .sorted(Comparator.comparing(Umamusume::getRarity))
    .collect(Collectors.toList());
```

### Example 3: Cache Hit Calculation
```java
int hitRate = (cacheHits / (cacheHits + cacheMisses)) * 100;
logger.info("Cache hit rate: {}%", hitRate);
```

---

## Optimization Tips

1. **Use Caching**: Avoid redundant API calls with LocalCache
2. **Lazy Evaluation**: Use streams for large datasets
3. **Batch Operations**: Process multiple items together
4. **Index Frequently Accessed Data**: Create maps for fast lookups
5. **Monitor Performance**: Log execution times for analysis

