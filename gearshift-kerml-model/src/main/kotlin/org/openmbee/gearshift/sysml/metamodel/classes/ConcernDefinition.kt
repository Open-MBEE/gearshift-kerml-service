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

import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML ConcernDefinition metaclass.
 * Specializes: RequirementDefinition
 * A ConcernDefinition is a RequirementDefinition that one or more stakeholders may be interested in having
 * addressed. These stakeholders are identified by the ownedStakeholders of the ConcernDefinition.
 */
fun createConcernDefinitionMetaClass() = MetaClass(
    name = "ConcernDefinition",
    isAbstract = false,
    superclasses = listOf("RequirementDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConcernDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Requirements::ConcernCheck')",
            description = "A ConcernDefinition must directly or indirectly specialize the base ConcernDefinition Requirements::ConcernCheck from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "concernDefinitionConcernCheckBinding",
            baseConcept = "Requirements::ConcernCheck",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A ConcernDefinition is a RequirementDefinition that one or more stakeholders may be interested in having addressed."
)
