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
- **Dual Code Generation**: Kotlin typed interfaces and TypeScript interfaces from metamodel
- **Mount System**: Read-only library sharing across sessions without copying
- **KerML Compliance**: Implements KerML specification with 85+ metaclasses and 127+ associations

## Module Structure

The project is a multi-module Gradle build with four modules:

```
gearshift-kerml-service/
├── mdm-framework/           # Core metamodel framework (MetaClass, MDMEngine, OCL, Z3)
├── gearshift-kerml-model/   # KerML metamodel definitions (85+ metaclasses, 127+ associations)
├── kerml-generated/         # Generated Kotlin code output (interfaces + implementations)
├── gearshift-kerml-runtime/ # KerML parser, writer, expression evaluator, behavior engine
└── docs/                    # Architecture and specification documentation
```

**Module dependency graph:**

```
mdm-framework
      ↑
gearshift-kerml-model
      ↑
kerml-generated
      ↑
gearshift-kerml-runtime
```

## Architecture Layers

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

## Core Components

### MDMEngine (`mdm-framework/.../runtime/MDMEngine.kt`)

The core runtime container and single source of truth:

```kotlin
val registry = MetamodelRegistry()
KerMLMetamodelLoader.initialize(registry)
val engine = MDMEngine(registry)

// Instance (node) management
val element = engine.createElement("Feature")
engine.setProperty(element.id!!, "declaredName", "MyFeature")

// Link (edge) management
engine.createLink("FeatureMembership_OwnedMemberFeature", sourceId, targetId)
val targets = engine.getLinkedTargets("FeatureMembership_OwnedMemberFeature", sourceId)

// Constraint evaluation (derived properties computed automatically)
val name = engine.getProperty(element.id!!, "name") // Evaluates derivation constraint

// Operation invocation
val result = engine.invokeOperation(element.id!!, "effectiveName", emptyMap())

// OCL expression evaluation
engine.registerEvaluator(ExpressionLanguage.OCL, OclExpressionEvaluator())
```

Key responsibilities:
- Element storage and access (by ID, by class)
- Association graph (MDMLink edges)
- Property access (stored, derived via constraints, association-based)
- Operation invocation with language dispatch
- Lifecycle events via registered `LifecycleHandler`s
- Expression evaluator registration (OCL, GQL, etc.)

### MDMObject (`mdm-framework/.../runtime/MDMObject.kt`)

Runtime instance representing a node in the model graph:

- Typed by a `MetaClass`
- Stores properties in a mutable map
- Has a unique ID assigned by the engine
- Immutable reference to metaclass definition

### MDMLink (`mdm-framework/.../runtime/MDMLink.kt`)

Runtime instance representing an edge connecting two MDMObjects:

- Typed by a `MetaAssociation`
- Directional (source -> target)
- Provides navigability checks and aggregation inspection
- Bidirectional traversal support

### MDMModel / KerMLModel

High-level model APIs built on MDMEngine:

- `MDMModel` - Base class with project metadata (SysML v2 API aligned)
- `KerMLModel` - KerML-specific: `parseString()`, `parseFile()`, library initialization, typed accessors

```kotlin
val model = KerMLModel()
val result = model.parseString("""
    package Vehicles {
        class Vehicle {
            feature mass : Real;
        }
    }
""")
```

### MountableEngine (`mdm-framework/.../runtime/MountableEngine.kt`)

Extends MDMEngine with mount support for sharing library content across sessions:

- **Mounts**: Read-only engines (libraries) attached to sessions without copying
- **Resolution order**: Local -> Explicit Mounts -> Implicit Mounts
- **Immutability enforcement**: Mounted elements cannot be modified, deleted, or be sources of new links
- **Use case**: KerML Kernel Semantic Library loaded once, mounted into multiple sessions

```kotlin
val engine = MountableEngine(schema, factory)
engine.mountImplicit()  // Auto-mount standard libraries

// Resolve library types through mount
val anything = engine.getElement("base-anything-id")

// Local operations work normally
val myClass = engine.createElement("Class")

// Modifying mounted content throws MountedElementReadOnlyException
```

Priority ranges: 0-99 reserved, 100-499 explicit mounts, 500-999 reserved, 1000+ implicit libraries.

## Metamodel System

### MetaClass (`mdm-framework/.../meta/MetaClass.kt`)

Defines a class type in the metamodel:

