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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * SysML ConjugatedPortTyping metaclass.
 * Specializes: FeatureTyping
 * A ConjugatedPortTyping is a FeatureTyping whose type is a ConjugatedPortDefinition. This
 * relationship is intended to be an abstract-syntax marker for a special surface notation for
 * conjugated typing of ports.
 */
fun createConjugatedPortTypingMetaClass() = MetaClass(
    name = "ConjugatedPortTyping",
    isAbstract = false,
    superclasses = listOf("FeatureTyping"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveConjugatedPortTypingPortDefinition",
            type = ConstraintType.DERIVATION,
            expression = "conjugatedPortDefinition.originalPortDefinition",
            description = "The portDefinition of a ConjugatedPortTyping is the originalPortDefinition of the conjugatedPortDefinition of the ConjugatedPortTyping."
        )
    ),
    description = "A ConjugatedPortTyping is a FeatureTyping whose type is a ConjugatedPortDefinition."
)
