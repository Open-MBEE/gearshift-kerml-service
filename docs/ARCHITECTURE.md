# Gearshift KerML Service Architecture

## Overview

The Gearshift KerML Service is a metadata-driven implementation of the KerML (Kernel Modeling Language) metamodel, built
on the **Gearshift Framework** - a next-generation MOF (Meta-Object Facility) combined with modern model data management
capabilities.

## Design Philosophy

**"Metadata that feels like JSON"**

The system combines:

- **Declarative Metamodeling**: Define types using Kotlin's named parameters or JSON
- **MOF Power**: Full metamodel-driven architecture with validation and constraints
- **Modern Data Management**: Indexed repository, query engine, and efficient lookups
- **KerML Compliance**: Implements KerML specification (e.g., 8.2.3.5 Name Resolution)

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                  Application Layer                          │
│  (KerML Service, Examples, Code Generation)                 │
└─────────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                  KerML Metamodel Layer                      │
│  (Element, Namespace, Membership, Feature, Type...)         │
└─────────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│              Gearshift Framework (Unified API)              │
│                   GearshiftEngine                           │
└─────────────────────────────────────────────────────────────┘
         │              │                │              │
    ┌────────┐   ┌──────────┐    ┌──────────┐   ┌──────────┐
    │Metamodel│   │  Engine  │    │Repository│   │  Query   │
    │Registry │   │  (MOF)   │    │          │   │  Engine  │
    └────────┘   └──────────┘    └──────────┘   └──────────┘
         │              │                │              │
         └──────────────┴────────────────┴──────────────┘
                           │
                    ┌──────────────┐
                    │ Name Resolver│
                    └──────────────┘
```

## Core Components

### 1. Metamodel Layer (`org.openmbee.gearshift.metamodel`)

Defines the structure for describing types:

- **MetaClass**: Represents a type/class
    - Properties: name, superclasses, attributes, constraints, operations
    - Supports abstract classes, inheritance

- **MetaProperty**: Defines properties/attributes
    - Type, multiplicity, aggregation (composite, shared, none)
    - Derived properties, redefinition, subsetting
    - Union properties, ordering, uniqueness

- **MetaAssociation**: Relationships between types
    - Binary associations with source/target ends

- **MetaConstraint**: Validation rules
    - OCL or custom language expressions

- **MetaOperation**: Methods/operations
    - Parameters, return type, body

**Key Feature**: Uses Jackson for JSON serialization - no Kotlin Serialization dependency.

### 2. Engine Layer (`org.openmbee.gearshift.engine`)

Runtime metamodel execution:

- **MetamodelRegistry**:
    - Stores and manages MetaClasses and MetaAssociations
    - Validates metamodel consistency
    - Tracks inheritance hierarchies

- **MDMEngine**:
    - Creates instances from MetaClasses
    - Validates instances against constraints
    - Handles property get/set with validation
    - Placeholder for derived property evaluation

- **MDMObject**:
    - Runtime instance of a MetaClass
    - Stores property values
    - Links back to MetaClass definition

- **NameResolver**: ⭐ NEW
    - Implements KerML 8.2.3.5 Name Resolution
    - Resolves qualified names (A::B::C)
    - Handles global scope ($::)
    - Supports redefinition context
    - Prevents circular resolution

### 3. Repository Layer (`org.openmbee.gearshift.repository`)

Efficient model storage:

- **ModelRepository**:
    - CRUD operations for MDMObjects
    - Type-based indexing (fast lookup by class)
    - Property-based indexing (fast lookup by property value)
    - Statistics and monitoring

### 4. Query Layer (`org.openmbee.gearshift.query`)

Fluent query API:

- **QueryEngine**:
  ```kotlin
  queryEngine
      .from("Feature")
      .where("isAbstract", true)
      .filter { it.getProperty("name")?.toString()?.startsWith("Base") == true }
      .execute()
  ```

### 5. Unified API (`org.openmbee.gearshift.GearshiftEngine`)

Single entry point combining all capabilities:

```kotlin
val engine = GearshiftEngine()

// Metamodel management
engine.registerMetaClass(metaClass)
engine.validateMetamodel()

// Instance management
val (id, instance) = engine.createInstance("Feature")
engine.setProperty(id, "name", "MyFeature")

// Queries
val features = engine.getInstancesByType("Feature")

// Name resolution
val resolved = engine.resolveName("A::B::C", namespaceId)
```

## KerML Metamodel Implementation

### Core Hierarchy

```
Element (abstract root)
├── Relationship (abstract)
│   ├── Membership
│   ├── Import (abstract)
│   │   ├── MembershipImport
│   │   └── NamespaceImport
│   └── Specialization
├── Namespace
│   ├── Feature
│   └── Type
│       └── Classifier
```

### Key Metaclasses

**Element**: Root of KerML hierarchy

- Properties: elementId, name, qualifiedName, ownedElement, owner

**Namespace**: Container for Elements

- Properties: ownedMembership, member, ownedImport, importedMembership
- Supports name resolution

**Membership**: Links Elements to Namespaces

- Properties: memberElement, memberName, visibility
- Result of name resolution

**Import**: Brings external elements into scope

- MembershipImport: Import specific membership
- NamespaceImport: Import all visible members

**Specialization**: Type inheritance

- Properties: specific (subtype), general (supertype), owningType

**Feature**: KerML feature

- Properties: isAbstract, isComposite, ownedFeature

**Type**: KerML type

- Properties: isSufficient, ownedFeatureMembership
- Inherits from Feature

## Key Patterns

### 1. Named Parameters for Metadata

Kotlin's named parameters make defining metadata feel like JSON:

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

### 2. Jackson JSON Interoperability

Serialize/deserialize metamodels:

```kotlin
// Save to JSON
MetamodelLoader.saveMetaClass(element, Path.of("element.json"))

