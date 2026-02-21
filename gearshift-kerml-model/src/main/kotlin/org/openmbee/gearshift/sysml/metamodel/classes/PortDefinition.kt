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
 * SysML PortDefinition metaclass.
 * Specializes: Structure, OccurrenceDefinition
 * A PortDefinition defines a point at which external entities can connect to and interact with a
 * system or part of a system. Any ownedUsages of a PortDefinition, other than PortUsages, must
 * not be composite.
 */
fun createPortDefinitionMetaClass() = MetaClass(
    name = "PortDefinition",
    isAbstract = false,
    superclasses = listOf("Structure", "OccurrenceDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkPortDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Ports::Port')",
            description = "A PortDefinition must directly or indirectly specialize the PortDefinition Ports::Port from the Systems Model Library."
        ),
        MetaConstraint(
            name = "derivePortDefinitionConjugatedPortDefinition",
            type = ConstraintType.DERIVATION,
            expression = """
                let conjugatedPortDefinitions : OrderedSet(ConjugatedPortDefinition) =
                    ownedMember->selectByKind(ConjugatedPortDefinition) in
                if conjugatedPortDefinitions->isEmpty() then null
                else conjugatedPortDefinitions->first()
                endif
            """.trimIndent(),
            description = "The conjugatedPortDefinition of a PortDefinition is the ownedMember that is a ConjugatedPortDefinition."
        ),
        MetaConstraint(
            name = "validatePortDefinitionConjugatedPortDefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                not oclIsKindOf(ConjugatedPortDefinition) implies
                ownedMember->
                    selectByKind(ConjugatedPortDefinition)->
                    size() = 1
            """.trimIndent(),
            description = "Unless it is a ConjugatedPortDefinition, a PortDefinition must have exactly one ownedMember that is a ConjugatedPortDefinition."
        ),
        MetaConstraint(
            name = "validatePortDefinitionOwnedUsagesNotComposite",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedUsage->
                    reject(oclIsKindOf(PortUsage))->
                    forAll(not isComposite)
            """.trimIndent(),
            description = "The ownedUsages of a PortDefinition that are not PortUsages must not be composite."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "portDefinitionPortBinding",
            baseConcept = "Ports::Port",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A PortDefinition defines a point at which external entities can connect to and interact with a system or part of a system."
)
