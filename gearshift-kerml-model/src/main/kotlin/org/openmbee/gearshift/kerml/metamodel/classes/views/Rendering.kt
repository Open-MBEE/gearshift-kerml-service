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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

fun createRenderingMetaClass() = MetaClass(
    name = "Rendering",
    isAbstract = false,
    superclasses = listOf("Structure"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveRenderingSubrendering",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeature->selectByKind(Feature)->select(f | f.type->exists(oclIsKindOf(Rendering)))",
            description = "Other Renderings used to carry out this Rendering."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "renderingPartBinding",
            baseConcept = "Views::renderings",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A Structure that defines how a View is rendered into a presentable format"
)
