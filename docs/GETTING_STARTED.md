# Getting Started with Gearshift KerML

This guide will help you get started with the Gearshift KerML Service.

## Quick Start

### 1. Build the Project

```bash
# Initialize Gradle wrapper (if needed)
gradle wrapper --gradle-version 8.5

# Build
./gradlew build

# Run the example application
./gradlew run
```

### 2. Your First Metamodel

Create a simple metamodel in Kotlin:

```kotlin
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.metamodel.*

fun main() {
    // Create the engine
    val engine = GearshiftEngine()

    // Define a simple Person metaclass
    val person = MetaClass(
        name = "Person",
        attributes = listOf(
            MetaProperty(
                name = "firstName",
                type = "String",
                multiplicity = "1"
            ),
            MetaProperty(
                name = "lastName",
                type = "String",
                multiplicity = "1"
            ),
            MetaProperty(
                name = "age",
                type = "Integer",
                multiplicity = "0..1"
            )
        ),
        constraints = listOf(
            MetaConstraint(
                name = "agePositive",
                language = "OCL",
                expression = "self.age >= 0",
                description = "Age must be non-negative"
            )
        )
    )

    // Register the metaclass
    engine.registerMetaClass(person)

    // Validate metamodel
    val errors = engine.validateMetamodel()
    println("Metamodel valid: ${errors.isEmpty()}")

    // Create an instance
    val (personId, personObj) = engine.createInstance("Person")
    engine.setProperty(personId, "firstName", "John")
    engine.setProperty(personId, "lastName", "Doe")
    engine.setProperty(personId, "age", 30)

    // Query
    val allPeople = engine.getInstancesByType("Person")
    println("Total people: ${allPeople.size}")
}
```

### 3. Load from JSON

Define your metamodel in JSON:

```json
{
  "name": "Person",
  "isAbstract": false,
  "attributes": [
    {
      "name": "firstName",
      "type": "String",
      "multiplicity": "1"
    },
    {
      "name": "lastName",
      "type": "String",
      "multiplicity": "1"
    }
  ],
  "constraints": []
}
```

Load it:

```kotlin
import org.openmbee.gearshift.metamodel.MetamodelLoader
import java.nio.file.Path

val person = MetamodelLoader.loadMetaClass(Path.of("person.json"))
engine.registerMetaClass(person)
```

## Core Concepts

### MetaClass

A `MetaClass` defines a type in your model:

```kotlin
MetaClass(
    name = "Element",              // Type name
    isAbstract = true,             // Can't be instantiated directly
    superclasses = listOf("Base"), // Inheritance
    attributes = listOf(/*...*/),  // Properties
    constraints = listOf(/*...*/), // Validation rules
    operations = listOf(/*...*/),  // Methods
    description = "Documentation"
)
```

### MetaProperty

A `MetaProperty` defines an attribute or association:

```kotlin
MetaProperty(
    name = "ownedElements",
    type = "Element",
    multiplicity = "0..*",           // Zero or more
    aggregation = AggregationKind.COMPOSITE,
    isOrdered = true,                // Order matters
    isUnique = true,                 // No duplicates
    isDerived = false,               // Stored value
    isReadOnly = false,              // Can be modified
    redefines = listOf("baseElements"),
    subsets = listOf("allElements")
)
```

### Multiplicity

Standard UML multiplicity notation:

- `"1"` - Exactly one (default)
- `"0..1"` - Optional (zero or one)
- `"0..*"` - Zero or more
- `"1..*"` - One or more
- `"2..5"` - Between 2 and 5

### Aggregation

Three kinds:

```kotlin
AggregationKind.NONE       // Simple reference
AggregationKind.SHARED     // Shared ownership
AggregationKind.COMPOSITE  // Exclusive ownership (lifecycle)
```

## Working with Instances

### Create Instances

```kotlin
// Create with auto-generated ID
val (id, obj) = engine.createInstance("Person")

// Create with specific ID
val (customId, obj2) = engine.createInstance("Person", "person-123")
```

### Set Properties

```kotlin
engine.setProperty(id, "firstName", "Jane")
engine.setProperty(id, "age", 25)
```

### Get Properties

```kotlin
val name = engine.getProperty(id, "firstName") as String
val age = engine.getProperty(id, "age") as Int?
```

### Query

```kotlin
// By type
val allPeople = engine.getInstancesByType("Person")

// By property value
val jane = engine.getInstancesByProperty("firstName", "Jane")

// Fluent query
val adults = engine.queryEngine
    .from("Person")
    .filter { (it.getProperty("age") as? Int ?: 0) >= 18 }
    .execute()
```

### Validate

```kotlin
// Validate instance
val errors = engine.validateInstance(id)
if (errors.isNotEmpty()) {
    errors.forEach { println("Error: $it") }
}
```

## Name Resolution (KerML Feature)

Resolve qualified names:

