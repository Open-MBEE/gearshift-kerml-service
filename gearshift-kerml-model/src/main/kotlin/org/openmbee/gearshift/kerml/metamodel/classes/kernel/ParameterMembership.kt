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

import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaOperation

fun createParameterMembershipMetaClass() = MetaClass(
    name = "ParameterMembership",
    superclasses = listOf("FeatureMembership"),
    operations = listOf(
        MetaOperation(
            name = "parameterDirection",
            returnType = "FeatureDirectionKind",
            body = MetaOperation.ocl("ownedMemberParameter.direction"),
            description = "The direction of the ownedMemberParameter."
        )
    ),
    description = """
        A ParameterMembership is a FeatureMembership that identifies its memberFeature as a parameter, which is
        always owned, and must have a direction. A ParameterMembership must be owned by a Behavior, a Step,
        or the result parameter of a ConstructorExpression.
    """.trimIndent()
)