# 8.4 Semantics

## 8.4.1 Semantics Overview

A KerML model is intended to *represent* a system being modeled. The model is *interpreted* to make statements about the
modeled system. The model may describe an existing system, in which case, if the model is correct, the statements it is
interpreted to make about the system should all be true. A model may also be used to specify an imagined or planned
system, in which case the statements the model is interpreted to make should be true for any system that is properly
constructed and operated according to the model.

The *semantics* of KerML specify how a KerML model is to be interpreted. The semantics are defined in terms of the
abstract syntax representation of the model, and only for models which are *valid* relative to the structure and
constraints specified for the KerML abstract syntax (see 8.3). As further specified in this subclause, models expressed
in KerML are given semantics by implicitly reusing elements from the semantic models in the Kernel Model Library (see
Clause 9). These library models represent conditions on the structure and behavior of the system being modeled, which
are further augmented in a user model as appropriate.

A formal specification of semantics allows models to be interpreted consistently. In particular, all KerML models extend
library models expressed in KerML itself, understandable by KerML modelers. These library models can then be ultimately
reduced to a small, core subset of KerML, which is grounded in mathematical logic. The goal is to provide uniform model
interpretation, which improves communication between everyone involved in modeling, including modelers and tool
builders.

KerML semantics are specified by a combination of mathematics and model libraries, as illustrated in Figure 41. The left
side of this diagram shows the abstract syntax packages corresponding to the three layers of KerML (see 6.1). The right
side shows the corresponding semantic layering.

1. The **Root Layer** defines the syntactic foundation of KerML and, as such, does not have a semantic interpretation
   relative to the modeled system.
2. The **Core Layer** is grounded in mathematical semantics, supported by the `Base` package from the Kernel Model
   Library (see 9.2.2). Subclause 8.4.3 specifies the semantics of the Core layer.
3. The **Kernel Layer** is given semantics fully through its relationship to the Model Library (see Clause 9). Subclause
   8.4.4 specifies the semantics of the Kernel layer.

![Figure 41. KerML Semantic Layers](figure_41.png)

*Figure 41. KerML Semantic Layers*

## 8.4.2 Semantic Constraints and Implied Relationships

As described in 8.4.1, KerML semantics are specified by a combination of a mathematical interpretation of the Core layer
and a set of required relationships between Core and Kernel model elements and elements of the Kernel Semantic Library (
see 9.2). The latter requirements are formalized by *semantic constraints* included in the KerML abstract syntax (see
also 8.3.1 on the various kinds of constraints in the abstract syntax). Additionally, other semantic constraints require
relationships between elements within a user model necessary for the model to be semantically well formed.

Specifically, there are four categories of semantic constraints, each dealing with a different kind of relationship:

1. **Specialization constraints.** These constraints require that `Type` elements of a certain kind directly or
   indirectly specialize some specific `baseType` from the Kernel Semantic Library. They are the fundamental means for
   providing semantics to abstract syntax elements in the Kernel layer. Specialization constraints always have the word
   *Specialization* in their name. For example, `checkDataTypeSpecialization` requires that a `DataType` directly or
   indirectly specialize the Semantic Library DataType `Base::DataValue`.

2. **Redefinition constraints.** These constraints require that certain `Features` in a model have `Redefinition`
   relationships with certain other `Features` in the model. While `Redefinitions` are kinds of `Specializations`,
   redefinition constraints differ from the specialization constraints described above in that they are between two
   elements of a user model, rather than between an element of a user model and an element of a library model.
   Redefinition constraints always have the word *Redefinition* in their name. For example,
   `checkConnectorEndRedefinition` requires that the ends of a `Connector` redefine any ends of the `Types` that it
   specializes.

3. **Type-featuring constraints.** These constraints require that certain `Features` in a model have `TypeFeaturing`
   relationships with certain other `Types` in the model. They arise at points in a model in which the
   `OwningMembership` structure is different than the required `Featuring` relationship, so `FeatureMembership` cannot
   be used. Type-featuring constraints always have the words *TypeFeaturing* in their name. For example,
   `checkFeatureValueExpressionTypeFeaturing` requires that the `valueExpression` owned by a `FeatureValue`
   relationship (a kind of `OwningMembership`) have the same `featuringTypes` as the owning `featureWithValue` of the
   `FeatureValue`, rather than being featured by the `featureWithValue` itself (as would have been the case for a
   `FeatureMembership`).

4. **Binding-connector constraints.** These constraints require that `BindingConnectors` exist between certain
   `Features` in a model. The primary example of such a constraint is `checkFeatureValueBindingConnector`, which
   requires that the `featureWithValue` of a `FeatureValue` own a `BindingConnector` between itself and the `result`
   parameter of the `valueExpression` of the `FeatureValue`.

A KerML model parsed from the textual concrete syntax (see 8.2) or obtained through model interchange (see Clause 10)
will not necessarily meet the semantic constraints specified for the abstract syntax. In this case, a tool may insert
certain *implied* `Relationships` into the model in order to meet the semantic constraints. The overview subclauses for
the Core Semantics (see 8.4.3.1) and Kernel Semantics (see 8.4.4.1) include tables that define what implied
`Relationships` should be included to satisfy each semantic constraint when it would otherwise be violated. In all
cases, the semantics of a model are only defined if it meets all semantic and validation constraints (see 8.3.1).

When including implied `Relationships` for specialization constraints, it is possible that multiple such constraints may
apply to a single element. For example, a `Structure` is a kind of `Class`, which is a kind of `Classifier`, and there
are specialization constraints for all three of these metaclasses, with corresponding implied `Subclassification`
`Relationships`. However, simply including all three implied `Subclassifications` would be redundant, because the
`Subclassification` implied by the `checkStructureSpecialization` constraint will also automatically satisfy the
`checkClassSpecialization` and `checkClassifierSpecialization` constraints.

Therefore, in order to avoid redundant `Relationships`, a tool should observe the following rules when selecting which
`Specializations` to actually include for a certain `specificType`, out of the set of those implied by all
specialization constraints applicable to the `Type`:

1. If there is any `ownedSpecialization` or other implied `Specialization` whose `generalType` is a direct or indirect
   subtype of (but not the same as) the `generalType` of an implied `Specialization`, or if there is an
   `ownedSpecialization` with the same `generalType`, then that implied `Specialization` should *not* be included.

2. If there are two implied `Specializations` with the same `generalType`, then only one should be included.

> **Note:** The above rules do *not* apply to `Redefinitions` implied by redefinition constraints, because
`Redefinition` relationships have semantics beyond just basic `Specialization`.

## 8.4.3 Core Semantics

### 8.4.3.1 Core Semantics Overview

#### 8.4.3.1.1 Core Semantic Constraints

The Core semantics are primarily specified mathematically, but the Core metaclasses `Type`, `Classifier`, and `Feature`
also have certain semantic constraints (see 8.4.2). Subclause 8.4.3.1.2 describes the general mathematical framework for
Core semantics, with specific rules for `Types`, `Classifiers` and `Features` given in 8.4.3.2, 8.4.3.3, and 8.4.3.4,
respectively. The following summarizes the corresponding semantic constraints.

The `checkTypeSpecialization` and `checkFeatureSpecialization` constraints are actually already implied by the
mathematical semantics for `Types` and `Features`, but they are included in the abstract syntax so that they can also be
reflected syntactically in models by the implied `Relationships` shown in Table 8. In addition, Table 9 lists the
implied `Relationships` for semantic constraints on the Core metaclass `Feature` that actually support the semantics of
various Kernel-layer constructs, as further described in the Kernel Semantics (8.4.4) subclauses referenced in the table
entries for those constraints. In all cases, the `source` and `owningRelatedElement` of the `Relationship` is the
`Element` being constrained, with the `target` being as given in the last column of the table.

### Table 8. Core Semantics Implied Relationships

| Semantic Constraint          | Implied Relationship | Target                           |
|------------------------------|----------------------|----------------------------------|
| `checkTypeSpecialization`    | `Subclassification`  | `Base::Anything` (see 9.2.2.2.1) |
| `checkFeatureSpecialization` | `Subsetting`         | `Base::things` (see 9.2.2.2.7)   |

**Notes:**

1. The `checkTypeSpecialization` constraint applies to all `Types`, but the `Subclassification` `Relationship` is only
   implied for `Classifiers` (see 8.4.3.3).

2. Satisfaction of the `checkFeatureSpecialization` constraint implies satisfaction of the `checkTypeSpecialization`
   constraint (see 8.4.3.4).

### Table 9. Core Semantics Implied Relationships Supporting Kernel Semantics

| Semantic Constraint                                       | Implied Relationship | Target                                                                                                                                                                     |
|-----------------------------------------------------------|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `checkFeatureDataValueSpecialization`                     | `Subsetting`         | `Base::dataValues` (see 9.2.2.2.3). Supports Data Types Semantics (see 8.4.4.2)                                                                                            |
| `checkFeatureOccurrenceSpecialization`                    | `Subsetting`         | `Occurrences::occurrences` (see 9.2.4.2.14). Supports Classes Semantics (see 8.4.4.3)                                                                                      |
| `checkFeatureSuboccurrenceSpecialization`                 | `Subsetting`         | `Occurrences::Occurrence::suboccurrences` (see 9.2.4.2.13). Supports Classes Semantics (see 8.4.4.3)                                                                       |
| `checkFeatureFeatureMembershipTypeFeaturing`              | `TypeFeaturing`      | A `Type` for which `isFeaturingType` is true on the `Feature`. Supports Classes Semantics (see 8.4.4.3). (See Note 1)                                                      |
| `checkFeatureObjectSpecialization`                        | `Subsetting`         | `Objects::objects` (see 9.2.5.2.8). Supports Structures Semantics (see 8.4.4.4)                                                                                            |
| `checkFeatureSubobjectSpecialization`                     | `Subsetting`         | `Objects::Object::subobjects` (see 9.2.5.2.7). Supports Structures Semantics (see 8.4.4.4)                                                                                 |
| `checkFeatureEndSpecialization`                           | `Subsetting`         | `Links::Link::participant` (see 9.2.3.2.3). Supports Associations and Connectors Semantics (see 8.4.4.5, 8.4.4.6)                                                          |
| `checkFeatureEndRedefinition`                             | `Redefinition`       | The end of the supertype at the same position as the owning `Type` of the `Feature`. Supports Associations and Connectors Semantics (see 8.4.4.5.1)                        |
| `checkFeatureCrossingSpecialization`                      | `CrossSubsetting`    | A cross `Feature` to be redefined and another `Feature` representing types of the owning end and other end. Supports Associations and Connectors Semantics (see 8.4.4.5.1) |
| `checkFeatureOwnedCrossFeatureRedefinitionSpecialization` | `Subsetting`         | The `ownedCrossFeature` of the end being redefined. Supports Associations and Connectors Semantics (see 8.4.4.5.1)                                                         |
| `checkFeatureOwnedCrossFeatureTypeFeaturing`              | `TypeFeaturing`      | The `types` of the owning end and other end. Supports Associations and Connectors Semantics (see 8.4.4.5.1). (See Note 3)                                                  |
| `checkFeatureStepSpecialization`                          | `Subsetting`         | `Performances::performances` (see 9.2.6.2.16). Supports Behaviors and Steps Semantics (see 8.4.4.7, 8.4.4.8)                                                               |
| `checkFeatureSubperformanceSpecialization`                | `Subsetting`         | `Performances::Performance::subperformances` (see 9.2.6.2.15). Supports Behaviors and Steps Semantics (see 8.4.4.7, 8.4.4.8)                                               |
| `checkFeatureEnclosedPerformanceSpecialization`           | `Subsetting`         | `Performances::Performance::enclosedPerformances` (see 9.2.6.2.15). Supports Behaviors and Steps Semantics (see 8.4.4.7, 8.4.4.8)                                          |
| `checkFeatureOwnedResultExpressionRedefinition`           | `Redefinition`       | `result` parameter of supertype `Expression`. Supports Functions and Expressions Semantics (see 8.4.4.9)                                                                   |
| `checkFeatureFlowFeatureRedefinition`                     | `Redefinition`       | `Transfer::source::sourceOutput` or `Transfer::target::targetInput` (see 9.2.7.2.9). Supports Flows Semantics (see 8.4.4.10.2)                                             |
| `checkFeatureValuationSpecialization`                     | `Subsetting`         | The `result` of the value `Expression` of an owned `FeatureValue` of a `Feature`. Supports Feature Values Semantics (see 8.4.4.11)                                         |

**Notes:**

1. For the `checkFeatureFeatureMembershipTypeFeaturing` constraint, if the `Feature` has `isVariable = false`, then the
   target `Type` is the `owningType` of the `Feature`. If the `Feature` has `isVariable = true` and the `owningType` is
   the base `Class` `Occurrences::Occurrence`, then the target is `Occurrences::Occurrence::snapshots` (see 9.2.4.2.13).
   Otherwise, the target `Type` shall be constructed so as to satisfy the constraint and shall be owned as an
   `ownedRelatedElement` of the implied `TypeFeaturing` relationship. For further details, see 8.4.4.3.

2. For the `checkFeatureCrossingSpecialization` constraint on an end `Feature`, the target feature chain shall consist
   of two `Features`. The first `Feature` is owned by the chain, is typed by the `featuringType` of the
   `ownedCrossFeature` of the end `Feature`, and is featured by the `owningType` of the end `Feature`. The second
   `Feature` is the `ownedCrossFeature` of the end `Feature`. For further details, see 8.4.4.5.1.

3. For the `checkFeatureOwnedCrossFeatureTypeFeaturing` constraint, if the `owningType` of the owning end `Feature` has
   exactly two `endFeatures`, then an `ownedCrossFeature` shall be featured by the `types` of the other end than its
   owning end `Feature`. If the `owningType` has more than two `endFeatures`, then the `ownedCrossFeature` shall be
   featured by a `Feature` representing a Cartesian product of the `types` of the other `endFeatures` of the
   `owningType` than the owning end `Feature` of the `ownedCrossFeature`. For further details, see 8.4.4.5.1.

#### 8.4.3.1.2 Core Semantics Mathematical Preliminaries

The mathematical specification of Core semantics uses a model-theoretic approach. Core mathematical semantics are
expressed in first order logic notation, extended as follows:

1. A conjunction specifying that multiple variables are members of the same set can be shortened to a comma-delimited
   series of variables followed by a single membership symbol (`s1, s2 ∈ S` is short for `s1 ∈ S ∧ s2 ∈ S`). Quantifiers
   can use this in variable declarations, rather than leaving it to the body of the statement before an implication (
   `∀tg, ts ∈ VT...` is short for `∀tg, ts tg ∈ VT ∧ ts ∈ VT ⇒ ...`).

