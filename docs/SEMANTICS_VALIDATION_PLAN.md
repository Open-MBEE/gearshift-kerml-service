# KerML Semantics Validation Plan

This document outlines the comprehensive plan for validating all KerML semantics defined in Section 8.4 of the KerML specification.

## Overview

KerML semantics are enforced through:
1. **Semantic Constraints** - OCL constraints defined in the metamodel that must be satisfied
2. **Implied Relationships** - Relationships automatically created to satisfy constraints
3. **Redundancy Elimination** - Rules to avoid creating duplicate implied relationships

The implementation spans:
- `KerMLSemanticHandler.kt` - Creates implied relationships via lifecycle events
- Metamodel definitions (Type.kt, Feature.kt, etc.) - Define `SemanticBinding` entries
- `OclExecutor.kt` - Evaluates OCL constraint expressions

---

## Phase 1: Table 8 - Core Semantics Implied Relationships

### Status: ✅ IMPLEMENTED (Tests Passing)

| Constraint | Implied Relationship | Target | Status |
|------------|---------------------|--------|--------|
| `checkTypeSpecialization` | `Subclassification` | `Base::Anything` | ✅ Done |
| `checkFeatureSpecialization` | `Subsetting` | `Base::things` | ✅ Done |

### Current Tests
- `CoreSemanticsImpliedRelationshipsTest.kt`
  - ✅ Creates implied Subclassification for classes without explicit specialization
  - ✅ Redundancy elimination: no duplicate when class already specializes another
  - ✅ Creates implied Subsetting for features without explicit subsetting
  - ✅ Redundancy elimination: no duplicate when feature already subsets another
  - ✅ Implied relationships marked with `isImplied = true`
  - ✅ Implied relationships owned by the specific type

---

## Phase 2: Table 9 - Core Semantics Supporting Kernel Semantics

### Status: 🔶 PARTIAL (Infrastructure exists, some tests disabled)

| Constraint | Implied Relationship | Target | Status |
|------------|---------------------|--------|--------|
| `checkFeatureDataValueSpecialization` | `Subsetting` | `Base::dataValues` | ✅ SemanticBinding defined |
| `checkFeatureOccurrenceSpecialization` | `Subsetting` | `Occurrences::occurrences` | 🔶 Binding defined, library resolution needed |
| `checkFeatureSuboccurrenceSpecialization` | `Subsetting` | `Occurrences::Occurrence::suboccurrences` | 🔶 Binding defined, library resolution needed |
| `checkFeatureFeatureMembershipTypeFeaturing` | `TypeFeaturing` | owningType or snapshots | ❌ Not implemented |
| `checkFeatureObjectSpecialization` | `Subsetting` | `Objects::objects` | 🔶 Binding defined, library resolution needed |
| `checkFeatureSubobjectSpecialization` | `Subsetting` | `Objects::Object::subobjects` | 🔶 Binding defined, library resolution needed |
| `checkFeatureEndSpecialization` | `Subsetting` | `Links::Link::participant` | 🔶 Binding defined, library resolution needed |
| `checkFeatureEndRedefinition` | `Redefinition` | supertype end at same position | ❌ Not implemented |
| `checkFeatureCrossingSpecialization` | `CrossSubsetting` | cross Feature chain | ❌ Not implemented |
| `checkFeatureOwnedCrossFeatureRedefinitionSpecialization` | `Subsetting` | ownedCrossFeature of redefined end | ❌ Not implemented |
| `checkFeatureOwnedCrossFeatureTypeFeaturing` | `TypeFeaturing` | types of other ends | ❌ Not implemented |
| `checkFeatureStepSpecialization` | `Subsetting` | `Performances::performances` | ❌ Binding not defined |
| `checkFeatureSubperformanceSpecialization` | `Subsetting` | `Performances::Performance::subperformances` | ❌ Binding not defined |
| `checkFeatureEnclosedPerformanceSpecialization` | `Subsetting` | `Performances::Performance::enclosedPerformances` | ❌ Binding not defined |
| `checkFeatureOwnedResultExpressionRedefinition` | `Redefinition` | result of supertype Expression | ❌ Not implemented |
| `checkFeatureFlowFeatureRedefinition` | `Redefinition` | `Transfer::source::sourceOutput` or `Transfer::target::targetInput` | 🔶 Constraint defined, handler incomplete |
| `checkFeatureValuationSpecialization` | `Subsetting` | result of FeatureValue Expression | 🔶 Constraint defined |

### Tasks

#### 2.1 Library Resolution Enhancement
- [ ] Fix qualified name resolution for library elements (e.g., `Occurrences::occurrences`)
- [ ] Ensure library is loaded before processing semantic bindings
- [ ] Cache library element references for performance

