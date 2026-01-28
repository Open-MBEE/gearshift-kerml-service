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

import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint

/**
 * KerML Redefinition metaclass.
 * Specializes: Subsetting
 * A subsetting where one feature redefines another.
 */
fun createRedefinitionMetaClass() = MetaClass(
    name = "Redefinition",
    isAbstract = false,
    superclasses = listOf("Subsetting"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateRedefinitionDirectionConformance",
            type = ConstraintType.VERIFICATION,
            expression = """
                let featuringTypes: Sequence(Type) =
                    if redefiningFeature.isVariable then Sequence{redefiningFeature.owningType}
                    else redefiningFeature.featuringType
                    endif in
                featuringTypes->forAll(t |
                    let direction: FeatureDirectionKind = t.directionOf(redefinedFeature) in
                    ((direction = FeatureDirectionKind::_'in' or
                      direction = FeatureDirectionKind::out) implies
                        redefiningFeature.direction = direction)
                    and
                    (direction = FeatureDirectionKind::inout implies
                        redefiningFeature.direction <> null))
            """.trimIndent(),
            description = "If the redefinedFeature of a Redefinition has a direction of in or out (relative to any featuringType of the redefiningFeature or the owningType, if the redefiningFeature has isVariable = true), then the redefiningFeature must have the same direction. If the redefinedFeature has a direction of inout, then the redefiningFeature must have a non-null direction."
        ),
        MetaConstraint(
            name = "validateRedefinitionEndConformance",
            type = ConstraintType.VERIFICATION,
            expression = "redefinedFeature.isEnd implies redefiningFeature.isEnd",
            description = "If the redefinedFeature of a Redefinition has isEnd = true, then the redefiningFeature must have isEnd = true."
        ),
        MetaConstraint(
            name = "validateRedefinitionFeaturingTypes",
            type = ConstraintType.VERIFICATION,
            expression = """
                let anythingType: Type =
                    redefiningFeature.resolveGlobal('Base::Anything').memberElement.oclAsType(Type) in
                let redefiningFeaturingTypes: Set(Type) =
                    if redefiningFeature.isVariable then Set{redefiningFeature.owningType}
                    else redefiningFeature.featuringType->asSet()->including(anythingType)
                    endif in
                let redefinedFeaturingTypes: Set(Type) =
                    if redefinedFeature.isVariable then Set{redefinedFeature.owningType}
                    else redefinedFeature.featuringType->asSet()->including(anythingType)
                    endif in
                redefiningFeaturingTypes <> redefinedFeaturingTypes
            """.trimIndent(),
            description = "The redefiningFeature of a Redefinition must have at least one featuringType that is not also a featuringType of the redefinedFeature."
        )
    ),
    description = "A subsetting where one feature redefines another"
)
