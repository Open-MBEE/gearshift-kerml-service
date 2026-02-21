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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML ReferenceUsage metaclass.
 * Specializes: Usage
 * A ReferenceUsage is a Usage that specifies a non-compositional (isComposite = false) reference
 * to something. The definition of a ReferenceUsage can be any kind of Classifier, with the default
 * being the top-level Classifier Base::Anything from the Kernel Semantic Library.
 */
fun createReferenceUsageMetaClass() = MetaClass(
    name = "ReferenceUsage",
    isAbstract = false,
    superclasses = listOf("Usage"),
    attributes = listOf(
        MetaProperty(
            name = "isReference",
            type = "Boolean",
            isDerived = true,
            redefines = listOf("isReference"),
            derivationConstraint = "deriveReferenceUsageIsReference",
            description = "Always true for a ReferenceUsage."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveReferenceUsageIsReference",
            type = ConstraintType.DERIVATION,
            expression = "true",
            description = "Always true for a ReferenceUsage."
        ),
        MetaConstraint(
            name = "validateReferenceUsageIsReference",
            type = ConstraintType.VERIFICATION,
            expression = "isReference",
            description = "A ReferenceUsage is always referential."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "namingFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "namingFeature",
            body = MetaOperation.ocl("""
                if owningType <> null and owningType.oclIsKindOf(TransitionUsage) and
                    owningType.oclAsType(TransitionUsage).inputParameter(2) = self then
                    owningType.oclAsType(TransitionUsage).triggerPayloadParameter()
                else self.oclAsType(Usage).namingFeature()
                endif
            """.trimIndent()),
            description = "If this ReferenceUsage is the payload parameter of a TransitionUsage, then its namingFeature is the payloadParameter of the triggerAction of that TransitionUsage (if any)."
        )
    ),
    description = "A ReferenceUsage is a Usage that specifies a non-compositional reference to something."
)