#### 2.2 TypeFeaturing Support
- [ ] Implement `createImpliedTypeFeaturing()` in KerMLSemanticHandler
- [ ] Handle `checkFeatureFeatureMembershipTypeFeaturing` constraint
- [ ] Handle `checkFeatureOwnedCrossFeatureTypeFeaturing` constraint

#### 2.3 Redefinition Support
- [ ] Implement `createImpliedRedefinition()` in KerMLSemanticHandler
- [ ] Handle `checkFeatureEndRedefinition` constraint
- [ ] Handle `checkFeatureOwnedResultExpressionRedefinition` constraint
- [ ] Handle `checkFeatureFlowFeatureRedefinition` constraint

#### 2.4 Additional Semantic Bindings
- [ ] Add `featureStepBinding` for Step specialization (`Performances::performances`)
- [ ] Add `featureSubperformanceBinding` (`Performances::Performance::subperformances`)
- [ ] Add `featureEnclosedPerformanceBinding` (`Performances::Performance::enclosedPerformances`)

#### 2.5 Test Coverage
- [ ] Enable `checkFeatureOccurrenceSpecialization` test after library resolution fix
- [ ] Enable `checkFeatureSuboccurrenceSpecialization` test after library resolution fix
- [ ] Add test for `checkFeatureObjectSpecialization`
- [ ] Add test for `checkFeatureSubobjectSpecialization`
- [ ] Add test for `checkFeatureEndSpecialization`
- [ ] Add test for `checkFeatureStepSpecialization`

---

## Phase 3: Table 10 - Kernel Semantics Implied Specializations

### Status: ❌ NOT IMPLEMENTED

| Constraint | Implied Relationship | Target | Status |
|------------|---------------------|--------|--------|
| `checkDataTypeSpecialization` | `Subclassification` | `Base::DataValue` | ❌ |
| `checkClassSpecialization` | `Subclassification` | `Occurrences::Occurrence` | ❌ |
| `checkStructureSpecialization` | `Subclassification` | `Objects::Object` | ❌ |
| `checkAssociationSpecialization` | `Subclassification` | `Links::Link` | ❌ |
| `checkAssociationBinarySpecialization` | `Subclassification` | `Links::BinaryLink` | ❌ |
| `checkAssociationStructureSpecialization` | `Subclassification` | `Objects::LinkObject` | ❌ |
| `checkBehaviorSpecialization` | `Subclassification` | `Performances::Performance` | ❌ |
| `checkFunctionSpecialization` | `Subclassification` | `Performances::Evaluation` | ❌ |
| `checkPredicateSpecialization` | `Subclassification` | `Performances::BooleanEvaluation` | ❌ |
| `checkInteractionSpecialization` | `Subclassification` | `Transfers::Transfer` | ❌ |
| `checkSuccessionSpecialization` | `Subclassification` | `Occurrences::HappensBefore` | ❌ |
| `checkSuccessionFlowSpecialization` | `Subclassification` | `Transfers::FlowTransfer` | ❌ |
| `checkMetaclassSpecialization` | `Subclassification` | `Metaobjects::Metaobject` | ❌ |
| `checkMetadataFeatureMetaclassSpecialization` | `FeatureTyping` | `Metaobjects::Metaobject` | ❌ |
| `checkMetadataFeatureSemanticSpecialization` | varies | baseType of SemanticMetadata | ❌ |
| `checkFeatureReferenceExpressionResultSpecialization` | `Subsetting` | referent | ❌ |
| `checkConstructorExpressionResultSpecialization` | varies | instantiatedType | ❌ |
| `checkInvocationExpressionResultSpecialization` | varies | instantiatedType | ❌ |
| `checkFeatureChainExpressionResultSpecialization` | `Subsetting` | last Feature in chain | ❌ |
| `checkSelectExpressionResultSpecialization` | `Subsetting` | referent | ❌ |
| `checkIndexExpressionResultSpecialization` | `Subsetting` | referent | ❌ |

### Tasks

#### 3.1 Kernel Classifier Bindings
Add semantic bindings to Kernel metaclasses:

```kotlin
// DataType.kt
SemanticBinding(
    name = "dataTypeDataValueBinding",
    baseConcept = "Base::DataValue",
    bindingKind = BindingKind.SPECIALIZES,
    condition = BindingCondition.Default
)

// Class.kt
SemanticBinding(
    name = "classOccurrenceBinding",
    baseConcept = "Occurrences::Occurrence",
    bindingKind = BindingKind.SPECIALIZES,
    condition = BindingCondition.Default
)

// Structure.kt
SemanticBinding(
    name = "structureObjectBinding",
    baseConcept = "Objects::Object",
    bindingKind = BindingKind.SPECIALIZES,
    condition = BindingCondition.Default
)

// Association.kt
SemanticBinding(
    name = "associationLinkBinding",
    baseConcept = "Links::Link",
    bindingKind = BindingKind.SPECIALIZES,
    condition = BindingCondition.Default
)

// etc. for all Table 10 entries
```

