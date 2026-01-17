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
 * KerML EndFeatureMembership metaclass.
 * Specializes: FeatureMembership
 * A FeatureMembership that identifies a Feature as an endFeature of a Type.
 */
fun createEndFeatureMembershipMetaClass() = MetaClass(
    name = "EndFeatureMembership",
    isAbstract = false,
    superclasses = listOf("FeatureMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateEndFeatureMembershipIsEnd",
            type = ConstraintType.VERIFICATION,
            expression = "ownedMemberFeature.isEnd",
            description = "The ownedMemberFeature of an EndFeatureMembership must be an endFeature."
        )
    ),
    description = "A FeatureMembership that identifies a Feature as an endFeature of a Type"
)