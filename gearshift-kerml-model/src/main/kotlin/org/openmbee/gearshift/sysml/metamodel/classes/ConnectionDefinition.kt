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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML ConnectionDefinition metaclass.
 * Specializes: PartDefinition, AssociationStructure
 * A ConnectionDefinition is a PartDefinition that is also an AssociationStructure. The endFeatures
 * of a ConnectionDefinition must be Usages.
 */
fun createConnectionDefinitionMetaClass() = MetaClass(
    name = "ConnectionDefinition",
    isAbstract = false,
    superclasses = listOf("PartDefinition", "AssociationStructure"),
    attributes = listOf(
        MetaProperty(
            name = "isSufficient",
            type = "Boolean",
            lowerBound = 1,
            upperBound = 1,
            redefines = "isSufficient",
            description = "A ConnectionDefinition always has isSufficient = true."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkConnectionDefinitionBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedEndFeature->size() = 2 implies
                specializesFromLibrary('Connections::BinaryConnection')
            """.trimIndent(),
            description = "A binary ConnectionDefinition must directly or indirectly specialize the ConnectionDefinition Connections::BinaryConnection from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkConnectionDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Connections::Connection')",
            description = "A ConnectionDefinition must directly or indirectly specialize the ConnectionDefinition Connections::Connection from the Systems Model Library."
        ),
        MetaConstraint(
            name = "validateConnectionDefinitionIsSufficient",
            type = ConstraintType.VERIFICATION,
            expression = "isSufficient",
            description = "A ConnectionDefinition must have isSufficient = true."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "connectionDefinitionConnectionBinding",
            baseConcept = "Connections::Connection",
            bindingKind = BindingKind.SPECIALIZES
        ),
        SemanticBinding(
            name = "connectionDefinitionBinaryConnectionBinding",
            baseConcept = "Connections::BinaryConnection",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.PropertyEquals("ownedEndFeature->size()", "2")
        )
    ),
    description = "A ConnectionDefinition is a PartDefinition that is also an AssociationStructure."
)
