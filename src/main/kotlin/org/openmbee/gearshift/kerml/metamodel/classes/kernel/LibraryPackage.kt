package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML LibraryPackage metaclass.
 * Specializes: Package
 * A package that contains library elements.
 */
fun createLibraryPackageMetaClass() = MetaClass(
    name = "LibraryPackage",
    isAbstract = false,
    superclasses = listOf("Package"),
    attributes = listOf(
        MetaProperty(
            name = "isStandard",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this is a standard library package"
        )
    ),
    description = "A package that contains library elements"
)
