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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML OccurrenceDefinition metaclass.
 * Specializes: Definition, Class
 * An OccurrenceDefinition is a Definition of a Class of individuals that have an independent life
 * over time and potentially an extent over space. This includes both structural things and behaviors
 * that act on such structures. If isIndividual is true, then the OccurrenceDefinition is constrained
 * to have (at most) a single instance that is the entire life of a single individual.
 */
fun createOccurrenceDefinitionMetaClass() = MetaClass(
    name = "OccurrenceDefinition",
    isAbstract = false,
    superclasses = listOf("Definition", "Class"),
    attributes = listOf(
        MetaProperty(
            name = "isIndividual",
            type = "Boolean",
            description = "Whether this OccurrenceDefinition is constrained to represent at most one thing."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkOccurrenceDefinitionIndividualSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "isIndividual implies specializesFromLibrary('Occurrences::Life')",
            description = "An OccurrenceDefinition with isIndividual = true must directly or indirectly specialize Occurrences::Life from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkOccurrenceDefinitionMultiplicitySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isIndividual implies
                multiplicity <> null and
                multiplicity.specializesFromLibrary('Base::zeroOrOne')
            """.trimIndent(),
            description = "An OccurrenceDefinition with isIndividual = true must have a multiplicity that specializes Base::zeroOrOne from the Kernel Semantic Library."
        )
    ),
    description = "An OccurrenceDefinition is a Definition of a Class of individuals that have an independent life over time and potentially an extent over space."
)
