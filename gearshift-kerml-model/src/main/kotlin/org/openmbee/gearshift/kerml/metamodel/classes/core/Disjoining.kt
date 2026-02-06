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
 * KerML Disjoining metaclass.
 * Specializes: Relationship
 * A relationship that specifies two types are disjoint.
 */
fun createDisjoiningMetaClass() = MetaClass(
    name = "Disjoining",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveDisjoiningOwningType",
            type = ConstraintType.DERIVATION,
            expression = "if typeDisjoined = owningRelatedElement then typeDisjoined else null endif",
            description = "A typeDisjoined that is also an owningRelatedElement."
        )
    ),
    description = "A relationship that specifies two types are disjoint"
)
