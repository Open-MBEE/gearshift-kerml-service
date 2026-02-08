# Gearshift KerML Service Architecture

## Overview

The Gearshift KerML Service is a metadata-driven implementation of the KerML (Kernel Modeling Language) metamodel, built
on the **Mdm Framework** - a next-generation MOF (Meta-Object Facility) combined with modern model data management
capabilities.

## Design Philosophy

**"Metadata that feels like JSON"**

The system combines:

- **Declarative Metamodeling**: Define types using Kotlin's named parameters or JSON
- **MOF Power**: Full metamodel-driven architecture with validation and constraints
- **Graph-Native Storage**: MDMObjects as nodes, MDMLinks as edges in a property graph
- **OCL Evaluation**: Full Object Constraint Language parser and executor
- **KerML Expression Evaluation**: Native evaluation of KerML expression trees
- **Behavior Execution**: Token-passing execution engine for KerML Behaviors
- **Z3/SMT Constraint Solving**: Parametric analysis, optimization, and conflict detection
- **KerML Compliance**: Implements KerML specification with 85+ metaclasses

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────────┐
│                     Application Layer                            │
│  (REST API, Code Generation, Examples)                          │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│              Execution & Analysis Layer                         │
│  Layer 3: Z3/SMT Solver (Constraint Solving, Trade Studies)    │
│  Layer 2: Behavior Execution Engine (Token-passing)            │
│  Layer 1: KerML Expression Evaluator (Native tree walk)        │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                KerML Metamodel Layer (85+ classes)              │
│  Root: Element, Namespace, Membership, Relationship             │
│  Core: Type, Classifier, Feature, Specialization                │
│  Kernel: DataType, Class, Expression, Connector, Flow           │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                  Mdm Framework                             │
│                    GearshiftEngine                               │
│            (Unified API / Single Entry Point)                    │
└─────────────────────────────────────────────────────────────────┘
        │           │           │           │           │
   ┌────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
   │Metamodel│ │  Engine  │ │Constraint│ │Repository│ │  Query   │
   │Registry │ │  (MOF)   │ │  Engine  │ │  Layer   │ │  Engine  │
   └────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘
        │           │           │           │           │
        │     ┌──────────┐ ┌──────────┐ ┌──────────┐   │
        │     │   Name   │ │   OCL    │ │  Z3/SMT  │   │
        │     │ Resolver │ │ Executor │ │  Solver  │   │
        │     └──────────┘ └──────────┘ └──────────┘   │
        └───────────────────────────────────────────────┘
                              │
                    ┌──────────────────┐
                    │  Graph Storage   │
                    │ (see STORAGE_ARCHITECTURE.md)
                    └──────────────────┘
```

## Core Components

### 1. GearshiftEngine (`GearshiftEngine.kt`)

The unified API providing access to all framework capabilities:

```kotlin
val engine = GearshiftEngine()

// Metamodel management
engine.registerMetaClass(metaClass)
engine.registerAssociation(association)
engine.validateMetamodel()

// Instance (node) management
val (id, instance) = engine.createInstance("Feature")
engine.setProperty(id, "declaredName", "MyFeature")
val obj = engine.getInstance(id)

// Link (edge) management
val link = engine.createLink("FeatureMembership_OwnedMemberFeature", sourceId, targetId)
val outgoing = engine.getOutgoingLinks(sourceId, "FeatureMembership_OwnedMemberFeature")

// Constraint evaluation
val result = engine.evaluateOcl(instance, "self.ownedMembership->size()")

// Name resolution (KerML 8.2.3.5)
val resolved = engine.resolveName("A::B::C", namespaceId)

