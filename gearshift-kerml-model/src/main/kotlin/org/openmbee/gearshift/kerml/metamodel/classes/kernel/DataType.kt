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
 * KerML DataType metaclass.
 * Specializes: Classifier
 * A classifier that represents a data type.
 */
fun createDataTypeMetaClass() = MetaClass(
    name = "DataType",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkDataTypeSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Base::DataValue')",
            libraryTypeName = "Base::DataValue",
            description = "A DataType must directly or indirectly specialize the base DataType Base::DataValue from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "validateDataTypeSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "ownedSpecialization.general->forAll(not oclIsKindOf(Class) and not oclIsKindOf(Association))",
            description = "A DataType must not specialize a Class or an Association."
        )
    ),
    description = "A classifier that represents a data type"
)
