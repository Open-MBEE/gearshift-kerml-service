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

import org.openmbee.gearshift.framework.meta.BindingCondition
import org.openmbee.gearshift.framework.meta.BindingKind
import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.SemanticBinding

/**
 * KerML BindingConnector metaclass.
 * Specializes: Connector
 * A connector that binds features together.
 */
fun createBindingConnectorMetaClass() = MetaClass(
    name = "BindingConnector",
    isAbstract = false,
    superclasses = listOf("Connector"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateBindingConnectorIsBinary",
            type = ConstraintType.VERIFICATION,
            expression = "relatedFeature->size() = 2",
            description = "A BindingConnector must be binary."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "bindingConnectorSelfLinksBinding",
            baseConcept = "Links::selfLinks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    description = "A connector that binds features together"
)
