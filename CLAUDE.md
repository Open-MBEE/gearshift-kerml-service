# Claude Code Guidelines for Gearshift KerML Service

Constraints, Properties, and Operations should always be added in alphabetical order

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
