package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML NamespaceImport metaclass.
 * Specializes: Import
 * An import that brings all visible members of a namespace into another namespace.
 */
fun createNamespaceImportMetaClass() = MetaClass(
    name = "NamespaceImport",
    isAbstract = false,
    superclasses = listOf("Import"),
    attributes = emptyList(),
    description = "An import that brings all visible members of a namespace into another namespace"
)
