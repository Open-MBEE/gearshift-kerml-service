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

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.generated.impl.*

/**
 * Factory for wrapping MDMObject instances in typed wrappers.
 */
object Wrappers {

    /**
     * Wrap an MDMObject in its corresponding typed wrapper.
     */
    fun wrap(obj: MDMObject, engine: GearshiftEngine): ModelElement {
        return when (obj.className) {
            "AnnotatingElement" -> AnnotatingElementImpl(obj, engine)
            "Annotation" -> AnnotationImpl(obj, engine)
            "Association" -> AssociationImpl(obj, engine)
            "AssociationStructure" -> AssociationStructureImpl(obj, engine)
            "Behavior" -> BehaviorImpl(obj, engine)
            "BindingConnector" -> BindingConnectorImpl(obj, engine)
            "BooleanExpression" -> BooleanExpressionImpl(obj, engine)
            "Class" -> ClassImpl(obj, engine)
            "Classifier" -> ClassifierImpl(obj, engine)
            "CollectExpression" -> CollectExpressionImpl(obj, engine)
            "Comment" -> CommentImpl(obj, engine)
            "Conjugation" -> ConjugationImpl(obj, engine)
            "Connector" -> ConnectorImpl(obj, engine)
            "ConstructorExpression" -> ConstructorExpressionImpl(obj, engine)
            "CrossSubsetting" -> CrossSubsettingImpl(obj, engine)
            "DataType" -> DataTypeImpl(obj, engine)
            "Dependency" -> DependencyImpl(obj, engine)
            "Differencing" -> DifferencingImpl(obj, engine)
            "Disjoining" -> DisjoiningImpl(obj, engine)
            "Documentation" -> DocumentationImpl(obj, engine)
            "ElementFilterMembership" -> ElementFilterMembershipImpl(obj, engine)
            "EndFeatureMembership" -> EndFeatureMembershipImpl(obj, engine)
            "Expression" -> ExpressionImpl(obj, engine)
            "Feature" -> FeatureImpl(obj, engine)
            "FeatureChainExpression" -> FeatureChainExpressionImpl(obj, engine)
            "FeatureChaining" -> FeatureChainingImpl(obj, engine)
            "FeatureInverting" -> FeatureInvertingImpl(obj, engine)
            "FeatureMembership" -> FeatureMembershipImpl(obj, engine)
            "FeatureReferenceExpression" -> FeatureReferenceExpressionImpl(obj, engine)
            "FeatureTyping" -> FeatureTypingImpl(obj, engine)
            "FeatureValue" -> FeatureValueImpl(obj, engine)
            "Featuring" -> FeaturingImpl(obj, engine)
            "Flow" -> FlowImpl(obj, engine)
            "FlowEnd" -> FlowEndImpl(obj, engine)
            "Function" -> FunctionImpl(obj, engine)
            "IndexExpression" -> IndexExpressionImpl(obj, engine)
            "Interaction" -> InteractionImpl(obj, engine)
            "Intersecting" -> IntersectingImpl(obj, engine)
            "Invariant" -> InvariantImpl(obj, engine)
            "InvocationExpression" -> InvocationExpressionImpl(obj, engine)
            "LibraryPackage" -> LibraryPackageImpl(obj, engine)
            "LiteralBoolean" -> LiteralBooleanImpl(obj, engine)
            "LiteralInfinity" -> LiteralInfinityImpl(obj, engine)
            "LiteralInteger" -> LiteralIntegerImpl(obj, engine)
            "LiteralRational" -> LiteralRationalImpl(obj, engine)
            "LiteralString" -> LiteralStringImpl(obj, engine)
            "Membership" -> MembershipImpl(obj, engine)
            "MembershipImport" -> MembershipImportImpl(obj, engine)
            "Metaclass" -> MetaclassImpl(obj, engine)
            "MetadataAccessExpression" -> MetadataAccessExpressionImpl(obj, engine)
            "MetadataFeature" -> MetadataFeatureImpl(obj, engine)
            "Multiplicity" -> MultiplicityImpl(obj, engine)
            "MultiplicityRange" -> MultiplicityRangeImpl(obj, engine)
            "Namespace" -> NamespaceImpl(obj, engine)
            "NamespaceImport" -> NamespaceImportImpl(obj, engine)
            "NullExpression" -> NullExpressionImpl(obj, engine)
            "OperatorExpression" -> OperatorExpressionImpl(obj, engine)
            "OwningMembership" -> OwningMembershipImpl(obj, engine)
            "Package" -> PackageImpl(obj, engine)
            "ParameterMembership" -> ParameterMembershipImpl(obj, engine)
            "PayloadFeature" -> PayloadFeatureImpl(obj, engine)
            "Predicate" -> PredicateImpl(obj, engine)
            "Redefinition" -> RedefinitionImpl(obj, engine)
            "ReferenceSubsetting" -> ReferenceSubsettingImpl(obj, engine)
            "ResultExpressionMembership" -> ResultExpressionMembershipImpl(obj, engine)
            "ReturnParameterMembership" -> ReturnParameterMembershipImpl(obj, engine)
            "SelectExpression" -> SelectExpressionImpl(obj, engine)
            "Specialization" -> SpecializationImpl(obj, engine)
            "Step" -> StepImpl(obj, engine)
            "Structure" -> StructureImpl(obj, engine)
            "Subclassification" -> SubclassificationImpl(obj, engine)
            "Subsetting" -> SubsettingImpl(obj, engine)
            "Succession" -> SuccessionImpl(obj, engine)
            "SuccessionFlow" -> SuccessionFlowImpl(obj, engine)
            "TextualRepresentation" -> TextualRepresentationImpl(obj, engine)
            "Type" -> TypeImpl(obj, engine)
            "TypeFeaturing" -> TypeFeaturingImpl(obj, engine)
            "Unioning" -> UnioningImpl(obj, engine)
            else -> BaseModelElementImpl(obj, engine)
        }
    }

    /**
     * Wrap an MDMObject with explicit type parameter.
     */
    inline fun <reified T : ModelElement> wrapAs(obj: MDMObject, engine: GearshiftEngine): T {
        return wrap(obj, engine) as T
    }
}

