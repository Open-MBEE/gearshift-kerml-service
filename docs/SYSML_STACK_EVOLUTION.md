# Stack Evolution: KerML -> KerML + SysML v2

This document shows how the architecture layers evolve when SysML v2 is added alongside KerML.

## Current State: KerML Only

```
┌─────────────────────────────────────────────────────────────────┐
│                     Application Layer                           │
│  REST API (Ktor), Code Generation, KerML Parser & Writer       │
│                   [gearshift-kerml-runtime]                     │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│              Execution & Analysis Layer                         │
│  Layer 3: Z3/SMT Solver (Constraint Solving, Trade Studies)    │
│  Layer 2: Behavior Execution Engine (Token-passing)            │
│  Layer 1: KerML Expression Evaluator (Native tree walk)        │
│       [gearshift-kerml-runtime + mdm-framework]                │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│           KerML Metamodel Layer (85+ metaclasses)               │
│  Root: Element, Namespace, Membership, Relationship             │
│  Core: Type, Classifier, Feature, Specialization                │
│  Kernel: DataType, Class, Expression, Connector, Flow           │
│               [gearshift-kerml-model]                           │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│     Generated Typed API (Interfaces + Implementations)          │
│                   [kerml-generated]                              │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                     Mdm Framework                               │
│                      MDMEngine                                  │
│            (Unified Runtime / Single Entry Point)               │
└─────────────────────────────────────────────────────────────────┘
        │           │           │           │           │
   ┌────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
   │Metamodel│ │ Runtime  │ │Constraint│ │  Query   │ │  Code    │
   │Registry │ │ (Mount)  │ │  Engine  │ │  Engine  │ │Generation│
   └────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘
        │           │           │           │
        │     ┌──────────┐ ┌──────────┐ ┌──────────┐
        │     │   Name   │ │   OCL    │ │  Z3/SMT  │
        │     │ Resolver │ │ Executor │ │  Solver  │
        │     └──────────┘ └──────────┘ └──────────┘
        └───────────────────────────────────────────┘
                              │
                    ┌──────────────────┐
                    │  Graph Storage   │
                    │ (InMemory / DB)  │
                    └──────────────────┘
```

## Target State: KerML + SysML v2

The key insight is that SysML v2 is a **layer on top of KerML**, not a replacement. SysML metaclasses
(Definition, Usage, etc.) extend KerML metaclasses (Classifier, Feature, etc.). The Mdm Framework and
execution layers remain unchanged — they are language-agnostic by design.

```
┌─────────────────────────────────────────────────────────────────┐
│                     Application Layer                           │
│                                                                 │
│  ┌──────────────────────────┐ ┌──────────────────────────────┐ │
│  │    KerML Services        │ │       SysML v2 Services       │ │
│  │  Parser, Writer,         │ │  Parser, Writer,              │ │
│  │  Library Loader          │ │  Library Loader,              │ │
│  │                          │ │  Requirements Trace,          │ │
│  │                          │ │  Allocation Analysis          │ │
│  └──────────────────────────┘ └──────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              SysML v2 REST API                            │  │
│  │  Projects, Commits, Branches, Elements, Relationships     │  │
│  │  (JSON-LD @id/@type format per SysML v2 API spec)         │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │         Code Generation (Kotlin + TypeScript)             │  │
│  │    KerML interfaces    │    SysML v2 interfaces           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│      [gearshift-kerml-runtime + gearshift-sysml-runtime]       │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│              Execution & Analysis Layer                         │
│                                                                 │
│  Layer 3: Z3/SMT Solver (Constraint Solving, Trade Studies)    │
│  Layer 2: Behavior Execution Engine (Token-passing)            │
│  Layer 1: KerML Expression Evaluator (Native tree walk)        │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  NEW: SysML-specific Analysis Extensions                  │  │
│  │  - Requirement verification tracing                       │  │
│  │  - Allocation consistency checking                        │  │
│  │  - Interface compatibility analysis                       │  │
│  │  - Timing/resource constraint propagation                 │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│       [gearshift-kerml-runtime + gearshift-sysml-runtime       │
│        + mdm-framework]                                        │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                   Metamodel Layer                                │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  SysML v2 Metamodel (~100+ metaclasses)                   │  │
│  │  Definitions: PartDef, ActionDef, ItemDef, PortDef, ...   │  │
│  │  Usages: PartUsage, ActionUsage, ItemUsage, PortUsage,... │  │
│  │  Requirements, Constraints, Analysis Cases, Allocations   │  │
│  │  View/Viewpoint, Rendering, Metadata                      │  │
│  │               [gearshift-sysml-model]                     │  │
│  └──────────────────────────┬───────────────────────────────┘  │
│                     extends │                                   │
│  ┌──────────────────────────┴───────────────────────────────┐  │
│  │  KerML Metamodel (85+ metaclasses)                        │  │
│  │  Root: Element, Namespace, Membership, Relationship        │  │
│  │  Core: Type, Classifier, Feature, Specialization           │  │
│  │  Kernel: DataType, Class, Expression, Connector, Flow      │  │
│  │               [gearshift-kerml-model]                     │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│     Generated Typed API (Interfaces + Implementations)          │
│                                                                 │
│  ┌─────────────────────┐  ┌──────────────────────────────────┐ │
│  │  KerML Interfaces   │  │  SysML v2 Interfaces             │ │
│  │  [kerml-generated]  │  │  [sysml-generated]               │ │
│  └─────────────────────┘  └──────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                     Mdm Framework                               │
│                      MDMEngine                                  │
│            (Unified Runtime / Single Entry Point)               │
│                                                                 │
│                    *** UNCHANGED ***                             │
│  The framework is metamodel-agnostic. KerML and SysML are      │
│  both just MetaClass/MetaAssociation definitions registered    │
│  in the same MetamodelRegistry. The engine, constraint system, │
│  OCL executor, mount system, and storage all work identically. │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
        │           │           │           │           │
   ┌────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
   │Metamodel│ │ Runtime  │ │Constraint│ │  Query   │ │  Code    │
   │Registry │ │ (Mount)  │ │  Engine  │ │  Engine  │ │Generation│
   └────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘
        │           │           │           │
        │     ┌──────────┐ ┌──────────┐ ┌──────────┐
        │     │   Name   │ │   OCL    │ │  Z3/SMT  │
        │     │ Resolver │ │ Executor │ │  Solver  │
        │     └──────────┘ └──────────┘ └──────────┘
        └───────────────────────────────────────────┘
                              │
                    ┌──────────────────┐
                    │  Graph Storage   │
                    │ (InMemory / DB)  │
                    └──────────────────┘
```

