# Gearshift Demo Plan: "From Text to Insight"

**Date:** February 14, 2026
**Duration:** ~15 minutes
**Projects:** gearshift-kerml-service + gearshift-view-editor

---

## Act 1: "The Language" (~3 min)

**Goal:** Establish what KerML is and show we can parse it.

1. **Open the View Editor** — show the clean 3-pane layout
2. **Paste the Spacecraft KerML model** in the textual editor
3. **Hit Parse** — show instant parse with element count, zero errors
4. **Walk the element tree** — click through packages, classes, features
5. **Inspect element properties** — show the detail panel with types, associations, IDs

**Talking point:** "We've implemented a complete KerML parser — 85 metaclass types, 127 association kinds — the full spec."

---

## Act 2: "The Diagram" (~4 min)

**Goal:** Show visual modeling is real and interactive.

1. **Switch to diagram view** — elements appear as rendered nodes
2. **Apply hierarchical layout** — watch everything snap into a readable tree
3. **Interact:** drag elements, resize, double-click to rename inline
4. **Show different renderers** — classes vs packages vs behaviors (different visual shapes)
5. **Open the palette** — drag a new element onto the canvas
6. **Zoom/pan** around the model
7. **Export SVG** — "ready for documentation"

**Talking point:** "This isn't a static picture — it's a live, interactive view of the parsed model. Every node is backed by the full metamodel."

---

## Act 3: "The Intelligence" (~4 min)

**Goal:** The differentiator — show the engine reasons about models.

1. **GQL Query** — query the model (e.g., "show me all features typed as Real") — show results table
2. **Parametric Analysis — Solve:** "Given these physics constraints, find valid parameter values" → Z3 finds assignments
3. **Parametric Analysis — Optimize:** "Minimize total mass subject to constraints" → show optimal value
4. **Parametric Analysis — Conflict Detection:** Add a contradictory constraint → solver identifies the conflicting requirements

**Talking point:** "The engine doesn't just store models — it reasons about them. Constraint solving, optimization, and conflict detection are built in."

---

## Act 4: "The Ecosystem" (~2 min)

**Goal:** Show generated code and API alignment.

1. **Show generated TypeScript types** — auto-generated from the same metamodel
2. **Show the SysML v2 API shape** — `@id`, `@type`, `ElementRef` — standard API format
3. **Show Kotlin typed interfaces** — fully type-safe model access, no string-based lookups

**Talking point:** "One metamodel definition generates the parser, Kotlin backend types, TypeScript frontend types, and the API schema. Single source of truth."

---

## Act 5: "The Vision" (~2 min)

**Goal:** Paint the picture of where this is going.

- Living documents with transclusions (model values embedded in docs that update live)
- Document generation from model views
- Multi-user collaboration on shared models
- The complete MBSE authoring environment

---

## Demo Models

### Primary: Spacecraft Model (`spacecraft-demo.kerml`)
- Package hierarchy: `Spacecraft > Subsystems > (Propulsion, Power, Thermal, Avionics)`
- Classes with features and specialization
- Parametric constraints (thrust, mass, delta-v via Tsiolkovsky equation)
- Behaviors (launch sequence with steps and guard conditions)
- Associations between subsystems
- Designed to exercise all 8 diagram renderers

### Backup: Minimal Vehicle Model
- Simpler fallback if time is short
- `package Vehicles { class Vehicle { feature mass : Real; } class Car :> Vehicle; }`

---

## Pre-Demo Checklist

- [ ] Backend service starts cleanly (`./gradlew run`)
- [ ] Frontend connects to backend (`/health` returns OK)
- [ ] Library status shows mounted (`/library-status`)
- [ ] Spacecraft model parses with zero errors
- [ ] Diagram renders all element types correctly
- [ ] Layout algorithm produces clean output
- [ ] Parametric analysis endpoints respond correctly
- [ ] GQL query returns expected results
- [ ] SVG export produces clean output
- [ ] Font sizes readable at presentation resolution

---

## Risk Mitigation

| Risk | Mitigation |
|------|-----------|
| Backend fails to start | Pre-start before demo, have terminal ready to restart |
| Parse errors in demo model | Pre-validate, have backup minimal model |
| Diagram layout looks messy | Pre-arrange layout, screenshot as fallback |
| Z3 solver timeout | Use small constraint sets, have cached response ready |
| Network issues | Run everything on localhost |
| Font too small for audience | Increase browser zoom to 150%, use dark theme if available |
