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

import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML Definition metaclass.
 * Specializes: Classifier
 * A classifier that defines something.
 */
fun createDefinitionMetaClass() = MetaClass(
    name = "Definition",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = listOf(
        MetaProperty(
            name = "isVariation",
            type = "Boolean",
            description = "Whether this is a variation definition"
        )
    ),
    description = "A classifier that defines something"
)
