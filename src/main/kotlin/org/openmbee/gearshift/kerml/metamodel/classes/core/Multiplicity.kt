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
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint

/**
 * KerML Multiplicity metaclass.
 * Specializes: Feature
 * A feature that specifies the multiplicity of another feature.
 */
fun createMultiplicityMetaClass() = MetaClass(
    name = "Multiplicity",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkMultiplicitySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Base::naturals')",
            description = "A Multiplicity must directly or indirectly specialize the Feature Base::naturals from the Kernel Semantic Library"
        ),
        MetaConstraint(
            name = "checkMultiplicityTypeFeaturing",
            type = ConstraintType.VERIFICATION,
            expression = """
                if owningType <> null and owningType.oclIsKindOf(Feature) then
                    featuringType = owningType.oclAsType(Feature).featuringType
                else
                    featuringType->isEmpty()
                endif
            """.trimIndent(),
            description = "If the owningType of a Multiplicity is a Feature, then the Multiplicity must have the same featuringTypes as that Feature. Otherwise, it must have no featuringTypes"
        )
    ),
    description = "A feature that specifies the multiplicity of another feature"
)
