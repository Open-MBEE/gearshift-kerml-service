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
package org.openmbee.gearshift.kerml

import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.runtime.ElementFactory
import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.impl.*

/**
 * Factory for creating typed KerML element instances.
 *
 * When MDMEngine creates an element, this factory returns the appropriate
 * typed implementation (e.g., PackageImpl, NamespaceImpl) instead of raw MDMObject.
 *
 * This enables:
 * 1. Type-safe access to element properties via the generated interfaces
 * 2. Correct typed instances when deserializing from storage
 * 3. Proper polymorphic behavior through the generated implementation hierarchy
 */
class KerMLElementFactory : ElementFactory {

    override fun createInstance(className: String, metaClass: MetaClass, engine: MDMEngine): MDMObject {
        if (metaClass.isAbstract) {
            throw IllegalArgumentException("Cannot instantiate abstract class: $className")
        }

        // Create a temporary MDMObject to pass to the migration constructor
        // The Impl classes will copy its properties into themselves
        val temp = MDMObject(className, metaClass)

        return when (className) {
            // Core Elements (excluding abstract: Element, Relationship)
            "AnnotatingElement" -> AnnotatingElementImpl(temp, engine)
            "Annotation" -> AnnotationImpl(temp, engine)
            "Comment" -> CommentImpl(temp, engine)
            "Dependency" -> DependencyImpl(temp, engine)
            "Documentation" -> DocumentationImpl(temp, engine)
            "Membership" -> MembershipImpl(temp, engine)
            "MetadataFeature" -> MetadataFeatureImpl(temp, engine)
            "Namespace" -> NamespaceImpl(temp, engine)
            "OwningMembership" -> OwningMembershipImpl(temp, engine)
            "TextualRepresentation" -> TextualRepresentationImpl(temp, engine)

            // Packages and Imports (excluding abstract: Import)
            "LibraryPackage" -> LibraryPackageImpl(temp, engine)
            "MembershipImport" -> MembershipImportImpl(temp, engine)
            "NamespaceImport" -> NamespaceImportImpl(temp, engine)
            "Package" -> PackageImpl(temp, engine)

            // Types and Classifiers
            "Classifier" -> ClassifierImpl(temp, engine)
            "Class" -> ClassImpl(temp, engine)
            "DataType" -> DataTypeImpl(temp, engine)
            "Metaclass" -> MetaclassImpl(temp, engine)
            "Structure" -> StructureImpl(temp, engine)
            "Type" -> TypeImpl(temp, engine)

            // Features
            "Feature" -> FeatureImpl(temp, engine)
            "FeatureChaining" -> FeatureChainingImpl(temp, engine)
            "FeatureMembership" -> FeatureMembershipImpl(temp, engine)
            "FeatureTyping" -> FeatureTypingImpl(temp, engine)
            "FeatureValue" -> FeatureValueImpl(temp, engine)
            "Multiplicity" -> MultiplicityImpl(temp, engine)
            "MultiplicityRange" -> MultiplicityRangeImpl(temp, engine)
            "TypeFeaturing" -> TypeFeaturingImpl(temp, engine)

            // Relationships
            "Conjugation" -> ConjugationImpl(temp, engine)
            "CrossSubsetting" -> CrossSubsettingImpl(temp, engine)
            "Differencing" -> DifferencingImpl(temp, engine)
            "Disjoining" -> DisjoiningImpl(temp, engine)
            "FeatureInverting" -> FeatureInvertingImpl(temp, engine)
            "Featuring" -> FeaturingImpl(temp, engine)
            "Intersecting" -> IntersectingImpl(temp, engine)
            "Redefinition" -> RedefinitionImpl(temp, engine)
            "ReferenceSubsetting" -> ReferenceSubsettingImpl(temp, engine)
            "Specialization" -> SpecializationImpl(temp, engine)
            "Subclassification" -> SubclassificationImpl(temp, engine)
            "Subsetting" -> SubsettingImpl(temp, engine)
            "Unioning" -> UnioningImpl(temp, engine)

            // Behaviors and Functions
            "Behavior" -> BehaviorImpl(temp, engine)
            "Function" -> FunctionImpl(temp, engine)
            "Interaction" -> InteractionImpl(temp, engine)
            "Invariant" -> InvariantImpl(temp, engine)
            "Predicate" -> PredicateImpl(temp, engine)
            "Step" -> StepImpl(temp, engine)

            // Expressions
            "BooleanExpression" -> BooleanExpressionImpl(temp, engine)
            "CollectExpression" -> CollectExpressionImpl(temp, engine)
            "ConstructorExpression" -> ConstructorExpressionImpl(temp, engine)
            "Expression" -> ExpressionImpl(temp, engine)
            "FeatureChainExpression" -> FeatureChainExpressionImpl(temp, engine)
            "FeatureReferenceExpression" -> FeatureReferenceExpressionImpl(temp, engine)
            "IndexExpression" -> IndexExpressionImpl(temp, engine)
            "InvocationExpression" -> InvocationExpressionImpl(temp, engine)
            "MetadataAccessExpression" -> MetadataAccessExpressionImpl(temp, engine)
            "NullExpression" -> NullExpressionImpl(temp, engine)
            "OperatorExpression" -> OperatorExpressionImpl(temp, engine)
            "SelectExpression" -> SelectExpressionImpl(temp, engine)

            // Literals
            "LiteralBoolean" -> LiteralBooleanImpl(temp, engine)
            "LiteralExpression" -> LiteralExpressionImpl(temp, engine)
            "LiteralInfinity" -> LiteralInfinityImpl(temp, engine)
            "LiteralInteger" -> LiteralIntegerImpl(temp, engine)
            "LiteralRational" -> LiteralRationalImpl(temp, engine)
            "LiteralString" -> LiteralStringImpl(temp, engine)

            // Connectors and Flows
            "Association" -> AssociationImpl(temp, engine)
            "AssociationStructure" -> AssociationStructureImpl(temp, engine)
            "BindingConnector" -> BindingConnectorImpl(temp, engine)
            "Connector" -> ConnectorImpl(temp, engine)
            "Flow" -> FlowImpl(temp, engine)
            "FlowEnd" -> FlowEndImpl(temp, engine)
            "Succession" -> SuccessionImpl(temp, engine)
            "SuccessionFlow" -> SuccessionFlowImpl(temp, engine)

            // Memberships
            "ElementFilterMembership" -> ElementFilterMembershipImpl(temp, engine)
            "EndFeatureMembership" -> EndFeatureMembershipImpl(temp, engine)
            "ParameterMembership" -> ParameterMembershipImpl(temp, engine)
            "ResultExpressionMembership" -> ResultExpressionMembershipImpl(temp, engine)
            "ReturnParameterMembership" -> ReturnParameterMembershipImpl(temp, engine)

            // Other
            "PayloadFeature" -> PayloadFeatureImpl(temp, engine)

            // Fallback for any unknown concrete class
            else -> {
                // Return raw MDMObject for unknown classes
                MDMObject(className, metaClass)
            }
        }
    }

    override fun supportsClass(className: String): Boolean {
        // This factory supports all KerML classes
        return true
    }
}
