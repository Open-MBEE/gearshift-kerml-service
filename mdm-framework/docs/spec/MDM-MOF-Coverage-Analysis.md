# MDM MOF Coverage Analysis

This document analyzes which MOF (Meta-Object Facility) concepts are covered by MDM and identifies gaps that may need to be addressed for a complete metamodeling framework.

---

## 1. MOF Core Concepts Mapping

### 1.1 Fully Covered

| MOF Concept | MDM Equivalent | Notes |
|-------------|----------------|-------|
| Class | MetaClass | Full support with abstract, inheritance |
| Property | MetaProperty | Primitive-typed attributes |
| Association | MetaAssociation | Binary associations with two ends |
| AssociationEnd | MetaAssociationEnd | Multiplicity, navigability, aggregation |
| Operation | MetaOperation | With parameters, return type, body |
| Parameter | MetaParameter | Typed with multiplicity |
| Constraint | MetaConstraint | OCL expressions with type taxonomy |
| AggregationKind | AggregationKind | NONE, SHARED, COMPOSITE |

### 1.2 Partially Covered

| MOF Concept | MDM Status | Gap |
|-------------|------------|-----|
| Package | Code-level only | No MetaPackage metaclass |
| Namespace | Code-level only | No MetaNamespace metaclass |
| Import | Not at meta-level | Exists in KerML but not MDM |
| DataType | String reference | No distinct MetaDataType |
| PrimitiveType | String reference | No MetaPrimitiveType |
| Enumeration | Kotlin enum | No MetaEnumeration |

### 1.3 Not Covered (MOF concepts absent from MDM)

| MOF Concept | Description | Impact |
|-------------|-------------|--------|
| Profile | Extension mechanism | Cannot extend metamodel without modification |
| Stereotype | Tagged extension | No lightweight extensibility |
| Tag | Key-value metadata | No arbitrary metadata attachment |
| TemplateParameter | Generic types | No parameterized metaclasses |
| UnionType | Type alternatives | `isUnion` flag exists but no formal union |
| Reflection | Meta-metamodel access | No `eClass()` equivalent |

---

## 2. Proposed MDM Meta-Metamodel

The following diagram shows the complete MDM meta-metamodel, including both existing concepts and proposed additions (marked with *).

```
                              ┌──────────────────┐
                              │  MetaElement*    │ (abstract root)
                              └────────┬─────────┘
                                       │
          ┌────────────────────────────┼────────────────────────────┐
          │                            │                            │
          ▼                            ▼                            ▼
┌─────────────────┐          ┌─────────────────┐          ┌─────────────────┐
│  MetaPackage*   │          │   MetaType*     │          │ MetaConstraint  │
├─────────────────┤          │   (abstract)    │          ├─────────────────┤
│ name            │          └────────┬────────┘          │ name            │
│ uri*            │                   │                   │ type            │
│ description     │     ┌─────────────┼─────────────┐     │ expression      │
└─────────────────┘     │             │             │     └─────────────────┘
        │               ▼             ▼             ▼
        │     ┌──────────────┐ ┌───────────┐ ┌──────────────┐
        │     │  MetaClass   │ │MetaDataType*│ │MetaEnumeration*│
        │     ├──────────────┤ ├───────────┤ ├──────────────┤
        │     │ isAbstract   │ │ name      │ │ name         │
        │     │ superclasses │ │           │ │ literals*    │
        │     │ attributes   │ └───────────┘ └──────────────┘
        │     │ operations   │       │
        │     │ constraints  │       │ includes
        │     └──────────────┘       ▼
        │                    ┌───────────────┐
        │                    │MetaPrimitiveType*│
        │                    ├───────────────┤
        │ contains           │ name          │
        ▼                    │ (String, Boolean, Integer, Real, UnlimitedNatural)
┌─────────────────┐          └───────────────┘
│ MetaClass       │
│ MetaDataType*   │
│ MetaEnumeration*│
│ MetaAssociation │
└─────────────────┘
```

