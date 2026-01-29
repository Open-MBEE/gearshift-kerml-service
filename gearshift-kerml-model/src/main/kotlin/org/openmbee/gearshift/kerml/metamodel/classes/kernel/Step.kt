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

import org.openmbee.gearshift.framework.meta.BindingCondition
import org.openmbee.gearshift.framework.meta.BindingKind
import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.SemanticBinding

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
