# Storage Architecture

## Overview

The Gearshift storage system uses a hot/cold architecture with a clear separation between live operational data and historical snapshots.

## Core Principles

### 1. Objects as Source of Truth

MDMObjects store all data, including relationship values via navigable association ends:

```
MDMObject:
  id: "obj-1"
  type: "Class"
  properties:
    declaredName: "Vehicle"
    ownedMembership: ["mem-1", "mem-2"]  // Navigable end values stored here
```

### 2. Edges are Derived

Edge storage is an index derived from navigable end properties, not independent data:

- **Purpose**: Enable reverse traversal for non-navigable ends
- **Sync**: Automatically maintained when properties change
- **Cache**: Store computed values for derived ends

```
EdgeData (derived/indexed for traversal):
  sourceId: "obj-1"
  targetId: "mem-1"
  associationName: "MembershipOwningNamespace_OwnedMembership"
```

### 3. Bidirectional Synchronization

When setting a navigable end property, the system automatically:

1. Updates the object's property
2. Updates the edge index
3. Updates the opposite end (if navigable)
4. Removes from old relationships

```kotlin
// User sets one end
membership.setProperty("membershipOwningNamespace", namespaceId)

// System automatically:
// 1. Updates membership's property
// 2. Updates edge index
// 3. Updates opposite end (if navigable)
namespace.ownedMembership.add(membershipId)
// 4. Removes from old namespace if there was one
oldNamespace?.ownedMembership.remove(membershipId)
```

The MetaAssociation definition drives this logic:
- Is opposite end navigable? → needs sync
- Multiplicity of opposite end → add to collection vs replace single value
- Old value handling → remove from previous relationship

## Hot/Cold Storage Pattern

### Hot Store (Graph)

- **Content**: Current state only
- **Optimized for**: Traversal, relationships
- **Technology**: Neo4j / In-memory
- **Query**: "What is it now?"

```
Node Labels: [Node, {type}]
Properties: id, ...all properties
Relationships: Derived edges for traversal
```

### Cold Store (JSON)

- **Content**: All historical snapshots
- **Optimized for**: Search, audit, history
- **Technology**: Elasticsearch / temporal DB
- **Query**: "What was it at time X?"

```kotlin
data class NodeJson(
    val id: String,
    val type: String,
    val properties: Map<String, Any?>,
    val commitId: String,
    val timestamp: Instant,
    val branchId: String
)
```

## Data Flow

```
                         setProperty()
                              │
                              ▼
┌──────────────────────────────────────────────────────────────┐
│                      MDMObject                               │
│  - Properties (navigable ends = source of truth)             │
│  - Bidirectional sync on write                               │
└──────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
┌─────────────────────────┐     ┌─────────────────────────────┐
│   Edge Index (derived)  │     │   NodeJson (on commit)      │
│   - Reverse traversal   │     │   - Snapshot document       │
│   - Non-navigable ends  │     │   - commitId, timestamp     │
│   - Derived end cache   │     │   - Denormalized            │
└─────────────────────────┘     └─────────────────────────────┘
              │                               │
              ▼                               ▼
┌─────────────────────────┐     ┌─────────────────────────────┐
│   HOT: Graph (Neo4j)    │     │   COLD: JSON (Elastic)      │
│   - Current state       │     │   - All history             │
│   - Traversal           │     │   - Search/audit            │
└─────────────────────────┘     └─────────────────────────────┘
```

## API Design

### Public API (what users call)

```kotlin
// Set a navigable end property
object.setProperty("ownedMembership", listOf(mem1Id, mem2Id))

// Read navigable end (direct property access)
object.getProperty("ownedMembership")

// Read non-navigable end (uses edge index)
storage.getSources(objectId, associationName)
```

### Internal (triggered automatically)

- Edge index updated as side effect of property changes
- Old edges removed, new edges created
- Derived ends invalidated/recomputed
- NodeJson snapshots created on commit

## Versioning Model

Aligned with SysML v2 API:

- **Project**: Contains branches, has unique ID
- **Branch**: Points to HEAD commit
- **Commit**: Contains changes with KerML payload

The graph (hot store) always represents HEAD of the current branch. History lives in the cold store.

## Git-Like Architecture: KerML as Commit Payload

### KerML is Like SQL

KerML is a **declarative language**, not a storage format. Think of it like SQL:

- `*.sql` files define schema changes and data mutations
- `*.kerml` files define model changes and element definitions

Both are **executed** against a current state to produce a new state. Neither is the "source of truth" for the live data—the materialized database/graph is.

### Commit Structure

```
Commit {
  id: "commit-3"
  parentId: "commit-2"
  branchId: "main"
  timestamp: "2026-01-15T10:30:00Z"
  payload: """
    package Vehicles {
      class Car :> Vehicle {
        feature wheels : Integer = 4;
      }
    }
  """  // *.kerml IS the change
}
```

The `payload` field contains raw KerML text—the declarative description of what to create, modify, or extend.

### Data Flow with Commits

```
┌─────────────────────────────────────────────────────────────────┐
│                     Git Repository                               │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐                   │
│  │ commit-1 │───▶│ commit-2 │───▶│ commit-3 │                   │
│  │ (kerml)  │    │ (kerml)  │    │ (kerml)  │                   │
│  └──────────┘    └──────────┘    └──────────┘                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼ Apply all commits in order
┌─────────────────────────────────────────────────────────────────┐
│                   Graph (Materialized State)                     │
│                                                                  │
│   (Namespace)──owns──▶(Class: Vehicle)                          │
│        │                    │                                    │
│        └──owns──▶(Class: Car)──specializes──┘                   │
│                       │                                          │
│                       └──owns──▶(Feature: wheels)               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼ Snapshot on commit (optional)
┌─────────────────────────────────────────────────────────────────┐
│               NodeJson (Searchable Index)                        │
│                                                                  │
│   { id: "car-1", type: "Class", name: "Car", ... }              │
│   { id: "wheels-1", type: "Feature", name: "wheels", ... }      │
└─────────────────────────────────────────────────────────────────┘
```

### Three Storage Tiers

| Tier | Content | Technology | Purpose |
|------|---------|------------|---------|
| **Git** | KerML payloads in commits | Git/GitHub | Version history, collaboration, branching |
| **Graph** | Materialized current state | Neo4j/In-memory | Live traversal, relationship queries |
| **JSON** | Snapshots per commit | Elasticsearch | Full-text search, historical queries |

### Why This Architecture?

1. **KerML is human-readable**: Diffs are meaningful, code review works naturally
2. **Git provides branching**: Standard merge/rebase workflows for model changes
3. **Graph is fast**: Current state queries don't need to replay history
4. **JSON is searchable**: "Find all Classes named 'Vehicle'" across all time

### Rebuilding State

To get the graph state at any point:

```kotlin
fun materializeAtCommit(commitId: String): GraphStorage {
    val storage = InMemoryGraphStorage()
    val commits = getCommitChain(commitId)  // Walk back to root

    commits.forEach { commit ->
        val ast = parseKerML(commit.payload)
        applyToGraph(ast, storage)  // Create/update nodes and edges
    }

    return storage
}
```

For performance, NodeJson snapshots can serve as checkpoints to avoid replaying from the beginning.

## Key Files

- `src/main/kotlin/org/openmbee/gearshift/storage/GraphStorage.kt` - Storage interface
- `src/main/kotlin/org/openmbee/gearshift/storage/InMemoryGraphStorage.kt` - In-memory implementation
- `src/main/kotlin/org/openmbee/gearshift/MDMModelFactory.kt` - Project/model factory with metadata