## New Modules

The SysML addition introduces **4 new modules** alongside the existing 4:

```
gearshift-kerml-service/
│
│  Existing (unchanged):
├── mdm-framework/              # Core framework — UNCHANGED
├── gearshift-kerml-model/      # KerML metaclasses — UNCHANGED
├── kerml-generated/            # Generated KerML code — UNCHANGED
├── gearshift-kerml-runtime/    # KerML parser/writer/eval — UNCHANGED
│
│  New:
├── gearshift-sysml-model/      # SysML v2 metaclass definitions
├── sysml-generated/            # Generated SysML v2 typed interfaces
├── gearshift-sysml-runtime/    # SysML v2 parser, writer, analysis
│
│  Evolved:
└── settings.gradle.kts         # Adds 3 new module includes
```

**Module dependency graph (with SysML):**

```
                mdm-framework
                      ↑
            ┌─────────┴──────────┐
   gearshift-kerml-model    (no SysML dep on framework directly)
            ↑                    │
     kerml-generated             │
            ↑                    │
   gearshift-kerml-runtime       │
            ↑                    │
            │         gearshift-sysml-model ←── depends on kerml-model
            │                    ↑
            │            sysml-generated ←── depends on kerml-generated
            │                    ↑
            └──── gearshift-sysml-runtime ←── depends on kerml-runtime
```

## What Changes Per Layer

### Mdm Framework — NO CHANGES

The framework is metamodel-agnostic by design. SysML metaclasses are just more `MetaClass` and
`MetaAssociation` entries in the same `MetamodelRegistry`. All existing infrastructure works as-is:

- `MDMEngine` handles SysML instances identically to KerML instances
- `ConstraintEngine` evaluates SysML constraints via the same OCL executor
- `MountableEngine` mounts SysML standard libraries the same way as KerML libraries
- `MetamodelCodeGenerator` and `TypeScriptCodeGenerator` generate SysML interfaces unchanged
- `OwnershipResolver` resolves SysML ownership intermediates (e.g., `DefinitionMembership`)
- `GraphStorage` stores SysML elements and links without modification

### Metamodel Layer — EXTENDS

SysML metaclasses extend KerML metaclasses. Key inheritance chains:

