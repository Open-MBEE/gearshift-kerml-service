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
            name = "checkConnectorBinaryObjectSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "connectorEnd->size() = 2 and association->exists(oclIsKindOf(AssociationStructure))",
            libraryTypeName = "Objects::binaryLinkObjects",
            description = "A binary Connector for an AssociationStructure must directly or indirectly specialize the base Connector Objects::binaryLinkObjects from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkConnectorBinarySpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "connectorEnd->size() = 2 and not association->exists(oclIsKindOf(AssociationStructure))",
            libraryTypeName = "Links::binaryLinks",
            description = "A binary Connector must directly or indirectly specialize the base Connector Links::binaryLinks from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkConnectorObjectSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "connectorEnd->size() <> 2 and association->exists(oclIsKindOf(AssociationStructure))",
            libraryTypeName = "Objects::linkObjects",
            description = "A Connector for an AssociationStructure must directly or indirectly specialize the base Connector Objects::linkObjects from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkConnectorSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "connectorEnd->size() <> 2 and not association->exists(oclIsKindOf(AssociationStructure))",
            libraryTypeName = "Links::links",
            description = "A Connector must directly or indirectly specialize the base Connector Links::links from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkConnectorTypeFeaturing",
            type = ConstraintType.VERIFICATION,
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
        )
    ),
    description = "A feature and relationship that represents a connector"
)
