# Plan: Fix Library Element UUID Generation to Match KerML Spec

## Problem

The current `KermlParseContext.generateDeterministicId()` uses a single-level UUID v5 scheme that diverges from the normative spec in three ways:

1. **Wrong namespace UUID**: Uses `6ba7b810-...` (NameSpace_DNS) instead of `6ba7b811-...` (NameSpace_URL)
2. **Missing two-level scheme**: The spec requires top-level packages to use a different name format (URL-based) than contained elements (path-based)
3. **Unnamed elements use random UUID in context key** (line 140), which defeats determinism and won't interoperate with other tools

## Spec Requirements (Clause 10)

### Top-level library package:
- **namespace**: `NameSpace_URL` UUID = `6ba7b811-9dad-11d1-80b4-00c04fd430c8`
- **name**: `https://www.omg.org/spec/KerML/<packageName>` (UTF-8)

### Contained elements:
- **namespace**: UUID of the top-level library package (computed above)
- **name**: result of `path()` operation on the element (UTF-8)

### `path()` operation (already defined in metamodel):
- **Element**: If `qualifiedName != null`, return it. Else if `owningRelationship != null`, return `owningRelationship.path() + '/' + indexOf(self)`. Else `""`.
- **Relationship**: If `owningRelationship == null && owningRelatedElement != null`, return `owningRelatedElement.path() + '/' + indexOf(self)`. Else delegate to Element's path.
- **OwningMembership**: If `ownedMemberElement.qualifiedName != null`, return `qualifiedName + '/owningMembership'`. Else delegate to Relationship's path.

## Implementation Plan

### Step 1: Fix `KermlParseContext` UUID generation

**File**: `gearshift-kerml-runtime/src/main/kotlin/org/openmbee/gearshift/kerml/parser/KermlParseContext.kt`

Changes:
1. Fix the namespace UUID constant: `6ba7b810-...` → `6ba7b811-9dad-11d1-80b4-00c04fd430c8`
2. Add `KERML_SPEC_URL_PREFIX` constant: `https://www.omg.org/spec/KerML/`
3. Add `generateTopLevelLibraryPackageId(packageName: String)` — uses NameSpace_URL + URL
4. Modify `generateDeterministicId` to take a `topLevelPackageUUID: UUID` parameter — for contained elements, use that as the namespace UUID and `path` as the name
5. Add a `computePathDuringParsing()` helper that replicates `path()` logic using parse-time context (qualified name, parent, position info) rather than OCL evaluation

### Step 2: Update the `create()` method's ID generation logic

**File**: Same as Step 1

The `create()` method currently has three branches. Update to:

```kotlin
val elementId = if (isLibraryContext) {
    if (isTopLevelLibraryPackage) {
        // Top-level package: namespace=NameSpace_URL, name=URL
        generateTopLevelLibraryPackageId(declaredName!!)
    } else {
        // Contained element: namespace=topLevelPkgUUID, name=path()
        val path = computePathDuringParsing(declaredName, typeName)
        generateDeterministicId(topLevelPackageUUID!!, path)
    }
} else {
    UUID.randomUUID().toString()
}
```

This requires threading the top-level package UUID through the parse context.

### Step 3: Thread `topLevelPackageUUID` through parse context

Add fields to `KermlParseContext`:
- `topLevelLibraryPackageName: String?` — e.g., "Base", "Occurrences"
- `topLevelLibraryPackageUUID: UUID?` — computed when parsing a top-level LibraryPackage

When `LibraryPackageVisitor` creates a top-level library package:
1. Compute its UUID using `generateTopLevelLibraryPackageId(packageName)`
2. Store it on the parse context so all child elements can use it

### Step 4: Implement `computePathDuringParsing()`

This replicates the `path()` operation using parse-time information. During parsing we know:
- `qualifiedName` (built from namespace stack)
- `parentQualifiedName`
- For unnamed elements: we can track child position via a counter on the parse context

For named elements: `path = qualifiedName` (same as the OCL `path()` when `qualifiedName != null`)

For unnamed elements (Relationships, Memberships, etc.): we need position tracking. Add a `childPositionCounter: MutableMap<String, Int>` to the context that tracks how many unnamed children have been created under each parent, giving us the 1-based index needed for `path()`.

For OwningMembership: `path = ownedMemberElement.qualifiedName + '/owningMembership'`

### Step 5: Update `LibraryPackageVisitor`

**File**: `gearshift-kerml-runtime/.../parser/visitors/LibraryPackageVisitor.kt`

When visiting a top-level `LibraryPackage`, compute the top-level UUID and pass it to child contexts.

### Step 6: Update tests

Verify that the generated UUIDs match expected values. We can compute expected UUIDs manually for well-known library elements:
- `Base` package: UUID v5(NameSpace_URL, "https://www.omg.org/spec/KerML/Base")
- `Base::Anything`: UUID v5(Base_pkg_UUID, "Base::Anything")
- `Base::things`: UUID v5(Base_pkg_UUID, "Base::things")

### Step 7: Update MEMORY.md

Document the normative UUID generation scheme.

## Files to Modify

1. `gearshift-kerml-runtime/.../parser/KermlParseContext.kt` — Main changes (Steps 1-4)
2. `gearshift-kerml-runtime/.../parser/visitors/LibraryPackageVisitor.kt` — Thread top-level UUID (Step 5)
3. Possibly `gearshift-kerml-runtime/.../parser/visitors/base/BaseElementVisitor.kt` — If it has ID generation logic

## Risks

- **Path computation at parse-time vs runtime**: The `path()` OCL operation uses runtime model navigation. We need to replicate this using parse-time context, which should be straightforward for named elements (just use qualifiedName) but requires careful handling of unnamed elements (position-based indexing).
- **Unnamed element ordering**: The spec uses `indexOf(self)` in `ownedRelatedElement` / `ownedRelationship` collections. During parsing, we need to track creation order to replicate this. If the parser creates elements in a different order than expected, UUIDs will differ.
- **Existing tests**: IDs of library elements will change, which may break tests that hardcode element IDs.