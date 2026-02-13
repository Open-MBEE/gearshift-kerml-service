# Appendix A: MDM Self-Definition

This appendix presents the Meta-Definition Model defined using its own notation — the bootstrap definition. This
self-referential definition demonstrates that MDM is expressive enough to describe itself.

---

## A.1 Primitive Types

MDM defines the following primitive types, which serve as the base types for MetaProperty:

```
PrimitiveType String
PrimitiveType Boolean
PrimitiveType Integer
PrimitiveType Real
PrimitiveType UnlimitedNatural
```

---

## A.2 Enumerations

### A.2.1 AggregationKind

```
Enumeration AggregationKind {
    NONE        -- No ownership relationship
    SHARED      -- Shared ownership (reference)
    COMPOSITE   -- Exclusive ownership (containment)
}
```

### A.2.2 ConstraintType

```
Enumeration ConstraintType {
    DERIVATION              -- Computes derived property values
    VERIFICATION            -- Validates invariants
    NON_NAVIGABLE_END       -- Computes non-navigable association ends
}
```

### A.2.3 BindingKind

```
Enumeration BindingKind {
    SPECIALIZES   -- Instance specializes the library concept
    SUBSETS       -- Instance subsets the library concept
}
```

### A.2.4 BodyLanguage

```
Enumeration BodyLanguage {
    OCL           -- Object Constraint Language
    PROPERTY_REF  -- Simple property reference
    GQL           -- Graph Query Language
}
```

---

## A.3 MetaClass

The root metaclass for defining types in the metamodel.

```
metaclass MetaClass {
    attributes {
        name : String [1]
            -- Unique identifier for this metaclass

        isAbstract : Boolean [1] = false
            -- If true, cannot be directly instantiated

        description : String [0..1]
            -- Human-readable documentation
    }

    constraints {
        checkNameNotEmpty : VERIFICATION
            expression = "name.size() > 0"
            description = "Metaclass name must not be empty"

        checkNoSuperclassCycles : VERIFICATION
            expression = "not self.allSuperclasses()->includes(self)"
            description = "Superclass hierarchy must be acyclic"
    }

    operations {
        allSuperclasses() : MetaClass [0..*]
            body = "superclasses->closure(superclasses)"
            bodyLanguage = OCL
            isQuery = true
            description = "All direct and indirect superclasses"

        allAttributes() : MetaProperty [0..*]
            body = "allSuperclasses()->including(self)->collect(attributes)->flatten()->asOrderedSet()"
            bodyLanguage = OCL
            isQuery = true
            description = "All attributes including inherited"

        allOperations() : MetaOperation [0..*]
            body = "allSuperclasses()->including(self)->collect(operations)->flatten()->asOrderedSet()"
            bodyLanguage = OCL
            isQuery = true
            description = "All operations including inherited"

        allConstraints() : MetaConstraint [0..*]
            body = "allSuperclasses()->including(self)->collect(constraints)->flatten()->asOrderedSet()"
            bodyLanguage = OCL
            isQuery = true
            description = "All constraints including inherited"

        conformsTo(other : MetaClass) : Boolean
            body = "self = other or allSuperclasses()->includes(other)"
            bodyLanguage = OCL
            isQuery = true
            description = "True if this metaclass is or inherits from other"
    }
}
```

### A.3.1 MetaClass Associations