2. Dots (`.`) appearing between metaproperty names have the same meaning as in OCL, including implicit
   collections [OCL].

3. Sets are identified in the usual set-builder notation, which specifies members of a set between curly braces (`{}`).
   The notation is extended with `#` before an opening brace to refer to the cardinality of a set.

Element names appearing in the mathematical semantics refer to the `Element` itself, rather than its instances, using
the same font conventions as given in 8.1.

The mathematical semantics use the following model-theoretic terms, explained in terms of this specification:

- **Vocabulary:** Model elements conforming to the KerML abstract syntax, with additional restrictions given in this
  subclause.
- **Universe:** All actual or potential things the vocabulary could possibly be about.
- **Interpretation:** The relationship between vocabulary and mathematical structures made of elements of the universe.

The above terms are mathematically defined below.

A **vocabulary** `V = (VT, VC, VF)` is a 3-tuple where:

- `VT` is a set of types (model elements classified by `Type` or its specializations, see 8.3.3.1).
- `VC ⊆ VT` is a set of classifiers (model elements classified by `Classifier` or its specializations, see 8.3.3.2),
  including at least `Base::Anything` from KerML Semantic Model Library (see 9.2.2).
- `VF ⊆ VT` is a set of features (model elements classified by `Feature` or its specializations, see 8.3.3.3), including
  at least `Base::things` from the KerML Semantic Model Library (see 9.2.2).
- `VT = VC ∪ VF`

An **interpretation** `I = (Δ, Σ, ·T)` for `V` is a 3-tuple where:

- `Δ` is a non-empty set (universe),
- `Σ = (P, <P)` is a non-empty set `P` with a strict partial ordering `<P` (marking set), and
- `·T` is an (interpretation) function relating elements of the vocabulary to sets of all non-empty tuples (sequences)
  of elements of the universe, with an element of the marking set in between each one for sequences of multiple
  elements. It has domain `VT` and co-domain that is the power set of `S`:

```
S = Δ¹ ∪ Δ×P×Δ ∪ Δ×P×Δ×P×Δ …
```

where `Δ¹` is the set of sets of size 1 covering all the elements of the universe (a unary Cartesian power).

The semantics of KerML are restrictions on the interpretation relationship, as given mathematically in this and
subsequent subclauses on the Core semantics. The phrase *result of interpreting* a model (vocabulary) element refers to
sequences paired with the element by `·T`, also called the *interpretation* of the model element, for short.

The (minimal interpretation) function `·minT` specializes `·T` to the subset of sequences that have no others in the
interpretation as tails, except when applied to `Anything`.

```
∀t ∈ Type, s1 ∈ S: s1 ∈ (t)minT ≡ s1 ∈ (t)T ∧ (t ≠ Anything ⇒ (∀s2 ∈ S: s2 ∈ (t)T ∧ s2 ≠ s1 ⇒ ¬tail(s2, s1)))
```

Functions and predicates for sequences are introduced below. Predicates prefixed with `form:` are defined in [fUML],
Clause 10 (Base Semantics).

- **length** is a function version of fUML's `sequence-length`:
  ```
  ∀s, n: n = length(s) ≡ (form:sequence-length s n)
  ```

- **at** is a function version of fUML's `in-position-count`:
  ```
  ∀x, s, n: x = at(s, n) ≡ (form:in-position-count s n x)
  ```

- **head** is true if the first sequence is the same as the second for some or all of the second starting at the
  beginning, otherwise is false:
  ```
  ∀s1, s2: head(s1, s2) ⇒ form:Sequence(s1) ∧ form:Sequence(s2)
  ∀s1, s2: head(s1, s2) ≡ (length(s1) ≤ length(s2)) ∧ (∀i ∈ Z⁺: i ≥ 1 ∧ i ≤ length(s1) ⇒ at(s1, i) = at(s2, i))
  ```

- **tail** is true if the first sequence is the same as the second for some or all of the second finishing at the end,
  otherwise is false:
  ```
  ∀s1, s2: tail(s1, s2) ⇒ form:Sequence(s1) ∧ form:Sequence(s2)
  ∀s1, s2: tail(s1, s2) ≡ (length(s1) ≤ length(s2)) ∧ (∀h, i ∈ Z⁺: (h = length(s2) − length(s1)) ∧ i > h ∧ i ≤ length(s2) ⇒ at(s1, i−h) = at(s2, i))
  ```

- **head-tail** is true if the first and second sequences are the head and tail of the third sequence, respectively,
  otherwise is false:
  ```
  ∀s1, s2: head-tail(s1, s2, s0) ⇒ form:Sequence(s1) ∧ form:Sequence(s2) ∧ form:Sequence(s0)
  ∀s1, s2: head-tail(s1, s2, s0) ≡ head(s1, s0) ∧ tail(s2, s0)
  ```

- **concat** is true if the first sequence has the second as head, the third as tail, and its length is the sum of the
  lengths of the other two, otherwise is false:
  ```
  ∀s0, s1, s2: concat(s0, s1, s2) ⇒ form:Sequence(s0) ∧ form:Sequence(s1) ∧ form:Sequence(s2)
  ∀s0, s1, s2: concat(s0, s1, s2) ≡ (length(s0) = length(s1) + length(s2)) ∧ head-tail(s1, s2, s0)
  ```

- **concat-around** is true if the first sequence has the second as head, the fourth as tail, and the third element in
  between:
  ```
  ∀s0, s1, p, s2: concat-around(s0, s1, p, s2) ⇒ form:Sequence(s0) ∧ form:Sequence(s1) ∧ form:Sequence(s2)
  ∀s0, s1, p, s2: concat-around(s0, s1, p, s2) ≡ (length(s0) = length(s1) + length(s2) + 1) ∧ head-tail(s1, s2, s0) ∧ at(p, length(s1) + 1)
  ```

- **reverse** is true if the sequences have the same elements, but in reverse order, otherwise is false:
  ```
  ∀s1, s2: reverse(s1, s2) ⇒ form:Sequence(s1) ∧ form:Sequence(s2)
  ∀s1, s2: reverse(s1, s2) ≡ (length(s1) = length(s2)) ∧ (∀i ∈ Z⁺: i ≥ 1 ∧ i ≤ length(s1) ⇒ at(s1, (length(s1) − i + 1) = at(s2, i))
  ```

### 8.4.3.2 Types Semantics

**Abstract syntax reference:** 8.3.3.1

The `checkTypeSpecialization` constraint requires that all `Types` directly or indirectly specialize `Base::Anything` (
see 9.2.2.2.1). However, there is *no* implied relationship that shall be inserted to satisfy this constraint for a
`Type` that is not a `Classifier` or a `Feature` (see also 8.4.3.3 and 8.4.3.4 on Classifiers and Features,
respectively).

The mathematical interpretation (see 8.4.3.1.2) of `Types` in a model shall satisfy the following rules:

1. All sequences in the interpretation of a `Type` are in the interpretations of the `Types` it specializes.
   ```
   ∀tg, ts ∈ VT: tg ∈ ts.specialization.general ⇒ (ts)T ⊆ (tg)T
   ```

2. No sequences in the interpretation of a `Type` are in the interpretations of its disjoining `Types`.
   ```
   ∀t, td ∈ VT: td ∈ t.disjoiningTypeDisjoining.disjoiningType ⇒ ((t)T ∩ (td)T = ∅)
   ```

3. The interpretations of a `Type` that has `unioningTypes` are all and only the interpretations of those `Types`.
   ```
   ∀t ∈ VT, utl: form:Sequence(utl) ∧ utl = t.unioningTypes ∧ length(utl) > 0 ⇒ (t)T = ∪(i=1 to length(utl)) (at(utl, i))T
   ```

4. The interpretations of a `Type` that has `intersectingTypes` are all and only the interpretations in common between
   all the `Types`.
   ```
   ∀t ∈ VT, itl: form:Sequence(itl) ∧ itl = t.intersectingTypes ∧ length(itl) > 0 ⇒ (t)T = ∩(i=1 to length(itl)) (at(itl, i))T
   ```

5. The interpretations of a `Type` that has `differencingTypes` are all and only the interpretations of the first
   `differencingType` that are not interpretations of the remaining ones.
   ```
   ∀t ∈ VT, dtl: form:Sequence(dtl) ∧ dtl = t.differencingTypes ∧ length(dtl) > 0 ⇒ (t)T = (at(dtl, 1))T ∖ ∪(i=2 to length(dtl)) (at(dtl, i))T
   ```

### 8.4.3.3 Classifiers Semantics

**Abstract syntax reference:** 8.3.3.2

The `checkTypeSpecialization` constraint is semantically required for `Classifiers` by the rules below. If necessary, it
may be syntactically satisfied in a model by inserting an implied `Subclassification` `Relationship` to
`Base::Anything` (see also Table 8).

The mathematical interpretation (see 8.4.3.1.2) of the `Classifiers` in a model shall satisfy the following rules:

1. If the interpretation of a `Classifier` includes a sequence, it also includes the 1-tail of that sequence.
   ```
   ∀c ∈ VC, s1 ∈ S: s1 ∈ (c)T ⇒ (∀s2 ∈ S: tail(s2, s1) ∧ length(s2) = 1 ⇒ s2 ∈ (c)T)
   ```

2. The interpretation of the `Classifier` `Anything` includes all sequences of all elements of the universe and
   markings.
   ```
   (Anything)T = S
   ```

### 8.4.3.4 Features Semantics

**Abstract syntax reference:** 8.3.3.3

The `checkFeatureSpecialization` constraint is semantically required by the first two rules below, combined with the
definition of `·T` in 8.4.3.1.2. If necessary, it may be syntactically satisfied in a model by inserting an implied
`Subsetting` `Relationship` to `Base::things` (see also Table 8). Note that satisfaction of the
`checkFeatureSpecialization` constraint implies satisfaction of the `checkTypeSpecialization` constraint, because
`Base::things` is a `FeatureTyping` specialization of `Base::Anything`.

The mathematical interpretation (see 8.4.3.1.2) of the `Features` in a model shall satisfy the following rules:

1. The interpretations of `Features` must have length greater than two.
   ```
   ∀s ∈ S, f ∈ VF: s ∈ (f)T ⇒ length(s) > 2
   ```

2. The interpretation of the `Feature` `things` is all sequences of length greater than two.
   ```
   (things)T = {s | s ∈ S ∧ length(s) > 2}
   ```

`Features` interpreted as sequences of length three or more can be treated as if they were interpreted as ordered
triples ("marked" binary relations), where the first and third elements are interpretations of the domain and co-domain
of the `Feature`, respectively, while the second element is a *marking* from `P`. The predicate `feature-pair` below
determines whether two sequences can be treated in this way.

Two sequences are a **feature pair** of a `Feature` if and only if the interpretation of the `Feature` includes a
sequence `s0` such that following are true:

- `s0` is the concatenation of the two sequences, in order, with an element of `P` (marking) in between them.
- The first sequence is in the minimal interpretation of all `featuringTypes` of the `Feature`.
- The second sequence is in the minimal interpretations of all `types` of the `Feature`.

```
∀s1, s2 ∈ S, p ∈ P, f ∈ VF: feature-pair(s1, p, s2, f) ≡
  ∃s0 ∈ S: s0 ∈ (f)T ∧ concat-around(s0, s1, p, s2) ∧
  (∀t1 ∈ VT: t1 ∈ f.featuringType ⇒ s1 ∈ (t1)minT) ∧
  (∀t2 ∈ VT: t2 ∈ f.type ⇒ s2 ∈ (t2)minT)
```

Markings for the same `s1` above can be related by `<P` to order `s2` across multiple interpretations (values) of `f`.
Interpretations of `f` can have the same `s1` and `s2`, differing only in `p` to distinguish duplicate `s2` (values of
`f`).

The interpretation of the `Features` in a model shall satisfy the following rules:

3. All sequences in an interpretation of a `Feature` have a tail with non-overlapping head and tail that are feature
   pairs of the `Feature`.
   ```
   ∀s0 ∈ S, f ∈ VF: s0 ∈ (f)T ⇒ ∃st, s1, s2 ∈ S, p ∈ P: tail(st, s0) ∧ head-tail(s1, s2, st) ∧ (length(st) > length(s1) + length(s2)) ∧ feature-pair(s1, p, s2, f)
   ```

4. Values of redefining `Features` are the same as the values of their `redefinedFeatures` restricted to the domain of
   the redefining `Feature`.
   ```
   ∀fg, fs ∈ VF: fg ∈ fs.redefinedFeature ⇒
     (∀s1 ∈ S: (∀fts ∈ VT: fts ∈ fs.featuringType ⇒ s1 ∈ (fts)minT) ⇒
       (∀s2 ∈ S, p ∈ P: (feature-pair(s1, p, s2, fs) ≡ feature-pair(s1, p, s2, fg))))
   ```

5. The `multiplicity` of a `Feature` includes the cardinality of its values, counting duplicates.
   ```
   ∀s1 ∈ S, f ∈ VF, n ∈ Z⁺: (∀t1 ∈ VT: t1 ∈ f.featuringType ⇒ s1 ∈ (t1)minT) ∧
     n = #{(p, s2) | feature-pair(s1, p, s2, f)} ⇒
     ∃p ∈ P: feature-pair(s1, p, (n), f.multiplicity)
   ```

6. If a `Feature` is unique, there are no values with the same markings.
   ```
   ∀s1, s2 ∈ S, p1, p2 ∈ P, f ∈ VF: f.isUnique ⇒
     (feature-pair(s1, p1, s2, f) ∧ feature-pair(s1, p2, s2, f) ⇒ p1 = p2)
   ```

7. If a `Feature` is ordered, the markings of its values are totally ordered and mark exactly one value each.
   ```
   ∀s1, s2, s3 ∈ S, p1, p2 ∈ P, f ∈ VF: f.isOrdered ⇒
     (feature-pair(s1, p1, s2, f) ∧ feature-pair(s1, p2, s3, f) ⇒ (p1 = p2 ∧ s2 = s3) ∨ p1 <P p2 ∨ p2 <P p1)
   ```

8. Sequences in the interpretation of an inverting feature are the reverse of those in the inverted feature.
   ```
   ∀f1, f2 ∈ VF: f2 ∈ f1.invertingFeatureInverting.invertingFeature ⇒
     (∀s1 ∈ S: s1 ∈ (f1)T ≡ (∃s2 ∈ S: s2 ∈ (f2)T ∧ reverse(s2, s1)))
   ```

