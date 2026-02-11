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

package org.openmbee.gearshift.generated

import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.runtime.ElementFactory
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
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

    override var engine: MDMEngine? = null

    override fun createInstance(className: String, metaClass: MetaClass): MDMObject {
        if (metaClass.isAbstract) {
            throw IllegalArgumentException("Cannot instantiate abstract class: $className")
        }

        val eng = engine ?: throw IllegalStateException("Engine not set on factory")

        return when (className) {
            "AnnotatingElement" -> AnnotatingElementImpl(className, metaClass, eng)
            "Annotation" -> AnnotationImpl(className, metaClass, eng)
            "Association" -> AssociationImpl(className, metaClass, eng)
            "AssociationStructure" -> AssociationStructureImpl(className, metaClass, eng)
            "Behavior" -> BehaviorImpl(className, metaClass, eng)
            "BindingConnector" -> BindingConnectorImpl(className, metaClass, eng)
            "BooleanExpression" -> BooleanExpressionImpl(className, metaClass, eng)
            "Class" -> ClassImpl(className, metaClass, eng)
            "Classifier" -> ClassifierImpl(className, metaClass, eng)
            "CollectExpression" -> CollectExpressionImpl(className, metaClass, eng)
            "Comment" -> CommentImpl(className, metaClass, eng)
            "Conjugation" -> ConjugationImpl(className, metaClass, eng)
            "Connector" -> ConnectorImpl(className, metaClass, eng)
            "ConstructorExpression" -> ConstructorExpressionImpl(className, metaClass, eng)
            "CrossSubsetting" -> CrossSubsettingImpl(className, metaClass, eng)
            "DataType" -> DataTypeImpl(className, metaClass, eng)
            "Dependency" -> DependencyImpl(className, metaClass, eng)
            "Differencing" -> DifferencingImpl(className, metaClass, eng)
            "Disjoining" -> DisjoiningImpl(className, metaClass, eng)
            "Documentation" -> DocumentationImpl(className, metaClass, eng)
            "ElementFilterMembership" -> ElementFilterMembershipImpl(className, metaClass, eng)
            "EndFeatureMembership" -> EndFeatureMembershipImpl(className, metaClass, eng)
            "Expression" -> ExpressionImpl(className, metaClass, eng)
            "Feature" -> FeatureImpl(className, metaClass, eng)
            "FeatureChainExpression" -> FeatureChainExpressionImpl(className, metaClass, eng)
            "FeatureChaining" -> FeatureChainingImpl(className, metaClass, eng)
            "FeatureInverting" -> FeatureInvertingImpl(className, metaClass, eng)
            "FeatureMembership" -> FeatureMembershipImpl(className, metaClass, eng)
            "FeatureReferenceExpression" -> FeatureReferenceExpressionImpl(className, metaClass, eng)
            "FeatureTyping" -> FeatureTypingImpl(className, metaClass, eng)
            "FeatureValue" -> FeatureValueImpl(className, metaClass, eng)
            "Featuring" -> FeaturingImpl(className, metaClass, eng)
            "Flow" -> FlowImpl(className, metaClass, eng)
            "FlowEnd" -> FlowEndImpl(className, metaClass, eng)
            "Function" -> FunctionImpl(className, metaClass, eng)
            "IndexExpression" -> IndexExpressionImpl(className, metaClass, eng)
            "Interaction" -> InteractionImpl(className, metaClass, eng)
            "Intersecting" -> IntersectingImpl(className, metaClass, eng)
            "Invariant" -> InvariantImpl(className, metaClass, eng)
            "InvocationExpression" -> InvocationExpressionImpl(className, metaClass, eng)
            "LibraryPackage" -> LibraryPackageImpl(className, metaClass, eng)
            "LiteralBoolean" -> LiteralBooleanImpl(className, metaClass, eng)
            "LiteralExpression" -> LiteralExpressionImpl(className, metaClass, eng)
            "LiteralInfinity" -> LiteralInfinityImpl(className, metaClass, eng)
            "LiteralInteger" -> LiteralIntegerImpl(className, metaClass, eng)
            "LiteralRational" -> LiteralRationalImpl(className, metaClass, eng)
            "LiteralString" -> LiteralStringImpl(className, metaClass, eng)
            "Membership" -> MembershipImpl(className, metaClass, eng)
            "MembershipExpose" -> MembershipExposeImpl(className, metaClass, eng)
            "MembershipImport" -> MembershipImportImpl(className, metaClass, eng)
            "Metaclass" -> MetaclassImpl(className, metaClass, eng)
            "MetadataAccessExpression" -> MetadataAccessExpressionImpl(className, metaClass, eng)
            "MetadataFeature" -> MetadataFeatureImpl(className, metaClass, eng)
            "Multiplicity" -> MultiplicityImpl(className, metaClass, eng)
            "MultiplicityRange" -> MultiplicityRangeImpl(className, metaClass, eng)
            "Namespace" -> NamespaceImpl(className, metaClass, eng)
            "NamespaceExpose" -> NamespaceExposeImpl(className, metaClass, eng)
            "NamespaceImport" -> NamespaceImportImpl(className, metaClass, eng)
            "NullExpression" -> NullExpressionImpl(className, metaClass, eng)
            "OperatorExpression" -> OperatorExpressionImpl(className, metaClass, eng)
            "OwningMembership" -> OwningMembershipImpl(className, metaClass, eng)
            "Package" -> PackageImpl(className, metaClass, eng)
            "ParameterMembership" -> ParameterMembershipImpl(className, metaClass, eng)
            "PayloadFeature" -> PayloadFeatureImpl(className, metaClass, eng)
            "Predicate" -> PredicateImpl(className, metaClass, eng)
            "Redefinition" -> RedefinitionImpl(className, metaClass, eng)
            "ReferenceSubsetting" -> ReferenceSubsettingImpl(className, metaClass, eng)
            "Rendering" -> RenderingImpl(className, metaClass, eng)
            "ResultExpressionMembership" -> ResultExpressionMembershipImpl(className, metaClass, eng)
            "ReturnParameterMembership" -> ReturnParameterMembershipImpl(className, metaClass, eng)
            "SelectExpression" -> SelectExpressionImpl(className, metaClass, eng)
            "Specialization" -> SpecializationImpl(className, metaClass, eng)
            "Step" -> StepImpl(className, metaClass, eng)
            "Structure" -> StructureImpl(className, metaClass, eng)
            "Subclassification" -> SubclassificationImpl(className, metaClass, eng)
            "Subsetting" -> SubsettingImpl(className, metaClass, eng)
            "Succession" -> SuccessionImpl(className, metaClass, eng)
            "SuccessionFlow" -> SuccessionFlowImpl(className, metaClass, eng)
            "TextualRepresentation" -> TextualRepresentationImpl(className, metaClass, eng)
            "Type" -> TypeImpl(className, metaClass, eng)
            "TypeFeaturing" -> TypeFeaturingImpl(className, metaClass, eng)
            "Unioning" -> UnioningImpl(className, metaClass, eng)
            "View" -> ViewImpl(className, metaClass, eng)
            "ViewRenderingMembership" -> ViewRenderingMembershipImpl(className, metaClass, eng)
            "Viewpoint" -> ViewpointImpl(className, metaClass, eng)
            "ViewpointPredicate" -> ViewpointPredicateImpl(className, metaClass, eng)

            // Fallback for any unknown concrete class
            else -> MDMObject(className, metaClass)
        }
    }

    override fun supportsClass(className: String): Boolean = true
}

