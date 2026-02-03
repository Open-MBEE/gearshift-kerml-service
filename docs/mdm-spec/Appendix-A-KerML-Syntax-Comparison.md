# Appendix A Supplement: KerML Syntax Comparison

This document compares the MDM self-definition notation with KerML textual syntax, demonstrating how closely MDM concepts map to KerML constructs.

---

## Syntax Mapping Summary

| MDM Concept | MDM Notation | KerML Equivalent |
|-------------|--------------|------------------|
| Metaclass declaration | `metaclass Foo { }` | `class Foo { }` or `metaclass Foo { }` |
| Abstract | `abstract metaclass` | `abstract class` |
| Inheritance | `specializes Parent` | `:> Parent` or `specializes Parent` |
| Property | `name : Type [1]` | `feature name : Type[1];` |
| Derived property | `isDerived = true` | `derived feature name : Type;` |
| Constraint | `checkFoo : VERIFICATION` | `inv checkFoo { ... }` |
| Operation | `allSuperclasses() : MetaClass[0..*]` | `function allSuperclasses { return : MetaClass[0..*]; }` |
| Association | `association Foo { sourceEnd...; targetEnd... }` | `assoc struct Foo { end source...; end target...; }` |
| Multiplicity | `[0..*]` | `[0..*]` |
| Composite | `aggregation = COMPOSITE` | `composite feature` |
| Documentation | `description = "..."` | `doc /* ... */` |

---

## Side-by-Side Examples

### MetaClass Definition

**MDM Notation (Appendix A):**
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
    }

    operations {
        allSuperclasses() : MetaClass [0..*]
            body = "superclasses->closure(superclasses)"
            bodyLanguage = OCL
            isQuery = true
    }
}
```

**KerML-Like Syntax:**
```kerml
metaclass MetaClass {
    doc /* The root metaclass for defining types in the metamodel. */

    feature name : String[1];
        doc /* Unique identifier for this metaclass */

    feature isAbstract : Boolean[1] default = false;
        doc /* If true, cannot be directly instantiated */

    feature description : String[0..1];
        doc /* Human-readable documentation */

    inv checkNameNotEmpty { name.size() > 0 }

    function allSuperclasses {
        doc /* All direct and indirect superclasses */
        return : MetaClass[0..*] = superclasses->closure(superclasses);
    }
}
```

---

### MetaProperty Definition

**MDM Notation:**
```
metaclass MetaProperty {
    attributes {
        name : String [1]
        type : String [1]
        lowerBound : Integer [1] = 1
        upperBound : UnlimitedNatural [1] = 1
        isDerived : Boolean [1] = false
        isReadOnly : Boolean [1] = false
        defaultValue : String [0..1]
    }

    constraints {
        checkValidMultiplicity : VERIFICATION
            expression = "lowerBound >= 0 and (upperBound = -1 or upperBound >= lowerBound)"
    }

    operations {
        isMultivalued() : Boolean
            body = "upperBound = -1 or upperBound > 1"
    }
}
```

**KerML-Like Syntax:**
```kerml
metaclass MetaProperty {
    doc /* Defines a primitive-typed attribute of a MetaClass. */

    feature name : String[1];
    feature type : String[1];
    feature lowerBound : Integer[1] default = 1;
    feature upperBound : UnlimitedNatural[1] default = 1;
    derived feature isDerived : Boolean[1] default = false;
    feature isReadOnly : Boolean[1] default = false;
    feature defaultValue : String[0..1];

    inv checkValidMultiplicity {
        lowerBound >= 0 and (upperBound = -1 or upperBound >= lowerBound)
    }

    function isMultivalued {
        return : Boolean[1] = upperBound = -1 or upperBound > 1;
    }
}
```

---

### Association Definition

**MDM Notation:**
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
}
```

**KerML-Like Syntax:**
```kerml
assoc struct Inheritance {
    doc /* Inheritance relationship between metaclasses */

    end feature subclass : MetaClass[0..*];
    end feature superclasses : MetaClass[0..*];
        doc /* Parent metaclasses from which this metaclass inherits */
}

assoc struct ClassAttributes {
    doc /* Ownership of attributes by a metaclass */

    end composite feature owningClass : MetaClass[1];
    end feature attributes : MetaProperty[0..*] ordered;
}
```

---

### Derived Property with Constraint

