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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML MetadataFeature metaclass.
 * Specializes: AnnotatingElement, Feature
 * A MetadataFeature is a Feature that is an AnnotatingElement used to annotate another Element with
 * metadata. It is typed by a Metaclass. All its ownedFeatures must redefine features of its metaclass
 * and any feature bindings must be model-level evaluable.
 */
fun createMetadataFeatureMetaClass() = MetaClass(
    name = "MetadataFeature",
    isAbstract = false,
    superclasses = listOf("AnnotatingElement", "Feature"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkMetadataFeatureSemanticSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "isSemantic() implies let annotatedTypes : Sequence(Type) = annotatedElement->selectAsKind(Type) in let baseTypes : Sequence(MetadataFeature) = evaluateFeature(resolveGlobal('Metaobjects::SemanticMetadata::baseType').memberElement.oclAsType(Feature))->selectAsKind(MetadataFeature) in annotatedTypes->notEmpty() and baseTypes()->notEmpty() and baseTypes()->first().isSyntactic() implies let annotatedType : Type = annotatedTypes->first() in let baseType : Element = baseTypes->first().syntaxElement() in if annotatedType.oclIsKindOf(Classifier) and baseType.oclIsKindOf(Feature) then baseType.oclAsType(Feature).type->forAll(t | annotatedType.specializes(t)) else if baseType.oclIsKindOf(Type) then annotatedType.specializes(baseType.oclAsType(Type)) else true endif",
            description = "If this MetadataFeature is an application of SemanticMetadata, then its annotatingElement must be a Type with appropriate specializations."
        ),
        MetaConstraint(
            name = "deriveMetadataFeatureMetaclass",
            type = ConstraintType.DERIVATION,
            expression = "let metaclassTypes : Sequence(Type) = type->selectByKind(Metaclass) in if metaclassTypes->isEmpty() then null else metaclassTypes->first() endif",
            description = "The metaclass of a MetadataFeature is one of its types that is a Metaclass."
        ),
        MetaConstraint(
            name = "validateMetadataFeatureAnnotatedElement",
            type = ConstraintType.VERIFICATION,
            expression = "let baseAnnotatedElementFeature : Feature = resolveGlobal('Metaobjects::Metaobject::annotatedElement').memberElement.oclAsType(Feature) in let annotatedElementFeatures : OrderedSet(Feature) = feature->select(specializes(baseAnnotatedElementFeature))->excluding(baseAnnotatedElementFeature) in annotatedElementFeatures->notEmpty() implies let annotatedElementTypes : Set(Feature) = annotatedElementFeatures.typing.type->asSet() in let metaclasses : Set(Metaclass) = annotatedElement.oclType().qualifiedName->collect(qn | resolveGlobal(qn).memberElement.oclAsType(Metaclass)) in metaclasses->forAll(m | annotatedElementTypes->exists(t | m.specializes(t)))",
            description = "The annotatedElements of a MetadataFeature must have an abstract syntax metaclass consistent with the annotatedElement declarations for the MetadataFeature."
        ),
        MetaConstraint(
            name = "validateMetadataFeatureBody",
            type = ConstraintType.VERIFICATION,
            expression = "ownedFeature->closure(ownedFeature)->forAll(f | f.declaredName = null and f.declaredShortName = null and f.valuation <> null implies f.valuation.value.isModelLevelEvaluable and f.redefinition.redefinedFeature->size() = 1)",
            description = "Each ownedFeature of a MetadataFeature must have no declared name, redefine a single Feature, either have no featureValue or a featureValue with a valueExpression that is model-level evaluable, and only have ownedFeatures that also meet these restrictions."
        ),
        MetaConstraint(
            name = "validateMetadataFeatureMetaclass",
            type = ConstraintType.VERIFICATION,
            expression = "type->selectByKind(Metaclass).size() = 1",
            description = "A MetadataFeature must have exactly one type that is a Metaclass."
        ),
        MetaConstraint(
            name = "validateMetadataFeatureMetaclassNotAbstract",
            type = ConstraintType.VERIFICATION,
            expression = "not metaclass.isAbstract",
            description = "The metaclass of a MetadataFeature must not be abstract."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "metadataFeatureMetaobjectsBinding",
            baseConcept = "Metaobjects::metaobjects",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "evaluateFeature",
            returnType = "Element",
            returnLowerBound = 0,
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "baseFeature", type = "Feature")
            ),
            body = MetaOperation.ocl("let selectedFeatures : Sequence(Feature) = feature->select(closure(ownedRedefinition.redefinedFeature)->includes(baseFeature)) in if selectedFeatures->isEmpty() then null else let selectedFeature : Feature = selectedFeatures->first() in let featureValues : FeatureValue = selectedFeature->closure(ownedRedefinition.redefinedFeature).ownedMember->selectAsKind(FeatureValue) in if featureValues->isEmpty() then null else featureValues->first().value.evaluate(self) endif"),
            description = "If the given baseFeature is a feature of this MetadataFeature, or is directly or indirectly redefined by a feature, then return the result of evaluating the appropriate (model-level evaluable) valueExpression for it (if any), with the MetadataFeature as the target."
        ),
        MetaOperation(
            name = "isSemantic",
            returnType = "Boolean",
            body = MetaOperation.ocl("specializesFromLibrary('Metaobjects::SemanticMetadata')"),
            description = "Check if this MetadataFeature has a metaclass which is a kind of SemanticMetadata."
        ),
        MetaOperation(
            name = "isSyntactic",
            returnType = "Boolean",
            body = MetaOperation.ocl("specializesFromLibrary('KerML::Element')"),
            description = "Check if this MetadataFeature has a metaclass that is a kind of KerML::Element (that is, it is from the reflective abstract syntax model)."
        ),
        MetaOperation(
            name = "syntaxElement",
            returnType = "Element",
            returnLowerBound = 0,
            returnUpperBound = 1,
            preconditions = listOf("isSyntactic()"),
            // TODO: Implement syntaxElement body - requires MOF reflection
            description = "If this MetadataFeature reflectively represents a model element, then return the corresponding Element instance from the MOF abstract syntax representation of the model."
        )
    ),
    description = "A MetadataFeature is a Feature that is an AnnotatingElement used to annotate another Element with metadata. It is typed by a Metaclass."
)
