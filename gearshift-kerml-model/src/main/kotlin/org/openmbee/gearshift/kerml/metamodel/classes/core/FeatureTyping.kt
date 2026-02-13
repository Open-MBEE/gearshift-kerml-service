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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * KerML FeatureTyping metaclass.
 * Specializes: Specialization
 * A specialization that types a feature with a type.
 */
fun createFeatureTypingMetaClass() = MetaClass(
    name = "FeatureTyping",
    isAbstract = false,
    superclasses = listOf("Specialization"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveFeatureTypingOwningFeature",
            type = ConstraintType.DERIVATION,
            expression = "if typedFeature = owningRelatedElement then typedFeature else null endif",
            description = "A typedFeature that is also the owningRelatedElement of this FeatureTyping."
        )
    ),
    description = "A specialization that types a feature with a type"
)
