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

import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML AnalysisCaseDefinition metaclass.
 * Specializes: CaseDefinition
 * An AnalysisCaseDefinition is a CaseDefinition for the case of carrying out an analysis.
 */
fun createAnalysisCaseDefinitionMetaClass() = MetaClass(
    name = "AnalysisCaseDefinition",
    isAbstract = false,
    superclasses = listOf("CaseDefinition"),
    constraints = listOf(
        MetaConstraint(
            name = "checkAnalysisCaseDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('AnalysisCases::AnalysisCase')",
            description = "An AnalysisCaseDefinition must directly or indirectly specialize the base AnalysisCaseDefinition AnalysisCases::AnalysisCase from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveAnalysisCaseDefinitionResultExpression",
            type = ConstraintType.DERIVATION,
            expression = """
                let results : OrderedSet(ResultExpressionMembership) =
                    featureMembership->
                        selectByKind(ResultExpressionMembership) in
                if results->isEmpty() then null
                else results->first().ownedResultExpression
                endif
            """.trimIndent(),
            description = "The resultExpression of an AnalysisCaseDefinition is the ownedResultExpression of its ResultExpressionMembership, if any."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "analysisCaseDefinitionAnalysisCaseBinding",
            baseConcept = "AnalysisCases::AnalysisCase",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "An AnalysisCaseDefinition is a CaseDefinition for the case of carrying out an analysis."
)
