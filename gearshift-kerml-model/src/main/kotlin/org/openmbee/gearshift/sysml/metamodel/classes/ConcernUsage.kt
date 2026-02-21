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
 * SysML ConcernUsage metaclass.
 * Specializes: RequirementUsage
 * A ConcernUsage is a Usage of a ConcernDefinition.
 */
fun createConcernUsageMetaClass() = MetaClass(
    name = "ConcernUsage",
    isAbstract = false,
    superclasses = listOf("RequirementUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConcernUsageFramedConcernSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(FramedConcernMembership) implies
                specializesFromLibrary('Requirements::RequirementCheck::concerns')
            """.trimIndent(),
            description = "If a ConcernUsage is owned via a FramedConcernMembership, then it must directly or indirectly specialize the ConcernUsage Requirements::RequirementCheck::concerns from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkConcernUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Requirements::concernChecks')",
            description = "A ConcernUsage must directly or indirectly specialize the base ConcernUsage Requirements::concernChecks from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "concernUsageConcernChecksBinding",
            baseConcept = "Requirements::concernChecks",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A ConcernUsage is a Usage of a ConcernDefinition."
)
