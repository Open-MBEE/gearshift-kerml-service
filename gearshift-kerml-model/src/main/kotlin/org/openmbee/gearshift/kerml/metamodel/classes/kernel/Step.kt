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
 * KerML Step metaclass.
 * Specializes: Feature
 * A feature that represents a step in a behavior.
 */
fun createStepMetaClass() = MetaClass(
    name = "Step",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkStepEnclosedPerformanceSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(Behavior) or owningType.oclIsKindOf(Step))
            """.trimIndent(),
            libraryTypeName = "Performances::Performance::enclosedPerformance",
            description = "A Step whose owningType is a Behavior or another Step must directly or indirectly specialize the Step Performances::Performance::enclosedPerformance."
        ),
        MetaConstraint(
            name = "checkStepOwnedPerformanceSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(Structure) or
                 owningType.oclIsKindOf(Feature) and
                 owningType.oclAsType(Feature).type->exists(oclIsKindOf(Structure)))
            """.trimIndent(),
            libraryTypeName = "Objects::Object::ownedPerformance",
            description = "A composite Step whose owningType is a Structure or a Feature typed by a Structure must directly or indirectly specialize the Step Objects::Object::ownedPerformance."
        ),
        MetaConstraint(
            name = "checkStepSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Performances::performances')",
            libraryTypeName = "Performances::performances",
            description = "A Step must directly or indirectly specialize the base Step Performances::performances from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkStepSubperformanceSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(Behavior) or owningType.oclIsKindOf(Step)) and
                isComposite
            """.trimIndent(),
            libraryTypeName = "Performances::Performance::subperformance",
            description = "A Step whose owningType is a Behavior or another Step, and which is composite, must directly or indirectly specialize the Step Performances::Performance::subperformance."
        ),
        MetaConstraint(
            name = "deriveStepBehavior",
            type = ConstraintType.DERIVATION,
            expression = "type->selectByKind(Behavior)",
            description = "The behaviors of a Step are all its types that are Behaviors."
        ),
        MetaConstraint(
            name = "deriveStepParameter",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "directedFeature",
            description = "The parameters of a Step are its directedFeatures, whose values are passed into and/or out of a performance of the Step."
        )
    ),
    description = "A feature that represents a step in a behavior"
)
