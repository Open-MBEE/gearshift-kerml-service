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
 * KerML Behavior metaclass.
 * Specializes: Class
 * A class that represents behavior.
 */
fun createBehaviorMetaClass() = MetaClass(
    name = "Behavior",
    isAbstract = false,
    superclasses = listOf("Class"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveBehaviorParameter",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "directedFeature",
            description = "The parameters of this Behavior are its directedFeatures, whose values are passed into and/or out of a performance of the Behavior."
        ),
        MetaConstraint(
            name = "deriveBehaviorStep",
            type = ConstraintType.DERIVATION,
            expression = "feature->selectByKind(Step)",
            description = "The steps of a Behavior are its features that are Steps."
        ),
        MetaConstraint(
            name = "validateBehaviorSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "ownedSpecialization.general->forAll(not oclIsKindOf(Structure))",
            description = "A Behavior must not specialize a Structure."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "behaviorPerformanceBinding",
            baseConcept = "Performances::Performance",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A class that represents behavior"
)
