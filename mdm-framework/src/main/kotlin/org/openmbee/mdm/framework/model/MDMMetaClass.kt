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

package org.openmbee.mdm.framework.model

import org.openmbee.mdm.framework.meta.ConstraintResult
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.ModelEngine
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Creates the MDM base metaclass with built-in validation constraints.
 *
 * These constraints use native Kotlin implementations since they require
 * metamodel introspection that OCL doesn't natively support.
 */
fun createMDMBaseClass() = MetaClass(
    name = "MDMBaseClass",
    isAbstract = true,
    constraints = listOf(
        MetaConstraint.native(
            name = "validatePropertyLowerBound",
            type = ConstraintType.VERIFICATION,
            description = "Validates that required properties (lowerBound >= 1) have values set"
        ) { element, engine ->
            val obj = element as MDMObject
            val metaClass = obj.metaClass
            val allProperties = collectAllProperties(metaClass, engine)

            for (property in allProperties) {
                // Skip derived properties - they're computed
                if (property.isDerived) continue

                val value = element.getProperty(property.name)

                // Check required properties (lowerBound >= 1)
                if (property.isRequired && value == null) {
                    return@native ConstraintResult.invalid(
                        "Required property '${property.name}' is not set"
                    )
                }
            }
            ConstraintResult.valid()
        },

        MetaConstraint.native(
            name = "validatePropertyMultiplicity",
            type = ConstraintType.VERIFICATION,
            description = "Validates that collection properties satisfy multiplicity bounds"
        ) { element, engine ->
            val obj = element as MDMObject
            val metaClass = obj.metaClass
            val allProperties = collectAllProperties(metaClass, engine)

            for (property in allProperties) {
                if (property.isDerived) continue

                val value = element.getProperty(property.name)

                // Check multiplicity bounds for collections
                if (value is Collection<*>) {
                    if (property.lowerBound > 0 && value.size < property.lowerBound) {
                        return@native ConstraintResult.invalid(
                            "Property '${property.name}' requires at least ${property.lowerBound} values, but has ${value.size}"
                        )
                    }
                    if (property.upperBound != -1 && value.size > property.upperBound) {
                        return@native ConstraintResult.invalid(
                            "Property '${property.name}' allows at most ${property.upperBound} values, but has ${value.size}"
                        )
                    }
                }
            }
            ConstraintResult.valid()
        }
    ),
    description = "Default base class for MDM Language Validation"
)

/**
 * Collect all properties from a class and its superclasses.
 */
private fun collectAllProperties(metaClass: MetaClass, engine: ModelEngine): List<MetaProperty> {
    val properties = mutableListOf<MetaProperty>()
    properties.addAll(metaClass.attributes)
    val schema = (engine as MDMEngine).schema
    for (superclassName in metaClass.superclasses) {
        schema.getClass(superclassName)?.let { superclass ->
            properties.addAll(collectAllProperties(superclass, engine))
        }
    }
    return properties
}