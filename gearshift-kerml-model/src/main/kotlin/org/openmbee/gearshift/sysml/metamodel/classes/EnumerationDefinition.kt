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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML EnumerationDefinition metaclass.
 * Specializes: AttributeDefinition
 * An EnumerationDefinition is an AttributeDefinition all of whose instances are given by an
 * explicit list of enumeratedValues. This is realized by requiring that the EnumerationDefinition
 * have isVariation = true, with the enumeratedValues being its variants.
 */
fun createEnumerationDefinitionMetaClass() = MetaClass(
    name = "EnumerationDefinition",
    isAbstract = false,
    superclasses = listOf("AttributeDefinition"),
    attributes = listOf(
        MetaProperty(
            name = "isVariation",
            type = "Boolean",
            redefines = listOf("isVariation"),
            description = "An EnumerationDefinition is considered semantically to be a variation whose allowed variants are its enumeratedValues."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateEnumerationDefinitionIsVariation",
            type = ConstraintType.VERIFICATION,
            expression = "isVariation",
            description = "An EnumerationDefinition must be a variation."
        )
    ),
    description = "An EnumerationDefinition is an AttributeDefinition all of whose instances are given by an explicit list of enumeratedValues."
)