```kotlin
val element = MetaClass(
    name = "Element",
    isAbstract = true,
    superclasses = emptyList(),
    attributes = listOf(
        MetaProperty(
            name = "declaredName",
            type = "String",
            description = "The declared name of this Element"
        ),
        MetaProperty(
            name = "isLibraryElement",
            type = "Boolean",
            isDerived = true,
            derivationConstraint = "deriveElementIsLibraryElement"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveElementName",
            type = ConstraintType.DERIVATION,
            expression = "self.effectiveName()",
            description = "Derivation for Element::name"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            body = MetaOperation.ocl("declaredName"),
            isQuery = true
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "elementAnythingBinding",
            baseConcept = "Base::Anything",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    )
)
```

**Key principle**: All non-primitive-typed properties are association ends defined separately. Only primitives (String,
Boolean, Integer) go in `attributes`.

### MetaAssociation (`mdm-framework/.../meta/MetaAssociation.kt`)

Defines relationships between types:

```kotlin
val association = MetaAssociation(
    name = "MembershipOwningNamespace_OwnedMembership",
    sourceEnd = MetaAssociationEnd(
        name = "membershipOwningNamespace",
        type = "Namespace",
        lowerBound = 0, upperBound = 1,
        isNavigable = true,
        aggregation = AggregationKind.NONE
    ),
    targetEnd = MetaAssociationEnd(
        name = "ownedMembership",
        type = "Membership",
        lowerBound = 0, upperBound = -1,
        isNavigable = true,
        aggregation = AggregationKind.COMPOSITE,
        isOrdered = true
    )
)
```

### MetaConstraint (`mdm-framework/.../meta/MetaConstraint.kt`)

Constraints with typed semantics:

| Type                         | Prefix           | Purpose                                    |
|------------------------------|------------------|--------------------------------------------|
| `DERIVATION`                 | `derive`         | Compute derived property values             |
| `VERIFICATION`               | `check`/`verify` | Validate invariants                         |
| `NON_NAVIGABLE_END`          | `compute`        | Calculate reverse association ends          |
| `REDEFINES_DERIVATION`       | `derive`         | Inferred from metamodel redefinition        |
| `SUBSETS_DERIVATION`         | `derive`         | Inferred from metamodel subsetting          |
| `IMPLICIT_REDEFINITION`      | -                | Create inferred Redefinition relationships  |
| `IMPLICIT_TYPE_FEATURING`    | -                | Create inferred TypeFeaturing               |
| `IMPLICIT_BINDING_CONNECTOR` | -                | Create BindingConnector between features    |

Constraint bodies can be OCL expressions or native Kotlin lambdas:

```kotlin
// Expression-based (serializable)
ConstraintBody.Expression(code = "self.effectiveName()", language = "OCL")

// Native (type-safe Kotlin lambda)
ConstraintBody.Native(impl = { context -> context.element.getProperty("declaredName") })
```

### MetaOperation (`mdm-framework/.../meta/MetaOperation.kt`)

Callable operations on metaclasses, with bodies as OCL, GQL, property references, or native Kotlin:

```kotlin
MetaOperation(
    name = "allRedefinedFeatures",
    returnType = "Feature",
    returnLowerBound = 0, returnUpperBound = -1,
    parameters = listOf(MetaParameter(name = "feature", type = "Feature")),
    body = MetaOperation.ocl("ownedRedefinition.redefinedFeature->closure(...)"),
    isQuery = true
)
```

### Semantic Bindings

Declare implied relationships to base library types with conditional application:

```kotlin
semanticBindings = listOf(
    // Unconditional: all Features subset Base::things
    SemanticBinding(
        name = "featureThingsBinding",
        baseConcept = "Base::things",
        bindingKind = BindingKind.SUBSETS,
        condition = BindingCondition.Default
    ),
    // Conditional: Feature typed by DataType subsets Base::dataValues
    SemanticBinding(
        name = "featureDataValueBinding",
        baseConcept = "Base::dataValues",
        bindingKind = BindingKind.SUBSETS,
        condition = BindingCondition.TypedBy("DataType")
    )
)
```

**BindingKind**: `SPECIALIZES`, `SUBSETS`, `REDEFINES`, `TYPE_FEATURES`

**BindingCondition** (sealed class): `Default`, `TypedBy`, `OwningTypeIs`, `PropertyEquals`, `And`, `Or`, `Not`,
`CollectionNotEmpty`, `CollectionEmpty`, `SizeEquals`, `HasElementOfType`, `IsEnd`, `IsPortion`, `IsComposite`,
`HasOwningFeatureMembership`, `IsEndWithOwningType`

