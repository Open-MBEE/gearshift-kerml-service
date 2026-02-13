# KerML Generator Architecture

This document describes the architecture for implementing a spec-compliant KerML generator (reverse-parser) that converts MDM model objects back to KerML textual syntax.

## Overview

The generator architecture mirrors the existing parser visitor hierarchy, providing a clean inverse transformation:

```
Parsing:   KerML Text → ANTLR Lexer/Parser → Parse Tree → Visitors → MDM Model Objects
Generation: MDM Model Objects → Generators → KerML Text
```

## 1. Core Interface Hierarchy

The generator interface hierarchy mirrors the visitor hierarchy for maintainability:

```kotlin
// Base interface - mirrors TypedKerMLVisitor
interface KerMLGenerator<T : ModelElement> {
    fun generate(element: T, context: GenerationContext): String
}

// Base abstract classes - mirror the visitor hierarchy
abstract class BaseElementGenerator<T : Element> : KerMLGenerator<T> {
    protected fun generateIdentification(element: Element): String
    protected fun generateVisibility(membership: Membership?): String
}

abstract class BaseTypeGenerator<T : Type> : BaseElementGenerator<T>() {
    protected fun generateTypePrefix(type: Type): String       // abstract, all, etc.
    protected fun generateTypeBody(type: Type, ctx: GenerationContext): String
    protected fun generateTypeRelationshipPart(type: Type, ctx: GenerationContext): String  // disjoint, union, intersect
}

abstract class BaseClassifierGenerator<T : Classifier> : BaseTypeGenerator<T>() {
    protected fun generateClassifierDeclaration(classifier: Classifier, ctx: GenerationContext): String
    protected fun generateSubclassifications(classifier: Classifier, ctx: GenerationContext): String
}

abstract class BaseFeatureGenerator<T : Feature> : BaseTypeGenerator<T>() {
    protected fun generateFeaturePrefix(feature: Feature): String  // direction, derived, composite, etc.
    protected fun generateFeatureDeclaration(feature: Feature, ctx: GenerationContext): String
    protected fun generateTypings(feature: Feature, ctx: GenerationContext): String
    protected fun generateSubsettings(feature: Feature, ctx: GenerationContext): String
    protected fun generateRedefinitions(feature: Feature, ctx: GenerationContext): String
    protected fun generateMultiplicity(feature: Feature): String
    protected fun generateValuePart(feature: Feature, ctx: GenerationContext): String
}

abstract class BaseExpressionGenerator<T : Expression> : BaseFeatureGenerator<T>() {
    protected fun generateOperatorExpression(expr: OperatorExpression, ctx: GenerationContext): String
    protected fun generateLiteralExpression(expr: LiteralExpression): String
}
```

## 2. Generation Context

Mirrors `KermlParseContext` but for output generation:

```kotlin
data class GenerationContext(
    val indentLevel: Int = 0,
    val indentString: String = "    ",  // 4 spaces default
    val currentNamespace: Namespace? = null,
    val importedNamespaces: Set<String> = emptySet(),
    val options: GenerationOptions = GenerationOptions()
) {
    fun indent(): GenerationContext = copy(indentLevel = indentLevel + 1)
    fun withNamespace(ns: Namespace): GenerationContext = copy(currentNamespace = ns)

    fun currentIndent(): String = indentString.repeat(indentLevel)

    // Determine shortest valid name given imports
    fun resolveDisplayName(element: Element): String {
        val qualifiedName = element.qualifiedName ?: element.declaredName ?: return ""
        // Check if can use short name based on imports and current namespace
        return shortestValidName(qualifiedName, currentNamespace, importedNamespaces)
    }
}

data class GenerationOptions(
    val preferSymbolicSyntax: Boolean = true,    // :> vs specializes
    val emitImpliedRelationships: Boolean = false, // Skip semantically-implied relationships
    val maxLineLength: Int = 120,
    val blankLinesBetweenMembers: Boolean = true
)
```