9. The interpretation of a `Feature` with a chain is determined by the interpretations of the subchains.
   ```
   ∀f ∈ VF, cfl: cfl = f.chainingFeature ∧ form:Sequence(cfl) ∧ length(cfl) > 1 ⇒ chain-feature-n(f, cfl)
   ```

## 8.4.4 Kernel Semantics

### 8.4.4.1 Kernel Semantics Overview

The semantics of constructs in the Kernel Layer are specified in terms of the foundational constructs defined in the
Core layer supported by reuse of model elements from the Kernel Semantic Model Library (see 9.2). The most common way in
which library model elements are used is through specialization, in order to meet subtyping constraints specified in the
abstract syntax. For example, `Structures` are required to (directly or indirectly) subclassify `Object` from the
`Objects` library model, while `Features` typed by `Structures` must subset `objects`. Similarly, `Behaviors` must
subclassify `Performance` from the `Performances` library model, while `Steps` (`Features` typed by `Behaviors`) must
subset `performances`. The requirement for such specialization is specified by specialization constraints in the
abstract syntax, as listed in Table 10 along with the implied `Specializations` that may be used to satisfy them (see
8.4.2 for discussion of specialization constraints and implied `Relationships`).

Sometimes more complicated reuse patterns are needed. For example, binary `Associations` (with exactly two ends)
specialize `BinaryLink` from the library, and additionally require the ends of the `Association` to redefine the
`source` and `target` ends of `BinaryLink`. Such patterns are specified by redefinition constraints and other kinds of
semantic constraints in the abstract syntax, as listed in Table 11 along with the implied `Relationships` that may be
used to satisfy them (see also 8.4.2). In addition the Core semantic constraints listed in Table 9 actually support the
semantics of Kernel layer constructs.

In all cases, all Kernel syntactic constructs can be ultimately reduced to semantically equivalent Core patterns.
Various elements of the Kernel abstract syntax essentially act as "markers" for modeling patterns typing the Kernel to
the Core. The following subclauses specify the semantics for each syntactic area of the Kernel Layer in terms of the
semantic constraints that must be satisfied for various Kernel elements, the pattern of relationships these imply, and
the model library elements that are reused to support this.

### Table 10. Kernel Semantics Implied Specializations

| Semantic Constraint                                   | Implied Relationship | Target                                                |
|-------------------------------------------------------|----------------------|-------------------------------------------------------|
| `checkDataTypeSpecialization`                         | `Subclassification`  | `Base::DataValue` (see 9.2.2.2.2)                     |
| `checkClassSpecialization`                            | `Subclassification`  | `Occurrences::Occurrence` (see 9.2.4.2.13)            |
| `checkStructureSpecialization`                        | `Subclassification`  | `Objects::Object` (see 9.2.5.2.7)                     |
| `checkAssociationSpecialization`                      | `Subclassification`  | `Links::Link` (see 9.2.3.2.3)                         |
| `checkAssociationBinarySpecialization`                | `Subclassification`  | `Links::BinaryLink` (see 9.2.3.2.4)                   |
| `checkAssociationStructureSpecialization`             | `Subclassification`  | `Objects::LinkObject` (see 9.2.5.2.10)                |
| `checkBehaviorSpecialization`                         | `Subclassification`  | `Performances::Performance` (see 9.2.6.2.15)          |
| `checkFunctionSpecialization`                         | `Subclassification`  | `Performances::Evaluation` (see 9.2.6.2.8)            |
| `checkPredicateSpecialization`                        | `Subclassification`  | `Performances::BooleanEvaluation` (see 9.2.6.2.2)     |
| `checkInteractionSpecialization`                      | `Subclassification`  | `Transfers::Transfer` (see 9.2.7.2.9)                 |
| `checkSuccessionSpecialization`                       | `Subclassification`  | `Occurrences::HappensBefore` (see 9.2.4.2.9)          |
| `checkSuccessionFlowSpecialization`                   | `Subclassification`  | `Transfers::FlowTransfer` (see 9.2.7.2.5)             |
| `checkMetaclassSpecialization`                        | `Subclassification`  | `Metaobjects::Metaobject` (see 9.2.16.2.2)            |
| `checkMetadataFeatureMetaclassSpecialization`         | `FeatureTyping`      | `Metaobjects::Metaobject` (see 9.2.16.2.2)            |
| `checkMetadataFeatureSemanticSpecialization`          | See Note 2           | `baseType` of `SemanticMetadata` (see 9.2.16.2.3)     |
| `checkFeatureReferenceExpressionResultSpecialization` | `Subsetting`         | The `referent` of the `FeatureReferenceExpression`    |
| `checkConstructorExpressionResultSpecialization`      | See Note 3           | The `instantiatedType` of the `ConstructorExpression` |
| `checkInvocationExpressionResultSpecialization`       | See Note 3           | The `instantiatedType` of the `InvocationExpression`  |
| `checkFeatureChainExpressionResultSpecialization`     | `Subsetting`         | The last `Feature` in the `targetFeature` chain       |
| `checkSelectExpressionResultSpecialization`           | `Subsetting`         | The `referent` of the `SelectExpression`              |
| `checkIndexExpressionResultSpecialization`            | `Subsetting`         | The `referent` of the `IndexExpression`               |

**Notes:**

1. For all constraints *other than* `checkMetadataFeatureSemanticSpecialization` and the other constraints listed below,
   the `source` of any implied `Relationship` is the annotated element of the constraint, with the `target` as given in
   the table. For `checkMetadataFeatureSemanticSpecialization`, see Note 2. For the following constraints, the `source`
   is the `result` parameter of the `Expression` that is the annotated element of the constraint:
    - `checkFeatureReferenceExpressionResultSpecialization`
    - `checkConstructorExpressionResultSpecialization`
    - `checkInvocationExpressionResultSpecialization`
    - `checkFeatureChainExpressionResultSpecialization`
    - `checkSelectExpressionResultSpecialization`
    - `checkIndexExpressionResultSpecialization`

2. The `checkMetadataFeatureSemanticSpecialization` constraint only applies to a `MetadataFeature` that has a
   `metaclass` that is a kind of `SemanticMetadata` (see 9.2.16.2.3). The source of the implied `Relationship` for this
   constraint is *not* the `MetadataFeature` but, rather, the `Type` annotated by the `MetadataFeature`, and a
   conforming tool need only insert the `Relationship` if the `MetadataFeature` is an `ownedMember` of the `Type`. The
   kind of `Relationship` that is implied and its `target` are determined as follows:
    - If the annotated `Type` and the `baseType` are both `Classifiers`, then `Subclassification` targeting the
      `baseType`.
    - If the annotated `Type` is a `Feature` and the `baseType` is a `Classifier`, then `FeatureTyping` targeting the
      `baseType`.
    - If the annotated `Type` and the `baseType` are both `Features`, then `Subsetting` targeting the `baseType`.
    - If the annotated `Type` is a `Classifier` and the `baseType` is a `Feature`, then `Subclassifications` targeting
      each of the types of the `Feature`.

3. For the `checkConstructorExpressionResultSpecialization` and `checkInvocationExpressionSpecialization` constraints,
   the implied `Relationship` is a `Subclassification` if the `instantiatedType` is a `Classifier`, a `Subsetting` if
   the `instantiatedType` is a `Feature`, and a `Specialization` otherwise.

### Table 11. Kernel Semantics Other Implied Relationships

| Semantic Constraint                                            | Implied Relationship | Target                                                                  |
|----------------------------------------------------------------|----------------------|-------------------------------------------------------------------------|
| `checkConnectorEndRedefinition`                                | `Redefinition`       | The end of a supertype `Connector` at the same position                 |
| `checkConnectorTypeFeaturing`                                  | `TypeFeaturing`      | The `defaultFeaturingType` of the `Connector` (See Note 2)              |
| `checkSuccessionSourceRedefinition`                            | `Redefinition`       | `Occurrences::HappensBefore::earlierOccurrence` (see 9.2.4.2.9)         |
| `checkSuccessionTargetRedefinition`                            | `Redefinition`       | `Occurrences::HappensBefore::laterOccurrence` (see 9.2.4.2.9)           |
| `checkInvocationExpressionBehaviorBindingConnector`            | `BindingConnector`   | `behavior` of `InvocationExpression` to `instantiatedType` (See Note 3) |
| `checkConstructorExpressionResultDefaultValueBindingConnector` | `BindingConnector`   | See Note 4                                                              |
| `checkInvocationExpressionDefaultValueBindingConnector`        | `BindingConnector`   | See Note 4                                                              |
| `checkFeatureValueBindingConnector`                            | `BindingConnector`   | `featureWithValue` to `result` of `valueExpression`                     |
| `checkFeatureValueExpressionTypeFeaturing`                     | `TypeFeaturing`      | `featuringTypes` of `featureWithValue`                                  |
| `checkFeatureChainExpressionTargetRedefinition`                | `Redefinition`       | See Note 5                                                              |
| `checkFeatureChainExpressionSourceTargetRedefinition`          | `Redefinition`       | See Note 5                                                              |
| `checkConstructorExpressionResultFeatureRedefinition`          | `Redefinition`       | See Note 6                                                              |

**Notes:**

1. For redefinition and type featuring constraints, except for `checkConstructorExpressionResultFeatureRedefinition`,
   the annotated element of the constraint is the `source` and `owningRelatedElement` of the implied `Relationship`,
   with the `target` as given in the last column of the table. For the
   `checkConstructorExpressionResultFeatureRedefinition` constraint, the `source` is an `ownedFeature` of the `result`
   parameter of the `ConstructorExpression`. For binding connector constraints, the annotated element of the constraint
   is the `owningNamespace` of the implied `Relationship`, with the `source` and `target` of the `Relationship` as given
   in the last column of the table.

2. For the `checkConnectorTypeFeaturing` constraint, an implied `TypeFeaturing` shall only be included to satisfy the
   constraint if the `Connector` has no `owningType`, no non-implied `ownedTypeFeaturings`, and a non-null
   `defaultFeaturingType`.

3. The `checkInvocationExpressionBehaviorBindingConnector` constraint only applies if the `instantiatedType` is not a
   `Function` or a `Feature` typed by a `Function`.

4. The `checkConstructorExpressionResultDefaultValueBindingConnector` and
   `checkInvocationExpressionDefaultValueBindingConnector` constraints apply to each `ownedFeature` that redefines a
   `Feature` for which there is an *effective default value* (see 8.4.4.11).

5. For the `checkFeatureChainExpressionTargetRedefinition` and `checkFeatureChainExpressionSourceTargetRedefinition`
   constraints, the `redefiningFeature` of the implied `Redefinition` is a nested `Feature` of the first owned input
   parameter of the `FeatureChainExpression` (corresponding to the `source` parameter of the `.` `Function`).

6. For the `checkConstructorExpressionResultFeatureRedefinition` constraint, the target of the `Redefinition` shall be
   the `feature` of the `instantiatedType` at the same position in order in the `instantiatingType` as the position of
   the redefining `ownedFeature` in the `ConstructorExpression` `result` parameter.

### 8.4.4.2 Data Types Semantics

**Abstract syntax reference:** 8.3.4.1

The `checkDataTypeSpecialization` constraint requires that `DataTypes` specialize the base `DataType`
`Base::DataValue` (see 9.2.2.2.2). The `checkFeatureDataValueSpecialization` constraint requires that `Features` typed
by a `DataType` specialize the `Feature` `Base::dataValues` (see 9.2.2.2.3), which is typed by `Base::DataValue`.

```kerml
datatype D specializes Base::DataValue {
    feature a : ScalarValue::String subsets Base::dataValues;
    feature b : D subsets Base::dataValues;
}
```

The `Type` `Base::DataValue` is disjoint with `Occurrences::Occurrence` and `Links::Link`, the base `Types` for
`Classes` and `Associations` (see 8.4.4.3 and 8.4.4.5, respectively). This means that a `DataType` cannot specialize a
`Class` or `Association` and that a `Feature` typed by a `DataType` cannot also be typed by a `Class` or `Association`.

### 8.4.4.3 Classes Semantics

**Abstract syntax reference:** 8.3.4.2

The `checkClassSpecialization` constraint requires that `Classes` specialize the base `Class`
`Occurrences::Occurrence` (see 9.2.4.2.13). The `checkFeatureOccurrenceSpecialization` constraint requires that
`Features` typed by a `Class` specialize the `Feature` `Occurrences::occurrences` (see 9.2.4.2.14), which is typed by
`Occurrences::Occurrence`. Further, the `checkFeatureSuboccurrenceSpecialization` constraint requires that composite
`Features` typed by a `Class`, and whose `owningType` is a `Class` or another `Feature` typed by a `Class`, specialize
the `Feature` `Occurrences::Occurrence::suboccurrences` (see 9.2.4.2.13), which subsets `Occurrences::occurrences`.

```kerml
class C specializes Occurrences::Occurrence {
    feature a : C subsets Occurrences::occurrences;
    composite feature b : C subsets Occurrences::Occurrence::suboccurrences;
}
```

The `Class` `Occurrences::Occurrence` is disjoint with `Base::DataValues`, the base `Type` for `DataTypes` (see
8.4.4.2). This means that a `Class` cannot specialize a `DataType` and that a `Feature` typed by a `Class` cannot also
be typed by a `DataType`. Note that `Occurrences::Occurrence` is *not* disjoint with `Link::Links`, because an
`AssociationStructure` is both an `Association` and a `Structure` (which is a kind of `Class`), so the base
`AssociationStructure` `Objects::LinkObject` specializes both `Link::Links` and (indirectly) `Occurrences::Occurrence`.

Unlike `DataValues`, `Occurrences` are modeled as occurring in three-dimensional space and persisting over time. The
`Occurrences` library model includes an extensive set of `Associations` between `Occurrences` that model various spatial
and temporal relations, such as `InsideOf`, `OutsideOf`, `HappensBefore`, `HappensDuring`, etc. In particular, the
`Association` `HappensBefore` is the base `Type` for `Successions`, the basic modeling construct for time-ordering
`Occurrences` (see 8.4.4.6 on the semantics of `Successions`). For further detail on the `Occurrences` model, see
9.2.4.1.

A `Class` (or any `Type` that directly or indirectly specializes `Occurrence`) may have `ownedFeatures` with
`isVariable` = true. The `checkFeatureFeatureMembershipTypeFeaturing` constraint requires that such
variable `Features` are featured by the `snapshots` of their `owningType`. A `snapshot` covers the entire spatial
extent of an `Occurrence` at a specific point in time. Therefore, an instance of the `owningType` can potentially have
a different value for the variable `Feature` at each point in time during its `Life`. (See 9.2.4.1 for more on
`snapshots` and `Lives`.)

