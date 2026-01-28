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
 * KerML Subsetting metaclass.
 * Specializes: Specialization
 * A specialization where one feature subsets another.
 */
fun createSubsettingMetaClass() = MetaClass(
    name = "Subsetting",
    isAbstract = false,
    superclasses = listOf("Specialization"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateSubsettingConstantConformance",
            type = ConstraintType.VERIFICATION,
            expression = "subsettedFeature.isConstant and subsettingFeature.isVariable implies subsettingFeature.isConstant",
            description = "If the subsettedFeature of a Subsetting has isConstant = true and the subsettingFeature has isVariable = true, then the subsettingFeature must have isConstant = true."
        ),
        MetaConstraint(
            name = "validateSubsettingFeaturingTypes",
            type = ConstraintType.VERIFICATION,
            expression = "subsettingFeature.canAccess(subsettedFeature)",
            description = "The subsettedFeature must be accessible by the subsettingFeature."
        ),
        MetaConstraint(
            name = "validateSubsettingUniquenessConformance",
            type = ConstraintType.VERIFICATION,
            expression = "subsettedFeature.isUnique implies subsettingFeature.isUnique",
            description = "If the subsettedFeature of a Subsetting has isUnique = true, then the subsettingFeature must have isUnique = true."
        )
    ),
    description = "A specialization where one feature subsets another"
)
