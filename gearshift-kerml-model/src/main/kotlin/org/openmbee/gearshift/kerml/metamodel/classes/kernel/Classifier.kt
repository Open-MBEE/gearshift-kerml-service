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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * KerML Classifier metaclass.
 * Specializes: Type
 * A type that classifies instances.
 */
fun createClassifierMetaClass() = MetaClass(
    name = "Classifier",
    isAbstract = false,
    superclasses = listOf("Type"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "computeClassifierFlowForPayloadType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.payloadType->includes(self))",
            isNormative = false,
            description = "The Flows that have this Classifier as a payloadType."
        ),
        MetaConstraint(
            name = "deriveClassifierOwnedSubclassification",
            type = ConstraintType.DERIVATION,
            expression = "ownedSpecialization->selectByKind(Subclassification)",
            description = "The ownedSubclassifications of a Classifier are its ownedSpecializations that are Subclassifications"
        ),
        MetaConstraint(
            name = "validateClassifierMultiplicityDomain",
            type = ConstraintType.VERIFICATION,
            expression = "multiplicity <> null implies multiplicity.featuringType->isEmpty()",
            description = "If a Classifier has a multiplicity, then the multiplicity must have no featuringTypes (meaning that its domain is implicitly Base::Anything)"
        )
    ),
    description = "A type that classifies instances"
)
