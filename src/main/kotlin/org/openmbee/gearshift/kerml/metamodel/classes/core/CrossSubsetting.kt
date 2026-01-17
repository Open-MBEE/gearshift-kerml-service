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

import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint

/**
 * KerML CrossSubsetting metaclass.
 * Specializes: Subsetting
 * A Subsetting that relates Features across different contextual types.
 */
fun createCrossSubsettingMetaClass() = MetaClass(
    name = "CrossSubsetting",
    isAbstract = false,
    superclasses = listOf("Subsetting"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateCrossSubsettingCrossedFeature",
            type = ConstraintType.VERIFICATION,
            expression = """
                crossingFeature.isEnd and crossingFeature.owningType <> null implies
                let endFeatures: Sequence(Feature) = crossingFeature.owningType.endFeature in
                let chainingFeatures: Sequence(Feature) = crossedFeature.chainingFeature in
                chainingFeatures->size() = 2 and
                endFeatures->size() = 2 implies
                chainingFeatures->at(1) = endFeatures->excluding(crossingFeature)->at(1)
            """.trimIndent(),
            description = "The crossedFeature of a CrossSubsetting must have exactly two chainingFeatures. If the crossingFeature of the CrossSubsetting is one of two endFeatures, then the first chainingFeature must be the other endFeature."
        ),
        MetaConstraint(
            name = "validateCrossSubsettingCrossingFeature",
            type = ConstraintType.VERIFICATION,
            expression = """
                crossingFeature.isEnd and
                crossingFeature.owningType <> null and
                crossingFeature.owningType.endFeature->size() > 1
            """.trimIndent(),
            description = "The crossingFeature of a CrossSubsetting must be an endFeature that is owned by a Type with at least two endFeatures."
        )
    ),
    description = "A Subsetting that relates Features across different contextual types"
)