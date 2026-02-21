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
 * SysML ActionDefinition metaclass.
 * Specializes: Behavior, OccurrenceDefinition
 * An ActionDefinition is a Definition that is also a Behavior that defines an Action performed by a system
 * or part of a system.
 */
fun createActionDefinitionMetaClass() = MetaClass(
    name = "ActionDefinition",
    isAbstract = false,
    superclasses = listOf("Behavior", "OccurrenceDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkActionDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::Action')",
            description = "An ActionDefinition must directly or indirectly specialize the ActionDefinition Actions::Action from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveActionDefinitionAction",
            type = ConstraintType.DERIVATION,
            expression = "usage->selectByKind(ActionUsage)",
            description = "The actions of an ActionDefinition are those of its usages that are ActionUsages."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "actionDefinitionActionBinding",
            baseConcept = "Actions::Action",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "An ActionDefinition is a Definition that is also a Behavior that defines an Action performed by a system or part of a system."
)