## 3. File Structure

Mirrors the visitor package structure:

```
generator/
├── base/
│   ├── KerMLGenerator.kt
│   ├── BaseElementGenerator.kt
│   ├── BaseTypeGenerator.kt
│   ├── BaseClassifierGenerator.kt
│   ├── BaseFeatureGenerator.kt
│   └── BaseExpressionGenerator.kt
├── ClassGenerator.kt
├── DataTypeGenerator.kt
├── StructureGenerator.kt
├── AssociationGenerator.kt
├── AssociationStructureGenerator.kt
├── FeatureGenerator.kt
├── StepGenerator.kt
├── ConnectorGenerator.kt
├── BindingConnectorGenerator.kt
├── SuccessionGenerator.kt
├── FlowGenerator.kt
├── SuccessionFlowGenerator.kt
├── BehaviorGenerator.kt
├── FunctionGenerator.kt
├── PredicateGenerator.kt
├── InteractionGenerator.kt
├── PackageGenerator.kt
├── LibraryPackageGenerator.kt
├── NamespaceGenerator.kt
├── ImportGenerator.kt
├── CommentGenerator.kt
├── DocumentationGenerator.kt
├── TextualRepresentationGenerator.kt
├── MetadataFeatureGenerator.kt
├── ExpressionGenerator.kt
├── BooleanExpressionGenerator.kt
├── InvariantGenerator.kt
├── LiteralGenerators.kt
├── MultiplicityGenerator.kt
└── GeneratorFactory.kt  // Dispatches by element type
```

## 4. Concrete Generator Examples

### 4.1 ClassGenerator

```kotlin
class ClassGenerator : BaseClassifierGenerator<Class>() {

    override fun generate(cls: Class, ctx: GenerationContext): String = buildString {
        val indent = ctx.currentIndent()

        // typePrefix: abstract?
        append(indent)
        append(generateTypePrefix(cls))

        // CLASS keyword
        append("class ")

        // classifierDeclaration: identification + specialization
        append(generateClassifierDeclaration(cls, ctx))

        // typeBody
        append(generateTypeBody(cls, ctx))
    }

    override fun generateTypePrefix(type: Type): String = buildString {
        if (type.isAbstract) append("abstract ")
    }

    override fun generateClassifierDeclaration(classifier: Classifier, ctx: GenerationContext): String = buildString {
        // Identification: <shortName> declaredName
        append(generateIdentification(classifier))

        // Subclassifications: :> SuperClass1, SuperClass2
        val subclassifications = generateSubclassifications(classifier, ctx)
        if (subclassifications.isNotEmpty()) {
            append(" ")
            append(subclassifications)
        }
    }

    override fun generateSubclassifications(classifier: Classifier, ctx: GenerationContext): String {
        // Get explicit subclassifications (not implied ones)
        val explicitSubclassifications = classifier.ownedSubclassification
            .filter { !isImpliedRelationship(it) }

        if (explicitSubclassifications.isEmpty()) return ""

        val symbol = if (ctx.options.preferSymbolicSyntax) ":>" else "specializes"
        val superclassNames = explicitSubclassifications
            .mapNotNull { it.superclassifier?.let { sc -> ctx.resolveDisplayName(sc) } }

        return "$symbol ${superclassNames.joinToString(", ")}"
    }

    override fun generateTypeBody(type: Type, ctx: GenerationContext): String {
        val members = getExplicitMembers(type)  // Filter out implied members

        if (members.isEmpty()) return ";"

        return buildString {
            appendLine(" {")
            val bodyCtx = ctx.indent()

            // Generate each member
            members.forEach { membership ->
                membership.ownedMemberElement?.let { element ->
                    append(generateVisibility(membership))
                    append(GeneratorFactory.generate(element, bodyCtx))
                    appendLine()
                }
            }

            append(ctx.currentIndent())
            append("}")
        }
    }
}
```

