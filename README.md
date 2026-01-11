# GearShift KerML Service

A metadata-driven KerML metamodel implementation using the **Gearshift Framework** - a next-generation MOF (Meta-Object Facility) combined with model data management.

## Architecture

### Gearshift Framework

The core framework (`org.openmbee.gearshift`) provides:

- **Metamodel Layer** (`org.openmbee.gearshift.metamodel`)
  - `MetaClass` - Define types with attributes, operations, and constraints
  - `MetaProperty` - Define properties with multiplicity, aggregation, etc.
  - `MetaAssociation` - Define relationships between types
  - `MetaConstraint` - OCL or custom constraint expressions
  - `MetaOperation` - Define operations/methods

- **Engine Layer** (`org.openmbee.gearshift.engine`)
  - `MetamodelRegistry` - Register and manage metaclasses
  - `MofEngine` - Create instances, validate constraints, handle derived properties
  - `MofObject` - Runtime instances of metaclasses
  - `NameResolver` - KerML 8.2.3.5 compliant name resolution engine

- **Repository Layer** (`org.openmbee.gearshift.repository`)
  - `ModelRepository` - CRUD operations with indexing
  - Type-based and property-based indexing for fast queries

- **Query Layer** (`org.openmbee.gearshift.query`)
  - `QueryEngine` - Fluent API for querying model instances

- **Unified API** (`GearshiftEngine`)
  - Single entry point combining all capabilities

### KerML Metamodel

The KerML-specific implementation (`org.openmbee.gearshift.kerml`) defines:

- `Element` - Root of KerML hierarchy
- `Relationship` - Base for relationships
- `Membership` - Element membership in Namespaces
- `Namespace` - Container for Elements
- `Import` - Import mechanisms (Membership, Namespace)
- `Specialization` - Type specialization relationships
- `Feature` - KerML features
- `Type` - KerML types
- `Classifier` - KerML classifiers

## Key Features

### 1. JSON-like Syntax with Kotlin

Define metamodels using Kotlin's named parameters:

```kotlin
val element = MetaClass(
    name = "Element",
    isAbstract = true,
    attributes = listOf(
        MetaProperty(
            name = "name",
            type = "String",
            multiplicity = "0..1"
        )
    )
)
```

### 2. Jackson JSON Serialization

Save/load metamodels as JSON:

```kotlin
// Save to JSON
MetamodelLoader.saveMetaClass(element, Path.of("element.json"))

// Load from JSON
val loaded = MetamodelLoader.loadMetaClass(Path.of("element.json"))
```

### 3. MOF Engine

Create instances and manage them:

```kotlin
val engine = GearshiftEngine()
engine.registerMetaClass(element)

// Create instance
val (id, instance) = engine.createInstance("Element")
engine.setProperty(id, "name", "MyElement")
```

### 4. Model Repository

Indexed storage with queries:

```kotlin
// Query by type
val allFeatures = engine.getInstancesByType("Feature")

// Query by property
val named = engine.getInstancesByProperty("name", "MyFeature")

// Fluent queries
val results = engine.queryEngine
    .from("Feature")
    .where("isAbstract", true)
    .execute()
```

### 5. Validation

Metamodel and instance validation:

```kotlin
// Validate metamodel
val errors = engine.validateMetamodel()

// Validate instance
val instanceErrors = engine.validateInstance(id)
```

### 6. Name Resolution (KerML 8.2.3.5)

Full KerML compliant name resolution:

```kotlin
// Resolve qualified name
val result = engine.resolveName(
    qualifiedName = "BaseTypes::Integer",
    localNamespaceId = "my-namespace-id"
)

// Access resolved element
if (result != null) {
    val element = result.memberElement
    val membership = result.membership
}

// Global scope resolution
val globalResult = engine.resolveName("$::StandardLibrary::Boolean", anyNamespaceId)

// Redefinition context
val redefinedResult = engine.resolveName(
    qualifiedName = "baseFeature",
    localNamespaceId = derivedTypeId,
    isRedefinitionContext = true
)
```

See [docs/NAME_RESOLUTION.md](docs/NAME_RESOLUTION.md) for complete documentation.

## Building and Running

### Prerequisites

- Java 17 or later
- Gradle 8.x (or use the Gradle wrapper)

### Setup

```bash
# Initialize Gradle wrapper (if not present)
gradle wrapper --gradle-version 8.5

# Build
./gradlew build

# Run
./gradlew run
```

### IntelliJ IDEA

1. Open the project directory
2. IntelliJ will auto-detect the Gradle project
3. Wait for dependencies to download
4. Run `Application.kt`

## Project Structure

```
src/main/kotlin/org/openmbee/gearshift/
├── GearshiftEngine.kt           # Main unified API
├── metamodel/                   # Metamodel definitions
│   ├── MetaClass.kt
│   ├── MetaProperty.kt
│   ├── MetaAssociation.kt
│   ├── MetaConstraint.kt
│   ├── MetaOperation.kt
│   └── MetamodelLoader.kt      # JSON serialization
├── engine/                      # MOF engine
│   ├── MetamodelRegistry.kt
│   ├── MofEngine.kt
│   ├── MofObject.kt
│   └── NameResolver.kt         # KerML 8.2.3.5 name resolution
├── repository/                  # Model storage
│   └── ModelRepository.kt
├── query/                       # Query engine
│   └── QueryEngine.kt
└── kerml/                       # KerML implementation
    ├── KerMLMetamodel.kt       # KerML metamodel definition
    └── Application.kt          # Main application
```

## Dependencies

- **Kotlin** 1.9.22
- **Jackson** 2.16.1 (JSON serialization)
- **ANTLR** 4.13.1 (for GQL, OCL, and future KerML grammars)
- **Kotlin Logging** 6.0.3
- **Logback** 1.4.14

## Next Steps

1. **Expand KerML Metamodel** - Add complete KerML specification
2. **Code Generation** - Generate Java/Kotlin API from metamodel
3. **OCL Integration** - Evaluate OCL constraints
4. **KerML Parser** - Integrate KerML ANTLR grammar
5. **Persistence** - Add database backend for repository
6. **REST API** - Expose via HTTP API

## Design Philosophy

**Gearshift** combines the power of:
- **MOF** - Metamodel-driven architecture
- **Model Data Management** - Efficient storage and querying
- **Modern Kotlin** - Expressive syntax with named parameters
- **JSON Interoperability** - Easy import/export of metamodels

The result is a framework that feels like working with JSON while providing the full power of a metamodel-driven system.
