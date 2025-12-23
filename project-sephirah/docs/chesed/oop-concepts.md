# Object-Oriented Programming Concepts

## Overview
This document explains OOP concepts using the Umamusume database project as concrete examples.

## 1. Encapsulation
**Definition**: Bundling data and methods together, hiding internal details from the outside world.

### Implementation in Project
```java
// Umamusume class encapsulates character data
public class Umamusume {
    private String id;
    private String name;
    private String japaneseName;
    private Rarity rarity;
    private Stats stats;  // Nested encapsulation
    
    // Controlled access through getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

### Benefits
- **Data Protection**: Internal state cannot be corrupted by external code
- **Flexibility**: Can change internal implementation without affecting public interface
- **Maintainability**: Single responsibility for each class

---

## 2. Inheritance
**Definition**: Creating new classes based on existing classes, forming an "IS-A" relationship.

### Implementation in Project
```
AbstractUma (Base Class)
├── SpeedUma (extends AbstractUma)
├── StaminaUma (extends AbstractUma)
└── UniqueSkillUma (extends AbstractUma + implements SpecialAbility)
```

### AbstractUma Base Class
- **Abstract Methods**: `getCharacterType()`, `calculateSpecialPower()`, `getSpecialty()`
- **Concrete Methods**: `getFullName()`, `getTotalStats()`
- **Purpose**: Define common interface for all Uma types

### Benefits
- **Code Reuse**: Common functionality defined once in base class
- **Polymorphic Behavior**: Treat different types uniformly
- **Extensibility**: Easy to add new Uma types

---

## 3. Polymorphism
**Definition**: Objects of different types responding to the same message (method call) in different ways.

### Implementation in Project
```java
// All AbstractUma types respond to calculateSpecialPower()
// but implement differently
List<AbstractUma> characters = new ArrayList<>();
characters.add(new SpeedUma(...));
characters.add(new StaminaUma(...));

for (AbstractUma uma : characters) {
    int power = uma.calculateSpecialPower();  // Different implementations
}
```

### Types of Polymorphism
1. **Compile-time (Method Overloading)**: Same method name, different parameters
2. **Runtime (Method Overriding)**: Subclass implements parent's abstract method
3. **Interface Implementation**: Class implements interface methods

### Benefits
- **Flexibility**: Code works with parent type, handles any subtype
- **Extensibility**: Add new types without modifying existing code
- **Maintainability**: Reduces conditional logic

---

## 4. Abstraction
**Definition**: Representing complex functionality in a simplified interface.

### Implementation in Project
```java
// SpecialAbility interface abstracts ability behavior
public interface SpecialAbility {
    String getAbilityName();
    String getAbilityDescription();
    int getPowerLevel();
    void activate();
}

// UniqueSkillUma implements the abstraction
public class UniqueSkillUma extends AbstractUma implements SpecialAbility {
    // Provides concrete implementation
}
```

### Benefits
- **Simplified Interface**: Users see only what they need
- **Implementation Hiding**: Complexity hidden from users
- **Contract Definition**: Interface defines what implementer must provide

---

## 5. Composition
**Definition**: "HAS-A" relationship where objects contain other objects.

### Implementation in Project
```java
public class Umamusume {
    // Composition: Uma HAS-A Stats object
    private Stats stats;  // Nested class
    
    // Composition: Uma HAS-A list of proficiencies
    private List<TrackProficiency> proficiencies;
}

// Stats inner class
public static class Stats {
    private int speed;
    private int stamina;
    private int power;
    private int guts;
    private int intelligence;
}
```

### Benefits
- **Flexibility**: Change behavior at runtime by swapping composed objects
- **Reusability**: Stats can be reused across different characters
- **Cleaner Hierarchy**: Avoids deep inheritance chains

---

## 6. Design Patterns Using OOP

### Factory Pattern
```java
// UmaFactory creates different Uma types
public class UmaFactory {
    public static AbstractUma createUma(UmaType type, String name, 
                                       String japaneseName, Rarity rarity) {
        switch(type) {
            case SPEED: return new SpeedUma(name, japaneseName, rarity);
            case STAMINA: return new StaminaUma(name, japaneseName, rarity);
            default: return null;
        }
    }
}
```

### Facade Pattern
```java
// CharacterService acts as facade
public class CharacterService {
    // Hides complexity of API calls, caching, rate limiting
    public Umamusume getCharacterById(int id) { ... }
    public List<Umamusume> searchCharactersByName(String name) { ... }
}
```

---

## Key Takeaways

| Concept | Benefit | Example |
|---------|---------|---------|
| **Encapsulation** | Data protection & flexibility | Umamusume class with private fields |
| **Inheritance** | Code reuse & polymorphism | AbstractUma → SpeedUma/StaminaUma |
| **Polymorphism** | Flexible, extensible code | Different calculateSpecialPower() implementations |
| **Abstraction** | Simplified interface | SpecialAbility interface |
| **Composition** | Flexibility & reusability | Stats inner class in Umamusume |

