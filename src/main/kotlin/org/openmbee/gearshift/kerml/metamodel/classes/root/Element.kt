package org.openmbee.gearshift.kerml.metamodel.classes.root

import com.fasterxml.jackson.annotation.JsonProperty
import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Element (Root.Element)
 * Abstract root of the KerML element hierarchy.
 */
fun createElementMetaClass() = MetaClass(
    name = "Element",
    isAbstract = true,
    description = "Abstract root of the KerML element hierarchy",
    attributes = listOf(
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "elementId",
            type = "String",
            multiplicity = "1",
            description = "Unique identifier for this element"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "aliasIds",
            type = "String",
            multiplicity = "0..*",
            isOrdered = true,
            description = "Alias identifiers for this element"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "declaredShortName",
            type = "String",
            multiplicity = "0..1",
            description = "Declared short name"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "declaredName",
            type = "String",
            multiplicity = "0..1",
            description = "Declared name"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "isImpliedIncluded",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this element is implied included"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "shortName",
            type = "String",
            multiplicity = "0..1",
            isDerived = true,
            description = "Effective short name (derived)"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "name",
            type = "String",
            multiplicity = "0..1",
            isDerived = true,
            description = "Effective name (derived)"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "qualifiedName",
            type = "String",
            multiplicity = "0..1",
            isDerived = true,
            description = "Fully qualified name (derived)"
        ),
        org.openmbee.gearshift.metamodel.MetaProperty(
            name = "isLibraryElement",
            type = "Boolean",
            multiplicity = "1",
            isDerived = true,
            description = "Whether this is a library element (derived)"
        )
    )
)