```
association Inheritance {
    sourceEnd subclass : MetaClass [0..*]
        navigable = false

    targetEnd superclasses : MetaClass [0..*]
        navigable = true
        description = "Parent metaclasses from which this metaclass inherits"
}

association ClassAttributes {
    sourceEnd owningClass : MetaClass [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd attributes : MetaProperty [0..*] {ordered}
        navigable = true
        description = "Primitive-typed properties of this metaclass"
}

association ClassConstraints {
    sourceEnd owningClass : MetaClass [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd constraints : MetaConstraint [0..*] {ordered}
        navigable = true
        description = "Validation and derivation rules for this metaclass"
}

association ClassOperations {
    sourceEnd owningClass : MetaClass [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd operations : MetaOperation [0..*] {ordered}
        navigable = true
        description = "Behavioral specifications for this metaclass"
}

association ClassSemanticBindings {
    sourceEnd owningClass : MetaClass [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd semanticBindings : SemanticBinding [0..*] {ordered}
        navigable = true
        description = "Links to base library concepts"
}

association ClassOwnershipBinding {
    sourceEnd owningClass : MetaClass [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd ownershipBinding : OwnershipBinding [0..1]
        navigable = true
        description = "Ownership intermediate configuration"
}
```

---

## A.4 MetaProperty

Defines a primitive-typed attribute of a MetaClass.

```
metaclass MetaProperty {
    attributes {
        name : String [1]
            -- Property identifier

        type : String [1]
            -- Primitive type name (String, Boolean, Integer, Real, UnlimitedNatural)

        lowerBound : Integer [1] = 1
            -- Minimum cardinality

        upperBound : UnlimitedNatural [1] = 1
            -- Maximum cardinality (-1 = unlimited)

        isDerived : Boolean [1] = false
            -- If true, value is computed by derivationConstraint

        isReadOnly : Boolean [1] = false
            -- If true, cannot be modified after initialization

        isOrdered : Boolean [1] = false
            -- If true and multivalued, collection maintains insertion order

        isUnique : Boolean [1] = true
            -- If true and multivalued, collection contains no duplicates

        isUnion : Boolean [1] = false
            -- If true, value is union of all subsetted properties

        defaultValue : String [0..1]
            -- Default value expression

        description : String [0..1]
            -- Human-readable documentation
    }

    constraints {
        checkValidPrimitiveType : VERIFICATION
            expression = "Set{'String', 'Boolean', 'Integer', 'Real', 'UnlimitedNatural'}->includes(type)"
            description = "Type must be a valid primitive type"

        checkValidMultiplicity : VERIFICATION
            expression = "lowerBound >= 0 and (upperBound = -1 or upperBound >= lowerBound)"
            description = "Multiplicity bounds must be valid"

        checkDerivedHasConstraint : VERIFICATION
            expression = "isDerived implies derivationConstraint->notEmpty()"
            description = "Derived properties should have a derivation constraint"

        checkUnionIsMultivalued : VERIFICATION
            expression = "isUnion implies (upperBound = -1 or upperBound > 1)"
            description = "Union properties must be multivalued"
    }

    operations {
        isMultivalued() : Boolean
            body = "upperBound = -1 or upperBound > 1"
            bodyLanguage = OCL
            isQuery = true

        isRequired() : Boolean
            body = "lowerBound > 0"
            bodyLanguage = OCL
            isQuery = true
    }
}
```

### A.4.1 MetaProperty Associations

```
association PropertyDerivation {
    sourceEnd derivedProperty : MetaProperty [0..*]
        navigable = false

    targetEnd derivationConstraint : MetaConstraint [0..1]
        navigable = true
        description = "Constraint that computes the derived value"
}

association PropertyRedefines {
    sourceEnd redefiningProperty : MetaProperty [0..*]
        navigable = false

    targetEnd redefines : MetaProperty [0..*]
        navigable = true
        description = "Properties that this property redefines"
}

association PropertySubsets {
    sourceEnd subsettingProperty : MetaProperty [0..*]
        navigable = false

    targetEnd subsets : MetaProperty [0..*]
        navigable = true
        description = "Properties that this property subsets"
}
```

---

## A.5 MetaAssociation

Defines a relationship between two metaclasses.

