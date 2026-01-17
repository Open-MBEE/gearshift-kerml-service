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

import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint

/**
 * KerML Succession metaclass.
 * Specializes: Connector
 * A connector that represents succession between steps.
 */
fun createSuccessionMetaClass() = MetaClass(
    name = "Succession",
    isAbstract = false,
    superclasses = listOf("Connector"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkSuccessionSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Occurrences::happensBeforeLinks')",
            libraryTypeName = "Occurrences::happensBeforeLinks",
            description = "A Succession must directly or indirectly specialize the Feature Occurrences::happensBeforeLinks from the Kernel Semantic Library."
        )
    ),
    description = "A connector that represents succession between steps"
)
