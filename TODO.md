# TODO — Runtime Issues

## OCL Parser Gaps

- [ ] `Set{}` / `OrderedSet{}` / `Sequence{}` collection literal constructors not supported as expression arguments
  - Causes: `modelLevelEvaluable(Set{})` and similar expressions fail with "no viable alternative"
  - Impact: Derived properties using these return null instead of computed value

## Performance / Cycle Issues

- [ ] `processImpliedRelationships` performance (~18s)
  - Disabled in API (`ProjectStore.createModel`) as workaround
  - Need to profile and optimize, then re-enable

- [ ] Investigate OCL evaluator multi-parameter propagation
  - `inheritedMemberships` chain was made native (`supertypes`, `inheritableMemberships`, `inheritedMemberships`, `nonPrivateMemberships` on Type; `supertypes` on Feature)
  - The cascade/stack overflow was caused by OCL not propagating `excludedTypes` correctly, not by implied relationships
  - Native implementations fix the cycle but should understand why OCL dropped the parameters

## Solver

- [ ] Mass budget solver only finds 3 variables / 4 constraints instead of 7 / 14
  - Cross-class feature references (e.g., `Propulsion::mass` within `Spacecraft`) not resolved by `collectReferencedFeatures`
  - Demo test `should solve the mass budget with valid rollup` fails

## Branching & Merge (Git-Backed)

Design: use git as the merge engine for KerML model changes. The KerML text is already
the source of truth and is persisted as files on disk — lean into that.

### Architecture

- Each project's data directory becomes a git repo (managed via JGit, in-process)
- `createCommitWithKerML` → `git add` + `git commit` of `.kerml` files
- Branching → `git branch` / `git checkout`
- Merge → `git merge`, then reparse the result; post-merge validation catches semantic breaks
- History, revert, diff → delegated to git

### Model Index (`ID ↔ QN`)

A bidirectional map committed alongside `.kerml` files (e.g. `.gearshift/index.json`):

```json
{ "entries": { "550e8400-...": "Vehicles::Car", ... } }
```

Purpose:
- **ID stability across reparse** — on reparse, load previous index, remap IDs where QN matches
- **Rename detection** — QN changed but element is structurally the same → preserve old ID
- **API resolution** — ID → QN → element lookup, avoids scanning all elements
- **Post-merge reconciliation** — diff old vs new index to classify adds/deletes/renames

Reconciliation runs as a post-parse step:
`parse → reconcileIndex(previousIndex, currentElements) → commit index + kerml together`

### Merge Flow

```
POST /projects/{projectId}/branches/{branch}/merge?source={sourceBranch}

1. jgit.merge(sourceBranch)
2. if clean merge:
   a. reparse merged .kerml files
   b. reconcile model index (preserve IDs where possible)
   c. if parse succeeds → 201, return new commit
   d. if parse fails → 409, "semantic conflict" (syntactically valid but broken references)
3. if git conflict markers:
   a. return 409 with conflict regions
   b. client resolves, POSTs resolved KerML
   c. reparse + reconcile + commit
```

### Multi-File Consideration

Splitting KerML into one file per top-level package improves merge granularity —
edits to different packages never conflict. Aligns with typical SysML project organization.

### Tasks

- [ ] Add JGit dependency to `gearshift-kerml-runtime`
- [ ] Implement `ModelIndex` (ID ↔ QN bidirectional map, JSON serialization)
- [ ] Integrate index reconciliation into `KerMLModel` post-parse
- [ ] Refactor `FileProjectBackend` to init/commit to a git repo
- [ ] Add branch CRUD endpoints to `SysmlApiRoutes`
- [ ] Add merge endpoint with post-merge reparse + validation
- [ ] Translate git conflict markers into API response format
- [ ] Investigate multi-file project layout (one `.kerml` per package)

## Cleanup

- [ ] Re-enable implied relationships once performance is fixed
- [ ] Audit non-navigable association end constraints that use `allInstances()` for correctness with local-only scope