```
metaclass MetaAssociation {
    attributes {
        name : String [1]
            -- Association identifier

        isDerived : Boolean [1] = false
            -- If true, relationship is computed

        description : String [0..1]
            -- Human-readable documentation
    }

    constraints {
        checkAtLeastOneNavigable : VERIFICATION
            expression = "sourceEnd.isNavigable or targetEnd.isNavigable"
            description = "At least one end must be navigable"

        checkCompositeMultiplicity : VERIFICATION
            expression = "sourceEnd.aggregation = AggregationKind::COMPOSITE implies targetEnd.upperBound = 1"
            description = "Composite aggregation requires single owner"
    }
}
```

### A.5.1 MetaAssociation Associations

```
association AssociationSourceEnd {
    sourceEnd owningAssociation : MetaAssociation [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd sourceEnd : MetaAssociationEnd [1]
        navigable = true
        description = "First end of the association"
}

association AssociationTargetEnd {
    sourceEnd owningAssociation : MetaAssociation [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd targetEnd : MetaAssociationEnd [1]
        navigable = true
        description = "Second end of the association"
}
```

---

## A.6 MetaAssociationEnd

Defines one end of a MetaAssociation.

```
metaclass MetaAssociationEnd {
    attributes {
        name : String [1]
            -- End identifier (property name from opposite perspective)

        lowerBound : Integer [1] = 0
            -- Minimum cardinality

        upperBound : UnlimitedNatural [1] = -1
            -- Maximum cardinality (-1 = unlimited)

        isNavigable : Boolean [1] = true
            -- If true, can be traversed from opposite end

        aggregation : AggregationKind [1] = NONE
            -- Ownership semantics

        isOrdered : Boolean [1] = false
            -- If true and multivalued, collection maintains order

        isUnique : Boolean [1] = true
            -- If true and multivalued, no duplicates

        isDerived : Boolean [1] = false
            -- If true, value is computed

        isUnion : Boolean [1] = false
            -- If true, value is union of subsetted ends

        description : String [0..1]
            -- Human-readable documentation
    }

    constraints {
        checkValidMultiplicity : VERIFICATION
            expression = "lowerBound >= 0 and (upperBound = -1 or upperBound >= lowerBound)"
            description = "Multiplicity bounds must be valid"
    }

    operations {
        isMultivalued() : Boolean
            body = "upperBound = -1 or upperBound > 1"
            bodyLanguage = OCL
            isQuery = true
    }
}
```

### A.6.1 MetaAssociationEnd Associations

```
association EndType {
    sourceEnd typedEnd : MetaAssociationEnd [0..*]
        navigable = false

    targetEnd type : MetaClass [1]
        navigable = true
        description = "Target metaclass of this end"
}

association EndDerivation {
    sourceEnd derivedEnd : MetaAssociationEnd [0..*]
        navigable = false

    targetEnd derivationConstraint : MetaConstraint [0..1]
        navigable = true
        description = "Constraint that computes the derived value"
}

association EndRedefines {
    sourceEnd redefiningEnd : MetaAssociationEnd [0..*]
        navigable = false

    targetEnd redefines : MetaAssociationEnd [0..*]
        navigable = true
        description = "Ends that this end redefines"
}

association EndSubsets {
    sourceEnd subsettingEnd : MetaAssociationEnd [0..*]
        navigable = false

    targetEnd subsets : MetaAssociationEnd [0..*]
        navigable = true
        description = "Ends that this end subsets"
}
```

---

## A.7 MetaOperation

Defines a behavioral specification on a MetaClass.

```
metaclass MetaOperation {
    attributes {
        name : String [1]
            -- Operation identifier

        returnType : String [0..1]
            -- Return type name (primitive or metaclass)

        body : String [0..1]
            -- Implementation expression

        bodyLanguage : BodyLanguage [1] = OCL
            -- Language of body expression

        isAbstract : Boolean [1] = false
            -- If true, no body provided

        isQuery : Boolean [1] = true
            -- If true, does not modify state

        description : String [0..1]
            -- Human-readable documentation
    }

    constraints {
        checkAbstractNoBody : VERIFICATION
            expression = "isAbstract implies body->isEmpty()"
            description = "Abstract operations must not have a body"

        checkConcreteHasBody : VERIFICATION
            expression = "not isAbstract implies body->notEmpty()"
            description = "Concrete operations must have a body"
    }
}
```

