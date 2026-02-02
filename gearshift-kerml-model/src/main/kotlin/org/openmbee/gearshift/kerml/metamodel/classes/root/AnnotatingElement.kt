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
package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * KerML AnnotatingElement metaclass.
 * Specializes: Element
 * An element that annotates other elements.
 */
fun createAnnotatingElementMetaClass() = MetaClass(
    name = "AnnotatingElement",
    isAbstract = false,
    superclasses = listOf("Element"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveAnnotatingElementAnnotatedElement",
            type = ConstraintType.DERIVATION,
            expression = """
                if annotation->notEmpty() then annotation.annotatedElement
                else Sequence{owningNamespace} endif
            """.trimIndent(),
            description = "Derivation for AnnotatingElement::annotatedElement"
        ),
        MetaConstraint(
            name = "deriveAnnotatingElementAnnotation",
            type = ConstraintType.DERIVATION,
            expression = """
                if owningAnnotatingRelationship = null then ownedAnnotatingRelationship
                else ownedAnnotatingRelationship->prepend(owningAnnotatingRelationship) endif
            """.trimIndent(),
            description = "Derivation for AnnotatingElement::annotation"
        ),
        MetaConstraint(
            name = "deriveAnnotatingElementOwnedAnnotatingRelationship",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Annotation)->select(a | a.annotatedElement <> self)",
            description = "Derivation for AnnotatingElement::ownedAnnotatingRelationship"
        )
    ),
    description = "An element that annotates other elements"
)
