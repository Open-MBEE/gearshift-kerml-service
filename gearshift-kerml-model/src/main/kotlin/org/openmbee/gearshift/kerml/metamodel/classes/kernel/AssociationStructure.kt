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

import org.openmbee.gearshift.framework.meta.BindingCondition
import org.openmbee.gearshift.framework.meta.BindingKind
import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.SemanticBinding

/**
 * KerML AssociationStructure metaclass.
 * Specializes: Association, Structure
 * An association that is also a structure.
 */
fun createAssociationStructureMetaClass() = MetaClass(
    name = "AssociationStructure",
    isAbstract = false,
    superclasses = listOf("Association", "Structure"),
    attributes = emptyList(),
    constraints = listOf(
        // Note: checkAssociationStructureBinarySpecialization for Objects::BinaryLinkObject requires collection size checking,
        // which is not yet supported by BindingCondition
        MetaConstraint(
            name = "checkAssociationStructureBinarySpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "endFeature->size() = 2",
            libraryTypeName = "Objects::BinaryLinkObject",
            description = "A binary AssociationStructure must directly or indirectly specialize the base AssociationStructure Objects::BinaryLinkObject from the Kernel Semantic Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "associationStructureLinkObjectBinding",
            baseConcept = "Objects::LinkObject",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "An association that is also a structure"
)
