# Testing Framework

## Overview

A comprehensive testing framework has been implemented for the Gearshift KerML service using:

- **JUnit 5** - Test runner
- **Kotest** - BDD-style test DSL and assertions
- **MockK** - Mocking framework

## Test Structure

```
src/test/kotlin/org/openmbee/gearshift/
├── engine/
│   ├── MDMEngineTest.kt                  # Core MOF engine tests
│   └── OperationInvocationTest.kt        # Operation invocation tests
├── metamodel/
│   └── MetamodelRegistryTest.kt          # Metamodel registry tests
└── integration/
    └── GearshiftEngineIntegrationTest.kt # Full stack integration tests
```

## Test Suites

### 1. MDMEngineTest

Tests core MOF engine functionality:

**Instance Management:**

- Creating instances of registered classes
- Error handling for non-existent classes
- Preventing instantiation of abstract classes

**Property Management:**

- Setting and getting property values
- Error handling for non-existent properties
- Read-only property enforcement
- Null value handling

**Property Inheritance:**

- Accessing properties from superclasses
- Multi-level inheritance

**Validation:**

- Required property validation
- Unique collection validation
- Valid instance checking

**Instance Tracking:**

- Tracking all created instances

### 2. OperationInvocationTest

Tests operation invocation functionality:

**Simple Property Access Bodies:**

- Invoking operations that return property values
- Handling null property values
- Operations without bodies

**Operation Inheritance:**

- Invoking inherited operations
- Deep inheritance hierarchy support

**Operation Parameters:**

- Operations with parameters
- Required parameter validation
- Unknown parameter detection
- Optional parameters with defaults

**Error Handling:**

- Non-existent operation errors
- Null instance handling

**Query Operations:**

- Marking operations as queries
- Query operation execution

### 3. MetamodelRegistryTest

Tests metamodel registry functionality:

**Class Registration:**

- Registering and retrieving metaclasses
- Listing all registered classes
- Handling non-existent classes

**Association Registration:**

- Registering and retrieving associations
- Listing all registered associations

**Metamodel Validation:**

- Valid metamodel validation
- Missing superclass detection

**Registry Management:**

- Clearing all registered elements

### 4. GearshiftEngineIntegrationTest

Tests the full stack integration:

**Complete Workflow:**

- Metamodel registration → Instance creation → Property setting → Operation invocation → Validation

**Repository Integration:**

- Storing and retrieving instances
- Querying by type
- Querying by property value
- Instance deletion

**Inheritance and Operations:**

- Inherited operation invocation
- Deep inheritance hierarchies

**Metamodel Validation:**

- Consistency checking

**Statistics and Monitoring:**

- Repository statistics

**Data Management:**

- Clearing instances (keeping metamodel)
- Clearing everything

**KerML Element Operations:**

- effectiveName() operation testing
- Null handling

## Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "OperationInvocationTest"

# Run with verbose output
./gradlew test --info

# Generate test report
./gradlew test
# Report available at: build/reports/tests/test/index.html
```

## Test Coverage

### Operation Invocation

✅ Simple property access bodies
✅ Operation inheritance
✅ Parameter validation
✅ Error handling
✅ Query operations

### Core MOF Engine

✅ Instance creation and management
✅ Property get/set
✅ Property inheritance
✅ Validation
✅ Instance tracking

### Integration

✅ Full workflow (metamodel → instances → operations)
✅ Repository integration
✅ Statistics and monitoring

## Example Test

```kotlin
describe("MDMEngine operation invocation") {
    context("invoking operations with simple property access bodies") {
        it("should invoke operation that returns a property value") {
            val registry = MetamodelRegistry()
            val metaClass = MetaClass(
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
                        isQuery = true
                    )
                )
            )
            registry.registerClass(metaClass)

            val engine = MDMEngine(registry)
            val instance = engine.createInstance("Element")
            engine.setProperty(instance, "declaredName", "TestElement")

            val result = engine.invokeOperation(instance, "effectiveName")

            result shouldBe "TestElement"
        }
    }
}
```

## Future Test Additions

- **OCL Integration Tests**: When OCL evaluator is integrated
- **Complex Operation Bodies**: When expression evaluation is implemented
- **Derived Property Tests**: When derived property evaluation is complete
- **Constraint Evaluation Tests**: When constraint engine is integrated
- **Performance Tests**: For large metamodels and instance counts
- **Concurrent Access Tests**: Thread-safety validation
- **Name Resolution Tests**: Comprehensive KerML name resolution testing
- **Parser Integration Tests**: When KerML parser is integrated

## Test-Driven Development

The testing framework supports TDD workflow:

1. **Write failing test** for new feature
2. **Implement feature** to make test pass
3. **Refactor** with confidence
4. **Repeat**

The comprehensive test suite ensures that:

- New features work correctly
- Existing features aren't broken by changes
- Edge cases are handled properly
- API contracts are maintained
