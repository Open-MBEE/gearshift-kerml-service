# Plan: Upgrade DemoAPI to SysML v2 API and Services

## Context

The current `DemoApi.kt` provides a minimal custom API (`/parse`, `/query`, `/health`, `/library-status`) for interactive KerML exploration. The goal is to add a standards-compliant SysML v2 REST API alongside it, implementing the core subset of the [Systems Modeling API and Services](references/sysml_OpenAPI.json) spec. This enables the frontend (which already has `project.model.ts` types ready) to manage projects, commits, and elements using the official API shape.

The existing `STORAGE_ARCHITECTURE.md` doc describes a "KerML as commit payload" model where commits contain KerML text and the graph is materialized by replaying commits. We'll implement this pattern with in-memory storage + JSON export.

## Scope (Core Subset)

**In scope:**
- Project CRUD (3 endpoints)
- Commit management (3 endpoints: list, create, get)
- Element read (3 endpoints: list, get by ID, roots)
- Relationship read (1 endpoint)
- Element JSON serialization (`@id`/`@type` format per ptc-25-04-21.json)
- JSON export/import of project state

**Deferred:**
- Branch/Tag CRUD
- Diff & Merge
- Query CRUD (saved queries)
- Meta/datatypes endpoints
- Pagination (stubbed but not cursor-based)

**Preserved:**
- All existing demo endpoints (`/parse`, `/query`, `/health`, `/library-status`, `/`)

---

## Phase 1: Version Control Data Model

Add in-memory data structures for the SysML v2 version control concepts.

### New file: `mdm-framework/.../runtime/VersionControl.kt`

```kotlin
data class BranchData(
    val id: String,          // @id
    val name: String,
    val owningProject: String, // project ID
    val headCommitId: String?,
    val created: Instant
)

data class CommitData(
    val id: String,          // @id
    val description: String?,
    val owningProject: String,
    val previousCommit: List<String>,  // parent commit IDs
    val created: Instant,
    val changes: List<DataVersionData>
)

data class DataVersionData(
    val id: String,          // @id
    val identityId: String,  // element ID being changed
    val payload: Map<String, Any?>?  // null = deletion
)
```

### New file: `mdm-framework/.../storage/ProjectStore.kt`

In-memory multi-project store:
- `projects: Map<String, ProjectMetadata>`
- `branches: Map<String, BranchData>` (keyed by branchId)
- `commits: Map<String, CommitData>` (keyed by commitId)
- `projectModels: Map<String, KerMLModel>` (live materialized state per project)
- CRUD methods for each entity
- On create project: auto-create "main" branch + initial empty commit
- On create commit: parse KerML payload, apply to model, snapshot element state

**Key files to modify:**
- `mdm-framework/src/main/kotlin/org/openmbee/mdm/framework/runtime/MDMModel.kt` — reuse `ProjectMetadata`

---

## Phase 2: Element JSON Serializer

Serialize `MDMObject` instances to the `@id`/`@type` JSON-LD format defined in `ptc-25-04-21.json`.

### New file: `mdm-framework/.../runtime/ElementSerializer.kt`

Responsibilities:
- `MDMObject → Map<String, Any?>` with `@id`, `@type`, and all properties
- Association ends → `{"@id": "uuid"}` (Identified reference pattern)
- Arrays of references → `[{"@id": "..."}, ...]`
- Nullable properties → include as `null` or omit (configurable)
- Primitive properties → direct values
- Use `MetamodelRegistry` to discover all attributes + association ends for a given type

This reuses the pattern already established in the TypeScript code generator (`ElementRef` = `{"@id": string}`) but at runtime in Kotlin.

**Key files to reference:**
- `mdm-framework/.../storage/GraphStorage.kt` — existing `toNodeData()` extension (lines 294-300)
- `mdm-framework/.../runtime/MDMEngine.kt` — `getProperty()`, `getLinkedTargets()`
- `references/ptc-25-04-21.json` — target schema for element shapes

---

## Phase 3: SysML v2 API Routes

Add new Ktor route groups under `/projects/...` in the existing server.

### Modify: `gearshift-kerml-runtime/.../api/DemoApi.kt`

Refactor into modular route files or add route groups. Keep existing demo routes intact.

### New file: `gearshift-kerml-runtime/.../api/SysmlApiRoutes.kt`

**Project endpoints:**
| Method | Path | Handler |
|--------|------|---------|
| GET | `/projects` | List all projects |
| POST | `/projects` | Create project (name, description) → auto-creates main branch + initial commit |
| GET | `/projects/{projectId}` | Get project by ID |
| PUT | `/projects/{projectId}` | Update project name/description |
| DELETE | `/projects/{projectId}` | Delete project and all data |

**Commit endpoints:**
| Method | Path | Handler |
|--------|------|---------|
| GET | `/projects/{projectId}/commits` | List commits for project |
| POST | `/projects/{projectId}/commits` | Create commit (KerML payload or element changes) |
| GET | `/projects/{projectId}/commits/{commitId}` | Get commit by ID |

