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
import org.openmbee.gearshift.framework.meta.MetaOperation

/**
 * KerML ReturnParameterMembership metaclass.
 * Specializes: ParameterMembership
 * A ReturnParameterMembership is a ParameterMembership that indicates that the ownedMemberParameter
 * is the result parameter of a Function or Expression. The direction of the ownedMemberParameter must be out.
 */
fun createReturnParameterMembershipMetaClass() = MetaClass(
    name = "ReturnParameterMembership",
    isAbstract = false,
    superclasses = listOf("ParameterMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateReturnParameterMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = "owningType.oclIsKindOf(Function) or owningType.oclIsKindOf(Expression)",
            description = "The owningType of a ReturnParameterMembership must be a Function or Expression."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "parameterDirection",
            returnType = "FeatureDirectionKind",
            body = "FeatureDirectionKind::out",
            redefines = "parameterDirection",
            description = "The ownedMemberParameter of a ReturnParameterMembership must have direction out. (This is a leaf operation that cannot be further redefined.)"
        )
    ),
    description = "A ReturnParameterMembership is a ParameterMembership that indicates that the ownedMemberParameter is the result parameter of a Function or Expression."
)