For example, a variable `Feature` declared as in the following is required to have as its `featuringType` a `Feature`
that redefines `Occurrence::snapshots` and is itself featured by the `owningType` of the variable `Feature`.

```kerml
class C1 {
    var feature v1;
}
```

Thus, the above variable `Feature` declaration is semantically equivalent to (with implied `Specializations` also
shown):

```kerml
class C1 specializes Occurrences::Occurrence {
    var feature v1 subsets Base::things featured by C1_snapshots {
        member feature C1_snapshots
            redefines Occurrences::Occurrence::snapshots
            featured by C1;
    }
}
```

The `Feature` `C1_snapshots` is shown above as nested in the corresponding variable `Feature` `v1` for purposes of
presentation in the textual notation. However, when an implied `TypeFeaturing` relationship is added to satisfy the
`checkFeatureFeatureMembershipTypeFeaturing` constraint, the "snapshots" `featuringType` is included as
an `ownedRelatedFeature` of the implied `TypeFeaturing`. That is, the implied abstract syntax ownership
structure for, e.g., the variable `Feature` `C1::v1` above is:

```
Feature v1
    [Subsetting (IMPLIED)] Feature Base::things
    [TypeFeaturing (IMPLIED)] Feature C1_snapshots (OWNED)
        [Redefinition] Occurrences::snapshots
        [TypeFeaturing] C1
```

(The name `C1_snapshots` is used here for correspondence to the earlier textual notation presentation. This
`Feature` would not be expected to be named in an actual implementation.)

### 8.4.4.4 Structures Semantics

**Abstract syntax reference:** 8.3.4.3

The `checkStructureSpecialization` constraint requires that `Structures` specialize the base `Structure`
`Objects::Object` (see 9.2.5.2.7). The `checkFeatureObjectSpecialization` constraint requires that `Features` typed by a
`Structure` specialize the `Feature` `Objects::objects` (see 9.2.5.2.8), which is typed by `Objects::Object`. Further,
the `checkFeatureSubobjectSpecialization` constraint requires that composite `Features` typed by a `Structure`, and
whose `owningType` is a `Structure` or another `Feature` typed by a `Structure`, specialize the `Feature`
`Objects::Object::subobjects` (see 9.2.5.2.7), which subsets `Object::objects`.

```kerml
struct S specializes Objects::Object {
    feature a : S subsets Object::objects;
    composite feature b : S subsets Objects::Object::subobjects;
}
```

`Objects` are `Occurrences` representing physical or virtual structures that occur over time. For physical structures,
the `Objects` library model also provides the specialization `StructuredSpaceObject`, which models `Objects` that can be
spatially decomposed into cells of the same or lower dimension. The `Type` `Object` is disjoint with the `Type`
`Performance`, another specialization of `Occurrence`, which is the base `Type` for `Behaviors` (see 8.4.4.7 on the
semantics of `Behaviors`). For further detail on the `Objects` model, see 9.2.5.1.

### 8.4.4.5 Associations Semantics

**Abstract Syntax Reference:** 8.3.4.4

#### 8.4.4.5.1 Associations

The `checkAssociationSpecialization` and `checkFeatureEndSpecialization` constraints require that an
`Association` specialize the base `Association` `Links::Link` (see 9.2.3.2.3) and that its `associationEnds`
subset `Links::Link::participant`. In addition, the `validateFeatureEndMultiplicity` constraint requires
that the `associationEnds` must have multiplicity `1..1`. These constraints essentially require an N-ary
`Association` to have the form (with implied relationships included):

```kerml
assoc A specializes Links::Link {
    end feature e1[1..1] subsets Links::Link::participant;
    end feature e2[1..1] subsets Links::Link::participant;
    ...
    end feature eN[1..1] subsets Links::Link::participant;
}
```

The `Link` instance for an `Association` is thus a tuple of `participants`, where each `participant` is a single
value of an `associationEnd` of the `Association`. Note also that the `Feature` `Link::participant` is declared
readonly, meaning that the `participants` in a link cannot change once the link is created.

The `checkFeatureEndRedefinition` constraint requires that, if an `Association` has an
`ownedSubclassification` to another `Association`, then its `associationEnds` redefine the
`associationEnds` of the `superclassifier` `Association`. In this case, the `subclassifier` `Association`
will indirectly specialize `Link` through a chain of `Subclassifications`, and each of its `associationEnds` will
indirectly subset `Links::participant` through a chain of `Redefinition` and `Subsetting`.

The `checkAssociationBinarySpecialization` constraint requires that a binary `Association` (one with
exactly two `associationEnds`) specialize `Links::BinaryLink`. `BinaryLink` specializes `Link` to have exactly
two `participants` corresponding to two ends called `source` and `target`. As required by the
`checkFeatureEndRedefinition` constraint, the first `associationEnd` of a binary `Association` will redefine
`Links::BinaryLink::source` and its second `associationEnd` will redefine `Links::BinaryLink::target`.

```kerml
assoc B specializes Links::BinaryLink {
    end feature e1 redefines Links::BinaryLink::source;
    end feature e2 redefines Links::BinaryLink::target;
}
```

Note that, as `associationEnds` of `BinaryLink`, `source` and `target` already have multiplicities of `1..1`, which
ensures that the ends of any binary `Association` do too.

A binary `Association` can also specify *cross features* for one or both of its `associationEnds` using
`CrossSubsetting`. Such a cross feature must be a `feature` of the `type` of the other `associationEnd` than the
one for the cross feature.

The `validateCrossSubsettingCrossedFeature` constraint requires that the target of a `CrossSubsetting` be
a feature chain consisting of the other `associationEnd` and the cross feature. `CrossSubsetting` is a kind of
`Subsetting`, so it semantically requires that the value of an `associationEnd` be one of the values of the cross
feature for the other `associationEnd`.

This also means that, if an `associationEnd` of a binary `Association` has a cross feature, then the cross-feature
multiplicity applies to each set of instances (links) of the `Association` that have the same (singleton) value for the
`associationEnd`. Cross feature uniqueness and ordering apply to the collection of values of the other
`associationEnd` in each of those link sets, preventing duplication in each collection and ordering them to form a
sequence.

For example, the binary `Association` `B1` below specifies cross features for both its ends (without implied
relationships included):

```kerml
classifier T1 {
    feature e2_cross[0..1] : T2;
}
classifier T2 {
    feature e1_cross[1..4] nonunique ordered : T1;
}
assoc B1 {
    end feature e1 : T1 crosses e2.e1_cross;
    end feature e2 : T2 crosses e1.e2_cross;
}
```

The multiplicities specified for `e1_cross` and `e2_cross` then mean that:

- For each value of `e1_cross`, there is at most one instance of `B1` for which `e1` has that value and `e2` has
  the corresponding value of `e2_cross` (multiplicity `0..1`).
- For each value of `e2_cross`, there are one to four instances of `B1` for which `e2` has that value and `e1` has
  the corresponding value of `e1_cross` (multiplicity `1..4`). Further, there may be more than one of these
  instances with the same value of `e1` (nonunique) and the instances have an implied ordering (ordered).

*Note.* Ordering and uniqueness are irrelevant on the `associationEnds` themselves, since they must always have
multiplicity `1..1`.

Note that cross features impose only *necessary* conditions on the instances of an `Association`, which do not
require existence of instances of the `Association` for all values of its cross features. To make these conditions also
sufficient, requiring existence of these instances, the `Association` must have `isSufficient = true` (see
8.3.3.1.10). For example, if `B1` above has `isSufficient = true`, then an instance `t1` of `T1` having a value `t2` for
`e2_cross` is sufficient to require that an instance of `B1` exist linking `t1` to `t2` and, therefore, that `t1` is a
value of
`e1_cross` for `t2`.

```kerml
assoc all B1 { // "all" declares isSufficient = true
    end feature e1 : T1 crosses e2.e1_cross;
    end feature e2 : T2 crosses e1.e2_cross;
}
```

Cross features may also be directly owned by the corresponding `associationEnd`. The
`checkFeatureOwnedCrossFeatureTypeFeaturing` constraint requires such an *owned cross feature* (for a
binary `Association`) be featured by the `type` of the other `associationEnd` (which means it must be owned by
the `associationEnd` via `OwnedMembership` but not `FeatureMembership`). Further, the
`checkFeatureCrossingSpecialization` constraint requires that the `associationEnd` has a
`CrossSubsetting` that targets a feature chain whose first `Feature` is the other `associationEnd` and whose
second `Feature` is the owned cross feature.

An owned cross feature may be declared with the declaration of the corresponding `associationEnd`. For example,
the following binary `Association` declaration (the cross feature names are optional, but they are included here for
convenience of reference):

```kerml
assoc B2 {
    end e1_cross [1..4] nonunique ordered feature e1 : T1;
    end e2_cross [0..1] feature e2 : T2;
}
```

is parsed (with implied relationships included) as:

```kerml
assoc B2 specializes Links::BinaryLink {
    end feature e1 : T1 redefines Links::BinaryLink::source;
        crosses e2.e1_cross {
            member feature e1_cross[1..4] nonunique ordered : T1
                featured by T2;
        }
    end feature e2 : T2 redefines Links::BinaryLink::target;
        crosses e1.e2_cross {
            member feature e2_cross[0..1] : T2 featured by T1;
        }
}
```

*Note.* The feature chain notations `e2.e1_cross` and `e1.e2_cross` in the above notional equivalent will actually
not parse, because `e1_cross` is not in the namespace of `e2` and `e2_cross` is not in the namespace of `e1`. However,
the `Features` meet the *semantic* requirements for a feature chain (i.e., the `type` of the first `Feature` is the
`featuringType` of the second `Feature`), so the construct is valid in the abstract syntax.

An `Association` with three or more `associationEnds` may also have ends with cross features, but, in this case,
the cross features *must* be owned by their corresponding `associationEnds`. For example, the ternary
`Association`:

```kerml
assoc Ternary {
    end a_cross[1] feature a[1] : A;
    end b_cross[0..2] feature b[1] : B;
    end c_cross[*] nonunique ordered feature c[1] : C;
}
```

is effectively parsed (including implied relationships) as:

```kerml
assoc Ternary specializes Links::Link {
    end feature a[1] : A subsets Links::Link::participant
        crosses b_c.a_cross {
            member feature b_c : B_C featured by Ternary;
            member feature B_C : C featured by B;
            member feature a_cross[1] : A featured by B_C;
        }
    end feature b[1] : B subsets Links::Link::participant
        crosses a_c.b_cross {
            member feature a_c : A_C featured by Ternary;
            member feature A_C : C featured by A;
            member feature b_cross[0..2] : B featured by A_C;
        }
    end feature c[1] : C subsets Links::Link::participant
        crosses a_b.c_cross {
            member feature a_b : A_B featured by Ternary;
            member feature A_B : C featured by B;
            member feature c_cross[*] : C featured by A_B;
        }
}
```

Consider specifically the `associationEnd` `a` in the above `Association`. Since the `Association` is not binary,
there is no longer a single other `associationEnd` to `a`. So, in order to satisfy the
`checkFeatureOwnedCrossFeatureTypeFeaturing` constraint, the cross feature `a_cross` is featured by the
`Feature` `B_C`, which is constructed as being typed by `C` and featured by `B`. According to the Core semantics for
`Features` (see 8.4.3.4), the `Feature` `B_C` is (minimally) interpreted as having instances that are pairs of instances
of `B` and `C`, in that order. That is, the feature can be considered to semantically represent a *Cartesian product* of
the
set of instances of `B` and the set of instances of `C`. The `associationEnd` `a` then has a `CrossSubsetting` of a
feature chain that starts with the `Feature` `b_c`, which is typed by `B_C`, followed by the cross feature `a_cross`,
which is featured by `B_C`. As a result, the values of `a_cross` for each instance of `Ternary` are the values of the
`associationEnd` `a` on all the instances of `Ternary` that have the same values for the other two
`associationEnds`.

Note also that the `Features` `B_C` and `b_c` are shown above as nested in the `associationEnd` `a` for purposes of
presentation in the textual notation. However, when added with the implied relationships needed to satisfy semantic
constraints, these `Features` are actually `ownedRelatedElements` of, respectively, the implied `TypeFeaturing`
relationship on owned cross feature `a_cross` and the first `FeatureChaining` relationship in the target feature
chain of the implied `CrossSubsetting` relationship on the `associationEnd` `a`.

That is, the implied abstract syntax ownership structure is:

```
Feature a
    [CrossSubsetting (IMPLIED)] Feature (OWNED)
        [FeatureChaining] Feature b_c (OWNED)
            [FeatureTyping] Feature B_C
            [TypeFeaturing] Association Ternary
        [FeatureChaining] a_cross
    [OwningMembership] Feature a_cross (OWNED)
        [FeatureTyping (IMPLIED)] Classifier A
        [TypeFeaturing (IMPLIED)] Feature B_C (OWNED)
            [TypeFeaturing] Classifier B
            [FeatureTyping] Classifier C
```

(The names `B_C` and `b_c` are included here for correspondence to the earlier textual notation presentation. These
`Features` would not be expected to be named in an actual implementation.)

Similar syntax and semantics apply to all three of the `associationEnds` of `Ternary`. Each instance of `Ternary`
consists of three `participants`, one value for each of the `associationEnds` `a`, `b` and `c`. But the multiplicities
specified for the owned cross features of the `associationEnds` then assert that:

1. For any specific values of `b` and `c`, there must be exactly one instance of `Ternary`, with the single value
   allowed
   for `a`.
2. For any specific values of `a` and `c`, there may be up to two instances of `Ternary`, all of which must have
   different
   values for `b` (default uniqueness).
3. For any specific values of `a` and `b`, there may be any number of instances of `Ternary`, which are ordered and
   allow repeated values for `c`.

This approach is applied to any N-ary `Association` with N of 3 or greater by extending the pattern for representing
a Cartesian product of `Classifiers` using a `Feature` to any number of `Classifiers`. The operation
`Feature::isCartesianProduct` checks whether a `Feature` meets the pattern for representing a Cartesian
product. If so, then the operation `Feature::asCartesianProduct` returns the ordered list of Classifiers in the
product. (See 8.3.3.3.4 for the specifications of these operations.)

