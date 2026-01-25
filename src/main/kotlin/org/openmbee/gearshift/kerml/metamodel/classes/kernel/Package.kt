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

import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint
import org.openmbee.gearshift.metamodel.MetaOperation
import org.openmbee.gearshift.metamodel.MetaParameter

/**
 * KerML Package metaclass.
 * Specializes: Namespace
 * A Namespace used to group Elements, without any instance-level semantics. It may have one or
 * more model-level evaluable filterCondition Expressions used to filter its importedMemberships.
 * Any imported member must meet all of the filterConditions.
 */
fun createPackageMetaClass() = MetaClass(
    name = "Package",
    isAbstract = false,
    superclasses = listOf("Namespace"),
    attributes = emptyList(),
    operations = listOf(
        MetaOperation(
            name = "importedMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(
                    name = "excluded",
                    type = "Namespace",
                    lowerBound = 0,
                    upperBound = -1
                )
            ),
            description = "Exclude Elements that do not meet all the filterConditions.",
            body = "self.oclAsType(Namespace).importedMemberships(excluded)->select(m | self.includeAsMember(m.memberElement))",
            isQuery = true,
            redefines = "importedMemberships"
        ),
        MetaOperation(
            name = "includeAsMember",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(
                    name = "element",
                    type = "Element",
                    lowerBound = 1,
                    upperBound = 1
                )
            ),
            description = "Determine whether the given element meets all the filterConditions.",
            body = """
                let metadataFeatures: Sequence(AnnotatingElement) =
                    element.ownedAnnotation.annotatingElement->selectByKind(MetadataFeature) in
                self.filterCondition->forAll(cond |
                    metadataFeatures->exists(elem | cond.checkCondition(elem)))
            """.trimIndent(),
            isQuery = true
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "derivePackageFilterCondition",
            type = ConstraintType.DERIVATION,
            expression = "ownedMembership->selectByKind(ElementFilterMembership).condition",
            description = "The filterConditions of a Package are the conditions of its owned ElementFilterMemberships."
        )
    ),
    description = "A Namespace used to group Elements, without any instance-level semantics"
)
