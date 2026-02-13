# KerML Name Resolution - Full Specification Implementation

This document provides complete implementation notes for KerML 8.2.3.5 Name Resolution, including all subsections.

## Table of Contents

1. [Overview (8.2.3.5.1)](#overview)
2. [Local and Global Namespaces (8.2.3.5.2)](#local-and-global-namespaces)
3. [Local and Visible Resolution (8.2.3.5.3)](#local-and-visible-resolution)
4. [Full Resolution (8.2.3.5.4)](#full-resolution)
5. [Implementation Status](#implementation-status)

## Overview (8.2.3.5.1)

### Qualified Names

A qualified name consists of:

- One or more **segment names** (simple NAMEs)
- Optional **qualification part** (all segments except the last)
- Optional **global scope qualifier** (`$` prefix)

Examples:

```
A           -> segments: [A]
A::B::C     -> segments: [A, B, C], qualification: A::B
$::A::B::C  -> global scope, segments: [A, B, C]
```

### Resolution Result

Name resolution returns a **Membership**, but in most cases the required Element is the `memberElement` of that
Membership.

**Exception**: For `importedMembership` of a `MembershipImport`, the required Element is the Membership itself.

### Basic Resolution Process

**✅ Implemented in `NameResolver.doResolve()`**

1. **Single segment, no global scope**:
   ```kotlin
   if (segments.size == 1 && !hasGlobalScope) {
       if (isRootNamespace(localNamespace)) {
           fullResolution(segment, globalNamespace)
       } else {
           fullResolution(segment, localNamespace)
       }
   }
   ```

2. **Single segment with global scope** (`$::Name`):
   ```kotlin
   if (segments.size == 1 && hasGlobalScope) {
       fullResolution(segment, globalNamespace)
   }
   ```

3. **Multi-segment qualified name** (`A::B::C`):
   ```kotlin
   if (segments.size > 1) {
       // Resolve qualification part (A::B)
       val qualPartResult = resolve(qualificationPart, localNamespace)
       // Must be a Namespace
       require(isNamespace(qualPartResult.memberElement))
       // Resolve last segment (C) relative to namespace
       visibleResolution(lastSegment, qualPartResult.memberElement)
   }
   ```

### Redefinition Context

**✅ Implemented in `resolveInRedefinitionContext()`**

For `redefinedFeature` of a `Redefinition` with an `owningFeature` that has an `owningType`:

- Repeat basic resolution with each general Type's `ownedSpecialization`
- Use general Type as localNamespace
- Continue until resolution found

### Implementation Notes

Per specification notes:

1. **Incremental Resolution**: ✅ Implemented
    - Resolution stack prevents infinite loops
    - Can resolve to Membership without immediately resolving memberElement
    - Handles circular dependencies

2. **Circularity Handling**: ✅ Implemented
    - Visited sets when traversing Imports/Specializations
    - Thread-local resolution stack
    - Graceful handling of cycles

## Local and Global Namespaces (8.2.3.5.2)

### Root Namespace

Every Namespace except a **root Namespace** is nested in an **owningNamespace**.

A root Namespace has an implicit containing Namespace called its **globalNamespace**.

### Global Namespace Contents

The globalNamespace includes:

- All **visible Memberships** of all **available** root Namespaces
- At minimum: all root Namespaces from KerML Model Libraries (Clause 9)
- If tool imports a model interchange project: all root Namespaces from used projects
- Tool may provide additional means for making Namespaces available

**Implementation Status**: ⏳ Partial

- `findGlobalNamespace()` placeholder exists
- Full model library loading not yet implemented
- Project import mechanism not yet implemented

### Local Namespace Determination

The localNamespace for resolving a qualified name depends on the context Relationship:

#### Import (8.3.2.4.2)

```kotlin
localNamespace = import.importOwningNamespace
```

**Status**: ✅ Specified in metamodel

#### Membership (8.3.2.4.3)

**Status**: ⏳ Requires Expression metamodel

Complex rules for:

- FeatureReferenceExpression (use non-invocation Namespace)
- InstantiationExpression (use non-invocation Namespace)
- FeatureChainExpression (use result parameter)
- Otherwise: membershipOwningNamespace

**Non-invocation Namespace**: Nearest containing Namespace that is NOT:

- FeatureReferenceExpression
- InstantiationExpression
- ownedFeature of InstantiationExpression
- ownedFeature of result of ConstructorExpression

#### Specialization (8.3.3.1.8)

```kotlin
if (isReferenceSubsetting && referencingFeature.isEndFeature &&
    referencingFeature.owningType is Connector) {
    localNamespace = connector.owningNamespace
} else if (owningType != null) {
    localNamespace = owningType.owningNamespace
} else {
    localNamespace = specialization.owningNamespace
}
```

**Status**: ⏳ Requires Connector, ReferenceSubsetting metamodel

#### Conjugation (8.3.3.1.2)

```kotlin
if (owningType != null) {
    localNamespace = owningType.owningNamespace
} else {
    localNamespace = conjugation.owningNamespace
}
```

**Status**: ⏳ Requires Conjugation metamodel

#### FeatureChaining (8.3.3.3.5)

```kotlin
if (isFirstOwnedFeatureChaining) {
    // Determine as if owningRelationship was context
    localNamespace = determineFromOwningRelationship()
} else {
    localNamespace = previousFeatureChaining.chainingFeature
}
```

**Status**: ⏳ Requires FeatureChaining metamodel

## Local and Visible Resolution (8.2.3.5.3)

### Local Resolution

**✅ Implemented in `resolveInOwnedMemberships()` and related methods**

A Namespace defines a mapping from names to its memberships.

Each Membership is the local resolution for:

- `memberShortName` (if non-null)
- `memberName` (if non-null)

Includes:

- Owned Memberships
- Imported Memberships
- Inherited Memberships (if Namespace is a Type)

**Well-formed constraint**: At most one Membership resolves any given name.

### Visible Resolution

**✅ Implemented in `visibleResolution()`**

Visible Memberships comprise:

1. All `ownedMembership` with `visibility = public`
2. All `importedMembership` derived from Imports with `visibility = public`
3. If Namespace is a Type: all `inheritedMembership` with `visibility = public`

```kotlin
fun visibleResolution(name: String, namespace: MDMObject): ResolutionResult? {
    // Search owned memberships with visibility = public
    resolveInOwnedMemberships(name, namespace)?.let {
        if (isPublicVisibility(it.membership)) return it
    }

    // Search imported memberships with visibility = public
    resolveInImportedMemberships(name, namespace)?.let {
        if (isPublicVisibility(it.membership)) return it
    }

    // If Type, search inherited memberships with visibility = public
    if (isType(namespace)) {
        resolveInInheritedMemberships(name, namespace)?.let {
            if (isPublicVisibility(it.membership)) return it
        }
    }

    return null
}
```

**Current Implementation**: Visibility checking not yet enforced (all treated as public)

## Full Resolution (8.2.3.5.4)

**✅ Implemented in `fullResolution()`**

Full resolution considers Memberships in:

- The target Namespace
- All directly or indirectly containing Namespaces
- Up to the globalNamespace

### Algorithm

For Namespace other than globalNamespace:

1. **Check local resolution**:
   ```kotlin
   if (hasLocalResolution(name, namespace)) {
       return localResolution(name, namespace)
   }
   ```

2. **Recurse upward**:
   ```kotlin
   if (!isRootNamespace(namespace)) {
       return fullResolution(name, namespace.owningNamespace)
   } else {
       return resolveInGlobalNamespace(name)
   }
   ```

### Global Namespace Resolution

Resolution in globalNamespace returns the Membership whose:

- `shortMemberName` equals the simple name, OR
- `memberName` equals the simple name

**Non-deterministic note**: If multiple Memberships resolve the name, one is chosen but which one is
implementation-defined.

```kotlin
fun resolveInGlobalNamespace(name: String): ResolutionResult? {
    val globalNs = findGlobalNamespace()
    val allRootNamespaces = getAllRootNamespaces()

    // Collect all visible memberships from all root namespaces
    val candidates = allRootNamespaces.flatMap { rootNs ->
        getVisibleMemberships(rootNs)
            .filter { membership ->
                membership.memberShortName == name ||
                membership.memberName == name
            }
    }

    // Non-deterministic: choose one if multiple exist
    return candidates.firstOrNull()?.let { membership ->
        ResolutionResult(
            membershipId = getMembershipId(membership),
            membership = membership,
            memberElement = membership.memberElement
        )
    }
}
```

## Implementation Status

### ✅ Fully Implemented

1. **Basic Resolution Process (8.2.3.5.1)**
    - Single segment resolution
    - Multi-segment resolution
    - Global scope qualifier
    - Redefinition context
    - Circularity prevention

2. **Visible Resolution (8.2.3.5.3)**
    - Owned memberships
    - Imported memberships
    - Inherited memberships (for Types)

3. **Full Resolution (8.2.3.5.4)**
    - Local resolution
    - Recursive upward search
    - Global namespace resolution (basic)

### ⏳ Partially Implemented

1. **Global Namespace (8.2.3.5.2)**
    - ✅ Concept and structure
    - ⏳ KerML Model Libraries loading
    - ⏳ Project import mechanism
    - ⏳ Tool-specific namespace availability

2. **Visibility Enforcement (8.2.3.5.3)**
    - ✅ Metamodel has visibility property
    - ⏳ Enforcement in resolution (currently treats all as public)

3. **Local Namespace Determination (8.2.3.5.2)**
    - ✅ Basic Import case
    - ⏳ Expression contexts (FeatureReferenceExpression, etc.)
    - ⏳ Specialization edge cases
    - ⏳ FeatureChaining

### ❌ Not Yet Implemented

1. **Expression Metamodel**
    - FeatureReferenceExpression
    - InstantiationExpression
    - FeatureChainExpression
    - ConstructorExpression

2. **Advanced Relationships**
    - ReferenceSubsetting
    - Conjugation
    - FeatureChaining

3. **memberShortName Support**
    - Currently only checks memberName
    - Need to add memberShortName to Membership metamodel

## Future Enhancements

### Priority 1: Core Functionality

1. Add `memberShortName` to Membership metaclass
2. Implement visibility checking in resolution
3. Create global namespace with KerML standard libraries

### Priority 2: Expression Support

1. Define Expression metaclass hierarchy
2. Implement non-invocation namespace determination
3. Handle FeatureReferenceExpression context

### Priority 3: Advanced Features

1. Model interchange project support
2. Used project dependencies
3. Tool-specific namespace availability mechanisms
4. Performance optimization (caching)

## Testing Recommendations

### Unit Tests Needed

1. **Basic Resolution**
   ```kotlin
   testSingleSegmentResolution()
   testMultiSegmentResolution()
   testGlobalScopeResolution()
   testRedefinitionContext()
   ```

2. **Visibility**
   ```kotlin
   testPublicMembershipsVisible()
   testPrivateMembershipsHidden()
   testImportedVisibility()
   ```

3. **Full Resolution**
   ```kotlin
   testResolutionInNestedNamespaces()
   testResolutionFallbackToGlobal()
   testLocalResolutionTakesPrecedence()
   ```

4. **Edge Cases**
   ```kotlin
   testCircularImports()
   testCircularSpecializations()
   testNonDeterministicGlobalResolution()
   ```

## References

- KerML Specification v1.0, Section 8.2.3.5: Name Resolution
- [NameResolver.kt](../src/main/kotlin/org/openmbee/gearshift/engine/NameResolver.kt) - Implementation
- [KerMLMetamodel.kt](../src/main/kotlin/org/openmbee/gearshift/kerml/KerMLMetamodel.kt) - Metamodel
- [Name Resolution](../architecture/name-resolution.md) - User guide