### Ownership Bindings

Declare that a metaclass acts as an ownership intermediate (e.g., Membership, FeatureMembership):

```kotlin
MetaClass(
    name = "FeatureMembership",
    ownershipBinding = OwnershipBinding(
        ownedElementEnd = "ownedMemberFeature",
        ownerEnd = "owningType"
    )
)
```

The `OwnershipResolver` finds the most-specific intermediate for parent/child type pairs by inspecting the inheritance
hierarchy and deriving type constraints from association end declarations.

### MetamodelRegistry (`mdm-framework/.../runtime/MetamodelRegistry.kt`)

Central registry providing:

- Class and association lookup by name
- Inheritance hierarchy queries (`getAllSuperclasses`, `isSubclassOf`)
- Navigable association ends for a class
- Validation (circular inheritance, missing references)

## Constraint System

### ConstraintEngine (`mdm-framework/.../constraints/ConstraintEngine.kt`)

Evaluates constraints on instances:

- **Derived properties**: Computes values using OCL or registered evaluators
- **Validation constraints**: Checks invariants against instances
- **Non-navigable ends**: Computes reverse association traversal
- **Implicit relationships**: Creates implied relationships (redefinition, specialization)

### ConstraintRegistry (`mdm-framework/.../constraints/ConstraintRegistry.kt`)

Registry for constraint evaluators:

- Registers derived property, validation, and association end evaluators
- Supports inheritance-based lookup (checks superclasses)
- Evaluators can be Kotlin lambdas or OCL expressions

## OCL Implementation

### Architecture

```
OCL text -> OclParser -> OclExpression AST -> OclExecutor -> result
            (ANTLR)      (OclAst.kt)         (OclVisitor)
```

### OclParser (`mdm-framework/.../query/ocl/OclParser.kt`)

Parses OCL text to AST using ANTLR 4.13.1:

- Uses `OCLLexer` and `OCLParser` generated from `OCL.g4` grammar
- Visitor pattern builds custom AST nodes
- Full error handling via `OclErrorListener`

### OclAst (`mdm-framework/.../query/ocl/OclAst.kt`)

Complete AST node hierarchy:

- **Literals**: Null, Boolean, Integer, Real, String, UnlimitedNatural, Collection
- **Navigation**: Variable, PropertyCall, NavigationCall, ArrowCall
- **Operations**: OperationCall, InfixExp, PrefixExp
- **Iterators**: select, reject, collect, forAll, exists, any, one, closure, isUnique, sortedBy, iterate
- **Control**: IfExp, LetExp
- **Types**: oclIsKindOf, oclIsTypeOf, oclAsType

### OclExecutor (`mdm-framework/.../query/ocl/OclExecutor.kt`)

Evaluates AST against MDM objects via the `EngineAccessor` interface:

```kotlin
val executor = OclExecutor(accessor, contextObject, contextId)
val result = executor.evaluate(ast)

// Example expressions:
// "self.ownedMembership->size()"
// "self.ownedMembership->select(m | m.visibility = 'public')"
// "self.owner.oclAsType(Namespace).member->includes(self)"
// "self.ownedMembership->closure(m | m.memberElement.ownedMembership)"
```

**Recursion safety**: Uses `LinkedHashSet` for visited tracking and `ArrayDeque` for work queue in operations like
`closure`.

**Type casting**: `OclAsTypeView` wrapper for `oclAsType()` - operations dispatch on the cast type.

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
- **LiteralExpression** -> returns `Sequence{self}`
- **OperatorExpression** -> evaluate arguments, apply operator via KernelFunctionLibrary
- **InvocationExpression** -> evaluate arguments, bind to parameters, apply function body
- **FeatureReferenceExpression** -> resolve referent Feature, return its value
- **FeatureChainExpression** -> evaluate source, navigate target feature on result
- **SelectExpression / CollectExpression / IndexExpression** -> collection operations
- **NullExpression** -> returns empty sequence

**KerMLExpressionOperationHandler** (`KerMLExpressionOperationHandler.kt`)
- Wires the evaluator into MDMEngine by replacing OCL-based operation bodies with `MetaOperation.native{}` handlers
- Installs native `evaluate(target)`, `modelLevelEvaluable(visited)`, and `checkCondition(target)` operations on
  Expression metaclasses

### Layer 2: Behavior Execution Engine (`gearshift-kerml-runtime/.../exec/`)

Token-passing execution semantics for Behaviors composed of Steps connected by Successions, with data transfer via
Flows. Inspired by fUML but dramatically simplified.