This gives the following general semantics for owned cross feature multiplicity: For an `Association` with N
`associationEnds`, with N of 2 or greater, consider the i-th `associationEnd` `ei`. The multiplicity of the owned
cross feature of `ei` applies to each set of instances of the `Association` that have the same (singleton) values for
each of the N-1 `associationEnds` other than `ei`. Uniqueness and ordering of the owned cross feature apply to the
collection of values of `ei` in each of those link sets, preventing duplication in each collection and ordering them to
form a sequence.

As described previously, the `checkFeatureEndRedefinition` constraint requires the `associationEnds` of a
specialized `Association` to redefine the ends of the `Associations` it specializes. If a redefining
`associationEnd` has an owned cross feature, the
`checkFeatureOwnedCrossFeatureRedefinitionSpecialization` constraint further requires that the owned
cross feature of the redefining `associationEnd` must subset the cross feature of the redefined `associationEnd`.
Note that this constraint must be satisfied even if the cross feature of the redefined `associationEnd` is not owned
by that `associationEnd`.

For example, consider the following specialization of the `Association` `B2` shown earlier:

```kerml
assoc B2a specializes B2 {
    end e1a_cross [0..2] nonunique ordered feature e1a : T1;
    end e2a_cross [1..1] feature e2a : T2;
}
```

This is parsed (with implied relationships included) as:

```kerml
assoc B2a specializes B2 {
    end feature e1a : T1 redefines B2::e1
        crosses e2a.e1a_cross {
            member feature e1a_cross[0..2] nonunique ordered : T1
                subsets B2::e1::e1_cross featured by T2;
        }
    end feature e2a : T2 redefines B2::e2
        crosses e1a.e2a_cross {
            member feature e2a_cross[1..1] : T2
                subsets B2::e2::e2_cross featured by T1;
        }
}
```

#### 8.4.4.5.2 Association Structures

An `AssociationStructure` is both an `Association` and a `Structure` and, therefore, the semantic constraints
of both `Associations` and `Structures` (see 8.4.4.4) apply to `AssociationStructures`. The
`checkAssociationStructureSpecialization` constraint requires an `AssociationStructure` to specialize
`Objects::LinkObject` (see 9.2.5.2.5), which specializes both `Links::Link` and `Objects::Object`. The
`checkAssociationStructureBinarySpecialization` constraint requires a binary `AssociationStructure`
to specialize `Objects::BinaryLinkObject` (see 9.2.5.2.1), which specializes both `Links::BinaryLink` and
`Objects::LinkObject`.

### 8.4.4.6 Connectors Semantics

**Abstract syntax reference:** 8.3.4.5

#### 8.4.4.6.1 Connectors

A `Connector` can only be typed by `Associations`. The `checkConnectorSpecialization` constraint then
requires that `Connectors` specialize the base `Feature` `Link::links` (see 9.2.3.2.4), which is typed by the base
`Association` `Links::Link` (see 9.2.3.2.3). Further, the `checkFeatureEndRedefinition` constraint requires
that the `connectorEnds` of a `Connector` redefine the `associationEnds` of its typing `Associations`. As a
result, a `Connector` typed by an N-ary `Association` is essentially required to have the form (with implicit
relationships included):

```kerml
connector a : A subsets Links::links {
    end feature e1 redefines A::e1 references f1;
    end feature e2 redefines A::e2 references f2;
    ...
    end feature eN redefines A::eN references fN;
}
```

where `e1`, `e2`, ..., `eN` are the names of `associationEnds` of the `Association` `A`, in the order they are defined
in `A`,
and the `f1`, `f2`, ..., `fN` are the `relatedFeatures` of the `Connector`. Multiplicities declared for `connectorEnds`
have the same special semantics as for `associationEnds` (see 8.4.4.5). If `A` is an `AssociationStructure`, then
the `checkConnectorObjectSpecialization` constraint requires that the `Connector` subsets
`Objects::linkObjects` (see 9.2.5.2.6) instead of `Links::link`.

A binary `Connector` is a `Connector` with exactly two `connectorEnds`, that is, a `Connector` typed by a binary
`Association`. The `checkConnectorBinarySpecialization` constraint requires that binary `Connectors`
specialize the base `Feature` `Link::binaryLinks` (see 9.2.3.2.2), which is typed by the `Association`
`Links::BinaryLink` (see 9.2.3.2.1). In particular, if no type is explicitly declared for a binary `Connector`, then
its `connectorEnds` simply redefine the `source` and `target` ends of the `Association` `BinaryLink`, which are
inherited by the `Feature` `binaryLinks`.

```kerml
connector b : B subsets Links::binaryLinks {
    end feature source redefines B::source references f1;
    end feature target redefines B::target references f2;
}
```

If `B` is an `AssociationStructure`, then the `checkConnectorBinaryObjectSpecialization` constraint
requires that the `Connector` subsets `Objects::binaryLinkObjects` (see 9.2.5.2.2) instead of
`Links::binaryLinks`.

A `connectorEnd` may also have an owned cross feature, with the same syntax and semantics as for an owned cross
feature of an `associationEnd` (see 8.4.4.5.1). If the `connectorEnd` redefines an `associationEnd` (or other
`connectorEnd`), then its owned cross feature must meet the same semantic constraints as for the owned cross
feature of an `associationEnd` that redefines another `associationEnd` (see 8.4.4.5.1).

For example, the declaration

```kerml
connector b2 : B2 {
    end e1a_cross [0..2] nonunique ordered feature e1a
        references f1;
    end e2a_cross [1..1] feature e2a references f2;
}
```

is parsed (with implied relationships included) as:

```kerml
connector b2 : B2 subsets Links::binaryLinks {
    end feature e1a redefines B2::e1 references f1
        crosses e2a.e1a_cross {
            member feature e1a_cross[0..2] nonunique ordered : T1
                subsets B2::e1::e1_cross featured by T2;
        }
    end feature e2 redefines B2::e2 references f2
        crosses e1.e2_cross {
            member feature e2a_cross[1..1] : T2
                subsets B2::e2::e2_cross featured by T1;
        }
}
```

A `Connector` specifies a subset of the `Links` of its typing `Associations` for which the `participants` are
values of the `relatedFeatures` of the `Connector`. In addition, the `checkConnectorTypeFeaturing` constraint
requires that the `featuringTypes` of a `Connector` be consistent with those of its `relatedFeatures`. Typically,
a `Connector` will have an `owningType` that is its `featuringType`, in which case all of its `relatedFeatures`
must also be featured in the context of this `Type`.

```kerml
// This is the simplest case of a Connector satisfying checkConnectorTypeFeaturing,
// in which the Connector and its relatedFeatures all have the same owningType.
classifier C1 {
    feature f1;
    feature f2;
    connector cc1 {
        end feature references f1;
        end feature references f2;
    }
}
```

An implied `TypeFeaturing` may be included to satisfy the `checkConnectorTypeFeaturing` constraint, but only
if the `Connector` has no explicit `owningType` or owned `TypeFeaturings`, and the `defaultFeaturingType` of
the `Connector` is not null. The target of the implied `TypeFeaturing` is then given by the
`defaultFeaturingType`. The `deriveConnectorDefaultFeaturingType` constraint ensures that, if
`defaultFeaturingType` is non null, then it is the innermost `Type` such that, if it is the `featuringType` of a
`Connector`, the `checkConnectorTypeFeaturing` constraint will be met.

```kerml
classifier C2 {
    feature f1;
    feature f2 {
        // The defaultFeaturingType for Connector cc2 is Classifier C2, which is the
        // common featuringType of the relatedFeatures of cc2.
        member connector cc2 featured by C2 {
            end feature references f1;
            end feature references f2;
        }
    }
}
```

The primary case in which an implicit `TypeFeaturing` is necessary is for a `BindingConnector` that is itself
added implicitly for a `FeatureValue` (see 8.4.4.11).

The `checkConnectorTypeFeaturing` and `deriveConnectorDefaultFeaturingType` constraint uses the
`Feature::isFeaturedWithin` operation (see 8.3.3.3.4), which specially handles variable features. The
semantics of variable `Features` requires them to have `featuringTypes` that represent the `snapshots` of their
`owningTypes`. For example, consider the following:

```kerml
class CL1 {
    var feature v1 featured by CL1_snapshots {
        member feature CL1_snapshots featured by CL1;
    }
}
class CL2 specializes CL1 {
    var feature v2 featured by CL2_snapshots {
        member feature CL2_snapshots featured by CL2;
    }
    member connector ccv featured by CL2_snapshots {
        end feature references v1;
        end feature references v2;
    }
}
```

While the class `CL2` specializes `CL1`, there is no explicit `Specialization` relationship between `CL1_snapshots`
and `CL2_snapshots`. However, any instance of `CL2` is an instance of `CL1`, and `CL1_snapshots` and
`CL2_snapshots` are both redefinitions of the same base `Feature` `Occurrences::Occurrence::snapshots`,
so, semantically, `CL2_snapshots` can be considered to be a redefinition of `CL1_snapshots`. The
`isFeaturedWithin` operation takes this into account by using the `Type::isCompatibleWith` operation. By
default, `isCompatibleWith` is just the same as `specializes`, but it is overridden in `Feature` to also treat
`Features` such as `CL1_snapshots` and `CL2_snapshots` as being compatible (see 9.2.5.2.7). This is why, in the
example above, the `defaultFeaturingType` for `ccv` is `CL2_snapshots`, which satisfies the
`checkConnectorTypeFeaturing` constraint for `ccv`.

In addition, the `isFeaturedWithin` operation specially considers a variable `Feature` to be featured within its
`owningType`, even though it is not directly featured by the `owningType`. This allows variable and non-variable
`Features` to be connected within a common featuring context.

```kerml
class CL3 {
    feature f;
    var feature v;
    connector cfv featured by CL3 {
        end feature references f;
        end feature references v;
    }
}
```

Semantically, this means that, within an instance of `CL3` each value of `cfv` links a value `f` and a value of `v` for
some
specific snapshot of `CL3`. However, which snapshot this is for each value of `cfv` is not determined in this
specification, unless additional temporal constraints are explicitly included in the model.

#### 8.4.4.6.2 Binding Connectors

The `checkBindingConnectorSpecialization` constraint requires that `BindingConnectors` specialize the
`Feature` `Links::selfLinks` (see 9.2.3.2.6), which is typed by the `Association` `SelfLink` (see 9.2.3.2.5).
`SelfLink` has two `associationEnds` that subset each other, meaning they identify the same things (have the
same values), which then also applies to `BindingConnector` `connectorEnds` that redefine the
`associationEnds` of `SelfLink`. The general semantic constraints for `Connectors` also apply to
`BindingConnectors`.

Thus, a `BindingConnector` declaration of the form

```kerml
binding f1 = f2;
```

is, with implied `Relationships` included, semantically equivalent to

```kerml
connector subsets Links::selfLinks {
    end feature thisThing redefines Links::SelfLink::thisThing references f1;
    end feature sameThing redefines Links::SelfLink::sameThing references f2;
}
```

#### 8.4.4.6.3 Successions

The `checkSuccessionSpecialization` constraint requires that `Successions` specialize the `Feature`
`Occurrences::happensBeforeLinks` (see 9.2.4.2.2), which is typed by the `Association` `HappensBefore` (see
9.2.4.2.1). `HappensBefore` (see 9.2.4.2.1) has two `associationEnds`, asserting that the `Occurrence` identified
by its first `associationEnd` (`earlierOccurrence`) temporally precedes the one identified by its second
(`laterOccurrence`), which then also applies to `Succession` `connectorEnds` that redefine the
`associationEnds` of `HappensBefore`. The general semantic constraints for `Connectors` also apply to
`Successions`.

Thus, a `Succession` declaration of the form

```kerml
succession first f1 then f2;
```

is, with implied `Relationships` included, semantically equivalent to

```kerml
connector subsets Occurrences::happensBeforeLinks {
    end feature earlierOccurrence references f1
        redefines Occurrences::HappensBefore::earlierOccurrence;
    end feature laterOccurrence references f2
        redefines Occurrences::HappensBefore::laterOccurrence;
}
```

### 8.4.4.7 Behaviors Semantics

**Abstract syntax reference:** 8.3.4.6

#### 8.4.4.7.1 Behaviors

The `checkBehaviorSpecialization` constraint requires that `Behaviors` specialize
`Performances::Performance` (see 9.2.6.2.14). In addition, the `checkFeatureParameterRedefinition`
constraint requires that any owned `parameters` (i.e., directed `ownedFeatures`) of a `Behavior` redefine
corresponding `parameters` of any other `Behaviors` it specializes.

```kerml
behavior B specializes Performances::Performance {
    in feature x[0..*] subsets Base::things;
    out feature y[0..1] subsets Base::things;
    inout feature z subsets Base::things;
}
behavior B1 specializes B {
    in feature x1[1] redefines B::x;
    out feature y1[1] redefines B::y;
    // z is inherited without redefinition
}
```

#### 8.4.4.7.2 Steps

The `checkStepSpecialization` constraint requires that `Steps` specialize `Performances::performances`
(see 9.2.6.2.15). In addition, the `checkFeatureParameterRedefinition` constraint requires that any owned
`parameters` (i.e., directed `ownedFeatures`) of a `Step` redefine corresponding `parameters` of any other `Steps`
or `Behaviors` it specializes. In particular, a `Step` explicitly typed by a `Behavior` will generally redefine the
`parameters` of that `Behavior`.

```kerml
step b : B subsets Performances::performances {
    in feature x redefines B::x = x1;
    out feature y redefines B::y;
    inout feature z redefines B::z := z1 ;
}
step b1 : B1 subsets b {
    in feature x redefines B1::x, b::x;
    out feature y redefines B2::y, b::y;
}
```

Further, the `checkStepEnclosedPerformanceSpecialization`
and `checkStepSubperformanceSpecialization` constraints require that a `Step` whose `owningType` is a
`Behavior` or another `Step` specialize `Performances::Performance::enclosedPerformance` or, if it is
composite, `Performances::Performance::subperformance` (see 9.2.6.2.14). Finally, the
`checkStepOwnedPerformanceSpecialization` constraint requires that a composite `Step` whose `owningType`
is a `Structure` or a `Feature` typed by a `Structure` specialize `Objects::Object::ownedPerformance` (see
9.2.5.2.7).

```kerml
step s subsets Performances::performances {
    step s1 subsets Performances::Performance::enclosedPerformance;
    composite step s2 subsets Performances::Performance::subperformance;
    struct S specializes Objects::Object {
        composite step ss subsets Objects::Object::ownedPerformance;
    }
}
```

### 8.4.4.8 Functions Semantics

