# Claude Code Guidelines for Gearshift KerML Service

Constraints, Properties, and Operations should always be added in alphabetical order

## Properties vs Association Ends

**All properties typed by a non-primitive are association ends.** Only primitive-typed properties (String, Boolean,
Integer, etc.) belong in the `attributes` list. Properties referencing other metaclasses (Element, Membership,
Namespace, etc.) are association ends and should be defined in the associations files, not as attributes.

- Primitives → `attributes` list in MetaClass
- Non-primitives → `MetaAssociationEnd` in associations file

## Constraint Types

Constraints have a `type` field using `ConstraintType`:

- `DERIVATION` (default) - For computing derived property values. Name prefix: `derive`
- `VERIFICATION` - For validating invariants. Name prefix: `check` or `verify`
- `NON_NAVIGABLE_END` - For calculating non-navigable association ends. Name prefix: `compute`

## Derived Property Constraints

When defining derived properties with `derivationConstraint`:

- The `derivationConstraint` field should reference a **named constraint**, not contain OCL directly
- Add the actual OCL expression in the `constraints` list using `MetaConstraint`
- Constraint names follow the pattern: `derive<ClassName><PropertyName>` (e.g., `deriveElementName`)

Example:

```kotlin
// In attributes list - reference the constraint by name
MetaProperty(
    name = "name",
    isDerived = true,
    derivationConstraint = "deriveElementName",  // Reference to named constraint
    ...
)

// In constraints list - define the actual OCL expression
MetaConstraint(
    name = "deriveElementName",
    type = ConstraintType.DERIVATION,
    expression = "effectiveName()",  // The OCL expression
    description = "Derivation for Element::name"
)
```

## OCL Implementation Guidelines

When adding new constraints, derived properties, or operations that use OCL expressions:

1. **Check for missing OCL operations** - Review the OCL expression for any operations not yet implemented in
   `OclExecutor.kt`

2. **Implement missing operations** - Add support in the appropriate section:
    - Iterator operations: `visitIterator()` method
    - Collection operations: `evaluateCollectionOperation()` method
    - Object/String/Number operations: `evaluateOperation()` method
    - Type operations: `visitTypeOp()` method

3. **Prevent infinite recursion in iterators** - For recursive operations like `closure`:
    - Use a visited set to track already-processed elements
    - Use a work queue pattern instead of direct recursion
    - Example: `computeClosure()` uses `LinkedHashSet` for results and `ArrayDeque` for work queue

4. **Add tests** - Create tests in the appropriate test file to verify:
    - The OCL expression parses correctly
    - The operation evaluates to the expected result
    - Edge cases are handled (null values, empty collections, etc.)

## OCL Parser Architecture

```
OCL text → OclParser → OclExpression AST → OclExecutor → result
            (ANTLR)      (OclAst.kt)        (OclVisitor)
```

- `OclParser.kt` - Parses OCL text to AST using ANTLR
- `OclAst.kt` - AST node definitions
- `OclVisitor.kt` - Visitor interface for AST traversal
- `OclExecutor.kt` - Evaluates AST against MDM objects

## Build Commands

```bash
# Compile
./gradlew compileKotlin --no-configuration-cache

# Run tests
./gradlew test --no-configuration-cache

# Generate ANTLR grammar
./gradlew generateGrammarSource --no-configuration-cache
```

## Typed Interfaces over String-Based Access

**IMPORTANT: Always use the generated typed interfaces (`kerml-generated/`) instead of string-based `engine.getPropertyValue(obj, "propName")` or `engine.isInstanceOf(obj, "TypeName")`.** The generated `*Impl` classes extend `MDMObject` and implement typed interfaces, so:

- **Type checks:** Use `obj is Succession` instead of `engine.isInstanceOf(obj, "Succession")`
- **Property access:** Use `(obj as Feature).declaredName` instead of `engine.getPropertyValue(obj, "declaredName") as? String`
- **Collection filtering:** Use `.filterIsInstance<Succession>()` instead of `.filter { engine.isInstanceOf(it, "Succession") }`
- **Caution with non-null properties on intermediate model objects:** Some generated properties (e.g., `Membership.memberElement`, `EndFeatureMembership.ownedMemberFeature`) are typed non-null but may be unset at runtime, causing NPE. Use `engine.getPropertyValue()` with safe casts for these cases.

## Logging Guidelines

**Never use `println`, `print`, `System.out`, `System.err`, or `e.printStackTrace()`.**
Use kotlin-logging (already in all modules) instead.

### Adding a logger

```kotlin
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}
```

Place `private val logger` at file level (top of file, after imports), not inside a class.

### Use lambda syntax

```kotlin
logger.debug { "Loaded ${items.size} elements" }   // GOOD — message only built if DEBUG enabled
logger.debug("Loaded ${items.size} elements")       // BAD  — string always built
logger.error(e) { "Failed to load $fileName" }      // GOOD — exception + lazy message
```

### Log level guidance

| Level | Use for |
|-------|---------|
| ERROR | Unrecoverable failures, caught exceptions that indicate bugs |
| WARN  | Recoverable problems, missing optional resources |
| INFO  | Startup banners, summary statistics, milestones |
| DEBUG | Per-element details, generated output, intermediate state |
| TRACE | Extremely verbose loop-level tracing (rarely used) |

### Configuration files

- **Production:** `gearshift-kerml-runtime/src/main/resources/logback.xml` (and `gearshift-kerml-model/src/main/resources/logback.xml`)
- **Tests:** `gearshift-kerml-runtime/src/test/resources/logback-test.xml` — defaults to WARN

### Per-test log files

Each test class automatically gets its own DEBUG-level log file at `build/test-logs/<TestClassName>.log`. The console stays quiet (WARN only).

After running tests:
```bash
# List all per-test log files
ls build/test-logs/

# Read a specific test's log
cat build/test-logs/ClassifierGeneratorTest.log
```

- **KerMLTestSpec tests** (Kotest) get this automatically via `beforeSpec`/`afterSpec` hooks in `KerMLTestSupport.kt`
- **JUnit tests** need `@ExtendWith(TestLogExtension::class)` on the class

### Seeing debug output during test development

Per-test log files already capture DEBUG output. For additional console output, edit `logback-test.xml` and add a package-level logger:
```xml
<logger name="org.openmbee.gearshift.kerml.generator" level="DEBUG" />
```
**Revert before committing.**