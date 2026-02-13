# DocGen Pipeline Backend Implementation Plan

## Goal

Implement server-side document generation through a single endpoint: `POST /docgen/render-view`. The backend resolves a View element natively — its exposed elements, viewpoint constraints, and subviews — using the metamodel it already has. The pipeline to execute comes from one of two sources:

1. **Frontend-provided KerML text** — the frontend constructs the pipeline (via `KermlGeneratorService`) and sends it as an optional `kerml` field. The backend parses it with the standard `KerMLModel.parseString()`.
2. **View's own Rendering** — if no KerML text is provided, the backend extracts the pipeline from the View's Rendering element (resolved via `View.rendering` derived property).

Either way, the View element is always the anchor: the backend uses its derived properties for expose/viewpoint/subview resolution. The pipeline source is the only variable.

---

## Current State

### Frontend (gearshift-view-editor) — Already Built
- `ViewResolverService` resolves View elements into `PipelineDefinition` objects **client-side**
  - Reads `NamespaceExpose`/`MembershipExpose` children to find exposed elements
  - Reads `ViewRenderingMembership` to determine presentation kind (table, list, paragraph, image)
  - Builds a `PipelineDefinition` (collect → filter → sort → present) from the View structure
- `KermlGeneratorService` serializes `PipelineDefinition` → KerML textual syntax
- `PipelineEngineService` executes pipelines client-side (interim)
- `*ByExpression` steps (OCL) log warnings and pass through — **only the backend can evaluate these**
- 4 built-in presets: `all-owned-elements-table`, `requirements-table`, `class-catalog`, `features-list`

### Backend (gearshift-kerml-service) — Existing Capabilities
- **View/Viewpoint/Rendering metamodel already defined:**
  - `View` — derived props: `expose`, `exposedElement`, `rendering`, `satisfiedViewpoint`, `subview`
  - `Viewpoint` — a Predicate that constrains Views
  - `ViewpointPredicate` — BooleanExpression typed by Viewpoint
  - `Rendering` — Structure that defines how a View is rendered, with `subrendering`
  - `ViewRenderingMembership` — connects View to its Rendering via `ownedRendering`
  - `Expose` (abstract), `NamespaceExpose`, `MembershipExpose` — determine visible elements
  - Full associations in `ViewsAssociations.kt` with derivation constraints
- **KerML parser** — ANTLR-based, handles Behavior/Step/Succession/Binding syntax
- **BehaviorExecutionEngine** — token-passing DAG executor with tracing
- **OCL engine** — parser + evaluator with iterators, type ops, string ops, collection ops
- **KerML expression evaluator** — walks MDMObject trees for native KerML expressions
- **MDMEngine** — element storage, property access, association graph, operation dispatch
- **Ktor REST API** — project/element/commit endpoints + parametric analysis endpoints
- **ProjectStore** — multi-project in-memory store with commit history

### What Moves to the Backend
The frontend currently duplicates model resolution logic that the backend can do natively:
1. **Expose resolution** moves to backend — `View.exposedElement` derived property replaces manual tree-walking
2. **Viewpoint validation** moves to backend — `View.satisfiedViewpoint` + constraint evaluation
3. **Subview hierarchy** moves to backend — `View.subview` + recursive rendering
4. **OCL expression evaluation** moves to backend — `*ByExpression` steps actually execute instead of passing through
5. **Deep model traversal** moves to backend — `CollectOwned` etc. operate on the full element graph

**What stays on the frontend:** Pipeline construction. The frontend's `KermlGeneratorService` still builds pipelines from user interactions (preset selection, drag-and-drop pipeline editor, etc.) and serializes them to KerML text. The backend receives this text via the `kerml` field and parses it with the standard `KerMLModel.parseString()`.

**The backend owns: View resolution + pipeline execution. The frontend owns: pipeline construction + display.**

---

## Architecture

**Single endpoint, two pipeline sources:**