**Abstract syntax reference:** 8.3.4.7

#### 8.4.4.8.1 Functions and Predicates

`Functions` are kinds of `Behaviors`. The `checkFunctionSpecialization` constraint requires that `Functions`
specialize the base `Function` `Performances::Evaluation` (see 9.2.6.2.4), which is a specialization of
`Performances::Performance`. All other semantic constraints on `Behaviors` (see 8.4.4.7) also apply to
`Functions`. In addition, the `checkFeatureResultRedefinition` constraint requires that the `result`
`parameter` of a `Function` always redefine the `result` of its supertypes that are also `Functions`, regardless
of their parameter position.

```kerml
function F specializes Performances::Evaluation {
    in a;
    in b;
    return result redefines Performances::Evaluation::result;
}
function G specializes F {
    in a redefines F::a;
    return result redefines F::result;
    in b redefines F::b;
}
```

Further, if a `Function` owns an `Expression` via a `ResultExpressionMembership`, then the
`checkFunctionResultBindingConnector` constraint requires that the `Function` have, as an `ownedFeature`, a
`BindingConnector` between the `result` `parameter` of the `Expression` and the `result` `parameter` of the
`Function`.

```kerml
function H specializes Performances::Evaluation {
    return redefines Performances::Evaluation::result;
    binding result = resultExpr.result; // Implied
    resultExpr
}
```

where `resultExpr` is an arbitrary `Expression` and `resultExpr.result` represents a `Feature` chain to the
`Expression` result.

A `Predicate` is a kind of `Function`, so all semantic constraints for `Functions` also apply to `Predicates`. In
addition, the `checkPredicateSpecialization` constraint requires that `Predicates` specialize the base
`Predicate` `Performances::BooleanEvaluation` (see 9.2.6.2.1), which is a specialization of
`Performances::Evaluation`. `BooleanEvaluation` has a `result` `parameter` typed by `Boolean`, so
`Predicates` always have a `Boolean` result.

```kerml
predicate P specializes Performances::BooleanEvaluation {
    in x : ScalarValues::Real;
    return redefines Performances::BooleanEvaluation::result;
    x > 0
}
```

#### 8.4.4.8.2 Expressions and Invariants

`Expressions` are kinds of `Steps`. The `checkExpressionSpecialization` constraint requires that
`Expressions` specialize the base `Expression` `Performances::evaluations` (see 9.2.6.2.5), which is a
specialization of `Performances::performances`. All other semantic constraints on `Steps` (see 8.4.4.7) also
apply to `Expressions`. In addition, the `checkFeatureResultRedefinition` constraint requires that the `result`
`parameter` of an `Expression` always redefine the `result` of any its supertypes that are `Functions` or other
`Expressions`, regardless of their parameter position.

```kerml
expr f : F subsets Performances::evaluations {
    in a redefines F::a;
    in b redefines F::b;
    return result redefines F::result, Performances::evaluations::result;
}
expr g : G subsets f {
    return result redefines G::result, f::result;
}
```

Further, if an `Expression` owns another `Expression` via a `ResultExpressionMembership`, then the
`checkExpressionResultBindingConnector` constraint requires that the `Expression` have, as an
`ownedFeature`, a `BindingConnector` between the `result` `parameter` of the owned `Expression` and the
`result` `parameter` of the owning `Expression`.

```kerml
expr h subsets Performances::Evaluation {
    binding result = resultExpr.result; // Implied
    resultExpr
}
```

where `resultExpr` is an arbitrary `Expression` and `resultExpr.result` represents a `Feature` chain to the
`Expression` result.

A `BooleanExpression` is a kind of `Expression`, so all semantic constraints for `Expressions` also apply to
`BooleanExpressions`. In addition, the `checkBooleanExpressionSpecialization` constraint requires that
`BooleanExpressions` specialize the base `BooleanExpression` `Performances::booleanEvaluations` (see
9.2.6.2.2), which is a specialization of `Performances::evaluations`.

```kerml
expr p : P subsets Performances::booleanEvaluations {
    in x : ScalarValues::Integer redefines P::x;
    return redefines P::x, Performance::BooleanEvaluation::result;
}
```

An `Invariant` is a kind of `BooleanExpression`, so all semantic constraints for `BooleanExpressions` also
apply to `Invariants`. In addition, the `checkInvariantSpecialization` constraint requires that `Invariants`
specialize *either* the `BooleanExpression` `Performances::trueEvaluations` (see 9.2.6.2.17) or, if the
`Invariant` is negated, the `BooleanExpression` `Performances::falseEvaluations` (see 9.2.6.2.6), both of
which are specializations of `Performances::booleanEvaluations`. The `BooleanExpression`
`trueEvaluations` has its `result` bound to `true`, while the `BooleanExpression` `falseEvaluations` has its
`result` bound to `false`.

```kerml
inv true i1 subsets Performances::trueEvaluations {
    p(3)
}
inv false i2 subsets Performances::falseEvaluations {
    p(-3)
}
```

### 8.4.4.9 Expressions Semantics

**Abstract syntax reference:** 8.3.4.8

#### 8.4.4.9.1 Null Expressions

The `checkNullExpressionSpecialization` constraint requires that `NullExpressions` specialize the
`Expression` `Performances::nullEvaluations` (see 9.2.6.2.13), which is typed by the `Function`
`Performances::NullEvaluation` (see 9.2.6.2.12). The `result` `parameter` of `NullEvaluation` has
multiplicity `0..0`, which means that a `NullExpression` always produces an empty result. The general semantic
constraints for `Expressions` (see 8.4.4.8) also apply to `NullExpressions`.

#### 8.4.4.9.2 Literal Expressions

The `checkLiteralExpressionSpecialization` constraint requires that `LiteralExpressions` specialize the
`Expression` `Performances::literalEvaluations` (see 9.2.6.2.9), which is typed by the `Function`
`Performances::LiteralEvaluation` (see 9.2.6.2.8). The `result` `parameter` of `LiteralEvaluation` has
multiplicity `1..1` and is typed by `Base::DataValue` (see 9.2.2.2.2). This means that a `LiteralExpression`
always produces a single `DataValue` as its result. What value is actually produced depends on the kind of
`LiteralExpression`. The general semantic constraints for `Expressions` (see 8.4.4.8) also apply to
`LiteralExpressions`.

With the exception of `LiteralInfinity`, each kind of `LiteralExpression` has a `value` property typed by a
UML primitive type [UML, MOF]. The result produced by such a `LiteralExpression` is given by this `value`.
`LiteralInfinity` does not have a `value` property, because its result is always "infinity" (written `*` in the KerML
textual notation; see 8.2.5.8.4), which is a number from the `DataType` `ScalarValues::Positive` that is greater
than all the integers.

*Note.* In the abstract syntax, the `value` property of `LiteralRational` has type `Real` (see 8.3.4.8.13), because
that is the available UML/MOF primitive type. However, only the rational-number subset of the real numbers can be
represented using a finite literal. So the result of a `LiteralRational` is actually always classified in the KerML
`DataType` `Rational`.

#### 8.4.4.9.3 Feature Reference Expressions

There is no specific specialization requirement for a `FeatureReferenceExpression`. However, the general
`checkExpressionSpecialization` constraint (see 8.4.4.8) requires that a `FeatureReferenceExpression`
specialize `Performances::Evaluation` (see 9.2.6.2.4). All other general semantic constraints for `Expressions`
(see 8.4.4.8) also apply to `FeatureReferenceExpressions`.

A `FeatureReferenceExpression` is parsed with a non-owning `Membership` relationship to its `referent`
`Feature` (see 8.2.5.8.3). The `checkFeatureReferenceExpressionBindingConnector` constraint then
requires that there be a `BindingConnector` between this member `Feature` and the result parameter of the
`FeatureReferenceExpression`. The `checkFeatureReferenceExpressionResultSpecialization`
constraint further requires that the result parameter also subset the `Feature`. While this subsetting is technically
implied by the semantics of the `BindingConnector` (see 8.4.4.6), including the `Subsetting` relationship allows
for simpler static type checking of the result of the `FeatureReferenceExpression`.

Given the above, a `FeatureReferenceExpression` whose `referent` is a `Feature` `f` is semantically equivalent
to the `Expression`

```kerml
expr subsets Performances::evaluations {
    alias for f;
    return result
        redefines Performances::Evaluation::result
        subsets f;
    member binding result = f;
}
```

A body `Expression` (see 8.2.5.8.3) is parsed as a `FeatureReferenceExpression` that contains the
`Expression` body as its owned `referent`. That is, a body `Expression` of the form

```kerml
{ body }
```

is semantically equivalent to

```kerml
expr subsets Performances::evaluations {
    expr e subsets Performances::evaluation { body }
    return result
        redefines Performances::Evaluation::result
        subsets e;
    binding result = e;
}
```

This means that the `result` of the `Expression` is the `Evaluation` of the body `Expression` itself, rather than the
result of actually evaluating the body. If and when this `Evaluation` actually occurs can then be further constrained,
e.g., within an invoked `Function` for which the body `Expression` is an argument (as done, for example, by
`ControlFunctions` -- see 8.4.4.9.6).

#### 8.4.4.9.4 Constructor Expressions

A `ConstructorExpression` of the form `new T(e1, e2, ...)`, where `T` is the name of a `Type` and `e1`, `e2`, ...
are argument `Expressions`, is parsed with a `Membership` to `T` (its `instantiatedType`) and a `result`
`parameter` having nested `ownedFeatures` of the `result` that have `FeatureValue` relationships to the
`arguments` (see 8.2.5.8.3). The `checkConstructorExpressionSpecialization` constraint requires that
`ConstructorExpressions` specialize the `Expression` `Performances::constructorEvaluations` (see
9.2.6.2.3), which subsets `Performances::evaluations` (see 9.2.6.2.5), redefining its `result` `parameter` to
have multiplicity `1..1`. This means that a `ConstructorExpression` always produces a single value as its result.

In addition, the `checkConstructorExpressionResultSpecialization` constraint requires that the `result` of
a `ConstructorExpression` specialize the `instantiatedType` (via a `FeatureTyping` if the
`instantiatedType` is a `Classifier` or a `Subsetting` if it is a `Feature`), and the
`checkConstructorExpressionResultFeatureRedefinition` constraint requires that the nested
`ownedFeatures` of the `result` each redefine a public `feature` of the `instantiatedType`. Thus, a
`ConstructorExpression` of this form is semantically equivalent to

```kerml
expr subsets Performances::constructorEvaluations {
    alias of T; // If T is a feature chain, this is an OwningMembership.
    return result : T [1] redefines Performances::constructorEvaluations::result {
        feature a redefines T::a = e1;
        feature b redefines T::b = e2;
        ...
    }
}
```

where, in the positional-argument notation, the `features` of `T` are defined in order. If the named-argument notation
`new T(a = e1, b = e2, ...)` is used, then the nested `ownedFeatures` redefine the named `features` of `T`,
regardless of order.

The semantic constraints for `FeatureValues` (see 8.4.4.11) then require that each nested `ownedFeature` is bound
to the result of the corresponding `Expression` (i.e., `a` is bound to `e1.result`, etc.). Thus, a
`ConstructorExpression` represents an `Evaluation` that results in a single instance of `Type` `T` whose `features`
have values determined by the results of the argument `Expressions`.

#### 8.4.4.9.5 Invocation Expressions

An `InvocationExpression` of the form `F(e1, e2, ...)`, where `F` is the name of a `Function` and `e1`, `e2`, ...
are argument `Expressions`, is parsed with a `Membership` to `F` (its `instantiatedType`) and input `parameters`
that have `FeatureValue` relationships to the `arguments` (see 8.2.5.8.3). The general semantic constraints for
`Expressions` (see 8.4.4.8.2) also apply to `InvocationExpressions`. In addition, the
`checkInvocationExpressionSpecialization` constraint requires that an `InvocationExpression`
specialize its `instantiatedType` (via a `FeatureTyping`). Thus, an `InvocationExpression` of this form is
semantically equivalent to

```kerml
expr : F subsets Performances::evaluations {
    alias of F;
    feature a redefines F::a = e1;
    feature b redefines F::b = e2;
    ...
    return result redefines F::result;
}
```

If, instead of a `Function` `F`, the `instantiatedType` is a non-`Function` `Behavior` `B`, then `B` has no `result`
parameter for the `InvocationExpression` `result` to redefine. Instead, the
`checkInvocationExpressionBehaviorBindingConnector` constraint requires that the
`InvocationExpression` have an owned `BindingConnector` between itself and its `result` parameter -- that is,
the `InvocationExpression` evaluates, as an `Expression`, to itself, as an instance of `B`. In addition, the
`checkInvocationExpressionBehaviorResultSpecialization` constraint requires that the `result`
parameter of the expression specialize the `instantiatedType`.

```kerml
expr e : B subsets Performances::evaluations {
    alias of B;
    feature a redefines B::a = e1;
    feature b redefines B::b = e2;
    ...
    return result : B redefines Performances::evaluations::result;
    binding result = e;
}
```

Note that, in this case, the derived `function` of the `InvocationExpression` will always be
`Performances::Evaluation`, the type of `Performances::evaluations`.

If the `instantiatedType` is a `Feature`, the semantics are similar, except that the `InvocationExpression` has a
`Subsetting` relationship with the `instantiatedType`, instead of a `FeatureTyping` relationship. If the `Feature`
is typed by a `Function`, then the `InvocationExpression` is effectively treated as an invocation of that
`Function`. If the `Feature` is typed by a non-`Function` `Behavior`, then the `InvocationExpression` is treated as a
`Performance` of that `Behavior`, returning itself as the result. Note also that, if the `instantiatedType` is a
`Feature` with `chainingFeatures`, then it will be related to the `InvocationExpression` by an
`OwningMembership` (but not a `FeatureMembership`).

#### 8.4.4.9.6 Operator Expressions

An `OperatorExpression` is an `InvocationExpression` in which the invoked `Function` is identified by an
`operator` symbol. The `instantiatedType` of an `OperatorExpression` is specially derived to be the
`Function` that is the resolution of the `operator` symbol as a name in the first one of the library `Packages`
`BaseFunctions`, `DataFunctions` or `ControlFunctions`. The general semantic constraints for `Expressions`
(see 8.4.4.9) also apply to `OperatorExpressions`.

With the exception of operators for `ControlFunctions` (see below), the concrete syntax for
`OperatorExpressions` (see 8.2.5.8.1) is thus essentially just a special surface syntax for
`InvocationExpressions` of the standard library `Functions` identified by their `operator` symbols. For
example, a unary `OperatorExpression` such as

