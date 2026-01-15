# KerML Name Resolution Implementation

This document describes the implementation of KerML Name Resolution (Section 8.2.3.5) in the Gearshift framework.

## Overview

Name resolution is the process of determining which **Element** is identified by a qualified name. The result is
actually a **Membership** relationship, but in most cases the required Element is the `memberElement` of that
Membership.

## Qualified Names

A qualified name consists of one or more **segment names** separated by `::`:

- `A` - Single segment name
- `A::B::C` - Multi-segment qualified name with segments A, B, and C
- `$::A::B` - Global scope qualifier followed by segments

### Components

- **Segment Name**: A simple name (lexical NAME token)
- **Qualification Part**: For multi-segment names, all segments except the last
    - Example: `A::B::C` has qualification part `A::B`
- **Global Scope Qualifier**: The `$` prefix indicating global scope

## Name Resolution Process

Implemented in [NameResolver.kt](../src/main/kotlin/org/openmbee/gearshift/engine/NameResolver.kt).

### Basic Resolution Algorithm (KerML 8.2.3.5.1)

```kotlin
fun resolve(qualifiedName: String, localNamespaceId: String): ResolutionResult?
```

The basic process:

1. **Single segment with global scope** (`$::Name`)
    - Resolve relative to the global Namespace

2. **Single segment without global scope** (`Name`)
    - If localNamespace is a root namespace, resolve relative to global namespace
    - Otherwise, perform full resolution relative to localNamespace

3. **Multi-segment qualified name** (`A::B::C`)
    - Resolve the qualification part (`A::B`) relative to localNamespace
    - Result must be a Namespace
    - Perform visible resolution of the last segment (`C`) relative to that Namespace

### Full Resolution (KerML 8.2.3.5.4)

Full resolution searches all memberships including imported ones:

```kotlin
private fun fullResolution(name: String, namespace: MDMObject): ResolutionResult?
```

Process:

1. Try visible resolution first
2. If that fails and namespace is a Type, search inherited memberships

### Visible Resolution (KerML 8.2.3.5.3)

Visible resolution searches only directly visible memberships:

```kotlin
private fun visibleResolution(name: String, namespace: MDMObject): ResolutionResult?
```

Searches:

1. Owned memberships
2. Imported memberships

### Redefinition Context Resolution

Special case for resolving redefined features:

```kotlin
fun resolve(
    qualifiedName: String,
    localNamespaceId: String,
    isRedefinitionContext: Boolean = true
): ResolutionResult?
```

When `isRedefinitionContext` is true:

- Repeat basic resolution with each general Type of the owning Type's specializations
- Continue until a resolution is found

## Metamodel Support

### Required Metaclasses

The following metaclasses support name resolution (defined
in [KerMLMetamodel.kt](../src/main/kotlin/org/openmbee/gearshift/kerml/KerMLMetamodel.kt)):

#### Membership

```kotlin
MetaClass(
    name = "Membership",
    superclasses = listOf("Relationship"),
    attributes = listOf(
        MetaProperty(name = "memberElement", type = "Element"),
        MetaProperty(name = "memberName", type = "String"),
        MetaProperty(name = "membershipOwningNamespace", type = "Namespace"),
        MetaProperty(name = "visibility", type = "VisibilityKind")
    )
)
```

#### Namespace

```kotlin
MetaClass(
    name = "Namespace",
    superclasses = listOf("Element"),
    attributes = listOf(
        MetaProperty(name = "ownedMembership", type = "Membership", multiplicity = "0..*"),
        MetaProperty(name = "member", type = "Element", multiplicity = "0..*", isDerived = true),
        MetaProperty(name = "ownedImport", type = "Import", multiplicity = "0..*"),
        MetaProperty(name = "importedMembership", type = "Membership", multiplicity = "0..*", isDerived = true)
    )
)
```

#### Import

```kotlin
MetaClass(
    name = "Import",
    superclasses = listOf("Relationship"),
    isAbstract = true,
    attributes = listOf(
        MetaProperty(name = "importOwningNamespace", type = "Namespace"),
        MetaProperty(name = "isRecursive", type = "Boolean", defaultValue = "false")
    )
)
```

