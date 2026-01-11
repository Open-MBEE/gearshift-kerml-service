package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML MetadataFeature metaclass.
 * Specializes: Feature
 * A feature that holds metadata.
 */
fun createMetadataFeatureMetaClass() = MetaClass(
    name = "MetadataFeature",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    description = "A feature that holds metadata"
)