**Execution Model** (`ExecutionModel.kt`)
- `Token` (sealed): `ControlToken` (execution flow) and `ObjectToken` (carries data)
- `StepState`: WAITING -> READY -> EXECUTING -> COMPLETED
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
4. Execute step: Expression -> Layer 1 evaluator; Behavior -> recurse with child context
5. Evaluate guard conditions on outgoing Successions
6. Propagate tokens along satisfied Successions, transfer data via Flows
7. Repeat until no READY steps or `maxSteps` exceeded

### Layer 3: Z3/SMT Constraint Solver

Constraint solving for parametric analysis, requirement conflict detection, and trade studies.

**OclToZ3Translator** (`mdm-framework/.../query/ocl/OclToZ3Translator.kt`)
- Implements `OclVisitor<Expr<*>>` - translates OCL AST to Z3 expressions
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
- Infers Z3Sort from Feature types (Integer->INT, Real->REAL, Boolean->BOOL)
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
- `Annotation`, `Comment`, `Documentation`, `TextualRepresentation`
- `AnnotatingElement`, `Dependency`

**Core Package (23 classes)** - Types and features:

- `Type`, `Classifier`, `Feature`, `Structure`
- `Specialization`, `Subclassification`, `Conjugation`, `Disjoining`, `Differencing`, `Intersecting`, `Unioning`
- `FeatureMembership`, `FeatureTyping`, `Featuring`, `TypeFeaturing`
- `Subsetting`, `Redefinition`, `ReferenceSubsetting`, `CrossSubsetting`
- `FeatureChaining`, `FeatureInverting`, `EndFeatureMembership`
- `Multiplicity`, `MultiplicityRange`

**Kernel Package (48+ classes)** - Concrete concepts:

- **Classifiers**: `DataType`, `Class`, `Association`, `AssociationStructure`, `Behavior`, `Function`, `Predicate`,
  `Metaclass`, `Interaction`
- **Features**: `Step`, `Connector`, `BindingConnector`, `Succession`, `ConnectorAsUsage`, `FeatureValue`,
  `MetadataFeature`
- **Expressions**: `Expression`, `BooleanExpression`, `Invariant`, `LiteralExpression`, `LiteralBoolean`,
  `LiteralInteger`, `LiteralRational`, `LiteralString`, `LiteralInfinity`, `NullExpression`,
  `OperatorExpression`, `InvocationExpression`, `FeatureChainExpression`, `FeatureReferenceExpression`,
  `CollectExpression`, `SelectExpression`, `IndexExpression`, `ConstructorExpression`,
  `InstantiationExpression`, `MetadataAccessExpression`
- **Flows**: `Flow`, `FlowEnd`, `PayloadFeature`, `SuccessionFlow`
- **Memberships**: `ParameterMembership`, `ResultExpressionMembership`, `ReturnParameterMembership`,
  `ElementFilterMembership`
- **Containers**: `Package`, `LibraryPackage`

### Association Definitions (37+ files)

Organized by domain across root, core, and kernel packages:

- **Root**: Elements, Dependencies, Annotations, Namespaces, Imports
- **Core**: Types, Specializations, Conjugations, Features, Subsetting, Multiplicities, etc.
- **Kernel**: Associations, Connectors, Behaviors, Functions, Expressions, Flows, Packages, etc.

### KerMLMetamodelLoader

Initializes the complete metamodel in dependency order:

```kotlin
fun initialize(registry: MetamodelRegistry) {
    // 1. Register Root package classes
    registerRootClasses(registry)

    // 2. Register Core package classes
    registerCoreClasses(registry)

    // 3. Register Kernel package classes
    registerKernelClasses(registry)

    // 4. Register all associations
    registerAssociations(registry)
}
```

### Inheritance Hierarchy (key chains)

```
Element (abstract)
├── Relationship (abstract)
│   ├── Specialization -> Subclassification, Conjugation, Disjoining, ...
│   ├── Featuring -> TypeFeaturing
│   └── ... (Membership, Import, etc.)
└── Namespace
    └── Type
        ├── Classifier
        │   ├── DataType
        │   ├── Class -> Behavior
        │   ├── Structure -> Association, AssociationStructure
        │   └── Function -> Predicate
        └── Feature
            ├── Step -> Expression -> BooleanExpression, LiteralExpression, ...
            ├── Connector -> BindingConnector, Succession
            └── Multiplicity -> MultiplicityRange
```

