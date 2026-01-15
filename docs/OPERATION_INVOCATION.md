# Operation Invocation Feature

## Overview

Operation invocation has been fully implemented in the Gearshift KerML framework, allowing metamodel-defined operations
to be called on instance objects.

## Architecture

### Core Components

1. **MetaOperation** ([MetaOperation.kt](../src/main/kotlin/org/openmbee/gearshift/metamodel/MetaOperation.kt))
    - Metadata for operations
    - Supports parameters, return types, and body expressions
    - Query operation flag

2. **MDMEngine** ([MDMEngine.kt](../src/main/kotlin/org/openmbee/gearshift/engine/MDMEngine.kt#L190-L281))
    - `invokeOperation()` - Main invocation method
    - `findOperation()` - Searches class hierarchy
    - `validateArguments()` - Parameter validation
    - `evaluateOperation()` - Executes operation body

3. **GearshiftEngine** ([GearshiftEngine.kt](../src/main/kotlin/org/openmbee/gearshift/GearshiftEngine.kt#L127-L143))
    - Unified API for operation invocation
    - Repository integration

## Usage

### Define an Operation in Metamodel

```kotlin
val elementMetaClass = MetaClass(
    name = "Element",
    attributes = listOf(
        MetaProperty(
            name = "declaredName",
            type = "String",
            multiplicity = "0..1"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            body = "declaredName",
            description = "Returns the effective name for this Element",
            isQuery = true
        )
    )
)
```

### Invoke Operation on Instance

```kotlin
val engine = GearshiftEngine()
engine.registerMetaClass(elementMetaClass)

// Create instance
val (elementId, element) = engine.createInstance("Element")
engine.setProperty(elementId, "declaredName", "MyElement")

// Invoke operation
val effectiveName = engine.invokeOperation(elementId, "effectiveName")
println(effectiveName) // Output: "MyElement"
```

### Operations with Parameters

```kotlin
val calculator = MetaClass(
    name = "Calculator",
    operations = listOf(
        MetaOperation(
            name = "add",
            returnType = "Int",
            parameters = listOf(
                MetaParameter(name = "a", type = "Int"),
                MetaParameter(name = "b", type = "Int")
            )
        )
    )
)

// Invoke with arguments
val result = engine.invokeOperation(
    calculatorId,
    "add",
    mapOf("a" to 5, "b" to 3)
)
```

## Features

### ✅ Implemented

1. **Operation Definition**
    - Name, return type, parameters
    - Body expressions
    - Description and query flag

2. **Operation Invocation**
    - Instance-based invocation
    - Parameter passing
    - Return value handling

3. **Inheritance Support**
    - Operations inherited from superclasses
    - Multi-level inheritance hierarchy traversal

4. **Validation**
    - Operation existence checking
    - Required parameter validation
    - Unknown parameter detection
    - Optional parameter support

5. **Simple Body Evaluation**
    - Property access expressions (e.g., `"declaredName"`)
    - Automatic property value retrieval

### ⏳ Future Enhancements

1. **Complex Body Evaluation**
    - OCL expression evaluation
    - Arithmetic operations
    - Conditional logic
    - Collection operations

2. **Operation Overriding**
    - Subclass operation override support
    - Super operation invocation

3. **Side Effects**
    - State-modifying operations
    - Transaction support

4. **Performance**
    - Operation result caching
    - Lazy evaluation

## Current Limitations

1. **Body Evaluation**: Currently supports only simple property access expressions
    - ✅ Works: `body = "declaredName"` (returns property value)
    - ⏳ Planned: `body = "declaredName + ' (v' + version + ')'"` (complex expressions)

2. **Type Checking**: No runtime type validation of operation results

3. **Async Operations**: No support for asynchronous operation execution

## Implementation Details

### Operation Resolution Algorithm

```
1. Check instance's metaclass for operation
2. If not found, recursively check each superclass
3. Return first match (depth-first search)
4. Throw exception if not found
```

### Parameter Validation

```
1. Check all required parameters are provided
2. Check no unknown parameters are provided
3. Parameters with defaults are optional
```

### Body Evaluation

```
1. If body is null, return null
2. If body is simple identifier (property name):
   - Return property value from instance
3. Otherwise:
   - Log warning (complex evaluation not yet implemented)
   - Return null
```

## Examples

### KerML Element effectiveName

The first operation implemented for KerML:

```kotlin
// Element.kt
MetaOperation(
    name = "effectiveName",
    returnType = "String",
    description = "Return an effective name for this Element. " +
                  "By default this is the same as its declaredName.",
    body = "declaredName",
    isQuery = true
)
```

Usage:

```kotlin
val (id, _) = engine.createInstance("Element")
engine.setProperty(id, "declaredName", "MyFeature")

val name = engine.invokeOperation(id, "effectiveName")
// Returns: "MyFeature"
```

### Query Operations

Operations marked with `isQuery = true` indicate they don't modify state:

```kotlin
MetaOperation(
    name = "getName",
    returnType = "String",
    body = "name",
    isQuery = true  // Read-only operation
)
```

## Testing

Comprehensive tests
in [OperationInvocationTest.kt](../src/test/kotlin/org/openmbee/gearshift/engine/OperationInvocationTest.kt):

- Simple property access bodies
- Operation inheritance
- Parameter validation
- Error handling
- Query operations

See [TESTING.md](TESTING.md) for details.

## Error Handling

### Operation Not Found

```kotlin
// Throws: IllegalArgumentException
// Message: "Operation 'foo' not found in class: Element"
engine.invokeOperation(id, "foo")
```

### Missing Required Parameter

```kotlin
// Throws: IllegalArgumentException
// Message: "Missing required parameter 'b' for operation 'add'"
engine.invokeOperation(id, "add", mapOf("a" to 5))
```

### Unknown Parameter

```kotlin
// Throws: IllegalArgumentException
// Message: "Unknown parameter 'c' for operation 'add'"
engine.invokeOperation(id, "add", mapOf("a" to 5, "b" to 3, "c" to 7))
```

## Performance Considerations

1. **Operation Lookup**: O(depth of inheritance) - cached at metaclass level
2. **Parameter Validation**: O(number of parameters)
3. **Property Access**: O(1) - HashMap lookup
4. **Future**: Consider caching operation references per instance class

## Integration Points

### With Repository

Operations are invoked on instances stored in the repository. The GearshiftEngine handles instance retrieval
automatically.

### With Name Resolver

Future: Operations may reference resolved names in their bodies.

### With Constraint Engine

Future: Operations may be used in constraint expressions.

## API Reference

### GearshiftEngine

```kotlin
fun invokeOperation(
    instanceId: String,
    operationName: String,
    arguments: Map<String, Any?> = emptyMap()
): Any?
```

### MDMEngine

```kotlin
fun invokeOperation(
    instance: MDMObject,
    operationName: String,
    arguments: Map<String, Any?> = emptyMap()
): Any?
```

## Next Steps

To fully leverage operation invocation in KerML:

1. **Add More Operations**: Define operations for other KerML metaclasses
2. **Integrate OCL**: Enable complex body evaluation
3. **Add Constraints**: Link operations with constraint validation
4. **Performance Tuning**: Profile and optimize for large models

## Summary

Operation invocation is now a first-class feature of the Gearshift framework, providing:

- Metamodel-driven operation definitions
- Instance-based invocation with validation
- Inheritance support
- Clean API integration

The foundation is in place for rich behavioral modeling in KerML.