// Queries
val features = engine.getInstancesByType("Feature")
```

### 2. MDMEngine (`engine/MDMEngine.kt`)

Lower-level engine providing detailed control:

- Object and link creation/deletion with lifecycle notifications
- Property management with automatic indexing
- Constraint and operation invocation
- OCL expression evaluation via `OclExecutor`
- Name resolution via `NameResolver`
- Access to `ConstraintEngine` for derived properties

### 3. MDMObject (`engine/MDMObject.kt`)

Runtime instance representing a node in the model graph:

- Typed by a `MetaClass`
- Stores properties in a mutable map
- Has a unique ID assigned by repository
- Immutable reference to metaclass definition

### 4. MDMLink (`engine/MDMLink.kt`)

Runtime instance representing an edge connecting two MDMObjects:

- Typed by a `MetaAssociation`
- Directional (source → target)
- Provides navigability checks and aggregation inspection
- Bidirectional traversal support

## Metamodel System

### MetaClass (`metamodel/MetaClass.kt`)

Defines a class type:

```kotlin
val element = MetaClass(
    name = "Element",
    isAbstract = true,
    superclasses = emptyList(),
    attributes = listOf(
        MetaProperty(
            name = "declaredName",
            type = "String",
            multiplicity = "0..1"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveElementName",
            type = ConstraintType.DERIVATION,
            expression = "self.effectiveName()"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String"
        )
    )
)
```

**Key principle**: All non-primitive-typed properties are association ends defined separately. Only primitives (String,
Boolean, Integer) go in `attributes`.

### MetaAssociation (`metamodel/MetaAssociation.kt`)

Defines relationships between types:

```kotlin
val ownershipAssociation = MetaAssociation(
    name = "MembershipOwningNamespace_OwnedMembership",
    sourceEnd = MetaAssociationEnd(
        name = "membershipOwningNamespace",
        type = "Namespace",
        multiplicity = "0..1",
        isNavigable = true,
        aggregation = AggregationKind.NONE
    ),
    targetEnd = MetaAssociationEnd(
        name = "ownedMembership",
        type = "Membership",
        multiplicity = "0..*",
        isNavigable = true,
        aggregation = AggregationKind.COMPOSITE,
        isOrdered = true
    )
)
```

### MetamodelRegistry (`engine/MetamodelRegistry.kt`)

Central registry providing:

- Class and association lookup by name
- Inheritance hierarchy queries (superclasses, subclasses, isSubclassOf)
- Navigable association ends for a class
- Validation (circular inheritance, missing references)
- Thread-safe via ConcurrentHashMaps

## Constraint System

### ConstraintEngine (`constraints/ConstraintEngine.kt`)

Evaluates constraints on instances:

- **Derived properties**: Computes values using OCL or registered evaluators
- **Validation constraints**: Checks invariants against instances
- **Non-navigable ends**: Computes reverse association traversal
- **Implicit relationships**: Creates implied relationships (redefinition, specialization)

### Constraint Types (`constraints/ConstraintTypes.kt`)

| Type                         | Prefix           | Purpose                                    |
|------------------------------|------------------|--------------------------------------------|
| `DERIVATION`                 | `derive`         | Compute derived property values            |
| `VERIFICATION`               | `check`/`verify` | Validate invariants                        |
| `NON_NAVIGABLE_END`          | `compute`        | Calculate reverse association ends         |
| `IMPLICIT_REDEFINITION`      | -                | Create inferred Redefinition relationships |
| `IMPLICIT_TYPE_FEATURING`    | -                | Create inferred TypeFeaturing              |
| `IMPLICIT_BINDING_CONNECTOR` | -                | Create BindingConnector between features   |

### ConstraintRegistry (`constraints/ConstraintRegistry.kt`)

Registry for constraint evaluators:

- Registers derived property, validation, and association end evaluators
- Supports inheritance-based lookup (checks superclasses)
- Evaluators can be Kotlin lambdas or OCL expressions

## OCL Implementation

### Architecture

```
OCL text → OclParser → OclExpression AST → OclExecutor → result
           (ANTLR)     (OclAst.kt)        (OclVisitor)
