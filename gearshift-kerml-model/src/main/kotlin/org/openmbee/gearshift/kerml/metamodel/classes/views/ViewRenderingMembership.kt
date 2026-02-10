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

package org.openmbee.gearshift.kerml.metamodel.classes.views

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

fun createViewRenderingMembershipMetaClass() = MetaClass(
    name = "ViewRenderingMembership",
    superclasses = listOf("FeatureMembership"),
    constraints = listOf(
        MetaConstraint(
            name = "deriveViewRenderingMembershipOwnedRendering",
            type = ConstraintType.DERIVATION,
            expression = "ownedMemberFeature->selectByKind(Rendering)->first()",
            description = "The Rendering owned through this ViewRenderingMembership."
        )
    ),
    description = "A FeatureMembership that identifies the Rendering of a View"
)