**MDM Notation:**
```
metaclass Element {
    attributes {
        name : String [0..1]
            isDerived = true
            derivationConstraint = "deriveElementName"
    }

    constraints {
        deriveElementName : DERIVATION
            expression = "self.effectiveName()"
    }
}
```

**KerML-Like Syntax:**
```kerml
class Element {
    derived feature name : String[0..1] = effectiveName();
}
```

> Note: KerML collapses the derivation constraint directly into the feature's value expression.

---

### Enumeration

**MDM Notation:**
```
Enumeration AggregationKind {
    NONE        -- No ownership relationship
    SHARED      -- Shared ownership (reference)
    COMPOSITE   -- Exclusive ownership (containment)
}
```

**KerML-Like Syntax:**
```kerml
enum def AggregationKind {
    doc /* Defines ownership semantics on association ends */

    enum NONE;
        doc /* No ownership relationship */
    enum SHARED;
        doc /* Shared ownership (reference) */
    enum COMPOSITE;
        doc /* Exclusive ownership (containment) */
}
```

> Note: KerML uses `enum def` and `enum` for enumeration literals.

---

### SemanticBinding (No Direct KerML Equivalent)

**MDM Notation:**
```
metaclass SemanticBinding {
    attributes {
        name : String [1]
        baseConcept : String [1]
        bindingKind : BindingKind [1] = SPECIALIZES
    }
}
```

**KerML-Like Syntax (Hypothetical Extension):**
```kerml
metaclass SemanticBinding {
    feature name : String[1];
    feature baseConcept : String[1];
        doc /* Qualified name of library concept (e.g., "Base::Anything") */
    feature bindingKind : BindingKind[1] default = BindingKind::SPECIALIZES;
}
```

> Note: SemanticBinding is an MDM-specific concept for linking instances to library types. KerML achieves similar effects through explicit specialization relationships.

---

## Key Differences

### 1. Properties vs. Features
- **MDM**: Distinguishes `attributes` (primitives) from association ends
- **KerML**: Uses `feature` uniformly; ownership implied by `composite`

### 2. Derived Values
- **MDM**: Separate `derivationConstraint` reference + `MetaConstraint` definition
- **KerML**: Inline `= expression` directly on `derived feature`

### 3. Constraint Declarations
- **MDM**: Named constraints with explicit `type` (DERIVATION, VERIFICATION, etc.)
- **KerML**: `inv { }` for invariants; derivations are inline expressions

### 4. Association Navigability
- **MDM**: Explicit `isNavigable` flag on each end
- **KerML**: Navigability is typically determined by where the feature is defined

### 5. Documentation
- **MDM**: `description` attribute as a property
- **KerML**: `doc /* */` as inline documentation element

### 6. Default Values
- **MDM**: `defaultValue : String` (expression as string)
- **KerML**: `default = <expr>` or `default := <expr>` (typed expression)

---

## Potential Notation Choices

Given the close mapping, the MDM specification could adopt either:

### Option 1: MDM-Specific Notation (Current)
- Clear separation of MDM concepts
- Explicit about meta-level concerns
- Not dependent on KerML syntax evolution

### Option 2: KerML-Aligned Notation
- Familiar to KerML users
- Demonstrates that MDM is "self-hosting"
- May blur lines between M1 and M2 concepts

### Option 3: Hybrid Notation
- Use KerML syntax for structure (`class`, `feature`, `:>`)
- Use MDM-specific keywords for meta-concerns (`derivationConstraint`, `VERIFICATION`)

---

## Recommendation

The MDM specification should use a **KerML-aligned notation** where possible, with explicit extensions for MDM-specific concepts. This approach:

1. Reduces cognitive load for KerML users
2. Demonstrates expressiveness of the base language
3. Clearly identifies where MDM extends beyond KerML

**Proposed notation keywords:**

| Concept | Proposed Syntax |
|---------|-----------------|
| Metaclass | `metaclass Foo :> Bar { }` |
| Property | `feature name : Type[mult];` |
| Derived | `derived feature name : Type = expr;` |
| Constraint | `inv name { expr }` |
| Operation | `function name { in p : T; return : R = expr; }` |
| Association | `assoc struct Name { end feature ...; }` |
| Semantic Binding | `binding name specializes "Base::Concept" when { condition }` |

---

## Change History

| Version | Date | Description |
|---------|------|-------------|
| 0.1 | 2026-02-02 | Initial comparison |
