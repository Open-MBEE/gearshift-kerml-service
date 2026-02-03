# Meta-Definition Model (MDM) Specification Outline

## Document Purpose

This document outlines the structure for a specification of the Meta-Definition Model (MDM) — a standardized approach for declarative language definition. MDM provides a MOF-like metamodeling capability for defining modeling languages such as KerML and its extensions (SysML v2, domain-specific profiles).

---

## 1. Introduction

### 1.1 Scope
- Purpose of MDM as a metamodeling framework
- Relationship to MOF (Meta-Object Facility)
- Target use cases: KerML, SysML v2, and custom language extensions
- What is NOT in scope (implementation details, storage formats)

### 1.2 Normative References
- OMG MOF specification
- OMG OCL specification
- KerML specification

### 1.3 Terms and Definitions
- Metamodel, Metaclass, Meta-property, Meta-operation
- Constraint, Derivation, Association, Association End
- Multiplicity, Aggregation, Ownership

---

## 2. Overview

### 2.1 Architecture
- MDM as a self-describing metamodel (M2 layer in MOF terms)
- Relationship between MDM and target metamodels (e.g., KerML at M1)
- The role of constraints and OCL

### 2.2 Core Concepts
- **MetaClass** — Type definitions in the metamodel
- **MetaProperty** — Attributes of metaclasses (primitive-typed)
- **MetaAssociation** — Relationships between metaclasses
- **MetaOperation** — Behavioral specifications
- **MetaConstraint** — Validation and derivation rules
- **SemanticBinding** — Links to library concepts

### 2.3 Design Principles
- Declarative over imperative
- Separation of structure from semantics
- Explicit derivation and validation rules
- Support for language extension through superclassing

---

## 3. MetaClass

### 3.1 Definition
The fundamental unit of type definition in MDM. A MetaClass defines the structure and behavior of instances in the target metamodel.

### 3.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Identifier for the metaclass |
| isAbstract | Boolean | 1 | If true, cannot be directly instantiated |
| superclasses | MetaClass | 0..* | Parent metaclasses (inheritance) |
| attributes | MetaProperty | 0..* | Primitive-typed properties |
| constraints | MetaConstraint | 0..* | Validation and derivation rules |
| operations | MetaOperation | 0..* | Behavioral specifications |
| semanticBindings | SemanticBinding | 0..* | Links to library concepts |
| ownershipBinding | OwnershipBinding | 0..1 | Ownership intermediate configuration |
| description | String | 0..1 | Human-readable documentation |

> **Note:** `superclasses` is a reference to MetaClass instances, establishing a proper relationship in the metamodel (not a string identifier).

### 3.3 Association Ends
Association ends are NOT declared within MetaClass but rather through MetaAssociation. This separation ensures:
- Clean distinction between attributes (primitives) and relationships (references)
- Bidirectional relationships can be properly modeled
- Navigability can be explicitly controlled

### 3.4 Inheritance Semantics
- Properties, operations, and constraints are inherited from superclasses
- Multiple inheritance is supported
- Property redefinition and subsetting applies

---

## 4. MetaProperty

### 4.1 Definition
A MetaProperty represents an attribute of a MetaClass. Properties are restricted to primitive types; non-primitive typed relationships are modeled as MetaAssociationEnds.

### 4.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Property identifier |
| type | PrimitiveType | 1 | Data type (String, Boolean, Integer, Real, UnlimitedNatural) |
| lowerBound | Integer | 1 | Minimum cardinality (default: 1) |
| upperBound | UnlimitedNatural | 1 | Maximum cardinality (-1 = unlimited, default: 1) |
| isDerived | Boolean | 1 | If true, value is computed |
| derivationConstraint | MetaConstraint | 0..1 | Constraint that computes derived value |
| isReadOnly | Boolean | 1 | If true, cannot be modified after creation |
| isOrdered | Boolean | 1 | If true, collection maintains order |
| isUnique | Boolean | 1 | If true, collection has no duplicates |
| isUnion | Boolean | 1 | If true, value is union of subsetted properties |
| defaultValue | String | 0..1 | Default value expression |
| redefines | MetaProperty | 0..* | Properties this property redefines |
| subsets | MetaProperty | 0..* | Properties this property subsets |
| description | String | 0..1 | Human-readable documentation |

> **Note:** `type` references a PrimitiveType, `derivationConstraint` references a MetaConstraint, and `redefines`/`subsets` reference MetaProperty instances.

### 4.3 Primitive Types
- **String** — Character sequences
- **Boolean** — true/false values
- **Integer** — Whole numbers
- **Real** — Floating-point numbers
- **UnlimitedNatural** — Non-negative integers or unlimited (*)