// Load from JSON
val loaded = MetamodelLoader.loadMetaClass(Path.of("element.json"))
```

### 3. Metadata-Driven Execution

Everything is driven by metadata:

```kotlin
// Metadata defines what's allowed
val metaClass = MetaClass(
    name = "Feature",
    attributes = listOf(
        MetaProperty(name = "isAbstract", type = "Boolean")
    )
)

// Engine enforces the metadata
engine.registerMetaClass(metaClass)
val (id, obj) = engine.createInstance("Feature")
engine.setProperty(id, "isAbstract", true)  // ✓ Valid
engine.setProperty(id, "unknownProp", "x")  // ✗ Throws exception
```

### 4. Incremental Name Resolution

Handles circular dependencies:

```kotlin
// Resolves incrementally, avoiding infinite loops
val result = engine.resolveName("A::B", namespaceId)

// Tracks resolution stack to detect cycles
// Can resolve to Membership before resolving memberElement
```

## Data Flow

### Metamodel Definition Flow

```
1. Define MetaClass in Kotlin (or load from JSON)
   ↓
2. Register in MetamodelRegistry
   ↓
3. Validate metamodel consistency
   ↓
4. Ready for instance creation
```

### Instance Creation Flow

```
1. Request instance creation
   ↓
2. MDMEngine validates metaclass exists
   ↓
3. Create MDMObject with link to MetaClass
   ↓
4. Store in ModelRepository with indexing
   ↓
5. Return instance ID and object
```

### Name Resolution Flow

```
1. Parse qualified name (A::B::C)
   ↓
2. Check for cycles (resolution stack)
   ↓
3. Execute resolution algorithm:
   - Single segment: full resolution
   - Multi-segment: resolve qualification, then last segment
   - Global scope: resolve from global namespace
   ↓
4. Return Membership and memberElement
```

## Extension Points

### 1. Constraint Evaluation

Currently placeholder - integrate OCL evaluator:

```kotlin
// TODO in MDMEngine
private fun evaluateConstraint(instance: MDMObject, constraint: MetaConstraint): Boolean {
    // Integrate OCL or custom evaluator
}
```

### 2. Derived Properties

Currently placeholder - evaluate derivation expressions:

```kotlin
// TODO in MDMEngine
private fun evaluateDerivedProperty(instance: MDMObject, property: MetaProperty): Any? {
    // Evaluate derivationConstraint
}
```

### 3. Code Generation

Create a compiler to generate API classes from metamodel:

```kotlin
// Planned: org.openmbee.gearshift.compiler
class CodeGenerator {
    fun generateKotlinAPI(metaClass: MetaClass): String {
        // Generate data class or interface
    }
}
```

### 4. Persistence

Add database backend for repository:

```kotlin
// Planned: org.openmbee.gearshift.repository.impl
class JdbcModelRepository : ModelRepository {
    // Store in relational database
}
```

## Technology Stack

- **Kotlin** 1.9.22 - Language
- **Gradle** 8.5+ - Build tool
- **Jackson** 2.16.1 - JSON serialization
- **ANTLR** 4.13.1 - Grammar parsing (GQL, OCL, KerML)
- **SLF4J + Logback** - Logging

## Performance Considerations

1. **Indexing**: Type and property indices for O(1) lookups
2. **Lazy Resolution**: Name resolution only when needed
3. **Caching Opportunity**: Resolution results could be cached
4. **Concurrent Access**: ConcurrentHashMap for thread-safety

## Compliance

Implements KerML v1.0 specification:

- ✅ 8.2.3.5 Name Resolution (full implementation)
- ✅ 8.2.3.4 Membership and Visibility (partial - metamodel defined)
- ⏳ 8.4.2 Implied Relationships (planned)
- ⏳ Full metamodel (ongoing)

## Future Roadmap

1. **Complete KerML Metamodel**: Add all KerML metaclasses from spec
2. **KerML Parser**: Integrate KerML ANTLR grammar
3. **OCL Integration**: Constraint and derived property evaluation
4. **Code Generation**: Generate APIs from metamodel
5. **REST API**: HTTP service for model management
6. **Persistence**: Database backend
7. **GQL/Query Language**: Advanced querying
8. **Incremental Compilation**: Parse and resolve incrementally

## Summary

The Gearshift KerML Service provides a modern, metadata-driven foundation for KerML implementation. By combining
MOF-style metamodeling with contemporary software patterns (Kotlin DSLs, JSON interop, indexed repositories), it creates
a framework that is both specification-compliant and developer-friendly.

The "metadata that feels like JSON" philosophy makes defining metamodels intuitive while maintaining the rigor and power
of formal metamodeling approaches.
