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

/**
 * SysML LoopActionUsage metaclass.
 * Specializes: ActionUsage
 * A LoopActionUsage is an ActionUsage that specifies that its bodyAction should be performed repeatedly. Its
 * subclasses WhileLoopActionUsage and ForLoopActionUsage provide different ways to determine how many
 * times the bodyAction should be performed.
 */
fun createLoopActionUsageMetaClass() = MetaClass(
    name = "LoopActionUsage",
    isAbstract = true,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveLoopActionUsageBodyAction",
            type = ConstraintType.DERIVATION,
            expression = """
                let parameter : Feature = inputParameter(2) in
                if parameter <> null and parameter.oclIsKindOf(ActionUsage) then
                    parameter.oclAsType(ActionUsage)
                else
                    null
                endif
            """.trimIndent(),
            description = "The bodyAction of a LoopActionUsage is its second input parameter, which must be an ActionUsage."
        )
    ),
    description = "A LoopActionUsage is an ActionUsage that specifies that its bodyAction should be performed repeatedly."
)
