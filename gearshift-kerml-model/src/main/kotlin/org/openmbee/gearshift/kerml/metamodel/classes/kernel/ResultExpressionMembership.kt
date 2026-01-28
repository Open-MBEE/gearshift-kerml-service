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
 * KerML ResultExpressionMembership metaclass.
 * Specializes: FeatureMembership
 * A ResultExpressionMembership is a FeatureMembership that indicates that the ownedResultExpression
 * provides the result values for the Function or Expression that owns it.
 */
fun createResultExpressionMembershipMetaClass() = MetaClass(
    name = "ResultExpressionMembership",
    isAbstract = false,
    superclasses = listOf("FeatureMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveResultExpressionMembershipOwnedResultExpression",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "ownedMemberFeature->selectByKind(Expression)->first()",
            description = "The Expression that provides the result for the owner of the ResultExpressionMembership."
        ),
        MetaConstraint(
            name = "validateResultExpressionMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = "owningType.oclIsKindOf(Function) or owningType.oclIsKindOf(Expression)",
            description = "The owningType of a ResultExpressionMembership must be a Function or Expression."
        )
    ),
    description = "A ResultExpressionMembership is a FeatureMembership that indicates that the ownedResultExpression provides the result values for the Function or Expression that owns it."
)
