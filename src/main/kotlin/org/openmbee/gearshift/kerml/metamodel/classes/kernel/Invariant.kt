package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Invariant metaclass.
 * Specializes: BooleanExpression
 * A boolean expression that specifies a constraint.
 */
fun createInvariantMetaClass() = MetaClass(
    name = "Invariant",
    isAbstract = false,
    superclasses = listOf("BooleanExpression"),
    attributes = listOf(
        MetaProperty(
            name = "isNegated",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this invariant is negated"
        )
    ),
    description = "A boolean expression that specifies a constraint"
)