```

### OclParser (`constraints/parsers/ocl/OclParser.kt`)

Parses OCL text to AST using ANTLR:

- Uses `OCLLexer` and `OCLParser` generated from grammar
- Visitor pattern builds custom AST nodes
- Full error handling via `OclErrorListener`

### OclAst (`constraints/parsers/ocl/OclAst.kt`)

Complete AST node hierarchy:

- **Literals**: Null, Boolean, Integer, Real, String, UnlimitedNatural, Collection
- **Navigation**: Variable, PropertyCall, NavigationCall, ArrowCall
- **Operations**: OperationCall, InfixExp, PrefixExp
- **Iterators**: select, reject, collect, forAll, exists, any, one, closure, iterate
- **Control**: IfExp, LetExp
- **Types**: oclIsKindOf, oclIsTypeOf, oclAsType

### OclExecutor (`constraints/parsers/ocl/OclExecutor.kt`)

Evaluates AST against MDM objects:

```kotlin
val executor = OclExecutor(context)
val result = executor.evaluate(ast)

// Example expressions:
// "self.ownedMembership->size()"
// "self.ownedMembership->select(m | m.visibility = 'public')"
// "self.owner.oclAsType(Namespace).member->includes(self)"
// "self.ownedMembership->closure(m | m.memberElement.ownedMembership)"
```

**Recursion safety**: Uses LinkedHashSet for visited tracking and ArrayDeque for work queue in operations like
`closure`.

## Execution & Analysis

Three execution layers build on the OCL engine and KerML metamodel to provide expression evaluation, behavior
execution, and constraint solving.

### Layer 1: KerML Expression Evaluator (`gearshift-kerml-runtime/.../eval/`)

Evaluates KerML Expression model elements by walking MDMObject expression trees. This is distinct from OCL evaluation,
which processes text-based constraint expressions.

**KernelFunctionLibrary** (`KernelFunctionLibrary.kt`)
- Registry mapping KerML operator strings to native evaluation functions
- Covers: arithmetic (`+`, `-`, `*`, `/`, `%`, `**`), comparison (`<`, `>`, `<=`, `>=`), equality (`=`, `<>`),
  boolean (`and`, `or`, `not`, `xor`, `implies`), string (`ToString`, `Size`, `Substring`, `Concat`),
  control (`if`, `??`, `#`)
- Handles Integer/Rational promotion and short-circuit boolean evaluation

**KerMLExpressionEvaluator** (`KerMLExpressionEvaluator.kt`)

```kotlin
class KerMLExpressionEvaluator(engine: MDMEngine, functionLibrary: KernelFunctionLibrary) {
    fun evaluate(expression: MDMObject, target: MDMObject): List<MDMObject>
    fun isModelLevelEvaluable(expression: MDMObject, visited: Set<MDMObject> = emptySet()): Boolean
}
```

Dispatches on `MDMObject.className`:
- **LiteralExpression** → returns `Sequence{self}`
- **OperatorExpression** → evaluate arguments, apply operator via KernelFunctionLibrary
- **InvocationExpression** → evaluate arguments, bind to parameters, apply function body
- **FeatureReferenceExpression** → resolve referent Feature, return its value
- **FeatureChainExpression** → evaluate source, navigate target feature on result
- **SelectExpression / CollectExpression / IndexExpression** → collection operations
- **NullExpression** → returns empty sequence

**KerMLExpressionOperationHandler** (`KerMLExpressionOperationHandler.kt`)
- Wires the evaluator into MDMEngine by replacing OCL-based operation bodies with `MetaOperation.native{}` handlers
- Installs native `evaluate(target)`, `modelLevelEvaluable(visited)`, and `checkCondition(target)` operations on
  Expression metaclasses

### Layer 2: Behavior Execution Engine (`gearshift-kerml-runtime/.../exec/`)

Token-passing execution semantics for Behaviors composed of Steps connected by Successions, with data transfer via
Flows. Inspired by fUML but dramatically simplified.

**Execution Model** (`ExecutionModel.kt`)
- `Token` (sealed): `ControlToken` (execution flow) and `ObjectToken` (carries data)
- `StepState`: WAITING → READY → EXECUTING → COMPLETED
- `StepExecution`: runtime state per Step node
- `ExecutionGraph`: Steps + SuccessionEdges + FlowEdges with topology queries