## Parser System

### KerML Parsing (`gearshift-kerml-runtime/.../parser/`)

ANTLR-based parser with visitor pattern:

- **KerMLModel** - High-level API: `parseString()`, `parseFile()`
- **KermlParseContext** - Runtime context with MDMEngine, namespace stack, reference collector
- **KerMLErrorListener** - Custom error listener collecting parse errors with location info
- **ReferenceCollector / ReferenceResolver** - Deferred resolution of forward references
- **80+ visitor files** organized by metaclass domain (base visitors + specialized visitors)
- **Deterministic UUIDs** for library elements via v5 namespace UUIDs from qualified names

### KerML Writer (`gearshift-kerml-runtime/.../generator/`)

Generates KerML textual notation from MDM model objects:

- **KerMLWriter** - Top-level API: `write(namespace)`, `writeToFile(namespace, file)`
- **GeneratorFactory** - Routes MDMObject to type-specific generator
- **50+ generator files** organized by metaclass domain
- Base generators: `KerMLGenerator`, `BaseElementGenerator`, `BaseFeatureGenerator`, `BaseClassifierGenerator`,
  `BaseTypeGenerator`

### Implied Relationships

**KerMLSemanticHandler** processes semantic bindings to create implied relationships:

- Registered as a `LifecycleHandler` on MDMEngine
- After parsing, `processAllPending()` creates implied specializations, subsettings, etc.
- Uses `SemanticBinding` definitions from metaclasses to determine which library types to bind
- `BindingCondition` system evaluates whether each binding applies to a given element

## Code Generation

### Kotlin Code Generation (`MetamodelCodeGenerator`)

Generates typed Kotlin interfaces and implementations from MetaClass definitions:

```kotlin
// Generated interface
interface Element : ModelElement {
    var aliasIds: List<String>
    var declaredName: String?
    val isLibraryElement: Boolean       // derived (readonly)
    val name: String?                   // derived (readonly)
    fun effectiveName(): String?
}

// Generated implementation
class ElementImpl(wrapped: MDMObject, engine: MDMEngine) : Element {
    override var declaredName: String?
        get() = wrapped.getProperty("declaredName") as? String
        set(value) { engine.setProperty(wrapped.id!!, "declaredName", value) }

    override val name: String?
        get() = engine.getProperty(wrapped.id!!, "name") as? String  // triggers derivation
}
```

Output: `kerml-generated/src/main/kotlin/org/openmbee/gearshift/generated/`
- `interfaces/` - Type-safe interfaces (86 files)
- `impl/` - MDMObject-wrapping implementations (85 files)
- `KerMLElementFactory.kt` - Factory for creating typed instances

### TypeScript Code Generation (`TypeScriptCodeGenerator`)

Generates TypeScript interfaces for SysML v2 API compatibility:

```typescript
export interface Element {
    '@id': string;
    '@type': KerMLMetaclass;
    aliasIds: string[];
    declaredName?: string;
    readonly isLibraryElement: boolean;
    readonly name?: string;
    clientDependency: ElementRef[];
    readonly documentation: ElementRef[];
}

export type KerMLMetaclass =
    | 'AnnotatingElement'
    | 'Annotation'
    | 'Association'
    // ... (60+ concrete classes)
```

Output: `build/generated-ts/`
- `kerml.model.ts` - TypeScript interface declarations
- `kerml-metaclass.type.ts` - Discriminated union type of concrete metaclass names

Features:
- Association ends -> `ElementRef` (ID-based references)
- Optional properties use `?` on property name (not `| undefined`)
- Only concrete classes appear in `KerMLMetaclass` union type
- Topological sort ensures `extends` references are valid

### Code Generation Runner

Both generators are invoked via `CodeGeneratorRunner` with mode dispatch:

```bash
# Generate Kotlin code (default)
./gradlew generateMetamodelCode --no-configuration-cache

# Generate TypeScript types
./gradlew generateTypeScriptTypes --no-configuration-cache

# Generate TypeScript to custom directory
./gradlew generateTypeScriptTypes -PtsOutputDir=/custom/path
```

## Storage

### GraphStorage (`mdm-framework/.../storage/GraphStorage.kt`)

Interface abstracting graph-native storage:

- **Node operations**: `getNode()`, `setNode()`, `deleteNode()`, `deleteNodeCascade()`
- **Edge operations**: `getEdge()`, `setEdge()`, `deleteEdge()`, `getOutgoingEdges()`, `getIncomingEdges()`
- **Batch operations**: `getAllNodes()`, `getAllEdges()`, `clear()`