```kotlin
// Create a namespace hierarchy
val (nsId, namespace) = engine.createInstance("Namespace")
val (elemId, element) = engine.createInstance("Element")
val (membId, membership) = engine.createInstance("Membership")

// Set up membership
engine.setProperty(membId, "memberElement", elemId)
engine.setProperty(membId, "memberName", "MyElement")
engine.setProperty(nsId, "ownedMembership", listOf(membId))

// Resolve name
val result = engine.resolveName("MyElement", nsId)
println("Resolved: ${result?.memberElement}")

// Qualified names
val result2 = engine.resolveName("A::B::C", rootNsId)

// Global scope
val result3 = engine.resolveName("$::StandardLib::Boolean", anyNsId)
```

## Common Patterns

### 1. Define Base Types

```kotlin
// Base Element
val element = MetaClass(
    name = "Element",
    isAbstract = true,
    attributes = listOf(
        MetaProperty(name = "id", type = "String", multiplicity = "1"),
        MetaProperty(name = "name", type = "String", multiplicity = "0..1")
    )
)

// Specialized types
val feature = MetaClass(
    name = "Feature",
    superclasses = listOf("Element"),
    attributes = listOf(
        MetaProperty(name = "isAbstract", type = "Boolean")
    )
)
```

### 2. Composite Structures

```kotlin
val package = MetaClass(
    name = "Package",
    superclasses = listOf("Namespace"),
    attributes = listOf(
        MetaProperty(
            name = "ownedElements",
            type = "Element",
            multiplicity = "0..*",
            aggregation = AggregationKind.COMPOSITE
        )
    )
)
```

### 3. Derived Properties

```kotlin
MetaProperty(
    name = "qualifiedName",
    type = "String",
    isDerived = true,
    isReadOnly = true,
    derivationConstraint = "self.owner.qualifiedName + '::' + self.name"
)
// Note: Derivation evaluation not yet implemented
```

### 4. Bidirectional Associations

Use `MetaAssociation`:

```kotlin
val ownership = MetaAssociation(
    name = "Ownership",
    sourceEnd = MetaAssociationEnd(
        name = "owner",
        type = "Element",
        multiplicity = "0..1"
    ),
    targetEnd = MetaAssociationEnd(
        name = "ownedElements",
        type = "Element",
        multiplicity = "0..*",
        aggregation = AggregationKind.COMPOSITE
    )
)
```

## Tips and Best Practices

### 1. Start Simple

Begin with a small metamodel and expand:

```kotlin
// Start with core types
Element -> Relationship -> Feature -> Type

// Then add specializations
Type -> Classifier -> Class
```

### 2. Use Inheritance

Leverage superclasses to avoid duplication:

```kotlin
// Common properties in base class
val namedElement = MetaClass(
    name = "NamedElement",
    attributes = listOf(
        MetaProperty(name = "name", type = "String")
    )
)

// Specialized classes inherit
val feature = MetaClass(
    name = "Feature",
    superclasses = listOf("NamedElement")
    // Automatically has 'name' property
)
```

### 3. Validate Early

Always validate after defining metamodel:

```kotlin
val errors = engine.validateMetamodel()
if (errors.isNotEmpty()) {
    throw IllegalStateException("Invalid metamodel: $errors")
}
```

### 4. Use Constraints

Add business rules as constraints:

```kotlin
constraints = listOf(
    MetaConstraint(
        name = "uniqueNames",
        expression = "self.ownedElements->forAll(e1, e2 | e1 <> e2 implies e1.name <> e2.name)",
        description = "Owned elements must have unique names"
    )
)
```

### 5. Index for Performance

The repository automatically indexes by type and properties for fast queries.

## Next Steps

- Read [ARCHITECTURE.md](ARCHITECTURE.md) for system design
- See [NAME_RESOLUTION.md](NAME_RESOLUTION.md) for KerML name resolution details
- Explore [KerMLMetamodel.kt](../src/main/kotlin/org/openmbee/gearshift/kerml/KerMLMetamodel.kt) for complete KerML metamodel
- Check [MetamodelExample.kt](../src/main/kotlin/org/openmbee/gearshift/kerml/examples/MetamodelExample.kt) for more examples

## Troubleshooting

### Build Issues

```bash
# Clean and rebuild
./gradlew clean build

# Skip tests
./gradlew build -x test
```

### Validation Errors

Check metamodel consistency:
- All superclass names must reference registered MetaClasses
- Property types must be valid (registered MetaClass or primitive)
- No circular inheritance

### Resolution Failures

For name resolution issues:
- Ensure Namespace/Membership structures are correct
- Check that memberName is set on Memberships
- Verify ownedMembership lists are populated

## Resources

- [README.md](../README.md) - Project overview
- [ARCHITECTURE.md](ARCHITECTURE.md) - System architecture
- [NAME_RESOLUTION.md](NAME_RESOLUTION.md) - Name resolution guide
- KerML Specification v1.0 - Official specification (in references/)