### A.7.1 MetaOperation Associations

```
association OperationParameters {
    sourceEnd owningOperation : MetaOperation [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd parameters : MetaParameter [0..*] {ordered}
        navigable = true
        description = "Input parameters for this operation"
}

association OperationPreconditions {
    sourceEnd constrainedOperation : MetaOperation [0..*]
        navigable = false

    targetEnd preconditions : MetaConstraint [0..*]
        navigable = true
        description = "Constraints that must hold before invocation"
}
```

---

## A.8 MetaParameter

Defines a parameter of a MetaOperation.

```
metaclass MetaParameter {
    attributes {
        name : String [1]
            -- Parameter identifier

        type : String [1]
            -- Parameter type name

        lowerBound : Integer [1] = 1
            -- Minimum cardinality

        upperBound : UnlimitedNatural [1] = 1
            -- Maximum cardinality

        defaultValue : String [0..1]
            -- Default value expression

        description : String [0..1]
            -- Human-readable documentation
    }
}
```

---

## A.9 MetaConstraint

Defines a validation or derivation rule.

```
metaclass MetaConstraint {
    attributes {
        name : String [1]
            -- Constraint identifier

        type : ConstraintType [1] = DERIVATION
            -- Category of constraint

        expression : String [1]
            -- Constraint body

        language : String [1] = "OCL"
            -- Expression language

        description : String [0..1]
            -- Human-readable documentation
    }

    constraints {
        checkVerificationReturnsBoolean : VERIFICATION
            expression = "type = ConstraintType::VERIFICATION implies self.returnsBoolean()"
            description = "Verification constraints must return Boolean"
    }

    operations {
        returnsBoolean() : Boolean
            body = "true"  -- Would need type inference in practice
            bodyLanguage = OCL
            isQuery = true
            description = "Check if expression returns Boolean"
    }
}
```

---

## A.10 SemanticBinding

Links metaclass instances to base library concepts.

```
metaclass SemanticBinding {
    attributes {
        name : String [1]
            -- Binding identifier

        baseConcept : String [1]
            -- Qualified name of library concept (e.g., "Base::Anything")

        bindingKind : BindingKind [1] = SPECIALIZES
            -- Type of relationship created

        description : String [0..1]
            -- Human-readable documentation
    }
}
```

### A.10.1 SemanticBinding Associations

```
association BindingCondition {
    sourceEnd owningBinding : SemanticBinding [1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd condition : BindingCondition [0..1]
        navigable = true
        description = "Condition that determines when binding applies"
}
```

---

## A.11 BindingCondition

Abstract base for conditions that control when semantic bindings apply.

```
abstract metaclass BindingCondition {
    attributes {
        description : String [0..1]
    }
}

metaclass DefaultCondition specializes BindingCondition {
    -- Always applies (no additional attributes)
}

metaclass TypedByCondition specializes BindingCondition {
    attributes {
        metaclass : String [1]
            -- Metaclass name that types the element
    }
}

metaclass OwningTypeIsCondition specializes BindingCondition {
    attributes {
        metaclass : String [1]
            -- Required metaclass of owning type
    }
}

metaclass PropertyEqualsCondition specializes BindingCondition {
    attributes {
        property : String [1]
            -- Property to check

        value : String [1]
            -- Required value
    }
}

metaclass CollectionNotEmptyCondition specializes BindingCondition {
    attributes {
        property : String [1]
            -- Collection property that must be non-empty
    }
}

metaclass AndCondition specializes BindingCondition {
    -- Composite condition: all sub-conditions must hold
}

metaclass OrCondition specializes BindingCondition {
    -- Composite condition: at least one sub-condition must hold
}

metaclass NotCondition specializes BindingCondition {
    -- Negates the nested condition
}
```

### A.11.1 BindingCondition Associations