### 4.2 FeatureGenerator

Features have the most complex syntax:

```kotlin
class FeatureGenerator : BaseFeatureGenerator<Feature>() {

    override fun generate(feature: Feature, ctx: GenerationContext): String = buildString {
        append(ctx.currentIndent())

        // featurePrefix: direction derived abstract composite/portion var/const
        append(generateFeaturePrefix(feature))

        // FEATURE keyword (optional in many contexts)
        if (needsFeatureKeyword(feature)) {
            append("feature ")
        }

        // featureDeclaration
        append(generateFeatureDeclaration(feature, ctx))

        // valuePart: = expr or := expr or default expr
        feature.valuation?.let {
            append(generateValuePart(feature, ctx))
        }

        // typeBody
        append(generateTypeBody(feature, ctx))
    }

    override fun generateFeaturePrefix(feature: Feature): String = buildString {
        // Direction: in, out, inout
        feature.direction?.let { append("$it ") }

        // Modifiers in spec order
        if (feature.isDerived) append("derived ")
        if (feature.isAbstract) append("abstract ")
        if (feature.isComposite) append("composite ")
        else if (feature.isPortion) append("portion ")

        // readonly is implicit (no var/const keyword means readonly)
        if (feature.isConstant) append("const ")
        else if (feature.isVariable) append("var ")

        if (feature.isEnd) append("end ")
    }

    override fun generateFeatureDeclaration(feature: Feature, ctx: GenerationContext): String = buildString {
        if (feature.isSufficient) append("all ")

        // Identification
        append(generateIdentification(feature))

        // Multiplicity: [n..m] ordered nonunique
        append(generateMultiplicity(feature))

        // Typings: : Type1, Type2
        val typings = generateTypings(feature, ctx)
        if (typings.isNotEmpty()) append(typings)

        // Subsettings: :> feature1, feature2
        val subsettings = generateSubsettings(feature, ctx)
        if (subsettings.isNotEmpty()) append(" $subsettings")

        // Redefinitions: :>> feature1
        val redefinitions = generateRedefinitions(feature, ctx)
        if (redefinitions.isNotEmpty()) append(" $redefinitions")
    }

    override fun generateMultiplicity(feature: Feature): String {
        val mult = feature.multiplicity ?: return ""

        return buildString {
            append("[")
            // Handle bounds
            val lower = mult.lower ?: 0
            val upper = mult.upper  // null means *

            if (lower == upper && upper != null) {
                append(upper)
            } else {
                append(lower)
                append("..")
                append(upper?.toString() ?: "*")
            }
            append("]")

            if (feature.isOrdered) append(" ordered")
            if (feature.isNonunique) append(" nonunique")
        }
    }

    override fun generateTypings(feature: Feature, ctx: GenerationContext): String {
        val explicitTypings = feature.ownedTyping.filter { !isImpliedRelationship(it) }
        if (explicitTypings.isEmpty()) return ""

        val symbol = if (ctx.options.preferSymbolicSyntax) ":" else "typed by"
        val typeNames = explicitTypings.mapNotNull {
            it.type?.let { t -> ctx.resolveDisplayName(t) }
        }

        return " $symbol ${typeNames.joinToString(", ")}"
    }

    override fun generateSubsettings(feature: Feature, ctx: GenerationContext): String {
        val explicitSubsettings = feature.ownedSubsetting
            .filterNot { it is Redefinition }
            .filter { !isImpliedRelationship(it) }

        if (explicitSubsettings.isEmpty()) return ""

        val symbol = if (ctx.options.preferSymbolicSyntax) ":>" else "subsets"
        val featureNames = explicitSubsettings.mapNotNull {
            it.subsettedFeature?.let { f -> ctx.resolveDisplayName(f) }
        }

        return "$symbol ${featureNames.joinToString(", ")}"
    }

    override fun generateRedefinitions(feature: Feature, ctx: GenerationContext): String {
        val explicitRedefinitions = feature.ownedRedefinition.filter { !isImpliedRelationship(it) }
        if (explicitRedefinitions.isEmpty()) return ""

        val symbol = if (ctx.options.preferSymbolicSyntax) ":>>" else "redefines"
        val featureNames = explicitRedefinitions.mapNotNull {
            it.redefinedFeature?.let { f -> ctx.resolveDisplayName(f) }
        }

        return "$symbol ${featureNames.joinToString(", ")}"
    }
}
```