```
KerML                          SysML v2
─────                          ────────
Classifier ──────────────────► Definition
  ├── Class ─────────────────► PartDefinition, ActionDefinition, ...
  ├── DataType ──────────────► AttributeDefinition, EnumerationDefinition
  ├── Association ───────────► ConnectionDefinition, AllocationDefinition
  ├── Behavior ──────────────► ActionDefinition (also extends Class)
  └── Structure ─────────────► ItemDefinition, PortDefinition

Feature ─────────────────────► Usage
  ├── Step ──────────────────► ActionUsage, CalculationUsage
  ├── Connector ─────────────► ConnectionUsage, InterfaceUsage, AllocationUsage
  └── (Feature directly) ───► PartUsage, ItemUsage, PortUsage, AttributeUsage

Expression ──────────────────► CalculationUsage (also a Step)
BooleanExpression ───────────► ConstraintUsage
Invariant ───────────────────► RequirementUsage (constraint checking)
```

SysML also adds new association files for Definition/Usage bindings, requirement traces,
allocation links, and view/viewpoint membership.

### Metamodel Registration — LAYERED

```kotlin
fun initializeSysMLMetamodel(registry: MetamodelRegistry) {
    // 1. KerML must be initialized first (SysML extends it)
    KerMLMetamodelLoader.initialize(registry)

    // 2. Register SysML metaclasses (they reference KerML superclasses)
    registerSysMLDefinitions(registry)
    registerSysMLUsages(registry)
    registerSysMLRequirements(registry)
    registerSysMLAllocations(registry)
    registerSysMLViews(registry)

    // 3. Register SysML associations
    registerSysMLAssociations(registry)
}
```

### Execution & Analysis Layer — EXTENDS

The three execution layers work as-is for SysML (since SysML Expressions are KerML Expressions).
New SysML-specific analysis services would be added:

- **Requirement Verification Tracing**: Walk `satisfy`/`verify` relationships between
  RequirementUsage and ConstraintUsage, check satisfaction status
- **Allocation Consistency**: Validate that AllocationUsage links are type-compatible
  and that all required allocations are present
- **Interface Compatibility**: Check PortUsage compatibility across ConnectionUsages
- **Timing/Resource Propagation**: Propagate constraint values through part decomposition
  hierarchies using the Z3 solver

### Application Layer — PARALLEL SERVICES

SysML gets its own parser, writer, and library loader alongside KerML's:

- **SysML Parser**: Extends KerML parser with SysML-specific grammar rules
  (definition/usage syntax, requirement text blocks, etc.)
- **SysML Writer**: Extends KerML writer with SysML textual notation
- **SysML Library Loader**: Loads SysML standard libraries (ISQ, SI units, etc.)
  as implicit mounts alongside KerML kernel libraries
- **SysML v2 REST API**: Already partially implemented — serves projects, commits,
  elements in JSON-LD format per the SysML v2 API specification

### Mount System — EXTENDED LIBRARY SET

```
Current mounts (KerML only):
  - KerML Kernel Semantic Library (Base, Kernel, Links, Occurrences, ...)

With SysML:
  - KerML Kernel Semantic Library    (implicit, priority 1000)
  - SysML Standard Library           (implicit, priority 1100)
  - ISQ/SI Units Library             (implicit, priority 1200)
  - User project libraries           (explicit, priority 100-499)
```

### Code Generation — ADDITIONAL OUTPUT

Both generators produce SysML interfaces in addition to KerML:

```
kerml-generated/                    sysml-generated/
├── interfaces/                     ├── interfaces/
│   ├── Element.kt                  │   ├── Definition.kt
│   ├── Feature.kt                  │   ├── Usage.kt
│   ├── Type.kt                     │   ├── PartDefinition.kt
│   └── ... (86 files)              │   ├── PartUsage.kt
├── impl/                           │   └── ... (~100 files)
│   └── ... (85 files)              ├── impl/
└── KerMLElementFactory.kt          │   └── ... (~100 files)
                                    └── SysMLElementFactory.kt

build/generated-ts/
├── kerml.model.ts                  # KerML interfaces (existing)
├── kerml-metaclass.type.ts         # KerML union type (existing)
├── sysml.model.ts                  # SysML interfaces (NEW)
└── sysml-metaclass.type.ts         # SysML union type (NEW)
```

## Current Progress

SysML scaffolding already exists in the codebase:

| Component                   | Status                                                         |
|-----------------------------|----------------------------------------------------------------|
| SysML metaclass definitions | 34 classes defined (Definitions + Usages)                      |
| SysML associations          | Not yet defined                                                |
| SysMLMetamodelLoader        | Placeholder (empty)                                            |
| SysML parser/writer         | Not yet started                                                |
| SysML standard libraries    | Not yet started                                                |
| SysML v2 REST API           | Partially implemented (projects, commits, elements)            |
| SysML code generation       | Not yet started (generators are ready, just need registration) |
| SysML semantic bindings     | Not yet defined                                                |
