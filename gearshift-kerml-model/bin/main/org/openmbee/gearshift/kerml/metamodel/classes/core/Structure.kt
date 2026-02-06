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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML Structure metaclass.
 * Specializes: Class
 * A class that represents a structure.
 */
fun createStructureMetaClass() = MetaClass(
    name = "Structure",
    isAbstract = false,
    superclasses = listOf("Class"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateStructureSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "ownedSpecialization.general->forAll(not oclIsKindOf(Behavior))",
            description = "A Structure must not specialize a Behavior."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "structureObjectBinding",
            baseConcept = "Objects::Object",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A class that represents a structure"
)
