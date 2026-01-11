package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AssociationStructure metaclass.
 * Specializes: Association, Structure
 * An association that is also a structure.
 */
fun createAssociationStructureMetaClass() = MetaClass(
    name = "AssociationStructure",
    isAbstract = false,
    superclasses = listOf("Association", "Structure"),
    attributes = emptyList(),
    description = "An association that is also a structure"
)
