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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML AnalysisCaseUsage metaclass.
 * Specializes: CaseUsage
 * An AnalysisCaseUsage is a Usage of an AnalysisCaseDefinition.
 */
fun createAnalysisCaseUsageMetaClass() = MetaClass(
    name = "AnalysisCaseUsage",
    isAbstract = false,
    superclasses = listOf("CaseUsage"),
    constraints = listOf(
        MetaConstraint(
            name = "checkAnalysisCaseUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('AnalysisCases::analysisCases')",
            description = "An AnalysisCaseUsage must directly or indirectly specialize the base AnalysisCaseUsage AnalysisCases::analysisCases from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkAnalysisCaseUsageSubAnalysisCaseSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(AnalysisCaseDefinition) or
                owningType.oclIsKindOf(AnalysisCaseUsage)) implies
                specializesFromLibrary('AnalysisCases::AnalysisCase::subAnalysisCases')
            """.trimIndent(),
            description = "A composite AnalysisCaseUsage whose owningType is an AnalysisCaseDefinition or AnalysisCaseUsage must specialize the AnalysisCaseUsage AnalysisCases::AnalysisCase::subAnalysisCases from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveAnalysisCaseUsageResultExpression",
            type = ConstraintType.DERIVATION,
            expression = """
                let results : OrderedSet(ResultExpressionMembership) =
                    featureMembership->
                        selectByKind(ResultExpressionMembership) in
                if results->isEmpty() then null
                else results->first().ownedResultExpression
                endif
            """.trimIndent(),
            description = "The resultExpression of an AnalysisCaseUsage is the ownedResultExpression of its ResultExpressionMembership, if any."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "analysisCaseUsageAnalysisCasesBinding",
            baseConcept = "AnalysisCases::analysisCases",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "analysisCaseUsageSubAnalysisCaseBinding",
            baseConcept = "AnalysisCases::AnalysisCase::subAnalysisCases",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.IsComposite,
                BindingCondition.Or(listOf(
                    BindingCondition.OwningTypeIs("AnalysisCaseDefinition"),
                    BindingCondition.OwningTypeIs("AnalysisCaseUsage")
                ))
            ))
        )
    ),
    description = "An AnalysisCaseUsage is a Usage of an AnalysisCaseDefinition."
)
