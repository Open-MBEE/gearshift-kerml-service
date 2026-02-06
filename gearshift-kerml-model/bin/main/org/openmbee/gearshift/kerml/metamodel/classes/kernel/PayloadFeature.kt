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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML PayloadFeature metaclass.
 * Specializes: Feature
 * A PayloadFeature is the ownedFeature of a Flow that identifies the things carried by the kinds
 * of transfers that are instances of the Flow.
 */
fun createPayloadFeatureMetaClass() = MetaClass(
    name = "PayloadFeature",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkPayloadFeatureRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = "redefinesFromLibrary('Transfers::Transfer::payload')",
            description = "A PayloadFeature must redefine the Feature Transfers::Transfer::payload from the Kernel Semantic Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "payloadFeatureRedefinition",
            baseConcept = "Transfers::Transfer::payload",
            bindingKind = BindingKind.REDEFINES,
            condition = BindingCondition.Default
        )
    ),
    description = "A PayloadFeature is the ownedFeature of a Flow that identifies the things carried by the kinds of transfers that are instances of the Flow."
)