---

## 3. Detailed Gap Analysis

### 3.1 MetaPackage / MetaNamespace

**Current state:** KerML metamodel is organized into three packages (root, core, kernel) but this is purely code organization in `KerMLMetamodelLoader`.

**Gap:** No way to:
- Define package dependencies in the metamodel
- Control visibility across package boundaries
- Model package imports

**Proposed addition:**

```
metaclass MetaPackage {
    feature name : String[1];
    feature uri : String[0..1];
        doc /* Unique identifier for this package */
    feature description : String[0..1];
}

assoc struct PackageContents {
    end composite feature owningPackage : MetaPackage[0..1];
    end feature ownedElements : MetaElement[0..*];
}

assoc struct PackageImport {
    end feature importingPackage : MetaPackage[0..*];
    end feature importedPackage : MetaPackage[0..*];
}
```

### 3.2 MetaDataType / MetaPrimitiveType

**Current state:** Property types are strings ("String", "Boolean", etc.) with no validation.

**Gap:** No way to:
- Distinguish primitive types from class references
- Define custom primitive types
- Validate type references

**Proposed addition:**

```
abstract metaclass MetaType {
    feature name : String[1];
}

metaclass MetaDataType :> MetaType {
    doc /* A type whose instances are values (not objects) */
}

metaclass MetaPrimitiveType :> MetaDataType {
    doc /* Built-in primitive types */
}

-- Pre-defined instances:
MetaPrimitiveType String
MetaPrimitiveType Boolean
MetaPrimitiveType Integer
MetaPrimitiveType Real
MetaPrimitiveType UnlimitedNatural
```

With this, MetaProperty.type becomes:
```
feature type : MetaType[1];  -- was String
```

### 3.3 MetaEnumeration

**Current state:** Enumerations (AggregationKind, ConstraintType, VisibilityKind) are Kotlin enums, not metamodel elements.

**Gap:** No way to:
- Define enumerations in metamodel definitions
- Reference enumeration literals in constraints
- Extend enumerations

**Proposed addition:**

```
metaclass MetaEnumeration :> MetaDataType {
    feature literals : MetaEnumerationLiteral[1..*] ordered;
}

metaclass MetaEnumerationLiteral {
    feature name : String[1];
    feature value : Integer[0..1];
        doc /* Optional ordinal value */
    feature description : String[0..1];
}

assoc struct EnumerationLiterals {
    end composite feature owningEnumeration : MetaEnumeration[1];
    end feature ownedLiterals : MetaEnumerationLiteral[1..*] ordered;
}
```

Example usage:
```
enumeration AggregationKind {
    literal NONE;
    literal SHARED;
    literal COMPOSITE;
}
```

### 3.4 Type References

**Current state:** Type references are untyped strings throughout.

**Gap:** No distinction between:
- Primitive type reference ("String")
- Enumeration reference ("AggregationKind")
- Metaclass reference ("MetaClass")
- Collection type ("MetaClass[0..*]")

**Proposed addition:**

```
abstract metaclass TypeReference {
    doc /* A reference to a type, with optional multiplicity */
}

metaclass SimpleTypeRef :> TypeReference {
    feature referencedType : MetaType[1];
}

metaclass CollectionTypeRef :> TypeReference {
    feature elementType : MetaType[1];
    feature lowerBound : Integer[1] default = 0;
    feature upperBound : UnlimitedNatural[1] default = -1;
    feature isOrdered : Boolean[1] default = false;
    feature isUnique : Boolean[1] default = true;
}
```

---

## 4. MDM Extensions Beyond MOF

MDM includes concepts not found in standard MOF:

### 4.1 SemanticBinding

Links metaclass instances to base library concepts. No MOF equivalent.

```
metaclass SemanticBinding {
    feature baseConcept : String[1];
    feature bindingKind : BindingKind[1];
    composite feature condition : BindingCondition[0..1];
}
```

**Rationale:** Enables declarative specification of implied relationships to standard library types, separating semantic intent from structural inheritance.