```kerml
not expr
```

is equivalent to the `InvocationExpression`

```kerml
DataFunctions::'not' (expr)
```

and a binary `OperatorExpression` such as

```kerml
expr_1 + expr_2
```

is equivalent to the `InvocationExpression`

```kerml
DataFunctions::'+' (expr_1, expr_2)
```

where these `InvocationExpressions` are then semantically interpreted as in 8.4.4.9.5.

The `+` and `-` operators are the only operators that have both unary and binary usages. However, the corresponding
library `Functions` have optional `0..1` multiplicity on their second `parameters`, so it is acceptable to simply not
provide an input for the second `argument` when mapping the unary usages of these operators.

`Functions` in the library models `BaseFunctions` and `ScalarFunctions` are extensively specialized in other
library models to constrain their `parameter` `types` (e.g., the `Package` `RealFunctions` constrains `parameter`
`types` to be `Real`, etc.). The result values the evaluation of such a `Function` shall be determined by the most
specialized of its subtypes that is consistent with the `types` of the dynamic result values from evaluating its
argument `Expressions`.

#### 8.4.4.9.7 Control Functions

Certain `OperatorExpressions` denote invocations of `Functions` in the `ControlFunctions` library model (see
9.4.17) that have one or more `parameters` that are `Expressions`. In the concrete syntax for such
`OperatorExpressions` (see 8.2.5.8.1), the `arguments` corresponding to these `parameters` are parsed as if they
were body `Expressions` (as described in 8.4.4.9.3), so they can effectively be passed without being immediately
evaluated.

The second and third arguments of the ternary conditional test operator `if` are for `Expression` parameters.
Therefore, the notation for a conditional test `OperatorExpression` of the form

```kerml
if expr_1 ? expr_2 else expr_3
```

is parsed as

```kerml
ControlFunctions::'if' (expr_1, { expr_2 }, { expr_3 })
```

The second arguments of the binary conditional logical operators `and`, `or`, and `implies` are for `Expression`
parameters. Therefore, the notation for a conditional logical `OperatorExpression` of the form

```kerml
expr_1 and expr_2
```

is parsed as

```kerml
ControlFunctions::'and' (expr_1, { expr_2 })
```

and similarly for `or` and `implies`.

A `FeatureChainExpression` is an `OperatorExpression` whose operator corresponds to the `Function`
`ControlFunctions::'.'`. This `Function` has a single `parameter` called `source`, but this `parameter` has a
nested `Feature` called `target`. A `FeatureChainExpression` is parsed with an argument `Expression` for the
`source` `parameter` and, additionally, a non-parameter `Membership` for its `target` `Feature`, which is an alias
`Membership` if the `target` `Feature` is not a chain and an `OwningMembership` if the `target` `Feature` is a chain.

The `checkFeatureChainExpressionTargetRedefinition` constraint requires that the `source` `parameter` of
the `FeatureChainExpression` have a nested `Feature` that redefines
`ControlFunctions::'.'::source::target`, and the
`checkFeatureChainExpressionSourceTargetRedefinition` requires that this nested `Feature` also redefine
the `target` `Feature`. The `checkFeatureChainExpressionResultSpecialization` constraint requires that
the `result` `parameter` of a `FeatureChainExpression` subset the feature chain consisting of the redefining
`source` `parameter` of the `FeatureChainExpression` and the nested `Feature` of that `parameter`.

Given the above, a `FeatureChainExpression` of the form

```kerml
src.f
```

(where `src` is an `Expression`) is semantically equivalent to the `Expression`

```kerml
expr : ControlFunctions::'.' subsets Performances::evaluations {
    feature redefines ControlFunctions::'.'::source = src {
        feature redefines ControlFunctions::'.'::source::target
            redefines f;
    }
    alias for f;
    return subsets source.f;
}
```

A `FeatureChainExpression` whose `target` `Feature` is a `Feature` chain, of the form

```kerml
src.f.g.h
```

is semantically equivalent to the `Expression`

```kerml
expr : ControlFunctions::'.' subsets Performances::evaluations {
    feature redefines ControlFunctions::'.'::source = src {
        feature redefines ControlFunctions::'.'::source::target
            redefines tgt;
    }
    feature tgt chains f.g.h;
    return subsets source.tgt;
}
```

The performance of the `Function` `'.'` then results in the effective chaining of the value of its `source` `parameter`
(which will be the result of the argument `Expression` of the `FeatureChainExpression`) and the
`source::target` `Feature` (which will be the `target` `Feature` of the `FeatureChainExpression`).

#### 8.4.4.9.8 Metadata Access Expressions

The `checkMetadataAccessExpressionSpecialization` constraint requires that a
`MetadataAccessExpression` specialize the `Expression` `Performances::metadataAccessEvaluations`
(see 9.2.6.2.11), which is typed by the `Function` `Performances::MetadataAccessEvaluation` (see
9.2.6.2.10). The `result` `parameter` of `MetadataAccessEvaluation` is ordered and typed by
`Metaobjects::Metaobject` (see 9.2.16.2.1). The general semantic constraints for `Expressions` (see 8.4.4.9)
also apply to `MetadataAccessExpressions`.

A `MetadataAccessExpression` evaluates to an ordered set of `Metaobjects`, which are determined as follows:

- A `Metaobject` representing each `MetadataFeature` (see 8.3.4.12.3) owned by the
  `referencedElement` of the `MetadataAccessExpression` that has the `referencedElement` as an
  `annotatedElement`, in the order that the `MetadataFeatures` appear in the model. Each of these
  `Metaobjects` is an instance of the `metaclass` of the corresponding `MetadataFeature`, with the
  `features` of each instance having values determined by evaluating the bound `Expressions` of the
  `features` in the `MetadataFeature` as model-level evaluable `Expressions` (see below).
- Followed by a `Metaobject` that is an instance of the `Metaclass` from the reflective KerML abstract
  syntax library model (see 9.2.17) corresponding to the MOF metaclass of the `referencedElement` of
  the `MetadataAccessExpression`, with `features` having values corresponding to the values of the
  MOF properties for the `referencedElement`.

Note that every `Metaclass` is required to specialize `Metaobjects::Metaobject`, so the typing of the results of
a `MetadataAccessExpression` is consistent.

For example, the `MetadataAccessExpression` `C.metadata` for the following `referencedElement`:

```kerml
class C {
    metadata M;
}
```

would evaluate to two `Metaobjects`: an instance of the `Metaclass` `M` representing the `MetadataFeature`
annotation on `C` and an instance of `KerML::Class` representing the `referencedElement` `C` itself.

#### 8.4.4.9.9 Model-Level Evaluable Expressions

A model-level evaluable `Expression` is an `Expression` that can be evaluated using metadata available within a
model itself. This means that the evaluation rules for such an `Expression` can be defined entirely within the
abstract syntax. A model-level evaluable `Expression` is evaluated on a given `targetElement` (see 8.4.4.13 and
8.4.4.14 for the targets used in the case of metadata `values` and `filterConditions`, respectively), using the
`Expression::evaluate` operation, resulting in an ordered list of `Elements`. The rules for this operation are
specified in the abstract syntax (see 8.3.4.8) and are summarized below:

1. A `NullExpression` evaluates to the empty list.
2. A `LiteralExpression` evaluates to itself.
3. A `FeatureReferenceExpression` is evaluated by first determining a `valueExpression` for the
   `referent`:
    - If the `targetElement` is a `Type` that has a `feature` that is the `referent` or (directly or
      indirectly) redefines it, then use the `valueExpression` of the `FeatureValue` for that
      `feature` (if any).
    - Else, if the `referent` has no `featuringTypes`, then use the `valueExpression` of the
      `FeatureValue` for the `referent` (if any).

   Then:
    - If such a `valueExpression` exists, the `FeatureReferenceExpression` evaluates to the
      result of evaluating that `Expression` on the `target`.
    - Else, if the `referent` is not an `Expression`, the `FeatureReferenceExpression` evaluates
      to the `referent`.
    - Else, the `FeatureReferenceExpression` evaluates to the empty list.
4. A `MetadataAccessExpression` evaluates to the `ownedElements` of the `referencedFeature` that
   are `MetadataFeatures` and have the `referencedElement` as an `annotatedElement`, plus a
   `MetadataFeature` whose `annotatedElement` is the `referencedElement`, whose `metaclass` is the
   reflective `Metaclass` in the KerML library model (see 9.2.17) corresponding to the MOF class of the
   `referencedElement`, and whose `ownedFeatures` are bound to the values of the MOF properties of the
   `referencedElement`.
5. An `InvocationExpression` evaluates to an application of its `function` to argument values
   corresponding to the results of evaluating each of the argument `Expressions` of the
   `InvocationExpression`, with the correspondence as given below.

Every `Element` in the list resulting from a model-level evaluation of an `Expression` according to the above rules
will be either a `LiteralExpression` or a `Feature` that is not an `Expression`. If each of these `Elements` is
further evaluated according to its regular instance-level semantics, then the resulting list of instances will
correspond
to the result that would be obtained by evaluating the original `Expression` using its regular semantics on the
referenced metadata of the `targetElement`.

### 8.4.4.10 Interactions Semantics

**Abstract syntax reference:** 8.3.4.9

#### 8.4.4.10.1 Interactions

An `Interaction` is both an `Association` and a `Behavior`, and, therefore, the semantic constraints for both
`Associations` (see 8.4.4.5) and `Behaviors` (see 8.4.4.7) apply. In particular, the
`checkAssociationSpecialization` constraint requires that an `Interaction` specialize `Links::Link` (see
9.2.3.2.3), or, if it is a binary `Interaction` (with exactly two end `Features`), the
`checkAssociationBinarySpecialization` constraint requires that it specializes `Links::BinaryLink` (see
9.2.3.2.1). And the `checkBehaviorSpecialization` constraint requires that it also specialize
`Performances::Performance` (see 9.2.6.2.14).

These constraints require an N-ary `Interaction` to have the form (with implied relationships included)

```kerml
interaction I specializes Link::Link, Performances::Performance {
    end feature e1 subsets Links::Link::participant;
    end feature e2 subsets Links::Link::participant;
    ...
    end feature eN subsets Links::Link::participant;
}
```

with a binary `Interaction` having the form

```kerml
interaction B specializes Links::BinaryLink, Performances::Performance {
    end feature e1 redefines Links::BinaryLink::source;
    end feature e2 redefines Links::BinaryLink::target;
}
```

The `checkFeatureEndRedefinition` and `checkFeatureParameterRedefinition` constraints also apply to
`Interactions`.

```kerml
interaction I1 specializes Links::BinaryLink, Performances::Performance {
    in feature x1;
    out feature y1;
    end feature e1;
    end feature f1;
}
interaction I2 specializes I1 {
    in feature x2 redefines x1;
    out feature y2 redefines y1;
    end feature e2 redefines e1;
    end feature f2 redefines f1;
}
```

#### 8.4.4.10.2 Flows

A `Flow` is both a `Connector` and a `Step` and, therefore, the semantic constraints for both `Connectors` (see
8.4.4.6) and `Steps` (see 8.4.4.7) also apply to `Flows`. In addition, the `checkFlowSpecialization` constraint
requires that `Flows` specialize `Transfers::transfers` (see 9.2.7.2.11). In addition, if the `Flow` has
`ownedEndFeatures` (see below), then it must specialize `Transfers::flowTransfers` (see 9.2.7.2.4).

The textual notation for a `Flow`, of the form

```kerml
flow of i : T from f1.f1_out to f2.f2_in;
```

is parsed with `i : T` as a `PayloadFeature` and having two `FlowEnds`, one referencing `f1` with an owned `Feature`
redefining `f1_out` and one referencing `f2` with an owned `Feature` redefining `f2_in` (see 8.2.5.9.2). A
`PayloadFeature` is just a `Feature` owned by a `Flow` that has the special semantic constraint
`checkPayloadFeatureRedefinition` that requires that a `PayloadFeature` redefine
`Transfers::Transfer::payload` (see 9.2.7.2.9). A `FlowEnd` is an end `Feature` owned by a `Flow` that is
required to have a single `ownedFeature`. The general `checkFeatureEndRedefinition` constraint (see 8.4.4.6)
requires that the two `FlowEnds` of a `Flow` redefine `Transfers::Transfer::source` and
`Transfers::Transfer::target` (see 9.2.7.2.9), respectively. The
`checkFeatureFlowFeatureRedefinition` constraint then requires that the `ownedFeatures` of the `FlowEnds`
redefine `Transfer::source::sourceOutput` or `Transfer::target::targetInput`.

```kerml
flow subsets Transfers::flowTransfers {
    // PayloadFeature
    feature i : T redefines Transfers::Transfer::item;
    // First FlowEnd
    end feature redefines Transfers::Transfer::source references f1 {
        feature redefines Transfers::Transfer::source::sourceOutput, f1_out;
    }
    // Second FlowEnd
    end feature references f2 redefines Transfers::Transfer::target {
        feature redefines Transfers::Transfer::target::targetInput, f2_in;
    }
}
```

A `SuccessionFlow` is semantically the same, except that the `checkSuccessionFlowSpecialization`
constraint requires that it specialize `Transfers::flowTransfersBefore` (see 9.2.7.2.5), which means that the
`SuccessionFlow` additionally has the semantics of a `Succession` between its `source` and `target` (see 8.4.4.6.3
on the semantics of `Successions`).

### 8.4.4.11 Feature Values Semantics

**Abstract syntax reference:** 8.3.4.10

A `FeatureValue` is a kind of `OwningMembership` between a `Feature` and an `Expression`. Note that the
`FeatureValue` relationship is *not* a `Featuring` relationship, so its `featureWithValue` (that is, its owning
`Feature`) is *not* the `featuringType` of the `valueExpression`. Instead, the
`checkExpressionFeaturingType` constraint requires that the `valueExpression` have the same
`featuringTypes` as the `featureWithValue`. Most commonly, if the `featureWithValue` is an `ownedFeature`
of a `Type`, this means that the `Expression` will have that `Type` as its `featuringType`.

The `checkFeatureValuationSpecialization` constraint requires that, if the `featureWithValue` has no
explicit `ownedSpecializations` and is not directed, then it subsets the `result` `parameter` of the `value`
`Expression`. This reflects the semantics that the values of the `featureWithValue` is determined by the `value`
`Expression`, giving the `featureWithValue` an implied typing that is useful for static type checking. On the other
hand, if the `featureWithValue` has `ownedSpecializations` or is directed, then its static typing can be
considered determined by its declaration excluding the `FeatureValue` (but including any implied
`Specializations`), which should then be validated against the typing of the `result` of the `valueExpression`.

