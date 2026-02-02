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
 * KerML MetadataAccessExpression metaclass.
 * Specializes: Expression
 * A MetadataAccessExpression is an Expression whose result is a sequence of instances of Metaclasses
 * representing the metadataFeatures of the referencedElement.
 */
fun createMetadataAccessExpressionMetaClass() = MetaClass(
    name = "MetadataAccessExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveMetadataAccessExpressionReferencedElement",
            type = ConstraintType.DERIVATION,
            expression = "let membership : Membership = ownedMembership->first() in if membership = null then null else membership.memberElement endif",
            description = "The referencedElement of a MetadataAccessExpression is the memberElement of its first ownedMembership (if any)."
        ),
        MetaConstraint(
            name = "validateMetadataAccessExpressionReferencedElement",
            type = ConstraintType.VERIFICATION,
            expression = "referencedElement <> null",
            description = "A MetadataAccessExpression must have a non-null referencedElement."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "metadataAccessExpressionEvaluationsBinding",
            baseConcept = "Performances::metadataAccessEvaluations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "evaluate",
            returnType = "Element",
            returnLowerBound = 0,
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "target", type = "Element")
            ),
            redefines = "evaluate",
            preconditions = listOf("isModelLevelEvaluable"),
            body = "referencedElement.metadataFeature->collect(m | m.metaclass)->selectByKind(Metaclass)->collect(mc | metaclassFeature(mc))",
            description = "The model-level evaluation of a MetadataAccessExpression results in instances of Metaclasses representing metadata."
        ),
        MetaOperation(
            name = "metaclassFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "metaclass", type = "Metaclass")
            ),
            body = "metaclass.ownedFeature->select(f | f.name = referencedElement.name)->any(true)",
            description = "Returns the Feature of the given Metaclass with the same name as the referencedElement."
        ),
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            redefines = "modelLevelEvaluable",
            body = "true",
            description = "A MetadataAccessExpression is always model-level evaluable."
        )
    ),
    description = "A MetadataAccessExpression is an Expression whose result is a sequence of instances of Metaclasses representing the metadataFeatures of the referencedElement."
)
