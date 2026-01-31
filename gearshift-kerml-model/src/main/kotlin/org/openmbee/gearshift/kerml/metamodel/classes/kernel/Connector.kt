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

import org.openmbee.gearshift.framework.meta.BindingCondition
import org.openmbee.gearshift.framework.meta.BindingKind
import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.SemanticBinding

/**
 * KerML Connector metaclass.
 * Specializes: Feature, Relationship
 * A feature and relationship that represents a connector.
 */
fun createConnectorMetaClass() = MetaClass(
    name = "Connector",
    isAbstract = false,
    superclasses = listOf("Feature", "Relationship"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConnectorTypeFeaturing",
            type = ConstraintType.IMPLICIT_TYPE_FEATURING,
            expression = """
                relatedFeature->forAll(f |
                    if featuringType->isEmpty() then f.isFeaturedWithin(null)
                    else featuringType->forAll(t | f.isFeaturedWithin(t))
                    endif)
            """.trimIndent(),
            description = "Each relatedFeature of a Connector must have each featuringType of the Connector as a direct or indirect featuringType (where a Feature with no featuringType is treated as if the Classifier Base::Anything was its featuringType)."
        ),
        MetaConstraint(
            name = "validateConnectorBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "connectorEnd->size() > 2 implies not specializesFromLibrary('Links::BinaryLink')",
            description = "If a Connector has more than two connectorEnds, then it must not specialize, directly or indirectly, the Association BinaryLink from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "validateConnectorRelatedFeatures",
            type = ConstraintType.VERIFICATION,
            expression = "not isAbstract implies relatedFeature->size() >= 2",
            description = "If a Connector is concrete (not abstract), then it must have at least two relatedFeatures."
        ),
        MetaConstraint(
            name = "deriveConnectorAssociation",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "type->selectByKind(Association)",
            description = "The Associations that type the Connector."
        ),
        MetaConstraint(
            name = "deriveConnectorConnectorEnd",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "endFeature",
            description = "The connectorEnds of a Connector are its endFeatures, which redefine the endFeatures of the associations of the Connector."
        ),
        MetaConstraint(
            name = "deriveConnectorDefaultFeaturingType",
            type = ConstraintType.DERIVATION,
            expression = """
                let commonFeaturingTypes : OrderedSet(Type) =
                    relatedFeature->closure(featuringType)->select(t |
                        relatedFeature->forAll(f | f.isFeaturedWithin(t))
                    ) in
                let nearestCommonFeaturingTypes : OrderedSet(Type) =
                    commonFeaturingTypes->reject(t1 |
                        commonFeaturingTypes->exists(t2 |
                            t2 <> t1 and t2->closure(featuringType)->contains(t1)
                        )
                    ) in
                if nearestCommonFeaturingTypes->isEmpty() then null
                else nearestCommonFeaturingTypes->first()
                endif
            """.trimIndent(),
            description = "The defaultFeaturingType of a Connector is the innermost common direct or indirect featuringType of the relatedFeatures of the Connector."
        ),
        MetaConstraint(
            name = "deriveConnectorRelatedFeature",
            type = ConstraintType.DERIVATION,
            expression = "connectorEnd.ownedReferenceSubsetting->select(s | s <> null).subsettedFeature",
            description = "The relatedFeatures of a Connector are the referencedFeatures of its connectorEnds."
        ),
        MetaConstraint(
            name = "deriveConnectorSourceFeature",
            type = ConstraintType.DERIVATION,
            expression = "if relatedFeature->isEmpty() then null else relatedFeature->first() endif",
            description = "The sourceFeature of a Connector is its first relatedFeature (if any)."
        ),
        MetaConstraint(
            name = "deriveConnectorTargetFeature",
            type = ConstraintType.DERIVATION,
            expression = "if relatedFeature->size() < 2 then OrderedSet{} else relatedFeature->subSequence(2, relatedFeature->size())->asOrderedSet() endif",
            description = "The targetFeatures of a Connector are the relatedFeatures other than the sourceFeature."
        )
    ),
    semanticBindings = listOf(
        // Binary connector (2 ends) for AssociationStructure -> binaryLinkObjects
        SemanticBinding(
            name = "connectorBinaryObjectBinding",
            baseConcept = "Objects::binaryLinkObjects",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.CollectionSizeEquals("connectorEnd", 2),
                BindingCondition.HasElementOfType("association", "AssociationStructure")
            ))
        ),
        // Binary connector (2 ends) NOT for AssociationStructure -> binaryLinks
        SemanticBinding(
            name = "connectorBinaryBinding",
            baseConcept = "Links::binaryLinks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.CollectionSizeEquals("connectorEnd", 2),
                BindingCondition.Not(BindingCondition.HasElementOfType("association", "AssociationStructure"))
            ))
        ),
        // Non-binary connector (<> 2 ends) for AssociationStructure -> linkObjects
        SemanticBinding(
            name = "connectorObjectBinding",
            baseConcept = "Objects::linkObjects",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.CollectionSizeNotEquals("connectorEnd", 2),
                BindingCondition.HasElementOfType("association", "AssociationStructure")
            ))
        ),
        // Non-binary connector (<> 2 ends) NOT for AssociationStructure -> links
        SemanticBinding(
            name = "connectorLinksBinding",
            baseConcept = "Links::links",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.CollectionSizeNotEquals("connectorEnd", 2),
                BindingCondition.Not(BindingCondition.HasElementOfType("association", "AssociationStructure"))
            ))
        )
    ),
    description = "A feature and relationship that represents a connector"
)
