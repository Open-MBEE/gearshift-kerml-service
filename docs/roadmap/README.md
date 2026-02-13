# Roadmap

Consolidated view of planned and in-progress work across all major feature areas.

---

## SysML v2 REST API

Implements the core subset of the [Systems Modeling API and Services](https://www.omg.org/spec/SystemsModelingAPI) spec
alongside the existing demo endpoints.

### Completed

- Existing demo API (`/parse`, `/query`, `/health`, `/library-status`)
- `ProjectStore` — in-memory multi-project store with commit history
- `ElementSerializer` — MDMObject to `@id`/`@type` JSON-LD format
- Project/Commit/Element REST endpoints (list, get, create)

### In Progress

- **Phase 1: Version Control Data Model** — `BranchData`, `CommitData`, `DataVersionData` structs
- **Phase 2: Element JSON Serializer** — Full property + association end serialization
- **Phase 3: SysML v2 API Routes** — Project CRUD (5 endpoints), Commit management (3), Element read (3), Relationship
  read (1)

### Remaining

- **Phase 4: JSON Export/Import** — Project export as JSON (all commits + current element state), import from JSON
- **Phase 5: Frontend Service Updates** — Angular `ProjectService` calling new endpoints; `put`/`delete` on
  `ApiClientService`

### Deferred

- Branch/Tag CRUD
- Diff & Merge
- Saved Query CRUD
- Meta/datatypes endpoints
- Cursor-based pagination

---

## Semantics Validation

Validates all KerML semantics from Section 8.4: implied relationships, redundancy elimination, and verification
constraints.

### Completed

- **Table 8 — Core Implied Relationships** — `checkTypeSpecialization` (implied `Subclassification` to
  `Base::Anything`), `checkFeatureSpecialization` (implied `Subsetting` to `Base::things`), redundancy elimination, full
  test coverage
- **Milestone 2 — Library Resolution** — Qualified name resolution for library elements fixed

### In Progress (Table 9 — Core Supporting Kernel Semantics)

- SemanticBindings defined for `dataValues`, `occurrences`, `suboccurrences`, `objects`, `subobjects`,
  `Link::participant`
- Library resolution needed for Occurrences/Objects/Links-scoped bindings
- Flow feature redefinition constraint defined (handler incomplete)
- Feature valuation specialization constraint defined

### Remaining

| Area                                                                                                                                                                                                                                    | Status                      |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------|
| **Table 9 completion** — TypeFeaturing, Redefinition, Step/Subperformance/EnclosedPerformance bindings                                                                                                                                  | Not implemented             |
| **Table 10 — Kernel Specializations** — DataType→DataValue, Class→Occurrence, Structure→Object, Association→Link, Behavior→Performance, Function→Evaluation, Predicate→BooleanEvaluation, expression result subsetting (22 constraints) | Not implemented             |
| **Table 11 — Other Implied Relationships** — Connector end redefinition, Succession source/target redefinition, BindingConnector support, FeatureValue binding, expression chain constraints (12 constraints)                           | Not implemented             |
| **Redundancy Elimination** — Transitive redundancy detection, same-generalType deduplication                                                                                                                                            | Partial (basic skip exists) |
| **Verification Constraints** — ~26+ `validate*` constraints for Types, Features, Associations, Connectors, Expressions                                                                                                                  | Not implemented             |

---

## DocGen Pipeline

> Full implementation plan: [docgen-pipeline.md](docgen-pipeline.md)

Server-side document generation via `POST /docgen/render-view`. The backend resolves View elements natively (expose,
viewpoint, subview) and executes rendering pipelines.

### Architecture

- **Frontend owns:** Pipeline construction (presets, drag-and-drop editor) via `KermlGeneratorService`
- **Backend owns:** View resolution + pipeline execution
- **Single endpoint**, three pipeline sources (priority order): frontend-provided KerML text, View's own Rendering
  element, default pipeline

### What Exists (Frontend)

- `ViewResolverService` — client-side View element resolution
- `KermlGeneratorService` — serializes `PipelineDefinition` to KerML text
- `PipelineEngineService` — client-side pipeline execution (interim)
- 4 built-in presets: `all-owned-elements-table`, `requirements-table`, `class-catalog`, `features-list`

### What Exists (Backend)

- View/Viewpoint/Rendering metamodel fully defined with derived properties and associations
- KerML parser (ANTLR), BehaviorExecutionEngine, OCL engine, KerML expression evaluator
- Ktor REST API with ProjectStore

### Remaining Phases

| Phase | Description                                                                           | Status      |
|-------|---------------------------------------------------------------------------------------|-------------|
| 1     | DocGen library metamodel (`DocGen.kerml` standard library)                            | Done        |
| 2     | `ViewResolverService` — server-side View resolution using derived properties          | Done        |
| 3     | `PipelineExtractor` — extract pipeline from Rendering or parsed KerML                 | Not started |
| 4     | `PresentationModel` — Kotlin types matching frontend `PresentationNode` union         | Not started |
| 5     | `PipelineExecutor` — step dispatch (Collect, Filter, Sort, Combine, Present, Section) | Not started |
| 6     | REST endpoint + service wiring (`DocGenPipelineService`, `DocGenRoutes`)              | Not started |
| 7     | Subview hierarchy & document assembly                                                 | Not started |
| 8     | Testing — ViewResolver, PipelineExtractor, PipelineExecutor, endpoint integration     | Not started |
| 9     | Frontend integration — switch from client-side to server-side rendering               | Not started |

---

## SysML Stack Evolution

SysML v2 is a layer on top of KerML. The Mdm Framework and execution layers remain unchanged (language-agnostic by
design). SysML metaclasses extend KerML metaclasses.

### Target Architecture

- **4 new modules:** `gearshift-sysml-model`, `sysml-generated`, `gearshift-sysml-runtime` (+ evolved
  `settings.gradle.kts`)
- **Existing modules unchanged:** `mdm-framework`, `gearshift-kerml-model`, `kerml-generated`, `gearshift-kerml-runtime`
- **Mount system extended:** KerML Kernel Library + SysML Standard Library + ISQ/SI Units Library
- **Code generation extended:** SysML interfaces + union types for both Kotlin and TypeScript

### Current Progress

| Component                   | Status                                                |
|-----------------------------|-------------------------------------------------------|
| SysML metaclass definitions | 34 classes defined (Definitions + Usages)             |
| SysML associations          | Not yet defined                                       |
| SysMLMetamodelLoader        | Placeholder (empty)                                   |
| SysML parser/writer         | Not yet started                                       |
| SysML standard libraries    | Not yet started                                       |
| SysML v2 REST API           | Partially implemented (projects, commits, elements)   |
| SysML code generation       | Not yet started (generators ready, need registration) |
| SysML semantic bindings     | Not yet defined                                       |

### SysML-Specific Analysis (Future)

- Requirement verification tracing (`satisfy`/`verify` relationships)
- Allocation consistency checking
- Interface compatibility analysis (PortUsage across ConnectionUsages)
- Timing/resource constraint propagation via Z3 solver