## 5. Handling Implied vs Explicit Relationships

The `KerMLSemanticHandler` creates implied relationships during parsing. The generator must distinguish these to avoid emitting redundant syntax:

```kotlin
// In BaseTypeGenerator
protected fun isImpliedRelationship(rel: Relationship): Boolean {
    // Option 1: Check metadata flag set during semantic processing
    return rel.metadata?.get("implied") == true

    // Option 2: Check for known implied patterns
    // e.g., Subsetting from any Feature to Base::Anything is always implied
}

protected fun getExplicitMembers(type: Type): List<Membership> {
    return type.ownedMembership.filter { membership ->
        membership.ownedMemberElement?.let { element ->
            when (element) {
                is Relationship -> !isImpliedRelationship(element)
                else -> true
            }
        } ?: false
    }
}
```

### Recommendation: Mark Implied Relationships at Parse Time

Modify `KerMLSemanticHandler` to set metadata on relationships it creates:

```kotlin
// In KerMLSemanticHandler
private fun createImpliedSubsetting(feature: Feature, subsettedFeature: Feature) {
    val subsetting = engine.create<Subsetting>()
    subsetting.subsettingFeature = feature
    subsetting.subsettedFeature = subsettedFeature
    subsetting.setMetadata("implied", true)  // Mark as implied
}
```

## 6. Syntax Choice Decisions

The KerML grammar supports multiple syntactic forms for the same semantics:

| Semantic | Symbolic Form | Keyword Form |
|----------|---------------|--------------|
| Typing | `:` | `typed by` |
| Specialization | `:>` | `specializes` |
| Subsetting | `:>` | `subsets` |
| Redefinition | `:>>` | `redefines` |
| Reference | `::>` | `references` |
| Conjugation | `~` | `conjugates` |

The `GenerationOptions.preferSymbolicSyntax` flag controls which form to emit. Default is symbolic as it's more concise and commonly used.

## 7. Generator Factory

Dispatches to the appropriate generator based on element type:

```kotlin
object GeneratorFactory {
    private val generators = mapOf<KClass<*>, KerMLGenerator<*>>(
        Class::class to ClassGenerator(),
        DataType::class to DataTypeGenerator(),
        Structure::class to StructureGenerator(),
        Association::class to AssociationGenerator(),
        AssociationStructure::class to AssociationStructureGenerator(),
        Feature::class to FeatureGenerator(),
        Step::class to StepGenerator(),
        Connector::class to ConnectorGenerator(),
        BindingConnector::class to BindingConnectorGenerator(),
        Succession::class to SuccessionGenerator(),
        Flow::class to FlowGenerator(),
        SuccessionFlow::class to SuccessionFlowGenerator(),
        Behavior::class to BehaviorGenerator(),
        Function::class to FunctionGenerator(),
        Predicate::class to PredicateGenerator(),
        Interaction::class to InteractionGenerator(),
        Package::class to PackageGenerator(),
        LibraryPackage::class to LibraryPackageGenerator(),
        Namespace::class to NamespaceGenerator(),
        Import::class to ImportGenerator(),
        Comment::class to CommentGenerator(),
        Documentation::class to DocumentationGenerator(),
        TextualRepresentation::class to TextualRepresentationGenerator(),
        MetadataFeature::class to MetadataFeatureGenerator(),
        Expression::class to ExpressionGenerator(),
        BooleanExpression::class to BooleanExpressionGenerator(),
        Invariant::class to InvariantGenerator(),
        // Literals
        LiteralInteger::class to LiteralIntegerGenerator(),
        LiteralReal::class to LiteralRealGenerator(),
        LiteralBoolean::class to LiteralBooleanGenerator(),
        LiteralString::class to LiteralStringGenerator(),
        LiteralInfinity::class to LiteralInfinityGenerator(),
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : ModelElement> generate(element: T, ctx: GenerationContext): String {
        // Find most specific generator
        val generator = findGenerator(element::class) as? KerMLGenerator<T>
            ?: throw IllegalArgumentException("No generator for ${element::class.simpleName}")
        return generator.generate(element, ctx)
    }

    private fun findGenerator(klass: KClass<*>): KerMLGenerator<*>? {
        // Check exact match first
        generators[klass]?.let { return it }

        // Walk up inheritance hierarchy
        klass.supertypes.forEach { supertype ->
            val superKlass = supertype.classifier as? KClass<*> ?: return@forEach
            findGenerator(superKlass)?.let { return it }
        }

        return null
    }
}
```