**ExecutionContext** (`ExecutionContext.kt`)
- Lexical scoping with `bind()`, `resolve()`, `isDefined()`, `createChild()`
- Parent chain for nested behavior invocations

**BehaviorExecutionEngine** (`BehaviorExecutionEngine.kt`)

```kotlin
class BehaviorExecutionEngine(
    engine: MDMEngine, expressionEvaluator: KerMLExpressionEvaluator, maxSteps: Int = 1000
) {
    fun execute(behavior: MDMObject, inputs: Map<String, Any?> = emptyMap()): ExecutionResult
}
```

Execution loop:
1. Build execution graph from Behavior's Steps and Successions
2. Place initial control tokens on Steps with no predecessors
3. Find READY steps (all input tokens available)
4. Execute step: Expression → Layer 1 evaluator; Behavior → recurse
5. Evaluate guard conditions on outgoing Successions
6. Propagate tokens along satisfied Successions, transfer data via Flows
7. Repeat until no READY steps or `maxSteps` exceeded

### Layer 3: Z3/SMT Constraint Solver

Constraint solving for parametric analysis, requirement conflict detection, and trade studies.

**OclToZ3Translator** (`mdm-framework/.../query/ocl/OclToZ3Translator.kt`)
- Implements `OclVisitor<Expr<*>>` — translates OCL AST to Z3 expressions
- Supports: arithmetic, comparison, equality, boolean operators, if-then-else, let expressions, variables, literals
- Unsupported (throws `UnsupportedOperationException`): iterators, navigation, strings, type operations

**ConstraintSolverService** (`mdm-framework/.../constraints/ConstraintSolverService.kt`)

```kotlin
class ConstraintSolverService {
    fun solve(variables: List<Z3Variable>, constraints: List<String>): SolverResult
    fun isSatisfiable(variables: List<Z3Variable>, constraints: List<String>): Boolean
    fun optimize(variables, constraints, objective, minimize): OptimizationResult
    fun findConflicts(variables, constraints): ConflictResult
}
```

- Creates a new Z3 Context per call and disposes it after use (thread-safe)
- Parses OCL via `OclParser`, translates via `OclToZ3Translator`
- Uses Z3 `assertAndTrack()` + `unsatCore()` for conflict detection
- Uses Z3 `Optimize` class for minimization/maximization

**ParametricAnalysisService** (`gearshift-kerml-runtime/.../analysis/ParametricAnalysisService.kt`)
- KerML-specific layer that extracts constraints from Invariant model elements
- Infers Z3Sort from Feature types (Integer→INT, Real→REAL, Boolean→BOOL)
- `solveConstraints()`: find satisfying assignments over KerML Features
- `checkRequirementConsistency()`: detect conflicting requirements
- `tradeStudy()`: optimize an objective subject to constraints

**Dependency**: `tools.aqua:z3-turnkey:4.13.0` (bundles Z3 Java API + native binaries for all platforms)

## KerML Metamodel Implementation

### Package Structure

**Root Package (14 classes)** - Fundamental elements:

- `Element` - Abstract root of all KerML elements
- `Relationship` - Abstract base for relationships
- `Namespace` - Container with membership
- `Membership`, `OwningMembership` - Member ownership
- `Import`, `MembershipImport`, `NamespaceImport`
- `Annotation`, `Comment`, `Documentation`
- `AnnotatingElement`, `Dependency`

**Core Package (23 classes)** - Types and features:

- `Type`, `Classifier`, `Feature`
- `Structure`, `Class`
- `Specialization`, `Subsetting`, `Redefinition`
- `FeatureMembership`, `FeatureTyping`, `Featuring`
- `Conjugation`, `Disjoining`, `Intersecting`, `Unioning`, `Differencing`

**Kernel Package (48+ classes)** - Concrete concepts:

