# KerML Parser Visitors

This package contains visitor-based parsers for converting KerML grammar elements (from the ANTLR-generated parser) into Gearshift engine instances.

## Architecture

The parser follows the **Visitor Pattern** to traverse ANTLR parse trees and instantiate KerML metamodel elements in the Gearshift engine.

### Components

1. **KerMLElementVisitor.kt** - Base visitor interface and abstract implementations
2. **NonFeatureElementVisitors.kt** - Visitors for NonFeatureElement types
3. **FeatureElementVisitors.kt** - Visitors for FeatureElement types
4. **AnnotatingElementVisitors.kt** - Visitors for AnnotatingElement types
5. **KerMLVisitorFactory.kt** - Factory for creating and accessing visitors
6. **KerMLParseCoordinator.kt** - Orchestrates parsing and reference resolution

## Element Hierarchy (from KerML Spec 8.2.3.4.3)

```
MemberElement : Element =
    AnnotatingElement | NonFeatureElement

NonFeatureElement : Element =
    Dependency | Namespace | Type | Classifier | DataType | Class | Structure |
    Metaclass | Association | AssociationStructure | Interaction | Behavior |
    Function | Predicate | Multiplicity | Package | LibraryPackage |
    Specialization | Conjugation | Subclassification | Disjoining |
    FeatureInverting | FeatureTyping | Subsetting | Redefinition | TypeFeaturing

FeatureElement : Feature =
    Feature | Step | Expression | BooleanExpression | Invariant |
    Connector | BindingConnector | Succession | Flow | SuccessionFlow

AnnotatingElement =
    Comment | Documentation | TextualRepresentation | MetadataFeature
```

## Usage Pattern

### Step 1: Generate ANTLR Parser

First, generate the ANTLR parser from KerML.g4:

```bash
antlr4 -Dlanguage=Kotlin -package org.openmbee.gearshift.kerml.antlr KerML.g4
```

### Step 2: Implement Visitor Methods

Once the ANTLR parser is generated, update each visitor's `visit()` method to accept the specific ANTLR context type. For example:

```kotlin
class NamespaceVisitor : BaseKerMLVisitor<KerMLParser.NamespaceContext>() {
    override fun visit(ctx: KerMLParser.NamespaceContext, engine: GearshiftEngine): Any? {
        val (instanceId, instance) = createInstance(engine, "Namespace")

        // Parse identification from ctx.namespaceDeclaration().identification()
        ctx.namespaceDeclaration().identification()?.let { id ->
            id.declaredName?.text?.let { name ->
                setProperty(engine, instanceId, "name", name)
            }
            id.declaredShortName?.text?.let { shortName ->
                setProperty(engine, instanceId, "shortName", shortName)
            }
        }

        // Parse namespace body elements
        ctx.namespaceBody()?.namespaceBodyElement()?.forEach { bodyElement ->
            // Delegate to appropriate visitor based on element type
            when {
                bodyElement.namespaceMember() != null -> {
                    // Parse namespace member
                }
                bodyElement.aliasMember() != null -> {
                    // Parse alias member
                }
                bodyElement.import_() != null -> {
                    // Parse import
                }
            }
        }

        return instanceId
    }
}
```

### Step 3: Use the Parse Coordinator

```kotlin
val engine = GearshiftEngine()
KerMLMetamodel.initialize(engine)

val coordinator = KerMLParseCoordinator(engine)

// Parse a KerML file
val input = CharStreams.fromPath(Paths.get("model.kerml"))
val lexer = KerMLLexer(input)
val tokens = CommonTokenStream(lexer)
val parser = KerMLParser(tokens)

val rootContext = parser.rootNamespace()
val rootInstanceId = coordinator.parseRootNamespace(rootContext)

// Resolve all cross-references
val unresolvedRefs = coordinator.resolveReferences()
if (unresolvedRefs.isNotEmpty()) {
    println("Warning: ${unresolvedRefs.size} unresolved references")
    unresolvedRefs.forEach { ref ->
        println("  Cannot resolve: ${ref.targetQualifiedName}")
    }
}

// Get statistics
val stats = coordinator.getParseStatistics()
println("Parsed ${stats.totalElements} elements")
println("Element distribution: ${stats.elementsByType}")
```

## Implementation Checklist

For each visitor stub marked with `TODO`, implement:

1. **Extract context data** - Get data from ANTLR parse tree nodes
2. **Create instance** - Call `createInstance(engine, "ElementType")`
3. **Set properties** - Call `setProperty(engine, instanceId, propertyName, value)`
4. **Handle relationships** - Create relationship instances or record unresolved references
5. **Recurse to children** - Visit child elements using appropriate visitors
6. **Return instance ID** - Return the created element's ID

## Example: Complete Implementation

Here's a complete example for the `DependencyVisitor`:

```kotlin
class DependencyVisitor : BaseKerMLVisitor<KerMLParser.DependencyContext>() {
    override fun visit(ctx: KerMLParser.DependencyContext, engine: GearshiftEngine): Any? {
        val (instanceId, instance) = createInstance(engine, "Dependency")

        // Parse identification
        ctx.identification()?.let { id ->
            id.declaredName?.text?.let { name ->
                setProperty(engine, instanceId, "name", name)
            }
            id.declaredShortName?.text?.let { shortName ->
                setProperty(engine, instanceId, "shortName", shortName)
            }
        }

        // Parse clients (list of qualified names)
        val clients = ctx.client.map { it.text }
        setProperty(engine, instanceId, "clients", clients)

        // Parse suppliers (list of qualified names)
        val suppliers = ctx.supplier.map { it.text }
        setProperty(engine, instanceId, "suppliers", suppliers)

        // Parse prefix metadata annotations
        ctx.prefixMetadataAnnotation()?.forEach { annotation ->
            val annotationVisitor = KerMLVisitorFactory.AnnotatingElements.prefixMetadataAnnotation
            annotationVisitor.visit(annotation, engine)
        }

        // Parse relationship body (if it contains elements)
        ctx.relationshipBody()?.relationshipOwnedElement()?.forEach { element ->
            // Parse owned elements
        }

        return instanceId
    }
}
```

## Reference Resolution

References to other elements (via QualifiedName) are not immediately resolvable during parsing. Use the coordinator to:

1. **Record unresolved references** during parsing:
   ```kotlin
   coordinator.recordUnresolvedReference(
       sourceInstanceId = instanceId,
       propertyName = "specializes",
       targetQualifiedName = ctx.qualifiedName().text
   )
   ```

2. **Resolve after parsing** is complete:
   ```kotlin
   val unresolved = coordinator.resolveReferences()
   ```

## Next Steps

1. Generate ANTLR parser from KerML.g4
2. Update visitor type parameters to use specific ANTLR context types
3. Implement each visitor's `visit()` method following the patterns above
4. Test with example KerML files
5. Add error handling and validation
