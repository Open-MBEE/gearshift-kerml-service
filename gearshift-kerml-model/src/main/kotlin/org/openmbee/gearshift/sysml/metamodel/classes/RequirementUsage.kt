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
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML RequirementUsage metaclass.
 * Specializes: ConstraintUsage
 * A RequirementUsage is a Usage of a RequirementDefinition.
 */
fun createRequirementUsageMetaClass() = MetaClass(
    name = "RequirementUsage",
    isAbstract = false,
    superclasses = listOf("ConstraintUsage"),
    attributes = listOf(
        MetaProperty(
            name = "reqId",
            type = "String",
            lowerBound = 0,
            upperBound = 1,
            redefines = "declaredShortName",
            description = "An optional modeler-specified identifier for this RequirementUsage, which is the declaredShortName for the RequirementUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "requirementUsageRequirementChecksBinding",
            baseConcept = "Requirements::requirementChecks",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A RequirementUsage is a Usage of a RequirementDefinition."
)
