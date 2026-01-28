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
 * KerML FlowEnd metaclass.
 * Specializes: Feature
 * A FlowEnd is a Feature that is one of the connectorEnds giving the source or target of a Flow.
 * For Flows typed by FlowTransfer or its specializations, FlowEnds must have exactly one ownedFeature,
 * which redefines Transfer::source::sourceOutput or Transfer::target::targetInput and redefines
 * the corresponding feature of the relatedElement for its end.
 */
fun createFlowEndMetaClass() = MetaClass(
    name = "FlowEnd",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateFlowEndIsEnd",
            type = ConstraintType.VERIFICATION,
            expression = "isEnd",
            description = "A FlowEnd must be an endFeature."
        ),
        MetaConstraint(
            name = "validateFlowEndNestedFeature",
            type = ConstraintType.VERIFICATION,
            expression = "ownedFeature->size() = 1",
            description = "A FlowEnd must have exactly one ownedFeature."
        ),
        MetaConstraint(
            name = "validateFlowEndOwningType",
            type = ConstraintType.VERIFICATION,
            expression = "owningType <> null and owningType.oclIsKindOf(Flow)",
            description = "The owningType of a FlowEnd must be a Flow."
        )
    ),
    description = "A FlowEnd is a Feature that is one of the connectorEnds giving the source or target of a Flow."
)
