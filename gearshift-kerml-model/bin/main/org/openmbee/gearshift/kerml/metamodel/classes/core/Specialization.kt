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
 * KerML Specialization metaclass.
 * Specializes: Relationship
 * A relationship that makes one type a specialization of another.
 *
 * Association ends (defined in SpecializationAssociations.kt):
 * - general : Type [1] {redefines target}
 * - specific : Type [1] {redefines source}
 * - owningType : Type [0..1] {subsets specific, redefines owningRelatedElement}
 */
fun createSpecializationMetaClass() = MetaClass(
    name = "Specialization",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateSpecializationSpecificNotConjugated",
            type = ConstraintType.VERIFICATION,
            expression = "not specific.isConjugated",
            description = "The specificType of a Specialization cannot be a conjugatedType"
        )
    ),
    description = "A relationship that makes one type a specialization of another"
)