- Classifiers: `DataType`, `Association`, `Behavior`, `Function`
- Expressions: `Expression`, `LiteralExpression`, `OperatorExpression`, `InvocationExpression`
- Specialized: `CollectExpression`, `SelectExpression`, `FeatureChainExpression`
- Flows: `Flow`, `FlowEnd`, `SuccessionFlow`, `Connector`
- Metadata: `MetadataFeature`, `MetadataAccessExpression`

### Association Definitions (37+ files)

Organized by domain:

- `ElementsAssociations.kt` - Core element relationships
- `TypesAssociations.kt` - Type hierarchy
- `FeaturesAssociations.kt` - Feature relationships
- `ClassifiersAssociations.kt` - Classifier relationships
- `ExpressionsAssociations.kt` - Expression relationships

### KerMLMetamodelLoader (`kerml/KerMLMetamodelLoader.kt`)

Initializes the complete metamodel:

```kotlin
fun loadKerMLMetamodel(registry: MetamodelRegistry) {
    // 1. Register Root package classes
    registerRootClasses(registry)

    // 2. Register Core package classes
    registerCoreClasses(registry)

    // 3. Register Kernel package classes
    registerKernelClasses(registry)

    // 4. Register all associations
    registerAssociations(registry)

    // 5. Validate
    registry.validate()
}
```

## Parser System

### KerML Parsing (`kerml/parser/`)

**Main components:**

- `KerMLParseCoordinator.kt` - Main parsing orchestration
- `KerMLVisitorFactory.kt` - Factory for creating visitors
- **45+ visitor files** organized by domain

**Visitor architecture:**

- Visitor pattern for ANTLR-generated parse trees
- Each visitor handles specific metaclasses
- Coordination via `KerMLParseCoordinator`

## Repository & Query

### ModelRepository (`repository/ModelRepository.kt`)

In-memory storage for MDMObjects:

- Three indices: by ID, by type, by property value
- O(1) lookups for common operations
- Thread-safe via ConcurrentHashMaps
- Automatic index maintenance on changes

### LinkRepository (`repository/LinkRepository.kt`)

Stores MDMLink instances:

- Indexed by source ID, target ID, association name
- Bidirectional traversal support
- Cascade deletion support

### QueryEngine (`query/QueryEngine.kt`)

Fluent query API:

```kotlin
val results = queryEngine
    .from("Feature")
    .where("isAbstract", true)
    .filter { it.getProperty("declaredName")?.toString()?.startsWith("Base") == true }
    .execute()
```

## Code Generation

### MetamodelCodeGenerator (`codegen/MetamodelCodeGenerator.kt`)

Generates typed interfaces from metamodel:

```kotlin
// Generated interface
interface IFeature : IType {
    val declaredName: String?
    val isAbstract: Boolean
    val ownedFeature: List<IFeature>

    fun effectiveName(): String?
}

// Generated implementation
class FeatureImpl(wrapped: MDMObject) : TypeImpl(wrapped), IFeature {
    override val declaredName: String?
        get() = wrapped.getProperty("declaredName") as? String
    // ...
}
```

Output locations:

- `org.openmbee.gearshift.generated.interfaces` - Type-safe interfaces
- `org.openmbee.gearshift.generated.impl` - Base implementations
- `org.openmbee.gearshift.generated.Wrappers` - Factory utilities

## Factory Pattern

### MDMModelFactory (`MDMModelFactory.kt`)

Abstract base factory for models:

```kotlin
abstract class MDMModelFactory(
    val engine: GearshiftEngine,
    projectId: String? = null,
    projectName: String = "Untitled Project"
) {
    val project: ProjectMetadata  // SysML v2 API aligned
    val projectId: String

    fun <T> createElement(typeName: String): T
    fun resolveQualifiedName(qualifiedName: String): MDMObject?
    fun <T : IElement> getWrapper(obj: MDMObject): T
}
```

### KerMLModelFactory (`kerml/KerMLModelFactory.kt`)

KerML-specific factory with full metamodel initialization.

## Lifecycle & Events

