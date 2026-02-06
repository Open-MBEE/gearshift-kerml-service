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
package org.openmbee.gearshift.kerml.metamodel.associations

import org.openmbee.mdm.framework.meta.AggregationKind
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 6: Annotation
 * Defines associations for Annotation and AnnotatingElement.
 */
fun createAnnotationAssociations(): List<MetaAssociation> {

    // Element has textualRepresentation : TextualRepresentation [0..*] {ordered, derived, subsets annotatingElement, ownedElement}
    val representedElementTextualRepresentationAssociation = MetaAssociation(
        name = "representedElementTextualRepresentationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "representedElement",
            type = "Element",
            upperBound = 1,
            lowerBound = 1,
            isDerived = true,
            subsets = listOf("owner"),
            redefines = listOf("annotatedElement"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "textualRepresentation",
            type = "TextualRepresentation",
            upperBound = -1,
            lowerBound = 0,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("annotatingElement", "ownedElement"),
            derivationConstraint = "deriveElementTextualRepresentation"
        ),
    )

    // Element has ownedAnnotation : Annotation [0..*] {ordered, derived, subsets annotation, ownedRelationship}
    val owningAnnotatedElementOwnedAnnotationAssociation = MetaAssociation(
        name = "owningAnnotatedElementOwnedAnnotationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningAnnotatedElement",
            type = "Element",
            upperBound = 1,
            lowerBound = 0,
            isDerived = true,
            subsets = listOf("annotatedElement", "owningRelatedElement"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedAnnotation",
            type = "Annotation",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("annotation", "ownedRelationship"),
            derivationConstraint = "deriveElementOwnedAnnotation"
        ),
    )

    // Annotation has annotatedElement : Element [1..1] {redefines target}
    val annotationAnnotatedElementAssociation = MetaAssociation(
        name = "annotationAnnotatedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "annotation",
            type = "Annotation",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "annotatedElement",
            type = "Element",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target"),
        ),
    )

    // AnnotatingElement has annotation : Annotation [0..*] {ordered, derived, subsets sourceRelationship}
    val annotatingElementAnnotationAssociation = MetaAssociation(
        name = "annotatingElementAnnotationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "annotatingElement",
            type = "AnnotatingElement",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("source"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "annotation",
            type = "Annotation",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("sourceRelationship"),
            derivationConstraint = "deriveAnnotatingElementAnnotation"
        ),
    )

    // Annotation has ownedAnnotatingElement : AnnotatingElement [0..1] {subsets annotatingElement, ownedRelatedElement}
    val owningAnnotatingRelationshipOwnedAnnotatingElementAssociation = MetaAssociation(
        name = "owningAnnotatingRelationshipOwnedAnnotatingElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningAnnotatingRelationship",
            type = "Annotation",
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("annotation", "owningRelationship"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedAnnotatingElement",
            type = "AnnotatingElement",
            lowerBound = 0,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("annotatingElement", "ownedRelatedElement"),
        )
    )

    // AnnotatingElement has ownedAnnotatingRelationship : Annotation [0..*] {ordered, derived, subsets annotation, ownedRelationship}
    val owningAnnotatingElementOwnedAnnotatingRelationshipAssociation = MetaAssociation(
        name = "owningAnnotatingElementOwnedAnnotatingRelationshipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningAnnotatingElement",
            type = "AnnotatingElement",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("annotatingElement", "owningRelatedElement"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedAnnotatingRelationship",
            type = "Annotation",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("annotation", "ownedRelationship"),
            derivationConstraint = "deriveAnnotatingElementOwnedAnnotatingRelationship"
        )
    )

    // Element has documentation : Documentation [0..*] {ordered, derived, subsets annotatingElement, ownedElement}
    val documentedElementDocumentationAssociation = MetaAssociation(
        name = "documentedElementDocumentationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "documentedElement",
            type = "Element",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owner"),
            redefines = listOf("annotatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "documentation",
            type = "Documentation",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("annotatingElement", "ownedElement"),
            derivationConstraint = "deriveElementDocumentation"
        )
    )

    // AnnotatingElement has annotatedElement : Element [1..*] {ordered, derived}
    val annotatingElementAnnotatedElementAssociation = MetaAssociation(
        name = "annotatingElementAnnotatedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "annotatingElement",
            type = "AnnotatingElement",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            isNavigable = false,
        ),
        targetEnd = MetaAssociationEnd(
            name = "annotatedElement",
            type = "Element",
            lowerBound = 1,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveAnnotatingElementAnnotatedElement"
        ),
    )
    return listOf(
        representedElementTextualRepresentationAssociation,
        owningAnnotatedElementOwnedAnnotationAssociation,
        annotationAnnotatedElementAssociation,
        annotatingElementAnnotationAssociation,
        owningAnnotatingRelationshipOwnedAnnotatingElementAssociation,
        owningAnnotatingElementOwnedAnnotatingRelationshipAssociation,
        documentedElementDocumentationAssociation,
        annotatingElementAnnotatedElementAssociation,
    )
}