**InMemoryGraphStorage** - Development/testing implementation using internal maps.

## Data Flow

### Instance Creation

```
1. Request createElement("Feature")
   |
2. Validate metaclass exists in registry
   |
3. Create MDMObject linked to MetaClass
   |
4. Store in element map (indexed by ID)
   |
5. Trigger lifecycle handlers (onObjectCreated)
   |
6. Return MDMObject
```

### Property Access with Derivation

```
1. Request getProperty(id, "name")
   |
2. Check if property is derived (via MetaProperty.isDerived)
   |
3a. If not derived: return stored value
3b. If derived: look up derivationConstraint
   |
4. Determine expression language, find evaluator
   |
5. Parse expression (e.g., OCL via OclParser)
   |
6. Execute via evaluator (e.g., OclExecutor)
   |
7. Return computed value
```

### Name Resolution (KerML 8.2.3.5)

```
1. Parse qualified name (A::B::C)
   |
2. Check for cycles (resolution stack)
   |
3. Execute resolution algorithm:
   - Single segment: full resolution in namespace
   - Multi-segment: resolve qualification, then last segment
   - Global scope ($::): resolve from model root
   |
4. Return Membership containing memberElement
```

## Technology Stack

| Dependency          | Version | Purpose                                        |
|---------------------|---------|------------------------------------------------|
| Kotlin              | 2.1.10  | Language                                        |
| JDK                 | 21      | Runtime                                         |
| Gradle              | 9.2.1   | Build tool                                      |
| Jackson             | 2.16.1  | JSON serialization                              |
| ANTLR               | 4.13.1  | Grammar parsing (OCL, GQL, KerML)              |
| Z3 (z3-turnkey)     | 4.13.0  | SMT solver for constraint solving               |
| Ktor                | 2.3.7   | Web server (runtime REST API)                   |
| kotlin-logging      | 6.0.3   | Logging facade                                  |
| Logback             | 1.4.14  | Logging implementation                          |
| Kotest              | 5.8.0   | Testing framework                               |
| JUnit Jupiter       | 5.10.1  | Test runner                                     |
| MockK               | 1.13.9  | Mocking library                                 |

## Build Commands

```bash
# Compile all modules
./gradlew compileKotlin --no-configuration-cache

# Run all tests
./gradlew test --no-configuration-cache

# Generate ANTLR grammar sources
./gradlew generateGrammarSource --no-configuration-cache

# Generate Kotlin code from metamodel
./gradlew generateMetamodelCode --no-configuration-cache

# Generate TypeScript types
./gradlew generateTypeScriptTypes --no-configuration-cache
```

## Related Documentation

- **[STORAGE_ARCHITECTURE.md](STORAGE_ARCHITECTURE.md)** - Graph storage design
- **[KERML_GENERATOR_ARCHITECTURE.md](KERML_GENERATOR_ARCHITECTURE.md)** - KerML code generation architecture
- **[NAME_RESOLUTION.md](NAME_RESOLUTION.md)** - KerML 8.2.3.5 name resolution
- **[OPERATION_INVOCATION.md](OPERATION_INVOCATION.md)** - Operation handling
- **[OPERATOR_EXPRESSION_SPEC.md](OPERATOR_EXPRESSION_SPEC.md)** - Operator semantics
- **[SEMANTICS_VALIDATION_PLAN.md](SEMANTICS_VALIDATION_PLAN.md)** - Validation strategy
- **[API_IMPLEMENTATION_PLAN.md](API_IMPLEMENTATION_PLAN.md)** - REST API roadmap
- **[GETTING_STARTED.md](GETTING_STARTED.md)** - Tutorial guide
- **[TESTING.md](TESTING.md)** - Test strategy
- **[../CLAUDE.md](../CLAUDE.md)** - Development guidelines and conventions

## KerML Compliance

Implements KerML v1.0 specification:

- 85+ metaclasses across Root, Core, and Kernel packages
- 127+ associations with full subsetting/redefinition semantics
- OCL constraint evaluation for derivations, validations, and non-navigable ends
- Semantic bindings for implied relationships to library types
- KerML textual notation parsing and generation
- Name resolution (KerML 8.2.3.5)
- Membership and visibility (KerML 8.2.3.4)
- Expression evaluation (native tree walk + kernel function library)
- Behavior execution (token-passing with guard conditions)
- Parametric analysis (Z3/SMT constraint solving)
