package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Association metaclass.
 * Specializes: Classifier, Relationship
 * A classifier and relationship that represents an association.
 */
fun createAssociationMetaClass() = MetaClass(
    name = "Association",
    isAbstract = false,
    superclasses = listOf("Classifier", "Relationship"),
    attributes = emptyList(),
    description = "A classifier and relationship that represents an association"
)
