# KerML Metamodel Consolidation

## Overview

The KerML metamodel has been consolidated from a monolithic approach to a modular, file-per-class structure.

## What Changed

### Old Approach: `KerMLMetamodel.kt` ❌ DEPRECATED

Single 366-line file with inline class definitions:

```kotlin
// OLD - DON'T USE
import org.openmbee.gearshift.kerml.KerMLMetamodel

KerMLMetamodel.initialize(engine)
```

**Problems:**
- Hard to maintain (366 lines in one file)
- Incomplete (missing many KerML properties)
- No operations support
- Difficult to find specific classes
- Will become massive as more classes are added

### New Approach: `KerMLMetamodelLoader.kt` ✅ RECOMMENDED

Modular structure with individual files per class:

```kotlin
// NEW - USE THIS
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader

KerMLMetamodelLoader.initialize(engine)
```

**Benefits:**
- Easy to maintain (one file per class)
- Complete KerML spec compliance
- Supports operations (e.g., `effectiveName()`)
- Organized by package (`root`, `core`, `kernel`)
- Scalable to 100+ classes

## Structure

```
src/main/kotlin/org/openmbee/gearshift/kerml/metamodel/classes/
├── root/          # Root package (Element, Relationship, Namespace, etc.)
│   ├── Element.kt
│   ├── Relationship.kt
│   ├── Namespace.kt
│   └── ...
├── core/          # Core package (Feature, Type, Classifier, etc.)
│   ├── Feature.kt
│   ├── Type.kt
│   ├── Classifier.kt
│   └── ...
└── kernel/        # Kernel package (Definition, Usage, Expression, etc.)
    ├── Definition.kt
    ├── Usage.kt
    ├── Expression.kt
    └── ...
```

## Migration Guide

### For Application Code

**Before:**
```kotlin
import org.openmbee.gearshift.kerml.KerMLMetamodel

val engine = GearshiftEngine()
KerMLMetamodel.initialize(engine)
```

**After:**
```kotlin
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader

val engine = GearshiftEngine()
KerMLMetamodelLoader.initialize(engine)
```

### For Tests

**Before:**
```kotlin
@BeforeEach
fun setup() {
    engine = GearshiftEngine()
    KerMLMetamodel.initialize(engine)
}
```

**After:**
```kotlin
@BeforeEach
fun setup() {
    engine = GearshiftEngine()
    KerMLMetamodelLoader.initialize(engine)
}
```

## Adding New Classes

With the modular approach, adding a new class is simple:

### 1. Create the Class File

```kotlin
// src/main/kotlin/org/openmbee/gearshift/kerml/metamodel/classes/core/MyNewClass.kt
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty
import org.openmbee.gearshift.metamodel.MetaOperation

fun createMyNewClassMetaClass() = MetaClass(
    name = "MyNewClass",
    superclasses = listOf("BaseClass"),
    attributes = listOf(
        MetaProperty(
            name = "myProperty",
            type = "String",
            multiplicity = "0..1"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "myOperation",
            returnType = "String",
            body = "myProperty"
        )
    )
)
```

### 2. Register in Loader

Add to `KerMLMetamodelLoader.kt`:

```kotlin
private fun registerCorePackage(engine: GearshiftEngine) {
    // ... existing registrations ...

    engine.registerMetaClass(createMyNewClassMetaClass())
}
```

Done! No need to modify a massive file.

## Statistics

The new loader provides statistics:

```kotlin
val stats = KerMLMetamodelLoader.getStatistics(engine)
println("Total classes: ${stats["total"]}")
println("Root package: ${stats["root"]}")
println("Core package: ${stats["core"]}")
println("Kernel package: ${stats["kernel"]}")
```

## File Comparison

### Element Class Example

**Old (`KerMLMetamodel.kt`):**
```kotlin
private fun registerElement(engine: GearshiftEngine) {
    val element = MetaClass(
        name = "Element",
        attributes = listOf(
            MetaProperty(name = "elementId", ...),
            MetaProperty(name = "name", ...),
            MetaProperty(name = "qualifiedName", ...),
            MetaProperty(name = "ownedElement", ...),
            MetaProperty(name = "owner", ...)
        )
        // NO operations
    )
    engine.registerMetaClass(element)
}
```

**New (`Element.kt`):**
```kotlin
fun createElementMetaClass() = MetaClass(
    name = "Element",
    isAbstract = true,
    attributes = listOf(
        MetaProperty(name = "elementId", ...),
        MetaProperty(name = "aliasIds", ...),
        MetaProperty(name = "declaredShortName", ...),
        MetaProperty(name = "declaredName", ...),
        MetaProperty(name = "isImpliedIncluded", ...),
        MetaProperty(name = "shortName", isDerived = true, ...),
        MetaProperty(name = "name", isDerived = true, ...),
        MetaProperty(name = "qualifiedName", isDerived = true, ...),
        MetaProperty(name = "isLibraryElement", isDerived = true, ...)
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            body = "declaredName",
            isQuery = true
        )
    )
)
```

**Differences:**
- ✅ More complete (9 properties vs 5)
- ✅ Follows KerML spec
- ✅ Has operations
- ✅ Separate file (easy to find and edit)

## Current Status

- ✅ New `KerMLMetamodelLoader` created
- ✅ Old `KerMLMetamodel` deprecated with warnings
- ✅ All references updated (`Application.kt`, `KerMLParserExample.kt`)
- ✅ 108 modular class files available
- ⏳ Need to fix missing imports in some class files

## Next Steps

1. **Fix compilation errors** in modular class files (missing imports)
2. **Complete missing classes** - some functions may not be implemented yet
3. **Add operations** to other classes (following Element example)
4. **Eventually delete** `KerMLMetamodel.kt` (after deprecation period)

## Benefits Summary

| Aspect | Old Approach | New Approach |
|--------|-------------|--------------|
| **File size** | 366 lines (growing) | ~30-50 lines per class |
| **Findability** | Ctrl+F in huge file | Direct file navigation |
| **Properties** | Incomplete | Complete per spec |
| **Operations** | None | Supported |
| **Scalability** | Poor (1 massive file) | Excellent (100+ files OK) |
| **Organization** | Flat | Hierarchical packages |
| **Maintainability** | Low | High |
| **Collaboration** | Merge conflicts | Clean separation |

## Questions?

- See [ARCHITECTURE.md](ARCHITECTURE.md) for overall system design
- See [OPERATION_INVOCATION.md](OPERATION_INVOCATION.md) for operations
- See individual class files for examples