Concrete subtypes:

- **MembershipImport**: Imports a specific Membership
- **NamespaceImport**: Imports all visible members from a Namespace

#### Specialization

```kotlin
MetaClass(
    name = "Specialization",
    superclasses = listOf("Relationship"),
    attributes = listOf(
        MetaProperty(name = "specific", type = "Type"),
        MetaProperty(name = "general", type = "Type"),
        MetaProperty(name = "owningType", type = "Type")
    )
)
```

## Implementation Details

### Circularity Handling

The implementation handles circular references in two ways:

1. **Resolution Stack**: Thread-local set tracking names currently being resolved
   ```kotlin
   private val resolutionStack = ThreadLocal.withInitial { mutableSetOf<String>() }
   ```

2. **Visited Set**: When traversing Import/Specialization graphs
   ```kotlin
   val visited = mutableSetOf<String>()
   if (!visited.add(namespaceId)) continue // Skip already visited
   ```

### Integration with GearshiftEngine

Name resolution is exposed through the unified API:

```kotlin
val engine = GearshiftEngine()

// Resolve a name
val result = engine.resolveName(
    qualifiedName = "BaseTypes::Integer",
    localNamespaceId = "my-namespace-id"
)

// Access result
if (result != null) {
    val memberElement = result.memberElement
    val membership = result.membership
}
```

## Usage Examples

### Example 1: Simple Name Resolution

```kotlin
// Create a namespace with a member
val (nsId, namespace) = engine.createInstance("Namespace", "my-namespace")
val (elemId, element) = engine.createInstance("Element", "my-element")
val (membId, membership) = engine.createInstance("Membership")

engine.setProperty(membId, "memberElement", elemId)
engine.setProperty(membId, "memberName", "MyElement")
engine.setProperty(nsId, "ownedMembership", listOf(membId))

// Resolve the name
val result = engine.resolveName("MyElement", nsId)
assert(result?.memberElement == element)
```

### Example 2: Qualified Name Resolution

```kotlin
// Resolve A::B::C from root namespace
val result = engine.resolveName("A::B::C", rootNamespaceId)

// This will:
// 1. Resolve A from root -> namespaceA
// 2. Resolve B from namespaceA -> namespaceB
// 3. Resolve C from namespaceB -> element
```

### Example 3: Global Scope

```kotlin
// Resolve $::StandardLibrary::Boolean from any context
val result = engine.resolveName("$::StandardLibrary::Boolean", anyNamespaceId)
// Always resolves from global namespace regardless of context
```

### Example 4: Redefinition Context

```kotlin
// Resolve redefined feature
val result = engine.resolveName(
    qualifiedName = "baseFeature",
    localNamespaceId = derivedTypeId,
    isRedefinitionContext = true
)
// Searches through general types if not found locally
```

## Incremental Resolution

Per KerML specification 8.2.3.5.1 Note:

- Name resolution must proceed incrementally during parsing
- Avoid infinite loops from circular dependencies
- Must handle cases where Memberships are resolved before their memberElements

The implementation handles this through:

1. Resolution stack to detect cycles
2. Lazy resolution - can resolve to Membership without immediately resolving memberElement
3. Visited sets when traversing relationship graphs

## Future Enhancements

1. **Caching**: Add resolution result cache for performance
2. **Scope Management**: Proper global namespace initialization
3. **Alias Support**: Handle aliasMemberships
4. **Visibility Rules**: Enforce visibility constraints during resolution
5. **Error Reporting**: Detailed error messages for resolution failures

## References

- KerML Specification v1.0, Section 8.2.3.5: Name Resolution
- [NameResolver.kt](../src/main/kotlin/org/openmbee/gearshift/engine/NameResolver.kt)
- [KerMLMetamodel.kt](../src/main/kotlin/org/openmbee/gearshift/kerml/KerMLMetamodel.kt)
- [GearshiftEngine.kt](../src/main/kotlin/org/openmbee/gearshift/GearshiftEngine.kt)