## 8. Entry Point: KerMLWriter

```kotlin
class KerMLWriter {

    fun write(rootNamespace: Namespace, options: GenerationOptions = GenerationOptions()): String {
        val ctx = GenerationContext(options = options)

        return buildString {
            // Collect imports for context
            val imports = collectImports(rootNamespace)
            val ctxWithImports = ctx.copy(importedNamespaces = imports)

            // Generate imports first
            rootNamespace.ownedImport.forEach { import ->
                appendLine(GeneratorFactory.generate(import, ctxWithImports))
            }

            if (rootNamespace.ownedImport.isNotEmpty()) appendLine()

            // Generate members in order: comments, non-features, features
            val sortedMembers = sortMembersForOutput(rootNamespace.ownedMember)

            sortedMembers.forEach { member ->
                member.ownedMemberElement?.let { element ->
                    append(GeneratorFactory.generate(element, ctxWithImports))
                    if (options.blankLinesBetweenMembers) appendLine()
                    appendLine()
                }
            }
        }.trimEnd() + "\n"
    }

    private fun collectImports(namespace: Namespace): Set<String> {
        return namespace.ownedImport
            .mapNotNull { it.importedNamespace?.qualifiedName }
            .toSet()
    }

    private fun sortMembersForOutput(members: List<Membership>): List<Membership> {
        // Order: Comments/Documentation, Imports (already handled), Non-features, Features
        return members.sortedWith(compareBy { membership ->
            when (val element = membership.ownedMemberElement) {
                is Comment, is Documentation -> 0
                is TextualRepresentation -> 1
                is MetadataFeature -> 2
                is Feature -> 4
                else -> 3  // Non-feature elements
            }
        })
    }
}
```

## 9. Testing Strategy

### 9.1 Round-Trip Testing

The gold standard for generator correctness:

```kotlin
@Test
fun `round-trip preserves semantics`() {
    val originalKerml = """
        package Vehicles {
            abstract class Vehicle {
                feature mass : Real;
            }
            class Car :> Vehicle {
                feature numWheels : Integer = 4;
            }
        }
    """.trimIndent()

    // Parse -> Generate -> Parse again
    val model1 = KerMLModel().parseString(originalKerml)
    val generatedKerml = KerMLWriter().write(model1.rootNamespace!!)
    val model2 = KerMLModel().parseString(generatedKerml)

    // Compare semantic content (not string equality)
    assertSemanticEquivalence(model1, model2)
}
```

### 9.2 Semantic Equivalence Checker