```
association CompositeConditionOperands {
    sourceEnd compositeCondition : BindingCondition [0..1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd operands : BindingCondition [0..*] {ordered}
        navigable = true
        description = "Sub-conditions for And/Or composites"
}

association NotConditionOperand {
    sourceEnd notCondition : NotCondition [0..1]
        navigable = false
        aggregation = COMPOSITE

    targetEnd operand : BindingCondition [1]
        navigable = true
        description = "Condition to negate"
}
```

---

## A.12 OwnershipBinding

Marks a metaclass as an ownership intermediate.

```
metaclass OwnershipBinding {
    attributes {
        ownedElementEnd : String [1]
            -- Name of association end pointing to owned element

        ownerEnd : String [1]
            -- Name of association end pointing back to owner

        description : String [0..1]
    }
}
```

---

## A.13 Metamodel Diagram

```
                                    ┌─────────────────┐
                                    │   MetaClass     │
                                    ├─────────────────┤
                                    │ name            │
                                    │ isAbstract      │
                                    │ description     │
                                    └────────┬────────┘
                                             │
              ┌──────────────────────────────┼──────────────────────────────┐
              │                              │                              │
              ▼                              ▼                              ▼
    ┌─────────────────┐            ┌─────────────────┐            ┌─────────────────┐
    │  MetaProperty   │            │  MetaOperation  │            │ MetaConstraint  │
    ├─────────────────┤            ├─────────────────┤            ├─────────────────┤
    │ name            │            │ name            │            │ name            │
    │ type            │            │ returnType      │            │ type            │
    │ lowerBound      │            │ body            │            │ expression      │
    │ upperBound      │            │ bodyLanguage    │            │ language        │
    │ isDerived       │            │ isAbstract      │            └─────────────────┘
    │ isReadOnly      │            │ isQuery         │
    │ ...             │            └────────┬────────┘
    └─────────────────┘                     │
                                            ▼
                                  ┌─────────────────┐
                                  │  MetaParameter  │
                                  ├─────────────────┤
                                  │ name            │
                                  │ type            │
                                  │ lowerBound      │
                                  │ upperBound      │
                                  └─────────────────┘


    ┌─────────────────┐           ┌─────────────────┐
    │ MetaAssociation │◆─────────▶│MetaAssociationEnd│
    ├─────────────────┤           ├─────────────────┤
    │ name            │           │ name            │
    │ isDerived       │           │ type ──────────────▶ MetaClass
    └─────────────────┘           │ lowerBound      │
         │                        │ upperBound      │
         │ sourceEnd              │ isNavigable     │
         │ targetEnd              │ aggregation     │
         └───────────────────────▶│ isDerived       │
                                  └─────────────────┘


    ┌─────────────────┐           ┌─────────────────┐
    │ SemanticBinding │◆─────────▶│BindingCondition │
    ├─────────────────┤           ├─────────────────┤
    │ name            │           │ (abstract)      │
    │ baseConcept     │           └────────┬────────┘
    │ bindingKind     │                    │
    └─────────────────┘                    ├── DefaultCondition
                                           ├── TypedByCondition
                                           ├── PropertyEqualsCondition
                                           ├── AndCondition
                                           ├── OrCondition
                                           └── NotCondition
```

---

## A.14 Bootstrap Notes

This self-definition demonstrates MDM's expressiveness but raises the classic bootstrapping question: how is the first
MDM interpreter constructed?

**Practical Resolution:**

1. An initial MDM interpreter is implemented directly in a host language (e.g., Kotlin, Java)
2. The interpreter can then load and validate this self-definition
3. The self-definition serves as the normative specification

**Consistency Check:**
A conforming MDM implementation must be able to:

1. Parse this self-definition
2. Validate it against itself
3. Produce equivalent behavior when processing other metamodels

---

## Change History

| Version | Date       | Description   |
|---------|------------|---------------|
| 0.1     | 2026-02-02 | Initial draft |