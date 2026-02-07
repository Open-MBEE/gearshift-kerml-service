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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * KerML Subclassification metaclass.
 * Specializes: Specialization
 * A Specialization relationship between Classifiers.
 *
 * Association ends (defined in ClassifiersAssociations.kt):
 * - subclassifier : Classifier [1] {redefines specific}
 * - superclassifier : Classifier [1] {redefines general}
 * - owningClassifier : Classifier [0..1] {redefines owningType}
 */
fun createSubclassificationMetaClass() = MetaClass(
    name = "Subclassification",
    isAbstract = false,
    superclasses = listOf("Specialization"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveSubclassificationOwningClassifier",
            type = ConstraintType.DERIVATION,
            expression = "if subclassifier = owningRelatedElement then subclassifier else null endif",
            description = "The Classifier that owns this Subclassification relationship, which must also be its subclassifier."
        )
    ),
    description = "A Specialization relationship between Classifiers"
)
