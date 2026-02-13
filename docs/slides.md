---
marp: true
theme: default
paginate: true
backgroundColor: #fff
style: |
  section {
    font-family: 'Helvetica Neue', Arial, sans-serif;
  }
  h1 {
    color: #1a365d;
  }
  h2 {
    color: #2c5282;
  }
  code {
    background: #edf2f7;
    padding: 2px 6px;
    border-radius: 4px;
  }
  table {
    font-size: 0.85em;
  }
  .columns {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1.5em;
  }
---

# GearShift

### A Model-Intelligent KerML Engine

**From Text to Insight**

Charles Galey
February 2026

---

## Provenance and Attribution

The ideas presented here are informed by my prior experience in systems engineering and language standardization.

The software implementation discussed today was developed independently by me in my personal capacity.

Following this presentation, the source code will be contributed to the OpenMBEE project under the Apache 2.0 license.

---

## The Problem

Systems engineers work with **models** — but today's tools treat them as passive data.

- Models are stored but not **understood**
- Constraint checking is manual or bolted on
- Trade studies happen in spreadsheets, disconnected from the model
- Conflicting requirements hide until integration
- Diagrams are static pictures, not live views

**What if the tool actually _reasoned_ about your model?**

---

## What is GearShift?

A **model-intelligent** KerML engine that doesn't just store models — it understands them.

| Capability | Description |
|---|---|
| **Parse** | Full KerML textual notation parser (85 metaclasses, 127 associations) |
| **Visualize** | Interactive diagrams generated from the live model |
| **Reason** | Constraint solving, optimization, and conflict detection via Z3/SMT |
| **Generate** | Kotlin + TypeScript code from a single metamodel definition |
| **Execute** | Behavior simulation with token-passing execution |

---

## Architecture at a Glance

```
                     ┌─────────────────────────────┐
                     │     View Editor (Angular)    │  ← Interactive UI
                     └──────────────┬──────────────┘
                                    │ REST API
                     ┌──────────────┴──────────────┐
                     │    KerML Runtime (Ktor)      │  ← Parser, Writer, API
                     ├─────────────────────────────┤
                     │  Execution & Analysis Layer  │
                     │  Expression │ Behavior │ Z3  │  ← Intelligence
                     ├─────────────────────────────┤
                     │  KerML Metamodel (85 classes)│  ← Single Source of Truth
                     ├─────────────────────────────┤
                     │      Mdm Framework           │
                     │  Engine │ OCL │ Graph Store   │  ← Core Infrastructure
                     └─────────────────────────────┘
```

---

## Act 1: The Language

**We parse the full KerML specification.**

```
package SpacecraftDesign {

    abstract class Component {
        feature mass : ScalarValues::Integer;
        feature powerDraw : ScalarValues::Integer;
    }

    class Propulsion :> Subsystem {
        feature thrust : ScalarValues::Integer;
        feature specificImpulse : ScalarValues::Integer;
    }
}
```

- ANTLR-based parser with 80+ visitor classes
- Instant parse with element count and error reporting
- Full element tree: packages, classes, features, associations, behaviors

---

## Act 1: What "Full KerML" Means

Not a toy subset — the **complete** KerML v1.0 metamodel:

| Package | Classes | Examples |
|---|---|---|
| **Root** | 14 | Element, Namespace, Membership, Import |
| **Core** | 23 | Type, Classifier, Feature, Specialization |
| **Kernel** | 48+ | Class, Behavior, Expression, Connector, Flow |
| **Associations** | 127+ | Ownership, typing, subsetting, redefinition |

Plus: name resolution (KerML 8.2.3.5), visibility rules, semantic bindings, implied relationships, and a read-only **library mount system** for the Kernel Semantic Library.

---

## Act 2: The Diagram

**Interactive visualization backed by the full metamodel.**

Every diagram node is a live model element — not a static picture.

- Hierarchical auto-layout snaps elements into readable trees
- Drag, resize, rename elements inline
- Different visual renderers for classes, packages, behaviors, associations
- Palette for creating new elements on the canvas
- SVG export for documentation

> "Every visual element maps 1:1 to the parsed metamodel — the diagram _is_ the model."

---

## Act 3: The Intelligence

### This is the differentiator.

The engine doesn't just store models — **it reasons about them.**

Three solver modes built on the Z3/SMT theorem prover:

| Mode | Question It Answers |
|---|---|
| **Solve** | "Find valid parameter values that satisfy all constraints" |
| **Optimize** | "What is the _best_ value subject to constraints?" |
| **Conflict Detection** | "Which requirements contradict each other?" |

---

## Solve: Find Valid Allocations

**Given a mass budget with constraints, find a valid allocation:**

