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

fun createViewMetaClass() = MetaClass(
    name = "View",
    isAbstract = false,
    superclasses = listOf("Structure"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveViewExpose",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Expose)",
            description = "The Exposes owned by this View, determining elements visible in the View."
        ),
        MetaConstraint(
            name = "deriveViewExposedElement",
            type = ConstraintType.DERIVATION,
            expression = "expose.importedElement->asSet()",
            description = "The Elements exposed by this View through its Exposes."
        ),
        MetaConstraint(
            name = "deriveViewRendering",
            type = ConstraintType.DERIVATION,
            expression = "featureMembership->selectByKind(ViewRenderingMembership).ownedRendering->first()",
            description = "The Rendering of this View, owned via a ViewRenderingMembership."
        ),
        MetaConstraint(
            name = "deriveViewSatisfiedViewpoint",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeature->selectByKind(ViewpointPredicate)",
            description = "The ViewpointPredicates that this View satisfies."
        ),
        MetaConstraint(
            name = "deriveViewSubview",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeature->selectByKind(Feature)->select(f | f.type->exists(oclIsKindOf(View)))",
            description = "Other Views used in the rendering of this View."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "viewPartBinding",
            baseConcept = "Views::views",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A Structure that exposes Elements for stakeholders, rendered by a Rendering and constrained by Viewpoints"
)