#### 3.2 Expression Result Bindings
- [ ] Implement result parameter subsetting for expressions
- [ ] Handle `checkFeatureReferenceExpressionResultSpecialization`
- [ ] Handle `checkFeatureChainExpressionResultSpecialization`
- [ ] Handle constructor and invocation expression result specialization

#### 3.3 Test Coverage
Create `KernelSpecializationsTest.kt`:
- [ ] Test DataType -> Base::DataValue
- [ ] Test Class -> Occurrences::Occurrence
- [ ] Test Structure -> Objects::Object
- [ ] Test Association -> Links::Link
- [ ] Test Behavior -> Performances::Performance
- [ ] Test Function -> Performances::Evaluation
- [ ] Test expression result subsetting

---

## Phase 4: Table 11 - Kernel Semantics Other Implied Relationships

### Status: ❌ NOT IMPLEMENTED

| Constraint | Implied Relationship | Target | Status |
|------------|---------------------|--------|--------|
| `checkConnectorEndRedefinition` | `Redefinition` | end of supertype Connector | ❌ |
| `checkConnectorTypeFeaturing` | `TypeFeaturing` | defaultFeaturingType | ❌ |
| `checkSuccessionSourceRedefinition` | `Redefinition` | `HappensBefore::earlierOccurrence` | ❌ |
| `checkSuccessionTargetRedefinition` | `Redefinition` | `HappensBefore::laterOccurrence` | ❌ |
| `checkInvocationExpressionBehaviorBindingConnector` | `BindingConnector` | behavior to instantiatedType | ❌ |
| `checkConstructorExpressionResultDefaultValueBindingConnector` | `BindingConnector` | default values | ❌ |
| `checkInvocationExpressionDefaultValueBindingConnector` | `BindingConnector` | default values | ❌ |
| `checkFeatureValueBindingConnector` | `BindingConnector` | featureWithValue to result | ❌ |
| `checkFeatureValueExpressionTypeFeaturing` | `TypeFeaturing` | featuringTypes of featureWithValue | ❌ |
| `checkFeatureChainExpressionTargetRedefinition` | `Redefinition` | nested Feature target | ❌ |
| `checkFeatureChainExpressionSourceTargetRedefinition` | `Redefinition` | nested Feature source target | ❌ |
| `checkConstructorExpressionResultFeatureRedefinition` | `Redefinition` | features at same position | ❌ |

### Tasks

#### 4.1 BindingConnector Support
- [ ] Implement `createImpliedBindingConnector()` in KerMLSemanticHandler
- [ ] Handle `checkFeatureValueBindingConnector` - critical for feature values
- [ ] Handle invocation/constructor binding connectors

#### 4.2 Connector/Succession Constraints
- [ ] Add semantic constraints to Connector.kt metamodel
- [ ] Add semantic constraints to Succession.kt metamodel
- [ ] Implement connector end redefinition logic

#### 4.3 Expression Chain Constraints
- [ ] Handle `checkFeatureChainExpressionTargetRedefinition`
- [ ] Handle `checkFeatureChainExpressionSourceTargetRedefinition`

#### 4.4 Test Coverage
Create `KernelImpliedRelationshipsTest.kt`:
- [ ] Test connector end redefinition
- [ ] Test succession source/target redefinition
- [ ] Test feature value binding connector
- [ ] Test expression type featuring

---

## Phase 5: Redundancy Elimination

### Status: 🔶 PARTIAL

The spec defines rules to avoid redundant implied relationships:

1. If there's an `ownedSpecialization` or other implied `Specialization` whose `generalType` is a subtype of another implied specialization's general, skip the more general one
2. If there are two implied `Specializations` with the same `generalType`, only include one

### Current Implementation
- ✅ Basic check: skip implied relationship if any explicit specialization exists
- ❌ Transitive redundancy detection (check if explicit path leads to general)
- ❌ Same-generalType deduplication

### Tasks
- [ ] Implement `checksTransitivelySpecializes(specific, general)` helper
- [ ] Check if explicit specialization path already leads to the implied target
- [ ] Track implied relationships per element to avoid duplicates
- [ ] Add comprehensive redundancy elimination tests

---

## Phase 6: Verification Constraints

### Status: ❌ NOT IMPLEMENTED

