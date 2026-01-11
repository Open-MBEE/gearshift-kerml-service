package org.openmbee.gearshift.kerml

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.metamodel.*

/**
 * Defines the KerML metamodel using the Gearshift framework.
 * This is a starting point - you can expand this with the full KerML specification.
 */
object KerMLMetamodel {

    /**
     * Initialize the KerML metamodel in a Gearshift engine.
     */
    fun initialize(engine: GearshiftEngine) {
        // Core element hierarchy
        registerElement(engine)
        registerRelationship(engine)

        // Namespace and Membership for name resolution (KerML 8.2.3.5)
        registerMembership(engine)
        registerNamespace(engine)
        registerImport(engine)
        registerSpecialization(engine)

        registerFeature(engine)
        registerType(engine)
        registerClassifier(engine)

        println("KerML Metamodel initialized successfully!")
    }

    private fun registerElement(engine: GearshiftEngine) {
        val element = MetaClass(
            name = "Element",
            isAbstract = true,
            description = "Root of the KerML element hierarchy",
            attributes = listOf(
                MetaProperty(
                    name = "elementId",
                    type = "String",
                    multiplicity = "1",
                    isReadOnly = true,
                    description = "Unique identifier for this element"
                ),
                MetaProperty(
                    name = "name",
                    type = "String",
                    multiplicity = "0..1",
                    description = "Optional name of the element"
                ),
                MetaProperty(
                    name = "qualifiedName",
                    type = "String",
                    multiplicity = "0..1",
                    isDerived = true,
                    isReadOnly = true,
                    description = "Fully qualified name derived from containment hierarchy"
                ),
                MetaProperty(
                    name = "ownedElement",
                    type = "Element",
                    multiplicity = "0..*",
                    aggregation = AggregationKind.COMPOSITE,
                    isOrdered = true,
                    description = "Elements owned by this element"
                ),
                MetaProperty(
                    name = "owner",
                    type = "Element",
                    multiplicity = "0..1",
                    isDerived = true,
                    description = "Element that owns this element"
                )
            )
        )
        engine.registerMetaClass(element)
    }

    private fun registerRelationship(engine: GearshiftEngine) {
        val relationship = MetaClass(
            name = "Relationship",
            superclasses = listOf("Element"),
            isAbstract = true,
            description = "Abstract base for relationships between elements",
            attributes = listOf(
                MetaProperty(
                    name = "source",
                    type = "Element",
                    multiplicity = "1..*",
                    description = "Source elements of the relationship"
                ),
                MetaProperty(
                    name = "target",
                    type = "Element",
                    multiplicity = "1..*",
                    description = "Target elements of the relationship"
                )
            )
        )
        engine.registerMetaClass(relationship)
    }

    private fun registerFeature(engine: GearshiftEngine) {
        val feature = MetaClass(
            name = "Feature",
            superclasses = listOf("Element"),
            isAbstract = false,
            description = "Represents a feature in KerML",
            attributes = listOf(
                MetaProperty(
                    name = "isAbstract",
                    type = "Boolean",
                    multiplicity = "1",
                    defaultValue = "false",
                    description = "Whether this feature is abstract"
                ),
                MetaProperty(
                    name = "isComposite",
                    type = "Boolean",
                    multiplicity = "1",
                    defaultValue = "false",
                    description = "Whether this feature is composite"
                ),
                MetaProperty(
                    name = "ownedFeature",
                    type = "Feature",
                    multiplicity = "0..*",
                    aggregation = AggregationKind.COMPOSITE,
                    isOrdered = true,
                    description = "Features owned by this feature"
                )
            ),
            constraints = listOf(
                MetaConstraint(
                    name = "uniqueNames",
                    language = "OCL",
                    expression = "self.ownedFeature->forAll(f1, f2 | f1 <> f2 implies f1.name <> f2.name)",
                    description = "Owned features must have unique names"
                )
            )
        )
        engine.registerMetaClass(feature)
    }

    private fun registerType(engine: GearshiftEngine) {
        val type = MetaClass(
            name = "Type",
            superclasses = listOf("Feature"),
            isAbstract = false,
            description = "Represents a type in KerML",
            attributes = listOf(
                MetaProperty(
                    name = "isSufficient",
                    type = "Boolean",
                    multiplicity = "1",
                    defaultValue = "false",
                    description = "Whether this type is sufficient"
                ),
                MetaProperty(
                    name = "ownedFeatureMembership",
                    type = "FeatureMembership",
                    multiplicity = "0..*",
                    aggregation = AggregationKind.COMPOSITE,
                    isOrdered = true,
                    description = "Feature memberships owned by this type"
                )
            )
        )
        engine.registerMetaClass(type)
    }