If the `FeatureValue` has `isDefault = false`, the `checkFeatureValueBindingConnector` constraint
requires that its `featureWithValue` have an `ownedMember` that is a `BindingConnector` between that `Feature`
and the `result` `parameter` of the `valueExpression` of the `FeatureValue`. In addition, if the `FeatureValue`
has `isInitial = false`, then the `featuringTypes` of this `BindingConnector` must be the same as those of
the `featureWithValue`. Most commonly, if the `featureWithValue` is an `ownedFeature` of a `Type`, then the
`BindingConnector` will have that `Type` as its `featuringType`. Other general semantic constraints for
`Connectors` (see 8.4.4.6) also apply to the `BindingConnector` required for a `FeatureValue`.

Given the above, the textual notation for a `FeatureValue` with `isDefault = false` and `isInitial = false`,
of the form

```kerml
type T {
    feature f = expr;
}
```

is semantically equivalent to

```kerml
type T {
    feature f {
        member expr e featured by T { ... }
        member binding b featured by T of f = e.result;
    }
}
```

where `e` is the semantic interpretation of `expr` as described in 8.4.4.9.

If a `FeatureValue` has `isDefault = false` but `isInitial = true`, then the
`validateFeatureValueIsInitial` constraint requires that the `featureWithValue` of the `FeatureValue`
have `isVariable = true`, and the `checkFeatureValueBindingConnector` constraint requires different
`featuringTypes` for the `BindingConnector` than when `isInitial = false`. In this case, the
`BindingConnector` must be featured by the `startShot` (see 9.2.4.2.13) of the `that` reference of its owning
`featureWithValue` (see 9.2.2.2.7). Note that this is only possible if the `featureWithValue` is featured by a
`Class` (see also 8.4.4.3 on the semantics of `Classes`). Most commonly, if the `featureWithValue` is an
`ownedFeature` of a `Class` or a `Feature` typed by a `Class`, then the `BindingConnector` will have the
`startShot` of that `Class` as its `featuringType`, meaning that the binding only applies initially, that is, at the
very
start of an `Occurrence` that is an instance of the `Class`.

Thus, the textual notation for a `FeatureValue` with `isDefault = false` and `isInitial = true`, of the form

```kerml
class C {
    var feature f := expr;
}
```

is semantically equivalent to (see also 8.4.4.3 on the semantics of variable features)

```kerml
class C specializes Occurrences::Occurrence {
    feature f specializes Base::things featured by C_snapshots {
        member feature C_snapshots
            redefines Occurrences::snapshots
            featured by C;
        member expr e featured by C_snapshots { ... }
        member binding b featured by that.startShot of f = e.result;
    }
}
```

(note that the `that` is considered to be implicitly typed by `Occurrence` in this case).

If a `FeatureValue` has `isDefault = true`, then no `BindingConnector` is required for the
`featureWithValue` at its point of declaration. Instead, the
`checkInvocationExpressionDefaultValueBindingConnector` constraint requires that an
`InvocationExpression` own a `BindingConnector` between the `featureWithValue` and `valueExpression`
of any `FeatureValue` that is the effective default value for a `Feature` of the invoked `Type` of the
`InvocationExpression`, where *effective default value* is defined as follows:

- If the `Feature` has an owned `FeatureValue` with `isDefault = true`, then this is its effective default
  value.
- If the `Feature` does not have an owned `FeatureValue`, but the set of effective default values of the
  `Features` it redefines has a single unique member, then this is the effective default value of the original
  `Feature`.
- Otherwise the `Feature` does not have an effective default value.

For example, given the `Type` declaration

```kerml
type T {
    feature f default = e;
}
```

a binding for `f` is included for the invocation `T()`, which is then semantically equivalent to

```kerml
expr : T {
    binding f = f::e.result;
}
```

where `f::e.result` is the `result` of the `valueExpression` from the default `FeatureValue`. On the other hand,
for the invocation `T(f = 1)`, the `Feature` `f` will be bound to 1 rather than the `FeatureValue` default. A similar
construction applies for `FeatureValues` with `isDefault = true` and `isInitial = true`. (See also 8.4.4.9 on
the general semantics of `InvocationExpressions`.)

### 8.4.4.12 Multiplicities Semantics

**Abstract syntax reference:** 8.3.4.11

#### 8.4.4.12.1 Multiplicities

A `Multiplicity` is a kind of `Feature`, so the general semantics of `Features` (see 8.4.3.4) also apply to a
`Multiplicity`. In addition, the `checkMultiplicitySpecialization` constraint requires that a
`Multiplicity` specialize the `Feature` `Base::naturals` (see 9.2.2.2.5), which is typed by the `DataType`
`ScalarValues::Natural` (see 9.3.2.2.4). This constraint effectively requires that the co-domain of a
`Multiplicity` be a subset of the natural numbers, which can be specified by reference to a library `Multiplicity`
(such as `Base::exactlyOne` or `Base::oneToMany`) or using a `MultiplicityRange` from the Kernel layer (see
8.4.4.12.2).

The `validateTypeOwnedMultiplicity` constraint requires that a `Type` have at most one `ownedMember` that is a
`Multiplicity`. If a `Type` has such an owned `Multiplicity`, then it is the `typeWithMultiplicity` of that
`Multiplicity`. The value of the `Multiplicity` is then the *cardinality* of its `typeWithMultiplicity` and,
therefore, the `type` (co-domain) of the `Multiplicity` restricts that cardinality. The cardinality of a `Type` is
defined generally as follows:

- For a `Classifier`, the cardinality is the number of basic instances of the `Classifier`, that is, those
  instances that represent the things classified by the `Classifier` and are not instances of any subtype of
  the `Classifier` that is a `Feature`.
- For a `Feature`, the cardinality is the number of values of the `Feature` for any specific featuring instance
  (where duplicate features are included in the count, if the `Feature` is non-unique).

However, there are special rules for the semantics of `Multiplicity` for end `Features` (see 8.4.4.5).

The `checkMultiplicityTypeFeaturing` constraint requires that a `Multiplicity` with a `Feature` as its
`owningNamespace` have the same `featuringTypes` (domain) as that `Feature`, and, otherwise, have no
`featuringTypes`. In particular, if a `Multiplicity` is owned by a `Feature` that has an `owningType`, then the
`featuringType` of the `Multiplicity` is the `owningType` of its owning `Feature`. This means that the
`Multiplicity` has a value for each instance of the `featuringType` that is the cardinality of the instances of its
owning `Feature` that are featured by that same instance of the `featuringType`.

```kerml
classifier C1 {
    feature f {
        // Implied TypeFeaturing by C1.
        // Gives the cardinality of the values of f for each
        // instance of C1 (which is constrained to be 1).
        multiplicity subsets Base::exactlyOne;
    }
}
```

If a `Type` does not have an owned `Multiplicity`, but has `ownedSpecializations`, then its cardinality is
constrained by the `Multiplicities` for all of the general `Types` of those `ownedSpecializations` (i.e., its direct
supertypes). In practice, this means that the effective `Multiplicity` of the `Type` is the most restrictive
`Multiplicity` of its direct supertypes.

```kerml
classifier C2 {
    feature f {
        multiplicity subsets Base::exactlyOne;
    }
    feature g {
        multiplicity subsets Base::oneToMany;
    }
    // The multiplicities exactlyOne and oneToMany both apply
    // to h, which means that, effectively, it has a multiplicity
    // of exactlyOne.
    feature h subsets f, g;
}
```

#### 8.4.4.12.2 Multiplicity Ranges

A `MultiplicityRange` is a `Multiplicity` whose co-domain is given as an inclusive range of values of the type
`Natural`. It thus constrains the cardinality of its `typeWithMultiplicity` to be within this range. A
`MultiplicityRange` of the form

```kerml
[expr_1 .. expr_2]
```

represents the range of values that are greater than or equal to the result of the `Expression` `expr_1` and less than
or
equal to the result of the `Expression` `expr_2`. Note that all other `Natural` values are less than the value of `*`,
representing positive infinity, so the `MultiplicityRange` `[0..*]` is the range of all values of `Natural` (that is,
no restriction on cardinality).

A `MultiplicityRange` having only a single expression:

```kerml
[expr]
```

is interpreted in one of the following ways:

- If `expr` evaluates to `*`, then it is equivalent to the range `[0..*]` (i.e., the entire extent of `Natural`).
- Otherwise, it is equivalent to `[expr..expr]` (that is, the cardinality is restricted to the single value given
  by the result of `expr`).

*Note.* The KerML textual notation grammar only allows `LiteralExpressions` and
`FeatureReferenceExpressions` as the `boundExpressions` in a `MultiplicityRange` (see 8.2.5.11).
However, the abstract syntax allows arbitrary `Expressions` (see 8.3.4.11).

The `checkMultiplicityRangeExpressionTypeFeaturing` constraint requires that the `boundExpressions`
of a `MultiplicityRange` have the same `featuringTypes` as the `MultiplicityRange`. The `featuringTypes`
of a `MultiplicityRange` are determined by the `checkMultiplicityTypeFeaturing` constraint (8.4.4.12.1).

If the `MultiplicityRange` has an `owningNamespace` that is not a `Feature`, then it has no `featuringTypes`, so
its domain is implicitly `Base::Anything`, and its `boundExpressions` can only reference other `Features` in that
context.

```kerml
package P {
    // Implicitly featured by Anything.
    feature n : ScalarValues::Natural;
    classifier C3 {
        // An ownedMember, not an ownedFeature.
        // Implicitly featured by Anything.
        // Implied Subsetting of Base::naturals.
        multiplicity [P::n];
    }
}
```

If the `MultiplicityRange` has an `owningNamespace` that is a `Feature`, then it is required to have
`featuringTypes` that are the same as the owning `Feature`. In particular, if its owning `Feature` has an
`owningType`, then the `featuringType` of the `MultiplicityRange` (and its `boundExpressions`) is the
`owningType` of its owning `Feature`.

```kerml
classifier C4 {
    feature n : ScalarValues::Natural;
    feature m : Member {
        // Implied TypeFeaturing by C4.
        // Implied Subsetting of Base::naturals.
        multiplicity [1..C4::n];
    }
}
```

### 8.4.4.13 Metadata Semantics

**Abstract syntax reference:** 8.3.4.12

#### 8.4.4.13.1 Metaclasses

The `checkMetaclassSpecialization` constraint requires that `Metaclasses` specialize the base `Metaclass`
`Metaobjects::Metaobject` (see 9.2.16.2.1). A `Metaclass` is a kind of `Structure` (see 8.4.4.4), but its
instances are `Metaobjects` that are part of the structure of a model itself, rather than as an instance in the system
represented by the model. The KerML library model is a reflective model of the MOF abstract syntax for KerML,
containing one KerML `Metaclass` corresponding to each MOF metaclass in the abstract syntax model (see 9.2.17
for more details on the relationship between the KerML model and the abstract syntax).

#### 8.4.4.13.2 Metadata Features

A `MetadataFeature` is both a `Feature` typed by a `Metaclass` and an `AnnotatingElement` that annotates other
`Elements` in a model. The `checkMetadataFeatureSpecialization` requires that `MetadataFeatures`
specialize the `Feature` `Metaobjects::metaobjects` (see 9.2.16.2.2). At a meta-level, a `MetadataFeature`
can be treated as if the reflective `Metaclasses` of its `annotatedElements` were its `featuringTypes`. In this
case, the `MetadataFeature` defines a map from its `annotatedElements`, as instances of their `Metaclasses`, to
a single instance of the `metaclass` of the `MetadataFeature`.

Further, a model-level evaluable `Expression` is an `Expression` that can be evaluated using metadata available
within a model itself (see 8.4.4.9). If a model-level evaluable `Expression` is evaluated on such metadata according
to the regular semantics of `Expressions`, then the result will correspond to the static evaluation of the
`Expression` within the model. Therefore, if a `MetadataFeature` is instantiated as above, the binding of its
`features` to the results of evaluating the model-level evaluable `valueExpressions` of its `FeatureValues` can
be interpreted according to the regular semantics of `FeatureValues` (see 8.4.4.11) and `BindingConnectors`
(see 8.4.4.6).

When a `valueExpression` is model-level evaluated (as described in 8.4.4.9), its target is the `MetadataFeature`
that owns the `featureWithValue`. This means that the `valueExpression` for a nested `Feature` of a
`MetadataFeature` may reference other `Features` of the `MetadataFeature`, as well as `Features` with no
`featuringTypes` or `Anything` as a `featuringType`.

#### 8.4.4.13.3 Semantic Metadata

A semantic `MetadataFeature` is one that directly or indirectly specializes `Metaobjects::SemanticMetadata`
(see 9.2.16.2.3). It is used to introduce a user-defined specialization constraint on the `Type` annotated by the
`MetadataFeature`. `SemanticMetadata` has the `Feature` `baseType` typed by the reflective `Metaclass`
`KerML::Type` (see 9.2.17) that is redefined by a semantic `MetadataFeature`. The target of the effective
specialization constraint defined by a semantic `MetadataFeature` is determined by the `valueExpression` bound
to its `baseType` `Feature` using a `FeatureValue` (see 8.4.4.11), which is evaluated as a model-level evaluable
`Expression` (see 8.4.4.9).

Specifically, for each semantic `MetadataFeature` annotating a `Type`, the
`checkMetadataFeatureSemanticSpecialization` constraint requires that the annotated `Type` directly or
indirectly specialize the `Type` bound to the `baseType` of the `MetadataFeature`, *unless* the annotated `Type` is a
`Classifier` and the `baseType` is a `Feature`. For the case when the `Type` is a `Classifier` and the `baseType` is
a `Feature`, the constraint requires that the annotated `Classifier` directly or indirectly specialize each `type` of the
`baseType` `Feature`.

### 8.4.4.14 Packages Semantics

**Abstract syntax reference:** 8.3.4.13

`Packages` do not have instance-level semantics (they do not affect instances).

The `filterConditions` of a `Package` are model-level evaluable `Expressions` that are evaluated as described in
8.4.4.9. All `filterConditions` are checked against every `Membership` that would otherwise be imported into
the `Package` if it had no `filterConditions`. A `Membership` shall be imported into the `Package` if and only if
every `filterCondition` evaluates to `true` either with no `targetElement`, or with any `MetadataFeature` of the
`memberElement` of the `Membership` as the `targetElement`.

---

*This document is based on the KerML Specification v1.0*