Beyond implied relationships, many OCL constraints verify model validity:

| Constraint Type | Count | Example |
|-----------------|-------|---------|
| `validateType*` | 8 | `validateTypeAtMostOneConjugator` |
| `validateFeature*` | 18 | `validateFeatureEndMultiplicity` |
| `validate*` (other) | many | Association, Connector, Expression constraints |

### Tasks
- [ ] Create `ConstraintValidator` service that runs verification constraints
- [ ] Integrate with model lifecycle (validate after changes settle)
- [ ] Report constraint violations with helpful messages
- [ ] Add configurable validation modes (strict, lenient)

---

## Implementation Order

### Milestone 1: Core Complete (Current)
- ✅ Table 8 implied relationships
- ✅ Basic redundancy elimination
- ✅ Core test coverage

### Milestone 2: Library Resolution ✅ COMPLETE
- ✅ Fixed qualified name resolution for library elements
- Tests still disabled pending semantic handler implementation (see Milestone 3)

### Milestone 3: Table 9 Complete
- Implement TypeFeaturing support
- Implement Redefinition support
- Add missing semantic bindings
- Full test coverage

### Milestone 4: Table 10 (Kernel Specializations)
- Add semantic bindings to Kernel metaclasses
- Handle expression result subsetting
- Test coverage

### Milestone 5: Table 11 (Other Implied Relationships)
- Implement BindingConnector support
- Handle connector/succession constraints
- Expression chain constraints

### Milestone 6: Verification
- Constraint validation service
- Full verification constraint coverage
- Performance optimization

---

## Test File Structure

```
gearshift-kerml-runtime/src/test/kotlin/org/openmbee/gearshift/kerml/
├── CoreSemanticsImpliedRelationshipsTest.kt       # Table 8 (✅ exists)
├── KernelSemanticsImpliedRelationshipsTest.kt     # Table 9 (🔶 exists, partial)
├── KernelSpecializationsTest.kt                   # Table 10 (❌ to create)
├── KernelImpliedRelationshipsTest.kt              # Table 11 (❌ to create)
├── RedundancyEliminationTest.kt                   # Redundancy rules (❌ to create)
└── ConstraintValidationTest.kt                    # Verification (❌ to create)
```

---

## Appendix: Metamodel Files Requiring Updates

### Semantic Bindings to Add

| File | Binding Name | Target |
|------|--------------|--------|
| `DataType.kt` | dataTypeDataValueBinding | `Base::DataValue` |
| `Class.kt` | classOccurrenceBinding | `Occurrences::Occurrence` |
| `Structure.kt` | structureObjectBinding | `Objects::Object` |
| `Association.kt` | associationLinkBinding | `Links::Link` |
| `Association.kt` | associationBinaryLinkBinding (conditional) | `Links::BinaryLink` |
| `AssociationStructure.kt` | associationStructureLinkObjectBinding | `Objects::LinkObject` |
| `Behavior.kt` | behaviorPerformanceBinding | `Performances::Performance` |
| `Function.kt` | functionEvaluationBinding | `Performances::Evaluation` |
| `Predicate.kt` | predicateBooleanEvaluationBinding | `Performances::BooleanEvaluation` |
| `Interaction.kt` | interactionTransferBinding | `Transfers::Transfer` |
| `Succession.kt` | successionHappensBeforeBinding | `Occurrences::HappensBefore` |
| `SuccessionFlow.kt` | successionFlowFlowTransferBinding | `Transfers::FlowTransfer` |
| `Metaclass.kt` | metaclassMetaobjectBinding | `Metaobjects::Metaobject` |
| `Step.kt` | stepPerformancesBinding | `Performances::performances` |
| `Feature.kt` | featureSubperformanceBinding | `Performances::Performance::subperformances` |
| `Feature.kt` | featureEnclosedPerformanceBinding | `Performances::Performance::enclosedPerformances` |

### Constraints to Add (OCL expressions already in spec)

| File | Constraint Name | Type |
|------|-----------------|------|
| `Connector.kt` | checkConnectorEndRedefinition | IMPLICIT_REDEFINITION |
| `Connector.kt` | checkConnectorTypeFeaturing | IMPLICIT_TYPE_FEATURING |
| `Succession.kt` | checkSuccessionSourceRedefinition | IMPLICIT_REDEFINITION |
| `Succession.kt` | checkSuccessionTargetRedefinition | IMPLICIT_REDEFINITION |
| `FeatureValue.kt` | checkFeatureValueBindingConnector | IMPLICIT_BINDING_CONNECTOR |
| `FeatureValue.kt` | checkFeatureValueExpressionTypeFeaturing | IMPLICIT_TYPE_FEATURING |