**Element endpoints (read-only, against a commit):**
| Method | Path | Handler |
|--------|------|---------|
| GET | `/projects/{projectId}/commits/{commitId}/elements` | List all elements (JSON-LD format) |
| GET | `/projects/{projectId}/commits/{commitId}/elements/{elementId}` | Get single element |
| GET | `/projects/{projectId}/commits/{commitId}/roots` | Get root namespace elements |

**Relationship endpoint:**
| Method | Path | Handler |
|--------|------|---------|
| GET | `/projects/{projectId}/commits/{commitId}/elements/{relatedElementId}/relationships` | Get relationships for element |

**Response format:**
- All responses use `@id`/`@type` JSON-LD format
- Error responses: `{"@type": "Error", "description": "..."}`
- List responses return JSON arrays directly

**Key files to modify:**
- `gearshift-kerml-runtime/.../api/DemoApi.kt` — install new route group, inject `ProjectStore`
- `gearshift-kerml-runtime/build.gradle.kts` — no new deps needed (already has Ktor + Jackson)

---

## Phase 4: JSON Export/Import

Leverage existing `GraphStorage.export()`/`import()` to persist project state.

### New file: `gearshift-kerml-runtime/.../api/ExportRoutes.kt` (or in SysmlApiRoutes)

| Method | Path | Handler |
|--------|------|---------|
| GET | `/projects/{projectId}/export` | Export project as JSON (all commits + current element state) |
| POST | `/projects/import` | Import project from JSON |

Export format aligned with model interchange spec (section 10.3):
```json
{
  "project": { "@id": "...", "@type": "Project", "name": "...", ... },
  "commits": [ ... ],
  "elements": [ { "@id": "...", "@type": "Class", ... }, ... ]
}
```

**Key files to reference:**
- `mdm-framework/.../storage/GraphStorage.kt` — `GraphData`, `export()`, `import()`
- `docs/model-interchange/kerml-model-interchange.md` — interchange format spec

---

## Phase 5: Frontend Service Updates

### Modify: `gearshift-view-editor/src/app/core/services/api/`

Add a new `ProjectService` (or `SysmlApiService`) that calls the new endpoints:

### New file: `gearshift-view-editor/src/app/core/services/api/project.service.ts`

```typescript
@Injectable({ providedIn: 'root' })
export class ProjectService {
  getProjects(): Observable<Project[]>
  createProject(req: ProjectRequest): Observable<Project>
  getProject(id: string): Observable<Project>
  getCommits(projectId: string): Observable<Commit[]>
  createCommit(projectId: string, req: CommitRequest): Observable<Commit>
  getElements(projectId: string, commitId: string): Observable<Record<string, unknown>[]>
  getElement(projectId: string, commitId: string, elementId: string): Observable<Record<string, unknown>>
  getRoots(projectId: string, commitId: string): Observable<Record<string, unknown>[]>
  exportProject(projectId: string): Observable<Blob>
}
```

### Modify: `gearshift-view-editor/src/app/core/services/api/api-client.service.ts`
- Add `put<T>()` and `delete<T>()` methods

**Existing types to reuse** (already defined):
- `project.model.ts` — `Project`, `ProjectRequest`, `Branch`, `Commit`, `CommitRequest`, `DataVersion`, `DataIdentity`

---

## File Summary

### New files (backend):
1. `mdm-framework/src/main/kotlin/org/openmbee/mdm/framework/runtime/VersionControl.kt` — Branch, Commit, DataVersion data classes
2. `mdm-framework/src/main/kotlin/org/openmbee/mdm/framework/storage/ProjectStore.kt` — in-memory multi-project store
3. `mdm-framework/src/main/kotlin/org/openmbee/mdm/framework/runtime/ElementSerializer.kt` — MDMObject → @id/@type JSON
4. `gearshift-kerml-runtime/src/main/kotlin/org/openmbee/gearshift/api/SysmlApiRoutes.kt` — all SysML v2 API route handlers

### Modified files (backend):
5. `gearshift-kerml-runtime/src/main/kotlin/org/openmbee/gearshift/api/DemoApi.kt` — wire up new routes + ProjectStore

### New files (frontend):
6. `gearshift-view-editor/src/app/core/services/api/project.service.ts` — Angular service for SysML v2 API

### Modified files (frontend):
7. `gearshift-view-editor/src/app/core/services/api/api-client.service.ts` — add `put`/`delete` methods

---

## Verification

1. **Compile**: `./gradlew compileKotlin --no-configuration-cache`
2. **Tests**: `./gradlew test --no-configuration-cache`
3. **Manual smoke test**:
   - Start server: `./gradlew runDemoApi --no-configuration-cache`
   - Create project: `curl -X POST localhost:8080/projects -H 'Content-Type: application/json' -d '{"name":"Test"}'`
   - Create commit with KerML: `curl -X POST localhost:8080/projects/{id}/commits -H 'Content-Type: application/json' -d '{"description":"initial","change":[...]}'`
   - List elements: `curl localhost:8080/projects/{id}/commits/{cid}/elements`
   - Verify `@id`/`@type` JSON format in responses
   - Verify existing `/parse` and `/query` still work
4. **Frontend**: `ng serve` and verify API client can list projects
