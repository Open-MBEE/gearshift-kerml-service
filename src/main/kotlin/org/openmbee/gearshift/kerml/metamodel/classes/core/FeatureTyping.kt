package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML FeatureTyping metaclass.
 * Specializes: Specialization
 * A specialization that types a feature with a type.
 */
fun createFeatureTypingMetaClass() = MetaClass(
    name = "FeatureTyping",
    isAbstract = false,
    superclasses = listOf("Specialization"),
    attributes = emptyList(),
    description = "A specialization that types a feature with a type"
)
