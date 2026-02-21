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
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML AttributeUsage metaclass.
 * Specializes: Usage
 * An AttributeUsage is a Usage whose type is a DataType. Nominally, if the type is an
 * AttributeDefinition, an AttributeUsage is a usage of an AttributeDefinition to represent
 * the value of some system quality or characteristic. However, other kinds of kernel DataTypes
 * are also allowed, to permit use of DataTypes from the Kernel Model Libraries. An AttributeUsage
 * itself as well as all its nested features must be referential (non-composite).
 */
fun createAttributeUsageMetaClass() = MetaClass(
    name = "AttributeUsage",
    isAbstract = false,
    superclasses = listOf("Usage"),
    attributes = listOf(
        MetaProperty(
            name = "isReference",
            type = "Boolean",
            isDerived = true,
            redefines = listOf("isReference"),
            derivationConstraint = "deriveAttributeUsageIsReference",
            description = "Always true for an AttributeUsage."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkAttributeUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Base::dataValues')",
            description = "An AttributeUsage must directly or indirectly specialize Base::dataValues from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "deriveAttributeUsageIsReference",
            type = ConstraintType.DERIVATION,
            expression = "true",
            description = "Always true for an AttributeUsage."
        ),
        MetaConstraint(
            name = "validateAttributeUsageFeatures",
            type = ConstraintType.VERIFICATION,
            expression = "feature->forAll(not isComposite)",
            description = "All features of an AttributeUsage must be non-composite."
        ),
        MetaConstraint(
            name = "validateAttributeUsageIsReference",
            type = ConstraintType.VERIFICATION,
            expression = "isReference",
            description = "An AttributeUsage is always referential."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "attributeUsageDataValuesBinding",
            baseConcept = "Base::dataValues",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "An AttributeUsage is a Usage whose type is a DataType, representing the value of some system quality or characteristic."
)
