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

/**
 * KerML Association metaclass.
 * Specializes: Classifier, Relationship
 * A classifier and relationship that represents an association.
 */
fun createAssociationMetaClass() = MetaClass(
    name = "Association",
    isAbstract = false,
    superclasses = listOf("Classifier", "Relationship"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkAssociationBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "associationEnd->size() = 2 implies specializesFromLibrary('Links::BinaryLink')",
            description = "A binary Association must directly or indirectly specialize the base Association Links::binaryLink from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkAssociationSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Links::Link')",
            libraryTypeName = "Links::Link",
            description = "An Association must directly or indirectly specialize the base Association Links::Link from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "conditionalImplicitBinaryLink",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "associationEnd->size() = 2",
            libraryTypeName = "Links::BinaryLink",
            description = "Binary associations implicitly specialize Links::BinaryLink."
        ),
        MetaConstraint(
            name = "deriveAssociationAssociationEnd",
            type = ConstraintType.DERIVATION,
            expression = "feature->select(isEnd)",
            description = "The associationEnds of an Association are those of its features for which isEnd = true."
        ),
        MetaConstraint(
            name = "deriveAssociationRelatedType",
            type = ConstraintType.DERIVATION,
            expression = "associationEnd.type",
            description = "The relatedTypes of an Association are the types of its associationEnds."
        ),
        MetaConstraint(
            name = "deriveAssociationSourceType",
            type = ConstraintType.DERIVATION,
            expression = "if relatedType->isEmpty() then null else relatedType->first() endif",
            description = "The sourceType of an Association is its first relatedType (if any)."
        ),
        MetaConstraint(
            name = "deriveAssociationTargetType",
            type = ConstraintType.DERIVATION,
            expression = "if relatedType->size() < 2 then OrderedSet{} else relatedType->subSequence(2, relatedType->size())->asOrderedSet() endif",
            description = "The targetType of an Association is all relatedTypes after the first."
        ),
        MetaConstraint(
            name = "validateAssociationBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "associationEnd->size() > 2 implies not specializesFromLibrary('Links::BinaryLink')",
            description = "If an Association has more than two associationEnds, then it must not specialize, directly or indirectly, the Association BinaryLink from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "validateAssociationEndTypes",
            type = ConstraintType.VERIFICATION,
            expression = "ownedEndFeature->forAll(type->size() = 1)",
            description = "The ownedEndFeatures of an Association must have exactly one type."
        ),
        MetaConstraint(
            name = "validateAssociationRelatedTypes",
            type = ConstraintType.VERIFICATION,
            expression = "not isAbstract implies relatedType->size() >= 2",
            description = "If an Association is concrete (not abstract), then it must have at least two relatedTypes."
        ),
        MetaConstraint(
            name = "validateAssociationStructureIntersection",
            type = ConstraintType.VERIFICATION,
            expression = "oclIsKindOf(Structure) = oclIsKindOf(AssociationStructure)",
            description = "If an Association is also a kind of Structure, then it must be an AssociationStructure."
        )
    ),
    description = "A classifier and relationship that represents an association"
)
