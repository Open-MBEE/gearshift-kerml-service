package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Feature metaclass.
 * Specializes: Type
 * A type that is also a feature.
 */
fun createFeatureMetaClass() = MetaClass(
    name = "Feature",
    isAbstract = false,
    superclasses = listOf("Type"),
    attributes = listOf(
        MetaProperty(
            name = "direction",
            type = "FeatureDirectionKind",
            multiplicity = "0..1",
            description = "The direction of the feature"
        ),
        MetaProperty(
            name = "isComposite",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this feature is composite"
        ),
        MetaProperty(
            name = "isEnd",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this feature is an end feature"
        ),
        MetaProperty(
            name = "isOrdered",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether values of this feature are ordered"
        ),
        MetaProperty(
            name = "isPortion",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this feature is a portion"
        ),
        MetaProperty(
            name = "isReadOnly",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this feature is read-only"
        ),
        MetaProperty(
            name = "isUnique",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether values of this feature are unique"
        )
    ),
    description = "A type that is also a feature"
)
