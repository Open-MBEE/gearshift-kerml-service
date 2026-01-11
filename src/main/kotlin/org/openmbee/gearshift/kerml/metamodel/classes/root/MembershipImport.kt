package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML MembershipImport metaclass.
 * Specializes: Import
 * An import that brings a specific membership into a namespace.
 */
fun createMembershipImportMetaClass() = MetaClass(
    name = "MembershipImport",
    isAbstract = false,
    superclasses = listOf("Import"),
    attributes = emptyList(),
    description = "An import that brings a specific membership into a namespace"
)
