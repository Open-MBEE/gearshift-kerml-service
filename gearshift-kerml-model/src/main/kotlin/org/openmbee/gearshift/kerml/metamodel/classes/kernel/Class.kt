/*
 * Copyright 2026 Charles Galey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint

/**
 * KerML Class metaclass.
 * Specializes: Classifier
 * A classifier that represents a class.
 */
fun createClassMetaClass() = MetaClass(
    name = "Class",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkClassSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Occurrences::Occurrence')",
            libraryTypeName = "Occurrences::Occurrence",
            description = "A Class must directly or indirectly specialize the base Class Occurrences::Occurrence from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "validateClassSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedSpecialization.general->forAll(not oclIsKindOf(DataType)) and
                not oclIsKindOf(Association) implies
                    ownedSpecialization.general->forAll(not oclIsKindOf(Association))
            """.trimIndent(),
            description = "A Class must not specialize a DataType and it can only specialize an Association if it is also itself a kind of Association (such as an AssociationStructure or Interaction)."
        )
    ),
    description = "A classifier that represents a class"
)
