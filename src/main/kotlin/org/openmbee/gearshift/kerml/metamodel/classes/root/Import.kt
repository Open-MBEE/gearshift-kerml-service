package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Import metaclass.
 * Specializes: Relationship
 * An abstract relationship that imports elements into a namespace.
 */
fun createImportMetaClass() = MetaClass(
    name = "Import",
    isAbstract = true,
    superclasses = listOf("Relationship"),
    attributes = listOf(
        MetaProperty(
            name = "visibility",
            type = "VisibilityKind",
            multiplicity = "1",
            description = "The visibility of the imported elements"
        ),
        MetaProperty(
            name = "isRecursive",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether the import is recursive"
        ),
        MetaProperty(
            name = "isImportAll",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether all elements are imported"
        )
    ),
    description = "An abstract relationship that imports elements into a namespace"
)