```kotlin
fun assertSemanticEquivalence(model1: KerMLPackage, model2: KerMLPackage) {
    // Compare element counts by type
    assertEquals(countElements<Class>(model1), countElements<Class>(model2))
    assertEquals(countElements<Feature>(model1), countElements<Feature>(model2))

    // Compare structural properties
    val classes1 = findElements<Class>(model1).associateBy { it.qualifiedName }
    val classes2 = findElements<Class>(model2).associateBy { it.qualifiedName }

    assertEquals(classes1.keys, classes2.keys, "Class names should match")

    classes1.forEach { (name, cls1) ->
        val cls2 = classes2[name]!!
        assertEquals(cls1.isAbstract, cls2.isAbstract, "isAbstract for $name")
        assertEquals(
            cls1.superclassifier.map { it.qualifiedName }.toSet(),
            cls2.superclassifier.map { it.qualifiedName }.toSet(),
            "superclassifiers for $name"
        )
        // ... more property comparisons
    }
}
```

### 9.3 Test Categories

| Category | Description | Example |
|----------|-------------|---------|
| Element-specific | Test each element type in isolation | `ClassGeneratorTest`, `FeatureGeneratorTest` |
| Syntax variants | Test symbolic vs keyword forms | `:>` vs `specializes` |
| Edge cases | Empty bodies, no name, complex multiplicities | `feature [0..*] ordered nonunique` |
| Round-trip | Parse-generate-parse equivalence | Full KerML files from spec examples |
| Implied filtering | Verify implied relationships not emitted | Subsetting to `Base::Anything` |

### 9.4 Ultimate Validation: Standard Library Round-Trip

The definitive test of spec compliance is round-tripping the official KerML standard libraries:

```kotlin
@Test
fun `round-trip KerML standard libraries`() {
    val libraryFiles = listOf(
        "Base.kerml",
        "Kernel.kerml",
        "Performances.kerml",
        "ControlPerformances.kerml",
        "TransitionPerformances.kerml",
        "Occurrences.kerml",
        "Objects.kerml",
        "Items.kerml",
        "Links.kerml",
        "Transfers.kerml",
        "ScalarValues.kerml",
        "BaseFunctions.kerml",
        "DataFunctions.kerml",
        "ScalarFunctions.kerml",
        "ControlFunctions.kerml",
        "CollectionFunctions.kerml",
        "SequenceFunctions.kerml"
    )

    libraryFiles.forEach { filename ->
        val originalKerml = loadLibraryFile(filename)
        val model1 = KerMLModel().parseString(originalKerml)

        val generatedKerml = KerMLWriter().write(model1.rootNamespace!!)
        val model2 = KerMLModel().parseString(generatedKerml)

        assertSemanticEquivalence(model1, model2, "Library: $filename")

        // Optionally verify generated text parses without errors
        assertDoesNotThrow { KerMLModel().parseString(generatedKerml) }
    }
}
```

This is the gold standard because:

1. **Complete grammar coverage** - Libraries use virtually every KerML construct
2. **Real-world complexity** - Deep nesting, feature chains, all relationship types
3. **Spec authority** - These are the normative reference implementations
4. **Implicit relationship testing** - Libraries rely heavily on semantic inference

A generator that can faithfully round-trip all standard libraries is provably spec-compliant.

## 10. Implementation Estimate

| Component | File Count | Complexity |
|-----------|------------|------------|
| Base generators | 5 | Medium |
| Concrete generators | ~40 | Varies |
| GenerationContext | 1 | Low |
| GeneratorFactory | 1 | Low |
| KerMLWriter | 1 | Medium |
| Tests | ~50+ | High |
| **Total** | ~100 files | |

## 11. Future Considerations

### 11.1 Preserving Original Formatting

To support minimal-diff round-trips, consider:
- Storing original token positions during parsing
- Tracking whitespace/comment placement
- Providing a "preserve formatting" mode

### 11.2 Incremental Generation

For IDE integration:
- Generate only changed elements
- Support partial document updates
- Integrate with LSP text document sync

### 11.3 Pretty-Print Modes

Different output formats for different use cases:
- **Compact**: Minimal whitespace, single-line where possible
- **Readable**: Standard indentation, one member per line
- **Verbose**: Keyword syntax, explicit all properties
