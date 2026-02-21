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

/**
 * SysML ConjugatedPortDefinition metaclass.
 * Specializes: PortDefinition
 * A ConjugatedPortDefinition is a PortDefinition that is a conjugation of its original
 * PortDefinition. That is, a ConjugatedPortDefinition inherits all the features of the original
 * PortDefinition, but input flows of the original become outputs and output flows become inputs.
 * Every PortDefinition (that is not itself a ConjugatedPortDefinition) has exactly one
 * corresponding ConjugatedPortDefinition, whose effective name is the name of the original
 * PortDefinition, with the character ~ prepended.
 */
fun createConjugatedPortDefinitionMetaClass() = MetaClass(
    name = "ConjugatedPortDefinition",
    isAbstract = false,
    superclasses = listOf("PortDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateConjugatedPortDefinitionConjugatedPortDefinitionIsEmpty",
            type = ConstraintType.VERIFICATION,
            expression = "conjugatedPortDefinition = null",
            description = "A ConjugatedPortDefinition must not itself have a conjugatedPortDefinition."
        ),
        MetaConstraint(
            name = "validateConjugatedPortDefinitionOriginalPortDefinition",
            type = ConstraintType.VERIFICATION,
            expression = "ownedPortConjugator.originalPortDefinition = originalPortDefinition",
            description = "The originalPortDefinition of the ownedPortConjugator of a ConjugatedPortDefinition must be the originalPortDefinition of the ConjugatedPortDefinition."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "effectiveName",
            body = MetaOperation.ocl("""
                let originalName : String = originalPortDefinition.name in
                if originalName = null then null
                else '~' + originalName
                endif
            """.trimIndent()),
            description = "If the name of the originalPortDefinition is non-empty, then return that with the character ~ prepended."
        )
    ),
    description = "A ConjugatedPortDefinition is a PortDefinition that is a conjugation of its original PortDefinition."
)
