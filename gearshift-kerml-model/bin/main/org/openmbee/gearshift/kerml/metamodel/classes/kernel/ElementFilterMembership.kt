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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * KerML ElementFilterMembership metaclass.
 * Specializes: OwningMembership
 * A Membership between a Namespace and a model-level evaluable Boolean-valued Expression,
 * asserting that imported members of the Namespace should be filtered using the condition Expression.
 */
fun createElementFilterMembershipMetaClass() = MetaClass(
    name = "ElementFilterMembership",
    isAbstract = false,
    superclasses = listOf("OwningMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateElementFilterMembershipConditionIsBoolean",
            type = ConstraintType.VERIFICATION,
            expression = "condition.result.specializesFromLibrary('ScalarValues::Boolean')",
            description = "The result parameter of the condition Expression must directly or indirectly specialize ScalarValues::Boolean."
        ),
        MetaConstraint(
            name = "validateElementFilterMembershipConditionIsModelLevelEvaluable",
            type = ConstraintType.VERIFICATION,
            expression = "condition.isModelLevelEvaluable",
            description = "The condition Expression must be model-level evaluable."
        )
    ),
    description = "A Membership between a Namespace and a model-level evaluable Boolean-valued Expression, asserting that imported members of the Namespace should be filtered using the condition Expression"
)