### 4.2 OwnershipBinding

Marks a metaclass as an ownership intermediate.

```
metaclass OwnershipBinding {
    feature ownedElementEnd : String[1];
    feature ownerEnd : String[1];
}
```

**Rationale:** Supports ownership patterns where intermediates (like Membership) sit between owner and owned element.

### 4.3 Constraint Type Taxonomy

Rich categorization of constraints beyond MOF's simple invariants.

| Type | Purpose |
|------|---------|
| DERIVATION | Computes derived property values |
| VERIFICATION | Validates invariants |
| NON_NAVIGABLE_END | Computes non-navigable association ends |
| IMPLICIT_BINDING_CONNECTOR | Creates BindingConnector relationships |
| IMPLICIT_REDEFINITION | Creates Redefinition relationships |
| IMPLICIT_TYPE_FEATURING | Creates TypeFeaturing relationships |

**Rationale:** Explicit categorization enables targeted constraint evaluation and better tool support.

---

## 5. Recommended Additions

### Priority 1: Essential for Metamodel Organization

1. **MetaPackage** — Namespace organization
2. **MetaEnumeration** — First-class enumeration definitions
3. **MetaPrimitiveType** — Formalized primitive types

### Priority 2: Type Safety

4. **TypeReference hierarchy** — Typed references instead of strings
5. **Type validation** — Registry validates type references

### Priority 3: Extensibility

6. **MetaProfile** — Extension mechanism for constrained variants
7. **MetaStereotype** — Lightweight metaclass extension

### Priority 4: Reflexivity

8. **MetaElement** — Abstract root for all meta-elements
9. **getMetaClass()** — Runtime access to metaclass

---

## 6. KerML Constructs Used by MDM

The following table identifies which KerML constructs are used to express MDM concepts:

| MDM Concept | KerML Construct | KerML Keywords |
|-------------|-----------------|----------------|
| MetaClass | Class | `class`, `abstract class`, `metaclass` |
| Inheritance | Specialization | `:>`, `specializes` |
| MetaProperty | Feature | `feature`, `derived feature` |
| Multiplicity | Multiplicity | `[min..max]` |
| MetaAssociation | Association | `assoc`, `assoc struct` |
| AssociationEnd | Feature + end | `end feature` |
| Aggregation | composite | `composite feature` |
| MetaOperation | Function | `function`, `return` |
| MetaParameter | Feature (in) | `in`, `out`, `inout` |
| MetaConstraint | Invariant | `inv { }` |
| Documentation | Comment | `doc /* */` |

### 6.1 KerML Constructs NOT Used by MDM

| KerML Construct | Why Not Used |
|-----------------|--------------|
| Behavior | MDM is structural, not behavioral |
| Interaction | Not needed for metamodeling |
| Connector | Associations suffice |
| ItemFlow | Not needed |
| Expression (most) | Only OCL in constraints |
| FeatureChaining | Not needed at meta-level |
| Conjugation | Not needed |
| Differencing/Intersecting/Unioning | Not needed |

---

## 7. Summary

**MDM Coverage:**
- ✅ Core MOF structural concepts (Class, Property, Association, Operation)
- ✅ Constraint mechanisms (OCL with rich taxonomy)
- ✅ Aggregation and ownership patterns
- ⚠️ Package/Namespace (code-level only)
- ⚠️ Type references (strings, not typed)
- ❌ Enumeration as meta-concept
- ❌ Profile/Stereotype mechanism
- ❌ Reflection/Meta-metamodel access

**MDM Innovations:**
- SemanticBinding for library concept linkage
- OwnershipBinding for intermediate patterns
- Constraint type taxonomy

**Conclusion:** MDM covers the essential MOF subset needed to define KerML. The main gaps are organizational (packages) and type-safety (typed references). These could be added without disrupting the current design.

---

## Change History

| Version | Date | Description |
|---------|------|-------------|
| 0.1 | 2026-02-02 | Initial analysis |
