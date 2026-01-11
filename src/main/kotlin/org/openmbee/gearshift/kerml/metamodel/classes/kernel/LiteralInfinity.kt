package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML LiteralInfinity metaclass.
 * Specializes: LiteralExpression
 * A literal expression representing infinity.
 */
fun createLiteralInfinityMetaClass() = MetaClass(
    name = "LiteralInfinity",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = emptyList(),
    description = "A literal expression representing infinity"
)