### 4.4 Derived Properties
A derived property has no storage; its value is computed by a derivation constraint. The constraint is referenced by `derivationConstraint` and must have type DERIVATION.

---

## 5. MetaAssociation

### 5.1 Definition
A MetaAssociation defines a relationship between two metaclasses. Each association has exactly two ends.

### 5.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Association identifier |
| sourceEnd | MetaAssociationEnd | 1 | First end of the association |
| targetEnd | MetaAssociationEnd | 1 | Second end of the association |
| isDerived | Boolean | 1 | If true, relationship is computed |
| description | String | 0..1 | Human-readable documentation |

### 5.3 Association Semantics
- Associations may be unidirectional (one navigable end) or bidirectional (both navigable)
- Derived associations are computed from constraints
- Aggregation semantics are specified on association ends

---

## 6. MetaAssociationEnd

### 6.1 Definition
A MetaAssociationEnd represents one end of a MetaAssociation, specifying the type, multiplicity, and navigability of that end.

### 6.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | End identifier (property name from source perspective) |
| type | MetaClass | 1 | Target metaclass of this end |
| lowerBound | Integer | 1 | Minimum cardinality |
| upperBound | UnlimitedNatural | 1 | Maximum cardinality |
| isNavigable | Boolean | 1 | If true, can be traversed from opposite end |
| aggregation | AggregationKind | 1 | Ownership semantics |
| isOrdered | Boolean | 1 | Collection maintains order |
| isUnique | Boolean | 1 | Collection has no duplicates |
| isDerived | Boolean | 1 | Value is computed |
| isUnion | Boolean | 1 | Value is union of subsetted ends |
| derivationConstraint | MetaConstraint | 0..1 | Constraint that computes derived value |
| redefines | MetaAssociationEnd | 0..* | Ends this end redefines |
| subsets | MetaAssociationEnd | 0..* | Ends this end subsets |
| description | String | 0..1 | Human-readable documentation |

> **Note:** `type` references a MetaClass, not a string identifier.

### 6.3 AggregationKind
- **NONE** — No ownership relationship
- **SHARED** — Shared ownership (reference)
- **COMPOSITE** — Exclusive ownership (containment)

---

## 7. MetaOperation

### 7.1 Definition
A MetaOperation defines a behavioral specification on a MetaClass — a query or transformation that can be invoked on instances.

### 7.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Operation identifier |
| returnType | TypeReference | 0..1 | Return type (MetaClass or PrimitiveType) |
| parameters | MetaParameter | 0..* | Input parameters |
| body | String | 0..1 | Implementation expression |
| bodyLanguage | BodyLanguage | 1 | Language of body expression |
| isAbstract | Boolean | 1 | If true, no body provided |
| isQuery | Boolean | 1 | If true, does not modify state |
| preconditions | MetaConstraint | 0..* | Constraints that must hold before invocation |
| description | String | 0..1 | Human-readable documentation |

### 7.3 MetaParameter

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Parameter identifier |
| type | TypeReference | 1 | Parameter type |
| lowerBound | Integer | 1 | Minimum cardinality |
| upperBound | UnlimitedNatural | 1 | Maximum cardinality |
| defaultValue | String | 0..1 | Default value expression |

### 7.4 BodyLanguage
- **OCL** — Object Constraint Language expressions
- **PROPERTY_REF** — Simple property reference
- **GQL** — Graph Query Language (extension)

---

## 8. MetaConstraint

### 8.1 Definition
A MetaConstraint defines a rule that applies to instances of a MetaClass. Constraints may validate invariants, compute derived values, or establish implicit relationships.

### 8.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Constraint identifier |
| type | ConstraintType | 1 | Category of constraint |
| expression | String | 1 | Constraint body (typically OCL) |
| language | String | 1 | Expression language (default: "OCL") |
| description | String | 0..1 | Human-readable documentation |

### 8.3 ConstraintType

| Type | Description | Naming Convention |
|------|-------------|-------------------|
| DERIVATION | Computes derived property values | `derive<Class><Property>` |
| VERIFICATION | Validates invariants | `check<Property>` or `verify<Rule>` |
| NON_NAVIGABLE_END | Computes non-navigable association ends | `compute<Class><Property>` |
| IMPLICIT_BINDING_CONNECTOR | Creates implicit BindingConnector relationships | Domain-specific |
| IMPLICIT_REDEFINITION | Creates implicit Redefinition relationships | Domain-specific |
| IMPLICIT_TYPE_FEATURING | Creates implicit TypeFeaturing relationships | Domain-specific |