```
inv { totalMass == propulsionMass + powerMass + avionicsMass
                 + thermalMass + structureMass + payloadMass }

inv { propulsionMass >= 3000 }    inv { propulsionMass <= 12000 }
inv { powerMass >= 500 }          inv { powerMass <= 3000 }
inv { avionicsMass >= 200 }       inv { avionicsMass <= 1500 }
inv { totalMass <= 25000 }
```

The Z3 solver finds a valid assignment for all parameters simultaneously.
No spreadsheet iteration needed.

---

## Optimize: Trade Studies

**"What is the lightest spacecraft that meets all requirements?"**

```
inv { totalMass == Propulsion::mass + Power::mass
                 + Avionics::mass + Thermal::mass
                 + Structure::mass + payloadMass }

inv { Propulsion::mass >= 3000 }
inv { Power::mass >= 500 }
inv { payloadMass >= 1000 }
```

**Objective:** `minimize totalMass`

The solver returns the **optimal** allocation — a real trade study, driven directly from the model.

---

## Conflict Detection: Find Contradictions

**Requirements that can't all be satisfied simultaneously:**

```
// Requirement A: Heavy payload
inv { payloadMass >= 10000 }

// Requirement B: Lightweight vehicle
inv { totalMass <= 5000 }

// Requirement C: Propulsion has positive mass
inv { Propulsion::mass >= 500 }
```

The solver identifies the **minimal conflicting subset** — pinpointing exactly which requirements contradict each other. No more "it doesn't work" — you get "these three requirements can't coexist."

---

## Behavior Execution

**Token-passing simulation of KerML Behaviors.**

```
behavior LaunchSequence {
    step preflightChecks;
    step ignition;
    step liftoff;
    step maxQ;
    step stageSeparation;
    step orbitalInsertion;

    succession first preflightChecks then ignition;
    succession first ignition then liftoff;
    ...
}
```

- Steps execute when all predecessor tokens arrive
- Guard conditions gate transitions
- Data flows carry values between steps
- Nested behavior invocation with scoped contexts

---

## Act 4: The Ecosystem

### One metamodel definition generates everything.

```
MetaClass("Feature")
        │
        ├──→  Kotlin Interface     (Feature.kt)
        ├──→  Kotlin Implementation (FeatureImpl.kt)
        ├──→  TypeScript Interface  (kerml.model.ts)
        ├──→  Parser Visitor        (FeatureVisitor.kt)
        ├──→  KerML Writer          (FeatureGenerator.kt)
        └──→  API Schema            (@id, @type, ElementRef)
```

- **Kotlin**: Type-safe `obj is Feature`, `feature.declaredName`
- **TypeScript**: `ElementRef` ID-based references matching SysML v2 API
- **API**: JSON-LD format aligned with OMG Systems Modeling API spec

---

## SysML v2 API Alignment

GearShift speaks the **standard API**:

```json
{
    "@id": "a1b2c3d4-...",
    "@type": "Feature",
    "declaredName": "mass",
    "isAbstract": false,
    "isComposite": true,
    "owner": { "@id": "e5f6a7b8-..." },
    "ownedMembership": [
        { "@id": "c9d0e1f2-..." }
    ]
}
```

Projects, commits, elements — version-controlled model data with a REST API matching the OMG Systems Modeling API specification.

---

## Technology Stack

| Component | Technology |
|---|---|
| Language | Kotlin 2.1 / JDK 21 |
| Build | Gradle 9.2 |
| Parser | ANTLR 4.13 |
| Solver | Z3 4.13 (z3-turnkey) |
| Web Server | Ktor 2.3 |
| Frontend | Angular |
| Testing | Kotest + JUnit 5 |
| Constraint Language | OCL (parsed + executed) |
| Expression Evaluation | Native KerML tree walk |

---

## What's Next

| Area | Status |
|---|---|
| **SysML v2 Layer** | KerML foundation complete; SysML metaclasses extending it |
| **DocGen Pipeline** | Server-side document generation from View elements |
| **Living Documents** | Model values transcluded into docs, updating live |
| **Semantics Validation** | Tables 8-11 — implied relationships and verification |
| **Multi-user Collaboration** | Shared models with branching and merge |

**The vision:** A complete MBSE authoring environment where the model is the single source of truth for code, documents, diagrams, and analysis — all generated, all live.

---

## Key Takeaways

1. **Full KerML compliance** — 85 metaclasses, 127 associations, not a subset

2. **Model intelligence** — constraint solving, optimization, and conflict detection built in

3. **Single source of truth** — one metamodel generates Kotlin, TypeScript, API schema, parser, and writer

4. **Standards-aligned** — SysML v2 API format, KerML spec-compliant name resolution and semantics

5. **Interactive** — live diagrams, inline editing, behavior simulation

---

<!-- _class: lead -->

# GearShift

### From Text to Insight

**Questions?**