    private fun registerClassifier(engine: GearshiftEngine) {
        val classifier = MetaClass(
            name = "Classifier",
            superclasses = listOf("Type"),
            isAbstract = false,
            description = "Represents a classifier in KerML"
        )
        engine.registerMetaClass(classifier)
    }

    // ===== Name Resolution Support (KerML 8.2.3.5) =====

    private fun registerMembership(engine: GearshiftEngine) {
        val membership = MetaClass(
            name = "Membership",
            superclasses = listOf("Relationship"),
            isAbstract = false,
            description = "Represents membership of an Element in a Namespace",
            attributes = listOf(
                MetaProperty(
                    name = "memberElement",
                    type = "Element",
                    multiplicity = "1",
                    description = "The element that is a member"
                ),
                MetaProperty(
                    name = "memberName",
                    type = "String",
                    multiplicity = "0..1",
                    description = "The name by which the member is known"
                ),
                MetaProperty(
                    name = "membershipOwningNamespace",
                    type = "Namespace",
                    multiplicity = "0..1",
                    description = "The namespace that owns this membership"
                ),
                MetaProperty(
                    name = "visibility",
                    type = "VisibilityKind",
                    multiplicity = "1",
                    defaultValue = "public",
                    description = "Visibility of the membership"
                )
            )
        )
        engine.registerMetaClass(membership)
    }

    private fun registerNamespace(engine: GearshiftEngine) {
        val namespace = MetaClass(
            name = "Namespace",
            superclasses = listOf("Element"),
            isAbstract = false,
            description = "Element that can contain other Elements via Membership",
            attributes = listOf(
                MetaProperty(
                    name = "ownedMembership",
                    type = "Membership",
                    multiplicity = "0..*",
                    aggregation = AggregationKind.COMPOSITE,
                    isOrdered = true,
                    description = "Memberships owned by this namespace"
                ),
                MetaProperty(
                    name = "member",
                    type = "Element",
                    multiplicity = "0..*",
                    isDerived = true,
                    description = "Elements that are members (derived from memberships)"
                ),
                MetaProperty(
                    name = "ownedImport",
                    type = "Import",
                    multiplicity = "0..*",
                    aggregation = AggregationKind.COMPOSITE,
                    description = "Imports owned by this namespace"
                ),
                MetaProperty(
                    name = "importedMembership",
                    type = "Membership",
                    multiplicity = "0..*",
                    isDerived = true,
                    description = "Memberships imported into this namespace"
                )
            )
        )
        engine.registerMetaClass(namespace)
    }

    private fun registerImport(engine: GearshiftEngine) {
        val import = MetaClass(
            name = "Import",
            superclasses = listOf("Relationship"),
            isAbstract = true,
            description = "Base for importing elements into a namespace",
            attributes = listOf(
                MetaProperty(
                    name = "importOwningNamespace",
                    type = "Namespace",
                    multiplicity = "0..1",
                    description = "Namespace that owns this import"
                ),
                MetaProperty(
                    name = "isRecursive",
                    type = "Boolean",
                    multiplicity = "1",
                    defaultValue = "false",
                    description = "Whether import is recursive"
                )
            )
        )
        engine.registerMetaClass(import)

        // MembershipImport
        val membershipImport = MetaClass(
            name = "MembershipImport",
            superclasses = listOf("Import"),
            isAbstract = false,
            description = "Import of a specific Membership",
            attributes = listOf(
                MetaProperty(
                    name = "importedMembership",
                    type = "Membership",
                    multiplicity = "1",
                    description = "The membership being imported"
                )
            )
        )
        engine.registerMetaClass(membershipImport)

        // NamespaceImport
        val namespaceImport = MetaClass(
            name = "NamespaceImport",
            superclasses = listOf("Import"),
            isAbstract = false,
            description = "Import of all visible members from a Namespace",
            attributes = listOf(
                MetaProperty(
                    name = "importedNamespace",
                    type = "Namespace",
                    multiplicity = "1",
                    description = "The namespace being imported"
                )
            )
        )
        engine.registerMetaClass(namespaceImport)
    }

    private fun registerSpecialization(engine: GearshiftEngine) {
        val specialization = MetaClass(
            name = "Specialization",
            superclasses = listOf("Relationship"),
            isAbstract = false,
            description = "Relationship where one type specializes another",
            attributes = listOf(
                MetaProperty(
                    name = "specific",
                    type = "Type",
                    multiplicity = "1",
                    description = "The specializing type (subtype)"
                ),
                MetaProperty(
                    name = "general",
                    type = "Type",
                    multiplicity = "1",
                    description = "The generalized type (supertype)"
                ),
                MetaProperty(
                    name = "owningType",
                    type = "Type",
                    multiplicity = "0..1",
                    description = "Type that owns this specialization"
                )
            )
        )
        engine.registerMetaClass(specialization)
    }
}