### 8.4 Constraint Semantics
- **DERIVATION** constraints are evaluated lazily when the derived property is accessed
- **VERIFICATION** constraints are checked during validation phases
- **Implicit relationship** constraints create derived model elements

---

## 9. SemanticBinding

### 9.1 Definition
A SemanticBinding links instances of a MetaClass to concepts in a base library, enabling semantic interpretation beyond structural typing.

### 9.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| name | String | 1 | Binding identifier |
| baseConcept | QualifiedName | 1 | Target library concept (e.g., "Base::Anything") |
| bindingKind | BindingKind | 1 | Type of relationship created |
| condition | BindingCondition | 0..1 | When binding applies |

### 9.3 BindingKind
- **SPECIALIZES** — Instance specializes the library concept
- **SUBSETS** — Instance subsets the library concept

### 9.4 BindingCondition
Conditions control when a semantic binding applies:

| Condition | Description |
|-----------|-------------|
| Default | Always applies |
| TypedBy(metaclass) | Element is typed by specific metaclass |
| OwningTypeIs(metaclass) | Owning type is specific metaclass |
| PropertyEquals(property, value) | Property has specific value |
| CollectionNotEmpty(property) | Collection is non-empty |
| And, Or, Not | Logical composition of conditions |

---

## 10. OwnershipBinding

### 10.1 Definition
An OwnershipBinding marks a MetaClass as an "ownership intermediate" — a class that sits between an owner and owned element in the ownership hierarchy (e.g., Membership in KerML).

### 10.2 Properties

| Property | Type | Multiplicity | Description |
|----------|------|--------------|-------------|
| ownedElementEnd | MetaAssociationEnd | 1 | End pointing to owned element |
| ownerEnd | MetaAssociationEnd | 1 | End pointing back to owner |

### 10.3 Ownership Semantics
- The framework identifies the most specific intermediate based on inheritance
- Enables proper containment hierarchies with intermediate wrappers

---

## 11. Well-Formedness Rules

### 11.1 MetaClass Rules
- `name` must be unique within the metamodel
- `superclasses` must not contain cycles
- Abstract classes cannot have ownershipBinding

### 11.2 MetaProperty Rules
- `type` must be a valid PrimitiveType
- If `isDerived`, then `derivationConstraint` should reference a valid DERIVATION constraint
- `redefines` targets must be properties of superclasses
- `subsets` targets must have compatible types

### 11.3 MetaAssociation Rules
- `sourceEnd.type` and `targetEnd.type` must be valid metaclasses
- At least one end must be navigable
- Composite aggregation must have upperBound of 1 on the non-composite end

### 11.4 MetaConstraint Rules
- `expression` must be valid in the specified `language`
- DERIVATION constraints must return a value compatible with the derived property type
- VERIFICATION constraints must return Boolean

---

## 12. OCL Support (Informative)

### 12.1 Required OCL Features
The following OCL features are required for MDM constraint evaluation:

**Navigation:**
- Property access: `self.property`
- Association navigation: `self.associationEnd`
- Qualified navigation: `self.property->collect(p | p.subproperty)`

**Collection Operations:**
- `select`, `reject`, `collect`, `forAll`, `exists`
- `isEmpty`, `notEmpty`, `size`, `includes`, `excludes`
- `union`, `intersection`, `including`, `excluding`
- `flatten`, `asSet`, `asOrderedSet`, `asBag`, `asSequence`
- `closure` — Transitive closure over a navigation

**Type Operations:**
- `oclIsKindOf(type)`, `oclIsTypeOf(type)`
- `oclAsType(type)`

**Arithmetic and Logic:**
- Standard arithmetic: `+`, `-`, `*`, `/`, `mod`, `div`
- Comparison: `=`, `<>`, `<`, `<=`, `>`, `>=`
- Logic: `and`, `or`, `xor`, `not`, `implies`

---

## 13. Extensibility

### 13.1 Language Extension
New modeling languages are defined by creating MetaClasses that inherit from base language metaclasses.

### 13.2 Constraint Extension
Custom constraint types may be added for domain-specific implicit relationships.

### 13.3 Profile Mechanism
(To be defined — mechanism for constrained extensions without new metaclasses)

---

## Appendices

### Appendix A: MDM Self-Definition
The MDM metaclasses defined using MDM notation (bootstrap definition).

### Appendix B: KerML Metamodel (Normative Example)
Complete KerML metamodel definition using MDM.

### Appendix C: Mapping to MOF
Formal mapping between MDM concepts and MOF concepts.

### Appendix D: Serialization Formats (Informative)
Non-normative guidance on JSON and XMI serialization.

---

## Change History

| Version | Date | Description |
|---------|------|-------------|
| 0.1 | 2026-02-02 | Initial outline |