```
Frontend                              Backend (gearshift-kerml-service)
────────                              ──────────────────────────────────

User selects View element
  │
  ├─ (optional) Builds pipeline
  │   via KermlGeneratorService
  │   → KerML text
  │
POST /docgen/render-view ──────→  DocGenRoutes
  { viewElementId,                     │
    projectId, branchId,          DocGenPipelineService.renderView()
    kerml? }                           │
                                  ┌────┴────────────────────────────┐
                                  │  View Resolution (always)       │
                                  │  ViewResolverService            │
                                  │    1. engine.getElement(viewId) │
                                  │    2. view.exposedElement       │
                                  │       (derived → OCL)           │
                                  │    3. view.satisfiedViewpoint   │
                                  │       (derived → OCL)           │
                                  │    4. view.subview              │
                                  │       (derived → OCL)           │
                                  └────┬────────────────────────────┘
                                       │
                                  ┌────┴────────────────────────────┐
                                  │  Pipeline Resolution            │
                                  │                                 │
                                  │  if request.kerml provided:     │
                                  │    model.parseString(kerml)     │
                                  │    → extract pipeline from      │
                                  │      parsed behavior            │
                                  │                                 │
                                  │  else if view.rendering exists: │
                                  │    → extract pipeline from      │
                                  │      Rendering element          │
                                  │                                 │
                                  │  else:                          │
                                  │    → default pipeline           │
                                  │      (collect all → table)      │
                                  └────┬────────────────────────────┘
                                       │
                                  PipelineExecutor.execute(
                                    pipeline, exposedElements)
                                       │
                                  ┌────┴────────────────────────────┐
                                  │  Subview Recursion (if any)     │
                                  │  For each subview:              │
                                  │    → recursive renderView()     │
                                  │    → wrap in SectionNode        │
                                  └────┬────────────────────────────┘
                                       │
                                ←── PresentationNode[] (JSON)
```

The View is always the anchor. The `kerml` field is the mechanism for the frontend to send a pipeline (presets, user-constructed pipelines, interactive editing). When absent, the View's own Rendering defines the pipeline. This is a standard `KerMLModel.parseString()` — no special parsing path needed.

---

## Implementation Phases

### Phase 1: DocGen Library Metamodel

**Goal:** Define the DocGen behaviors so the parser can resolve `import DocGen::*` and the Rendering elements can contain typed steps.

**Approach:** Provide a **KerML standard library file** that the parser loads, just like KerML standard libraries.

**Files to create/modify:**

1. **`gearshift-kerml-runtime/src/main/resources/libraries/DocGen.kerml`** (new)
   - Write the DocGen library as actual KerML text (from the design doc Sections 3-7):
     - Enums: `DirectionKind`, `SourcePropertyKind`
     - Abstract bases: `QueryOp` (in elements, out result), `PresentOp` (in elements, title?, expression?), `DocGenPipeline` (in context)
     - Collect behaviors: `CollectOwned`, `CollectOwners`, `CollectTypes`, `CollectFeatures`, `CollectByRelationship`, `CollectByExpression`
     - Filter behaviors: `FilterByMetaclass`, `FilterByName`, `FilterByExpression`
     - Sort behaviors: `SortByName`, `SortByFeature`, `SortByExpression`
     - Combine behaviors: `Union`, `Intersection`, `RemoveDuplicates`
     - Present behaviors: `PresentParagraph`, `PresentTable`, `PresentList`, `PresentImage`
     - Structure: `SectionDef` (specializes DocGenPipeline), `DocumentDef`, `DocumentMeta`
     - Support types: `ColumnSpec`

2. **Modify `KerMLModel.kt`** — Load `DocGen.kerml` as a library mount alongside existing KerML standard libraries

**Deliverable:** `import DocGen::*;` resolves successfully. Rendering elements containing DocGen steps parse correctly.

---

### Phase 2: View Resolver Service

**Goal:** Implement the server-side equivalent of the frontend's `ViewResolverService` — take a View element, resolve everything from the model using existing derived properties and associations.

**File to create:**

1. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/ViewResolverService.kt`** (new)

   ```kotlin
   class ViewResolverService(private val engine: MDMEngine) {

       /**
        * Resolve a View element into everything needed for pipeline execution.
        * Uses the existing metamodel derived properties — no manual tree-walking.
        */
       fun resolveView(viewId: String): ResolvedView {
           val viewObj = engine.getElement(viewId)
               ?: throw ViewNotFoundException(viewId)

           // 1. Exposed elements — derived via: expose.importedElement->asSet()
           //    View.exposedElement association (deriveViewExposedElement)
           val exposedElements = resolveExposedElements(viewObj)

           // 2. Rendering — derived via: featureMembership
           //    ->selectByKind(ViewRenderingMembership).ownedRendering->first()
           //    View.rendering association (deriveViewRendering)
           val rendering = resolveRendering(viewObj)

           // 3. Satisfied viewpoints — derived via:
           //    ownedFeature->selectByKind(ViewpointPredicate)
           //    View.satisfiedViewpoint association (deriveViewSatisfiedViewpoint)
           val viewpoints = resolveViewpoints(viewObj)

           // 4. Subviews — derived via:
           //    ownedFeature->select(f | f.type->exists(oclIsKindOf(View)))
           //    View.subview association (deriveViewSubview)
           val subviews = resolveSubviews(viewObj)

           return ResolvedView(
               view = viewObj,
               exposedElements = exposedElements,
               rendering = rendering,
               viewpoints = viewpoints,
               subviews = subviews
           )
       }

       private fun resolveExposedElements(view: MDMObject): List<MDMObject> {
           // Use the derived 'exposedElement' association end
           // which evaluates: expose.importedElement->asSet()
           // Falls back to the View element itself if no exposes defined
       }

       private fun resolveRendering(view: MDMObject): MDMObject? {
           // Use the derived 'rendering' association end
           // which evaluates via ViewRenderingMembership.ownedRendering
       }

       private fun resolveViewpoints(view: MDMObject): List<MDMObject> {
           // Use the derived 'satisfiedViewpoint' association end
       }

       private fun resolveSubviews(view: MDMObject): List<MDMObject> {
           // Use the derived 'subview' association end
       }
   }

   data class ResolvedView(
       val view: MDMObject,
       val exposedElements: List<MDMObject>,
       val rendering: MDMObject?,         // The Rendering element (contains the pipeline)
       val viewpoints: List<MDMObject>,   // ViewpointPredicates to validate
       val subviews: List<MDMObject>      // Nested Views for recursive rendering
   )
   ```

**Key insight:** The derived property constraints already exist in `View.kt`:
- `deriveViewExpose` → `ownedRelationship->selectByKind(Expose)`
- `deriveViewExposedElement` → `expose.importedElement->asSet()`
- `deriveViewRendering` → `featureMembership->selectByKind(ViewRenderingMembership).ownedRendering->first()`
- `deriveViewSatisfiedViewpoint` → `ownedFeature->selectByKind(ViewpointPredicate)`
- `deriveViewSubview` → `ownedFeature->selectByKind(Feature)->select(f | f.type->exists(oclIsKindOf(View)))`

These OCL expressions are evaluated by the existing `OclExecutor` through the `MDMEngine.getPropertyValue()` → derivation constraint path. **No new model traversal code needed — the metamodel does the work.**

**Deliverable:** Given a View element ID, resolve its exposed elements, rendering, viewpoints, and subviews using existing metamodel machinery.

---

### Phase 3: Pipeline Extraction

**Goal:** Extract the pipeline structure (steps, successions, bindings) from either a Rendering element or parsed KerML text.

**The Rendering IS the pipeline.** A Rendering is a Structure containing Steps typed by DocGen behaviors, connected by Successions and BindingConnectors. The extraction logic is the same regardless of where the source `MDMObject` came from:
- A Rendering element in the stored model (resolved via `View.rendering`)
- A Behavior parsed from frontend-provided KerML text (`model.parseString(request.kerml)`)

Both produce the same `MDMObject` tree — the `PipelineExtractor` doesn't need to know the origin.

**Files to create:**

1. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/PipelineModel.kt`** (new)
   - Kotlin sealed interface hierarchy for pipeline steps:

   ```kotlin
   data class PipelineDefinition(
       val name: String,
       val title: String?,
       val steps: List<PipelineStep>,
       val successions: List<StepSuccession>,
       val bindings: List<StepBinding>,
       val entryStepId: String
   )

   sealed interface PipelineStep { val id: String; val behaviorType: String }

   // Query steps
   data class CollectOwnedStep(override val id: String, val depth: Int = -1) : PipelineStep { ... }
   data class CollectOwnersStep(override val id: String, val depth: Int = 1) : PipelineStep { ... }
   data class CollectTypesStep(override val id: String) : PipelineStep { ... }
   data class CollectFeaturesStep(override val id: String, val includeInherited: Boolean = false) : PipelineStep { ... }
   data class CollectByRelationshipStep(override val id: String, ...) : PipelineStep { ... }
   data class CollectByExpressionStep(override val id: String, val expression: String) : PipelineStep { ... }
   data class FilterByMetaclassStep(override val id: String, val metaclass: String, val include: Boolean = true) : PipelineStep { ... }
   data class FilterByNameStep(override val id: String, val pattern: String, ...) : PipelineStep { ... }
   data class FilterByExpressionStep(override val id: String, val expression: String) : PipelineStep { ... }
   data class SortByNameStep(override val id: String, val reversed: Boolean = false) : PipelineStep { ... }
   data class SortByFeatureStep(override val id: String, val featureName: String, ...) : PipelineStep { ... }
   data class SortByExpressionStep(override val id: String, val expression: String, ...) : PipelineStep { ... }
   data class UnionStep(override val id: String) : PipelineStep { ... }
   data class IntersectionStep(override val id: String) : PipelineStep { ... }
   data class RemoveDuplicatesStep(override val id: String) : PipelineStep { ... }

   // Present steps
   data class PresentParagraphStep(override val id: String, val sourceProperty: SourcePropertyKind = ..., val iterate: Boolean = true, val expression: String? = null) : PipelineStep { ... }
   data class PresentTableStep(override val id: String, val title: String? = null, val columns: List<ColumnSpec>, ...) : PipelineStep { ... }
   data class PresentListStep(override val id: String, val ordered: Boolean = false, ...) : PipelineStep { ... }
   data class PresentImageStep(override val id: String, ...) : PipelineStep { ... }

   // Section step (recursive)
   data class SectionStep(override val id: String, val title: String?, val iterate: Boolean, val nestedPipeline: PipelineDefinition) : PipelineStep { ... }

   data class ColumnSpec(val header: String, val property: String, val width: String? = null)
   data class StepSuccession(val fromStepId: String, val toStepId: String)
   data class StepBinding(val fromStepId: String, val fromPort: String, val toStepId: String, val toPort: String)
   enum class DirectionKind { FORWARD, BACKWARD, BOTH }
   enum class SourcePropertyKind { DOCUMENTATION, NAME, VALUE }
   ```

2. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/PipelineExtractor.kt`** (new)

   ```kotlin
   class PipelineExtractor(private val engine: MDMEngine) {

       /**
        * Extract pipeline from a Rendering element (View-driven path).
        * The Rendering's owned Steps, Successions, and BindingConnectors
        * define the pipeline structure.
        */
       fun extractFromRendering(rendering: MDMObject): PipelineDefinition

       /**
        * Extract pipeline from a parsed Behavior (ad-hoc KerML text path).
        * Used when the frontend sends raw KerML pipeline text.
        */
       fun extractFromBehavior(behavior: MDMObject): PipelineDefinition

       // Shared extraction logic — both paths produce the same PipelineDefinition:
       // 1. Find all Step children → map to typed PipelineStep by checking typing
       // 2. Find all Succession children → extract from/to step references
       // 3. Find all BindingConnector children → extract port wiring
       // 4. Identify entry step (has `in :>> elements = context`)
       // 5. Handle nested SectionDef steps recursively
   }
   ```

**Deliverable:** Extract `PipelineDefinition` from either a Rendering model element or a parsed KerML behavior.

---

### Phase 4: Presentation Node Output Model

**Goal:** Define Kotlin types for the JSON response matching the frontend's `PresentationNode` discriminated union.

**File to create:**

1. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/PresentationModel.kt`** (new)

   ```kotlin
   @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
   @JsonSubTypes(...)
   sealed interface PresentationNode { val id: String; val type: String }

   data class ParagraphNode(override val id: String, val content: List<ParagraphContent>) : PresentationNode
   data class TableNode(override val id: String, val title: String?, val header: List<TableRow>?, val body: List<TableRow>, ...) : PresentationNode
   data class ListNode(override val id: String, val title: String?, val ordered: Boolean, val items: List<ListItem>) : PresentationNode
   data class SectionNode(override val id: String, val name: String, val nameRef: ElementRef?, val children: List<PresentationNode>, val isAppendix: Boolean) : PresentationNode
   data class ImageNode(override val id: String, val elementRef: ElementRef, ...) : PresentationNode

   // Content types
   sealed interface ParagraphContent
   data class TransclusionContent(val elementRef: ElementRef, val property: String) : ParagraphContent
   data class LiteralContent(val html: String) : ParagraphContent
   data class ElementRef(val elementId: String, val projectId: String, val branchId: String)

   // Table/List support
   data class TableRow(val cells: List<TableCell>)
   data class TableCell(val content: ParagraphContent, val colspan: Int? = null, val rowspan: Int? = null)
   data class ListItem(val content: List<ParagraphContent>, val children: List<ListItem>? = null)
   ```

**Deliverable:** Serializable Kotlin types that produce JSON matching `presentation.model.ts`.

---

### Phase 5: Pipeline Executor — Step Implementations

**Goal:** Implement the core execution logic — topological sort, step dispatch, and each individual step operation.

**Files to create:**

1. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/PipelineExecutor.kt`** (new)

   ```kotlin
   class PipelineExecutor(
       private val engine: MDMEngine,
       private val oclExecutor: OclExecutor,
       private val projectId: String,
       private val branchId: String
   ) {
       fun execute(pipeline: PipelineDefinition, contextElements: List<MDMObject>): List<PresentationNode>
   }
   ```

   Algorithm:
   1. Build adjacency list from successions
   2. Topological sort (Kahn's algorithm) — reject cycles
   3. For each step in topo order: resolve inputs from bindings → dispatch → store result
   4. Collect `PresentationNode` results from present/section steps
   5. Return assembled `PresentationNode[]`

2. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/steps/CollectStepExecutors.kt`** (new)

   | Step | Implementation |
   |------|----------------|
   | `CollectOwned` | Walk `ownedMembership → memberElement` recursively to `depth` |
   | `CollectOwners` | Walk `element.owner` chain up to `depth` |
   | `CollectTypes` | For each Feature, follow `FeatureTyping.type` association |
   | `CollectFeatures` | Collect `ownedFeature`; if `includeInherited`, walk `Specialization` chain |
   | `CollectByRelationship` | Find relationships matching `relationshipMetaclass`, follow `direction` |
   | `CollectByExpression` | Evaluate OCL `expression` against each input via `OclExecutor` |

3. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/steps/FilterStepExecutors.kt`** (new)

   | Step | Implementation |
   |------|----------------|
   | `FilterByMetaclass` | `engine.isInstanceOf(it, metaclass) == include` |
   | `FilterByName` | Match `element.name` against `pattern` (glob or regex) |
   | `FilterByExpression` | Evaluate OCL `expression` per element, keep if truthy |

4. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/steps/SortStepExecutors.kt`** (new)

   | Step | Implementation |
   |------|----------------|
   | `SortByName` | Sort by `element.name`, optionally reversed |
   | `SortByFeature` | Resolve `featureName` value per element, sort |
   | `SortByExpression` | Evaluate OCL `expression` per element, sort by result |

5. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/steps/CombineStepExecutors.kt`** (new)

   | Step | Implementation |
   |------|----------------|
   | `Union` | Merge `branchA` + `branchB`, preserving order |
   | `Intersection` | Keep elements in both branches (by ID) |
   | `RemoveDuplicates` | Deduplicate by element ID |

6. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/steps/PresentStepExecutors.kt`** (new)

   | Step | Implementation |
   |------|----------------|
   | `PresentParagraph` | Create `TransclusionContent` per element for `sourceProperty`. If `expression` set, evaluate OCL first — elements→transclude, String→`LiteralContent` |
   | `PresentTable` | Header row from `columns[].header`. Body rows: one `TransclusionContent` per column per element |
   | `PresentList` | `ListItem` per element with content from `sourceProperty` |
   | `PresentImage` | `ImageNode` with `ElementRef` and `artifactType` |

7. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/steps/SectionStepExecutor.kt`** (new)
   - Iterate `[0..*]`: one recursive `PipelineExecutor.execute()` per element → `SectionNode`
   - Non-iterate: single recursive call with all elements

8. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/ElementPropertyResolver.kt`** (new)
   - Shared utility resolving "name", "documentation", "type", "value", "qualifiedName" from `MDMObject`

**Deliverable:** Full pipeline execution from `PipelineDefinition` + context elements → `PresentationNode[]`.

---

### Phase 6: REST Endpoint & Service Wiring

**Goal:** Expose a single `POST /docgen/render-view` endpoint and wire the full resolution chain.

**Files to create/modify:**

1. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/DocGenPipelineService.kt`** (new)

   ```kotlin
   class DocGenPipelineService(private val projectStore: ProjectStore) {

       fun renderView(request: RenderViewRequest): RenderViewResponse {
           val model = projectStore.getModel(request.projectId) ?: error(...)

           // 1. Resolve the View — expose, viewpoints, subviews (always from model)
           val resolver = ViewResolverService(model.engine)
           val resolved = resolver.resolveView(request.viewElementId)

           // 2. Resolve the pipeline (priority order)
           val extractor = PipelineExtractor(model.engine)
           val pipeline = resolvePipeline(request, resolved, model, extractor)

           // 3. Execute pipeline against exposed elements
           val executor = PipelineExecutor(model.engine, OclExecutor(model.engine), ...)
           val nodes = executor.execute(pipeline, resolved.exposedElements)

           // 4. If subviews exist, recurse and wrap in SectionNodes
           val allNodes = if (resolved.subviews.isNotEmpty()) {
               buildDocumentFromViewHierarchy(resolved, executor, extractor)
           } else {
               nodes
           }

           // 5. Validate viewpoint constraints (if any)
           val violations = validateViewpoints(resolved.viewpoints, resolved.exposedElements)

           return RenderViewResponse(success = true, nodes = allNodes, violations = violations)
       }

       /**
        * Pipeline resolution — three sources, checked in priority order:
        *
        * 1. Frontend-provided KerML text (request.kerml)
        *    → model.parseString(kerml) → extract from parsed behavior
        *    Used for: presets, user-constructed pipelines, interactive editing
        *
        * 2. View's own Rendering (View.rendering derived property)
        *    → extract from Rendering element in the stored model
        *    Used for: Views with model-defined rendering pipelines
        *
        * 3. Default pipeline (collect all → sort → present as table)
        *    Used for: Views with no Rendering and no frontend override
        */
       private fun resolvePipeline(
           request: RenderViewRequest,
           resolved: ResolvedView,
           model: KerMLModel,
           extractor: PipelineExtractor
       ): PipelineDefinition {
           // Priority 1: Frontend-provided KerML text
           if (request.kerml != null) {
               val parsed = model.parseString(request.kerml)
                   ?: error("Failed to parse provided KerML pipeline")
               return extractor.extractFromBehavior(parsed)
           }

           // Priority 2: View's own Rendering
           if (resolved.rendering != null) {
               return extractor.extractFromRendering(resolved.rendering)
           }

           // Priority 3: Default
           return defaultPipeline()
       }
   }
   ```

2. **`gearshift-kerml-runtime/src/main/kotlin/.../api/DocGenRoutes.kt`** (new)

   ```kotlin
   fun Route.docGenRoutes(service: DocGenPipelineService) {
       route("/docgen") {
           post("/render-view") {
               val request = call.receive<RenderViewRequest>()
               val response = service.renderView(request)
               call.respond(
                   if (response.success) HttpStatusCode.OK else HttpStatusCode.BadRequest,
                   response
               )
           }
       }
   }
   ```

3. **`gearshift-kerml-runtime/src/main/kotlin/.../docgen/DocGenApiModel.kt`** (new)

   ```kotlin
   data class RenderViewRequest(
       val viewElementId: String,
       val projectId: String,
       val branchId: String,
       val commitId: String? = null,
       val kerml: String? = null  // optional: frontend-provided pipeline overrides View's Rendering
   )

   data class RenderViewResponse(
       val success: Boolean,
       val nodes: List<PresentationNode> = emptyList(),
       val violations: List<ViewpointViolation> = emptyList(),
       val errors: List<String> = emptyList()
   )

   data class ViewpointViolation(
       val viewpointName: String,
       val message: String,
       val severity: String  // "error", "warning"
   )
   ```

4. **Modify routing root** — Register `docGenRoutes`

**Deliverable:** Single working endpoint `POST /docgen/render-view` that:
- Always resolves View's expose/viewpoint/subview from the model
- Uses `request.kerml` if provided (standard parse), else View's Rendering, else default
- Returns `PresentationNode[]` + viewpoint violations

---

### Phase 7: Subview Hierarchy & Document Assembly

**Goal:** Handle View hierarchies — a View with subviews produces a nested document (SectionNodes containing child SectionNodes).

**Logic in `DocGenPipelineService`:**

```kotlin
private fun buildDocumentFromViewHierarchy(
    resolved: ResolvedView,
    executor: PipelineExecutor,
    extractor: PipelineExtractor
): List<PresentationNode> {
    val sections = mutableListOf<PresentationNode>()

    // Render the parent View's own content (if it has a Rendering)
    if (resolved.rendering != null) {
        val pipeline = extractor.extractFromRendering(resolved.rendering)
        sections += executor.execute(pipeline, resolved.exposedElements)
    }

    // Recurse into each subview
    for (subviewObj in resolved.subviews) {
        val subResolved = viewResolver.resolveView(subviewObj.id)
        val childNodes = buildDocumentFromViewHierarchy(subResolved, executor, extractor)
        val subviewName = (subviewObj as? Element)?.name ?: "Unnamed"

        sections += SectionNode(
            id = generateId(),
            name = subviewName,
            nameRef = ElementRef(subviewObj.id, projectId, branchId),
            children = childNodes,
            isAppendix = false
        )
    }

    return sections
}
```

This mirrors the legacy MDK `ProductViewParser.parse()` flow:
1. Walk View hierarchy depth-first
2. For each View: resolve exposed elements → execute viewpoint method → produce Section
3. Assemble into nested Section tree

**Deliverable:** Multi-section documents from View hierarchies.

---

### Phase 8: Testing

**Files to create:**

1. **`gearshift-kerml-runtime/src/test/kotlin/.../docgen/ViewResolverServiceTest.kt`** (new)
   - Parse a KerML model containing View/Viewpoint/Rendering/Expose elements
   - Assert `resolveView()` correctly derives exposed elements, rendering, viewpoints, subviews
   - Test View with no Rendering (should use default pipeline)
   - Test View with no Expose (should fall back to View itself)
   - Test nested View hierarchy (parent with subviews)

2. **`gearshift-kerml-runtime/src/test/kotlin/.../docgen/PipelineExtractorTest.kt`** (new)
   - Parse each of the 4 preset KerML examples
   - Assert correct step count, types, successions, bindings, configuration values
   - Test extraction from a Rendering element (not just parsed Behavior)
   - Test nested section extraction (class-catalog preset)

3. **`gearshift-kerml-runtime/src/test/kotlin/.../docgen/PipelineExecutorTest.kt`** (new)
   - Build in-memory KerML model with packages, features, requirements
   - Execute each preset pipeline, assert `PresentationNode[]` output
   - Test the full View-driven path end-to-end

4. **`gearshift-kerml-runtime/src/test/kotlin/.../docgen/StepExecutorTest.kt`** (new)
   - Unit tests for each step executor in isolation
   - OCL expression steps (`CollectByExpression`, `FilterByExpression`, `SortByExpression`)

5. **`gearshift-kerml-runtime/src/test/kotlin/.../docgen/RenderViewEndpointTest.kt`** (new)
   - Integration tests via Ktor test client for `POST /docgen/render-view`:
     - View ID only (pipeline from Rendering)
     - View ID + KerML text (frontend-provided pipeline override)
     - View ID, no Rendering, no KerML (default pipeline)
     - View with subviews (nested SectionNode output)
     - Invalid View ID (error response)

**Test model fixture:**

```kerml
package TestModel {
    import DocGen::*;

    part def Vehicle {
        doc /* A vehicle for testing */
        attribute mass : Real;
    }

    requirement def REQ_001 {
        doc /* The vehicle shall have 4 wheels */
    }

    requirement def REQ_002 {
        doc /* The engine shall produce 200hp */
    }

    // A View that exposes the test model, rendered as a table
    view def VehicleOverview {
        expose TestModel::*;

        render asTable : Rendering {
            step collect : CollectOwned {
                :>> depth = -1;
                in :>> elements = context;
            }
            step sort : SortByName;
            step present : PresentTable {
                :>> columns = (
                    ColumnSpec { :>> header = "Name"; :>> property = "name"; },
                    ColumnSpec { :>> header = "Type"; :>> property = "type"; },
                    ColumnSpec { :>> header = "Documentation"; :>> property = "documentation"; }
                );
            }
            succession collect then sort;
            succession sort then present;
            binding collect.result = sort.elements;
            binding sort.result = present.elements;
        }
    }
}
```

---

### Phase 9: Frontend Integration

**Changes in gearshift-view-editor:**

All calls go through the same endpoint. The frontend decides whether to send a pipeline:

1. **View with model-defined Rendering** — send just the View ID:
   ```typescript
   return this.http.post<RenderViewResponse>('/docgen/render-view', {
     viewElementId: view.elementId,
     projectId: view.projectId,
     branchId: view.branchId
   }).pipe(map(r => r.nodes));
   ```

2. **Preset or user-constructed pipeline** — send View ID + KerML text:
   ```typescript
   const kerml = this.kermlGenerator.generate(pipeline);
   return this.http.post<RenderViewResponse>('/docgen/render-view', {
     viewElementId: view.elementId,
     projectId: view.projectId,
     branchId: view.branchId,
     kerml  // overrides the View's own Rendering
   }).pipe(map(r => r.nodes));
   ```

3. **Fallback** — Keep `PipelineEngineService` for offline/disconnected mode

The frontend's `ViewResolverService` simplifies significantly — it no longer needs to resolve exposed elements, build pipelines from View structure, or determine rendering kind. It just sends the View ID (and optionally a pipeline) and displays what comes back.

---

## File Summary

| Phase | File | Type |
|-------|------|------|
| 1 | `gearshift-kerml-runtime/src/main/resources/libraries/DocGen.kerml` | New |
| 1 | `gearshift-kerml-runtime/.../KerMLModel.kt` | Modify |
| 2 | `gearshift-kerml-runtime/.../docgen/ViewResolverService.kt` | New |
| 3 | `gearshift-kerml-runtime/.../docgen/PipelineModel.kt` | New |
| 3 | `gearshift-kerml-runtime/.../docgen/PipelineExtractor.kt` | New |
| 4 | `gearshift-kerml-runtime/.../docgen/PresentationModel.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/PipelineExecutor.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/steps/CollectStepExecutors.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/steps/FilterStepExecutors.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/steps/SortStepExecutors.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/steps/CombineStepExecutors.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/steps/PresentStepExecutors.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/steps/SectionStepExecutor.kt` | New |
| 5 | `gearshift-kerml-runtime/.../docgen/ElementPropertyResolver.kt` | New |
| 6 | `gearshift-kerml-runtime/.../docgen/DocGenPipelineService.kt` | New |
| 6 | `gearshift-kerml-runtime/.../docgen/DocGenApiModel.kt` | New |
| 6 | `gearshift-kerml-runtime/.../api/DocGenRoutes.kt` | New |
| 6 | Routing root (`Application.kt` or similar) | Modify |
| 8 | `gearshift-kerml-runtime/src/test/.../docgen/ViewResolverServiceTest.kt` | New |
| 8 | `gearshift-kerml-runtime/src/test/.../docgen/PipelineExtractorTest.kt` | New |
| 8 | `gearshift-kerml-runtime/src/test/.../docgen/PipelineExecutorTest.kt` | New |
| 8 | `gearshift-kerml-runtime/src/test/.../docgen/StepExecutorTest.kt` | New |
| 8 | `gearshift-kerml-runtime/src/test/.../docgen/RenderViewEndpointTest.kt` | New |

---

## Key Design Decisions

### 1. Single Endpoint, View Always Anchors

**Decision:** One endpoint: `POST /docgen/render-view`. The View element ID is always required. An optional `kerml` field lets the frontend override the pipeline.

**Rationale:**
- The View is always needed for expose/viewpoint/subview resolution — these come from the model, not the frontend
- The pipeline is the only variable: frontend-provided KerML text, View's own Rendering, or a default
- A single endpoint keeps the API surface small and the frontend simple
- The frontend's `KermlGeneratorService` already produces valid KerML text — sending it as a string field is trivial
- The backend's `KerMLModel.parseString()` handles it as a standard parse — no special code path

### 2. Rendering = Pipeline Container

**Decision:** The Rendering element (resolved via `View.rendering`) IS the pipeline — its Steps, Successions, and BindingConnectors define the execution DAG.

**Rationale:** This follows SysML v2 semantics: a Rendering is a Structure that defines *how* a View is presented. DocGen library behaviors (CollectOwned, PresentTable, etc.) are the step types within the Rendering. No separate "pipeline definition" is needed — the Rendering itself is the pipeline definition.

### 3. Derived Properties Do the Heavy Lifting

**Decision:** Use the existing metamodel derivation constraints (`deriveViewExposedElement`, `deriveViewRendering`, etc.) evaluated via `OclExecutor`, rather than writing manual traversal code.

**Rationale:**
- The derivation constraints are already defined and tested
- They implement the SysML v2 specification semantics exactly
- Changes to the spec are absorbed by updating the constraints, not the service code
- The `OclExecutor` handles all the edge cases (empty collections, type filtering, etc.)

### 4. DocGen Library as KerML Text

**Decision:** KerML text library file (`DocGen.kerml`) loaded by the parser.

**Rationale:** The parser already supports library loading. Writing the library as KerML text keeps it aligned with the specification documents. Steps typed by DocGen behaviors resolve naturally through the parser's import mechanism.

### 5. Pipeline Extraction to Intermediate Model

**Decision:** Extract to intermediate `PipelineDefinition` model, then execute.

**Rationale:** DocGen steps need domain-specific semantics (model traversal, OCL evaluation, presentation rendering). The intermediate model enables validation, clear error messages, and testability of extraction and execution independently.

### 6. Transclusion References in Output

**Decision:** Return `ElementRef` + property, not materialized text.

**Rationale:** The frontend's transclusion system renders element properties live for real-time updates and inline editing. Materializing server-side would break these features.

---

## Risks & Mitigations

| Risk | Mitigation |
|------|------------|
| Derived property constraints may not yet be wired for evaluation | Test `View.exposedElement`, `View.rendering` derivation early in Phase 2. If OCL evaluation isn't connected, wire it — the constraints and OCL engine both exist. |
| Parser can't handle DocGen KerML syntax (ColumnSpec tuples, enum access) | Test parsing of all 4 presets early in Phase 1. Extend grammar if needed. |
| Rendering structure varies — users may define Renderings with non-standard step layouts | Validate step typing against DocGen library during extraction. Return clear errors for unrecognized step types. |
| Large models cause slow pipeline execution | Execution timeout. Lazy/streaming traversal for `CollectOwned` depth=-1. |
| OCL expressions in user pipelines may be unsafe | Use `OclExecutor` timeout/recursion-depth guards. Element-count limits on collections. |
| Frontend-backend PresentationNode JSON mismatch | Manual alignment against `presentation.model.ts`. Future: generate TS types from Kotlin model. |
| View hierarchy cycles (View A subview → View B subview → View A) | Track visited View IDs during recursion. Break cycles with error. |
