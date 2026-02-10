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

fun createViewpointPredicateMetaClass() = MetaClass(
    name = "ViewpointPredicate",
    isAbstract = false,
    superclasses = listOf("BooleanExpression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveViewpointPredicateViewpointDefinition",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "type->selectByKind(Viewpoint)",
            description = "The Viewpoints that type this ViewpointPredicate."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "viewpointPredicateCheckBinding",
            baseConcept = "Views::viewpointChecks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    description = "A BooleanExpression that evaluates whether a View satisfies a Viewpoint"
)
