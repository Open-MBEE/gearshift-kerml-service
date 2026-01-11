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
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass
import MetaProperty

/**
 * KerML Type metaclass.
 * Specializes: Namespace
 * A namespace that can be specialized.
 */
fun createTypeMetaClass() = MetaClass(
    name = "Type",
    isAbstract = false,
    superclasses = listOf("Namespace"),
    attributes = listOf(
        MetaProperty(
            name = "isAbstract",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this type is abstract"
        ),
        MetaProperty(
            name = "isSufficient",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this type is sufficient"
        )
    ),
    description = "A namespace that can be specialized"
)