### LifecycleHandler (`engine/Lifecycle.kt`)

Interface for model change events:

```kotlin
interface LifecycleHandler {
    fun onObjectCreated(obj: MDMObject)
    fun onObjectDeleted(obj: MDMObject)
    fun onPropertyChanged(obj: MDMObject, property: String, oldValue: Any?, newValue: Any?)
    fun onLinkCreated(link: MDMLink)
    fun onLinkDeleted(link: MDMLink)

    val priority: Int  // Lower values called first
}
```

## Data Flow

### Instance Creation

```
1. Request createInstance("Feature")
   ↓
2. Validate metaclass exists in registry
   ↓
3. Create MDMObject linked to MetaClass
   ↓
4. Store in ModelRepository (indexed by ID, type)
   ↓
5. Trigger lifecycle handlers (onObjectCreated)
   ↓
6. Return (id, instance)
```

### Property Access with Derivation

```
1. Request getProperty("name")
   ↓
2. Check if property is derived
   ↓
3a. If not derived: return stored value
3b. If derived: look up derivationConstraint
   ↓
4. Parse OCL expression
   ↓
5. Execute via OclExecutor
   ↓
6. Return computed value
```

### Name Resolution (KerML 8.2.3.5)

```
1. Parse qualified name (A::B::C)
   ↓
2. Check for cycles (resolution stack)
   ↓
3. Execute resolution algorithm:
   - Single segment: full resolution in namespace
   - Multi-segment: resolve qualification, then last segment
   - Global scope ($::): resolve from model root
   ↓
4. Return Membership containing memberElement
```

## Technology Stack

- **Kotlin** 1.9+ - Language
- **Gradle** 8.5+ - Build tool
- **Jackson** 2.16+ - JSON serialization
- **ANTLR** 4.13+ - Grammar parsing (OCL, KerML)
- **Z3** 4.13+ (via z3-turnkey) - SMT solver for constraint solving and parametric analysis
- **SLF4J + kotlin-logging** - Logging
- **ConcurrentHashMap** - Thread-safe collections

## Build Commands

```bash
# Compile
./gradlew compileKotlin --no-configuration-cache

# Run tests
./gradlew test --no-configuration-cache

# Generate ANTLR grammar
./gradlew generateGrammarSource --no-configuration-cache

# Generate code from metamodel
./gradlew generateCode --no-configuration-cache
```

## Related Documentation

- **STORAGE_ARCHITECTURE.md** - Hot/cold storage pattern, Git-like versioning with KerML as commit payload
- **CLAUDE.md** - Development guidelines and conventions

## KerML Compliance

Implements KerML v1.0 specification:

- ✅ 8.2.3.5 Name Resolution (full implementation)
- ✅ 8.2.3.4 Membership and Visibility
- ✅ Metamodel (85+ classes, 37+ associations)
- ✅ OCL Expression Evaluation
- ⏳ 8.4.2 Implied Relationships (partial - framework in place)
- ⏳ Complete constraint derivations

## Summary

The Gearshift KerML Service provides a production-ready, metadata-driven foundation for KerML implementation. Key
achievements:

1. **Complete Metamodel**: 85+ KerML metaclasses with full inheritance hierarchy
2. **OCL Support**: Full parser and executor for Object Constraint Language
3. **Expression Evaluation**: Native KerML expression tree evaluation with KernelFunctionLibrary
4. **Behavior Execution**: Token-passing engine for Steps, Successions, and Flows
5. **Parametric Analysis**: Z3/SMT solver integration for constraint solving, optimization, and conflict detection
6. **Constraint System**: Derivation, validation, and implicit relationship support
7. **Graph Storage**: MDMObjects and MDMLinks as property graph with efficient indexing
8. **Code Generation**: Auto-generated type-safe interfaces
9. **Versioning Ready**: Architecture supports Git-like version control with KerML as commit payload

The "metadata that feels like JSON" philosophy makes defining metamodels intuitive while maintaining the rigor of formal
metamodeling approaches.
